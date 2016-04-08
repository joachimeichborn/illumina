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

public class SwitchDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Switches\"],\"order\":3,\"type\":1,\"media\":[\"all\"],\"name\":\"Test Switch A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Switches\"],\"order\":5,\"type\":4,\"media\":[\"all\"],\"name\":\"Test Switch B\", \"readonly\":0}";
		final String jsonUpdate1 = "{\"state\":\"on\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"off\",\"timestamp\":1453932587}";

		final SwitchDevice deviceA = new SwitchDevice("switch_a", "Switches", new JSONObject(jsonDevicePropertiesA));
		final SwitchDevice deviceB = new SwitchDevice("switch_b", "Switches", new JSONObject(jsonDevicePropertiesB));

		Assert.assertEquals("switch_a", deviceA.getId());
		Assert.assertEquals("Switches", deviceA.getGroupId());
		Assert.assertEquals("Test Switch A", deviceA.getName());
		Assert.assertEquals(3, deviceA.getOrder());
		Assert.assertTrue(deviceA.isReadonly());

		Assert.assertEquals("switch_b", deviceB.getId());
		Assert.assertEquals("Switches", deviceB.getGroupId());
		Assert.assertEquals("Test Switch B", deviceB.getName());
		Assert.assertEquals(5, deviceB.getOrder());
		Assert.assertTrue(!deviceB.isReadonly());

		Assert.assertFalse(deviceA.isOn());

		deviceA.update(new JSONObject(jsonUpdate1));
		Assert.assertTrue(deviceA.isOn());
		Assert.assertEquals(1453932586, deviceA.getTimestamp());

		deviceA.update(new JSONObject(jsonUpdate2));
		Assert.assertFalse(deviceA.isOn());
		Assert.assertEquals(1453932587, deviceA.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Switches\"],\"order\":3,\"type\":1,\"media\":[\"all\"],\"name\":\"Test Switch A\", \"readonly\":1}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Switches\"],\"order\":5,\"type\":4,\"media\":[\"all\"],\"name\":\"Test Switch B\", \"readonly\":0}";
		final String jsonUpdate1 = "{\"state\":\"on\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"off\",\"timestamp\":1453932587}";

		final SwitchDevice deviceA = new SwitchDevice("switch_a", "Switches", new JSONObject(jsonDevicePropertiesA));
		final SwitchDevice deviceB = new SwitchDevice("switch_b", "Switches", new JSONObject(jsonDevicePropertiesB));

		ParcelCheckHelper.compareParceledVersion(deviceA, SwitchDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, SwitchDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));
		deviceB.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, SwitchDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, SwitchDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate2));
		deviceB.update(new JSONObject(jsonUpdate2));

		ParcelCheckHelper.compareParceledVersion(deviceA, SwitchDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, SwitchDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));
		deviceB.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, SwitchDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, SwitchDevice.CREATOR);
	}
}