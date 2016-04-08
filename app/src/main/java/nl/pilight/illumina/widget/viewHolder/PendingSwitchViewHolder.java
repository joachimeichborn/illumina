package nl.pilight.illumina.widget.viewHolder;

import android.view.View;
import android.widget.CheckBox;

import nl.pilight.illumina.R;
import nl.pilight.illumina.layouts.GifView;
import nl.pilight.illumina.pilight.devices.PendingSwitchDevice;

public class PendingSwitchViewHolder extends AbstractDeviceViewHolder<PendingSwitchDevice> {
	private static final String TAG = PendingSwitchViewHolder.class.getName();

	private final CheckBox checkBox;
	private final GifView loader;

	public PendingSwitchViewHolder(final View aView) {
		super(aView);

		checkBox = (CheckBox) aView.findViewById(android.R.id.checkbox);
		loader = (GifView) aView.findViewById(R.id.loader);
	}

	@Override
	void initView() {
		if (device.isOn()) {
			loader.setVisibility(loader.GONE);
			checkBox.setVisibility(checkBox.VISIBLE);
			checkBox.setChecked(true);
			checkBox.setEnabled(!device.isReadonly());
		} else if (device.isPending()) {
			checkBox.setVisibility(checkBox.GONE);
			loader.setVisibility(loader.VISIBLE);
			checkBox.setEnabled(false);
		} else if (device.isOff()) {
			checkBox.setVisibility(checkBox.VISIBLE);
			loader.setVisibility(loader.GONE);
			checkBox.setChecked(false);
			checkBox.setEnabled(!device.isReadonly());
		}
	}
}
