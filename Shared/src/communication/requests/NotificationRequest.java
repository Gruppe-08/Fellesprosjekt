package communication.requests;

public class NotificationRequest {
	
	Boolean read = false;
	int appointmentId;
	int notificationId;
	
	public NotificationRequest(){
		
	}
	
	public Boolean isRead() {
		return read;
	}
	
	public void setRead(Boolean read) {
		this.read = read;
	}
	
	public int getAppointmentId() {
		return appointmentId;
	}
	
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public int getNotificationId() {
		return notificationId;
	}
	
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
}
