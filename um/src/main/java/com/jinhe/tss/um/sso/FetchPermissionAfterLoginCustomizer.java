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
 * 登录后将UMS中相关用户对角色信息复制到本地应用的数据库中
 * </p>
 */
public class FetchPermissionAfterLoginCustomizer implements ILoginCustomizer {
    
    ILoginService loginSerivce = (ILoginService) Global.getContext().getBean("LoginService");
    PermissionService permissionService = (PermissionService) Global.getContext().getBean("PermissionService");

    public void execute() {
        
        Long logonUserId = Environment.getOperatorId();
        
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
        session.setAttribute(SSOConstants.LOGINNAME_IN_SESSION, Environment.getOperatorName());
    }
}
