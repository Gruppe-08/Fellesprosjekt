package communication.responses;

import java.util.ArrayList;

import models.Room;

public class RoomResponse extends BaseResponse {
	ArrayList<Room> rooms = new ArrayList<Room>();

	public void addRoom(Room room) { rooms.add(room); }
	
	public ArrayList<Room> getRooms() { return rooms; }

	public void setRooms(ArrayList<Room> rooms) { this.rooms = rooms; }
}
