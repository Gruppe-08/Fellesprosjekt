package calendar;

import controllers.NotificationViewController;
import models.Notification;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class NotificationBox extends AnchorPane {
	
	private NotificationBox box = this;
	private Notification notification;
	
	public NotificationBox(Notification notification) {
		this.notification = notification;
		this.setPrefHeight(100);
		this.setPrefWidth(801);
		putText();
		style();
		putButtons();
	}
	
	private void putButtons() {
		
		Button confirm = new Button("Confirm");
		confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				NotificationViewController.respondedToNotification(notification, 1, box);
			}
			
		});
		Button decline = new Button("Decline");

		decline.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				NotificationViewController.respondedToNotification(notification, 0, box);
			}
		});
		
		AnchorPane.setTopAnchor(confirm, 40.0);
		AnchorPane.setTopAnchor(decline, 40.0);
		AnchorPane.setRightAnchor(confirm, 150.0);
		AnchorPane.setRightAnchor(decline, 50.0);
		confirm.setStyle("-fx-background-color: white");
		decline.setStyle("-fx-background-color: white");
		this.getChildren().addAll(confirm, decline);
	}
	
	private void style() {
		this.setStyle("-fx-background-color: cyan");
	}
	
	private void putText() {
		Text msgText = new Text(notification.getMessage());
		msgText.setFont(Font.font("Helvetica Neue", FontPosture.REGULAR, 20));
		Text createdText = new Text("Created: " + notification.getCreated());
		createdText.setFont(Font.font("Helvetica Neue", FontPosture.REGULAR, 20));
		AnchorPane.setLeftAnchor(msgText, 20.0);
		AnchorPane.setLeftAnchor(createdText, 20.0);
		AnchorPane.setTopAnchor(msgText, 20.0);
		AnchorPane.setTopAnchor(createdText, 60.0);
		this.getChildren().addAll(msgText, createdText);
	}

}
