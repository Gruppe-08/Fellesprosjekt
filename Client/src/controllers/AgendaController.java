package controllers;

import java.util.ArrayList;

import calendar.AgendaPane;
import calendar.State;
import calendar.Window;
import models.Appointment;
import models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import communication.requests.AppointmentRequest;
import communication.responses.AppointmentResponse;

public class AgendaController {
	@FXML
	Button add_appointment;
	
	@FXML
	ScrollPane scrollPane;
	
	@FXML 
	AnchorPane appointmentPane;
 
	
	ArrayList<Appointment> appointments = null;

	public void initialize() {
		
		add_appointment.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				State.getWindowController().loadPage(Window.APPOINTMENT, new AppointmentController());
			}
		});
		scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #ffffff");
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		AppointmentRequest request = new AppointmentRequest();
		appointmentPane.setPrefHeight(5.0);
		User user = State.getUser();		
	
		request.addUsername(user.getUsername());
		System.out.println("Sending appointment request");
		State.getConnectionController().sendTCP(request);
		
		AppointmentResponse response = (AppointmentResponse) State.getConnectionController().getObject("communication.responses.AppointmentResponse");
		appointments = response.getAppointments();
		
		AgendaPane pane;
		double topAnchor = 5.0;
		for (Appointment appointment : appointments) {
			pane = new AgendaPane(appointment);
			AnchorPane.setLeftAnchor(pane, 5.0);
			AnchorPane.setRightAnchor(pane, 5.0);
			AnchorPane.setTopAnchor(pane, topAnchor);
			appointmentPane.getChildren().add(pane);
			topAnchor += 120.0;
			appointmentPane.setPrefHeight(appointmentPane.getPrefHeight() + 120.0);
		}
	}
}
