package com.jinhe.tss.portal.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.PTActionSupport;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.entity.PortalStructure;
import com.jinhe.tss.portal.helper.MenuTreeParser;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.INavigatorService;
import com.jinhe.tss.portal.service.IPortalService;

public class NavigatorAction extends PTActionSupport {
	
	private INavigatorService service;
	private IPortalService portalService;

	private Long id;
	private Long parentId;
    private Long portalId;
    private Integer disabled ;
    private Integer type;
	private Long targetId; // 移动或者排序的目标节点ID
	private int direction; // 分＋1（向下），和－1（向上）    
    
    private Navigator navigator = new Navigator();
    
    /**
     * <p> 生成单个菜单 </p>
     * @return
     */
    public String getMenuXML(){
        return print("MainMenu", service.getMenuXML(id));
    }
    
	/**
	 * <p>
	 * 菜单的树型展示。
     * 菜单依附于门户而存在，要想给某角色授于菜单管理权限，首先要授予门户节点的查看权限。
	 * </p>
	 * @return
	 */
	public String getAllNavigator4Tree(){        
        List<?> data = service.getAllNavigator();
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
		encoder.setNeedRootNode(false);
        return print("MenuTree", encoder);
	}

	/**
	 * <p>
	 * 单个菜单控件的详细信息
	 * </p>
	 * @return
	 */
	public String getNavigatorInfo(){
		XFormEncoder encoder;
        if(isCreateNew()){
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("parentId", parentId);
        	map.put("portalId", portalId);
            map.put("target", "_blank");
        	map.put("type", type);
            encoder = new XFormEncoder("template/xform/MenuXForm" + type + ".xml", map);           
        }
        else {
        	Navigator info = service.getNavigatorInfo(id);            
            encoder = new XFormEncoder("template/xform/MenuXForm" + type + ".xml", info);
        }        
        
        return print(Navigator.TYPE_MENU.equals(type) ? "MenuInfo" : "MenuItemInfo", encoder); 
	}
	
	/**
	 * <p>
	 * 保存菜单控件
	 * </p>
	 * @return
	 */
	public String save(){
        boolean isNew = navigator.getId() == null;
        
        navigator = service.saveMenu(navigator);
        return doAfterSave(isNew, navigator, "MenuTree");
	}
	
	/**
	 * <p>
	 * 删除菜单控件.
	 * </p>
	 * @return
	 */
	public String delete(){
		service.deleteMenu(id);	
		return printSuccessMessage();
	}
	
	/**
	 * <p>
	 * 停用/启用 菜单Navigator（将其下的disabled属性设为"1"/"0"）
	 * </p>
	 * @return
	 */
	public String disable(){
		service.disable(id, disabled);
        return printSuccessMessage();
	}
	
	/**
	 * <p>
	 * 同组下的Navigator排序
	 * </p>
	 * @return
	 */
	public String sort(){
		service.sort(id, targetId, direction);        
        return printSuccessMessage();
	}
    
    /**
     * 移动
     * @return
     */
    public String move(){
        if(id.equals(targetId)){
            throw new BusinessException("节点不能移动到自身节点下");
        }
        service.moveMenu(id, targetId, portalId);
        return printSuccessMessage();
    }
    
    /**
     * 根据菜单获取菜单项树
     * @return
     */
    public String getMenus4TreeByPortal(){
        List<?> data = service.getMenusByPortal(portalId);   

        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        return print("MenuTree", encoder);
    }
    
    public String getPortalStructuresByPortal4Tree(){
        List<?> data = portalService.getPortalStructuresByPortal(portalId);      
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());

        final int type = this.type;
        encoder.setTranslator(new ITreeTranslator(){
            public Map<String, Object> translate(Map<String, Object> attributes) {
                Object psType = attributes.get("type");
                switch(type){
                case 3:
                    if(psType.equals(PortalStructure.TYPE_PORTAL))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                case 2:
                    if(psType.equals(PortalStructure.TYPE_PORTAL) || psType.equals(PortalStructure.TYPE_PAGE))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                case 1:  //此处为菜单替换方式中目标版面项，可以选择版面或者页面
                    if((psType.equals(PortalStructure.TYPE_PORTAL) || psType.equals(PortalStructure.TYPE_PORTLET_INSTANCE)))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                case 0:
                    if(!psType.equals(PortalStructure.TYPE_PORTLET_INSTANCE))
                        attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                    break;
                default:
                    throw new BusinessException("取门户树时传的参数不正确！");
                }
                return attributes;
            }            
        });
        encoder.setNeedRootNode(false);
        return print("SiteTree", encoder);
    }
    
    /**
     * 移动的时候用到
     * @return
     */
    public String getNavigators4Tree(){        
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
        
        final Long portalId = this.portalId;
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
        
        return print("SiteTree", encoder);
    }
    
	public void setService(INavigatorService service) {
		this.service = service;
	}
    public void setPortalService(IPortalService portalService) {
        this.portalService = portalService;
    }
	
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setParentId(Long groupId) {
		this.parentId = groupId;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
    public Navigator getNavigator() {
        return navigator;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }
}