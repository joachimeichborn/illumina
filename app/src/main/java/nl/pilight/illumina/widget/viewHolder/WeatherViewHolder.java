package nl.pilight.illumina.widget.viewHolder;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.Device;
import nl.pilight.illumina.pilight.devices.WeatherDevice;

public class WeatherViewHolder extends AbstractDeviceViewHolder<WeatherDevice> {

	private final ViewGroup temperature;
	private final TextView temperatureText;
	private final ViewGroup humidity;
	private final TextView humidityText;
	private final ViewGroup pressure;
	private final TextView pressureText;
	private final ViewGroup windgust;
	private final TextView windgustText;
	private final ViewGroup windavg;
	private final TextView windavgText;
	private final ViewGroup sunrise;
	private final TextView sunriseText;
	private final ViewGroup sunset;
	private final TextView sunsetText;
	private final ViewGroup battery;
	private final ImageView batteryImage;
	private final ViewGroup winddir;
	private final ImageView winddirImage;
	private int oldWinddir;
	private final ViewGroup update;
	private final ImageButton updateButton;

	private static Drawable batteryFullDrawable;
	private static Drawable batteryEmptyDrawable;

	public WeatherViewHolder(final View aView) {
		super(aView);

		temperature = (ViewGroup) aView.findViewById(R.id.temperature);
		temperatureText = (TextView) aView.findViewById(R.id.temperature_text);

		humidity = (ViewGroup) aView.findViewById(R.id.humidity);
		humidityText = (TextView) aView.findViewById(R.id.humidity_text);

		pressure = (ViewGroup) aView.findViewById(R.id.pressure);
		pressureText = (TextView) aView.findViewById(R.id.pressure_text);

		windgust = (ViewGroup) aView.findViewById(R.id.windgust);
		windgustText = (TextView) aView.findViewById(R.id.windgust_text);

		windavg = (ViewGroup) aView.findViewById(R.id.windavg);
		windavgText = (TextView) aView.findViewById(R.id.windavg_text);

		winddir = (ViewGroup) aView.findViewById(R.id.winddir);
		winddirImage = (ImageView) aView.findViewById(R.id.winddir_image);

		battery = (ViewGroup) aView.findViewById(R.id.battery);
		batteryImage = (ImageView) aView.findViewById(R.id.battery_image);

		sunrise = (ViewGroup) aView.findViewById(R.id.sunrise);
		sunriseText = (TextView) aView.findViewById(R.id.sunrise_text);

		sunset = (ViewGroup) aView.findViewById(R.id.sunset);
		sunsetText = (TextView) aView.findViewById(R.id.sunset_text);

		update = (ViewGroup) aView.findViewById(R.id.update);
		updateButton = (ImageButton) aView.findViewById(R.id.update_btn);

		updateButton.setEnabled(false);
		animateUpdateButton(0.3F);

		oldWinddir = 0;

		if (batteryEmptyDrawable == null) {
			batteryEmptyDrawable = aView.getResources().getDrawable(R.drawable.battery_empty_light);
		}
		if (batteryFullDrawable == null) {
			batteryFullDrawable = aView.getResources().getDrawable(R.drawable.battery_full_light);
		}
	}

	@Override
	void initView() {
		if (device.hasTemperatureValue() && device.isShowTemperature()) {
			temperature.setVisibility(View.VISIBLE);
			temperatureText.setText(String.format("%.1f", device.getTemperature()) + "°");
		} else {
			temperature.setVisibility(View.GONE);
		}

		if (device.hasSunriseValue() && device.hasSunsetValue() && device.isShowSunriseset()) {
			sunrise.setVisibility(View.VISIBLE);
			sunset.setVisibility(View.VISIBLE);
			sunriseText.setText(Double.toString(device.getSunrise()).replace(".", ":"));
			sunsetText.setText(Double.toString(device.getSunset()).replace(".", ":"));
		} else {
			sunrise.setVisibility(View.GONE);
			sunset.setVisibility(View.GONE);
		}

		if (device.hasHumidityValue() && device.isShowHumidity()) {
			humidity.setVisibility(View.VISIBLE);
			humidityText.setText(String.format("%.1f", device.getHumidity()) + "%");
		} else {
			humidity.setVisibility(View.GONE);
		}

		if (device.hasPressureValue() && device.isShowPressure()) {
			pressure.setVisibility(View.VISIBLE);
			pressureText.setText(String.format("%.1f", device.getPressure()));
		} else {
			pressure.setVisibility(View.GONE);
		}

		if (device.hasWinddirValue() && device.isShowWind()) {
			winddir.setVisibility(View.VISIBLE);
			if (Build.VERSION.SDK_INT < 11) {
				RotateAnimation animation = new RotateAnimation(oldWinddir, device.getWinddir(),
						Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setInterpolator(new LinearInterpolator());
				animation.setDuration(1);
				animation.setFillAfter(true);
				winddirImage.startAnimation(animation);
				oldWinddir = device.getWinddir();
			} else {
				winddirImage.setRotation(device.getWinddir());
			}
		} else {
			winddir.setVisibility(View.GONE);
		}

		if (device.hasWindgustValue() && device.isShowWind()) {
			windgust.setVisibility(View.VISIBLE);
			windgustText.setText(String.valueOf(device.getWindgust()));
		} else {
			windgust.setVisibility(View.GONE);
		}

		if (device.hasWindavgValue() && device.isShowWind()) {
			windavg.setVisibility(View.VISIBLE);
			windavgText.setText(String.valueOf(device.getWindavg()));
		} else {
			windavg.setVisibility(View.GONE);
		}

		if (device.hasBatteryValue() && device.isShowBattery()) {
			battery.setVisibility(View.VISIBLE);
			batteryImage.setImageDrawable(device.getBattery()
					? batteryFullDrawable : batteryEmptyDrawable);
		} else {
			battery.setVisibility(View.GONE);
		}

		if (device.isShowUpdate()) {
			update.setVisibility(View.VISIBLE);
			updateButton.setOnClickListener((new View.OnClickListener() {
				@Override
				public void onClick(final View aView) {
					if (updateButton.isEnabled()) {
						getDeviceChangeListener().onDeviceChange(device, Device.Property.UPDATE);
						updateButton.setEnabled(false);
						animateUpdateButton(0.3F);
					}
				}
			}));
			if (!device.getUpdate()) {
				if (updateButton.isEnabled()) {
					updateButton.setEnabled(false);
					animateUpdateButton(0.3F);
				}
			} else {
				if (!updateButton.isEnabled()) {
					updateButton.setEnabled(true);
					animateUpdateButton(1F);
				}
			}
		} else {
			update.setVisibility(View.GONE);
		}
	}

	private void animateUpdateButton(final float aAlpha) {
		if (Build.VERSION.SDK_INT >= 11) {
			updateButton.setAlpha(aAlpha);
		} else {
			final AlphaAnimation alpha = new AlphaAnimation(aAlpha, aAlpha);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			updateButton.startAnimation(alpha);
		}
	}
}
