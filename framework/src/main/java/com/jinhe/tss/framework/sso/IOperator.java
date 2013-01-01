package com.jinhe.tss.framework.sso;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 用户信息数据对象接口
 * </p>
 */
public interface IOperator extends Serializable {

    /**
     * <p>
     * 获取用户所有属性集合
     * </p>
     * @return
     */
    public Map<String, Object> getAttributesMap();

    /**
     * <p>
     * 获取用户相关属性
     * </p>
     * @param name
     * @return
     */
    public Object getAttribute(String name);

    /**
     * <p>
     * 获取用户ID
     * </p>
     * @return
     */
    public Long getId();

    /**
     * <p>
     * 获取用户登录名
     * </p>
     * @return
     */
    public String getLoginName();

    /**
     * <p>
     * 获取用户名
     * </p>
     * @return
     */
    public String getUserName();

    /**
     * <p>
     * 获取用户认证方式：用户身份识别类全类名
     * </p>
     * @return
     */
    public String getAuthenticateMethod();

    /**
     * <p>
     * 是否匿名用户
     * </p>
     * @return
     */
    public boolean isAnonymous();
}
