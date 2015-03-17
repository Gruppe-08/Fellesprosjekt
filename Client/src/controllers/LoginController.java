package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import communication.requests.AuthenticationRequest;
import communication.responses.AuthenticationResponse;
import calendar.State;
import calendar.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginController implements Initializable {
	
	@FXML
	Button login_submit;
	
	@FXML
	Hyperlink login_create_user;
	
	@FXML
	Hyperlink login_forgot_password;
	
	@FXML
	TextField login_username;
	
	@FXML
	PasswordField login_password;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		login_submit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				AuthenticationRequest request = new AuthenticationRequest();
				request.setUsername(login_username.getText());
				request.setPassword(login_password.getText());
				
				System.out.println("Requesting authentication...");
				State.getConnectionController().sendTCP(request);
				Object object = State.getConnectionController().getObject("communication.responses.AuthenticationResponse");
				AuthenticationResponse auth = (AuthenticationResponse) object;
				if (auth.wasSuccessful()) {
					State.setUser(auth.getUser());
					System.out.println(auth.getUser());
					State.myWindowController.loginSuccessful();
				}
				else {
					Alert loginAlert = new Alert(AlertType.ERROR, 
							"Wrong username or password.");
					loginAlert.showAndWait();
				}
			}
		});
	}
}