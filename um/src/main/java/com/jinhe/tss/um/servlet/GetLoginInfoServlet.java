package com.jinhe.tss.um.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
import com.jinhe.tss.um.service.ILoginService;

/**
 * <p>
 * 通过用户登录名，获取用户认证方式及用户名<br>
 * </p>
 */
@WebServlet(urlPatterns="/getLoginInfo.in")
public class GetLoginInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 8680769606094382553L;
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ILoginService service = (ILoginService) Global.getContext().getBean("LoginService");
        
        String loginName = request.getParameter(SSOConstants.LOGINNAME_IN_SESSION);
        try {
        	String[] info = service.getLoginInfoByLoginName(loginName);
            XmlHttpEncoder encoder = new XmlHttpEncoder();
            encoder.put("UserName",  info[0]); //返回用户姓名
            encoder.put("ClassName", info[1]); //返回身份认证器类名：全路径
            
            response.setCharacterEncoding("utf-8");
            encoder.print(new XmlPrintWriter(response.getWriter()));
        } 
        catch(BusinessException e) {
        	throw new BusinessException(e.getMessage(), false); // 无需打印登录异常
        }
    }

}
