package nl.pilight.illumina.widget.viewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.pilight.devices.DimmerDevice;

public class DimmerViewHolder extends AbstractDeviceViewHolder<DimmerDevice> {
	private final CheckBox checkBox;
	private final SeekBar seekBar;
	private final TextView seekBarText;

	public DimmerViewHolder(View view) {
		super(view);

		checkBox = (CheckBox) view.findViewById(android.R.id.checkbox);
		seekBar = (SeekBar) view.findViewById(R.id.seekbar);
		seekBarText = (TextView) view.findViewById(R.id.seekbar_text);

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(final SeekBar aSeekBar, final int aI, final boolean aB) {
				seekBarText.setText(String.valueOf(aI + device.getMinDimLevel()));
			}

			@Override
			public void onStartTrackingTouch(final SeekBar aSeekBar) {
			}

			@Override
			public void onStopTrackingTouch(final SeekBar aSeekBar) {
				device.setDimLevel(seekBar.getProgress() + device.getMinDimLevel());
				getDeviceChangeListener().onDeviceChange(device, Device.Property.DIMLEVEL);
			}
		});
	}

	@Override
	void initView() {
		checkBox.setChecked(device.isOn());
		checkBox.setEnabled(!device.isReadonly());
		seekBar.setMax(device.getMaxDimLevel() - device.getMinDimLevel());
		seekBar.setProgress(device.getDimLevel() - device.getMinDimLevel());
		seekBar.setEnabled(!device.isReadonly());
		seekBarText.setText(String.valueOf(device.getDimLevel()));
	}
}
