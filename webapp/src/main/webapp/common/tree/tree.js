//////////////////////////////////////////////////////////////////
//	对象名称：Tree												//
//	职责：	负责处理由于树类型引起的不同之处，同时记录树的全局变量。	//
//////////////////////////////////////////////////////////////////

/*
 * 函数说明：初始化树对象
 * 参数：
 * 返回值：	Tree对象
 * 作者：scq
 * 时间：2004-6-24
 */
function instanceTree() {
	var type = getValue(_TREE_ATTRIBUTE_TREE_TYPE, _TREE_TYPE_SINGLE);
	treeObj = new Tree();
	if(type == _TREE_TYPE_MULTI){
		treeObj.setAttribute(_TREE_ATTRIBUTE_TREE_TYPE, _TREE_TYPE_MULTI);
		treeObj.setAttribute(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, getValue(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE_MULTI_DEFAULT_VALUE));
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
	}else{
		treeObj.setAttribute(_TREE_ATTRIBUTE_TREE_TYPE, _TREE_TYPE_SINGLE);
		treeObj.setAttribute(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, getValue(_TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE, _TREE_ATTRIBUTE_SELECTED_WITH_CHANGE_STATE_SINGLE_DEFAULT_VALUE));
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
	if(type == _TREE_TYPE_MENU){
		treeObj.isMenu = function(){
			return true;
		}
	}else{
		treeObj.isMenu = function(){
			return false;
		}
	}
}
/*
 * 函数说明：树对象，处理由于控件参数不同，引起的不同之处
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-24
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
	 * 函数说明：	 设定控件的数据，数据来源为xml节点、数据岛、数据文件或xml字符串
	 * 参数：	dataSrc	数据源
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-25
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
	 * 函数说明：	 获取默认选择状态数据：xml节点、数据岛、数据文件或xml字符串
	 * 参数：	selectedSrc	节点选中状态的数据源
	 *			isClearOldSelected	是否清除原先选中节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-12-23
	 */
	this.loadSelectedData = function (selectedSrc, isClearOldSelected) {
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
	 * 函数说明：	 获取默认选择状态数据：id字符串，多id之间用","隔开
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
	 * 函数说明：设置默认激活节点
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
	 * 函数说明：获取数据的根节点
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
	 * 函数说明：设定当前高亮（激活）的节点
	 * 参数：	treeNode	TreeNode节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-24
	 */
	this.setActiveNode = function (treeNode) {
	    _activedNode = treeNode.getXmlNode();
	}
	/*
	 * 函数说明：根据属性配置，点击节点文字标签时是否改变节点选择状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-27
	 */
	this.isSelectByActived = function (){
		return _treeNodeSelectedChangeState == "true";
	}

	/*
	 * 函数说明：根据属性配置，点击节点文字标签时是否改变节点伸缩状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-27
	 */
	this.isChangeFolderStateByClickLabel = function (){
		return _treeNodeClickOpenNode == "true";
	}
	/*
	 * 函数说明：获取当前高亮（激活）的节点
	 * 参数：
	 * 返回值：	TreeNode对象
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.getActiveNode = function () {
	    return instanceTreeNode(_activedNode);
	}
	/*
	 * 函数说明：设定对象属性值
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
	 * 函数说明：获取对象属性
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
	 * 函数说明：根据节点不同的checkType属性值获取选择状态图标的地址
	 * 参数：	node	xml节点
	 * 返回值：	String	图标地址
	 * 作者：scq
	 * 时间：2004-6-24
	 */
	this.getCheckTypeImageSrc = function (node) {
	    alert("Tree对象：此方法[getCheckTypeImageSrc]尚未初始化！");
	}
	/*
	 * 函数说明：判断节点是否高亮（激活）
	 * 参数：	node	xml节点
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.isActiveNode = function (node) {
	    return _activedNode == node;
	}
	/*
	 * 函数说明：判断节点是否为被拖动的节点
	 * 参数：	node	xml节点
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.isMovedNode = function (node) {
	    return _movedNode == node;
	}
	/*
	 * 函数说明：判断节点是否为查选结果节点
	 * 参数：	node	xml节点
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2006-1-9
	 */
	this.isFindedNode = function (node) {
	    return _findedNode == node;
	}
	/*
	 * 函数说明：获取节点文字链接的样式名
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
	 * 函数说明：节点被选中时是否需要激活（高亮）节点
	 * 参数：state	节点选中状态
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-7-1
	 */
	this.isActiveBySelected = function (state) {
		return _treeNodeSelectedChangeState == "true" && state == 1;
	}
	/*
	 * 函数说明：设定被拖动的节点
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.setMovedNode = function (node) {
	    _movedNode = node;
	}
	/*
	 * 函数说明：获取节点的下一中选中状态
	 * 参数：	treeNode	TreeNode节点对象
	 * 返回值：	0/1	不选中/选中
	 * 作者：scq
	 * 时间：2004-6-28
	 */
	this.getNextState = function (treeNode) {
		alert("Tree对象：此方法[getNextState]尚未初始化！");
	}
	/*
	 * 函数说明：根据特定的节点，刷新所有节点的选择状态
	 * 参数：	node	xml节点对象
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-28
	 */
	this.refreshStates = function (node) {
		alert("Tree对象：此方法[refreshStates]尚未初始化！");
	}
	/*
	 * 函数说明：树是否可以移动节点
	 * 参数：
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.isCanMoveNode = function () {
	    return _canMoveNode == "true";
	}
	/*
	 * 函数说明：树是否禁止改变所有的选择状态
	 * 参数：
	 * 返回值：	true/false
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.isAllDisabledCheckType = function () {
	    return _allCheckTypeDisabled == "true";
	}
	/*
	 * 函数说明：获取选中的节点的TreeNode对象数组
	 * 参数：	hasHalfChecked	是否包含半选节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-30
	 */
	this.getSelectedTreeNode = function (hasHalfChecked) {
	    alert("Tree对象：此方法[getSelectedTreeNode]尚未初始化！");
	}
	/*
	 * 函数说明：禁止所有节点改变选中状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-29
	 */
	this.disable = function (){
		_allCheckTypeDisabled = "true";
	}
	/*
	 * 函数说明：允许没有被特殊指定不能选中的节点改变选中状态
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2005-10-29
	 */
	this.enable = function (){
		_allCheckTypeDisabled = "false";
	}
	/*
	 * 函数说明：新增节点后，是否需要将焦点移到新节点上
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
	 * 函数说明：设定查询结果中的当前节点为特殊高亮显示
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
 * 函数说明：获取定义的默认打开节点
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
 * 函数说明：获取控件参数
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
 * 函数说明：根据给定节点的选中状态，选中默认节点（多选树，根据xml节点）
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
 * 函数说明：根据给定节点的选中状态，选中默认节点（多选树，根据Id字符串）
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
 * 函数说明：去除所有选中节点的选中状态
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
 * 函数说明：根据给定节点的选中状态，选中默认节点（单选树）
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
 * 函数说明：根据给定节点的选中状态，选中默认节点 (多选树，选节点时不考虑父子关系)
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
 * 函数说明：根据给定节点的选中状态，选中默认节点 (多选树，选节点时考虑父子关系)
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
 * 函数说明：根据给定节点的选中状态，选中默认节点 (多选树，选节点时不考虑父子关系)
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
 * 函数说明：根据给定节点的选中状态，选中默认节点 (多选树，选节点时考虑父子关系)
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
 * 函数说明：刷新相关节点的选中状态（多选树），同时根据参数决定是否激活当前节点
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
 * 函数说明：刷新所有父节点的选择状态
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
 * 函数说明：获取枝节点的选择状态
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
 * 函数说明：刷新所有子节点
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
 * 函数说明：根据节点选择状态，获取图标地址（多选树）
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
 * 函数说明：根据节点选择状态，获取图标地址（单选树）
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
 * 函数说明：清除特定节点以外的其他节点的选中状态
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
 * 函数说明：获取选中节点的Xml对象数组（多选树）
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
 * 函数说明：根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
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
 * 函数说明：获取选中节点的TreeNode对象数组（多选树）
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
 * 函数说明：根据不同的是否包括半选状态，分别以不同的方式返回xml节点。
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
 * 函数说明：获取选中节点的TreeNode对象（单选树）
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
 * 函数说明：获取选中节点的Xml对象（单选树）
 * 参数：
 * 返回值：	Xml对象
 * 作者：scq
 * 时间：2005-8-22
 */
function singleGetSelectedXmlNode(){
	return this.getXmlRoot().selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + _TREE_XML_NODE_ATTRIBUTE_CHECKTYPE + "='1']");
}

/*
 * 函数说明：获取节点的下一选中状态（多选1、2 -> 0; 0 -> 1）
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
 * 函数说明：获取节点的下一选中状态（单选）
 * 参数：
 * 返回值：	状态1
 * 作者：scq
 * 时间：2004-6-30
 */
function singleGetNextState() {
    return 1;
}