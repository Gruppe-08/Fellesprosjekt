package communication.requests;

import java.util.ArrayList;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;

/* This is the class that will be sent to request a set of appointments,
 * by including a set of usernames and group_ids you can filter out what
 * kinds of appointments you want, the server will respond with an
 * AppointmentResponse
 */
public class AppointmentRequest {
	
	private ArrayList<String> usernames = new ArrayList<String>();
	private ArrayList<Integer> groupIDs = new ArrayList<Integer>();
	
	public AppointmentRequest() {
		
	}
	
	public void addUsername(String username) {
		usernames.add(username);
	}
	
	public ArrayList<String> getUsernames() {
		return usernames;
	}

	public ArrayList<Integer> getGroupIDs() {
		return groupIDs;
	}

	public void removeUsername(String username) {
		usernames.remove(username);
	}
	
	public void addGroupID(int group_id) {
		groupIDs.add(group_id);
	}
	
	public void removeGroupID(int group_id) {
		usernames.remove(group_id);
	}
}
