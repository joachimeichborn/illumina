package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;
import nl.pilight.illumina.pilight.devices.states.SwitchState;

public class DimmerDevice extends AbstractDevice implements ToggleableDevice {
	private static final String TAG = DimmerDevice.class.getName();

	private static final String JSON_DIMLEVEL_KEY = "dimlevel";
	private static final String JSON_STATE_KEY = "state";
	public static final String JSON_MIN_DIMLEVEL_KEY = "dimlevel-minimum";
	public static final String JSON_MAX_DIMLEVEL_KEY = "dimlevel-maximum";

	private int dimLevel;

	private SwitchState currentState = SwitchState.UNKNOWN;
	private SwitchState requestedState = SwitchState.UNKNOWN;
	private final int minDimLevel;
	private final int maxDimLevel;

	public DimmerDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.DIMMER, aJsonDevice);

		minDimLevel = aJsonDevice.optInt(JSON_MIN_DIMLEVEL_KEY, 0);
		maxDimLevel = aJsonDevice.optInt(JSON_MAX_DIMLEVEL_KEY, 15);
	}

	public static final Creator<DimmerDevice> CREATOR = new Creator<DimmerDevice>() {
		@Override
		public DimmerDevice createFromParcel(final Parcel aParcel) {
			return new DimmerDevice(aParcel);
		}

		@Override
		public DimmerDevice[] newArray(final int aSize) {
			return new DimmerDevice[aSize];
		}
	};

	public DimmerDevice(final Parcel aParcel) {
		super(aParcel);
		currentState = SwitchState.getByJsonValue(aParcel.readString());
		requestedState = SwitchState.getByJsonValue(aParcel.readString());
		dimLevel = aParcel.readInt();
		minDimLevel = aParcel.readInt();
		maxDimLevel = aParcel.readInt();
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);
		aParcel.writeString(currentState.getJsonValue());
		aParcel.writeString(requestedState.getJsonValue());
		aParcel.writeInt(dimLevel);
		aParcel.writeInt(minDimLevel);
		aParcel.writeInt(maxDimLevel);
	}

	@Override
	public void update(final JSONObject aJsonValues) {
		super.update(aJsonValues);

		currentState = SwitchState.getByJsonValue(aJsonValues.optString(JSON_STATE_KEY, ""));
		dimLevel = aJsonValues.optInt(JSON_DIMLEVEL_KEY, dimLevel);
	}

	public int getDimLevel() {
		return dimLevel;
	}

	public int getMinDimLevel() {
		return minDimLevel;
	}

	public int getMaxDimLevel() {
		return maxDimLevel;
	}


	public void setDimLevel(final int aDimLevel) {
		dimLevel = aDimLevel;
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

		final DimmerDevice otherDevice = (DimmerDevice) aOther;

		if (currentState != otherDevice.currentState) {
			return false;
		}

		if (requestedState != otherDevice.requestedState) {
			return false;
		}

		return identicalBase(otherDevice);
	}

	public JSONObject getJsonCode(final Property aChangedProperty) throws JSONException {
		final JSONObject code = super.getJsonCode(aChangedProperty);

		if (aChangedProperty == Property.DIMLEVEL) {
			final JSONObject values;
			if (!code.has(JSON_VALUES_KEY)) {
				values = new JSONObject();
				code.put(JSON_VALUES_KEY, values);
			} else {
				values = code.getJSONObject(JSON_VALUES_KEY);
			}

			values.put(JSON_DIMLEVEL_KEY, getDimLevel());
		} else if (aChangedProperty == Property.UPDATE) {
			code.put(JSON_STATE_KEY, requestedState.getJsonValue());
		} else if (aChangedProperty == Property.VALUE) {
			code.put(JSON_STATE_KEY, requestedState.getJsonValue());
		}

		return code;
	}

	@Override
	public void toggle() {
		if (currentState == SwitchState.ON) {
			requestedState = SwitchState.OFF;
		} else {
			requestedState = SwitchState.ON;
		}
	}
}
