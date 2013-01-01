package com.jinhe.tss.util;

import junit.framework.TestCase;

public class InfoEncoderTest extends TestCase {
    
    public void testInfoEncoder() {
        InfoEncoder test = new InfoEncoder();
        System.out.println(test.createEncryptor("Jon.King"));
        System.out.println(test.createDecryptor("TcftExflLdPiFfNNVkG9JQ=="));
        
        String md5PWD = InfoEncoder.string2MD5("Admin_123456");
        System.out.println(md5PWD);
        assertEquals("E5E0A2593A3AE4C038081D5F113CEC78", md5PWD);
    }

}
