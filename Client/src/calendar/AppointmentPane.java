package calendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import models.Appointment;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AppointmentPane extends VBox {
	
	private Appointment appointment;
	
	
	public AppointmentPane(Appointment appointment) {
		this.appointment = appointment;
		this.setPrefWidth(300);
		this.setPrefHeight(calculateHeight());
		this.setPadding(new Insets(5,5,5,5));
		this.setStyle("-fx-background-color: #4ca1ff");
		this.putText();
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#38597F", 1.0));
		this.setEffect(dropShadow);
	}
	
	private int calculateHeight() {
		LocalDateTime startTime = util.DateUtil.deserialize(this.appointment.getStartTime());
		LocalDateTime endTime = util.DateUtil.deserialize(this.appointment.getEndTime());
		Long diff = startTime.until(endTime, ChronoUnit.MINUTES);
		AnchorPane.setLeftAnchor(this, 75.0);
		int height = (int)(0.85 * diff) + 1;
		return height;
	}
	
	private void putText() {
		Text titleText = new Text(appointment.getTitle());
		titleText.setFont(Font.font("Helvetica", FontWeight.THIN, 20));
		this.getChildren().addAll(titleText);
	}
}
