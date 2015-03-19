package controllers;

import java.util.HashMap;
import java.util.Map.Entry;

import communication.requests.GetGroupsRequest;
import communication.requests.GetUsersRequest;
import communication.responses.GetUsersResponse;
import communication.responses.GroupResponse;
import communication.responses.UserResponse;
import calendar.State;
import calendar.Window;
import util.DateUtil;
import models.Appointment;
import models.Group;
import models.User;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ViewAppointmentController {
	
	private Stage stage;
	private Appointment appointment;

    @FXML
    private Label date;

    @FXML
    private Button edit_button;

    @FXML
    private TableColumn<NameStatusPair, String> name_column;
    
    @FXML
    private TableColumn<NameStatusPair, String> status_column;

    @FXML
    private TableView<NameStatusPair> member_table;

    @FXML
    private Button accept_button;

    @FXML
    private Label description;

    @FXML
    private Button decline_button;

    @FXML
    private Label location1;	

    @FXML
    private Label title;

    @FXML
    private Label to_time;

    @FXML
    private Label from_time;

    @FXML
    void onOk(ActionEvent event) {
    	stage.close();
    }

    @FXML
    void onEdit(ActionEvent event) {
    	stage.close();
    	AppointmentController controller = new AppointmentController(appointment);	
		State.getWindowController().loadPage(Window.APPOINTMENT, controller);
    }

    @FXML
    void onAccept(ActionEvent event) {

    }

    @FXML
    void onDecline(ActionEvent event) {

    }

	public void initialize(Stage stage, Appointment appointment) {
		this.stage = stage;
		this.appointment = appointment;
		
		//Set factories for member table
		name_column.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<NameStatusPair,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<NameStatusPair, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().name);
			}
		});
		status_column.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<NameStatusPair,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(
					CellDataFeatures<NameStatusPair, String> param) {
				String status = param.getValue().status;
				if(status.equals("pending"))
					return new ReadOnlyStringWrapper("Pending");
				else if(status.equals("attending"))
					return new ReadOnlyStringWrapper("Attending");
				else if(status.equals("not_attending"))
					return new ReadOnlyStringWrapper("Declined");
				return null;
			}
		});
		
		//Load data from appointment into view
		title.setText(appointment.getTitle());
		description.setText(appointment.getDescription());
		from_time.setText(DateUtil.deserializeTime(appointment.getStartTime()).toString());
		to_time.setText(DateUtil.deserializeTime(appointment.getEndTime()).toString());
		date.setText(DateUtil.deserializeDate(appointment.getStartTime()).toString());
		location1.setText(appointment.getLocation());
		
		putRelationsInTable();
	}
	
	private void putRelationsInTable() {
		GetUsersResponse userResponse = 
				(GetUsersResponse) State.getConnectionController().sendRequest(new GetUsersRequest(), 
						GetUsersResponse.class);
		for(User user : userResponse.getUserList()) {
			String status = appointment.getUserRelations().get(user.getUsername());
			if(status != null) {
				String name = user.getFirstname() + " " + user.getLastname();
				member_table.getItems().add(new NameStatusPair(name, status));
			}
		}
		System.out.println("got users");
		GroupResponse groupResponse = 
				(GroupResponse) State.getConnectionController().sendRequest(new GetGroupsRequest(), 
						GroupResponse.class);
		for(Group group : groupResponse.getGroups()) {
			String status = appointment.getUserRelations().get(group.getGroupID());
			if(status != null) {
				member_table.getItems().add(new NameStatusPair(group.getName(), status));
				
			}
		}
		System.out.println("got groups");

	}

	class NameStatusPair {
		String name;
		String status;
		
		NameStatusPair(String name, String status) {
			this.name = name;
			this.status = status;
		}
	};
}
