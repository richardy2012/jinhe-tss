/*
 * 树类型
 */
var _TREE_TYPE_SINGLE = "single";
var _TREE_TYPE_MULTI  = "multi";
var _TREE_TYPE_MENU   = "menu";
/*
 * 树控件属性名称
 */
var _TREE_ATTRIBUTE_BASE_URL = "baseurl";
var _TREE_ATTRIBUTE_TREE_TYPE = "treeType";
var _TREE_ATTRIBUTE_SRC = "src";
var _TREE_ATTRIBUTE_SELECTED_SRC = "selected";	  // 选中节点数据源
var _TREE_ATTRIBUTE_SELECTED_IDS = "selectedIds"; //选中节点id字符串
var _TREE_ATTRIBUTE_CAN_MOVE_NODE = "canMoveNode";	
var _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE = "treeNodeSelectedChangeState"; 	// 选中、激活节点是否同步
var _TREE_ATTRIBUTE_OPEN_WITH_CLICK = "treeNodeClickOpenNode";	// 点击文字是否展开/收缩节点
var _TREE_ATTRIBUTE_DISABLED_ALL_CHECKTYPE = "allCheckTypeDisabled";	// 禁止所有节点改变选中状态
var _TREE_ATTRIBUTE_JUST_SELECT_SELF = "selectSelf";	// 选中节点时，不考虑父子关系
var _TREE_ATTRIBUTE_FOCUS_NEW_TREE_NODE = "focusNewTreeNode";	// 新增节点焦点不自动移到新节点上
var _TREE_ATTRIBUTE_DEFAULT_OPEN = "defaultOpen";	// 是否自动打开节点
var _TREE_ATTRIBUTE_DEFAULT_ACTIVE = "defaultActive";	// 默认激活节点方式：none-不选中；root-选中根节点；valid-选中第一个有效节点

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
节点名称
 */
var _TREE_XML_NODE_NAME = "treeNode";
/*
 * 根节点名称
 */
var _TREE_XML_ROOT_NODE_NAME = "actionSet";
/*
 * “全部”节点的ID值
 */
var _TREE_XML_ROOT_TREE_NODE_ID = "_rootId";

/*
 * 选中状态图标地址（控件所在目录为根目录，起始不能有“/”）
 */
var _MULTI_NO_CHECKED_IMAGE_SRC   = "images/no_checked.gif";
var _MULTI_CHECKED_IMAGE_SRC      = "images/checked.gif";
var _MULTI_HALF_CHECKED_IMAGE_SRC = "images/half_checked.gif";
var _SINGLE_NO_SELECTED_IMAGE_SRC = "images/no_selected.gif";
var _SINGLE_SELECTED_IMAGE_SRC    = "images/selected.gif";
var _MULTI_CAN_NOT_CHECK_IMAGE_SRC = "images/checkbox_disabled.gif";
var _SINGLE_CAN_NOT_SELECT_IMAGE_SRC = "images/radio_disabled.gif";
/*
 * 伸缩状态图标地址
 */
var _TREE_NODE_CONTRACT_IMAGE_SRC = "images/contract.gif";
var _TREE_NODE_EXPAND_IMAGE_SRC   = "images/expand.gif";
var _TREE_NODE_LEAF_IMAGE_SRC     = "images/leaf.gif";
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
var _TREE_NODE_DISPLAY_ROW_HEIGHT = 20;	
/*
 * 滚动条的宽度（象素）
 */
var _TREE_SCROLL_BAR_WIDTH = 17;
/*
 * 树控件显示区最小宽度（象素）
 */
var _TREE_BOX_MIN_WIDTH = 10;
/*
 * 树控件显示区最小高度（象素）
 */
var _TREE_BOX_MIN_HEIGHT = 22;
/*
 * 滚动条的滚动事件延迟时间（毫妙）
 */
var _TREE_SCROLL_DELAY_TIME = 0;
/*
 * 拖动节点到最上、下行时循环滚动事件每次延迟时间（毫妙）
 */
var _TREE_SCROLL_REPEAT_DELAY_TIME = 300;
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
var _TREE_SEARCH_TYPE_INEXACT_SEARCH = "hazy";	// 模糊查询
var _TREE_SEARCH_TYPE_EXACT_SEARCH = "rigor";	// 精确查询
/*
 * 查询提示信息
 */
var _TREE_SEARCH_NO_RESULT_MSG          = "没有查询到相应的结果！";
var _TREE_SEARCH_NO_CONDITION_VALUE_MSG = "查询条件不能为空！";
var _TREE_SEARCH_NO_CONDITION_NAME_MSG  = "查询条件的属性名称不能为空！";


/*
 * 根据xml节点获取TreeNode对象的一个实例
 * 参数：	node	xml节点
 * 返回值：	TreeNode
 */
function instanceTreeNode(node) {
	if(node == null) {
		return null;
	}
	return new TreeNode(node);
}

/*
 * 对象名称：TreeNode
 * 参数：	node	xml节点
 * 职责：	树节点对象接口。负责处理节点状态变化。	
 */
function TreeNode(node) {
	this.node = node;
}

TreeNode.prototype = new function() {

	/*
	 * 获取xml节点
	 */
	this.getXmlNode = function() {
		return this.node;
	}
	/*
	 * 是否为子节点已经打开的节点
	 * 返回：	true/false
	 */
	this.isOpened = function() {
		return this.node.getAttribute("_open") == "true";
	}
	/*
	 * 是否为可选择节点
	 * 返回：	true/false
	 */
	this.isCanSelected = function() {
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CANSELECTED) != "0";
	}
	/*
	 * 是否为可用链接节点，即display!=0
	 * 返回：	tree/false
	 */
	this.isEnabled = function() {
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY) != '0';
	}
	/*
	 * 是否为激活节点
	 * 返回：	true/false
	 */
	this.isActive = function() {
		return treeObj.isActiveNode(this.node);
	}
	/*
	 * 获取节点的选择状态
	 * 返回：	多选树：0/1/2；单选数：1/0
	 */
	this.getSelectedState = function() {
		var state = this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CHECKTYPE);
		if(/^(1|2)$/.test(state)) {
			return parseInt(state);
		} 
		return 0;
	}
	/*
	 * 点击节点文字标签时，根据现有状态改成下一个选择状态
	 * 参数：	noChildren	不包含子节点
	 * 根据原有的选择状态，改变状态。如为1，2则返回0，否则返回1
	 */
	this.changeSelectedStateByActive = function(noChildren) {
		treeObj.changeCheckedStateByActive(this, noChildren);
	}
	/*
	 * 根据现有状态改成下一个选择状态
	 * 参数：noChildren	选中节点时不包含子节点
	 *		 noFireChangeEvent	是否触发onChange事件
	 * 根据原有的选择状态，改变状态。如为1，2则返回0，否则返回1
	 */
	this.changeSelectedState = function(noChildren, noFireChangeEvent) {
		this.setSelectedState(treeObj.getNextState(this), noChildren, noFireChangeEvent);
	}
	/*
	 * 设置选中状态，同时刷新相关节点的选择状态
	 * 参数：	state	选择状态
	 *			noChildren	只选中自己节点（只对选中时有效）
	 *			noFireChangeEvent	是否触发onChange事件
	 */
	this.setSelectedState = function(state, noChildren, noFireChangeEvent) {
		if(!this.isCanSelected() || treeObj.isAllDisabledCheckType()) {	//不可选择则返回
			return;
		}
		if(state == 1 && treeObj.isActiveBySelected(state)) {
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeSelectedAndActived(Selected)";
			eventBeforeSelectedAndActived.fire(eventObj);
			if(!eventObj.returnValue) {
				return;
			}
		}
		if(state == 1) {
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeSelected";
			eventBeforeSelected.fire(eventObj);
			if(!eventObj.returnValue) {
				return;
			}
		}
		justSelected(this, state, noChildren, noFireChangeEvent);
		if(!this.isActive() && treeObj.isActiveBySelected(state)) {
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeActivedBySelected";
			eventBeforeActived.fire(eventObj);
			if(!eventObj.returnValue) {
				return;
			}
			justActive(this);
		}
	}

	/*
	 * 获取父节点的TreeNode对象
	 * 返回：	TreeNode/null
	 */
	this.getParent = function() {
		return instanceTreeNode(this.node.parentNode);
	}
	/*
	 * 获取ids，自己和子节点的id字符串，默认为自己和全部子节点中选中状态(全选、半选)的节点id字符串
	 * 参数：isAll	是否为全部自己、子节点的Id
	 *       onlySelected	只包括全选的
	 * 返回：	id，字符串：id1,id2,id3
	 */
	this.getIds = function(isAll, onlySelected) {
		if(isAll) {
			var path = ".|.//" + _TREE_XML_NODE_NAME;
		} 
		else {
			if(onlySelected) {
				var path = ".[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']|.//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']";
			} else {
				var path = ".[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' or @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']|.//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' or @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']";
			}
		}
		var nodes = this.node.selectNodes(path);
		var ids = "";
		for(var i = 0; i < nodes.length; i++) {
			var id = nodes[i].getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
			if(id == _TREE_XML_ROOT_TREE_NODE_ID) {
				continue;
			}
			if(i > 0) {
				ids += ",";
			}
			ids += id;
		}
		return ids;
	}
	
	/*
	 * 获取id
	 */
	this.getId = function() {
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
	}
	/*
	 * 设定id
	 */
	this.setId = function(id) {
		var node = treeObj.getXmlRoot().selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + id + "']");
		if( node && node != this.node ) {
			return alert("同id的节点已经存在！[id:" + id + "]");
		}
		
		//设置xml对象的id
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_ID, id);
	}
	/*
	 * 获取Name
	 * 返回：	name，字符串
	 */
	this.getName = function() {
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_NAME);
	}
	/*
	 * 设定Name
	 */
	this.setName = function(name) {
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_NAME, name);
	}
	/*
	 * 获取FullName
	 * 返回：	fullName，字符串
	 */
	this.getFullName = function() {
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_FULLNAME);
	}
	/*
	 * 设定FullName
	 */
	this.setFullName = function(fullName) {
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_FULLNAME, fullName);
	}
	/*
	 * 激活节点
	 * 参数：noChildren		选中节点时，是否不包含子节点
	 */
	this.setActive = function(noChildren) {
		if( !this.isCanSelected() ) {
			return;
		}
		
        justActive(this);
		justSelected(this, treeObj.getNextState(this), noChildren);
	}
	/*
	 * 打开节点，让节点出现在可视区域内。
	 */
	this.focus = function() {
		// 打开未被打开的父节点，父节点的父节点，以此类推。
		openNode(this.node.parentNode);

		displayObj.resetTotalTreeNodes();

		// 如果节点没有在可视区域内，则滚动节点到可是区域
		displayObj.scrollTo(this.node);
	}
	/*
	 * 设置链接为可用
	 * 参数：isAllParent	是否同时启用所有停用的父节点
	 */
	this.enabled = function(isAllParent) {
		if( isAllParent ) {
			var node = this.node;
			while( node && node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID) != _TREE_XML_ROOT_TREE_NODE_ID
					&& node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY) == '0') {
				node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY, "1");
				node = node.parentNode;
			}
		}
		else {
			this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY, "1");
		}
	}

	/*
	 * 设置链接为不可用
	 * 参数：isAllChildren	是否同时停用子节点
	 */
	this.disabled = function(isAllChildren) {
		if(isAllChildren) {
			var nodes = this.node.selectNodes(".|.//" + _TREE_XML_NODE_NAME);
			for(var i = 0; i < nodes.length; i++) {
				nodes[i].setAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY, "0");
			}
		}
		else {
			this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY, "0");
		}
	}
	/*
	 * 设定节点的可选择属性
	 * 参数：	canSelected:	1/0 前者代表可选择，后者代表不可选
	 */
	this.setCanSelected = function(canSelected) {
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_CANSELECTED, canSelected);
	}

	/*
	 * 点击文字标签时，改变节点伸缩状态
	 */
	this.changeFolderStateByActive = function() {
		treeObj.changeOpenStateByActive(this);
	}

	/*
	 * 改变节点的伸缩状态
	 */
	this.changeFolderState = function() {
		if(this.isOpened()) {	
			this.close();	// 关闭子节点
		} 
		else {
			this.open();	// 打开子节点
		}
	}
	/*
	 * 打开子节点
	 */
	this.open = function() {
		this.node.setAttribute("_open", "true");	// 标记当前节点为打开状态

		// 此节点打开，打开因此节点关闭而关闭的子枝节点，同时去除标记。
		openChildNodesCloseByThisNode(this.node);

		displayObj.resetTotalTreeNodes();
		
		// 如果节点或其打开的子节点没有在可视区域内，则滚动节点使其及其子节点全部出现在可视区或使其在最上端
		displayObj.scrollTo(this.node);
	}
	/*
	 * 关闭子节点
	 */
	this.close = function() {
		this.node.setAttribute("_open", "false");	//标记当前节点为关闭状态

		//此节点关闭，关闭此节点的打开的子枝节点，同时标记关闭的原因。
		closeOpendChildNodes(this.node);

		displayObj.resetTotalTreeNodes();
	}
	/*
	 * 删除当前节点
	 * 返回：	true/false	前者表删除成功，后者表失败
	 */
	this.remove = function() {
		//删除xml中的此节点
		this.node.parentNode.removeChild(this.node);

		displayObj.resetTotalTreeNodes();
		return true;
	}
 
	this.appendChild = function(xml) {
		return _appendChild(xml, this.node);
	}
 
	this.appendRoot = function(xml) {
		return return _appendChild(xml, treeObj.getXmlRoot());;
	}
	/*
	 * 移动当前节点
	 * 参数：	toTreeNode	目标节点的TreeNode对象
	 *			moveState	移动状态：-1，移动到目标节点的上面，1，移动到目标节点的下面，1为缺省状态
	 * 返回：	true/false	是否移动节点成功
	 */
	this.moveTo = function(toTreeNode, moveState) {
		if( !(toTreeNode instanceof TreeNode) || this.node.parentNode == null ) {
			return false;
		}
		
		var beforeNode = (moveState == -1 ? toTreeNode.getXmlNode() : toTreeNode.getXmlNode().nextSibling;
		toTreeNode.getXmlNode().parentNode.insertBefore(this.node, beforeNode);
		
		displayObj.resetTotalTreeNodes();
		return true;
	}
	/*
	 * 获取当前节点的XML节点对象，该对象是一个浅拷贝对象（不包含当前节点子节点）。
	 */
	this.toElement = function() {
		return this.node.cloneNode(false);
	}
	/*
	 * 获取当前节点的XML节点对象的xml字符串
	 * 返回：	xml字符串，当前节点的浅拷贝对象的xml字符串
	 */
	this.toString = function() {
		return this.toElement().xml;
	}
	/*
	 * 获取节点属性字符串
	 */
	this.getAttribute = function(name) {
		return this.node.getAttribute(name);
	}
	/*
	 * 设置节点属性字符串
	 */
	this.setAttribute = function(name, value) {
		value = value || "";

		if(name == _TREE_XML_NODE_ATTRIBUTE_ID) {	//修改id
			this.setId(value);
		}
		else if(name == _TREE_XML_NODE_ATTRIBUTE_NAME) {	//修改name
			this.setName(value);
		}
		else if(name == _TREE_XML_NODE_ATTRIBUTE_FULLNAME) { //修改fullname
			this.setFullName(value);
		}
		else if(name == _TREE_XML_NODE_ATTRIBUTE_DISPLAY) {	// 修改display
			if(value == 1) {
				this.enabled();
			} else {
				this.disabled();
			}
		} else if(name == _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE) { //修改checkType
			this.setSelectedState(value);
		} 
		else if(name == _TREE_XML_NODE_ATTRIBUTE_CANSELECTED) { //修改canSelected
			this.setCanSelected(value);
		}
		else {	// 修改其他属性
			this.node.setAttribute(name, value);
		}
	}
	/*
	 * 使用一段合法的xml字符串更新该节点的所有属性信息。
	 */
	this.setAttrbutesByXmlStr = function(xml) {
		var newNodeXML = loadXmlToNode(xml);
		if(newNodeXML && newNodeXML.documentElement) {
			var attributes = newNodeXML.documentElement.attributes;
			for(var i = 0; i < attributes.length; i++) {
				this.setAttribute(attributes[i].name, attributes[i].value);
			}
		}		
	}
	/*
	 * 刷新页面显示
	 */
	this.reload = function () {
		displayObj.reload();
	}

	
	/*
	 * 添加节点
	 * 参数：	xml	合法的节点xml字符串
	 * 返回：	TreeNode/null	前者表添加根节点成功，返回新节点的TreeNode; 后者表失败
	 */
	function appendChild(xml, parent) {
		//生成新节点
		var xmlDom = loadXmlToNode(xml);
		if(xmlDom == null || xmlDom.documentElement == null || xmlDom.documentElement.nodeName != _TREE_XML_NODE_NAME) {
			return alert("TreeNode对象：新增节点xml数据不能正常解析！");
		}
		
		var newNode = parent.appendChild(xmlDom.documentElement);

		var treeNode = instanceTreeNode(newNode);
		if(treeNode instanceof TreeNode) {
			refreshStatesByNode(treeNode);	 // 根据新节点的选择状态刷新相关节点
		}

		displayObj.resetTotalTreeNodes();
		return treeNode;
	}
	
	/*
	 * 打开因此节点关闭而关闭的节点，即子节点本身是打开的，只是此节点关闭才不显示的
	 */
	function openChildNodesCloseByThisNode(node) {
		var nodes = node.selectNodes(".//" + _TREE_XML_NODE_NAME + "[@_closeBy = '" + node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID) + "']");
		for(var i = 0; i < nodes.length; i++) {
			nodes[i].setAttribute("_open", "true");
			nodes[i].removeAttribute("_closeBy");	//去除因父节点关闭而不显示的标记
		}
	}
	/*
	 * 关闭此节点下已经打开的子节点，即此节点关闭的话，打开的字节点也应关闭
	 */
	function closeOpendChildNodes(node) {
		var nodes = node.selectNodes(".//" + _TREE_XML_NODE_NAME + "[@_open = 'true']");
		for(var i = 0; i < nodes.length; i++) {
			nodes[i].setAttribute("_open", "false");
			nodes[i].setAttribute("_closeBy", node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID));	// 因此节点关闭而不显示
		}
	}
	/*
	 * 激活节点，触发相应事件
	 */
	function justActive(treeNode) {
		// 设置节点为激活
		treeObj.setActiveNode(treeNode);
	}

	/*
	 * 选中节点 
	 */
	function justSelected(treeNode, state, noChildren) {
        if(false == treeObj.isMenu()) {
            if(state == 1 && noChildren && treeNode.node.hasChildNodes()) {
                setNodeState(treeNode.node, 2);
            } else {
                setNodeState(treeNode.node, state);
            }
			
			// 刷新相应节点的选中状态
            refreshStatesByNode(treeNode, noChildren);	 
        }
	}
	
	/*
	 * 根据给定的节点的选中状态，刷新相应节点的选中状态
	 * 参数：	TreeNode节点对象
	 *			noChildren	选中节点时，只选中自己节点，不影响子节点
	 */
	function refreshStatesByNode(treeNode, noChildren) {
		treeObj.refreshStates(treeNode, noChildren);
	}
}





/*
 * 初始化树对象
 */
function instanceTree() {
	var type = getValue(_TREE_ATTRIBUTE_TREE_TYPE, _TREE_TYPE_SINGLE);
	treeObj = new Tree();
	if(type == _TREE_TYPE_MULTI){
		treeObj.setAttribute(_TREE_ATTRIBUTE_TREE_TYPE, _TREE_TYPE_MULTI);
		treeObj.setAttribute(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, 
							getValue(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE_MULTI_DEFAULT_VALUE));
		//获取下一个选中状态
		treeObj.getNextState = multiGetNextState;
		//获取选择状态图标（多选树）
		treeObj.getCheckTypeImageSrc = getMultiCheckTypes;
		//根据特定的节点刷新所有节点的选择状态
		treeObj.refreshStates = multiRefreshStates;
		//获取选中节点TreeNode对象数组
		treeObj.getSelectedTreeNode = multiGetSelectedTreeNode;
		//获取选中节点Xml对象数组
		treeObj.getSelectedXmlNode = multiGetSelectedXmlNode;
	} else {
		treeObj.setAttribute(_TREE_ATTRIBUTE_TREE_TYPE, _TREE_TYPE_SINGLE);
		treeObj.setAttribute(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, 
							getValue(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE_SINGLE_DEFAULT_VALUE));
		//获取下一个选中状态
		treeObj.getNextState = singleGetNextState;
		//获取选择状态图标（单选树）
		treeObj.getCheckTypeImageSrc = getSingleCheckTypes;
		//清除其他节点的选中状态
		treeObj.refreshStates = clearOtherSelectedState;
		//获取选中节点的TreeNode对象
		treeObj.getSelectedTreeNode = singleGetSelectedTreeNode;
		//获取选中节点的Xml对象
		treeObj.getSelectedXmlNode = singleGetSelectedXmlNode;
	}
	
	treeObj.isMenu = function() {
		return type == _TREE_TYPE_MENU;
	}
}


/*
 * 对象名称：Tree	
 * 树对象，处理由于控件参数不同，引起的不同之处
 */
function Tree() {
	var _baseUrl = getValue(_TREE_ATTRIBUTE_BASE_URL, _TREE_ATTRIBUTE_BASE_URL_DEFAULT_VALUE);
	var _treeType = getValue(_TREE_ATTRIBUTE_TREE_TYPE, _TREE_ATTRIBUTE_TREE_TYPE_DEFAULT_VALUE);
	var _src = getValue(_TREE_ATTRIBUTE_SRC, _TREE_ATTRIBUTE_SRC_DEFAULT_VALUE);
	var _selectedSrc = getValue(_TREE_ATTRIBUTE_SELECTED_SRC, _TREE_ATTRIBUTE_SELECTED_SRC_DEFAULT_VALUE);
	var _selectedIds = getValue(_TREE_ATTRIBUTE_SELECTED_IDS, _TREE_ATTRIBUTE_SELECTED_IDS_DEFAULT_VALUE);
	var _canMoveNode = getValue(_TREE_ATTRIBUTE_CAN_MOVE_NODE, _TREE_ATTRIBUTE_CAN_MOVE_NODE_DEFAULT_VALUE);
	var _treeNodeSelectedChangeState = "false";
	var _treeNodeClickOpenNode = getValue(_TREE_ATTRIBUTE_OPEN_WITH_CLICK, _TREE_ATTRIBUTE_OPEN_WITH_CLICK_DEFAULT_VALUE);
	var _allCheckTypeDisabled = getValue(_TREE_ATTRIBUTE_DISABLED_ALL_CHECKTYPE, _TREE_ATTRIBUTE_DISABLED_ALL_CHECKTYPE_DEFAULT_VALUE);
	var _justSelectSelf = 	getValue(_TREE_ATTRIBUTE_JUST_SELECT_SELF, _TREE_ATTRIBUTE_JUST_SELECT_SELF_DEFAULT_VALUE);
	var _focusNewNode = getValue(_TREE_ATTRIBUTE_FOCUS_NEW_TREE_NODE, _TREE_ATTRIBUTE_FOCUS_NEW_TREE_NODE_DEFAULT_VALUE);
	var _defaultOpen = getValue(_TREE_ATTRIBUTE_DEFAULT_OPEN, _TREE_ATTRIBUTE_DEFAULT_OPEN_DEFAULT_VALUE);
	var _defaultActive = getValue(_TREE_ATTRIBUTE_DEFAULT_ACTIVE, _TREE_ATTRIBUTE_DEFAULT_ACTIVE_DEFAULT_VALUE);
	var _activedNode = null;
	var _movedNode = null;
	var _xmlRoot = null;
	var _scrollTimer = null;
	var _findedNode = null;
	
	/*
	 * 设定控件的数据，数据来源为xml节点、数据岛、数据文件或xml字符串
	 */
	this.loadData = function (dataSrc){
		if(isNullOrEmpty(dataSrc)){
			dataSrc = _src;
		}
		var ds = new DataSource(dataSrc);
		_xmlRoot = ds.xmlRoot;
		if(_defaultOpen == "true"){
			openNode(getDefaultOpenedNode(_xmlRoot));
		}
	}

	/*
	 * 获取默认选择状态数据：xml节点、数据岛、数据文件或xml字符串
	 * 参数：	selectedSrc	节点选中状态的数据源
	 *			isClearOldSelected	是否清除原先选中节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-12-23
	 */
	this.loadSelectedData = function (selectedSrc, isClearOldSelected){
		if(_xmlRoot == null){
			return;
		}
		if(isNullOrEmpty(selectedSrc)){
			selectedSrc = _selectedSrc;
		}
		var ds = new DataSource(selectedSrc);
		if(treeObj.getAttribute(_TREE_ATTRIBUTE_TREE_TYPE) == _TREE_TYPE_SINGLE){	//单选树
			singleCheckedDefault(_xmlRoot, ds.xmlRoot);
		}else{
			multiCheckedDefault(_xmlRoot, ds.xmlRoot, isClearOldSelected);
		}
	}
	
	/*
	 * 	 获取默认选择状态数据：id字符串，多id之间用","隔开
	 * 参数：	selectedIds	节点选中状态的Id字符串
	 *			isClearOldSelected	是否清除原先选中节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-4-19
	 */
	this.loadSelectedDataByIds = function (selectedIds, isClearOldSelected, isDependParent){
		if(_xmlRoot == null){
			return;
		}
		if(isNullOrEmpty(selectedIds)){
			selectedIds = _selectedIds;
		}
		if(treeObj.getAttribute(_TREE_ATTRIBUTE_TREE_TYPE) == _TREE_TYPE_SINGLE){	//单选树
			if(selectedIds == null){
				return;
			}
			eval("var selectedIds = '" + selectedIds + "';");
			var node = _xmlRoot.selectSingleNode("//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='"+selectedIds+"']");
			var treeNode = instanceTreeNode(node);
			if(treeNode != null){
				treeNode.setSelectedState(1, false, true);
				treeNode.focus();
			}
		}else{
			multiCheckedDefaultByIds(_xmlRoot, selectedIds, isClearOldSelected, isDependParent);
		}
	}

	/*
	 * 设置默认激活节点
	 * 参数：	type	默认激活类型
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-11-17
	 */
	this.setDefaultActive = function (type) {
		if(isNullOrEmpty(type)){
			type = _defaultActive;
		}
		if(_xmlRoot == null || type == "none"){
			return;
		}
		var activeNode = null;
		if(type == "root"){
			activeNode = _xmlRoot.selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + _TREE_XML_ROOT_TREE_NODE_ID + "']");
		}else if(type == "valid"){
			activeNode = _xmlRoot.selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[(@" + _TREE_XML_NODE_ATTRIBUTE_CANSELECTED + "!='0' or not(@" + _TREE_XML_NODE_ATTRIBUTE_CANSELECTED + ")) and @" + _TREE_XML_NODE_ATTRIBUTE_ID + "!='" + _TREE_XML_ROOT_TREE_NODE_ID + "']");
		}
		var treeNode = instanceTreeNode(activeNode);
		if(treeNode != null){
			treeNode.setActive();
			treeNode.focus();
		}
	}

	/*
	 * 获取数据的根节点
	 * 参数：
	 * 返回值：	xml节点
	 * 作者：scq
	 * 时间：2004-6-25
	 */
	this.getXmlRoot = function () {
		if(_xmlRoot == null){
			var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
			xmlDom.async = false;
			if (xmlDom.loadXML("<actionSet/>")){	
				return xmlDom.documentElement;
			}
		}
		return _xmlRoot;
	}
	/*
	 * 设定当前高亮（激活）的节点
	 * 参数：	treeNode	TreeNode节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-24
	 */
	this.setActiveNode = function (treeNode) {
	    _activedNode = treeNode.getXmlNode();
	}
	/*
	 * 根据属性配置，点击节点文字标签时是否改变节点选择状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-27
	 */
	this.isSelectByActived = function (){
		return _treeNodeSelectedChangeState == "true";
	}

	/*
	 * 根据属性配置，点击节点文字标签时是否改变节点伸缩状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-27
	 */
	this.isChangeFolderStateByClickLabel = function (){
		return _treeNodeClickOpenNode == "true";
	}
	/*
	 * 获取当前高亮（激活）的节点
	 * 参数：
	 * 返回值：	TreeNode对象
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.getActiveNode = function () {
	    return instanceTreeNode(_activedNode);
	}
	/*
	 * 设定对象属性值
	 * 参数：	name	属性名
	 *			value	属性值
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-24
	 */
	this.setAttribute = function (name, value) {
	    switch (name) {
	        case _TREE_ATTRIBUTE_BASE_URL:
				_baseUrl = value;
	            break;
	        case _TREE_ATTRIBUTE_TREE_TYPE:
				_treeType = value;
	            break;
	        case _TREE_ATTRIBUTE_SRC:
				_src = value;
	            break;
	        case _TREE_ATTRIBUTE_CAN_MOVE_NODE:
				_canMoveNode = value;
	            break;
	        case _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE:
				_treeNodeSelectedChangeState = value;
	            break;
	        case _TREE_ATTRIBUTE_OPEN_WITH_CLICK:
				_treeNodeClickOpenNode = value;
	            break;
	        case _TREE_ATTRIBUTE_DISABLED_ALL_CHECKTYPE:
				_allCheckTypeDisabled = value;
	            break;
	        case _TREE_ATTRIBUTE_JUST_SELECT_SELF:
				_justSelectSelf = value;
	            break;
	        default :
				alert("Tree对象：没有属性[" + name + "]!");
	    }
	}
	/*
	 * 获取对象属性
	 * 参数：	name	属性名称
	 * 返回值：	属性值
	 * 作者：scq
	 * 时间：2004-6-24
	 */
	this.getAttribute = function (name) {
	    switch (name) {
	        case _TREE_ATTRIBUTE_BASE_URL:
				return _baseUrl;
	        case _TREE_ATTRIBUTE_TREE_TYPE:
				return _treeType;
	        case _TREE_ATTRIBUTE_SRC:
				return _src;
	        case _TREE_ATTRIBUTE_CAN_MOVE_NODE:
				return _canMoveNode;
	        case _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE:
				return _treeNodeSelectedChangeState;
	        case _TREE_ATTRIBUTE_OPEN_WITH_CLICK:
				return _treeNodeClickOpenNode;
	        case _TREE_ATTRIBUTE_DISABLED_ALL_CHECKTYPE:
				return _allCheckTypeDisabled;
	        case _TREE_ATTRIBUTE_JUST_SELECT_SELF:
				return _justSelectSelf;
	        default :
				alert("Tree对象：没有属性[" + name + "]!");
	    }
	}
	/*
	 * 根据节点不同的checkType属性值获取选择状态图标的地址
	 * 参数：	node	xml节点
	 * 返回值：	String	图标地址
	 * 作者：scq
	 * 时间：2004-6-24
	 */
	this.getCheckTypeImageSrc = function (node) {
	    alert("Tree对象：此方法[getCheckTypeImageSrc]尚未初始化！");
	}
	/*
	 * 判断节点是否高亮（激活）
	 * 参数：	node	xml节点
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.isActiveNode = function (node) {
	    return _activedNode == node;
	}
	/*
	 * 判断节点是否为被拖动的节点
	 * 参数：	node	xml节点
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.isMovedNode = function (node) {
	    return _movedNode == node;
	}
	/*
	 * 判断节点是否为查选结果节点
	 * 参数：	node	xml节点
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2006-1-9
	 */
	this.isFindedNode = function (node) {
	    return _findedNode == node;
	}
	/*
	 * 获取节点文字链接的样式名
	 * 参数：	node	xml节点
	 * 返回值：	String	样式名
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.getClassName = function (node, defaultClassName) {
		if(this.isMovedNode(node)){
			return _TREE_NODE_MOVED_STYLE_NAME;
		}else if(this.isActiveNode(node)){
			return _TREE_NODE_SELECTED_STYLE_NAME;
		}else if(this.isFindedNode(node)){
			return _TREE_NODE_FINDED_STYLE_NAME;
		}
		if(isNullOrEmpty(defaultClassName)){
			return null;
		}
		return defaultClassName;
	}
	/*
	 * 节点被选中时是否需要激活（高亮）节点
	 * 参数：state	节点选中状态
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-7-1
	 */
	this.isActiveBySelected = function (state) {
		return _treeNodeSelectedChangeState == "true" && state == 1;
	}
	/*
	 * 设定被拖动的节点
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.setMovedNode = function (node) {
	    _movedNode = node;
	}
	/*
	 * 获取节点的下一中选中状态
	 * 参数：	treeNode	TreeNode节点对象
	 * 返回值：	0/1	不选中/选中
	 * 作者：scq
	 * 时间：2004-6-28
	 */
	this.getNextState = function (treeNode) {
		alert("Tree对象：此方法[getNextState]尚未初始化！");
	}
	/*
	 * 根据特定的节点，刷新所有节点的选择状态
	 * 参数：	node	xml节点对象
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-28
	 */
	this.refreshStates = function (node) {
		alert("Tree对象：此方法[refreshStates]尚未初始化！");
	}
	/*
	 * 树是否可以移动节点
	 * 参数：
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.isCanMoveNode = function () {
	    return _canMoveNode == "true";
	}
	/*
	 * 树是否禁止改变所有的选择状态
	 * 参数：
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.isAllDisabledCheckType = function () {
	    return _allCheckTypeDisabled == "true";
	}
	/*
	 * 获取选中的节点的TreeNode对象数组
	 * 参数：	hasHalfChecked	是否包含半选节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.getSelectedTreeNode = function (hasHalfChecked) {
	    alert("Tree对象：此方法[getSelectedTreeNode]尚未初始化！");
	}
	/*
	 * 禁止所有节点改变选中状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-29
	 */
	this.disable = function (){
		_allCheckTypeDisabled = "true";
	}
	/*
	 * 允许没有被特殊指定不能选中的节点改变选中状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-29
	 */
	this.enable = function (){
		_allCheckTypeDisabled = "false";
	}
	/*
	 * 新增节点后，是否需要将焦点移到新节点上
	 * 参数：
	 * 返回值：	true	需要移到新节点上
	 *			false	不需要移到新节点上
	 * 作者：scq
	 * 时间：2005-11-17
	 */
	this.isFocusNewTreeNode = function(){
		return _focusNewNode == "true";
	}

	/*
	 * 设定查询结果中的当前节点为特殊高亮显示
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-7-2
	 */
	this.setFindedNode = function (node) {
	    _findedNode = node;
	}
	this.test = function(name){
		return eval(name);
	}
}
/*
 * 获取定义的默认打开节点
 * 参数：	xmlRoot	xml数据
 * 返回值：默认打开的xml节点
 * 作者：scq
 * 时间：2004-6-11
 */
function getDefaultOpenedNode(xmlRoot) {
	if(xmlRoot == null){
		return;
	}
	var actionSetNode = xmlRoot;
	var openedNodeId = actionSetNode.getAttribute(_TREE_XML_NODE_ATTRIBUTE_DEFAULT_OPENED_NODE_ID);
	var openedNode = null;
	if(!isNullOrEmpty(openedNodeId)){
		openedNode = actionSetNode.selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='"+openedNodeId+"']");
	}
	if(isNullOrEmpty(openedNode)){
		openedNode = actionSetNode.selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CANSELECTED + "!='0' or not(@" + _TREE_XML_NODE_ATTRIBUTE_CANSELECTED + ")]");
	}
	return isNullOrEmpty(openedNode)?actionSetNode.firstChild:openedNode;
}
/*
 * 获取控件参数
 * 参数：	name	参数名
 *			defaultValue	默认值
 * 返回值：	String	属性值
 * 作者：scq
 * 时间：2004-6-11
 */
function getValue(name, defaultValue) {
	var value = eval("element." + name);
    if(value == null){
		return defaultValue;
	}
	return value;
}

/*
 * 根据给定节点的选中状态，选中默认节点（多选树，根据xml节点）
 * 参数：	node	需要处理默认选中状态的数据节点
 *			defaltCheckedNode	默认选中状态的数据节点
 *			isClearOldSelected	是否清除原先选中节点
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-23
 */
function multiCheckedDefault(node, defaultCheckedNode, isClearOldSelected) {
	if(node == null){
		return;
	}
	if(isClearOldSelected){
		clearSelected(node);
	}
	if(defaultCheckedNode == null){
		return;
	}
	if (treeObj.getAttribute(_TREE_ATTRIBUTE_JUST_SELECT_SELF) == "true"){
		multiCheckedDefaultOnlySelf(node, defaultCheckedNode);
	}else{
		multiCheckedDefaultWithOther(node, defaultCheckedNode);
	}
}
/*
 * 根据给定节点的选中状态，选中默认节点（多选树，根据Id字符串）
 * 参数：	node	需要处理默认选中状态的数据节点
 *			defaltCheckedIds	默认选中状态的数据节点
 *			isClearOldSelected	是否清除原先选中节点
 * 返回值：
 * 作者：沈超奇
 * 时间：2005-4-19
 */
function multiCheckedDefaultByIds(node, defaltCheckedIds, isClearOldSelected, isDependParent) {
	if(node == null){
		return;
	}
	if(isClearOldSelected){
		clearSelected(node);
	}
	if(defaltCheckedIds == null || defaltCheckedIds == ""){
		return;
	}
	if (treeObj.getAttribute(_TREE_ATTRIBUTE_JUST_SELECT_SELF) == "true"){
		multiCheckedDefaultByIdsOnlySelf(node, defaltCheckedIds);
	}else{
		multiCheckedDefaultByIdsWithOther(node, defaltCheckedIds, isDependParent);
	}
}

/*
 * 去除所有选中节点的选中状态
 * 参数：	node	需要处理去除所有节点选中状态的数据节点
 * 返回值：
 * 作者：沈超奇
 * 时间：2005-9-24
 */
function clearSelected(node){
	var nodes = node.selectNodes(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' or @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']");
	for(var i = 0; i < nodes.length; i++){
		setNodeState(nodes[i], 0);
	}
}

/*
 * 根据给定节点的选中状态，选中默认节点（单选树）
 * 参数：	node	需要处理默认选中状态的数据节点
 *			defaltCheckedNode	默认选中状态的数据节点
 * 返回值：
 * 作者：沈超奇
 * 时间：2005-4-19
 */
function singleCheckedDefault(node, defaultCheckedNode) {
	if(defaultCheckedNode == null || node == null){
		return;
	}
	var checkedNode = defaultCheckedNode.selectSingleNode("//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
	if(checkedNode == null){
		return;
	}
	var fNodeId = checkedNode.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
	var xpath = "//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + fNodeId + "']";
	var fNode = node.selectSingleNode(xpath);
	var treeNode = instanceTreeNode(fNode);
	if(treeNode != null){
		treeNode.changeSelectedState(false, true);
		treeNode.focus();
	}
}

/*
 * 根据给定节点的选中状态，选中默认节点 (多选树，选节点时不考虑父子关系)
 * 参数：	node	需要处理默认选中状态的数据节点
 *			defaltCheckedNode	默认选中状态的数据节点
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-23
 */
function multiCheckedDefaultOnlySelf(node, defaultCheckedNode) {
	if(node == null || defaultCheckedNode == null){
		return;
	}
	var checkedNodes = defaultCheckedNode.selectNodes("//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
	for(var i = 0; i < checkedNodes.length; i++){
		var fNodeId = checkedNodes[i].getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
		var xpath = "//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + fNodeId + "']";
		var fNode = node.selectSingleNode(xpath);
		setNodeState(fNode, 1);
		openNode(fNode);
	}
}
/*
 * 根据给定节点的选中状态，选中默认节点 (多选树，选节点时考虑父子关系)
 * 参数：	node	需要处理默认选中状态的数据节点
 *			defaltCheckedNode	默认选中状态的数据节点
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-23
 */
function multiCheckedDefaultWithOther(node, defaultCheckedNode) {
	if(node == null || defaultCheckedNode == null){
		return;
	}
	var checkedNodes = defaultCheckedNode.selectNodes("//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' && ..[not(@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1') || not(@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + ")]]");
	for(var i = 0; i < checkedNodes.length; i++){
		var fNodeId = checkedNodes[i].getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
		var xpath = "//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + fNodeId + "']";
		var fNode = node.selectSingleNode(xpath);
		if(fNode != null){
			setNodeState(fNode, 1);
			refreshParentNodeState(fNode);
			refreshChildrenNodeState(fNode);
		}
	}
}
/*
 * 根据给定节点的选中状态，选中默认节点 (多选树，选节点时不考虑父子关系)
 * 参数：	node	需要处理默认选中状态的数据节点
 *			defaltCheckedIds	默认选中状态的数据节点id字符串
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-23
 */
function multiCheckedDefaultByIdsOnlySelf(node, defaltCheckedIds) {
	if(node == null || defaltCheckedIds == null){
		return;
	}
	var checkedNodeIds = defaltCheckedIds.split(',');
	for(var i = 0; i < checkedNodeIds.length; i++){
		var xpath = "//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + checkedNodeIds[i] + "']";
		var fNode = node.selectSingleNode(xpath);
		if(fNode != null){
			setNodeState(fNode, 1);
			openNode(fNode);
		}
	}
}

/*
 * 根据给定节点的选中状态，选中默认节点 (多选树，选节点时考虑父子关系)
 * 参数：	node	需要处理默认选中状态的数据节点
 *			defaltCheckedIds	默认选中状态的数据节点id字符串
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-23
 */
function multiCheckedDefaultByIdsWithOther(node, defaltCheckedIds, isDependParent) {
	if(node == null || defaltCheckedIds == null){
		return;
	}
	var checkedNodeIds = defaltCheckedIds.split(',');
	for(var i = 0; i < checkedNodeIds.length; i++){
		var xpath = "//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + checkedNodeIds[i] + "']";
		var fNode = node.selectSingleNode(xpath);
		if(fNode != null){
			setNodeState(fNode, 1);

            //如果下溯，则设置全部子节点为选中
            if(true==isDependParent){
                var xpath = ".//" + _TREE_XML_NODE_NAME;
                var subnodes = fNode.selectNodes(xpath);
                for(var j = 0; j < subnodes.length; j++){
                    setNodeState(subnodes[j], 1);
                }
            
            }
		}
	}
	//标记包含选中节点的所有父节点为选中
	var xpath = "//" + _TREE_XML_NODE_NAME + "[not(@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + " and @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + " = '1') and .//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + " = '1']]";
	var nodes = node.selectNodes(xpath);
	for(var i = 0; i < nodes.length; i++){
		setNodeState(nodes[i], 1);
	}
	//标记所有全选节点中包含未全选节点的节点为半选
	var nodes = node.selectNodes("//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + " = '1' and .//" + _TREE_XML_NODE_NAME + "[not(@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + " = '1') || not(@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + ")]]");
	for(var i = 0; i < nodes.length; i++){
		setNodeState(nodes[i], 2);
	}
}
/*
 * 刷新相关节点的选中状态（多选树），同时根据参数决定是否激活当前节点
 * 参数：	treeNode	TreeNode节点对象
 *			noChildren	选中节点时，不包含子节点
 * 返回值：
 * 作者：scq
 * 时间：2004-6-29
 */
function multiRefreshStates(treeNode, noChildren) {
	if (treeObj.getAttribute(_TREE_ATTRIBUTE_JUST_SELECT_SELF) == "true"){
		return;
	}
	refreshParentNodeState(treeNode.getXmlNode());

	if(noChildren && treeNode.getSelectedState() == 2){
		return;
	}
	refreshChildrenNodeState(treeNode.getXmlNode());
}
/*
 * 刷新所有父节点的选择状态
 * 参数：	tempNode	xml节点
 * 返回值：
 * 作者：scq
 * 时间：2004-6-28
 */
function refreshParentNodeState(tempNode) {
	tempNode = tempNode.parentNode;
	while(tempNode != treeObj.getXmlRoot()){
		setNodeState(tempNode, getChildsNodeState(tempNode));
		tempNode = tempNode.parentNode;
	}
}
/*
 * 获取枝节点的选择状态
 * 参数：	node	xml节点
 * 返回值：	0/1/2	不选/选中/半选
 * 作者：scq
 * 时间：2004-6-28
 */
function getChildsNodeState(node) {
	var nodeChildNum = node.childNodes.length;	//所有子节点数
	var nodeCheckedChildNum = node.selectNodes("./" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']").length;	//全选子节点数
	var nodeHalfCheckedChildNum = node.selectNodes("./" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']").length;	//半选子节点数
	if(nodeCheckedChildNum == 0 && nodeHalfCheckedChildNum == 0){	
		return 0;		//所有子节点都没有选中，标记未选中
	}else if(nodeChildNum == nodeCheckedChildNum){
		return 1;		//所有子节点都被全选，标记全选
	}
	return 2;
}
/*
 * 刷新所有子节点
 * 参数：	node	xml节点对象
 * 返回值：
 * 作者：scq
 * 时间：2004-6-28
 */
function refreshChildrenNodeState(node) {
	var childNodes = node.selectNodes(".//" + _TREE_XML_NODE_NAME);
	for(var i = 0, len = childNodes.length; i < len; i++){
		setNodeState(childNodes[i], node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CHECKTYPE));
	}
}

/*
 * 根据节点选择状态，获取图标地址（多选树）
 * 参数：	node	xml节点
 * 返回值：	String	图标地址
 * 作者：scq
 * 时间：2004-6-29
 */
function getMultiCheckTypes(node) {
	var checkType = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CHECKTYPE);
	var canSelected = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CANSELECTED);
	if(canSelected == 0){
		return this.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _MULTI_CAN_NOT_CHECK_IMAGE_SRC;
	}
	if(checkType == 1) {
		return this.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _MULTI_CHECKED_IMAGE_SRC;
	}
	if(checkType == 2){
		return this.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _MULTI_HALF_CHECKED_IMAGE_SRC;
	}
	return this.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _MULTI_NO_CHECKED_IMAGE_SRC;
}
/*
 * 根据节点选择状态，获取图标地址（单选树）
 * 参数：	node	xml节点
 * 返回值：	String	图标地址
 * 作者：scq
 * 时间：2004-6-29
 */
function getSingleCheckTypes(node) {
	var checkType = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CHECKTYPE);
	var canSelected = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CANSELECTED);
	if(canSelected == 0){
		return this.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _SINGLE_CAN_NOT_SELECT_IMAGE_SRC;
	}
	if(checkType == 1) {
		return this.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _SINGLE_SELECTED_IMAGE_SRC;
	}
	return this.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _SINGLE_NO_SELECTED_IMAGE_SRC;
}
/*
 * 清除特定节点以外的其他节点的选中状态
 * 参数：	treeNode	TreeNode节点对象
 * 返回值：
 * 作者：scq
 * 时间：2004-6-29
 */
function clearOtherSelectedState(treeNode) {
	var childNodes = this.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
	for(var i = 0, len = childNodes.length; i < len; i++){
		if(childNodes[i] == treeNode.getXmlNode()){
			continue;
		}
		setNodeState(childNodes[i], "0");
	}
}
/*
 * 获取选中节点的Xml对象数组（多选树）
 * 参数：	hasHalfChecked	是否包含半选节点
 * 返回值：	Xml对象数组
 * 作者：scq
 * 时间：2005-8-22
 */
function multiGetSelectedXmlNode(hasHalfChecked) {
	var xmlNodeArray = new Array();
	var xmlNodes = null;
	if(hasHalfChecked != false){	//包括半选状态
		xmlNodes = this.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' or @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']");
	}else{	//不包括半选状态
		xmlNodes = this.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
	}
	for(var i = 0; i < xmlNodes.length; i++){
		xmlNodeArray[i] = xmlNodes[i];
	}
	try{
		xmlNodeArray.rootNode = this.getXmlRoot().cloneNode(false);	//获取actionSet节点
	}catch(e){
		alert("控件数据错误，xml不能解析！");
		throw(e);
	}
	xmlNodeArray.hasHalfChecked = hasHalfChecked;
	//给数组提供toElement方法，根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
	xmlNodeArray.toElement = xmlNodesToXmlNode;
	return xmlNodeArray;
}

/*
 * 根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
 * 参数：
 * 返回值：	xml节点
 * 作者：scq
 * 时间：2005-8-22
 */
function xmlNodesToXmlNode() {
	for(var i = 0, len = this.length; i < len; i++){
		if(this[i] == null){
			continue;
		}
		var parentNode = null;
		if(this.hasHalfChecked != false){	//包括半选状态，则以原有节点层次关系返回xml
			parentNode = this.rootNode.selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='"+this[i].parentNode.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID)+"']");
		}
		if (parentNode == null){
			parentNode = this.rootNode;
		}
		parentNode.appendChild(this[i].cloneNode(false));
	}
	return this.rootNode;
}

/*
 * 获取选中节点的TreeNode对象数组（多选树）
 * 参数：	hasHalfChecked	是否包含半选节点
 * 返回值：	TreeNode对象数组
 * 作者：scq
 * 时间：2004-6-29
 */
function multiGetSelectedTreeNode(hasHalfChecked) {
	var treeNodeArray = new Array();	
	var treeNodes = null;
	if(hasHalfChecked != false){	//包括半选状态
		treeNodes = this.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' or @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']");
	}else{	//不包括半选状态
		treeNodes = this.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
	}
	//生成返回的对象数组
	for(var i = 0, len = treeNodes.length; i < len; i++){
		treeNodeArray[i] = instanceTreeNode(treeNodes[i]);
	}
	try{
		treeNodeArray.rootNode = this.getXmlRoot().cloneNode(false);	//获取actionSet节点
	}catch(e){
		alert("控件数据错误，xml不能解析！");
		throw(e);
	}
	treeNodeArray.hasHalfChecked = hasHalfChecked;
	//给数组提供toElement方法，根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
	treeNodeArray.toElement = treeNodesToXmlNode;
	return treeNodeArray;
}
/*
 * 根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
 * 参数：
 * 返回值：	xml节点
 * 作者：scq
 * 时间：2004-6-30
 */
function treeNodesToXmlNode() {
	for(var i = 0, len = this.length; i < len; i++){
		if(this[i] == null){
			continue;
		}
		var parentNode = null;
		if(this.hasHalfChecked != false){	//包括半选状态，则以原有节点层次关系返回xml
			parentNode = this.rootNode.selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='"+this[i].getXmlNode().parentNode.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID)+"']");
		}
		if (parentNode == null){
			parentNode = this.rootNode;
		}
		parentNode.appendChild(this[i].getXmlNode().cloneNode(false));
	}
	return this.rootNode;
}
/*
 * 获取选中节点的TreeNode对象（单选树）
 * 参数：
 * 返回值：	TreeNode对象
 * 作者：scq
 * 时间：2004-6-29
 */
function singleGetSelectedTreeNode(){
	var node = this.getXmlRoot().selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
	return instanceTreeNode(node);
}

/*
 * 获取选中节点的Xml对象（单选树）
 * 参数：
 * 返回值：	Xml对象
 * 作者：scq
 * 时间：2005-8-22
 */
function singleGetSelectedXmlNode(){
	return this.getXmlRoot().selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
}

/*
 * 获取节点的下一选中状态（多选1、2 -> 0; 0 -> 1）
 * 参数：	treeNode	TreeNode对象
 * 返回值：	状态0/1/2
 * 作者：scq
 * 时间：2004-6-30
 */
function multiGetNextState(treeNode) {
	if(/^(2|1)$/.test(treeNode.getSelectedState())){	//半选、全选时，置为不选
		return 0;
	}	
	return 1;	//不选时，置为全选
}
/*
 * 获取节点的下一选中状态（单选）
 * 参数：
 * 返回值：	状态1
 * 作者：scq
 * 时间：2004-6-30
 */
function singleGetNextState() {
    return 1;
}