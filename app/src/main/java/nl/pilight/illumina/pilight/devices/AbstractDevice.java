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

package nl.pilight.illumina.pilight.devices;

import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import nl.pilight.illumina.pilight.DeviceType;

public abstract class AbstractDevice implements Device, Serializable {
	private static final String TAG = AbstractDevice.class.getName();

	private static final String JSON_NAME_KEY = "name";
	private static final String JSON_ORDER_KEY = "order";
	private static final String JSON_READ_ONLY_KEY = "readonly";
	private static final String JSON_TIMESTAMP_KEY = "timestamp";

	private static final int FALLBACK_ORDER = 1000;
	public static final String JSON_VALUES_KEY = "values";
	public static final String JSON_UPDATE_KEY = "update";
	public static final String JSON_ALL_KEY = "all";

	public static class DeviceBuilder {
		public static Device build(final String aDeviceId, final String aGroupId,
								   final JSONObject aDeviceJson) {
			final int typeCode = aDeviceJson.optInt("type", -1);
			final DeviceType type = DeviceType.getByTypeCode(typeCode);

			return type.build(aDeviceId, aGroupId, aDeviceJson);
		}
	}

	private final String id;
	private final String name;
	private final String groupId;
	private final DeviceType type;
	private final int order;
	private final boolean all;
	private final boolean readonly;

	private int timestamp;

	public String getGroupId() {
		return groupId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public DeviceType getType() {
		return type;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public int getOrder() {
		return order;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public AbstractDevice(final Parcel aParcel) {
		id = aParcel.readString();
		name = aParcel.readString();
		groupId = aParcel.readString();
		type = DeviceType.valueOf(aParcel.readString());
		order = aParcel.readInt();
		timestamp = aParcel.readInt();
		all = Boolean.parseBoolean(aParcel.readString());
		readonly = Boolean.parseBoolean(aParcel.readString());
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		aParcel.writeString(id);
		aParcel.writeString(name);
		aParcel.writeString(groupId);
		aParcel.writeString(type.name());
		aParcel.writeInt(order);
		aParcel.writeInt(timestamp);
		aParcel.writeString(Boolean.toString(all));
		aParcel.writeString(Boolean.toString(readonly));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return "AbstractDevice{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", groupId='" + groupId + '\'' +
				", type=" + type +
				", order=" + order +
				", all=" + all +
				", timestamp=" + timestamp +
				'}';
	}

	@Override
	public boolean equals(final Object aOther) {
		if (this == aOther) {
			return true;
		}

		if (aOther == null || getClass() != aOther.getClass()) {
			return false;
		}

		final AbstractDevice otherDevice = (AbstractDevice) aOther;
		return TextUtils.equals(id, otherDevice.id)
				&& TextUtils.equals(groupId, otherDevice.groupId);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + groupId.hashCode();
		return result;
	}

	@Override
	public boolean isReadonly() {
		return readonly;
	}

	public AbstractDevice(final String aDeviceId, final String aGroupId, final DeviceType aDeviceType, final JSONObject aDeviceJson) {
		id = aDeviceId;
		groupId = aGroupId;
		type = aDeviceType;
		name = aDeviceJson.optString(JSON_NAME_KEY, id);
		order = aDeviceJson.optInt(JSON_ORDER_KEY, FALLBACK_ORDER);
		all = aDeviceJson.optInt(JSON_ALL_KEY) == 1;
		readonly = aDeviceJson.optInt(JSON_READ_ONLY_KEY, 1) == 1;
	}

	public void update(final JSONObject aJsonValues) {
		timestamp = aJsonValues.optInt(JSON_TIMESTAMP_KEY, 0);
	}


	public JSONObject getJsonCode(final Property aChangedProperty) throws JSONException {
		final JSONObject values = new JSONObject();

		if (aChangedProperty == Property.UPDATE) {
			values.put(JSON_UPDATE_KEY, 1);
		}

		if (all) {
			values.put(JSON_ALL_KEY, 1);
		}

		final JSONObject code = new JSONObject();

		if (values.length() != 0) {
			code.put(JSON_VALUES_KEY, values);
		}

		return code;
	}

	/**
	 * Check whether two devices are identical, that means they do not only must have the same configuration but also the same values
	 *
	 * @return true if the two objects are identical, that is, the same device in the same state
	 */
	boolean identicalBase(final AbstractDevice aOtherDevice) {
		if (this == aOtherDevice) {
			return true;
		}
		if (aOtherDevice == null) {
			return false;
		}
		if (id != aOtherDevice.getId()) {
			return false;
		}
		if (groupId != aOtherDevice.getGroupId()) {
			return false;
		}
		if (name != aOtherDevice.getName()) {
			return false;
		}
		if (type != aOtherDevice.getType()) {
			return false;
		}
		if (order != aOtherDevice.getOrder()) {
			return false;
		}
		if (timestamp != aOtherDevice.getTimestamp()) {
			return false;
		}

		if (readonly != aOtherDevice.readonly) {
			return false;
		}

		return true;
	}

	public abstract boolean identical(final Object aOther);
}
