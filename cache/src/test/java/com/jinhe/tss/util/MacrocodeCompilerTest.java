package com.jinhe.tss.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MacrocodeCompilerTest {
    
	@Test
    public void testMacrocodeCompiler() {
        String code = "<table id=\"${portlet.id}\" description=\"修饰器\">"
            + "             <tr>"
            + "                 <td>#{title}</td>"
            + "             </tr>"
            + "             <tr>    "
            + "                 <td>${js.content}</td>"
            + "             </tr>"
            + "         </table>${ignore}";

        Map<String, Object> macro = new HashMap<String, Object>();
        macro.put("${portlet.id}", "1001");
        macro.put("#{title}", new Object() {
                                public String toString() {
                                    return "HTML5可以做五件事情， 超出你的想象";
                                }
                            });
        macro.put("${d}", "|test");
        macro.put("${js.content}", "作为下一代的网页语言，HTML5 拥有很多让人期待已久的新特性，它可以说是近十年来 Web 标准最巨大的飞跃。");
        
//        System.out.println(MacrocodeCompiler.run(code, macro));
        
        String result = "<table id=\"1001\" description=\"修饰器\">" +
        		"             <tr>" +
        		"                 <td>HTML5可以做五件事情， 超出你的想象</td>" +
        		"             </tr>" +
        		"             <tr>" +
        		"                     <td>作为下一代的网页语言，HTML5 拥有很多让人期待已久的新特性，它可以说是近十年来 Web 标准最巨大的飞跃。</td>" +
        		"             </tr>" +
        		"         </table>";
        assertEquals( result, MacrocodeCompiler.run(code, macro, false) );
        assertEquals( result + "${ignore}", MacrocodeCompiler.run(code, macro) );
        
        assertEquals( "test|test", MacrocodeCompiler.run("test${d}", macro));
    }

}
