package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;
import nl.pilight.illumina.pilight.devices.states.ScreenState;

public class ScreenDevice extends AbstractDevice implements ToggleableDevice {
	private static final String JSON_STATE_KEY = "state";

	private ScreenState currentState = ScreenState.UNKNOWN;
	private ScreenState requestedState = ScreenState.UNKNOWN;

	public ScreenDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.SCREEN, aJsonDevice);
	}

	public static final Creator<ScreenDevice> CREATOR = new Creator<ScreenDevice>() {
		@Override
		public ScreenDevice createFromParcel(final Parcel aParcel) {
			return new ScreenDevice(aParcel);
		}

		@Override
		public ScreenDevice[] newArray(int size) {
			return new ScreenDevice[size];
		}
	};

	public ScreenDevice(final Parcel aParcel) {
		super(aParcel);
		currentState = ScreenState.getByJsonValue(aParcel.readString());
		requestedState = ScreenState.getByJsonValue(aParcel.readString());
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);
		aParcel.writeString(currentState.getJsonValue());
		aParcel.writeString(requestedState.getJsonValue());
	}

	@Override
	public void update(JSONObject aJsonValues) {
		super.update(aJsonValues);
		currentState = ScreenState.getByJsonValue(aJsonValues.optString(JSON_STATE_KEY, ""));
	}


	public void requestState(final ScreenState aState) {
		requestedState = aState;
	}

	public boolean isUp() {
		return currentState == ScreenState.UP;
	}

	@Override
	public void toggle() {
		if (currentState == ScreenState.UP) {
			requestedState = ScreenState.DOWN;
		} else {
			requestedState = ScreenState.UP;
		}
	}

	public JSONObject getJsonCode(final Property aChangedProperty) throws JSONException {
		final JSONObject code = super.getJsonCode(aChangedProperty);

		if (aChangedProperty == Property.UPDATE) {
			code.put(JSON_STATE_KEY, requestedState);
		}

		return code;
	}

	@Override
	public boolean identical(Object aOther) {
		if (this == aOther) {
			return true;
		}

		if (null == aOther) {
			return false;
		}

		if (getClass() != aOther.getClass())
			return false;

		final ScreenDevice otherDevice = (ScreenDevice) aOther;

		if (currentState != otherDevice.currentState) {
			return false;
		}

		if (requestedState != otherDevice.requestedState) {
			return false;
		}

		return identicalBase(otherDevice);
	}
}
