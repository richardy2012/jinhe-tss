package com.jinhe.tss.framework;

import com.jinhe.tss.framework.img.ImageCodeAPI;

public class ImageCodeAPITest {

    public static void main(String[] args) {
        String msg = "123456789";
        String path = "barcode.png";
        ImageCodeAPI.generateFile(msg, path);
    }
    
}
