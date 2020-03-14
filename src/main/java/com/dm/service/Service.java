package com.dm.service;

/**
* <h1>Service</h1>
* 
* This is an interface for different services, this exposes 
* methods like execute,shutdown, getServiceName.
*
* @author  Shalini Prasannakumar
* @version 1.0
* @since   2020-02-11 
*/
public interface Service {
	
	public String getServiceName();
	public void executeService();
	public void shutDownService();

}
