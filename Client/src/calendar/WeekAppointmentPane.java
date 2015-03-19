package calendar;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sun.xml.internal.bind.v2.runtime.Location;

import controllers.AppointmentController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.Appointment;

public class WeekAppointmentPane extends VBox {
	
	private Appointment appointment;

	
	public WeekAppointmentPane(Appointment appointment) {
		this.appointment = appointment;
		this.setPadding(new Insets(10,10,10,10));
		this.setStyle("-fx-background-color: rgba(107, 211, 255, 0.3); -fx-background-radius: 13;");
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#38597F", 0.2));
		this.setEffect(dropShadow);
		int height = calculateHeight();
		this.setPrefHeight(height);
		putText();
		
		this.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				AppointmentController controller = new AppointmentController(appointment);	
				State.getWindowController().loadPage(Window.APPOINTMENT, controller);
			}
		});
 	}
	//TODO: Find a way to format the text that allows for correct scaling of appointments
	//Tempfix: small font size
	private void putText() {
		Label titleText = new Label(appointment.getTitle());
		titleText.setWrapText(true);
		titleText.setStyle("-fx-text-fill: #1d93c6");
		titleText.setFont(Font.font("Helvetica Neue", FontWeight.THIN, 13));
		this.getChildren().add(titleText);

	}
		
	private int calculateHeight() {
		LocalDateTime startTime = util.DateUtil.deserializeDateTime(this.appointment.getStartTime());
		LocalDateTime endTime = util.DateUtil.deserializeDateTime(this.appointment.getEndTime());
		Long diff = startTime.until(endTime, ChronoUnit.MINUTES);
		int height = (int)(0.85 * diff) + 1;
		return height;
	}
}
