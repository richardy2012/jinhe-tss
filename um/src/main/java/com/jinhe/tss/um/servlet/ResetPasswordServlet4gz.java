package com.jinhe.tss.um.servlet;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.web.dispaly.SuccessMessageEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.service.IUserService;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.InfoEncoder;

/**
 * <p> Reset Password Servlet </p>
 * <p>
 * 规则：修改某用户密码时同时修改主用户组、OA组、OA LDAP库用户三个地方的密码，以此确保三个地方密码一致。<br>
 * 1、先验证旧密码是否正确（和主用户组密码），不相等则抛出异常结束修改密码流程；<br>
 * 2、修改用户对应的OA系统里LDAP的用户密码，先通过用户自身名称和密码（OA组里可找到）创建LDAP连接，<br>
 *    如创建不了则使用管理员身份创建，然后修改。如果修改失败则结束修改密码流程；<br>
 * 3、修改对应的其它用户组下 OA组中用户的密码；<br>
 * 4、修改主用户组里用户密码。（LoginName + Password MD5加密）<br>
 * </p>
 */
public class ResetPasswordServlet4gz extends HttpServlet {

	private static final long serialVersionUID = -740569423483772472L;
    
    IUserService  userService = (IUserService) Global.getContext().getBean("UserService");
    ILoginService loginService = (ILoginService) Global.getContext().getBean("LoginService");
 
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=GBK");
    	
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        String checkOldPassword = request.getParameter("checkOldPassword");
        
        if(EasyUtils.isNullOrEmpty(userId)) {
        	throw new BusinessException("修改密码时用户ID不能为空");
        }
       
		Long id = Long.valueOf(userId);
		User user = userService.getUserById(id);
        if(user == null) {
           throw new BusinessException("修改密码时找不到用户ID为" + id + "用户，可能已被删除，请联系管理员");
        }
        
        // 检查是否要求旧密码是否正确
        if(!"false".equals(checkOldPassword)
                && !user.getPassword().equals(InfoEncoder.string2MD5(user.getLoginName() + "_" + password))) {
            throw new BusinessException("旧密码输入不正确");
        }
        
        verifyOAPassword(user, newPassword);
        
		// 更新密码
		user.setPassword(InfoEncoder.string2MD5(user.getLoginName() + "_" + (newPassword == null ? password : newPassword)));
		userService.updateUser(user);
        
		SuccessMessageEncoder encoder = new SuccessMessageEncoder("设置新密码成功！密码修改存在延时，请在3分钟后重新登陆系统。如果还有问题，请与管理员联系！");
		encoder.print(new XmlPrintWriter(response.getWriter()));
    }
    
    void verifyOAPassword(User user, String newPassword){
        IPWDOperator oaOperatorDTO = loginService.translateUser(user.getId(), "OA");
        User oaUser =  userService.getUserById(oaOperatorDTO.getId());
        if(oaUser == null) {
            return;
        }
       
        // 初始化参数设置
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.PROVIDER_URL, ParamConfig.getAttribute("oa.ldap.url"));
        env.put(Context.SECURITY_PRINCIPAL, oaUser.getLoginName());
        env.put(Context.SECURITY_CREDENTIALS, oaUser.getPassword());

        // 连接到数据源
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
        } catch (Exception e) {
            env.put(Context.SECURITY_PRINCIPAL, ParamConfig.getAttribute("oa.ldap.administrator"));
            env.put(Context.SECURITY_CREDENTIALS, ParamConfig.getAttribute("oa.ldap.password"));
            try {
                ctx = new InitialDirContext(env);
            } catch (Exception e2) {
                throw new BusinessException("连接LDAP失败，密码修改失败", e2);
            }
        }
        
        ModificationItem modificationItem[] = new ModificationItem[1];
        modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", newPassword));
        try {
            ctx.modifyAttributes(oaUser.getOtherAppUserId(), modificationItem);
        } catch (javax.naming.NoPermissionException e) {
            throw new BusinessException("用户无权修改LDAP用户密码，修改密码失败", e);
        } catch (NamingException e) {
            throw new BusinessException("用户目录:" + oaUser.getOtherAppUserId() + "有误，修改密码失败", e);
        }
        
        oaUser.setPassword(newPassword);
        userService.updateUser(oaUser);
    }
}

	