
var GridCache = new Collection();

function $G(gridId, data) {
	var gridObj = GridCache.get(gridId);
	if( gridObj == null || data ) {
		gridObj = new Grid($(gridId), data);
	}
	
	return gridObj;
}



var scrollbarSize = 17;
var cellWidth = 100; //基本列宽
var cellHeight = 22; //数据行高
var GRID_SCROLL_DELAY_TIME = 0; // 滚动条的滚动事件延迟时间（毫妙）

var Grid = function(element, data) {
	this.id = element.id;
	this.element = element;
 
	this.baseurl  = element.baseurl || "";
	this.iconPath = this.baseurl + "images/"
	
	this.element.innerHTML = "<div id='" + this.id + "Box' style='position:absolute;overflow:auto;left:0px;top:0px;z-index:1'></div>";
	this.gridBox   = $(this.id + "Box");
	this.gridBox.style.height = this.windowHeight = element.height || "100%";
	this.gridBox.style.width  = this.windowWidth  = element.width  || "100%";

	this.pageSize = Math.floor(this.windowHeight / cellHeight);
	
	this.load(data);	

	// 添加Grid事件处理
	this.attachEventHandler();	

	GridCache.add(this.id, this);	
}


/*
 * 根据页面上的行数，获取相应的Row对象
 * 参数：	index	行序号
 * 返回值：	Row对象
 */
Grid.prototype.getRowByIndex = function (index) {
	if(index >= this.totalRowsNum || index < 0) {
		alert("Grid对象：行序号[" + index + "]超出允许范围[0 - " + this.totalRowsNum + "]！");
		return null;
	}
	return this.rows[index];
}

Grid.prototype.load = function(data, append) {
	if("object" != typeof(data) || data.nodeType != 1) {
		alert("传入的Grid数据有问题。")	
	} 
	
	this.gridDoc = new Grid_DOCUMENT(data.node);	
	if( this.gridDoc.xmlDom == null ) return;
 
	this.xslDom = getXmlDOM();
	this.xslDom.resolveExternals = false;
	this.xslDom.load(this.baseurl + "grid.xsl");

	// 初始化XSL里的变量
	var startNum = append ? this.totalRowsNum : 0;
	this.xslDom.selectSingleNode("/xsl:stylesheet/xsl:script").text = "\r\n var cellHeight=" + cellHeight 
		+ "; var startNum=" + startNum + ";"
		+ "; var gridId='" + this.id + "'; \r\n"; 
		
	var gridTableHtml = this.gridDoc.transformXML(this.xslDom); // 利用XSL把XML解析成Html
	
	if(append) {
		var tempGridNode = document.createElement("div");
		tempGridNode.innerHTML = gridTableHtml.replace(/<\/br>/gi, "") ;
		var tempRows = tempGridNode.childNodes[0].tBodies[0].rows;
		for(var i=0; i < tempRows.length; i++) {
			var cloneRow = tempRows[i].cloneNode(true);
			this.tbody.appendChild(cloneRow);
		}

		tempGridNode.removeNode(true);
	}
	else {
		this.gridBox.innerHTML = ""; // 初始化容器
		this.gridBox.innerHTML = gridTableHtml.replace(/<\/br>/gi, "") ;
	}
	
	this.tbody = this.gridBox.childNodes[0].tBodies[0];
	this.rows = this.tbody.rows;
	this.totalRowsNum = this.rows.length;
	for(var i=startNum; i < this.totalRowsNum; i++) {
		this.processDataRow(this.rows[i]); // 表格行TR
	}
	
	// for (this.gridDoc.columnsMap)
	// var sortable = tHead.td.getAttribute("sortable");
}

function getTbody() {
	return 
}

Grid.prototype.processDataRow = function(curRow) {
	attachHighlightRowEvent(curRow);
	
	var cells = curRow.childNodes;
	for(var j=0; j < cells.length; j++) {
		var cell = cells[j];
		var columnName = cell.getAttribute("name");
		if( columnName == null || columnName == "cellsequence" || columnName == "cellheader")  {
			continue;
		}
 
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
	// 添加滚动条事件
	var oThis = this;

	this.gridBox.onscroll = function() {
		 if(oThis.gridBox.scrollHeight - oThis.gridBox.scrollTop <= oThis.windowHeight) {
			// alert("到达底部");
			var eventFirer = new EventFirer(oThis.element, "onScrollToBottom");
			eventFirer.fire(createEventObject());
		 }
	}

	this.element.onmousewheel = function() {
		oThis.gridBox.scrollTop += -Math.round(window.event.wheelDelta / 120) * cellHeight;
	}
	
	this.element.onkeydown = function() {
		switch (event.keyCode) {
		    case 33:	//PageUp
				oThis.gridBox.scrollTop -= oThis._ageSize * cellHeight;
				return false;
		    case 34:	//PageDown
				oThis.gridBox.scrollTop += oThis.pageSize * cellHeight;
				return false;
		    case 35:	//End
				oThis.gridBox.scrollTop = oThis.gridBox.offsetHeight - oThis.windowHeight;
				return false;
		    case 36:	//Home
				oThis.gridBox.scrollTop = 0;
				return false;
		    case 37:	//Left
				oThis.gridBox.scrollLeft -= 10;
				return false;
		    case 38:	//Up
				oThis.gridBox.scrollTop -= cellHeight;
				return false;
		    case 39:	//Right
				oThis.gridBox.scrollLeft += 10;
				return false;
		    case 40:	//Down
				oThis.gridBox.scrollTop += cellHeight;
				return false;
		}
	}
 
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
 			
				var eventFirer = new EventFirer(this, "onRightClickRow");
				eventFirer.fire(oEvent);  // 触发右键事件
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
			this.dataRows = this.selectNodes(".//data//row");
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