package communication.requests;

/* This is the class the client sends to authenticate itself against our userbase.
 * The client will get username and password from user input and send it to the
 * server, on the serverside the information is checked against the userbase
 * and if we can find a matching user that has the a matching passwordhash
 * we mark the sending connection as authenticated and allow it call upon 
 * other methods
 */
public class AuthenticationRequest {
	private String username;
	private String password;
	
	public AuthenticationRequest() {}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "AuthenticationRequest [username=" + username + ", password="
				+ password + "]";
	}
}
