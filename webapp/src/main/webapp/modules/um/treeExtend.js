
/* 扩展内容复选框类型样式  */
var _TREE_EXTEND_CHECK_ICON_STYLE  = "extendCheckIcon"
var _TREE_EXTEND_CHECK_LABEL_STYLE = "extendCheckLabel";

/* options相关节点名称 */
var _TREE_OPTIONS_NODE = "options";
var _TREE_OPTION_NODE  = "option";
var _TREE_OPTION_ID_NODE = "operationId";
var _TREE_OPTION_TEXT_NODE = "operationName";
var _TREE_OPTION_DEPENDID_NODE = "dependId";

/* 权限选项图标路径文件名前缀  */
var _EXTEND_NODE_ICON = "images/multistate";


var ExtendTree = function(element) {	

	var ExtendRow = function(tr, node) {
	    this.row = tr;
	    this.row.style.height = _TREE_NODE_HEIGHT;
	    this.node = node;
	    this.node.setAttribute("baseUrl", treeThis._baseUrl + _EXTEND_NODE_ICON);
		
	    /* 创建扩展内容的所有列内容  */
	    var _options = treeThis.getOptions();
        for(var i = 0; i < _options.length; i++) {
            var curOption   = _options[i];
            var curOptionID = getNodeText(curOption.selectSingleNode(_TREE_OPTION_ID_NODE));
            var value = this.node.getAttribute(curOptionID);

            // 无属性则设置默认值0
            if(value == null) {
                this.node.setAttribute(curOptionID, "0");
            }
	 		
			var cell = this.row.insertCell(i);
            cell.appendChild(getCloneCellCheckbox());
            cell.align = "center";
            cell.id = this.row.id + "-" + curOptionID;
			
	        var checkType = cell.firstChild.firstChild;
	        checkType.id  = curOptionID;

	        this.node.setAttribute(curOptionID + "-img", cell.id);
			setCellCheckType(node, curOptionID, value);
        }
	 
	    /* 加速创建扩展内容checkbox列内容，获取副本对象 */
	    function getCloneCellCheckbox() {
	        if(window._tempCloneCellCheckbox == null) {		
	            var checkType = Element.createElement("img", _TREE_EXTEND_CHECK_ICON_STYLE);
	            checkType.align = "absmiddle";
				var nobr = Element.createElement("nobr");
	            nobr.appendChild(checkType);
	            window._tempCloneCellCheckbox = nobr;
	        }
	        return window._tempCloneCellCheckbox.cloneNode(true); // 缓存起来
	    }

	    this.getXmlNode = function () {
	        return this.node;
	    }
	}


/*
 * 改变权限项选中状态为下一状态
 * 参数：	optionId                    权限项id
			boolean: shiftKey           是否同时按下shiftKey
			nextState                   指定的click后的状态，可选
 * 返回：	nextState                   click后的状态，不能超过pState
 */
TreeNode.prototype.changeExtendSelectedState = function(optionId, shiftKey, nextState) {
	var curState = this.node.getAttribute(optionId);
	var pState   = this.node.getAttribute("pstate");
	
	// nextState 不能超过pState
	if("2" == nextState && "2" != pState) {
		nextState = "1";
	}        

	if("3" == curState || "4" == curState) { // 当前若是禁用状态则不变
		nextState = curState;
	}
	else if(null == nextState) { // 自动切换状态
		switch(curState || "") {            
			case "0":
			case "":
				nextState = "1";
				break;
			case "1":
				if("2" == pState) {
					nextState = "2";
				} else {
					nextState = "0";
				}
				break;
			case "2":
				nextState = "0";
				break;
		}        
	}

	// 扩展项（权限项）状态改变（该事件在serpermission.js里响应）
	var eventExtendNodeChange = new EventFirer($$("tree"), "onExtendNodeChange"); 
	var eventObj = createEventObject();
	eventObj.treeNode = this;
	eventObj.returnValue = true;
	eventObj.optionId = optionId;
	eventObj.defaultValue = curState || "0";
	eventObj.newValue = nextState;
	eventObj.shiftKey = shiftKey;
	eventObj.type = "_ExtendNodeChange";
	eventExtendNodeChange.fire(eventObj);

	var flag = true;
	if(false == eventObj.returnValue) { 
		flag = false; // 调用语句取消该事件
	}
	else {
		if(nextState != curState) {
			this.node.setAttribute(optionId, nextState);
		}
	}

	// 修改权限项显示图标(只修改open状态的节点)
	setCellCheckType(this.node, optionId, nextState);

	return {flag:flag, state: flag ? nextState : curState};
}

/*
 * 设定选中状态
 *			 0 未选中
 *			 1 仅此节点有权限
 *			 2 所有子节点有权限
 *			 3 未选中禁用
 *			 4 选中禁用
 */
function setCellCheckType (node, optionId, value) {
	var cellIndex = node.getAttribute(optionId + "-img");
	if (cellIndex == null || $$(cellIndex) == null) return;

	var checkType = $$(cellIndex).firstChild.firstChild;
    checkType.state = value;

    value = value || "0";
    if("0" == value ) {
        var checkedChild = node.selectSingleNode(".//treeNode[@" + optionId + "='1' or @" + optionId + "='2' or @" + optionId + "='4']");
        if( checkedChild ) {
            value = "0_2";
        }
    }
    checkType.src = node.getAttribute("baseUrl") + value + ".gif"; // 设定扩展内容checkbox图片地址
}