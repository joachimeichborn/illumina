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

public class LabelDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Labels\"],\"order\":3,\"type\":15,\"media\":[\"all\"],\"name\":\"Test Label A\"}";
		final String jsonUpdate1 = "{\"label\":\"text\",\"color\":\"green\",\"timestamp\":1453932586}";

		final LabelDevice deviceA = new LabelDevice("label_a", "Labels", new JSONObject(jsonDevicePropertiesA));

		Assert.assertEquals("label_a", deviceA.getId());
		Assert.assertEquals("Labels", deviceA.getGroupId());
		Assert.assertEquals("Test Label A", deviceA.getName());
		Assert.assertEquals(3, deviceA.getOrder());
		Assert.assertTrue(deviceA.isReadonly());

		deviceA.update(new JSONObject(jsonUpdate1));

		Assert.assertEquals("text", deviceA.getLabel());
		Assert.assertEquals("green", deviceA.getColor());
		Assert.assertEquals(1453932586, deviceA.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Labels\"],\"order\":3,\"type\":15,\"media\":[\"all\"],\"name\":\"Test Label A\"}";
		final String jsonUpdate1 = "{\"label\":\"text\",\"color\":\"green\",\"timestamp\":1453932586}";

		final LabelDevice deviceA = new LabelDevice("label_a", "Labels", new JSONObject(jsonDevicePropertiesA));

		ParcelCheckHelper.compareParceledVersion(deviceA, LabelDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, LabelDevice.CREATOR);
	}
}