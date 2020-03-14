package com.dm.service;


import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dm.service.util.Utility;

public class UtilityTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	//@Test(expected = FileNotFoundException.class)
	@Test
	public void throwsFileNotFoundExceptionWhenGivenFileIsMissing() throws IOException {
		thrown.expect(NoSuchFileException.class);
		Utility.readFromFile("mockFile.csv");
	}
	
	@Test
	public void removeCurrencySymbolShouldReturnValueWithoutSymbol() {
		Assert.assertEquals("123",Utility.removeCurrencySymbol("$123"));
	}
	@Test
	public void removeCurrencySymbolShouldReturnSameValueOnGivingInputWithoutSymbol() {
		Assert.assertEquals("123",Utility.removeCurrencySymbol("123"));
	}


}
