package com.jinhe.tss.um.service;

import org.dom4j.Document;

/**
 * <p>
 * 应用、资源类型、权限选项、资源注册接口
 * </p>
 */
public interface IResourceRegisterService {
	
	/**
     * 设置IResourceRegisterService的实现类状态为UMS初始化数据状态，以区分UMS应用发布时的运行状态。
	 * @param initial
	 */
	void setInitial(boolean initial);

    /**
     * 导入XML格式的资源配置文件
     * @param doc
     * @param applicationType
     */
    void applicationResourceRegister(Document doc, String applicationType);
}

	