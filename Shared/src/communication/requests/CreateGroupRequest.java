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

	
	public void addMember(User user){
		group.addUser(user);
	}
	
	public void addMembers(ArrayList<User> members){
		for (User member : members) {
			if(!group.getMembers().contains(member)){
				group.addUser(member);
			}
		}
	}
	
	public Group getGroup(){
		return group;
	}
	
	public ArrayList<User> getMembers(){
		return group.getMembers();
	}
	
	
	@Override
	public String toString(){
		return group.getName() + ": " + group.getMembers().toString();
	}
}
