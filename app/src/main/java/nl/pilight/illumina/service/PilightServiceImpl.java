/*
 * illumina, a pilight remote
 *
 * Copyright (c) 2014 Peter Heisig <http://google.com/+PeterHeisig>
 *                    CurlyMo <http://www.pilight.org>
 *
 * illumina is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * illumina is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with illumina. If not, see <http://www.gnu.org/licenses/>.
 */

package nl.pilight.illumina.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.pilight.Illumina;
import nl.pilight.illumina.Logger;
import nl.pilight.illumina.communication.StreamingSocket;
import nl.pilight.illumina.communication.StreamingSocketImpl;
import nl.pilight.illumina.pilight.Configuration;
import nl.pilight.illumina.pilight.DeviceUpdateHandler;
import nl.pilight.illumina.pilight.Group;
import nl.pilight.illumina.pilight.devices.AbstractDevice;
import nl.pilight.illumina.pilight.devices.Device;

public class PilightServiceImpl extends Service implements PilightService, DeviceUpdateHandler {

	private static final String TAG = PilightServiceImpl.class.getName();

	private Configuration mConfiguration;

	private boolean mCurrentlyTriesReconnecting;

	private enum PilightState {
		Connected,
		Connecting,
		Disconnected,
		Disconnecting,
		HandshakePending,
		ConfigRequested,
		ValuesRequested,
		Error
	}

	private PilightState mState = PilightState.Disconnected;

	private final Handler mPilightHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			final Bundle data = msg.getData();

			switch (msg.what) {
				case StreamingSocket.MSG_CONNECTED:
					onSocketConnected();
					break;


				case StreamingSocket.MSG_DISCONNECTED:
					if (mState != PilightState.Disconnected) {
						onSocketDisconnected();
					}
					break;

				case StreamingSocket.MSG_ERROR:
					if (mState == PilightState.Connecting && !mCurrentlyTriesReconnecting) {
						onSocketConnectionFailed();
					} else {
						onSocketError();
					}
					break;

				case StreamingSocket.MSG_MESSAGE_RECEIVED:
					assert data != null;
					onSocketMessage(data.getString(StreamingSocket.EXTRA_MESSAGE));
					break;

				default:
					Logger.warn(TAG, "Unhandled message from socket");
					break;
			}
		}
	};

	private final StreamingSocket mPilight = new StreamingSocketImpl(mPilightHandler);

	private void sendSocketMessage(JSONObject json) {
		final String jsonString = json.toString();

		Logger.info(TAG, "Sending " + jsonString);
		mPilight.send(jsonString);
	}

	@Override
	public void onDeviceUpdated(Device device) {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(Extra.DEVICE, device);
		sendBroadcast(News.DEVICE_CHANGE, bundle);
	}

	private void onSocketConnectionFailed() {
		Logger.warn(TAG, "Pilight connection failed");
		sendBroadcast(News.ERROR, Error.CONNECTION_FAILED);
		mState = PilightState.Disconnected;
	}

	private void onSocketDisconnected() {
		Logger.info(TAG, "Pilight disconnected");
		sendBroadcast(News.DISCONNECTED);
		mState = PilightState.Disconnected;
	}

	private void onSocketError() {
		Logger.info(TAG, "Pilight socket error");
		if (!mCurrentlyTriesReconnecting && mState != PilightState.Disconnected) {
			mCurrentlyTriesReconnecting = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Logger.info(TAG, "Reconnecting");
					connect();
				}
			}, 100);

		} else if (mState != PilightState.Disconnected) {
			sendBroadcast(News.ERROR, Error.REMOTE_CLOSED);
		}

		mState = PilightState.Disconnected;
	}

	private void onSocketConnected() {
		Logger.info(TAG, "Pilight connected, handshake initiated");

		final JSONObject json = new JSONObject();
		final JSONObject joptions = new JSONObject();

		try {
			json.put("action", "identify");
			json.put("media", "mobile");
			joptions.put("config", 1);
			json.put("options", joptions);
		} catch (JSONException exception) {
			Logger.error(TAG, "Error creating handshake message", exception);
		}

		sendSocketMessage(json);

		mState = PilightState.HandshakePending;
	}

	private void onSocketMessage(String message) {
		JSONObject json = new JSONObject();
		JSONObject request = null;

//        if (TextUtils.isEmpty(message)) {
//            log.info("received message is empty");
//
//        } else {
//            try {
//                json = new JSONObject(message);
//            } catch (JSONException exception) {
//                log.info("decoding json failed with: " + exception.getMessage());
//                sendBroadcast(News.ERROR, Error.UNKNOWN);
//                mState = PilightState.Disconnected;
//            }
//        }

		switch (mState) {

			case ConfigRequested:
				try {
					json = new JSONObject(message);
					if (!json.isNull("message") && json.getString("message").equals(new String("config"))) {
						try {
							if (!json.isNull("config")) {
								JSONObject jconfig = json.getJSONObject("config");
								if (!jconfig.isNull("gui")) {
									mConfiguration = new Configuration(this, jconfig.getJSONObject("gui"));
									try {
										request = new JSONObject();
										request.put("action", "request values");
										sendSocketMessage(request);
										mState = PilightState.ValuesRequested;
									} catch (JSONException exception) {
										Logger.error(TAG, "Error creating values request message", exception);
									}

									return;

								}
							}
						} catch (JSONException exception) {
							Logger.info(TAG, "Error reading config " + exception.getMessage());
						}
					}
				} catch (JSONException exception) {
				}
				break;

			case ValuesRequested:
				try {
					json = new JSONObject(message);
					if (!json.isNull("message") && json.getString("message").equals(new String("values"))) {
						try {
							if (!json.isNull("values")) {
								mPilight.startHeartBeat();

								JSONArray jvalues = json.getJSONArray("values");
								for (int i = 0; i < jvalues.length(); i++) {
									mConfiguration.updateDevices(jvalues.getJSONObject(i));
								}

								if (!mCurrentlyTriesReconnecting) {
									sendBroadcast(News.CONNECTED);
								}

								mCurrentlyTriesReconnecting = false;
								mState = PilightState.Connected;
							}
						} catch (JSONException exception) {
							Logger.info(TAG, "Error reading values " + exception.getMessage());
						}
					}
				} catch (JSONException exception) {
				}
				break;

			case HandshakePending:
				if (message.equals(new String("{\"status\":\"success\"}"))) {
					request = new JSONObject();

					try {
						request.put("action", "request config");
					} catch (JSONException exception) {
						Logger.error(TAG, "Error creating config request message", exception);
					}

					sendSocketMessage(request);
					mState = PilightState.ConfigRequested;

					return;
				}
				break;

			case Connected:
				try {
					json = new JSONObject(message);
					onPilightMessage(json);
				} catch (JSONException exception) {
					Logger.error(TAG, "Error creating values update message", exception);
				}
				break;

			case Connecting:
				Logger.warn(TAG, "Impossible state 'Connecting'");
				break;

			case Disconnected:
				Logger.warn(TAG, "Impossible state 'Disconnected'");
				break;

			case Disconnecting:
				Logger.warn(TAG, "Impossible state 'Disconnecting'");
				break;

			default:
				// nothing
				break;
		}
	}

	private void onPilightMessage(JSONObject json) {
		Logger.info(TAG, "Pilight message received: " + json.toString());

		if (json.isNull("origin")) {
			Logger.warn(TAG, "Has no origin, ignored");
		} else if (!TextUtils.equals(json.optString("origin"), "update")) {
			Logger.warn(TAG, "Wrong origin, ignored");
		} else {
			mConfiguration.updateDevices(json);
		}
	}

	public boolean isConnected() {
		final boolean isEndpointUnchanged =
				getPortFromPreferences() == mPilight.getPort()
						&& TextUtils.equals(getHostFromPreferences(), mPilight.getHost());

		return isEndpointUnchanged && mPilight.isConnected();
	}

	public void connect() {
		Logger.info(TAG, "Connect request");

		mPilight.connect(getHostFromPreferences(), getPortFromPreferences());
		mState = PilightState.Connecting;
	}

	public void disconnect() {
		Logger.info(TAG, "Disconnect request");

		if (mState == PilightState.Disconnected) {
			Logger.info(TAG, "Ignored, already disconnected");
			return;
		}

		mState = PilightState.Disconnecting;
		mPilight.disconnect();
	}

	private String getHostFromPreferences() {
		assert getApplication() != null;
		return ((Illumina) getApplication())
				.getSharedPreferences()
				.getString(Illumina.PREF_HOST, "");
	}

	private int getPortFromPreferences() {
		assert getApplication() != null;
		return ((Illumina) getApplication())
				.getSharedPreferences()
				.getInt(Illumina.PREF_PORT, 0);
	}

	public void sendDeviceChange(final Device aDevice, final AbstractDevice.Property aChangedProperty) {
		try {
			final JSONObject json = new JSONObject();

			final JSONObject code = aDevice.getJsonCode(aChangedProperty);
			json.put("action", "control");
			json.put("code", code);
			code.put("device", aDevice.getId());

			sendSocketMessage(json);

		} catch (JSONException exception) {
			Logger.error(TAG, "Sending change failed with " + exception.getMessage());
		}
	}

	// ------------------------------------------------------------------------
	//
	//      Lifecycle
	//
	// ------------------------------------------------------------------------

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return Service.START_STICKY;
	}

	// ------------------------------------------------------------------------
	//
	//      Binding
	//
	// ------------------------------------------------------------------------

	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private final Messenger mMessenger = new Messenger(new IncomingHandler());

	/**
	 * Keeps track of all current registered clients.
	 */
	private final ArrayList<Messenger> mClients = new ArrayList<>();

	/**
	 * Handler of incoming messages from clients.
	 */
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(final Message msg) {
			final Bundle data = msg.getData();

			if (data != null) {
				data.setClassLoader(Group.class.getClassLoader());
			}

			switch (msg.what) {
				case Request.REGISTER:
					mClients.add(msg.replyTo);
					break;

				case Request.STATE:
					sendState(msg.replyTo);
					break;

				case Request.UNREGISTER:
					if (mClients.contains(msg.replyTo)) { // FIXME dirty hack! (see #36)
						mClients.remove(msg.replyTo);
					}
					break;

				case Request.PILIGHT_CONNECT:
					if (!isConnected()) {
						mCurrentlyTriesReconnecting = false;
						connect();
					} else {
						sendBroadcast(News.CONNECTED);
					}
					break;

				case Request.PILIGHT_DISCONNECT:
					disconnect();
					break;

				case Request.GROUP_LIST:
					if (!mClients.contains(msg.replyTo)) { // FIXME dirty hack! (see #36)
						mClients.add(msg.replyTo);
					}

					sendLocationList(mClients.get(mClients.indexOf(msg.replyTo)));
					break;

				case Request.LOCATION:
					if (!mClients.contains(msg.replyTo)) { // FIXME dirty hack! (see #36)
						mClients.add(msg.replyTo);
					}

					assert data != null;
					sendLocation(data.getString(Extra.LOCATION_ID),
							mClients.get(mClients.indexOf(msg.replyTo)));
					break;

				case Request.DEVICE_CHANGE:
					assert data != null;
					final int propertyOrdinal = data.getInt(Extra.CHANGED_PROPERTY);
					final AbstractDevice.Property property = Device.Property.values[propertyOrdinal];
					sendDeviceChange((Device) data.getParcelable(Extra.DEVICE),
							property);
					break;

				default:
					super.handleMessage(msg);
			}
		}
	}

	private void sendState(Messenger receiver) {
		try {
			if (isConnected()) {
				receiver.send(Message.obtain(null, News.CONNECTED));
			} else {
				receiver.send(Message.obtain(null, News.DISCONNECTED));
			}
		} catch (RemoteException exception) {
			Logger.error(TAG, "Sending disconnected state failed", exception);
		}
	}

	private void sendLocation(String locationId, Messenger receiver) {
		final Message message = Message.obtain(null, News.LOCATION);
		final Bundle data = new Bundle();

		data.putParcelable(Extra.LOCATION, mConfiguration.getGroup(locationId));

		assert message != null;
		message.setData(data);

		try {
			receiver.send(message);
		} catch (RemoteException exception) {
			Logger.error(TAG, "Sending location failed", exception);
		}
	}

	private void sendLocationList(Messenger receiver) {
		// fix due to issue #34
		if (mConfiguration == null) {
			sendBroadcast(News.ERROR, Error.HANDSHAKE_FAILED);
			mState = PilightState.Disconnected;
			return;
		}

		final Message message = Message.obtain(null, News.LOCATION_LIST);
		final Bundle data = new Bundle();

		data.putParcelableArrayList(Extra.LOCATION_LIST,
				new ArrayList<>(mConfiguration.getGroups()));

		assert message != null;
		message.setData(data);

		try {
			receiver.send(message);
		} catch (RemoteException exception) {
			Logger.error(TAG, "Sending location list failed", exception);
		}
	}

	private void sendBroadcast(final int what) {
		sendBroadcast(what, null, 0);
	}

	private void sendBroadcast(final int what, int arg1) {
		sendBroadcast(what, null, arg1);
	}

	private void sendBroadcast(final int what, Bundle data) {
		sendBroadcast(what, data, 0);
	}

	private void sendBroadcast(final int what, Bundle data, int arg1) {
		final ArrayList<Messenger> deadClients = new ArrayList<>();

		for (Messenger client : mClients) {
			final Message message = Message.obtain(null, what, arg1, 0);

			if (data != null && message != null) {
				message.setData(data);
			}

			try {
				client.send(message);
			} catch (RemoteException e) {
				// The client is dead.  Remove it from the list;
				// we are going through the list from back to front
				// so this is safe to do inside the loop.
				deadClients.add(client);
			}
		}

		mClients.removeAll(deadClients);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

}
