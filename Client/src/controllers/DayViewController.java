package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import communication.requests.AppointmentRequest;
import communication.responses.AppointmentResponse;
import calendar.AppointmentPane;
import calendar.State;
import models.Appointment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class DayViewController implements Initializable{
	
	@FXML private AnchorPane dayPane;	
	@FXML private Button getButton;
	
	
	public void initialize(URL location, ResourceBundle resources) {
		
		getButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("handled button press");
				ArrayList<Appointment> appointments = getAppointments();
				for (Appointment appointment : appointments) {
					AppointmentPane appPane = new AppointmentPane(appointment);
					dayPane.getChildren().add(appPane);
					AnchorPane.setTopAnchor(appPane, /*calculateAppointmentPlacement(appointment.getStartTime())*/ 600.0);
					System.out.println("added appointment");
				}
			}
		});	
	}
	
	public double calculateAppointmentPlacement(String startTime) {
		String hourandminutes = startTime.substring(11);
		System.out.println(hourandminutes);
		String[] time = hourandminutes.split(":");
		int hour = Integer.valueOf(time[0]);
		int minutes = Integer.valueOf(time[1]);
		double pixels = (hour * 50 + minutes * 0.833);
		return pixels;
	}
	
	public ArrayList<Appointment> getAppointments() {
		AppointmentRequest req = new AppointmentRequest();
		req.addUsername(State.getUser().getUsername());
		State.getConnectionController().sendTCP(req);
		AppointmentResponse resp = (AppointmentResponse)State.getConnectionController().getObject("communication.responses.AppointmentResponse");
		return resp.getAppointments();
	}
	

}
