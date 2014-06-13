/*
 * illumina, a pilight remote
 *
 * Copyright (c) 2014 Peter Heisig <http://google.com/+PeterHeisig>
 *                    CurlyMo <http://www.pilight.org>
 *
 * illumina is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * illumina is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with illumina. If not, see <http://www.gnu.org/licenses/>.
 */

package nl.pilight.illumina.pilight;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Device implements Parcelable {

    public static final String VALUE_ON = "on";
    public static final String VALUE_OFF = "off";
    public static final String VALUE_UP = "up";
    public static final String VALUE_DOWN = "down";
    public static final String VALUE_OPENED = "opened";
    public static final String VALUE_CLOSED = "closed";
    public static enum DeviceTypes {
        UNKNOWN,
        SWITCH,
        DIMMER,
        WEATHER,
        RELAY,
        SCREEN,
        CONTACT,
        PENDINGSW,
        DATETIME,
        XBMC,
        LIRC,
        WEBCAM
    }
    public static enum Properties {
        DIMLEVEL,
        VALUE,
        UPDATE
    }
    private String mId;
    private String mLocationId;
    private String mName;
    private int mOrder;
    private int mTimestamp;
    private int mMinInterval;
    private int mPollInterval;
    private String mValue;
    private String mState;
    private DeviceTypes mType = DeviceTypes.UNKNOWN;
    private int mDimLevel;
    private int mTemperature;
    private boolean mShowTemperature;
    private boolean mHasDateTimeFormat;
    private int mHumidity;
    private boolean mShowHumidity;
    private int mSunrise;
    private int mSunset;
    private boolean mShowSunriseset;
    private boolean mShowBattery;
    private boolean mShowUpdate;
    private int mGUIDecimals;
    private int mDeviceDecimals;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private String mDateTimeFormat;
    private boolean mAll;
    private boolean mHasHealthyBattery;
    private boolean mHasBatteryValue = false;
    private boolean mHasTemperatureValue = false;
    private boolean mHasHumidityValue = false;
    private boolean mHasSunriseValue = false;
    private boolean mHasSunsetValue = false;
    private boolean mIsReadOnly = false;

    public Device() {}

    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String locationId) {
        mLocationId = locationId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        mOrder = order;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public void setState(String state) {
        mState = state;
    }

    public DeviceTypes getType() {
        return mType;
    }

    public void setType(DeviceTypes type) {
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAll(boolean all) { mAll = all; }

    public boolean getAll() { return mAll; }

    public void setDimLevel(int dimLevel) {
        mDimLevel = dimLevel;
    }

    public int getDimLevel() {
        return mDimLevel;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public int getSunrise() {
        return mSunrise;
    }

    public int getSunset() {
        return mSunset;
    }

    public int getTimestamp() {
        return mTimestamp;
    }

    public int getPollInterval() {
        return mPollInterval;
    }

    public int getMinInterval() { return mMinInterval; }

    public int getYear() { return mYear; }

    public int getMonth() { return mMonth; }

    public int getDay() { return mDay; }

    public int getHour() { return mHour; }

    public int getMinute() { return mMinute; }

    public int getSecond() { return mSecond; }

    public String getDateTimeFormat() { return mDateTimeFormat; }

    public void setReadOnly(boolean readOnly) {
        mIsReadOnly = readOnly;
    }

    public void setTemperature(int temperature) {
        mHasTemperatureValue = true;
        mTemperature = temperature;
    }

    public void setSunrise(int sunrise) {
        mHasSunriseValue = true;
        mSunrise = sunrise;
    }

    public void setSunset(int sunset) {
        mHasSunsetValue = true;
        mSunset = sunset;
    }

    public void setTimestamp(int timestamp) {
        mTimestamp = timestamp;
    }

    public void setPollInterval(int pollinterval) { mPollInterval = pollinterval; }

    public void setMinInterval(int mininterval) { mMinInterval = mininterval; }

    public boolean isShowTemperature() {
        return mShowTemperature;
    }

    public boolean isShowSunriseset() {
        return mShowSunriseset;
    }

    public boolean isShowBattery() {
        return mShowBattery;
    }

    public boolean hasHealthyBattery() {
        return mHasHealthyBattery;
    }

    public boolean hasTemperatureValue() {
        return mHasTemperatureValue;
    }

    public boolean hasHumidityValue() {
        return mHasHumidityValue;
    }

    public boolean hasSunriseValue() {
        return mHasSunriseValue;
    }

    public boolean hasSunsetValue() {
        return mHasSunsetValue;
    }

    public boolean hasBatteryValue() {
        return mHasBatteryValue;
    }

    public boolean hasDatetTimeFormat() { return mHasDateTimeFormat; }

    public void setHealthyBattery(boolean hasHealthyBattery) {
        mHasBatteryValue = true;
        mHasHealthyBattery = hasHealthyBattery;
    }

    public void setShowTemperature(boolean showTemperature) {
        mShowTemperature = showTemperature;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        mHasHumidityValue = true;
        mHumidity = humidity;
    }

    public boolean isShowHumidity() {
        return mShowHumidity;
    }

    public boolean isShowUpdate() {
        return mShowUpdate;
    }

    public void setShowHumidity(boolean showHumidity) {
        mShowHumidity = showHumidity;
    }

    public void setShowBattery(boolean showBattery) {
        mShowBattery = showBattery;
    }

    public void setShowSunriseset(boolean showSunriseset) {
        mShowSunriseset = showSunriseset;
    }

    public void setShowUpdate (boolean showUpdate) {
        mShowUpdate = showUpdate;
    }

    public int getGUIDecimals() {
        return mGUIDecimals;
    }

    public void setGUIDecimals(int decimals) {
        mGUIDecimals = decimals;
    }

    public int getDeviceDecimals() {
        return mDeviceDecimals;
    }

    public void setDeviceDecimals(int decimals) {
        mDeviceDecimals = decimals;
    }

    public boolean isOn() {
        return TextUtils.equals(mState, VALUE_ON);
    }

    public boolean isUp() {
        return TextUtils.equals(mState, VALUE_UP);
    }

    public String getState() { return mState; }

    public boolean isOpened() {
        return TextUtils.equals(mState, VALUE_OPENED);
    }

    public boolean isClosed() {
        return TextUtils.equals(mState, VALUE_CLOSED);
    }

    public void setYear(int year) { mYear = year; }

    public void setMonth(int month) { mMonth = month; }

    public void setDay(int day) { mDay = day; }

    public void setHour(int hour) { mHour = hour; }

    public void setMinute(int minute) { mMinute = minute; }

    public void setSecond(int second) { mSecond = second; }

    public void setDateTimeFormat(String datetimeformat) {
        mHasDateTimeFormat = true;
        mDateTimeFormat = datetimeformat;
    }

    public static final Parcelable.Creator<Device> CREATOR
            = new Parcelable.Creator<Device>() {

        @Override
        public Device createFromParcel(Parcel parcel) {
            return new Device(parcel);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }

    };

    public Device(Parcel parcel) {
        mId = parcel.readString();
        mLocationId = parcel.readString();
        mName = parcel.readString();
        mOrder = parcel.readInt();
        mMinInterval = parcel.readInt();
        mTimestamp = parcel.readInt();
        mPollInterval = parcel.readInt();
        mValue = parcel.readString();
        mState = parcel.readString();
        mAll = Boolean.parseBoolean(parcel.readString());
        mType = DeviceTypes.values()[parcel.readInt()];
        mDimLevel = parcel.readInt();
        mTemperature = parcel.readInt();
        mHumidity = parcel.readInt();
        mSunrise = parcel.readInt();
        mSunset = parcel.readInt();
        mYear = parcel.readInt();
        mMonth = parcel.readInt();
        mDay = parcel.readInt();
        mHour = parcel.readInt();
        mMinute = parcel.readInt();
        mSecond = parcel.readInt();
        mGUIDecimals = parcel.readInt();
        mDeviceDecimals = parcel.readInt();
        mDateTimeFormat = parcel.readString();
        mHasDateTimeFormat = Boolean.parseBoolean(parcel.readString());
        mHasHealthyBattery = Boolean.parseBoolean(parcel.readString());
        mShowTemperature = Boolean.parseBoolean(parcel.readString());
        mShowHumidity = Boolean.parseBoolean(parcel.readString());
        mShowBattery = Boolean.parseBoolean(parcel.readString());
        mShowSunriseset = Boolean.parseBoolean(parcel.readString());
        mShowUpdate = Boolean.parseBoolean(parcel.readString());
        mHasBatteryValue = Boolean.parseBoolean(parcel.readString());
        mHasHumidityValue = Boolean.parseBoolean(parcel.readString());
        mHasTemperatureValue = Boolean.parseBoolean(parcel.readString());
        mHasSunriseValue = Boolean.parseBoolean(parcel.readString());
        mHasSunsetValue = Boolean.parseBoolean(parcel.readString());
        mIsReadOnly = Boolean.parseBoolean(parcel.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mLocationId);
        parcel.writeString(mName);
        parcel.writeInt(mOrder);
        parcel.writeInt(mTimestamp);
        parcel.writeInt(mMinInterval);
        parcel.writeInt(mPollInterval);
        parcel.writeString(mValue);
        parcel.writeString(mState);
        parcel.writeString(mAll ? "true" : "false");
        parcel.writeInt(mType.ordinal());
        parcel.writeInt(mDimLevel);
        parcel.writeInt(mTemperature);
        parcel.writeInt(mHumidity);
        parcel.writeInt(mSunrise);
        parcel.writeInt(mSunset);
        parcel.writeInt(mYear);
        parcel.writeInt(mMonth);
        parcel.writeInt(mDay);
        parcel.writeInt(mHour);
        parcel.writeInt(mMinute);
        parcel.writeInt(mSecond);
        parcel.writeInt(mGUIDecimals);
        parcel.writeInt(mDeviceDecimals);
        parcel.writeString(mDateTimeFormat);
        parcel.writeString(mHasDateTimeFormat ? "true" : "false");
        parcel.writeString(mHasHealthyBattery ? "true" : "false");
        parcel.writeString(mShowTemperature ? "true" : "false");
        parcel.writeString(mShowHumidity ? "true" : "false");
        parcel.writeString(mShowBattery ? "true" : "false");
        parcel.writeString(mShowSunriseset ? "true" : "false");
        parcel.writeString(mShowUpdate ? "true" : "false");
        parcel.writeString(mHasBatteryValue ? "true" : "false");
        parcel.writeString(mHasHumidityValue ? "true" : "false");
        parcel.writeString(mHasTemperatureValue ? "true" : "false");
        parcel.writeString(mHasSunsetValue ? "true" : "false");
        parcel.writeString(mHasSunriseValue ? "true" : "false");
        parcel.writeString(mIsReadOnly ? "true" : "false");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return mName;
    }

    public boolean isWritable() {
        switch (mType) {
            case DIMMER:
            case SCREEN:
            case SWITCH:
            case PENDINGSW:
                return !mIsReadOnly;

            case WEATHER:
            case CONTACT:
            default:
                return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Device device = (Device) o;
        return TextUtils.equals(mId, device.mId)
                && TextUtils.equals(mLocationId, device.mLocationId);

    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mLocationId.hashCode();
        return result;
    }

}
