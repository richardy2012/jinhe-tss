
/* 扩展内容复选框类型样式  */
var _TREE_EXTEND_CHECK_ICON_STYLE  = "extendCheckIcon"
var _TREE_EXTEND_CHECK_LABEL_STYLE = "extendCheckLabel";

/* options相关节点名称 */
var _TREE_OPTIONS_NODE = "options";
var _TREE_OPTION_NODE  = "option";
var _TREE_OPTION_ID_NODE = "operationId";
var _TREE_OPTION_TEXT_NODE = "operationName";
var _TREE_OPTION_DEPENDID_NODE = "dependId";

/* option节点type类型 */
var _OPTION_TYPE_SINGLE = "single";
var _OPTION_TYPE_MULTI  = "multi";

/* 节点属性名称 */
var _TREE_NODE_ATTRIBUTE_MODIFY = "_modify";

/* 权限选项图标路径文件名前缀  */
var _EXTEND_NODE_ICON = "images/multistate";

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

	this.getOptions = function() {
		return _options;
	}

	this.getOptionById = function(id) {
		if(this.getXmlRoot()) {
			return this.getXmlRoot().selectSingleNode("./" + _TREE_OPTIONS_NODE + "/" + _TREE_OPTION_NODE + "[./operationId='" + id + "']");
		}
		return null;
	}

	/*
	 * 设置option节点集合
	 */
	this.setOptions = function() {
		if( this.getXmlRoot() ) {
			_options = this.getXmlRoot().selectNodes("./" + _TREE_OPTIONS_NODE + "/" + _TREE_OPTION_NODE);

            // 增加被依赖项节点方便反向查询
            var dependedMap = {};
			for(var i=0; i < _options.length; i++) {
				var curOption = _options[i];
				var operationId = curOption.selectSingleNode("./operationId").text;
				var dependIds = curOption.selectSingleNode("./dependId").text.replace(/^\s*|\s*$/g, "");

				dependIds = dependIds.split(",");
				for(var j=0; j < dependIds.length; j++) {
					var dependId = dependIds[j];
					if( dependedMap[dependId] == null ) {
						dependedMap[dependId] = [];
					}
					dependedMap[dependId][dependedMap[dependId].length] = operationId;
				}
			}
			for(var dependId in dependedMap) {
				var option = this.getOptionById(dependId);
				var node = this.getXmlRoot().ownerDocument.createElement("dependedId");
				node.text = dependedMap[dependId].join(",");
				option.appendChild(node);
			}
		}	
	}

	this.init();
	this.setOptions();
}
 
/*
 * 改变权限项选中状态为下一状态
 * 参数：	id	                        权限项id
			boolean: shiftKey           是否同时按下shiftKey
			string: nextState           指定状态(可选)
 * 返回：	object
 */
TreeNode.prototype.changeExtendSelectedState = function(id, shiftKey, nextState) {
	var curState = this.getAttribute(id);
	var pState = this.getAttribute("pstate");

	// 不能超过pState
	if("2" == nextState && "2" != pState) {
		nextState = "1";
	}        

	if("3"==curState || "4"==curState) { // 当前若是禁用状态则不变
		nextState = curState;
	}
	else if(null == nextState) { // 如果没有指定下一状态，则自动切换
		switch(curState || "") {            
			case "0":
			case "":
				nextState = "1";
				break;
			case "1":
				if("2" == pState) {
					nextState = "2";
				} else {
					nextState = "0";
				}
				break;
			case "2":
				nextState = "0";
				break;
		}        
	}

	var flag = true;
	var defaultValue = this.getAttribute(id);

	var eventObj = createEventObject();
	eventObj.treeNode = this;
	eventObj.returnValue = true;
	eventObj.optionId = id;
	eventObj.defaultValue = defaultValue || "0";
	eventObj.newValue = nextState;
	eventObj.shiftKey = shiftKey;
	eventObj.type = "_ExtendNodeChange";
	eventExtendNodeChange.fire(eventObj);

	if(false == eventObj.returnValue) { 
		flag = false; // 调用语句取消该事件
	}
	else {
		var oldState = this.getAttribute(id);
		if(nextState != oldState) {
			this.setAttribute(id, nextState);
			this.setAttribute(_TREE_NODE_ATTRIBUTE_MODIFY, "1");
		}
	}

	return {flag:flag, state: flag ? nextState : curState};
}

//////////////////////////////////////////////////////////////////
//	对象名称：ExtendRow	 											//
//	职责：	负责页面上tr对象中显示节点。							//
//////////////////////////////////////////////////////////////////
 
/*
 * 对象说明：封装树节点显示在屏幕上的一个tr对象
 * 参数：	tr	tr的Dom对象
 */
function ExtendRow(tr, treeObj) {
	this.treeObj = treeObj;
    this.row = tr;
    this.row.height = _TREE_NODE_DISPLAY_ROW_HEIGHT;

    this.cells = tr.cells;
    
    this.node = null;
}

ExtendRow.prototype = new function () {
    /*
     * 重新设定相关xml节点
     * 参数：	node	树节点的xml节点
     */
    this.setXmlNode = function (node) {
        if(node == null) {
            for(var i=0; i < this.cells.length; i++) {
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
     */
    this.getXmlNode = function () {
        return this.node;
    }
    /*
     * 创建扩展内容的所有列
     */
    this.createCells = function() {
        var curColumns = this.cells.length;
        var totalColumns = this.treeObj.getOptions().length;
        if(totalColumns > curColumns) {
            for(var i = curColumns; i < totalColumns; i++) {
                this.row.insertCell();
            }
        } else {
            for(var i=totalColumns; i < curColumns; i++) {
                this.row.deleteCell(totalColumns);
            }
        }
    }
    /*
     * 设定扩展内容各列内容
     */
    this.setCells = function() {
        var _options = this.treeObj.getOptions();
        for(var i=0; i < _options.length; i++) {
            var curOption   = _options[i];
            var curOptionID = curOption.selectSingleNode(_TREE_OPTION_ID_NODE).text;
            var curNodeAttr = this.node.getAttribute(curOptionID);

            // 无属性则设置默认值0
            if(null == curNodeAttr) {
                this.node.setAttribute(curOptionID, "0");
            }

            this.setCell(i, curNodeAttr, curOption);
        }
    }
    /*
     * 设定扩展内容空列内容
     * 参数：	cellIndex	td序号
     */
    this.setEmptyCell = function(cellIndex) {
        this.cells[cellIndex].innerText = " ";
    }
    /*
     * 设定扩展内容单列内容
	 * 参数：	cellIndex	td序号
     *			value       选中状态
	 *			               0 未选中
	 *			               1 仅此节点有权限
	 *			               2 所有子节点有权限
	 *			               3 未选中禁用
	 *			               4 选中禁用
     */
    this.setCell = function(cellIndex, value, option) {
		// 设定扩展内容checkbox列内容
		var cell = this.cells[cellIndex];
        var nobr = cell.firstChild;
        if( null == nobr || "NOBR" != nobr.nodeName.toUpperCase()) {
            cell.innerText = "";
            cell.appendChild(this.getCloneCellCheckbox());
            cell.align = "center";
        }
		
		// 设定扩展内容checkbox图片地址
		var optionId  = option.selectSingleNode(_TREE_OPTION_ID_NODE).text; // 对应option的id，每列唯一
	
		var dependIDNode = option.selectSingleNode(_TREE_OPTION_DEPENDID_NODE);
        var dpependId  = dependIDNode ? dependIDNode.text : "";  // 依赖option的name，用于同时选中
        
        var treeNode = new TreeNode(this.node);
        var checkType = nobr.firstChild;
        checkType.id = optionId;
        checkType.type = _OPTION_TYPE_MULTI;
        checkType.state = value;

        value = value || "0";
        if("0" == value ) {
            var checkedChild = this.node.selectSingleNode(".//treeNode[@" + optionId + "='1' or @" + optionId + "='2' or @" + optionId + "='4']");
            if( checkedChild ) {
                value = "0_2";
            }
        }

        checkType.src = this.treeObj._baseUrl + _EXTEND_NODE_ICON + value + ".gif";;
    }
 
    /*
     * 加速创建扩展内容checkbox列内容，获取副本对象
     */
    this.getCloneCellCheckbox = function() {
        if(window._tempCloneCellCheckbox == null) {		
            var checkType = Element.createElement("img", _TREE_EXTEND_CHECK_ICON_STYLE);
            checkType.align = "absmiddle";
			// var checkTypeLabel = Element.createElement("span", _TREE_EXTEND_CHECK_LABEL_STYLE);

			var nobr = Element.createElement("nobr");
            nobr.appendChild(checkType);
           // nobr.appendChild(checkTypeLabel);

            window._tempCloneCellCheckbox = nobr;
        }
        return window._tempCloneCellCheckbox.cloneNode(true); // 缓存起来
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


	/* 确定冻结部分宽度 */
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
	 * 参数：   boolean:frozen         是否要获取冻结部分(默认true)
	 * 返回值： number:frozenWidth     冻结部分宽度
	 */
	function getFrozenWidth(frozen) {
        if( frozen ) {
            return Math.max(1, _windowWidth - _frozenWidth);
        } 
		return _frozenWidth;
	}

	/* 设置滚动条的大小 */
	function setScrollElementSize() {
		setScrollBoxSize();

		_vScrollDiv.style.height = (_totalTreeNodesNum - _pageSize) * _rowHeight + _windowHeight;
		setScrollDivSize();
	}
	
	/* 设置滚动条占位器的大小 */
	function setScrollDivSize() {
		var maxWidth = _rootTable.offsetWidth;
		for(var i=0; i < _rootTable.rows.length; i++) {
			maxWidth = Math.max(maxWidth, _rootTable.rows[i].cells[0].offsetWidth);
		}
		_hScrollDiv.style.width = maxWidth; 
	}
	
	/* 设置滚动条容器的大小  */
	function setScrollBoxSize() {
        var frozenWidth = getFrozenWidth(true);
        var unfrozenWidth = getFrozenWidth(false);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize) {
			_vScrollBox.style.display = 'block';
			_hScrollBox.style.width = frozenWidth;
		}
		else {
			_vScrollBox.style.display = 'none';
			_hScrollBox.style.width = frozenWidth + (0 < _options.length ? 0 : _TREE_SCROLL_BAR_WIDTH);
		}
		if(_rootTable.offsetWidth > frozenWidth || _extendTable.offsetWidth > unfrozenWidth) {
			_hScrollBox.style.display = 'block';
			_vScrollBox.style.height = _windowHeight;
		} else {
			_hScrollBox.style.display = 'none';
			_vScrollBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
		}
	}
 	
	/* 生成扩展内容滚动条 */
	function createExtendScrollElement() {
		var hScrollStr = '<div id="treeExHScrollBox" style="position:absolute;overflow-x:auto;overflow-y:hidden;heigth:17px;width:100%;bottom:0px;left:0px;"><div id="treeExHScrollDiv" style="height:1px"></div></div>';
		element.insertAdjacentHTML('afterBegin', hScrollStr);
		_exHScrollBox = element.all("treeExHScrollBox");
		_exHScrollDiv = element.all("treeExHScrollDiv");
		_exHScrollBox.onscroll = onExHScroll;
	}
 
	function setExtendScrollElementSize() {
		setExtendScrollBoxPosition();
		setExtendScrollBoxSize();
		setExtendScrollDivSize();
	}
 
	function setExtendScrollBoxPosition() {
        var frozenWidth = getFrozenWidth(true);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize || 0 < _options.length) {
			_exHScrollBox.style.left = frozenWidth;
		} else {
			_exHScrollBox.style.left = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
		}
	}
 
	function setExtendScrollBoxSize() {
		_exHScrollBox.style.width = getFrozenWidth(false);
	}
 
	function setExtendScrollDivSize() {
		_exHScrollDiv.style.width = _extendTable.offsetWidth;
	}
	
	/* 刷新扩展内容滚动条是否可见 */
	function refreshExtendScrollVisible() {
		var visible = false;
		if(0 < treeObj.getOptions().length) {
			visible = true;
		}
		_exHScrollBox.style.visibility = visible ? "visible" : "hidden";
	}

	/* 生成页面上显示节点的table对象。 */
	function createTableElement() {
		var tableStr = '<div id="treeRootBox" style="position:absolute;overflow:hidden;top:'+_TREE_HEAD_HEIGHT+'px;left:0px;"><table id="treeRootTable" cellspacing="0"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_rootBox = element.all("treeRootBox");
		_rootTable = element.all("treeRootTable");
		createTableRows(0,_pageSize);
	}
	
	/* 生成页面上显示节点的table的各行对象。 */
	function createTableRows(begin,end) {
		for(var i = begin; i < end; i++) {
			var tr = _rootTable.insertRow();
			tr.insertCell();
			_Rows[i] = new Row(tr);
		}
	}

	/* 设置显示节点的table对象的大小 */
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
	
	/* 生成页面上显示节点扩展内容的表头对象。 */
	function createExtendHeadTableElement() {
		var tableStr = '<div id="treeExtendHeadBox" style="position:absolute;top:0px;height:'+_TREE_HEAD_HEIGHT+'px;left:0px;overflow:hidden;"><table id="treeExtendHeadTable" border="1" class="extendTable"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_extendHeadBox = element.all("treeExtendHeadBox");
		_extendHeadTable = element.all("treeExtendHeadTable");
		createExtendHeadTableRows();
	}
	
	/* 生成页面上显示节点扩展内容的表头的行对象。*/
	function createExtendHeadTableRows() {
        if(0 < _extendHeadTable.rows.length) {
            _extendHeadTable.deleteRow(0);
        }
		var tr = _extendHeadTable.insertRow();
        var _options = treeObj.getOptions();
        for(var i=0; i < _options.length; i++) {
            var td = tr.insertCell();
            var text = _options[i].selectSingleNode(_TREE_OPTION_TEXT_NODE).text;
            td.innerHTML = "<nobr>" + text + "</nobr>";
            td.align = "center";
            td.title = text;
        }
	}
	
	/* 设置显示节点扩展内容的表头对象的大小 */
	function setExtendHeadTableElementSize() {
        var frozenWidth = getFrozenWidth(true);
        var unfrozenWidth = getFrozenWidth(false);
        var _options = treeObj.getOptions();

		if(_totalTreeNodesNum > _pageSize || 0 < _options.length) {
			_extendHeadBox.style.left = frozenWidth;
			_extendHeadBox.style.width = unfrozenWidth;
		} else {
			_extendHeadBox.style.left = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
			_extendHeadBox.style.width = unfrozenWidth;
		}
	}
	
	/* 生成页面上显示节点扩展内容的table对象。 */
	function createExtendTableElement() {
		var tableStr = '<div id="treeExtendBox" style="position:absolute;top:'+_TREE_HEAD_HEIGHT+'px;left:0px;overflow:hidden;"><table id="treeExtendTable" border="1" class="extendTable"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_extendBox = element.all("treeExtendBox");
		_extendTable = element.all("treeExtendTable");
		createExtendTableRows(0,_pageSize);
	}
	
	/* 生成页面上显示节点扩展内容的table的各行对象。 */
	function createExtendTableRows(begin,end) {
		for(var i = begin; i < end; i++) {
			var tr = _extendTable.insertRow();
			tr.insertCell();
			_ExtendRows[i] = new ExtendRow(tr);
		}
	}
	
	/* 设置显示节点扩展内容的table对象的大小 */
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
	
	/* 根据滚动状态，显示可视范围内的树节点。
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



$$("treeExtendBox").onclick = function() {
	var eventObj = window.event.srcElement;
	window.event.returnValue = false;
 
	var row = getExtendRow(eventObj);
	if(row instanceof ExtendRow) {
		var treeNode = instanceTreeNode(row.getXmlNode());
		if((row instanceof ExtendRow)  && (treeNode instanceof TreeNode)) {
			treeNode.changeExtendSelectedState(eventObj.id, event.shiftKey);
		}
	} 
}


