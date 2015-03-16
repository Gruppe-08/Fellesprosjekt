package communication.requests;

import java.util.ArrayList;

import models.Group;
import models.User;

public class CreateGroupRequest {
	private Group group;
	
	public CreateGroupRequest(){
		this.group = new Group();
	}
	
	public CreateGroupRequest(Group group){
		this.group = group;
	}

	
	public void addMember(String username){
		group.addUser(username);
	}
	
	public void addMembers(ArrayList<String> usernames){
		for (String username : usernames) {
			if(!group.getMembers().contains(username)){
				group.addUser(username);
			}
		}
	}
	
	public Group getGroup(){
		return group;
	}
	
	public ArrayList<String> getMembers(){
		return group.getMembers();
	}
	
	
	@Override
	public String toString(){
		return group.getName() + ": " + group.getMembers().toString();
	}
}
