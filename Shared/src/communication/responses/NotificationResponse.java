package communication.responses;

import java.util.ArrayList;
import models.Notification;

public class NotificationResponse {
	private ArrayList<Notification> notifications;
	
	public NotificationResponse() { }
	
	public NotificationResponse(ArrayList<Notification> notifications){
		this.notifications = notifications;
	}
	
	public void setNotifications(ArrayList<Notification> notifications){
		this.notifications = notifications;
	}
	
	public ArrayList<Notification> getNotifications(){
		return notifications;
	}
}
