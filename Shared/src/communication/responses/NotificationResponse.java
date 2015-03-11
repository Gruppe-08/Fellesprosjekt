package communication.responses;

import java.util.List;

import models.Notification;

public class NotificationResponse {
	private List<Notification> notifications;
	
	public NotificationResponse() { }
	
	public NotificationResponse(List<Notification> notifications){
		this.notifications = notifications;
	}

}
