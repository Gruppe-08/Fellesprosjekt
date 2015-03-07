package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import calendar.Calendar;
import calendar.State;

public class WindowController implements Initializable {
	Calendar myCalendar = null;
	BorderPane myWindow = null;
	
	@FXML
	private Pane userHeader;
	
	@FXML
	private AnchorPane mainPane;
	
	@FXML
	private BorderPane main_window;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LoginController loginController = (LoginController)loadPage("Login.fxml");
	}
	
	public void loginSuccessful() {
		UserHeaderController headerController = (UserHeaderController)loadHeader("UserHeader.fxml");
		headerController.header_username.setText(State.getUser().getFirstname() +
				" " + State.getUser().getLastname());
		loadPage("AlternateDayView.fxml");
		userHeader.toFront();
		mainPane.toBack();
	}
	
	public Object loadPage(String pageName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/" + pageName));
		try {
			Pane root = loader.load();
	        mainPane.getChildren().clear();
	        mainPane.getChildren().add(root);
	        return loader.getController();
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Object loadHeader(String pageName){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/" + pageName));
		try {
			Pane root = loader.load();
			userHeader.getChildren().clear();
	        userHeader.getChildren().add(root);
	        return loader.getController();
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
