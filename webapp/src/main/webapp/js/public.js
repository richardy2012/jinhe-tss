/*
 *	函数说明：tab页工作区初始化
 *	参数：	
 */
function initWorkSpace(hide) {
	ws.onTabClose = function(event) {
		var tab = event.tab;
		detachReminder(tab.SID); // 解除提醒
	}

	if(false != hide) {
		ws.onTabCloseAll = function(event) {
			hideWorkSpace();
		}
		ws.onTabChange = function(event) {
			var fromTab = event.lastTab;
			var toTab = event.tab;
			showWorkSpace();
		}
	}
}

/*
 *	函数说明：隐藏tab页工作区
 *	参数：	
 *	返回值：
 */
function hideWorkSpace() {
	var ws = $("ws");
	var tr = ws.parentNode.parentNode;
	tr.style.display = "none";
	tr.previousSibling.style.display = "none";    
}
/*
 *	函数说明：显示tab页工作区
 *	参数：	
 *	返回值：
 */
function showWorkSpace() {
	var ws = $("ws");
	var tr = ws.parentNode.parentNode;
	tr.style.display = "";
	tr.previousSibling.style.display = "";
}





/*
 *	函数说明：左栏添加拖动效果
 *	参数：	
 *	返回值：
 */
function initPaletteResize() {
	var palette = $("palette");
	Element.attachColResize(palette, -1);
}
/*
 *	函数说明：右上栏添加拖动效果
 *	参数：	
 *	返回值：
 */
function initListContainerResize() {
	var listContainer = $("listContainer");
	Element.attachRowResize(listContainer, 8);
}
/*
 *	函数说明：用户信息初始化
 *	参数：	
 *	返回值：
 */
function initUserInfo() {
	var p = new HttpRequestParams();
	p.url = "ums/user!getOperatorInfo.action";
	p.setHeader("appCode", APP_CODE);
	p.setHeader("anonymous", "true");

	var request = new HttpRequest(p);
	request.onresult = function() {
		var userName = this.getNodeValue("name");

		var userInfoObj = $("userInfo");
		userInfoObj.innerText = userName;
	}
	request.send();   
}
/*
 *	函数说明：工具条初始化
 *	参数：	
 *	返回值：
 */
function initToolBar() {
	var tbObj = $("toolbar");
	toolbar = ToolBars.create(tbObj);
}






/*
 *	函数说明：点击树刷新按钮
 *	参数：	
 *	返回值：
 */
function onClickTreeBtRefresh() {
	loadInitData();
}
/*
 *	函数说明：点击树标题按钮
 *	参数：	
 *	返回值：
 */
function onClickTreeTitleBt() {
	var treeTitleObj = $("treeTitle");
	var statusTitleObj = $("statusTitle");

	var block = Blocks.getBlock("treeContainer");
	if(null!=block) {
		block.switchTo();
	}
	if(true==block.visible) {
		treeTitleObj.firstChild.className = "opened";
		statusTitleObj.firstChild.className = "opened";

		var block = Blocks.getBlock("statusContainer");
		if(null!=block) {
			block.show();
		}
	}else{
		treeTitleObj.firstChild.className = "closed";
		statusTitleObj.firstChild.className = "opened";

		var block = Blocks.getBlock("statusContainer");
		if(null!=block) {
			block.show(false);
		}
	}
}
/*
 *	函数说明：点击状态栏标题按钮
 *	参数：	
 *	返回值：
 */
function onClickStatusTitleBt() {
	var treeTitleObj = $("treeTitle");
	var statusTitleObj = $("statusTitle");

	var block = Blocks.getBlock("statusContainer");
	if(null!=block) {
		block.switchTo();
	}
	if(true==block.visible) {
		statusTitleObj.firstChild.className = "opened";        
	}else{
		statusTitleObj.firstChild.className = "closed";

		var block = Blocks.getBlock("treeContainer");
		if(null!=block && true!=block.visible) {
			treeTitleObj.firstChild.className = "opened";

			var block = Blocks.getBlock("treeContainer");
			if(null!=block) {
				block.show();
			}
		}
	}
}
/*
 *	函数说明：点击左栏控制按钮
 *	参数：	
 *	返回值：
 */
function onClickPaletteBt() {
	var paletteBtObj = $("paletteBt");

	var block = Blocks.getBlock("palette");
	if(null!=block) {
		block.switchTo();
	}
	if(false==block.visible) {
		paletteBtObj.className = "iconClosed";
	}else{
		paletteBtObj.className = "icon";
	}
}
/*
 *	函数说明：点击树标题
 *	参数：	
 *	返回值：
 */
function onClickTreeTitle() {
	var treeTitleObj = $("treeTitle");
	Focus.focus(treeTitleObj.firstChild.id);
}
/*
 *	函数说明：点击状态栏标题
 *	参数：	
 *	返回值：
 */
function onClickStatusTitle() {
	var statusTitleObj = $("statusTitle");
	Focus.focus(statusTitleObj.firstChild.id);
}
/*
 *	函数说明：点击grid标题
 *	参数：	
 *	返回值：
 */
function onClickGridTitle() {
	Focus.focus("gridTitle");
}




/*
 *	函数说明：删除缓存(公用)
 *	参数：	string:cacheID      缓存数据id
			boolean:flag        是否清除关联的XML数据
 *	返回值：
 */
function delCacheData(cacheID,flag) {
	var cacheData = Cache.Variables.get(cacheID);
	Cache.Variables.del(cacheID);

	//2007-1-18
	if(false!=flag) {
		for(var i=0;null!=cacheData && i<cacheData.length;i++) {
			Cache.XmlIslands.del(cacheData[i]);
		}
	}
}



/*
 *	函数说明：检测右键菜单项是否可见
 *	参数：	string:code     操作码
 *	返回值：
 */
function getOperation(code) {
	var flag = false;
	var treeObj = $("tree");
	var treeNode = treeObj.getActiveTreeNode();
	if(null!=treeNode) {
		var _operation = treeNode.getAttribute("_operation");
		flag = checkOperation(code,_operation);
	}
	return flag;
}
/*
 *	函数说明：检测操作权限
 *	参数：	string:code             操作码
			string:_operation       权限
 *	返回值：
 */
function checkOperation(code,_operation) {
	var flag = false;
	if("string"==typeof(code) && "string"==typeof(_operation)) {
		var reg = new RegExp("(^"+code+",)|(^"+code+"$)|(,"+code+",)|(,"+code+"$)","gi");
		flag = reg.test(_operation);
	}
	return flag;
}





/*
 *	函数说明：获取树节点属性
 *	参数：	string:name         属性名
 *	返回值：string:value        属性值
 */
function getTreeAttribute(name) {
	var value = null;
	var treeObj = $("tree");
	var treeNode = treeObj.getActiveTreeNode();
	if(null!=treeNode) {
		value = treeNode.getAttribute(name);
	}
	return value;   
}
/*
 *	函数说明：修改树节点属性
 *	参数：  string:id               树节点id
			string:attrName         属性名
			string:attrValue        属性值
			string:refresh          是否刷新树
 *	返回值：
 */
function modifyTreeNode(id,attrName,attrValue,refresh) {
	var treeObj = $("tree");
	var treeNode = treeObj.getTreeNodeById(id);
	if(null!=treeNode) {
		treeNode.setAttribute(attrName,attrValue);
	}
	if(false!=refresh) {
		treeObj.reload();
	}
}
/*
 *	函数说明：添加子节点
 *	参数：	string:id           树节点id
			XmlNode:xmlNode     XmlNode实例
 *	返回值：
 */
function appendTreeNode(id,xmlNode) {
	var treeObj = $("tree");
	var treeNode = treeObj.getTreeNodeById(id);
	if(null!=treeNode && null!=xmlNode) {
		treeObj.insertTreeNodeXml(xmlNode.toXml(),treeNode);
	}
}
/*
 *	函数说明：获取树全部节点id数组
 *	参数：	XmlNode:xmlNode         XmlNode实例
			string:xpath            选取节点xpath
 *	返回值：Array:Ids               节点id数组
 */
function getTreeNodeIds(xmlNode,xpath) {
	  var Ids = [];
	  var treeNodes = xmlNode.selectNodes(xpath);
	  for(var i=0,iLen=treeNodes.length;i<iLen;i++) {
		  var curNode = treeNodes[i];
		  var id = curNode.getAttribute("id");
		  if(null!=id) {
			  Ids.push(id);
		  }
	  }
	  return Ids;
}
/*
 *	函数说明：树节点定位
 *	参数：	Element:treeObj         tree控件
			Element:keywordObj      关键字输入框
 *	返回值：
 */
function searchTree(treeObj,keywordObj) {
	//覆盖树控件搜索出错信息提示方法
	var tempAlert = window.alert;
	window.alert = function(str) {
		var balloon = Balloons.create(str);
		balloon.dockTo(keywordObj);
	}

	if(false!=treeObj.research) {
		var keyword = treeObj.keyword;
		treeObj.searchNode(keyword, "name", "hazy", "down");
		treeObj.research = false;
	}else{
		treeObj.searchNext("down", true);
	}

	//还原信息提示方法
	window.alert = tempAlert;
}
/*
 *	函数说明：树节点定位
 *	参数：	Element:treeObj         tree控件
			Element:btObj           搜索按钮
			Element:keywordObj      关键字输入框
 *	返回值：
 */
function attachSearchTree(treeObj,btObj,keywordObj) {
	//设置搜索按钮操作
	btObj.onclick = function() {
		searchTree(treeObj,keywordObj);
	}

	//设置搜索关键字操作
	keywordObj.value = "";
	keywordObj.onchange = function() {
		treeObj.research = true;
		treeObj.keyword = this.value;
	}
	keywordObj.onchange();    
}
/*
 *	函数说明：清除tree数据
 *	参数：	Element:treeObj         tree控件对象
 *	返回值：
 */
function clearTreeData(treeObj) {
	var xmlReader = new XmlReader("<actionSet/>");
	var emptyNode = new XmlNode(xmlReader.documentElement);
	treeObj.load(emptyNode.node);
	treeObj.research = true;
}    
/*
 *	函数说明：删除树选中节点
 *	参数：	Element:treeObj         tree控件对象
			Array:exceptIds         例外的id
 *	返回值：
 */
function removeTreeNode(treeObj,exceptIds) {
	var selectedNodes = treeObj.getSelectedTreeNode();
	if(null==exceptIds) {
		exceptIds = ["_rootId"];
	}

	for(var i=0,iLen=selectedNodes.length;i<iLen;i++) {
		var curNode = selectedNodes[i];
		var id = curNode.getId();

		var flag = true;
		for(var j=0,jLen=exceptIds.length;j<jLen;j++) {
			if(id==exceptIds[j]) {
				flag = false;
				break;
			}
		}
		if(true==flag) {
			treeObj.removeTreeNode(curNode);
		}
	}
}
/*
 *	函数说明：将树选中节点添加到另一树中(注：过滤重复id节点，并且结果树只有一层结构)
 *	参数：	Element:fromTreeObj         树控件
			Element:toTreeObj           树控件
			Function:checkFunction      检测单个节点是否允许添加
 *	返回值：
 */
function addTreeNode(fromTreeObj,toTreeObj,checkFunction) {
	var selectedNodes = fromTreeObj.getSelectedTreeNode(false);

	var reload = false;
	for(var i=0,iLen=selectedNodes.length;i<iLen;i++) {
		var curNode = selectedNodes[i];

		//2007-5-23 过滤不可选择的节点
		var canselected = curNode.getAttribute("canselected");
		if("0"==canselected) {
			continue;
		}


		curNode.setSelectedState(0,true,true);

		if(null!=checkFunction) {
			var result = checkFunction(curNode);
			if(null!=result && true==result.error) {

				if(null!=result.message) {
					//显示错误信息
					var balloon = Balloons.create(result.message);
					balloon.dockTo(toTreeObj);
				}

				if(true==result.stop) {
					return;
				}else{
					continue;
				}
			}
		}

		var groupName = curNode.getName();
		var id = curNode.getId();

		var sameAttributeTreeNode = hasSameAttributeTreeNode(toTreeObj,"id",id);
		if("_rootId"!=id && false==sameAttributeTreeNode) {
			//至少有一行添加才刷新grid
			reload = true;

			var treeNode = toTreeObj.getTreeNodeById("_rootId");
			if(null!=treeNode) {
				//排除子节点
				var cloneNode = new XmlNode(curNode.node).cloneNode(false);
				toTreeObj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
			}
		}
	}
	if(true==reload) {
		toTreeObj.reload();
	}
	fromTreeObj.reload();
}
/*
 *	函数说明：检测是否有相同属性节点
 *	参数：	Element:treeObj         tree控件对象
			string:attrName         属性名
			string:attrValue        属性值
 *	返回值：
 */
function hasSameAttributeTreeNode(treeObj,attrName,attrValue) {
	var flag = new Boolean(false);
	var xmlIsland = treeObj.getTreeNodeById("_rootId").node;
	var treeNode = xmlIsland.selectSingleNode(".//treeNode[@"+attrName+"='"+attrValue+"']");
	if(null!=treeNode) {
		flag = new Boolean(true);
		flag.treeNode = treeNode;
	}
	return flag;
}
/*
 *	函数说明：显示当前树节点信息
 *	参数：	
 *	返回值：
 */
function showTreeNodeStatus(params) {
	var treeObj = $("tree");
	var treeNode = treeObj.getActiveTreeNode();
	if(null!=treeNode) {

		var id = treeNode.getId();

		var block = Blocks.getBlock("statusContainer");
		if(null!=block && "_rootId"!=id) {
			block.open();

			for(var item in params) {
				var name = params[item];
				var val = treeNode.getAttribute(item);
				block.writeln(name,val);                
			}

			block.close();
		}
		if("_rootId"==id) {
		   block.hide();
		}
	}
}



/*
 *	函数说明：初始化翻页工具条
 *	参数：	object:toolbarObj       工具条对象
			XmlNode:xmlIsland       XmlNode实例
			function:callback       回调函数
 *	返回值：
 */
function initGridToolBar(toolbarObj, xmlIsland, callback) {
	//初始化
	toolbarObj.init = function() {
		this.clear();
		this.create();
		this.attachEvents();
	}
	
	//清空内容
	toolbarObj.clear = function() {
		this.innerHTML = "";
	}
	
	//创建按钮
	toolbarObj.create = function() {
		var totalpages = toolbarObj.getTotalPages();
		var curPage = toolbarObj.getCurrentPage();

		var str = [];
		str[str.length] = "<span class=\"button refresh\" id=\"GridBtRefresh\" title=\"刷新\"></span>";
		str[str.length] = "<span class=\"button first\"   id=\"GridBtFirst\"   title=\"第一页\"></span>";
		str[str.length] = "<span class=\"button prev\"    id=\"GridBtPrev\"    title=\"上一页\"></span>";
		str[str.length] = "<span class=\"button next\"    id=\"GridBtNext\"    title=\"下一页\"></span>";
		str[str.length] = "<span class=\"button last\"    id=\"GridBtLast\"    title=\"最后一页\"></span>";
		
		str[str.length] = "<select id=\"GridPageList\">";
		for(var i=0; i <= totalpages; i++) {
			str[str.length] = "  <option value=\"" + i + "\"" + (curPage == i ? " selected" : "") + ">" + i + "</option>";
		}
		str[str.length] = "</select>";

		this.innerHTML = str.join("");
	}
	
	//绑定事件
	toolbarObj.attachEvents = function() {
		var gridBtRefreshObj = $("GridBtRefresh");
		var gridBtFirstObj   = $("GridBtFirst");
		var gridBtPrevObj    = $("GridBtPrev");
		var gridBtNextObj    = $("GridBtNext");
		var gridBtLastObj    = $("GridBtLast");
		var gridPageListObj  = $("GridPageList");

		Event.attachEvent(gridBtRefreshObj, "click", function() {
			var curPage = toolbarObj.getCurrentPage();
			toolbarObj.gotoPage(curPage);
		});
		Event.attachEvent(gridBtFirstObj, "click", function() {
			toolbarObj.gotoPage("1");
		});
		Event.attachEvent(gridBtLastObj, "click", function() {
			var lastpage = toolbarObj.getLastPage();
			toolbarObj.gotoPage(lastpage);
		});
		Event.attachEvent(gridBtNextObj, "click", function() {
			var curPage = toolbarObj.getCurrentPage();
			var lastpage = toolbarObj.getLastPage();
			var page = lastpage;
			if(curPage < lastpage) {
				page = curPage + 1;
			}
			toolbarObj.gotoPage(page);
		});
		Event.attachEvent(gridBtPrevObj, "click", function() {
			var curPage = toolbarObj.getCurrentPage();
			var page = 1;
			if(curPage > 1) {
				page = curPage - 1;
			}
			toolbarObj.gotoPage(page);
		});
		Event.attachEvent(gridPageListObj, "change", function() {
			toolbarObj.gotoPage(gridPageListObj.value);
		});
	}
	
	//获取当前页码
	toolbarObj.getCurrentPage = function() {
		var currentpage = xmlIsland.getAttribute("currentpage");
		if(null == currentpage) {
			currentpage = 1;
		} else {
			currentpage = parseInt(currentpage);
		}
		return currentpage;
	}
	
	//获取最后一页页码
	toolbarObj.getLastPage = function() {
		var lastpage = this.getTotalPages();
		if(null == lastpage) {
			lastpage = 1;
		} else {
			lastpage = parseInt(lastpage);
		}
		return lastpage;
	}
	
	//获取总页码
	toolbarObj.getTotalPages = function() {
		var totalpages = xmlIsland.getAttribute("totalpages");
		if(null == totalpages) {
			totalpages = 1;
		} else {
			totalpages = parseInt(totalpages);
		}
		return totalpages;
	}
	
	//转到指定页
	toolbarObj.gotoPage = function(page) {
		callback(page);
	}
	
	toolbarObj.init();
}





/*
 *	函数说明：禁止点击按钮
 */
function disableButton(btObj) {
	btObj.disabled = true;
}
/*
 *	函数说明：允许点击按钮：
 */
function enableButton(btObj) {
	btObj.disabled = false;
}
/*
 *	函数说明：同步按钮禁止/允许状态
 */
function syncButton(btObjs, request) {
	for(var i=0; i < btObjs.length; i++) {
		disableButton(btObjs[i]);
	}

	request.ondata = function() {
		for(var i=0; i < btObjs.length; i++) {
			enableButton(btObjs[i]);
		}
	}
}



/*
 *	函数说明：初始化导航条
 *	参数：	string:curId       当前菜单项id
 */
function initNaviBar(curId) {	
	var isModule = (window.location.href.indexOf("module") > 0);
	var relativePath = isModule ? "../../../" : "../";

	var p = new HttpRequestParams();
	p.url = relativePath + "navi.xml";

	var request = new HttpRequest(p);
	request.onresult = function() {
		var data = this.getNodeValue("NaviInfo");

		var str = [];
		var menuItems = data.selectNodes("MenuItem");
		for(var i=0; i < menuItems.length; i++) {
			var menuItem = menuItems[i];
			var id   = menuItem.getAttribute("id");
			var href = menuItem.getAttribute("href");
			var name = menuItem.getAttribute("name");

			if( false == /^javascript\:/.test(href) ) {
				href = relativePath + href;
			}
			
			var cssStyle = (curId == id) ? "naviActive" : "navi";
			str[str.length] = "<a href=\"" + href + "\" class=\"" + cssStyle + "\">" + name + "</a>";
		}
		$("navibar").innerHTML = str.join(" ");
	}
	request.send();
}

/*
 *	函数说明：清除树节点操作权限
 *	参数：	xmlNode:treeNode                XmlNode实例
			boolean:clearChildren           是否清除子节点
 */
function clearOperation(treeNode, clearChildren) {
	treeNode.removeAttribute("_operation");

	if(false != clearChildren) {
		var childs = treeNode.selectNodes(".//treeNode");
		for(var i=0,iLen=childs.length;i<iLen;i++) {
			childs[i].removeAttribute("_operation");
		}
	}
}



/*
 *	函数说明：检查密码强度
 *	参数：	object:formObj                  xform对象
			string:url                      请求地址
			string:password                 密码
			string:loginName                登录名
 */
function checkPasswordSecurityLevel(formObj, url, password, loginName) {
	var p = new HttpRequestParams();
	p.url = url;
	p.setHeader("appCode", APP_CODE);
	p.setContent("password", password);
	p.setContent("loginName", loginName);

	var request = new HttpRequest(p);
	request.onresult = function(error) {
		var securityLevel = this.getNodeValue(XML_SECURITY_LEVEL);
		formObj.securityLevel = securityLevel;

		showPasswordSecurityLevel(formObj);
	}
	request.onsuccess = function() {
		formObj.securityLevel = null;
	}
	request.send();
}

/*
 *	函数说明：显示密码强度提示信息
 *	参数：	object:formObj                  xform对象
 *	返回值：
 */
function showPasswordSecurityLevel(formObj) {
	var errorInfo = {
		0: "您输入的密码安全等级为\"不可用\"，不安全",
		1: "您输入的密码安全等级为\"低\"，只能保障基本安全",
		2: "您输入的密码安全等级为\"中\"，较安全",
		3: "您输入的密码安全等级为\"高\"，很安全"
	};
	formObj.showCustomErrorInfo("password", errorInfo[formObj.securityLevel]);
}


