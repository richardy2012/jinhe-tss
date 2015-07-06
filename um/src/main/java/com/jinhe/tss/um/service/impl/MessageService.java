package com.jinhe.tss.um.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.service.IMessageService;
import com.jinhe.tss.util.EasyUtils;
 
public class MessageService implements IMessageService {
	
	@Autowired private ICommonDao commonDao;
	@Autowired private IGroupDao groupDao;
 
	public void sendMessage(Message message){
		String[] receiverIds = message.getReceiverIds().split(",");
		String[] receivers = message.getReceiver().split(",");
		for(int i = 0; i < receiverIds.length; i++){
			Message temp = new Message();
			temp.setReceiverId(EasyUtils.obj2Long(receiverIds[i]));
			temp.setReceiver(receivers[i]);
			temp.setTitle(message.getTitle());
			temp.setContent(message.getContent());
			
			temp.setSenderId(Environment.getUserId());
			temp.setSender(Environment.getUserName());
			temp.setSendTime(new Date());
			
            commonDao.createWithoutFlush(temp);
		}
		commonDao.flush();
	}
 
	public Message viewMessage(Long id) {
		Message message = (Message) commonDao.getEntity(Message.class, id);
		message.setReadTime(new Date());
		commonDao.update(message);
		
		return message;
	}
	
	public void deleteMessage(Long id){
        commonDao.delete( Message.class, id );
	}
 
	@SuppressWarnings("unchecked")
	public List<Message> getInboxList(){
		Long userId = Environment.getUserId();
		String hql = " from Message m where m.receiverId = ?";
		return (List<Message>) commonDao.getEntities(hql, userId);
	}
 
}
