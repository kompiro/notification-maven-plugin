package org.kompiro.nortification.buildresult.strategy;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import org.apache.maven.plugin.logging.Log;
import org.kompiro.notification.ui.NotificationWindow;

public class SwingNotificationStrategy implements NotificationStrategy {

	private Log log;
	private SwingNotificationStrategyParameter parameter;

	public SwingNotificationStrategy(Log log,SwingNotificationStrategyParameter parameter) {
		this.log = log;
		this.parameter = parameter;
	}

	@Override
	public void show() {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					@SuppressWarnings("serial")
					final NotificationWindow window = new NotificationWindow(){
						@Override
						protected void notifyClose() {
							synchronized (SwingNotificationStrategy.this) {
								SwingNotificationStrategy.this.notify();
							}
						}
					};
					window.setLocation(0, -10000); // hide initialization window
					window.setTitle(parameter.getTitle());
					window.setMessage(parameter.getMessage());
					window.setDuration(parameter.getDuration());
					window.setColor(parameter.getType().getColor());
					window.setIconURL(parameter.getType().getIconURL());
					window.setStickMode(parameter.isStick());
					window.pack();
					window.notifyUI();
				}
			});
		} catch (InterruptedException e) {
			log.warn(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		}
		try {
			if(parameter.isStick()){
				synchronized (this) {
					this.wait();
				}
			} else {
				Thread.sleep(parameter.getDuration() + 1200); // add reverse time
			}
		} catch (InterruptedException e) {
			log.warn(e);
		}
	}

}
