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
		}else{
			if(onlySelected) {
				var path = ".[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']|.//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']";
			}else{
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
	 * 返回：	id，字符串
	 */
	this.getId = function() {
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
	}
	/*
	 * 设定id
	 */
	this.setId = function(id) {
		var node = treeObj.getXmlRoot().selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + id + "']");
		if(node != null && node != this.node) {
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
		if(!this.isCanSelected()) {
			return;
		}
		if(!treeObj.isAllDisabledCheckType() && treeObj.isSelectByActived() && treeObj.getNextState(this) == 1) {
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeSelectedAndActived(Active)";
			eventBeforeSelectedAndActived.fire(eventObj);
			if(!eventObj.returnValue) {
				return;
			}
		}
		if(!this.isActive()) {
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeActived";
			eventBeforeActived.fire(eventObj);
			if(!eventObj.returnValue) {
				return;
			}
		}
        justActive(this);
		if(!treeObj.isAllDisabledCheckType() && treeObj.isSelectByActived()) {
			if(treeObj.getNextState(this) == 1) {
				var eventObj = createEventObject();
				eventObj.treeNode = this;
				eventObj.returnValue = true;
				eventObj.type = "_BeforeSelectedByActive";
				eventBeforeSelected.fire(eventObj);
				if(!eventObj.returnValue) {
					return;
				}
			}
			justSelected(this, treeObj.getNextState(this), noChildren, false);
		}
	}
	/*
	 * 打开节点，让节点出现在可视区域内。
	 */
	this.focus = function() {
		//打开未被打开的父节点，父节点的父节点，以此类推。
		openNode(this.node.parentNode);

		displayObj.resetTotalTreeNodes();

		//如果节点没有在可视区域内，则滚动节点到可是区域
		displayObj.scrollTo(this.node);
	}
	/*
	 * 设置链接为可用
	 * 参数：isAllParent	是否同时启用所有停用的父节点
	 */
	this.enabled = function(isAllParent) {
		if(isAllParent) {
			var node = this.node;
			while(node != null && node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID) != _TREE_XML_ROOT_TREE_NODE_ID
					&& node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY) == '0') {
				node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY, "1");
				node = node.parentNode;
			}
		}else{
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
		}else{
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

	/**
	 * 点击文字标签时，改变节点伸缩状态
	 */
	this.changeFolderStateByActive = function() {
		//改变节点的伸缩状态
		treeObj.changeOpenStateByActive(this);
	}

	/*
	 * 改变节点的伸缩状态
	 */
	this.changeFolderState = function() {
		if(this.isOpened()) {	
			this.close();	//关闭子节点
		}else{
			this.open();	//打开子节点
            
            //触发展开事件
            var eventObj = createEventObject();
            eventObj.treeNode = this;
            eventNodeExpand.fire(eventObj);
		}
	}
	/*
	 * 打开子节点
	 */
	this.open = function() {
		this.node.setAttribute("_open", "true");	//标记当前节点为打开状态

		//此节点打开，打开因此节点关闭而关闭的子枝节点，同时去除标记。
		openChildNodesCloseByThisNode(this.node);

		displayObj.resetTotalTreeNodes();
		//如果节点或其打开的子节点没有在可视区域内，则滚动节点使其及其子节点全部出现在可视区或使其在最上端
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