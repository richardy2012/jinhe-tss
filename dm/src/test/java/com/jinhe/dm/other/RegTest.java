package com.jinhe.dm.other;

import java.util.Date;
import java.util.regex.Pattern;

import org.junit.Test;

import com.jinhe.tss.util.DateUtil;

public class RegTest {

	@Test
  	public void test() {
  		String paramValue = "today - 7"; // "2012-12-12"
  		Date dateObj;
  		if (Pattern.compile("^today[\\s]*-[\\s]*\\d").matcher(paramValue).matches()) {
  			int deltaDays = Integer.parseInt(paramValue.split("-")[1].trim());
  			Date today = DateUtil.noHMS(new Date());
  			dateObj = DateUtil.subDays(today, deltaDays);
  		} 
  			else {
  				try {
  					dateObj = DateUtil.parse(paramValue);
  				} catch(Exception e) {
  					dateObj = null;
  				}
  			}
  			
  		System.out.println(dateObj);
  	}
  	
}
