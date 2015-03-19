package calendar;

import java.time.LocalDateTime;

import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.DeleteAppointmentRequest;
import communication.responses.BaseResponse;
import util.DateUtil;
import models.Appointment;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;


public class AgendaPane extends AnchorPane{
	private VBox text;
	private Text title;
	private Text description;
	private Text time;
	private Hyperlink delete;
	
	Appointment appointment;

	public AgendaPane(Appointment appointment) {
		this.appointment = appointment;
		
		this.setPrefSize(600, 50);
		this.setPadding(new Insets(5,5,5,5));
		this.setStyle("-fx-background-color: white");
		
		setTitle(appointment.getTitle());
		setDescription(appointment.getDescription());
		setTime(appointment.getStartTime(), appointment.getEndTime());
		
		this.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				State.openAppointmentView(getClass(), appointment);
			}
		});
		
		text = new VBox();
		text.setPrefHeight(50);
		text.setPrefWidth(400);
		text.setSpacing(5);
		
		delete = new Hyperlink("x");
		delete.setTextFill(Paint.valueOf("red"));
		delete.setId(appointment.getId().toString());
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("restriction")
			@Override
			public void handle(ActionEvent event) {
				Hyperlink link = (Hyperlink) event.getSource();
				int id = Integer.parseInt(link.getId());

				DeleteAppointmentRequest request = new DeleteAppointmentRequest(id);
				State.getConnectionController().sendTCP(request);				
				Logger.logMsg(Logger.DEBUG, "waiting for response");
				BaseResponse response = (BaseResponse) State.getConnectionController().getObject("communication.responses.BaseResponse");
				
				if(response.wasSuccessful()){
					Logger.logMsg(Logger.DEBUG, "Delete appointment successfull");
					State.getWindowController().loadPage(Window.AGENDA);
				} else {
					Alert loginAlert = new Alert(AlertType.ERROR, 
							"Could not delete appointment.");
					loginAlert.showAndWait();
				}
			}
		});
		
		text.getChildren().addAll(title, description, time);
		this.getChildren().addAll(text, delete);
		AnchorPane.setRightAnchor(delete, 0.0);
		AnchorPane.setLeftAnchor(text, 0.0);
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#38597F", 1.0));
		this.setEffect(dropShadow);
	}
	
	public void setTitle(String title){
		this.title = new Text(title);
		this.title.setFont(Font.font("Helvetica neue medium", FontWeight.BOLD, 12));
	}
	
	public void setDescription(String description){
		this.description = new Text(description);
	}
	
	public void setTime(String startTime, String endTime){
		LocalDateTime start = DateUtil.deserializeDateTime(startTime);
		LocalDateTime end = DateUtil.deserializeDateTime(endTime);
		String date = start.getMonth().toString().toLowerCase() + " " + start.getDayOfMonth();
		String start_time = start.getHour() + ":" + start.getMinute();
		String end_time = end.getHour() + ":" + end.getMinute();
		
		this.time = new Text(
				String.format("%s, from %s to %s", date, start_time, end_time));
	}
}
