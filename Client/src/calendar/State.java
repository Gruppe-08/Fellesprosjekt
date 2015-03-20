package calendar;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controllers.ChooseRoomController;
import controllers.ConnectionController;
import controllers.ViewAppointmentController;
import controllers.WindowController;
import models.Appointment;
import models.User;

public class State {
	public static WindowController myWindowController = null;
	public static ConnectionController myConnectionController = null;
	public static Calendar myCalendar = null;
	public static User user = null;
	public static Stage stage = null;
	
	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		State.stage = stage;
	}

	public static void setWindowController(WindowController myWindowController) {
		State.myWindowController = myWindowController;
	}

	public static ConnectionController getConnectionController() {
		return myConnectionController;
	}

	public static void setConnectionController(
			ConnectionController myConnectionController) {
		State.myConnectionController = myConnectionController;
	}

	public static Calendar getCalendar() {
		return myCalendar;
	}

	public static void setCalendar(Calendar myCalendar) {
		State.myCalendar = myCalendar;
	}
	
	public static void setUser(User user){
		State.user = user;
	}
	
	public static User getUser(){
		return user;
	}
	
	public static WindowController getWindowController() {
		return myWindowController;
	}
	
	public static void openAppointmentView(Object callingObject, Appointment appointment) {
		FXMLLoader loader = new FXMLLoader(State.myWindowController.getClass().getResource("../views/ViewAppointment.fxml"));
		try {
			Parent root = loader.load();
			ViewAppointmentController controller = (ViewAppointmentController)loader.getController();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			controller.initialize(stage, appointment);
			stage.setScene(scene);
			stage.initOwner(State.getStage());
			stage.initModality(Modality.WINDOW_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}