package calendar;

public enum Window {
	DAY("Day.fxml"), 
	WEEK("Week.fxml"),
	MONTH("Month.fxml"),
	AGENDA("Agenda.fxml"),
	APPOINTMENT("Appointment.fxml"),
	NOTIFICATIONS("NotificationView.fxml"),
	LOGIN("Login.fxml"),
	CREATE_USER("CreateUser.fxml"),
	CREATE_GROUP("CreateGroup.fxml"),
	ADMIN("Admin.fxml");
	
	private final String filename;
	
	private Window(final String text) {
        this.filename = text;
    }

    @Override
    public String toString() {
        return filename;
    }
}
