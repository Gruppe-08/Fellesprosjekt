package communication.requests;

public class NotificationRequest {
	
	String type = "";
	int read = -1;
	String status = null;
	int appointmentId;
	int notificationId;
	
	
	public NotificationRequest(){
		
	}
	public String getType() {
		return type;
	}
	
	public int getReadId() {
		return read;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getAppointmentId() {
		return appointmentId;
	}
	
	public int getNotificationId() {
		return notificationId;
	}
	
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	
	public void setRead(int readId) {
		this.read = readId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
