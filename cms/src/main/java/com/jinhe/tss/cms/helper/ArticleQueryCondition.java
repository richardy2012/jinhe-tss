package com.jinhe.tss.cms.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jinhe.tss.framework.component.param.ParamConfig;
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
    
    private Long articleId;
    private String keyword;
    
    private Integer daysAgo;
    
    // 是否取全部，否的话只取创建日期为 ARTICLE_DAYS_AGO 内的文章
    private boolean fetchAll = false;   
    
    private String orderField;  // 排序字段
    private Integer isDesc;     // 是否降序排序
    
    public Set<String> getIgnoreProperties() {
        if(channelId == null){
            super.getIgnoreProperties().add("channelId");
        }
        if(channelIds == null){
            super.getIgnoreProperties().add("channelIds");
        }
        if(createTime == null){
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
        map.put("${status}", " and a.status = :status");
        map.put("${createTime}", " and a.createTime > :createTime");
        
        map.put("${channelId}",  " and a.channel.id = :channelId");
        
        return map;
    }

    // 是否取全部，否的话只取创建日期为 betweenDay 内的文章
    public void setFetchStartTime(boolean fetchAll) {
        if(!fetchAll){
            java.util.Calendar calendar = Calendar.getInstance();
            
            // 为了性能，可以將天數設置短一點，只取这个时间段内的文章，太老的文章不去做处理
            int howlong;
            try {
                howlong = Integer.parseInt(ParamConfig.getAttribute("article.days.ago"));
            } catch(Exception e){
                howlong = 365; // 默认一年
            }
            calendar.add(Calendar.DAY_OF_MONTH, howlong);
            this.createTime = calendar.getTime();
        }
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

    public boolean isFetchAll() {
        return fetchAll;
    }

    public void setFetchAll(boolean fetchAll) {
        this.fetchAll = fetchAll;
    }

    public Integer getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(Integer daysAgo) {
        this.daysAgo = daysAgo;
        
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        gc.add(5, -1 * daysAgo); // daysAgo天前
        this.createTime = gc.getTime();
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
	
	public Long getArticleId() {
		return articleId;
	}
	
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
}