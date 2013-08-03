/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
*/

package com.jinhe.tss.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmlUtilTest {
 
	@Test
    public void testToXmlForm() {
        assertEquals("&lt;qqq&amp;www\\&gt;", XmlUtil.toFormXml("<qqq&www\\>"));
    }
    
	@Test
    public void testReplaceXMLPropertyValue() {
        assertEquals("&lt;qqq&amp;www\\&gt;", XmlUtil.replaceXMLPropertyValue("<qqq&www\\>"));
    }
    
	@Test
    public void testStripNonValidXMLCharacters() {
        System.out.print(XmlUtil.stripNonValidXMLCharacters("<server code=\"TSS\" userDepositoryCode=\"tss\" />"));
    }
}

