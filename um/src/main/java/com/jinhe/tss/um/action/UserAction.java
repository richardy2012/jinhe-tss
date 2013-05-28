package com.jinhe.tss.um.action;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.sso.AnonymousOperator;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.online.OnlineUserManagerFactory;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormTemplet;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.UMQueryCondition;
import com.jinhe.tss.um.helper.dto.GroupAutoMapping;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.um.service.IUserService;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;

@Controller
@RequestMapping("/user") 
public class UserAction extends BaseActionSupport {

	private IUserService userService;
	private IApplicationService applicationService;
	
	private Long    userId;
	private Long    toUserId;
	private Long    groupId;
	private Long    toGroupId;
	private String  applicationId;
	private String  user2GroupExistTree;
	private String  user2RoleExistTree;
	private Integer accountState;
	private Integer groupType;
	private String  password;
	private int     direction = 0;
	private String  authenticateMethod;
	private String  disabled;
	private Long    mainGroupId;
	private Integer page;
	private String  field;
	private Integer orderType;
	private Long    appUserId;
	private Long    ruleId;
    private String  isModifyOrRegister; // "modify"：修改用户， "register"：注册用户
	
    private User user = new User();
    private UMQueryCondition userQueryCon = new UMQueryCondition();
    private GroupAutoMapping autoMappingDto = new GroupAutoMapping();

    /**
     * 获取一个User（用户）对象的明细信息、用户对用户组信息、用户对角色的信息
     */
    public String getUserInfoAndRelation() {
        TreeEncoder existRoleTree = new TreeEncoder(null);
        Map<String, Object> data;
        Map<String, Object> map = new HashMap<String, Object>(); 
        if (!UMConstants.IS_NEW.equals(userId)){ // 编辑用户
            data =  userService.getInfo4UpdateExsitUser(userId);
            User user = (User) data.get("UserInfo");
            map.putAll(user.getAttributesForXForm()); 
           
            // 用户已经对应的角色
            existRoleTree = new TreeEncoder(data.get("User2RoleExistTree"));
        }
        else {
            map.put("applicationId", applicationId);
            map.put("disabled", disabled);
            data =  userService.getInfo4CreateNewUser(groupId);
        }
        
        // 所有该用户自己建立的角色
        TreeEncoder roleTree = new TreeEncoder(data.get("User2RoleTree"), new LevelTreeParser());
        roleTree.setNeedRootNode(false);
        
        // 用户对组
        TreeEncoder groupTree = new TreeEncoder(data.get("User2GroupExistTree"));
 
        Document baseinfoXForm = XMLDocUtil.createDoc(UMConstants.USER_BASEINFO_XFORM);
        Document authenXForm   = XMLDocUtil.createDoc(UMConstants.USER_AUTHINFO_XFORM);
        XFormEncoder baseinfoXFormEncoder = new XFormEncoder(baseinfoXForm, map, true, false);
        XFormEncoder authenXFormEncoder   = new XFormEncoder(authenXForm,   map, true, false); 
        
        return print(new String[]{"UserInfo", "AuthenticateInfo", "User2GroupExistTree", "User2RoleTree", "User2RoleExistTree"}, 
                new Object[]{baseinfoXFormEncoder, authenXFormEncoder, groupTree, roleTree, existRoleTree});
    }

	/**
	 * 获取认证方式
	 */
	public String initAuthenticateMethod(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		XFormEncoder authXFormEncoder = new XFormEncoder(UMConstants.AUTH_METHOD_XFORM, map);
		return print("AuthenticateInfo", authXFormEncoder);
	}
	
	public String uniteAuthenticateMethod(){
		userService.uniteAuthenticateMethod(groupId, authenticateMethod);
        return printSuccessMessage();
	}
	
	/**
	 * 新增或修改一个User对象的明细信息、用户对用户组信息、用户对角色的信息
	 */
	public String saveUser() {
		userService.createOrUpdateUserInfo(mainGroupId, user, user2GroupExistTree, user2RoleExistTree);
        return printSuccessMessage();
	}
	
	/**
	 * 启用或者停用用户
	 */
	public String startOrStopUser() {
		userService.startOrStopUser(userId, accountState, groupId);
        return printSuccessMessage();
	}
	
	/**
	 * 用户的移动
	 */
	public String moveUser() {
		userService.moveUser(groupId, toGroupId, userId);
        return printSuccessMessage();
	}
	
	public String importUser(){
		userService.importUser(groupId, toGroupId, userId);
		return printSuccessMessage();
	}
	
	/**
	 * 用户的排序
	 */
	public String sortUser() {
		userService.sortUser(groupId, userId, toUserId, direction);
        return printSuccessMessage();
	}
	
	/**
	 * 用户的排序(跨页)
	 */
	public String sortUserCrossPage() {
		if(1 == direction) { // 向下移动,取下一页第一个
			page = page + 1;
			PageInfo users = userService.getUsersByGroupId(groupId, page);
			List<?> list = users.getItems();
			if( !EasyUtils.isNullOrEmpty(list) ){
			    User toUser = (User)list.get(0);		
				userService.sortUser(groupId, userId, toUser.getId(), direction);			
			}			
		}
		else if(-1 == direction){ // 向上，取上一页最后一个
			page = page - 1;
			PageInfo users = userService.getUsersByGroupId(groupId, page);
			List<?> list = users.getItems();
			if( !EasyUtils.isNullOrEmpty(list) ){
			    User toUser = (User) list.get(list.size() - 1 );				
				userService.sortUser(groupId, userId, toUser.getId(), direction);			
			}			
		}
        return printSuccessMessage();
	}
	
	/**
	 * 删除用户
	 */
	public String deleteUser() {
		userService.deleteUser(groupId, userId, groupType);
        return printSuccessMessage();
	}
	
	/**
	 * 搜索用户
	 */
	public String searchUser() {
		if( !EasyUtils.isNullOrEmpty(field) ) {
			if(orderType != null && orderType == -1) {
				userQueryCon.addOrderByFields( "u." + field + " desc " );
			} else {
				userQueryCon.addOrderByFields( "u." + field + " asc " );
			}
		}
		
        PageInfo users = userService.searchUser(userQueryCon, page);
        GridDataEncoder usersGridEncoder = new GridDataEncoder(users.getItems(), XMLDocUtil.createDoc(UMConstants.MAIN_USER_GRID));
        if(Group.OTHER_GROUP_TYPE.equals(userQueryCon.getGroupType())){
        	Application app = applicationService.getApplication(applicationId);
        	usersGridEncoder = new GridDataEncoder(users.getItems(), getTemplate(UMConstants.OTHER_USER_GRID, app.getName()));
        }
        
        return print(new String[]{"SourceList", "PageList"}, new Object[]{usersGridEncoder, users});
	}
	
	/**
	 * 搜索用户(手工对应时搜索用户)
	 */
	public String searchMappingUser() {
        PageInfo users = userService.searchUser(userQueryCon, page);
        GridDataEncoder usersGridEncoder = new GridDataEncoder(users.getItems(), UMConstants.MANUAL_MAPPING_USER_GRID);
        return print(new String[]{ "SourceList", "PageList" }, new Object[]{ usersGridEncoder, users });
	}
    
    /**
     * 根据用户组的id获取所在用户组的所有用户
     */
    public String getUsersByGroupId() {
        field = "icon".equals(field) ? null : field;
        
        String orderBy = null;
		if( !EasyUtils.isNullOrEmpty(field) ) {
			if(orderType != null && orderType == -1) {
				orderBy = "u." + field + " desc " ;
			} else {
				orderBy = "u." + field + " asc " ;
			}
		}
		
        PageInfo users = userService.getUsersByGroupId(groupId, page, orderBy);
        GridDataEncoder gridEncoder = new GridDataEncoder(users.getItems(), XMLDocUtil.createDoc(UMConstants.MAIN_USER_GRID));
        if(Group.OTHER_GROUP_TYPE.equals(groupType)){
        	Application app = applicationService.getApplication(applicationId);
        	gridEncoder = new GridDataEncoder(users.getItems(), getTemplate(UMConstants.OTHER_USER_GRID, app.getName()));
        }
   
        return print(new String[]{"SourceList", "PageList"}, new Object[]{gridEncoder, users});
    }
 
    
    /* 拼出其他用户组的列表头 */
    private Document getTemplate(String uri, String applicationName){
    	SAXReader saxReader = new SAXReader();
    	Document doc;
    	try{
    		URL fileURL = URLUtil.getResourceFileUrl(uri);
    		doc = saxReader.read(fileURL);
    	} catch (DocumentException e) {
			throw new BusinessException("模板获取失败!");
		}
    	return XMLDocUtil.dataXml2Doc(doc.asXML().replaceAll("applicationName", applicationName));
    }
	
	/**
	 * 编辑模糊对应信息
	 */
	public String editAutoMappingInfo() {
		userService.editAutoMappingInfo(autoMappingDto.getGroupId(), autoMappingDto.getToGroupId(), 
		        autoMappingDto.getType(), autoMappingDto.getMode());
        return printSuccessMessage();
	}
	
	/**
	 * 编辑手工对应信息
	 */
	public String editManualMappingInfo() {
		userService.editManualMappingInfo(userId, appUserId, applicationId);
        return printSuccessMessage();
	}

	/**
	 * 得到操作权限
	 */
	public String getOperation() {
        String resultStr = "u1,u2,u3,u4,u4t";

        String resourceTypeId = Group.getResourceType(groupType);
        List<?> parentOperations = PermissionHelper.getInstance().getOperationsByResource(resourceTypeId, groupId, Environment.getOperatorId());

        if (parentOperations.contains(UMConstants.GROUP_SORT_OPERRATION)) {
            resultStr += ",u5"; // 如果对所在组有排序权限，则对该节点有排序权限
        }
        if(parentOperations.contains(UMConstants.GROUP_MANUAL_MAPPING_OPERRATION)) {
        	resultStr += ",u6";
        }
        if(parentOperations.contains(UMConstants.GROUP_SYNC_OPERRATION)) {
        	resultStr += ",u7";
        }
        
		return print("Operation", "p1,p2," + resultStr);
	}

	/**
	 * 获取认证系统
	 */
	public String getAuthenticateApp(){		
		//获得登陆用户可访问的应用系统名称列表		
		List<?> apps = applicationService.getApplications();
		String[] appEditor = EasyUtils.generateComboedit(apps, "applicationId", "name", "|");
		
		StringBuffer sb = new StringBuffer();
	    sb.append("<column name=\"authenticateAppId\" caption=\"认证系统\" mode=\"string\" editor=\"comboedit\" ");
	    sb.append(" editorvalue=\"").append(appEditor[0]).append("\" ");
	    sb.append(" editortext=\"") .append(appEditor[1]).append("\"/>");

		return print("AuthenticateApplication", sb);
	}

	/**
	 * 根据用户组的id获取所在用户组的所有用户
	 */
	public String getSelectedUsersByGroupId() {
		List<?> users = userService.getUsersByGroup(groupId);
		return print("Group2UserListTree", new TreeEncoder(users));
	}

	/**
	 * 获取模糊对应信息
	 */
	public String getAutoMappingInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		XFormEncoder autoXFormEncoder = new XFormEncoder(UMConstants.AUTO_MAPPING_XFORM, map);
        return print("AutoMapping", autoXFormEncoder);
	}

	/**
	 * 获取手工对应信息
	 */
	public String getManualMappingInfo() {
		List<?> users = userService.getManualMappingInfo(groupId);
		GridDataEncoder usersGridEncoder = new GridDataEncoder(users, UMConstants.MANUAL_MAPPING_GRID);
		
		XFormTemplet templet = new XFormTemplet(UMConstants.MANUAL_MAPPING_XFORM);
		Document doc = templet.getTemplet();
        return print(new String[]{"ManualMapping", "SearchManualMapping"}, new Object[]{usersGridEncoder, doc.asXML()});
	}

	/**
	 * 初始化密码
	 */
	public String initPassword() {		
		userService.initPasswordByGroupId(groupId, password);
        return printSuccessMessage("初始化密码成功！");
	}
	
	/**
	 * 用户自注册
	 */
	public String registerUser() {
        user.setGroupId(UMConstants.SELF_REGISTER_GROUP_ID_NOT_AUTHEN);
		user.setApplicationId(UMConstants.TSS_APPLICATION_ID);
		userService.registerUser(user);
        return printSuccessMessage("用户注册成功！");
	}
    
    /**
     * 用户自己修改个人信息
     */
    public String modifyUserSelf(){
        User old = userService.getUserById(Environment.getOperatorId());
        BeanUtil.setDataToBean(old, user.getAttributesForXForm());
        userService.updateUser(old);
        return printSuccessMessage();
    }

	/**
	 * 获得用户个人信息(注册信息)。
     * 用于用户修改自己的注册信息和密码时用。
	 */
	public String getUserInfo() {
        User user = null;
        if(Context.getIdentityCard().isAnonymous()){
            user = new User();  // 是匿名用户,返回空模版给其注册 
        }else {
            user = userService.getUserById(Environment.getOperatorId()); // 用户信息
        }
        XFormEncoder xEncoder = new XFormEncoder(UMConstants.USER_REGISTER_XFORM, user);
        
        if("modify".equals(isModifyOrRegister)){
            xEncoder.setColumnAttribute("loginName", "editable", "false");
            xEncoder.setColumnAttribute("password", "editable", "false");
            
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("userId", Environment.getOperatorId());
            tempMap.put("userName", Environment.getUserName());
            tempMap.put("loginName", Environment.getOperatorName());
            XFormEncoder pwdEncoder = new XFormEncoder(UMConstants.PASSWORD_CHANGE_XFORM, tempMap);
            return print(new String[]{"UserInfo", "PasswordInfo"}, new Object[]{xEncoder, pwdEncoder});
        }
        return print(new String[]{"UserInfo", "PasswordInfo"}, new Object[]{xEncoder, ""});
	}
    
    /**
     * 密码提示模板
     */
    public String getForgetPasswordInfo() {
        XFormEncoder xEncoder = new XFormEncoder(UMConstants.PASSWORD_FORGET_XFORM);
        return print("ForgetInfo", xEncoder);
    }
    
    /**
     * 获取当前在线用户信息
     */
    public String getOperatorInfo() {
        XmlHttpEncoder encoder = new XmlHttpEncoder();
        encoder.put("id", Environment.getOperatorId());
        if(Context.getIdentityCard().isAnonymous()){
            encoder.put("loginName", AnonymousOperator.anonymous.getLoginName());
            encoder.put("name", AnonymousOperator.anonymous.getLoginName());
        }
        else {
            encoder.put("loginName", Environment.getOperatorName());
            encoder.put("name", Environment.getUserName());
        }
        
        encoder.print(getWriter());
        return XML;
    }
    
    /**
     * 读取在线用户信息
     */
    public String getOnlineUserInfo(){
        Collection<String> userList = OnlineUserManagerFactory.getManager().getOnlineUserNames();
        return print(new String[]{"size", "users"}, 
                new Object[]{userList.size(), EasyUtils.list2Str(userList, " | ")});
    }
	
	/**
     * 设置密码策略
	 */
	public String setPasswordRule(){
		userService.updateUserPasswordRule(userId, ruleId);
		return printSuccessMessage("设置成功!");
	}

    public User getUser() {
        return user;
    }
 
    public UMQueryCondition getUserQueryCon() {
        return userQueryCon;
    }
 
    public GroupAutoMapping getAutoMappingDto() {
        return autoMappingDto;
    }

	public void setUserService(IUserService service) { this.userService = service; }

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setToGroupId(Long toGroupId) {
		this.toGroupId = toGroupId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public void setAccountState(Integer accountState) {
		this.accountState = accountState;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
 
	public void setAuthenticateMethod(String authenticateMethod) {
		this.authenticateMethod = authenticateMethod;
	}

	public void setUser2GroupExistTree(String user2GroupExistTree) {
		this.user2GroupExistTree = user2GroupExistTree;
	}

	public void setUser2RoleExistTree(String user2RoleExistTree) {
		this.user2RoleExistTree = user2RoleExistTree;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public void setMainGroupId(Long mainGroupId) {
		this.mainGroupId = mainGroupId;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

    public void setIsModifyOrRegister(String isModifyOrRegister) {
        this.isModifyOrRegister = isModifyOrRegister;
    }

    public void setApplicationService(IApplicationService applicationService) {
        this.applicationService = applicationService;
    }
}