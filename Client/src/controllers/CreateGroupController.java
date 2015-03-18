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
import calendar.Window;
import communication.requests.CreateGroupRequest;
import communication.requests.GetUsersRequest;
import communication.responses.BaseResponse;
import communication.responses.GetUsersResponse;
import communication.responses.UserResponse;
import javafx.scene.control.CheckBox;

public class CreateGroupController implements Initializable {

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
		UserResponse response = (UserResponse)State.getConnectionController().getObject("communication.responses.UserResponse");
		users = response.getUsers();
		double offset = 0;
		for (User user : users){
			
			CheckBox checkbox = addCheckbox(user);
			listPane.getChildren().add(checkbox);
			
			AnchorPane.setTopAnchor(checkbox, offset);
			AnchorPane.setLeftAnchor(checkbox, 5.0);
			offset += 25;
		}
		
		
		create.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ArrayList<String> members = addUsers();
				String nameString = name.getText();
				Group group = new Group (nameString, members);
				createGroup(group);
				State.getWindowController().loadPage(Window.GROUP);
			}
		});
		
		cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				State.getWindowController().loadPage(Window.GROUP);
			}
		});
	}
	
	private CheckBox addCheckbox(User user){
		CheckBox checkbox = new CheckBox();
		checkbox.setText(user.getUsername());
		return checkbox;
		
	}

	private ArrayList<String> addUsers(){
		ArrayList<String> members = new ArrayList<String>();
		ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
		for (Node node : listPane.getChildren()) {
			checkboxes.add((CheckBox)node);
		}
		for ( CheckBox checkbox : checkboxes){
			if (checkbox.isSelected()){
				for (User user : users) {
					if (user.getUsername() == checkbox.getText()) {
						members.add(user.getUsername());
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

		if (!res.wasSuccessful()){
			new Alert(AlertType.WARNING, "Failed to create group.").showAndWait();
		}
	}
}
