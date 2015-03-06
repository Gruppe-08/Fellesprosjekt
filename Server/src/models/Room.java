package models;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Room {
	private Property<Number> roomId = new SimpleIntegerProperty();
	private Property<String> name = new SimpleStringProperty();
	private Property<Number> capacity = new SimpleIntegerProperty();
		
	public Room(int roomId, String name, int capacity) {
		setRoomId(roomId);
		setName(name);
		setCapacity(capacity);
	}
	
	public Room(){
		
	}
	
		
	public Integer getRoomId() {
		return roomId.getValue().intValue();
	}
	
	public void setRoomId(Integer room_id) {
		this.roomId.setValue(room_id);
	}
	
	public String getName() {
		return name.getValue();
	}
	
	public void setName(String name) {
		this.name.setValue(name);
	}
	

	public Integer getCapacity() {
		return capacity.getValue().intValue();
	}
	
	public void setCapacity(Integer capacity) {
		this.capacity.setValue(capacity);		
	}

}
