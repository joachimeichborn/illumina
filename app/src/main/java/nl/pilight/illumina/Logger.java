package nl.pilight.illumina;

import android.util.Log;

public class Logger {
	private static boolean LOGGING = true;

	public static void error(final String aTag, final String aMessage) {
		if (LOGGING)
			Log.e(aTag, aMessage);
	}

	public static void error(final String aTag, final String aMessage, final Throwable aThrowable) {
		if (LOGGING)
			Log.e(aTag, aMessage, aThrowable);
	}

	public static void warn(final String aTag, final String aMessage) {
		if (LOGGING)
			Log.w(aTag, aMessage);
	}

	public static void warn(final String aTag, final String aMessage, final Throwable aThrowable) {
		if (LOGGING)
			Log.w(aTag, aMessage, aThrowable);
	}

	public static void info(final String aTag, final String aMessage) {
		if (LOGGING)
			Log.i(aTag, aMessage);
	}

	public static void debug(final String aTag, final String aMessage) {
		if (LOGGING)
			Log.d(aTag, aMessage);
	}

	public static void verbose(final String aTag, final String aMessage) {
		if (LOGGING)
			Log.v(aTag, aMessage);
	}
}
