package calendar;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import models.Appointment;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class AppointmentPane extends Pane {
	
	private Appointment appointment;
	
	
	public AppointmentPane(Appointment appointment) {
		this.appointment = appointment;
		this.setPrefWidth(300);
		this.setPrefHeight(calculateHeight());
		this.setStyle("-fx-background-color: #0078FF");
		this.putText();		
	}
	
	private int calculateHeight() {
		LocalDateTime startTime = util.DateUtil.deserialize(this.appointment.getStartTime());
		LocalDateTime endTime = util.DateUtil.deserialize(this.appointment.getEndTime());
		Long diff = startTime.until(endTime, ChronoUnit.MINUTES);
		AnchorPane.setLeftAnchor(this, 50.0);
		int height = (int)(0.85 * diff) + 1;
		return height;
	}
	
	private double calculateTextPlacement() {
		double height = this.getHeight();
		if (height >= 30.0) {
			return 24.0;
		}
		else {
			return height * 0.4;
		}
	}
	
	private void putText() {
		Text titleText = new Text(appointment.getTitle());
		titleText.setStyle("-fx-font-family: 'Helvetica';");
		titleText.setStyle("-fx-font-size: 15px;");
		titleText.setStyle("-fx-font-style: italic");
		titleText.setStyle("-fx-font-weight: lighter");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");		
		this.getChildren().addAll(titleText);
	}
	
	@Override
	protected void layoutChildren() {
		Text titleText = (Text)this.getChildren().get(0);
		titleText.setLayoutX(10.0);
		titleText.setLayoutY(calculateTextPlacement());
	}
}
