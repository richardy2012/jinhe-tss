package com.jinhe.tss.framework.component.param;

/**
 * <p>
 * 参数管理常量
 * </p>
 */
public class ParamConstants {

	// =============================== 系统参数常量定义 ===========================//
	public static final Integer TRUE  = new Integer(1);// 是(状态)
	public static final Integer FALSE = new Integer(0);// 否(状态)
	public static final String PARAM_IDENTIFIER = "code"; // 一个参数的唯一标识
	public static final String COPY_PREFIX_CODE = "copy_"; //复制前缀(code)
	public static final String COPY_PREFIX_NAME = "副本_"; //复制前缀(name)
	public static final String IS_NEW_TAG = "1";	     //新建标志
	public static final Long DEFAULT_PARENT_ID = new Long(0);	//默认父结点ID
	public static final int SAVE_FLAG = 0;  //新建操作
	public static final int EDIT_FLAG = 1;  //编辑操作
	
	//   ========= 组 参数 项 类型 =========
	public static final Integer GROUP_PARAM_TYPE = new Integer(0);	//参数组
	public static final Integer NORMAL_PARAM_TYPE = new Integer(1);	//参数
	public static final Integer ITEM_PARAM_TYPE = new Integer(2);	//参数项
	
	//	========= 参数类型 ========
	public static final Integer SIMPLE_PARAM_MODE = new Integer(0);	//参数类型  简单参数
	public static final Integer COMBO_PARAM_MODE = new Integer(1);	//参数类型  下拉型参数
	public static final Integer TREE_PARAM_MODE = new Integer(2);	//参数类型  树型参数
    
	//	 =============================== XFORM图标路径定义 ===========================//
	public static final String PARAM_GROUP_TREE_ICON_PATH_START = "images/param_group.gif";		//参数组图标路径(启用)
	public static final String PARAM_GROUP_TREE_ICON_PATH_STOP  = "images/param_group_2.gif";	//参数组图标路径(停用)
	public static final String PARAM_ITEM_TREE_ICON_PATH_START = "images/param_item.gif";		//参数项图标路径(启用)
	public static final String PARAM_ITEM_TREE_ICON_PATH_STOP = "images/param_item_2.gif";		//参数项图标路径(停用)
	public static final String PARAM_SIMPLE_ICON_PATH_START = "images/param_simple.gif";	//树结点 简单参数图标路径(启用)
	public static final String PARAM_SIMPLE_ICON_PATH_STOP = "images/param_simple_2.gif";	//树结点 简单参数图标路径(停用)
	public static final String PARAM_COMBO_ICON_PATH_START = "images/param_combo.gif";	//树结点 下拉参数图标路径(启用)
	public static final String PARAM_COMBO_ICON_PATH_STOP = "images/param_combo_2.gif";	//树结点 下拉参数图标路径(停用)
	public static final String PARAM_TREE_ICON_PATH_START = "images/param_tree.gif";		//树结点 树型参数图标路径(启用)
	public static final String PARAM_TREE_ICON_PATH_STOP = "images/param_tree_2.gif";		//树结点 树型参数图标路径(停用)
	
	//	 =============================== XFORM模版路径定义 ===========================//	
	public static final String XFORM_NEW_GROUP = "template/param/xform/groupparam.xml";			//参数组模板路径
	public static final String XFORM_NEW_PARAM_SIMPLE = "template/param/xform/simpleparam.xml";	//简单参数模板路径
	public static final String XFORM_NEW_PARAM_COMPLEX = "template/param/xform/complexparam.xml";	//复杂（下拉，树型）参数模板路径
	public static final String XFORM_NEW_PARAM_ITEM = "template/param/xform/itemparam.xml";	    //参数项模板路径
}

	