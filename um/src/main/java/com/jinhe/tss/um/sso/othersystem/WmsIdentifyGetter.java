package com.jinhe.tss.um.sso.othersystem;

import java.sql.Connection;
import java.util.Properties;

import com.jinhe.tss.framework.persistence.connpool.DBHelper;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.IdentityGetter;
import com.jinhe.tss.um.sso.UMIdentityGetter;
import com.jinhe.tss.util.InfoEncoder;
 
public class WmsIdentifyGetter extends UMIdentityGetter implements IdentityGetter {
    
	/**
     * 判断用户输入的密码是否和第三方系统（WMS）密码的一致，如果是，则将用户当前的密码也设置为该密码。
     * 
     * @param operator
     * @param password
     * @return
     */
    public boolean indentify(IPWDOperator operator, String password) {
        log.debug("用户登陆时密码在主用户组中验证不通过，转向WMS进行再次验证。");
        
        String loginName = operator.getLoginName().toUpperCase();
        password = InfoEncoder.string2MD5(password).toLowerCase();
        
        Properties p = new Properties();
        p.setProperty(DBHelper.DB_CONNECTION_DRIVER_CLASS, "oracle.jdbc.driver.OracleDriver");
        p.setProperty(DBHelper.DB_CONNECTION_URL, "jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 10.8.73.139)(PORT = 1521)) (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = wms)))");
        p.setProperty(DBHelper.DB_CONNECTION_USERNAME, "wms");
        p.setProperty(DBHelper.DB_CONNECTION_PASSWORD, "wms800best");
        
        String sql = "select id from GV_SYS_ACCOUNT t " +
        		" where upper(t.loginName) = ? and t.password = ? and t.status_id = 3";
 
        Connection connection = DBHelper.getConnection(p);
        try {
        	DBHelper.executeQuerySQL(connection , sql, loginName, password);
        	log.debug("用户【" + loginName + "】的密码在WMS中验证通过。");
            return true; // 如果连接成功则返回True
        }
        catch(Exception e) {
        	log.warn("用户【" + loginName + "】的密码在WMS中验证不通过。");
            return false;
        }
    }
}
