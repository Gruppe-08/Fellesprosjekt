package communication.responses;

import java.util.ArrayList;

import models.User;

public class GetUsersResponse extends BaseResponse {
	private ArrayList<User> userList =  new ArrayList<User>();
	
	public GetUsersResponse(){
		
	}
	
	public GetUsersResponse(ArrayList<User> userList){
		this.userList = userList;
	}
	
	public ArrayList<User> getUserList(){
		return userList;
	}
	
	public void setUserList(ArrayList<User> userList){
		this.userList = userList;
	}

}
