package nl.pilight.illumina.pilight;


import org.jmock.Expectations;
import org.jmock.Mockery;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;

import java.util.List;

import nl.pilight.illumina.BuildConfig;
import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.pilight.devices.SwitchDevice;
import nl.pilight.illumina.pilight.devices.WeatherDevice;

@RunWith(RobolectricGradleTestRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class)
public class ConfigurationTest {
	private final static String jsonConfig = "{\"outlet_b\":{\"group\":[\"Outlets\"],\"order\":2,\"type\":1,\"media\":[\"all\"],\"readonly\":0,\"name\":\"Outlet 1\"}," +
			"\"temperature_b\":{\"temperature-decimals\":3,\"show-temperature\":1,\"group\":[\"Misc\"],\"order\":5,\"type\":3,\"media\":[\"all\"],\"name\":\"Temperature 2\"}," +
			"\"temperature_a\":{\"temperature-decimals\":3,\"show-temperature\":1,\"group\":[\"Misc\"],\"order\":4,\"type\":3,\"media\":[\"all\"],\"name\":\"Temperature 1\"}," +
			"\"outlet_a\":{\"group\":[\"Outlets\"],\"order\":1,\"type\":1,\"media\":[\"all\"],\"readonly\":0,\"name\":\"Fan\"}}";

	@Test
	public void testInitialization() throws JSONException {
		final JSONObject initJson = new JSONObject(jsonConfig);
		final Configuration config = new Configuration(null, initJson);

		final List<Group> groups = config.getGroups();
		Assert.assertEquals(2, groups.size());
		Assert.assertEquals("Misc", groups.get(0).getId());
		Assert.assertEquals("Outlets", groups.get(1).getId());

		final Group miscGroup = config.getGroup("Misc");
		Assert.assertEquals(2, miscGroup.getDevices().size());
		final WeatherDevice tempA = (WeatherDevice) miscGroup.getDevice("temperature_a");
		Assert.assertEquals("Temperature 1", tempA.getName());
		Assert.assertEquals(tempA, miscGroup.getDevices().get(0));
		Assert.assertEquals(DeviceType.WEATHER, tempA.getType());
		Assert.assertEquals(4, tempA.getOrder());
		final WeatherDevice tempB = (WeatherDevice) miscGroup.getDevice("temperature_b");
		Assert.assertEquals("Temperature 2", tempB.getName());
		Assert.assertEquals(tempB, miscGroup.getDevices().get(1));
		Assert.assertEquals(DeviceType.WEATHER, tempB.getType());
		Assert.assertEquals(5, tempB.getOrder());

		final Group outletGroup = config.getGroup("Outlets");
		Assert.assertEquals(2, outletGroup.getDevices().size());
		final SwitchDevice outletA = (SwitchDevice) outletGroup.getDevice("outlet_a");
		Assert.assertEquals("Fan", outletA.getName());
		Assert.assertEquals(outletA, outletGroup.getDevices().get(0));
		Assert.assertEquals(DeviceType.SWITCH, outletA.getType());
		Assert.assertEquals(1, outletA.getOrder());
		final SwitchDevice outletB = (SwitchDevice) outletGroup.getDevice("outlet_b");
		Assert.assertEquals("Outlet 1", outletB.getName());
		Assert.assertEquals(outletB, outletGroup.getDevices().get(1));
		Assert.assertEquals(DeviceType.SWITCH, outletB.getType());
		Assert.assertEquals(2, outletB.getOrder());
	}

	@Test
	public void testUpdate() throws JSONException {
		final Mockery context = new Mockery();
		final DeviceUpdateHandler handler = context.mock(DeviceUpdateHandler.class);
		context.checking(new Expectations() {
			{
				exactly(2).of(handler).onDeviceUpdated(with(any(Device.class)));
			}
		});

		final JSONObject initJson = new JSONObject(jsonConfig);
		final Configuration config = new Configuration(handler, initJson);

		final JSONObject updateJson1 = new JSONObject("{\"origin\":\"update\",\"values\":{\"timestamp\":1453932585,\"temperature\":85},\"uuid\":\"0000-74-da-38-1ad423\",\"devices\":[\"temperature_b\"],\"type\":3}");
		config.updateDevices(updateJson1);
		final JSONObject updateJson2 = new JSONObject("{\"devices\":[\"outlet_a\"],\"type\":1,\"origin\":\"update\",\"values\":{\"state\":\"on\",\"timestamp\":1453932586}}");
		config.updateDevices(updateJson2);

		final Group outletGroup = config.getGroup("Outlets");
		final SwitchDevice outletA = (SwitchDevice) outletGroup.getDevice("outlet_a");
		Assert.assertTrue(outletA.isOn());
		final SwitchDevice outletB = (SwitchDevice) outletGroup.getDevice("outlet_b");
		Assert.assertFalse(outletB.isOn());

		final Group miscGroup = config.getGroup("Misc");
		final WeatherDevice tempA = (WeatherDevice) miscGroup.getDevice("temperature_a");
		Assert.assertEquals(0, tempA.getTemperature(), 0.1);
		final WeatherDevice tempB = (WeatherDevice) miscGroup.getDevice("temperature_b");
		Assert.assertEquals(85, tempB.getTemperature(), 0.1);
	}
}