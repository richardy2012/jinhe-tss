//////////////////////////////////////////////////////////////////////////
//	对象名称：Display														//
//	职责：	负责处理将用户可视部分的节点显示到页面上。						//
//			控件一切页面上的元素都有此对象生成和调度（tr对象有Row对象专门处理）。//
//////////////////////////////////////////////////////////////////////////

/*
 * 函数说明：初始化Display对象
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-25
 */
function instanceDisplay() {
	displayObj = new Display();
	displayObj.init();
}
/*
 * 函数说明：分层显示对象，控制页面显示范围，将应该展示给用户的节点展示在界面上。
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-7
 */
function Display() {
	var _windowHeight = Math.max(element.offsetHeight - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_HEIGHT);
	var _windowWidth = Math.max(element.offsetWidth - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_WIDTH);
	var _rowHeight = _TREE_NODE_DISPLAY_ROW_HEIGHT;
	var _pageSize = Math.floor(_windowHeight / _rowHeight);
	var _totalTreeNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[../@_open = 'true']");
	var _totalTreeNodesNum = _totalTreeNodes.length;
	var _vScrollBox = null;
	var _vScrollDiv = null;
	var _hScrollBox = null;
	var _hScrollDiv = null;
	var _rootBox = null;
	var _rootTable = null;
	var _scrollTimer = null;
	var _startNum = null;
	var _Rows = new Array(_pageSize);
	element.style.overflow = 'hidden';
	element.onresize = resize;
	element.onmousewheel = function(){
		_vScrollBox.scrollTop += -Math.round(window.event.wheelDelta/120)*_rowHeight;
	}
	element.onkeydown = function(){
		switch (event.keyCode) {
		    case 33:	//PageUp
				_vScrollBox.scrollTop -= _pageSize * _rowHeight;
				return false;
		    case 34:	//PageDown
				_vScrollBox.scrollTop += _pageSize * _rowHeight;
				return false;
		    case 35:	//End
				_vScrollBox.scrollTop = _vScrollDiv.offsetHeight - _windowHeight;
				return false;
		    case 36:	//Home
				_vScrollBox.scrollTop = 0;
				return false;
		    case 37:	//Left
				_hScrollBox.scrollLeft -= 10;
				return false;
		    case 38:	//Up
				_vScrollBox.scrollTop -= _rowHeight;
				return false;
		    case 39:	//Right
				_hScrollBox.scrollLeft += 10;
				return false;
		    case 40:	//Down
				_vScrollBox.scrollTop += _rowHeight;
				return false;
		}
	}
	/*
	 * 方法说明：生成默认展示的树节点。
	 * 参数：	
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-7
	 */
	this.init = function(){
		element.innerHTML = "";
		createScrollElement();
		createTableElement();
		setScrollElementSize();
		setTableElementSize();
//		this.reload();
	}
	
	/*
	 * 函数说明：生成滚动条
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function createScrollElement() {
		var vScrollStr = '<div id="treeVScrollBox" style="position:absolute;overflow-y:auto;heigth:100%;width:17px;top:0px;right:0px;"><div id="treeVScrollDiv" style="width:1px"></div></div>';
		var hScrollStr = '<div id="treeHScrollBox" style="position:absolute;overflow-x:auto;overflow-y:hidden;heigth:17px;width:100%;bottom:0px;left:0px"><div id="treeHScrollDiv" style="higth:1px"></div></div>';
		element.insertAdjacentHTML('afterBegin', vScrollStr + hScrollStr);
		_vScrollBox = element.all("treeVScrollBox");
		_vScrollDiv = element.all("treeVScrollDiv");
		_hScrollBox = element.all("treeHScrollBox");
		_hScrollDiv = element.all("treeHScrollDiv");
		_vScrollBox.onscroll = onVScroll;
		_hScrollBox.onscroll = onHScroll;
	}
	/*
	 * 函数说明：设置滚动条的大小
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-9
	 */
	function setScrollElementSize() {
		_vScrollBox.style.height = _windowHeight;

		_vScrollDiv.style.height = (_totalTreeNodesNum - _pageSize) * _rowHeight + _windowHeight;

		_hScrollBox.style.width = _windowWidth;

		_hScrollDiv.style.width = _rootTable.style.width; 
	}
	/*
	 * 函数说明：生成页面上显示节点的table对象。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-7
	 */
	function createTableElement() {
		var tableStr = '<div id="treeRootBox" style="position:absolute;overflow:hidden;top:0px;left:0px"><table id="treeRootTable" cellspacing="0"></table></div>';
		element.insertAdjacentHTML('afterBegin', tableStr);
		_rootBox = element.all("treeRootBox");
		_rootTable = element.all("treeRootTable");
		for(var i = 0; i < _pageSize; i++){
			var tr = _rootTable.insertRow();
			tr.insertCell();
			_Rows[i] = new Row(tr);
		}
	}
	/*
	 * 函数说明：设置显示节点的table对象的大小
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-9
	 */
	function setTableElementSize() {
		_rootBox.style.height = _windowHeight;
		_rootBox.style.width = _windowWidth;
	}
	/*
	 * 方法说明：根据滚动状态，显示可视范围内的树节点。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-7
	 */
	this.reload = function refresh(){
		var st = new Date();
		if(_totalTreeNodesNum <= _pageSize){
			_startNum = 0;
		}else{
			_startNum = Math.ceil(_vScrollBox.scrollTop  / _rowHeight);
		}
		//显示节点
		for(var i = 0; i < _pageSize; i++){
			var nodeIndex = i + _startNum;
			if(nodeIndex < _totalTreeNodesNum){
				_Rows[i].setXmlNode(_totalTreeNodes[nodeIndex]);
			}else{
				_Rows[i].setXmlNode();
			}
		}
		//同步横向滚动条的大小
		_hScrollDiv.style.width = _rootTable.offsetWidth;

		refreshUI();

		var et = new Date();
		window.status=et-st;
	}
	/*
	 * 函数说明：根据页面上的行数，获取相应的Row对象
	 * 参数：	index	行序号
	 * 返回值：	Row对象/null
	 * 作者：scq
	 * 时间：2004-6-29
	 */
	this.getRowByIndex = function (index) {
		if(index >= _pageSize || index < 0){
			alert("Display对象：行序号[" + index + "]超出允许范围[0 - " + _pageSize + "]！");
			return null;
		}
		return _Rows[index];
	}
	/*
	 * 函数说明：重新获取所有可以显示的节点数组
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.resetTotalTreeNodes = function(){
		_totalTreeNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[../@_open = 'true']");
		_totalTreeNodesNum = _totalTreeNodes.length;

		_vScrollDiv.style.height = Math.max(1,(_totalTreeNodesNum - _pageSize) * _rowHeight + _windowHeight);
	}
	/*
	 * 函数说明：获取页面table对象
	 * 参数：
	 * 返回值：	table对象
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.getRootTable = function(){
		return _rootTable;
	}
	/*
	 * 函数说明：将节点滚动到可是范围之内
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.scrollTo = function(node){
		var nodeIndex = null;
		for(var i = 0; i < _totalTreeNodesNum; i++){
			if(_totalTreeNodes[i] == node){
				nodeIndex = i;
				break;
			}
		}
		if(nodeIndex == null){
			return;
		}
		var childNums = node.selectNodes(".//" + _TREE_XML_NODE_NAME + "[../@_open = 'true']").length;
		if(childNums + 1 > _pageSize 
			|| nodeIndex < _startNum 
			|| nodeIndex >= _startNum + _pageSize){
            _vScrollBox.style.display = 'block';
			_vScrollBox.scrollTop = nodeIndex * _rowHeight;
		}else if(nodeIndex + childNums + 1 - _pageSize > _startNum){
            _vScrollBox.style.display = 'block';
			_vScrollBox.scrollTop = (nodeIndex + childNums + 1 - _pageSize) * _rowHeight;
		}else{
			this.reload();
		}
	}
	/*
	 * 函数说明：向上滚动一个节点
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.scrollUp = function(){
		_vScrollBox.scrollTop -= _rowHeight;
	}
	/*
	 * 函数说明：向下滚动一个节点
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.scrollDown = function(){
		_vScrollBox.scrollTop += _rowHeight;
	}
	/*
	 * 函数说明：获取滚动条的位置
	 * 参数：
	 * 返回值：	int	象素值
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.getScrollTop = function(){
		return _vScrollBox.scrollTop;
	}
	/*
	 * 函数说明：纵向滚动事件触发后，延时执行reload，如果第二次触发时，上次的事件还没有执行，
	 * 则取消上次事件，触发本次事件。为的是防止多次触发，屏幕抖动。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function onVScroll() {
 		if (_scrollTimer) {
			window.clearTimeout(_scrollTimer);
		}
		_scrollTimer = window.setTimeout(refresh, _TREE_SCROLL_DELAY_TIME);
	}
	/*
	 * 函数说明：横向滚动事件
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function onHScroll() {
		_rootBox.scrollLeft = this.scrollLeft;
	}
	/*
	 * 函数说明：当窗口大小改变后，初始化所有相关参数，并且重新计算所要显示的节点。
	 * 参数：
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function resize() {
		//2005-9-8 增加延时，避免极短时间内重复触发多次
		clearTimeout(element._resizeTimeout);
		element._resizeTimeout = setTimeout(function(){

			var tempWindowHeight = Math.max(element.offsetHeight - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_HEIGHT);
			var tempWindowWidth = Math.max(element.offsetWidth - _TREE_SCROLL_BAR_WIDTH, _TREE_BOX_MIN_WIDTH);

			if(_windowHeight!=tempWindowHeight || _windowWidth!=tempWindowWidth){
				_windowHeight = tempWindowHeight;
				_windowWidth = tempWindowWidth;
			}else{
				//触发前后尺寸无变化
				return ;
			}
				
			var pageSize = Math.floor(_windowHeight / _rowHeight);
			_vScrollBox.style.height = _windowHeight;
			_hScrollBox.style.width = _windowWidth;

			_rootBox.style.height = _windowHeight;
			_rootBox.style.width = _windowWidth;

			//2005-9-8 修正尺寸变化时行数显示错误问题
			if(pageSize > _pageSize){//高度增加时

				_Rows = new Array(pageSize);
				for(var i = 0; i < pageSize; i++){
					if(i < _pageSize){
						var tr = _rootTable.rows[i];
					}else{
						var tr = _rootTable.insertRow();
						tr.insertCell();
					}
					_Rows[i] = new Row(tr);
				}
				_pageSize = pageSize;
				refresh();

			}else if(pageSize < _pageSize){//高度减少时

				_Rows = new Array(pageSize);
				for(var i = 0; i < pageSize; i++){
					var tr = _rootTable.rows[i];
					_Rows[i] = new Row(tr);
				}
				for(var i = pageSize; i < _pageSize; i++){
					_rootTable.deleteRow(pageSize);
				}
				_pageSize = pageSize;
				refresh();

			}else{
				refreshUI();
			}
		},20);
	}
	/*
	 * 函数说明：刷新页面展示：数据展示框、滚动条等
	 * 参数：
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	function refreshUI(){
		if(_totalTreeNodesNum > _pageSize){
			_vScrollBox.style.display = 'block';
			_hScrollBox.style.width = _windowWidth;
			_rootBox.style.width = _windowWidth;
		}else{
			_vScrollBox.style.display = 'none';
			_hScrollBox.style.width = _windowWidth + _TREE_SCROLL_BAR_WIDTH;
			_rootBox.style.width = _windowWidth + _TREE_SCROLL_BAR_WIDTH;
		}
		if(_rootTable.offsetWidth > _windowWidth){
			_hScrollBox.style.display = 'block';
			_vScrollBox.style.height = _windowHeight;
			_rootBox.style.height = _windowHeight;
		}else{
			_hScrollBox.style.display = 'none';
			_vScrollBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
			_rootBox.style.height = _windowHeight + _TREE_SCROLL_BAR_WIDTH;
		}
	}
	/*
	 * 函数说明：获取页面上所能展示的行数
	 * 参数：
	 * 返回值：int	行数
	 * 作者：scq
	 * 时间：2004-6-8
	 */
	this.getPageSize = function () {
	    return _pageSize;
	}
	this.test = function(name){
		return eval(name);
	}
}