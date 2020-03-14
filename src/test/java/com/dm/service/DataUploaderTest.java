package com.dm.service;

import org.junit.BeforeClass;
import org.junit.Test;

import com.dm.service.ServiceLocator;

public class DataUploaderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testStartUpload() {
		// Uncomment this to start execution
		 ServiceLocator.getService("DataUploadService").executeService();

	}

}
