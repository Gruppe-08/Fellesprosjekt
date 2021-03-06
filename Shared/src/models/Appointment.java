package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import util.DateUtil;

public class Appointment {
	private Integer id;
	private String title = "";
	private String description = "";
	private String location;
	private String startTime;
	private String endTime;
	private Integer roomId;
	private String ownerUsername;
	private HashMap<String, String> userRelations = new HashMap<String, String>();
	private HashMap<Integer, String> groupRelations = new HashMap<Integer, String>();

	
	public Appointment(Integer id, String title, String description, String location, String startTime, String endTime,
			Integer roomId, String ownerUsername) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomId = roomId;
		this.ownerUsername = ownerUsername;
	}

	public HashMap<String, String> getUserRelations() {
		return userRelations;
	}

	public void setUserRelations(HashMap<String, String> userRelations) {
		this.userRelations = userRelations;
	}

	public Appointment(String title, String description, String startTime, String endTime) {
		this.title = title;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public Appointment(){
		// Has to be present because of KryoNet.
	}
	

	public Appointment(Integer id, String title, String description, String location, String startTime, String endTime) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStartTime() {
		
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = DateUtil.serializeDateTime(startTime);
		System.out.println("Setting startTime");
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTime(LocalDateTime date) {
		this.startTime = DateUtil.serializeDateTime(date);
		
	}

	public Integer getRoomId() {
		return roomId;
	}


	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}


	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}
	
	@Override
	public String toString() {
		return "Appointment [id=" + id + ", title=" + title + ", description="
				+ description + ", location=" + location + ", startTime="
				+ startTime + ", endTime=" + endTime + ", roomId=" + roomId
				+ ", ownerUsername=" + ownerUsername + "]";
	}

	public HashMap<Integer, String> getGroupRelations() {
		return groupRelations;
	}

	public void setGroupRelations(HashMap<Integer, String> groupRelations) {
		this.groupRelations = groupRelations;
	}

}