package com.jinhe.tss.framework.sso;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.BeanUtil;

/**
 * <p>
 * 身份转换对象工厂类
 * </p>
 */
public class IdentityTranslatorFactory {
    
    /** 用户身份转换器 */
    protected static IdentityTranslator translator;

    /**
     * 获取身份转换对象
     * @return
     */
    public static IdentityTranslator getTranslator() {
        if (translator == null) {
            String configValue = Config.getAttribute(SSOConstants.IDENTITY_TRANSLATOR);
            if (configValue != null) {
                translator = (IdentityTranslator) BeanUtil.newInstanceByName(configValue);
            } else {
                throw new BusinessException("当前系统没有定义默认身份转换器，用户不能实现单点登录");
            }
        }
        return translator;
    }
}
