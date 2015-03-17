package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import calendar.State;
import calendar.Window;
import communication.requests.CreateUserRequest;
import communication.requests.GetUsersRequest;
import communication.requests.UpdateUserRequest;
import communication.responses.BaseResponse;
import communication.responses.GetUsersResponse;
import models.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class AdminController implements Initializable {
	User user;
	ObservableList<User> users;
	
	@FXML 
	ListView<User> user_list;
	
	@FXML
	TextField firstname;
	
	@FXML
	TextField lastname;
	
	@FXML
	TextField username;
	
	@FXML
	CheckBox is_admin;
	
	@FXML
	Button submit;
	
	@FXML
	Hyperlink delete;
	
	@FXML
	Button create_user;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeList();
		
		firstname.textProperty().addListener(f -> submit.setDisable(false));
		lastname.textProperty().addListener(f -> submit.setDisable(false));
		is_admin.selectedProperty().addListener(f -> submit.setDisable(false));
		
		user_list.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<User>() {
                public void changed(ObservableValue<? extends User> ov, 
                    User old_user, User new_user) {
                        user = new_user;
                        enableFields();
                        updateFields();
                        updateUser();
            }	
        });
		
	}
	
	public void initializeList(){
		disableAll();
		
		GetUsersRequest req = new GetUsersRequest();
		State.getConnectionController().sendTCP(req);
		GetUsersResponse res = (GetUsersResponse) State.getConnectionController().getObject("communication.responses.GetUsersResponse");
		
		users = FXCollections.observableArrayList(res.getUserList());
		user_list.setItems(users);
	}
	

	private void updateUser() {
		user.setFirstname(firstname.textProperty().getValue());
		user.setLastname(lastname.textProperty().getValue());
		user.setIsAdmin(is_admin.selectedProperty().getValue());
	}

	private void disableAll() {
		submit.setDisable(true);
		delete.setDisable(true);
		firstname.setDisable(true);
		lastname.setDisable(true);
		is_admin.setDisable(true);
	}
	
	private void enableFields(){
		delete.setDisable(false);
		firstname.setDisable(false);
		lastname.setDisable(false);
		is_admin.setDisable(false);
	}
	
	public void submitChanges(){
		UpdateUserRequest req = new UpdateUserRequest();
		updateUser();
		req.setUser(user);
		req.setDelete(false);
		State.getConnectionController().sendTCP(req);
		BaseResponse res = (BaseResponse) State.getConnectionController().getObject("communication.responses.BaseResponse");

		Alert editAlert;
		if(!res.wasSuccessful()){
			editAlert = new Alert(AlertType.ERROR, 
					"Could not update this user.");
			editAlert.showAndWait();
		} else {
			editAlert = new Alert(AlertType.INFORMATION, "User " + user + " edited.");
			editAlert.showAndWait();
			user_list.setItems(users);
		}
	}
	
	public void deleteUser(){
		UpdateUserRequest req = new UpdateUserRequest();
		req.setUser(user);
		req.setDelete(true);
		
		State.getConnectionController().sendTCP(req);
		BaseResponse res = (BaseResponse) State.getConnectionController().getObject("communication.responses.BaseResponse");
		Alert deleteAlert;
		if(!res.wasSuccessful()){
			 deleteAlert = new Alert(AlertType.ERROR, 
					"Could not delete this user.");
			deleteAlert.showAndWait();
		} else {
			user = new User();
			
			deleteAlert = new Alert(AlertType.INFORMATION, 
					"User " + user + " deleted.");
			deleteAlert.showAndWait();
			initializeList();
		}
	}
	
	public void createUser(){
		System.out.println("Create user!");
		CreateUserController controller = new CreateUserController();
		State.getWindowController().loadPage(Window.CREATE_USER, controller);
	}
	
	private void updateFields() {
		username.setText(user.getUsername());
		firstname.setText(user.getFirstname());
		lastname.setText(user.getLastname());
		is_admin.setSelected(user.isAdmin());	
		submit.setDisable(true);
	}
	
	class UserBinding {
		public User user;
		public StringProperty nameProperty;
		public StringProperty fistnameProperty;
		public StringProperty lastnameProperty;
		public SimpleBooleanProperty adminProperty;
		
		public UserBinding(User user) {
			this.user = user;
			nameProperty = new SimpleStringProperty(
					user.getFirstname() + " " + user.getLastname());
			fistnameProperty = new SimpleStringProperty(user.getFirstname());
			lastnameProperty = new SimpleStringProperty(user.getLastname());
			adminProperty = new SimpleBooleanProperty(user.isAdmin());
		}
	}

}
