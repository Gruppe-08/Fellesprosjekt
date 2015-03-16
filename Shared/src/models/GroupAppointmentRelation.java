package models;

public class GroupAppointmentRelation {
	private int groupID;
	private int appointmentID;
	
	GroupAppointmentRelation(){}
	
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	public int getAppointmentID() {
		return appointmentID;
	}
	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}
}
