package com.jinhe.tss.um.sso;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.log.IBusinessLogger;
import com.jinhe.tss.framework.component.log.Log;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.ILoginCustomizer;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.service.ILoginService;

/**
 * <p>
 * 登录后将TSS中相关用户对角色信息复制到本地应用的数据库中
 * </p>
 */
public class FetchPermissionAfterLogin implements ILoginCustomizer {
    
    ILoginService loginSerivce = (ILoginService) Global.getBean("LoginService");
    PermissionService permissionService = (PermissionService) Global.getBean("PermissionService");
    IBusinessLogger businessLogger = ((IBusinessLogger) Global.getBean("BusinessLogger"));

    public void execute() {
        
        Long logonUserId = Environment.getUserId();
        
        // 1.获取登陆用户的权限（拥有的角色）
        List<Long> roleIds = loginSerivce.getRoleIdsByUserId(logonUserId);
        
        // 2.保存到用户权限（拥有的角色）对应表
        List<Object[]> userRoles = new ArrayList<Object[]>();
        for( Long roleId : roleIds ) {
        	userRoles.add( new Object[] { logonUserId, roleId } );
        }
        permissionService.saveUserRolesAfterLogin(userRoles, logonUserId);
        roleIds.add(UMConstants.ANONYMOUS_ROLE_ID); // 默认加上匿名角色
        
        // 将用户角色信息塞入到session里        
        HttpSession session = Context.getRequestContext().getSession();
        session.setAttribute(SSOConstants.USER_RIGHTS_IN_SESSION, roleIds);
        session.setAttribute(SSOConstants.LOGINNAME_IN_SESSION, Environment.getUserCode());
        
        // 获取登陆用户所在父组，可用于宏代码解析等
        List<Object[]> fatherGroups = loginSerivce.getGroupsByUserId(logonUserId);
        int index = 1, level = fatherGroups.size(); // 层级
        session.setAttribute("GROUP_LEVEL", level);
        
        Object[] lastGroup = new Object[] {-0, "noGroup"};
        for(Object[] temp : fatherGroups) {
        	session.setAttribute("GROUP_" + index + "_ID", temp[0]);
        	session.setAttribute("GROUP_" + index + "_NAME", temp[1]);
        	index++;
        	
        	lastGroup = temp;
        }
        
        session.setAttribute("GROUP_LAST_ID", lastGroup[0]);
    	session.setAttribute("GROUP_LAST_NAME", lastGroup[1]);
    	
    	// 记录登陆成功的日志信息
		Log _log = new Log(Environment.getUserName(), session.getAttribute("LOGIN_MSG"));
    	_log.setOperateTable( "用户登录" );
    	businessLogger.output(_log);
    }
}
