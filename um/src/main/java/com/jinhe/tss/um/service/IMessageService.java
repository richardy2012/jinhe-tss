package com.jinhe.tss.um.service;

import java.util.List;

import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.helper.UMQueryCondition;
 
public interface IMessageService {
	
	/**
	 * <p>
	 * 发送短消息
	 * </p>
	 * @param message
	 */
	public void sendMessage(Message message);
	
	/**
	 * <p>
	 * 查看短消息 并将标志位改成已读
	 * </p>
	 * @param id
	 * @return
	 */
	public Message viewMessage(Long id);
	
	/**
	 * <p>
	 * 删除短消息 将标志位设为已删状态
	 * </p>
	 * @param id
	 */
	public void deleteMessage(Long id);
	
	/**
	 * <p>
	 * 获取收件箱列表
	 * </p>
	 * @return
	 */
	public List<Message> getInboxList();
	
	/**
	 * <p>
	 * 获取发件箱列表
	 * </p>
	 * @return
	 */
	public List<?> getOutboxList();
    
    
    public List<?> getGroupsList();
    
    public List<?> getUsersByCondition(UMQueryCondition condition);
    
    public Object[] getGroupUserTreeList();
}