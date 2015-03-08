package communication.requests;

public class DeleteAppointmentRequest {
	private int appointmentId;

	DeleteAppointmentRequest() {}
	
	public DeleteAppointmentRequest(int appointmentId) {
		setAppointmentId(appointmentId);
	}
	
	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointment_id) {
		this.appointmentId = appointment_id;
	}
}
