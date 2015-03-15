package models;

import java.util.ArrayList;

public class Group {
	private int groupID;
	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	private String name;
	private ArrayList<User> members;
	
	public Group(){
		name = "";
		members = new ArrayList<User>();
	}
	
	public Group(String name, ArrayList<User> members){
		this.name = name;
		this.members = members;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void addUser(User user){
		members.add(user);
	}
	
	public void removeUser(User user){
		try{
			members.remove(user);
		} catch(Exception e){
			throw new IllegalArgumentException("This user could not be deleted.");
		}

	}

	public ArrayList<User> getMembers() {
		return this.members;
	}
	

}
