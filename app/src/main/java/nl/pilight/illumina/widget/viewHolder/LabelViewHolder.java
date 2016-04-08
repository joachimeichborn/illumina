package nl.pilight.illumina.widget.viewHolder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.LabelDevice;

public class LabelViewHolder extends AbstractDeviceViewHolder<LabelDevice> {
	private final TextView label;

	public LabelViewHolder(final View aView) {
		super(aView);
		label = (TextView) aView.findViewById(R.id.label_text);
	}

	@Override
	void initView() {
		label.setText(device.getLabel());
		label.setTextColor(Color.parseColor(device.getColor()));
	}
}
