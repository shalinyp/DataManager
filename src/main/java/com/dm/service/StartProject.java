package com.dm.service;

public class StartProject {

	public static void main(String[] args) {
		 ServiceLocator.getService("DataUploadService").executeService();

	}

}
