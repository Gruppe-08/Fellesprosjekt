package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import calendar.AgendaPane;
import calendar.State;
import models.Appointment;
import models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import communication.requests.AppointmentRequest;
import communication.requests.AuthenticationRequest;
import communication.responses.AppointmentResponse;
import communication.responses.AuthenticationResponse;

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
<<<<<<< Updated upstream
		//appointments = response.getAppointments();
=======
		System.out.println("Recieved appointment response");
		appointments = response.getAppointments();
		System.out.println(appointments.toString());
		
>>>>>>> Stashed changes
		
		AgendaPane pane;
		for (Appointment appointment : appointments) {
			System.out.println(appointment.getTitle());
			pane = new AgendaPane(appointment);
			appointmentPane.getChildren().add(pane);
		}

		add_appointment.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				Appointment a = new Appointment();
				a.setTitle("Test");
				a.setDescription("Test");
				a.setStartTime(null);
				a.setEndTime(null);
				
				appointmentPane.getChildren().add(new AgendaPane(a));
			}
		});
	}

}
