package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import models.User;

import org.junit.Test;

import server.DatabaseConnector;
import communication.requests.AuthenticationRequest;
import communication.requests.GetUsersRequest;
import communication.responses.AuthenticationResponse;
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
}
