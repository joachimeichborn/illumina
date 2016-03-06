package nl.pilight.illumina.pilight.devices.states;

import android.text.TextUtils;

import nl.pilight.illumina.Logger;

public enum PendingSwitchState {
	RUNNING("running"), PENDING("pending"), STOPPED("stopped"), UNKNOWN("unknown");

	private static final String TAG = PendingSwitchState.class.getName();
	private String jsonValue;

	PendingSwitchState(final String aJsonValue) {
		jsonValue = aJsonValue;
	}

	public String getJsonValue() {
		return jsonValue;
	}

	public static PendingSwitchState getByJsonValue(final String aJsonValue) {
		for (final PendingSwitchState state : PendingSwitchState.values()) {
			if (TextUtils.equals(state.getJsonValue(), aJsonValue)) {
				return state;
			}
		}

		Logger.warn(TAG, "Could not find state for json value '" + aJsonValue + "' returning default state " + UNKNOWN);
		return UNKNOWN;
	}
}
