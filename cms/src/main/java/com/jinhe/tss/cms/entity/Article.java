package com.jinhe.tss.cms.entity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.grid.GridAttributesMap;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.DateUtil;

/** 
 * <p>文章Article实体对象</p>
 */
@Entity
@Table(name = "cms_article")
@SequenceGenerator(name = "article_sequence", sequenceName = "article_sequence", initialValue = 1, allocationSize = 10)
public class Article extends OperateInfo implements ITreeNode, IGridNode, IXForm {
    
	public static final String[] IGNORE_PROPERTIES = new String[] { "id", "status", "hitCount", "issueDate" };
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "article_sequence")
    private Long   id;				//文章编号 PK
	
	@Column(nullable = false)
    private String title;			//标题
    private String subtitle;		//副标题
    private String keyword;			//关键字
    private String author;			//作者
    private String summary;			//摘要 
    
    @Column(length = 4000, nullable = false)  
    private String  content;		//正文内容  
    
    private Date    overdueDate;    // 过期时间
    private Date    issueDate;	    // 发布日期	开始为空,只有审核和发布阶段可以改
    private String  pubUrl;         // 发布路径
    
    private Integer hitCount = 0;   //点击率
	
    private Integer status = CMSConstants.START_STATUS;	   // 文章的状态 1：编辑中 2：待发布 3：已发布生成xml 4：过期 
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Channel channel;      // 文章所属栏目
    
    private Integer seqNo;
 
    private Integer isTop = CMSConstants.FALSE;   // 文章是否置顶
    
    @Transient 
    Map<String, Attachment> attachments = new LinkedHashMap<String, Attachment>();  // 存放文章附件列表
 
    public String getAuthor() {
        return author;
    }
 
    public void setAuthor(String author) {
        this.author = author;
    }
 
    public String getContent() {
        return content;
    }
 
    public void setContent(String content) {
        this.content = content;
    }
 
    public Integer getHitCount() {
        return hitCount;
    }
 
    public void setHitCount(Integer hitCount) {
        this.hitCount = hitCount;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Date getIssueDate() {
        return issueDate;
    }
 
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
 
    public String getKeyword() {
        return keyword;
    }
 
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getStatus() {
        return status;
    }
 
    public void setStatus(Integer status) {
        this.status = status;
    }
 
    public String getSubtitle() {
        return subtitle;
    }
 
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
 
    public String getSummary() {
        return summary;
    }
 
    public void setSummary(String summary) {
        this.summary = summary;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
	public String getPubUrl() {
		return pubUrl;
	}
 
	public void setPubUrl(String pubUrl) {
		this.pubUrl = pubUrl;
	}
 
	public Date getOverdueDate() {
		return overdueDate;
	}
 
	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}
    
    public String getName(){
        return title;
    }
    
    public GridAttributesMap getAttributes(GridAttributesMap map) {
        Map<String, Object> attributes = new LinkedHashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, attributes);
        map.putAll(attributes);
        
        map.put("icon", "../framework/images/article.gif");
        
        return map;
    }

    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        map.put("id", id);
        
        map.put("createTime", DateUtil.format(createTime));
        if(overdueDate != null) {
        	map.put("overdueDate", DateUtil.format(overdueDate));
        }
        if(issueDate != null) {
        	map.put("issueDate", DateUtil.format(issueDate));
        }
        
        return map;
    }

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id.toString(), title);
        return map;
    }
 
    public Map<String, Attachment> getAttachments() {
        return attachments;
    }
    
    public String toString(){
        return "(id:" + this.id + ", title:" + this.title + ")"; 
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
}