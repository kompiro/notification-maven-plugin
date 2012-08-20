package org.kompiro.nortification.buildresult;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.maven.execution.BuildFailure;
import org.apache.maven.execution.BuildSuccess;
import org.apache.maven.execution.BuildSummary;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.project.MavenProject;
import org.kompiro.nortification.buildresult.strategy.NotificationType;

class NotifySessionEndedHandler implements InvocationHandler {
	private static final String METHOD_NAME_OF_SESSION_ENDED = "sessionEnded";
	private final ExecutionListener defaultExecutionListener;
	private final NotificationMojo mojo;

	NotifySessionEndedHandler(NotificationMojo mojo,
			ExecutionListener defaultExecutionListener) {
		this.defaultExecutionListener = defaultExecutionListener;
		this.mojo = mojo;
	}

	@Override
	public Object invoke(Object target, Method method, Object[] args)
			throws Throwable {
		if (isExecutedSessionEnded(method)) {
			MavenProject project = mojo.session.getResult().getProject();
			BuildSummary buildSummary = mojo.session.getResult().getBuildSummary(
					project);
			if (buildSummary instanceof BuildSuccess) {
				String title = "BUILD SUCCESS";
				String message = String.format("Project : %s\nTotal Time : %ss", project.getName(),
						buildSummary.getTime());
				mojo.showNotificationUI(title, message, NotificationType.SUCCESS);
			} else if (buildSummary instanceof BuildFailure) {
				BuildFailure failure = (BuildFailure) buildSummary;
				String title = "BUILD FAILUERE!";
				String message;
				if (failure.getCause() != null) {
					message = String.format("Project : %s\nTotal Time : %ss\n%s",
							project.getName(),
							buildSummary.getTime(), failure.getCause()
									.getLocalizedMessage());
				} else {
					message = String.format("Total Time : %ss",
							buildSummary.getTime());
				}
				mojo.showNotificationUI(title, message, NotificationType.ERROR);
			}
		}
		return method.invoke(defaultExecutionListener, args);
	}

	private boolean isExecutedSessionEnded(Method method) {
		return method.getName().equals(METHOD_NAME_OF_SESSION_ENDED);
	}
}