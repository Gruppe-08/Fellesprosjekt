package models;

import java.util.ArrayList;

public class Appointment {
	private Integer id;
	private String title = "";
	private String description = "";
	private String location;
	private String startTime;
	private String endTime;
	private Integer roomId;
	private String ownerUsername;
	private ArrayList<UserAppointmentRelation> userRelations = new ArrayList<UserAppointmentRelation>();
	private ArrayList<GroupAppointmentRelation> groupRelations = new ArrayList<GroupAppointmentRelation>();
	
	
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
	
	public ArrayList<UserAppointmentRelation> getUserRelations() {
		return userRelations;
	}

	public void setUserRelations(ArrayList<UserAppointmentRelation> userRelations) {
		this.userRelations = userRelations;
	}

	public ArrayList<GroupAppointmentRelation> getGroupRelations() {
		return groupRelations;
	}

	public void setGroupRelations(ArrayList<GroupAppointmentRelation> groupRelations) {
		this.groupRelations = groupRelations;
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

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

}