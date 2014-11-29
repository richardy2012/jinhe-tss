package com.jinhe.tss.portal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.cache.CacheHelper;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.entity.permission.NavigatorPermission;
import com.jinhe.tss.portal.entity.permission.NavigatorResource;
import com.jinhe.tss.portal.helper.PSTreeTranslator4CreateMenu;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.INavigatorService;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;

@Controller
@RequestMapping("/auth/navigator")
public class NavigatorAction extends BaseActionSupport {

	@Autowired private INavigatorService service;
	@Autowired private IPortalService portalService;
    
    /**
     * 生成单个菜单
     */
	@RequestMapping("/xml/{id}")
    public void getNavigatorXML(HttpServletResponse response, @PathVariable("id") Long id) {
        print("MainMenu", service.getNavigatorXML(id));
    }
	
    /** 刷新一下参数的缓存 */
	@RequestMapping("/cache/{key}")
    public void flushCache(HttpServletResponse response, @PathVariable("key") Object key) {
		Pool navigatorPool = CacheHelper.getNoDeadCache();;
        navigatorPool.removeObject(PortalConstants.NAVIGATOR_CACHE + key);
        printSuccessMessage();
    }
    
	/**
	 * <p>
	 * 菜单的树型展示。
     * 菜单依附于门户而存在，要想给某角色授于菜单管理权限，首先要授予门户节点的查看权限。
	 * </p>
	 */
	@RequestMapping("/list")
	public void getAllNavigator4Tree(HttpServletResponse response) {        
        List<?> data = service.getAllNavigator();
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
		encoder.setNeedRootNode(false);
        print("MenuTree", encoder);
	}

	/**
	 * 单个菜单的详细信息
	 */
	@RequestMapping("/{id}")
	public void getNavigatorInfo(HttpServletResponse response, HttpServletRequest request, 
			@PathVariable("id") Long id) {
		
		Object type;
		Map<String, Object> map;
        if( DEFAULT_NEW_ID.equals(id) ) {
        	map = new HashMap<String, Object>();
            map.put("target", "_blank");
            map.put("parentId", request.getParameter("parentId"));
            map.put("portalId", request.getParameter("portalId"));
        	map.put("type", type = request.getParameter("type"));
        }
        else {
        	Navigator info = service.getNavigator(id);            
        	map = info.getAttributes4XForm();
        	type = info.getType();
        }        
        XFormEncoder encoder = new XFormEncoder("template/xform/MenuXForm" + type + ".xml", map);;
        
        print("MenuInfo", encoder); 
	}
	
	/**
	 * 保存菜单
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void save(HttpServletResponse response, Navigator navigator) {
        boolean isNew = navigator.getId() == null;
        
        navigator = service.saveNavigator(navigator);
        doAfterSave(isNew, navigator, "MenuTree");
	}
	
	/**
	 * 删除菜单
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(HttpServletResponse response, @PathVariable("id") Long id) {
		service.deleteNavigator(id);	
		printSuccessMessage();
	}
	
	/**
	 * 停用/启用 菜单Navigator（将其下的disabled属性设为"1"/"0"）
	 */
    @RequestMapping("/disable/{id}/{state}")
    public void disable(HttpServletResponse response, 
    		@PathVariable("id") Long id, 
    		@PathVariable("state") int state) {
    	
		service.disable(id, state);
        printSuccessMessage();
	}
	
	/**
	 * 同组下的Navigator排序
	 */
	@RequestMapping(value = "/sort/{id}/{targetId}/{direction}", method = RequestMethod.POST)
	public void sort(HttpServletResponse response, 
            @PathVariable("id") Long id, 
            @PathVariable("targetId") Long targetId,  
            @PathVariable("direction") int direction) {
		
		service.sort(id, targetId, direction);        
        printSuccessMessage();
	}
    
    /**
     * 移动
     */
	@RequestMapping(value = "/move/{id}/{targetId}", method = RequestMethod.POST)
    public void moveTo(HttpServletResponse response, 
    		@PathVariable("id") Long id, 
    		@PathVariable("targetId") Long targetId) {
		
        if(id.equals(targetId)) {
            throw new BusinessException("节点不能移动到自身节点下");
        }
        service.moveNavigator(id, targetId);
        printSuccessMessage();
    }
	
    /**
     * 移动的时候用到
     */
	@RequestMapping("/tree/{id}")
    public void getPortalNavigatorTree(HttpServletResponse response, @PathVariable("id") Long id) {       
    	
		Navigator self = service.getNavigator(id);
        Long portalId = self.getPortalId();
        
        List<?> list;
        if(portalId != null) {
        	list = service.getNavigatorsByPortal(portalId);
        }
        else {
        	list = service.getAllNavigator();
        }
        
        // 过滤移动节点自身
        list.remove(self);
        
        TreeEncoder encoder = new TreeEncoder(list, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        
        print("MenuTree", encoder);
    }
    
	@RequestMapping("/pstree/{portalId}/{type}")
    public void getStructuresByPortal(HttpServletResponse response, 
    		@PathVariable("portalId") Long portalId, 
    		@PathVariable("type") final int type) {
    	
        List<?> data = portalService.getStructuresByPortal(portalId);      
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());

        encoder.setTranslator(new PSTreeTranslator4CreateMenu(type));
        encoder.setNeedRootNode(false);
        print("StructureTree", encoder);
    }
	
	@RequestMapping("/operations/{resourceId}")
    public void getOperations(HttpServletResponse response, @PathVariable("resourceId") Long resourceId) {
        List<String> list = PermissionHelper.getInstance().getOperationsByResource(resourceId,
                        NavigatorPermission.class.getName(), NavigatorResource.class);

        print("Operation", EasyUtils.list2Str(list));
    }
}