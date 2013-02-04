//////////////////////////
//			常量			//
//////////////////////////
/*
 * 树类型
 */
var _TREE_TYPE_SINGLE = "single";
var _TREE_TYPE_MULTI = "multi";
var _TREE_TYPE_MENU = "menu";
/*
 * 树控件属性名称
 */
var _TREE_ATTRIBUTE_BASE_URL = "baseurl";	//基本路径
var _TREE_ATTRIBUTE_TREE_TYPE = "treeType";	//树类型
var _TREE_ATTRIBUTE_SRC = "src";	//数据源
var _TREE_ATTRIBUTE_SELECTED_SRC = "selected";	//选中节点数据源
var _TREE_ATTRIBUTE_SELECTED_IDS = "selectedIds";	//选中节点id字符串
var _TREE_ATTRIBUTE_CAN_MOVE_NODE = "canMoveNode";	//是否可拖动节点
var _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE = "treeNodeSelectedChangeState";	//选中、激活节点是否同步
var _TREE_ATTRIBUTE_OPEN_WITH_CLICK = "treeNodeClickOpenNode";	//点击文字是否展开/收缩节点
var _TREE_ATTRIBUTE_DISABLED_ALL_CHECKTYPE = "allCheckTypeDisabled";	//禁止所有节点改变选中状态
var _TREE_ATTRIBUTE_JUST_SELECT_SELF = "selectSelf";	//选中节点时，不考虑父子关系
var _TREE_ATTRIBUTE_FOCUS_NEW_TREE_NODE = "focusNewTreeNode";	//新增节点焦点不自动移到新节点上
var _TREE_ATTRIBUTE_DEFAULT_OPEN = "defaultOpen";	//是否自动打开节点
var _TREE_ATTRIBUTE_DEFAULT_ACTIVE = "defaultActive";	//默认激活节点方式：none-不选中；root-选中根节点；valid-选中第一个有效节点

/*
 * 树控件属性相应的属性值
 */
var _TREE_ATTRIBUTE_BASE_URL_DEFAULT_VALUE = "public/htc/Tree/";	//控件所在目录
var _TREE_ATTRIBUTE_TREE_TYPE_DEFAULT_VALUE = _TREE_TYPE_SINGLE;
var _TREE_ATTRIBUTE_SRC_DEFAULT_VALUE = null;
var _TREE_ATTRIBUTE_SELECTED_SRC_DEFAULT_VALUE = null;
var _TREE_ATTRIBUTE_SELECTED_IDS_DEFAULT_VALUE = null;
var _TREE_ATTRIBUTE_CAN_MOVE_NODE_DEFAULT_VALUE = "false";	//值域： "true" "false"
var _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE_SINGLE_DEFAULT_VALUE = "true";	//值域： "true" "false"
var _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE_MULTI_DEFAULT_VALUE = "false";	//值域： "true" "false"
var _TREE_ATTRIBUTE_OPEN_WITH_CLICK_DEFAULT_VALUE = "false";	//值域： "true" "false"
var _TREE_ATTRIBUTE_DISABLED_ALL_CHECKTYPE_DEFAULT_VALUE = "false";
var _TREE_ATTRIBUTE_JUST_SELECT_SELF_DEFAULT_VALUE = "false";
var _TREE_ATTRIBUTE_FOCUS_NEW_TREE_NODE_DEFAULT_VALUE = "true";
var _TREE_ATTRIBUTE_DEFAULT_OPEN_DEFAULT_VALUE = "true";	
var _TREE_ATTRIBUTE_DEFAULT_ACTIVE_DEFAULT_VALUE = "none";	//第一个有效节点
/*
 * 节点属性名称
 */
var _TREE_XML_NODE_ATTRIBUTE_DEFAULT_OPENED_NODE_ID = "openednodeid";
var _TREE_XML_NODE_ATTRIBUTE_ID = "id";
var _TREE_XML_NODE_ATTRIBUTE_NAME = "name";
var _TREE_XML_NODE_ATTRIBUTE_FULLNAME = "fullname";
var _TREE_XML_NODE_ATTRIBUTE_CANSELECTED = "canselected";
var _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE = "checktype";
var _TREE_XML_NODE_ATTRIBUTE_DISPLAY = "display";
/*
 * 节点名称
 */
var _TREE_XML_NODE_NAME = "treeNode";
/*
 * 根节点名称
 */
var _TREE_XML_ROOT_NODE_NAME = "actionSet";
/**
 * “全部”节点的ID值
 */
var _TREE_XML_ROOT_TREE_NODE_ID = "_rootId";
/*
 * 选中状态图标地址（控件所在目录为根目录，起始不能有“/”）
 */
var _MULTI_NO_CHECKED_IMAGE_SRC = "images/no_checked.gif";
var _MULTI_CHECKED_IMAGE_SRC = "images/checked.gif";
var _MULTI_HALF_CHECKED_IMAGE_SRC = "images/half_checked.gif";
var _SINGLE_NO_SELECTED_IMAGE_SRC = "images/no_selected.gif";
var _SINGLE_SELECTED_IMAGE_SRC = "images/selected.gif";
var _MULTI_CAN_NOT_CHECK_IMAGE_SRC = "images/checkbox_disabled.gif";
var _SINGLE_CAN_NOT_SELECT_IMAGE_SRC = "images/radio_disabled.gif";
/*
 * 伸缩状态图标地址
 */
var _TREE_NODE_CONTRACT_IMAGE_SRC = "images/contract.gif";
var _TREE_NODE_EXPAND_IMAGE_SRC = "images/expand.gif";
var _TREE_NODE_LEAF_IMAGE_SRC = "images/leaf.gif";
var _TREE_ROOT_NODE_CONTRACT_IMAGE_SRC = "images/root_contract.gif";
var _TREE_ROOT_NODE_EXPAND_IMAGE_SRC = "images/root_expand.gif";
var _TREE_ROOT_NODE_LEAF_IMAGE_SRC = "images/root_leaf.gif";
/*
 * 节点选中样式名称
 */
var _TREE_WAIT_LOAD_DATA_MSG = '<span style="margin:5 0 0 8;font-size:12px;color:#666">正在加载数据...</span>';
/*
 * 控件样式名
 */
var _TREE_STYLE_NAME = "Tree";
/*
 * 鼠标移到节点上方样式
 */
var _TREE_NODE_OVER_STYLE_NAME = "hover";
/*
 * 节点移动样式名称
 */
var _TREE_NODE_MOVED_STYLE_NAME = "moved";
/*
 * 查询结果节点样式名称
 */
var _TREE_NODE_FINDED_STYLE_NAME = "finded";
/*
 * 目标节点划线样式
 */
var _TREE_NODE_MOVE_TO_LINE_STYLE = "1px solid #333399";
/*
 * 目标节点隐藏划线样式
 */
var _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE = "1px solid #ffffff";
/*
 * 节点选中样式名称
 */
var _TREE_NODE_SELECTED_STYLE_NAME = "selected";
/*
 * 节点伸缩图标样式名称
 */
var _TREE_NODE_FOLDER_STYLE_NAME = "folder";
/*
 * 节点选择状态图标样式名称
 */
var _TREE_NODE_CHECK_TYPE_STYLE_NAME = "checkType";
/*
 * 节点显示的行高（象素），只用于计算显示的行数，不能控制显示时行的高度
 * 如果要修改显示的行高，修改样式文件
 */
var _TREE_NODE_DISPLAY_ROW_HEIGHT = 20;	//正整数
/*
 * 滚动条的宽度（象素）
 */
var _TREE_SCROLL_BAR_WIDTH = 17;	//正整数
/*
 * 树控件显示区最小宽度（象素）
 */
var _TREE_BOX_MIN_WIDTH = 10;	//正整数
/*
 * 树控件显示区最小高度（象素）
 */
var _TREE_BOX_MIN_HEIGHT = 22;	//正整数
/*
 * 滚动条的滚动事件延迟时间（毫妙）
 */
var _TREE_SCROLL_DELAY_TIME = 0;	//非负整数
/*
 * 拖动节点到最上、下行时循环滚动事件每次延迟时间（毫妙）
 */
var _TREE_SCROLL_REPEAT_DELAY_TIME = 300;	//非负整数
/*
 * 节点自定义图标样式名称
 */
var _TREE_NODE_ICON_STYLE_NAME = "icon";
/*
 * 节点自定义图标属性名
 */
var _TREE_NODE_ICON_ATTRIBUTE = "icon";
/*
 * 节点自定义图标尺寸
 */
var _TREE_NODE_ICON_WIDTH = 16;
var _TREE_NODE_ICON_HEIGHT = 16;
/*
 * 查询方式
 */
var _TREE_SEARCH_TYPE_INEXACT_SEARCH = "hazy";	//模糊查询
var _TREE_SEARCH_TYPE_EXACT_SEARCH = "rigor";	//精确查询
/*
 * 查询提示信息
 */
var _TREE_SEARCH_NO_RESULT_MSG = "没有查询到相应的结果！";	//没有查询到结果
var _TREE_SEARCH_NO_CONDITION_VALUE_MSG = "查询条件不能为空！";	//没有查询条件值
var _TREE_SEARCH_NO_CONDITION_NAME_MSG = "查询条件的属性名称不能为空！";	//没有查询条件名称
