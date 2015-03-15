package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.sun.media.jfxmedia.logging.Logger;

import communication.requests.CreateGroupRequest;
import communication.requests.GetGroupsRequest;
import communication.responses.BaseResponse;
import communication.responses.GroupResponse;
import server.DatabaseConnector;
import models.Group;
import models.User;

public class GroupController {
	private static Connection db;
	private static PreparedStatement statement;
	private static ResultSet res;
	
	public static GroupResponse handleGetGroupsRequest(GetGroupsRequest request) {
		db = DatabaseConnector.getDB();
		GroupResponse response = new GroupResponse();
		try {
			statement = db.prepareStatement("SELECT * FROM UserGroup");
			res = statement.executeQuery();
			while(res.next())
				response.addGroup(parseGroupFromResultset(res));
			response.setSuccessful(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			response.setSuccessful(false);
		}
		
		return response;
	}
	
	private static Group parseGroupFromResultset(ResultSet res) throws SQLException {
		Group group = new Group();
		group.setGroupID(res.getInt("group_id"));
		group.setName(res.getString("title"));
		
		return group;
	}

	public static void addGroup(Group group) throws SQLException{
		db = DatabaseConnector.getDB();

		String addGroup = String.format("INSERT INTO UserGroup(title) VALUES('%s')", group.getName());	
		statement = db.prepareStatement(addGroup,  Statement.RETURN_GENERATED_KEYS);
		statement.execute();
		res = statement.getGeneratedKeys();
		
		if(returnedGroupId()){
			// get generated groupId
			int groupId = res.getInt(1);
			addUsers(group, groupId);
		}
}
	
	public static BaseResponse handleCreateGroupRequest(CreateGroupRequest request) {
		Group group = request.getGroup();
		BaseResponse res = new BaseResponse();
		try{
			addGroup(group);
			res.setSuccessful(true);
		} catch(SQLException e){
			res.setErrorMessage("Could not create group.");
			Logger.logMsg(Logger.ERROR, "Failed to create group: " + e);
			res.setSuccessful(false);
		}
		return res;
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
					"INSERT INTO UserGroupRelation(group_id, username)" + 
					String.format("VALUES ('%s','%s')", groupId, user.getUsername())
			);
			statement.execute();
			
		} catch(SQLException e) {
			System.out.println("Add user to group failed.");
			e.printStackTrace();
		}
	}
}
