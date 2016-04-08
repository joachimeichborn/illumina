package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;

public class LabelDevice extends AbstractDevice {
	private static final String JSON_LABEL_KEY = "label";
	private static final String JSON_COLOR_KEY = "color";

	private String label;
	private String color;

	public LabelDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.LABEL, aJsonDevice);
	}

	public static final Creator<LabelDevice> CREATOR = new Creator<LabelDevice>() {
		@Override
		public LabelDevice createFromParcel(final Parcel aParcel) {
			return new LabelDevice(aParcel);
		}

		@Override
		public LabelDevice[] newArray(int size) {
			return new LabelDevice[size];
		}
	};

	public LabelDevice(final Parcel aParcel) {
		super(aParcel);

		label = aParcel.readString();
		color = aParcel.readString();
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);
		aParcel.writeString(label);
		aParcel.writeString(color);
	}

	@Override
	public void update(final JSONObject aJsonValues) {
		super.update(aJsonValues);

		label = aJsonValues.optString(JSON_LABEL_KEY, "");
		color = aJsonValues.optString(JSON_COLOR_KEY, "black");
	}

	public String getLabel() {
		return label;
	}

	public String getColor() {
		return color;
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

		final LabelDevice otherDevice = (LabelDevice) aOther;

		if (!TextUtils.equals(label, otherDevice.getLabel())) {
			return false;
		}

		if (!TextUtils.equals(color, otherDevice.getColor())) {
			return false;
		}

		return identicalBase(otherDevice);
	}
}
