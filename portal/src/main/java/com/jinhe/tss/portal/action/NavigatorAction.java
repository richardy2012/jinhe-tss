package com.jinhe.tss.portal.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.helper.MenuTreeParser;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.INavigatorService;
import com.jinhe.tss.portal.service.IPortalService;

public class NavigatorAction extends BaseActionSupport {
	
	@Autowired private INavigatorService service;
	@Autowired private IPortalService portalService;
    
    /**
     * <p> 生成单个菜单 </p>
     */
    public void getMenuXML(Long id){
        print("MainMenu", service.getMenuXML(id));
    }
    
	/**
	 * <p>
	 * 菜单的树型展示。
     * 菜单依附于门户而存在，要想给某角色授于菜单管理权限，首先要授予门户节点的查看权限。
	 * </p>
	 */
	public void getAllNavigator4Tree(){        
        List<?> data = service.getAllNavigator();
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
		encoder.setNeedRootNode(false);
        print("MenuTree", encoder);
	}

	/**
	 * <p>
	 * 单个菜单控件的详细信息
	 * </p>
	 */
	public void getNavigatorInfo(Long id, Long parentId, int type){
		XFormEncoder encoder;
        if( DEFAULT_NEW_ID.equals(id) ){
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("parentId", parentId);
            map.put("target", "_blank");
        	map.put("type", type);
            encoder = new XFormEncoder("template/xform/MenuXForm" + type + ".xml", map);           
        }
        else {
        	Navigator info = service.getNavigatorInfo(id);            
            encoder = new XFormEncoder("template/xform/MenuXForm" + type + ".xml", info);
        }        
        
        print(Navigator.TYPE_MENU.equals(type) ? "MenuInfo" : "MenuItemInfo", encoder); 
	}
	
	/**
	 * <p>
	 * 保存菜单控件
	 * </p>
	 */
	public void save(Navigator navigator){
        boolean isNew = navigator.getId() == null;
        
        navigator = service.saveMenu(navigator);
        doAfterSave(isNew, navigator, "MenuTree");
	}
	
	/**
	 * <p>
	 * 删除菜单控件.
	 * </p>
	 */
	public void delete(Long id){
		service.deleteMenu(id);	
		printSuccessMessage();
	}
	
	/**
	 * <p>
	 * 停用/启用 菜单Navigator（将其下的disabled属性设为"1"/"0"）
	 * </p>
	 */
	public void disable(Long id, int state){
		service.disable(id, state);
        printSuccessMessage();
	}
	
	/**
	 * <p>
	 * 同组下的Navigator排序
	 * </p>
	 */
	public void sort(Long id, Long targetId, int direction){
		service.sort(id, targetId, direction);        
        printSuccessMessage();
	}
    
    /**
     * 移动
     */
    public void move(Long id, Long targetId){
        if(id.equals(targetId)){
            throw new BusinessException("节点不能移动到自身节点下");
        }
        service.moveMenu(id, targetId);
        printSuccessMessage();
    }
    
    /**
     * 根据菜单获取菜单项树
     */
    public void getMenus4TreeByPortal(Long portalId){
        List<?> data = service.getMenusByPortal(portalId);   

        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        print("MenuTree", encoder);
    }
    
    public void getPortalStructuresByPortal4Tree(Long portalId, final int type){
        List<?> data = portalService.getPortalStructuresByPortal(portalId);      
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());

        encoder.setTranslator(new ITreeTranslator(){
            public Map<String, Object> translate(Map<String, Object> attributes) {
                Object psType = attributes.get("type");
                switch(type){
                case 3:
                    if(psType.equals(Structure.TYPE_PORTAL))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                case 2:
                    if(psType.equals(Structure.TYPE_PORTAL) || psType.equals(Structure.TYPE_PAGE))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                case 1:  //此处为菜单替换方式中目标版面项，可以选择版面或者页面
                    if((psType.equals(Structure.TYPE_PORTAL) || psType.equals(Structure.TYPE_PORTLET_INSTANCE)))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                case 0:
                    if(!psType.equals(Structure.TYPE_PORTLET_INSTANCE))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                default:
                    throw new BusinessException("取门户树时传的参数不正确！");
                }
                return attributes;
            }            
        });
        encoder.setNeedRootNode(false);
        print("SiteTree", encoder);
    }
    
    /**
     * 移动的时候用到
     */
    public void getNavigators4Tree(Long id, final Long portalId) {        
        List<?> data = service.getMenusByPortal(portalId);
        
        //过滤移动节点自身
        for(Iterator<?> it = data.iterator(); it.hasNext();){
            Navigator menu = (Navigator) it.next();
            if(menu.getId().equals(id)){
                it.remove();
                break;
            }
        }
        
        TreeEncoder encoder = new TreeEncoder(data, new MenuTreeParser());
        
        encoder.setTranslator(new ITreeTranslator(){
            public Map<String, Object> translate(Map<String, Object> attributes) {  
                if( !attributes.get("portalId").equals(portalId) )
                    attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                
                if( ((Integer)attributes.get("type")) < 1 )
                    attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                
                return attributes;
            }            
        });
        encoder.setNeedRootNode(false);
        
        print("SiteTree", encoder);
    }
}