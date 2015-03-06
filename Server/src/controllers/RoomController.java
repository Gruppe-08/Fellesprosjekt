package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.DatabaseConnector;
import models.Room;

public class RoomController {
	private static Connection db;
	private static Statement statement;

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
		while (resultSet.next()) {
			room.setRoomId(resultSet.getInt("room_id"));
			room.setName(resultSet.getString("name"));
			room.setCapacity(resultSet.getInt("capacity"));
		}
		return room;
	}

	public static void deleteRoom(int roomId){
		try {
			db = DatabaseConnector.getDB();
			statement = db.createStatement();

			String deleteRoom = String.format("DELETE FROM Room WHERE room_id = " + roomId);
			System.out.println("query: " + deleteRoom);
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

