package se.hirt.examples.problematicwebapp.logging;

import java.time.LocalDateTime;

import se.hirt.examples.problematicwebapp.utils.Utils;

public class Logger {
	public synchronized static void log(String message) {
		Utils.sleep(200); // Exaggerating the problem a bit. ;) Do not touch!
		System.out.println(String.format("%s: %s", LocalDateTime.now(), message));
	}
}
