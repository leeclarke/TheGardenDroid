package model;

import java.util.List;

import models.UserDataType;

import org.junit.Test;

import util.BaseUnitTest;

public class UserDataTypeTest  extends BaseUnitTest {

	@Test
	public void createRetrieve() {
		String typeName = "Plant Height", desc = "This is a test";
		UserDataType test = new UserDataType(typeName, desc);
		test.save();
		
		UserDataType resp = UserDataType.findById(test.id);
		assertNotNull(resp);
		assertEquals(typeName, resp.name);
		assertEquals(desc,resp.description);
		
		
	}
	
	@Test
	public void testFetchActive() {
		List<UserDataType> resp = UserDataType.fetchActiveDataTypes();
		assertNotNull(resp);
		assertTrue(resp.size() >0);
		
	}
}
