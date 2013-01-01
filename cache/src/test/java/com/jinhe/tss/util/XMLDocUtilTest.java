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

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import junit.framework.TestCase;

public class XMLDocUtilTest extends TestCase{
 
    public void testMap2DataNode() {
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("tel", new Object[] { "057188889999", "13588899889" });
        map.put("email", "jinpj@g-soft.com.cn");
        map.put("age", new Integer(24));
        map.put("id", new Object[] { new Integer(23), "<![CDATA[sss]]>" });

        Element node = XMLDocUtil.map2DataNode(map, "row");
//        System.out.println(node.asXML());
        
        assertEquals("<row><id><![CDATA[23]]></id><id><![CDATA[&lt;![CDATA[sss]]&gt;]]></id><email><![CDATA[jinpj@g-soft.com.cn]]></email><age><![CDATA[24]]></age><tel><![CDATA[057188889999]]></tel><tel><![CDATA[13588899889]]></tel></row>", node.asXML());

        XMLDocUtil.dataNodes2Map(node);
    }
    
    public void testMap2AttributeNode() {
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", "jinpj@g-soft.com.cn");
        map.put("age", new Integer(24));

        Element node = XMLDocUtil.map2AttributeNode(map, "row");
        
        assertEquals("<row email=\"jinpj@g-soft.com.cn\" age=\"24\"/>", node.asXML());
    }
}

