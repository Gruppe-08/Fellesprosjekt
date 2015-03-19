package tests;

import static org.junit.Assert.*;
import models.User;

import org.junit.Test;

import util.UserUtil;

public class UserUtilTest {

	@Test
	public void testIsValidUser() {
		User user = new User("kristian", "Kristian", "Aurlien", true);
		Boolean result = UserUtil.isValidUser(user);
		assertTrue(result);
		
		user = new User();
		result = UserUtil.isValidUser(user);
		assertFalse(result);
	}
	
	@Test
	public void testIsValidPassword() {
		assertTrue(UserUtil.isValidPassword("123456"));
		assertFalse(UserUtil.isValidPassword("12345"));
	}
	
	@Test
	public void testIsMatchingPasswords(){
		assertTrue(UserUtil.isMatchingPasswords("kebab1","kebab1"));
		assertFalse(UserUtil.isMatchingPasswords("kebab1","burger1"));

	}
	
	

}
