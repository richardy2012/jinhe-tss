//////////////////////////////////////////////////////////////////
//	对象名称：TreeNode											//
//	职责：	树节点对象接口。负责处理节点状态变化。					//
//////////////////////////////////////////////////////////////////

/*
 * 函数说明：根据xml节点获取TreeNode对象的一个实例
 * 参数：	node	xml节点
 * 返回值：	TreeNode/null
 * 作者：scq	
 * 时间：2004-6-11
 */
function instanceTreeNode(node) {
	if(node == null){
		return null;
	}
	return new TreeNode(node);
}

/*
 * 控件接口TreeNode对象
 * 参数：	node	xml节点
 * 方法：	
 */
function TreeNode(node){
	this.node = node;
}
TreeNode.prototype = new function(){

	/*
	 * 获取xml节点
	 */
	this.getXmlNode = function(){
		return this.node;
	}
	/*
	 * 是否为子节点已经打开的节点
	 * 返回：	true/false
	 */
	this.isOpened = function(){
		return this.node.getAttribute("_open") == "true";
	}
	/*
	 * 是否为可选择节点
	 * 返回：	true/false
	 */
	this.isCanSelected = function(){
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CANSELECTED) != "0";
	}
	/*
	 * 是否为可用链接节点，即display!=0
	 * 返回：	tree/false
	 */
	this.isEnabled = function(){
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY) != '0';
	}
	/*
	 * 是否为激活节点
	 * 返回：	true/false
	 */
	this.isActive = function(){
		return treeObj.isActiveNode(this.node);
	}
	/*
	 * 获取节点的选择状态
	 * 返回：	多选树：0/1/2；单选数：1/0
	 */
	this.getSelectedState = function(){
		var state = this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CHECKTYPE);
		if(/^(1|2)$/.test(state)){
			return parseInt(state);
		}else{
			return 0;
		}
	}
	/*
	 * 点击节点文字标签时，根据现有状态改成下一个选择状态
	 * 参数：	noChildren	不包含子节点
	 * 根据原有的选择状态，改变状态。如为1，2则返回0，否则返回1
	 */
	this.changeSelectedStateByActive = function(noChildren){
		treeObj.changeCheckedStateByActive(this, noChildren);
	}
	/*
	 * 根据现有状态改成下一个选择状态
	 * 参数：noChildren	选中节点时不包含子节点
	 *			noFireChangeEvent	是否触发onChange事件
	 * 根据原有的选择状态，改变状态。如为1，2则返回0，否则返回1
	 */
	this.changeSelectedState = function(noChildren, noFireChangeEvent){
		this.setSelectedState(treeObj.getNextState(this), noChildren, noFireChangeEvent);
	}
	/*
	 * 设置选中状态，同时刷新相关节点的选择状态
	 * 参数：	state	选择状态
	 *			noChildren	只选中自己节点（只对选中时有效）
	 *			noFireChangeEvent	是否触发onChange事件
	 */
	this.setSelectedState = function(state, noChildren, noFireChangeEvent){
		if(!this.isCanSelected() || treeObj.isAllDisabledCheckType()){	//不可选择则返回
			return;
		}
		if(state == 1 && treeObj.isActiveBySelected(state)){
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeSelectedAndActived(Selected)";
			eventBeforeSelectedAndActived.fire(eventObj);
			if(!eventObj.returnValue){
				return;
			}
		}
		if(state == 1){
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeSelected";
			eventBeforeSelected.fire(eventObj);
			if(!eventObj.returnValue){
				return;
			}
		}
		justSelected(this, state, noChildren, noFireChangeEvent);
		if(!this.isActive() && treeObj.isActiveBySelected(state)){
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeActivedBySelected";
			eventBeforeActived.fire(eventObj);
			if(!eventObj.returnValue){
				return;
			}
			justActive(this);
		}
	}

	/*
	 * 获取父节点的TreeNode对象
	 * 返回：	TreeNode/null
	 */
	this.getParent = function(){
		return instanceTreeNode(this.node.parentNode);
	}
	/*
	 * 获取ids，自己和子节点的id字符串，默认为自己和全部子节点中选中状态(全选、半选)的节点id字符串
	 * 参数：isAll	是否为全部自己、子节点的Id
	 *       onlySelected	只包括全选的
	 * 返回：	id，字符串：id1,id2,id3
	 */
	this.getIds = function(isAll, onlySelected){
		if(isAll){
			var path = ".|.//" + _TREE_XML_NODE_NAME;
		}else{
			if(onlySelected){
				var path = ".[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']|.//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']";
			}else{
				var path = ".[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' or @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']|.//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1' or @" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='2']";
			}
		}
		var nodes = this.node.selectNodes(path);
		var ids = "";
		for(var i = 0; i < nodes.length; i++){
			var id = nodes[i].getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
			if(id == _TREE_XML_ROOT_TREE_NODE_ID){
				continue;
			}
			if(i > 0){
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
	this.getId = function(){
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID);
	}
	/*
	 * 设定id
	 */
	this.setId = function(id){
		var node = treeObj.getXmlRoot().selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_ID + "='" + id + "']");
		if(node != null && node != this.node){
			return alert("同id的节点已经存在！[id:" + id + "]");
		}
		//设置xml对象的id
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_ID, id);
	}
	/*
	 * 获取Name
	 * 返回：	name，字符串
	 */
	this.getName = function(){
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_NAME);
	}
	/*
	 * 设定Name
	 */
	this.setName = function(name){
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_NAME, name);
	}
	/*
	 * 获取FullName
	 * 返回：	fullName，字符串
	 */
	this.getFullName = function(){
		return this.node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_FULLNAME);
	}
	/*
	 * 设定FullName
	 */
	this.setFullName = function(fullName){
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_FULLNAME, fullName);
	}
	/*
	 * 激活节点
	 * 参数：noChildren		选中节点时，是否不包含子节点
	 */
	this.setActive = function(noChildren){
		if(!this.isCanSelected()){
			return;
		}
		if(!treeObj.isAllDisabledCheckType() && treeObj.isSelectByActived() && treeObj.getNextState(this) == 1){
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeSelectedAndActived(Active)";
			eventBeforeSelectedAndActived.fire(eventObj);
			if(!eventObj.returnValue){
				return;
			}
		}
		if(!this.isActive()){
			var eventObj = createEventObject();
			eventObj.treeNode = this;
			eventObj.returnValue = true;
			eventObj.type = "_BeforeActived";
			eventBeforeActived.fire(eventObj);
			if(!eventObj.returnValue){
				return;
			}
		}
        justActive(this);
		if(!treeObj.isAllDisabledCheckType() && treeObj.isSelectByActived()){
			if(treeObj.getNextState(this) == 1){
				var eventObj = createEventObject();
				eventObj.treeNode = this;
				eventObj.returnValue = true;
				eventObj.type = "_BeforeSelectedByActive";
				eventBeforeSelected.fire(eventObj);
				if(!eventObj.returnValue){
					return;
				}
			}
			justSelected(this, treeObj.getNextState(this), noChildren, false);
		}
	}
	/*
	 * 打开节点，让节点出现在可视区域内。
	 */
	this.focus = function(){
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
	this.enabled = function(isAllParent){
		if(isAllParent){
			var node = this.node;
			while(node != null && node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID) != _TREE_XML_ROOT_TREE_NODE_ID
					&& node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY) == '0'){
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
	this.disabled = function(isAllChildren){
		if(isAllChildren){
			var nodes = this.node.selectNodes(".|.//" + _TREE_XML_NODE_NAME);
			for(var i = 0; i < nodes.length; i++){
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
	this.setCanSelected = function(canSelected){
		this.node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_CANSELECTED, canSelected);
	}

	/**
	 * 点击文字标签时，改变节点伸缩状态
	 */
	this.changeFolderStateByActive = function(){
		//改变节点的伸缩状态
		treeObj.changeOpenStateByActive(this);
	}

	/*
	 * 改变节点的伸缩状态
	 */
	this.changeFolderState = function(){
		if(this.isOpened()){	
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
	this.open = function(){
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
	this.close = function(){
		this.node.setAttribute("_open", "false");	//标记当前节点为关闭状态

		//此节点关闭，关闭此节点的打开的子枝节点，同时标记关闭的原因。
		closeOpendChildNodes(this.node);

		displayObj.resetTotalTreeNodes();
	}
	/*
	 * 删除当前节点
	 * 返回：	true/false	前者表删除成功，后者表失败
	 */
	this.remove = function(){
		//删除xml中的此节点
		this.node.parentNode.removeChild(this.node);

		displayObj.resetTotalTreeNodes();
		return true;
	}
	/*
	 * 添加子节点
	 * 参数：	xml	合法的节点xml字符串
	 * 返回：	TreeNode/null	前者表添加子节点成功，返回新节点的TreeNode，后者表失败
	 */
	this.appendChild = function(xml){
		//生成子节点
		var newNodeXML = loadXmlToNode(xml);
		if(newNodeXML == null || newNodeXML.documentElement == null || newNodeXML.documentElement.nodeName != _TREE_XML_NODE_NAME){
			alert("TreeNode对象：新增节点xml数据不能正常解析！");
			return null;
		}
		//添加子节点
		var newNode = this.node.appendChild(newNodeXML.documentElement);

		var treeNode = instanceTreeNode(newNode);
		if(treeNode instanceof TreeNode){
			refreshStatesByNode(treeNode);		//根据新节点的选择状态刷新相关节点
		}

		displayObj.resetTotalTreeNodes();

		return treeNode;
	}
	/*
	 * 创建日期：2006-4-8
	 * 添加根节点
	 * 参数：	xml	合法的节点xml字符串
	 * 返回：	TreeNode/null	前者表添加根节点成功，返回新节点的TreeNode，后者表失败
	 */
	this.appendRoot = function(xml){
		//生成新节点
		var newNodeXML = loadXmlToNode(xml);
		if(newNodeXML == null || newNodeXML.documentElement == null || newNodeXML.documentElement.nodeName != _TREE_XML_NODE_NAME){
			alert("TreeNode对象：新增节点xml数据不能正常解析！");
			return null;
		}
		//添加根节点
		var newNode = treeObj.getXmlRoot().appendChild(newNodeXML.documentElement);

		var treeNode = instanceTreeNode(newNode);
		if(treeNode instanceof TreeNode){
			refreshStatesByNode(treeNode);		//根据新节点的选择状态刷新相关节点
		}

		displayObj.resetTotalTreeNodes();

		return treeNode;
	}
	/*
	 * 移动当前节点
	 * 参数：	toTreeNode	目标节点的TreeNode对象
	 *			moveState	移动状态：-1，移动到目标节点的上面，1，移动到目标节点的下面，1为缺省状态
	 * 返回：	true/false	前者表移动节点成功，后者表失败
	 */
	this.moveTo = function(toTreeNode, moveState){
		if(!(toTreeNode instanceof TreeNode) 
			|| this.node.parentNode == null){
			return false;
		}
		if(moveState == -1){	//移动到目标节点上方
			toTreeNode.getXmlNode().parentNode.insertBefore(this.node, toTreeNode.getXmlNode());
		}else{					//移动到目标节点下方
			toTreeNode.getXmlNode().parentNode.insertBefore(this.node, toTreeNode.getXmlNode().nextSibling);
		}
		displayObj.resetTotalTreeNodes();
		return true;
	}
	/*
	 * 获取当前节点的XML节点对象，该对象是一个浅拷贝对象（不包含当前节点子节点）。
	 * 返回：	xml节点，当前节点的浅拷贝对象
	 */
	this.toElement = function(){
		return this.node.cloneNode(false);
	}
	/*
	 * 获取当前节点的XML节点对象的xml字符串
	 * 返回：	xml字符串，当前节点的浅拷贝对象的xml字符串
	 */
	this.toString = function(){
		return this.toElement().xml;
	}
	/*
	 * 获取节点属性字符串
	 * 返回：	属性字符串
	 */
	this.getAttribute = function(name){
		return this.node.getAttribute(name);
	}
	/*
	 * 设置节点属性字符串
	 */
	this.setAttribute = function(name, value){
        //2007-5-31 防止抛错
        if(null==value){
            value = "";
        }
		if(name == _TREE_XML_NODE_ATTRIBUTE_ID){	//修改id
			this.setId(value);
		}else if(name == _TREE_XML_NODE_ATTRIBUTE_NAME){	//修改name
			this.setName(value);
		}else if(name == _TREE_XML_NODE_ATTRIBUTE_FULLNAME){ //修改fullname
			this.setFullName(value);
		}else if(name == _TREE_XML_NODE_ATTRIBUTE_DISPLAY){	//修改display
			if(value == 1){
				this.enabled();
			}else{
				this.disabled();
			}
		}else if(name == _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE){ //修改checkType
			this.setSelectedState(value);
		}else if(name == _TREE_XML_NODE_ATTRIBUTE_CANSELECTED){ //修改canSelected
			this.setCanSelected(value);
		}else{	//修改其他属性
			this.node.setAttribute(name, value);
		}
	}
	/*
	 * 使用一段合法的xml字符串更新该节点的所有属性信息。
	 */
	this.setAttrbutesByXmlStr = function(xml){
		var newNodeXML = loadXmlToNode(xml);
		if(newNodeXML == null || newNodeXML.documentElement == null){
			return;
		}
		var attributes = newNodeXML.documentElement.attributes;
		for(var i = 0, len = attributes.length; i < len; i++){
			this.setAttribute(attributes[i].name, attributes[i].value);
		}
	}
	/*
	 * 函数说明：刷新页面显示
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.reload = function () {
		displayObj.reload();
	}
	/*
	 * 根据给定的节点的选中状态，刷新相应节点的选中状态
	 * 参数：	TreeNode节点对象
	 *			noChildren	选中节点时，只选中自己节点，不影响子节点
	 */
	function refreshStatesByNode(treeNode, noChildren){
		treeObj.refreshStates(treeNode, noChildren);
	}
	/*
	 * 函数说明：打开因此节点关闭而关闭的节点，即子节点本身是打开的，只是此节点关闭才不显示的
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-7-5
	 */
	function openChildNodesCloseByThisNode(node) {
		var childOpenedBranchNodes = node.selectNodes(".//" + _TREE_XML_NODE_NAME + "[@_closeBy = '" + node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID) + "']");
		for(var i = 0, len = childOpenedBranchNodes.length; i < len; i++){
			childOpenedBranchNodes[i].setAttribute("_open", "true");
			childOpenedBranchNodes[i].removeAttribute("_closeBy");	//去除因父节点关闭而不显示的标记
		}
	}
	/*
	 * 函数说明：关闭此节点下已经打开的子节点，即此节点关闭的话，打开的字节点也应关闭
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-7-5
	 */
	function closeOpendChildNodes(node) {
		var childOpenedBranchNodes = node.selectNodes(".//" + _TREE_XML_NODE_NAME + "[@_open = 'true']");
		for(var i = 0, len = childOpenedBranchNodes.length; i < len; i++){
			childOpenedBranchNodes[i].setAttribute("_open", "false");
			childOpenedBranchNodes[i].setAttribute("_closeBy", node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_ID));	//因此节点关闭而不显示
		}
	}
	/*
	 * 函数说明：激活节点，触发相应事件
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-27
	 */
	function justActive(treeNode){
		//设置节点为激活
		treeObj.setActiveNode(treeNode);
		//触发激活节点事件
		var eventObj = createEventObject();
		eventObj.treeNode = treeNode;
		eventObj.type="_Active"
		eventNodeActived.fire(eventObj);
	}

	/*
	 * 函数说明：选中节点，触发相应事件
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-27
	 */
	function justSelected(treeNode, state, noChildren, noFireChangeEvent){
        if(false==treeObj.isMenu()){
            if(state == 1 && noChildren && treeNode.node.hasChildNodes()){
                setNodeState(treeNode.node, 2);
            }else{
                setNodeState(treeNode.node, state);
            }
            refreshStatesByNode(treeNode, noChildren);		//刷新相应节点的选中状态
            if(state == 1){	//选中节点，触发选中节点事件
                var eventObj = createEventObject();
                eventObj.treeNode = treeNode;
                eventObj.type="_Selected"
                eventNodeSelected.fire(eventObj);
            }
            if(!noFireChangeEvent){
                var eventObj = createEventObject();
                eventObj.treeNode = treeNode;
                eventObj.type = "_Change";
                eventTreeChange.fire(eventObj);
            }
        }
	}
}