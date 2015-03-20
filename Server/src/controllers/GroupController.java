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
import communication.requests.GroupRequest;
import communication.responses.BaseResponse;
import communication.responses.GroupResponse;
import server.DatabaseConnector;
import models.Group;
import models.User;

public class GroupController {
	private static Connection db;

	public static GroupResponse handleGetGroupsRequest(GetGroupsRequest request) {
		db = DatabaseConnector.getDB();

		GroupResponse response = new GroupResponse();
		try {
			PreparedStatement statement = db.prepareStatement("SELECT * FROM UserGroup");
			ResultSet res = statement.executeQuery();
			while(res.next())
				response.addGroup(parseGroupFromResultset(res));
			response.setSuccessful(true);

		} catch (SQLException e) {
			e.printStackTrace();
			response.setSuccessful(false);
		}

		return response;
	}

	public static GroupResponse handleGroupRequest(GroupRequest request) {
		db = DatabaseConnector.getDB();
		GroupResponse response = new GroupResponse();

		if (request.getType().equals("leave")) {
			try {
				removeUserFromGroup(request.getGroupId(), request.getUsername());
			}
			catch (SQLException e){
				e.printStackTrace();
			}
			return response;
		}
		else if(request.getType().equals("groups")) {
			try {
				for (Group group : getGroupsForUser(request.getUsername())) {
					response.addGroup(group);
				}
				response.setSuccessful(true);
			}
			catch (SQLException e) {
				e.printStackTrace();
				response.setSuccessful(false);
			}
			return response;
		}
		else {
			return null;
		}
	}

	public static Group getGroup(int groupID) throws SQLException {
		PreparedStatement statement = db.prepareStatement(
				"SELECT * FROM UserGroup g WHERE g.group_id = " + groupID);
		ResultSet res = statement.executeQuery();
		if(res.next()) {
			return parseGroupFromResultset(res);
		}
		else
			throw new SQLException();
	}

	private static Group parseGroupFromResultset(ResultSet res) throws SQLException {
		Group group = new Group();
		group.setGroupID(res.getInt("group_id"));
		group.setName(res.getString("title"));

		PreparedStatement statement = db.prepareStatement(
				"SELECT * FROM UserGroupRelation ug WHERE ug.group_id = " + group.getGroupID());
		ResultSet userRes = statement.executeQuery();

		while(userRes.next()) {
			group.addUser(userRes.getString("username"));
		}

		return group;
	}

	public static void addGroup(Group group) throws SQLException{
		db = DatabaseConnector.getDB();

		String addGroup = String.format("INSERT INTO UserGroup(title) VALUES('%s')", group.getName());	
		PreparedStatement statement = db.prepareStatement(addGroup,  Statement.RETURN_GENERATED_KEYS);
		statement.execute();
		ResultSet res = statement.getGeneratedKeys();

		if(res.next()){
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
		for (String username : group.getMembers()) {
			addUserToGroup(groupId, username);
		}
	}

	private static void addUserToGroup(int groupId, String username) throws SQLException {
		try {
			PreparedStatement statement = db.prepareStatement(
					"INSERT INTO UserGroupRelation(group_id, username)" + 
							String.format("VALUES ('%s','%s')", groupId, username)
					);
			statement.execute();

		} catch(SQLException e) {
			System.out.println("Add user to group failed.");
			e.printStackTrace();
		}
	}

	private static void removeUserFromGroup(int groupId, String username) throws SQLException {
		try {
			PreparedStatement statement = db.prepareStatement(
					String.format("DELETE FROM UserGroupRelation WHERE `group_id` = %s AND `username` = '%s'", groupId, username));
			statement.execute();
			System.out.println("deleted");
		}
		catch(SQLException e) {
			System.out.println("Removing user from group failed");
			e.printStackTrace();
		}
	}
	private static ArrayList<Group> getGroupsForUser(String username) throws SQLException {
		ArrayList<Group> groups = new ArrayList<Group>();
		PreparedStatement statement = db.prepareStatement(
				String.format("SELECT * FROM UserGroup ug, UserGroupRelation ugr WHERE ugr.username = '%s' AND ugr.group_id = ug.group_id;", username));
		ResultSet res = statement.executeQuery();
		while(res.next()) {
			groups.add(parseGroupFromResultset(res));
		}
		return groups;
	}
}
