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

public class PendingSwitchDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Switches\"],\"order\":3,\"type\":7,\"media\":[\"all\"],\"name\":\"Test PendingSwitch A\", \"readonly\": 0}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Switches\"],\"order\":5,\"type\":7,\"media\":[\"all\"],\"name\":\"Test PendingSwitch B\", \"readonly\": 1}";
		final String jsonUpdate1 = "{\"state\":\"running\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"pending\",\"timestamp\":1453932587}";
		final String jsonUpdate3 = "{\"state\":\"stopped\",\"timestamp\":1453932588}";

		final PendingSwitchDevice deviceA = new PendingSwitchDevice("pending_switch_a", "Switches", new JSONObject(jsonDevicePropertiesA));
		final PendingSwitchDevice deviceB = new PendingSwitchDevice("pending_switch_b", "Switches", new JSONObject(jsonDevicePropertiesB));

		Assert.assertEquals("pending_switch_a", deviceA.getId());
		Assert.assertEquals("Switches", deviceA.getGroupId());
		Assert.assertEquals("Test PendingSwitch A", deviceA.getName());
		Assert.assertEquals(3, deviceA.getOrder());
		Assert.assertFalse(deviceA.isReadonly());

		Assert.assertEquals("pending_switch_b", deviceB.getId());
		Assert.assertEquals("Switches", deviceB.getGroupId());
		Assert.assertEquals("Test PendingSwitch B", deviceB.getName());
		Assert.assertEquals(5, deviceB.getOrder());
		Assert.assertTrue(deviceB.isReadonly());

		Assert.assertFalse(deviceA.isOn());
		Assert.assertFalse(deviceA.isPending());
		Assert.assertFalse(deviceA.isOff());

		deviceA.update(new JSONObject(jsonUpdate1));

		Assert.assertTrue(deviceA.isOn());
		Assert.assertFalse(deviceA.isPending());
		Assert.assertFalse(deviceA.isOff());
		Assert.assertEquals(1453932586, deviceA.getTimestamp());

		deviceA.update(new JSONObject(jsonUpdate2));

		Assert.assertFalse(deviceA.isOn());
		Assert.assertTrue(deviceA.isPending());
		Assert.assertFalse(deviceA.isOff());
		Assert.assertEquals(1453932587, deviceA.getTimestamp());

		deviceA.update(new JSONObject(jsonUpdate3));

		Assert.assertFalse(deviceA.isOn());
		Assert.assertFalse(deviceA.isPending());
		Assert.assertTrue(deviceA.isOff());
		Assert.assertEquals(1453932588, deviceA.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDevicePropertiesA = "{\"group\":[\"Switches\"],\"order\":3,\"type\":7,\"media\":[\"all\"],\"name\":\"Test PendingSwitch A\", \"readonly\": 0}";
		final String jsonDevicePropertiesB = "{\"group\":[\"Switches\"],\"order\":5,\"type\":7,\"media\":[\"all\"],\"name\":\"Test PendingSwitch B\", \"readonly\": 1}";
		final String jsonUpdate1 = "{\"state\":\"running\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"pending\",\"timestamp\":1453932587}";
		final String jsonUpdate3 = "{\"state\":\"stopped\",\"timestamp\":1453932588}";

		final PendingSwitchDevice deviceA = new PendingSwitchDevice ("pending_switch_a", "Switches", new JSONObject(jsonDevicePropertiesA));
		final PendingSwitchDevice deviceB = new PendingSwitchDevice ("pending_switch_b", "Switches", new JSONObject(jsonDevicePropertiesB));

		ParcelCheckHelper.compareParceledVersion(deviceA, PendingSwitchDevice.CREATOR);
		ParcelCheckHelper.compareParceledVersion(deviceB, PendingSwitchDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(deviceA, PendingSwitchDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate2));

		ParcelCheckHelper.compareParceledVersion(deviceA, PendingSwitchDevice.CREATOR);

		deviceA.update(new JSONObject(jsonUpdate3));

		ParcelCheckHelper.compareParceledVersion(deviceA, PendingSwitchDevice.CREATOR);
	}
}