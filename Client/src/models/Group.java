package models;

import java.util.ArrayList;

public class Group {
	private String name;
	private String description;
	private ArrayList<User> members;
	
	public Group(){
		name = "";
		description = "";
		members = new ArrayList<User>();
	}
	
	public Group(String name, String description, ArrayList<User> members){
		this.name = name;
		this.description = description;
		this.members = members;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
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
	

}
