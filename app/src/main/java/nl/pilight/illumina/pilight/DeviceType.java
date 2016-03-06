package nl.pilight.illumina.pilight;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import nl.pilight.illumina.Logger;
import nl.pilight.illumina.pilight.devices.ContactDevice;
import nl.pilight.illumina.pilight.devices.DateTimeDevice;
import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.pilight.devices.DimmerDevice;
import nl.pilight.illumina.pilight.devices.LabelDevice;
import nl.pilight.illumina.pilight.devices.PendingSwitchDevice;
import nl.pilight.illumina.pilight.devices.ScreenDevice;
import nl.pilight.illumina.pilight.devices.SwitchDevice;
import nl.pilight.illumina.pilight.devices.UnknownDevice;
import nl.pilight.illumina.pilight.devices.WeatherDevice;
import nl.pilight.illumina.pilight.devices.XbmcDevice;

/**
 * The supported device types. The comments for each device are based on the information from https://www.pilight.org/modules/protocols/#devtypes
 */
public enum DeviceType {
	UNKNOWN() {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new UnknownDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * A switch device is a device that can have a certain amount of states.
	 */
	SWITCH(1) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new SwitchDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * A dimmer device is basically a switch, but with an additonal value. The additional value represents the dimlevel of the device.
	 */
	DIMMER(2) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new DimmerDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * A weather device receives certain values, but can't send out values. The weather device will therefor only show value labels in the GUI.
	 */
	WEATHER(3) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new WeatherDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * A relay device is exactly the same as a switch except a relay device can have an inversed state. You can have relays that turn on when you connect them or relay devices that turn off. In the GUI a relay will just show as a toggle switch.
	 */
	RELAY(4) {
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new SwitchDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * A screen device is also very similar to a switch, but it doesn't have a fixed state. For most screens, pilight doesn't know wether the screen is up or down. Therefor the GUI will show stateless momentary buttons.
	 */
	SCREEN(5) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new ScreenDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * A contact device can have a closed and opened state. This means that a contact have been lost or made. Some contact devices only send a single state when the contact is lost. (These devices are supported yet).
	 */
	CONTACT(6) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new ContactDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * A pending switch is like a normal switch but has an additional state between on and off called pending. Pending means that a device is being turned on or off but not yet done switching. While the protocol is pending it can't be controlled. You have to wait before it reaches the on or off state. So, when the device is pending, make sure the switch element is disabled.
	 */
	PENDING_SW(7) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new PendingSwitchDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * These protocol are self explanatory.
	 */
	DATE_TIME(8) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new DateTimeDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	/**
	 * These protocol are self explanatory. Check the wiki for further explanation.
	 */
	XBMC(9) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new XbmcDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	},
	LABEL(15) {
		@Override
		public Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson) {
			return new LabelDevice(aDeviceId, aGroupId, aDeviceJson);
		}
	};

	private static final String TAG = DeviceType.class.getName();

	private List<Integer> typeCodes = new LinkedList<>();

	DeviceType(final Integer... aTypeCodes) {
		typeCodes.addAll(Arrays.asList(aTypeCodes));
	}

	public abstract Device build(final String aDeviceId, final String aGroupId, final JSONObject aDeviceJson);

	public static DeviceType getByTypeCode(final Integer aTypeCode) {
		for (final DeviceType type : DeviceType.values()) {
			if (type.typeCodes.contains(aTypeCode)) {
				return type;
			}
		}

		Logger.info(TAG, "Could not find device type for code " + aTypeCode + ", using type UNKNOWN");
		return DeviceType.UNKNOWN;
	}
}
