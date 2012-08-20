package org.kompiro.nortification.buildresult.strategy;

public class SwingNotificationStrategyParameter {
	private int duration;
	private String title;
	private String message;
	private NotificationType type;
	private boolean stick;

	public SwingNotificationStrategyParameter(int duration, String title,
			String message, NotificationType type, boolean stick) {
		this.duration = duration;
		this.title = title;
		this.message = message;
		this.type = type;
		this.stick = stick;
	}

	public int getDuration() {
		return duration;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public NotificationType getType() {
		return type;
	}

	public boolean isStick() {
		return stick;
	}
}