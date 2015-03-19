package calendar;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import controllers.AppointmentController;
import models.Appointment;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AppointmentPane extends VBox {
	
	private Appointment appointment;
	
	
	public AppointmentPane(Appointment appointment) {
		this.appointment = appointment;
		this.setPrefWidth(900);
		this.setPrefHeight(calculateHeight());
		this.setPadding(new Insets(5,5,5,5));
		this.setStyle("-fx-background-color: rgba(107, 211, 255, 0.3); -fx-background-radius: 13;");
		this.putText();
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#38597F", 0.2));
		this.setEffect(dropShadow);
		
		this.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				AppointmentController controller = new AppointmentController(appointment);	
				State.getWindowController().loadPage(Window.APPOINTMENT, controller);

			}
		});
	}
	
	private int calculateHeight() {
		LocalDateTime startTime = util.DateUtil.deserializeDateTime(this.appointment.getStartTime());
		LocalDateTime endTime = util.DateUtil.deserializeDateTime(this.appointment.getEndTime());
		Long diff = startTime.until(endTime, ChronoUnit.MINUTES);
		AnchorPane.setLeftAnchor(this, 75.0);
		int height = (int)(0.85 * diff) + 1;
		return height;
	}
	
	private void putText() {
		Label titleText = new Label(appointment.getTitle());
		titleText.setWrapText(true);
		titleText.setStyle("-fx-text-fill: #1d93c6");
		titleText.setFont(Font.font("Helvetica Neue", FontWeight.THIN, 16));
		this.getChildren().add(titleText);
	}
}
