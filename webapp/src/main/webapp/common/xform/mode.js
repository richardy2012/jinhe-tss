function Mode_String(name) {
	this.name = name;
	this.obj = element.all(name);
	if(this.obj == null){
		return;
	}
	 
	var tempThis = this;
	this.obj._value = this.obj.value;

	this.setEditable();

	this.obj.onblur = function() {
		if("text" == this.type) {
			this.value = this.value.replace(/(^\s*)|(\s*$)/g, "");
		}

		if(this.getAttribute("inputCmd") != "null") {
			try  {
				eval(this.getAttribute("inputCmd"));
			} catch(e){ }

			return;
		}
		else if(this.value == "" && this.empty == "false") {
			showErrorInfo("请输入 [" + this.caption.replace(/\s/g, "") + "]", this);
		}
		else if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false){
			showErrorInfo("[" + this.caption.replace(/\s/g, "") + "] 格式不正确，请更正。", this);
		}
		else{
			element.updateData(this);

			if(this.tagName != "TEXTAREA") {
				this.title = this.value;
			}
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
	s = s | this.obj.getAttribute("editable");
	this.obj.editable = s;

	var disabled = (s == "false");
	this.obj.className = (disabled ? "string_disabled" : "string");

	if(this.obj.tagName != "TEXTAREA") {
		this.obj.disabled = disabled;  // textarea 禁止状态无法滚动显示所有内容，所以改为只读
		this.obj.title = this.obj.value;
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
	this.obj.title  = this.obj._value;

	var tempValueList = {};
	var tempValueArr = this.obj._value.split(",");
	for(var i=0; i < tempValueArr.length; i++) {
		var x = tempValueArr[i];
		tempValueList[x] = true;
	}

	var valueList = this.obj.editorvalue;
	var textList  = this.obj.editortext;
	
	var isMatch = false;
	var selectedIndex = [];
	for(var i=0; valueList != null && i < valueList.split('|').length; i++){
		var tempValue = valueList.split('|')[i];
		var tempLable = textList.split('|')[i];

		if(tempLable=="&#124;"){
			tempLable = "|";
		}

		var option = new Option();
		option.value = tempValue;
		option.text  = tempLable;
		this.obj.options[this.obj.options.length] = option;

		if(true == tempValueList[tempValue]) {
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

		this.title = this._value;
	}
}

Mode_ComboEdit.prototype.setValue = function(s) {
	var tempValueList = {};
	s = s.split(",");
	for(var i = 0;i < s.length; i++){
		tempValueList[s[i]] = true;
	}
	var isMatch = false;
	for(var i=0; i < this.obj.options.length; i++){
		var opt = this.obj.options[i];
		if(tempValueList[opt.value]) {
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
	var tempEmpty = this.obj.getAttribute("empty");
	var tempValue = this.obj.value;
	if(tempValue == "" && tempEmpty == "false") {
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
	try{
		this.obj.focus();
	}catch(e){
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
	this.obj.title  = this.obj.value;

	var tempRadios = "";
	var valueList = this.obj.editorvalue;
	var textList = this.obj.editortext;
	var valueArray = valueList.split('|');
	for(var i=0; i < valueArray.length; i++ ) {
		var tempValue = valueArray[i];
		var tempLable = textList.split('|')[i];

		var tempID   = this.obj.binding + '_radio_' + this.obj.uniqueID + "_" + i;
		var tempName = this.obj.binding + '_radio_' + this.obj.uniqueID;
		tempRadios += '<input type="radio" class="radio" id="' + tempID + '" name="' + tempName + '" value="' + tempValue + '"' 
			+ (this.obj._value == tempValue ? ' checked' : '') + (this.obj.getAttribute('editable') == 'false' ? ' disabled' : '') 
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
			tempObj.title = tempObj.value;
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
	this.obj.title  = this.obj.value;

	if(this.obj.getAttribute('empty') == "false") {
		this.obj.insertAdjacentHTML("afterEnd", "<span style='color:red;position:relative;left:3px;top:-2px'>*</span>");
	}

	this.obj.disabled = (this.obj.getAttribute("editable") == "false");
	this.obj.className = (this.obj.disabled ? "string_disabled" : "string");	

	this.obj.onfocus = function() {
		var tempEvent = this.onpropertychange;
		this.onpropertychange = null;
		this.value = element.stringToNumber(this.value);
		this.onpropertychange = tempEvent;
		this.select();
	}
	this.obj.onblur = function() {
		if(this.getAttribute("inputCmd") != "null") {
			var _srcElement = this;
			var _ancestor = element;
			try {
				eval(this.getAttribute("inputCmd"));
			}catch(e){}
			return;
		}
		else if(this.value=="" && this.empty=="false") {
			showErrorInfo("请输入 [" + this.caption.replace(/\s/g, "") + "]", this);
		}
		else if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false) {
			if(this.errorInfo != "null") {
				showErrorInfo(this.errorInfo);
			} else {
				showErrorInfo("[" + this.caption.replace(/\s/g, "") + "] 格式不正确，请更正。", this);
			}
		}
		else {
			tempThis.setValue(this.value);
			element.updateData(this);
			this.title = this.value;
		}
	}

	this.obj.onpropertychange = function() {
		if(window.event.propertyName == "value") {
			if(this.inputReg != "null" && eval(this.inputReg).test(this.value) == false) { // 输入不符合
				var value = element.stringToNumber(this._value);
				if(eval(this.inputReg).test(value)) {
					restore(this, value);
				} else {
					restore(this,"");
				}
			} else {
				this._value = this.value;
			}
		}
	};
}

Mode_Number.prototype.setValue = function(value) {
	restore(this.obj, element.numberToString(value, this.obj.pattern));
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
	this.obj.title  = this.obj.value;

	if(this.obj.clickOnly!="false"){ // 可通过column上clickOnly来控制是否允许可手工输入
		this.obj.readOnly = true;
	}
	
	//如果不可为空，添加星号
	if(this.obj.getAttribute('empty')=="false"){
		this.obj.insertAdjacentHTML("afterEnd","<span style='color:red;position:relative;left:3px;top:-2px'>*</span>");
	}

	//添加点击按钮
	this.obj.insertAdjacentHTML('afterEnd','<button style="width:20px;height:18px;background-color:transparent;border:0px;"><img src="'+_baseurl+'function.gif"></button>');
	this.obj.className = (this.obj.getAttribute("editable")=="false"?"function_disabled":"function");
	this.obj.disabled = (this.obj.getAttribute("editable")=="false");
	waitingForVisible(function(){
		tempThis.obj.style.width = Math.max(1,tempThis.obj.offsetWidth - 20);
	});

	this.obj.onblur = function(){
		if("text"==this.type){
			var tempValue = this.value.replace(/(^\s*)|(\s*$)/g,"");
			if(tempValue != this.value){
				this.value = tempValue;
			}
		}
		if(this.getAttribute("inputCmd")!="null"){
			var _srcElement = this;
			var _ancestor = element;
			try{
				eval(this.getAttribute("inputCmd"));
			}catch(e){}
			return;
		}else if(this.value=="" && this.empty=="false"){
			showErrorInfo("请输入 ["+this.caption.replace(/\s/g,"")+"]",this);
		}else if(this.inputReg!="null" && eval(this.inputReg).test(this.value)==false){
			if(this.errorInfo!="null"){
				showErrorInfo(this.errorInfo,this);
			}else{
				showErrorInfo("["+this.caption.replace(/\s/g,"")+"] 格式不正确，请更正",this);
			}
		}else{
			element.updateData(this);

			if(this.tagName!="TEXTAREA"){
				//2006-6-19 增加title提示信息
				this.title = this.value;
			}
		}
	};
	this.obj.onpropertychange = function(){
		if(window.event.propertyName=="value"){
			if(this.inputReg!="null" && eval(this.inputReg).test(this.value)==false){//输入不符合
				if(eval(this.inputReg).test(this._value)==true){//测试旧值是否符合要求
					restore(this,this._value);
				}else{
					restore(this,"");
				}
			}else if(this.value.replace(/[^\u0000-\u00FF]/g,"**").length>parseInt(this.maxLength)){
				var tempValue = this.value.substringB(0,this.maxLength);
				restore(this,tempValue);
			}else{
				this._value = this.value;
				element.beforeUpdateData(this);
			}
		}
	};

	var tempThisObj = this.obj;
	var tempBtObj = this.obj.nextSibling;
	tempBtObj.disabled = (this.obj.getAttribute("editable")=="false"?"disabled":"");
	tempBtObj.className = (this.obj.getAttribute("editable")=="false"?"bt_disabled":"");
	tempBtObj.onclick = function(){
		var _srcElement = tempThisObj;
		var _ancestor = element;
		try{
			eval(tempThisObj.cmd);
		}catch(e){
			showErrorInfo("运行自定义JavaScript代码<"+tempThisObj.cmd+">出错，异常信息："+e.description,tempThisObj);
			throw(e);
		}
	}
}
Mode_Function.prototype.setValue = function(s){
	this.obj._value = this.obj.value = s;
}
Mode_Function.prototype.setEditable = function(s){
	this.obj.disabled = (s=="true"?false:true);
	this.obj.className = (s=="true"?"function":"function_disabled");
	this.obj.nextSibling.disabled = (s=="true"?false:true);
	this.obj.nextSibling.className = (s=="true"?"":"bt_disabled");
	this.obj.editable = s;
}
Mode_Function.prototype.validate = validate;
Mode_Function.prototype.reset = function(fireOnDataChange){
	this.fireOnDataChange = fireOnDataChange;
	this.obj.value = this.obj.defaultValue;
	this.fireOnDataChange = true;
}
Mode_Function.prototype.saveasDefaultValue = function(){
	this.obj.defaultValue = this.obj.value;
}
Mode_Function.prototype.setFocus = function(){
	try{
		this.obj.focus();
	}catch(e){
	}
}








function Mode_Date(name,index){
	this.name = name;
	var tempObj = element.all(name);
	if(tempObj==null){
		return;
	}
	if(tempObj.attributes==null && tempObj.length!=null){
		if(null==index){//未给定序号，或者重复绑定
			tempObj = tempObj[0];
		}else{//多值，给定序号
			tempObj = tempObj[index];
			tempObj.multipleIndex = index;
		}
	}else{

	}
	this.obj = tempObj;
	this.init();
}
Mode_Date.prototype.init = function(){
	var tempThis = this;
	this.obj._value = this.obj.value;
	//2006-6-19 增加title提示信息
	this.obj.title = this.obj.value;
	
	if(this.obj.pattern==null || this.obj.pattern=="null"){
		showErrorInfo("["+this.obj.binding+"] column未设置pattern",this.obj);
		return;
	}
	//如果不可为空，添加星号
	if(this.obj.getAttribute('empty')=="false"){
		this.obj.insertAdjacentHTML("afterEnd","<span style='color:red;position:relative;left:3px;top:-2px'>*</span>");
	}

	//添加点击按钮
	this.obj.insertAdjacentHTML('afterEnd','<button id=functionBtn style="width:20px;height:18px;background-color:transparent;border:0px;"><img src="'+_baseurl+'date.gif"></button>');
	this.obj.className = (this.obj.getAttribute("editable")=="false"?"date_disabled":"date");
	this.obj.disabled = (this.obj.getAttribute("editable")=="false");
	waitingForVisible(function(){
		tempThis.obj.style.width = Math.max(1,tempThis.obj.offsetWidth - 20);
	});

	var tempThisObj = this.obj;
	var tempBtObj = this.obj.nextSibling;
	tempBtObj.disabled = (this.obj.getAttribute("editable")=="false"?"disabled":"");
	tempBtObj.className = (this.obj.getAttribute("editable")=="false"?"bt_disabled":"");
	tempBtObj.onclick = function(){
		var curDateStr;
		var curDate = element.stringToDate(tempThisObj.value,tempThisObj.pattern);
//            if(curDate==null){
//                curDateStr = "";
//            }else{
//                curDateStr = curDate.getYear() + "-" + (curDate.getMonth()+1) + "-" + curDate.getDate();
//            }

		var returnDate = window.showModalDialog(_baseurl + "calendar.htm" , [curDate,tempThisObj.getAttribute("empty")]  ,"dialogWidth:304px;dialogHeight:340px;status:yes;help:no;unadorned:on;resize:on");
		if(returnDate!=null){
			tempThisObj.value = element.dateToString(new Date(returnDate),tempThisObj.pattern); 
			tempThisObj.focus();
		}
	}

	this.obj.onblur = function(){
		var tempDate = element.stringToDate(this.value,this.pattern);
		if(tempDate!=null){
			this._value = element.dateToString(tempDate,this.pattern);
		}
		if(window.document.activeElement!=this.nextSibling){
			if(this.value=="" && this.empty!="false"){//为空并且允许为空时

				this.value = "";

			}else if(tempDate==null){//错误的日期字符串
				if(this.errorInfo!=null && this.errorInfo!="null"){
					showErrorInfo(this.errorInfo,this);
				}else{
					if(this.caption!="null" && this.caption!=null){
						showErrorInfo("["+this.caption.replace(/\s/g,"")+"] 格式不正确，请更正",this);
					}
				}
				this.value = this._value;//自动更正为上一次正确匹配的值

			}else{
				if(this.value!=this._value){
					var externalCheck = true;
					if(this.inputCmd!=null && this.inputCmd!="null"){
						var _srcElement = this;
						var _ancestor = element;
						if(eval(this.inputCmd)==false){
							externalCheck = false;
						}
					}
					if(externalCheck==true){
						this.value = this._value;
					}else{						
						this.value = this.defaultValue;
					}
				}
			}
			element.updateData(this);

			//2006-6-19 增加title提示信息
			this.title = this.value;
		}
	}



	this.obj.onpropertychange = function(){
		if(window.event.propertyName=="value"){
			if(this.inputReg!="null" && eval(this.inputReg).test(this.value)==false){//输入不符合
				if(eval(this.inputReg).test(this._value)==true){                        
					restore(this,this._value);
				}else{
					restore(this,"");
				}
			}else{
				this._value = this.value;
			}
		}
	}
}
Mode_Date.prototype.setValue = function(s){
	var tempDate = element.stringToDate(s,this.obj.pattern);
	if(tempDate!=null){
		this.obj.value = element.dateToString(tempDate,this.obj.pattern);
	}else{
		this.obj.value = "";
	}
}
Mode_Date.prototype.setEditable = function(s){
	this.obj.disabled = (s=="true"?false:true);
	this.obj.className = (s=="true"?"date":"date_disabled");
	this.obj.nextSibling.disabled = (s=="true"?false:true);
	this.obj.nextSibling.className = (s=="true"?"":"bt_disabled");
	this.obj.editable = s;
}
Mode_Date.prototype.validate = function(){
	this.tempValidate = validate;
	var tempValidate = this.tempValidate();

	//2005-11-14 修正输入时有根据pattern检测格式而checkForm时未检测的BUG
	if(tempValidate==true){
		
		
		if(this.obj.value=="" && this.obj.empty!="false"){
			
		}else{
			var tempDate = element.stringToDate(this.obj.value,this.obj.pattern);
			if(tempDate==null){//错误的日期字符串
				if(this.obj.errorInfo!=null && this.obj.errorInfo!="null"){
					showErrorInfo(this.obj.errorInfo,this.obj);
				}else{
					if(this.obj.caption!="null" && this.obj.caption!=null){
						showErrorInfo("["+this.obj.caption.replace(/\s/g,"")+"] 格式不正确，请更正",this.obj);
					}
				}
				tempValidate = false;
			}
		}
	}
	return tempValidate;
}
Mode_Date.prototype.reset = function(fireOnDataChange){
	this.fireOnDataChange = fireOnDataChange;
	this.obj.value = this.obj.defaultValue;
	this.fireOnDataChange = true;
}
Mode_Date.prototype.saveasDefaultValue = function(){
	this.obj.defaultValue = this.obj.value;
}
Mode_Date.prototype.setFocus = function(){
	try{
		this.obj.focus();
	}catch(e){
	}
}








function Mode_Hidden(name,index){
	this.name = name;
	var tempObj = element.all(name);
	if(tempObj==null){
		return;
	}
	if(tempObj.attributes==null && tempObj.length!=null){
		if(null==index){//未给定序号，或者重复绑定
			tempObj = tempObj[0];
		}else{//多值，给定序号
			tempObj = tempObj[index];
			tempObj.multipleIndex = index;
		}
	}else{

	}
	this.obj = tempObj;
	this.init();
}
Mode_Hidden.prototype.init = function(){
	var tempThis = this;
}
Mode_Hidden.prototype.setValue = function(s){
}
Mode_Hidden.prototype.setEditable = function(s){
	this.obj.disabled = (s=="true"?false:true);
	this.obj.editable = s;
}
Mode_Hidden.prototype.validate = function(){
	return true;
}
Mode_Hidden.prototype.reset = function(){
}
Mode_Hidden.prototype.saveasDefaultValue = function(){
}
Mode_Hidden.prototype.setFocus = function(){
}








function Mode_Boolean(name,index){
	this.name = name;
	var tempObj = element.all(name);
	if(tempObj==null){
		return;
	}
	if(tempObj.attributes==null && tempObj.length!=null){
		if(null==index){//未给定序号，或者重复绑定
			tempObj = tempObj[0];
		}else{//多值，给定序号
			tempObj = tempObj[index];
			tempObj.multipleIndex = index;
		}
	}else{

	}
	this.obj = tempObj;
	this.init();
}
Mode_Boolean.prototype.init = function(){
	var tempThis = this;
	this.obj._value = this.obj.getAttribute('value');

	this.obj.disabled = (this.obj.getAttribute("editable")=="false");
	this.obj.checked = this.obj._value=="1"?true:false;

	var tempObj = this.obj;
	this.obj.onclick = function(){
		this.value = this.checked?"1":"0";
		element.updateData(this,tempThis.fireOnDataChange);
		this._value = this.value;
	}
}
Mode_Boolean.prototype.setValue = function(s){
	this.obj.checked = s=="1"?true:false;
	this.obj.value = this.obj._value = s;
}
Mode_Boolean.prototype.setEditable = function(s){
	this.obj.disabled = (s=="true"?false:true);
	this.obj.editable = s;
}
Mode_Boolean.prototype.validate = function(){
	return true;
}
Mode_Boolean.prototype.reset = function(fireOnDataChange){
	this.fireOnDataChange = fireOnDataChange;
	this.obj.checked = this.obj.defaultChecked;
	this.fireOnDataChange = true;
}
Mode_Boolean.prototype.saveasDefaultValue = function(){
	this.obj.defaultChecked = this.obj.checked;
}
Mode_Boolean.prototype.setFocus = function(){
	try{
		this.obj.focus();
	}catch(e){
	}
}



function validate(){
	var tempEmpty = this.obj.empty;
	var tempErrorInfo = this.obj.errorInfo;
	var tempCaption = this.obj.caption.replace(/\s/g,"");
	var tempSubmitReg = this.obj.submitReg;
	var tempValue = this.obj.value;

	if(tempValue==""){
		if(tempEmpty =="false"){
			if(tempErrorInfo!="null" && tempErrorInfo!=null){
				showErrorInfo(tempErrorInfo,this.obj);
			}else{
				if(tempCaption!="null" && tempCaption!=null){
					showErrorInfo("["+tempCaption+"] 格式不正确，请更正",this.obj);
				}
			}
			if(this.isInstance!=false){
				this.setFocus();
			}
			if(event!=null){
				event.returnValue = false;
			}
			return false;
		}else{
			return true;
		}
	}else if(tempSubmitReg!="null" && tempSubmitReg!=null && !eval(tempSubmitReg).test(tempValue)){
		if(tempErrorInfo!="null" && tempErrorInfo!=null){
			showErrorInfo(tempErrorInfo,this.obj);
		}else{
			showErrorInfo("["+tempCaption+"] 格式不正确，请更正",this.obj);
		}
		if(this.isInstance!=false){
			this.setFocus();
		}
		if(event!=null){
			event.returnValue = false;
		}
		return false;
	}else{
		return true;
	}
}

function showErrorInfo(str,obj){
	clearTimeout(element.errorInfoTimeout);
	element.errorInfoTimeout = setTimeout(function(){
		//2006-4-25 弃用内建错误显示方法，改用页面全局Balllon对象
		if(null!=window.Balloons){
			var balloon = Balloons.create(str);
			balloon.dockTo(obj);
		}
	},100);
}

function hideErrorInfo(){
	if(null!=window.Balloons){
		Balloons.dispose();
	}
}

function restore(obj,value){    
	var tempEvent = obj.onpropertychange;
	if(null==tempEvent){
		clearTimeout(obj.timeout);
		tempEvent = obj._onpropertychange;
	}else{
		obj._onpropertychange = tempEvent;
	}
	obj.onpropertychange = null;
	obj.timeout = setTimeout(function(){
		obj.value = value;
		obj.onpropertychange = tempEvent;
	},10);
}

function waitingForVisible(func){
	//控件未隐藏,则直接执行
	if(0!=element.offsetWidth){
		func();
		return;
	}

	//控件隐藏,则等待onresize
	var task = element.resizeTask;
	if(null==task){
		task = [];
	}
	task[task.length] = func;
	element.resizeTask = task;

	if(null==element.onresize){
		element.onresize = function(){
			var task = element.resizeTask;
			if(null!=task){
				for(var i=0,iLen=task.length;i<iLen;i++){
					task[i]();
				}
			}
			element.onresize = null;
		}
	}
}