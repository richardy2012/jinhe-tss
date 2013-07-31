package com.jinhe.tss.portal;
 
public interface PortalConstants {
    
    public static final Integer TRUE  = 1;
    public static final Integer FALSE = 0;
    
    public static final Long ROOT_ID = 0L;
 
    public final static String COPY_PREFIX = "副本_"; // 复制实体时实体名字前缀
    
    public static final String PORTAL_CACHE    = "portal_tree";    // 门户树缓存池
    public static final String NAVIGATOR_CACHE = "navigator_pool"; // 导航栏缓存池

    /**
     * 模板路径
     */
    public static final String PORTALSTRUCTURE_XFORM_PATH = "template/xform/Structure.xml"; // 门户结构
    public static final String ISSUE_XFORM_TEMPLET_PATH   = "template/xform/IssueInfo.xml";       // 门户发布信息模板
    public static final String GROUP_XFORM_TEMPLET_PATH   = "template/xform/ElementGroup.xml";    // Group
    public static final String IMPORT_ELEMENT_XFORM_PATH  = "template/xform/ImportElement.xml";   //导入组件

    /**
     * 资源文件目录
     */
    public static final String MODEL_DIR        = "portal/model/";
    public static final String PORTAL_MODEL_DIR = "/" + MODEL_DIR + "portal";
    
    /** 是否启用权限过滤 */
    public final static boolean isPermissionFiltrate = true;
    
    /** 是否注册资源 */
    public final static boolean isResourceRegister   = true;
    
    /** 项目标识以及资源类型 */
    public final static String PORTAL_RESOURCE_TYPE    = "11";
    public final static String NAVIGATOR_RESOURCE_TYPE = "12";
    
    /**
     * 门户结构操作选项 (0表示不判断权限)
     */
    public final static String PORTAL_NONE_OPERRATION   = "0"; //不过滤权限
    public final static String PORTAL_VIEW_OPERRATION   = "1"; //Portal查看操作ID
    public final static String PORTAL_EDIT_OPERRATION   = "2"; //Portal维护操作ID
    public final static String PORTAL_DEL_OPERRATION    = "3"; //Portal删除操作ID
    public final static String PORTAL_ADD_OPERRATION    = "4"; //Portal新增操作ID
    public final static String PORTAL_ORDER_OPERRATION  = "5"; //Portal排序操作ID
    public final static String PORTAL_STOP_OPERRATION   = "6"; //Portal停用操作ID
    public final static String PORTAL_START_OPERRATION  = "7"; //Portal启用操作ID
    public final static String PORTAL_BROWSE_OPERRATION = "8"; //Portal浏览操作ID
    
    public final static String[] PORTAL_OPERRATIONS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
    public final static String[] PORTAL_PARENT_OPERRATIONS = new String[]{"p_0", "p_1", "p_2", "p_3", "p_4", "p_5", "p_6", "p_7", "p_8"};
    
    /**
     * 导航栏操作选项 (0表示不判断权限)
     */
    public final static String NAVIGATOR_NONE_OPERRATION   = "0"; //不过滤权限
    public final static String NAVIGATOR_VIEW_OPERRATION   = "1"; //MENU浏览操作ID
    public final static String NAVIGATOR_EDIT_OPERRATION   = "2"; //MENU维护操作ID
    
    public final static String[] NAVIGATOR_OPERRATIONS = new String[]{"0", "1", "2"};
    public final static String[] NAVIGATOR_PARENT_OPERRATIONS = new String[]{"p_0", "p_1", "p_2"};
}

