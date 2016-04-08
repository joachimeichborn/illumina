package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;

import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;

public class WeatherDevice extends AbstractDevice {
	private static final String JSON_SHOW_TEMPERATURE_KEY = "show-temperature";
	private static final String JSON_TEMPERATURE_KEY = "temperature";
	private static final String JSON_SHOW_SUNRISESET_KEY = "show-sunriseset";
	private static final String JSON_SHOW_HUMIDITY_KEY = "show-humidity";
	private static final String JSON_SHOW_PRESSURE_KEY = "show-pressure";
	private static final String JSON_SHOW_WIND_KEY = "show-wind";
	private static final String JSON_SHOW_BATTERY_KEY = "show-battery";
	private static final String JSON_SHOW_UPDATE_KEY = "show-update";

	private final boolean showTemperature;
	private boolean hasTemperatureValue;

	private double temperature;
	private final boolean showSunriseset;
	private boolean hasSunriseValue;
	private boolean hasSunsetValue;
	private double sunrise;
	private double sunset;
	private final boolean showHumidity;
	private boolean hasHumidityValue;
	private double humidity;
	private final boolean showPressure;
	private boolean hasPressureValue;
	private double pressure;
	private final boolean showWind;
	private boolean hasWinddirValue;
	private boolean hasWindgustValue;
	private boolean hasWindavgValue;
	private int winddir;
	private int windgust;
	private int windavg;
	private final boolean showBattery;
	private boolean hasBatteryValue;
	private boolean battery;
	private boolean showUpdate;
	private boolean update;

	public WeatherDevice(final String aDeviceId, final String aGroupId, final JSONObject aJsonDevice) {
		super(aDeviceId, aGroupId, DeviceType.WEATHER, aJsonDevice);

		showTemperature = aJsonDevice.optInt(JSON_SHOW_TEMPERATURE_KEY, 0) == 1;
		showSunriseset = aJsonDevice.optInt(JSON_SHOW_SUNRISESET_KEY, 0) == 1;
		showHumidity = aJsonDevice.optInt(JSON_SHOW_HUMIDITY_KEY, 0) == 1;
		showPressure = aJsonDevice.optInt(JSON_SHOW_PRESSURE_KEY, 0) == 1;
		showWind = aJsonDevice.optInt(JSON_SHOW_WIND_KEY, 0) == 1;
		showBattery = aJsonDevice.optInt(JSON_SHOW_BATTERY_KEY, 0) == 1;
		showUpdate = aJsonDevice.optInt(JSON_SHOW_UPDATE_KEY, 0) == 1;
	}

	public static final Creator<WeatherDevice> CREATOR
			= new Creator<WeatherDevice>() {
		@Override
		public WeatherDevice createFromParcel(final Parcel aParcel) {
			return new WeatherDevice(aParcel);
		}

		@Override
		public WeatherDevice[] newArray(final int aSize) {
			return new WeatherDevice[aSize];
		}
	};

	public WeatherDevice(final Parcel aParcel) {
		super(aParcel);

		showTemperature = Boolean.parseBoolean(aParcel.readString());
		hasTemperatureValue = Boolean.parseBoolean(aParcel.readString());
		temperature = aParcel.readDouble();
		showSunriseset = Boolean.parseBoolean(aParcel.readString());
		hasSunriseValue = Boolean.parseBoolean(aParcel.readString());
		hasSunsetValue = Boolean.parseBoolean(aParcel.readString());
		sunrise = aParcel.readDouble();
		sunset = aParcel.readDouble();
		showHumidity = Boolean.parseBoolean(aParcel.readString());
		hasHumidityValue = Boolean.parseBoolean(aParcel.readString());
		humidity = aParcel.readDouble();
		showPressure = Boolean.parseBoolean(aParcel.readString());
		hasPressureValue = Boolean.parseBoolean(aParcel.readString());
		pressure = aParcel.readDouble();
		showWind = Boolean.parseBoolean(aParcel.readString());
		hasWinddirValue = Boolean.parseBoolean(aParcel.readString());
		hasWindgustValue = Boolean.parseBoolean(aParcel.readString());
		hasWindavgValue = Boolean.parseBoolean(aParcel.readString());
		winddir = aParcel.readInt();
		windgust = aParcel.readInt();
		windavg = aParcel.readInt();
		showBattery = Boolean.parseBoolean(aParcel.readString());
		hasBatteryValue = Boolean.parseBoolean(aParcel.readString());
		battery = Boolean.parseBoolean(aParcel.readString());
		showUpdate = Boolean.parseBoolean(aParcel.readString());
		update = Boolean.parseBoolean(aParcel.readString());
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		super.writeToParcel(aParcel, aFlags);

		aParcel.writeString(Boolean.toString(showTemperature));
		aParcel.writeString(Boolean.toString(hasTemperatureValue));
		aParcel.writeDouble(temperature);
		aParcel.writeString(Boolean.toString(showSunriseset));
		aParcel.writeString(Boolean.toString(hasSunriseValue));
		aParcel.writeString(Boolean.toString(hasSunsetValue));
		aParcel.writeDouble(sunrise);
		aParcel.writeDouble(sunset);
		aParcel.writeString(Boolean.toString(showHumidity));
		aParcel.writeString(Boolean.toString(hasHumidityValue));
		aParcel.writeDouble(humidity);
		aParcel.writeString(Boolean.toString(showPressure));
		aParcel.writeString(Boolean.toString(hasPressureValue));
		aParcel.writeDouble(pressure);
		aParcel.writeString(Boolean.toString(showWind));
		aParcel.writeString(Boolean.toString(hasWinddirValue));
		aParcel.writeString(Boolean.toString(hasWindgustValue));
		aParcel.writeString(Boolean.toString(hasWindavgValue));
		aParcel.writeInt(winddir);
		aParcel.writeInt(windgust);
		aParcel.writeInt(windavg);
		aParcel.writeString(Boolean.toString(showBattery));
		aParcel.writeString(Boolean.toString(hasBatteryValue));
		aParcel.writeString(Boolean.toString(battery));
		aParcel.writeString(Boolean.toString(showUpdate));
		aParcel.writeString(Boolean.toString(update));
	}

	@Override
	public void update(JSONObject aJsonValues) {
		super.update(aJsonValues);

		if (aJsonValues.has(JSON_TEMPERATURE_KEY)) {
			hasTemperatureValue = true;
			temperature = aJsonValues.optDouble(JSON_TEMPERATURE_KEY, 0);
		}
		if (aJsonValues.has("sunrise")) {
			hasSunriseValue = true;
			sunrise = aJsonValues.optDouble("sunrise", 0);
		}
		if (aJsonValues.has("sunset")) {
			hasSunsetValue = true;
			sunset = aJsonValues.optDouble("sunset", 0);
		}
		if (aJsonValues.has("humidity")) {
			hasHumidityValue = true;
			humidity = aJsonValues.optDouble("humidity", 0);
		}
		if (aJsonValues.has("pressure")) {
			hasPressureValue = true;
			pressure = aJsonValues.optDouble("pressure", 0);
		}
		if (aJsonValues.has("winddir")) {
			hasWinddirValue = true;
			winddir = aJsonValues.optInt("winddir", 0);
		}
		if (aJsonValues.has("windgust")) {
			hasWindgustValue = true;
			windgust = aJsonValues.optInt("windgust", 0);
		}
		if (aJsonValues.has("windavg")) {
			hasWindavgValue = true;
			windavg = aJsonValues.optInt("windavg", 0);
		}
		if (aJsonValues.has("battery")) {
			hasBatteryValue = true;
			battery = aJsonValues.optInt("battery", 1) == 1;
		}
		if (aJsonValues.has("update")) {
			update = aJsonValues.optInt("update", 0) == 1;
		}
	}

	public boolean isShowTemperature() {
		return showTemperature;
	}

	public boolean hasTemperatureValue() {
		return hasTemperatureValue;
	}

	public double getTemperature() {
		return temperature;
	}

	public boolean isShowSunriseset() {
		return showSunriseset;
	}

	public boolean hasSunriseValue() {
		return hasSunriseValue;
	}

	public boolean hasSunsetValue() {
		return hasSunsetValue;
	}

	public double getSunrise() {
		return sunrise;
	}

	public double getSunset() {
		return sunset;
	}

	public boolean isShowHumidity() {
		return showHumidity;
	}

	public boolean hasHumidityValue() {
		return hasHumidityValue;
	}

	public double getHumidity() {
		return humidity;
	}

	public boolean isShowPressure() {
		return showPressure;
	}

	public boolean hasPressureValue() {
		return hasPressureValue;
	}

	public double getPressure() {
		return pressure;
	}

	public boolean isShowWind() {
		return showWind;
	}

	public boolean hasWinddirValue() {
		return hasWinddirValue;
	}

	public boolean hasWindgustValue() {
		return hasWindgustValue;
	}

	public boolean hasWindavgValue() {
		return hasWindavgValue;
	}

	public int getWinddir() {
		return winddir;
	}

	public int getWindgust() {
		return windgust;
	}

	public int getWindavg() {
		return windavg;
	}

	public boolean isShowBattery() {
		return showBattery;
	}

	public boolean hasBatteryValue() {
		return hasBatteryValue;
	}

	public boolean getBattery() {
		return battery;
	}

	public boolean isShowUpdate() {
		return showUpdate;
	}

	public boolean getUpdate() {
		return update;
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

		final WeatherDevice otherDevice = (WeatherDevice) aOther;

		if (showTemperature != otherDevice.showTemperature || hasTemperatureValue != otherDevice.hasTemperatureValue || temperature != otherDevice.temperature) {
			return false;
		}

		if (showSunriseset != otherDevice.showSunriseset || hasSunriseValue != otherDevice.hasSunriseValue || hasSunsetValue != otherDevice.hasSunsetValue || sunrise != otherDevice.sunrise || sunset != otherDevice.sunset) {
			return false;
		}

		if (showHumidity != otherDevice.showHumidity || hasHumidityValue != otherDevice.hasHumidityValue || humidity != otherDevice.humidity) {
			return false;
		}

		if (showPressure != otherDevice.showPressure || hasPressureValue != otherDevice.hasPressureValue || pressure != otherDevice.pressure) {
			return false;
		}

		if (showWind != otherDevice.showWind || hasWinddirValue != otherDevice.hasWinddirValue || hasWindgustValue != otherDevice.hasWindgustValue || hasWindavgValue != otherDevice.hasWindavgValue || winddir != otherDevice.winddir || windgust != otherDevice.windgust || windavg != otherDevice.windavg) {
			return false;
		}

		if (showBattery != otherDevice.showBattery || hasBatteryValue != otherDevice.hasBatteryValue || battery != battery) {
			return false;
		}

		if (showUpdate != otherDevice.showUpdate || update != otherDevice.update) {
			return false;
		}

		return identicalBase(otherDevice);
	}
}
