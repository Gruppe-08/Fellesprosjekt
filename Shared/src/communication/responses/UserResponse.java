package communication.responses;

import java.util.ArrayList;

import models.User;

public class UserResponse extends BaseResponse {
	private ArrayList<User> users = new ArrayList<User>();

	public ArrayList<User> getUsers() {
		return users;
	}

	public void addUser(User user) {
		users.add(user);
	}
}
