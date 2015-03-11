package controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import com.sun.media.jfxmedia.logging.Logger;

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
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Pair;
import models.Appointment;
import models.User;
import util.DateUtil;
import calendar.State;
import communication.requests.BusyCheckRequest;
import communication.requests.GetUsersRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.BusyCheckResponse;
import communication.responses.PutAppointmentResponse;
import communication.responses.UserResponse;

public class CreateAppointmentController implements Initializable {
	Appointment appointment = new Appointment();
	boolean isValid = false;
	
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
    	PutAppointmentRequest request = new PutAppointmentRequest();
    	request.setAppointment(appointment);
    	for(int i =  0; i < invite_user_list.getItems().size(); i++) {
    		System.out.println(i);
    		if(added_column.getCellData(i).get()) {
    			appointment.getUserRelations().add(invite_user_list.getItems().get(i).getValue().getUsername());
    			System.out.println("added " + invite_user_list.getItems().get(i).getValue().getUsername());
    		}
    		
    	}
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
    
    @FXML
    void onFieldChanged() {
    	System.out.println("ei");
    	title.setStyle("");
    	if(title.getText().equals(""))
    		title.setStyle("-fx-border-color: red");
    	else
        	appointment.setTitle(title.getText());
    	
    	boolean dateTimeValid = true;
    	try {
    		from_time.setStyle("");
    		from_date.setStyle("");
    		LocalTime startTime = LocalTime.parse(from_time.getText());
    		LocalDateTime startDate = from_date.getValue().atTime(startTime);
    		appointment.setStartTime(DateUtil.serializeDateTime(startDate));
    	} catch(DateTimeParseException e) {
    		from_time.setStyle("-fx-border-color: red");
    		from_date.setStyle("-fx-border-color: red");
    		dateTimeValid = false;
    	}
    	
    	try {
    		to_time.setStyle("");
    		to_date.setStyle("");
    		LocalTime endTime = LocalTime.parse(to_time.getText());
    		LocalDateTime endDate = from_date.getValue().atTime(endTime);
    		appointment.setEndTime(DateUtil.serializeDateTime(endDate));
    	} catch(DateTimeParseException e) {
    		to_time.setStyle("-fx-border-color: red");
    		to_date.setStyle("-fx-border-color: red");
    		dateTimeValid = false;
    	}
    	if(dateTimeValid)
    		updateAvailableUsers();
    };
    
    private void initalizeInviteTable() {
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
				return Bindings.createStringBinding(
						new Callable<String>() {
							@Override
							public String call() throws Exception {
								if(param.getValue().getKey().getValue()) {
									return "Yes";
								}
								else
									return "No";
							}
							
						}, param.getValue().getKey());
			}
		});

        add.setCellValueFactory( f -> f.getValue().getCompleted());
        loadedColumn.setCellFactory( tc -> new CheckBoxTableCell<>());
        
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
    	BusyCheckRequest request = new BusyCheckRequest();
    	
    	Appointment a = new Appointment();
    	LocalTime startTime = LocalTime.parse(from_time.getText());
    	LocalTime endTime = LocalTime.parse(to_time.getText());
    	
    	LocalDateTime startDate = from_date.getValue().atTime(startTime);
    	LocalDateTime endDate = to_date.getValue().atTime(endTime);
    	
    	a.setStartTime(DateUtil.serializeDateTime(startDate));
		a.setEndTime(DateUtil.serializeDateTime(endDate));
		
    	request.setAppointment(a);
    	for(Pair<BooleanProperty, User> item : invite_user_list.getItems()) {
    		request.getUsernames().add(item.getValue().getUsername());
    	}
    	
    	State.getConnectionController().sendTCP(request);
    	BusyCheckResponse response = (BusyCheckResponse) State.getConnectionController().getObject("communication.responses.BusyCheckResponse");
    	
    	if(response.getErrorMessage() != null) {
    		Alert loginAlert = new Alert(AlertType.ERROR, 
					"Internal error");
			loginAlert.showAndWait();
    	} else {
        	for(Pair<BooleanProperty, User> item : invite_user_list.getItems()) {
        		if(response.getUsernames().contains(item.getValue().getUsername())) {
        			item.getKey().set(false);
        		}
        		else item.getKey().set(true);
        			
        	}
    	}    	
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	   	appointment.setOwnerUsername(State.getUser().getUsername());
	   	
	   	ChangeListener<String> stringChangeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				onFieldChanged();
			}
	   	};
	   	ChangeListener<LocalDate> dateChangeListener = new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable,
					LocalDate oldValue, LocalDate newValue) {
				onFieldChanged();
			}
	   	};
	   	
	   	title.textProperty().addListener(f -> onFieldChanged());
	   	from_time.textProperty().addListener(stringChangeListener);
	   	to_time.textProperty().addListener(stringChangeListener);
	   	from_date.valueProperty().addListener(dateChangeListener);
	   	to_date.valueProperty().addListener(dateChangeListener);
    
    	initalizeInviteTable();
    	onFieldChanged();
	}

}