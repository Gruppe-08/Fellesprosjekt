package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import models.Notification;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import calendar.Calendar;
import calendar.NotificationService;
import calendar.State;
import calendar.Window;

public class WindowController implements Initializable {
	Calendar myCalendar = null;
	AnchorPane myWindow = null;
	public static Window previous_window = null;
	
	@FXML private AnchorPane mainPane;
	@FXML private AnchorPane main_window;
	@FXML private ImageView profilepic;
	@FXML private ImageView notificationAlert;
	
	@FXML private MenuButton menu;
    @FXML private ToggleButton dayToggle;
    @FXML private ToggleButton weekToggle;
    @FXML private ToggleButton monthToggle;
    @FXML private ToggleButton agendaToggle;
    
    @FXML private ToggleGroup viewToggle;
    
    @FXML private Text username;
    
    @FXML private MenuItem notification;
    @FXML private MenuItem logout;
    @FXML private MenuItem groups;
    
    private ArrayList<Notification> notifications = new ArrayList<Notification>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LoginController loginController = (LoginController)loadPage(Window.LOGIN);
		
		Platform.runLater(new Runnable() {
			@Override public void run() {
				notification.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						loadPage(Window.NOTIFICATIONS);
					}
				});

				groups.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						loadPage(Window.GROUP);
					}
				});
				logout.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						logout();
					}
				});
				dayToggle.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						loadPage(Window.DAY);
						viewToggle.selectToggle(dayToggle);
					}
				});
				weekToggle.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						loadPage(Window.WEEK);

						viewToggle.selectToggle(weekToggle);
					}
				});
				monthToggle.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						loadPage(Window.MONTH);
						viewToggle.selectToggle(monthToggle);
					}
				});
				agendaToggle.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						loadPage(Window.AGENDA);
						viewToggle.selectToggle(agendaToggle);
					}
				});	
			}
		});
	}
	
	protected void logout() {
		State.getConnectionController().close();
		State.getStage().close();
	}

	public void loginSuccessful() {
		enableAndShowButtons();
		username.setText(State.getUser().getFirstname() + " " + State.getUser().getLastname());
		loadPage(Window.WEEK);
		NotificationService service = new NotificationService(State.getConnectionController(), State.getWindowController());
		service.start();
	}
	
	public Object loadPage(Window window) {
       return loadPage(window, null);
	}
	
	public Object loadPage(Window window, Object controller){				
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/" + window));
		
		if(controller != null) {
			loader.setController(controller);
		}
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
	
	private void enableAndShowButtons() {
		profilepic.setVisible(true);
		username.setVisible(true);
		menu.setVisible(true);
		dayToggle.setVisible(true);
		weekToggle.setVisible(true);
		monthToggle.setVisible(true);
		agendaToggle.setVisible(true);
		dayToggle.setSelected(true);
	}
	
	public synchronized void addNotification(Notification notification) {
		if (notification.isRead() == 0) {
			for (Notification not : notifications) {
				if (notification.getId() == not.getId()) {
					return;
				}
			}
			notifications.add(notification);
			notificationAlert.setVisible(true);
		}
	}
	
	public synchronized Notification getNotification() {
		notificationAlert.setVisible(false);
		if (! notifications.isEmpty()) {
			return notifications.remove(notifications.size()-1);
		}
		return null;
	}
}
