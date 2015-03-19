package tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import models.User;

import org.junit.Test;

import com.sun.media.jfxmedia.logging.Logger;

import server.DatabaseConnector;
import communication.requests.AuthenticationRequest;
import communication.requests.BusyCheckRequest;
import communication.requests.CreateUserRequest;
import communication.requests.GetUsersRequest;
import communication.requests.UpdateUserRequest;
import communication.responses.AuthenticationResponse;
import communication.responses.BaseResponse;
import communication.responses.BusyCheckResponse;
import communication.responses.CreateUserResponse;
import communication.responses.GetUsersResponse;
import controllers.UserController;

public class UserControllerTest {
	
	public UserControllerTest() {
		DatabaseConnector.initializeDatabase();
	}
	
	@Test
	public void handleAuthenticationRequest() {
		// Try to authenticate unvalid user
		AuthenticationRequest request = getAuthenticationRequest(false);
		AuthenticationResponse response = UserController.handleAuthenticationRequest(request);
		assertNull(response.getUser());
		assertFalse(response.getSuccessful());
		assertNotNull(response.getErrorMessage());
		
		
		// Authenticate valid user
		request = getAuthenticationRequest(true);
		response = UserController.handleAuthenticationRequest(request);
		assertNotNull(response.getUser());
		assertTrue(response.getSuccessful());
		assertNull(response.getErrorMessage());
		
	}

	public AuthenticationRequest getAuthenticationRequest(Boolean valid) {
		AuthenticationRequest request = new AuthenticationRequest();
		request.setUsername("test");
		if(valid) {
			request.setPassword("123456");
		} else {
			request.setPassword("");
		}
		return request;
	}
	
	@Test
	public void handleGetUsersRequestTest() {
		GetUsersRequest request = new GetUsersRequest();
		GetUsersResponse response = UserController.handleGetUsersRequest(request);
		
		ArrayList<User> users = response.getUserList();
		assertNotNull(users);
		
	}
	
	//public static GetUsersResponse handleGetUsersRequest(
			//GetUsersRequest request){
		//GetUsersResponse response = new GetUsersResponse();
		//try {
			//response.setUserList(getUsers());
			//response.setSuccessful(true);
		//}
		//catch(Exception e){
			//response.setSuccessful(false);
			//response.setErrorMessage(e.getMessage());
		//}
		//return response;
	//}
	
	@Test
	public void handleCreateUserRequestTest(){
		CreateUserRequest request = new CreateUserRequest();
		CreateUserResponse response = UserController.handleCreateUserRequest(request);
		//User user = response.createNewUser(request.getUser(),request.getPassword()); 
		//assertNotNull(user);
		assertNull(response.getErrorMessage()); 
		
	}
	
	
	//public static CreateUserResponse handleCreateUserRequest(CreateUserRequest request) {
	//CreateUserResponse response = new CreateUserResponse();
	//try {
		//createNewUser(request.getUser(), request.getPassword());
		//response.setSuccessful(true);
	//}
	//catch(Exception e) {
		//System.out.println(e.getMessage());
		//response.setSuccessful(false);
		//response.setErrorMessage(e.getMessage());
	//}
	//return response;
//}

	
	
	
	@Test
	public void handleBusyCheckTest(){
		
	}
	
	

	
	@Test
	public void handleUpdateUserRequestTest(){
		UpdateUserRequest req = new UpdateUserRequest();
		BaseResponse res = UserController.handleUpdateUserRequest(req);
		assertTrue(res.wasSuccessful()); 
		assertNull(res.getErrorMessage());
		
	}
	
	

	
	//public static BaseResponse handleUpdateUserRequest(UpdateUserRequest request) {
		//BaseResponse res = new BaseResponse();
		
		//try {
			//if(request.isDeleteRequest()){
				//String username = request.getUser().getUsername();
				//deleteUser(username);
			//} else {
				//updateUser(request.getUser());
			//}
			//res.setSuccessful(true);
		//} catch (SQLException e) {
			//res.setSuccessful(false);
			//res.setErrorMessage(e.getMessage());
			//Logger.logMsg(Logger.ERROR, e.getMessage());
		//}
		
		//return res;
	//}
}
