package com.jinhe.dm.data.util;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class _DateUtilTest {
	
	@Test
	public void test() {
		Date day1 = _DateUtil.today();
		Date day2 = _DateUtil.addDays(day1, 10);
		Date day3 = _DateUtil.subDays(day1, 10);
		
		Assert.assertEquals(21, _DateUtil.daysBetweenFromAndTo(day3, day2).size());
		
		_DateUtil.getDay(day1);
		int month = _DateUtil.getMonth(day1);
		int year = _DateUtil.getYear(day1);
		
		Date day4 = _DateUtil.noHMS(new Date());
		Assert.assertEquals(0, _DateUtil.getHour(day4));
		
		_DateUtil.isMonthEnd(day1);
		
		Assert.assertEquals("202006", _DateUtil.toYYYYMM(2020, 6));
		
		Assert.assertEquals(_DateUtil.toYYYYMM(year, month),
				_DateUtil.toYYYYMM(day1));
	}

}
