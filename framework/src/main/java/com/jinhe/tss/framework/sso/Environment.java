package com.jinhe.tss.framework.sso;

import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;

/**
 * <p>
 * 环境变量对象：利用线程变量，保存运行时用户信息等参数
 * </p>
 */
public class Environment {
    /**
     * <p>
     * 获取用户ID信息
     * </p>
     * @return
     */
    public static Long getOperatorId() {
        IdentityCard card = Context.getIdentityCard();
        if (card == null) {
            return null;
        }
        return card.getOperator().getId();
    }

    /**
     * <p>
     * 获取用户账号（LoginName）
     * </p>
     * @return
     */
    public static String getOperatorName() {
        IdentityCard card = Context.getIdentityCard();
        if (card == null) {
            return null;
        }
        return card.getLoginName();
    }
    
    public static Object getOperatorInfo(String field) {
        IdentityCard card = Context.getIdentityCard();
        if ( card != null && card.getOperator() != null ) {
        	return card.getOperator().getAttributesMap().get(field);
        }
        return null;
    }

    /**
     * <p>
     * 获取当前SessionID
     * </p>
     * @return
     */
    public static String getSessionId() {
        RequestContext requestContext = Context.getRequestContext();
        if (requestContext == null) {
            return null;
        }
        return requestContext.getSessionId();
    }

    /**
     * <p>
     * 获取令牌
     * </p>
     * @return
     */
    public static String getToken() {
        return Context.getToken();
    }

    /**
     * 获取用户姓名
     * @return
     */
    public static String getUserName() {
        IdentityCard card = Context.getIdentityCard();
        if (card == null) {
            return null;
        }
        return card.getUserName();
    }

    /**
     * <p>
     * 获取用户客户端IP
     * </p>
     * @return
     */
    public static String getClientIp() {
        RequestContext requestContext = Context.getRequestContext();
        if (requestContext == null) {
            return null;
        }
        return requestContext.getClientIp();
    }
    
    /**
     * <p>
     * 获取应用系统上下文根路径(即发布路径)，一般为"/tss"
     * </p>
     * @return
     */
    public static String getContextPath(){
        return Context.getApplicationContext().getCurrentAppServer().getPath();
    }
}
