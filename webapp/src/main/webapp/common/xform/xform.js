

var XForm = function(element) {
	this.element = element;

	var pointUrl = this.element.getAttribute("baseurl");
	this._baseurl = pointUrl == null? "" : pointUrl;

	this.xmlDoc;

	this.tempDom = new ActiveXObject("MSXML.DOMDocument");
	this.tempDom.async = false;

	this.xslDom = new ActiveXObject('MSXML.DOMDocument');
	this.xslDom.async = false;
	this.xslDom.resolveExternals = false;
}


XForm.prototype.attachEvents = function() {
	// 回车自动聚焦下一个（input、button等）
	this.element.onkeydown = function() {
		var srcElement = event.srcElement;
		if(window.event.keyCode == 13 && srcElement.tagName.toLowerCase() != "textarea"){
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
			if(tempDom.parseError != 0){
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
			if("object" == typeof(data) && 1 == data.nodeType){
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
					alert("指定的函数运行出错:"+tempEvent);
					throw e;
				}
			}
		}

		if(this.element.firstChild != null) {
			var tempTarget = xmlDoc.xmlObj.getAttribute("target");
			if(tempTarget ==null){
				tempTarget = element.getAttribute("target");
			}
			if(tempTarget!=null){
				element.firstChild.target = tempTarget;					
			}
			element.firstChild.attachEvent('onsubmit',function(){checkForm()});
			element.firstChild.attachEvent('onreset',function(){resetForm()});

			//添加标题栏				
			var theTable = element.firstChild.all.tags("TABLE")[0];
			if(theTable!=null && element.getAttribute("caption")!=null){
				var count = theTable.rows(0).cells.length;
				for(var i=0;i<theTable.rows(0).cells.length;i++){
					count+=parseInt(theTable.rows(0).cells(i).colSpan);
				}
				var captionTR = theTable.insertRow(0);
				var captionTD = captionTR.insertCell();
				captionTD.colSpan = count;
				captionTD.id = "titleBox";
				captionTD.className = "titleBox";
				captionTD.style.cssText = "font-size:12px;height:19px;background-image:url("+_baseurl+"titlebg.gif);background-repeat:no-repeat;"
				//2006-4-11 设置标题
				//captionTD.innerText = element.getAttribute("caption");
				setCaption(element.caption);

				var tempDivHeight = _height-theTable.rows[0].offsetHeight-(theTable.rows[2]==null?0:theTable.rows[2].offsetHeight);
				if(tempDivHeight>0 && _overflow == true){
					theTable.rows(1).cells(0).firstChild.style.height = tempDivHeight;
				}
			}else if(theTable!=null && _overflow == true){
				theTable.rows(0).cells(0).firstChild.style.height = _height-theTable.rows[1].offsetHeight;
			}
					
			//初始化money单位对象
			var unit = xmlDoc.declare.getAttribute("moneyUnit");
			var unitLabel = xmlDoc.declare.getAttribute("moneyUnitLabel");
			var pattern = xmlDoc.declare.getAttribute("moneyPattern");
			var reg = xmlDoc.declare.getAttribute("moneyReg");
			_moneyUnit = new MoneyUnit(unit, unitLabel, pattern, reg);
		}

		//针对多值的，自动增加多行
		cloneMultiple();

		//绑定各个column对应的编辑方式
		attachEditor();
	
		//触发onload事件
		var _ancestor = element;
		var onload1 = element.getAttribute("onload");
		var onload2 = xmlDoc.declare.getAttribute("onload");

		if(element.getAttribute("onloadPriority") =="true"){
			if(onload1!=null){
				eval(onload1);
			}
			if(onload2!=null){
				eval(onload2);
			}
		}else{
			if(onload2!=null){
				eval(onload2);
			}else{
				if(onload1!=null){
					eval(onload1);
				}			
			}
		}

		//自动聚焦
		if(element.autoFocus!="false" && element.autoFocus!=false){
			setFocus();
		}
	}
}
/*
 *  函数说明：如果有多值的column，则复制相应的页面元素
 *  参数：  
 */
function cloneMultiple(){
	var cols = xmlDoc.Columns;
	for(var i=0;i<cols.length;i++){
		var tempColName = cols[i].getAttribute("name");
		var tempMultiple = cols[i].getAttribute("multiple");
		var tempMode = cols[i].getAttribute("mode");
		if("true"==tempMultiple && "hidden"!=tempMode){//多值，非隐藏
			var tempObj = element.all(tempColName);
			if(tempObj==null){
				continue;
			}
			if(tempObj.attributes==null && tempObj.length!=null){
				tempObj = tempObj[0];
			}

			var tempNodeValue = getRowNodeValue(tempColName);
			for(var j=tempNodeValue.length-1;j>0;j--){
				var br = document.createElement("br");
				tempObj.insertAdjacentElement("afterEnd",br);

				var cloneObj = tempObj.cloneNode(true);

				//select不能用.value或者setAttribute("value"来设置value属性，所以换用此方法
				if("TEXTAREA"==cloneObj.nodeName.toUpperCase()){
					cloneObj.value = tempNodeValue[j];                    
				}else{
					if("file"!=tempMode){
						cloneObj.attributes["value"].nodeValue = tempNodeValue[j];
					}
				}

				br.insertAdjacentElement("afterEnd",cloneObj);

				//删除值图标
				var subtract = document.createElement("span");
				subtract.className = "subtract";
				subtract.innerText = "-";
				subtract.params = {
					index:j,
					name:tempColName
				};
				subtract.onclick = function(){
					delRowNodeValue(this.params.name,this.params.index);
				}
				cloneObj.insertAdjacentElement("afterEnd",subtract);
			}

			//增加值图标
			var plus = document.createElement("span");
			plus.className = "plus";
			plus.innerText = "+";
			plus.params = {
				name:tempColName
			};
			plus.onclick = function(){
				addRowNodeValue(this.params.name);
			}
			tempObj.insertAdjacentElement("afterEnd",plus);
		}
	}
}

function attachEditor(){
	var cols = xmlDoc.Columns;
	for(var i=0;i<cols.length;i++){
		var tempColName = cols[i].getAttribute("name");
		var tempColMode = cols[i].getAttribute("mode");
		var tempColEditor = cols[i].getAttribute("editor");
		var tempMultiple = cols[i].getAttribute("multiple");
		var tempNodeValue = getRowNodeValue(tempColName);

		//一般不允许多个对象绑定同一个column，如果有则只取第一个
		var tempObj = element.all(tempColName);
		if(tempObj==null){
			continue;
		}
		if(tempObj.attributes==null && tempObj.length!=null){
			tempObj = tempObj[0];
		}

		if("true"==tempMultiple){
			var arr = [];

			for(var j=0,jLen=Math.max(1,tempNodeValue.length);j<jLen;j++){
				var curInstance;
				switch(tempColMode){
					case "string":
						if(tempColEditor=="comboedit"){
							curInstance = new Mode_ComboEdit(tempColName,j);
						}else if(tempColEditor=="radio"){
							curInstance = new Mode_Radio(tempColName,j);
						}else{
							curInstance = new Mode_String(tempColName,j);
						}
						break;
					case "number":
						curInstance = new Mode_Number(tempColName,j);
						break;
					case "money":
						curInstance = new Mode_Money(tempColName,j);
						break;
					case "function":
						curInstance = new Mode_Function(tempColName,j);
						break;
					case "date":
						curInstance = new Mode_Date(tempColName,j);
						break;
					case "hidden":
						curInstance = new Mode_Hidden(tempColName,j);
						break;
					case "boolean":
						curInstance = new Mode_Boolean(tempColName,j);
						break;
					case "file":
						curInstance = new Mode_File(tempColName,j);
						break;
				}
				curInstance.saveasDefaultValue();
				arr[arr.length] = curInstance;
			}

			_instances[tempColName] = arr;

		}else{
			var curInstance;
			switch(tempColMode){
				case "string":
					if(tempColEditor=="comboedit"){
						curInstance = new Mode_ComboEdit(tempColName);
					}else if(tempColEditor=="radio"){
						curInstance = new Mode_Radio(tempColName);
					}else{
						curInstance = new Mode_String(tempColName);
					}
					break;
				case "number":
					curInstance = new Mode_Number(tempColName);
					break;
				case "money":
					curInstance = new Mode_Money(tempColName);
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
}


function checkForm(){
	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	for(var i=0;i<cols.length;i++){
		var tempColName = cols[i].getAttribute("name");
		var tempInstance = _instances[tempColName];
		if(tempInstance!=null){
			if(0<tempInstance.length){//多值
				for(var j=0,jLen=tempInstance.length;j<jLen;j++){
					var isValidate = tempInstance[j].validate();
					if(isValidate==false){
						return false;
					}
				}
			}else{
				var isValidate = tempInstance.validate();
				if(isValidate==false){
					return false;
				}
			}
		}else{//2005-12-29 layout内不存在时创建虚拟实例执行校验
			var tempInstance = {};
			tempInstance.isInstance = false;
			tempInstance.obj = {
				isInstance:false,
				empty:cols[i].getAttribute("empty"),
				errorInfo:cols[i].getAttribute("errorInfo"),
				caption:cols[i].getAttribute("caption"),
				submitReg:cols[i].getAttribute("submitReg"),
				value:getRowNodeValue(tempColName)
			};


			tempInstance.validate = validate;
			var isValidate = tempInstance.validate();
			if(isValidate==false){
				return false;
			}
		}
	
	}
	var submitCmd = xmlDoc.xmlObj.getAttribute("submitCmd");
	var _ancestor = element;
	if(eval(submitCmd) ==false){
		if(event!=null){
			event.returnValue = false;
		}
		return false;
	}

	var formObj=element.firstChild;
	formObj.elements['xml'].value = getDataXMLString(element.submitDataXMLString);

	return true;
}
function resetForm(fireOnDataChange){
	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	for(var i=0;i<cols.length;i++){
		var tempColName = cols[i].getAttribute("name");
		if(_instances[tempColName]!=null){
			if(0<_instances[tempColName].length){//多值
				for(var j=0,jLen=_instances[tempColName].length;j<jLen;j++){
					_instances[tempColName][j].reset(fireOnDataChange);
				}
			}else{
				_instances[tempColName].reset(fireOnDataChange);
			}
		}
	
	}
	if(event!=null){
		event.returnValue = false;
	}

	//触发onreset事件
	var oEvent = createEventObject();
	event_onreset.fire (oEvent);
}
function updateData(obj,fire){
	var oldValue = getRowNodeValue(obj.binding);
	if(event.propertyName=="checked"){
		var newValue = obj.checked==true?1:0;
	}else if(obj.tagName.toLowerCase()=="select"){
		var newValue = obj._value;            
	}else{
		var newValue = obj.value;
	}

	if(null!=obj.multipleIndex){//多值
		//原值根据序号获取
		oldValue = oldValue[obj.multipleIndex];
		
		//新值需要更改成数组形式
		var tempNewValue = [];
		tempNewValue[obj.multipleIndex] = newValue;
		newValue = tempNewValue;
	}

	if(newValue!=oldValue && (newValue != "" || oldValue != null)){
		setRowNodeValue(obj.binding,newValue);

		//触发ondatachange事件
		if(fire!=false){//2005-10-9 默认true
			fireDataChange(obj,oldValue,newValue);
		}
	}
}
function updateDataExternal(name,value,fire){

	var tempNode = getColumn(name);
	var tempNodeMode = tempNode.getAttribute("mode");
	if(tempNode.getAttribute("isConst")!="true"){
		var oldValue = getData(name);
		var showValue;

		if(tempNodeMode=="money" && typeof(value)=="object"){
			var tempMoney = value.formattedStr();
			setRowNodeValue(name,tempMoney);
			showValue = tempMoney;
		}else{
			setRowNodeValue(name,value);
			showValue = value;
		}
		
		//更改页面显示数据
		var tempSrcElement;
		var tempInstance = _instances[name];
		if(tempInstance!=null){
			if(0<tempInstance.length){//多值
				for(var i=0,iLen=tempInstance.length;i<iLen;i++){
					var curShowValue = showValue[i];
					if(null==curShowValue && "undefined"==typeof(curShowValue)){//未定义则忽略，null则表示清除原值，两者有区别
						continue;
					}
					curShowValue = curShowValue||"";
					tempInstance[i].setValue(curShowValue);
					tempSrcElement = tempInstance[i].obj;
				}
			}else{
				tempInstance.setValue(showValue);
				tempSrcElement = tempInstance.obj;
			}
		}else{
			tempSrcElement = {binding:name};
		}

		//触发ondatachange事件
		if(fire!=false){
			fireDataChange(tempSrcElement,oldValue,value);
		}
	}else{
		alert("不允许修改该\""+name+"\"的值");
	}
}
function updateUnbindingDataExternal(id,value){
	element.all(id).value = value;
	var tempNode = xmlDoc.Layout.selectSingleNode(".//*[@id='"+id+"']");
	if(tempNode!=null){
		tempNode.setAttribute("value",value);
	}
}
function Class_XMLDocument(xmlObj){
	this.xmlObj = xmlObj;
	this.transformXML = function(xslObj){			
		var tempXMLDom = new ActiveXObject('MSXML.DOMDocument');
		tempXMLDom.async = false;
		tempXMLDom.resolveExternals = false;
		tempXMLDom.loadXML(this.toString());

		return tempXMLDom.transformNode(xslObj).replace(/&amp;nbsp;/g,"&nbsp;").replace(/\u00A0/g,"&amp;nbsp;");
	}
	this.toString = function(){
		if(this.xmlObj!=null){
			return this.xmlObj.xml;
		}else{
			return null;
		}
	}
	this.refresh = function(){
		if(this.xmlObj!=null){
			this.declare = this.xmlObj.selectSingleNode("./declare");
			this.Layout = this.xmlObj.selectSingleNode("./layout");
			this.Script = this.xmlObj.selectSingleNode("./script");
			this.Data = this.xmlObj.selectSingleNode("./data");
			if(this.Data==null){				
				var tempDataNode = tempDom.createElement("data");
				this.xmlObj.appendChild(tempDataNode);

				this.Data = tempDataNode;
			}
			this.Row = this.xmlObj.selectSingleNode("./data/row[0]");
			if(this.Row==null){
				var tempRowNode = tempDom.createElement("row");
				this.Data.appendChild(tempRowNode);	
				
				this.Row = tempRowNode;
			}
			this.Columns = this.xmlObj.selectNodes("./declare/column");

			this.ColByName = {};
			for(var i=0;i<this.Columns.length;i++){
				this.ColByName[this.Columns[i].getAttribute("name")] = this.Columns[i];
			}
		}
	}
	this.refresh();
}
function setEditable(s){

	if(element.editable!=s){
		element.editable = s;

		var buttonBox = element.all("buttonBox");
		if(buttonBox!=null){
			buttonBox.style.display = (s=="true"?"block":"none");
		}

		var cols = xmlDoc.Columns;
		for(var i=0;i<cols.length;i++){
			var tempAttr = cols[i].getAttribute("name");
			var tempColumnEditable = cols[i].getAttribute("editable");
			s = (s=="true" && tempColumnEditable!="false")?"true":"false";
			var tempInstance = _instances[tempAttr];
			if(tempInstance!=null){
				if(0<tempInstance.length){//多值
					for(var j=0,jLen=tempInstance.length;j<jLen;j++){
						tempInstance[j].setEditable(s);
					}
				}else{
					tempInstance.setEditable(s);
				}
			}
		}

		setFocus();
	}
}

function getData(name,replace){
	var tempNodeValue = getRowNodeValue(name);
	var tempNodeMode = getColumn(name).getAttribute("mode");
	if(tempNodeMode=="money"){
		return new Money(tempNodeValue,_moneyUnit);
	}else{
		if(true == replace){
			tempNodeValue = tempNodeValue.replace(/"/g,"&quot;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/'/g,"&#39;");
		}
		return tempNodeValue;
	}
}

function submit(){
	if(element.firstChild!=null){
		element.firstChild.submit();
	}
}

function getFormName(){
	if(element.firstChild!=null && element.firstChild.tagName=="FORM"){
		return element.firstChild.attributes["name"];
	}
}

function fireDataChange(obj,oldValue,newValue){
	var oEvent = createEventObject();
	oEvent.result = {
		srcElement:obj,
		name:obj==null?null:obj.binding,
		oldValue:oldValue,
		newValue:newValue
	};
	event_ondatachange.fire (oEvent);
}

function getColumn(name){
	var tempCol = xmlDoc.ColByName[name];
	if(tempCol==null){
		alert(name+"不存在");
		throw(name+"不存在");
	}else{
		return tempCol;
	}
}

function xml() {
	return xmlDoc.toString();
}

function setAction(action){
	xmlDoc.xmlObj.setAttribute("action",action);
}

function getAction(){
	return xmlDoc.xmlObj.getAttribute("action");
}

function getDataXMLString(mode){
	switch(mode){
		case "data":
			return xmlDoc.Data.xml;
			break;
		default:
			return xmlDoc.toString();
			break;
	}
}
//2005-9-13 以column定义为获取依据
function reloadData(rowNode){
	//2005-11-17 修正comboedit类型默认第一项的值
	fixComboeditDefaultValue(rowNode);

	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	//var attrs = rowNode.attributes;
	for(var i=0;i<cols.length;i++){
		var tempAttr = cols[i].getAttribute("name");
		var tempValue = rowNode.getAttribute(tempAttr);
		updateDataExternal(tempAttr,tempValue||"",false);
	}
}

function fixComboeditDefaultValue(rowNode){
	var cols = xmlDoc.Columns;
	for(var i=0;i<cols.length;i++){
		var tempAttr = cols[i].getAttribute("name");
		var tempMode = cols[i].getAttribute("mode");
		var tempEditor = cols[i].getAttribute("editor");
		var tempEditorValue = cols[i].getAttribute("editorvalue")||"";
		var tempFirstValue = tempEditorValue.split("|")[0];
		var tempEmpty = cols[i].getAttribute("empty");
		var tempValue = getRowNodeValue(tempAttr);

		//2006-6-23 当empty=false(不允许为空)时自动取第一项值
		if((tempValue==null || tempValue.length==0) && tempFirstValue!="" && tempMode=="string" && (tempEditor=="comboedit" || tempEditor=="radio") && tempEmpty=="false"){
			setRowNodeValue(tempAttr,tempFirstValue);
		}
	}

}

//2005-9-15 设置标题栏文字
function setCaption(str){
	var titleBox = element.all("titleBox");
	if(titleBox!=null){
		titleBox.innerHTML = str;
		element.caption = str;
	}
}

function saveasDefaultValue(){
	//隐藏上次的错误信息层
	hideErrorInfo();

	var cols = xmlDoc.Columns;
	for(var i=0;i<cols.length;i++){
		var tempColName = cols[i].getAttribute("name");
		var tempInstance = _instances[tempColName];
		if(0<tempInstance.length){//多值
			for(var j=0,jLen=tempInstance.length;j<jLen;j++){
				tempInstance[j].saveasDefaultValue();
			}
		}else{
			tempInstance.saveasDefaultValue();
		}        
	}
}
function setFocus(name,index){
	index = index||0;//多值时必须指定序号，默认为0

	if(name==null || name==""){
		var tempColumn = xmlDoc.declare.selectSingleNode("column[(@editable='true' or not(@editable)) and (@display!='none' or not(@display))]");
		if(tempColumn!=null){
			name = tempColumn.getAttribute("name");
		}else{
			return;
		}
	}
	var tempInstance = _instances[name];
	if(null!=tempInstance){
		if(0<tempInstance.length && null!=tempInstance[index]){//多值
			tempInstance[index].setFocus();
		}else{
			tempInstance.setFocus();
		}
	}
}

function setColumnEditable(name,s){

	var tempNode = getColumn(name);
	tempNode.setAttribute("editable",s);
	
	//更改页面显示数据
	var tempInstance = _instances[name];
	if(null!=tempInstance){
		if(0<tempInstance.length){//多值
			for(var i=0,iLen=tempInstance.length;i<iLen;i++){
				tempInstance[i].setEditable(s);                
			}
		}else{
			tempInstance.setEditable(s);
		}
	}
}

function stringToMoney(value){
	return new Money(value,_moneyUnit).getSystemUnitValueStr();
}

function stringToNumber(str){
	str = str.replace(/[^0-9\.\-]/g,'');
	if(str ==""){
		return 0;
	}else{
		return parseFloat(str);
	}
}

function stringToDate(str,pattern){
	var testYear = str.substr(pattern.indexOf("yyyy"),4);
	var testMonth = str.substr(pattern.indexOf("MM"),2);
	var testDay = str.substr(pattern.indexOf("dd"),2);

	var testDate = testYear + "/" + testMonth + "/" + testDay;

	var HH = pattern.indexOf("HH");
	var mm = pattern.indexOf("mm");
	var ss = pattern.indexOf("ss");
	var testHour = -1==HH?"00":str.substr(HH,2);
	var testMinute = -1==mm?"00":str.substr(mm,2);
	var testSecond = -1==ss?"00":str.substr(ss,2);
	testDate += " " + testHour + ":" + testMinute + ":" + testSecond;

	testDate = new Date(testDate);
	
	if(testDate.getFullYear()!=parseInt(testYear,10) || testDate.getMonth()!=parseInt(testMonth,10)-1 || testDate.getDate()!=parseInt(testDay,10)){
		return null;
	}else{
		return new Date(testDate);
	}
}

function moneyToString(value){		
	return new Money(value,_moneyUnit).formattedStr();
}

function numberToString(number,pattern){
	if(pattern =="null"){
		pattern = "0";
	}
	if(typeof(number) == "string") {
		number = stringToNumber(number);
	}

	if(typeof(number) != "number") {
		if(obj.getAttribute("errorInfo")!=null){
			alert(obj.getAttribute("errorInfo"));
		}else{
			alert("给定参数number类型错误，typeof(number)="+typeof(number));
		}
	}

	var xmldomobj = new ActiveXObject('MSXML.DOMDocument');
	var xsldomobj = new ActiveXObject('MSXML.DOMDocument');
	var xmldomdoc = '<root/>';
	var xsldomdoc = '<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/TR/WD-xsl"><xsl:template match="/root"><xsl:eval>formatNumber('+number+',"'+pattern+'")</xsl:eval></xsl:template></xsl:stylesheet>';
	
	xmldomobj.loadXML(xmldomdoc);
	xsldomobj.loadXML(xsldomdoc);

	var str = xmldomobj.documentElement.transformNode(xsldomobj);
	xmldomobj = null;
	xsldomobj = null;

	return str.replace(/^\./,"0.");
}

function dateToString(dateObj,pattern){
	if(typeof(dateObj) != "object") {
		alert("给定参数number类型错误，typeof(dateObj)="+typeof(dateObj));
		return;
	}
	function addZero(number,digit){
		var str = number.toString(10);
		if(str.length>digit){
			return str;
		}else{
			var zero = (1<<(digit-str.length)).toString(2).substring(1);
			str = zero+str;
			return str;
		}
	}

	var dateStr = dateObj.getFullYear() + '-' + addZero(dateObj.getMonth() + 1,2) + '-' + addZero(dateObj.getDate(),2);
	var timeStr = addZero(dateObj.getHours(),2) + ':' + addZero(dateObj.getMinutes(),2) + ':' + addZero(dateObj.getSeconds(),2);

	var datePattern = pattern.split(" ")[0];
	var timePattern = pattern.split(" ")[1];

	var xmldomobj = new ActiveXObject('MSXML.DOMDocument');
	var xsldomobj = new ActiveXObject('MSXML.DOMDocument');

	var xmlStr = [];
	xmlStr[xmlStr.length] = "<root xml:space=\"preserve\" xmlns:dt=\"urn:schemas-microsoft-com:dataTypes\">";
	xmlStr[xmlStr.length] = "<Date dt:dt=\"datetime\">";
	xmlStr[xmlStr.length] = dateStr;
	xmlStr[xmlStr.length] = "</Date>";
	xmlStr[xmlStr.length] = "<Time dt:dt=\"datetime\">";
	xmlStr[xmlStr.length] = dateStr + "T" + timeStr;
	xmlStr[xmlStr.length] = "</Time>";
	xmlStr[xmlStr.length] = "</root>";
	xmldomobj.loadXML(xmlStr.join(""));

	var xslStr = [];
	xslStr[xslStr.length] = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/TR/WD-xsl\" xmlns:dt=\"urn:schemas-microsoft-com:dataTypes\">";
	xslStr[xslStr.length] = "<xsl:template match=\"/\">";
	xslStr[xslStr.length] = "<xsl:for-each select=\"root/Date\">";
	xslStr[xslStr.length] = "<xsl:eval>formatDate(this.nodeTypedValue, \"" + datePattern + "\");</xsl:eval>";
	xslStr[xslStr.length] = "</xsl:for-each>";
	if("" != timePattern && null != timePattern){
		xslStr[xslStr.length] = "<xsl:for-each select=\"root/Time\">";
		xslStr[xslStr.length] = "<xsl:eval>\" \" + formatTime(this.nodeTypedValue, \"" + timePattern + "\");</xsl:eval>";
		xslStr[xslStr.length] = "</xsl:for-each>";
	}
	xslStr[xslStr.length] = "</xsl:template>";
	xslStr[xslStr.length] = "</xsl:stylesheet>";
	xsldomobj.loadXML(xslStr.join(""));

	var str = xmldomobj.transformNode(xsldomobj);
	xmldomobj = null;
	xsldomobj = null;

	return str;
}

function showCustomErrorInfo(name,str,index){
	index = index||0;

	var tempInstance = _instances[name];
	if(tempInstance!=null){
		if(0<tempInstance.length && null!=tempInstance[index]){
			showErrorInfo(str,tempInstance[index].obj);
		}else{
			showErrorInfo(str,tempInstance.obj);
		}
	}
}

function getColumnAttribute(name,attrName){
	var curColumn = xmlDoc.ColByName[name];
	if(curColumn!=null){
		return curColumn.getAttribute(attrName);
	}else{
		alert("指定的列["+name+"]不存在");
		return null;
	}
}

function setLabelContent(name,content){
	var labelObj = element.all("label_"+name);
	if(labelObj!=null){
		if(labelObj.length>1){
			labelObj = labelObj[0];
		}
		labelObj.innerHTML = content;

	}
}
function getXmlDocument(){
	return xmlDoc.xmlObj;
}
/*
 *  函数说明：获取row节点上与column对应的值
 *
 */
function getRowNodeValue(name){
	//是否多值
	var multiple = "false";
	var tempColumn = xmlDoc.ColByName[name];
	if(null!=tempColumn){
		multiple = tempColumn.getAttribute("multiple")||multiple;
	}

	var tempRowNode = xmlDoc.Row;
	var tempNodeValue;
	if("true"!=multiple){//单值返回单值
		var tempNode = tempRowNode.selectSingleNode(name);
		tempNodeValue = (null==tempNode?null:tempNode.text);
	}else{//多值返回数组
		var tempNodes = tempRowNode.selectNodes(name);
		tempNodeValue = [];
		for(var i=0,iLen=tempNodes.length;i<iLen;i++){
			tempNodeValue[tempNodeValue.length] = tempNodes[i].text;
		}            
	}

	return tempNodeValue;
}
/*
 *  函数说明：设置row节点上与column对应的值
 *  参数：  string:name             列名
			string/array:value      值
 */
function setRowNodeValue(name,value){
	//是否多值
	var multiple = "false";
	var tempColumn = xmlDoc.ColByName[name];
	if(null!=tempColumn){
		multiple = tempColumn.getAttribute("multiple");
	}

	var tempRowNode = xmlDoc.Row;
	if("true"==multiple && true==(value instanceof Array)){//多值并且给定值是数组，设置多值
		var tempNodes = tempRowNode.selectNodes(name);
		for(var i=0,iLen=tempNodes.length;i<iLen;i++){//以实际多值行数为准，给定值超过上限的部分将被忽略
			var curValue = value[i];
			if(null==curValue && "undefined"==typeof(curValue)){//未定义则忽略，null则表示清除原值，两者有区别
				continue;
			}
			curValue = curValue||"";

			var tempNode = tempNodes[i];
			var tempCDATANode = tempNode.selectSingleNode("cdata()");
			if(null!=tempCDATANode){
				tempCDATANode.text = curValue;
			}else{
				var newCDATANode = tempDom.createCDATASection(curValue);
				tempNode.appendChild(newCDATANode);
			}
		}

	}else{//单值或者给定值不是数组,设置单值
		if(true==(value instanceof Array)){//单值，给定值却是数组，则取第一个
			value = value[0];
		}
		var tempNode = tempRowNode.selectSingleNode(name);
		if(null==tempNode){
			//创建多值节点
			var newNode = tempDom.createElement(name);
			tempRowNode.appendChild(newNode);
			tempNode = newNode;
		}
		var tempCDATANode = tempNode.selectSingleNode("cdata()");
		if(null!=tempCDATANode){
			tempCDATANode.text = value;
		}else{
			var newCDATANode = tempDom.createCDATASection(value);
			tempNode.appendChild(newCDATANode);
		}
	}
}
/*
 *  函数说明：增加多值的行
 *  参数：  string:name             列名
 */
function addRowNodeValue(name){
	//是否多值
	var tempColumn = xmlDoc.ColByName[name];
	var tempMode = tempColumn.getAttribute("mode");
	var tempEditor = tempColumn.getAttribute("editor");
	var tempEditorValue = tempColumn.getAttribute("editorvalue")||"";
	var tempFirstValue = tempEditorValue.split("|")[0];
	var tempEmpty = tempColumn.getAttribute("empty");
	var tempDefaultValue = tempColumn.getAttribute("defaultValue");

	var multiple = "false";
	if(null!=tempColumn){
		multiple = tempColumn.getAttribute("multiple");
	}
	if("true"==multiple){
		var tempRowNode = xmlDoc.Row;

		//如果没有该列名的值，则先创建一个空值
		var tempNode = tempRowNode.selectSingleNode(name);
		if(null==tempNode){
			var newNode = tempDom.createElement(name);
			var newCDATANode = tempDom.createCDATASection("");
			newNode.appendChild(newCDATANode);
			tempRowNode.appendChild(newNode);                
		}

		//创建多值节点
		var newNode = tempDom.createElement(name);

		//新值
		var newValue = "";

		//2006-8-10 当empty=false(不允许为空)时自动取第一项值
		if(tempFirstValue!="" && tempMode=="string" && (tempEditor=="comboedit" || tempEditor=="radio") && tempEmpty=="false"){
			newValue = tempFirstValue;
		}else if(null != tempDefaultValue){
			newValue = tempDefaultValue;
		
		}

		var newCDATANode = tempDom.createCDATASection(newValue);
		newNode.appendChild(newCDATANode);
		tempRowNode.appendChild(newNode);

		//刷新界面
		reload();
	}
}
/*
 *  函数说明：删除指定的多值行
 *  参数：  string:name             列名
			number:index            序号
 */
function delRowNodeValue(name,index){
	//是否多值
	var multiple = "false";
	var tempColumn = xmlDoc.ColByName[name];
	if(null!=tempColumn){
		multiple = tempColumn.getAttribute("multiple");
	}
	if("true"==multiple){
		var tempRowNode = xmlDoc.Row;
		var tempNode = tempRowNode.selectSingleNode(name+"["+index+"]");
		if(null!=tempNode){
			//删除指定节点
			tempNode.parentNode.removeChild(tempNode);

			//刷新界面
			reload();
		}
	}
}

/*
 *  函数说明：上传文件
 *  参数：  string:action           请求地址
			string:params           参数
			function:callback       回调方法
 */
function upload(action,params,callback){
	var hasFileColumn = xmlDoc.xmlObj.selectSingleNode("//column[@mode='file']")!=null;
	if(hasFileColumn==true){
		var uploadFrame = createUploadFrame();

		window.iframeOnload = function(){
			//获取iframe中response变量值
			var win = window.frames[uploadFrame];
			var response = win.response;
			if(null==response){
				response = {};
				response.type = "Error";
				response.msg = "文件上传失败";
				response.description = "服务器没有响应。可能是连接超时，请稍后重试";
			}

			//删除iframe元素
			var frameObj = window.document.getElementById(uploadFrame);
			frameObj.removeNode(true);

			//回调
			if(null!=callback){
				callback(response);
			}
		}



		var formObj = element.firstChild;
		formObj.target = uploadFrame;
		formObj.method = "post";
		formObj.enctype = "multipart/form-data";
		formObj.encoding = "multipart/form-data";

		if(null!=params){
			params = "?" + params;
		}else{
			params = "";
		}
		formObj.action = action + params;

		formObj.submit();
	}else{
		return null;
	}
}
/*
 *	函数说明：创建上传提交用iframe
 *	参数：  function:callback       回调方法
 *	返回值：
 */
function createUploadFrame(callback){
	var frameName = "frame" + new Date().valueOf();
	frameObj = window.document.createElement("<iframe onload='if(null!=window.iframeOnload){window.iframeOnload()}' name='"+frameName+"' id='"+frameName+"' src='about:blank' style='display:none'></iframe>");
	element.appendChild(frameObj);
	return frameName;
}
function beforeUpdateData(obj,fire){
	var oldValue = getRowNodeValue(obj.binding);
	if(event.propertyName=="checked"){
		var newValue = obj.checked==true?1:0;
	}else if(obj.tagName.toLowerCase()=="select"){
		var newValue = obj._value;            
	}else{
		var newValue = obj.value;
	}

	if(null!=obj.multipleIndex){//多值
		//原值根据序号获取
		oldValue = oldValue[obj.multipleIndex];
		
		//新值需要更改成数组形式
		var tempNewValue = [];
		tempNewValue[obj.multipleIndex] = newValue;
		newValue = tempNewValue;
	}

	if(newValue!=oldValue && (newValue != "" || oldValue != null)){
		clearTimeout(obj.bdcTimeout);
		obj.bdcTimeout = setTimeout(function(){
			fireBeforeDataChange(obj,oldValue,newValue);
		},200);
	}
}
function fireBeforeDataChange(obj,oldValue,newValue){
	var oEvent = createEventObject();
	oEvent.result = {
		srcElement:obj,
		name:obj==null?null:obj.binding,
		oldValue:oldValue,
		newValue:newValue
	};
	event_onbeforedatachange.fire (oEvent);
}