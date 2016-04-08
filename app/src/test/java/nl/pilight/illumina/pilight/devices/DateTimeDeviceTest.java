package nl.pilight.illumina.pilight.devices;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import java.util.HashMap;
import java.util.Map;

import nl.pilight.illumina.BuildConfig;

@RunWith(RobolectricGradleTestRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class)

public class DateTimeDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Times\"],\"order\":3,\"type\":8,\"media\":[\"all\"],\"name\":\"Test DateTime A\"}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Times\"],\"order\":5,\"type\":8,\"media\":[\"all\"],\"name\":\"Test DateTime B\", \"format\":\"HH:mm:ss_gggg-M-D\"}";
		final String jsonUpdate1 = "{\"year\":\"2016\",\"month\":\"02\",\"day\":\"29\",\"hour\":\"23\",\"minute\":\"13\",\"second\":\"45\",\"timestamp\":1453932586}";

		final DateTimeDevice deviceA = new DateTimeDevice("datetime_a", "Times", new JSONObject(jsonDevicePropertiesA));
		final DateTimeDevice deviceB = new DateTimeDevice("datetime_b", "Times", new JSONObject(jsonDevicePropertiesB));

		Assert.assertEquals("datetime_a", deviceA.getId());
		Assert.assertEquals("Times", deviceA.getGroupId());
		Assert.assertEquals("Test DateTime A", deviceA.getName());
		Assert.assertEquals(3, deviceA.getOrder());
		Assert.assertTrue(deviceA.isReadonly());

		Assert.assertEquals("datetime_b", deviceB.getId());
		Assert.assertEquals("Times", deviceB.getGroupId());
		Assert.assertEquals("Test DateTime B", deviceB.getName());
		Assert.assertEquals(5, deviceB.getOrder());
		Assert.assertTrue(deviceB.isReadonly());

		deviceA.update(new JSONObject(jsonUpdate1));
		Assert.assertEquals("2016-02-29 23:13:45", deviceA.getFormattedDate());
		Assert.assertEquals(1453932586,deviceA.getTimestamp());

		deviceB.update(new JSONObject(jsonUpdate1));
		Assert.assertEquals("23:13:45_2016-2-29", deviceB.getFormattedDate());
		Assert.assertEquals(1453932586,deviceB.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDevicePropertiesB = "{\"group\":[\"Times\"],\"order\":5,\"type\":8,\"media\":[\"all\"],\"name\":\"Test DateTime B\", \"datetime-format\":\"HH:mm:ss_YY-M-D\"}";
		final String jsonUpdate1 = "{\"year\":\"2016\",\"month\":\"02\",\"day\":\"29\",\"hour\":\"23\",\"minute\":\"13\",\"second\":\"45\",\"timestamp\":1453932586}";

		final DateTimeDevice device = new DateTimeDevice("datetime_a", "Times", new JSONObject(jsonDevicePropertiesB));

		ParcelCheckHelper.compareParceledVersion(device, DateTimeDevice.CREATOR);

		device.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(device, DateTimeDevice.CREATOR);
	}

	@Test
	public void testDateTimeFormatConversion(){
		final Map<String, String> formats = new HashMap<>();
		formats.put("M", "M");
		formats.put("MM", "MM");
		formats.put("MMM", "MMM");
		formats.put("MMMM", "MMMM");
		formats.put("D", "dd");
		formats.put("DD", "dd");
		formats.put("DDD", "DDD");
		formats.put("DDDD", "DDD");
		formats.put("e", "E");
		formats.put("E", "E");
		formats.put("w", "w");
		formats.put("W", "w");
		formats.put("YY", "yy");
		formats.put("YYYY", "yyyy");
		formats.put("gg", "yy");
		formats.put("GG", "yy");
		formats.put("a", "a");
		formats.put("A", "a");
		formats.put("H", "H");
		formats.put("h", "h");
		formats.put("m", "m");
		formats.put("s", "s");
		formats.put("S", "S");
		formats.put("SS", "SS");
		formats.put("SSS", "SSS");
		formats.put("Z", "z");
		formats.put("L", "yyyy-MM-dd HH:mm:ss");
		formats.put("LLLL", "yyyy-MM-dd HH:mm:ss");

		for (final Map.Entry<String,String> entry:formats.entrySet()) {
			Assert.assertEquals(entry.getValue(), DateTimeDevice.convertDateTimeFormat(entry.getKey()));
		}
	}
}