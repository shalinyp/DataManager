package com.dm.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* <h1>DataUploadService</h1>
* 
* This service is for posting data from client to server
*
* @version 1.0
* @since   2020-02-11 
*/
public class DataUploadService implements Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUploadService.class);

	@Override
	public String getServiceName() {
		return "DataUploadService";
	}

	@Override
	public void executeService() {
		try {
			DataUploader.getInstance().startUpload();
		} catch (InterruptedException | IOException e) {
			LOGGER.error("Unexpected exception has occurred in DataUploadService " + e.getMessage());
		}
	}

	@Override
	public void shutDownService() {
		LOGGER.debug("Shutting down service....");

	}

}
