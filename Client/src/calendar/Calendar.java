package calendar;

import java.io.IOException;
import java.time.LocalTime;

import util.DateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.minlog.Log;

import communication.ClassRegistration;
import controllers.ConnectionController;

public class Calendar extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		State.setCalendar(this);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Main.fxml"));
		Parent root = (Parent)loader.load();
		State.setWindowController(loader.getController());
		Scene scene = new Scene(root, 1024, 768);
		State.setStage(stage);
		stage.setTitle("Calendar");
		stage.setScene(scene);
		stage.show();

		//Connect to server
		try {
			ConnectionController connectionCtrl = new ConnectionController("localhost", 5437);
			
			State.setConnectionController(connectionCtrl);
			//Register class
			ClassRegistration.register(connectionCtrl);
			
			//RMI
			ObjectSpace.registerClasses(connectionCtrl.getKryo());
		}
		catch(IOException e) {
			root.setDisable(true);

			Alert loginAlert = new Alert(AlertType.ERROR, 
					e.getMessage() +
					"\nPlease contact your server administrator");
			loginAlert.showAndWait();	
		}
	}

	public static void main(String[] args) {
		Log.set(Log.LEVEL_DEBUG);
		launch(args);
	}
}