package com.jinhe.tss.framework.sso.online;

import javax.persistence.MappedSuperclass;

/**
 * <p> 在线用户库里的用户 </p>
 * 
 * 同一用户对应到不同的应用、不同的SessionID，在在线用户库里都被视为不同的在线用户，
 * 但它们用可能共享一个相同的token（比如同一用户、不同的应用的情况下）。
 */
@MappedSuperclass
public class OnlineUser {
    
    /** 用户编号 */
    protected Long userId;

    /** 应用Code  */
    protected String appCode;

    /** Session编号  */
    protected String sessionId;

    /** 令牌：包含标准用户ID、平台SessionID和令牌的生成时间 */
    protected String token;
    
    public OnlineUser() {
        
    }
 
    public OnlineUser(Long userId, String appCode, String sessionId, String token) {
        this.userId = userId;
        this.appCode = appCode;
        this.sessionId = sessionId;
        this.token = token;
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((appCode == null) ? 0 : appCode.hashCode());
        result = PRIME * result + ((sessionId == null) ? 0 : sessionId.hashCode());
        result = PRIME * result + ((token == null) ? 0 : token.hashCode());
        result = PRIME * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if ( !(obj instanceof OnlineUser))
            return false;
        
        final OnlineUser other = (OnlineUser) obj;
        if (appCode == null) {
            if (other.appCode != null)
                return false;
        } else if (!appCode.equals(other.appCode))
            return false;
        
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        } else if (!sessionId.equals(other.sessionId))
            return false;
        
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        
        return true;
    }
    
    public String toString() {
        return userId + ":" + appCode + ":" + sessionId + ":" + token;
    }

    public String getAppCode() {
        return appCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getToken() {
        return token;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
