package com.jinhe.tss.portal.entity;

import junit.framework.Assert;

import org.junit.Test;

import com.jinhe.tss.portal.entity.ThemeInfo.ThemeInfoId;

public class ThemeInfoTest {
	
	@Test
	public void testThemeInfo() {
		ThemeInfoId tiId1 = new ThemeInfoId(1L, 2L);
		ThemeInfoId tiId2 = new ThemeInfoId(1L, 2L);
		ThemeInfoId tiId3 = new ThemeInfoId(2L, 2L);
		
		Assert.assertTrue(tiId1.equals(tiId2));
		Assert.assertFalse(tiId1.equals(tiId3));
		
		Assert.assertTrue(tiId1.hashCode() == tiId2.hashCode());
		
		Assert.assertNotNull(tiId1.getThemeId());
	}
}
