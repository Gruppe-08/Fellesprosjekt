package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.sun.media.jfxmedia.logging.Logger;

import communication.ClassRegistration;
import communication.responses.AuthenticationResponse;
import controllers.ConnectionController;
import controllers.LoginController;

public class LoginControllerTest {
	LoginController controller;
	ConnectionController connection;
	AuthenticationResponse auth;
	
	public LoginControllerTest() {
		try {
    		connection = new ConnectionController("localhost", 5437);
    		ClassRegistration.register(connection);

    	} catch(IOException e) {
    		Logger.logMsg(Logger.ERROR, e.getMessage());
    	}
	}
	
	

	@Test
	public void testLoginUser() {
		// Test valid user
		controller = new LoginController();
		auth = controller.loginUser("kristian", "motorsykkel", connection);
		assertTrue( auth.wasSuccessful() );
		
	}
	
	@Test
	public void testLoginUnvalidUser() {
		// Test Unvalid user
		controller = new LoginController();
		auth = controller.loginUser("kristian", "motorsykke", connection);
		assertFalse(auth.wasSuccessful() );
	}

}
