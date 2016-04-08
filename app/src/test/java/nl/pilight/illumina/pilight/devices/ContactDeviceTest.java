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

public class ContactDeviceTest {
	@Test
	public void testConstructionAndUpdate() throws JSONException {
		final String jsonDeviceProperties = "{\"group\":[\"Contacts\"],\"order\":2,\"type\":6,\"media\":[\"all\"],\"name\":\"Test Contact A\"}";
		final String jsonUpdate1 = "{\"state\":\"closed\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"opened\",\"timestamp\":1453932587}";

		final ContactDevice device = new ContactDevice("contact_a", "Contacts", new JSONObject(jsonDeviceProperties));

		Assert.assertEquals("contact_a", device.getId());
		Assert.assertEquals("Contacts", device.getGroupId());
		Assert.assertEquals("Test Contact A", device.getName());
		Assert.assertEquals(2, device.getOrder());
		Assert.assertTrue(device.isReadonly());
		Assert.assertFalse(device.isClosed());

		device.update(new JSONObject(jsonUpdate1));

		Assert.assertTrue(device.isClosed());
		Assert.assertEquals(1453932586, device.getTimestamp());

		device.update(new JSONObject(jsonUpdate2));

		Assert.assertFalse(device.isClosed());
		Assert.assertEquals(1453932587, device.getTimestamp());
	}

	@Test
	public void testSerialization() throws JSONException {
		final String jsonDeviceProperties = "{\"group\":[\"Contacts\"],\"order\":1,\"type\":6,\"media\":[\"all\"],\"name\":\"Test Contact A\"}";
		final String jsonUpdate1 = "{\"state\":\"closed\",\"timestamp\":1453932586}";
		final String jsonUpdate2 = "{\"state\":\"opened\",\"timestamp\":1453932587}";

		final ContactDevice device = new ContactDevice("contact_a", "Contacts", new JSONObject(jsonDeviceProperties));

		ParcelCheckHelper.compareParceledVersion(device,ContactDevice.CREATOR);

		device.update(new JSONObject(jsonUpdate1));

		ParcelCheckHelper.compareParceledVersion(device, ContactDevice.CREATOR);

		device.update(new JSONObject(jsonUpdate2));

		ParcelCheckHelper.compareParceledVersion(device, ContactDevice.CREATOR);
	}
}