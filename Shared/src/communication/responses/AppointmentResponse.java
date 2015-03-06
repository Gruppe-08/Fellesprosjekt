package communication.responses;

import java.util.ArrayList;

import models.Appointment;

public class AppointmentResponse extends BaseResponse {
	private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
	
	public AppointmentResponse() {}
	
	public void addAppointment(Appointment new_appointment) {
		//Remove duplicate appointments
		for(Appointment appointment : appointments) {
			if(new_appointment.getId() == appointment.getId()) //TODO: Hashmap?
				return;
		}
		
		appointments.add(new_appointment);
	}

	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}
}
