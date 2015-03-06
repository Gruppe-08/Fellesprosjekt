package calendar;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AgendaPane extends VBox{
	
	public AgendaPane(String appointment) {
		this.setPrefHeight(50);
		this.setPrefWidth(600);
		this.setSpacing(5);
		this.setPadding(new Insets(5,5,5,5));
		this.setStyle("-fx-background-color: white");
		
		this.getChildren().add(new Text(appointment));
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#38597F", 1.0));
		this.setEffect(dropShadow);
		
		
	}
}
