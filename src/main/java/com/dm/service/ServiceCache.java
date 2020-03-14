package com.dm.service;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>ServiceCache</h1> 
 * This class is a cache for all the service objects
 *
 * @version 1.0
 * @since 2020-02-11
 */
public class ServiceCache {

   private List<Service> services;

   public ServiceCache(){
      services = new ArrayList<>();
   }

   public Service getService(String serviceName){
   
      for (Service service : services) {
         if(service.getServiceName().equalsIgnoreCase(serviceName)){
            return service;
         }
      }
      return null;
   }

   public void addService(Service newService){
      boolean exists = false;
      
      for (Service service : services) {
         if(service.getServiceName().equalsIgnoreCase(newService.getServiceName())){
            exists = true;
         }
      }
      if(!exists){
         services.add(newService);
      }
   }
}
