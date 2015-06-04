package com.jinhe.tss.um.sso;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.ILoginCustomizer;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
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

    public void execute() {
        
        Long logonUserId = Environment.getUserId();
        
        // 1.获取登陆用户的权限（拥有的角色）
        List<Object[]> userRoles = loginSerivce.getUserRolesAfterLogin(logonUserId);
        
        // 2.保存到用户权限（拥有的角色）对应表
        permissionService.saveUserRolesAfterLogin(userRoles, logonUserId);
        
        // 将用户角色信息塞入到session里
        List<Long> roleIds = new ArrayList<Long>();
        for( Object[] urInfo : userRoles ){
            roleIds.add((Long) urInfo[1]);
        }
        
        HttpSession session = Context.getRequestContext().getSession();
        session.setAttribute(SSOConstants.USER_RIGHTS_IN_SESSION, roleIds);
        session.setAttribute(SSOConstants.LOGINNAME_IN_SESSION, Environment.getUserCode());
        
        // 获取登陆用户所在父组，可用于宏代码解析等
        List<Object[]> fatherGroups = loginSerivce.getGroupsByUserId(logonUserId);
        int index = 1, level = fatherGroups.size(); // 层级
        session.setAttribute("GROUP_LEVEL", level);
        for(Object[] temp : fatherGroups) {
        	session.setAttribute("GROUP_" + index + "_ID", temp[0]);
        	session.setAttribute("GROUP_" + index + "_NAME", temp[1]);
        	index++;
        }
    }
}
