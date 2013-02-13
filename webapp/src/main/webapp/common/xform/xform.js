

var XForm = function(element) {
	this.element = element;
	this.form = this.form;

	var pointUrl = this.element.getAttribute("baseurl");
	this._baseurl = pointUrl == null? "" : pointUrl;

	this.xmlDoc;

	this.tempDom = new ActiveXObject("MSXML.DOMDocument");
	this.tempDom.async = false;

	this.xslDom = new ActiveXObject('MSXML.DOMDocument');
	this.xslDom.async = false;
	this.xslDom.resolveExternals = false;

	this._width;
    this._height;

    this._overflow = false;

	this._instances = {};
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

function load(data, dataPath, dataType) {
	this.element.data = data;
	this.element.dataPath = dataPath;
	this.element.dataType = dataType;
	reload();
}

function loadXslDoc() {
	xslDom.load(_baseurl + "xform.xsl");
	xslDom.selectSingleNode("/xsl:stylesheet/xsl:script").text = "\r\nvar uniqueID=\"" + element.uniqueID 
		+ "\";\r\nvar baseurl=\"" + _baseurl + "\";\r\nvar formEditable=\"" + element.editable + "\";\r\n";
}

function loadXmlDoc() {	
	var data = this.element.data;
	if(data == null || data == "") return;

	var dataPath = this.element.dataPath;
	var dataType = this.element.dataType;
	switch(dataType) {
		case "url":
			tempDom.load(data);
			if(tempDom.parseError != 0) {
				alert("data地址有问题，解析XML不正确");
			}
			if(dataPath == null || dataPath == "") {
				dataPath = "/*";
			}

			var curXmlDom = tempDom.selectSingleNode(dataPath);
			if(curXmlDom == null) {
				alert("dataPath属性设置不正确或数据源有问题");
			}
			xmlDoc = new Class_XMLDocument(curXmlDom);
			break;

		case "node":
			if("object" == typeof(data) && 1 == data.nodeType) {
				var curXmlDom = data;
				xmlDoc = new Class_XMLDocument(curXmlDom);
			}
			break;
	}
}

XForm.prototype.reload = reload() {
	// 隐藏上次的错误信息层
	hideErrorInfo();

	this.loadXslDoc();
	this.loadXmlDoc();
	
	if(this.xmlDoc != null && this.xmlDoc.xmlObj != null) {
		if(element.getAttribute("actionbaseurl") != null) {
			xmlDoc.xmlObj.setAttribute("action", element.getAttribute("actionbaseurl") + xmlDoc.xmlObj.getAttribute("action"));
		}

		// 修正comboedit类型默认第一项的值
		fixComboeditDefaultValue(xmlDoc.Row);

		var str = xmlDoc.transformXML(xslDom);
		str = str.replace(/<\/br>/gi, "");

		this.element.innerHTML = str;
		
		// 载入外部定义函数
		var script = xmlDoc.Script;
		if(script != null) {
			window.execScript(script.text);
		}

		// 覆盖ondatachange事件
		var tempEvent = xmlDoc.declare.getAttribute("ondatachange");
		var event = this.element.getAttribute("ondatachange") || tempEvent;
		
		this.element.ondatachange = function() {
			if(event != null) {
				try{
					eval(tempEvent);
				} catch(e) {
					alert("指定的函数运行出错:" + tempEvent);
					throw e;
				}
			}
		}

		if(this.form != null) {
			var tempTarget = xmlDoc.xmlObj.getAttribute("target") || this.element.getAttribute("target");
			if(tempTarget != null) {
				this.form.target = tempTarget;					
			}

			this.form.attachEvent('onsubmit', checkForm);
			this.form.attachEvent('onreset', resetForm);

			//添加标题栏				
			var theTable = this.form.all.tags("TABLE")[0];
			if(theTable != null && this.element.getAttribute("caption") != null) {
				var count = theTable.rows(0).cells.length;
				for(var i = 0;i < theTable.rows(0).cells.length; i++) {
					count += parseInt(theTable.rows(0).cells(i).colSpan);
				}
				var captionTR = theTable.insertRow(0);
				var captionTD = captionTR.insertCell();
				captionTD.colSpan = count;
				captionTD.id = "titleBox";
				captionTD.className = "titleBox";
				captionTD.style.cssText = "font-size:12px;height:19px;background-image:url(" +_baseurl + "titlebg.gif);background-repeat:no-repeat;"

				setCaption(this.element.caption);

				var tempDivHeight = _height - theTable.rows[0].offsetHeight - (theTable.rows[2] == null ? 0 : theTable.rows[2].offsetHeight);
				if(tempDivHeight > 0 && _overflow == true) {
					theTable.rows(1).cells(0).firstChild.style.height = tempDivHeight;
				}
			}
			else if(theTable != null && _overflow == true) {
				theTable.rows(0).cells(0).firstChild.style.height = _height-theTable.rows[1].offsetHeight;
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
		if(this.element.autoFocus != "false" && this.element.autoFocus != false) {
			setFocus();
		}
	}
}

XForm.prototype.attachEditor = function() {
	var cols = xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var tempColName   = cols[i].getAttribute("name");
		var tempColMode   = cols[i].getAttribute("mode");
		var tempColEditor = cols[i].getAttribute("editor");
		var nodeValue = getRowNodeValue(tempColName);

		// 取layout中绑定该columne的元素
		var tempObj = this.element.all(tempColName);
		if(tempObj == null) {
			continue;
		}

		// 一般不允许多个对象绑定同一个column，如果有则只取第一个
		if(tempObj.attributes == null && tempObj.length != null) {
			tempObj = tempObj[0];
		}

		var curInstance;
		switch(tempColMode) {
			case "string":
				if(tempColEditor == "comboedit") {
					curInstance = new Mode_ComboEdit(tempColName);
				}
				else if(tempColEditor == "radio") {
					curInstance = new Mode_Radio(tempColName);
				}
				else {
					curInstance = new Mode_String(tempColName);
				}
				break;
			case "number":
				curInstance = new Mode_Number(tempColName);
				break;
			case "function":
				curInstance = new Mode_Function(tempColName);
				break;
			case "date":
				curInstance = new Mode_Date(tempColName);
				break;
			case "hidden":
				curInstance = new Mode_Hidden(tempColName);
				break;
			case "boolean":
				curInstance = new Mode_Boolean(tempColName);
				break;
			case "file":
				curInstance = new Mode_File(tempColName);
				break;
		}
		curInstance.saveasDefaultValue();
		_instances[tempColName] = curInstance;
	}
}

XForm.prototype.checkForm = function() {
	// 隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var tempColName  = cols[i].getAttribute("name");
		var tempInstance = _instances[tempColName];
		if(tempInstance != null) {
			if(tempInstance.validate() == false) {
				return false;
			}
		}
		else { // layout内不存在时创建虚拟实例执行校验
			var tempInstance = {};
			tempInstance.isInstance = false;
			tempInstance.obj = {
				isInstance: false,
				empty: cols[i].getAttribute("empty"),
				errorInfo: cols[i].getAttribute("errorInfo"),
				caption: cols[i].getAttribute("caption"),
				submitReg: cols[i].getAttribute("submitReg"),
				value: getRowNodeValue(tempColName)
			};

			tempInstance.validate = validate;
			if(tempInstance.validate() == false) {
				return false;
			}
		}
	}

	var submitCmd = xmlDoc.xmlObj.getAttribute("submitCmd");
	if( eval(submitCmd) == false ) {
		if(event != null) {
			event.returnValue = false;
		}
		return false;
	}

	var formObj = this.form;
	formObj.elements['xml'].value = getDataXMLString(this.element.submitDataXMLString);

	return true;
}

XForm.prototype.resetForm = function(fireOnDataChange) {
	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var tempColName = cols[i].getAttribute("name");
		if(_instances[tempColName] != null) {
			_instances[tempColName].reset(fireOnDataChange);
		}
	}
	if(event != null) {
		event.returnValue = false;
	}

	// 触发onreset事件
	var oEvent = createEventObject();
	event_onreset.fire(oEvent);
}

function updateData(obj, fire) {
	var oldValue = getRowNodeValue(obj.binding);
	if(event.propertyName == "checked") {
		var newValue = obj.checked == true ? 1 : 0;
	}
	else if(obj.tagName.toLowerCase() == "select") {
		var newValue = obj._value;            
	}
	else{
		var newValue = obj.value;
	}

	if(newValue != oldValue && (newValue != "" || oldValue != null)) {
		setRowNodeValue(obj.binding, newValue);

		// 触发ondatachange事件
		if(fire != false) { // 默认true
			fireDataChange(obj, oldValue, newValue);
		}
	}
}
function updateDataExternal(name, value, fire) {
	var node = getColumn(name);
	var nodeMode = node.getAttribute("mode");
	if(node.getAttribute("isConst") != "true") {
		var oldValue  = getData(name);

		setRowNodeValue(name, value);
		
		// 更改页面显示数据
		var tempSrcElement;
		var tempInstance = _instances[name];
		if(tempInstance != null) {
			tempInstance.setValue(value);
			tempSrcElement = tempInstance.obj;
		}
		else {
			tempSrcElement = { binding: name };
		}

		// 触发ondatachange事件
		if(fire != false) {
			fireDataChange(tempSrcElement, oldValue, value);
		}
	}
	else {
		alert("不允许修改该\"" + name + "\"的值");
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
			
			this.ColByName = {};
			for(var i = 0; i < this.Columns.length; i++) {
				this.ColByName[this.Columns[i].getAttribute("name")] = this.Columns[i];
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
			var tempInstance = _instances[name];
			if(tempInstance != null) {
				var columnEditable = cols[i].getAttribute("editable");
				s = (s == "true" && columnEditable != "false") ? "true": "false";
				tempInstance.setEditable(s);
			}
		}

		setFocus();
	}
}

function getData(name, replace) {
	var nodeValue = getRowNodeValue(name);
	var nodeMode  = getColumn(name).getAttribute("mode");

	if(true == replace) {
		nodeValue = nodeValue.replace(/\"/g, "&quot;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\'/g, "&#39;");
	}
	return nodeValue;
}

function submit() {
	if(this.form != null) {
		this.form.submit();
	}
}

function getFormName() {
	if(this.form != null && this.form.tagName == "FORM") {
		return this.form.attributes["name"];
	}
}

function fireDataChange(obj, oldValue, newValue) {
	var oEvent = createEventObject();
	oEvent.result = {
		srcElement: obj,
		name: obj == null ? null : obj.binding,
		oldValue: oldValue,
		newValue: newValue
	};
	event_ondatachange.fire(oEvent);
}

function getColumn(name) {
	var tempCol = xmlDoc.ColByName[name];
	if(tempCol == null) {
		alert(name + "不存在");
		throw(name + "不存在");
	}
	return tempCol;
}

function xml() {
	return xmlDoc.toString();
}

function setAction(action) {
	xmlDoc.xmlObj.setAttribute("action", action);
}

function getAction() {
	return xmlDoc.xmlObj.getAttribute("action");
}

function getDataXMLString(mode) {
	switch(mode) {
		case "data":
			return xmlDoc.Data.xml;
			break;
		default:
			return xmlDoc.toString();
			break;
	}
}

// 以column定义为获取依据
function reloadData(rowNode) {
	// 修正comboedit类型默认第一项的值
	fixComboeditDefaultValue(rowNode);

	// 隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var name = cols[i].getAttribute("name");
		var value = rowNode.getAttribute(name);
		updateDataExternal(name, value || "", false);
	}
}

function fixComboeditDefaultValue(rowNode) {
	var cols = this.xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var name = cols[i].getAttribute("name");
		var mode = cols[i].getAttribute("mode");
		var editor = cols[i].getAttribute("editor");
		var editorValue = cols[i].getAttribute("editorvalue")||"";
		var firstValue = editorValue.split("|")[0];
		var empty = cols[i].getAttribute("empty");
		var value = getRowNodeValue(name);

		// 当empty = false(表示不允许为空)时，下拉列表的默认值自动取第一项值
		if((value == null || value.length == 0) && firstValue != "" && mode=="string" && (editor=="comboedit" || editor=="radio") && empty=="false") {
			setRowNodeValue(name, firstValue);
		}
	}
}

// 设置标题栏文字
function setCaption(str) {
	var titleBox = this.element.all("titleBox");
	if(titleBox != null) {
		titleBox.innerHTML = str;
		this.element.caption = str;
	}
}

function saveasDefaultValue() {
	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	for(var i = 0; i < cols.length; i++) {
		var tempColName = cols[i].getAttribute("name");
		var tempInstance = _instances[tempColName];
		tempInstance.saveasDefaultValue();    
	}
}

function setFocus(name, index) {
	index = index || 0; // 多值时必须指定序号，默认为0

	if( name == null || name == "") {
		var column = xmlDoc.declare.selectSingleNode("column[(@editable='true' or not(@editable)) and (@display!='none' or not(@display))]");
		if(column == null) {
			return;
		}
		name = tempColumn.getAttribute("name");
	}

	var tempInstance = _instances[name];
	if(null != tempInstance) {
		tempInstance.setFocus();
	}
}

function setColumnEditable(name,s) {
	var node = getColumn(name);
	node.setAttribute("editable", s);
	
	//更改页面显示数据
	var tempInstance = _instances[name];
	if(null != tempInstance) {
		tempInstance.setEditable(s);
	}
}



function stringToNumber(str) {
	str = str.replace(/[^0-9\.\-]/g, '');
	if(str == "") {
		return 0;
	}
	return parseFloat(str);
}

function stringToDate(str, pattern) {
	var testYear  = str.substr(pattern.indexOf("yyyy"), 4);
	var testMonth = str.substr(pattern.indexOf("MM"), 2);
	var testDay   = str.substr(pattern.indexOf("dd"), 2);

	var testDate = testYear + "/" + testMonth + "/" + testDay;

	testDate = new Date(testDate);
	return new Date(testDate);
}

function numberToString(number, pattern) {
	return number.toString();
}

function dateToString(dateObj, pattern) {
	return dateObj.toString();
}

function showCustomErrorInfo(name, str, index) {
	index = index || 0;

	var instance = _instances[name];
	if(instance != null) {
		if(0 < instance.length && null != instance[index]) {
			showErrorInfo(str, instance[index].obj);
		} 
		else {
			showErrorInfo(str, instance.obj);
		}
	}
}

function getColumnAttribute(name, attrName) {
	var column = xmlDoc.ColByName[name];
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
function getRowNodeValue(name) {
	var rowNode = xmlDoc.Row;
	var node = rowNode.selectSingleNode(name);
	var nodeValue = (null == node ? null : node.text);

	return nodeValue;
}

/*
 *  函数说明：设置row节点上与column对应的值
 *  参数：  string:name             列名
			string/array:value      值
 */
function setRowNodeValue(name,value) {
	var tempRowNode = xmlDoc.Row;

	if(true==(value instanceof Array)) { // 单值，给定值却是数组，则取第一个
		value = value[0];
	}

	var node = tempRowNode.selectSingleNode(name);
	if(null == node) { // 创建单值节点
		node = tempDom.createElement(name);
		tempRowNode.appendChild(node);
	}

	var tempCDATANode = node.selectSingleNode("cdata()");
	if(null != tempCDATANode) {
		tempCDATANode.text = value;
	}
	else{
		var newCDATANode = tempDom.createCDATASection(value);
		node.appendChild(newCDATANode);
	}
}


/*
 *  函数说明：上传文件
 *  参数：  string:action           请求地址
			string:params           参数
			function:callback       回调方法
 */
function upload(action, params, callback) {
	if(xmlDoc.xmlObj.selectSingleNode("//column[@mode='file']") == null) {
		return null;
	}

	var uploadFrame = createUploadFrame();
	window.iframeOnload = function() {
		var win = window.frames[uploadFrame];  
		var response = win.response; // 获取iframe中response变量值
		if(null == response) {
			response = {};
			response.type = "Error";
			response.msg = "文件上传失败";
			response.description = "服务器没有响应。可能是连接超时，请稍后重试";
		}

		//删除iframe元素
		$(uploadFrame).removeNode(true);

		if(null != callback) {
			callback(response);
		}
	}

	var formObj = this.form;
	formObj.target = uploadFrame;
	formObj.method = "post";
	formObj.enctype  = "multipart/form-data";
	formObj.encoding = "multipart/form-data";

	if(null != params) {
		formObj.action = action + "?" + params;
	}
	formObj.submit();
}

/*
 *	创建上传提交用iframe
 */
function createUploadFrame() {
	var frameName = "frame" + new Date().valueOf();
	var frameObj = window.document.createElement("<iframe onload='if(null!=window.iframeOnload) {window.iframeOnload()}' name='" + frameName 
		+ "' id='" + frameName + "' src='about:blank' style='display:none'/>");
	this.element.appendChild(frameObj);

	return frameName;
}

function beforeUpdateData(obj, fire) {
	var oldValue = getRowNodeValue(obj.binding);
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