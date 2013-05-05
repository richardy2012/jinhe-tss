function $G(gridId) {
	var element = $(gridId);
	var gridObj = new Grid(element);
	
	return gridObj;
}

var Grid = function(element) {
	this.element = element;

	this.xmlDom = getXmlDOM();
	this.xslDom = getXmlDOM();
	this.xslDom.resolveExternals = false;

	this.xmlDoc;
	
	this._baseurl  = element._baseurl || "";
	this._iconPath = this._baseurl + "images/"

	this._columnList = {};
}

Grid.prototype.load = function(data) {
	if("object" != typeof(data) || data.nodeType != 1) {
		alert("传入的XForm数据有问题。")	
	} 
	
	this.gridDoc = new Grid_DOCUMENT(data.node);	
	if( this.gridDoc.xmlDom ) {
		this.xslDom.load(this._baseurl + "grid.xsl");
		this.xslDom.selectSingleNode("/xsl:stylesheet/xsl:script").text = "\r\n var cellHeight=22; \r\n";
		
		var htmlStr = this.gridDoc.transformXML(this.xslDom); // 利用XSL把XML解析成Html
		this.element.innerHTML = htmlStr.replace(/<\/br>/gi, "");
		
		// 触发onload事件
		var onload = this.element.getAttribute("onload");
		if(onload) {
			eval(onload);
		}
	}
}

var Grid_DOCUMENT = function(xmlDom) {
	this.xmlDom = xmlDom;

	this.transformXML = function(xslDom) {			
		return this.xmlDom.transformNode(xslDom).replace(/&amp;nbsp;/g, "&nbsp;").replace(/\u00A0/g, "&amp;nbsp;");
	}
	
	this.refresh = function() {
		if( this.xmlDom ) {
			this.declare = this.xmlDom.selectSingleNode("./declare");
			this.Script  = this.xmlDom.selectSingleNode("./script");
			this.Columns = this.xmlDom.selectNodes("./declare/column");
			this.Data    = this.xmlDom.selectSingleNode("./data");

			this.columnsMap = {};
			for(var i = 0; i < this.Columns.length; i++) {
				this.columnsMap[this.Columns[i].getAttribute("name")] = this.Columns[i];
			}
		}
	}

	this.refresh();
}