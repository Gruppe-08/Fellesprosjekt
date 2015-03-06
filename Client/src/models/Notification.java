package models;

import java.time.LocalDateTime;

import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Notification {

	public Notification(int id, NotificationType type, String message, LocalDateTime timeCreated) {
		setNotificationId(id);
		setType(type);
		setMessage(message);
		setTimeCreated(timeCreated);
	}

	public Notification() {
		
	}
	
	private Property<Number> notificationId = new SimpleIntegerProperty();
	private NotificationType type;
	private Property<String> message = new SimpleStringProperty();
	private Property<LocalDateTime> timeCreated = new ObjectPropertyBase<LocalDateTime>(null) {
		@Override
		public Object getBean() {
			return this;
		}
		@Override
		public String getName() {
			return "timeCreated";
		}
	};
	
	public int getNotificationId() {
		return notificationId.getValue().intValue();
	}
	
	public void setNotificationId(int notificationId) {
		this.notificationId.setValue(notificationId);
	}
	
	public NotificationType getType() {
		return type;
	}
	
	public void setType(NotificationType type) {
		this.type = type;
	}
	
	public String getMessage() {
		return message.getValue();
	}
	
	public void setMessage(String message) {
		this.message.setValue(message);
	}
	
	public LocalDateTime getTimeCreated() {
		return timeCreated.getValue();
	}
	
	public void setTimeCreated(LocalDateTime timeCreated) {
		this.timeCreated.setValue(timeCreated);
	}
}
