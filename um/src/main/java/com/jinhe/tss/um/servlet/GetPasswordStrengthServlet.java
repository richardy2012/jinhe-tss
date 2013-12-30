package com.jinhe.tss.um.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
import com.jinhe.tss.um.helper.PasswordRule;

/** 
 * 获取密码强度
 */
@WebServlet(urlPatterns="/getPasswordStrength.in")
public class GetPasswordStrengthServlet extends HttpServlet {

	private static final long serialVersionUID = -192831928301L;
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String loginName = request.getParameter("loginName");
		String password  = request.getParameter("password");
		
		String level = PasswordRule.getStrengthLevel(password, loginName);
		
		response.setContentType("text/html;charset=UTF-8");
		XmlHttpEncoder encoder = new XmlHttpEncoder();
		encoder.put("SecurityLevel", level);
		encoder.print(new XmlPrintWriter(response.getWriter()));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
