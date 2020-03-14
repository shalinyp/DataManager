package com.dm.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dm.service.util.Constants;
import com.dm.service.util.Utility;

/**
* <h1>DataUploader</h1>
* 
* DataUploader is a singleton which triggers data upload.
* It creates producer and consumer threads.
*
* @version 1.0
* @since   2020-02-11 
*/
public class DataUploader {

	private static DataUploader dataUploaderServiceImplObj = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataUploader.class);

	private DataUploader() {
	}
	

  /**
   * Gets a singleton object of the class
   * @return DataUploader object
   */

	public static DataUploader getInstance() {

		if (dataUploaderServiceImplObj == null)
			return new DataUploader();
		else
			return dataUploaderServiceImplObj;

	}
    

  /**
   * Creates a blocking queue of size as mentioned in the constant file. 
   * Creates and starts one producer thread and multiple consumer threads.
   * The number of the threads can be configured using the constant file.
   * 
   * @return DataUploader object
   * @throws InterruptedException, IOException
   */
	
	public void startUpload() throws InterruptedException, IOException {

		BlockingQueue<Customer> customerQueue = new ArrayBlockingQueue<>(Constants.SIZE_OF_QUEUE);																											
		BlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(Constants.SIZE_OF_QUEUE);
		
		BufferedReader br = Utility.readFromFile(Constants.FILE_PATH);
		Thread producer = new Thread(new DataUploadProducer(customerQueue, orderQueue,br));
		DataUploadConsumer dataUploadConsumer = new DataUploadConsumer(customerQueue, orderQueue,client);

		ExecutorService executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_CONSUMER_THREADS);
		Future f1 = null;
		for (int i = 1; i <= Constants.NUMBER_OF_CONSUMER_THREADS; i++) {
			 f1 = executorService.submit(dataUploadConsumer);
		}
		producer.start();
		while (true) {
			if (!producer.isAlive()) {
				DataUploadConsumer.initiateShutdown();
				break;
			}
		}
		// Consumer threads shutdown
		while (true) {
			if (customerQueue.isEmpty() && orderQueue.isEmpty() && DataUploadConsumer.isShutDown()) {
				executorService.shutdown();
				try {
					while (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
						LOGGER.debug("Still waiting for termination");
					}
				} catch (InterruptedException e) {
					LOGGER.error("Unexpected exception has occurred");
					throw e;
				}
				break;
			}
		}
		

}
