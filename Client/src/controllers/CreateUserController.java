package controllers;

import calendar.State;
import calendar.Window;
import communication.requests.CreateUserRequest;
import communication.responses.CreateUserResponse;
import models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.UserUtil;

public class CreateUserController {
	@FXML
	TextField firstname;
	
	@FXML
	TextField lastname;
	
	@FXML
	TextField username;
	
	@FXML
	PasswordField password;
	
	@FXML
	PasswordField confirmPassword;
	
	@FXML
	Button submit;
	
	@FXML
	CheckBox admin;

	
	public void initialize() {
		submit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				User user = new User(username.getText(), firstname.getText(), lastname.getText(), admin.isSelected());

				String p1= password.getText();
				String p2 = confirmPassword.getText();
				
				Boolean validPassword = UserUtil.isValidPassword(p1) && UserUtil.isMatchingPasswords(p1, p2);
				Boolean validUser = UserUtil.isValidUser(user);
				
				if(validPassword && validUser){
					password.setStyle("");
					confirmPassword.setStyle("");
					sendRequest(user, p1);
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					if(!validPassword){
						password.setStyle("-fx-border-color: red");
						confirmPassword.setStyle("-fx-border-color: red");
						
						String error = "";
						if(password.getText().length() < 6) error = "The password must be 6 characters or more.\n";
						else error = "The passwords does not match";
						alert.setContentText(error);
					}
					alert.showAndWait();
				}
				
			}

			private void sendRequest(User user, String password) {
				CreateUserRequest request = new CreateUserRequest();
				request.setPassword(password);
				request.setUser(user);
				State.getConnectionController().sendTCP(request);
				
				CreateUserResponse res = (CreateUserResponse)  State.getConnectionController().getObject("communication.responses.CreateUserResponse");
				
				if (res.wasSuccessful()) {
					AdminController controller = new AdminController();
					State.myWindowController.loadPage(Window.ADMIN, controller);
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("Could not create user.");
					alert.showAndWait();
				}
			}
		});
	}
}
