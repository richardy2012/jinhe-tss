
var GridCache = new Collection();

function $G(gridId, data) {
	var gridObj = GridCache.get(gridId);
	if( gridObj == null || data ) {
		gridObj = new Grid($(gridId), data);
	}
	
	return gridObj;
}


/*
 * 节点显示的行高（象素），只用于计算显示的行数，不能控制显示时行的高度
 * 如果要修改显示的行高，修改样式文件
 */
var scrollbarSize = 17;
var cellWidth = 100; //基本列宽
var cellHeight = 22; //数据行高

var GRID_SCROLL_DELAY_TIME = 0;          // 滚动条的滚动事件延迟时间（毫妙）

var Grid = function(element, data) {
	this.id = element.id;
	this.element = element;
 
	this._baseurl  = element.baseurl || "";
	this._iconPath = this._baseurl + "images/"
	
	this.element.innerHTML = "<div id='" + this.id + "Box' style='position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:1'></div>";
	this.gridBox   = $(this.id + "Box");
	
	this.load(data);	
	
	this.windowHeight = Math.max(this.element.offsetHeight - scrollbarSize, cellHeight);
	this.windowWidth  = Math.max(this.element.offsetWidth  - scrollbarSize, cellWidth);
	this.pageSize     = Math.floor(this.windowHeight / cellHeight);
	
	// 生成滚动条
	var vScrollStr = "<div id='" + this.id + "VScrollBox' style='position:absolute;overflow-y:auto;heigth:100%;width:17px;top:0px;right:0px;'>"
						+ "<div id='" + this.id + "VScrollDiv' style='width:1px'></div></div>";
	var hScrollStr = "<div id='" + this.id + "HScrollBox' style='position:absolute;overflow-x:auto;overflow-y:hidden;heigth:17px;width:100%;bottom:0px;left:0px'>'"
						+ "<div id='" + this.id + "HScrollDiv' style='higth:1px'></div></div>";
	this.element.insertAdjacentHTML('afterBegin', vScrollStr + hScrollStr);
	this._vScrollBox = $(this.id + "VScrollBox");
	this._vScrollDiv = $(this.id + "VScrollDiv");
	this._hScrollBox = $(this.id + "HScrollBox");
	this._hScrollDiv = $(this.id + "HScrollDiv");
	
	var oThis = this;
	
	/*
	 * 纵向滚动事件触发后，延时执行reload，如果第二次触发时，上次的事件还没有执行，
	 * 则取消上次事件，触发本次事件。为的是防止多次触发，屏幕抖动。
	 */
	this._vScrollBox.onscroll = function() {
		if (this._scrollTimer) {
			window.clearTimeout(this._scrollTimer);
		}
		this._scrollTimer = window.setTimeout(oThis.refresh, GRID_SCROLL_DELAY_TIME);
	};
	this._vScrollBox.style.height = this.windowHeight; // 设置滚动条的大小
	this._vScrollDiv.style.height = (this.totalRows - this.pageSize) * cellHeight + this.windowHeight;
	
	/* 横向滚动事件 */
	this._hScrollBox.onscroll = function() {
		oThis.gridBox.scrollLeft = this.scrollLeft;
	};
	this._hScrollBox.style.width = this.windowWidth;
	this._hScrollDiv.style.width = this.gridBox.style.width; 
	
	// 设置显示节点的table对象的大小
	this.gridBox.style.height = this.windowHeight;
	this.gridBox.style.width  = this.windowWidth;
	
	this.element.onmousewheel = function() {
		oThis._vScrollBox.scrollTop += -Math.round(window.event.wheelDelta / 120) * cellHeight;
	}
	
	this.element.onkeydown = function() {
		switch (event.keyCode) {
		    case 33:	//PageUp
				oThis._vScrollBox.scrollTop -= oThis._ageSize * cellHeight;
				return false;
		    case 34:	//PageDown
				oThis._vScrollBox.scrollTop += oThis.pageSize * cellHeight;
				return false;
		    case 35:	//End
				oThis._vScrollBox.scrollTop = oThis._vScrollDiv.offsetHeight - oThis.windowHeight;
				return false;
		    case 36:	//Home
				oThis._vScrollBox.scrollTop = 0;
				return false;
		    case 37:	//Left
				oThis._hScrollBox.scrollLeft -= 10;
				return false;
		    case 38:	//Up
				oThis._vScrollBox.scrollTop -= cellHeight;
				return false;
		    case 39:	//Right
				oThis._hScrollBox.scrollLeft += 10;
				return false;
		    case 40:	//Down
				oThis._vScrollBox.scrollTop += cellHeight;
				return false;
		}
	}
	
	// 添加Grid事件处理
	this.attachEventHandler();	

	GridCache.add(this.id, this);
}

/*
 * 根据滚动状态，显示可视范围内的树节点。
 */
Grid.prototype.refresh = function () {
	this.startNum = (this.totalRows <= this.pageSize) ? 0 : Math.ceil(this._vScrollBox.scrollTop  / cellHeight);
	
	// 显示节点
	for(var i = 0; i < this.pageSize; i++) {
		var nodeIndex = i + this.startNum;
		if(nodeIndex < this.totalRows) {
			this.rows[i].style.display = "";
		} else {
			this.rows[i].deleteRow();
		}
	}
	
	// 同步横向滚动条的大小
	this_hScrollDiv.style.width = this.gridBox.offsetWidth;

	// refreshUI();
}


/*
 * 根据页面上的行数，获取相应的Row对象
 * 参数：	index	行序号
 * 返回值：	Row对象/null
 */
Grid.prototype.getRowByIndex = function (index) {
	if(index >= this.totalRows || index < 0) {
		alert("Grid对象：行序号[" + index + "]超出允许范围[0 - " + this.totalRows + "]！");
		return null;
	}
	return this.rows[index];
}

Grid.prototype.load = function(data) {
	if("object" != typeof(data) || data.nodeType != 1) {
		alert("传入的Grid数据有问题。")	
	} 
	
	this.gridDoc = new Grid_DOCUMENT(data.node);	
	if( this.gridDoc.xmlDom == null ) {
		return;
	}
 
	// 初始化各容器状态
	this.gridBox.innerHTML = "";
	
	this.xslDom = getXmlDOM();
	this.xslDom.resolveExternals = false;
	this.xslDom.load(this._baseurl + "grid.xsl");
	// 初始化XSL里的变量
	this.xslDom.selectSingleNode("/xsl:stylesheet/xsl:script").text = "\r\n var cellHeight=22; var gridId='" + this.element.id + "'; \r\n"; 
		
	var gridTableHtml = this.gridDoc.transformXML(this.xslDom); // 利用XSL把XML解析成Html
	this.gridBox.innerHTML = "<nobr>" + gridTableHtml.replace(/<\/br>/gi, "") + "</nobr>";
	
	this.rows = $(this.element.id + "Div").childNodes[0].tBodies[0].rows;
	this.totalRows = this.rows.length;
	for(var i=0; i < this.totalRows; i++) {
		var curRow = this.rows[i];  // 表格行TR
		attachHighlightRowEvent(curRow);
		
		var cells = curRow.childNodes;
		for(var j=0; j < cells.length; j++) {
			var cell = cells[j];
			var columnName = cell.getAttribute("name");
			if( columnName && columnName != "cellsequence"  && columnName != "cellheader")  {
				var value = curRow.getAttribute(columnName); // xsl解析后，各行的各个TD值统一记录在了TR上。
				curRow.removeAttribute(columnName);
				
				var nobrNodeInCell = cell.childNodes[0];  // nobr 节点
				var columnNode = this.gridDoc.columnsMap[columnName]; 
				if( columnNode ) {
					var mode = columnNode.getAttribute("mode");
					switch( mode ) {
						case "string":
							var editor = columnNode.getAttribute("editor");
							var editortext = columnNode.getAttribute("editortext");
							var editorvalue = columnNode.getAttribute("editorvalue");
							if(editor == "comboedit" && editorvalue && editortext) {
								var listNames  = editortext.split("|");
								var listValues = editorvalue.split("|");
								for(var n = 0; n < listValues.length; n++) {
									if(value == listValues[n]) {
										value = listNames[n];
										break;
									}
								}
							}
							
							nobrNodeInCell.innerText = value;
							cell.setAttribute("title", value);								
							break;
						case "number":
							nobrNodeInCell.innerText = value;
							cell.setAttribute("title", value);
							break;                       
						case "function":                          
							break;    
						case "image":          
							nobrNodeInCell.innerHTML = "<img src='" + value + "'/>";
							break;    
						case "boolean":      
							var checked = (value =="true") ? "checked" : "";
							nobrNodeInCell.innerHTML = "<input class='selectHandle' name='" + columnName + "' type='radio' " + checked + "/>";
							nobrNodeInCell.all.tags("INPUT")[0].disabled = true;
							break;
					}
				}						
			}		
		}	
	}
	
	// for (this.gridDoc.columnsMap)
	// var sortable = tHead.td.getAttribute("sortable");
}

function attachHighlightRowEvent(curRow) {
	// 鼠标经过行时高亮显示
	curRow.onmouseover = function() { 
		 this.oldClassName = this.className;
		 addClass(this, "rolloverRow");    // 鼠标经过时添加class为highlight的值			 
	}			
	curRow.onmouseout = function() { 
		this.className = this.oldClassName; // 鼠标离开时还原之前的class值
		this.removeAttribute("oldClassName");
	}
}

// 添加Grid事件处理
Grid.prototype.attachEventHandler = function() {
 
	this.element.onclick = function() { // 单击行
		if( notOnGridHead(event.srcElement) ) { // 确保点击处不在表头
			var _srcElement = event.srcElement;
			// clickTR(_srcElement);
			// clickTD(_srcElement);
		}
	}
	this.element.ondblclick = function() { // 双击行
		if( notOnGridHead(event.srcElement) ) { 
			var _srcElement = event.srcElement;
			// dbClickTR(_srcElement);
		}
	}

	this.element.oncontextmenu = function() {
		var _srcElement = event.srcElement;
		if( notOnGridHead(_srcElement) ) { // 确保点击处不在表头
			
			var trObj = _srcElement;
			if( trObj.tag != "TR" ) {
				trObj = trObj.parentElement;
			}

			if(trObj && trObj.getAttribute("_index") ) {
				var rowIndex = parseInt( trObj.getAttribute("_index") );
				var oEvent = createEventObject();
				oEvent.result = {
					rowIndex: rowIndex
				};
 			
				var eventRightClickGridRow = new EventFirer(this, "onRightClickRow");
				eventRightClickGridRow.fire(oEvent);  // 触发右键事件
			}
		}
	}
	
	// 确保点击处不在表头
	function notOnGridHead(srcElement) { 
		return !isContainTag(srcElement, "THEAD");
	}
	
	function isContainTag(obj, tag) {
        while( obj ) {
			if (obj.tagName == tag) {
				return true;
			}
            obj = obj.parentElement;
        }
		return false;
    }
}

//动态给js添加class属性
function addClass (element, className) {
	if( !element.className ) {
		element.className = className;    // 如果element本身不存在class,则直接添加class
	} else {
		element.className += " " +className;  // 如果之前有一个class值，注意中间要多一个空格,然后再加上
	} 
}


var Grid_DOCUMENT = function(xmlDom) {
	this.xmlDom = xmlDom;

	this.transformXML = function(xslDom) {			
		return this.xmlDom.transformNode(xslDom).replace(/&amp;nbsp;/g, "&nbsp;").replace(/\u00A0/g, "&amp;nbsp;");
	}
	
	this.refresh = function() {
		this.hasData = (this.xmlDom && this.xmlDom.xml != "");
		if( this.hasData ) {
			this.Declare = this.xmlDom.selectSingleNode("./declare");
			this.Script  = this.xmlDom.selectSingleNode("./script");
			this.Columns = this.xmlDom.selectNodes("./declare/column");
			this.Data    = this.xmlDom.selectSingleNode("./data");

			this.columnsMap = {};
			for(var i = 0; i < this.Columns.length; i++) {
				this.columnsMap[this.Columns[i].getAttribute("name")] = this.Columns[i];
			}
			
			this.header = this.Declare.getAttribute("header");
			this.hasHeader = (this.header == "radio" || this.header == "checkbox");
	 
			this.VisibleColumns = this.selectNodes(".//declare//column[(@display!='none') or not(@display)]");
			this.Rows = this.selectNodes(".//data//row");
 
			this.RowByIndex = {};
			for(var i=0; i < this.Rows.length; i++) {
				var curRow = this.Rows[i];
				var _index = curRow.getAttribute("_index");
				this.RowByIndex[_index] = curRow;
			}
		}
	}

	this.refresh();
}
Grid_DOCUMENT.prototype.selectNodes = function(xpath) {
	return this.xmlDom.selectNodes(xpath);
}
Grid_DOCUMENT.prototype.selectSingleNode = function(xpath) {
	return this.xmlDom.selectSingleNode(xpath);
}