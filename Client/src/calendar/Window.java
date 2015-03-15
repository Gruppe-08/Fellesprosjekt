package calendar;

public enum Window {
	DAY("Day.fxml"), 
	WEEK("Week.fxml"), 
	AGENDA("Agenda.fxml"),
	APPOINTMENT("Appointment.fxml"),
	LOGIN("Login.fxml"),
	CREATE_USER("CreateUser.fxml");
	
	private final String filename;
	
	private Window(final String text) {
        this.filename = text;
    }

    @Override
    public String toString() {
        return filename;
    }
}
