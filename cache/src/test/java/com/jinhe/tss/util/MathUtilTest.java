package com.jinhe.tss.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class MathUtilTest {
	
	@Test
	public void testMathUtil() {
		
		assertTrue( MathUtil.addDoubles(-2.021d, MathUtil.addDoubles(1.02d, 1.001d)) == 0d);
		
		assertTrue(3 == MathUtil.addInteger(1, 2));
		
		assertTrue(1.21d == MathUtil.multiply(1.1d, 1.1d));
		
		int value = MathUtil.randomInt(10);
		
		assertTrue(value >= 0);
		assertTrue(value <= 10);
	}

}
