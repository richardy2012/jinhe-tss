package com.jinhe.tss.cms.helper;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class ImageProcessorTest {
	
	@Test
	public void testResize() {
		try {
			String filePath = "D:\\Temp\\cms\\1.jpg";
			ImageProcessor imageProcessor = new ImageProcessor(filePath);
			imageProcessor.resize(0.68);
		} 
		catch (IOException e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}
}
