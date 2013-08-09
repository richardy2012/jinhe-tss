package com.jinhe.tss.um.service;

import java.util.List;

import com.jinhe.tss.um.entity.Message;
 
public interface IMessageService {
	
	/**
	 * 发送短消息
	 * @param message
	 */
	void sendMessage(Message message);
	
	/**
	 * 查看短消息 并将标志位改成已读
	 * @param id
	 * @return
	 */
	Message viewMessage(Long id);
	
	/**
	 * 删除短消息 将标志位设为已删状态
	 * @param id
	 */
	void deleteMessage(Long id);
	
	/**
	 * 获取收件箱列表
	 * @return
	 */
	List<Message> getInboxList();
	
	/**
	 * 获取发件箱列表
	 * @return
	 */
	List<?> getOutboxList();
    
}