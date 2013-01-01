package com.jinhe.tss.um.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.UMQueryCondition;
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
			temp.setReceiverId(EasyUtils.convertObject2Long(receiverIds[i]));
			temp.setReceiver(receivers[i]);
			temp.setStatus(Message.SEND_STATUS);
			temp.setTitle(message.getTitle());
			temp.setContent(message.getContent());
			
			insertSenderInfo(temp);
            commonDao.createWithoutFlush(temp);
		}
		commonDao.flush();
	}
	
	public void saveMessage(Message message){
		insertSenderInfo(message);
		message.setStatus(Message.EDIT_STATUS);
        commonDao.create(message);
	}
	
	private void insertSenderInfo(Message message){
		message.setSenderId(Environment.getOperatorId());
		message.setSender(Environment.getUserName());
		message.setSendTime(new Date());
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
	
	public List<?> getOutboxList(){
		return getOutMessages(Environment.getOperatorId(), Message.SEND_STATUS);
	}
	
	public List<?> getDraftList(){
		return getOutMessages(Environment.getOperatorId(), Message.EDIT_STATUS);
	}
	
    private List<?> getOutMessages(Long userId, Integer status){
        return commonDao.getEntities(" from Message m where m.senderId = ? and m.status = ?", userId, status);
    }
    
    public Object[] getGroupUserTreeList() {
        List<?> groups = groupDao.getVisibleSubGroups(UMConstants.MAIN_GROUP_ID);
        List<?> relationUsers = groupDao.getUsersAndRelation(Environment.getOperatorId());
       
        List<Object> users = new ArrayList<Object>();
        List<Object> groupUsers = new ArrayList<Object>();
        if( !EasyUtils.isNullOrEmpty(relationUsers) ){
            for( Object temp : relationUsers ) {
                Object[] objs = (Object[]) temp;
                users.add(objs[0]);
                groupUsers.add(objs[1]);
            }
        }
        return new Object[]{groups, users, groupUsers};
    }

    public List<?> getGroupsList() {
        return groupDao.getVisibleSubGroups(UMConstants.MAIN_GROUP_ID);
    }

    public List<?> getUsersByCondition(UMQueryCondition condition) {
        StringBuffer hql = new StringBuffer("select u, g from User u, GroupUser gu, Group g, Group g1 " +
        		"where g.id = gu.groupId and gu.userId = u.id and g1.id = ? and g.decode like g1.decode||'%'");
        if( condition.getType() != null && condition.getKeyword() != null ) {
            switch (condition.getType()) {
                case 1: hql.append(" and u.loginName like ");   break;
                case 2: hql.append(" and u.userName like ");    break;
                case 3: hql.append(" and u.employeeNo like ");  break;
                case 4: hql.append(" and u.certificateNumber like ");   break;
                default: break;
            }
            hql.append("%" + condition.getKeyword() + "%");
        }
        
        List<User> users = new ArrayList<User>();
        List<?> list = commonDao.getEntities(hql.toString(), condition.getGroupId());
        for( Object temp : list ){
            Object[] objs = (Object[]) temp;
            User user = (User)objs[0];
            
            Group group = (Group)objs[1];
            user.setGroupId(group.getId());
            user.setGroupName(group.getName());
            users.add(user);
        }
        return users;
    }
 
}
