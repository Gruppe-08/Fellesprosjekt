package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import models.Group;
import models.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import calendar.State;
import communication.requests.CreateGroupRequest;
import communication.requests.GetUsersRequest;
import communication.responses.BaseResponse;
import communication.responses.GetUsersResponse;
import javafx.scene.control.CheckBox;

public class GroupController implements Initializable {

	@FXML
	Button cancel;
	@FXML
	Button create;
	@FXML
	AnchorPane listPane;
	@FXML
	TextField name;
	@FXML
	TextArea description;

	ArrayList<User> users;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		GetUsersRequest request = new GetUsersRequest();
		State.getConnectionController().sendTCP(request);
		GetUsersResponse response = (GetUsersResponse)State.getConnectionController().getObject("communication.responses.GetUsersResponse");
		double offset = 0;
		users = response.getUserList();
		for (User user : users){
			
			users.add(user);
			CheckBox checkbox = addCheckbox(user);
			listPane.getChildren().add(checkbox);
			
			AnchorPane.setTopAnchor(checkbox, offset);
			AnchorPane.setLeftAnchor(checkbox, 5.0);
			offset += 25;
		}
		
		
		create.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ArrayList<User> members = addUsers();
				String nameString = name.getText();
				String descriptionString = description.getText();
				Group group = new Group (nameString, descriptionString, members);
				createGroup(group);
			}
		});
	}
	
	private CheckBox addCheckbox(User user){
		CheckBox checkbox = new CheckBox();
		checkbox.setText(user.getUsername());
		return checkbox;
		
	}

	private ArrayList<User> addUsers(){
		ArrayList<User> members = new ArrayList<User>();
		ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
		for (Node node : listPane.getChildren()) {
			checkboxes.add((CheckBox)node);
		}
		for ( CheckBox checkbox : checkboxes){
			if (checkbox.isSelected()){
				for (User user : users) {
					if (user.getUsername() == checkbox.getText()) {
						members.add(user);
					}
				}
			}
		}
		return members;
	}
	
	public static void createGroup(Group group){
		CreateGroupRequest req = new CreateGroupRequest(group);
		State.getConnectionController().sendTCP(req);

		BaseResponse res = (BaseResponse) State.getConnectionController().getObject("communication.responses.BaseResponse");

		Alert groupAlert;
		if (res.wasSuccessful()){
			groupAlert = new Alert(AlertType.INFORMATION ,"Group created");	
		} else {
			groupAlert = new Alert(AlertType.WARNING, "Faled to create group.");
		}
		groupAlert.showAndWait();
	}

}
