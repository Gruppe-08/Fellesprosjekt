package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import util.DateUtil;
import calendar.State;
import calendar.Window;
import communication.requests.PutAppointmentRequest;
import communication.responses.PutAppointmentResponse;
import models.Appointment;
import models.RepetitionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AppointmentController {
	Appointment appointment; 
	Boolean isNew;
	
	@FXML
    TextField title;
    
    @FXML
    TextArea description;

    @FXML
    DatePicker date;
    
    @FXML
    TextField from_time;
    
    @FXML
    TextField to_time;
    
    @FXML
    Button cancel_button;

    @FXML
    Button ok_button;
    
    public AppointmentController(){ 
    	isNew = true;
    }
    
    public AppointmentController(Appointment appointment){    	
    	this.appointment = appointment;
    	this.isNew = (appointment == null);
    }
    
    public void initialize(){
    	if(appointment != null){
    		title.setText(appointment.getTitle()); 
        	description.setText(appointment.getDescription());
        	
        	date.setValue( DateUtil.deserializeDate(appointment.getStartTime()) );
        	
        	from_time.setText( DateUtil.deserializeTime(appointment.getStartTime()).toString() );
        	to_time.setText( DateUtil.deserializeTime(appointment.getEndTime()).toString() );
    	} else {
    		appointment = new Appointment();
    	}
    	
    	if(isNew == false){
    		ok_button.setText("Save changes");
    	}
    }

    @FXML
    void onOk(ActionEvent event) {
    	LocalTime startTime = LocalTime.parse(from_time.getText());
    	LocalTime endTime = LocalTime.parse(to_time.getText());
    	
    	LocalDateTime startDate = date.getValue().atTime(startTime);
    	LocalDateTime endDate = date.getValue().atTime(endTime);
    	
    	appointment.setTitle(title.getText());
    	appointment.setDescription(description.getText());
 
    	appointment.setStartTime(DateUtil.serializeDateTime(startDate));
    	appointment.setEndTime(DateUtil.serializeDateTime(endDate));
    	
    	appointment.setOwnerUsername(State.getUser().getUsername());
    	
    	title.setStyle("");
    	if(title.getText().equals(""))
    		title.setStyle("-fx-border-color: red");
    	else
        	appointment.setTitle(title.getText());
    	
    	PutAppointmentRequest request = new PutAppointmentRequest();
    	request.setAppointment(appointment);
    	request.setNewAppointment(isNew);
    	
    	State.getConnectionController().sendTCP(request);
    	PutAppointmentResponse response = (PutAppointmentResponse) State.getConnectionController().getObject("communication.responses.PutAppointmentResponse");
    	
    	if(response.getErrorMessage() != null) {
    		Alert loginAlert = new Alert(AlertType.ERROR, 
					response.getErrorMessage());
			loginAlert.showAndWait();
    	} else {
    		State.getWindowController().loadPage(Window.AGENDA);
    	}
    	
    }

    @FXML
    void onCancel(ActionEvent event) {
    	State.getWindowController().loadPage(Window.AGENDA);
    }
}