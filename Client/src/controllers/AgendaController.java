package controllers;

import java.util.ArrayList;

import calendar.AgendaPane;
import calendar.State;
import models.Appointment;
import models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import communication.requests.AppointmentRequest;
import communication.responses.AppointmentResponse;

public class AgendaController {
	@FXML
	Button add_appointment;
	
	@FXML
	VBox appointmentPane;
	
	ArrayList<Appointment> appointments = null;

	public void initialize() {
		AppointmentRequest request = new AppointmentRequest();
		User user = State.getUser();
		
		request.addUsername(user.getUsername());
		System.out.println("Sending appointment request");
		State.getConnectionController().sendTCP(request);
		
		AppointmentResponse response = (AppointmentResponse) State.getConnectionController().getObject("communication.responses.AppointmentResponse");
		appointments = response.getAppointments();
		
		AgendaPane pane;
		for (Appointment appointment : appointments) {
			pane = new AgendaPane(appointment);
			appointmentPane.getChildren().add(pane);
		}
	}
	
	@FXML
	void onAddAppointment(ActionEvent event){
		State.getWindowController().loadPage("createAppointment.fxml");
	}
	
	@FXML
	void onDeleteAppointment(ActionEvent event){
		
	}

}
