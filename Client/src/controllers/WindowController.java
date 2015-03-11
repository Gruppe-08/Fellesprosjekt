package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

		loadPage("CreateGroup.fxml");
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
	
	public Object loadNewStage(String pageName) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/" + pageName));
		try {
			Pane root = loader.load();
	        Scene scene = new Scene(root);
	        Stage openedStage = new Stage();
	        openedStage.setScene(scene);
	        openedStage.show();
	        return loader.getController();
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
