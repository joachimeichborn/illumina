package nl.pilight.illumina.pilight.devices.states;

import nl.pilight.illumina.Logger;

public enum ContactState {
	CLOSED("closed"), OPEN("open"), UNKNOWN("unknown");

	private static final String TAG = ContactState.class.getName();
	private final String jsonValue;

	ContactState(final String aJsonValue) {
		jsonValue = aJsonValue;
	}

	public String getJsonValue() {
		return jsonValue;
	}

	public static ContactState getByJsonValue(final String aJsonValue) {
		for (final ContactState switchState : ContactState.values()) {
			if (switchState.getJsonValue().equals(aJsonValue)) {
				return switchState;
			}
		}

		Logger.warn(TAG, "Cannot map json value '" + aJsonValue + "' to a specific contact state, returning " + UNKNOWN);
		return UNKNOWN;
	}
}
