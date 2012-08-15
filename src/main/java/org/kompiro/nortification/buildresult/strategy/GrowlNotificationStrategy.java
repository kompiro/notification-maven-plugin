package org.kompiro.nortification.buildresult.strategy;

import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

public class GrowlNotificationStrategy implements NotificationStrategy {
	
	private Log log;
	private String title;
	private String message;
	
	public GrowlNotificationStrategy(Log log, String title,  String message) {
		this.log = log;
		this.title = title;
		this.message = message;
	}

	@Override
	public void show() {
		ProcessBuilder builder = new ProcessBuilder(new String[]{
				"growlnotify",
				"-t",
				title,
				"-m",
				message
		});
		try {
			Process process = builder.start();
			int result = process.waitFor();
			log.debug(String.valueOf(result));
		} catch (IOException e) {
			log.error(e);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

}
