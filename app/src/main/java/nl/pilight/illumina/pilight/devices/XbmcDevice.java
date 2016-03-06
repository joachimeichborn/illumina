package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;

public class XbmcDevice extends AbstractDevice {
	public static final String JSON_ACTION_KEY = "action";
	public static final String JSON_MEDIA_KEY = "media";
	public static final String JSON_SHOW_MEDIA_KEY = "show-media";
	public static final String JSON_SHOW_ACTION_KEY = "show-action";
	private final boolean showMedia;
	private final boolean showAction;

	public enum MediaType {
		MOVIE("movie"), EPISODE("episode"), SONG("song"), UNKNOWN("unknown");

		final private String jsonValue;

		MediaType(final String aJsonValue) {
			jsonValue = aJsonValue;
		}

		public String getJsonValue() {
			return jsonValue;
		}

		public static MediaType getByJsonValue(final String aJsonValue) {
			for (final MediaType type : MediaType.values()) {
				if (type.getJsonValue().equals(aJsonValue)) {
					return type;
				}
			}

			return UNKNOWN;
		}
	}

	public enum Action {
		HOME("home"), SHUTDOWN("shutdown"), PLAY("play"), PAUSE("pause"), STOP("stop"), ACTIVE("active"), INACTIVE("inactive"), UNKNOWN("unknown");

		final private String jsonValue;

		Action(final String aJsonValue) {
			jsonValue = aJsonValue;
		}

		public String getJsonValue() {
			return jsonValue;
		}

		public static Action getByJsonValue(final String aJsonValue) {
			for (final Action action : Action.values()) {
				if (action.getJsonValue().equals(aJsonValue)) {
					return action;
				}
			}

			return UNKNOWN;
		}
	}

	private MediaType media = MediaType.UNKNOWN;
	private Action action = Action.UNKNOWN;

	public XbmcDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.XBMC, aJsonDevice);

		showMedia = aJsonDevice.optInt(JSON_SHOW_MEDIA_KEY, 0) == 1;
		showAction = aJsonDevice.optInt(JSON_SHOW_ACTION_KEY, 0) == 1;
	}

	public static final Creator<XbmcDevice> CREATOR = new Creator<XbmcDevice>() {

		@Override
		public XbmcDevice createFromParcel(final Parcel aParcel) {
			return new XbmcDevice(aParcel);
		}

		@Override
		public XbmcDevice[] newArray(int size) {
			return new XbmcDevice[size];
		}

	};

	public XbmcDevice(final Parcel aParcel) {
		super(aParcel);

		media = MediaType.getByJsonValue(aParcel.readString());
		action = Action.getByJsonValue(aParcel.readString());
		showMedia = Boolean.parseBoolean(aParcel.readString());
		showAction = Boolean.parseBoolean(aParcel.readString());
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);

		aParcel.writeString(media.getJsonValue());
		aParcel.writeString(action.getJsonValue());
		aParcel.writeString(Boolean.toString(showMedia));
		aParcel.writeString(Boolean.toString(showAction));
	}

	@Override
	public void update(final JSONObject aJsonValues) {
		super.update(aJsonValues);
		media = MediaType.getByJsonValue(aJsonValues.optString(JSON_MEDIA_KEY, ""));
		action = Action.getByJsonValue(aJsonValues.optString(JSON_ACTION_KEY, ""));
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

		final XbmcDevice otherDevice = (XbmcDevice) aOther;

		if (media != otherDevice.media) {
			return false;
		}

		if (action != otherDevice.action) {
			return false;
		}

		return identicalBase(otherDevice);
	}

	public boolean isShowMedia() {
		return showMedia;
	}

	public MediaType getMedia() {
		return media;
	}

	public boolean isShowAction() {
		return showAction;
	}

	public Action getAction() {
		return action;
	}
}
