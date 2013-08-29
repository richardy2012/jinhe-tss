package com.jinhe.tss.util;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FileHelperTest {
	
	public static String CK_FILE_PATH = "log4j.properties";
    
	@Test
    public void testFileHelper() throws Exception {
         System.out.println(File.pathSeparatorChar);
         System.out.println(File.separatorChar);
         System.out.println(File.pathSeparator);
         System.out.println(File.separator);
         
         URL url = URLUtil.getResourceFileUrl(CK_FILE_PATH);
         String log4jPath = url.getPath(); 
         File classDir = new File(log4jPath).getParentFile();
         
         Assert.assertTrue(FileHelper.checkFile(classDir, CK_FILE_PATH));
         
         File tempDir1 = FileHelper.createDir(classDir + "/temp1");
         File tempDir2 = FileHelper.createDir(classDir + "/temp2");
         File tempFile1 = new File(tempDir1.getPath() + "/1.txt");
         File tempFile2 = new File(tempDir2.getPath() + "/2.txt");
         
         FileHelper.writeFile(tempFile1, "111111111111");
         FileHelper.writeFile(tempFile2, "222222222222");
         
         FileHelper.copyFile(tempDir2, tempFile1);
         
         FileHelper.copyFolder(tempDir2, tempDir1);
         
         FileHelper.exportZip(tempDir2.getPath(), tempDir1);
         
         FileHelper.findPathByName(tempDir2, "1.txt");
         
         FileHelper.getFileNameNoSuffix("1.txt");
         FileHelper.getFileSuffix("1.txt");
         
         FileHelper.listFileNamesByTypeDeeply("txt", tempDir2);
         FileHelper.listFiles(tempDir2);
         FileHelper.listFilesDeeply(tempDir2);
         FileHelper.listSubDir(tempDir2);
         
         FileHelper.readFile(tempFile1);
         FileHelper.readFile(tempFile1, "UTF-8");
         
         FileHelper.zip(tempDir2);
         List<String> fileList = FileHelper.listFilesByType("zip", classDir);
         Assert.assertEquals(1, fileList.size());
         
		 String zipPath = fileList.get(0);
         File zipFile = new File(classDir + "/" + zipPath);
		 FileHelper.upZip(zipFile);
         
         FileHelper.wirteOldFile(tempFile1.getPath(), tempDir2, "2.txt");
         
         FileHelper.renameFile(tempFile1.getPath(), "1_1.txt");
         FileHelper.deleteFile(tempDir1);
         FileHelper.deleteFile(tempDir2);
         FileHelper.deleteFile(zipFile);
    }
}
