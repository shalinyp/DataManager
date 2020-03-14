package com.dm.service;

/**
 * <h1>ServiceLocator</h1>
 * 
 * This is a singleton class through which the clients access the services.
 *
 * @version 1.0
 * @since 2020-02-11
 */
public class ServiceLocator {
	   private static ServiceCache cache;

	   static {
	      cache = new ServiceCache();		
	   }
	   private ServiceLocator() {
	   }

	   public static Service getService(String serviceName){

	      Service service = cache.getService(serviceName);

	      if(service != null){
	         return service;
	      }

	      ServiceInitialContext context = new ServiceInitialContext();
	      Service serviceObj = (Service)context.lookup(serviceName);
	      cache.addService(serviceObj);
	      return serviceObj;
	   }
	}
