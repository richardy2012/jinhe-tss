package com.jinhe.dm.data;

import java.sql.DriverManager;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinhe.dm.DMConstants;
import com.jinhe.tss.framework.component.cache.CacheHelper;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamService;

@Controller
@RequestMapping("/ds")
public class DataSourceManager {
	
	@Autowired ParamService paramService;
	
	/** 系统配置参数 */
	@RequestMapping(value = "/param/config", method = RequestMethod.GET)
	@ResponseBody
	public Object getCacheConfigs() {
		List<Param> cacheParams = paramService.getParamsByParentCode(CacheHelper.CACHE_PARAM);
		return cacheParams;
	}
	
	@RequestMapping(value = "/connpool", method = RequestMethod.POST)
	@ResponseBody
	public Object configConnpool(String code, String name, String value) {
		Param param = paramService.getParam(code);
		if(param == null) {
			// 新增时还需要在 ComboParam（”数据源列表“） 下增加一个param选项
			Param paramGroup = CacheHelper.getCacheParamGroup(paramService);
			Param dsList = paramService.getParam(DMConstants.DATASOURCE_LIST);
		} 
		else {
			param.setValue(value);
			param.setName(name);
			paramService.saveParam(param);
		}
		return "数据源配置成功";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Object testConn(String driver, String url, String user, String pwd) {
        try {
            Class.forName(driver);
			DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            return "测试失败，原因：" + e.getMessage();
        } 
		return "测试成功";
	}

}
