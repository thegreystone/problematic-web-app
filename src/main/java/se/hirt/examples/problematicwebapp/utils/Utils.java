package se.hirt.examples.problematicwebapp.utils;

import se.hirt.examples.problematicwebapp.logging.Logger;

public class Utils {
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Logger.log(e.getMessage());
		}
	}
}
