var FontWrapper = function(element) {
	this.element = element;
	this.style = element.style;
	this.font_color = "red";   
	this.count = 123;
	
	//定义鼠标onmouseup事件的调用函数   
	var font2blue = function() {   
		if (event.srcElement == element) {   
			element.style.color='blue';   
		}   
	}   
	
	//定义鼠标onmousedown事件的调用函数   
	var font2yellow = function() {   
		if (event.srcElement == element) {   
			element.style.color = 'yellow';   
		}   
	}   
	  
	//定义鼠标onmouseover事件的调用函数   
	var glowit = function() {   
		if (event.srcElement == element) {   
			font_color = element.style.color;   
			element.style.color = 'white';   
			element.style.filter = "glow(color=red,strength=2)";   
		}   
	}   
	  
	//定义鼠标onmouseout事件的调用函数   
	var noglow = function() {   
		if (event.srcElement == element) {   
			element.style.filter = "";   
			element.style.color = this.font_color;   
		}   
	} 
	
	element.onmouseover = glowit;
	element.onmouseout = noglow;
	element.onmousedown = font2yellow;
	element.onmouseup = font2blue;		
}

// 定义向右移动文字的方法   
FontWrapper.prototype.move_right = function() {
	// alert(this.count);
	alert(this.element);
	this.element.style.left += 20;   
} 

var count = 1;
function XmlNode() {
	
}

XmlNode.prototype.test = function() {
	// alert(event);
	alert(count ++);
}


var Element = {};   
Element.move_down = function(element) {   
	element.style.top += 20;   
}       

/* 常用方法缩写 */
$ = function(id){
	return document.getElementById(id);
}

var Event = {};
Event.attachEvent = function(srcElement, eventName, listener) {
	if(window.DOMParser) {
		srcElement.addEventListener(eventName, listener, false);
	}
	else {
		srcElement.attachEvent("on" + eventName, listener);
	}
}