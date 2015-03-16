package calendar;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import communication.requests.NotificationRequest;
import communication.responses.NotificationResponse;
import models.Notification;
import controllers.ConnectionController;
import controllers.WindowController;

public class NotificationService extends ScheduledService {
	
	ConnectionController conn;
	WindowController controller;
	
	public NotificationService(ConnectionController conn, WindowController controller) {
		this.conn = conn;
		this.controller = controller;
		this.setPeriod(new Duration(10000));
		this.setDelay(new Duration(1000));

	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() {
				conn.sendTCP(new NotificationRequest());
				NotificationResponse response = (NotificationResponse)conn.getObject("communication.responses.NotificationResponse");
				for (Notification notification: response.getNotifications()) {
					Platform.runLater(new Runnable () {
						@Override
						public void run() {
							controller.addNotification(notification);
						}
					});
				}
				return null;
			}	
		};		
	}
}