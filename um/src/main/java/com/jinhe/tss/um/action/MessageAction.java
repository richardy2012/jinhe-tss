package com.jinhe.tss.um.action;

import java.util.List;

import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormTemplet;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.helper.UMQueryCondition;
import com.jinhe.tss.um.service.IMessageService;
 
public class MessageAction extends BaseActionSupport {

	private static final String XFORM_URI       = "template/xform/message.xml";
	private static final String GRID_URI        = "template/grid/messageGrid.xml";
	private static final String USER_GRID_URI   = "template/grid/userGrid.xml";
	private static final String SEARCH_USER_URI = "template/xform/searchUser.xml";
	
	private static final String SAVE_MODE = "save";
	
	private IMessageService service;
	
	private Long id;
	private String type;
	private String mode;
	private Integer boxId; // 信箱類型，分收件箱、發件箱、草稿箱
	
	private Message message = new Message();
	private UMQueryCondition condition = new UMQueryCondition();
	
	public String getMessageInfo(){
		Message message = new Message();
		if( id != null ){
		    message = service.viewMessage(id);

			if("reply".equals(type)) { // 回复
				Message newMessage = new Message();
				newMessage.setReceiverId(message.getSenderId());
				newMessage.setReceiver(message.getSender());
				newMessage.setTitle("Re: " + message.getTitle());
				message = newMessage;
			} 
			else if("forward".equals(type)) { // 转发
				Message newMessage = new Message();
				newMessage.setContent(message.getContent());
				newMessage.setTitle(message.getTitle());
				message = newMessage;
			}
		}
		XFormEncoder messagerEncoder = new XFormEncoder(XFORM_URI, message);
		return print("MessageInfo", messagerEncoder);
	}
	
	public String saveMessage(){
		if(SAVE_MODE.equals(mode)){
			service.saveMessage(message);
			return printSuccessMessage("保存成功!");
		} 
		else {
			service.sendMessage(message);
			return printSuccessMessage("发送成功!");
		}
	}
	
	public String viewMessage(){
		XFormEncoder messagerEncoder = new XFormEncoder(XFORM_URI, service.viewMessage(id));
		return print("MessageInfo", messagerEncoder);
	}
	
	public String deleteMessage(){
		service.deleteMessage(id);
		return printSuccessMessage("删除成功!");
	}
	
	public String getSearchUserInfo(){
		GridDataEncoder encoder = new GridDataEncoder(null, USER_GRID_URI);
		XFormTemplet template = new XFormTemplet(SEARCH_USER_URI);
		return print(new String[]{"SearchUser", "ExistUserList"}, 
		        new Object[]{template.getTemplet().asXML(), encoder});
	}
	
	public String getGroupTree(){
		List<?> groups = service.getGroupsList();
		TreeEncoder encoder = new TreeEncoder(groups, new LevelTreeParser());
		encoder.setNeedRootNode(false);
		return print("GroupTree", encoder);
	}
	
	public String searchUsers(){
		List<?> users = service.getUsersByCondition(condition);
		GridDataEncoder encoder = new GridDataEncoder(users, USER_GRID_URI);
		return print("SourceList", encoder);
	}
	
	public String getMessageList(){
		List<?> messages = null;
		switch (boxId) {
    		case 1:	break;
    		case 2:	messages = service.getInboxList(); break;
    		case 3:	messages = service.getDraftList(); break;
    		case 4: messages = service.getOutboxList(); break;
    		default: break;
		}
		
		return print("MessageList", new GridDataEncoder(messages, GRID_URI));
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setService(IMessageService service) {
		this.service = service;
	}

	public Message getMessage() {
		return message;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setBoxId(Integer boxId) {
		this.boxId = boxId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UMQueryCondition getCondition() {
		return condition;
	}
}
