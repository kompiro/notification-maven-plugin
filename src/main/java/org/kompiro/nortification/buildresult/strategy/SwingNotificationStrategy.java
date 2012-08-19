package org.kompiro.nortification.buildresult.strategy;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import org.apache.maven.plugin.logging.Log;
import org.kompiro.nortification.ui.NotificationWindow;

public class SwingNotificationStrategy implements NotificationStrategy {

	private Log log;
	private int duration;
	private String title;
	private String message;
	private NotificationType type;

	public SwingNotificationStrategy(Log log,int duration, String title,  String message,  NotificationType type) {
		this.log = log;
		this.duration = duration;
		this.title = title;
		this.message = message;
		this.type = type;
	}

	@Override
	public void show() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					final NotificationWindow window = new NotificationWindow();
					window.setLocation(0, -10000); // hide initialization window
					window.setTitle(title);
					window.setMessage(message);
					window.setDuration(duration);
					window.setColor(type.getColor());
					window.pack();
					window.notifyUI();
				}
			});
		} catch (InterruptedException e) {
			log.error(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		}
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

}
