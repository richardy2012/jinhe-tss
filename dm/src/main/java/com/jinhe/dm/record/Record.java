package com.jinhe.dm.record;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.record.permission.RecordResource;
import com.jinhe.tss.framework.component.param.ParamManager;
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
    public static final String OPERATION_VIEW    = "1"; // 查看
    public static final String OPERATION_EDIT    = "2"; // 维护
    public static final String OPERATION_DELETE  = "3"; // 删除
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "record_sequence")
    private Long    id;         // 主键
    
    @Column(length = 100, nullable = false)
    private String  name;       // 展示名称
    
    @Column(length = 2000)  
    private String  param;      // 录入配置
    
    private String  datasource; // 保存至哪个数据源
    
    @Column(nullable = false)
    private Integer type;  // 种类  0：数据录入分组   1: 数据录入
    private String  remark; 
    
    private Long    parentId;  // 父节点
    private Integer seqNo;    // 排序号
    private String  decode;  // 层码，要求唯一
    private Integer levelNo;// 层次值
    
    public String toString() {
        return "数据录入【id = " + this.id + ", name = " + this.name + "】";
    }
    
    public String getDatasource() {
        if( datasource == null ) {
            try {
                return ParamManager.getValue(DMConstants.DEFAULT_CONN_POOL).trim(); // 默认数据源
            } catch (Exception e) {
            }
        }
        return datasource;
    }
    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);;
       
        String icon_path;
        if (TYPE0 == type) {
            icon_path = "images/folder.gif";
        } 
        else {
            icon_path = "images/record.gif";
        } 
        map.put("icon", icon_path);
        map.put("parentId", parentId);
        map.put("type", type);
        if(TYPE1 == type) {
            map.put("param", param);
        }
 
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

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
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
}
