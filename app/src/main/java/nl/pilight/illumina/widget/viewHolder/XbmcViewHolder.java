package nl.pilight.illumina.widget.viewHolder;

import android.view.View;
import android.widget.ImageView;

import nl.pilight.illumina.R;
import nl.pilight.illumina.pilight.devices.XbmcDevice;

public class XbmcViewHolder extends AbstractDeviceViewHolder<XbmcDevice> {
	private static final String TAG = XbmcViewHolder.class.getName();

	private ImageView movie;
	private ImageView episode;
	private ImageView music;
	private ImageView aScreen;
	private ImageView iScreen;
	private ImageView stop;
	private ImageView pause;
	private ImageView home;
	private ImageView shutdown;
	private ImageView play;

	public XbmcViewHolder(final View aView) {
		super(aView);

		movie = (ImageView) aView.findViewById(R.id.movie_image);
		episode = (ImageView) aView.findViewById(R.id.episode_image);
		music = (ImageView) aView.findViewById(R.id.music_image);
		aScreen = (ImageView) aView.findViewById(R.id.screen_active_image);
		iScreen = (ImageView) aView.findViewById(R.id.screen_inactive_image);
		stop = (ImageView) aView.findViewById(R.id.stop_image);
		pause = (ImageView) aView.findViewById(R.id.pause_image);
		home = (ImageView) aView.findViewById(R.id.home_image);
		shutdown = (ImageView) aView.findViewById(R.id.shutdown_image);
		play = (ImageView) aView.findViewById(R.id.play_image);
	}

	@Override
	void initView() {
		movie.setVisibility(View.GONE);
		episode.setVisibility(View.GONE);
		music.setVisibility(View.GONE);
		aScreen.setVisibility(View.GONE);
		iScreen.setVisibility(View.GONE);
		stop.setVisibility(View.GONE);
		pause.setVisibility(View.GONE);
		home.setVisibility(View.GONE);
		shutdown.setVisibility(View.GONE);
		play.setVisibility(View.GONE);

		if (device.isShowMedia()) {
			switch (device.getMedia()) {
				case MOVIE:
					movie.setVisibility(View.VISIBLE);
					break;
				case EPISODE:
					episode.setVisibility(View.VISIBLE);
					break;
				case SONG:
					music.setVisibility(View.VISIBLE);
					break;
			}
		}

		if (device.isShowAction()) {
			switch (device.getAction()) {
				case HOME:
					home.setVisibility(View.VISIBLE);
					break;
				case SHUTDOWN:
					shutdown.setVisibility(View.VISIBLE);
					break;
				case PLAY:
					play.setVisibility(View.VISIBLE);
					break;
				case PAUSE:
					pause.setVisibility(View.VISIBLE);
					break;
				case STOP:
					stop.setVisibility(View.VISIBLE);
					break;
				case ACTIVE:
					aScreen.setVisibility(View.VISIBLE);
					break;
				case INACTIVE:
					iScreen.setVisibility(View.VISIBLE);
					break;
			}
		}
	}
}
