//////////////////////////
//		  公用函数	   	//
//////////////////////////
/*
 * 判断值是否为null或空字符串
 * 参数：	value	需要判断的值
 * 返回：	true/false
 */
function isNullOrEmpty(value){
	return (value == null || (typeof(value) == 'string' && value == ""));
}
/*
 * 函数说明：判断节点是否为父节点的最后一个节点
 * 参数：node	xml节点对象
 * 返回值：true/false
 * 作者：scq
 * 时间：2004-6-7
 */
function isLastChild(node) {
	return node == node.parentNode.lastChild;
}
/*
 * 函数说明：打开默认打开节点
 * 参数：	openedNode	xml对象中需要打开的节点
 * 返回值：
 * 作者：scq
 * 时间：2004-6-11
 */
function openNode(openedNode) {
	if(openedNode == null){
		return;
	}
	while(openedNode != null){
		openedNode.setAttribute("_open", "true");
		if(openedNode == treeObj.getXmlRoot()){
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
function setNodeState(node, state){
	if(node == null){
		return;
	}
	if(null!=state){
		node.setAttribute(_TREE_XML_NODE_ATTRIBUTE_CHECKTYPE, state);	//在xml节点中标记选择状态
	}else{//2006-4-8 修正当state为null时出错
		node.removeAttribute(_TREE_XML_NODE_ATTRIBUTE_CHECKTYPE);	//在xml节点中标记选择状态
	}
}
/*
 * 函数说明：将字符串转化成xml节点对象
 * 参数：	xml	xml字符串
 * 返回值：	xml节点对象
 * 作者：scq
 * 时间：2004-6-28
 */
function loadXmlToNode(xml) {
	try{
		var newNodeXML = new ActiveXObject("MSXML.DOMDocument");
		newNodeXML.async = false;
		newNodeXML.loadXML(xml);
		return newNodeXML;
	}catch(e){
		alert("xml数据不能正常解析！[xml:" + xml + "]");
		throw(e);
		return null;
	}
}
/*
 * 获取对象在树控件可视区域中的位置（对象上边缘距可视区域上边缘的距离）
 * 参数：	objElement	对象
 * 返回：	int
 */
function getTop(objElement) {//获取对象相对于控件顶部的距离。
	var top = 0;
	var obj = objElement;
	while (obj != element) {
		top = top + obj.offsetTop;
		obj = obj.offsetParent;
	}
	return top;
}
/*
 * 函数说明：根据显示的对象，获取相应的Row对象
 * 参数：	obj	节点显示在页面上的对象
 * 返回值：	Row对象
 * 作者：scq
 * 时间：2004-6-29
 */
function getRow(obj) {
	if(!/^(a|img)$/.test(obj.tagName.toLowerCase())){
		return null;
	}
	try{
		var index = getRowIndex(obj);
	}catch(e){
		return null;
	}
	return displayObj.getRowByIndex(index);
}
/*
 * 函数说明：如果拖到页面的最上、下方，相应的滚动树
 * 参数：	obj	事件触发对象
 * 返回值：
 * 作者：scq
 * 时间：2004-6-30
 */
function startScrollTree(obj) {
    if(obj == null){
		return;
	}
	if(isLastLine(obj)){
		scrollDown();
	}
	if(isFirstLine(obj)){
		scrollUp();
	}
}
/*
 * 函数说明：定时向上滚动
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-30
 */
function scrollUp() {
	if(element.scroller != null){
		window.clearTimeout(element.scroller);
		element.scroller=null;
	}
	displayObj.scrollUp();
	element.scroller = window.setTimeout(scrollUp, _TREE_SCROLL_REPEAT_DELAY_TIME);
}
/*
 * 函数说明：定时向下滚动
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-30
 */
function scrollDown() {
	if(element.scroller != null){
		window.clearTimeout(element.scroller);
		element.scroller=null;
	}
	displayObj.scrollDown();
	element.scroller = window.setTimeout(scrollDown, _TREE_SCROLL_REPEAT_DELAY_TIME);
}

/*
 * 函数说明：如果拖到的不是页面的最上、下方，或者停止拖动，则停止滚动树
 * 参数：	obj	事件触发对象
 * 返回值：
 * 作者：scq
 * 时间：2004-6-30
 */
function stopScrollTree(obj) {
	if(obj != null && (isLastLine(obj) || isFirstLine(obj))){
		return;
	}
	if (element.scroller) {
		window.clearTimeout(element.scroller);
		element.scroller = null;
	}
}
/*
 * 函数说明：对象是否在最下面的行中
 * 参数：	odj	显示的对象
 * 返回值：	true/false
 * 作者：scq
 * 时间：2004-6-30
 */
function isLastLine(obj) {
	return getRowIndex(obj) == (displayObj.getPageSize() - 1);
}/*
 * 函数说明：对象是否在最上面的行中
 * 参数：	odj	显示的对象
 * 返回值：	true/false
 * 作者：scq
 * 时间：2004-6-30
 */
function isFirstLine(obj) {
    return getRowIndex(obj) == 0;
}
/*
 * 函数说明：获取对象所在行序号
 * 参数：	obj	对象
 * 返回值：	行序号
 * 作者：scq
 * 时间：2004-6-30
 */
function getRowIndex(obj) {
    while(obj.tagName != null && obj.tagName.toLowerCase() != "tr"){
		obj = obj.parentNode;
	}
	return obj.rowIndex;
}