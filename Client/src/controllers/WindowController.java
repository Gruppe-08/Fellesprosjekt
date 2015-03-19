package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import models.Notification;
import models.User;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import calendar.Calendar;
import calendar.NotificationService;
import calendar.State;
import calendar.Window;

public class WindowController implements Initializable {
	Calendar myCalendar = null;
	AnchorPane myWindow = null;
	MenuItem admin = null;
	public static Window previous_window = null;
	
	@FXML private AnchorPane mainPane;
	@FXML private AnchorPane main_window;
	@FXML private ImageView profilepic;
	@FXML private ImageView notificationAlert;
	
	@FXML private MenuButton menu;
    @FXML private ToggleButton dayToggle;
    @FXML private ToggleButton weekToggle;
    @FXML private ToggleButton agendaToggle;
    
    @FXML private ToggleGroup viewToggle;
    
    @FXML private Text username;
    
    @FXML private MenuItem notification;
    @FXML private MenuItem logout;
    @FXML private MenuItem groups;
    
    private ArrayList<Notification> notifications = new ArrayList<Notification>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPage(Window.LOGIN);
		
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
		User user = State.getUser();
		enableAndShowButtons();
		
		if(user.isAdmin()){
			insertAdminButton();
		}
		username.setText(user.getFirstname() + " " + user.getLastname());
		
		
		loadPage(Window.WEEK);
		NotificationService service = new NotificationService(State.getConnectionController(), State.getWindowController());
		service.start();
		
	}

	private void insertAdminButton() {
		admin = new MenuItem();
		menu.getItems().add(admin);
		
		admin.setText("Admin panel");
		admin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				AdminController controller = new AdminController();
				loadPage(Window.ADMIN, controller);
			}
		});
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
		username.setVisible(true);
		menu.setVisible(true);
		dayToggle.setVisible(true);
		weekToggle.setVisible(true);
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
