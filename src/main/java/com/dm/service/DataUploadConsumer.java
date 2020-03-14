package com.dm.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.dm.service.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>DataUploadConsumer</h1> This is a modified consumer class for
 * producer-consumer model. This takes and sends data using a wrapper method for
 * API calls. In case of 503 exception, the unsend data is put back into the
 * queue for retry.
 * 
 *
 * @version 1.0
 * @since 2020-02-11
 */

public class DataUploadConsumer implements /*Callable<Integer>*/ Runnable {

	private static volatile boolean isShuttingDown = false;
	BlockingQueue<Customer> customerQueue = null;
	BlockingQueue<Order> orderQueue = null;
	Client client = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUploadConsumer.class);
	

	public static void initiateShutdown() {
		isShuttingDown = true;
	}

	public DataUploadConsumer(BlockingQueue<Customer> customerQueue, BlockingQueue<Order> orderQueue, Client client) {
		this.customerQueue = customerQueue;
		this.orderQueue = orderQueue;
		this.client = client;
	}

	/**
	 * Runnable method used by different consumer thread to send data to the server.
	 * 
	 */



	@Override
	public void run() {

		List<Customer> custList = new ArrayList<>();
		List<Order> orderList = new ArrayList<>();
		
		// To be removed
				if (myWriter == null) {

					try {
						myWriter = new FileWriter("result.txt");
					} catch (IOException e) {
						System.out.print(e);
					}
				}
				
				
		//////////////////
		while (true) {
			try {

				// Condition indicating that the producer has completed producing all the items
				// and there are no pending items in both the queues to be processed.
				if (customerQueue.isEmpty() && orderQueue.isEmpty() && isShutDown())
					break;
				sendCustomerDataToServer(custList, client);
				sendOrderDataToServer(orderList, client);
				custList.clear();
				orderList.clear();
				// Thread.sleep(1000);
			} catch (Exception e) {
				LOGGER.error("An unexpected exception has occurred " + e.getMessage());

			}
		}
	}

	/**
	 * This method sends Order data to the server, incase of 503 exception the data
	 * is put back into the order queue
	 * 
	 * @param order list and client object
	 * @return void
	 */
	private void sendOrderDataToServer(List<Order> orderList, Client client) {
		ImportResults ordResults;
		try {
			orderQueue.drainTo(orderList, Constants.NUMBER_OF_ITEMS_IN_ONE_BATCH);
			if (!orderList.isEmpty()) {
				ordResults = client.importOrders(orderList);
				//writeResult(ordResults, myWriter, "--------------- OKOrder "+toJsonString(orderList));
			}
		} catch (ServiceUnavailableException se) {
			//writeResult2(orderList, myWriter, "--------------- ErrorOrder");
			LOGGER.debug("Retrying the send of Orders....");
			orderList.forEach(e -> {
				try {
					orderQueue.put(e);
					LOGGER.debug("\n Order " + e.getId());
				} catch (InterruptedException ie) {
					LOGGER.error("Unexpected exception has occurred " + ie.getMessage());
				}
			});

		}
	}

	/**
	 * This method sends Customer data to the server, incase of 503 exception the
	 * data is put back into the customer queue
	 * 
	 * @param customer list and client object
	 * @return void
	 */

	private void sendCustomerDataToServer(List<Customer> custList, Client client) {
		ImportResults custResults;
		try {
			customerQueue.drainTo(custList, Constants.NUMBER_OF_ITEMS_IN_ONE_BATCH);
			if (!custList.isEmpty()) {
				custResults = client.importCustomer(custList);
				System.out.println(custResults);
				writeResult(custResults, myWriter, "--------------- OKCustomer "+toJsonString(custList));
			}

		} catch (ServiceUnavailableException se) {
			//writeResult1(custList, myWriter, "--------------- ErrorCustomer");
			LOGGER.debug("Retrying the send of Customers....");
			custList.forEach(e -> {
				try {
					customerQueue.put(e);
					LOGGER.debug("\n Customer " + e.getId());
				} catch (InterruptedException ie) {
					LOGGER.error("Unexpected exception has occurred " + ie.getMessage());
				}
			});

		}
	}

	/**
	 * Returns the state of producer thread and boolean indicates that producer has
	 * finished producing and consumer can stop once it has finished its task
	 * 
	 * @return boolean
	 */
	public static boolean isShutDown() {
		return isShuttingDown;
	}
	
	

}
