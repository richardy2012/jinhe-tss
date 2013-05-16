package com.jinhe.tss.um;

import java.util.Calendar;

/**
 * UMS相关常量定义文件
 */
public final class UMConstants {

	public static final Integer TRUE  = new Integer(1); // 是(状态)
	public static final Integer FALSE = new Integer(0); // 否(状态)
	
	/** 有效期设置 */
	public static final int ROLE_LIFE_TYPE = Calendar.YEAR; //角色有效期的类型(年)
    public static final int ROLE_LIFE_TIME = 50;            //角色有效期(50年)
    
	public static final int STRATEGY_LIFE_TYPE = Calendar.DAY_OF_YEAR; //策略有效期的类型(日)
	public static final int STRATEGY_LIFE_TIME = 7;                   //策略有效期（7天）
    
	
    /** 默认根结点ID */
    public static final Long DEFAULT_ROOT_ID = new Long(-1); 
    
	public static final String COPY_PREFIX_NAME = "副本_";
	
	public static final String DATA_SOURCE_TYPE_ORACLE = "1";
	public static final String DATA_SOURCE_TYPE_LDAP   = "2";
	public static final String DATA_SOURCE_TYPE_DB2    = "3";
	
	/**
	 * 远程接口路径设置
	 */
	public static final String GENERAL_SEARCH_SERVICE_URL = "/remote/RemoteSearchService"; //综合查询的远程接口路径
	public static final String PERMISSION_SERVICE_URL     = "/remote/PermissionService";  //授权的远程接口路径
 
	/**
	 * 同步功能相关常量定义
	 */
	public static final Long ALL_SYNC    = new Long(1); //完全同步
	public static final Long SINGLE_SYNC = new Long(2); //单向同步
	
    /**
     * 模板的名称定义
     */
    public static final String GROUP_XFORM_TEMPLATE_CODE = "baseXForm4Group";
    public static final String GROUP_GRID_TEMPLATE_CODE  = "baseGrid4Group";
    public static final String GROUP_BASE_INFO_TAB_KEY   = "baseinfo";
    
    public static final String USER_XFORM_TEMPLATE_CODE = "baseXForm4User";
    public static final String USER_GRID_TEMPLATE_CODE  = "baseGrid4User";
    
    public static final String USER_BASE_INFO_TAB_KEY   = "baseinfo";
    public static final String USER_AUTHEN_INFO_TAB_KEY = "authenticateInfo";
    
    public static final String USER_REGISTER_XFORM_CODE = "registerXForm4User";
    public static final String USER_REGISTER_TAB_KEY    = "register";
    
	/**
	 * Grid模板文件的路径定义
	 */
	public static final String MANUAL_MAPPING_GRID      = "template/grid/manualMapping.xml"; //手工对应Grid模板
	public static final String MANUAL_MAPPING_USER_GRID = "template/grid/manualMappingUser.xml"; //手工对应搜索用户Grid模板

	public static final String GENERAL_SEARCH_MAPPING_GRID = "template/grid/generalsearchmapping.xml"; //综合查询搜索用户对应的结果Grid模板
	public static final String GENERAL_SEARCH_ROLE_GRID    = "template/grid/generalsearchroleinfo.xml";// 综合查询根据用户搜索角色情况的Grid模板
	public static final String GENERAL_SEARCH_USER_GRID    = "template/grid/generalsearchuserinfo.xml";//综合查询根据角色搜索用户情况的Grid模板	
	public static final String GENERAL_SEARCH_STRATEGY_GRID = "template/grid/generalsearchuserstrategyinfo.xml";// 综合查询搜索用户因转授而获得的角色的情况的Grid模板
	public static final String GENERAL_SYN_GRID = "template/grid/generalsearchsyninfo.xml";
	
	public static final String MAIN_USER_GRID  = "template/grid/user_list.xml"; //其他用户组下的用户浏览Grid模板
	public static final String OTHER_USER_GRID = "template/grid/otheruser.xml"; //其他用户组下的用户浏览Grid模板
	
	/**
	 *  XForm模板文件的路径定义
	 */
	public static final String GROUP_MAIN_XFORM  = "template/xform/group_main.xml";    
	public static final String GROUP_ASSISTANT_XFORM  = "template/xform/group_assistant.xml";
	public static final String GROUP_OTHER_XFORM  = "template/xform/group_other.xml";
	
	public static final String USER_REGISTER_XFORM  = "template/xform/user_register.xml";  
	public static final String USER_BASEINFO_XFORM  = "template/xform/user_baseinfo.xml"; 
	public static final String USER_AUTHINFO_XFORM  = "template/xform/user_authinfo.xml"; 
	
	public static final String ROLE_XFORM      = "template/xform/role.xml";    
	public static final String ROLEGROUP_XFORM = "template/xform/roleGroup.xml"; 
	public static final String STRATEGY_XFORM  = "template/xform/strategy.xml"; 
    
	public static final String RESOURCETYPE_XFORM           = "template/xform/resourceType.xml";        //ResourceType(新建)
	public static final String APPLICATION_XFORM            = "template/xform/application.xml";         //Application(新建)
	public static final String OTHER_APPLICATION_XFORM      = "template/xform/other_application.xml";   //其他应用系统(新建)
	public static final String OPERATION_XFORM              = "template/xform/operation.xml";           //Operation对象
	public static final String AUTO_MAPPING_XFORM           = "template/xform/automapping.xml";         //模糊对应
	public static final String MANUAL_MAPPING_XFORM         = "template/xform/manualMapping.xml";       //手工对应
	public static final String SERACH_PERMISSION_XFORM      = "template/xform/searchpermission.xml";    //查询权限的
	public static final String AUTH_METHOD_XFORM            = "template/xform/authenticatemethod.xml";  //查询权限转授的
	public static final String IMPORT_APP_XFORM             = "template/xform/importapplication.xml";   //应用系统导入的
	public static final String SEARCH_PERMISSION_XFORM = "template/xform/searchPermissionInfo.xml"; //综合查询搜索用户授权信息的
	public static final String PASSWORDINFO_XFORM      = "template/xform/passwordRule.xml";         //密码规则的
	public static final String PASSWORD_TACTIC_XFORM   = "template/xform/passwordRuleId.xml";       //密码策略设置的
    public static final String PASSWORD_FORGET_XFORM   = "template/xform/password_forget.xml";      //密码忘记提示 
    public static final String PASSWORD_CHANGE_XFORM   = "template/xform/password_change.xml";      //密码修改

	/**
	 * 对象节点 Tree 图标文件的路径定义
	 */
	public static final String START_GROUP_TREENODE_ICON        = "../framework/images/user_group.gif";       //Group图标(启用)
	public static final String START_CODE_GROUP_TREENODE_ICON  = "../framework/images/user_group_code.gif";  //有批次的Group图标(启用)
	public static final String STOP_GROUP_TREENODE_ICON       = "../framework/images/user_group_2.gif";     //Group图标(停用)
	public static final String STOP_CODE_GROUP_TREENODE_ICON = "../framework/images/user_group_code_2.gif";//有批次的Group图标(停用)
	public static final String START_STRATEGY_TREENODE_ICON = "../framework/images/rule.gif";  //Strategy图标(启用)
	public static final String STOP_STRATEGY_TREENODE_ICON = "../framework/images/rule_2.gif"; //Strategy图标(停用)
	public static final String START_USER_GRID_NODE_ICON  = "../framework/images/user.gif";    //User对象节点Grid图标(启用)
	public static final String STOP_USER_GRID_NODE_ICON  = "../framework/images/user_2.gif";   //User对象节点Grid图标(停用)
	public static final String START_ADMIN_USER_GRID_NODE_ICON  = "../framework/images/admin.gif";     //User管理员对象节点Grid图标(启用)
	public static final String STOP_ADMIN_USER_GRID_NODE_ICON  = "../framework/images/admin_2.gif";    //User管理员对象节点Grid图标(停用)
	public static final String START_GROUP_ROLE_TREENODE_ICON = "../framework/images/role_group.gif";  //Role组对象根节点Tree图标(启用)
	public static final String STOP_GROUP_ROLE_TREENODE_ICON = "../framework/images/role_group_2.gif"; //Role组对象根节点Tree图标(停用)
	public static final String START_ROLE_TREENODE_ICON = "../framework/images/role.gif";   //Role对象子节点Tree图标(启用)
	public static final String STOP_ROLE_TREENODE_ICON = "../framework/images/role_2.gif";  //Role对象子节点Tree图标(停用)
	public static final String APPLICATION_TREENODE_ICON   = "../framework/images/app.gif";  //Application图标
	public static final String RESOURCETYPE_TREENODE_ICON = "../framework/images/resource_type.gif";//ResourceType图标
	public static final String OPERATION_TREENODE_ICON   = "../framework/images/permission.gif";    //权限选项Tree图标
	public static final String RESOURCE_TREENODE_ICON   = "../framework/images/resource.gif";       //资源Tree图标
	
	
	// ===========================================================================
	// 定义一些约定好的常量
	// ===========================================================================
	
	public static final Long IS_NEW = new Long(-10); //判断是否为新建对象(和前台约定好的)

    public static final Long NEW_RESOURCE_DEFAULT_PARENT_ID = new Long(0); //新建资源默认的父节点
	
    public static String ADMIN_ROLE_NAME = "系统管理员";     // UMS管理员角色
    public static Long ADMIN_ROLE_ID     = new Long(-1);     //UMS管理员角色的ID
    public static Long ANONYMOUS_ROLE_ID = new Long(-10000); //UMS匿名角色的ID

    public static final String ADMIN_USER_NAME = "Admin";          //系统管理员的ID(初始化数据库时写死的)
	public static final Long ADMIN_USER_ID     = new Long(-1);     //系统管理员的ID(初始化数据库时写死的)
	public static final Long ANONYMOUS_USER_ID = new Long(-10000); //匿名用户的ID(初始化数据库时写死的)
    
	public static final Long MAIN_GROUP_ID                     = new Long(-2); //主用户组的ID (初始化数据库时写死的)
	public static final Long ASSISTANT_GROUP_ID                = new Long(-3); //辅助用户组的ID(初始化数据库时写死的)
	public static final Long OTHER_APPLICATION_GROUP_ID        = new Long(-4); //其他应用组的ID(初始化数据库时写死的)
    public static final Long APPLICATION_RESOURCE_ROOT_ID      = new Long(-5); //应用系统资源根的ID(视图生成)
	public static final Long ROLE_ROOT_ID                      = new Long(-6); //角色资源根的ID(视图生成)
	public static final Long SELF_REGISTER_GROUP_ID            = new Long(-7); //自注册用户组的ID(初始化数据库时写死的)
	public static final Long SELF_REGISTER_GROUP_ID_AUTHENED   = new Long(-8); //自注册用户组的ID(已认证)(初始化数据库时写死的)
	public static final Long SELF_REGISTER_GROUP_ID_NOT_AUTHEN = new Long(-9); //自注册用户组的ID(未认证)(初始化数据库时写死的)
    
	
	// ===========================================================================
    // 资源权限相关
    // ===========================================================================
    
    /**
     * 系统类型id
     */
    public static final String PLATFORM_SYSTEM_NAME = "平台系统"; 
    public static final String OTHER_SYSTEM_NAME    = "其他系统"; 
    
    public static final String PLATFORM_SYSTEM_APP = "-1"; 
    public static final String OTHER_SYSTEM_APP    = "-2"; 
    
    /**
     * 权限选项id的常量定义完毕
     */
    public static final String APPLICATION_TREE_NODE  = "1";  //应用系统节点id
    public static final String RESOURCETYPE_TREE_NODE = "2";  //资源类型节点id
    public static final String OPERATION_TREE_NODE    = "3";  //权限选项节点id

    /**
     * 授权模式
     */
    public static final String LOWER_PERMISSION            = "1"; // 普通授权
    public static final String AUTHORISE_PERMISSION        = "2"; // 可授权授权
    public static final String PASSON_AUTHORISE_PERMISSION = "3"; // 可传递授权
    public static final String SUB_AUTHORISE_PERMISSION    = "4"; // 权限转授
    
    /** 
     * 授权时层次节点的权限维护状态(1-仅此节点,2-该节点及所有下层节点) 
     */
    public static final Integer PERMIT_NODE_SELF = 1; // 仅此节点
    public static final Integer PERMIT_SUB_TREE  = 2; // 该节点及所有下层节点
    
    /**
     * 权限项纵向依赖的类型
     */
    public static final String DEPEND_UP_DOWN = "1"; // 向上兼向下
    public static final String DEPEND_UP      = "2"; // 向上
    public static final String DEPEND_DOWN    = "3"; // 向下
    
    /**
     * 项目标识以及资源类型
     */
    public final static String TSS_APPLICATION_ID = "tss";
    
    public final static String APPLICATION_RESOURCE_TYPE_ID    = "1"; //应用系统资源类型ID
    public final static String MAINGROUP_RESOURCE_TYPE_ID      = "2"; //主用户组资源类型ID
    public final static String ASSISTANTGROUP_RESOURCE_TYPE_ID = "3"; //辅助用户组资源类型ID
    public final static String OTHERAPPGROUP_RESOURCE_TYPE_ID  = "4"; //其他应用用户组资源类型ID
    public final static String ROLE_RESOURCE_TYPE_ID           = "5"; //角色资源类型ID

    /**
     * 用户组资源操作选项 (0表示不判断权限)，默认给新建（子组、用户）的权限
     */
    public final static String USER_ADD_OPERRATION             = "1";  //新建用户
    public final static String GROUP_ADD_OPERRATION            = "2";  //新建组
    public final static String GROUP_DEL_OPERRATION            = "3";  //删除组
    public final static String GROUP_EDIT_OPERRATION           = "4";  //编辑组
    public final static String GROUP_VIEW_OPERRATION           = "5";  //查看组
    public final static String USER_BROWSE_OPERRATION          = "6";  //浏览用户
    public final static String GROUP_ENABLE_OPERRATION         = "7";  //启用组
    public final static String GROUP_DISABLE_OPERRATION        = "7t"; //停用组
    public final static String GROUP_SORT_OPERRATION           = "8";  //排序组
    public final static String GROUP_MOVE_OPERRATION           = "9";  //移动组
    
    public final static String USER_SEARCH_OPERRATION          = "10"; //搜索用户
    public final static String USER_INIT_PMD_OPERRATION        = "11"; //密码初始化
    public final static String USER_SET_AUTHEN_OPERRATION      = "12"; //设置认证方式
    public final static String GROUP_COPY_OPERRATION           = "13"; //复制组
    public final static String GROUP_SYNC_OPERRATION           = "15"; //同步组
    public final static String GROUP_MANUAL_MAPPING_OPERRATION = "17"; //用户对应
    
    /**
     * 应用系统资源操作选项 (0表示不判断权限)
     */
    public final static String APPLICATION_ADD_OPERRATION    = "2";  //新增应用系统
    public final static String APPLICATION_DEL_OPERRATION    = "3";  //删除应用系统
    public final static String APPLICATION_EDIT_OPERRATION   = "4";  //编辑应用系统
    public final static String APPLICATION_VIEW_OPERRATION   = "5";  //查看应用系统
    public final static String APPLICATION_BROWSE_OPERRATION = "6";  //查看应用系统资源列表
    public final static String APPLICATION_SORT_OPERRATION   = "7";  //应用系统资源排序

    /**
     * 角色(组)资源操作选项 (0表示不判断权限)
     */
    public final static String ROLE_ADD_OPERRATION       = "1";  //新建角色
    public final static String ROLE_GROUP_ADD_OPERRATION = "2";  //新建角色组
    public final static String ROLE_DEL_OPERRATION       = "3";  //删除角色/组
    public final static String ROLE_EDIT_OPERRATION      = "4";  //编辑角色/组
    public final static String ROLE_VIEW_OPERRATION      = "5";  //查看角色/组
    public final static String ROLE_ENABLE_OPERRATION    = "6";  //启用角色/组
    public final static String ROLE_DISABLE_OPERRATION   = "7";  //停用角色/组
    public final static String ROLE_SORT_OPERRATION      = "8";  //排序角色/组
    public final static String PERMISSION_SET_OPERRATION = "9";  //角色权限设置
    
}