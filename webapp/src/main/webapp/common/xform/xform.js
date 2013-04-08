
var _baseurl = "";
var _iconPath = _baseurl + "/icon/"

var XForm = function(element) {
	this.element = element;
	this.form = element.firstChild;

	this.tempDom = new ActiveXObject("MSXML.DOMDocument");
	this.tempDom.async = false;

	this.xslDom = new ActiveXObject('MSXML.DOMDocument');
	this.xslDom.async = false;
	this.xslDom.resolveExternals = false;

	this.xmlDoc;
	this._width;
    this._height;

	this._columnList = {};
}


XForm.prototype.attachEvents = function() {
	// 回车自动聚焦下一个（input、button等）
	this.element.onkeydown = function() {
		var srcElement = event.srcElement;
		if(window.event.keyCode == 13 && srcElement.tagName.toLowerCase() != "textarea") {
			window.event.keyCode = 9;  // 相当于按了下Tab键，光标移动到下一个元素上
		}
	}
	
	this.element.onselectstart = function() {
		event.cancelBubble = true; // 拖动选择事件取消冒泡
	}
}

XForm.prototype.load = function(data, dataType) {
	this.element.data = data;
	this.element.dataType = dataType;
	reload();
}

XForm.prototype.reload = reload() {
	// 隐藏上次的错误信息层
	hideErrorInfo();

	this.xslDom.load(_baseurl + "xform.xsl");
	this.xslDom.selectSingleNode("/xsl:stylesheet/xsl:script").text = "\r\nvar uniqueID=\"" + element.uniqueID 
		+ "\";\r\nvar baseurl=\"" + _baseurl + "\";\r\nvar formEditable=\"" + element.editable + "\";\r\n";

	var data = this.element.data;
	if(data != null && data != "") {
		var curXmlDom;
		switch(this.element.dataType) {
			case "url":
				tempDom.load(data);
				if(tempDom.parseError != 0) {
					alert("data地址有问题，解析XML不正确.");
				}

				var curXmlDom = tempDom.selectSingleNode("/*");
				if(curXmlDom == null) {
					alert("数据源有问题.");
				}
				break;
			case "node":
				if("object" == typeof(data) && 1 == data.nodeType) {
					var curXmlDom = data;
				}
				break;
		}
		this.xmlDoc = new Class_XMLDocument(curXmlDom);
	}
	
	if(this.xmlDoc != null && this.xmlDoc.xmlObj != null) {
		// 修正comboedit类型默认第一项的值
		fixComboeditDefaultValue(xmlDoc.Row);

		var htmlStr = this.xmlDoc.transformXML(xslDom); // 利用XSL把XML解析成Html
		this.element.innerHTML = htmlStr.replace(/<\/br>/gi, "");

		if(this.form != null) {
			this.form.attachEvent('onsubmit', checkForm);
			this.form.attachEvent('onreset', resetForm);

			// 添加标题栏				
			var theTable = this.form.all.tags("TABLE")[0];
			if(theTable != null && this.element.getAttribute("caption") != null) {
				var count = theTable.rows(0).cells.length;
				for(var i = 0; i < theTable.rows(0).cells.length; i++) {
					count += parseInt(theTable.rows(0).cells(i).colSpan);
				}
				var captionTR = theTable.insertRow(0);
				var captionTD = captionTR.insertCell();
				captionTD.colSpan = count;
				captionTD.id = "titleBox";
				captionTD.className = "titleBox";
				captionTD.style.cssText = "font-size:12px;height:19px;background-image:url(" + _iconPath + "titlebg.gif);background-repeat:no-repeat;";
				captionTD.innerHTML = this.element.getAttribute("caption");

				var tempDivHeight = _height - theTable.rows[0].offsetHeight - (theTable.rows[2] == null ? 0 : theTable.rows[2].offsetHeight);
				if(tempDivHeight > 0 && _overflow == true) {
					theTable.rows(1).cells(0).firstChild.style.height = tempDivHeight;
				}
			}
		}

		// 绑定各个column对应的编辑方式
		attachEditor();
	
		// 触发onload事件
		var onload = this.element.getAttribute("onload");
		if(onload != null) {
			eval(onload);
		}

		// 自动聚焦
		setFocus();
	}
}

XForm.prototype.attachEditor = function() {
	var cols = this.xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var colName   = cols[i].getAttribute("name");
		var colMode   = cols[i].getAttribute("mode");
		var colEditor = cols[i].getAttribute("editor");
		var nodeValue = getColumnValue(colName);

		// 取layout中绑定该columne的元素
		var tempObj = this.element.all(colName);
		if(tempObj == null) {
			continue;
		}

		var curInstance;
		switch(colMode) {
			case "string":
				if(colEditor == "comboedit") {
					curInstance = new Mode_ComboEdit(colName);
				}
				else if(colEditor == "radio") {
					curInstance = new Mode_Radio(colName);
				}
				else {
					curInstance = new Mode_String(colName);
				}
				break;
			case "number":
				curInstance = new Mode_Number(colName);
				break;
			case "function":
				curInstance = new Mode_Function(colName);
				break;
			case "hidden":
				curInstance = new Mode_Hidden(colName);
				break;
		}
		curInstance.saveAsDefaultValue();
		_columnList[colName] = curInstance;
	}
}

XForm.prototype.checkForm = function() {
	// 隐藏上次的错误信息层
	hideErrorInfo();

	var cols = this.xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var colName  = cols[i].getAttribute("name");
		var _column = _columnList[colName];
		if(_column != null) {
			if(_column.validate() == false) {
				return false;
			}
		}
		else { // layout内不存在时创建虚拟实例执行校验
			var _column = {};
			_column.obj = {
				empty: cols[i].getAttribute("empty"),
				errorInfo: cols[i].getAttribute("errorInfo"),
				caption: cols[i].getAttribute("caption"),
				submitReg: cols[i].getAttribute("submitReg"),
				value: getColumnValue(colName)
			};

			_column.validate = validate;
			if(_column.validate() == false) {
				return false;
			}
		}
	}

	this.form.elements['xml'].value = this.xmlDoc.Data.xml;

	return true;
}

XForm.prototype.resetForm = function(fireOnDataChange) {
	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = this.xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var colName = cols[i].getAttribute("name");
		if(_columnList[colName] != null) {
			_columnList[colName].reset(fireOnDataChange);
		}
	}
	if(event != null) {
		event.returnValue = false;
	}
}

XFrom.prototype.updateData = function(obj) {
	var oldValue = this.getColumnValue(obj.binding);
	if(event.propertyName == "checked") {
		var newValue = obj.checked == true ? 1 : 0;
	}
	else if(obj.tagName.toLowerCase() == "select") {
		var newValue = obj._value;            
	}
	else {
		var newValue = obj.value;
	}

	if(newValue != oldValue && (newValue != "" || oldValue != null)) {
		this.setColumnValue(obj.binding, newValue);
	}
}

XFrom.prototype.updateDataExternal = function(name, value) {
	var node = this.getColumn(name);
	var oldValue  = getData(name);

	this.setColumnValue(name, value);
	
	// 更改页面显示数据
	var tempSrcElement;
	var _column = _columnList[name];
	if(_column != null) {
		_column.setValue(value);
		tempSrcElement = _column.obj;
	}
	else {
		tempSrcElement = { binding: name };
	}

	// 触发ondatachange事件
	if(fire != false) {
		fireDataChange(tempSrcElement, oldValue, value);
	}
}

function updateUnbindingDataExternal(id, value) {
	this.element.all(id).value = value;
	var node = xmlDoc.Layout.selectSingleNode(".//*[@id='" + id + "']");
	if(node != null) {
		node.setAttribute("value", value);
	}
}

function Class_XMLDocument(xmlObj) {
	this.xmlObj = xmlObj;
	this.transformXML = function(xslObj) {			
		var tempXMLDom = new ActiveXObject('MSXML.DOMDocument');
		tempXMLDom.async = false;
		tempXMLDom.resolveExternals = false;
		tempXMLDom.loadXML(this.toString());

		return tempXMLDom.transformNode(xslObj).replace(/&amp;nbsp;/g, "&nbsp;").replace(/\u00A0/g, "&amp;nbsp;");
	}
	this.toString = function() {
		if(this.xmlObj != null) {
			return this.xmlObj.xml;
		}
		return null;
	}
	this.refresh = function() {
		if(this.xmlObj != null) {
			this.declare = this.xmlObj.selectSingleNode("./declare");
			this.Layout  = this.xmlObj.selectSingleNode("./layout");
			this.Script  = this.xmlObj.selectSingleNode("./script");
			this.Columns = this.xmlObj.selectNodes("./declare/column");
			
			this.Data    = this.xmlObj.selectSingleNode("./data");
			if(this.Data == null) {				
				var dataNode = tempDom.createElement("data");
				this.xmlObj.appendChild(dataNode);

				this.Data = dataNode;
			}
			
			this.Row = this.xmlObj.selectSingleNode("./data/row[0]");
			if(this.Row == null) {
				var rowNode = tempDom.createElement("row");
				this.Data.appendChild(rowNode);	
				
				this.Row = rowNode;
			}
			
			this.columnsMap = {};
			for(var i = 0; i < this.Columns.length; i++) {
				this.columnsMap[this.Columns[i].getAttribute("name")] = this.Columns[i];
			}
		}
	}
	this.refresh();
}

function setEditable(s) {
	if(element.editable != s ) {
		element.editable = s;

		var buttonBox = element.all("buttonBox");
		if(buttonBox != null) {
			buttonBox.style.display = (s=="true" ? "block": "none");
		}

		var cols = xmlDoc.Columns;
		for(var i = 0; i < cols.length; i++) {
			var name = cols[i].getAttribute("name");
			var _column = _columnList[name];
			if(_column != null) {
				var columnEditable = cols[i].getAttribute("editable");
				s = (s == "true" && columnEditable != "false") ? "true": "false";
				_column.setEditable(s);
			}
		}

		setFocus();
	}
}

XForm.prototype.getData = function(name, replace) {
	var nodeValue = this.getColumnValue(name);
	if(true == replace) {
		nodeValue = nodeValue.replace(/\"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\'/g, "&#39;");
	}
	return nodeValue;
}

XForm.prototype.getColumn = function(name) {
	var _column = this.xmlDoc.columnsMap[name];
	if(_column == null) {
		alert(name + "不存在");
	}
	return _column;
}

XForm.prototype.xml = function() {
	return xmlDoc.toString();
}

// <row user="2222" password="aaaaaaaaa" id="222" date="2005/09/09"/>
XForm.prototype.reloadData = function(rowNode) {
	// 修正comboedit类型默认第一项的值
	this.fixComboeditDefaultValue(rowNode);

	// 隐藏上次的错误信息层
	hideErrorInfo();

	var cols = this.xmlDoc.Columns;  // 以column定义为获取依据
	for(var i = 0; i < cols.length; i++) {
		var name  = cols[i].getAttribute("name");
		var value = rowNode.getAttribute(name);
		this.updateDataExternal(name, value || "");
	}
}

XForm.prototype.fixComboeditDefaultValue = function(rowNode) {
	var cols = this.xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var name   = cols[i].getAttribute("name");
		var empty  = cols[i].getAttribute("empty");
		var editor = cols[i].getAttribute("editor");
		var editorValue = cols[i].getAttribute("editorvalue") || "";
		var firstValue = editorValue.split("|")[0];
		var value = this.getColumnValue(name);

		// 当empty = false(表示不允许为空)时，下拉列表的默认值自动取第一项值
		if((value == null || value.length == 0) && firstValue != "" && (editor=="comboedit" || editor=="radio") && empty=="false") {
			this.setColumnValue(name, firstValue);
		}
	}
}


XForm.prototype.saveAsDefaultValue = function() {
	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = this.xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var colName = cols[i].getAttribute("name");
		var _column = _columnList[colName];
		_column.saveAsDefaultValue();    
	}
}

XForm.prototype.setFocus = function(name) {
	if( name == null || name == "") {
		var column = this.xmlDoc.declare.selectSingleNode("column[(@editable='true' or not(@editable)) and (@display!='none' or not(@display))]");
		if(column == null) {
			return;
		}
		name = column.getAttribute("name");
	}

	var _column = this._columnList[name];
	if( _column != null ) {
		_column.setFocus();
	}
}

function setColumnEditable(name, booleanValue) {
	var _columnNode = this.getColumn(name);
	_columnNode.setAttribute("editable", booleanValue);
	
	var _column = this._columnList[name];
	if( _column != null ) {
		_column.setEditable(booleanValue);
	}
}


XForm.prototype.showCustomErrorInfo = function(name, str) {
	var instance = this._columnList[name];
	if( instance != null ) {
		showErrorInfo(str, instance.obj);
	}
}

function getColumnAttribute(name, attrName) {
	var column = xmlDoc.columnsMap[name];
	if(column != null) {
		return column.getAttribute(attrName);
	}
	else {
		alert("指定的列[" + name + "]不存在");
		return null;
	}
}

function setLabelContent(name, content) {
	var labelObj = this.element.all("label_" + name);
	if(labelObj != null) {
		if(labelObj.length > 1) {
			labelObj = labelObj[0];
		}
		labelObj.innerHTML = content;
	}
}

function getXmlDocument() {
	return this.xmlDoc.xmlObj;
}

/*
 * 获取row节点上与column对应的值
 */
XFrom.prototype.getColumnValue = function(name) {
	var rowNode = this.xmlDoc.Row;
	var node = rowNode.selectSingleNode(name);
	var nodeValue = (null == node ? null : node.text);

	return nodeValue;
}

/*
 *  函数说明：设置row节点上与column对应的值
 *  参数：  string:name             列名
			string/array:value      值
 */
XForm.prototype.setColumnValue = function(name, value) {
	// 单值，给定值却是数组，则取第一个
	if(value instanceof Array) { 
		value = value[0];
	}

	var tempRowNode = xmlDoc.Row;
	var node = tempRowNode.selectSingleNode(name);
	if(null == node) { 
		node = this.tempDom.createElement(name); // 创建单值节点
		tempRowNode.appendChild(node);
	}

	var tempCDATANode = node.selectSingleNode("cdata()");
	if( tempCDATANode != null) {
		tempCDATANode.text = value;
	}
	else{
		var newCDATANode = tempDom.createCDATASection(value);
		node.appendChild(newCDATANode);
	}
}

function beforeUpdateData(obj, fire) {
	var oldValue = getColumnValue(obj.binding);
	if(event.propertyName == "checked") {
		var newValue = obj.checked==true ? 1 : 0;
	}
	else if(obj.tagName.toLowerCase() == "select") {
		var newValue = obj._value;            
	}
	else {
		var newValue = obj.value;
	}

	if(newValue != oldValue && (newValue != "" || oldValue != null)) {
		clearTimeout(obj.bdcTimeout);
		obj.bdcTimeout = setTimeout(function() {
			var oEvent = createEventObject();
			oEvent.result = {
				srcElement: obj,
				name: obj.binding,
				oldValue: oldValue,
				newValue: newValue
			};
			event_onbeforedatachange.fire (oEvent);
		}, 200);
	}
}