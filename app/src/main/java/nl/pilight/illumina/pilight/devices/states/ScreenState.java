package nl.pilight.illumina.pilight.devices.states;

import nl.pilight.illumina.Logger;

public enum ScreenState {
	UP("up"), DOWN("down"), UNKNOWN("unknown");

	private static final String TAG = ScreenState.class.getName();
	private final String jsonValue;

	ScreenState(final String aJsonValue) {
		jsonValue = aJsonValue;
	}

	public String getJsonValue() {
		return jsonValue;
	}

	public static ScreenState getByJsonValue(final String aJsonValue) {
		for (final ScreenState switchState : ScreenState.values()) {
			if (switchState.getJsonValue().equals(aJsonValue)) {
				return switchState;
			}
		}

		Logger.warn(TAG, "Cannot map json value '" + aJsonValue + "' to a specific screen state, returning " + UNKNOWN);
		return UNKNOWN;
	}
}
