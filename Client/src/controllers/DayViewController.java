package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import communication.requests.AppointmentRequest;
import communication.responses.AppointmentResponse;
import calendar.AppointmentPane;
import calendar.State;
import models.Appointment;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class DayViewController implements Initializable{
	
	@FXML private AnchorPane dayPane;	
	@FXML private Text dayLabel;
	@FXML private Button prevDay;
	@FXML private Button nextDay;
	private String currentDay;
	
	Map<String, ArrayList<Appointment>> cachedAppointments = new HashMap<String, ArrayList<Appointment>>();
	private final static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static ArrayList<Node> blankDayPane = new ArrayList<Node>();
	

	
	public void initialize(URL location, ResourceBundle resources) {
		
		//Necessary for concurrency, take a look at how the use of runLater can be avoided
		Platform.runLater(new Runnable() {		
			@Override public void run() {
				
				//Saves a blank dayPane as a template to use when changing dates
				for (Node child : dayPane.getChildren()) {
					blankDayPane.add(child);
				}
				currentDay = getToday();
				dayLabel.setText(currentDay);	//Needs more beautiful text 
				
				//Caches the appointments
				for (Appointment appointment : getAppointments()) {
					if (cachedAppointments.get(appointment.getStartTime().substring(0, 10)) == null) {
						cachedAppointments.put(appointment.getStartTime().substring(0, 10), new ArrayList<Appointment>());
					}
					cachedAppointments.get(appointment.getStartTime().substring(0, 10)).add(appointment);
				}
				addAppointmentsForNewDay();
			}
		});
		
		prevDay.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				decrementDay();
				dayPane.getChildren().retainAll(blankDayPane);
				addAppointmentsForNewDay();
			}
		});
		
		nextDay.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				incrementDay();
				dayPane.getChildren().retainAll(blankDayPane);
				addAppointmentsForNewDay();
			}
		});
		
	}
	
	private void addAppointmentsForNewDay() {
		if (cachedAppointments.get(currentDay) != null) {
			for (Appointment appointment : cachedAppointments.get(currentDay)) {
				AppointmentPane pane = new AppointmentPane(appointment);
				dayPane.getChildren().add(pane);
				AnchorPane.setTopAnchor(pane, calculateAppointmentPlacement(appointment.getStartTime()));
			}	
		}
	}
	
	private void incrementDay() {
		LocalDate today = LocalDate.parse(currentDay, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		currentDay = today.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		dayLabel.setText(currentDay);
	}
	
	private void decrementDay() {
		LocalDate today = LocalDate.parse(currentDay, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		currentDay = today.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		dayLabel.setText(currentDay);
	}
	
	private String getToday() {
		System.out.println(LocalDateTime.now().format(defaultFormatter).substring(0,10));
		return LocalDateTime.now().format(defaultFormatter).substring(0,10);
	}
		
	private double calculateAppointmentPlacement(String startTime) {
		String hourandminutes = startTime.substring(11);
		System.out.println(hourandminutes);
		String[] time = hourandminutes.split(":");
		int hour = Integer.valueOf(time[0]);
		int minutes = Integer.valueOf(time[1]);
		double pixels = (hour * 50 + minutes * 0.833);
		return pixels;
	}
	
	private ArrayList<Appointment> getAppointments() {
		AppointmentRequest req = new AppointmentRequest();
		req.addUsername(State.getUser().getUsername());
		State.getConnectionController().sendTCP(req);
		AppointmentResponse resp = (AppointmentResponse)State.getConnectionController().getObject("communication.responses.AppointmentResponse");
		return resp.getAppointments();
	}
	

}
