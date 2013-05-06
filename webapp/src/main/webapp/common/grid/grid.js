function $G(gridId, data) {
	var element = $(gridId);
	var gridObj = new Grid(element, data);
	
	return gridObj;
}

var scrollBarZoom = 20;
var scrollbarSize = 17;
var cellWidth = 100; //基本列宽
var cellHeight = 22; //数据行高

var Grid = function(element, data) {
	this.element = element;

	this.xmlDom = getXmlDOM();
	this.xslDom = getXmlDOM();
	this.xslDom.resolveExternals = false;

	this.xmlDoc;
	
	this._baseurl  = element._baseurl || "";
	this._iconPath = this._baseurl + "images/"

	this._columnList = {};
	
	this.element.innerHTML = "<div id='gridBox' style='position:absolute;left:0px;top:0px;width:100%;height:100%;z-index:1'></div>" + 
						"<div id='scrollBox' style='position:absolute;left:0px;top:0px;width:1px;height:1px;z-index:2'></div>" + 
						"<div id='hint' style='position:absolute;right:30;top:" + (this.element.offsetHeight/2-12) + ";width:1px;height:1px;visibility:hidden;z-index:4'>" + 
							"<div style='width:60;height:20px;text-align:right;padding:3px;border:1px solid black;background-color:#FFFFEE;font-size:10px;font-family:verdana;'>" + 
								"<span id='number'></span>" + 
								"<span style='width:16px;height:12px;position:relative;left:-12px;'>" + 
									"<div id='up' style='font-family:webdings;font-size:12px;position:absolute;left:16;top:-1;line-height:6px;width:12px;height:8px;padding-top:3px;'>5</div>" + 
									"<div id='down' style='font-family:webdings;font-size:12px;position:absolute;left:16;top:8;line-height:6px;width:12px;height:8px;'>6</div>" + 
								"</span>" + 
							"</div>" + 
							"<div style='position:absolute;left:3;top:3;width:60;height:22;background-color:black;filter:alpha(opacity=30);z-index:-10'></div>" + 
						"</div>" + 
						"<div id='other' style='position:absolute;left:0px;top:0px;width:1;height:1;z-index:3'></div>&nbsp;";
	this._gridBox   = $("gridBox");
	this._scrollBox = $("scrollBox");
	this._hint      = $("hint");
	this._other     = $("other");
	
	if(this.element.currentStyle.position=="static"){
		this.element.runtimeStyle.position = "relative";
	}	
	
	// 添加Grid事件处理
	this.attachEventHandler();
	
	this.load(data);
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
	this.initContainers();
	
	this.xslDom.load(this._baseurl + "grid.xsl");
	this.xslDom.selectSingleNode("/xsl:stylesheet/xsl:script").text = "\r\n var cellHeight=22; \r\n"; // 初始化XSL里的变量
	var gridTableHtml = this.gridDoc.transformXML(this.xslDom); // 利用XSL把XML解析成Html
	this._gridBox.innerHTML = "<nobr>" + gridTableHtml.replace(/<\/br>/gi, "") + "</nobr>";
	
	var rows = $("gridTable").childNodes[0].tBodies[0].rows;
	for(var i=0; i < rows.length; i++) {
		 var curRow = rows[i]; // 表格行
		 var cells = curRow.childNodes;
		 for(var j=0; j < cells.length; j++) {
			var columnName = cells[j].getAttribute("name");
			if( columnName && columnName != "cellsequence") {
				var cellFirstChild = cells[j].childNodes[0];
				cellFirstChild.innerText = curRow.getAttribute(columnName);
			}
		}
	}
	
}

Grid.prototype.initContainers = function() {
	var len = this._other.childNodes.length;
	for(var i=0; i < len; i++) {
		this._other.childNodes[0].removeNode(true);
	}
	
	// 刷新界面和数据
	this._gridBox.innerHTML = "";
	this._scrollBox.innerHTML = "";

	var totalWidth  = this.element.offsetWidth;
	var totalHeight = this.element.offsetHeight;
 
	// 计算表体的行数
	this.len = Math.floor((totalHeight - scrollbarSize - 1 * cellHeight) / cellHeight);
	this._curTotalRows_Table = Math.min(this.len, this.gridDoc.Rows.length);

	// 计算控件绝对定位坐标
	var tempParent = this.element;
	this.absLeft = 0;
	this.absTop = 0;
	while(tempParent.tagName != "BODY"){
		this.absLeft	+= tempParent.offsetLeft;
		this.absTop	+= tempParent.offsetTop;
		tempParent = tempParent.offsetParent;
	}
}

//  添加Grid事件处理
Grid.prototype.attachEventHandler = function() {
	
	attachHintControl();
	 
	this.element.onselectstart = this.element.onmousedown = function(){
		event.returnValue = false;
	}
	this.element.onclick = function(){ // 单击行
		if( notOnGridHead(event.srcElement) ) { // 确保点击处不在表头
			var _srcElement = event.srcElement;
			clickTR(_srcElement);
			clickTD(_srcElement);
		}
	}
	this.element.ondblclick = function() { // 双击行
		if( notOnGridHead(event.srcElement) ) { // 确保点击处不在表头
			var _srcElement = event.srcElement;
			clickTR(_srcElement);
		}
	}
	this.element.onmouseover = function(){
		highlightRow();
	}
	this.element.onmouseout = function() {
		lowlightRow();
	}
	this.element.onmousewheel = function(){
		event.returnValue = false;
		if( $("scrollbarV") ) {
			$("scrollbarV").scrollTop += - Math.round(window.event.wheelDelta/120) * scrollBarZoom;
		}
	}

	this.element.oncontextmenu = function(){
		var _srcElement = event.srcElement;
		if( notOnGridHead(_srcElement) && notOnGridScollBar(_srcElement) ) { // 确保点击处不在表头和滚动条区域
			
			var trObj = offsetToNode(this._gridBox, _srcElement, "TR");
			if(trObj && trObj.getAttribute("_index") ) {
				var xmlNodeIndex = parseInt( trObj.getAttribute("_index") );

				// 触发onrightclickrow事件
				var oEvent = createEventObject();
				oEvent.result = {
					rowIndex_Xml: xmlNodeIndex
				};
				event_onrightclickrow.fire (oEvent);
			}
		}
	}

	this.element.onkeydown = function() {
		event.returnValue = false;
		switch(event.keyCode){
			case 33:
				scrollTo(begin - len);
				break;
			case 34:
				scrollTo(begin + len);
				break;
			case 40:
				scrollBy(1);
				break;
			case 38:
				scrollBy(-1);
				break;
		}
	}
	
	
	// 确保点击处不在表头
	function notOnGridHead(srcElement) { 
		return offsetToNode(this._gridBox, event.srcElement, "THEAD") == null ;
	}
	
	// 确保点击处不在滚动条区域
	function notOnGridScollBar(srcElement) { 
		return  offsetToNode(this._scrollBox, "DIV") == null;
	}
	
	function offsetToNode(container, obj, tag) {
        while(container.contains(obj) && obj.tagName != tag){
            obj = obj.parentElement;
        }
		
        if(obj.tagName == tag && container.contains(obj))	{
            return obj;
        }
		return null;
    }
	
	
	function attachHintControl() {
		var hintTimeout;
		function getNumber() {
			return parseInt($("number").innerText);
		}
		
        $("hint").onmouseover = function() {
                clearTimeout(hintTimeout);
        }
        $("hint").onmouseout = function() {
                hintTimeout = setTimeout(hideHint, 1000);

                function hideHint(){
                    $("hint").style.visibility = 'hidden';
                    gotoLine(Math.round($("scrollbarV").scrollTop/scrollBarZoom), true); // 执行取数据刷新显示
                }
        }
        $("up").onmousedown = function(){
            this.style.left = 17;
            this.style.top = 0;			
            clearTimeout(hintTimeout);
            $("number").innerText = Math.max(getNumber() - 1, 1);
			
            this.setCapture();
        }
        $("up").onmouseup = function(){
            this.style.left = 16;
            this.style.top = -1;
            this.releaseCapture();
            
            scrollTo(getNumber() - 1); //执行取数据刷新显示
        }
        $("down").onmousedown = function(){
            this.style.left = 17;
            this.style.top = 9;
            clearTimeout(hintTimeout);
            $("number").innerText = Math.min(getNumber() + 1, this.gridDoc.Rows.length - len + 1);
            this.setCapture();
        }
        $("down").onmouseup = function(){
            this.style.left = 16;
            this.style.top = 8;
            this.releaseCapture();

            scrollTo(getNumber() - 1); // 执行取数据刷新显示
        }
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
			for(var i=0; i < this.Rows.length; i++){
				var curRow = this.Rows[i];
				var _index = curRow.getAttribute("_index");
				this.RowByIndex[_index] = curRow;
			}
		}
	}

	this.refresh();
}

Grid_DOCUMENT.prototype.refreshIndex = function(startIndex) {
	for(var i = startIndex || 0; i < this.Rows.length; i++) {
		this.Rows[i].setAttribute("_index", i);		
	}
}
Grid_DOCUMENT.prototype.selectNodes = function(xpath){
	return this.xmlDom.selectNodes(xpath);
}
Grid_DOCUMENT.prototype.selectSingleNode = function(xpath){
	return this.xmlDom.selectSingleNode(xpath);
}