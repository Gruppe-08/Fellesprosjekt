package controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Appointment;
import models.Group;
import models.Room;
import models.User;
import util.DateUtil;
import calendar.State;
import calendar.Window;
import communication.requests.BusyCheckRequest;
import communication.requests.GetGroupsRequest;
import communication.requests.GetRoomsRequest;
import communication.requests.GetUsersRequest;
import communication.requests.PutAppointmentRequest;
import communication.responses.BusyCheckResponse;
import communication.responses.GetUsersResponse;
import communication.responses.GroupResponse;
import communication.responses.PutAppointmentResponse;
import communication.responses.RoomResponse;

public class AppointmentController implements Initializable {
	Appointment appointment;
	Boolean isNew;
	boolean datesValid = false;
	boolean titleValid = false;
	boolean descriptionValid = true;
	
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
    Text header_text;
    
    @FXML
    Button cancel_button;

    @FXML
    Button ok_button;
    
    @FXML
    Text top_text;
    
    @FXML
    TableView<Invitable> invite_user_list;
    
    @FXML
    private TableColumn<Invitable, String> available_column;
    
    @FXML
    private TableColumn<Invitable, Boolean> added_column;

    @FXML
    private TableColumn<Invitable, String> name_column;
    
    @FXML
    TableView<InvitableGroup> invite_group_list;
    
    @FXML
    private TableColumn<InvitableGroup, Boolean> group_added_column;

    @FXML
    private TableColumn<InvitableGroup, String> title_column;
    
    @FXML
    private TableColumn<InvitableGroup, String> group_available_column;
    
    @FXML
    private TextField location;
    
    @FXML
    private CheckBox use_location_check;
    
    @FXML
    private Button room_button;

    public AppointmentController(){ 
    	isNew = true;
    	this.appointment = new Appointment();
    }
    
    public AppointmentController(Appointment appointment){    	
    	this.appointment = appointment;
    	this.isNew = (appointment == null);
    }
    
    @FXML
    private void onChooseRoom() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ChooseRoom.fxml"));
		try {
			Parent root = loader.load();
			ChooseRoomController controller = (ChooseRoomController)loader.getController();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			controller.initialize(stage, this);
			stage.setScene(scene);
			stage.initOwner(State.getStage());
			stage.initModality(Modality.WINDOW_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
    
	private void fillAppointmentFields() {
		if(!isNew){
    		title.setText(appointment.getTitle()); 
        	description.setText(appointment.getDescription());
        	
        	date.setValue(DateUtil.deserializeDate(appointment.getStartTime()) );
        	
        	from_time.setText( DateUtil.deserializeTime(appointment.getStartTime()).toString() );
        	to_time.setText( DateUtil.deserializeTime(appointment.getEndTime()).toString() );
        	
        	location.setText(appointment.getLocation());
        	if(appointment.getLocation() != null)
        		use_location_check.setSelected(true);
        	
        	//Clear users that have been invited from invite list
        	for(Entry<String, String> userRelation : appointment.getUserRelations().entrySet()) {
        		ListIterator<Invitable> iter = invite_user_list.getItems().listIterator();
        		while(iter.hasNext()) {
        			Invitable user = iter.next();
        			if(userRelation.getKey().equals(user.user.getUsername()))
        				iter.remove();
        		}
        	}
        	//Clear groups that have been invited from invite list
        	for(Entry<Integer, String> groupRelation : appointment.getGroupRelations().entrySet()) {
        		ListIterator<InvitableGroup> iter = invite_group_list.getItems().listIterator();
        		while(iter.hasNext()) {
        			InvitableGroup group = iter.next();
        			if(groupRelation.getKey().equals(group.group.getGroupID()))
        				iter.remove();
        		}
        	}
        	
    	} else {
    		appointment = new Appointment();
    	}
    	
    	if(isNew == false) {
    		header_text.setText("Edit Appointment");

    		ok_button.setText("Save");
    	} else {
    		ok_button.setText("Ok");
    	}
	}

    @FXML
    void onOk(ActionEvent event) {
    	if(titleValid && datesValid && descriptionValid) {
	    	PutAppointmentRequest request = new PutAppointmentRequest();
	    	request.setAppointment(appointment);
	    	for(Invitable user : invite_user_list.getItems()) {
	    		if(user.selected.getValue())
	    			request.getAppointment().getUserRelations().put(user.user.getUsername(), "pending");
	    	}
	    	for(InvitableGroup group : invite_group_list.getItems()) {
	    		if(group.selected.getValue())
	    			request.getAppointment().getGroupRelations().put(group.group.getGroupID(), "pending");	
	    	}
	    	
	    	if(use_location_check.isSelected()) {
	    		appointment.setLocation(location.getText());
	    		appointment.setRoomId(null);
	    	}
	    	request.setNewAppointment(isNew);
	    	
	    	State.getConnectionController().sendTCP(request);
	    	PutAppointmentResponse response = (PutAppointmentResponse) State.getConnectionController().getObject("communication.responses.PutAppointmentResponse");
	    	
	    	if(response.getErrorMessage() != null) {
	    		Alert loginAlert = new Alert(AlertType.ERROR, 
						response.getErrorMessage());
				loginAlert.showAndWait();
	    	} else {
	        	State.getWindowController().loadPage(State.getWindowController().getLastPage());
	    	}
    	}
    }

    @FXML
    void onCancel(ActionEvent event) {
    	State.getWindowController().loadPage(State.getWindowController().getLastPage());
    }
    
    void validateTitleField() {
    	title.setStyle("");
    	if(title.getText().equals("") || (title.getText().length() > 20)) {
    		title.setStyle("-fx-border-color: red");
    		titleValid = false;
    	}
    	else {
        	appointment.setTitle(title.getText());
        	titleValid = true;
    	}
    }
    
    void validateDescriptionField() {
    	description.setStyle("");
    	if(description.getText().length() > 30) {
    		description.setStyle("-fx-border-color: red");
    		descriptionValid = false;
    	}
    	else {
    		appointment.setDescription(description.getText());
    		descriptionValid = true;
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
        group_available_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InvitableGroup,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<InvitableGroup, String> param) {
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
        group_added_column.setCellFactory(new Callback<TableColumn<InvitableGroup,Boolean>, TableCell<InvitableGroup,Boolean>>() {		
			@Override
			public TableCell<InvitableGroup, Boolean> call(
					TableColumn<InvitableGroup, Boolean> param) {
				return new CheckBoxTableCell<InvitableGroup, Boolean>();
			}
		});
		group_added_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InvitableGroup,Boolean>, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(
					CellDataFeatures<InvitableGroup, Boolean> param) {
				return param.getValue().selected;
			}
		});
        title_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InvitableGroup,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<InvitableGroup, String> param) {
				return param.getValue().titleProperty;
			}
		});
        
        //Fill user invite list with users
    	GetUsersRequest request = new GetUsersRequest();
    	State.getConnectionController().sendTCP(request);
    	GetUsersResponse response = (GetUsersResponse)State.getConnectionController().getObject(
    			"communication.responses.GetUsersResponse");
    	if(response.wasSuccessful()) {
    		for(User user : response.getUserList()) {
    			invite_user_list.getItems().add(new Invitable(user));
    		}
    	}
    	
    	//Fill group invite list with groups
    	GetGroupsRequest group_request = new GetGroupsRequest();
    	State.getConnectionController().sendTCP(group_request);
    	GroupResponse group_response = (GroupResponse)State.getConnectionController().getObject(
    			"communication.responses.GroupResponse");
    	if(group_response.wasSuccessful()) {
    		for(Group group : group_response.getGroups()) {
    			invite_group_list.getItems().add(new InvitableGroup(group));
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
	   	appointment.setOwnerUsername(State.getUser().getUsername());
	   	
    	initalizeInviteTable();
    	if(!isNew)
    		fillAppointmentFields();
	   	
	   	title.textProperty().addListener(f -> validateTitleField());
	   	description.textProperty().addListener(f -> validateDescriptionField());
	   	from_time.textProperty().addListener(f -> onChronoFieldChanged());
	   	to_time.textProperty().addListener(f -> onChronoFieldChanged());
	   	date.valueProperty().addListener(f -> onChronoFieldChanged());
	   	room_button.disableProperty().bind(use_location_check.selectedProperty());
	   	location.disableProperty().bind(use_location_check.selectedProperty().not());
	   	use_location_check.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if(newValue == true) {
					appointment.setRoomId(null);
					appointment.setLocation("");
					location.setText("");
					location.promptTextProperty().set("Enter name of location");
				}
				else {
					appointment.setLocation(null);
					location.setText("");
					location.promptTextProperty().set("");					
				}
			}
		});
    	validateTitleField();
    	onChronoFieldChanged();
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
	
	class InvitableGroup {
		public Group group;
		public StringProperty titleProperty;
		public BooleanProperty available = new SimpleBooleanProperty(true);
		public BooleanProperty selected = new SimpleBooleanProperty();
		
		public InvitableGroup(Group group) {
			this.group = group;
			titleProperty = new SimpleStringProperty(
					group.getName());
		}
	}

	public void setRoom(Room room) {
		appointment.setRoomId(room.getRoomId());
		appointment.setLocation(null);
		location.setText(room.getName());
	}
}