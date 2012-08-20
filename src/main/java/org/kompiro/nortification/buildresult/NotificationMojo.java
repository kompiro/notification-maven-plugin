package org.kompiro.nortification.buildresult;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Proxy;

import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.kompiro.nortification.buildresult.strategy.GrowlNotificationStrategy;
import org.kompiro.nortification.buildresult.strategy.NotificationStrategy;
import org.kompiro.nortification.buildresult.strategy.NotificationType;
import org.kompiro.nortification.buildresult.strategy.SwingNotificationStrategy;
import org.kompiro.nortification.buildresult.strategy.SwingNotificationStrategyParameter;

/**
 * Notify build result
 * 
 * @author <a href="kompiro@gmail.com">Hiroki Kondo</a>
 * @goal notify
 * @phase initialize
 * @requiresProject
 * @description notify build result to user.
 */
public class NotificationMojo extends AbstractMojo {

	/**
	 * The Maven Session Object
	 * 
	 * @parameter property="session"
	 * @required
	 * @readonly
	 */
	protected MavenSession session;

	/**
	 * Notification duration
	 * 
	 * @parameter property="maven.notification.duration" default-value=2000
	 */
	private Integer duration;

	/**
	 * Notification strategy<br>
	 * strategies:
	 * <ul>
	 * <li>swing : simple notification window
	 * <li>growl : use growlnotify (growl command line) notification
	 * </ul>
	 * 
	 * @parameter property="maven.notification.strategy" default-value=swing
	 */
	private String strategy;
	
	/**
	 * Stick notification information(only swing)<br>
	 * 
	 * @parameter property="maven.notification.stick" default-value=false
	 */
	private boolean stick;

	public void execute() throws MojoExecutionException {
		MavenExecutionRequest request = session.getRequest();
		ExecutionListener defaultExecutionListener = request
				.getExecutionListener();
		Object instance = Proxy.newProxyInstance(this.getClass()
				.getClassLoader(), new Class[] { ExecutionListener.class },
				new NotifySessionEndedHandler(this, defaultExecutionListener));
		request.setExecutionListener((ExecutionListener) instance);
	}

	void showNotificationUI(final String title, final String message,
			final NotificationType type) {
		NotificationStrategy notifyStrategy;
		if (strategy.equals("growl")) {
			notifyStrategy = new GrowlNotificationStrategy(getLog(), title,
					message);
		} else {
			notifyStrategy = new SwingNotificationStrategy(getLog(), new SwingNotificationStrategyParameter(duration, title, message,
					type, stick));
		}
		notifyStrategy.show();
	}
}
