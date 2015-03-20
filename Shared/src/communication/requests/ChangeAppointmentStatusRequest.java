package communication.requests;

public class ChangeAppointmentStatusRequest {
	private String username;
	private int groupID;
	
	private int appointmentID;
	private boolean isGroup;
	private String status;
	
	public ChangeAppointmentStatusRequest() {};
	
	public ChangeAppointmentStatusRequest(int appontmentID, String status, int groupID) {
		this.appointmentID = appontmentID;
		this.status = status;
		this.groupID = groupID;
		this.isGroup = true;
	}
	public String getStatus() {
		return status;
	}
	public ChangeAppointmentStatusRequest(int appontmentID,String status, String username) {
		this.appointmentID = appontmentID;
		this.username = username;
		this.status = status;
		this.isGroup = false;
	}
	public String getUsername() {
		return username;
	}
	public int getAppointmentID() {
		return appointmentID;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public int getGroupID() {
		return groupID;
	}
}
