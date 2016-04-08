package nl.pilight.illumina.pilight.devices;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import nl.pilight.illumina.BuildConfig;

@RunWith(RobolectricGradleTestRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class)

public class UnknownDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Unknowns\"],\"order\":3,\"type\":-1,\"media\":[\"all\"],\"name\":\"Test Unknown A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Unknowns\"],\"order\":5,\"type\":-1,\"media\":[\"all\"],\"name\":\"Test Unknown B\", \"readonly\":0}";
		final String jsonUpdate1 = "{\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"timestamp\":1453932587}";

		final UnknownDevice deviceA = new UnknownDevice( "unknown_a", "Unknowns", new JSONObject(jsonDevicePropertiesA));
		final UnknownDevice deviceB = new UnknownDevice( "unknown_b", "Unknowns", new JSONObject(jsonDevicePropertiesB));

		Assert.assertEquals("unknown_a", deviceA.getId());
		Assert.assertEquals("Unknowns", deviceA.getGroupId());
		Assert.assertEquals("Test Unknown A", deviceA.getName());
		Assert.assertEquals(3, deviceA.getOrder());
		Assert.assertTrue(deviceA.isReadonly());

		Assert.assertEquals("unknown_b", deviceB.getId());
		Assert.assertEquals("Unknowns", deviceB.getGroupId());
		Assert.assertEquals("Test Unknown B", deviceB.getName());
		Assert.assertEquals(5, deviceB.getOrder());
		Assert.assertTrue(!deviceB.isReadonly());

		deviceA.update(new JSONObject(jsonUpdate1));
		Assert.assertEquals(1453932586, deviceA.getTimestamp());

		deviceA.update(new JSONObject(jsonUpdate2));
		Assert.assertEquals(1453932587, deviceA.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Unknowns\"],\"order\":3,\"type\":-1,\"media\":[\"all\"],\"name\":\"Test Unknown A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Unknowns\"],\"order\":5,\"type\":-1,\"media\":[\"all\"],\"name\":\"Test Unknown B\", \"readonly\":0}";
		final String jsonUpdate1 = "{\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"timestamp\":1453932587}";

		final UnknownDevice deviceA = new UnknownDevice( "unknown_a", "Unknowns", new JSONObject(jsonDevicePropertiesA));
		final UnknownDevice deviceB = new UnknownDevice( "unknown_b", "Unknowns", new JSONObject(jsonDevicePropertiesB));

		ParcelCheckHelper.compareParceledVersion(deviceA, UnknownDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, UnknownDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));
		deviceB.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, UnknownDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, UnknownDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate2));
		deviceB.update(new JSONObject(jsonUpdate2));

		ParcelCheckHelper.compareParceledVersion(deviceA, UnknownDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, UnknownDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));
		deviceB.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, UnknownDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, UnknownDevice.CREATOR);
	}
}