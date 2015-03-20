package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import models.User;

import org.junit.Test;

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
		request.setUsername("kristian");
		if(valid) {
			request.setPassword("motorsykkel");
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
	
	
	@Test
	public void handleUpdateUserRequestTest(){
		UpdateUserRequest req = new UpdateUserRequest();
		User user = new User("villevold","Ingrid","Vold",false);
		req.setUser(user);
		req.setDelete(true);
		BaseResponse res = UserController.handleUpdateUserRequest(req);
		assertTrue(res.wasSuccessful());
		
	}
	
	
	@Test
	public void handleCreateUserRequestTest(){
		User user = new User("villevold","Ingrid","Vold",false);
		CreateUserRequest request = new CreateUserRequest();
		request.setUser(user);
		request.setPassword("kebab1");
		CreateUserResponse response = UserController.handleCreateUserRequest(request);
		assertTrue(response.wasSuccessful());
		
		
	}
	
	@Test
	public void handleBusyCheckTest(){
		BusyCheckRequest request = new BusyCheckRequest();
		BusyCheckResponse response = UserController.handleBusyCheck(request);
		
		ArrayList<String> usernames = response.getUsernames();
		assertNotNull(usernames);		
		
	}

}
