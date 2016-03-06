package nl.pilight.illumina.pilight.devices;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import nl.pilight.illumina.pilight.DeviceType;

public interface Device extends Parcelable {
	public String getId();

	public String getName();

	public int getOrder();

	public String getGroupId();

	public DeviceType getType();

	public boolean isReadonly();

	public void update(final JSONObject aJsonValues) throws JSONException;

	public JSONObject getJsonCode(final AbstractDevice.Property aChangedProperty) throws JSONException;

	public boolean identical(final Object aOther);

	enum Property {
		DIMLEVEL,
		VALUE,
		UPDATE;

		public static final Property[] values = values();
	}
}
