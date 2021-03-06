package models;
public class Notification {

	private int id;
	private String message;
	private String created;
	private String triggerDate;
	private NotificationType notificationType;
	private boolean isAlarm;
	private Appointment appointment;
	private String username;
	private int read;
	private String status;
	
	public Notification(NotificationType type, String message, String timeCreated) {
		setType(type);
		setMessage(message);
		setCreated(timeCreated);
		
	}
	
	public Notification() {
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getTriggerDate() {
		return triggerDate;
	}

	public void setTriggerDate(String triggerDate) {
		this.triggerDate = triggerDate;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String type) {
		if(type.equals("user")) {
			notificationType = NotificationType.USER;
		} else if(type.equals("group")){
			notificationType = NotificationType.GROUP;
		} else if(type.equals("appointment")){
			notificationType = NotificationType.APPOINTMENT;
		} else {
			throw new IllegalArgumentException("Invalid notification type: " + type);
		}
	}
	
	public void setNotificationType(NotificationType type){
		this.type = type;
	}

	public boolean isAlarm() {
		return isAlarm;
	}

	public void setAlarm(int i) {
		this.isAlarm = (i == 1);
	}
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	private NotificationType type;
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int isRead() {
		return read;
	}
	
	public void setRead(int read) {
		this.read = read;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
