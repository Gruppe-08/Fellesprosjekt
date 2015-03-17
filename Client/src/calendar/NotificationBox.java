package calendar;

import models.Notification;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class NotificationBox extends AnchorPane {
	
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
		Button decline = new Button("Decline");
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
