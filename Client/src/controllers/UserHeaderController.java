package controllers;
import calendar.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class UserHeaderController {
    @FXML
    private MenuItem notification;

    @FXML
    private MenuItem logout;

    @FXML
    private Text header_username;

    @FXML
    private MenuItem groups;
	
	public void initialize(){
		header_username.setText(State.getUser().getFirstname() +
				" " + State.getUser().getLastname());
		
		notification.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Notification");
			}
		});

		groups.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Notification");
			}
		});
		logout.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Notification");
			}
		});
	}
}
