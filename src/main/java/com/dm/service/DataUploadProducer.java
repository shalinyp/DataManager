package com.dm.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dm.service.models.CustOrder;
import com.dm.service.util.Utility;

import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;

/**
 * <h1>DataUploadProducer</h1> This is the producer class which creates the
 * customer and order objects and puts it in the blocking queue
 * 
 *
 * @author Shalini Prasannakumar
 * @version 1.0
 * @since 2020-02-11
 */

public class DataUploadProducer implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUploadProducer.class);

	BlockingQueue<Customer> customerQueue = null;
	BlockingQueue<Order> orderQueue = null;
	BufferedReader bReader = null;

	public DataUploadProducer(BlockingQueue<Customer> customerQueue, BlockingQueue<Order> orderQueue,
			BufferedReader bReader) {
		this.customerQueue = customerQueue;
		this.orderQueue = orderQueue;
		this.bReader = bReader;
	}

	/**
	 * Producer thread each data from the file and converts it into CustOrder object.
	 * The object is then places in the blocking queue.
	 * 
	 * @return void
	 * @param void
	 */
	@Override
	public void run() {

		String readLine = "";
		String header = "";
		int index = 0;
		CustOrder custOrderObj = null;
		Customer cust = null;
		Order order  = null;

		if (bReader == null)
			return;

		try {

			header = bReader.readLine(); // Save the first line (Header)

			while ((readLine = bReader.readLine()) != null) {
				if ((!readLine.equals("")) && (!readLine.equals(header))) {
					CustOrder custOrder = processData(readLine, ++index,custOrderObj,cust,order);
					if (custOrder != null)
						addToQueue(custOrder);
				}
			}

		} catch (IOException | NullPointerException ioe) {
			LOGGER.error("Problem in reading file, cannot proceed" + ioe.getMessage());
			// initiateShutdown

		} finally {
			try {
				bReader.close();
			} catch (IOException ioe) {
				LOGGER.error("BufferedReader could not be closed" + ioe.getMessage());
			}
		}

	}

	/**
	 * Add CustOrder object to queue
	 * 
	 * @return void
	 * @param CustOrder object
	 */
	public void addToQueue(CustOrder custOrder) {

		try {
			//System.out.println("Customer object size  - "+ ObjectSizeFetcher.getObjectSize(new Customer()));
			customerQueue.put(custOrder.getCust());
			//System.out.println("Order object size  - "+ ObjectSizeFetcher.getObjectSize(new Order()));
			orderQueue.put(custOrder.getOrder());
		} catch (InterruptedException ie) {
			LOGGER.error("Item could not be added to the Queue " + ie.getMessage());
		}

	}

	/**
	 * This method takes each line from the csv file and creates Customer and
	 * Order objects out of it.
	 * @param order 
	 * @param cust 
	 * @param custOrderObj 
	 * 
	 * @return void
	 * @param a line from csv file, index for generating order id 
	 */
	private CustOrder processData(String line, int index, CustOrder custOrderObj, Customer cust, Order order) {
			
		
		if(custOrderObj == null)
			custOrderObj = new CustOrder();
		if(cust == null)
			cust = new Customer();
		if(order == null)
			order = new Order();
		try {
			String[] items = line.split(",");
			cust.setId(items[0]);
			cust.setFirstName(items[1]);
			cust.setLastName(items[2]);
			cust.setAge(Integer.parseInt(items[3]));
			cust.setStreet(items[4]);
			cust.setCity(items[5]);
			cust.setState(items[6]);
			cust.setZip(items[7]);

			order.setCustomerId(items[0]);
			order.setId(Utility.getUniqueID('o', index));// UUID.randomUUID().toString()
			String[] money = items[8].split("\\.");
			order.setCurrency(Utility.getCurrencyFromDollar(money[0]));
			order.setCentAmount(Utility.getCentsFromDollar(money[1]));
			order.setPick(items[9]);
			custOrderObj.setCust(cust);
			custOrderObj.setOrder(order);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
			LOGGER.error("Could not create CustOrder object " + e.getMessage());
			custOrderObj = null;
		}
		return custOrderObj;

	}

}
