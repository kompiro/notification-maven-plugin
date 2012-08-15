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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.kompiro.nortification.buildresult.strategy.GrowlNotificationStrategy;
import org.kompiro.nortification.buildresult.strategy.NotificationStrategy;
import org.kompiro.nortification.buildresult.strategy.SwingNotificationStrategy;
import org.kompiro.nortification.ui.NotificationType;

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

	private final class NotifySessionEndedHandler implements
			InvocationHandler {
		private static final String METHOD_NAME_OF_SESSION_ENDED = "sessionEnded";
		private final ExecutionListener defaultExecutionListener;

		private NotifySessionEndedHandler(
				ExecutionListener defaultExecutionListener) {
			this.defaultExecutionListener = defaultExecutionListener;
		}

		@Override
		public Object invoke(Object target, Method method, Object[] args)
				throws Throwable {
			if(isExecutedSessionEnded(method)){
				MavenProject project = session.getResult().getProject();
				BuildSummary buildSummary = session.getResult()
						.getBuildSummary(project);
				if (buildSummary instanceof BuildSuccess) {
					String title = "BUILD SUCCESS";
					String message = String.format(
							"Total Time : %ss", buildSummary.getTime());
					showNotificationUI(title, message,NotificationType.SUCCESS);
				} else if (buildSummary instanceof BuildFailure) {
					BuildFailure failure = (BuildFailure) buildSummary;
					String title = "BUILD FAILUERE!";
					String message;
					if(failure.getCause() != null){
						message = String.format(
								"Total Time : %ss\n%s", buildSummary.getTime(),
								failure.getCause().getLocalizedMessage());
					} else {
						message = String.format(
								"Total Time : %ss", buildSummary.getTime());
					}
					showNotificationUI(title, message,NotificationType.ERROR);
				}
			}
			return method.invoke(defaultExecutionListener, args);
		}

		private boolean isExecutedSessionEnded(Method method) {
			return method.getName().equals(METHOD_NAME_OF_SESSION_ENDED) ;
		}
	}

	/**
	 * The Maven Session Object
	 * 
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	protected MavenSession session;
	
    /**
     * Notification duration
     *
     * @parameter expression="${maven.notification.duration}" default-value=2000
     */
    private Integer duration;
    
    /**
     * Notification strategy<br>
     * strategies:
     * <ul>
     * 	<li>swing : simple notification window
     * 	<li>growl : use growlnotify (growl command line) notification
     * </ul>
     *
     * @parameter expression="${maven.notification.strategy}" default-value=swing
     */
    private String strategy;
    

	public void execute() throws MojoExecutionException {
		MavenExecutionRequest request = session.getRequest();
		ExecutionListener defaultExecutionListener = request.getExecutionListener();
		Object instance = Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
				new Class[]{ExecutionListener.class}, 
				new NotifySessionEndedHandler(defaultExecutionListener));
		request.setExecutionListener((ExecutionListener)instance);
	}

	private void showNotificationUI(final String title, final String message,final NotificationType type) {
		NotificationStrategy notifyStrategy;
		if(strategy.equals("growl")){
			notifyStrategy = new GrowlNotificationStrategy(getLog(),title,message);
		} else {
			notifyStrategy = new SwingNotificationStrategy(getLog(),duration,title,message,type);			
		}
		notifyStrategy.show();
	}
}
