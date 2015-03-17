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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import calendar.Window;
import communication.requests.BusyCheckRequest;
import communication.requests.GetUsersRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.BusyCheckResponse;
import communication.responses.PutAppointmentResponse;
import communication.responses.UserResponse;
import controllers.AppointmentController.Invitable;

public class AppointmentController implements Initializable {
	Appointment appointment;
	Boolean isNew;
	boolean datesValid = false;
	boolean titleValid = false;
	
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
    
    @FXML
    TableView<Invitable> invite_user_list;
    
    @FXML
    private TableColumn<Invitable, String> available_column;
    
    @FXML
    private TableColumn<Invitable, Boolean> added_column;

    @FXML
    private TableColumn<Invitable, String> name_column;
    
    public AppointmentController(){ 
    	isNew = true;
    }
    
    public AppointmentController(Appointment appointment){    	
    	this.appointment = appointment;
    	this.isNew = (appointment == null);
    }
    

	private void fillAppointmentFields() {
		if(appointment != null){
    		title.setText(appointment.getTitle()); 
        	description.setText(appointment.getDescription());
        	
        	System.out.println(appointment.getStartTime());
        	date.setValue( DateUtil.deserializeDate(appointment.getStartTime()) );
        	
        	from_time.setText( DateUtil.deserializeTime(appointment.getStartTime()).toString() );
        	to_time.setText( DateUtil.deserializeTime(appointment.getEndTime()).toString() );
    	} else {
    		appointment = new Appointment();
    	}
    	
    	if(isNew == false){
    		ok_button.setText("Save");
    	}
	}

    @FXML
    void onOk(ActionEvent event) {
    	if(titleValid && datesValid) {
	    	PutAppointmentRequest request = new PutAppointmentRequest();
	    	
	    	appointment.setDescription(description.getText());
	    	request.setAppointment(appointment);
	    	for(Invitable user : invite_user_list.getItems()) {
	    		if(user.selected.getValue())
	    			request.getAppointment().getUserRelations().add(
	    					user.user.getUsername());
	    		
	    	}
	    	request.setNewAppointment(true);
	    	
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
    	
    }

    @FXML
    void onCancel(ActionEvent event) {
    	State.getWindowController().loadPage(Window.AGENDA);
    }
    
    void validateTitleField() {
    	title.setStyle("");
    	if(title.getText().equals("")) {
    		title.setStyle("-fx-border-color: red");
    		titleValid = false;
    	}
    	else {
        	appointment.setTitle(title.getText());
        	titleValid = true;
    	}
    }
    
    void onChronoFieldChanged() {
    	datesValid = true;
    	try {
    		from_time.setStyle("");
    		date.setStyle("");
    		LocalTime startTime = LocalTime.parse(from_time.getText());
    		LocalDateTime startDate = date.getValue().atTime(startTime);
    		appointment.setStartTime(DateUtil.serializeDateTime(startDate));
    	} catch(DateTimeParseException e) {
    		from_time.setStyle("-fx-border-color: red");
    		date.setStyle("-fx-border-color: red");
    		datesValid = false;
    	}
    	
    	try {
    		to_time.setStyle("");
    		LocalTime endTime = LocalTime.parse(to_time.getText());
    		LocalDateTime endDate = date.getValue().atTime(endTime);
    		appointment.setEndTime(DateUtil.serializeDateTime(endDate));
    	} catch(DateTimeParseException e) {
    		to_time.setStyle("-fx-border-color: red");
    		date.setStyle("-fx-border-color: red");
    		datesValid = false;
    	}
    	if(datesValid)
    		updateAvailableUsers();
    };
    
    private void initalizeInviteTable() {
        name_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invitable,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Invitable, String> param) {
				return param.getValue().nameProperty;
			}
		});
        added_column.setCellFactory(new Callback<TableColumn<Invitable,Boolean>, TableCell<Invitable,Boolean>>() {		
			@Override
			public TableCell<Invitable, Boolean> call(
					TableColumn<Invitable, Boolean> param) {
				return new CheckBoxTableCell<Invitable, Boolean>();
			}
		});
		added_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invitable,Boolean>, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(
					CellDataFeatures<Invitable, Boolean> param) {
				return param.getValue().selected;
			}
		});
        available_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invitable,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<Invitable, String> param) {
				return Bindings.createStringBinding(new Callable<String>() {
					@Override
					public String call() throws Exception {
						if(param.getValue().available.get())
							return "Yes";
						else
							return "No";
					}
				}, param.getValue().available);
			}
		});
    	GetUsersRequest request = new GetUsersRequest();
    	State.getConnectionController().sendTCP(request);
    	UserResponse response = (UserResponse)State.getConnectionController().getObject(
    			"communication.responses.UserResponse");
    	if(response.wasSuccessful()) {
    		for(User user : response.getUsers()) {
    			invite_user_list.getItems().add(new Invitable(user));
    		}
    	}
    	
    	
    }

    
    private void updateAvailableUsers() {
    	BusyCheckRequest request = new BusyCheckRequest();
    	
    	Appointment a = new Appointment();
    	LocalTime startTime = LocalTime.parse(from_time.getText());
    	LocalTime endTime = LocalTime.parse(to_time.getText());
    	
    	LocalDateTime startDate = date.getValue().atTime(startTime);
    	LocalDateTime endDate = date.getValue().atTime(endTime);
    	
    	a.setStartTime(DateUtil.serializeDateTime(startDate));
		a.setEndTime(DateUtil.serializeDateTime(endDate));
		
    	request.setAppointment(a);
    	for(Invitable item : invite_user_list.getItems()) {
    		request.getUsernames().add(item.user.getUsername());
    	}
    	
    	State.getConnectionController().sendTCP(request);
    	BusyCheckResponse response = (BusyCheckResponse) State.getConnectionController().getObject("communication.responses.BusyCheckResponse");
    	
    	if(response.getErrorMessage() != null) {
    		Alert loginAlert = new Alert(AlertType.ERROR, 
					"Internal error");
			loginAlert.showAndWait();
    	} else {
        	for(Invitable item : invite_user_list.getItems()) {
        		if(response.getUsernames().contains(item.user.getUsername())) {
        			item.available.setValue(false);
        		}
        		else item.available.setValue(true);
        	}
    	}    	
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		appointment = new Appointment();
	   	appointment.setOwnerUsername(State.getUser().getUsername());
	   	
	   	title.textProperty().addListener(f -> validateTitleField());
	   	from_time.textProperty().addListener(f -> onChronoFieldChanged());
	   	to_time.textProperty().addListener(f -> onChronoFieldChanged());
	   	date.valueProperty().addListener(f -> onChronoFieldChanged());
	   	
    	initalizeInviteTable();
    	onChronoFieldChanged();
    	fillAppointmentFields();
	}
	
	class Invitable {
		public User user;
		public StringProperty nameProperty;
		public BooleanProperty available = new SimpleBooleanProperty(true);
		public BooleanProperty selected = new SimpleBooleanProperty();
		
		public Invitable(User user) {
			this.user = user;
			nameProperty = new SimpleStringProperty(
					user.getFirstname() + " " + user.getLastname());
		}
	}
}