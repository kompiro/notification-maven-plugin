package org.kompiro.nortification.buildresult.strategy;

import java.awt.Color;
import java.net.URL;

public enum NotificationType {
	
	SUCCESS(new Color(204,255,153),NotificationType.class.getResource("weather_sun.png")),
	ERROR(new Color(255,153,204),NotificationType.class.getResource("weather_lightning.png")),
	INFO(new Color(255,255,153),NotificationType.class.getResource("weather_cloudy.png"));
	
	private NotificationType(Color color,URL iconURL){
		this.color = color;
		this.iconURL = iconURL;
	}
	
	private Color color;
	private URL iconURL;
	
	public Color getColor() {
		return color;
	}
	
	public URL getIconURL() {
		return iconURL;
	}
}
