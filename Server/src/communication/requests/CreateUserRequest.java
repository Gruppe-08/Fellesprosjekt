package communication.requests;

import models.User;

public class CreateUserRequest {
	private User user;
	private String password;
	
	public CreateUserRequest() {}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String toString() {
		return "CreateUserRequest [name=" + user.getFirstname() + " " + user.getLastname() 
				+ ", username=" + user.getUsername() 
				+ ", password=" + password + "]";
	}

}
