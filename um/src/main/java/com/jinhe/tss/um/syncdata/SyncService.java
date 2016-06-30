package com.jinhe.tss.um.syncdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IApplicationDao;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.GroupUser;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.dto.GroupDTO;
import com.jinhe.tss.um.helper.dto.UserDTO;
import com.jinhe.tss.um.permission.ResourcePermission;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;
 
/**
 * TODO 遗留问题
 * 1、如果用户在fromApp中更换的部门，同步时无法自动为其更换部门
 *
 */
@Service("SyncService")
public class SyncService implements ISyncService, Progressable {
	
    @Autowired private IGroupDao  groupDao;
    @Autowired private IApplicationDao  applicationDao;
    @Autowired private ResourcePermission resourcePermission;

    private Map<String, String> initParam(String paramDescXML){
        Map<String, String> param = new HashMap<String, String>();
        if ( EasyUtils.isNullOrEmpty(paramDescXML) ) 
            return param;
        
        Document doc = XMLDocUtil.dataXml2Doc(paramDescXML);
        for (Iterator<?> it = doc.getRootElement().elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            param.put(element.getName(), element.getTextTrim());
        }
        return param;
    }
    
    public Map<String, Object> getCompleteSyncGroupData(Long mainGgroupId, String applicationId, String fromGroupId) {
        Application application = applicationDao.getApplication(applicationId);
        if(application == null) {
            throw new BusinessException("未找到其它应用系统（" + applicationId + ")配置信息");
        }
        
        // 保存UM用户组对其它应用用户组 的 ID对应的关系 key:fromGroupId -- value:mainGgroupId
        Map<String, Long> idMapping = new HashMap<String, Long>();
        
        // 取已经同步的用户组. 设置父子节点关系时用到（其实只需”同步节点“的父节点 ＋ 子枝）
        List<?> allGroups = groupDao.getEntitiesByNativeSql("select t.* from um_group t where t.fromGroupId is not null ", Group.class); 
        for(Iterator<?> it = allGroups.iterator();it.hasNext();){
            Group group = (Group)it.next();
            idMapping.put(group.getFromGroupId(), group.getId());
        }

        Map<String, String> appParams = initParam(application.getParamDesc());
        Integer dataSourceType = application.getDataSourceType();
        List<?> groups = getGroups(dataSourceType, appParams, fromGroupId); //从其它系统获取需要同步的所有用户组
        List<?> users  = getUsers (dataSourceType, appParams, fromGroupId); //从其它系统获取需要同步的所有用户
        
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("groupId", mainGgroupId);
        paramsMap.put("groups", groups);
        paramsMap.put("users", users);
        paramsMap.put("idMapping", idMapping);
        paramsMap.put("fromApp", applicationId);

        return paramsMap;
    }
    
    private List<?> getGroups(Integer dataSourceType, Map<String, String> appParams, String groupId){
        String sql = appParams.get(SyncDataHelper.QUERY_GROUP_SQL_NAME);
        return SyncDataHelper.getOutDataDao(dataSourceType).getOtherGroups(appParams, sql, groupId);
    }

    private List<?> getUsers(Integer dataSourceType, Map<String, String> appParams, String groupId){
        String sql = appParams.get(SyncDataHelper.QUERY_USER_SQL_NAME);
        return SyncDataHelper.getOutDataDao(dataSourceType).getOtherUsers( appParams, sql, groupId );
    }
    
    @SuppressWarnings("unchecked")
	public void execute(Map<String, Object> paramsMap, Progress progress) {
    	String fromApp = (String) paramsMap.get("fromApp");
        List<?> groups = (List<?>)paramsMap.get("groups");
        List<?> users  = (List<?>)paramsMap.get("users");
        Map<String, Long> idMapping = (Map<String, Long>)paramsMap.get("idMapping");
        
        syncGroups(groups, idMapping, progress, fromApp);
        syncUsers (users, idMapping, progress);
    }

    private void syncGroups(List<?> otherGroups, Map<String, Long> idMapping, Progress progress, String fromApp) {
        for (int i = 0; i < otherGroups.size(); i++) {
            GroupDTO groupDto = (GroupDTO) otherGroups.get(i);
            String fromGroupId = groupDto.getId();
            
            Group group = new Group();
            group.setName(groupDto.getName());
            if(groupDto.getDisabled() != null) {
            	group.setDisabled(groupDto.getDisabled());
            }
            group.setDescription(groupDto.getDescription());
            group.setFromApp(fromApp);
            group.setFromGroupId(fromGroupId);
            
            Long parentId = idMapping.get(groupDto.getParentId()); // 获取其它应用组的父组对应UM中组的ID
            parentId = (parentId == null) ? UMConstants.MAIN_GROUP_ID : parentId;
            group.setParentId(parentId);
            group.setSeqNo(groupDao.getNextSeqNo(parentId));
            group.setGroupType(Group.MAIN_GROUP_TYPE);
            
            // 检查组（和该fromApp下的组对应的组）是否已经存在
            List<?> temp = groupDao.getEntities("from Group t where t.fromGroupId=? and t.fromApp=?", fromGroupId, fromApp);
            if( EasyUtils.isNullOrEmpty(temp) ) {
        		/* 
        		 * 有可能管理员已经在tss里手动创建了同名的分组，这样情况下同步组过来会违反组名唯一性约束 
        		 * 先找出同名的Group, 为其设置fromApp和fromGroup
        		 */
        		String name = group.getName();
        		temp = groupDao.getEntities("from Group t where t.parentId=? and t.name=?", parentId, name);
        		if( !EasyUtils.isNullOrEmpty(temp) ) {
        			group = (Group) temp.get(0);
        			group.setFromApp(fromApp);
        			group.setFromGroupId(fromGroupId);
                	groupDao.saveGroup(group);
        		}
        		else {
        			groupDao.saveGroup(group);
        		}
            } else {
            	group = (Group) temp.get(0);
            }
            idMapping.put(fromGroupId, group.getId()); // 保存对应结果
            
            // 补齐权限
            resourcePermission.addResource(group.getId(), group.getResourceType());
            
            updateProgressInfo(progress, otherGroups.size(), i);
        }
    }
    
    private void syncUsers(List<?> otherUsers, Map<String, Long> idMapping, Progress progress) {
        List<String> loginNames = new ArrayList<String>();
        for (int i = 0; i < otherUsers.size(); i++) {
            UserDTO userDto = (UserDTO) otherUsers.get(i);
            if(userDto.getDisabled() == null) {
            	userDto.setDisabled(ParamConstants.FALSE);
            }
            
            // 如果用户登陆名相同，只保存第一个
            if(loginNames.contains(userDto.getLoginName())) continue;
            
            // 如果用户所属的组不存在，则不导入该用户
            Long mainGroupId = idMapping.get(userDto.getGroupId());
            if(mainGroupId == null) continue;
            
            /* 检查相同账号的用户否已经存在: 
             * 如果是之前同步过的，则只更新字段；
             * 如果是已经存在的但不是从该fromApp同步过来的，则忽略该fromApp用户；
             * 如果用户不存在，则新建。
             */
            List<?> temp = groupDao.getEntities("from User t where t.loginName=?", userDto.getLoginName());
            if(temp != null && temp.size() > 0) {
            	User existUser = (User) temp.get(0);
            	if( userDto.getId().equals(existUser.getFromUserId()) ) { 
					existUser.setUserName(userDto.getUserName());
					if(userDto.getEmail() != null) {
						existUser.setEmail(userDto.getEmail());
					}
					existUser.setDisabled(userDto.getDisabled());
					groupDao.update(existUser);
            	}
            }
            else {
            	User user = new User();
                SyncDataHelper.setUserByDTO(user, userDto);
            	user.setGroupId(mainGroupId);
            	groupDao.createObject(user);
            	groupDao.createObject(new GroupUser(user.getId(), mainGroupId));
            }
            
            loginNames.add(userDto.getLoginName());
                
            updateProgressInfo(progress, otherUsers.size(), i);
        }
        
        // 如果循环结束了进度还没有完成，则取消进度（不取消会导致页面一直在请求进度信息）
        if( !progress.isCompleted() ) {
            progress.add(8888888); // 通过设置一个大数（远大于总数）来使进度完成
        }
    }
    
    /** 更新进度信息 */
    private void updateProgressInfo(Progress progress, long total, int index){
    	groupDao.flush();
        
        index = index + 1; // index 从0开始计数
        if(index % 20 == 0) {
            progress.add(20); // 每同步20个更新一次进度信息
        } 
        else if(index == total) {
            progress.add(index % 20); // 如果已经同步完，则将总数除以20取余数做为本次完成个数来更新进度信息
        }
    }
}
