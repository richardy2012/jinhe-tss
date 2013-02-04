//////////////////////////////////////////
//	对象名称：Search					//
//	职责：	查询树上节点。				//
//////////////////////////////////////////

/*
 * 函数说明：初始化Search对象
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-7-2
 */
function instanceSearch() {
    searchObj = new Search();
}

/*
 * 对象说明：负责查询树节点对象的对象
 * 参数：
 * 返回值：
 * 作者：scq
 * 时间：2004-6-14
 */
function Search() {
	var _findedNodes = new Array();
	var _findedIndex = null;
	var _findedNode = null;
	/*
	 * 函数说明：查询得到所有符合要求的结果
	 * 参数：	searchStr	查询的字符串
	 *			searchBy	查询的属性名称
	 *			searchType	查询方式：hazy(模糊)/rigor(精确)，默认为rigor
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.search = function(searchStr, searchBy, searchType){
		_findedNodes = new Array();
		if(isNullOrEmpty(searchStr)){
			alert(_TREE_SEARCH_NO_CONDITION_VALUE_MSG);
			return false;
		}
		if(isNullOrEmpty(searchBy)){
			alert(_TREE_SEARCH_NO_CONDITION_NAME_MSG);
			return false;
		}
		if(searchType == _TREE_SEARCH_TYPE_INEXACT_SEARCH){
			var allNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME);
			for(var i = 0, len = allNodes.length; i < len; i++){	//模糊查询所有节点
				if(allNodes[i].getAttribute(searchBy) != null && allNodes[i].getAttribute(searchBy).indexOf(searchStr) != -1){
					_findedNodes[_findedNodes.length] = allNodes[i];
				}
			}
		}else{
			alert(".//" + _TREE_XML_NODE_NAME + "[@" + searchBy + "='" + searchStr + "']");
			_findedNodes = treeObj.getXmlRoot().selectNodes(".//" + _TREE_XML_NODE_NAME + "[@" + searchBy + "='" + searchStr + "']");
		}
		_findedIndex = -1;
		return true;
	}
	/*
	 * 函数说明：是否拥有查询结果
	 * 参数：
	 * 返回值：true/false
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.hasResult = function(){
		return _findedNodes.length > 0;
	}
	/*
	 * 函数说明：获取查询得到的第一个结果
	 * 参数：
	 *			direct		查询方向：默认为向下查询
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.first = function (direct) {
		if(direct == "down"){
			_findedIndex = 0;
		}else{
			_findedIndex = _findedNodes.length - 1;
		}
		showFindedTreeNode(_findedIndex);
	}
	/*
	 * 函数说明：获取查询结果的下一个结果
	 * 参数：
	 *			direct		查询方向：默认为向下查询
	 *			isCircle	是否循环查询，默认为不循环查询
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */	
	this.next = function (direct, isCircle){
		if(direct == "down"){
			_findedIndex += 1;
			if(_findedNodes.length <= _findedIndex){
				if(isCircle){
					_findedIndex = 0;
				}else{
					_findedIndex = _findedNodes.length - 1;
				}
			}
		}else{
			_findedIndex -= 1;
			if(_findedIndex < 0){
				if(isCircle){
					_findedIndex = _findedNodes.length - 1;
				}else{
					_findedIndex = 0;
				}
			}
		}
		showFindedTreeNode(_findedIndex);
	}
	/*
	 * 函数说明：展示查询结果，将查询得到的节点以查询结果特定的样式高亮
	 * 参数：	index	节点在结果集中的序号
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-6-14
	 */
	function showFindedTreeNode(index) {
		if(_findedNodes.length == 0){
			alert(_TREE_SEARCH_NO_RESULT_MSG);
			setFindedNode(null);
			return;
		}
		setFindedNode(_findedNodes[index]);
	}
	/*
	 * 函数说明：设定查询结果节点高亮
	 * 参数：	node	xml节点
	 * 返回值：
	 * 作者：scq
	 * 时间：2004-7-2
	 */
	function setFindedNode(node) {
		_findedNode = node;
		treeObj.setFindedNode(node);
		treeNode = instanceTreeNode(node);
		if(treeNode instanceof TreeNode){
			treeNode.focus();
		}else{
			displayObj.resetTotalTreeNodes();
			displayObj.reload();
		}
	}
	this.test = function(name){
		return eval(name);
	}
}
