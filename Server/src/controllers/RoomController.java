package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

import communication.requests.GetRoomsRequest;
import communication.responses.RoomResponse;
import server.DatabaseConnector;
import util.DateUtil;
import models.Room;

public class RoomController {
	private static Connection db;
	private static Statement statement;
	
	public static RoomResponse handleGetRoomsRequest(GetRoomsRequest request) {
		RoomResponse response = new RoomResponse();
		
		if(request.getOnlyAvailable()) {
			response.setRooms(getRoomsAvailableBetween(request.getFromTime(), request.getToTime()));
		}
		else
			response.setRooms(getRooms());
		
		return response;
	}
	
	public static ArrayList<Room> getRooms() {
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		try {
			db = DatabaseConnector.getDB();
			statement = db.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM Room");
			while(res.next()) {
				rooms.add(parseResultSetToRoom(res));
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rooms;
	}
	
	public static ArrayList<Room> getRoomsAvailableBetween(String from, String to) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		try {
			db = DatabaseConnector.getDB();
			statement = db.createStatement();
			String subquery = "SELECT a.room_id FROM Appointment a WHERE (" +
					"a.start_date BETWEEN '" + from + "' AND '"+ to  +
					"' OR a.end_date BETWEEN '" + from  + "' AND '"+ to  + "')" +
					"AND a.room_id IS NOT NULL GROUP BY a.room_id";
			String query = "SELECT * FROM Room r WHERE r.room_id NOT IN (" + subquery + ")";

			ResultSet res = statement.executeQuery(query);
			
			while(res.next()) {
				rooms.add(parseResultSetToRoom(res));
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rooms;
	}

	public static Room getRoom(int roomId){
		Room room = null;

		try{
			db = DatabaseConnector.getDB();
			statement = db.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Room WHERE room_id = " + roomId);
			room = parseResultSetToRoom(resultSet);

		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return room;
	}


	public static void addRoom(Room room){
		try {
			db = DatabaseConnector.getDB();
			statement = db.createStatement();

			String insertRoom = String.format(
					"INSERT INTO Room(name, capacity) "
							+ "VALUES ('%s', %s)", 
							room.getName(),
							room.getCapacity().toString() 	

					);

			statement.execute(insertRoom);
		} catch(Exception e) {
			System.out.println(e);
		}

	}


	public static Room parseResultSetToRoom(ResultSet resultSet) throws SQLException{
		Room room = new Room();
		room.setRoomId(resultSet.getInt("room_id"));
		room.setName(resultSet.getString("name"));
		room.setCapacity(resultSet.getInt("capacity"));
		return room;
	}

	public static void deleteRoom(int roomId){
		try {
			db = DatabaseConnector.getDB();
			statement = db.createStatement();

			String deleteRoom = String.format("DELETE FROM Room WHERE room_id = " + roomId);
			statement.execute(deleteRoom);
		}

		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void updateRoom(Room room){
		try {
			db = DatabaseConnector.getDB();
			statement = db.createStatement();
			String updateRoom = String.format("UPDATE Room SET room_id ='%s', name ='%s', capacity='%s' WHERE room_id='%s'", room.getRoomId(), room.getName(), room.getCapacity());
			statement.execute(updateRoom);
		}
		catch(Exception e) {
			System.out.println(e);
			
		}
	}

}

