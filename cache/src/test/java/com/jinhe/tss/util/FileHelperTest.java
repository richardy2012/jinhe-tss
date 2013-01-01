package com.jinhe.tss.util;

import java.io.File;

import junit.framework.TestCase;

public class FileHelperTest extends TestCase {
    
    public void testFileHelper() throws Exception {
         System.out.println(File.pathSeparatorChar);
         System.out.println(File.separatorChar);
         System.out.println(File.pathSeparator);
         System.out.println(File.separator);
         
//         FileHelper.zip("D:/temp/cms");
//         
//         FileHelper.upZip(new File("D:/temp/1/cms.zip"));
        
        // createDir("c:/test2/test/test.txt");
        // FileHelper.copyFolder("D:\\Temp", "D:\\Temp2");
        // FileHelper.deleteFile(new File("D:\\Temp2"));
        // new File("D:\\Temp2").delete();
        // FileHelper.listFilesByType("", new File("D:\\Temp2"));
        
//        FileHelper.exportZip("D:/Temp/temp", new File("D:\\Temp\\Portal"));
//        System.out.println(FileHelper.getFileSuffix("portal.zip"));
//        System.out.println(FileHelper.getFileNameNoSuffix("portal.zip"));
//        FileHelper.unZip(new File("D:/Temp/temp/portal.zip"), "D:/Temp/temp");
        
//        List<File> list = FileHelper.listFilesByTypeDeeply(".xml", new File("G:/cms/demo/jh"));
//        for(int i = 0; i < list.size(); i++){
//            System.out.println(i + "----" +list.get(i));
//        }
    }

}
