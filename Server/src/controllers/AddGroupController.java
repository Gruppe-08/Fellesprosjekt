package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import server.DatabaseConnector;
import models.Group;
import models.User;

public class AddGroupController {
	private static Connection db;
	private static PreparedStatement statement;
	private static ResultSet res;
	
	private static String addUserGroupRelation = "INSERT INTO UserGroupRelation(group_id, username)";
	
	public static void addGroup(Group group){
		db = DatabaseConnector.getDB();

		String addGroup = String.format("INSERT INTO UserGroup(title) VALUES('%s')", group.getName());	
		try {
			statement = db.prepareStatement(addGroup,  Statement.RETURN_GENERATED_KEYS);
			statement.execute();
			res = statement.getGeneratedKeys();
			
			if(returnedGroupId()){
				// get generated groupId
				int groupId = res.getInt(1);
				addUsers(group, groupId);
			}	
		} catch (SQLException e) {
			System.out.println("Add group failed.");
			e.printStackTrace();
		}
	}


	private static void addUsers(Group group, int groupId) throws SQLException {
		for (User user : group.getMembers()) {
			addUserToGroup(groupId, user);
		}
	}


	private static boolean returnedGroupId() throws SQLException {
		return res.next();
	}


	private static void addUserToGroup(int groupId, User user) throws SQLException {
		try {
			statement = db.prepareStatement(
					addUserGroupRelation + 
					String.format("VALUES ('%s','%s')", groupId, user.getUsername())
			);
			statement.execute();
			
		} catch(SQLException e) {
			System.out.println("Add user to group failed.");
			e.printStackTrace();
		}
	}
}
