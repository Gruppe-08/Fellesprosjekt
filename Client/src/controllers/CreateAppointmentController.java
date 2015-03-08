package controllers;

import java.time.LocalDateTime;

import calendar.State;
import util.DateUtil;
import models.Appointment;
import models.RepetitionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CreateAppointmentController {

    @FXML
    private DatePicker date;

    @FXML
    private RadioButton yearly_check;

    @FXML
    private TextField from_date;

    @FXML
    private TextField to_date;

    @FXML
    private CheckBox repeat_check;

    @FXML
    private TextField title_text;

    @FXML
    private Button ok_button;

    @FXML
    private TextArea description_text;

    @FXML
    private RadioButton monthly_check;

    @FXML
    private RadioButton daily_check;

    @FXML
    void onOk(ActionEvent event) {
    	LocalDateTime startDateTime = date.getValue().atTime(DateUtil.deserializeTime(from_date.getText()));
    	LocalDateTime endDateTime = date.getValue().atTime(DateUtil.deserializeTime(to_date.getText()));
    	
    	Appointment appointment = new Appointment();
    	appointment.setTitle(title_text.getText());
    	appointment.setDescription(description_text.getText());
    	appointment.setStartTime(startDateTime.toString());
    	appointment.setEndTime(endDateTime.toString());
    	if(repeat_check.isSelected()) {
    		
    	}
    }

    @FXML
    void onCancel(ActionEvent event) {

    }
}