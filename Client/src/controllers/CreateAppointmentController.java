package controllers;

import java.awt.Checkbox;
import java.time.LocalDateTime;
import java.time.LocalTime;

import util.DateUtil;
import calendar.State;
import communication.requests.GetUsersRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.PutAppointmentResponse;
import communication.responses.UserResponse;
import models.Appointment;
import models.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

public class CreateAppointmentController {
	@FXML
    TextField title;
    
    @FXML
    TextArea description;

    @FXML
    DatePicker from_date;
    
    @FXML
    TextField from_time;
    
    @FXML
    DatePicker to_date;
    
    @FXML
    TextField to_time;
    
    @FXML
    Button cancel_button;

    @FXML
    Button ok_button;
    
    @FXML
    ListView invite_user_list;
    
    void initialize() {
    	System.out.println("sad");
    	fillInviteList();
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
    	
    	title.setStyle("");
    	if(title.getText().equals(""))
    		title.setStyle("-fx-border-color: red");
    	else
        	appointment.setTitle(title.getText());
    	
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
    	State.getWindowController().loadPage("Agenda.fxml");
    }
    
    private void initalizeInviteTable() {
        TableColumn<User, Boolean> checkColumn = new TableColumn<User, Boolean>("Invited");
        checkColumn.setCellFactory(new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>() {

			@Override
			public TableCell<User, Boolean> call(
					TableColumn<User, Boolean> param) {
				
				return new CheckBoxTableCell();
			}
        	
        });
        TableColumn<User, String> nameColumn = new TableColumn<User, String>("Name");
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<User, String> param) {
				return new ReadOnlyObjectWrapper(
						param.getValue().getFirstname() +
						" " +
						param.getValue().getLastname());
			}
		});
        
        
    	
    	GetUsersRequest request = new GetUsersRequest();
    	State.getConnectionController().sendTCP(request);
    	UserResponse response = (UserResponse)State.getConnectionController().getObject(
    			"communication.responses.UserResponse");
    	if(response.wasSuccessful()) {
    		for(User user : response.getUsers()) {
    			invite_user_list.getItems().add(user);
    		}
    	}
    }
    
    private void updateAvailableUsers() {
    	
    }
}