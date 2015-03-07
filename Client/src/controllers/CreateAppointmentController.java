package controllers;

import java.time.LocalDateTime;

import models.Appointment;
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
    	LocalDateTime startTime = date.getValue().atTime(DateUtil.deserializeTime(from_date.getText());
    	Appointment appointment = new Appointment(title_text.getText(),
    			description_text.getText(), startTime, endTime);
    }

    @FXML
    void onCancel(ActionEvent event) {

    }
}