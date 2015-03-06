package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import calendar.State;
import communication.requests.CreateUserRequest;
import communication.responses.CreateUserResponse;
import models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

	
	public void initialize() {
		submit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Handle createUser");
				User user = new User(username.getText(), firstname.getText(), lastname.getText());

				String p1= password.getText();
				String p2 = confirmPassword.getText();
				
				Boolean validPassword = UserUtil.isValidPassword(p1) && UserUtil.isMatchingPasswords(p1, p2);
				Boolean validUser = UserUtil.isValidUser(user);
				
				if(validPassword && validUser){
					System.out.println("Set valid style");
					password.setStyle("");
					confirmPassword.setStyle("");
					sendRequest(user, p1);
				} else {
					// TODO: give user feedback on what is wrong
					System.out.println("Input is unvalid");
					if(!validPassword){
						System.out.println("Set unvalid style");
						password.setStyle("-fx-border-color: red");
						confirmPassword.setStyle("-fx-border-color: red");
					}

				}
				
			}

			private void sendRequest(User user, String password) {
				CreateUserRequest request = new CreateUserRequest();
				request.setPassword(password);
				request.setUser(user);
				
				System.out.println("Requesting createUser...");
				State.getConnectionController().sendTCP(request);
				Object object = State.getConnectionController().getObject("communication.responses.CreateUserResponse");
				CreateUserResponse res = (CreateUserResponse) object;
				
				if (res.wasSuccessful()) {
					State.myWindowController.loadPage("Login.fxml");
				}
				else {
					// TODO: Flash error message to show that login failed.
					System.out.println("Login failed");
				}
			}
		});
	}
}
