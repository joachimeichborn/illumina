package nl.pilight.illumina.widget.viewHolder;

import android.view.View;
import android.widget.TextView;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.DateTimeDevice;

public class DateTimeViewHolder extends AbstractDeviceViewHolder<DateTimeDevice> {
	private final TextView dateTime;

	public DateTimeViewHolder(final View aView) {
		super(aView);

		dateTime = (TextView) aView.findViewById(R.id.datetime);
	}

	@Override
	void initView() {
		dateTime.setText(device.getFormattedDate());
	}
}
