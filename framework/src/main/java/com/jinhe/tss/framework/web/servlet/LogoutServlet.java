package com.jinhe.tss.framework.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jinhe.tss.framework.web.dispaly.SuccessMessageEncoder;

/**
 * <p> 注销登录的Servlet。 </p>
 * 
 */
@WebServlet(name="LogoutServlet", urlPatterns="/logout.in")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 807941967787932751L;
 
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        doPost(arg0, arg1);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 只销毁了session。在线用户库里的注销工作在session的SessionDestroyedListener里完成
    	request.getSession().invalidate(); 
        
        response.setContentType("text/html;charset=GBK");
        PrintWriter out = response.getWriter();
        out.print( new SuccessMessageEncoder("注销成功", SuccessMessageEncoder.NO_POPUP_TYPE).toXml());
        out.flush();
        out.close();
    }
}
