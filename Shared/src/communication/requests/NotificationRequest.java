package communication.requests;

public class NotificationRequest {
	
	int readId = -1;	
	int status = -1;	//1 means that appointment was accepted, 0 declined and -1 means that the invitation is still pending
	
	public NotificationRequest(){
		
	}
	
	public int getReadId() {
		return readId;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setReadId(int readId) {
		this.readId = readId;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
}
