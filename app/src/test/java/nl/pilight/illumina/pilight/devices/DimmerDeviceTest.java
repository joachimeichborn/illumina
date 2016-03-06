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

public class DimmerDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Dimmers\"],\"order\":3,\"type\":2,\"media\":[\"all\"],\"name\":\"Test Dimmer A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Dimmers\"],\"order\":5,\"type\":2,\"media\":[\"all\"],\"name\":\"Test Dimmer B\", \"readonly\":0, \"dimlevel-minimum\": 5, \"dimlevel-maximum\": 10}";
		final String jsonUpdate1 = "{\"state\":\"on\",\"dimlevel\":7,\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"off\",\"dimlevel\":4,\"timestamp\":1453932587}";

		final DimmerDevice deviceA = new DimmerDevice("dimmer_a", "Dimmers", new JSONObject(jsonDevicePropertiesA));
		final DimmerDevice  deviceB = new DimmerDevice("dimmer_b", "Dimmers", new JSONObject(jsonDevicePropertiesB));

		Assert.assertEquals("dimmer_a", deviceA.getId());
		Assert.assertEquals("Dimmers", deviceA.getGroupId());
		Assert.assertEquals("Test Dimmer A", deviceA.getName());
		Assert.assertEquals(3, deviceA.getOrder());
		Assert.assertTrue(deviceA.isReadonly());
		Assert.assertEquals(0, deviceA.getMinDimLevel());
		Assert.assertEquals(15, deviceA.getMaxDimLevel());

		Assert.assertEquals("dimmer_b", deviceB.getId());
		Assert.assertEquals("Dimmers", deviceB.getGroupId());
		Assert.assertEquals("Test Dimmer B", deviceB.getName());
		Assert.assertEquals(5, deviceB.getOrder());
		Assert.assertTrue(!deviceB.isReadonly());
		Assert.assertEquals(5, deviceB.getMinDimLevel());
		Assert.assertEquals(10, deviceB.getMaxDimLevel());

		Assert.assertFalse(deviceA.isOn());
		Assert.assertEquals(0, deviceA.getDimLevel());

		deviceA.update(new JSONObject(jsonUpdate1));
		Assert.assertTrue(deviceA.isOn());
		Assert.assertEquals(7, deviceA.getDimLevel());
		Assert.assertEquals(1453932586, deviceA.getTimestamp());

		deviceA.update(new JSONObject(jsonUpdate2));
		Assert.assertFalse(deviceA.isOn());
		Assert.assertEquals(4, deviceA.getDimLevel());
		Assert.assertEquals(1453932587,deviceA.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Dimmers\"],\"order\":3,\"type\":2,\"media\":[\"all\"],\"name\":\"Test Dimmer A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Dimmers\"],\"order\":5,\"type\":2,\"media\":[\"all\"],\"name\":\"Test Dimmer B\", \"readonly\":0, \"dimlevel-minimum\": 5, \"dimlevel-maximum\": 10}";
		final String jsonUpdate1 = "{\"state\":\"on\",\"dimlevel\":7,\"timestamp\":1453932586}";

		final DimmerDevice deviceA = new DimmerDevice("dimmer_a", "Dimmers", new JSONObject(jsonDevicePropertiesA));
		final DimmerDevice deviceB = new DimmerDevice("dimmer_b", "Dimmers", new JSONObject(jsonDevicePropertiesB));

		ParcelCheckHelper.compareParceledVersion(deviceA, DimmerDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, DimmerDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));
		deviceB.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, DimmerDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, DimmerDevice.CREATOR);
	}
}