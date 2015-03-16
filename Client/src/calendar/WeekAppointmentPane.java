package calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import communication.requests.AppointmentRequest;
import communication.responses.AppointmentResponse;
import controllers.CreateAppointmentController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.Appointment;

public class WeekAppointmentPane extends VBox {
	
	Appointment appointment;
	
	public WeekAppointmentPane(Appointment appointment) {
		this.appointment = appointment;
		this.setPadding(new Insets(5,5,5,5));
		this.setStyle("-fx-background-color: #A7F0F0");
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#38597F", 0.2));
		this.setEffect(dropShadow);
		this.setPrefHeight(calculateHeight());
		putText();
		
		this.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				CreateAppointmentController controller = new CreateAppointmentController(appointment);	
				State.getWindowController().loadPage(Window.APPOINTMENT, controller);
			}
		});
 	}
	//TODO: Find a way to format the text that allows for correct scaling of appointments
	//Tempfix: small font size
	private void putText() {
		Text titleText = new Text(appointment.getTitle());
		titleText.setFont(Font.font("Helvetica", FontWeight.THIN, 10));
		this.getChildren().add(titleText);
	}
		
	private int calculateHeight() {
		LocalDateTime startTime = util.DateUtil.deserializeDateTime(this.appointment.getStartTime());
		LocalDateTime endTime = util.DateUtil.deserializeDateTime(this.appointment.getEndTime());
		Long diff = startTime.until(endTime, ChronoUnit.MINUTES);
		int height = (int)(0.85 * diff) + 1;
		return height;
	}
	
	public void onMouseClicked(){
		System.out.println("Test");
		
	}

}
