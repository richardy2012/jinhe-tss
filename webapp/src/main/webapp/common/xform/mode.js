function Mode_String(name) {
	this.name = name;
	this.obj = element.all(name);
	if(this.obj == null) {
		return;
	}
	 
	var tempThis = this;
	this.obj._value = this.obj.value;

	this.setEditable();

	this.obj.onblur = function() {
		if("text" == this.type) {
			this.value = this.value.replace(/(^\s*)|(\s*$)/g, "");
		}

		if(this.value == "" && this.empty == "false") {
			showErrorInfo("请输入 [" + this.caption.replace(/\s/g, "") + "]", this);
		}
		else if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false){
			showErrorInfo("[" + this.caption.replace(/\s/g, "") + "] 格式不正确，请更正。", this);
		}
		else {
			element.updateData(this);
		}
	}

	this.obj.onpropertychange = function() {
		if(window.event.propertyName == "value") {
			if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false){ // 输入不符合
				restore(this, this._value);
			}
			else if(this.value.replace(/[^\u0000-\u00FF]/g, "**").length > parseInt(this.maxLength)) {
				restore(this, this.value.substringB(0, this.maxLength));
			}
			else{
				this._value = this.value;
				element.beforeUpdateData(this);
			}
		}
	};
}

Mode_String.prototype.setValue = function(s) {
	this.obj._value = this.obj.value = s;
}

Mode_String.prototype.setEditable = function(s) {
	s = s || this.obj.getAttribute("editable");
	this.obj.editable = s;

	var disabled = (s == "false");
	this.obj.className = (disabled ? "string_disabled" : "string");

	if(this.obj.tagName != "TEXTAREA") {
		this.obj.disabled = disabled;  // textarea 禁止状态无法滚动显示所有内容，所以改为只读
	} else {
		this.obj.readOnly = disabled;        
	}
}

Mode_String.prototype.validate = validate;

Mode_String.prototype.reset = function(fireOnDataChange) {
	this.fireOnDataChange = fireOnDataChange;
	this.obj.value = this.obj.defaultValue;
	this.fireOnDataChange = true;
}

Mode_String.prototype.saveasDefaultValue = function() {
	this.obj.defaultValue = this.obj.value;
}

Mode_String.prototype.setFocus = function(){
	try {
		this.obj.focus();
	} catch(e){ }
}



// 下拉选择框，单选或多选
function Mode_ComboEdit(name) {
	this.name = name;
	this.obj = element.all(name);
	if(this.obj == null) {
		return;
	}
 
	var tempThis = this;
	this.obj._value = this.obj.attributes["value"].nodeValue;

	var valueList = {};
	var valueArr = this.obj._value.split(",");
	for(var i=0; i < valueArr.length; i++) {
		var x = valueArr[i];
		valueList[x] = true;
	}

	var valueList = this.obj.editorvalue;
	var textList  = this.obj.editortext;
	
	var isMatch = false;
	var selectedIndex = [];
	for(var i=0; valueList != null && i < valueList.split('|').length; i++){
		var value = valueList.split('|')[i];
		var tempLable = textList.split('|')[i];

		if(tempLable=="&#124;"){
			tempLable = "|";
		}

		var option = new Option();
		option.value = value;
		option.text  = tempLable;
		this.obj.options[this.obj.options.length] = option;

		if(true == valueList[value]) {
			isMatch = true;
			this.obj.options[this.obj.options.length].selected = true;
		}
	}

	this.obj.disabled = (this.obj.getAttribute("editable") == "false");

	if(isMatch){
		this.obj.defaultSelectedIndex = selectedIndex.join(",");
	} 
	else {
		this.obj.defaultSelectedIndex = this.obj.selectedIndex = -1;
	}
	
	this.obj.onchange = function() {
		var x = [];
		for(var i=0; i < this.options.length; i++) {
			var opt = this.options[i];
			if(opt.selected) {
				x[x.length] = opt.value;
			}
		}
		this._value = x.join(",");
		element.updateData(this, tempThis.fireOnDataChange);
	}
}

Mode_ComboEdit.prototype.setValue = function(s) {
	var valueList = {};
	s = s.split(",");
	for(var i = 0;i < s.length; i++){
		valueList[s[i]] = true;
	}
	var isMatch = false;
	for(var i=0; i < this.obj.options.length; i++){
		var opt = this.obj.options[i];
		if(valueList[opt.value]) {
			opt.selected = true;
			isMatch = true;
		}
	}

	if(false == isMatch){
		this.obj.selectedIndex = -1;	
	}
}

Mode_ComboEdit.prototype.setEditable = function(s) {
	this.obj.disabled  = (s == "true" ? false : true);
	this.obj.className = (s == "true" ? "comboedit" : "comboedit_disabled");
	this.obj.editable = s;
}

Mode_ComboEdit.prototype.validate = function() {
	var empty = this.obj.getAttribute("empty");
	var value = this.obj.value;
	if(value == "" && empty == "false") {
		showErrorInfo("[" + this.obj.caption.replace(/\s/g, "") + "] 不允许为空，请选择。", this.obj);
		return false;
	}
	return true;
}

Mode_ComboEdit.prototype.reset = function(fireOnDataChange) {
	this.fireOnDataChange = fireOnDataChange;

	this.obj.selectedIndex = -1;
	var selectedIndex = this.obj.defaultSelectedIndex;
	if(selectedIndex != "") {
		selectedIndex = selectedIndex.split(",");
		for(var i=0; i < selectedIndex.lengt; i++) {
			this.obj.options[selectedIndex[i]].selected = true;
		}
	}
	this.fireOnDataChange = true;
}

Mode_ComboEdit.prototype.saveasDefaultValue = function() {
	var selectedIndex = [];
	for(var i=0; i < this.obj.options.length; i++){
		var opt = this.obj.options[i];
		if(opt.selected) {
			selectedIndex[selectedIndex.length] = i;
		}
	}
	this.obj.defaultSelectedIndex = selectedIndex.join(",");
}

Mode_ComboEdit.prototype.setFocus = function() {
	try {
		this.obj.focus();
	} catch(e) {
	}
}




function Mode_Radio(name) {
	this.name = name;
	this.obj = element.all(name);
	if(this.obj == null) {
		return;
	}

	var tempThis = this;
	this.obj._value = this.obj.value;

	var tempRadios = "";
	var valueList = this.obj.editorvalue;
	var textList = this.obj.editortext;
	var valueArray = valueList.split('|');
	for(var i=0; i < valueArray.length; i++ ) {
		var value = valueArray[i];
		var tempLable = textList.split('|')[i];

		var tempID   = this.obj.binding + '_radio_' + this.obj.uniqueID + "_" + i;
		var tempName = this.obj.binding + '_radio_' + this.obj.uniqueID;
		tempRadios += '<input type="radio" class="radio" id="' + tempID + '" name="' + tempName + '" value="' + value + '"' 
			+ (this.obj._value == value ? ' checked' : '') + (this.obj.getAttribute('editable') == 'false' ? ' disabled' : '') 
			+ ' binding="' + this.obj.binding + '">' + '<label for="' + tempID + '">' + tempLable + '</label>';
	}
	this.obj.innerHTML = tempRadios;
	
	var inputObjs = this.obj.all.tags("INPUT");
	var tempObj   = this.obj;
	for(var i=0; i < inputObjs.length; i++) {
		var inputObj = inputObjs[i];
		inputObj.style.cssText = tempObj.defaultStyle;
		inputObj.multipleIndex = tempObj.multipleIndex;

		inputObj.onclick = function() {
			element.updateData(this, tempThis.fireOnDataChange);
			tempObj._value = this.value;
		}
	}
}

Mode_Radio.prototype.setValue = function(value) {
	var inputObjs = this.obj.all.tags("INPUT");
	for(var i=0; i < inputObjs.length; i++) {
		var inputObj = inputObjs[i];
		if(inputObj.value == value ) {
			inputObj.checked = true;
			this.obj._value = inputObj.value;
			return;
		} 
		else {
			inputObj.checked = false;
		}
	}
}

Mode_Radio.prototype.setEditable = function(s) {
	var inputObjs = this.obj.all.tags("INPUT");
	for(var i=0; i < inputObjs.length; i++) {
		var inputObj = inputObjs[i];
		inputObj.disabled = (s == "true" ? false : true);
	}
	this.obj.editable = s;
}

Mode_Radio.prototype.validate = function() {
	return true;
}

Mode_Radio.prototype.reset = function(fireOnDataChange) {
	this.fireOnDataChange = fireOnDataChange;
	var inputObjs = this.obj.all.tags("INPUT");
	for(var i=0; i < inputObjs.length; i++) {
		var inputObj = inputObjs[i];
		inputObj.checked = inputObj.defaultChecked;
	}
	this.fireOnDataChange = true;
}
Mode_Radio.prototype.saveasDefaultValue = function() {
	var inputObjs = this.obj.all.tags("INPUT");
	for(var i=0; i < inputObjs.length; i++){
		var inputObj = inputObjs[i];
		inputObj.defaultChecked = inputObj.checked;
	}
}
Mode_Radio.prototype.setFocus = function(){
	var inputObjs = this.obj.all.tags("INPUT");
	try{
		inputObjs[0].focus();
	}catch(e){
	}
}




function Mode_Number(name) {
	this.name = name;
	this.obj = element.all(name);
	if(this.obj == null) {
		return;
	}

	var tempThis = this;
	this.obj._value = this.obj.value;

	if(this.obj.getAttribute('empty') == "false") {
		this.obj.insertAdjacentHTML("afterEnd", "<span style='color:red;position:relative;left:3px;top:-2px'>*</span>");
	}

	this.obj.disabled = (this.obj.getAttribute("editable") == "false");
	this.obj.className = (this.obj.disabled ? "string_disabled" : "string");	

	this.obj.onfocus = function() {
		var tempEvent = this.onpropertychange;
		this.onpropertychange = null;
		this.value = stringToNumber(this.value);
		this.onpropertychange = tempEvent;
		this.select();
	}
	this.obj.onblur = function() {
		if(this.value=="" && this.empty == "false") {
			showErrorInfo("请输入 [" + this.caption.replace(/\s/g, "") + "]", this);
		}
		else if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false) {
			showErrorInfo("[" + this.caption.replace(/\s/g, "") + "] 格式不正确，请更正。", this);
		}
		else {
			tempThis.setValue(this.value);
			element.updateData(this);
		}
	}

	this.obj.onpropertychange = function() {
		if(window.event.propertyName == "value") {
			if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false) { // 输入不合法
				var value = stringToNumber(this._value);
				if(eval(this.inputReg).test(value)) {
					restore(this, value);
				} else {
					restore(this, "");
				}
			} else {
				this._value = this.value;
			}
		}
	};
}

Mode_Number.prototype.setValue = function(value) {
	restore(this.obj, numberToString(value, this.obj.pattern));
}

Mode_Number.prototype.setEditable = function(s) {
	this.obj.disabled  = (s == "true" ? false : true);
	this.obj.className = (s == "true" ? "string" : "string_disabled");
	this.obj.editable = s;
}

Mode_Number.prototype.validate = validate;

Mode_Number.prototype.reset = function(fireOnDataChange) {
	this.fireOnDataChange = fireOnDataChange;
	this.obj.value = this.obj.defaultValue;
	this.fireOnDataChange = true;
}

Mode_Number.prototype.saveasDefaultValue = function() {
	this.obj.defaultValue = this.obj.value;
}

Mode_Number.prototype.setFocus = function(){
	try {
		this.obj.focus();
	} catch(e) {
	}
}



function Mode_Function(name) {
	this.name = name;
	this.obj = element.all(name);
	if(this.obj == null) {
		return;
	}

	var tempThis = this;
	this.obj._value = this.obj.value;

	if(this.obj.clickOnly!="false"){ // 可通过column上clickOnly来控制是否允许可手工输入
		this.obj.readOnly = true;
	}
	
	//如果不可为空，添加星号
	if(this.obj.getAttribute('empty') == "false"){
		this.obj.insertAdjacentHTML("afterEnd", "<span style='color:red;position:relative;left:3px;top:-2px'>*</span>");
	}

	this.obj.disabled  = (this.obj.getAttribute("editable") == "false");
	this.obj.className = (this.obj.disabled ? "function_disabled" : "function");

	waitingForVisible(function() {
		tempThis.obj.style.width = Math.max(1, tempThis.obj.offsetWidth - 20);
	});

	this.obj.onblur = function() {
		if("text" == this.type) {
			this.value = this.value.replace(/(^\s*)|(\s*$)/g, "");
		}
		
		if(this.value=="" && this.empty=="false"){
			showErrorInfo("请输入 [" + this.caption.replace(/\s/g, "") + "]", this);
		} 
		else if(this.inputReg!="null" && eval(this.inputReg).test(this.value) == false) {
			showErrorInfo("[" + this.caption.replace(/\s/g,"") + "] 格式不正确，请更正",this);
		}
		else{
			element.updateData(this);
		}
	};
	this.obj.onpropertychange = function(){
		if(window.event.propertyName == "value") {
			if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false) { // 输入不符合
				restore(this, this._value);
			} 
			else if(this.value.replace(/[^\u0000-\u00FF]/g, "**").length > parseInt(this.maxLength)) {
				restore(this, this.value.substringB(0, this.maxLength));
			} 
			else {
				this._value = this.value;
				element.beforeUpdateData(this);
			}
		}
	};

	if( !this.obj.disabled ) 
		var tempThisObj = this.obj;

		//添加点击按钮
		this.obj.insertAdjacentHTML('afterEnd', '<button style="width:20px;height:18px;background-color:transparent;border:0px;"><img src="' + _iconPath + 'function.gif"></button>');
		var btObj = this.obj.nextSibling; // 动态添加进去的按钮
		btObj.onclick = function(){
			try {
				eval(tempThisObj.cmd);
			} catch(e) {
				showErrorInfo("运行自定义JavaScript代码<" + tempThisObj.cmd + ">出错，异常信息：" + e.description, tempThisObj);
				throw(e);
			}
		}
	}	
	
}
Mode_Function.prototype.setValue = function(value) {
	this.obj._value = this.obj.value = value;
}
Mode_Function.prototype.setEditable = function(s) {
	this.obj.disabled  = (s == "false");
	this.obj.className = (this.obj.disabled ? "function_disabled" : "function");

	this.obj.nextSibling.disabled = this.obj.disabled;
	this.obj.nextSibling.className = (this.obj.disabled ? "bt_disabled" : "");
	this.obj.editable = s;
}
Mode_Function.prototype.validate = validate;
Mode_Function.prototype.reset = function(fireOnDataChange) {
	this.fireOnDataChange = fireOnDataChange;
	this.obj.value = this.obj.defaultValue;
	this.fireOnDataChange = true;
}
Mode_Function.prototype.saveasDefaultValue = function() {
	this.obj.defaultValue = this.obj.value;
}
Mode_Function.prototype.setFocus = function() {
	try {
		this.obj.focus();
	} catch(e) {
	}
}





function Mode_Hidden(name) {
	this.name = name;
	this.obj = element.all(name);
	if(this.obj == null) {
		return;
	}
 
	var tempThis = this;
}
Mode_Hidden.prototype.setValue = function(s) {
}
Mode_Hidden.prototype.setEditable = function(s) {
}
Mode_Hidden.prototype.validate = function() {
	return true;
}
Mode_Hidden.prototype.reset = function() {
}
Mode_Hidden.prototype.saveasDefaultValue = function() {
}
Mode_Hidden.prototype.setFocus = function() {
}




function validate() {
	var empty = this.obj.empty;
	var errorInfo = this.obj.errorInfo;
	var caption = this.obj.caption.replace(/\s/g,"");
	var submitReg = this.obj.submitReg;
	var value = this.obj.value;

	if(value == "" && empty == "false") {
		errorInfo = "[" + caption.replace(/\s/g, "") + "] 不允许为空，请选择。";
	}

	if(submitReg != "null" && submitReg != null && !eval(submitReg).test(value)) {
		errorInfo = errorInfo || "[" + caption + "] 格式不正确，请更正.");
	}

	if( errorInfo != null ) {
		showErrorInfo(errorInfo, this.obj);

		if(this.isInstance != false) {
			this.setFocus();
		}
		if(event != null) {
			event.returnValue = false;
		}
		return false;
	}

	return true;
}

function showErrorInfo(errorInfo, obj) {
	clearTimeout(this.element.errorInfoTimeout);
	
	this.element.errorInfoTimeout = setTimeout(function() {
		// 页面全局Balllon对象
		if(null != window.Balloons) {
			var balloon = Balloons.create(errorInfo);
			balloon.dockTo(obj);
		}
	}, 100);
}

function hideErrorInfo() {
	if(null != window.Balloons) {
		Balloons.dispose();
	}
}

function restore(obj, value) {    
	var tempEvent = obj.onpropertychange;
	if( tempEvent == null ) {
		clearTimeout(obj.timeout);
		tempEvent = obj._onpropertychange;
	}
	else {
		obj._onpropertychange = tempEvent;
	}

	obj.onpropertychange = null;
	obj.timeout = setTimeout(function() {
		obj.value = value;
		obj.onpropertychange = tempEvent;
	}, 10);
}

function waitingForVisible(func, element) {
	// 控件未隐藏, 则直接执行
	if(0 != element.offsetWidth) {
		func();
		return;
	}

	// 控件隐藏, 则等待onresize
	var tasks = element.resizeTask || [];
	tasks[task.length] = func;
	element.resizeTask = tasks;

	if( element.onresize == null ) {
		element.onresize = function() {
			var tasks = element.resizeTask;
			for(var i=0; i < tasks.length; i++) {
				tasks[i]();
			}
			element.onresize = null;
		}
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