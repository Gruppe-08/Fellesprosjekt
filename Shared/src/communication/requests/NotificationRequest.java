package communication.requests;

public class NotificationRequest {
	
	String type = "";
	int read = -1;
	int status = -1;	//1 means that appointment was accepted, 0 declined and -1 means that the invitation is still pending
	int notificationId;
	
	public NotificationRequest(){
		
	}
	public String getType() {
		return type;
	}
	
	public int getReadId() {
		return read;
	}
	
	public int getStatus() {
		return status;
	}
	
	public int getNotificationId() {
		return notificationId;
	}
	
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	
	public void setRead(int readId) {
		this.read = readId;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
}
