package com.jinhe.tss.util;

import java.io.File;

import org.junit.Test;

public class FileHelperTest {
    
	@Test
    public void testFileHelper() throws Exception {
         System.out.println(File.pathSeparatorChar);
         System.out.println(File.separatorChar);
         System.out.println(File.pathSeparator);
         System.out.println(File.separator);
    }

}
