/*********************************** 常用函数  start **********************************/

/* 浏览器类型 */
_BROWSER_IE = "IE";
_BROWSER_FF = "FF";
_BROWSER_OPERA = "OPERA";
_BROWSER_CHROME = "CHROME";
_BROWSER = _BROWSER_IE;

/* 对象类型 */
_TYPE_NUMBER = "number";
_TYPE_OBJECT = "object";
_TYPE_FUNCTION = "function";
_TYPE_STRING = "string";
_TYPE_BOOLEAN = "boolean";

/* 默认唯一编号名前缀 */
_UNIQUE_ID_DEFAULT_PREFIX = "default__id";

/* 当前应用名 */
APP_CODE = "TSS";
CONTEXTPATH = "tss/";
APPLICATION = "tss";
URL_CORE = "/" + APPLICATION + "/common/";  // 界面核心包相对路径

URL_LOGOUT = "../logout.in";


/* 常用方法缩写 */
$ = function(id){
	return document.getElementById(id);
}

/* 对象名称：Public（全局静态对象） */
var Public = {};

Public.checkBrowser = function() {
	var ua = navigator.userAgent.toUpperCase();
	if(ua.indexOf(_BROWSER_IE)!=-1) {
		_BROWSER = _BROWSER_IE;
	}
	else if(ua.indexOf(_BROWSER_FF)!=-1) {
		_BROWSER = _BROWSER_FF;
	}
	else if(ua.indexOf(_BROWSER_OPERA)!=-1) {
		_BROWSER = _BROWSER_OPERA;
	}
	else if(ua.indexOf(_BROWSER_CHROME) != -1 ) {
		_BROWSER = _BROWSER_CHROME;
	}
}
Public.checkBrowser();

Public.executeCommand = function(callback, param) {
	var returnVal;
	try
	{
		switch (typeof(callback))
		{
		case _TYPE_STRING:
			returnVal = eval(callback);
			break;
		case _TYPE_FUNCTION:
			returnVal = callback(param);
			break;
		case _TYPE_BOOLEAN:
			returnVal = callback;
			break;
		}
	}
	catch (e)
	{
		returnVal = false;
	}
	return returnVal;
}

/* 显示等待状态 */
Public.showWaitingLayer = function () {
	var _waitingDivObj = $("_waitingDiv");
	if(_waitingDivObj == null) {
		var str = [];
		str[str.length] = "<TABLE width=\"100%\" height=\"100%\"><TR><TD align=\"center\">";
		str[str.length] = "	 <object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" ";
		str[str.length] = "		   codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0\" ";
		str[str.length] = "        width=\"140\" height=\"30\" id=\"loadingbar\" align=\"middle\">";
		str[str.length] = "		<param name=\"movie\" value=\"../images/loadingbar.swf\" />";
		str[str.length] = "		<param name=\"quality\" value=\"high\" />";
		str[str.length] = "		<param name=\"wmode\" value=\"transparent\" />";
		str[str.length] = "		<embed src=\"../images/loadingbar.swf\" quality=\"high\" ";
		str[str.length] = "		       wmode=\"transparent\" width=\"140\" height=\"30\" align=\"middle\" ";
		str[str.length] = "		       type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />";
		str[str.length] = "  </object>";
		str[str.length] = "</TD></TR></TABLE>";
		
		if(window.DOMParser) {
			var _waitingDivObj = document.createElement("div");    
			_waitingDivObj.id = "_waitingDiv";    
			_waitingDivObj.style.width ="100%";    
			_waitingDivObj.style.height = "100%";    
			_waitingDivObj.style.position = "absolute";    
			_waitingDivObj.style.left = "0px";   
			_waitingDivObj.style.top = "0px";   
			_waitingDivObj.style.cursor = "wait";   
		}
		else {
			_waitingDivObj = document.createElement('<div id="_waitingDiv" style="position:absolute;left:0px;top:0px;width:100%;height:100%;cursor:wait;align:center"></div>');
			str[str.length] = "<div style=\"background:black;filter:alpha(opacity=0);width:100%;height:100%;position:absolute;left:0;top:0;z-index:10000;\"/>";
		}

		_waitingDivObj.innerHTML = str.join("\r\n");
		document.body.appendChild(_waitingDivObj);
	}

	if(_waitingDivObj != null) {
		_waitingDivObj.style.display = "block";
	}
}

Public.hideWaitingLayer = function() {
	var _waitingDivObj = $("_waitingDiv");
	if( _waitingDivObj != null ) {
		setTimeout( function() {
			_waitingDivObj.style.display = "none";
		}, 100);
	}
}

Public.writeTitle = function() {
	if(window.dialogArguments) {
		var title = window.dialogArguments.title;
		if( title != null ) {
			document.write("<title>" + title + new Array(100).join("　") + "</title>");
		}
	}
}
Public.writeTitle();

/* 负责生成对象唯一编号（为了兼容FF） */
var UniqueID = {};
UniqueID.key = 0;

UniqueID.generator = function(prefix) {
	var uid = String(prefix || _UNIQUE_ID_DEFAULT_PREFIX) + String(this.key);
	this.key ++;
	return uid;
}

/* 缓存页面数据（xml、变量等） */
var Cache = {};
Cache.Variables = new Collection();
Cache.XmlIslands = new Collection();

/* 集合类: 类似java Map */
function Collection() {
	this.items = {};
}

Collection.prototype.add = function(id, item) {
	this.items[id] = item;
}

Collection.prototype.del = function(id) {
	delete this.items[id];
}

Collection.prototype.clear = function() {
	this.items = {};
}

Collection.prototype.get = function(id) {
	return this.items[id];
}

/*
 *	函数说明：原型继承
 *	参数：	function:Class		将被继承的类
 */
Collection.prototype.inherit = function(Class) {
	var inheritClass = new Class();
	for(var item in inheritClass) {
		this[item] = inheritClass[item];
	}
}


/* 负责管理页面上cookie数据 */
var Cookie = {};

Cookie.setValue = function(name, value, expires, path) {
	if(expires == null) {
		var exp = new Date();
		exp.setTime(exp.getTime() + 365*24*60*60*1000);
		expires = exp.toGMTString();
	}

	if(path == null) {
		path = "/";
	}
	window.document.cookie = name + "=" + escape(value) + "; expires=" + expires + "; path=" + path;
}

Cookie.getValue = function(name) {
	var cookies = window.document.cookie.split(";");
	for( var cookie in cookies ) {
		var index = cookie.indexOf("=");
		var curName = cookie.substring(0, index).replace(/^ /gi,"");
		var curValue = cookie.substring(index + 1);

		if(name == curName) {
			return unescape(curValue);
		}
	}
	return null;
}

Cookie.del = function(name, path) {
	var expires = new Date(0).toGMTString();
	this.setValue(name, "", expires, path);
}

Cookie.delAll = function(path) {
	var cookies = window.document.cookie.split(";");
	for(var cookie in cookies) {
		var index = cookie.indexOf("=");
		var curName = cookie.substring(0, index).replace(/^ /gi,"");
		this.del(curName, path);
	}
}

/* 负责获取当前页面地址参数 */
var Query = {};
Query.items = {}; // {}：Map，由key/value对组成; []:数组

Query.get = function(name, decode) {
	var str = this.items[name];
	if(decode == true) {
		str = unescape(str);
	}
	return str;
}

Query.set = function(name, value) {
	this.items[name] = value;
}

Query.parse = function(queryString) {
	var params = queryString.split("&");
	for(var param in params) {
		var name = param.split("=")[0];
		var value = param.split("=")[1];
		this.set(name, value);
	}
}

Query.init = function() {
	var queryString = window.location.search.substring(1);
	this.parse(queryString);
}

Query.init();


var Log = {};
Log.info = [];

Log.clear = function() {
	this.info = [];
}

// 写入日志信息
Log.write = function(str) {
	var index = this.info.length;
	this.info.push("[" + index + "]" + str);

	return index;
}

Log.read = function(index) {
	if(index == null) {
		return this.info.join("\r\n");
	}
	else {
		return this.info[index];
	}
}

// 扩展数组，增加数组项
Array.prototype.push = function(item) {
	this[this.length] = item;
}

String.prototype.convertEntry = function() {
	var str = this;
	str = str.repalce(/\&/g, "&amp;");
	str = str.repalce(/\"/g, "&quot;");
	str = str.repalce(/\</g, "&lt;");
	str = str.repalce(/\>/g, "&gt;");
	return str;
}

String.prototype.revertEntity = function() {
	var str = this;
	str = str.replace(/&quot;/g, "\"");
	str = str.replace(/&lt;/g,   "\<");
	str = str.replace(/&gt;/g,   "\>");
	str = str.replace(/&amp;/g,  "\&");
	return str;
}

String.prototype.convertCDATA = function() {
	var str = this;
	str = str.replace(/\<\!\[CDATA\[/g, "&lt;![CDATA[");
	str = str.replace(/\]\]>/g, "]]&gt;");
	return str;
}

String.prototype.revertCDATA = function(){
	var str = this;
	str = str.replace(/&lt;\!\[CDATA\[/g, "<![CDATA[");
	str = str.replace(/\]\]&gt;/g, "]]>");
	return str;
}

/*
 *	函数说明：根据给定字符串裁减原字符串
 *	参数：	string:trimStr  要裁减的字符串
 *	返回值：string:str      裁减后的字符串
 */
String.prototype.trim = function(trimStr){
	var str = this;
	if( 0 == str.indexOf(trimStr) ){
		str = str.substring(trimStr.length);
	}
	return str;
}

/*
 *	函数说明：按字节，从起始位置到终止位置截取
 *	参数：	number:startB       起始字节位置
			number:endB         终止字节位置
 *	返回值：string:str          截取后的字符串
 *	补充说明：当起始位置落在双字节字符中间时，强制成该字符右侧；当终止位置落在双字节字符中间时，强制成该字符左侧
 */
String.prototype.substringB = function(startB, endB){
	var str = this;

	var start , end;
	var iByte = 0;
	for(var i = 0 ; i < str.length ; i ++){

		if( iByte >= startB && null == start ){
			start = i;
		}
		if( iByte > endB && null == end){
			end = i - 1;
		}else if( iByte == endB && null == end ){
			end = i;
		}

		var chr = str.charAt(i);
		if( true == /[^\u0000-\u00FF]/.test( chr ) ){
			iByte += 2;
		}else{
			iByte ++;
		}
	}
	return str.substring(start,end);
}
/*
 *	函数说明：按字节，从起始位置开始截取指定字节数
 *	参数：	number:startB       起始字节位置
			number:lenB         截取字节数
 *	返回值：string:str          截取后的字符串
 */
String.prototype.substrB = function(startB, lenB){
	var str = this;
	return str.substringB(startB, startB + lenB);
}

/*
 *	函数说明：按字节，从起始位置开始截取指定字节数
 */
String.prototype.getBytes = function(){
	var str = this;
	return str.replace(/[^\u0000-\u00FF]/,"*").length;
}

// 扩展日期，获取四位数年份
Date.prototype.getFullYear = function() {
	var year = this.getYear();
	if(year < 1000) {
		year += 1900;
	}
	return year;
}

function convertToString(value) {
	if(value == null) {
		return "null";
	}

	var str = "";
	switch( typeof(value) ) {
		case "number":
		case "boolean":
		case "function":
			str = value.toString();
			break;
		case "object":
			if(null != value.toString){
				str = value.toString();
			} else {
				str = "[object]";
			}
			break;
		case "string":
			str = value;
			break;
		case "undefined":
			str = "";
			break;
	}
	return str;
}



/*********************************** 常用函数  end **********************************/

var Debug = {};
Debug.print = function(str, clear) {
	var debugObj = window.document.getElementById("debug");
	if(debugObj == null) {
		debugObj = window.document.createElement("textarea");
		debugObj.id = "debug";
		debugObj.readOnly = true;
		debugObj.cols = 80;
		debugObj.row = 20;
		debugObj.style.border = "1px solid gray";

		var clearObj = window.document.createElement("div");
		clearObj.style.position = "absolute";
		clearObj.style.right = "3px";
		clearObj.style.top = "0px";
		clearObj.style.fontSize = "10px";
		clearObj.style.fontFamily = "Verdana";
		clearObj.style.cursor = "hand";
		clearObj.innerHTML = "clear";

		var boxObj = window.document.createElement("div");
		boxObj.style.position = "absolute";
		boxObj.style.left = "200px";
		boxObj.style.top = "150px";
		boxObj.style.border = "1px solid gray";
		boxObj.style.paddingTop = "12px";
		boxObj.style.paddingLeft = "2px";
		boxObj.style.paddingRight = "2px";
		boxObj.style.paddingBottom = "2px";
		boxObj.style.backgroundColor = "#CCCCFF";

		window.document.body.appendChild(boxObj);
		boxObj.appendChild(debugObj);
		boxObj.appendChild(clearObj);

		clearObj.onclick = function(eventObj) {
			debugObj.value = "";
		}

		debugObj.onmousedown = function(eventObj) {
			eventObj = eventObj || event;
			eventObj.returnValue = false;

		    // 阻止事件冒泡传递，即不让父元素获取到子元素的事件。debugObj.onmousedown将不会传递到boxObj
			eventObj.cancelBubble = true;
		}

		boxObj.onblur = function(eventObj) {
			boxObj.style.display = "none"; // 失去焦点后隐藏起来
		}
		boxObj.onmousedown = function(eventObj) {
			eventObj = eventObj || event;

			this.isMouseDown = true;

			// 添加鼠标监控，弹出框跟着鼠标移动
			if (this.setCapture) {             
				this.setCapture();         
			} 
			else if(window.captureEvents){           
				window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);         
			}

			this.offX = eventObj.clientX - this.offsetLeft; 
			this.offY = eventObj.clientY - this.offsetTop;
		}
		boxObj.onmouseup = function(eventObj) {
			this.isMouseDown = false;
			
			// 释放鼠标监控
			if(this.releaseCapture){
				this.releaseCapture();
            }
			else if(window.captureEvents) {
				window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
            }
		}
		boxObj.onmousemove = function(eventObj) {
			eventObj = eventObj || event;
			if(this.isMouseDown == true) {
				this.style.left = eventObj.clientX - this.offX;
				this.style.top = eventObj.clientY - this.offY;
			}
		}

		debugObj.focus();
	}
	else {
		debugObj.parentNode.style.display = "";
		debugObj.focus();
	}

	if(true == clear) {
		debugObj.value = "";
	}

	debugObj.value += str + "\r\n";
	debugObj.scrollTop = debugObj.scrollHeight;
}


/*********************************** xml文档、节点相关操作  start **********************************/

function XmlReader(text) {
	this.xmlDom = null;

	if (window.DOMParser) {
		var parser = new DOMParser();
		this.xmlDom = parser.parseFromString(text, "text/xml");
	}
	else { // Internet Explorer
		this.xmlDom = new ActiveXObject("Msxml2.DOMDOCUMENT");
		this.xmlDom.async = false;
		this.xmlDom.loadXML(text); 
    } 

	this.documentElement = this.xmlDom.documentElement;
}

XmlReader.prototype.createElement = function(name) {
	var node = this.xmlDom.createElement(name);
	var xmlNode = new XmlNode(node);
	return xmlNode;
}

XmlReader.prototype.createCDATA = function(data) {
	var xmlNode;
	data = String(data).convertCDATA();
	if(window.DOMParser) {
		var tempReader = new XmlReader("<root><![CDATA[" + data + "]]></root>");
		var xmlNode =  new XmlNode(tempReader.documentElement.firstChild);
	}
	else {
		xmlNode = XmlNode(this.xmlDom.createCDATASection(data));
	}
	return xmlNode;
}

 XmlReader.prototype.createElementCDATA = function(name, data) {
	var xmlNode   = this.createElement(name);
	var cdataNode = this.createCDATA(data);
	xmlNode.appendChild(cdataNode);
	return xmlNode;
}

XmlReader.prototype.load = function(url, async) {
	if(window.DOMParser) {

	}
	else {
		var thisObj = this;
		this.xmlDom.async = async;
		this.xmlDom.onreadystatechange = function() {
			if(thisObj.xmlDom.readyState == 4) {
				var onloadType = typeof(thisObj.onload);
				try
				{
					if(onloadType == "function") {
						thisObj.onload();
					} 
					else if(onloadType == "string") {
						eval(thisObj.onload);
					}
				}
				catch (e)
				{
				}
			}
		}
		this.xmlDom.load(url);
	}

	this.documentElement = this.xmlDom.documentElement;
}

XmlReader.prototype.toString = function() {
	var str = [];
	str[str.length] = "[XmlReader Object]";
	str[str.length] = "xml:" + this.toXml();
	return str.join("\r\n");
}

XmlReader.prototype.toXml = function() {
	var str = "";
	if(window.DOMParser) { 
		var xmlSerializer = new XMLSerializer();
        str = xmlSerializer.serializeToString(this.xmlDom.documentElement);
	}
	else {
		str = this.xmlDom.xml;
	}
	return str;
}


var ELEMENT_NODE_TYPE = "1";  // 元素
var ATTRIBUTE_NODE_TYPE = "2"; // 属性
var TEXT_NODE_TYPE = "3"; // 文本
var COMMENTS_NODE_TYPE = "8"; // 注释
var DOCUMENT_NODE_TYPE = "9"; // 文档

/* XML Node */
function XmlNode(node) {
	this.node = node;
	this.nodeName = node.nodeName;
	this.nodeType = node.nodeType;
	this.nodeValue = node.nodeValue;
	this.text = node.text;
	this.firstChild = node.firstChild;
	this.lastChild = node.lastChild;
	this.childNodes = node.childNodes;
	this.attributes = node.attributes;
}

XmlNode.prototype.getAttribute = function(name) {
	if(ELEMENT_NODE_TYPE == this.nodeType) {
		return this.node.getAttribute(name);
	}
}

XmlNode.prototype.setAttribute = function(name, value, isCDATA) {
	if(ELEMENT_NODE_TYPE != this.nodeType) {
		return;
	}

	if(value == null) {
		if(isCDATA == 1) {
			this.removeCDATA(name);
		}
		else {
			this.removeAttribute(name);
		}
	}
	else {
		if(isCDATA == 1) {
			this.setCDATA(name, value);
		}
		else {
			this.node.setAttribute(name, value);
		}
	}
}

/* 删除节点属性 */
XmlNode.prototype.removeAttribute = function(name) {
	if(ELEMENT_NODE_TYPE == this.nodeType) {
		return this.node.removeAttribute(name);
	}
}

XmlNode.prototype.getCDATA = function(name) {
	var node = this.selectSingleNode(name + "/node()");
	if(node != null) {
		return node.nodeValue.revertCDATA();
	}
}

XmlNode.prototype.setCDATA = function(name, value) {
	var oldNode = this.selectSingleNode(name);
	if(oldNode == null) {
		var xmlReader = new XmlReader("<xml/>");
		var newNode = xmlReader.createElementCDATA(name, value);
		this.appendChild(newNode);
	}
	else {
		var CDATANode = oldNode.selectSingleNode("node()");
		CDATANode.removeNode();

		var xmlReader = new XmlReader("<xml/>");
		CDATANode = xmlReader.createCDATA(value);
		oldNode.appendChild(CDATANode);
	}
}

XmlNode.prototype.removeCDATA = function(name) {
	var node = this.selectSingleNode(name);
	if(node != null) {
		node.removeNode(true);
	}
}

XmlNode.prototype.cloneNode = function(deep) {
	var tempNode;
	if(window.ActiveXObject) {
		tempNode = new XmlNode(this.node.cloneNode(deep));
	} else {
		tempNode = new XmlNode(new XmlReader(this.toXml()).documentElement);
	}
	return tempNode;
}

XmlNode.prototype.getParent = function() {
	var xmlNode = null;
	if( this.node.parentNode != null ) {
		xmlNode = new XmlNode(this.node.parentNode);
	}
	return xmlNode;
}

XmlNode.prototype.removeNode = function() {
	var parentNode = this.node.parentNode;
	if(parentNode != null) {
		parentNode.removeChild(this.node);
	}
}

XmlNode.prototype.selectSingleNode = function(xpath) {
	var xmlNode = null;
	if(window.DOMParser) {
		var xPath;   
        var xresult = this.ownerDocument.evaluate(xPath, this.node  
            , null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);   
        if(xresult && xresult.snapshotLength > 0) {   
           return xresult.snapshotItem(0);   
        }     
	} 
	else {
		var node = this.node.selectSingleNode(xpath);
		if(node != null) {
			xmlNode = new XmlNode(node);
		}
	}
	return xmlNode;
}

XmlNode.prototype.appendChild = function(xmlNode) {
	this.node.appendChild(xmlNode.node);

	this.nodeValue = this.node.nodeValue;
	this.text = this.node.text;
	this.firstChild = this.node.firstChild;
	this.lastChild = this.node.lastChild;
	this.childNodes = this.node.childNodes;
}

XmlNode.prototype.getFirstChild = function() {
	if(this.firstChild) {
		var node = new XmlNode(this.firstChild);
		return node;
	}
	return null;
}

XmlNode.prototype.getLastChild = function() {
	if(this.lastChild) {
		var node = new XmlNode(this.lastChild);
		return node;
	}
	return null;
}

// 交换子节点
XmlNode.prototype.replaceChild = function(newNode, oldNode) {
	var oldParent = oldNode.getParent();
	if(oldParent != null && oldParent.equals(this)) {
		try
		{
			this.node.replaceChild(newNode.node, oldNode.node);
		}
		catch (e)
		{
		}
	}
}
		

// 交换节点
XmlNode.prototype.swapNode = function(xmlNode) {
	var parentNode = this.getParent();
	if(parentNode != null) {
		parentNode.replaceChild(xmlNode, this);
	}
}

/*
 *	获取前一个兄弟节点
 */
XmlNode.prototype.getPrevSibling = function() {
	var xmlNode = null;
	if(null!=this.node.previousSibling) {
		xmlNode = new XmlNode(this.node.previousSibling);
	}
	return xmlNode;
}

/*
 * 获取后一个兄弟节点
 */
XmlNode.prototype.getNextSibling = function() {
	if(this.node.nextSibling != null) {
		var node = new XmlNode(this.node.nextSibling);
		return node;
	}
	return null;
}

XmlNode.prototype.equals = function(xmlNode) {
	return null != xmlNode && this.node == xmlNode.node;
}


XmlNode.prototype.toString = function() {
	var str = [];
	str[str.length] = "[XmlNode]";
	str[str.length] = "nodeName:" + this.nodeName;
	str[str.length] = "nodeType:" + this.nodeType;
	str[str.length] = "nodeValue:" + this.nodeValue;
	str[str.length] = "xml:" + this.toXml();
	return str.join("\r\n");
}

XmlNode.prototype.toXml = function() {
	if(window.DOMParser) {
		var xs = new XMLSerializer();
		return xs.serializeToString(this.node);
	}
	else {
		return this.node.xml
	}
}

/*********************************** xml文档、节点相关操作  end **********************************/