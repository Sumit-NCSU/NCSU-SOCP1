package services;

import javax.inject.Inject;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

/**
 * @author sriva
 *
 */
public class DatabaseExecutionContext extends CustomExecutionContext {

	@Inject
	public DatabaseExecutionContext(ActorSystem actorSystem) {
		// uses a custom thread pool defined in application.conf
		super(actorSystem, "play.db");
	}
}
