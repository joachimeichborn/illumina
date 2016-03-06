package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;
import nl.pilight.illumina.pilight.devices.states.ContactState;

public class ContactDevice extends AbstractDevice {
	private static final String JSON_STATE_KEY = "state";

	private ContactState state = ContactState.UNKNOWN;

	public ContactDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.CONTACT, aJsonDevice);
	}

	public static final Creator<ContactDevice> CREATOR = new Creator<ContactDevice>() {
		@Override
		public ContactDevice createFromParcel(final Parcel aParcel) {
			return new ContactDevice(aParcel);
		}

		@Override
		public ContactDevice[] newArray(final int aSize) {
			return new ContactDevice[aSize];
		}
	};

	public ContactDevice(final Parcel aParcel) {
		super(aParcel);
		state = ContactState.getByJsonValue(aParcel.readString());
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);
		aParcel.writeString(state.getJsonValue());
	}

	@Override
	public void update(final JSONObject aJsonValues) {
		super.update(aJsonValues);

		state = ContactState.getByJsonValue(aJsonValues.optString(JSON_STATE_KEY, ""));
	}

	public boolean isClosed() {
		return state == ContactState.CLOSED;
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

		final ContactDevice otherDevice = (ContactDevice) aOther;

		if (state != otherDevice.state) {
			return false;
		}

		return identicalBase(otherDevice);
	}
}
