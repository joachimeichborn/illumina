package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;

public class UnknownDevice extends AbstractDevice {
	private static final String TAG = UnknownDevice.class.getName();

	public UnknownDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.UNKNOWN, aJsonDevice);
	}

	public UnknownDevice(final Parcel aParcel) {
		super(aParcel);
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);
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

		final UnknownDevice otherDevice = (UnknownDevice) aOther;

		return identicalBase(otherDevice);
	}

	public static final Creator<UnknownDevice> CREATOR = new Creator<UnknownDevice>() {
		@Override
		public UnknownDevice createFromParcel(final Parcel aParcel) {
			return new UnknownDevice(aParcel);
		}

		@Override
		public UnknownDevice[] newArray(final int aSize) {
			return new UnknownDevice[aSize];
		}
	};
}