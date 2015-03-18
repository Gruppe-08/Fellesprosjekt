package communication.requests;

import models.User;

public class UpdateUserRequest {
	private User user;
	private Boolean delete;
	
	public UpdateUserRequest() {}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setDelete(Boolean delete){
		this.delete = delete;
	}
	
	public Boolean isDeleteRequest(){
		return delete;
	}
	
	
	@Override
	public String toString() {
		return "UpdateUserRequest [name=" + user.getFirstname() + " " + user.getLastname() 
				+ ", username=" + user.getUsername() +  "]";
	}

}
