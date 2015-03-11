package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.text.DateFormatSymbols;

import util.DateUtil;
import models.Appointment;
import calendar.State;
import calendar.WeekAppointmentPane;
import communication.requests.AppointmentRequest;
import communication.responses.AppointmentResponse;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class WeekViewController implements Initializable {
	
	@FXML private Button nextWeekButton;
	@FXML private Button prevWeekButton;
	@FXML private Text weekLabel;
	@FXML private AnchorPane weekPane;
	@FXML private Text monthLabel;
	@FXML private Text yearLabel;
	
	@FXML private Text mondayLabel;
	@FXML private Text tuesdayLabel;
	@FXML private Text wednesdayLabel;
	@FXML private Text thursdayLabel;
	@FXML private Text fridayLabel;
	@FXML private Text saturdayLabel;
	@FXML private Text sundayLabel;
	
	private ArrayList<Node> blankWeekPane = new ArrayList<Node>();
	private Map<Integer, ArrayList<Appointment>> cachedAppointments = new HashMap<Integer, ArrayList<Appointment>>();
	private int currentWeek;
	private int currentYear;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Saves the blank pane, so deleting of appointments is easily done
		for (Node node : weekPane.getChildren()) {
			blankWeekPane.add(node);
		}
		ArrayList<Appointment> appointments = getAppointments();
		cacheAppointments(appointments);
		currentWeek = DateUtil.getWeekOfYear(DateUtil.getNow());
		currentYear = DateUtil.getYear(DateUtil.getNow());	
		addAppointmentsForNewWeek();
		updateLabels();
		
		nextWeekButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				weekPane.getChildren().retainAll(blankWeekPane);
				nextWeek();
			}
			
		});
		
		prevWeekButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				weekPane.getChildren().retainAll(blankWeekPane);
				prevWeek();
			}
		});
		
		
	}
	
	private void nextWeek() {
		if (currentWeek == DateUtil.getNumberOfWeeksInYear(currentYear)) {
			currentWeek = 1;
			currentYear++;
		}
		else {
			currentWeek++;
		}
		addAppointmentsForNewWeek();
		updateLabels();
	}
	
	private void prevWeek() {
		if (currentWeek == 1) {
			currentYear--;
			currentWeek = DateUtil.getNumberOfWeeksInYear(currentYear);
		}
		else {
			currentWeek--;
		}
		addAppointmentsForNewWeek();
		updateLabels();
	}
	
	private void addAppointmentsForNewWeek() {
		if (cachedAppointments.get(currentWeek)!= null) {
			for (Appointment appointment: cachedAppointments.get(currentWeek)) {
				if (DateUtil.getYear(appointment.getStartTime()) == currentYear) {
					WeekAppointmentPane weekAppointment = new WeekAppointmentPane(appointment);
					System.out.println("Added");
					double[] dimensions = calculateAppointmentPlacement(appointment); 
					AnchorPane.setLeftAnchor(weekAppointment, dimensions[0]);
					AnchorPane.setRightAnchor(weekAppointment, dimensions[2]);
					AnchorPane.setTopAnchor(weekAppointment, dimensions[1]);
					weekPane.getChildren().add(weekAppointment);
				}
			}
		}
	}
	//TODO: Make weeks numbers behave correctly when changing years
	private void updateLabels() {
		weekLabel.setText("Week " + currentWeek);
		yearLabel.setText(currentYear +"");
		int[] months= DateUtil.datesInSameMonth(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 2), DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 1)); 
		if (months[0] == months[1]) {
			monthLabel.setText(new DateFormatSymbols(Locale.US).getMonths()[months[0]-1]);
		}
		else {
			monthLabel.setText(new DateFormatSymbols(Locale.US).getMonths()[months[0]-1] + "/" + new DateFormatSymbols(Locale.US).getMonths()[months[1]-1]);
		}
		mondayLabel.setText("Mon " + String.valueOf(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 2).getDayOfMonth()));
		tuesdayLabel.setText("Tue " + String.valueOf(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 3).getDayOfMonth()));
		wednesdayLabel.setText("Wed " + String.valueOf(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 4).getDayOfMonth()));
		thursdayLabel.setText("Thu " + String.valueOf(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 5).getDayOfMonth()));
		fridayLabel.setText("Fri " + String.valueOf(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 6).getDayOfMonth()));
		saturdayLabel.setText("Sat " + String.valueOf(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 7).getDayOfMonth()));
		sundayLabel.setText("Sun " + String.valueOf(DateUtil.getDateOfDayInWeek(currentYear, currentWeek, 1).getDayOfMonth()));		
	}
	
	//Problematic with multiple years, will have to change later
	private void cacheAppointments(ArrayList<Appointment> appointments) {
		for (Appointment appointment : appointments) {
			int week = DateUtil.getWeekOfYear(appointment.getStartTime());
			if (cachedAppointments.get(week) == null) {
				cachedAppointments.put(week, new ArrayList<Appointment>());
			}
			cachedAppointments.get(week).add(appointment);
		}
	}
	
	private double[] calculateAppointmentPlacement(Appointment appointment) {
		double[] dimensions = new double[3];
		double dayOfWeek = DateUtil.getDayOfWeek(appointment.getStartTime());
		dimensions[0] = 75 + ((dayOfWeek-1) * 132.0) + 2.0;	//This is the x-coordinate placement of the appointment
		LocalTime time = DateUtil.deserializeDateTime(appointment.getStartTime()).toLocalTime();
		dimensions[1] = time.getHour() * 50 + time.getMinute() * 0.833;	//This is the y-coordinate placement of the appointment
		dimensions[2] = ((7-dayOfWeek) * 132.0) + 4.0;	
		return dimensions;
	}
	
	private ArrayList<Appointment> getAppointments() {
		AppointmentRequest req = new AppointmentRequest();
		req.addUsername(State.getUser().getUsername());
		State.getConnectionController().sendTCP(req);
		AppointmentResponse resp = (AppointmentResponse)State.getConnectionController().getObject("communication.responses.AppointmentResponse");
		return resp.getAppointments();
	}
}
