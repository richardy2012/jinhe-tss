package com.jinhe.tss.framework.sso.appserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * <p>
 * 应用访问配置信息管理器（类型：XML文件）
 * </p>
 * <servers>
 *   <server code="TSS" name="TSS" sessionIdName="JSESSIONID" baseURL="http://localhost:8088/tss"/>
 * </servers>
 */
public class FileAppServerStorer implements IAppServerStorer {

	private static final String APPSERVERS_CONFIG_FILE = "application.servers.file";
	private static final String APPSERVERS_CONFIG_FILE_DEFAULT = "appServers.xml";

    private Map<String, AppServer> cache;

	/**
	 * 初始化文件应用服务器列表
	 */
	private synchronized void init() {
		Document doc = getConfigDocument();
		
		List<Element> list = XMLDocUtil.selectNodes(doc, "/servers/server");
		for (Element appServerNode : list) {
			 AppServer bean = new AppServer();
		     BeanUtil.setDataToBean(bean, XMLDocUtil.dataNode2Map(appServerNode));
			 cache.put(bean.getCode(), bean);
		}
	}
 
	/**
	 * <p>
	 * 获取配置文件内容
	 * </p>
	 *
	 * @return
	 */
	protected Document getConfigDocument() {
		String fileName = Config.getAttribute(APPSERVERS_CONFIG_FILE);
		if (fileName != null) {
			return XMLDocUtil.createDocByAbsolutePath(fileName);
		} else {
			return XMLDocUtil.createDoc(APPSERVERS_CONFIG_FILE_DEFAULT);
		}
	}

	/**
	 * 根据应用服务器编号获取应用服务器对象
	 */
	public AppServer getAppServer(String code) {
		if (cache == null) {
            cache = new HashMap<String, AppServer>();
			init();
		}
		AppServer appServer = (AppServer) cache.get(code);
		if (appServer == null) {
			throw new BusinessException("系统【"
					+ Context.getApplicationContext().getCurrentAppCode() + "】中没有应用【"+ code + "】的相关访问配置信息");
		}
		return appServer;
	}
}
