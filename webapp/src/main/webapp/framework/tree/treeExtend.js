
/* 扩展内容复选框类型样式  */
var _TREE_EXTEND_CHECK_ICON_STYLE  = "extendCheckIcon"
var _TREE_EXTEND_CHECK_LABEL_STYLE = "extendCheckLabel";

/* options相关节点名称 */
var _TREE_XML_OPTIONS_NODE_NAME = "options";
var _TREE_XML_OPTION_NODE_NAME  = "option";
var _TREE_XML_OPTION_ID_NODE_NAME = "operationId";
var _TREE_XML_OPTION_TEXT_NODE_NAME = "operationName";
var _TREE_XML_OPTION_DEPEND_ID_NODE_NAME = "dependId";

/* option节点type类型 */
var _OPTION_TYPE_SINGLE = "single";
var _OPTION_TYPE_MULTI  = "multi";

/* 节点属性名称 */
var _TREE_XML_NODE_ATTRIBUTE_MODIFY = "_modify";

/* 权限选项图标路径文件名前缀  */
var _EXTEND_NODE_ICON_IMAGE_SRC = "images/multistate";

/* 标题行高度（主要是显示权限选项列名用） */
var _TREE_HEAD_HEIGHT = 20;


function $ET(treeId, dataXML) {
	var tree = TreeCache.get(treeId);
	if( tree == null || dataXML ) {
		var element = $$(treeId);

		dataXML = (typeof(dataXML) == 'string') ? dataXML : dataXML.toXml();
		element._dataXML = dataXML;
 
		tree = new ExtendTree(element);
		
		TreeCache.add(element.id, tree);
	}

	return tree;
}

var ExtendTree = function(element) {
	SingleCheckTree.call(this, element);

	var _options = [];


	this.init();
	this.setOptions();
}


function Tree() {
 
	/*
	 * 设置option节点集合
	 * 参数：	
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-27
	 */
	this.setOptions = function() {
		if( this.getXmlRoot() ) {
			_options = this.getXmlRoot().selectNodes("./" + _TREE_XML_OPTIONS_NODE_NAME + "/"+_TREE_XML_OPTION_NODE_NAME);

            //增加被依赖项节点方便反向查询
            this.setDependedId();
		}	
	}
	/*
	 * 设置option节点被依赖项
	 * 参数：	
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-27
	 */
	this.setDependedId = function() {
        var depended = {};
		for(var i=0,iLen=_options.length;i<iLen;i++) {
            var tempOption = _options[i];
            var tempId = tempOption.selectSingleNode("./operationId").text;
            var tempDependIds = tempOption.selectSingleNode("./dependId").text.replace(/^\s*|\s*$/g,"");
            if(""!=tempDependIds) {
                tempDependIds = tempDependIds.split(",");
                for(var j=0,jLen=tempDependIds.length;j<jLen;j++) {
                    var tempDependId = tempDependIds[j];
                    if(null==depended[tempDependId]) {
                        depended[tempDependId] = [];
                    }
                    depended[tempDependId][depended[tempDependId].length] = tempId;
                }
            }
        }
        for(var tempDependId in depended) {
            var tempDependOption = this.getOptionById(tempDependId);
            var node = _xmlRoot.ownerDocument.createElement("dependedId");
            node.text = depended[tempDependId].join(",");
            tempDependOption.appendChild(node);
        }
	}
	/*
	 * 获取option节点集合
	 * 参数：	
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-27
	 */
	this.getOptions = function() {
		return _options;
	}
	/*
	 * 获取指定option节点
	 * 参数：	string:id       operationId
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-11-10
	 */
	this.getOptionById = function(id) {
        var tempOption;        
		if(null!=_xmlRoot) {
			tempOption = _xmlRoot.selectSingleNode("./" + _TREE_XML_OPTIONS_NODE_NAME + "/"+_TREE_XML_OPTION_NODE_NAME + "[./operationId='" + id + "']");
		}
		return tempOption;
	}
}



TreeNode {

	/*
	 * 改变扩展内容项选中状态为下一状态
	 * 参数：	id	                        扩展项id
                boolean:shiftKey            是否同时按下shiftKey
                string:nextState            指定状态(可选)
	 * 返回：	object:returnObj	        包含成员flag和state
	 */
	this.changeExtendSelectedState = function(id,shiftKey,nextState) {
        var curState = this.getAttribute(id);
        var pState = this.getAttribute("pstate");

        //不能超过pState
        if(null!=nextState) {
            if("2"==nextState && "2"!=pState) {
                nextState = "1";
            }        
        }

        if("3"==curState || "4"==curState) {//当前若是禁用状态则不变
            nextState = curState;
        }else if(null==nextState) {//如果没有指定下一状态，则自动切换
            switch(curState||"") {            
                case "0":
                case "":
                    nextState = "1";
                    break;
                case "1":
                    if("2"==pState) {
                        nextState = "2";
                    }else{
                        nextState = "0";
                    }
                    break;
                case "2":
                    nextState = "0";
                    break;
            }        
        }

        var returnObj = {};
        var flag = this.setExtendSelectedState(id,shiftKey,nextState);
        if(true!=flag) {//调用语句取消该事件
            returnObj = {flag:flag,state:curState};
        }else{
            returnObj = {flag:flag,state:nextState};
        }


        return returnObj;
	}
	/*
	 * 设置扩展内容项选中状态
	 * 参数：	string:state	选择状态
	 *			string:id       id
	 */
	this.setExtendSelectedState = function(id,shiftKey,nextState) {
		var flag = true;
		var defaultValue = this.getAttribute(id);

		var eventObj = createEventObject();
		eventObj.treeNode = this;
		eventObj.returnValue = true;
        eventObj.optionId = id;
		eventObj.defaultValue = defaultValue||"0";
		eventObj.newValue = nextState;
        eventObj.shiftKey = shiftKey;
		eventObj.type = "_ExtendNodeChange";
		eventExtendNodeChange.fire(eventObj);
		if(false==eventObj.returnValue) {//调用语句取消该事件
			flag = false;
		}else{
            var oldState = this.getAttribute(id);
            if(nextState!=oldState) {
                this.setAttribute(id,nextState);
                this.setAttribute(_TREE_XML_NODE_ATTRIBUTE_MODIFY,"1");
            }
        }
		return flag;
	}
}

Event {
	var eventExtendNodeChange = new EventFirer("onExtendNodeChange");

}



ExtendDisplay {

	var _frozenWidth = 100;
	var _exHScrollBox;
	var _exHScrollDiv;
	var _extendBox;
	var _extendTable;
	var _extendHeadBox;
	var _extendHeadTable;
	var _ExtendRows = new Array(_pageSize);

	this.init = function() {
 
 
		createExtendScrollElement();
 
		createExtendTableElement();
		createExtendHeadTableElement();

 
		setExtendScrollElementSize();
 
		setExtendTableElementSize();
		setExtendHeadTableElementSize();

	}


	/*
	 * 确定冻结部分宽度
	 */
	function setFrozenWidth() {
		var totalColumns = treeObj.getOptions().length;
		if(totalColumns > 0) {
			_frozenWidth = 250;
		} else {
			_frozenWidth = _windowWidth;
		}
	}
	/*
	 * 获取冻结(或非冻结)部分宽度
	 * 参数：   boolean:frozen          是否要获取冻结部分(默认true)
	 * 返回值： number:frozenWidth       冻结部分宽度
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function getFrozenWidth(frozen) {
        if(false!=frozen) {
            //return _windowWidth * _frozenWidth;//比例系数形式
            return _frozenWidth;
        }else{
            //return Math.max(1,_windowWidth * (1-_frozenWidth));//比例系数形式
            return Math.max(1,_windowWidth - _frozenWidth);
        }
	}

		/*
	 * 设置滚动条的大小
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-9
	 */
	function setScrollElementSize() {
		setScrollBoxSize();

		_vScrollDiv.style.height = (_totalTreeNodesNum - _pageSize) * _rowHeight + _windowHeight;
		setScrollDivSize();
	}
	/*
	 * 设置滚动条占位器的大小
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function setScrollDivSize() {
		var maxWidth = _rootTable.offsetWidth;
		for(var i=0;i<_rootTable.rows.length;i++) {
			maxWidth = Math.max(maxWidth,_rootTable.rows[i].cells[0].offsetWidth);
		}
		_hScrollDiv.style.width = maxWidth; 
	}
	/*
	 * 设置滚动条容器的大小
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function setScrollBoxSize() {
        var frozenWidth = getFrozenWidth(true);
        var unfrozenWidth = getFrozenWidth(false);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize) {
			_vScrollBox.style.display = 'block';
			_hScrollBox.style.width = frozenWidth;
		}else{
			_vScrollBox.style.display = 'none';
            if(0<_options.length) {
                _hScrollBox.style.width = frozenWidth;            
            }else{
                _hScrollBox.style.width = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
            }
		}
		if(_rootTable.offsetWidth > frozenWidth || _extendTable.offsetWidth>unfrozenWidth) {
			_hScrollBox.style.display = 'block';
			_vScrollBox.style.height = _windowHeight;
		}else{
			_hScrollBox.style.display = 'none';
			_vScrollBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
		}
	}

	
	/*
	 * 生成扩展内容滚动条
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function createExtendScrollElement() {
		var hScrollStr = '<div id="treeExHScrollBox" style="position:absolute;overflow-x:auto;overflow-y:hidden;heigth:17px;width:100%;bottom:0px;left:0px;"><div id="treeExHScrollDiv" style="height:1px"></div></div>';
		element.insertAdjacentHTML('afterBegin', hScrollStr);
		_exHScrollBox = element.all("treeExHScrollBox");
		_exHScrollDiv = element.all("treeExHScrollDiv");
		_exHScrollBox.onscroll = onExHScroll;
	}
	/*
	 * 设置滚动条的大小
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function setExtendScrollElementSize() {
		setExtendScrollBoxPosition();
		setExtendScrollBoxSize();
		setExtendScrollDivSize();
	}
	/*
	 * 设置滚动条容器的位置
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function setExtendScrollBoxPosition() {
        var frozenWidth = getFrozenWidth(true);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize || 0<_options.length) {
			_exHScrollBox.style.left = frozenWidth;
		}else{
			_exHScrollBox.style.left = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
		}
	}
	/*
	 * 设置滚动条容器的大小
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function setExtendScrollBoxSize() {
		_exHScrollBox.style.width = getFrozenWidth(false);
	}
	/*
	 * 设置滚动条占位器的大小
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function setExtendScrollDivSize() {
		_exHScrollDiv.style.width = _extendTable.offsetWidth;
	}
	/*
	 * 刷新扩展内容滚动条是否可见
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function refreshExtendScrollVisible() {
		var visible = false;
		if(0<treeObj.getOptions().length) {
			visible = true;
		}
		_exHScrollBox.style.visibility = visible?"visible":"hidden";
	}

		/*
	 * 生成页面上显示节点的table对象。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-7
	 */
	function createTableElement() {
		var tableStr = '<div id="treeRootBox" style="position:absolute;overflow:hidden;top:'+_TREE_HEAD_HEIGHT+'px;left:0px;"><table id="treeRootTable" cellspacing="0"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_rootBox = element.all("treeRootBox");
		_rootTable = element.all("treeRootTable");
		createTableRows(0,_pageSize);
	}
	/*
	 * 生成页面上显示节点的table的各行对象。
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-9
	 */
	function createTableRows(begin,end) {
		for(var i = begin; i < end; i++) {
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
        var frozenWidth = getFrozenWidth(true);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize || 0<_options.length) {
            _rootBox.style.width = frozenWidth;
		}else{
			_rootBox.style.width = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
		}
		if(_rootTable.offsetWidth > frozenWidth) {
			_rootBox.style.height = _windowHeight;
		}else{
			_rootBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
		}
	}
	/*
	 * 生成页面上显示节点扩展内容的表头对象。
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function createExtendHeadTableElement() {
		var tableStr = '<div id="treeExtendHeadBox" style="position:absolute;top:0px;height:'+_TREE_HEAD_HEIGHT+'px;left:0px;overflow:hidden;"><table id="treeExtendHeadTable" border="1" class="extendTable"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_extendHeadBox = element.all("treeExtendHeadBox");
		_extendHeadTable = element.all("treeExtendHeadTable");
		createExtendHeadTableRows();
	}
	/*
	 * 生成页面上显示节点扩展内容的表头的行对象。
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function createExtendHeadTableRows() {
        if(0<_extendHeadTable.rows.length) {
            _extendHeadTable.deleteRow(0);
        }
		var tr = _extendHeadTable.insertRow();
        var _options = treeObj.getOptions();
        for(var i=0,iLen=_options.length;i<iLen;i++) {
            var td = tr.insertCell();
            var text = _options[i].selectSingleNode(_TREE_XML_OPTION_TEXT_NODE_NAME).text;
            td.innerHTML = "<nobr>" + text + "</nobr>";
            td.align = "center";
            td.title = text;
        }
	}
	/*
	 * 设置显示节点扩展内容的表头对象的大小
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function setExtendHeadTableElementSize() {
        var frozenWidth = getFrozenWidth(true);
        var unfrozenWidth = getFrozenWidth(false);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize || 0<_options.length) {
			_extendHeadBox.style.left = frozenWidth;
			_extendHeadBox.style.width = unfrozenWidth;
		}else{
			_extendHeadBox.style.left = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
			_extendHeadBox.style.width = unfrozenWidth;
		}
	}
	/*
	 * 生成页面上显示节点扩展内容的table对象。
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function createExtendTableElement() {
		var tableStr = '<div id="treeExtendBox" style="position:absolute;top:'+_TREE_HEAD_HEIGHT+'px;left:0px;overflow:hidden;"><table id="treeExtendTable" border="1" class="extendTable"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_extendBox = element.all("treeExtendBox");
		_extendTable = element.all("treeExtendTable");
		createExtendTableRows(0,_pageSize);
	}
	/*
	 * 生成页面上显示节点扩展内容的table的各行对象。
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function createExtendTableRows(begin,end) {
		for(var i = begin; i < end; i++) {
			var tr = _extendTable.insertRow();
			tr.insertCell();
			_ExtendRows[i] = new ExtendRow(tr);
		}
	}
	/*
	 * 设置显示节点扩展内容的table对象的大小
	 * 参数：
	 * 返回值：
	 * 作者：毛云
	 * 时间：2006-4-8
	 */
	function setExtendTableElementSize() {
        var frozenWidth = getFrozenWidth(true);
        var unfrozenWidth = getFrozenWidth(false);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize || 0<_options.length) {
			_extendBox.style.left = frozenWidth;
			_extendBox.style.width = unfrozenWidth;
		}else{
			_extendBox.style.left = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
			_extendBox.style.width = unfrozenWidth;
		}
		if(_rootTable.offsetWidth > frozenWidth) {
			_extendBox.style.height = _windowHeight;
		}else{
			_extendBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
		}
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
				_ExtendRows[i].setXmlNode(_totalTreeNodes[nodeIndex]);
			}else{
				_Rows[i].setXmlNode();
				_ExtendRows[i].setXmlNode();
			}
		}

		setFrozenWidth();
		setExtendScrollDivSize();
        createExtendHeadTableRows();
        setExtendHeadTableElementSize();

		refreshUI();

		var et = new Date();
		window.status=et-st;
	}

		/*
	 * 根据页面上的行数，获取相应的ExtendRow对象
	 * 参数：	index	行序号
	 * 返回值：	Row对象/null
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getExtendRowByIndex = function (index) {
		if(index >= _pageSize || index < 0) {
			alert("Display对象：行序号[" + index + "]超出允许范围[0 - " + _pageSize + "]！");
			return null;
		}
		return _ExtendRows[index];
	}

		/*
	 * 横向滚动事件
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function onExHScroll() {
		_extendBox.scrollLeft = this.scrollLeft;
		_extendHeadBox.scrollLeft = this.scrollLeft;
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

			var tempWindowHeight = Math.max(element.offsetHeight - _TREE_SCROLL_BAR_WIDTH - _TREE_HEAD_HEIGHT, _TREE_BOX_MIN_HEIGHT);
			var tempWindowWidth = Math.max(element.offsetWidth - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_WIDTH);
			if(_windowHeight!=tempWindowHeight || _windowWidth!=tempWindowWidth) {
				_windowHeight = tempWindowHeight;
				_windowWidth = tempWindowWidth;
			}else{
				//触发前后尺寸无变化
				return ;
			}
				
			var pageSize = Math.floor(_windowHeight / _rowHeight);
			setScrollBoxSize();
			setExtendScrollElementSize();

			setTableElementSize();
			setExtendTableElementSize();
            setExtendHeadTableElementSize();

			//2005-9-8 修正尺寸变化时行数显示错误问题
			if(pageSize > _pageSize) {//高度增加时

				_Rows = new Array(pageSize);
				_ExtendRows = new Array(pageSize);

				createTableRows(_pageSize,pageSize);
				createExtendTableRows(_pageSize,pageSize);

				for(var i = 0; i < _pageSize; i++) {
					var tr = _rootTable.rows[i];
					_Rows[i] = new Row(tr);

					var tr = _extendTable.rows[i];
					_ExtendRows[i] = new ExtendRow(tr);
				}
				_pageSize = pageSize;
				refresh();

			}else if(pageSize < _pageSize) {//高度减少时

				_Rows = new Array(pageSize);
				_ExtendRows = new Array(pageSize);

				for(var i = 0; i < pageSize; i++) {
					var tr = _rootTable.rows[i];
					_Rows[i] = new Row(tr);

					var tr = _extendTable.rows[i];
					_ExtendRows[i] = new ExtendRow(tr);
				}
				for(var i = pageSize; i < _pageSize; i++) {
					_rootTable.deleteRow(pageSize);
					_extendTable.deleteRow(pageSize);
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
        setScrollBoxSize();
        setExtendScrollBoxPosition();
        setExtendScrollBoxSize();
        setTableElementSize();
        setExtendTableElementSize();
        setExtendHeadTableElementSize();

		//同步横向滚动条的大小
		setScrollDivSize();

		refreshExtendScrollVisible();
	}
}










function _onclick() {
	var eventObj = window.event.srcElement;
	window.event.returnValue = false;

	var _treeBox = element.all("treeRootBox")
	var _extendBox = element.all("treeExtendBox");

	//树节点部分
    if(true==_treeBox.contains(eventObj)) {

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
		}else if(eventObj == row.getLabel()) {
			if(treeObj.isChangeFolderStateByClickLabel()) {
				//2006-4-22 只有当枝节点才允许执行
				if(treeNode.node.hasChildNodes()) {
					//点击节点文字时，改变节点伸缩状态
					treeNode.changeFolderState();
				}
			}
			treeNode.setActive(window.event.shiftKey);		//激活节点
		}
    //扩展项部分
	}else if(true==_extendBox.contains(eventObj)) {
		var row = getExtendRow(eventObj);
		if(row instanceof ExtendRow) {
			var treeNode = instanceTreeNode(row.getXmlNode());
		}
		if(!(row instanceof ExtendRow) || !(treeNode instanceof TreeNode)) {
			return;
		}
		var returnObj = treeNode.changeExtendSelectedState(eventObj.id,event.shiftKey);
	}
	displayObj.reload();
}


//////////////////////////////////////////////////////////////////
//	对象名称：ExtendRow	 												//
//	职责：	负责页面上tr对象中显示节点。							//
//			只要给定一个xml节点，此对象负责将节点显示到对应的tr中。		//
//////////////////////////////////////////////////////////////////

/*
 * 初始化ExtendRow对象
 * 参数：	tr	tr的Dom对象
 * 返回值：
 * 作者：毛云
 * 时间：2006-4-8
 */
function instanceExtendRow(tr) {
    return new ExtendRow(tr);
}
/*
 * 对象说明：封装树节点显示在屏幕上的一个tr对象
 * 参数：	tr	tr的Dom对象
 * 返回值：
 * 作者：毛云
 * 时间：2006-4-8
 */
function ExtendRow(tr) {

    this.row = tr;
    this.row.height = _TREE_NODE_DISPLAY_ROW_HEIGHT;

    this.cells = tr.cells;
    
    this.node = null;
}

ExtendRow.prototype = new function () {
    /*
     * 重新设定相关xml节点
     * 参数：	node	树节点的xml节点
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-8
     */
    this.setXmlNode = function (node) {
        if(node == null) {
            for(var i=0,iLen=this.cells.length;i<iLen;i++) {
                this.cells[i].innerHTML = "";
            }
			this.node = null;

            return;
        }
        this.node = node;
        this.createCells();
        this.setCells();
    }
    /*
     * 获取显示节点的xml对象
     * 参数：	
     * 返回值：	xml节点
     * 作者：毛云
     * 时间：2006-4-8
     */
    this.getXmlNode = function () {
        return this.node;
    }
    /*
     * 创建扩展内容的所有列
     * 参数：	node	xml节点
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.createCells = function() {
        var curColumns = this.cells.length;
        var totalColumns = treeObj.getOptions().length;
        if(totalColumns>curColumns) {
            for(var i=curColumns;i<totalColumns;i++) {
                this.row.insertCell();
            }
        }else{
            for(var i=totalColumns;i<curColumns;i++) {
                this.row.deleteCell(totalColumns);
            }
        }
    }
    /*
     * 设定扩展内容各列内容
     * 参数：	node	xml节点
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.setCells = function() {
        var _options = treeObj.getOptions();
        for(var i=0,iLen=_options.length;i<iLen;i++) {
            var curOption = _options[i];
            var curOptionID = curOption.selectSingleNode(_TREE_XML_OPTION_ID_NODE_NAME).text;
            var curNodeAttr = this.node.getAttribute(curOptionID);

            //无属性则设置默认值0
            if(null==curNodeAttr) {
                this.node.setAttribute(curOptionID,"0");
            }

            this.setCell(i,curNodeAttr,curOption);
        }
    }
    /*
     * 设定扩展内容空列内容
     * 参数：	cellIndex	td序号
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.setEmptyCell = function(cellIndex) {
        if(this.cells[cellIndex].innerText!=" ") {
            this.cells[cellIndex].innerText = " ";
        }
    }
    /*
     * 设定扩展内容单列内容
     * 参数：	cellIndex	td序号
     *			value		xml节点
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.setCell = function(cellIndex,value,option) {

        var curOptionIDNode = option.selectSingleNode(_TREE_XML_OPTION_ID_NODE_NAME);
        var curOptionTextNode = option.selectSingleNode(_TREE_XML_OPTION_TEXT_NODE_NAME);
        var curOptionDependIDNode = option.selectSingleNode(_TREE_XML_OPTION_DEPEND_ID_NODE_NAME);

        var curOptionID = curOptionIDNode.text;
        var curOptionText = curOptionTextNode.text;
        var curOptionDependID = null==curOptionDependIDNode?"":curOptionDependIDNode.text;

        this.setCellCheckbox(cellIndex);
        this.setCellCheckboxSrc(cellIndex,value,curOptionID,curOptionDependID);
        //this.setCellCheckboxLabel(cellIndex,curOptionText);
    }
    /*
     * 设定扩展内容checkbox列内容
     * 参数：	cellIndex   td序号
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.setCellCheckbox = function(cellIndex) {
        var nobr = this.cells[cellIndex].firstChild;
        if(null==nobr || "NOBR"!=nobr.nodeName.toUpperCase()) {
            this.cells[cellIndex].innerText = "";

            var nobr = this.getCloneCellCheckbox();
            this.cells[cellIndex].appendChild(nobr);

            this.cells[cellIndex].align = "center";
        }
    }
    /*
     * 加速创建扩展内容checkbox列内容，获取副本对象
     * 参数：	
     * 返回值：nobr		html对象
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.getCloneCellCheckbox = function() {
        if(window._tempCloneCellCheckbox==null) {		
            var nobr = createObjByTagName("nobr");

            var checkType = createObjByTagName("img", _TREE_EXTEND_CHECK_ICON_STYLE);
            checkType.align = "absmiddle";
            nobr.appendChild(checkType);

            var checkTypeLabel = createObjByTagName("span", _TREE_EXTEND_CHECK_LABEL_STYLE);
            nobr.appendChild(checkTypeLabel);

            window._tempCloneCellCheckbox = nobr;
        }
        return window._tempCloneCellCheckbox.cloneNode(true);
    }
    /*
     * 设定扩展内容checkbox图片地址
     * 参数：	cellIndex	td序号
     *			value       选中状态
                id          对应option的id，每列唯一
                dependId    依赖option的name，用于同时选中
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.setCellCheckboxSrc = function(cellIndex,value,id,dependId) {
        var nobr = this.cells[cellIndex].firstChild;
        var treeNode = new TreeNode(this.node);

        var checkType = nobr.firstChild;
        checkType.id = id;
        checkType.type = _OPTION_TYPE_MULTI;
        checkType.state = value;

        // 0 未选中
        // 1 仅此节点有权限
        // 2 所有子节点有权限
        // 3 未选中禁用
        // 4 选中禁用
        value = value||"0";
        if("0"==value) {
            var checkedChild = this.node.selectSingleNode(".//" + _TREE_XML_NODE_NAME + "[@" + id + "='1' or @" + id + "='2' or @" + id + "='4']");
            if(null!=checkedChild) {
                value = "0_2";
            }
        }

        var src = treeObj.getAttribute(_TREE_ATTRIBUTE_BASE_URL) + _EXTEND_NODE_ICON_IMAGE_SRC + value + ".gif";
        checkType.src = src;
    }
    /*
     * 设定扩展内容checkbox文字说明
     * 参数：	cellIndex	td序号
     *			extendNode	xml节点
     * 返回值：
     * 作者：毛云
     * 时间：2006-4-9
     */
    this.setCellCheckboxLabel = function(cellIndex,label) {
        var nobr = this.cells[cellIndex].firstChild;

        var checkType = nobr.firstChild;
        var checkTypeLabel = checkType.nextSibling;

        checkTypeLabel.innerText = label;
        checkTypeLabel.title = label;
    }
    /*
     * 创建页面显示的元素
     * 参数：	name	对象标记名(小写)
     *			className	样式类型名
     * 返回值：页面元素对象
     * 作者：毛云
     * 时间：2006-4-8
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

/* 
 * public方法：根据显示的对象，获取相应的ExtendRow对象
 * 参数：	obj	 节点显示在页面上的扩展内容对象
 * 返回值：	ExtendRow对象
 */
function getExtendRow(display, obj) {
	if(!/^(a|img)$/.test(obj.tagName.toLowerCase())) {
		return null;
	}

	try{
		var index = getRowIndex(obj);
	} catch(e) {
		return null;
	}
	return display.getExtendRowByIndex(index);
}