package model;

import models.User;
import models.Warning;

import org.junit.Test;

import util.BaseUnitTest;

public class UserDataModelTest extends BaseUnitTest {

	@Test
	public void testInsertFind() {
		String userName = "Lee", password = "password";
		User test = new User(userName, password);
		test.save();
		
		User resp = User.findById(test.id);
		assertNotNull(resp);
		assertEquals(userName, resp.userName);
		assertEquals(password, resp.password);
		
	}
}
