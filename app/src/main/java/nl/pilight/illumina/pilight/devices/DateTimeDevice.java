package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import nl.pilight.illumina.pilight.DeviceType;

public class DateTimeDevice extends AbstractDevice {
	private static final String JSON_DATETIME_FORMAT_KEY = "format";
	private static final String JSON_YEAR_KEY = "year";
	private static final String JSON_MONTH_KEY = "month";
	private static final String JSON_DAY_KEY = "day";
	private static final String JSON_HOUR_KEY = "hour";
	private static final String JSON_MINUTE_KEY = "minute";
	private static final String JSON_SECOND_KEY = "second";

	private final SimpleDateFormat dateTimeFormat;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;

	public DateTimeDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.DATE_TIME, aJsonDevice);

		final String originalFormat = aJsonDevice.optString(JSON_DATETIME_FORMAT_KEY, "yyyy-MM-DD HH:mm:ss");
		dateTimeFormat = new SimpleDateFormat(convertDateTimeFormat(originalFormat));
	}

	/**
	 * Convert the date time pattern used by pilight to a pattern that is as similar as possible and can be understood as SimpleDateFormat
	 * @param aOriginalFormat the date time format pattern as used by pilight
	 * @return the corresponding pattern that is understood as a SimpleDateFormat
	 */
	static String convertDateTimeFormat(final String aOriginalFormat) {
		String format = aOriginalFormat.replace('d', 'c');
		format = format.replaceAll("(^|[^D])D{1,2}($|[^D])", "$1dd$2");
		format = format.replaceAll("DDDD", "DDD");
		format = format.replace('e', 'E');
		format = format.replace('W', 'w');
		format = format.replaceAll("[gG]", "y");
		format = format.replace('Y', 'y');
		format = format.replace('A', 'a');
		format = format.replace('Z', 'z');
		format = format.replaceAll("L{1,4}", "yyyy-MM-dd HH:mm:ss");

		return format;
	}

	public static final Creator<DateTimeDevice> CREATOR = new Creator<DateTimeDevice>() {
		@Override
		public DateTimeDevice createFromParcel(final Parcel aParcel) {
			return new DateTimeDevice(aParcel);
		}

		@Override
		public DateTimeDevice[] newArray(int size) {
			return new DateTimeDevice[size];
		}
	};


	public DateTimeDevice(final Parcel aParcel) {
		super(aParcel);
		year = aParcel.readInt();
		month = aParcel.readInt();
		day = aParcel.readInt();
		hour = aParcel.readInt();
		minute = aParcel.readInt();
		second = aParcel.readInt();
		dateTimeFormat = new SimpleDateFormat(aParcel.readString());
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);
		aParcel.writeInt(year);
		aParcel.writeInt(month);
		aParcel.writeInt(day);
		aParcel.writeInt(hour);
		aParcel.writeInt(minute);
		aParcel.writeInt(second);
		aParcel.writeString(dateTimeFormat.toPattern());
	}

	@Override
	public void update(JSONObject aJsonValues) {
		super.update(aJsonValues);

		year = aJsonValues.optInt(JSON_YEAR_KEY);
		month = aJsonValues.optInt(JSON_MONTH_KEY);
		day = aJsonValues.optInt(JSON_DAY_KEY);
		hour = aJsonValues.optInt(JSON_HOUR_KEY);
		minute = aJsonValues.optInt(JSON_MINUTE_KEY);
		second = aJsonValues.optInt(JSON_SECOND_KEY);
	}

	public String getFormattedDate() {


		final GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
		return dateTimeFormat.format(calendar.getTime());
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

		final DateTimeDevice otherDevice = (DateTimeDevice) aOther;

		if (!dateTimeFormat.equals(otherDevice.dateTimeFormat)) {
			return false;
		}

		if (year != otherDevice.year || month != otherDevice.month || day != otherDevice.day || hour != otherDevice.hour || minute != otherDevice.minute || second != otherDevice.second) {
			return false;
		}

		return identicalBase(otherDevice);
	}
}
