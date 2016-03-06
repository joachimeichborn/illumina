/*
 * illumina, a pilight remote
 *
 * Copyright (c) 2014 Peter Heisig <http://google.com/+PeterHeisig>
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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.pilight.illumina.pilight.devices.Device;

public class Group implements Parcelable {
	private final String id;
	private final Map<String, Device> devices = new LinkedHashMap<>();

	public Group(final String aGroupId) {
		id = aGroupId;
	}

	public String getId() {
		return id;
	}

	public static final Parcelable.Creator<Group> CREATOR
			= new Parcelable.Creator<Group>() {

		@Override
		public Group createFromParcel(final Parcel aParcel) {
			return new Group(aParcel);
		}

		@Override
		public Group[] newArray(final int aSize) {
			return new Group[aSize];
		}

	};

	public Group(final Parcel aParcel) {
		id = aParcel.readString();

		final Bundle bundle = aParcel.readBundle();
		assert bundle != null;

		bundle.setClassLoader(getClass().getClassLoader());

		for (final String deviceId : bundle.keySet()) {
			devices.put(deviceId, (Device) bundle.getParcelable(deviceId));
		}
	}

	public List<Device> getDevices() {
		return new ArrayList<>(devices.values());
	}

	public void addDevice(final Device aDevice) {
		devices.put(aDevice.getId(), aDevice);
		sortDevices();
	}

	@Override
	public void writeToParcel(final Parcel aParcel, final int aFlags) {
		aParcel.writeString(id);

		final Bundle devices = new Bundle();

		for (String deviceId : this.devices.keySet()) {
			devices.putParcelable(deviceId, this.devices.get(deviceId));
		}

		aParcel.writeBundle(devices);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	private void sortDevices() {
		final List<Map.Entry<String, Device>> entries = new LinkedList<>(devices.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Device>>() {
			@Override
			public int compare(final Map.Entry<String, Device> aEntry1,
							   final Map.Entry<String, Device> aEntry2) {

				final int o1 = aEntry1.getValue().getOrder();
				final int o2 = aEntry2.getValue().getOrder();

				if (o1 > o2) {
					return 1;
				} else if (o1 < o2) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		devices.clear();
		for (final Map.Entry<String, Device> entry : entries) {
			devices.put(entry.getKey(), entry.getValue());
		}
	}

	public Device getDevice(final String aDeviceId) {
		return devices.get(aDeviceId);
	}
}
