package com.jinhe.tss.cms.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jinhe.tss.framework.persistence.pagequery.MacrocodeQueryCondition;

/** 
 * 文章列表的查询条件
 */
public class ArticleQueryCondition extends MacrocodeQueryCondition {
    
    /* 搜索期限 */
    public static final int QUERY_CONDITION_DATE_ONE    = 1;   // 一天以内
    public static final int QUERY_CONDITION_DATE_THREE  = 2;   // 三天以内
    public static final int QUERY_CONDITION_DATE_SEVENE = 7;   // 一星期以内
    
    private String  title;
    private String  author;
    private Integer status;  // 流程状态
    private Date    createTime;
   
    private Long    channelId;
    private List<Long> channelIds;
    
    private String keyword;
    
    private String orderField;  // 排序字段
    private Integer isDesc;     // 是否降序排序
    
    public Set<String> getIgnoreProperties() {
        if(channelId == null){
            super.getIgnoreProperties().add("channelId");
        }
        if(channelIds == null){
            super.getIgnoreProperties().add("channelIds");
        }
        if(getCreateTime() == null){
            super.getIgnoreProperties().add("createTime");
        }
        
        super.getIgnoreProperties().add("daysAgo");
        super.getIgnoreProperties().add("fetchAll");
        super.getIgnoreProperties().add("orderField");
        super.getIgnoreProperties().add("isDesc");
        return super.getIgnoreProperties();
    }
    
    public Map<String, Object> getConditionMacrocodes() {
        Map<String, Object> map =  new HashMap<String, Object>();
        map.put("${title}",  " and a.title  like :title");
        map.put("${author}", " and a.author like :author");
        map.put("${keyword}", " and a.keyword like :keyword");
        
        map.put("${status}", " and a.status = :status");
        map.put("${createTime}", " and a.createTime > :createTime");
        
        map.put("${channelId}",  " and a.channel.id = :channelId");
        
        return map;
    }
 
    public String getAuthor() {
        if(author != null){
            author = "%" + author.trim() + "%";           
        }
        return author;
    }
    public String getTitle() {
        if(title != null){
            title = "%" + title.trim() + "%";           
        }
        return title;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
 
    public Integer getStatus() {
        return status;
    }
 
    public void setStatus(Integer status) {
        this.status = status;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getChannelId() {
        return channelId;
    }

    public List<Long> getChannelIds() {
        return channelIds;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public void setChannelIds(List<Long> channelIds) {
        this.channelIds = channelIds;
    }
 
    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public Integer getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(Integer isDesc) {
        this.isDesc = isDesc;
    }
    
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}