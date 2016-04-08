package nl.pilight.illumina.widget.viewHolder;

import android.os.Build;
import android.view.View;
import android.widget.ImageButton;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.pilight.devices.ScreenDevice;
import nl.pilight.illumina.pilight.devices.states.ScreenState;

public class ScreenViewHolder extends AbstractDeviceViewHolder<ScreenDevice> {

	private final ImageButton upButton;
	private final ImageButton downButton;

	public ScreenViewHolder(final View aView) {
		super(aView);

		upButton = (ImageButton) aView.findViewById(R.id.up_action);
		downButton = (ImageButton) aView.findViewById(R.id.down_action);

		upButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View aView) {
				device.requestState(ScreenState.UP);
				getDeviceChangeListener().onDeviceChange(device, Device.Property.VALUE);
			}
		});

		downButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View aView) {
				device.requestState(ScreenState.DOWN);
				getDeviceChangeListener().onDeviceChange(device, Device.Property.VALUE);
			}
		});
	}

	@Override
	void initView() {
		upButton.setEnabled(!device.isReadonly());
		downButton.setEnabled(!device.isReadonly());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			upButton.setAlpha(!device.isReadonly() ? 1.0f : .5f);
			downButton.setAlpha(!device.isReadonly() ? 1.0f : .5f);
		}
	}
}
