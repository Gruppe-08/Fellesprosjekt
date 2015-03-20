package calendar;

import java.time.LocalDateTime;

import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.DeleteAppointmentRequest;
import communication.responses.BaseResponse;
import controllers.AppointmentController;
import util.DateUtil;
import models.Appointment;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;


public class AgendaPane extends AnchorPane {
	private Label title;
	private Label description;
	private Label time;
	private Label room;
	
	Appointment appointment;

	public AgendaPane(Appointment appointment) {
		this.appointment = appointment;
		
		this.setPrefSize(600, 110);
		this.setPadding(new Insets(5,5,5,5));
		this.setStyle("-fx-background-color: rgba(107, 211, 255, 0.3); -fx-background-radius: 13;");
		
		setTitle(appointment.getTitle());
		setDescription(appointment.getDescription());
		setTime(appointment.getStartTime(), appointment.getEndTime());
		if (appointment.getRoomId() == null) {
			setRoom("No location set");
		}
		else {
			setRoom(appointment.getRoomId().toString());
		}

		this.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				State.openAppointmentView(getClass(), appointment);
			}
		});
		
		this.getChildren().addAll(title, description, time);
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#38597F", 0.2));
		this.setEffect(dropShadow);
	}
	
	private void setTitle(String title){
		this.title = new Label(title);
		AnchorPane.setLeftAnchor(this.title, 5.0);
		AnchorPane.setTopAnchor(this.title, 7.0);
		this.title.setFont(Font.font("Helvetica neue ", FontWeight.BOLD, 18));
		this.title.setStyle("-fx-text-fill: #1d93c6");
	}
	
	private void setDescription(String description){
		this.description = new Label(description);
		AnchorPane.setTopAnchor(this.description, 37.0);
		AnchorPane.setLeftAnchor(this.description, 5.0);
		this.description.setFont(Font.font("Helvetica neue ", FontWeight.THIN, 12));
		this.description.setStyle("-fx-text-fill: #1d93c6");

	}
	
	private void setTime(String startTime, String endTime){
		LocalDateTime start = DateUtil.deserializeDateTime(startTime);
		LocalDateTime end = DateUtil.deserializeDateTime(endTime);
		String month = start.getMonth().toString().toLowerCase();
		String monthCapitalized = month.substring(0,1).toUpperCase() + month.substring(1);
		int day = start.getDayOfMonth();
		String start_time = DateUtil.serializeTime(start.toLocalTime());
		String end_time = DateUtil.serializeTime(end.toLocalTime());
		
		this.time = new Label(String.format("%s %s, from %s to %s", monthCapitalized, day, start_time, end_time));
		AnchorPane.setTopAnchor(this.time, 52.0);
		AnchorPane.setLeftAnchor(this.time, 5.0);
		this.time.setFont(Font.font("Helvetica neue ", FontWeight.THIN, 12));
		this.time.setStyle("-fx-text-fill: #1d93c6");
	}
	
	private void setRoom(String roomName) {
		this.room = new Label(roomName);
		AnchorPane.setTopAnchor(this.time, 67.0);
		AnchorPane.setLeftAnchor(this.time, 5.0);
		this.time.setFont(Font.font("Helvetica neue ", FontWeight.THIN, 12));
		this.time.setStyle("-fx-text-fill: #1d93c6");
	}
}
