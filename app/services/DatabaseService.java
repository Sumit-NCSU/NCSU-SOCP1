package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;

import models.LocationData;
import play.db.Database;
import util.DistanceUtil;

public class DatabaseService {
	private Database db;
	private DatabaseExecutionContext executionContext;

	@Inject
	public DatabaseService(Database db, DatabaseExecutionContext executionContext) {
		this.db = db;
		this.executionContext = executionContext;
	}

	// public CompletionStage<Void> updateLocation(LocationData currentLocation) {
	public double updateLocation(LocationData currentLocation) {
		// return CompletableFuture.runAsync(() -> {
		Connection con = db.getConnection();
		String sql = "CREATE TABLE IF NOT EXISTS location (id integer PRIMARY KEY, name text NOT NULL, latitude real, longitude real, timestamp integer)";
		try (Statement stmt = con.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		insertRecord(currentLocation, con);
		LocationData initialLocation = getStartLocation(currentLocation.getName(), con);
		if (initialLocation == null) {
			return 0;
		} else {
			double d = DistanceUtil.findDistance(initialLocation.getLatitude(), initialLocation.getLongitude(),
					currentLocation.getLatitude(), currentLocation.getLongitude());
			return d;
		}
		// }, executionContext);
	}

	private void insertRecord(LocationData currentLocation, Connection con) {
		String sql = "INSERT INTO location (name, latitude, longitude, timestamp) VALUES(?,?,?,?)";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, currentLocation.getName());
			pstmt.setDouble(2, currentLocation.getLatitude());
			pstmt.setDouble(3, currentLocation.getLongitude());
			pstmt.setLong(4, currentLocation.getTimestamp());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private LocationData getStartLocation(String name, Connection con) {
		String sql = "SELECT name, timestamp, latitude, longitude FROM location WHERE name = ? order by id limit 1";
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			LocationData location = new LocationData();
			while (rs.next()) {
				location.setName(rs.getString("name"));
				location.setTimestamp(rs.getLong("timestamp"));
				location.setLatitude(rs.getDouble("latitude"));
				location.setLongitude(rs.getDouble("longitude"));
			}
			return location;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}