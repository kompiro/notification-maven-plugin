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

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.kompiro.nortification.ui.NotificationType;
import org.kompiro.nortification.ui.NotificationWindow;

/**
 * Notify build result
 * 
 * @goal notify
 * @phase initialize
 * @requiresProject
 * @description notify build result to user.
 */
public class NotificationMojo extends AbstractMojo {
	/**
	 * The Maven Session Object
	 * 
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	protected MavenSession session;

	public void execute() throws MojoExecutionException {
		session.getRequest().setExecutionListener(
				new AbstractExecutionListener() {
					@Override
					public void sessionEnded(ExecutionEvent event) {
						final MavenProject project = session.getResult().getProject();
						BuildSummary buildSummary = session.getResult()
								.getBuildSummary(project);
						if (buildSummary instanceof BuildSuccess) {
							String title = "Build Success!";
							String message = String.format(
									"Time : '%s's", buildSummary.getTime());
							showNotificationUI(title, message,NotificationType.SUCCESS);
						} else if (buildSummary instanceof BuildFailure) {
							String title = "Build Failure!";
							String message = String.format(
									"Time : '%s's", buildSummary.getTime());
							showNotificationUI(title, message,NotificationType.ERROR);
							
						}
					}
				});
	}

	private void showNotificationUI(final String title, final String message,final NotificationType type) {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					final NotificationWindow window = new NotificationWindow();
					window.setLocation(0, -10000); // hide initialization window
					window.setTitle(title);
					window.setMessage(message);
					window.setDuration(2000);
					window.setType(type);
					window.pack();
					window.notifyUI();
				}
			});
		} catch (InterruptedException e) {
			getLog().error(e);
		} catch (InvocationTargetException e) {
			getLog().error(e);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			getLog().error(e);
		}
	}
}
