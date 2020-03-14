package com.dm.service;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.dm.service.DataUploadConsumer;
import com.dm.service.models.CustOrder;



@RunWith(MockitoJUnitRunner.class)
public class DataUploadConsumerTest {

	@Spy
	DataUploadConsumer dataUploadConsumerMock = mock(DataUploadConsumer.class);
	
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
	public void consumerThreadShouldStopWhenQueueIsEmptyAndProducerThreadHasStopped() {
		when(dataUploadConsumerMock.isShutDown()).thenReturn(true);
		
		dataUploadConsumerMock = new DataUploadConsumer(testCustQueue,testOrderQueue,testClient);
		ExecutorService testExecutorConsumerThread = Executors.newSingleThreadExecutor();
		testExecutorConsumerThread.submit(dataUploadConsumerMock);
		Assert.assertEquals(true, testExecutorConsumerThread.isTerminated());
	}

}
