/**
 * 金额单位对象。
 * 		1、默认金额单位为分
 * 		2、给定一个Money对象，可以将其转换为当前金额单位的Money对象
 * 		3、给定一个Money对象，可以获取当前金额单位的Money值。
 * 		4、toString方法返回：金额单位值＋金额单位中文描述
 * @author scq
 * 2004-5-24
 */
function MoneyUnit(value, description, pattern, regex){

	this._value = value;
	this._description = description;
	this._pattern = pattern;
	this._regex = regex;
	/**
	 * 返回当前单位数值。
	 * @return int
	 */
	this.getValue = function () {
		return this._value;
	}

	/**
	 * 
	 */
	this.toString = function () {
		return "单位: " + this._value + ", " + this._description;
	}
	/**
	 * 单位数值相等视为单位对象相等
	 */
	this.equals = function (obj) {
		if (obj == null || typeof(obj) != "object") {
			return false;
		}
		return obj.getValue() == this._value;
	}

	/**
	 * @return String
	 */
	this.getPattern = function () {
		return this._pattern;
	}

	/**
	 * @return String
	 */
	this.getRegex = function () {
		return this._regex;
	}
	/**
	 * 给定数值，按当前单位进行格式化。
	 * @param money
	 * @return String
	 */
	this.format = function(money) {
		return formatNumber(transformDefaultToSystem(money.getValue(), this._value), this._pattern);
	}

	/**
	 * 将Money对象转化为当前单位的同值Money对象
	 * @param money
	 * @return string
	 */
	this.transformMoney = function(money) {
		var str = transformDefaultToSystem(money.getValue(), this._value);
		return new Money(str, this);
	}
	/**
	 * @param number	数字格式的字符串，如"-123123.232"
	 * @return string	格式化后的字符串，如"-123,123.232"
	 */	
	function formatNumber(number, pattern){
		var xmldomobj	= new ActiveXObject('MSXML.DOMDocument');
		var xsldomobj	= new ActiveXObject('MSXML.DOMDocument');
		var xmldomdoc	= '<root/>';
		var xsldomdoc	= '<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/TR/WD-xsl"><xsl:template match="/root"><xsl:eval>formatNumber('+number+',"'+pattern+'")</xsl:eval></xsl:template></xsl:stylesheet>';
		
		xmldomobj.loadXML(xmldomdoc);
		xsldomobj.loadXML(xsldomdoc);

		var str		= xmldomobj.documentElement.transformNode(xsldomobj);
		xmldomobj	= null;
		xsldomobj	= null;

		return str;
	}
	/**
	 * @param valueStr	数字格式的字符串，如"-123123232"， unitValueStr 单位数值字符串，如"100"
	 * @return string	格式化后的字符串，如"-1231232.32"
	 */	
	function transformDefaultToSystem(valueStr, unitValueStr) {
		valueStr += "";
		unitValueStr += "";
		if(valueStr.indexOf('-') != -1){
			var str = "-0000000000000000" + valueStr.replace(/[^\d\.]/g,"");
		}else{
			var str = "0000000000000000" + valueStr.replace(/[^\d\.]/g,"");
		}
		point = str.length - (unitValueStr.length - 1);
		str = str.substring(0,point) + '.' + str.substring(point,str.length);
		str = str.replace(/^\-0*/,'-').replace(/^\-\./, '-0.').replace(/^0*/, '').replace(/^\./, '0.');
		return str;
	}
}

/**
 * 金额对象。
 * equals方法：
 * 		判断是否为相同的money对象，即：value和单位分别相同。如果单位不同，但是金额
 * 		相等（比如以万元为单位的壹万元和以分为单位的壹万元）的也视为不相等。如果需
 * 		要检查金额是否相等，则使用isSame方法
 * isSame方法：
 * 		金额值相同(单位可以不同)则返回true;
 * DEFAULT_UNIT:
 * @author scq
 * 2004-5-24
 */

function Money(value, unit){
	this._unit = unit;
	this._value = transformSystemToDefault(value, this._unit.getValue());

	/**
	 * @see string
	 */
	this.toString = function () {
		return "金额值: " + this.getSystemUnitValueStr() + ", " + this._unit;
	}

	/**
	 * @return String 当前单位格式化后的钱值
	 */
	this.formattedStr = function () {
		return this._unit.format(this);
	}

	/**
	 * 判断是否为相同的money对象，即：value和单位分别相同。如果单位不同，但是金额
	 * 相等（比如以万元为单位的壹万元和以分为单位的壹万元）的也视为不相等。
	 */
	this.equals = function (obj) {
		if (obj == null || typeof(obj) != "object") {
			return false;
		}
		return obj.getValue() == this._value && obj.getMoneyUnit() == this._unit;
	}
	/**
	 * isSame方法：
	 * 		金额值相同(单位可以不同)则返回true;
	 */
	this.isSame = function (obj) {
	    return obj.getValue() == this._value;
	}
	/**
	 * @return MoneyUnit
	 */
	this.getMoneyUnit =function() {
		return this._unit;
	}

	/**
	 * @return string 当前单位的钱值
	 */
	this.getSystemUnitValueStr = function(){
		return revertNumber(this._unit.format(this));
	}
	/**
	 * @return int 分为单位的钱值
	 */
	this.getValue = function(){
		return this._value;
	}


	/**
	 * 加操作。当前Money对象为被加数，给定参数为加数。
	 * 返回Money对象将以当前Money的钱单位为单位而不论参数addend的单位是什么。
	 * @param addend 加数
	 * @return Money
	 */
	this.plus = function(addend){
		var value = this._value + addend.getValue();
		return this._unit.transformMoney(new Money(value, this.DEFAULT_UNIT));
	}
	/**
	 * 相减操作。
	 * 返回Money对象将以当前Money的钱单位为单位而不论参数subtrahend的单位是什么。
	 * 如果相减结果为零则返回ZERO_MONEY
	 * @param subtrahend 减数
	 * @return Money
	 */
	this.subtract = function(subtrahend){
		var value = this._value - subtrahend.getValue();
		return this._unit.transformMoney(new Money(value, this.DEFAULT_UNIT));
	}
	/**
	 * 乘操作，给定multiplier乘以当前金额。
	 * @param multiplier
	 * @return Money
	 */
	this.multiply = function(multiplier){
		var value = this._value * multiplier;
		return this._unit.transformMoney(new Money(value, this.DEFAULT_UNIT));
	}
	/**
	 * 小于分的部分数值将被四舍五入。
	 * @param divisor
	 * @return Money
	 */
	this.divide = function(divisor){
		var value = Math.round(this._value / divisor);
		return this._unit.transformMoney(new Money(value, this.DEFAULT_UNIT));
	}

	/**
	 * @param string	格式化后的字符串，如"-123,123.232"
	 * @return number	数字格式的字符串，如"-123123.232"
	 */	
	function revertNumber(str){
		if(typeof(str)=="number"){
			return str + "";
		}else if(str==null || str.replace(/[^\d\.\-]/g,"")==""){
			return "0";		
		}else{
			return str.replace(/[^\d\.\-]/g,'');
		}
	}
	/**
	 * @param valueStr	当前单位数字格式的字符串，如"-1231232.32"， unitValueStr 单位数值字符串，如"100"
	 * @return string	格式化后的字符串，如"-123123232"
	 */	
	function transformSystemToDefault(valueStr, unitValueStr) {
		valueStr = revertNumber(valueStr);
		var point = valueStr.indexOf('.');
		if(point == -1) {
			point = valueStr.length;
			valueStr += ".";
		}
		valueStr += "0000000000000000";

		var newPoint = point + unitValueStr.length;

		var newValueStr =	valueStr.substring(0, point) 
							+ valueStr.substring(point + 1, newPoint)
							+ "." 
							+ valueStr.substring(newPoint, valueStr.length);
		return Math.round(parseFloat(newValueStr));
	}
}
Money.prototype.DEFAULT_UNIT = new MoneyUnit('1', '分', '#,##0', '/^\\-?\\d{1,16}$/');

