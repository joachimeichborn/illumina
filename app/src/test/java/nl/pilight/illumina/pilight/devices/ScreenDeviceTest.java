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

public class ScreenDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Screens\"],\"order\":3,\"type\":1,\"media\":[\"all\"],\"name\":\"Test Screen A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Screens\"],\"order\":5,\"type\":4,\"media\":[\"all\"],\"name\":\"Test Screen B\", \"readonly\":0}";
		final String jsonUpdate1 = "{\"state\":\"up\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"down\",\"timestamp\":1453932587}";

		final ScreenDevice deviceA = new ScreenDevice("screen_a", "Screens", new JSONObject(jsonDevicePropertiesA));
		final ScreenDevice deviceB = new ScreenDevice("screen_b", "Screens", new JSONObject(jsonDevicePropertiesB));

		Assert.assertEquals("screen_a", deviceA.getId());
		Assert.assertEquals("Screens", deviceA.getGroupId());
		Assert.assertEquals("Test Screen A", deviceA.getName());
		Assert.assertEquals(3, deviceA.getOrder());
		Assert.assertTrue(deviceA.isReadonly());

		Assert.assertEquals("screen_b", deviceB.getId());
		Assert.assertEquals("Screens", deviceB.getGroupId());
		Assert.assertEquals("Test Screen B", deviceB.getName());
		Assert.assertEquals(5, deviceB.getOrder());
		Assert.assertTrue(!deviceB.isReadonly());

		Assert.assertFalse(deviceA.isUp());

		deviceA.update(new JSONObject(jsonUpdate1));
		Assert.assertTrue(deviceA.isUp());
		Assert.assertEquals(1453932586, deviceA.getTimestamp());

		deviceA.update(new JSONObject(jsonUpdate2));
		Assert.assertFalse(deviceA.isUp());
		Assert.assertEquals(1453932587, deviceA.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Screens\"],\"order\":3,\"type\":1,\"media\":[\"all\"],\"name\":\"Test Screen A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Screens\"],\"order\":5,\"type\":4,\"media\":[\"all\"],\"name\":\"Test Screen B\", \"readonly\":0}";
		final String jsonUpdate1 = "{\"state\":\"up\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"down\",\"timestamp\":1453932587}";

		final ScreenDevice deviceA = new ScreenDevice("screen_a", "Screens", new JSONObject(jsonDevicePropertiesA));
		final ScreenDevice deviceB = new ScreenDevice("screen_b", "Screens", new JSONObject(jsonDevicePropertiesB));

		ParcelCheckHelper.compareParceledVersion(deviceA, ScreenDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, ScreenDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));
		deviceB.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, ScreenDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, ScreenDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate2));
		deviceB.update(new JSONObject(jsonUpdate2));

		ParcelCheckHelper.compareParceledVersion(deviceA, ScreenDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, ScreenDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));
		deviceB.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, ScreenDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, ScreenDevice.CREATOR);
	}
}