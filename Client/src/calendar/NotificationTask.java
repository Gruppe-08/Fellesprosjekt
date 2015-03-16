package calendar;

import java.util.ArrayList;

import models.Notification;
import communication.requests.NotificationRequest;
import communication.responses.NotificationResponse;
import controllers.ConnectionController;
import javafx.concurrent.Task;

public class NotificationTask extends Task {
	
	ConnectionController conn;
	
	public NotificationTask(ConnectionController conn) {
		this.conn = conn;
	}

	@Override
	protected ArrayList<Notification> call() throws Exception {
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		conn.sendTCP(new NotificationRequest());
		NotificationResponse response = (NotificationResponse)conn.getObject("communication.responses.NotificationResponse");
		for (Notification notification: response.getNotifications()) {
			notifications.add(notification);
		}
		return notifications;
	}
}
