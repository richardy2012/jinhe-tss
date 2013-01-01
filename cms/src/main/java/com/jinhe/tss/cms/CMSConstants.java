package com.jinhe.tss.cms;

/** 
 * CMS常用常量定义
 */
public class CMSConstants {
	
    /**
     * 发布流程控制：控制是否需要生成XML发布文件
     */
    public static final String PARAM_NAME_PUBLISHFLAG = "publishFlag";

    public static final String PUBLISH_ADD = "1"; // 增量发布
    public static final String PUBLISH_ALL = "2"; // 完全发布
    
    /**
     * 布尔型常量
     */
    public static final String BOOLEAN_TRUE  = "true";
    public static final String BOOLEAN_FALSE = "false";
    
    public static final Integer TRUE  = new Integer(1);
    public static final Integer FALSE = new Integer(0);
    
    public static final Integer STATUS_STOP  = TRUE;  // 站点停止状态
    public static final Integer STATUS_START = FALSE; // 站点启动状态
   
    /** 下载地址的路径 */
    public static final String DOWNLOAD_SERVLET_URL = "/download.fun?id="; 
    
    /** 根结点ID */
    public static final Long HEAD_NODE_ID = new Long(-1); 
    
    public static final String COPY_NAME_PREFIX = "副本_";   // 副本前缀
    
    public static final Integer DEFAULT_HIT_COUNT = new Integer(0);	//默认点击率
    
    /*
     * 文章附件
     */
    public static final Integer ATTACHMENTTYPE_PICTURE = new Integer(1);	//文章附件图片类型
    public static final Integer ATTACHMENTTYPE_OFFICE  = new Integer(2);	//文章附件OFFICE类型

    //资源授权相关
    public static final String RESOURCE_TYPE_CHANNEL  = "21";   //资源类型  站点栏目
    
    //栏目资源操作ID
    public static final String OPERATION_VIEW        = "1"; // 查看浏览
    public static final String OPERATION_ADD_CHANNEL = "2"; // 新建栏目
    public static final String OPERATION_ADD_ARTICLE = "3"; // 新建文章
    public static final String OPERATION_PUBLISH     = "4"; // 发布权限
    public static final String OPERATION_EDIT        = "5"; // 编辑权限
    public static final String OPERATION_DELETE      = "6"; // 删除权限
    public static final String OPERATION_STOP_START  = "7"; // 停用启用
    public static final String OPERATION_ORDER       = "8"; // 排序权限
    public static final String OPERATION_MOVE        = "9"; // 移动权限
    
    /*
     * 文章状态
     */
    public static final Integer LOCKING_STATUS   = new Integer(0); // locked
    public static final Integer START_STATUS     = new Integer(1); // 编辑中
    public static final Integer TOPUBLISH_STATUS = new Integer(2); // 待发布
    public static final Integer XML_STATUS       = new Integer(3); // 已发布生成xml文件
    public static final Integer OVER_STATUS      = new Integer(4); // 过期状态
   
    /*
     * 策略类型常量
     */
    public static final Integer TACTIC_TIME_TYPE    = new Integer(0); // 时间策略
    public static final Integer TACTIC_INDEX_TYPE   = new Integer(1); // 索引策略
    public static final Integer TACTIC_PUBLISH_TYPE = new Integer(2); // 发布策略
    public static final Integer TACTIC_EXPIRE_TYPE  = new Integer(3); // 文章过期策略

    //XForm 模板
    public static final String XFORM_SITE     = "template/xform/Site.xml";
    public static final String XFORM_CHANNEL  = "template/xform/Channel.xml";
    public static final String XFORM_ARTICLE  = "template/xform/Article.xml";
    
    public static final String XFORM_ARTICLEUPLOAD    = "template/xform/ArticleUpload.xml";
    public static final String XFORM_SEARCH_ARTICLE   = "template/xform/SearchArticle.xml";
    public static final String XFORM_TACTIC_INDEX     = "template/xform/TacticIndex.xml";
    public static final String XFORM_TACTIC_PUBLISH   = "template/xform/TacticPublish.xml";
    public static final String XFORM_EXPIRE_PUBLISH   = "template/xform/TacticExpire.xml";
    public static final String XFORM_TACTIC_TIME      = "template/xform/TacticTime.xml";
    
    public static final String XFORM_STATISTICS_SEARCH = "template/xform/StatisticsSearch.xml";
    
    //Grid 模板
    public static final String GRID_TEMPLATE_ARTICLELIST   = "template/grid/ArticleList.xml";
    public static final String GRID_ATTACHSLIST        = "template/grid/AttachsList.xml";
}