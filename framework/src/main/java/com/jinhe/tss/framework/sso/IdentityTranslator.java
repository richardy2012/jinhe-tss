package com.jinhe.tss.framework.sso;

/**
 * <p>
 * 用户身份转换器，根据不同应用间用户映射关系实现用户身份转换
 * </p>
 */
public interface IdentityTranslator {

    /**
     * <p>
     * 根据标准用户Id获取当前系统对应用户相关信息
     * </p>
     * @param standardUserId    标准系统用户ID
     * @return
     */
    IOperator translate(Long standardUserId);

    /**
     * <p>
     * 设置其他异构应用（Domino等）用户的密码
     * </p>
     * @param userId
     * @param password
     */
    void savePassword(Long userId, String password);

}
