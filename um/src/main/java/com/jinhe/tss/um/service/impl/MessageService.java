package com.jinhe.tss.um.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.service.IMessageService;
import com.jinhe.tss.util.EasyUtils;
 
@Service("MessageService")
public class MessageService implements IMessageService {
	
	@Autowired private ICommonDao commonDao;
	@Autowired private IGroupDao groupDao;
 
	public void sendMessage(Message message){
		String[] receiverIds = message.getReceiverIds().split(",");
		String[] receivers = message.getReceiver().split(",");
		for(int i = 0; i < receiverIds.length; i++){
			Message temp = new Message();
			temp.setReceiverId(EasyUtils.convertObject2Long(receiverIds[i]));
			temp.setReceiver(receivers[i]);
			temp.setStatus(Message.SEND_STATUS);
			temp.setTitle(message.getTitle());
			temp.setContent(message.getContent());
			
			temp.setSenderId(Environment.getOperatorId());
	        temp.setSender(Environment.getUserName());
	        temp.setSendTime(new Date());
	        
            commonDao.createWithoutFlush(temp);
		}
		commonDao.flush();
	}
 
	public Message viewMessage(Long id) {
		Message message = (Message) commonDao.getEntity(Message.class, id);
		if(Message.SEND_STATUS.equals(message.getStatus())){
			message.setHasRead(Message.HASREAD_STATUS);
			message.setStatus(Message.READ_STATUS);
			commonDao.update(message);
		}
		return message;
	}
	
	public void deleteMessage(Long id){
        commonDao.delete(commonDao.getEntity(Message.class, id));
	}
    
    @SuppressWarnings("unchecked")
	private List<Message> getInMessages(Long userId, Integer status){
        return (List<Message>) commonDao.getEntities(" from Message m where m.receiverId = ? and m.status = ?", userId, status);
    }
    
	public List<Message> getInboxList(){
		Long operatorId = Environment.getOperatorId();
		
		List<Message> returnList = new ArrayList<Message>();
		returnList.addAll(getInMessages(operatorId, Message.SEND_STATUS));
		returnList.addAll(getInMessages(operatorId, Message.READ_STATUS));
		return returnList;
	}
	
	public List<?> getOutboxList() {
		Long userId = Environment.getOperatorId();
		Integer status = Message.SEND_STATUS;
        return commonDao.getEntities(" from Message m where m.senderId = ? and m.status = ?", userId, status);
    }
}
