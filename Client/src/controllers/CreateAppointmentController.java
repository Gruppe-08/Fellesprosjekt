package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import util.DateUtil;
import calendar.State;
import communication.requests.PutAppointmentRequest;
import communication.responses.PutAppointmentResponse;
import models.Appointment;
import models.RepetitionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class CreateAppointmentController {
	
    @FXML
    private TextField title;

    @FXML
    DatePicker from_date;
    
    @FXML
    TextField from_time;
    
    @FXML
    DatePicker to_date;
    
    @FXML
    TextField to_time;
    
    @FXML
    CheckBox repeat_check;
    
    @FXML
    RadioButton repeay_daily;
    
    @FXML
    RadioButton repeay_monthly;

    @FXML
    RadioButton repeat_yearly;

    @FXML
    Button cancel_button;

    @FXML
    Button ok_button;

    @FXML
    private TextArea description;
    
    
    public void initialize(){
    	from_date.setValue(LocalDate.now());
    	to_date.setValue(LocalDate.now());
    }

    

    @FXML
    void onOk(ActionEvent event) {
    	LocalTime startTime = LocalTime.parse(from_time.getText());
    	LocalTime endTime = LocalTime.parse(to_time.getText());
    	
    	LocalDateTime startDate = from_date.getValue().atTime(startTime);
    	LocalDateTime endDate = to_date.getValue().atTime(endTime);
    	
    	Appointment appointment = new Appointment(
    			title.getText(),
    			description.getText(), 
    			DateUtil.serializeDateTime(startDate), 
    			DateUtil.serializeDateTime(endDate)
    	);
    	
    	appointment.setOwnerUsername(State.getUser().getUsername());
    	
    	
    	PutAppointmentRequest request = new PutAppointmentRequest();
    	request.setAppointment(appointment);
    	request.setNewAppointment(true);
    	
    	State.getConnectionController().sendTCP(request);
    	PutAppointmentResponse response = (PutAppointmentResponse) State.getConnectionController().getObject("communication.responses.PutAppointmentResponse");
    	
    	if(response.getErrorMessage() != null) {
    		Alert loginAlert = new Alert(AlertType.ERROR, 
					response.getErrorMessage());
			loginAlert.showAndWait();
    	} else {
    		State.getWindowController().loadPage("Agenda.fxml");
    	}
    	
    }

    @FXML
    void onCancel(ActionEvent event) {

    }
}