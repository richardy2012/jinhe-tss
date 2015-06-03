package com.jinhe.dm.record;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.record.permission.RecordResource;
import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.permission.IResource;
import com.jinhe.tss.util.BeanUtil;

@Entity
@Table(name = "dm_record")
@SequenceGenerator(name = "record_sequence", sequenceName = "record_sequence", initialValue = 1, allocationSize = 10)
public class Record extends OperateInfo implements IXForm, IDecodable, IResource {
	
	public static final int TYPE0 = 0;  // 数据录入分组
	public static final int TYPE1 = 1;  // 数据录入
    
	public static final Long DEFAULT_PARENT_ID = 0L;
    
    // 资源类型： 数据录入
    public static final String RESOURCE_TYPE = "D2"; 
    
    // 数据录入资源操作ID
    public static final String OPERATION_CDATA   = "1"; // 录入数据, create data/delete data
    public static final String OPERATION_EDIT    = "2"; // 维护录入
    public static final String OPERATION_DELETE  = "3"; // 删除录入
    public static final String OPERATION_VDATA   = "4"; // 查看数据，授此操作权限的用户能看到所有录入数据, view data
    public static final String OPERATION_EDATA   = "5"; // 维护数据，授此操作权限的用户能看到所有录入数据，且能编辑 edit data
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "record_sequence")
    private Long    id;         // 主键
    
    @Column(length = 100, nullable = false)
    private String  name;       // 展示名称
    
    @Column(nullable = false)
    private Integer type;       // 0：数据录入分组   1: 数据录入
    
    private String  datasource; // 保存至哪个数据源
    
    @Column(name = "rctable")
    private String  table;  // 保存至哪个表
    
    @Lob 
    private String define;  // 录入字段定义
    
    /** 定制的录入界面: 可以自己定制录入表单和展示表格 */
    private String customizePage;
    
    /** 定制的JS方法： 用于校验，自动计算等  */
    @Column(length = 4000)  
    private String customizeJS;
    
    /** 定制的过滤条件，可按登录人的角色、组织等信息进行过滤  */
    @Column(length = 1000)  
    private String customizeTJ;
   
    private String  remark; 
    
    private Long    parentId;  // 父节点
    private Integer seqNo;    // 排序号
    private String  decode;  // 层码，要求唯一
    private Integer levelNo;// 层次值
    
    public String toString() {
        return "数据录入【id = " + this.id + ", name = " + this.name + "】";
    }
    
    public String getDatasource() {
        return DMConstants.getDS(datasource);
    }
    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);;
       
        map.put("parentId", parentId);
        map.put("type", type);
        if(TYPE1 == type) {
            map.put("customizePage", customizePage);
            map.put("define", define);
        }
        map.put("icon", "images/" + (TYPE0 == type ? "folder" : "record") + ".gif");
 
        return map;
    }

    public Map<String, Object> getAttributes4XForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        return map;
    }

    public Class<?> getParentClass() {
        if(this.parentId.equals(DEFAULT_PARENT_ID)) {
            return RecordResource.class;
        }
        return this.getClass();
    }

	public String getResourceType() {
		return RESOURCE_TYPE;
	}
 
	public Serializable getPK() {
		return this.id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getDecode() {
		return decode;
	}

	public void setDecode(String decode) {
		this.decode = decode;
	}

	public Integer getLevelNo() {
		return levelNo;
	}

	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getDefine() {
		return define;
	}

	public void setDefine(String define) {
		this.define = define;
	}

	public String getCustomizePage() {
		return customizePage;
	}

	public void setCustomizePage(String customizePage) {
		this.customizePage = customizePage;
	}

	public String getCustomizeJS() {
		return customizeJS;
	}

	public void setCustomizeJS(String customizeJS) {
		this.customizeJS = customizeJS;
	}

	public String getCustomizeTJ() {
		return customizeTJ;
	}

	public void setCustomizeTJ(String customizeTJ) {
		this.customizeTJ = customizeTJ;
	}
}
