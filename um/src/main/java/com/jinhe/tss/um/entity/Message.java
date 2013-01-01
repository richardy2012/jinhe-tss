package com.jinhe.tss.um.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.web.dispaly.grid.GridAttributesMap;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.util.BeanUtil;
 
/**
 * 站内消息对象
 */
@Entity
@Table(name = "um_message")
@SequenceGenerator(name = "message_sequence", sequenceName = "message_sequence", initialValue = 1000, allocationSize = 10)
public class Message implements IXForm, IGridNode, IEntity{
    
    public static final Integer EDIT_STATUS    = new Integer(0); // 编辑
    public static final Integer SEND_STATUS    = new Integer(1); // 已发送
    public static final Integer READ_STATUS    = new Integer(2); // 已读
    
    public static final Integer UNREAD_STATUS  = UMConstants.FALSE; //未读
    public static final Integer HASREAD_STATUS = UMConstants.TRUE;  //已读
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "message_sequence")
	private Long   id;
	private String title;		// 标题
	
	@Column(length = 2000)
	private String content;		// 正文
	private Long   senderId;	// 发送者ID
	private String sender;		// 发送者
	private Long   receiverId;	// 接收者ID
	private String receiver;	// 接收者
	private Date   sendTime;    // 发送时间
	private Integer status  = EDIT_STATUS;	 // 状态
	private Integer hasRead = UNREAD_STATUS; // 是否已读
 
	@Transient
	private String   receiverIds; // 接收者ID列表
	
	public String getContent() {
		return content;
	}
 
	public Long getId() {
		return id;
	}
 
	public String getReceiver() {
		return receiver;
	}
	
	public String getSender() {
		return sender;
	}
 
	public Date getSendTime() {
		return sendTime;
	}
 
	public Integer getStatus() {
		return status;
	}
 
	public String getTitle() {
		return title;
	}
 
	public Integer getHasRead() {
		return hasRead;
	}
 
	public void setContent(String content) {
		this.content = content;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setSender(String sender) {
		this.sender = sender;
	}
 
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
 
	public void setStatus(Integer status) {
		this.status = status;
	}
 
	public void setTitle(String title) {
		this.title = title;
	}
 
	public void setHasRead(Integer hasRead) {
		this.hasRead = hasRead;
	}

	public Map<String, Object> getAttributesForXForm() {
		Map<String, Object> map = new HashMap<String, Object>();
		BeanUtil.addBeanProperties2Map(this, map);
		return map;
	}

	public GridAttributesMap getAttributes(GridAttributesMap map) {
		map.putAll(getAttributesForXForm());
		return map;
	}

    public String getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(String receiverIds) {
        this.receiverIds = receiverIds;
    }
}
