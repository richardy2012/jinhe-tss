package com.jinhe.tss.um.sso.online;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.online.OnlineUser;

/** 
 * <p> 在线用户信息 </p> 
 * 
 */
@Entity
@Table(name="online_user")
@SequenceGenerator(name = "online_user_sequence", sequenceName = "online_user_sequence", initialValue = 1000, allocationSize = 10)
public class DBOnlineUser extends OnlineUser implements IEntity{
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "online_user_sequence")
	private Long id;
    
    private Date   loginTime;
    private String clientIp;
    private String userName;
    
    public DBOnlineUser(){
        
    }
    
    public DBOnlineUser(Long userId, String sessionId, String appCode, String token, String userName) {
        super(userId, appCode, sessionId, token);
        
        this.loginTime = new Date();
        this.clientIp = Environment.getClientIp();
        this.userName = userName;
    }
 
    
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + this.id.hashCode();
        hash = hash * 31 + this.userId.hashCode();
        hash = hash * 31 + this.token.hashCode();

        return hash;
    }

    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if (!(obj instanceof DBOnlineUser)) {
            return false;
        }
        DBOnlineUser temp = (DBOnlineUser) obj;
        return temp.id == null ? this.id == null : temp.id.equals(this.id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
 
}
