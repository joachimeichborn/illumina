package nl.pilight.illumina.pilight;

import nl.pilight.illumina.pilight.devices.Device;

public interface DeviceUpdateHandler {
	void onDeviceUpdated(final Device aDevice);
}
