/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */

package com.jinhe.tss.framework.license;

import java.io.File;

import junit.framework.TestCase;

import com.jinhe.tss.util.FileHelper;

public class LicenseTest extends TestCase {
    
    public void testLicense() {
        
        // 第一步：生成公钥、私钥对。公钥公开，注意保管好私钥（如果泄露，则有可能被hacker随意创建license）。
        try {
            LicenseFactory.generateKey();
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
        
        // 第二步：根据产品、版本、Mac地址、有效期等信息，签名产生注册号，并将该注册号复制到license文件中。
        License license = null;
        try {
            license = License.fromConfigFile("cpu.license");
            LicenseFactory.sign(license);
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
        
        FileHelper.writeFile(new File(LicenseFactory.LICENSE_DIR + "/cpu.license"), license.toString());
        System.out.println(license);
        
        // 第三步：利用公钥对license进行合法性验证。可以在软件代码的重要模块中加入下面的验证，比如登录模块等。
        LicenseManager manager = LicenseManager.getInstance();
        assertEquals(true, manager.validateLicense(license.product, license.version));
        assertEquals("Commercial", manager.getLicenseType(license.product, license.version));
    }
}

