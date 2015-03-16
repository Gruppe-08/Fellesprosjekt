package communication.responses;

import java.util.ArrayList;

public class BusyCheckResponse extends BaseResponse {
	private ArrayList<String> usernames = new ArrayList<String>();

	public BusyCheckResponse() {
		
	}
	
	public ArrayList<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
}
