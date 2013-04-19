
/* 当前应用名 */
APP_CODE = "TSS";
CONTEXTPATH = "tss/";
APPLICATION = "tss";

// URL_CORE = "/" + APPLICATION + "/common/";  // 界面核心包相对路径
URL_CORE = "common/";  // 界面核心包相对路径

URL_LOGOUT = "../logout.in";


/***************************************** xmlhttp start ****************************************/

/* 错误类型 */
_ERROR_TYPE_OPERATION_EXCEPTION = 0;
_ERROR_TYPE_KNOWN_EXCEPTION = 1;
_ERROR_TYPE_UNKNOWN_EXCEPTION = 2;

/* 通讯用XML节点名 */
_XML_NODE_RESPONSE_ROOT    = "Response";
_XML_NODE_REQUEST_ROOT     = "Request";
_XML_NODE_RESPONSE_ERROR   = "Error";
_XML_NODE_RESPONSE_SUCCESS = "Success";
_XML_NODE_REQUEST_NAME     = "Name";
_XML_NODE_REQUEST_VALUE    = "Value";
_XML_NODE_REQUEST_PARAM    = "Param";

/* HTTP响应状态 */
_HTTP_RESPONSE_STATUS_LOCAL_OK = 0;
_HTTP_RESPONSE_STATUS_REMOTE_OK = 200;

/* HTTP响应解析结果类型 */
_HTTP_RESPONSE_DATA_TYPE_EXCEPTION = "exception";
_HTTP_RESPONSE_DATA_TYPE_SUCCESS = "success";
_HTTP_RESPONSE_DATA_TYPE_DATA = "data";

/* HTTP超时(1分钟) */
_HTTP_TIMEOUT = 1*1000;

/*
 *  XMLHTTP请求参数对象，负责配置XMLHTTP请求参数
 */
function HttpRequestParams() {
	this.url = "";
	this.method = "POST";
	this.async = true;
	this.content = {};
	this.header = {};
}

/*
 *	函数说明：设置发送数据
 *	参数：  string:name 		数据字段名
			string:value        数据内容
 */
HttpRequestParams.prototype.setContent = function(name, value) {
	this.content[name] = value;
}

/*
 *	函数说明：设置xform专用格式发送数据
 *	参数：	XmlNode:dataNode 	XmlNode实例，xform的data数据节点
			string:prefix 	    提交字段前缀
 */
HttpRequestParams.prototype.setXFormContent = function(dataNode, prefix) {
	if(dataNode.nodeName != "data") return;

	var rename = dataNode.getAttribute(name);
	var nodes = dataNode.selectNodes("./row/*");
	for(var i = 0; i < nodes.length; i++) {
		var name = rename || nodes[i].nodeName; // 从data节点上获取保存名，如果没有则用原名
		var value = nodes[i].text;
		
		// 前缀，xform declare节点上设置，以便于把值设置到action的bean对象里
		if(null != prefix){
			name = prefix + "." + name;
		}

		this.setContent(name, value, false);
	}
}

/*
 *	函数说明：清除制定名称的发送数据
 *	参数：	string:name 		数据字段名
 */
HttpRequestParams.prototype.clearContent = function(name) {
	delete this.content[name];
}

/*
 *	函数说明：清除所有发送数据
 */
HttpRequestParams.prototype.clearAllContent = function() {
	this.content = {};
}

/*
 *	函数说明：设置请求头信息
 *	参数：	string:name 		头信息字段名
			string:value        头信息内容
 */
HttpRequestParams.prototype.setHeader = function(name, value) {
	this.header[name] = value;
}


/*
 *  XMLHTTP请求对象，负责发起XMLHTTP请求并接收响应数据
	例子：
		var p = new HttpRequestParams();
		p.url = URL_GET_USER_NAME;
		p.setContent("loginName", loginName);
		p.setHeader("appCode", APP_CODE);

		var request = new HttpRequest(p);
		request.onresult = function(){
 
		}
		request.send();
 */
function HttpRequest(paramsInstance) {
	this.value = "";

	this.xmlhttp = new XmlHttp();
	this.xmlReader  = new XmlReader();

	this.params = paramsInstance;
}

/*
 *	函数说明：获取响应数据源代码
 *	参数：	
 *	返回值：string:result       响应数据源代码
 */
HttpRequest.prototype.getResponseText = function() {
	return this.value;
}

/*
 *	函数说明：获取响应数据XML文档对象
 *	参数：	
 *	返回值：XmlReader:xmlReader       XML文档对象
 */
HttpRequest.prototype.getResponseXml = function() {
	return this.xmlReader;
}

/*
 *	函数说明：获取响应数据XML文档指定节点对象值
 *	参数：	string:name             指定节点名
 *	返回值：any:value               根据节点内容类型不同而定
 */
HttpRequest.prototype.getNodeValue = function(name) {
	if(this.xmlReader.documentElement == null) return;

	var documentElement = new XmlNode(this.xmlReader.documentElement);
	var node = documentElement.selectSingleNode("/" + _XML_NODE_RESPONSE_ROOT + "/" + name);
	if(node == null) return;

	var data;
	var datas = node.selectNodes("node()");
	for(var i = 0; i < datas.length; i++) {
		var temp = datas[i];
		switch (temp.nodeType)
		{
			case _XML_NODE_TYPE_TEXT:
				if(temp.nodeValue.replace(/\s*/g, "") != "") {
					data = temp;
				}
				break;
			case _XML_NODE_TYPE_ELEMENT:
			case _XML_NODE_TYPE_CDATA:
				data = temp;
				break;
		}
		
		if(data != null) break;
	}

	if(data != null) {
		data = data.cloneNode(true); // 返回复制节点，以便清除整个原始文档
		switch(data.nodeType) {
			case _XML_NODE_TYPE_ELEMENT:
				return data;
			case _XML_NODE_TYPE_TEXT:
			case _XML_NODE_TYPE_CDATA:
				return data.nodeValue;
		}
	}
	return null
}

/*
 * 函数说明：发起XMLHTTP请求
 * 参数：boolean  是否等待其余请求完成再发送
 * 返回值：
 */

 HttpRequest.prototype.send = function(wait) {
	 var oThis = this;

	 if(wait) {
		 var count = HttpRequests.getCount();
		 if(count == 0) {
			 oThis.send();
		 }
		 else {
			 HttpRequests.onFinishAll( function() {
				 oThis.send();
			 });
		 }
		 return;
	 }
	
	 try {
		 if(this.params.ani != null) {
			 Public.showWaitingLayer();
		 }

		 this.xmlhttp.onreadystatechange = function() {
			 if(oThis.xmlhttp.readyState == 4) {
				 oThis.clearTimeout();

				 var response = {};
				 response.responseText = oThis.xmlhttp.responseText;
				 response.responseXML  = oThis.xmlhttp.responseXML;
				 response.status       = oThis.xmlhttp.status;
				 response.statusText   = oThis.xmlhttp.statusText;

				 if(oThis.isAbort != true) {
					 setTimeout( function() {
						 oThis.abort();

						 Public.hideWaitingLayer();
						 oThis.onload(response);
						 
						 HttpRequests.del(oThis); // 从队列中去除
						 oThis.executeCallback();
					 }, 100);
				 }
				 else {
					 Public.hideWaitingLayer();

					 HttpRequests.del(oThis);  // 从队列中去除
					 oThis.executeCallback();
				 }
			 }
		 }

		 this.xmlhttp.open(this.params.method, this.params.url, this.params.async);
		 this.setTimeout(); // 增加超时判定
		 this.packageContent();
		 this.setCustomRequestHeader();
		 this.xmlhttp.send(this.requestBody);

		 HttpRequests.add(this); // 存入队列

	 }
	 catch (e) {
		 Public.hideWaitingLayer();

		 //throw e;
		 var parserResult = {};
		 parserResult.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION;
		 parserResult.type = 1;
		 parserResult.msg = e.description;
		 parserResult.description = e.description;
		 parserResult.source = "";

		 this.onexception(parserResult);
	 }
 }

/*
 *	函数说明：超时中断请求
 */
HttpRequest.prototype.setTimeout = function(noConfirm) {
	var oThis = this;

	this.timeout = setTimeout(function() {
		if(noConfirm != true && confirm("服务器响应较慢，需要中断请求吗？") == true) {
			oThis.isAbort = true;
			oThis.abort();
			oThis.isAbort = false;
		}
		else {
			oThis.clearTimeout();
			oThis.setTimeout(true);
		}
	}, _HTTP_TIMEOUT);
}

/*
 *	函数说明：清除超时
 */
HttpRequest.prototype.clearTimeout = function() {
	clearTimeout(this.timeout);
}

/*
 *	函数说明：对发送数据进行封装，以XML格式发送
 */
HttpRequest.prototype.packageContent = function() {
	var contentXml = new XmlReader("<" + _XML_NODE_REQUEST_ROOT+"/>");
	var contentXmlRoot = new XmlNode(contentXml.documentElement);

	function setParamNode(name, value) {
		var tempNameNode  = contentXml.createElement(_XML_NODE_REQUEST_NAME);
		var tempCDATANode = contentXml.createCDATA(name);
		tempNameNode.appendChild(tempCDATANode);

		var tempValueNode = contentXml.createElement(_XML_NODE_REQUEST_VALUE);
		var tempCDATANode = contentXml.createCDATA(value);
		tempValueNode.appendChild(tempCDATANode);

		var tempParamNode = contentXml.createElement(_XML_NODE_REQUEST_PARAM);
		tempParamNode.appendChild(tempNameNode);
		tempParamNode.appendChild(tempValueNode);

		contentXmlRoot.appendChild(tempParamNode);
	}

	for(var name in this.params.content) {
		var value = this.params.content[name];
		if(value == null) {
			continue;
		}

		setParamNode(name, value);
	}

	var contentStr = contentXml.toXml();
	this.xmlhttp.setRequestHeader("Content-Length", contentStr.length);
	this.requestBody = contentStr;
}

/*
 *	函数说明：设置自定义请求头信息
 */
HttpRequest.prototype.setCustomRequestHeader = function() {
	this.xmlhttp.setRequestHeader("REQUEST-TYPE", "xmlhttp");
	this.xmlhttp.setRequestHeader("REFERER", this.params.url);
	for(var item in this.params.header) {									
		var itemValue = String(this.params.header[item]);
		if( itemValue != "" ) {
			this.xmlhttp.setRequestHeader(item, itemValue);
		}
	}

	// 当页面url具有参数token则加
	var token = Query.get("token");
	if( token != null ) {
		var exp = new Date();  
		exp.setTime(exp.getTime() + (30*1000));
		var expires = exp.toGMTString();  // 过期时间设定为30s
		Cookie.setValue("token", token, expires, "/" + CONTEXTPATH);
	}
	this.xmlhttp.setRequestHeader("CONTENT-TYPE","text/xml");
	this.xmlhttp.setRequestHeader("CONTENT-TYPE","application/octet-stream");
}

/*
 *	函数说明：加载数据完成，对结果进行处理
 *	参数：	Object:response     该对象各属性值继承自xmlhttp对象
 */
HttpRequest.prototype.onload = function(response) {
	this.value = response.responseText;

	//远程(200) 或 本地(0)才允许
	var httpStatus = response.status;
	var httpStatusText = response.statusText;
	if(httpStatus != _HTTP_RESPONSE_STATUS_LOCAL_OK && httpStatus != _HTTP_RESPONSE_STATUS_REMOTE_OK) {
		var param = {};
		param.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION;
		param.type = 1;
		param.source = this.value;
		param.msg = "HTTP " + httpStatus + " 错误\r\n" + httpStatusText;
		param.description = "请求远程地址\"" + this.params.url + "\"出错";
		new Message_Exception(param, this);
		this.returnValue = false;
		return;
	}

	var responseParser = new HTTP_Response_Parser(this.value);

	// 将通过解析后的xmlReader
	this.xmlReader = responseParser.xmlReader;

	if(responseParser.result.dataType ==_HTTP_RESPONSE_DATA_TYPE_EXCEPTION) {
		new Message_Exception(responseParser.result, this);
		this.returnValue = false;
	}
	else if(responseParser.result.dataType==_HTTP_RESPONSE_DATA_TYPE_SUCCESS) {
		new Message_Success(responseParser.result, this);
		this.returnValue = true;
	}
	else {
		this.ondata();
		this.onresult();
		this.returnValue = true;

		// 当返回数据中含脚本内容则自动执行
		var script = this.getNodeValue("script");
		if( script != null) {
			Element.createScript(script); // 创建script元素并添加到head中.
		}
	}

	// 清除原始文档
	this.xmlReader.xmlDom.loadXML("");
}

HttpRequest.prototype.ondata = HttpRequest.prototype.onresult = HttpRequest.prototype.onsuccess = HttpRequest.prototype.onexception = function() {

}

/*
 *	函数说明：终止XMLHTTP请求
 */
HttpRequest.prototype.abort = function() {
	if(null != this.xmlhttp) {
		this.xmlhttp.abort();
	}
}

/*
 *	函数说明：执行回调函数
 */
HttpRequest.prototype.executeCallback = function() {
	if( HttpRequests.getCount() == 0 && HttpRequests.callback != null ) {
		HttpRequests.callback();
		HttpRequests.callback = null;
	}
}


/*
 *  对象名称：HTTP_Response_Parser对象
 *  职责：负责分析处理后台响应数据
 *
 *  成功信息格式
 *  <Response>
 *      <Success>
 *          <type>1</type>
 *          <msg><![CDATA[ ]]></msg>
 *          <description><![CDATA[ ]]></description>
 *      </Success>
 *  </Response>
 *
 *  错误信息格式
 *  <Response>
 *      <Error>
 *          <type>1</type>
 *          <relogin>1</relogin>
 *          <msg><![CDATA[ ]]></msg>
 *          <description><![CDATA[ ]]></description>
 *      </Error>
 *  </Response>
 */
function HTTP_Response_Parser(responseText) {
	this.source = responseText;
	this.xmlReader = new XmlReader(responseText);
 
	this.result = {};
	var parseError = this.xmlReader.getParseError();
	if( parseError != null) {
		this.result.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION;
		this.result.source = this.source;
		this.result.msg = "服务器异常";
		this.result.description = "数据出错在第" + parseError.line + "行第" + parseError.linepos + "字符\r\n" + parseError.reason;
	} 
	else {
		var documentNode = new XmlNode(this.xmlReader.documentElement);
		var informationNode = documentNode.selectSingleNode("/" + _XML_NODE_RESPONSE_ROOT + "/*");
		var hasInformation = false;

		if( informationNode == null) {		
			this.result.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION; // 未找到有效节点则认为是异常信息
		}
		else if(informationNode.nodeName == _XML_NODE_RESPONSE_ERROR) { // 只要有Error节点就认为是异常信息
			this.result.dataType = _HTTP_RESPONSE_DATA_TYPE_EXCEPTION;
			this.result.source = this.source;
			hasInformation = true;
		}
		else if(informationNode.nodeName == _XML_NODE_RESPONSE_SUCCESS) { //只要有Success就认为是成功信息
			this.result.dataType = _HTTP_RESPONSE_DATA_TYPE_SUCCESS;
			hasInformation = true;
		} 
		else {
			this.result.dataType = _HTTP_RESPONSE_DATA_TYPE_DATA;
		}

		if(hasInformation) {
			var detailNodes = informationNode.selectNodes("*");
			for(var i = 0; i < detailNodes.length; i++) {
				var tempName  = detailNodes[i].nodeName;
				var tempValue = detailNodes[i].text;
				this.result[tempName] = tempValue;
			}
		}
	}
}


/*
 *  对象名称：XmlHttp对象，负责XmlHttp对象创建
 */
function XmlHttp() {
	if(window.ActiveXObject) {
		return new ActiveXObject("MSXML2.XMLHTTP");
	} 
	else if(window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} 
	else {
		alert("您的浏览器不支持XMLHTTP");
		return null;
	}
}

/*
 *  对象名称：Message_Success对象
 *  职责：负责处理成功信息
 */
function Message_Success(param, request) {
	request.ondata();

	var str = [];
	str[str.length] = "Success";
	str[str.length] = "type=\"" + param.type + "\"";
	str[str.length] = "msg=\"" + param.msg + "\"";
	str[str.length] = "description=\"" + param.description + "\"";

	if(param.type != "0" && request.params.type != "0") {
		alert(param.msg, str.join("\r\n"));
	}

	request.onsuccess(param);
}

/*
 *  对象名称：Message_Exception对象
 *  职责：负责处理异常信息
 *
 *  注意：本对象除了展示异常信息（通过alert方法，window.alert=Alert，Alert在framework.js里做了重新定义）外，
 *  还可以根据是否需要重新登录来再一次发送request请求，注意此处参数Message_Exception(param, request)，该
 *  request依然还是上一次发送返回异常信息的request，将登陆信息加入后（loginName/pwd等，通过_relogin.htm页面获得），
 *  再一次发送该request请求，从而通过AutoLoginFilter的验证，取回业务数据。  
 *  这样做的好处是，当session过期需要重新登陆时，无需离开当前页面回到登陆页登陆，保证了用户操作的连贯性。
 */
function Message_Exception(param, request) {
	request.ondata();

	var str = [];
	str[str.length] = "Error";
	str[str.length] = "type=\"" + param.type + "\"";
	str[str.length] = "msg=\"" + param.msg + "\"";
	str[str.length] = "description=\"" + param.description + "\"";
	str[str.length] = "source=\"" + param.source + "\"";

	if(param.type != "0" && request.params.type != "0") {
		alert(param.msg, str.join("\r\n"));
	}

	request.onexception(param);

	//初始化默认值
	if( request.params.relogin != null) {
		param.relogin = request.params.relogin;
	}
	else if( param.relogin == null ) { // 默认不重新登录
		param.relogin = "0";
	}

	if(param.relogin == "1") {
		Cookie.del("token","/" + CONTEXTPATH); // 先清除令牌

		var loginObj = window.showModalDialog(URL_CORE + "_relogin.htm", {title:"请重新登录"},"dialogWidth:250px;dialogHeight:200px;resizable:yes");
		if( loginObj != null) {
			var p = request.params;
			p.setHeader("loginName", loginObj.loginName);
			p.setHeader("password",  loginObj.password);
			p.setHeader("identifier", loginObj.identifier);

			request.send();
		}
	}
	else if(param.relogin == "2" ) { // 单点登录应用跳转，需要输入用户在目标系统中的密码
		var loginObj = window.showModalDialog(URL_CORE + "_relogin2.htm",{title:"请重新输入密码"},"dialogWidth:250px;dialogHeight:200px;resizable:yes");
		if(loginObj != null) {
			request.params.setHeader("pwd", loginObj.password);
			request.send();
		}
	}
}


/*
 *	对象名称：HttpRequests（全局静态对象）
 *	职责：负责所有http请求连接
 */
var HttpRequests = {};
HttpRequests.items = [];

/*
 *	函数说明：终止所有请求连接
 */
HttpRequests.closeAll = function() {
	for(var i = 0; i < this.items.length; i++) {
		this.items[i] = true;
		this.items[i].abort();
		this.items[i] = false;
	}
}

/*
 *	函数说明：加入一个请求连接
 */
HttpRequests.add = function(request) {
	this.items[this.items.length] = request;
}

/*
 *	函数说明：去除一个请求连接
 */
HttpRequests.del = function(request) {
	for(var i = 0; i < this.items.length; i++) {
		if(this.items[i] == request ) {
			this.items.splice(i, 1); // splice() 方法用于插入、删除或替换数组的元素
			break;
		}
	}
}

/*
 *	函数说明：统计当前连接数
 */
HttpRequests.getCount = function() {
	return this.items.length;
}

/*
 *	函数说明：等待当前请求全部结束
 */
HttpRequests.onFinishAll = function(callback) {
	this.callback = callback;
}


/*
 *  对象名称：Ajax请求对象
 *  职责：再次封装，简化xmlhttp使用
 */
function Ajax() {
	var arg = arguments[0];

	var p = new HttpRequestParams();
	p.url = arg.url;

	for(var item in arg.headers) {
		p.setHeader(item, arg.headers[item]);
	}
	for(var item in arg.contents) {
		p.setContent(item, arg.contents[item]);
	}

	var request = new HttpRequest(p);
	if(arg.onresult != null) {
		request.onresult = arg.onresult;
	}
	if(arg.onexception != null) {
		request.onexception = arg.onexception;
	}
	if(arg.onsuccess != null) {
		request.onsuccess = arg.onsuccess;
	}
	request.send();

	return request;
}

/***************************************** xmlhttp end ****************************************/

/*
 *	函数说明：重新封装alert
 *	参数：	string:info     简要信息
			string:detail   详细信息
 */
function Alert(info, detail) {
	info = convertToString(info);
	detail = convertToString(detail);

	var maxWords = 100;
	var params = {};
	params.type = "alert";
	params.info = info;
	params.detail = detail;
	if("" == detail && maxWords < info.length) {
		params.info = info.substring(0, maxWords) + "...";
		params.detail = info;        
	}
	params.title = "";
	window.showModalDialog(URL_CORE + '_info.htm', params, 'dialogwidth:280px; dialogheight:150px; status:yes; help:no;resizable:yes;unadorned:yes');
}

/*
 *	函数说明：重新封装confirm
 *	参数：	string:info             简要信息
			string:detail           详细信息
 *	返回值：boolean:returnValue     用户选择确定/取消
 */
function Confirm(info,detail) {
	info = convertToString(info);
	detail = convertToString(detail);

	var maxWords = 100;
	var params = {};
	params.type = "confirm";
	params.info = info;
	params.detail = detail;
	if("" == detail && maxWords<info.length) {
		params.info = info.substring(0, maxWords) + "...";
		params.detail = info;        
	}
	params.title = "";
	var returnValue = window.showModalDialog(URL_CORE + '_info.htm', params, 'dialogwidth:280px; dialogheight:150px; status:yes; help:no;resizable:yes;unadorned:yes');
	return returnValue;
}

/*
 *	函数说明：带是/否/取消三个按钮的对话框
 *	参数：	string:info             简要信息
			string:detail           详细信息
 *	返回值：boolean:returnValue     用户选择是/否/取消
 */
function Confirm2(info,detail) {
	info = convertToString(info);
	detail = convertToString(detail);

	var maxWords = 100;
	var params = {};
	params.type = "confirm2";
	params.info = info;
	params.detail = detail;
	if("" == detail && maxWords < info.length) {
		params.info = info.substring(0, maxWords) + "...";
		params.detail = info;        
	}
	params.title = "";
	var returnValue = window.showModalDialog(URL_CORE + '_info.htm', params, 'dialogwidth:280px; dialogheight:150px; status:yes; help:no;resizable:yes;unadorned:yes');
	return returnValue;
}

/*
 *	函数说明：重新封装prompt
 *	参数：	string:info             简要信息
			string:defaultValue     默认值
			string:title            标题
			boolean:protect         是否保护
			number:maxBytes         最大字节数
 *	返回值：string:returnValue      用户输入的文字
 */
function Prompt(info, defaultValue, title, protect, maxBytes) {
	info = convertToString(info);
	defaultValue = convertToString(defaultValue);
	title = convertToString(title);

	var params = {};
	params.info = info;
	params.defaultValue = defaultValue;
	params.title = title;
	params.protect = protect;
	params.maxBytes = maxBytes;
	var returnValue = window.showModalDialog(URL_CORE + '_prompt.htm', params, 'dialogwidth:280px; dialogheight:150px; status:yes; help:no;resizable:no;unadorned:yes');
	return returnValue;
}

/*
 *	函数说明：捕获页面js报错
 */
function onError(msg,url,line) {
	alert(msg, "错误:" + msg + "\r\n行:" + line + "\r\n地址:" + url);
	event.returnValue = true;
}

window._alert = window.alert;
window._confirm = window.confirm;
window._prompt = window.prompt;

// window.alert = Alert;
window.confirm = Confirm;
window.confirm2 = Confirm2;
window.prompt = Prompt;
window.onerror = onError;

document.oncontextmenu = function(eventObj) {
	eventObj = eventObj || window.event;
	var srcElement = Event.getSrcElement(eventObj);
	var tagName = srcElement.tagName.toLowerCase();
	if("input" != tagName && "textarea" != tagName) {
		event.returnValue = false;            
	}
}


/*
 *	函数说明：用户信息初始化
 */
function initUserInfo() {
	var p = new HttpRequestParams();
	p.url = "ums/user!getOperatorInfo.action";
	p.setHeader("appCode", APP_CODE);
	p.setHeader("anonymous", "true");

	var request = new HttpRequest(p);
	request.onresult = function() {
		var userName = this.getNodeValue("name");
		$("userInfo").innerText = userName;
	}
	request.send();
}

function logout() {
	var p = new HttpRequestParams();
	p.url = URL_CORE + URL_LOGOUT;

	var request = new HttpRequests(p);
	request.onsuccess = function() {
		Cookie.del("token", "/" + CONTEXTPATH);
		location.href = URL_CORE + "../login.htm";
	}
	request.send();
}

// 关闭页面时候自动注销
function logoutOnClose() {
	window.attachEvent("onuload", function() {
		if(10*1000 < window < screenTop || 10*1000 < window.screenLeft) {
			logout();
		}
	});
}





// 离开提醒
var Reminder = {};

Reminder.items = {};   // 提醒项
Reminder.count = 0;
Reminder.flag  = true; // 是否要提醒

Reminder.del = function(id) {
	if(this.items[id] != null) {
		delete this.item[id];
		this.count--;
	}
}

Reminder.remind = function() {
	if(this.getCount() > 0) {
		alert("当然有 <" + this.count + ">项修改未保存，请先保存");
	}
}

/*
 * 函数说明：统计提醒项
 */
Reminder.getCount = function() {
	if( true== this.flag) {
		return this.count;
	} else {
		return 0;
	}
}

/*
 * 函数说明：取消提醒
 */
Reminder.cancel = function() {
	this.flag = false;
}

/*
 * 函数说明：允许提醒
 */
Reminder.restore = function() {
	this.flag = true;
}

window.attachEvent("onbeforeunload", function() {
	if(Reminder.getCount() > 0) {            
		event.returnValue = "当前有 <" + count + "> 项修改未保存，您确定要离开吗？";
	}
});

/* 函数说明：给xform等添加离开提醒 */
function attachReminder(id, xform) {
	if(xform != null) {
		xform.ondatachange = function() {
			Reminder.add(id); // 数据有变化时才添加离开提醒
		}
	}
	else {
		Reminder.add(id);
	}
}

/*
 *	函数说明：大数据显示进度
 *	参数：	string:url                      同步进度请求地址
			xmlNode:data                    XmlNode实例
 *	返回值：
 */
var Progress = function(url, data, cancelUrl) {
	this.progressUrl = url;
	this.cancelUrl = cancelUrl;
	this.id = UniqueID.generator();
	this.refreshData(data);
}

/*
 *	函数说明：更新数据
 */
Progress.prototype.refreshData = function(data) {
	this.percent      = data.selectSingleNode("./percent").text;
	this.delay        = data.selectSingleNode("./delay").text;
	this.estimateTime = data.selectSingleNode("./estimateTime").text;
	this.code         = data.selectSingleNode("./code").text;

	var feedback = data.selectSingleNode("./feedback");
	if(feedback != null) {
		alert(feedback.text);
	}
}

/*
 *	开始执行
 */
Progress.prototype.start = function() {
	this.show();
	this.next();
}

/*
 *	停止执行
 */
Progress.prototype.stop = function() {
	var p = new HttpRequestParams();
	p.url = this.cancelUrl;
	p.setContent("code", this.code);

	var thisObj = this;
	var request = new HttpRequest(p);
	request.onsuccess = function() {
		thisObj.hide();
		clearTimeout(thisObj.timeout);
	}
	request.send();
}

/*
 *	函数说明：显示进度
 *	参数：
 *	返回值：
 */
Progress.prototype.show = function() {
	var thisObj = this;
	var barObj = $(this.id);
	if(null == barObj) {
		barObj = Element.createElement("div");
		barObj.id = this.id;
		barObj.style.width = "200px";
		barObj.style.height = "50px";
		barObj.style.paddingRight = "3px";
		barObj.style.paddingTop = "8px";
		barObj.style.position = "absolute";
		barObj.style.color = "#5276A3";
		barObj.style.textAlign = "center";
		barObj.style.visibility = "hidden";
		document.body.appendChild(barObj);

		barObj.innerHTML = "<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0\" width=\"140\" height=\"30\" id=\"loadingbar\" align=\"middle\">"
			 + "<param name=\"allowScriptAccess\" value=\"sameDomain\" />"
			 + "<param name=\"movie\" value=\"../platform/images/loadingbar.swf\" />"
			 + "<param name=\"quality\" value=\"high\" />"
			 + "<param name=\"wmode\" value=\"transparent\" />"
			 + "<embed src=\"../platform/images/loadingbar.swf\" quality=\"high\" wmode=\"transparent\" width=\"140\" height=\"30\" name=\"loadingbar\" align=\"middle\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />"
			 + "</object><div/>";
	}
	barObj.style.left = (document.body.offsetWidth - 200) / 2 + "px";
	barObj.style.top  = (document.body.offsetHeight - 50) / 2 + "px";

	var str = [];
	str[str.length] = "<div style=\"height:3px;font-size:1px;background-color:#FFFFFF;width:100%;text-align:left\">";
	str[str.length] = "<div style=\"height:3px;font-size:1px;background-color:#5276A3;width:" + this.percent + "%\"/>";
	str[str.length] = "</div>";
	str[str.length] = "<div style=\"padding-top:5px\">";
	str[str.length] = "<span style=\"font-size:16px;font-family:Arial;font-weight:bold\">" + this.percent + "%</span>";
	str[str.length] = "&nbsp;&nbsp;剩余时间:<span style=\"font-size:16px;font-family:Arial;font-weight:bold\">" + this.estimateTime + "</span>秒";
	str[str.length] = "</div>";
	str[str.length] = "<div style=\"padding-top:5px\">";
	str[str.length] = "<a href=\"#\" style=\"margin-top:30px;color:#5276A3;text-decoration:underline\">取 消</a>";
	str[str.length] = "</div>";
	barObj.childNodes[1].innerHTML = str.join("");
	barObj.style.visibility = "visible";

	var link = barObj.getElementsByTagName("a")[0];
	link.onclick = function() {
		thisObj.stop();
	}
}

/*
 *	函数说明：隐藏进度
 */
Progress.prototype.hide = function() {
	var barObj = $(this.id);
	if(null != barObj) {
		barObj.style.visibility = "hidden";
	}
}

/*
 *	函数说明： 同步进度
 */
Progress.prototype.sync = function() {
	var p = new HttpRequestParams();
	p.url = this.progressUrl;
	p.setContent("code", this.code);
	p.ani = false;

	var thisObj = this;
	var request = new HttpRequest(p);
	request.onexception = function() {
		thisObj.hide();
	}
	request.onresult = function() {
		var data = this.getNodeValue("ProgressInfo");
		thisObj.refreshData(data);
		thisObj.show();
		thisObj.next();
	}
	request.send();
}

/*
 *	函数说明： 延时进行下一次同步
 */
Progress.prototype.next = function() {
	var thisObj = this;

	var percent = parseInt(this.percent);
	var delay   = parseInt(this.delay) * 1000;
	if(100 > percent) {
		this.timeout = setTimeout(function() {
			thisObj.sync();
		}, delay);
	}
	else if(null != this.oncomplete) {
		setTimeout(function() {
			thisObj.hide();
			thisObj.oncomplete();
		}, 200);
	}
}


/*
 *	对象名称：Blocks
 *	职责：负责管理所有Block实例
 */
var Blocks = {};
Blocks.items = {};

/*
 *	函数说明：创建区块实例
 *	参数：	Object:blockObj		HTML对象
			Object:associate	关联的HTML对象
			boolean:visible		默认显示状态
 *	返回值：
 */
Blocks.create = function(blockObj, associate, visible) {
	var block = new Block(blockObj, associate, visible);
	this.items[block.uniqueID] = block;
}
/*
 *	函数说明：获取区块实例
 *	参数：	string:id		HTML对象id
 *	返回值：Block:block		Block实例
 */
Blocks.getBlock = function(id) {
	var block = this.items[id];
	return block;
}


/*
 *	对象名称：Block
 *	职责：负责控制区块显示隐藏等
 */
var Block = function(blockObj, associate, visible) {
	this.object = blockObj;
	this.uniqueID = this.object.id;
	this.associate = associate;
	this.visible = visible || true;

	this.width = null;
	this.height = null;	
	this.mode = null;

	this.init();
}

/*
 *	函数说明：初始化区块
 */
Block.prototype.init = function() {
	this.width  = this.object.currentStyle.width;
	this.height = this.object.currentStyle.height;

	if(false == this.visible) {
		this.hide();
	}
}

/*
 *	函数说明：显示详细信息
 *	参数：	boolean:useFixedSize	是否启用固定尺寸显示
 */
Block.prototype.show = function(useFixedSize) {
	if( null != this.associate ) {
		this.associate.style.display = "";
	}
	this.object.style.display = "";

	var width  = "auto";
	var height = "auto";
	
	// 启用固定尺寸
	if(false != useFixedSize) {
		width  = this.width || width;
		height = this.height || height;
	}
	this.object.style.width = width;
	this.object.style.height = height;

	this.visible = true;
}

/*
 *	函数说明：隐藏详细信息
 */
Block.prototype.hide = function() {
	if( null!= this.associate){
		this.associate.style.display = "none";
	}
	this.object.style.display = "none";

	this.visible = false;
}

/*
 *	函数说明：切换显示隐藏状态
 *	参数：	boolean:visible		是否显示状态（可选，无参数则默认切换下一状态）
 */
Block.prototype.switchTo = function(visible) {
	visible = visible || !this.visible;

	if( visible){
		this.show();	
	}
	else {
		this.hide();
	}
}

/*
 *	函数说明：原型继承
 *	参数：	function:Class		将被继承的类
 */
Block.prototype.inherit = function(Class) {
	var inheritClass = new Class();
	for(var item in inheritClass){
		this[item] = inheritClass[item];
	}
}


/*
 *	对象名称：WritingBlock
 *	职责：负责区块内容写入
 *
 */
function WritingBlock() {
	this.mode = null;
	this.line = 0;
	this.minLine = 3;
	this.maxLength = 16;
}

/*
 *	函数说明：打开分行写入模式
 */
WritingBlock.prototype.open = function(){
	this.mode = "line";
	this.line = 0;
	this.writeTable();
}

/*
 *	函数说明：写入分行模式用的表格
 */
WritingBlock.prototype.writeTable = function() {
	var str = [];
	str[str.length] = "<table class=hfull><tbody>";
	for(var i = 0;i < this.minLine; i++) {
		str[str.length] = "<tr>";
		str[str.length] = "  <td class=bullet>&nbsp;</td>";
		str[str.length] = "  <td style=\"width: 55px\"></td>";
		str[str.length] = "  <td></td>";
		str[str.length] = "</tr>";
	}
	str[str.length] = "</tbody></table>";

	this.object.innerHTML = str.join("");    
}

/*
 *	函数说明：清除内容
 */
WritingBlock.prototype.clear = function() {
	this.object.innerHTML = "";
}

/*
 *	函数说明：关闭分行写入模式
 */
WritingBlock.prototype.close = function() {
	this.mode = null;
}

/*
 *	函数说明：分行写入内容（左右两列）
 *	参数：	string:name     名称
			string:value    值
 */
WritingBlock.prototype.writeln = function(name, value) {
	if("line" == this.mode){
		var table = this.object.firstChild;
		if(null != table && "TABLE" != table.nodeName.toUpperCase()) {
			this.clear();
			table = null;
		}
		if(null == table) {
			this.writeTable();
		}

		// 大于最小行数，则插入新行
		if(this.line >= this.minLine) {
			var newrow = table.rows[0].cloneNode(true);
			table.firstChild.appendChild(newrow);
		}

		if(null != value && value.length > this.maxLength) {
			value = value.substring(0, this.maxLength) + "...";
		}

		var row = table.rows[this.line];
		var cells = row.cells;
		cells[1].innerText = name + ":";
		cells[2].innerText = value || "-";

		this.line++;
	}
}

/*
 *	函数说明：写入内容
 *	参数：	string:content		内容
 */
WritingBlock.prototype.write = function(content) {
	this.mode = null;
	this.object.innerHTML = content;
}


/*
 *	对象名称：Focus（全局静态对象）
 *	职责：负责管理所有注册进来对象的聚焦操作
 */
var Focus = {};
Focus.items = {};
Focus.lastID = null;

/*
 *	函数说明：注册对象
 *	参数：	object:focusObj		需要聚焦的HTML对象
 *	返回值：string:id			用于取回聚焦HTML对象的id
 */
Focus.register = function(focusObj) {
	var id = focusObj.id;

	//如果id不存在则自动生成一个
	if(null == id || "" == id) {
		id = UniqueID.generator();
		focusObj.id = id;
	}
	this.items[id] = focusObj;

	this.focus(id);
	return id;
}

/*
 *	函数说明：聚焦对象
 *	参数：	object:focusObj		需要聚焦的HTML对象
 *	返回值：string:id			用于取回聚焦HTML对象的id
 */
Focus.focus = function(id){
	var focusObj = this.items[id];
	if(null != focusObj && id != this.lastID){
		if(null != this.lastID) {
			this.blurItem(this.lastID);
		}
		
		focusObj.style.filter = ""; // 施加聚焦效果
		this.lastID = id;
	}
}

/*
 *	函数说明：施加失焦效果
 *	参数：	string:id			需要聚焦的HTML对象
 *	返回值：
 */
Focus.blurItem = function(id){
	var focusObj = this.items[id];
	if(null!=focusObj){
		focusObj.style.filter = "alpha(opacity=50) gray()";
	}
}

/*
 *	函数说明：释放对象
 *	参数：	object:focusObj		需要聚焦的HTML对象
 *	返回值：string:id			用于取回聚焦HTML对象的id
 */
Focus.unregister = function(id){
	var focusObj = this.items[id];
	if(null != focusObj){
		delete this.items[id];
	}
}