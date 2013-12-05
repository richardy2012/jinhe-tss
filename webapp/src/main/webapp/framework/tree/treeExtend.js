
/* 扩展内容复选框类型样式  */
var _TREE_EXTEND_CHECK_ICON_STYLE  = "extendCheckIcon"
var _TREE_EXTEND_CHECK_LABEL_STYLE = "extendCheckLabel";

/* options相关节点名称 */
var _TREE_OPTIONS_NODE = "options";
var _TREE_OPTION_NODE  = "option";
var _TREE_OPTION_ID_NODE = "operationId";
var _TREE_OPTION_TEXT_NODE = "operationName";
var _TREE_OPTION_DEPENDID_NODE = "dependId";

/* 节点属性名称 */
var _TREE_NODE_ATTRIBUTE_MODIFY = "_modify";

/* 权限选项图标路径文件名前缀  */
var _EXTEND_NODE_ICON = "images/multistate";

/* 标题行高度（主要是显示权限选项列名用） */
var _TREE_HEAD_HEIGHT = 20;


function $ET(treeId, dataXML) {
	var tree = TreeCache.get(treeId);

	if( tree == null && dataXML == null ) return;

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
	this._baseUrl    = element.getAttribute(_TREE_BASE_URL); 
	
	this.element = element;
	this.element.className = _TREE_STYLE;	

	var _treeXMLDom;
		
	this.init = function() {	
		this.loadData(this.element._dataXML);
	
		this.display = new TreeDisplay();
		this.display.resetTotalTreeNodes();
		this.display.reload();

		this.element._dataXML = "";
	}	
	
	/* 设定控件的数据，数据来源为xml字符串 */
	this.loadData = function (dataXML) {
		_treeXMLDom = loadXmlToNode(dataXML);
		if(_treeXMLDom) {			
			var openedNode = _treeXMLDom.selectSingleNode(".//treeNode[@canselected='1' or not(@canselected)]");
			var defaultOpenedNode = (openedNode ? openedNode : _treeXMLDom.firstChild);  // 默认打开第一个子节点
			openNode(defaultOpenedNode);
		}
	}
	
	/* 获取数据的根节点 */
	this.getXmlRoot = function () {
		return _treeXMLDom || loadXmlToNode("<actionSet/>");
	}
	
	this._options = [];

	this.getOptions = function() {
		return this._options;
	}

	this.getOptionById = function(id) {
		if(this.getXmlRoot()) {
			var option = this.getXmlRoot().selectSingleNode("./options/option[./operationId='" + id + "']")
			return option;
		}
		return null;
	}

	/*
	 * 设置option节点集合
	 */
	this.setOptions = function() {
		if( this.getXmlRoot() ) {
			this._options = this.getXmlRoot().selectNodes("./" + _TREE_OPTIONS_NODE + "/" + _TREE_OPTION_NODE);

            // 增加被依赖项节点方便反向查询
            var dependedMap = {};
			for(var i=0; i < this._options.length; i++) {
				var curOption = this._options[i];
				var operationId = getNodeText(curOption.selectSingleNode("./operationId"));
				var dependIds = getNodeText(curOption.selectSingleNode("./dependId")).replace(/^\s*|\s*$/g, "");
				
				if(dependIds == null || dependIds == "") continue;

				dependIds = dependIds.split(",");
				for(var j=0; j < dependIds.length; j++) {
					var dependId = dependIds[j];
					if(dependId == null || dependId == "") continue;

					if( dependedMap[dependId] == null ) {
						dependedMap[dependId] = [];
					}
					dependedMap[dependId][dependedMap[dependId].length] = operationId;
				}
			}
			for(var dependId in dependedMap) {				
				var node = this.getXmlRoot().ownerDocument.createElement("dependedId");
				node.text = dependedMap[dependId].join(",");
				
				var option = this.getOptionById(dependId);
				option.appendChild(node);
			}
		}	
	}

	/*
	 * 	鼠标单击事件响应函数
	 *			如果点击的是选择状态图标，则改变选择状态，同时根据treeNodeSelectAndActive属性，确定是否同时激活该节点。
	 *			如果点击的是伸缩状态图标，则打开或收缩当前节点的直系子节点。
	 *			如果点击的是文字连接，则激活该节点，同时根据treeNodeSelectAndActive属性，确定是否同时改变节点选择状态。
	 */
	this.element.onclick = function() {
		var srcElement = window.event.srcElement;
		preventDefault(event);

		var index = getRowIndex(srcElement);
		var row = treeThis.display.getRowByIndex(index);
		if(row instanceof Row) {
			var treeNode = instanceTreeNode(row.node, treeThis);
			if(srcElement == row.folder) {
				treeNode.changeFolderState();	//展开、收缩节点的直系子节点
			}
			treeThis.display.reload();
		}
	}


	var treeThis = this;

	/********************************  对象名称：Row *********************************************************
			  职责：负责页面上tr对象中显示节点。 只要给定一个xml节点，此对象负责将节点显示到对应的tr中
	*********************************************************************************************************/
 
	var Row = function(tr) {
		this.row = tr;

		/*
		 * 重新展示树节点
		 * 参数：	node	树节点的xml节点
		 */
		this.initRow = function (node) {
			this.node = node;
		
			if(this.nobr == null) {
				var tdCell = this.row.cells[0];
				if(tdCell.firstChild) {
					Element.removeNode(tdCell.firstChild);
				}
				this.nobr = Element.createElement("nobr");
				tdCell.appendChild(this.nobr);				
				
				this.line   = this.nobr.appendChild(Element.createElement("span"));
				this.folder = this.nobr.appendChild(Element.createElement("img", _TREE_NODE_FOLDER_STYLE));
				this.label  = this.nobr.appendChild(Element.createElement("span")); 
			}
			
			if(node == null) {
				Element.removeNode(this.nobr);			
				this.nobr = this.line = this.folder = this.label = this.node = null;
				return;
			}
			
			this.line.innerHTML = getFrontStr(this.row, node, treeThis.getXmlRoot());
			
			/* 设置伸缩图标 */
			var hasChild = node.hasChildNodes() || node.getAttribute("hasChild") == "1";
			var isOpen = node.getAttribute("_open") == "true";
			var folderImage = hasChild ? (isOpen ? _TREE_NODE_CONTRACT_IMAGE : _TREE_NODE_EXPAND_IMAGE) : _TREE_NODE_LEAF_IMAGE;;
			this.folder.src = treeThis._baseUrl  + folderImage;

			/* 设定文字链接 */
			var name = node.getAttribute(_TREE_NODE_NAME);			
			this.label.innerText = this.label.title = name;
		}

		/*
		 * 获取节点前面的制表符字符串
		 * 参数：	node	节点
		 *			rootNode	根节点
		 * 返回值：	string	制表符字符串
		 */
		function getFrontStr(row, node, rootNode) {
			if(node.parentNode == rootNode) {
				node.setAttribute("_childFrontStr", '');
				return '<span class="rootSpace"></span>';
			}
			
			if(isFirstLine(row) || node.parentNode.getAttribute("_childFrontStr") == null) {
				getFrontStr(row, node.parentNode, rootNode);
			}

			var parentFrontStr = node.parentNode.getAttribute("_childFrontStr");
			
			if( isLastChild(node) ) {
				node.setAttribute("_childFrontStr", parentFrontStr + '<span class="space"></span>');
				return parentFrontStr + '<span class="vHalfLine"></span>';
			}  

			node.setAttribute("_childFrontStr", parentFrontStr + '<span class="onlyVLine"></span>');
			return parentFrontStr + '<span class="vline"></span>';
		}
	}

	var ExtendRow = function(tr) {
	    this.row = tr;
	    this.row.style.height = _TREE_NODE_HEIGHT;

	    this.cells = tr.cells;
	    
	    this.node = null;

	    /*
	     * 重新设定相关xml节点
	     * 参数：	node	树节点的xml节点
	     */
	    this.initRow = function (node) {
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
		
	    this.getXmlNode = function () {
	        return this.node;
	    }
		
	    /* 创建扩展内容的所有列  */
	    this.createCells = function() {
	        var curColumns = this.cells.length;
	        var totalColumns = treeThis.getOptions().length;
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
		
	    /* 设定扩展内容各列内容 */
	    this.setCells = function() {
	        var _options = treeThis.getOptions();
	        for(var i=0; i < _options.length; i++) {
	            var curOption   = _options[i];
	            var curOptionID = getNodeText(curOption.selectSingleNode(_TREE_OPTION_ID_NODE));
	            var curNodeAttr = this.node.getAttribute(curOptionID);

	            // 无属性则设置默认值0
	            if(curNodeAttr == null) {
	                this.node.setAttribute(curOptionID, "0");
	            }

	            this.setCell(i, curNodeAttr, curOption);
	        }
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
				
				nobr = cell.firstChild;
	        }
			
			// 设定扩展内容checkbox图片地址
			var optionId  = getNodeText(option.selectSingleNode(_TREE_OPTION_ID_NODE)); // 对应option的id，每列唯一
		
			var dependIDNode = option.selectSingleNode(_TREE_OPTION_DEPENDID_NODE);
	        var dpependId  = dependIDNode ? getNodeText(dependIDNode) : "";  // 依赖option的name，用于同时选中
	        
	        var treeNode = new TreeNode(this.node);
	        var checkType = nobr.firstChild;
	        checkType.id = optionId;
	        checkType.state = value;

	        value = value || "0";
	        if("0" == value ) {
	            var checkedChild = this.node.selectSingleNode(".//treeNode[@" + optionId + "='1' or @" + optionId + "='2' or @" + optionId + "='4']");
	            if( checkedChild ) {
	                value = "0_2";
	            }
	        }

	        checkType.src = treeThis._baseUrl + _EXTEND_NODE_ICON + value + ".gif";;
	    }
	 
	    /* 加速创建扩展内容checkbox列内容，获取副本对象 */
	    this.getCloneCellCheckbox = function() {
	        if(window._tempCloneCellCheckbox == null) {		
	            var checkType = Element.createElement("img", _TREE_EXTEND_CHECK_ICON_STYLE);
	            checkType.align = "absmiddle";
				var nobr = Element.createElement("nobr");
	            nobr.appendChild(checkType);
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

	function TreeDisplay() {
		element.style.overflow = 'hidden'; // 溢出部分会被隐藏
		treeThis.setOptions();

		var _windowHeight = Math.max(element.offsetHeight - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_HEIGHT);
		var _windowWidth  = Math.max(element.offsetWidth  - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_WIDTH);
		var _pageSize     = Math.floor(_windowHeight / _TREE_NODE_HEIGHT);
		var _totalTreeNodes = treeThis.getXmlRoot().selectNodes(".//treeNode[../@_open='true' or @id='_rootId']");
		var _totalTreeNodesNum = _totalTreeNodes.length;
		var _startNum;
		
		var _Rows = new Array(_pageSize);
			
		element.innerHTML = "";
		
		// 生成滚动条
		var treeId = element.id;
		var _vScrollBoxName = treeId + "VScrollBox"; 
		var _vScrollDivName = treeId + "VScrollDiv"; 
		var _hScrollBoxName = treeId + "HScrollBox"; 
		var _hScrollDivName = treeId + "HScrollDiv"; 
		var _rootBoxName    = treeId + "RootBox"; 
		var _rootTableName  = treeId + "RootTable"; 

		// 生成滚动条
		var vScrollStr = '<div class="VScrollBox" id="' + _vScrollBoxName + '"><div id="' + _vScrollDivName + '" style="width:1px"></div></div>';
		var hScrollStr = '<div class="HScrollBox" id="' + _hScrollBoxName + '"><div id="' + _hScrollDivName + '" style="height:1px"></div></div>';
		element.insertAdjacentHTML('afterBegin', vScrollStr + hScrollStr);
 
		// 生成页面上显示节点的table对象 + 扩展内容。 */
		var tableStr = '<div class="RootBox" id="' + _rootBoxName + '"><table id="' + _rootTableName + '"></table></div>' +
		    '<div id="treeExtendBox" style="position:relative;display:inline-block;overflow:hidden;">' + 
		    '  <table id="treeExtendHeadTable" class="extendTable"></table></div>' +
		 	'  <table id="treeExtendTable" class="extendTable"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);

		var _vScrollBox = $$(_vScrollBoxName);
		var _vScrollDiv = $$(_vScrollDivName);
		var _hScrollBox = $$(_hScrollBoxName);
		var _hScrollDiv = $$(_hScrollDivName);
		var _rootBox   = $$(_rootBoxName);
		var _rootTable = $$(_rootTableName);
		for(var i = 0; i < _pageSize; i++) {
			var tr = _rootTable.insertRow(i);
			tr.insertCell();
			_Rows[i] = new Row(tr, treeThis);
		}
		
		/*
		 * 纵向滚动事件触发后，延时执行reload，如果第二次触发时，上次的事件还没有执行，
		 * 则取消上次事件，触发本次事件。为的是防止多次触发，屏幕抖动。
		 */
		var _scrollTimer;
		_vScrollBox.onscroll = function() {
			if (_scrollTimer) {
				window.clearTimeout(_scrollTimer);
			}
			_scrollTimer = window.setTimeout(refresh, _TREE_SCROLL_DELAY_TIME);
		};
		_vScrollBox.style.height = _windowHeight; // 设置滚动条的大小
		_vScrollDiv.style.height = (_totalTreeNodesNum - _pageSize) * _TREE_NODE_HEIGHT + _windowHeight;
		

		/* 横向滚动事件  */
		_hScrollBox.onscroll = function() {
			_rootBox.scrollLeft = this.scrollLeft;
		};
		_hScrollBox.style.width = _windowWidth;
		_hScrollDiv.style.width = _rootTable.style.width; 
		
		// 设置显示节点的table对象的大小
		_rootBox.style.height = _windowHeight;
		_rootBox.style.width  = _windowWidth;
		
		// extend ---------------------------------------------------------------------------
		var _frozenWidth = 100;
		var _ExtendRows = new Array(_pageSize);

		var _extendBox   = $$("treeExtendBox");
		var _extendHeadTable = $$("treeExtendHeadTable");
		var _extendTable = $$("treeExtendTable");
		for(var i = 0; i < _pageSize; i++) {
			var tr = _extendTable.insertRow(i);
			tr.insertCell();
			_ExtendRows[i] = new ExtendRow(tr);
		}


		createExtendHeadTableRows();
		
		setScrollBoxSize();
		setScrollDivSize();
		
		setTableElementSize();
		setExtendTableElementSize();
		
		var oThis = this;
		_extendTable.onclick = function() {
			var eventObj = window.event.srcElement;
			window.event.returnValue = false;
		 
			var row = getExtendRow(oThis, eventObj);
			if(row instanceof ExtendRow) {
				var treeNode = instanceTreeNode(row.getXmlNode());
				treeNode.changeExtendSelectedState(eventObj.id, event.shiftKey);
			} 
		}
		
		/*
		 * 获取冻结(或非冻结)部分宽度
		 * 参数：   boolean:frozen         是否要获取冻结部分(默认true)
		 * 返回值： number:frozenWidth     冻结部分宽度
		 */
		function getFrozenWidth(frozen) {
	        if( frozen ) {
	            return _frozenWidth;
	        } 
			return Math.max(1, _windowWidth - _frozenWidth);
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
	        var frozenWidth   = getFrozenWidth(true);
	        var unfrozenWidth = getFrozenWidth(false);
	        var _options = treeThis.getOptions();

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
		
		
		/* 设置显示节点的table对象的大小 */
		function setTableElementSize() {
	        var frozenWidth = getFrozenWidth(true);
	        var _options = treeThis.getOptions();

			if(_totalTreeNodesNum > _pageSize || 0 < _options.length) {
	            _rootBox.style.width = frozenWidth;
			} else {
				_rootBox.style.width = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
			}
			
			if(_rootTable.offsetWidth > frozenWidth) {
				_rootBox.style.height = _windowHeight;
			} else {
				_rootBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
			}
		}
		
		/* 生成页面上显示节点扩展内容的表头的行对象。*/
		function createExtendHeadTableRows() {
	        if(0 < _extendHeadTable.rows.length) {
	            _extendHeadTable.deleteRow(0);
	        }
			var tr = _extendHeadTable.insertRow(0);
	        var _options = treeThis.getOptions();
	        for(var i=0; i < _options.length; i++) {
	            var td = tr.insertCell();
	            var text = getNodeText(_options[i].selectSingleNode(_TREE_OPTION_TEXT_NODE));
	            td.innerHTML = "<nobr>" + text + "</nobr>";
	            td.align = "center";
	            td.title = text;
	        }
		}
 
			
		function setExtendTableElementSize() {
	        var frozenWidth = getFrozenWidth(true);
	        var unfrozenWidth = getFrozenWidth(false);
	        var _options = treeThis.getOptions();

			if(_totalTreeNodesNum > _pageSize || 0 < _options.length) {
				_extendBox.style.left = frozenWidth;
			} else {
				_extendBox.style.left  = frozenWidth + _TREE_SCROLL_BAR_WIDTH;
			}
			
			_extendBox.style.width = unfrozenWidth;
			_extendBox.style.height = _windowHeight + (_rootTable.offsetWidth > frozenWidth ? 0 : _TREE_SCROLL_BAR_WIDTH);
		}
		
		
		element.onmousewheel = function() {
			_vScrollBox.scrollTop += -Math.round(window.event.wheelDelta / 120) * _TREE_NODE_HEIGHT;
		}
	 
		/* 根据滚动状态，显示可视范围内的树节点。*/
		function refresh() {
			var startTime = new Date();
			
			if(_totalTreeNodesNum <= _pageSize) {
				_startNum = 0;
			} else {
				_startNum = Math.ceil(_vScrollBox.scrollTop  / _TREE_NODE_HEIGHT);
			}
			
			// 显示节点
			for(var i = 0; i < _pageSize; i++) {
				var nodeIndex = i + _startNum;
				if(nodeIndex < _totalTreeNodesNum) {
					_Rows[i].initRow(_totalTreeNodes[nodeIndex]);
					_ExtendRows[i].initRow(_totalTreeNodes[nodeIndex]);
				} else {
					_Rows[i].initRow();
					_ExtendRows[i].initRow();
				}
			}

			//同步横向滚动条的大小
			_hScrollDiv.style.width = _rootTable.offsetWidth;

	        createExtendHeadTableRows();
			
			refreshUI();

			window.status = new Date() - startTime;  // 看看效率如何
		}

		this.reload = refresh;
		
		/* 重新获取所有可以显示的节点数组 */
		this.resetTotalTreeNodes = function() {
			_totalTreeNodes = treeThis.getXmlRoot().selectNodes(".//treeNode[../@_open='true' or @id='_rootId']");
			_totalTreeNodesNum = _totalTreeNodes.length;

			_vScrollDiv.style.height = Math.max(1, (_totalTreeNodesNum - _pageSize) * _TREE_NODE_HEIGHT + _windowHeight);
		}

		/* 将节点滚动到可视范围之内 */
		this.scrollTo = function(node) {
			var nodeIndex = null;
			for(var i = 0; i < _totalTreeNodesNum; i++) {
				if(_totalTreeNodes[i] == node) {
					nodeIndex = i;
					break;
				}
			}
			if(nodeIndex == null) return;

			var childNums = node.selectNodes(".//treeNode[../@_open = 'true']").length;
			if(childNums + 1 > _pageSize || nodeIndex < _startNum  || nodeIndex >= _startNum + _pageSize) {
	            _vScrollBox.style.display = 'inline';
				_vScrollBox.scrollTop = nodeIndex * _TREE_NODE_HEIGHT;
			}
			else if (nodeIndex + childNums + 1 - _pageSize > _startNum) {
	            _vScrollBox.style.display = 'inline';
				_vScrollBox.scrollTop = (nodeIndex + childNums + 1 - _pageSize) * _TREE_NODE_HEIGHT;
			} 
			else {
				this.reload();
			}
		}
		
		/* 刷新页面展示：数据展示框、滚动条等  */
		function refreshUI() {
			setScrollBoxSize();
	        setTableElementSize();
	        setExtendTableElementSize();

			// 同步横向滚动条的大小
			setScrollDivSize();
		}
		
		/*
		 * 根据页面上的行数，获取相应的Row对象
		 */
		this.getRowByIndex = function (index) {
			if(index >= _pageSize || index < 0) {
				alert("TreeDisplay对象：行序号[" + index + "]超出允许范围[0 - " + _pageSize + "]！");
				return null;
			}
			return _Rows[index];
		}
		
		/*
		 * 根据页面上的行数，获取相应的ExtendRow对象
		 */
		this.getExtendRowByIndex = function (index) {
			if(index >= _pageSize || index < 0) {
				alert("Display对象：行序号[" + index + "]超出允许范围[0 - " + _pageSize + "]！");
				return null;
			}
			return _ExtendRows[index];
		}
	}

	this.init();
}

/* 根据id返回TreeNode对象，如果对象不存在，则返回null  */
ExtendTree.prototype.getTreeNodeById = function(id) {
	var node = this.getXmlRoot().selectSingleNode(".//treeNode[@id='" + id + "']");
	return instanceTreeNode(node, this);
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

	var eventExtendNodeChange = new EventFirer($$("tree"), "onExtendNodeChange"); // 扩展项（权限项）状态改变
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

	return {flag:flag, state: (flag ? nextState : curState)};
}