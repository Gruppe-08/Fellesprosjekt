package models;

import java.util.ArrayList;

public class Group {	
	private String name;
	private int groupID;
	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	private ArrayList<String> usernames;
	public void setMembers(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
	
	public Group(){
		name = "";
		usernames = new ArrayList<String>();
	}
	
	public Group(String name, ArrayList<String> usernames){
		this.name = name;
		this.usernames = usernames;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void addUser(String username){
		usernames.add(username);
	}
	
	public void removeUser(String username){
		try{
			usernames.remove(username);
		} catch(Exception e){
			throw new IllegalArgumentException("This user could not be deleted.");
		}

	}

	public ArrayList<String> getMembers() {
		return this.usernames;
	}

}
