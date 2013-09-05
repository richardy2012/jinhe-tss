package com.jinhe.tss.framework.license;

import java.util.Date;
import java.util.Properties;

import com.jinhe.tss.util.ConfigurableContants;
import com.jinhe.tss.util.DateUtil;
import com.jinhe.tss.util.EasyUtils;

/** 
 * <p> 授权许可 </p> 
 * 
 */
public final class License extends ConfigurableContants {
    
    String product;          //产品
    String version;          //版本号
    String macAddress;       //安装的服务器Mac地址
    Date   expiresDate;      //过期时间
    String licenseSignature; //license签名
    
    LicenseType licenseType; //license类型
    
    static Properties licenseProperties;
    
    /**
     * 把license的属性值拼成一个字符串，然后转身字节数组
     */
    public byte[] getFingerprint() {
        StringBuffer buf = new StringBuffer(100);
        buf.append(product).append(version).append(licenseType);
        if(macAddress != null) {
            buf.append(macAddress);
        }
        if(expiresDate != null) {
            buf.append(DateUtil.format(expiresDate));
        }
        return buf.toString().getBytes();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        buf.append("product=" + product).append("\n");
        buf.append("version=" + version).append("\n");
        buf.append("type=" + licenseType).append("\n");
        if(expiresDate != null) {
            buf.append("expiry=" + DateUtil.format(expiresDate)).append("\n");
        }
        if(macAddress != null && macAddress.length() > 0) {
            buf.append("macaddress=" + macAddress).append("\n");
        }
        buf.append("signature=" + licenseSignature);
        return buf.toString();
    }

    /**
     * 读取许可文件
     * @param fileName
     */
    public static License fromConfigFile(String fileName) throws Exception {

    	licenseProperties = init(fileName);
        
        License license = new License();
        license.product = licenseProperties.getProperty("product");
        license.version = licenseProperties.getProperty("version");
        license.licenseSignature = licenseProperties.getProperty("signature");
        
        String type = licenseProperties.getProperty("type");
        if( !EasyUtils.isNullOrEmpty(type) ) {
            license.licenseType = new LicenseType(type);
        }
        
        String expiry = licenseProperties.getProperty("expiry");
        if( !EasyUtils.isNullOrEmpty(expiry) ) {
            license.expiresDate = DateUtil.parse(expiry);
        }
        
        String macAddress = getProperty("macaddress");
        if( !EasyUtils.isNullOrEmpty(macAddress) ) {
            license.macAddress = macAddress;
        }
        
        return license;
    }
    
    /**
     * 授权许可类型。
     */
    static final class LicenseType {

        /** 非商业的 */
        static final LicenseType NON_COMMERCIAL = new LicenseType("Non-Commercial");
        
        /** 商业的 */
        static final LicenseType COMMERCIAL = new LicenseType("Commercial"); 
        
        /** 评测的 */
        static final LicenseType EVALUATION = new LicenseType("Evaluation");  
        
        /** 高级的 */
        static final LicenseType ADVANCED = new LicenseType("Advanced");  

        final String name;
        
        LicenseType(String name) {
            this.name = name;
        }

        public String toString()  {
            return name;
        }
    }
}