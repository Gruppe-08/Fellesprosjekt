package controllers;
import calendar.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class UserHeaderController {
	@FXML
	Text header_username;
	
	@FXML 
	Button menu_button;
	
	@FXML
	AnchorPane menu_pane;
	

	@FXML
	Button notification;

	@FXML
	Button profile;

	@FXML
	Button groups;

	@FXML
	Button logout;
	
	Boolean menuVisible = false;
	
	public void initialize(){
		
		menu_button.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				menuVisible = !menuVisible;
				menu_pane.setVisible(menuVisible);
				System.out.println(menuVisible);
				
				
				if (menuVisible){
					// Send header to front if the menu is visible
					menu_pane.toFront();
				} else {
					// Else, send header to back.
					menu_pane.toBack();
				}
			}
		});
		
		notification.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Notification");
			}
		});

		profile.setOnAction(new EventHandler<ActionEvent>(){
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
