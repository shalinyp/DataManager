/**
 * 
 */
package com.dm.service;


import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dm.service.DataUploadProducer;
import com.dm.service.models.CustOrder;


public class DataUploadProducerTest {

	/**
	 * @throws java.lang.Exception
	 */
	 BlockingQueue<Customer> testCustQueue = null;
	 BlockingQueue<Order> testOrderQueue = null;
	 Customer testCust = null;
	 Order testOrder  = null;
	 CustOrder testCustOrder = null;
	 BufferedReader br= null;
	 static String csvStr = "";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}
	
	@Before
	public void setUpBefore() throws Exception {
		
		testCustQueue = new ArrayBlockingQueue(2);
		testOrderQueue = new ArrayBlockingQueue(2);

		testCust = new Customer();
    	testOrder = new Order();
    	testCustOrder = new CustOrder();

	}


    @Test
    public void addingAnItemToQueueShouldIncreaseItsSizeByOne() {

    	testCustOrder.setCust(testCust);
    	testCustOrder.setOrder(testOrder);
    	DataUploadProducer testDataUploadProducer = new DataUploadProducer(testCustQueue,testOrderQueue,br);
    	testDataUploadProducer.addToQueue(testCustOrder);
    	Assert.assertEquals(1,testCustQueue.size());
    	Assert.assertEquals(1,testOrderQueue.size());
    }
    
    @Test
    public void nullCustOrderObjectShouldNotBeAddedToQueue() throws InterruptedException {
    	
    	Thread testProducerThread = new Thread(new DataUploadProducer(testCustQueue, testOrderQueue,br));
    	testProducerThread.start();
    	testProducerThread.join();
    	Assert.assertEquals(0,testCustQueue.size());
    	Assert.assertEquals(0,testOrderQueue.size());
    	
    }
    
    @Test
    public void invalidCustObjectShouldNotBeAddedToQueue() throws InterruptedException {
    	
    	csvStr = "customerId,first,last,age,street,city,state,zip,dollar,pick\r\n" + 
				"1,Emma,Silva,6,Irupota,GA,04563,WHITE";
    	br = new BufferedReader(new StringReader(csvStr));
    	Thread testProducerThread = new Thread(new DataUploadProducer(testCustQueue, testOrderQueue,br));
    	testProducerThread.start();
    	testProducerThread.join();
    	Assert.assertEquals(0,testCustQueue.size());
    	Assert.assertEquals(0,testOrderQueue.size());
    	
    }
    
    @Test
    public void validCustObjectShouldBeAddedToQueue() throws InterruptedException {
    	
    	csvStr = "customerId,first,last,age,street,city,state,zip,dollar,pick\r\n" + 
				"1,Emma,Silva,6,Nuteb Extension,Irupota,GA,04563,$456.34,WHITE";
    	br = new BufferedReader(new StringReader(csvStr));
    	Thread testProducerThread = new Thread(new DataUploadProducer(testCustQueue, testOrderQueue,br));
    	testProducerThread.start();
    	testProducerThread.join();
    	Assert.assertEquals(1,testCustQueue.size());
    	Assert.assertEquals(1,testOrderQueue.size());
    	
    }
    
    @Test
    public void whenQueueIsEmptyProducerThreadStops() throws InterruptedException
    {
    	csvStr = "customerId,first,last,age,street,city,state,zip,dollar,pick" ;
    	br = new BufferedReader(new StringReader(csvStr));
    	Thread testProducerThread = new Thread(new DataUploadProducer(testCustQueue, testOrderQueue,br));
    	testProducerThread.start();
    	testProducerThread.join();
    	Assert.assertEquals(true, testCustQueue.isEmpty());
    	Assert.assertEquals(true, testOrderQueue.isEmpty());
    	Assert.assertEquals(false,testProducerThread.isAlive());    	
    }
    
    
	


}
