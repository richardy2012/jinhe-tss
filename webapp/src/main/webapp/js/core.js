function XmlReader(text) {
	this.xmlDom = null;

	if (window.DOMParser) {
		var parser = new DOMParser();
		this.xmlDom = parser.parseFromString(text, "text/xml");
	}
	else { // Internet Explorer
		this.xmlDom = new ActiveXObject("Microsoft.XMLDOM");
		this.xmlDom.async = false;
		this.xmlDom.loadXML(text); 
    } 
}