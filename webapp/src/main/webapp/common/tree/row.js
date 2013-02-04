//////////////////////////////////////////////////////////////////
//	对象名称：Row	 												//
//	职责：	负责页面上tr对象中显示节点。							//
//			只要给定一个xml节点，此对象负责将节点显示到对应的tr中。		//
//////////////////////////////////////////////////////////////////

/*
 * 函数说明：初始化Row对象
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
	 * 函数说明：重新设定相关xml节点
	 * 参数：	node	树节点的xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setXmlNode = function (node) {
		if(this.nobr == null){
			this.setInnerObj();
		}
		if(node == null){
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
		if(!treeObj.isMenu()){
			this.setCheckType(treeObj.getCheckTypeImageSrc(node));
		}

		//2006-4-6 加入自定义图标
		this.setIcon(node);

		this.setLabel(node);
		this.node = node;
	}
	/*
	 * 函数说明：获取显示节点的xml对象
	 * 参数：	
	 * 返回值：	xml节点
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getXmlNode = function () {
	    return this.node;
	}
	/*
	 * 函数说明：获取页面显示的文字链接对象
	 * 参数：
	 * 返回值：	a对象
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getLabel = function () {
	    return this.label;
	}
	/*
	 * 函数说明：获取页面显示的选择状态对象
	 * 参数：
	 * 返回值：	img对象
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getCheckType = function () {
	    return this.checkType;
	}
	/*
	 * 函数说明：获取页面显示的伸缩状态对象
	 * 参数：
	 * 返回值：	img对象
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getFolder = function () {
	    return this.folder;
	}
	/*
	 * 函数说明：初始化参数（获取指向行内个对象的链接）
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

			if(!treeObj.isMenu()){
				this.checkType = this.folder.nextSibling;
				//2006-4-6 增加自定义图标
				this.icon = this.checkType.nextSibling;
				this.label = this.icon.nextSibling;
			}else{
				//2006-4-6 增加自定义图标
				this.icon = this.folder.nextSibling;
				this.label = this.icon.nextSibling;
			}
		}catch(e){
			this.nobr = createObjByTagName("nobr");
			this.row.cells[0].appendChild(this.nobr);
			this.line = this.nobr.appendChild(createObjByTagName("span"));
			this.folder = this.nobr.appendChild(createObjByTagName("img", _TREE_NODE_FOLDER_STYLE_NAME));
			if(!treeObj.isMenu()){
				this.checkType = this.nobr.appendChild(createObjByTagName("img", _TREE_NODE_CHECK_TYPE_STYLE_NAME));
			}
			//2006-4-6 增加自定义图标
			this.icon = this.nobr.appendChild(createObjByTagName("img", _TREE_NODE_ICON_STYLE_NAME));
			this.label = this.nobr.appendChild(createObjByTagName("a"));
		}
	}
	/*
	 * 函数说明：	设置制表符
	 * 参数：	htmlStr	制表符的HTML代码
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setLine = function(htmlStr){
		if(this.frontStr == htmlStr){
			return;
		}
		this.line.innerHTML = htmlStr;
		this.frontStr = htmlStr;
	}
	/*
	 * 函数说明：设置伸缩图标
	 * 参数：	node	树节点的xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setFolder = function(node){
		//2006-4-22 区分第一层树节点
		if(null!=node.parentNode && treeObj.getXmlRoot()==node.parentNode){//是第一层树节点
			if(node.hasChildNodes() || node.getAttribute("hasChild")=="1"){
				if(node.getAttribute("_open") == "true"){
					this.setFolderSrc(treeObj.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _TREE_ROOT_NODE_CONTRACT_IMAGE_SRC);
				}else{
					this.setFolderSrc(treeObj.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _TREE_ROOT_NODE_EXPAND_IMAGE_SRC);
				}
			}else{
				this.setFolderSrc(treeObj.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _TREE_ROOT_NODE_LEAF_IMAGE_SRC);
			}
		}else{
			if(node.hasChildNodes() || node.getAttribute("hasChild")=="1"){
				if(node.getAttribute("_open") == "true"){
					this.setFolderSrc(treeObj.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _TREE_NODE_CONTRACT_IMAGE_SRC);
				}else{
					this.setFolderSrc(treeObj.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _TREE_NODE_EXPAND_IMAGE_SRC);
				}
			}else{
				this.setFolderSrc(treeObj.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _TREE_NODE_LEAF_IMAGE_SRC);
			}		
		}
	}
	/*
	 * 函数说明：设定伸缩图标的地址
	 * 参数：	src	图标的地址
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setFolderSrc = function (src) {
	    if(this.folderSrc == src){
			return;
		}
		this.folder.src = src;
		this.folderSrc = src;
	}
	/*
	 * 函数说明：设定选择状态图标
	 * 参数：	imgSrc	图标地址
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setCheckType = function(imgSrc){
		if(this.checkTypeSrc == imgSrc){
			return;
		}
		this.checkType.src = imgSrc;
		this.checkTypeSrc = imgSrc;
	}
	/*
	 * 函数说明：设定文字链接
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	this.setLabel = function(node){
		var name = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_NAME);
		var fullName = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_FULLNAME);
		var canSelected = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_CANSELECTED);
		var display = node.getAttribute(_TREE_XML_NODE_ATTRIBUTE_DISPLAY);
		this.setName(name);
		if(fullName == null){
			fullName = name;
		}
		this.setTitle(fullName);
		this.setClassName(treeObj.getClassName(node));
		this.setAbled(canSelected, display);
	}
	/*
	 * 函数说明：设定文字链接的文本内容
	 * 参数：	name	文字内容
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setName = function (name){
		if(this.name == name){
			return;
		}
		this.label.innerText = name;
		this.name = name;
	}
	/*
	 * 函数说明：设定文字链接的提示信息
	 * 参数：	fullName	提示信息
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setTitle = function (fullName) {
		if(this.fullName == fullName){
			return;
		}
	    this.label.title = fullName;
		this.fullName = fullName;
	}
	/*
	 * 函数说明：设定文字链接的样式
	 * 参数：	className	节点文字链接样式名
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setClassName = function (className) {		
		if(isNullOrEmpty(className)){
			this.row.className = "";
			this.label.removeAttribute("className");
		}else if(this.className == className){
			return;
		}else{
			this.row.className = className;
			this.label.className = className;
		}
		this.className = className;
	}
	/*
	 * 函数说明：设定文字链接是否可用
	 * 参数：	canSelected	是否可以选择	0/1
	 *			display	是否正常显示	0/1
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.setAbled = function (canSelected, display) {
		if(display == '0' || canSelected == '0'){
			if(this.disabled){
				return;
			}
			this.label.setAttribute("disabled", true);
			this.disabled = true;
		}else{
			if(!this.disabled){
				return;
			}
			this.label.setAttribute("disabled", false);
			this.disabled = false;
		}
	}
	/*
	 * 函数说明：设置自定义图标
	 * 参数：	node	树节点的xml节点
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-6
	 */
	this.setIcon = function(node){
		var iconSrc = node.getAttribute(_TREE_NODE_ICON_ATTRIBUTE);
		if(null!=iconSrc && ""!=iconSrc){
			this.setIconSrc(iconSrc);
			this.icon.width = _TREE_NODE_ICON_WIDTH;
			this.icon.height = _TREE_NODE_ICON_HEIGHT;
			this.icon.style.display = "";
		}else{
			this.icon.style.display = "none";
		}
	}
	/*
	 * 函数说明：获取页面显示的图标对象
	 * 参数：
	 * 返回值：	img对象
	 * 作者：毛云
	 * 时间：2007-5-28
	 */
	this.getIcon = function () {
	    return this.icon;
	}
	/*
	 * 函数说明：设定自定义图标的地址
	 * 参数：	src	图标的地址
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-6
	 */
	this.setIconSrc = function (src) {
	    if(this.iconSrc == src){
			return;
		}
		this.icon.src = src;
		this.iconSrc = src;
	}

	/*
	 * 函数说明：获取节点前面的制表符字符串
	 * 参数：	node	节点
	 *			rootNode	根节点
	 * 返回值：	string	制表符字符串
	 * 作者：
	 * 时间：2004-6-7
	 */
	function getFrontStr(row, node, rootNode) {
		if(node.parentNode == rootNode){
			node.setAttribute("_childFrontStr", '');
			return '<span class="rootSpace"></span>';
		}
		var parentFrontStr = getParentFrontStr(row, node, rootNode);
		if(isLastChild(node)){
			node.setAttribute("_childFrontStr", parentFrontStr + '<span class="space"></span>');
			return parentFrontStr + '<span class="vHalfLine"></span>';
		}else{
			node.setAttribute("_childFrontStr", parentFrontStr + '<span class="onlyVLine"></span>');
			return parentFrontStr + '<span class="vline"></span>';
		}
	}
	/*
	 * 函数说明：获取父节点前面的制表符字符串
	 * 参数：	node	节点
	 *			rootNode	根节点
	 * 返回值：	string	制表符字符串
	 * 作者：
	 * 时间：2004-7-5
	 */
	function getParentFrontStr(row, node, rootNode) {
		if(isFirstLine(row) || node.parentNode.getAttribute("_childFrontStr") == null){
			getFrontStr(row, node.parentNode, rootNode);
		}
		return node.parentNode.getAttribute("_childFrontStr");
	}
	/*
	 * 函数说明：创建页面显示的元素
	 * 参数：	name	对象标记名(小写)
	 *			className	样式类型名
	 * 返回值：页面元素对象
	 * 作者：scq
	 * 时间：2004-6-23
	 */
	function createObjByTagName(name, className) {
   		var obj = window.document.createElement(name);
		if(className != null){
			obj.setAttribute("className", className);
		}
		if(name == "a"){
			obj.setAttribute("hideFocus", "1");
			obj.setAttribute("href", "");
		}
		return obj;
	}
	this.test = function(name){
		return eval(name);
	}
}