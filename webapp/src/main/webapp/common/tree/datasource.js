/*
 *	名    称：	数据源对象
 *	功能描述：	此对象主要实现获取控件数据，以及数据的常用处理功能；
 *	
 */


/*
 * 函数说明：数据源对象
 * 参数：
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-21
 */
function DataSource(src) {
	this.xmlRoot = this.loadXml(src);
}


/*
 * 函数说明：原型继承
 * 参数：
 * 返回值：
 * 作者：沈超奇
 * 时间：2004-12-21
 */
DataSource.prototype = new function () {
	/*
	 * 函数说明：根据数据源获取数据
	 * 参数：	src	数据源名称/文件路径
	 * 返回值：	xmlDom根节点
	 * 作者：沈超奇
	 * 时间：2004-12-21
	 */
	this.loadXml = function (src) {
	    if(src == null || (typeof(src) == "string" && src =="")){	//没有定义
			return null;
		}
		if(typeof(src) == "object"
			&& (src.tagName == _TREE_XML_ROOT_NODE_NAME || src.tagName == _TREE_XML_NODE_NAME)
			&& src.nodeTypeString == "element"){
			//2006-4-27 不使用副本，直接引用
			//return src.cloneNode(true);
			return src;
		}
		try{
			eval("src = " + src + ";");
			if(src == null || (typeof(src) == "string" && src =="")){	//没有定义
				return null;
			}
			if(typeof(src) == "object"){
				if(/^xml$/.test(src.tagName) && src.nodeTypeString == "document"){	//xml数据岛
					//2006-4-27 不使用副本，直接引用
					//return src.documentElement.cloneNode(true);
					return src.documentElement;
				}
				if((src.tagName == _TREE_XML_ROOT_NODE_NAME || src.tagName == _TREE_XML_NODE_NAME)
					 && src.nodeTypeString == "element"){	//xml节点
					//2006-4-27 不使用副本，直接引用
					//return src.cloneNode(true);
					return src;
				}
				throw("DataSource:装载数据出错!（数据源类型不合法）");
			}
		}catch(e){
		}
		try{
			//引用xml数据文件/xml数据字符串
			var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
			xmlDom.async = false;
			if (xmlDom.loadXML(src)){	//当src为xml数据时导入数据
				return xmlDom.documentElement;
			}
			if (xmlDom.load(src)) {		//当src为文件路径时导入数据
				return xmlDom.documentElement;
			}
		}catch(e){
		}
		alert("DataSource:装载数据出错!");
		return null;
	}
}