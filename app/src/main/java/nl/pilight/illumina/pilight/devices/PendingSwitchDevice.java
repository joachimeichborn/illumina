package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;
import nl.pilight.illumina.pilight.devices.states.PendingSwitchState;

public class PendingSwitchDevice extends AbstractDevice {
	private static final String TAG = PendingSwitchDevice.class.getName();
	private static final String JSON_STATE_KEY = "state";

	private PendingSwitchState state = PendingSwitchState.UNKNOWN;

	public PendingSwitchDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.PENDING_SW, aJsonDevice);
	}

	public static final Creator<PendingSwitchDevice> CREATOR = new Creator<PendingSwitchDevice>() {
		@Override
		public PendingSwitchDevice createFromParcel(final Parcel aParcel) {
			return new PendingSwitchDevice(aParcel);
		}

		@Override
		public PendingSwitchDevice[] newArray(int size) {
			return new PendingSwitchDevice[size];
		}
	};

	public PendingSwitchDevice(final Parcel aParcel) {
		super(aParcel);

		state = PendingSwitchState.getByJsonValue(aParcel.readString());
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);

		aParcel.writeString(state.getJsonValue());
	}

	public boolean isOn() {
		return state == PendingSwitchState.RUNNING;
	}

	public boolean isPending() {
		return state == PendingSwitchState.PENDING;
	}

	public boolean isOff() {
		return state == PendingSwitchState.STOPPED;
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

		final PendingSwitchDevice otherDevice = (PendingSwitchDevice) aOther;

		if (state != otherDevice.state) {
			return false;
		}


		return identicalBase(otherDevice);
	}

	@Override
	public void update(final JSONObject aJsonValues) {
		super.update(aJsonValues);

		state = PendingSwitchState.getByJsonValue(aJsonValues.optString(JSON_STATE_KEY, ""));
	}
}
