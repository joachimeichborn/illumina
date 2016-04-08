package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;
import nl.pilight.illumina.pilight.devices.states.SwitchState;

public class SwitchDevice extends AbstractDevice implements ToggleableDevice {
	private static final String TAG = SwitchDevice.class.getName();
	private static final String JSON_STATE_KEY = "state";

	private SwitchState currentState = SwitchState.UNKNOWN;
	private SwitchState requestedState = SwitchState.UNKNOWN;

	public SwitchDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.SWITCH, aJsonDevice);
	}

	public static final Creator<SwitchDevice> CREATOR = new Creator<SwitchDevice>() {

		@Override
		public SwitchDevice createFromParcel(final Parcel aParcel) {
			return new SwitchDevice(aParcel);
		}

		@Override
		public SwitchDevice[] newArray(int size) {
			return new SwitchDevice[size];
		}

	};

	public SwitchDevice(final Parcel aParcel) {
		super(aParcel);
		currentState = SwitchState.getByJsonValue(aParcel.readString());
		requestedState = SwitchState.getByJsonValue(aParcel.readString());
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
		currentState = SwitchState.getByJsonValue(aJsonValues.optString(JSON_STATE_KEY, ""));
	}

	public boolean isOn() {
		return currentState == SwitchState.ON;
	}

	@Override
	public boolean identical(final Object aOther) {
		if (this == aOther) {
			return true;
		}

		if (null == aOther) {
			return false;
		}

		if (getClass() != aOther.getClass())
			return false;

		final SwitchDevice otherDevice = (SwitchDevice) aOther;

		if (currentState != otherDevice.currentState) {
			return false;
		}

		if (requestedState != otherDevice.requestedState) {
			return false;
		}

		return identicalBase(otherDevice);
	}

	@Override
	public void toggle() {
		if (currentState == SwitchState.ON) {
			requestedState = SwitchState.OFF;
		} else {
			requestedState = SwitchState.ON;
		}
	}


	public JSONObject getJsonCode(final Property aChangedProperty) throws JSONException {
		final JSONObject code = super.getJsonCode(aChangedProperty);

		if (aChangedProperty == Property.VALUE) {
			code.put(JSON_STATE_KEY, requestedState.getJsonValue());
		}

		return code;
	}
}
