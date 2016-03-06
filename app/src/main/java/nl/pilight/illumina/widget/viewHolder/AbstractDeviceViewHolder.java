package nl.pilight.illumina.widget.viewHolder;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.widget.DeviceAdapter;

public abstract class AbstractDeviceViewHolder<T extends Device> {
	T device;
	private TextView nameView;
	private DeviceAdapter.DeviceChangeListener deviceChangeListener;

	AbstractDeviceViewHolder(final View aView) {
		nameView = (TextView) aView.findViewById(android.R.id.text1);
	}

	public void setDevice(final T aDevice) {
		device = aDevice;
		nameView.setText(aDevice.getName());
		nameView.setTypeface(nameView.getTypeface(), !aDevice.isReadonly()
				? Typeface.NORMAL : Typeface.ITALIC);

		initView();
	}

	abstract void initView();

	public void setDeviceChangeListener(final DeviceAdapter.DeviceChangeListener aDeviceChangeListener) {
		deviceChangeListener = aDeviceChangeListener;
	}

	DeviceAdapter.DeviceChangeListener getDeviceChangeListener() {
		return deviceChangeListener;
	}
}
