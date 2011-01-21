package model;

import models.SensorType;

import org.junit.Assert;
import org.junit.Test;

import util.BaseUnitTest;

public class SensorTypeTest  extends BaseUnitTest {
	
	@Test
	public void testVirtualSetting(){
		
		Assert.assertTrue(SensorType.ERROR.isVirtual());
	}

}
