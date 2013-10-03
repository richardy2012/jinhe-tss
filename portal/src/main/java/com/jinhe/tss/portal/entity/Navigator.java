package com.jinhe.tss.portal.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.dom4j.Element;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.entity.permission.NavigatorResourceView;
import com.jinhe.tss.um.permission.IResource;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * 导航栏内容定义器：描述导航栏（菜单）基本信息及内容定义信息
 */
@Entity
@Table(name = "portal_navigator", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_MENU", columnNames = { "parentId", "name" })
})
@SequenceGenerator(name = "navigator_sequence", sequenceName = "navigator_sequence", initialValue = 1)
public class Navigator extends OperateInfo implements IEntity, ILevelTreeNode, IXForm, IDecodable, IResource {

    /**
     * 菜单组
     */
    public static final Integer TYPE_MENU        = 1;
    
    /**
     * 普通URL方式: url = www.google.com
     */
    public static final Integer TYPE_MENU_ITEM_4 = 4;
    
    /**
     * CMS栏目方式: url = ${common.articleListUrl}&channelId=38
     */
    public static final Integer TYPE_MENU_ITEM_7 = 7;
    
    /**
     * 行为方式: params = appCode:\'UMS\',redirect:\'http://${PT_ip}/ums/redirect.html\',url:\'ums/permission.htm\'
     *         methodName = jumpTo（方法可定义在门户外挂文件或门户元素上）, contentId = 16, contentName = IFrame
     */
    public static final Integer TYPE_MENU_ITEM_6 = 6;
    
    /**
     * 页面/版面/Portlet 门户内部链接: contentId=66
     * 读取时再解析成 url = portal!previewPortal.action?portalId=" + portalId + "&id=" + contentId 
     */
    public static final Integer TYPE_MENU_ITEM_3 = 3;
    
    /**
     * 局部替换方式: contentId=66, targetId=88
     * 读取时再解析成 url = portal!getPortalXML.action?portalId=" + portalId + "&id=" + contentId + "&targetId=" + targetId
     */
    public static final Integer TYPE_MENU_ITEM_5 = 5;
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "navigator_sequence")
    private Long   id;
	
	@Column(nullable = false)
	private String  name;       // 菜单（项）名称
	private Integer type;       // 类型  参考 TYPE_MENU_X
	private Long   parentId;    // 菜单项对应菜单
    private String url;         // url地址                     
    private String target  ;    // 目标区域，_blank/_self  等  
    
    @Column(length = 1000)
    private String description; // 菜单内容的描述信息
	
    // Portal内部页面引用的菜单内容
    private Long   portalId;   // 菜单或菜单项所属于门户ID
    
    @ManyToOne
    private Structure content;    // 显示内容   
    
    @ManyToOne
    private Structure toContent;  // 目标：版面/Portlet替换  
    
 	private String methodName;    // 方法名                        
    private String params;        // 参数    
    
    // 菜单树结构信息
    @Column(nullable = false)
	private Integer seqNo;   // 顺序号
    private String  decode;  // 层码
    private Integer levelNo; // 层次值

    private Integer disabled = PortalConstants.FALSE;  // 是否停用
    
    @Transient 
    private List<Navigator> children = new ArrayList<Navigator>();

    public List<Navigator> getChildren(){
        return this.children;
    }
    
    public void addChild(Navigator navigator){
        this.children.add(navigator);
    }
    
    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("parentId", parentId);
        map.put("portalId", portalId);
        map.put("type", type);
        map.put("disabled", disabled);
        map.put("resourceTypeId", getResourceType());
        
        if(this.type.equals(TYPE_MENU)){
            map.put("icon", "../framework/images/portal/menu" + disabled + ".gif");
        } else {
            map.put("icon", "../framework/images/portal/menu_item" + disabled + ".gif");
        }       
        
        super.putOperateInfo2Map(map);
        return map;
    }
    
    /**
     * |- 菜单 <br>
     * ......|- 菜单项 <br>
     * ..............|- 菜单项 <br>
     * ..............|- 菜单项 <br>
     * ..............|- 菜单项 <br>
     * ......|- 菜单项 <br>
     * ..............|- 菜单项 <br>
     * ..............|- 菜单项 <br>
     * 
     * @param list
     */    
    public Element compose2Tree(List<Navigator> list){
        if( !this.type.equals(TYPE_MENU) ){
            throw new BusinessException("非[菜单]不能进行组装菜单操作!");
        }
        
        Map<Long, Element> map = new HashMap<Long, Element>();
        Element root = this.genMenuNode();
        
        for ( Navigator entity : list ) {
            map.put(entity.getId(), entity.genMenuNode());
        }
        
        for ( Navigator entity : list ) {
        	if(PortalConstants.TRUE.equals(entity.getDisabled())) {
        		continue; // 过滤掉停用的
        	}
        	
            Element node = map.get(entity.getId());
            Element parent = map.get(entity.getParentId());        
            if(parent != null){
                parent.add(node);
            }            
        }
        return root;
    }
    
    private Element genMenuNode(){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("type", type);
        
        map.put("url", url);
        map.put("target", target);
        map.put("methodName", methodName);
        map.put("params", params);
        
        if(content != null) {
            map.put("contentId", content.getId());
            
            // 页面/版面/Portlet 门户内部链接
            if(type.equals(TYPE_MENU_ITEM_3)){
                map.put("url", "portal!previewPortal.action?portalId=" + portalId + "&id=" + content.getId());
            }
        }
        
        // 局部替换方式
        if(type.equals(TYPE_MENU_ITEM_5)) {
            map.put("targetId", toContent.getId());
            map.put("action", "portal!getPortalXML.action?portalId=" + portalId + "&id=" + content.getId() 
                    + "&targetId=" + toContent.getId());
        }
        
        return XMLDocUtil.map2AttributeNode(map, this.type.equals(TYPE_MENU) ? "Menu" : "MenuItem");
    }

    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map, "children,content,toContent".split(","));
        
        if(content != null) {
            map.put("content.id", content.getId());
            map.put("content.name", content.getName());
        }
        if(toContent != null) {
            map.put("toContent.id", toContent.getId());
            map.put("toContent.name", toContent.getName());
        }
       
        return map;
    }
    
    public Class<?> getParentClass() {
        if(this.parentId.equals(PortalConstants.ROOT_ID)) {
            return NavigatorResourceView.class;
        }
        return getClass();
    }

    public String getResourceType() {
        return PortalConstants.NAVIGATOR_RESOURCE_TYPE;
    }
    
    public String toString(){
        return "(id:" + this.id + ", name:" + this.name + ")"; 
    }
 
	public String getMethodName() {
		return methodName;
	}
 
	public String getDescription() {
		return description;
	}
 
	public Long getParentId() {
		return parentId;
	}
 
	public Long getId() {
		return id;
	}
 
	public String getName() {
		return name;
	}
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
    public String getParams() {
        return params;
    }
 
    public String getTarget() {
        return target;
    }
 
    public Integer getType() {
        return type;
    }   
 
    public Long getPortalId() {
        return portalId;
    }
 
    public String getUrl() {
        return url;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public void setParams(String params) {
        this.params = params;
    }
 
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
 
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
 
    public void setTarget(String target) {
        this.target = target;
    }
 
    public void setType(Integer type) {
        this.type = type;
    }
 
    public void setUrl(String url) {
        this.url = url;
    }
 
    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }
 
    public Integer getDisabled() {
        return disabled;
    }
 
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
 
    public String getDecode() {
        return decode;
    }
 
    public Integer getLevelNo() {
        return levelNo;
    }
 
    public void setDecode(String decode) {
        this.decode = decode;
    }
 
    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }

    public Structure getContent() {
        return content;
    }

    public void setContent(Structure content) {
        this.content = content;
    }

    public Structure getToContent() {
        return toContent;
    }

    public void setToContent(Structure toContent) {
        this.toContent = toContent;
    }
}	