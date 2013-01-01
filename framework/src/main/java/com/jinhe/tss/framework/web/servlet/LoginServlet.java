package com.jinhe.tss.framework.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.web.dispaly.SuccessMessageEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;

/**
 * <p> 系统登录Servlet。 </p>
 * <p>
 * 登录部分工作已经在AutoLoginFilter中完成，此处只要返回成功信息即可。
 * </p>
 */
@WebServlet(name="LoginServlet", urlPatterns="/login.do")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 8087850681949512666L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 登录部分工作已经在AutoLoginFilter中完成，此处只要返回成功信息即可
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 如果是从其它系统单点登录到平台（TSS），则自动转到配置的门户首页地址
        if(request.getParameter("sso") != null){
            String ssoIndex = Config.getAttribute("sso.index.page");
            if(ssoIndex != null){
                response.sendRedirect(response.encodeRedirectURL(ssoIndex));
                return;
            }
        }
        
        response.setContentType("text/html;charset=GBK");
        XmlPrintWriter writer = new XmlPrintWriter(response.getWriter());
        new SuccessMessageEncoder("登录成功", SuccessMessageEncoder.NO_POPUP_TYPE).print(writer);
    }
}

	