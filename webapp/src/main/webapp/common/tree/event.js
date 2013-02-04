/*
 * 函数说明：禁止选取控件中文字事件
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
 * 函数说明：鼠标双击响应函数，触发自定义双击事件。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondblclick(){
	var eventObj = window.event.srcElement;
	var row = getRow(eventObj);
	if(row instanceof Row){
		var treeNode = instanceTreeNode(row.getXmlNode());
	}
	if(!(row instanceof Row) || !(treeNode instanceof TreeNode) || !treeNode.isCanSelected() || (eventObj != row.getLabel() && eventObj != row.getIcon())){	
		return;
	}
	//触发双击事件
	var eventObj = createEventObject();
	eventObj.treeNode = treeNode;
	eventNodeDoubleClick.fire(eventObj);
}

/*
 * 函数说明：	鼠标右键单击事件响应函数
 *			如果点击的是文字连接，则激活该节点，同时触发右键单击事件。
 * 参数：
 * 返回值：
 * 作者：毛云
 * 时间：2006-5-7
 */
function _oncontextmenu(){
	var eventObj = window.event.srcElement;
	window.event.returnValue = false;
	var row = getRow(eventObj);
	if(row instanceof Row){
		var treeNode = instanceTreeNode(row.getXmlNode());
	}
	if(!(row instanceof Row) || !(treeNode instanceof TreeNode)){
		return;
	}
    //设置节点为激活
    if(true==treeNode.isCanSelected()){
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
 * 函数说明：	鼠标单击事件响应函数
 *			如果点击的是选择状态图标，则改变选择状态，同时根据treeNodeSelectedChangeState属性，确定是否同时激活该节点。
 *			如果点击的是伸缩状态图标，则打开或收缩当前节点的直系子节点。
 *			如果点击的是文字连接，则激活该节点，同时根据treeNodeSelectedChangeState属性，确定是否同时改变节点选择状态。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _onclick(){
	var eventObj = window.event.srcElement;
	window.event.returnValue = false;
	var row = getRow(eventObj);
	if(row instanceof Row){
		var treeNode = instanceTreeNode(row.getXmlNode());
	}
	if(!(row instanceof Row) || !(treeNode instanceof TreeNode)){
		return;
	}
	if(eventObj == row.getCheckType()){		//根据不同的treeType，改变相应的选择状态
		treeNode.changeSelectedState(window.event.shiftKey);
	}else if(eventObj == row.getFolder()){
		treeNode.changeFolderState();		//展开、收缩节点的直系子节点
	}else if(eventObj == row.getLabel() || eventObj == row.getIcon()){
		if(treeObj.isChangeFolderStateByClickLabel()){
			//2006-4-22 只有当枝节点才允许执行
			if(treeNode.node.hasChildNodes()){
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
 * 函数说明：鼠标移到元素上。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _onmouseover(){
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || row.getLabel() != obj){
		return;
	}
	row.setClassName(treeObj.getClassName(row.getXmlNode(), _TREE_NODE_OVER_STYLE_NAME));
}
//鼠标离开节点
/*
 * 函数说明：鼠标离开元素时。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _onmouseout(){
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || row.getLabel() != obj){
		return;
	}
	row.setClassName(treeObj.getClassName(row.getXmlNode()));
}

///////////////////////	以下函数用于节点拖动 ////////////////////////////

/*
 * 函数说明：开始拖动事件响应，设定拖动节点
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondragstart(){
	if(!treeObj.isCanMoveNode()){		//确定是否提供拖动节点功能
		return;
	}
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || obj != row.getLabel()){
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

	row.setClassName(_TREE_NODE_MOVED_STYLE_NAME);
	window.event.dataTransfer.effectAllowed = "move";
}

/*
 * 函数说明：拖动完成，触发自定义节点拖动事件
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondrop(){
	if(!treeObj.isCanMoveNode()){		//同上
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
 * 函数说明：拖动结束，去除拖动时添加的样式
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function  _ondragend(){
	if(!treeObj.isCanMoveNode()){		//同上
		return;
	}
	stopScrollTree();
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || obj != row.getLabel()){
		return;
	}
	obj.runtimeStyle.borderBottom = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	obj.runtimeStyle.borderTop = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	treeObj.setMovedNode(null);
	displayObj.reload();
}

/*
 * 函数说明：拖动时，鼠标进入节点，设定目标节点和拖动状态
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondragenter(){
	if(!treeObj.isCanMoveNode()){		//同上
		return;
	}
    if(null==window._dataTransfer){
        return;
    }
	var obj = window.event.srcElement;
	//判断是否需要滚动树，如是则相应的滚动
	startScrollTree(obj);
	var row = getRow(obj);
	if(row instanceof Row){
		var node = row.getXmlNode();
	}

	//2006-4-7 区分是否同一棵树
	if(!(row instanceof Row)
		|| obj != row.getLabel()){		//拖动的不是文字链接，则无效
		return;
	}
	//是同一棵树
	if(window._dataTransfer && window._dataTransfer.moveTree==element){
		if(window._dataTransfer.movedNode == null
			|| node.parentNode != window._dataTransfer.movedNode.parentNode	//不是兄弟节点无效
			|| obj == window._dataTransfer.movedRow){		//目标节点相同无效
			return;
		}
	}else{
	}

	window._dataTransfer.toNode = node;
	if(displayObj.getScrollTop() + getTop(obj) > window._dataTransfer.movedNodeScrollTop){
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
 * 函数说明：拖动时，鼠标离开节点
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-1
 */
function _ondragleave(){
	if(!treeObj.isCanMoveNode()){
		return;
	}
	stopScrollTree(obj);
	var obj = window.event.srcElement;
	var row = getRow(obj);
	if(!(row instanceof Row) || obj != row.getLabel()){
		return;
	}
	obj.runtimeStyle.borderBottom = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	obj.runtimeStyle.borderTop = _TREE_NODE_MOVE_TO_HIDDEN_LINE_STYLE;
	window.event.dataTransfer.dropEffect = "none";
}
/////////////////////////////// 节点拖动结束 /////////////////////////////