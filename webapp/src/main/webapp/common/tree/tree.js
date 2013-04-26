/*
 * 树类型
 */
var _TREE_TYPE_SINGLE = "single";
var _TREE_TYPE_MULTI  = "multi";
var _TREE_TYPE_MENU   = "menu";
/*
 * 树控件属性名称
 */
var _TREE_BASE_URL = "baseurl";
var _TREE_BASE_URL_DEFAULT_VALUE = "common/Tree/";	// 默认控件所在目录

var _TREE_TREE_TYPE = "treeType";
var _TREE_SRC = "src";
var _TREE_SELECTED_SRC = "selected";    // 选中节点数据源
var _TREE_SELECTED_IDS = "selectedIds"; // 选中节点id字符串
var _TREE_CAN_MOVE_NODE = "canMoveNode";	
var _TREE_SELECTED_AND_ACTIVE = "treeNodeSelectedChangeState"; 	// 选中、激活节点是否同步
var _TREE_OPEN_WITH_CLICK = "treeNodeClickOpenNode";	// 点击文字是否展开/收缩节点
var _TREE_DISABLED_ALL_CHECKTYPE = "allCheckTypeDisabled";	// 禁止所有节点改变选中状态
var _TREE_JUST_SELECT_SELF = "selectSelf";	// 选中节点时，不考虑父子关系
var _TREE_FOCUS_NEW_TREE_NODE = "focusNewTreeNode";	// 新增节点焦点不自动移到新节点上
var _TREE_DEFAULT_OPEN   = "defaultOpen";	// 是否自动打开节点
var _TREE_DEFAULT_ACTIVE = "defaultActive";	// 默认激活节点方式：none-不选中；root-选中根节点；valid-选中第一个有效节点
 
/*
 * 节点属性名称
 */
var _DEFAULT_OPENED_TREE_NODE_ID = "openednodeid";
var _TREE_NODE_ID = "id";
var _TREE_NODE_NAME = "name";
var _TREE_NODE_FULLNAME = "fullname";
var _TREE_NODE_CANSELECTED = "canselected";
var _TREE_NODE_CHECKTYPE = "checktype";
var _TREE_NODE_DISPLAY = "display";

/*
节点名称
 */
var _TREE_NODE_NAME = "treeNode";
/*
 * 根节点名称
 */
var _TREE_ROOT_NODE_NAME = "actionSet";
/*
 * “全部”节点的ID值
 */
var _TREE_ROOT_TREE_NODE_ID = "_rootId";

/*
 * 选中状态图标地址（控件所在目录为根目录，起始不能有“/”）
 */
var _MULTI_NO_CHECKED_IMAGE   = "images/no_checked.gif";
var _MULTI_CHECKED_IMAGE      = "images/checked.gif";
var _MULTI_HALF_CHECKED_IMAGE = "images/half_checked.gif";
var _SINGLE_NO_SELECTED_IMAGE = "images/no_selected.gif";
var _SINGLE_SELECTED_IMAGE    = "images/selected.gif";
var _MULTI_CAN_NOT_CHECK_IMAGE  = "images/checkbox_disabled.gif";
var _RADIO_CAN_NOT_SELECT_IMAGE = "images/radio_disabled.gif";
/*
 * 伸缩状态图标地址
 */
var _TREE_NODE_CONTRACT_IMAGE = "images/contract.gif";
var _TREE_NODE_EXPAND_IMAGE   = "images/expand.gif";
var _TREE_NODE_LEAF_IMAGE     = "images/leaf.gif";
var _TREE_ROOT_NODE_CONTRACT_IMAGE = "images/root_contract.gif";
var _TREE_ROOT_NODE_EXPAND_IMAGE = "images/root_expand.gif";
var _TREE_ROOT_NODE_LEAF_IMAGE = "images/root_leaf.gif";

/*
 * 节点选中样式名称
 */
var _TREE_WAIT_LOAD_DATA_MSG = '<span style="margin:5 0 0 8;font-size:12px;color:#666">正在加载数据...</span>';
/*
 * 控件样式名
 */
var _TREE_STYLE = "Tree";
/*
 * 鼠标移到节点上方样式
 */
var _TREE_NODE_OVER_STYLE = "hover";
/*
 * 节点移动样式名称
 */
var _TREE_NODE_MOVED_STYLE = "moved";
/*
 * 查询结果节点样式名称
 */
var _TREE_NODE_FINDED_STYLE = "finded";
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
var _TREE_NODE_SELECTED_STYLE = "selected";
/*
 * 节点伸缩图标样式名称
 */
var _TREE_NODE_FOLDER_STYLE = "folder";
/*
 * 节点选择状态图标样式名称
 */
var _TREE_NODE_CHECK_TYPE_STYLE = "checkType";
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
var _TREE_NODE_ICON_STYLE = "icon";
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
		return this.node.getAttribute(_TREE_NODE_CANSELECTED) != "0";
	}
	/*
	 * 是否为可用链接节点，即display!=0
	 * 返回：	tree/false
	 */
	this.isEnabled = function() {
		return this.node.getAttribute(_TREE_NODE_DISPLAY) != '0';
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
		var state = this.node.getAttribute(_TREE_NODE_CHECKTYPE);
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
			var path = ".|.//" + _TREE_NODE_NAME;
		} 
		else {
			if(onlySelected) {
				var path = ".[@" + _TREE_NODE_CHECKTYPE + "='1']|.//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']";
			} else {
				var path = ".[@" + _TREE_NODE_CHECKTYPE + "='1' or @" + _TREE_NODE_CHECKTYPE + "='2']|.//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1' or @" + _TREE_NODE_CHECKTYPE + "='2']";
			}
		}
		var nodes = this.node.selectNodes(path);
		var ids = "";
		for(var i = 0; i < nodes.length; i++) {
			var id = nodes[i].getAttribute(_TREE_NODE_ID);
			if(id == _TREE_ROOT_TREE_NODE_ID) {
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
		return this.node.getAttribute(_TREE_NODE_ID);
	}
	/*
	 * 设定id
	 */
	this.setId = function(id) {
		var node = treeObj.getXmlRoot().selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + id + "']");
		if( node && node != this.node ) {
			return alert("同id的节点已经存在！[id:" + id + "]");
		}
		
		//设置xml对象的id
		this.node.setAttribute(_TREE_NODE_ID, id);
	}
	/*
	 * 获取Name
	 * 返回：	name，字符串
	 */
	this.getName = function() {
		return this.node.getAttribute(_TREE_NODE_NAME);
	}
	/*
	 * 设定Name
	 */
	this.setName = function(name) {
		this.node.setAttribute(_TREE_NODE_NAME, name);
	}
	/*
	 * 获取FullName
	 * 返回：	fullName，字符串
	 */
	this.getFullName = function() {
		return this.node.getAttribute(_TREE_NODE_FULLNAME);
	}
	/*
	 * 设定FullName
	 */
	this.setFullName = function(fullName) {
		this.node.setAttribute(_TREE_NODE_FULLNAME, fullName);
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
			while( node && node.getAttribute(_TREE_NODE_ID) != _TREE_ROOT_TREE_NODE_ID
					&& node.getAttribute(_TREE_NODE_DISPLAY) == '0') {
				node.setAttribute(_TREE_NODE_DISPLAY, "1");
				node = node.parentNode;
			}
		}
		else {
			this.node.setAttribute(_TREE_NODE_DISPLAY, "1");
		}
	}

	/*
	 * 设置链接为不可用
	 * 参数：isAllChildren	是否同时停用子节点
	 */
	this.disabled = function(isAllChildren) {
		if(isAllChildren) {
			var nodes = this.node.selectNodes(".|.//" + _TREE_NODE_NAME);
			for(var i = 0; i < nodes.length; i++) {
				nodes[i].setAttribute(_TREE_NODE_DISPLAY, "0");
			}
		}
		else {
			this.node.setAttribute(_TREE_NODE_DISPLAY, "0");
		}
	}
	/*
	 * 设定节点的可选择属性
	 * 参数：	canSelected:	1/0 前者代表可选择，后者代表不可选
	 */
	this.setCanSelected = function(canSelected) {
		this.node.setAttribute(_TREE_NODE_CANSELECTED, canSelected);
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

		if(name == _TREE_NODE_ID) {	//修改id
			this.setId(value);
		}
		else if(name == _TREE_NODE_NAME) {	//修改name
			this.setName(value);
		}
		else if(name == _TREE_NODE_FULLNAME) { //修改fullname
			this.setFullName(value);
		}
		else if(name == _TREE_NODE_DISPLAY) {	// 修改display
			if(value == 1) {
				this.enabled();
			} else {
				this.disabled();
			}
		} else if(name == _TREE_NODE_CHECKTYPE) { //修改checkType
			this.setSelectedState(value);
		} 
		else if(name == _TREE_NODE_CANSELECTED) { //修改canSelected
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
		if(xmlDom == null || xmlDom.documentElement == null || xmlDom.documentElement.nodeName != _TREE_NODE_NAME) {
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
		var nodes = node.selectNodes(".//" + _TREE_NODE_NAME + "[@_closeBy = '" + node.getAttribute(_TREE_NODE_ID) + "']");
		for(var i = 0; i < nodes.length; i++) {
			nodes[i].setAttribute("_open", "true");
			nodes[i].removeAttribute("_closeBy");	//去除因父节点关闭而不显示的标记
		}
	}
	/*
	 * 关闭此节点下已经打开的子节点，即此节点关闭的话，打开的字节点也应关闭
	 */
	function closeOpendChildNodes(node) {
		var nodes = node.selectNodes(".//" + _TREE_NODE_NAME + "[@_open = 'true']");
		for(var i = 0; i < nodes.length; i++) {
			nodes[i].setAttribute("_open", "false");
			nodes[i].setAttribute("_closeBy", node.getAttribute(_TREE_NODE_ID));	// 因此节点关闭而不显示
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



var treeObj;

/*
 * 初始化树对象
 */
function instanceTree() {
	
	treeObj = new Tree();
	
	var type = getValue(_TREE_TREE_TYPE, _TREE_TYPE_SINGLE);
	if(type == _TREE_TYPE_MULTI) {
		treeObj.setAttribute(_TREE_TREE_TYPE, _TREE_TYPE_MULTI);
		treeObj.setAttribute(_TREE_SELECTED_AND_ACTIVE, getValue(_TREE_SELECTED_AND_ACTIVE, "false"));

		/*
		 * 获取节点的下一选中状态（多选1、2 -> 0; 0 -> 1）
		 */
		treeObj.getNextState = function () {
			if(/^(2|1)$/.test(this.getSelectedState())) {	// 半选、全选时，置为不选
				return 0;
			}	
			return 1;	// 不选时，置为全选
		};		
		
		/*
		 * 根据节点选择状态，获取图标地址（多选树）
		 */
		treeObj.getCheckTypeImageSrc = function(node) {
			var checkType   = node.getAttribute(_TREE_NODE_CHECKTYPE);
			var canSelected = node.getAttribute(_TREE_NODE_CANSELECTED);
			if(canSelected == 0) {
				return this._baseUrl + _MULTI_CAN_NOT_CHECK_IMAGE;
			}
			if(checkType == 1) {
				return this._baseUrl + _MULTI_CHECKED_IMAGE;
			}
			if(checkType == 2) {
				return this._baseUrl + _MULTI_HALF_CHECKED_IMAGE;
			}
			return this._baseUrl + _MULTI_NO_CHECKED_IMAGE;
		};
		
		treeObj.refreshStates = multiRefreshStates; // 根据特定的节点刷新所有节点的选择状态				
		treeObj.getSelectedTreeNode = multiGetSelectedTreeNode; // 获取选中节点TreeNode对象数组				
		treeObj.getSelectedXmlNode  = multiGetSelectedXmlNode; // 获取选中节点Xml对象数组
	} 
	else {
		treeObj.setAttribute(_TREE_TREE_TYPE, _TREE_TYPE_SINGLE);
		treeObj.setAttribute(_TREE_SELECTED_AND_ACTIVE, getValue(_TREE_SELECTED_AND_ACTIVE, "true"));
		
		/*
		 * 获取节点的下一选中状态（单选）
		 */
		treeObj.getNextState = function() {
			return 1;
		};
		
		/*
		 * 根据节点选择状态，获取图标地址（单选树）
		 */
		treeObj.getCheckTypeImageSrc = function(node) {
			var checkType   = node.getAttribute(_TREE_NODE_CHECKTYPE);
			var canSelected = node.getAttribute(_TREE_NODE_CANSELECTED);
			if(canSelected == 0) {
				return this._baseUrl + _RADIO_CAN_NOT_SELECT_IMAGE;
			}
			if(checkType == 1) {
				return this._baseUrl + _SINGLE_SELECTED_IMAGE;
			}
			return this._baseUrl + _SINGLE_NO_SELECTED_IMAGE;
		};

		/*
		 * 清除特定节点以外的其他节点的选中状态
		 */
		treeObj.refreshStates = function(treeNode) {
			var childNodes = this.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']");
			for(var i = 0, len = childNodes.length; i < len; i++) {
				if(childNodes[i] == treeNode.getXmlNode()) {
					continue;
				}
				setNodeState(childNodes[i], "0");
			}
		};
		
		/*
		 * 获取选中节点的TreeNode对象（单选树）
		 */
		treeObj.getSelectedTreeNode = function() {
			var node = this.getXmlRoot().selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']");
			return instanceTreeNode(node);
		};
		
		/*
		 * 获取选中节点的Xml对象（单选树）
		 */
		treeObj.getSelectedXmlNode = function() {
			return this.getXmlRoot().selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']");
		};
	}
	
	treeObj.isMenu = function() {
		return type == _TREE_TYPE_MENU;
	}
}

/*
 * 获取选中节点的Xml对象数组（多选树）
 * 参数：	hasHalfChecked	是否包含半选节点
 * 返回值：	Xml对象数组
 */
function multiGetSelectedXmlNode(hasHalfChecked) {	
	var xmlNodes;
	if(hasHalfChecked) { // 包括半选状态
		xmlNodes = this.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1' or @" + _TREE_NODE_CHECKTYPE + "='2']");
	} else {	// 不包括半选状态
		xmlNodes = this.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']");
	}
	
	var xmlNodeArray = new Array();
	for(var i = 0; i < xmlNodes.length; i++) {
		xmlNodeArray[i] = xmlNodes[i];
	}
	try{
		xmlNodeArray.rootNode = this.getXmlRoot().cloneNode(false);	//获取actionSet节点
	} catch(e) {
		alert("控件数据错误，xml不能解析！");
		throw(e);
	}
	xmlNodeArray.hasHalfChecked = hasHalfChecked;
	
	//给数组提供toElement方法，根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
	xmlNodeArray.toElement = function() {
		for(var i = 0, len = this.length; i < len; i++) {
			if(this[i] == null) {
				continue;
			}
			var parentNode = null;
			if(this.hasHalfChecked) {	// 包括半选状态，则以原有节点层次关系返回xml
				parentNode = this.rootNode.selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + this[i].parentNode.getAttribute(_TREE_NODE_ID) + "']");
			}
			else {
				parentNode = this.rootNode;
			}
			parentNode.appendChild(this[i].cloneNode(false));
		}
		return this.rootNode;
	};
	return xmlNodeArray;
}

/*
 * 获取选中节点的TreeNode对象数组（多选树）
 * 参数：	hasHalfChecked	是否包含半选节点
 * 返回值：	TreeNode对象数组
 */
function multiGetSelectedTreeNode(hasHalfChecked) {	
	var treeNodes;
	if(hasHalfChecked) {	// 包括半选状态
		treeNodes = this.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1' or @" + _TREE_NODE_CHECKTYPE + "='2']");
	} else {	//不包括半选状态
		treeNodes = this.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']");
	}
	
	//生成返回的对象数组
	var treeNodeArray = new Array();	
	for(var i = 0, len = treeNodes.length; i < len; i++) {
		treeNodeArray[i] = instanceTreeNode(treeNodes[i]);
	}
	
	try{
		treeNodeArray.rootNode = this.getXmlRoot().cloneNode(false);	//获取actionSet节点
	}catch(e) {
		alert("控件数据错误，xml不能解析！");
		throw(e);
	}
	treeNodeArray.hasHalfChecked = hasHalfChecked;
	
	// 给数组提供toElement方法，根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
	treeNodeArray.toElement = function() {
		for(var i = 0; i < this.length; i++) {
			if(this[i] == null) continue;
			
			var xmlNode = this[i].getXmlNode();
			var parentNode;
			if( this.hasHalfChecked ) {	// 包括半选状态，则以原有节点层次关系返回xml
				parentNode = this.rootNode.selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + xmlNode.parentNode.getAttribute(_TREE_NODE_ID) + "']");
			}
			else {
				parentNode = this.rootNode;
			}
			parentNode.appendChild( xmlNode.cloneNode(false) );
		}
		return this.rootNode;
	};
	return treeNodeArray;
}
 
 
 
/*
 * 对象名称：Tree	
 */
function Tree() {
	
	var _baseUrl = getValue(_TREE_BASE_URL, _TREE_BASE_URL_DEFAULT_VALUE);
	var _treeType = getValue(_TREE_TREE_TYPE, _TREE_TYPE_SINGLE);
	var _src = getValue(_TREE_SRC);
	var _selectedSrc = getValue(_TREE_SELECTED_SRC);
	var _selectedIds = getValue(_TREE_SELECTED_IDS);
	var _canMoveNode = getValue(_TREE_CAN_MOVE_NODE, "false");
	var _treeNodeSelectedChangeState = "false";
	var _treeNodeClickOpenNode = getValue(_TREE_OPEN_WITH_CLICK, "false");
	var _allCheckTypeDisabled = getValue(_TREE_DISABLED_ALL_CHECKTYPE, "false");
	var _justSelectSelf = 	getValue(_TREE_JUST_SELECT_SELF, "false");
	var _focusNewNode = getValue(_TREE_FOCUS_NEW_TREE_NODE, "true");
	var _defaultOpen = getValue(_TREE_DEFAULT_OPEN, "true");
	var _defaultActive = getValue(_TREE_DEFAULT_ACTIVE, "none");
	var _activedNode = null;
	var _movedNode = null;
	var _xmlRoot = null;
	var _scrollTimer = null;
	var _findedNode = null;
	
	/*
	 * 设定控件的数据，数据来源为xml节点、数据岛、数据文件或xml字符串
	 */
	this.loadData = function (dataSrc) {
		if(isNullOrEmpty(dataSrc)) {
			dataSrc = _src;
		}
		var ds = new DataSource(dataSrc);
		_xmlRoot = ds.xmlRoot;
		if(_defaultOpen == "true") {
			openNode(getDefaultOpenedNode(_xmlRoot));
		}
	}

	/*
	 * 获取默认选择状态数据：xml节点、数据岛、数据文件或xml字符串
	 * 参数：	selectedSrc	节点选中状态的数据源
	 *			isClearOldSelected	是否清除原先选中节点
	 */
	this.loadSelectedData = function (selectedSrc, isClearOldSelected) {
		if(_xmlRoot == null) {
			return;
		}
		if(isNullOrEmpty(selectedSrc)) {
			selectedSrc = _selectedSrc;
		}
		var ds = new DataSource(selectedSrc);
		if(treeObj.getAttribute(_TREE_TREE_TYPE) == _TREE_TYPE_SINGLE) {	//单选树
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
	this.loadSelectedDataByIds = function (selectedIds, isClearOldSelected, isDependParent) {
		if(_xmlRoot == null) {
			return;
		}
		if(isNullOrEmpty(selectedIds)) {
			selectedIds = _selectedIds;
		}
		if(treeObj.getAttribute(_TREE_TREE_TYPE) == _TREE_TYPE_SINGLE) {	//单选树
			if(selectedIds == null) {
				return;
			}
			eval("var selectedIds = '" + selectedIds + "';");
			var node = _xmlRoot.selectSingleNode("//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='"+selectedIds+"']");
			var treeNode = instanceTreeNode(node);
			if(treeNode != null) {
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
		if(isNullOrEmpty(type)) {
			type = _defaultActive;
		}
		if(_xmlRoot == null || type == "none") {
			return;
		}
		var activeNode = null;
		if(type == "root") {
			activeNode = _xmlRoot.selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + _TREE_ROOT_TREE_NODE_ID + "']");
		}else if(type == "valid") {
			activeNode = _xmlRoot.selectSingleNode(".//" + _TREE_NODE_NAME + "[(@" + _TREE_NODE_CANSELECTED + "!='0' or not(@" + _TREE_NODE_CANSELECTED + ")) and @" + _TREE_NODE_ID + "!='" + _TREE_ROOT_TREE_NODE_ID + "']");
		}
		var treeNode = instanceTreeNode(activeNode);
		if(treeNode != null) {
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
		if(_xmlRoot == null) {
			var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
			xmlDom.async = false;
			if (xmlDom.loadXML("<actionSet/>")) {	
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
	this.isSelectByActived = function () {
		return _treeNodeSelectedChangeState == "true";
	}

	/*
	 * 根据属性配置，点击节点文字标签时是否改变节点伸缩状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-27
	 */
	this.isChangeFolderStateByClickLabel = function () {
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
	        case _TREE_BASE_URL:
				_baseUrl = value;
	            break;
	        case _TREE_TREE_TYPE:
				_treeType = value;
	            break;
	        case _TREE_SRC:
				_src = value;
	            break;
	        case _TREE_CAN_MOVE_NODE:
				_canMoveNode = value;
	            break;
	        case _TREE_SELECTED_AND_ACTIVE:
				_treeNodeSelectedChangeState = value;
	            break;
	        case _TREE_OPEN_WITH_CLICK:
				_treeNodeClickOpenNode = value;
	            break;
	        case _TREE_DISABLED_ALL_CHECKTYPE:
				_allCheckTypeDisabled = value;
	            break;
	        case _TREE_JUST_SELECT_SELF:
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
	        case _TREE_BASE_URL:
				return _baseUrl;
	        case _TREE_TREE_TYPE:
				return _treeType;
	        case _TREE_SRC:
				return _src;
	        case _TREE_CAN_MOVE_NODE:
				return _canMoveNode;
	        case _TREE_SELECTED_AND_ACTIVE:
				return _treeNodeSelectedChangeState;
	        case _TREE_OPEN_WITH_CLICK:
				return _treeNodeClickOpenNode;
	        case _TREE_DISABLED_ALL_CHECKTYPE:
				return _allCheckTypeDisabled;
	        case _TREE_JUST_SELECT_SELF:
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
		if(this.isMovedNode(node)) {
			return _TREE_NODE_MOVED_STYLE;
		}else if(this.isActiveNode(node)) {
			return _TREE_NODE_SELECTED_STYLE;
		}else if(this.isFindedNode(node)) {
			return _TREE_NODE_FINDED_STYLE;
		}
		if(isNullOrEmpty(defaultClassName)) {
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
	this.disable = function () {
		_allCheckTypeDisabled = "true";
	}
	/*
	 * 允许没有被特殊指定不能选中的节点改变选中状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-29
	 */
	this.enable = function () {
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
	this.isFocusNewTreeNode = function() {
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
	this.test = function(name) {
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
	if(xmlRoot == null) {
		return;
	}
	var actionSetNode = xmlRoot;
	var openedNodeId = actionSetNode.getAttribute(_DEFAULT_OPENED_TREE_NODE_ID);
	var openedNode = null;
	if(!isNullOrEmpty(openedNodeId)) {
		openedNode = actionSetNode.selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='"+openedNodeId+"']");
	}
	if(isNullOrEmpty(openedNode)) {
		openedNode = actionSetNode.selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CANSELECTED + "!='0' or not(@" + _TREE_NODE_CANSELECTED + ")]");
	}
	return isNullOrEmpty(openedNode)?actionSetNode.firstChild:openedNode;
}
/*
 * 获取控件参数
 * 参数：	name	参数名
 *			defaultValue	默认值
 * 返回值：	String	属性值
 */
function getValue(name, defaultValue) {
	var value = eval("element." + name) || defaultValue;
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
	if(node == null) {
		return;
	}
	if(isClearOldSelected) {
		clearSelected(node);
	}
	if(defaultCheckedNode == null) {
		return;
	}
	if (treeObj.getAttribute(_TREE_JUST_SELECT_SELF) == "true") {
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
	if(node == null) {
		return;
	}
	if(isClearOldSelected) {
		clearSelected(node);
	}
	if(defaltCheckedIds == null || defaltCheckedIds == "") {
		return;
	}
	if (treeObj.getAttribute(_TREE_JUST_SELECT_SELF) == "true") {
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
function clearSelected(node) {
	var nodes = node.selectNodes(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1' or @" + _TREE_NODE_CHECKTYPE + "='2']");
	for(var i = 0; i < nodes.length; i++) {
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
	if(defaultCheckedNode == null || node == null) {
		return;
	}
	var checkedNode = defaultCheckedNode.selectSingleNode("//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']");
	if(checkedNode == null) {
		return;
	}
	var fNodeId = checkedNode.getAttribute(_TREE_NODE_ID);
	var xpath = "//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + fNodeId + "']";
	var fNode = node.selectSingleNode(xpath);
	var treeNode = instanceTreeNode(fNode);
	if(treeNode != null) {
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
	if(node == null || defaultCheckedNode == null) {
		return;
	}
	var checkedNodes = defaultCheckedNode.selectNodes("//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']");
	for(var i = 0; i < checkedNodes.length; i++) {
		var fNodeId = checkedNodes[i].getAttribute(_TREE_NODE_ID);
		var xpath = "//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + fNodeId + "']";
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
	if(node == null || defaultCheckedNode == null) {
		return;
	}
	var checkedNodes = defaultCheckedNode.selectNodes("//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1' && ..[not(@" + _TREE_NODE_CHECKTYPE + "='1') || not(@" + _TREE_NODE_CHECKTYPE + ")]]");
	for(var i = 0; i < checkedNodes.length; i++) {
		var fNodeId = checkedNodes[i].getAttribute(_TREE_NODE_ID);
		var xpath = "//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + fNodeId + "']";
		var fNode = node.selectSingleNode(xpath);
		if(fNode != null) {
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
	if(node == null || defaltCheckedIds == null) {
		return;
	}
	var checkedNodeIds = defaltCheckedIds.split(',');
	for(var i = 0; i < checkedNodeIds.length; i++) {
		var xpath = "//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + checkedNodeIds[i] + "']";
		var fNode = node.selectSingleNode(xpath);
		if(fNode != null) {
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
	if(node == null || defaltCheckedIds == null) {
		return;
	}
	var checkedNodeIds = defaltCheckedIds.split(',');
	for(var i = 0; i < checkedNodeIds.length; i++) {
		var xpath = "//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + checkedNodeIds[i] + "']";
		var fNode = node.selectSingleNode(xpath);
		if(fNode != null) {
			setNodeState(fNode, 1);

            //如果下溯，则设置全部子节点为选中
            if(true==isDependParent) {
                var xpath = ".//" + _TREE_NODE_NAME;
                var subnodes = fNode.selectNodes(xpath);
                for(var j = 0; j < subnodes.length; j++) {
                    setNodeState(subnodes[j], 1);
                }
            
            }
		}
	}
	//标记包含选中节点的所有父节点为选中
	var xpath = "//" + _TREE_NODE_NAME + "[not(@" + _TREE_NODE_CHECKTYPE + " and @" + _TREE_NODE_CHECKTYPE + " = '1') and .//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + " = '1']]";
	var nodes = node.selectNodes(xpath);
	for(var i = 0; i < nodes.length; i++) {
		setNodeState(nodes[i], 1);
	}
	//标记所有全选节点中包含未全选节点的节点为半选
	var nodes = node.selectNodes("//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + " = '1' and .//" + _TREE_NODE_NAME + "[not(@" + _TREE_NODE_CHECKTYPE + " = '1') || not(@" + _TREE_NODE_CHECKTYPE + ")]]");
	for(var i = 0; i < nodes.length; i++) {
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
	if (treeObj.getAttribute(_TREE_JUST_SELECT_SELF) == "true") {
		return;
	}
	refreshParentNodeState(treeNode.getXmlNode());

	if(noChildren && treeNode.getSelectedState() == 2) {
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
	while(tempNode != treeObj.getXmlRoot()) {
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
	var nodeCheckedChildNum = node.selectNodes("./" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']").length;	//全选子节点数
	var nodeHalfCheckedChildNum = node.selectNodes("./" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='2']").length;	//半选子节点数
	if(nodeCheckedChildNum == 0 && nodeHalfCheckedChildNum == 0) {	
		return 0;		//所有子节点都没有选中，标记未选中
	}else if(nodeChildNum == nodeCheckedChildNum) {
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
	var childNodes = node.selectNodes(".//" + _TREE_NODE_NAME);
	for(var i = 0, len = childNodes.length; i < len; i++) {
		setNodeState(childNodes[i], node.getAttribute(_TREE_NODE_CHECKTYPE));
	}
}


 
//////////////////////////////////////////////////////////////////////////////
//		                       公用函数	   	                                //
//////////////////////////////////////////////////////////////////////////////


/*
 * 判断节点是否为父节点的最后一个节点
 * 参数：node	xml节点对象
 * 返回值：true/false
 */
function isLastChild(node) {
	return node == node.parentNode.lastChild;
}

/*
 * 打开默认打开节点
 * 参数：	openedNode	xml对象中需要打开的节点
 */
function openNode(openedNode) {
	while( openedNode ) {
		openedNode.setAttribute("_open", "true");
		if(openedNode == treeObj.getXmlRoot()) {
			return;
		}
		openedNode = openedNode.parentNode;
	}
}

/*
 * 设定节点的选择状态。
 * 参数：	node			节点的xml对象
 *			state			选择状态
 */
function setNodeState(node, state) {
	if(node == null) return;

	if( state ) {
		node.setAttribute(_TREE_NODE_CHECKTYPE, state);	//在xml节点中标记选择状态
	} else { 
		node.removeAttribute(_TREE_NODE_CHECKTYPE);
	}
}
 
/*
 * 获取对象在树控件可视区域中的位置（对象上边缘距可视区域上边缘的距离）, 获取对象相对于控件顶部的距离。
 * 参数：	objElement	对象
 * 返回：	int
 */
function getTop(objElement) {
	var top = 0;
	var obj = objElement;
	while (obj != element) {
		top = top + obj.offsetTop;
		obj = obj.offsetParent;
	}
	return top;
}

/*
 * 根据显示的对象，获取相应的Row对象
 * 参数：	obj	节点显示在页面上的对象
 * 返回值：	Row对象
 */
function getRow(obj) {
	if(!/^(a|img)$/.test(obj.tagName.toLowerCase())) {
		return null;
	}
	 
	try{
		var index = getRowIndex(obj);
		return displayObj.getRowByIndex(index);
	} catch(e) {		
	}	
}

/*
 * 如果拖到页面的最上、下方，相应的滚动树
 * 参数：	obj	事件触发对象
 */
function startScrollTree(obj) {
    if(obj == null) return;
	
	if(isLastLine(obj)) {
		scrollDown();
	}
	if(isFirstLine(obj)) {
		scrollUp();
	}
}

/*
 * 定时向上滚动
 */
function scrollUp() {
	if(element.scroller) {
		clearTimeout(element.scroller);
		element.scroller = null;
	}
	displayObj.scrollUp();
	element.scroller = setTimeout(scrollUp, _TREE_SCROLL_REPEAT_DELAY_TIME);
}

/*
 * 定时向下滚动
 */
function scrollDown() {
	if(element.scroller ) {
		clearTimeout(element.scroller);
		element.scroller=null;
	}
	displayObj.scrollDown();
	element.scroller = setTimeout(scrollDown, _TREE_SCROLL_REPEAT_DELAY_TIME);
}

/*
 * 如果拖到的不是页面的最上、下方，或者停止拖动，则停止滚动树
 * 参数：	obj	事件触发对象
 */
function stopScrollTree(obj) {
	if(obj && (isLastLine(obj) || isFirstLine(obj))) {
		return;
	}
	
	if (element.scroller) {
		window.clearTimeout(element.scroller);
		element.scroller = null;
	}
}

/*
 * 对象是否在最下面的行中
 * 参数：	odj	显示的对象
 */
function isLastLine(obj) {
	return getRowIndex(obj) == (displayObj.getPageSize() - 1);
}

/*
 * 对象是否在最上面的行中
 * 参数：	odj	显示的对象
 */
function isFirstLine(obj) {
    return getRowIndex(obj) == 0;
}

/*
 * 获取对象所在行序号
 * 参数：	obj	对象
 * 返回值：	行序号
 */
function getRowIndex(obj) {
    while(obj.tagName != null && obj.tagName.toLowerCase() != "tr") {
		obj = obj.parentNode;
	}
	return obj.rowIndex;
}

	


//////////////////////////////////////////////////////////////////
//	对象名称：Row	 												//
//	职责：	负责页面上tr对象中显示节点。							//
//			只要给定一个xml节点，此对象负责将节点显示到对应的tr中。		//
//////////////////////////////////////////////////////////////////

/*
 * 初始化Row对象
 * 参数：	tr	tr的Dom对象
 * 返回值：
 * 作者：scq
 * 时间：2004-6-23
 */
function instanceRow(tr) {
	return new Row(tr);
}
/*
 * 对象说明：封装树节点显示在屏幕上的一个tr对象
 * 参数：	tr	tr的Dom对象
 * 返回值：
 * 作者：scq
 * 时间：2004-6-23
 */
function Row(tr) {

	this.row = tr;
	this.nobr = null;
	this.line = null;
	this.folder = null;
	this.icon = null;
	this.checkType = null;
	this.label = null;
	
	this.node = null;

	this.frontStr = null;
	this.checkTypeSrc = null;
	this.folderSrc = null;
	this.iconSrc = null;
	this.name = null;
	this.fullName = null;
	this.className = null;
	this.disabled = false;
}

Row.prototype = new function () {
	/*
	 * 重新设定相关xml节点
	 * 参数：	node	树节点的xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setXmlNode = function (node) {
		if(this.nobr == null) {
			this.setInnerObj();
		}
		if(node == null) {
			this.setClassName();
			this.nobr.removeNode(true);
			this.nobr = null;
			this.line = null;
			this.folder = null;
			this.icon = null;
			this.checkType = null;
			this.label = null;

			this.node = null;

			this.frontStr = null;
			this.checkTypeSrc = null;
			this.folderSrc = null;
			this.iconSrc = null;
			this.name = null;
			this.fullName = null;
			this.className = null;
			this.disabled = false;

			return;
		}
	    this.setLine(getFrontStr(this.row, node, treeObj.getXmlRoot()));
		this.setFolder(node);
		if(!treeObj.isMenu()) {
			this.setCheckType(treeObj.getCheckTypeImageSrc(node));
		}

		//2006-4-6 加入自定义图标
		this.setIcon(node);

		this.setLabel(node);
		this.node = node;
	}
	/*
	 * 获取显示节点的xml对象
	 * 参数：	
	 * 返回值：	xml节点
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getXmlNode = function () {
	    return this.node;
	}
	/*
	 * 获取页面显示的文字链接对象
	 * 参数：
	 * 返回值：	a对象
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getLabel = function () {
	    return this.label;
	}
	/*
	 * 获取页面显示的选择状态对象
	 * 参数：
	 * 返回值：	img对象
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getCheckType = function () {
	    return this.checkType;
	}
	/*
	 * 获取页面显示的伸缩状态对象
	 * 参数：
	 * 返回值：	img对象
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getFolder = function () {
	    return this.folder;
	}
	/*
	 * 初始化参数（获取指向行内个对象的链接）
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-28
	 */
	this.setInnerObj = function () {
	    try{
			this.nobr = this.row.cells[0].firstChild;
			this.line = this.nobr.firstChild;
			this.folder = this.line.nextSibling;

			if(!treeObj.isMenu()) {
				this.checkType = this.folder.nextSibling;
				//2006-4-6 增加自定义图标
				this.icon = this.checkType.nextSibling;
				this.label = this.icon.nextSibling;
			}else{
				//2006-4-6 增加自定义图标
				this.icon = this.folder.nextSibling;
				this.label = this.icon.nextSibling;
			}
		}catch(e) {
			this.nobr = createObjByTagName("nobr");
			this.row.cells[0].appendChild(this.nobr);
			this.line = this.nobr.appendChild(createObjByTagName("span"));
			this.folder = this.nobr.appendChild(createObjByTagName("img", _TREE_NODE_FOLDER_STYLE));
			if(!treeObj.isMenu()) {
				this.checkType = this.nobr.appendChild(createObjByTagName("img", _TREE_NODE_CHECK_TYPE_STYLE));
			}
			//2006-4-6 增加自定义图标
			this.icon = this.nobr.appendChild(createObjByTagName("img", _TREE_NODE_ICON_STYLE));
			this.label = this.nobr.appendChild(createObjByTagName("a"));
		}
	}
	/*
	 * 	设置制表符
	 * 参数：	htmlStr	制表符的HTML代码
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setLine = function(htmlStr) {
		if(this.frontStr == htmlStr) {
			return;
		}
		this.line.innerHTML = htmlStr;
		this.frontStr = htmlStr;
	}
	/*
	 * 设置伸缩图标
	 * 参数：	node	树节点的xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setFolder = function(node) {
		//2006-4-22 区分第一层树节点
		if(null!=node.parentNode && treeObj.getXmlRoot()==node.parentNode) {//是第一层树节点
			if(node.hasChildNodes() || node.getAttribute("hasChild")=="1") {
				if(node.getAttribute("_open") == "true") {
					this.setFolderSrc(treeObj.getAttribute(_TREE_BASE_URL) + _TREE_ROOT_NODE_CONTRACT_IMAGE);
				}else{
					this.setFolderSrc(treeObj.getAttribute(_TREE_BASE_URL) + _TREE_ROOT_NODE_EXPAND_IMAGE);
				}
			}else{
				this.setFolderSrc(treeObj.getAttribute(_TREE_BASE_URL) + _TREE_ROOT_NODE_LEAF_IMAGE);
			}
		}else{
			if(node.hasChildNodes() || node.getAttribute("hasChild")=="1") {
				if(node.getAttribute("_open") == "true") {
					this.setFolderSrc(treeObj.getAttribute(_TREE_BASE_URL) + _TREE_NODE_CONTRACT_IMAGE);
				}else{
					this.setFolderSrc(treeObj.getAttribute(_TREE_BASE_URL) + _TREE_NODE_EXPAND_IMAGE);
				}
			}else{
				this.setFolderSrc(treeObj.getAttribute(_TREE_BASE_URL) + _TREE_NODE_LEAF_IMAGE);
			}		
		}
	}
	/*
	 * 设定伸缩图标的地址
	 * 参数：	src	图标的地址
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setFolderSrc = function (src) {
	    if(this.folderSrc == src) {
			return;
		}
		this.folder.src = src;
		this.folderSrc = src;
	}
	/*
	 * 设定选择状态图标
	 * 参数：	imgSrc	图标地址
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setCheckType = function(imgSrc) {
		if(this.checkTypeSrc == imgSrc) {
			return;
		}
		this.checkType.src = imgSrc;
		this.checkTypeSrc = imgSrc;
	}
	/*
	 * 设定文字链接
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setLabel = function(node) {
		var name = node.getAttribute(_TREE_NODE_NAME);
		var fullName = node.getAttribute(_TREE_NODE_FULLNAME);
		var canSelected = node.getAttribute(_TREE_NODE_CANSELECTED);
		var display = node.getAttribute(_TREE_NODE_DISPLAY);
		this.setName(name);
		if(fullName == null) {
			fullName = name;
		}
		this.setTitle(fullName);
		this.setClassName(treeObj.getClassName(node));
		this.setAbled(canSelected, display);
	}
	/*
	 * 设定文字链接的文本内容
	 * 参数：	name	文字内容
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setName = function (name) {
		if(this.name == name) {
			return;
		}
		this.label.innerText = name;
		this.name = name;
	}
	/*
	 * 设定文字链接的提示信息
	 * 参数：	fullName	提示信息
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setTitle = function (fullName) {
		if(this.fullName == fullName) {
			return;
		}
	    this.label.title = fullName;
		this.fullName = fullName;
	}
	/*
	 * 设定文字链接的样式
	 * 参数：	className	节点文字链接样式名
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setClassName = function (className) {		
		if(isNullOrEmpty(className)) {
			this.row.className = "";
			this.label.removeAttribute("className");
		}else if(this.className == className) {
			return;
		}else{
			this.row.className = className;
			this.label.className = className;
		}
		this.className = className;
	}
	/*
	 * 设定文字链接是否可用
	 * 参数：	canSelected	是否可以选择	0/1
	 *			display	是否正常显示	0/1
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setAbled = function (canSelected, display) {
		if(display == '0' || canSelected == '0') {
			if(this.disabled) {
				return;
			}
			this.label.setAttribute("disabled", true);
			this.disabled = true;
		}else{
			if(!this.disabled) {
				return;
			}
			this.label.setAttribute("disabled", false);
			this.disabled = false;
		}
	}
	/*
	 * 设置自定义图标
	 * 参数：	node	树节点的xml节点
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-6
	 */
	this.setIcon = function(node) {
		var iconSrc = node.getAttribute(_TREE_NODE_ICON_ATTRIBUTE);
		if(null!=iconSrc && ""!=iconSrc) {
			this.setIconSrc(iconSrc);
			this.icon.width = _TREE_NODE_ICON_WIDTH;
			this.icon.height = _TREE_NODE_ICON_HEIGHT;
			this.icon.style.display = "";
		}else{
			this.icon.style.display = "none";
		}
	}
	/*
	 * 获取页面显示的图标对象
	 * 参数：
	 * 返回值：	img对象
	 * 作者：毛云
	 * 时间：2007-5-28
	 */
	this.getIcon = function () {
	    return this.icon;
	}
	/*
	 * 设定自定义图标的地址
	 * 参数：	src	图标的地址
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-6
	 */
	this.setIconSrc = function (src) {
	    if(this.iconSrc == src) {
			return;
		}
		this.icon.src = src;
		this.iconSrc = src;
	}

	/*
	 * 获取节点前面的制表符字符串
	 * 参数：	node	节点
	 *			rootNode	根节点
	 * 返回值：	string	制表符字符串
	 * 作者：
	 * 时间：2004-6-7
	 */
	function getFrontStr(row, node, rootNode) {
		if(node.parentNode == rootNode) {
			node.setAttribute("_childFrontStr", '');
			return '<span class="rootSpace"></span>';
		}
		var parentFrontStr = getParentFrontStr(row, node, rootNode);
		if(isLastChild(node)) {
			node.setAttribute("_childFrontStr", parentFrontStr + '<span class="space"></span>');
			return parentFrontStr + '<span class="vHalfLine"></span>';
		}else{
			node.setAttribute("_childFrontStr", parentFrontStr + '<span class="onlyVLine"></span>');
			return parentFrontStr + '<span class="vline"></span>';
		}
	}
	/*
	 * 获取父节点前面的制表符字符串
	 * 参数：	node	节点
	 *			rootNode	根节点
	 * 返回值：	string	制表符字符串
	 * 作者：
	 * 时间：2004-7-5
	 */
	function getParentFrontStr(row, node, rootNode) {
		if(isFirstLine(row) || node.parentNode.getAttribute("_childFrontStr") == null) {
			getFrontStr(row, node.parentNode, rootNode);
		}
		return node.parentNode.getAttribute("_childFrontStr");
	}
	/*
	 * 创建页面显示的元素
	 * 参数：	name	对象标记名(小写)
	 *			className	样式类型名
	 * 返回值：页面元素对象
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	function createObjByTagName(name, className) {
   		var obj = window.document.createElement(name);
		if(className != null) {
			obj.setAttribute("className", className);
		}
		if(name == "a") {
			obj.setAttribute("hideFocus", "1");
			obj.setAttribute("href", "");
		}
		return obj;
	}
	this.test = function(name) {
		return eval(name);
	}
}




//////////////////////////////////////////////////////////////////////////
//	对象名称：Display														//
//	职责：	负责处理将用户可视部分的节点显示到页面上。						//
//			控件一切页面上的元素都有此对象生成和调度（tr对象有Row对象专门处理）。//
//////////////////////////////////////////////////////////////////////////

/*
 * 初始化Display对象
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-25
 */
function instanceDisplay() {
	displayObj = new Display();
	displayObj.init();
}
/*
 * 分层显示对象，控制页面显示范围，将应该展示给用户的节点展示在界面上。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-7
 */
function Display() {
	var _windowHeight = Math.max(element.offsetHeight - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_HEIGHT);
	var _windowWidth = Math.max(element.offsetWidth - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_WIDTH);
	var _rowHeight = _TREE_NODE_DISPLAY_ROW_HEIGHT;
	var _pageSize = Math.floor(_windowHeight / _rowHeight);
	var _totalTreeNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[../@_open = 'true']");
	var _totalTreeNodesNum = _totalTreeNodes.length;
	var _vScrollBox = null;
	var _vScrollDiv = null;
	var _hScrollBox = null;
	var _hScrollDiv = null;
	var _rootBox = null;
	var _rootTable = null;
	var _scrollTimer = null;
	var _startNum = null;
	var _Rows = new Array(_pageSize);
	element.style.overflow = 'hidden';
	element.onresize = resize;
	element.onmousewheel = function() {
		_vScrollBox.scrollTop += -Math.round(window.event.wheelDelta/120)*_rowHeight;
	}
	element.onkeydown = function() {
		switch (event.keyCode) {
		    case 33:	//PageUp
				_vScrollBox.scrollTop -= _pageSize * _rowHeight;
				return false;
		    case 34:	//PageDown
				_vScrollBox.scrollTop += _pageSize * _rowHeight;
				return false;
		    case 35:	//End
				_vScrollBox.scrollTop = _vScrollDiv.offsetHeight - _windowHeight;
				return false;
		    case 36:	//Home
				_vScrollBox.scrollTop = 0;
				return false;
		    case 37:	//Left
				_hScrollBox.scrollLeft -= 10;
				return false;
		    case 38:	//Up
				_vScrollBox.scrollTop -= _rowHeight;
				return false;
		    case 39:	//Right
				_hScrollBox.scrollLeft += 10;
				return false;
		    case 40:	//Down
				_vScrollBox.scrollTop += _rowHeight;
				return false;
		}
	}
	/*
	 * 方法说明：生成默认展示的树节点。
	 * 参数：	
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-7
	 */
	this.init = function() {
		element.innerHTML = "";
		createScrollElement();
		createTableElement();
		setScrollElementSize();
		setTableElementSize();
//		this.reload();
	}
	
	/*
	 * 生成滚动条
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function createScrollElement() {
		var vScrollStr = '<div id="treeVScrollBox" style="position:absolute;overflow-y:auto;heigth:100%;width:17px;top:0px;right:0px;"><div id="treeVScrollDiv" style="width:1px"></div></div>';
		var hScrollStr = '<div id="treeHScrollBox" style="position:absolute;overflow-x:auto;overflow-y:hidden;heigth:17px;width:100%;bottom:0px;left:0px"><div id="treeHScrollDiv" style="higth:1px"></div></div>';
		element.insertAdjacentHTML('afterBegin', vScrollStr + hScrollStr);
		_vScrollBox = element.all("treeVScrollBox");
		_vScrollDiv = element.all("treeVScrollDiv");
		_hScrollBox = element.all("treeHScrollBox");
		_hScrollDiv = element.all("treeHScrollDiv");
		_vScrollBox.onscroll = onVScroll;
		_hScrollBox.onscroll = onHScroll;
	}
	/*
	 * 设置滚动条的大小
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-9
	 */
	function setScrollElementSize() {
		_vScrollBox.style.height = _windowHeight;

		_vScrollDiv.style.height = (_totalTreeNodesNum - _pageSize) * _rowHeight + _windowHeight;

		_hScrollBox.style.width = _windowWidth;

		_hScrollDiv.style.width = _rootTable.style.width; 
	}
	/*
	 * 生成页面上显示节点的table对象。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-7
	 */
	function createTableElement() {
		var tableStr = '<div id="treeRootBox" style="position:absolute;overflow:hidden;top:0px;left:0px"><table id="treeRootTable" cellspacing="0"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_rootBox = element.all("treeRootBox");
		_rootTable = element.all("treeRootTable");
		for(var i = 0; i < _pageSize; i++) {
			var tr = _rootTable.insertRow();
			tr.insertCell();
			_Rows[i] = new Row(tr);
		}
	}
	/*
	 * 设置显示节点的table对象的大小
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-9
	 */
	function setTableElementSize() {
		_rootBox.style.height = _windowHeight;
		_rootBox.style.width = _windowWidth;
	}
	/*
	 * 方法说明：根据滚动状态，显示可视范围内的树节点。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-7
	 */
	this.reload = function refresh() {
		var st = new Date();
		if(_totalTreeNodesNum <= _pageSize) {
			_startNum = 0;
		}else{
			_startNum = Math.ceil(_vScrollBox.scrollTop  / _rowHeight);
		}
		//显示节点
		for(var i = 0; i < _pageSize; i++) {
			var nodeIndex = i + _startNum;
			if(nodeIndex < _totalTreeNodesNum) {
				_Rows[i].setXmlNode(_totalTreeNodes[nodeIndex]);
			}else{
				_Rows[i].setXmlNode();
			}
		}
		//同步横向滚动条的大小
		_hScrollDiv.style.width = _rootTable.offsetWidth;

		refreshUI();

		var et = new Date();
		window.status=et-st;
	}
	/*
	 * 根据页面上的行数，获取相应的Row对象
	 * 参数：	index	行序号
	 * 返回值：	Row对象/null
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getRowByIndex = function (index) {
		if(index >= _pageSize || index < 0) {
			alert("Display对象：行序号[" + index + "]超出允许范围[0 - " + _pageSize + "]！");
			return null;
		}
		return _Rows[index];
	}
	/*
	 * 重新获取所有可以显示的节点数组
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.resetTotalTreeNodes = function() {
		_totalTreeNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[../@_open = 'true']");
		_totalTreeNodesNum = _totalTreeNodes.length;

		_vScrollDiv.style.height = Math.max(1,(_totalTreeNodesNum - _pageSize) * _rowHeight + _windowHeight);
	}
	/*
	 * 获取页面table对象
	 * 参数：
	 * 返回值：	table对象
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.getRootTable = function() {
		return _rootTable;
	}
	/*
	 * 将节点滚动到可是范围之内
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.scrollTo = function(node) {
		var nodeIndex = null;
		for(var i = 0; i < _totalTreeNodesNum; i++) {
			if(_totalTreeNodes[i] == node) {
				nodeIndex = i;
				break;
			}
		}
		if(nodeIndex == null) {
			return;
		}
		var childNums = node.selectNodes(".//" + _TREE_NODE_NAME + "[../@_open = 'true']").length;
		if(childNums + 1 > _pageSize 
			|| nodeIndex < _startNum 
			|| nodeIndex >= _startNum + _pageSize) {
            _vScrollBox.style.display = 'block';
			_vScrollBox.scrollTop = nodeIndex * _rowHeight;
		}else if(nodeIndex + childNums + 1 - _pageSize > _startNum) {
            _vScrollBox.style.display = 'block';
			_vScrollBox.scrollTop = (nodeIndex + childNums + 1 - _pageSize) * _rowHeight;
		}else{
			this.reload();
		}
	}
	/*
	 * 向上滚动一个节点
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.scrollUp = function() {
		_vScrollBox.scrollTop -= _rowHeight;
	}
	/*
	 * 向下滚动一个节点
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.scrollDown = function() {
		_vScrollBox.scrollTop += _rowHeight;
	}
	/*
	 * 获取滚动条的位置
	 * 参数：
	 * 返回值：	int	象素值
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.getScrollTop = function() {
		return _vScrollBox.scrollTop;
	}
	/*
	 * 纵向滚动事件触发后，延时执行reload，如果第二次触发时，上次的事件还没有执行，
	 * 则取消上次事件，触发本次事件。为的是防止多次触发，屏幕抖动。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function onVScroll() {
 		if (_scrollTimer) {
			window.clearTimeout(_scrollTimer);
		}
		_scrollTimer = window.setTimeout(refresh, _TREE_SCROLL_DELAY_TIME);
	}
	/*
	 * 横向滚动事件
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function onHScroll() {
		_rootBox.scrollLeft = this.scrollLeft;
	}
	/*
	 * 当窗口大小改变后，初始化所有相关参数，并且重新计算所要显示的节点。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function resize() {
		//2005-9-8 增加延时，避免极短时间内重复触发多次
		clearTimeout(element._resizeTimeout);
		element._resizeTimeout = setTimeout(function() {

			var tempWindowHeight = Math.max(element.offsetHeight - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_HEIGHT);
			var tempWindowWidth = Math.max(element.offsetWidth - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_WIDTH);

			if(_windowHeight!=tempWindowHeight || _windowWidth!=tempWindowWidth) {
				_windowHeight = tempWindowHeight;
				_windowWidth = tempWindowWidth;
			}else{
				//触发前后尺寸无变化
				return ;
			}
				
			var pageSize = Math.floor(_windowHeight / _rowHeight);
			_vScrollBox.style.height = _windowHeight;
			_hScrollBox.style.width = _windowWidth;

			_rootBox.style.height = _windowHeight;
			_rootBox.style.width = _windowWidth;

			//2005-9-8 修正尺寸变化时行数显示错误问题
			if(pageSize > _pageSize) {//高度增加时

				_Rows = new Array(pageSize);
				for(var i = 0; i < pageSize; i++) {
					if(i < _pageSize) {
						var tr = _rootTable.rows[i];
					}else{
						var tr = _rootTable.insertRow();
						tr.insertCell();
					}
					_Rows[i] = new Row(tr);
				}
				_pageSize = pageSize;
				refresh();

			}else if(pageSize < _pageSize) {//高度减少时

				_Rows = new Array(pageSize);
				for(var i = 0; i < pageSize; i++) {
					var tr = _rootTable.rows[i];
					_Rows[i] = new Row(tr);
				}
				for(var i = pageSize; i < _pageSize; i++) {
					_rootTable.deleteRow(pageSize);
				}
				_pageSize = pageSize;
				refresh();

			}else{
				refreshUI();
			}
		},20);
	}
	/*
	 * 刷新页面展示：数据展示框、滚动条等
	 * 参数：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function refreshUI() {
		if(_totalTreeNodesNum > _pageSize) {
			_vScrollBox.style.display = 'block';
			_hScrollBox.style.width = _windowWidth;
			_rootBox.style.width = _windowWidth;
		}else{
			_vScrollBox.style.display = 'none';
			_hScrollBox.style.width = _windowWidth + _TREE_SCROLL_BAR_WIDTH;
			_rootBox.style.width = _windowWidth + _TREE_SCROLL_BAR_WIDTH;
		}
		if(_rootTable.offsetWidth > _windowWidth) {
			_hScrollBox.style.display = 'block';
			_vScrollBox.style.height = _windowHeight;
			_rootBox.style.height = _windowHeight;
		}else{
			_hScrollBox.style.display = 'none';
			_vScrollBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
			_rootBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
		}
	}
	/*
	 * 获取页面上所能展示的行数
	 * 参数：
	 * 返回值：int	行数
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.getPageSize = function () {
	    return _pageSize;
	}
	this.test = function(name) {
		return eval(name);
	}
}





//////////////////////////////////////////
//	对象名称：Search					//
//	职责：	查询树上节点。				//
//////////////////////////////////////////

/*
 * 初始化Search对象
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-2
 */
function instanceSearch() {
    searchObj = new Search();
}

/*
 * 对象说明：负责查询树节点对象的对象
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-14
 */
function Search() {
	var _findedNodes = new Array();
	var _findedIndex = null;
	var _findedNode = null;
	/*
	 * 查询得到所有符合要求的结果
	 * 参数：	searchStr	查询的字符串
	 *			searchBy	查询的属性名称
	 *			searchType	查询方式：hazy(模糊)/rigor(精确)，默认为rigor
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.search = function(searchStr, searchBy, searchType) {
		_findedNodes = new Array();
		if(isNullOrEmpty(searchStr)) {
			alert(_TREE_SEARCH_NO_CONDITION_VALUE_MSG);
			return false;
		}
		if(isNullOrEmpty(searchBy)) {
			alert(_TREE_SEARCH_NO_CONDITION_NAME_MSG);
			return false;
		}
		if(searchType == _TREE_SEARCH_TYPE_INEXACT_SEARCH) {
			var allNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME);
			for(var i = 0, len = allNodes.length; i < len; i++) {	//模糊查询所有节点
				if(allNodes[i].getAttribute(searchBy) != null && allNodes[i].getAttribute(searchBy).indexOf(searchStr) != -1) {
					_findedNodes[_findedNodes.length] = allNodes[i];
				}
			}
		}else{
			alert(".//" + _TREE_NODE_NAME + "[@" + searchBy + "='" + searchStr + "']");
			_findedNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_NODE_NAME + "[@" + searchBy + "='" + searchStr + "']");
		}
		_findedIndex = -1;
		return true;
	}
	/*
	 * 是否拥有查询结果
	 * 参数：
	 * 返回值：true/false
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.hasResult = function() {
		return _findedNodes.length > 0;
	}
	/*
	 * 获取查询得到的第一个结果
	 * 参数：
	 *			direct		查询方向：默认为向下查询
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.first = function (direct) {
		if(direct == "down") {
			_findedIndex = 0;
		}else{
			_findedIndex = _findedNodes.length - 1;
		}
		showFindedTreeNode(_findedIndex);
	}
	/*
	 * 获取查询结果的下一个结果
	 * 参数：
	 *			direct		查询方向：默认为向下查询
	 *			isCircle	是否循环查询，默认为不循环查询
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.next = function (direct, isCircle) {
		if(direct == "down") {
			_findedIndex += 1;
			if(_findedNodes.length <= _findedIndex) {
				if(isCircle) {
					_findedIndex = 0;
				}else{
					_findedIndex = _findedNodes.length - 1;
				}
			}
		}else{
			_findedIndex -= 1;
			if(_findedIndex < 0) {
				if(isCircle) {
					_findedIndex = _findedNodes.length - 1;
				}else{
					_findedIndex = 0;
				}
			}
		}
		showFindedTreeNode(_findedIndex);
	}
	/*
	 * 展示查询结果，将查询得到的节点以查询结果特定的样式高亮
	 * 参数：	index	节点在结果集中的序号
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */
	function showFindedTreeNode(index) {
		if(_findedNodes.length == 0) {
			alert(_TREE_SEARCH_NO_RESULT_MSG);
			setFindedNode(null);
			return;
		}
		setFindedNode(_findedNodes[index]);
	}
	/*
	 * 设定查询结果节点高亮
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-7-2
	 */
	function setFindedNode(node) {
		_findedNode = node;
		treeObj.setFindedNode(node);
		treeNode = instanceTreeNode(node);
		if(treeNode instanceof TreeNode) {
			treeNode.focus();
		}else{
			displayObj.resetTotalTreeNodes();
			displayObj.reload();
		}
	}
	this.test = function(name) {
		return eval(name);
	}
}






<!--
	控件名称：单选/多选树

	功能说明：	1、单选/多选树的显示，重载
				2、枝节点的打开关闭（响应鼠标事件、接口对象提供方法）
				3、兄弟节点间的移动位置（响应鼠标事件、接口对象提供方法）
				4、节点的增、删、改
				5、节点选择状态的更改（响应鼠标事件、接口对象提供方法），参数控制是否和激活节点相关联
				6、返回选中节点（参数控制是否包括半选节点），返回对象或数组，提供方法转换成xml
				7、TreeNode接口对象。

	使用方法：见“树控件文档.txt”、‘树XML模板规范.xml’（参考test.html文件）
	名称空间：Tree
	作　　者：scq
	时　　间：2004-6-3
 -->

<PUBLIC:ATTACH event="oncontentready" onevent="initialize()" />
<PUBLIC:ATTACH event="ondblclick" onevent="_ondblclick()" />
<PUBLIC:ATTACH event="onclick" onevent="_onclick()" />
<PUBLIC:ATTACH event="oncontextmenu" onevent="_oncontextmenu()" />
<PUBLIC:ATTACH event="ondragstart" onevent="_ondragstart()" />
<PUBLIC:ATTACH event="ondragover" onevent="_ondragenter()" />
<PUBLIC:ATTACH event="ondragleave" onevent="_ondragleave()" />
<PUBLIC:ATTACH event="ondrop" onevent="_ondrop()" />
<PUBLIC:ATTACH event="ondragend" onevent="_ondragend()" />
<PUBLIC:ATTACH event="onmouseover" onevent="_onmouseover()" />
<PUBLIC:ATTACH event="onmouseout" onevent="_onmouseout()" />
<PUBLIC:ATTACH event="onselectstart" onevent="_onselectstart()" />

<PUBLIC:PROPERTY NAME="treeType" /><!-- 树的类型 :value: multi/single-- >
<PUBLIC:PROPERTY NAME="src" /><!-- 树数据源（符合树XML规范的xml数据岛id） -->
<PUBLIC:PROPERTY NAME="selected" /><!-- 树数据源（符合树XML规范的xml数据岛id） -->
<PUBLIC:PROPERTY NAME="selectedIds" /><!-- 默认选中的节点id，多个id用“,”隔开 -->
<PUBLIC:PROPERTY NAME="canMoveNode" /><!-- 是否可以移动树节点，默认为false -->
<PUBLIC:PROPERTY NAME="treeNodeSelectedChangeState" /><!-- 当树被选择时，同时修改树的选择状态（此时会触发onChange事件），单选树默认为true，多选树默认为false -->
<PUBLIC:PROPERTY NAME="baseUrl" /><!--文件基本目录-->
<PUBLIC:PROPERTY NAME="treeNodeClickOpenNode" /><!--点击节点是否同步打开此节点-->
<PUBLIC:PROPERTY NAME="allCheckTypeDisabled" /><!--所有节点都不能改变选择状态-->
<PUBLIC:PROPERTY NAME="selectSelf" /><!--选中节点时只改变自己的选择状态，与父、子节点无关-->

<PUBLIC:METHOD NAME="getTreeNodeById"/>	<!-- 根据id获取相应的TreeNode对象，如果id对应对象不存在，返回null -->
<PUBLIC:METHOD NAME="getSelectedTreeNode"/>	<!-- 获取被选中的节点的TreeNode对象或对象数组 -->
<PUBLIC:METHOD NAME="getSelectedXmlNode"/>	<!-- 获取被选中的节点的XML对象或对象数组 -->
<PUBLIC:METHOD NAME="getActiveTreeNode"/>	<!-- 获取激活节点的TreeNode对象 -->
<PUBLIC:METHOD NAME="setActiveTreeNode"/>	<!-- 根据id激活节点 -->
<PUBLIC:METHOD NAME="insertTreeNodeXml"/>	<!-- 新增子节点 -->
<PUBLIC:METHOD NAME="removeTreeNode"/>	<!-- 删除节点 -->
<PUBLIC:METHOD NAME="moveTreeNode"/>	<!-- 移动节点 -->
<PUBLIC:METHOD NAME="moveExternalTreeNode"/>	<!-- 从外部（其他树）移动节点 -->
<PUBLIC:METHOD NAME="getTreeTitle"/>	<!-- 获取树的标题 -->
<PUBLIC:METHOD NAME="reload"/>	<!-- 重新载入数据源 -->
<PUBLIC:METHOD NAME="load"/>	<!-- xml节点、数据岛、数据文件或xml字符串，重新载入数据源 -->
<PUBLIC:METHOD NAME="loadXML"/>	<!-- xml节点、数据岛、数据文件或xml字符串，重新载入数据源 -->
<PUBLIC:METHOD NAME="loadDefaultChecked"/>	<!-- 根据给定数据(树xml数据)，处理默认选中的节点 -->
<PUBLIC:METHOD NAME="loadDefaultCheckedByIds"/>	<!-- 根据给定数据(id字符串，多个id用“,”隔开)，处理默认选中的节点 -->
<PUBLIC:METHOD NAME="getIds"/>	<!-- 获取全部（或全部全选，或全部全选、半选）节点的id字符串 -->
<PUBLIC:METHOD NAME="disable"/>	<!-- 禁止所有节点改变选中状态 -->
<PUBLIC:METHOD NAME="enable"/>	<!-- 允许没有被特殊指定不能选中的节点改变选中状态 -->
<PUBLIC:METHOD NAME="setDefaultActive"/>	<!-- 设定默认激活节点 -->
<PUBLIC:METHOD NAME="searchNode"/>	<!-- 查询节点 -->
<PUBLIC:METHOD NAME="searchNext"/>	<!-- 查询下一个节点 -->


<script>
    var _baseurl = element.baseurl==null?"":element.baseurl;
    document.write("<script src=\"" + _baseurl + "constant.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "public.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "tree.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "display.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "row.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "treenode.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "event.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "datasource.js\"><\/" + "script>");
    document.write("<script src=\"" + _baseurl + "search.js\"><\/" + "script>");
</script>


<script language="JavaScript">
/**
 * 事件触发混乱问题，暂时改用模拟事件
 */
function createEventObject() {
	return new Object();
}
function EventFirer(name) {
	var _name = name;
	this.fire = function (event) {
		//2006-4-6 修正原先模拟事件只能用字符串类型，更改为可同时支持function类型
		var func = element.getAttribute(_name);
		if(null==func) {
			return;
		}
		var funcType = typeof(func);
		if("string"==funcType) {
			eval(func);
		}else if("function"==funcType) {
			func(event);
		}
	}
}
var eventComponentReady = new EventFirer("oncomponentready");
var eventTreeReady = new EventFirer("onLoad");
var eventNodeExpand = new EventFirer("onTreeNodeExpand");
var eventNodeSelected = new EventFirer("onTreeNodeSelected");
var eventNodeActived = new EventFirer("onTreeNodeActived");
var eventNodeDoubleClick = new EventFirer("onTreeNodeDoubleClick");
var eventNodeMoved = new EventFirer("onTreeNodeMoved");
var eventTreeChange = new EventFirer("onChange");
var eventSelectedDefault = new EventFirer("onInitDefaultSelected");
var eventBeforeSelected = new EventFirer("onBeforeSelected");
var eventBeforeActived = new EventFirer("onBeforeActived");
var eventBeforeSelectedAndActived = new EventFirer("onBeforeSelectedAndActived");
var eventNodeRightClick = new EventFirer("onTreeNodeRightClick");


var treeObj = null;
var displayObj = null;
var searchObj = null;
var loador = null;
element.className = _TREE_STYLE;
//element.innerHTML = _TREE_WAIT_LOAD_DATA_MSG;
/*
 * 载入树，如果没有参数dataSrc，则通过控件属性src获取数据；否则根据dataSrc获取数据。
 * 参数：	dataSrc 树数据源（符合树XML规范的xml数据岛id），字符串，可以省略。
 * 返回：	无
 */
function initialize() {
	loador = window.setTimeout(initializeImp, 0);
}
function initializeImp() {
	window.clearTimeout(loador);
	instanceTree();
	instanceDisplay();
	instanceSearch();

	initData();

	//2006-4-6 增加isLoaded属性表示是否初始化完成
	element.isLoaded = true;

	//触发控件初始化完成事件
	eventComponentReady.fire(createEventObject()); 
}

/*
 * 根据id返回TreeNode对象，如果对象不存在，则返回null
 * 参数：	id	节点id，字符串
 * 返回：	TreeNode对象/null
 */
function getTreeNodeById(id) {
	var node = treeObj.getXmlRoot().selectSingleNode(".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_ID + "='" + id + "']");
	return instanceTreeNode(node);
}

/* 返回选取节点的TreeNode对象或对象数组
 * 参数：	hasHalfChecked	是否包括半选状态的节点，true为包括，false为不包括。
 * 返回：	单选树：TreeNode对象；多选树：TreeNode对象数组
 * 如果返回的是数组，则数组对象还提供toElement方法，将数组直接转换成xml字符串。
 * 如果hasHalfChecked参数为false，则不包括半选状态的节点，同时toElement方法
 * 给出的xml将所有TreeNode都放到根节点actionSet节点下；否则将给出包括全选、半
 * 选的所有节点，并按原有的节点层次关系给出xml字符串。
 */
function getSelectedTreeNode(hasHalfChecked) {
	return treeObj.getSelectedTreeNode(hasHalfChecked);
}

/* 返回选取节点的Xml对象或对象数组
 * 参数：	hasHalfChecked	是否包括半选状态的节点，true为包括，false为不包括。
 * 返回：	单选树：Xml对象；多选树：Xml对象数组
 * 如果返回的是数组，则数组对象还提供toElement方法，将数组直接转换成xml节点。
 * 如果hasHalfChecked参数为false，则不包括半选状态的节点，同时toElement方法
 * 给出的xml将所有Xml都放到根节点actionSet节点下；否则将给出包括全选、半
 * 选的所有节点，并按原有的节点层次关系给出xml字符串。
 */
function getSelectedXmlNode(hasHalfChecked) {
	return treeObj.getSelectedXmlNode(hasHalfChecked);
}

/*
 * 获取当前激活节点的TreeNode对象。如果没有激活的对象，则返回null。
 * 返回：	TreeNode对象
 */
function getActiveTreeNode() {
	return treeObj.getActiveNode();
}

/*
 * 设定相应id的节点为激活状态。
 * 参数：id 字符串，所要激活的节点的id，必须提供，否则会报错。
 * 返回：	无
 * 如果相应id的节点尚未被打开，也就是其父节点或父节点的父节点等没有被打开，那么先打开此节点。
 * 然后激活此节点，同时根据treeNodeSelectedChangeState属性，确定是否同时改变节点选择状态。
 */
function setActiveTreeNode(id) {
	var treeNode = getTreeNodeById(id);
	if(treeNode instanceof TreeNode) {
		treeNode.setActive();	//激活节点，同时根据treeNodeSelectedChangeState属性，确定是否同时改变节点选择状态。
		treeObj.setActiveNode(treeNode);
		treeNode.focus();		//打开节点，让节点出现在可视区域内。
	}
}

/*
 * 新增子节点，同时激活新节点
 * 参数：	newNodeXML	新节点合法的xml
 *			parentTreeNode	父节点合法的TreeNode对象
 * 返回：	true/false
 */
function insertTreeNodeXml(newNodeXML, parentTreeNode) {
	if(!(parentTreeNode instanceof TreeNode)) {
		return false;
	}
	var treeNode = parentTreeNode.appendChild(newNodeXML);		//新增子节点
	if(!(treeNode instanceof TreeNode)) {
		return false;
	}
	if(treeObj.isFocusNewTreeNode()) {
		treeNode.setActive();	//激活节点，同时根据treeNodeSelectedChangeState属性，确定是否同时改变节点选择状态。
		treeNode.focus();		//打开节点，让节点出现在可视区域内。
	}else{
		parentTreeNode.setActive();
		parentTreeNode.open();
	}
	return true;
}

/*
 * 删除节点
 * 参数：	treeNode	节点的TreeNode对象
 * 返回：	true/false
 */
function removeTreeNode(treeNode) {
	//alert(treeNode instanceof TreeNode);
	if(!(treeNode instanceof TreeNode)) {
		return false;
	}
	var result = treeNode.remove();		//删除节点
	displayObj.reload();
	return result;
}

/*
 * 跟据目标节点和移动状态，移动节点位置。
 * 参数：	movedTreeNode	移动节点TreeNode对象
 *			toTreeNode		目标节点TreeNode对象
 *			moveState		移动状态，-1为目标节点上方，1为目标节点下方
 * 返回：	true/false
 */
function moveTreeNode(movedTreeNode, toTreeNode, moveState) {
	if(!treeObj.isCanMoveNode()
		|| !(movedTreeNode instanceof TreeNode)
		|| !(toTreeNode instanceof TreeNode)) {
		return false;
	}
	var result = movedTreeNode.moveTo(toTreeNode, moveState);	//移动节点
	displayObj.reload();
	return result;
}

/*
 * 跟据目标节点和移动状态，从外部（其他树）移动节点位置。
 * 参数：	movedTreeNode	移动外部（其他树）的节点TreeNode对象
 *			toTreeNode		目标节点TreeNode对象
 *			moveState		移动状态，-1为目标节点上方，1为目标节点下方
 * 返回：	true/false
 */
function moveExternalTreeNode(movedTreeNode, toTreeNode, moveState) {

	var movedTreeNodeId = movedTreeNode.getId();
	var movedTreeNodeXml = movedTreeNode.getXmlNode().xml;
	var toTreeNodeParent = toTreeNode.getParent();

	if(toTreeNodeParent.getXmlNode().nodeName=="actionSet") {//根节点
		var newRootTreeNode = new TreeNode();		//新增根节点
		newRootTreeNode.appendRoot(movedTreeNodeXml);
	}else{//枝节点
		insertTreeNodeXml(movedTreeNodeXml,toTreeNodeParent);
	}
		
	var newNode = getTreeNodeById(movedTreeNodeId);
	moveTreeNode(newNode,toTreeNode,moveState);

	var newNode = getTreeNodeById(movedTreeNodeId);
	newNode.setActive();	//激活节点，同时根据treeNodeSelectedChangeState属性，确定是否同时改变节点选择状态。
	newNode.focus();		//打开节点，让节点出现在可视区域内。
}

/*
 * 获取树的标题
 * 参数：
 * 返回值：树标题字符串
 * 作者：scq
 * 时间：2004-6-14
 */
function getTreeTitle() {
	try{
		var title = treeObj.getXmlRoot().getAttribute("title");
		if(isNullOrEmpty(title)) {
			return "选择";
		}else{
			return title;
		}
	}catch(e) {
		alert("不能获取标题，xml数据为空或不能解析");
		throw(e);
	}
}
/*
 * 设定默认选中节点
 * 参数：	type	默认选中类型
 * 返回值：
 * 作者：scq
 * 时间：2005-11-17
 */
function setDefaultActive(type) {
	treeObj.setDefaultActive(type);
}
/*
 * 通过xml节点、数据岛、数据文件或xml字符串，重新载入数据源
 * 参数：	dataSrc	数据岛或者xml节点
 * 返回值：
 * 作者：scq
 * 时间：2004-7-2
 */
function loadXML(dataSrc) {
	treeObj.loadData(dataSrc);
	treeObj.setDefaultActive();
	displayObj.resetTotalTreeNodes();
	displayObj.reload();
	//触发载入完成事件
	eventTreeReady.fire(createEventObject()); 
}
function load (dataSrc) {
	loadXML(dataSrc);
}
function reload () {
	displayObj.resetTotalTreeNodes();
	displayObj.reload();
}
/*
 * 初始化数据和默认选中状态
 * 返回值：
 * 作者：scq
 * 时间：2004-12-23
 */
function initData() {
	treeObj.loadData();
	treeObj.loadSelectedData();
	treeObj.setDefaultActive();
	displayObj.resetTotalTreeNodes();
	displayObj.reload();
	//触发载入完成事件
	eventTreeReady.fire(createEventObject()); 
}
/*
 * 根据给定的数据，处理树节点的默认选中状态
 * 参数：	selectedSrc	默认选中的数据
 *		isClearOldSelected	是否清除原先选中节点
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-23
 */
function loadDefaultChecked(selectedSrc, isClearOldSelected) {
	treeObj.loadSelectedData(selectedSrc, isClearOldSelected);
	displayObj.resetTotalTreeNodes();
	displayObj.reload();
	var eventObj = createEventObject();
	eventObj.type = "_SelectedDefalt";
	eventSelectedDefault.fire(eventObj);
}
/*
 * 根据给定的数据，处理树节点的默认选中状态
 * 参数：	selectedIds	默认选中的数据(id字符串，多个id用“,”隔开)
 *		isClearOldSelected	是否清除原先选中节点
 *		isDependParent	    是否依赖父节点(父节点选中，则所有子节点均选中)
 * 返回值：
 * 作者：沈超奇
 * 时间：2005-4-19
 */
function loadDefaultCheckedByIds(selectedIds, isClearOldSelected, isDependParent) {
	treeObj.loadSelectedDataByIds(selectedIds, isClearOldSelected, isDependParent);
	displayObj.resetTotalTreeNodes();
	displayObj.reload();
	var eventObj = createEventObject();
	eventObj.type = "_SelectedDefalt";
	eventSelectedDefault.fire(eventObj);
}

/*
 * 获取ids，所有节点的id字符串，默认为所有选中状态(全选、半选)的节点id字符串
 * 参数：isAll	是否为全部节点的Id
 *       onlySelected	只包括全选的
 *	 exIdPatterns	匹配不包含的Id的正则表达式数组
 * 返回：	id，字符串：id1,id2,id3
 * 作者：沈超奇
 * 时间：2005-9-19
 */
function getIds(isAll, onlySelected, exIdPatterns) {
	if(isAll) {
		var path = ".//" + _TREE_NODE_NAME;
	}else{
		if(onlySelected) {
			var path = ".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1']";
		}else{
			var path = ".//" + _TREE_NODE_NAME + "[@" + _TREE_NODE_CHECKTYPE + "='1' or @" + _TREE_NODE_CHECKTYPE + "='2']";
		}
	}
	var nodes = treeObj.getXmlRoot().selectNodes(path);
	var ids = "";
	node:
	for(var i = 0; i < nodes.length; i++) {
		var id = nodes[i].getAttribute(_TREE_NODE_ID);
		if(id == _TREE_ROOT_TREE_NODE_ID) {
			continue;
		}
		if(exIdPatterns != null) {
			for(var j = 0; j < exIdPatterns.length; j++) {
				if(exIdPatterns[j].test(id)) {
					continue node;
				}
			}
		}
		if(ids.length > 0) {
			ids += ",";
		}
		ids += id;
	}
	return ids;
}

/*
 * 禁止所有节点改变选中状态
 * 参数：
 * 返回：
 * 作者：沈超奇
 * 时间：2005-10-29
 */
function disable() {
	treeObj.disable();
}

/*
 * 取消全部节点都被禁止改变选中状态，即没有特殊指定不能被选中的节点外，全都能改变节点状态
 * 参数：
 * 返回：
 * 作者：沈超奇
 * 时间：2005-10-29
 */
function enable() {
	treeObj.enable();
}

/*
 * 查询得到所有符合要求的结果
 * 参数：	searchStr	查询的字符串
 *			searchBy	查询的属性名称
 *			searchType	查询方式：hazy(模糊)/rigor(精确)，默认为rigor
 *			direct		查询方向
 * 返回值：
 * 作者：scq
 * 时间：2006-1-9
 */	
function searchNode(searchStr, searchBy, searchType, direct) {
	if(searchObj.search(searchStr, searchBy, searchType)) {
		searchObj.first(direct);
	}
}

/*
 * 获取查询结果的下一个结果
 * 参数：
 *			direct		查询方向：默认为向下查询
 *			isCircle	是否循环查询，默认为不循环查询
 * 返回值：
 * 作者：scq
 * 时间：2006-1-9
 */	
function searchNext(direct, isCircle) {
	searchObj.next(direct, isCircle);
}

</script>


/*
 *	名    称：	数据源对象
 *	功能描述：	此对象主要实现获取控件数据，以及数据的常用处理功能；
 *	
 */


/*
 * 数据源对象
 */
function DataSource(src) {
	this.xmlRoot = this.loadXml(src);
}
 
DataSource.prototype.loadXml= function (src) {
	if(isNullOrEmpty(src)) { 
		return null;
	}
	if(typeof(src) == "object"
		&& (src.tagName == _TREE_ROOT_NODE_NAME || src.tagName == _TREE_NODE_NAME)
		&& src.nodeTypeString == "element") {
		return src;
	}
	try{
		eval("src = " + src + ";");
		if(src == null || (typeof(src) == "string" && src =="")) {	//没有定义
			return null;
		}
		if(typeof(src) == "object") {
			if(/^xml$/.test(src.tagName) && src.nodeTypeString == "document") {	//xml数据岛
				return src.documentElement;
			}
			if((src.tagName == _TREE_ROOT_NODE_NAME || src.tagName == _TREE_NODE_NAME)
				 && src.nodeTypeString == "element") {	//xml节点
				return src;
			}
			throw("DataSource:装载数据出错!（数据源类型不合法）");
		}
	}catch(e) {
	}
	try{
		//引用xml数据文件/xml数据字符串
		var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
		xmlDom.async = false;
		if (xmlDom.loadXML(src)) {	//当src为xml数据时导入数据
			return xmlDom.documentElement;
		}
		if (xmlDom.load(src)) {		//当src为文件路径时导入数据
			return xmlDom.documentElement;
		}
	}catch(e) {
	}
	alert("DataSource:装载数据出错!");
	return null;																
}



/*
 * 禁止选取控件中文字事件
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _onselectstart() {
	event.returnValue = false;
	return false;  
}

/*
 * 鼠标双击响应函数，触发自定义双击事件。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondblclick() {
	var eventObj = window.event.srcElement;
	var row = getRow(eventObj);
	if(row instanceof Row) {
		var treeNode = instanceTreeNode(row.getXmlNode());
	}
	if(!(row instanceof Row) || !(treeNode instanceof TreeNode) || !treeNode.isCanSelected() || (eventObj != row.getLabel() && eventObj != row.getIcon())) {	
		return;
	}
	//触发双击事件
	var eventObj = createEventObject();
	eventObj.treeNode = treeNode;
	eventNodeDoubleClick.fire(eventObj);
}

/*
 * 	鼠标右键单击事件响应函数
 *			如果点击的是文字连接，则激活该节点，同时触发右键单击事件。
 * 参数：
 * 返回值：
 * 作者：毛云
 * 时间：2006-5-7
 */
function _oncontextmenu() {
	var eventObj = window.event.srcElement;
	window.event.returnValue = false;
	var row = getRow(eventObj);
	if(row instanceof Row) {
		var treeNode = instanceTreeNode(row.getXmlNode());
	}
	if(!(row instanceof Row) || !(treeNode instanceof TreeNode)) {
		return;
	}
    //设置节点为激活
    if(true==treeNode.isCanSelected()) {
        treeObj.setActiveNode(treeNode);
    }

    //触发右键激活节点事件
    var eventObj = createEventObject();
    eventObj.treeNode = treeNode;
    eventObj.clientX = event.clientX;
    eventObj.clientY = event.clientY;
    eventNodeRightClick.fire(eventObj);

	displayObj.reload();
    
}

/*
 * 	鼠标单击事件响应函数
 *			如果点击的是选择状态图标，则改变选择状态，同时根据treeNodeSelectedChangeState属性，确定是否同时激活该节点。
 *			如果点击的是伸缩状态图标，则打开或收缩当前节点的直系子节点。
 *			如果点击的是文字连接，则激活该节点，同时根据treeNodeSelectedChangeState属性，确定是否同时改变节点选择状态。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _onclick() {
	var eventObj = window.event.srcElement;
	window.event.returnValue = false;
	var row = getRow(eventObj);
	if(row instanceof Row) {
		var treeNode = instanceTreeNode(row.getXmlNode());
	}
	if(!(row instanceof Row) || !(treeNode instanceof TreeNode)) {
		return;
	}
	if(eventObj == row.getCheckType()) {		//根据不同的treeType，改变相应的选择状态
		treeNode.changeSelectedState(window.event.shiftKey);
	}else if(eventObj == row.getFolder()) {
		treeNode.changeFolderState();		//展开、收缩节点的直系子节点
	}else if(eventObj == row.getLabel() || eventObj == row.getIcon()) {
		if(treeObj.isChangeFolderStateByClickLabel()) {
			//2006-4-22 只有当枝节点才允许执行
			if(treeNode.node.hasChildNodes()) {
				//点击节点文字时，改变节点伸缩状态
				treeNode.changeFolderState();
			}
		}
		treeNode.setActive(window.event.shiftKey);		//激活节点
	}
	displayObj.reload();
}

//鼠标移到节点上
/*
 * 鼠标移到元素上。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _onmouseover() {
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || row.getLabel() != obj) {
		return;
	}
	row.setClassName(treeObj.getClassName(row.getXmlNode(), _TREE_NODE_OVER_STYLE));
}
//鼠标离开节点
/*
 * 鼠标离开元素时。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _onmouseout() {
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || row.getLabel() != obj) {
		return;
	}
	row.setClassName(treeObj.getClassName(row.getXmlNode()));
}

///////////////////////	以下函数用于节点拖动 ////////////////////////////

/*
 * 开始拖动事件响应，设定拖动节点
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondragstart() {
	if(!treeObj.isCanMoveNode()) {		//确定是否提供拖动节点功能
		return;
	}
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || obj != row.getLabel()) {
		return;
	}
	var node = row.getXmlNode();
	//设定拖动节点
	treeObj.setMovedNode(node);

	//2006-4-7 调整为全局
	//element.movedNode = node;
	//element.movedNodeScrollTop = displayObj.getScrollTop() + getTop(obj);
	//element.movedRow = obj;
	var tempData = {};
	tempData.moveTree = element;
	tempData.movedNode = node;
	tempData.movedNodeScrollTop = displayObj.getScrollTop() + getTop(obj);
	tempData.movedRow = obj;
	window._dataTransfer = tempData;

	row.setClassName(_TREE_NODE_MOVED_STYLE);
	window.event.dataTransfer.effectAllowed = "move";
}

/*
 * 拖动完成，触发自定义节点拖动事件
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondrop() {
	if(!treeObj.isCanMoveNode()) {		//同上
		return;
	}
	stopScrollTree();
	var obj = window.event.srcElement;
	obj.runtimeStyle.borderBottom = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	obj.runtimeStyle.borderTop = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	//触发自定义事件
	var eObj = createEventObject();
	eObj.movedTreeNode = instanceTreeNode(window._dataTransfer.movedNode);
	eObj.toTreeNode = instanceTreeNode(window._dataTransfer.toNode);
	eObj.moveState = window._dataTransfer.moveState;
	//2006-4-7 增加被拖动的节点所在树
	eObj.moveTree = window._dataTransfer.moveTree;
	eventNodeMoved.fire(eObj);
}

/*
 * 拖动结束，去除拖动时添加的样式
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function  _ondragend() {
	if(!treeObj.isCanMoveNode()) {		//同上
		return;
	}
	stopScrollTree();
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || obj != row.getLabel()) {
		return;
	}
	obj.runtimeStyle.borderBottom = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	obj.runtimeStyle.borderTop = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	treeObj.setMovedNode(null);
	displayObj.reload();
}

/*
 * 拖动时，鼠标进入节点，设定目标节点和拖动状态
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondragenter() {
	if(!treeObj.isCanMoveNode()) {		//同上
		return;
	}
    if(null==window._dataTransfer) {
        return;
    }
	var obj = window.event.srcElement;
	//判断是否需要滚动树，如是则相应的滚动
	startScrollTree(obj);
	var row = getRow(obj);
	if(row instanceof Row) {
		var node = row.getXmlNode();
	}

	//2006-4-7 区分是否同一棵树
	if(!(row instanceof Row)
		|| obj != row.getLabel()) {		//拖动的不是文字链接，则无效
		return;
	}
	//是同一棵树
	if(window._dataTransfer && window._dataTransfer.moveTree==element) {
		if(window._dataTransfer.movedNode == null
			|| node.parentNode != window._dataTransfer.movedNode.parentNode	//不是兄弟节点无效
			|| obj == window._dataTransfer.movedRow) {		//目标节点相同无效
			return;
		}
	}else{
	}

	window._dataTransfer.toNode = node;
	if(displayObj.getScrollTop() + getTop(obj) > window._dataTransfer.movedNodeScrollTop) {
		window._dataTransfer.moveState = 1;
		obj.runtimeStyle.borderBottom = _TREE_NODE_MOVE_TO_LINE_STYLE;
	}else{
		window._dataTransfer.moveState = -1;
		obj.runtimeStyle.borderTop = _TREE_NODE_MOVE_TO_LINE_STYLE;
	}
	window.event.returnValue = false;
	window.event.dataTransfer.dropEffect = "move";
}

/*
 * 拖动时，鼠标离开节点
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondragleave() {
	if(!treeObj.isCanMoveNode()) {
		return;
	}
	stopScrollTree(obj);
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || obj != row.getLabel()) {
		return;
	}
	obj.runtimeStyle.borderBottom = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	obj.runtimeStyle.borderTop = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	window.event.dataTransfer.dropEffect = "none";
}
/////////////////////////////// 节点拖动结束 /////////////////////////////

