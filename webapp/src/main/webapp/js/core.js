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
}

XmlReader.prototype.toXml = function() {
	var str = "";
	if(window.DOMParser) { 
		str = new XMLSerializer().serializeToString(this.xmlDom.documentElement);
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

XmlNode.prototype.setAttribute = function(name, value, format) {
	if(ELEMENT_NODE_TYPE != this.nodeType) {
		return;
	}

	if(value == null) {
		if(format == 1) {
			this.removeCDATA(name);
		}
		else {
			this.removeAttribute(name);
		}
	}
	else {
		if(format == 1) {
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
	if(window.ActiveXObject) {
		return new XmlNode(this.node.cloneNode(deep));
	} else {
		return new XmlNode(new XmlReader(this.toXml()).documentElement);
	}
}









