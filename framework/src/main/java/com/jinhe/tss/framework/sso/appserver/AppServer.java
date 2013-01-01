package com.jinhe.tss.framework.sso.appserver;

/**
 * <p>
 * 应用系统对象，包含应用系统的相关信息
 * 类似：
 * <server code="TSS"  userDepositoryCode="tss" name="TSS" sessionIdName="JSESSIONID" baseURL="http://localhost:8088/tss"/>
 * <server code="DEMO" userDepositoryCode="tss" name="Demo" sessionIdName="JSESSIONID" baseURL="http://localhost:8088/demo"/>
   <server code="OA" userDepositoryCode="OA" name="DOMINO" sessionIdName="LtpaToken" loginAction="/names.nsf?Login" 
        onlineAction="/app/testxml.nsf/success.xml" onlineValidationCode="Success" singleCookieHeader="true" 
        description="DomAuthSessId" baseURL="http://www.gzcz.com:8081"/>
 * </p>
 *
 */
public class AppServer {

    /**
     * 应用Code：与各应用系统配置文件application.properties中的application.code值对应
     */
    private String code;

    /**
     * 应用系统名称
     */
    private String name;

    /**
     * 应用系统对应的用户库编号：基于平台应用的都为tss用户库，异构类应用（如domino）等另行设置
     */
    private String userDepositoryCode;

    /**
     * 应用系统的访问路径，包括协议类型、域名、端口、上下文路径等信息。
     * 如果是同服务器（可简单配置为如： /demo、/al）
     */
    private String baseURL;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 应用系统使用对应的Cookie中保存SessionID的参数名称，如Java系统一般为JSESSIONID
     */
    private String sessionIdName;

    /**
     * 系统登陆Action地址（相对系统基本访问路径）
     */
    private String loginAction;

    /**
     * 系统中判断用户是否登录
     */
    private String onlineAction;

    /**
     * 系统中判断用户登录请求返回验证字符串
     */
    private String onlineValidationCode;

    /**
     * 系统是否要求将所有Cookie放在一个Header中传输
     */
    private Boolean singleCookieHeader;


    public String getUserDepositoryCode() {
        if (userDepositoryCode == null || "".equals(userDepositoryCode)) {
            return code;
        }
        return userDepositoryCode;
    }

    public void setUserDepositoryCode(String depositoryCode) {
        this.userDepositoryCode = depositoryCode;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionIdName() {
        return sessionIdName;
    }

    public void setSessionIdName(String sessionIdName) {
        this.sessionIdName = sessionIdName;
    }

    public String getLoginAction() {
        return loginAction;
    }

    public void setLoginAction(String loginAction) {
        this.loginAction = loginAction;
    }

    public String getOnlineAction() {
        return onlineAction;
    }

    public void setOnlineAction(String dominoOnlineAction) {
        this.onlineAction = dominoOnlineAction;
    }

    public String getOnlineValidationCode() {
        return onlineValidationCode;
    }

    public void setOnlineValidationCode(String dominoOnlineValidationCode) {
        this.onlineValidationCode = dominoOnlineValidationCode;
    }

    public Boolean isSingleCookieHeader() {
        return singleCookieHeader;
    }

    public void setSingleCookieHeader(Boolean singleCookieHeader) {
        this.singleCookieHeader = singleCookieHeader;
    }

    /**
     * <p>
     * 获取服务器的ContextPath值，baseURL类似："http://127.0.0.1:8088/tss"
     * </p>
     * @return  tss
     */
    public String getPath() {
        if (baseURL == null)
            return null;

        int index = baseURL.lastIndexOf("/");
        if (index > 6) {
            return baseURL.substring(index);
        } else {
            return "/";
        }
    }

    /**
     * <p>
     * 获取服务器的Domain值，baseURL类似："http://127.0.0.1:8088/tss"
     * </p>
     * @return  127.0.0.1
     */
    public String getDomain() {
        if (baseURL == null)
            return null;
        
        int index = baseURL.indexOf("/", 7);
        int colonIndex = baseURL.indexOf(":", 7);
        if (index < 0 && colonIndex < 0) {
            return baseURL.substring(7);
        } 
        else if (colonIndex > -1 && (colonIndex < index || index < 0)) {
            return baseURL.substring(7, colonIndex);
        } 
        else {
            return baseURL.substring(7, index);
        }
    }

    /**
     * <p>
     * 是否为Domino应用服务
     * </p>
     * @return
     */
    public boolean isForeignServer() {
        return loginAction != null && !"".equals(loginAction);
    }
    
    public static void main(String[] args){
        AppServer s = new AppServer();
        s.baseURL = "http://127.0.0.1:8088/tss";
        System.out.println(s.getPath());
        System.out.println(s.getDomain());
    }

}
