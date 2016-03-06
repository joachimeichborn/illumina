package nl.pilight.illumina.widget.viewHolder;

import android.view.View;
import android.widget.CheckBox;

import nl.pilight.illumina.pilight.devices.SwitchDevice;

public class SwitchViewHolder extends AbstractDeviceViewHolder<SwitchDevice> {

	private CheckBox mCheckBox;

	public SwitchViewHolder(final View aView) {
		super(aView);
		mCheckBox = (CheckBox) aView.findViewById(android.R.id.checkbox);
	}

	@Override
	void initView() {
		mCheckBox.setChecked(device.isOn());
		mCheckBox.setEnabled(!device.isReadonly());
	}
}
