package nl.pilight.illumina.pilight.devices.states;

import nl.pilight.illumina.Logger;

public enum SwitchState {
	ON("on"), OFF("off"), UNKNOWN("unknown");

	private static final String TAG = SwitchState.class.getName();
	private final String jsonValue;

	SwitchState(final String aJsonValue) {
		jsonValue = aJsonValue;
	}

	public String getJsonValue() {
		return jsonValue;
	}

	public static SwitchState getByJsonValue(final String aJsonValue) {
		for (final SwitchState switchState : SwitchState.values()) {
			if (switchState.getJsonValue().equals(aJsonValue)) {
				return switchState;
			}
		}

		Logger.warn(TAG, "Cannot map json value '" + aJsonValue + "' to a specific switch state, returning " + UNKNOWN);
		return UNKNOWN;
	}
}
