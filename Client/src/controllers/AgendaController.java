package controllers;

import java.net.URL;
import java.util.ArrayList;

import calendar.AgendaPane;
import calendar.State;
import models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import communication.requests.AppointmentRequest;
import communication.responses.AppointmentResponse;

public class AgendaController {
	@FXML
	Button add_appointment;
	
	@FXML
	VBox appointmentPane;
	
	ArrayList<String> appointments = null;

	public void initialize() {
		AppointmentRequest request = new AppointmentRequest();
		User user = State.getUser();
		
		request.addUsername(user.getUsername());
		State.getConnectionController().sendTCP(request);
		
		
		AppointmentResponse response = (AppointmentResponse) State.getConnectionController().getObject("communication.responses.AppointmentResponse");
		//appointments = response.getAppointments();
		
		AgendaPane pane;
		for (String appointment : appointments) {
			pane = new AgendaPane(appointment);
			appointmentPane.getChildren().add(pane);
		}
		
		//AgendaPane pane = new AgendaPane();
		
		
		
		
		/*add_appointment.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.out.println("TODO: add appointment");
			}
		}); */
	}

}
