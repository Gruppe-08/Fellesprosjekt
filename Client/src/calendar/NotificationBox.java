package calendar;

import models.Notification;
import models.NotificationType;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NotificationBox extends AnchorPane {
	
	private Notification notification;
	private Label statusText;
	
	public NotificationBox(Notification notification) {
		this.notification = notification;
		this.setPrefHeight(100);
		this.setPrefWidth(801);
		putText();
		this.setStyle("-fx-background-color: rgba(107, 211, 255, 0.3); -fx-background-radius: 13;");
		if (notification.getType() == NotificationType.APPOINTMENT) {
			showStatus(getStatus());
		}
		
		this.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				State.openAppointmentView(this, notification.getAppointment());
			}
		});
	}

	private void putText() {
		Label msgText = new Label(notification.getMessage());
		msgText.setStyle("-fx-text-fill: #1d93c6");
		msgText.setFont(Font.font("Helvetica Neue", FontWeight.THIN, 16));
		Label createdText = new Label("Created: " + notification.getCreated());
		createdText.setStyle("-fx-text-fill: #1d93c6");
		createdText.setFont(Font.font("Helvetica Neue", FontWeight.THIN, 16));
		AnchorPane.setLeftAnchor(msgText, 20.0);
		AnchorPane.setLeftAnchor(createdText, 20.0);
		AnchorPane.setTopAnchor(msgText, 20.0);
		AnchorPane.setTopAnchor(createdText, 60.0);
		this.getChildren().addAll(msgText, createdText);
	}
	
	public void showStatus(String status) {
		String statusString = null;
		String color = null;
		switch(status) {
			case "pending": 
				statusString = "Pending";
				color = "yellow";
				break;
			case "not_attending": 
				statusString = "Not attending";
				color = "#cd5e51";
				break;
			case "attending":
				statusString = "Attending";
				color = "#1dc69d";
				break;
		}
		Label statusText = new Label(statusString);
		statusText.setFont(Font.font("Helvetica Neue", FontWeight.THIN, 16));
		statusText.setStyle("-fx-text-fill: " + color);

		AnchorPane.setRightAnchor(statusText, 40.0);
		AnchorPane.setTopAnchor(statusText, 34.0);
		if (this.statusText != null) {
			this.getChildren().remove(this.statusText);
		}
		this.getChildren().add(statusText);
		this.statusText = statusText;
	}
	
	public String getStatus() {
		return notification.getStatus();
	}

}
