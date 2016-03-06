package nl.pilight.illumina.pilight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nl.pilight.illumina.Logger;
import nl.pilight.illumina.pilight.devices.AbstractDevice;
import nl.pilight.illumina.pilight.devices.Device;

public class Configuration {
	private static final String TAG = Configuration.class.getName();

	private static final String JSON_GROUP = "group";
	private static final String JSON_ORDER = "order";
	private static final String JSON_DEVICES = "devices";
	private static final String JSON_VALUES = "values";
	public static final String JSON_MEDIA_KEY = "media";

	private final DeviceUpdateHandler deviceUpdateHandler;
	private final Map<String, Group> groups = new TreeMap<>();

	public Configuration(final DeviceUpdateHandler aDeviceUpdateHandler, final JSONObject aDevicesJson) throws JSONException {
		deviceUpdateHandler = aDeviceUpdateHandler;
		initialize(aDevicesJson);
	}

	private void initialize(final JSONObject aDevicesJson) throws JSONException {
		Logger.info(TAG, "Initializing configuration " + aDevicesJson);
		final Iterator<String> devicesJsonIterator = aDevicesJson.keys();

		while (devicesJsonIterator.hasNext()) {
			final String deviceId = devicesJsonIterator.next();
			final JSONObject deviceJson = aDevicesJson.getJSONObject(deviceId);
			if (isMobileSupported(deviceJson)) {
				//We only use the group that is mentioned first and ignore others if present
				final String currentGroup = deviceJson.getJSONArray(JSON_GROUP).get(0).toString();

				if (!groups.containsKey(currentGroup)) {
					groups.put(currentGroup, new Group(currentGroup));
				}

				final Group group = groups.get(currentGroup);
				final Device device = AbstractDevice.DeviceBuilder.build(deviceId, currentGroup, deviceJson);
				group.addDevice(device);
			}
		}
	}

	private boolean isMobileSupported(final JSONObject aDeviceJson) throws JSONException {
		if (!aDeviceJson.has(JSON_MEDIA_KEY)) {
			return true;
		}
		final JSONArray mediaArray = aDeviceJson.getJSONArray(JSON_MEDIA_KEY);
		final int length = mediaArray.length();

		for (int i = 0; i < length; i++) {
			final String media = mediaArray.getString(i);
			if ("mobile".equals(media) || "all".equals(media)) {
				return true;
			}
		}

		return false;
	}

	public List<Group> getGroups() {
		return new ArrayList<>(groups.values());
	}

	public Group getGroup(final String aGroupId) {
		return groups.get(aGroupId);
	}

	public void updateDevices(final JSONObject aJson) {
		Logger.info(TAG, "Update devices with " + aJson.toString());
		if (aJson.has(JSON_DEVICES)) {
			final JSONArray jsonDevices = aJson.optJSONArray(JSON_DEVICES);
			final JSONObject jsonValues = aJson.optJSONObject(JSON_VALUES);

			final int deviceCount = jsonDevices.length();
			for (int i = 0; i < deviceCount; i++) {
				try {
					final String deviceId = jsonDevices.getString(i);
					for (final Group group : groups.values()) {
						final Device device = group.getDevice(deviceId);
						if (device != null) {
							device.update(jsonValues);
							deviceUpdateHandler.onDeviceUpdated(device);
							break;
						}
					}
				} catch (JSONException exception) {
					Logger.warn(TAG, "Updating device failed", exception);
				}
			}
		}
	}
}