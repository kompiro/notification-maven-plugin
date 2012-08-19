package org.kompiro.nortification.buildresult.strategy;

import java.awt.Color;

public enum NotificationType {
	
	SUCCESS(new Color(204,255,153)),ERROR(new Color(255,153,204)),INFO(new Color(255,255,153));
	
	private NotificationType(Color color){
		this.color = color;
	}
	
	private Color color;
	
	public Color getColor() {
		return color;
	}
}
