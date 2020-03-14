package com.dm.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>Utility</h1>
 * 
 * Utility class for the application
 *
 * @version 1.0
 * @since 2020-02-11
 */
public class Utility {

	private static final Currency currency = Currency.getInstance("USD");
	private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
	private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);

	private Utility() {
	}

	/** 
	 * Yet to implement
	 * Converts money string to Currency
	 * 
	 * @return Currency
	 * @param String
	 */
	public static Currency getCurrencyFromDollar(String money) {
		return currency;
	}

	/**
	 * Removes currency symbol from string
	 * 
	 * @return String
	 * @param String
	 */
	public static String removeCurrencySymbol(String str) {
		if (str.charAt(0) == '$')
			return str.substring(1);
		return str;
	}

	/**
	 * Removes cent from string
	 * 
	 * @return long
	 * @param String
	 * @throws NumberFormatException
	 */
	public static long getCentsFromDollar(String centStr) throws NumberFormatException {
		// long noFractionDigits = currency.getDefaultFractionDigits();
		return Long.parseLong(centStr);

	}

	/**
	 * Generates unique id
	 * 
	 * @return String
	 * @param char,index
	 */
	public static String getUniqueID(char ch, int index) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return "" + ch + timestamp.getTime() + index;

	}

	/**
	 * Reads from a file and returns an object of BufferedReader
	 * 
	 * @return BufferedReader object
	 * @param fileName
	 */
	public static BufferedReader readFromFile(String fileName) throws IOException {
		BufferedReader br = null;
		try {
			br = Files.newBufferedReader(Paths.get(fileName));
		} catch (NoSuchFileException fe) {
			LOGGER.error(" The file is missing in the " + fileName + "path" + fe.getMessage());
			throw fe;
		} catch (IOException ioe) {
			LOGGER.error(" There is some problem with the file reading " + ioe.getMessage());
			throw ioe;

		}
		return br;
	}
}
