package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import models.LocationData;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.DatabaseService;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	@Inject
	DatabaseService databaseService;

	/**
	 * An action that renders an HTML page with a welcome message. The configuration
	 * in the <code>routes</code> file means that this method will be called when
	 * the application receives a <code>GET</code> request with a path of
	 * <code>/</code>.
	 */
	public Result index() {
		return ok(views.html.index.render());
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result handleupdates() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest("Expecting Json data");
		} else {
			String name = json.findPath("username").textValue();
			long timestamp = json.findPath("timestamp").longValue();
			double latitude = json.findPath("latitude").doubleValue();
			double longitude = json.findPath("longitude").doubleValue();
			LocationData currentLocation = new LocationData(name, timestamp, latitude, longitude);
			// save to DB.
			double distance = databaseService.updateLocation(currentLocation);
			ObjectNode result = Json.newObject();
			result.put("name", name);
			result.put("distance", distance);
			if (name == null) {
				result.removeAll();
				result.put("status", "Missing parameter [name]");
				return badRequest(result);
			} else {
				return ok(result);
			}
		}
	}

}
