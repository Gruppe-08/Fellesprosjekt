package communication.responses;

import models.User;

/* Class used to signal to a client whether or not 
 * authentication succeeded. On success 'loggedIn' = true
 * and 'userId' denotes the unique key of the user in the
 * 'User'-table in the database. On failure, 'loggedIn' = false
 * and 'userId' = null
 */


public class AuthenticationResponse extends BaseResponse {
	private User user;
	
	public AuthenticationResponse() {}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
}
