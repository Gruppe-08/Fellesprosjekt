package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import communication.requests.NotificationRequest;
import communication.responses.NotificationResponse;
import models.Notification;
import calendar.NotificationBox;
import calendar.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class NotificationViewController implements Initializable {
	
	@FXML AnchorPane notificationPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		for (Notification notification : getReadNotifications()) {
			addNotificationBox(notification);
		}
		
		while (true) {
			Notification notification = State.getWindowController().getNotification();
			if (notification == null) {
				break;
			}
			addNotificationBox(notification);
			notification.setRead(1);
			notificationWasRead(notification);
		}
	}
	
	private ArrayList<Notification> getReadNotifications() {
		ArrayList<Notification> readNotifications = new ArrayList<Notification>();
		NotificationRequest req = new NotificationRequest();
		State.getConnectionController().sendTCP(req);
		NotificationResponse response = (NotificationResponse)State.getConnectionController().getObject("communication.responses.NotificationResponse");
		for (Notification notification : response.getNotifications()) {
			if (notification.isRead() == 1) {
				readNotifications.add(notification);
			}
		}
		return readNotifications;
	}
	
	private void addNotificationBox(Notification notification) {
		for (Node node : notificationPane.getChildren()) {
			AnchorPane.setTopAnchor(node, AnchorPane.getTopAnchor(node) + 100.0);
		}
		NotificationBox notBox = new NotificationBox(notification);
		AnchorPane.setTopAnchor(notBox, 0.0);
		notificationPane.getChildren().add(notBox);
	}
	
	//Should gather all notifications in one request
	private void notificationWasRead(Notification notification) {
		NotificationRequest req = new NotificationRequest();
		req.setReadId(notification.getId());
		State.getConnectionController().sendTCP(req);
	}
}
