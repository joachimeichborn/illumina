package nl.pilight.illumina.widget.viewHolder;

import android.view.View;
import android.widget.RadioButton;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.ContactDevice;

public class ContactViewHolder extends AbstractDeviceViewHolder<ContactDevice> {

	private RadioButton radioButton;

	public ContactViewHolder(final View aView) {
		super(aView);

		radioButton = (RadioButton) aView.findViewById(R.id.radiobutton);
	}

	@Override
	void initView() {
		radioButton.setChecked(device.isClosed());
		radioButton.setEnabled(!device.isReadonly());
	}
}
