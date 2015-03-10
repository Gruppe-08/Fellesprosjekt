package controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.util.Pair;
import models.Appointment;
import models.User;
import util.DateUtil;
import calendar.State;
import communication.requests.GetUsersRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.PutAppointmentResponse;
import communication.responses.UserResponse;

public class CreateAppointmentController implements Initializable {
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
    TableView<Pair<BooleanProperty, User>> invite_user_list;
    
    @FXML
    private TableColumn<Pair<BooleanProperty, User>, String> available_column;
    
    @FXML
    private TableColumn<Pair<BooleanProperty, User>, BooleanProperty> added_column;

    @FXML
    private TableColumn<Pair<BooleanProperty, User>, String> name_column;

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
    	System.out.println("gee");


        name_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<BooleanProperty, User>,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Pair<BooleanProperty, User>, String> param) {
				return new ReadOnlyObjectWrapper<String>(
						param.getValue().getValue().getFirstname() +
						" " +
						param.getValue().getValue().getLastname());
			}
		});
        //WTF am I even doing
        available_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pair<BooleanProperty, User>,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<Pair<BooleanProperty, User>, String> param) {
				return Bindings.createStringBinding(new Callable<String>() {
					public BooleanProperty property = param.getValue().getKey();
					@Override
					public String call() throws Exception {
						if(property.getValue())
							return "Yes";
						else
							return "No";
					}
				} );
			}
		});

        added_column.setCellFactory(new Callback<TableColumn<Pair<BooleanProperty, User>,BooleanProperty>, TableCell<Pair<BooleanProperty, User>,BooleanProperty>>() {
			@Override
			public TableCell<Pair<BooleanProperty, User>, BooleanProperty> call(TableColumn<Pair<BooleanProperty, User>, BooleanProperty> param) {
				return new CheckBoxTableCell();
			}
		});
        
    	GetUsersRequest request = new GetUsersRequest();
    	State.getConnectionController().sendTCP(request);
    	UserResponse response = (UserResponse)State.getConnectionController().getObject(
    			"communication.responses.UserResponse");
    	if(response.wasSuccessful()) {
    		for(User user : response.getUsers()) {
    			invite_user_list.getItems().add(new Pair<BooleanProperty, User>(new SimpleBooleanProperty(true), user));
    		}
    	}
    }
    
    private void updateAvailableUsers() {
    	
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	System.out.println("sad");
    	
    	initalizeInviteTable();
		
	}

}