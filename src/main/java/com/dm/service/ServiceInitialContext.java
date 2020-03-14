package com.dm.service;

/**
 * <h1>ServiceInitialContext</h1>
 * 
 * This is a service lookup class.
 *
 * @version 1.0
 * @since 2020-02-11
 */
public class ServiceInitialContext {
	
	public Object lookup(String serviceName) {

		if (serviceName.equalsIgnoreCase("DataUploadService")) {
			return new DataUploadService();
		}
		return null;
	}
}
