package calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import util.DateUtil;
import models.Appointment;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;


public class AgendaPane extends VBox{
	private Text title;
	private Text description;
	private Text time;

	public AgendaPane(Appointment appointment) {
		setTitle(appointment.getTitle());
		setDescription(appointment.getDescription());
		setTime(appointment.getStartTime(), appointment.getEndTime());
		
		this.getChildren().addAll(title, description, time);
		
		this.setPrefHeight(50);
		this.setPrefWidth(600);
		this.setSpacing(5);
		this.setPadding(new Insets(5,5,5,5));
		this.setStyle("-fx-background-color: white");
		
		
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
		this.time = new Text(
				String.format("From %s to %s", DateUtil.presentString(startTime), DateUtil.presentString(endTime)));
	}
}
