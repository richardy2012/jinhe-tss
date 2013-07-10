    /*
     *	后台响应数据节点名称
     */
    XML_DEFAULT_TOOLBAR = "DefaultToolBar";
    XML_MAIN_TREE = "RuleTree";
    XML_RULE_INFO = "RuleInfo";
    XML_RULE_TO_ROLE_TREE = "Rule2RoleTree";
    XML_RULE_TO_ROLE_EXIST_TREE = "Rule2RoleExistTree";
    XML_RULE_TO_GROUP_TREE = "Rule2GroupTree";
    XML_RULE_TO_GROUP_EXIST_TREE = "Rule2GroupExistTree";
    XML_RULE_TO_USER_TREE = "Rule2UserTree";
    XML_RULE_TO_USER_EXIST_TREE = "Rule2UserExistTree";
    XML_SEARCH_PERMISSION = "SearchPermission";
    XML_RESOURCE_TYPE = "ResourceType";

    XML_GROUP_TO_USER_LIST_TREE = "Group2UserListTree";
    XML_RULE_TO_GROUP_IDS = "rule2GroupIds";
    XML_RULE_TO_USER_IDS = "rule2UserIds";
    XML_RULE_TO_ROLE_IDS = "rule2RoleIds";

    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_RULE_PERMISSION = "rulePermission__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_RULE_DETAIL = "rule__id";
    CACHE_VIEW_RULE_DETAIL = "viewRule__id";
    CACHE_RULE_TO_USER_GRID = "rule2User__id";
    CACHE_SEARCH_PERMISSION = "searchPermission__id";
	
	ICON = "../framework/images/";
	
    /*
     *	名称
     */
    OPERATION_ADD = "新增\"$label\"";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    OPERATION_PERMISSION = "设置\"$label\"权限";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/reassign_init.xml";
    URL_RULE_DETAIL = "data/rule1.xml";
    URL_SAVE_RULE = "data/_success.xml";
    URL_STOP_RULE = "data/_success.xml";
    URL_START_RULE = "data/_success.xml";
    URL_DEL_RULE = "data/_success.xml";
    URL_GROUP_TO_USER_LIST = "data/rule2userlist.xml";


    URL_INIT = "ums/rule!getSubAuthorizeStrategys2Tree.action";
    URL_RULE_DETAIL = "ums/rule!getSubAuthorizeStrategyInfo.action";
    URL_SAVE_RULE = "ums/rule!saveSubAuthorizeStrategy.action";
    URL_STOP_RULE = "ums/rule!disable.action";
    URL_START_RULE = "ums/rule!disable.action";
    URL_DEL_RULE = "ums/rule!delete.action";
    URL_GROUP_TO_USER_LIST = "ums/role!getUsersByGroupId.action";

    function init(){
        initPaletteResize();
        initNaviBar("um.4");
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }
  
    function initMenus(){       
        var item1 = {
            label:"停用",
            callback: function() { stopOrStartTreeNode("1"); },
            icon:ICON + "stop.gif",
            visible:function(){return !isTreeRoot() && !isTreeNodeDisabled();}
        }
        var item2 = {
            label:"启用",
            callback: function() { stopOrStartTreeNode("0"); },
            icon:ICON + "start.gif",
            visible:function(){return !isTreeRoot() && isTreeNodeDisabled();}
        }
        var item3 = {
            label:"新建转授策略",
            callback:addNewRule,
            enable:function(){return true;},
            visible:function(){return isTreeRoot();}
        }
        var item4 = {
            label:"删除",
            callback:delRule,
            icon:ICON + "del.gif",
            visible:function(){return !isTreeRoot();}
        }
        var item5 = {
            label:"编辑",
            callback:editRuleInfo,
            icon:ICON + "edit.gif",
            visible:function(){return !isTreeRoot();}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item5);
        menu1.addItem(item4);
        menu1.addSeparator();
        menu1.addItem(item3);

        $$("tree").contextmenu = menu1;
    }
 
    function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function(){
            var ruleTreeNode = this.getNodeValue(XML_MAIN_TREE);			
			$T("tree", ruleTreeNode);
			
			var treeObj = $$("tree");
			treeObj.onTreeNodeActived = function(eventObj){
				var treeTitleObj = $("treeTitle");
				Focus.focus(treeTitleObj.firstChild.id);

				showTreeNodeInfo();
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj){
                editRuleInfo();
            }
            treeObj.onTreeNodeRightClick = function(eventObj){
				showTreeNodeInfo();

				if( treeObj.contextmenu ) {
					treeObj.contextmenu.show(eventObj.clientX, eventObj.clientY);                
				}
            }
        }
        request.send();
    }
	
	function stopOrStartTreeNode(state) {		
		var tree = $T("tree");
		var treeNode = tree.getActiveTreeNode();
		Ajax({
			url : URL_STOP_RULE + treeNode.getId() + "/" + state,
			onsuccess : function() { 
				treeNode.setAttribute("disabled", state);
				treeNode.setAttribute("icon", ICON + "rule" + (state=="0" ? "" : "_2") + ".gif");    
				tree.reload(); 
			}
		});
    }
 
    function delRule() {
        if( !confirm("您确定要删除吗？") ) return;

        var tree = $T("tree");
        var treeNode = tree.getActiveTreeNode();
		Ajax({
			url : URL_DEL_RULE + treeNode.getId(),
			method : "DELETE",
			onsuccess : function() { 
				var parentNode = treeNode.getParent();
				if( parentNode ) {
					tree.setActiveTreeNode(parentNode.getId());
				}
				tree.removeTreeNode(treeNode);
			}
		});	
    }
 
    function addNewRule(){
        var treeID = DEFAULT_NEW_ID;

        var phases = [];
        phases[0] = {page:"page1", label:"基本信息"};
        phases[1] = {page:"page3", label:"角色"};
        phases[2] = {page:"page4", label:"用户"};
        phases[3] = {page:"page2", label:"用户组"};

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadRuleDetailData(treeID,true,true);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_ADD.replace(/\$label/i, "转授策略");
        inf.phases = phases;
        inf.callback = callback;
        inf.SID = CACHE_RULE_DETAIL + treeID;
        var tab = ws.open(inf);
    }

    function editRuleInfo() {
		if( isTreeRoot() ) return; }
		
		var treeNode = $T("tree").getActiveTreeNode();
		var treeNodeID = treeNode.getId();
		var treeNodeName = treeNode.getName();

		var phases = [];
		phases[0] = {page:"page1", label:"基本信息"};
		phases[1] = {page:"page3", label:"角色"};
		phases[2] = {page:"page4", label:"用户"};
		phases[3] = {page:"page2", label:"用户组"};

		var callback = {};
		callback.onTabClose = function(eventObj){
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function(){
				loadRuleDetailData(treeNodeID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeNodeName);
		inf.SID = CACHE_RULE_DETAIL + treeNodeID;
		inf.defaultPage = "page1";
		inf.phases = phases;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadRuleDetailData(treeID) {
		var p = new HttpRequestParams();
		p.url = URL_RULE_DETAIL + treeID;
		
		var request = new HttpRequest(p);
		request.onresult = function(){
			var ruleInfoNode = this.getNodeValue(XML_RULE_INFO);
			var rule2RoleTreeNode = this.getNodeValue(XML_RULE_TO_ROLE_TREE);
			var rule2RoleExistTreeNode = this.getNodeValue(XML_RULE_TO_ROLE_EXIST_TREE);
			var rule2UserTreeNode = this.getNodeValue(XML_RULE_TO_GROUP_TREE);
			var rule2UserExistTreeNode = this.getNodeValue(XML_RULE_TO_USER_EXIST_TREE);
			var rule2GroupTreeNode = this.getNodeValue(XML_RULE_TO_GROUP_TREE);
			var rule2GroupExistTreeNode = this.getNodeValue(XML_RULE_TO_GROUP_EXIST_TREE);

			var GroupType1Node = rule2GroupTreeNode.selectSingleNode("//treeNode[@groupType='1']");
			if(GroupType1Node) {
				GroupType1Node.setAttribute("canselected","0");
			}
			var GroupType2Node = rule2GroupTreeNode.selectSingleNode("//treeNode[@groupType='2']");
			if(GroupType2Node) {
				GroupType2Node.setAttribute("canselected","0");
			}

			Cache.XmlIslands.add(treeID + "." + XML_RULE_INFO, ruleInfoNode);
			Cache.XmlIslands.add(treeID + "." + XML_RULE_TO_ROLE_TREE, rule2RoleTreeNode);
			Cache.XmlIslands.add(treeID + "." + XML_RULE_TO_ROLE_EXIST_TREE, rule2RoleExistTreeNode);
			Cache.XmlIslands.add(treeID + "." + XML_RULE_TO_USER_TREE, rule2UserTreeNode);
			Cache.XmlIslands.add(treeID + "." + XML_RULE_TO_USER_EXIST_TREE, rule2UserExistTreeNode);
			Cache.XmlIslands.add(treeID + "." + XML_RULE_TO_GROUP_TREE, rule2GroupTreeNode);
			Cache.XmlIslands.add(treeID + "." + XML_RULE_TO_GROUP_EXIST_TREE, rule2GroupExistTreeNode);

			var page1FormObj = $X("page1Form", ruleInfoNode);
			attachReminder("page1Form", page1FormObj);

			var page3Tree  = $T("page3Tree",  rule2RoleTreeNode);
			var page3Tree2 = $T("page3Tree2", rule2RoleExistTreeNode);
			var page2Tree  = $T("page2Tree",  rule2GroupTreeNode);
			var page2Tree2 = $T("page2Tree2", rule2GroupExistTreeNode);			
			var page4Tree  = $T("page4Tree",  rule2UserTreeNode);
			var page4Tree3 = $T("page4Tree3", rule2UserExistTreeNode);
			
			$("page4Tree").onTreeNodeDoubleClick = function(eventObj){
                onPage4TreeNodeDoubleClick(eventObj);
            }
			
			// 设置翻页按钮显示状态
			$("page1BtPrev").style.display = "none";
			$("page4BtPrev").style.display = "";
			$("page2BtPrev").style.display = "";
			$("page1BtNext").style.display = "";
			$("page4BtNext").style.display = "";
			$("page2BtNext").style.display = "none";
			
			// 设置按钮操作
			$("page3BtAdd").onclick = function(){
				addTreeNode(page3Tree, page3Tree2);
			}
			$("page3BtDel").onclick = function(){
				removeTreeNode($("page3Tree2"));
			}
			$("page2BtAdd").onclick = function(){
				addTreeNode(page2Tree, page2Tree2);
			}
			$("page2BtDel").onclick = function(){
				removeTreeNode($("page2Tree2"));
			}
			$("page4BtAdd").onclick = function(){
				addTreeNode($T("page4Tree2"), page2Tree3);
			}
			$("page4BtDel").onclick = function(){
				 removeTreeNode($("page4Tree3"));
			}

			// 设置保存按钮操作
			$("page1BtSave").onclick = $("page2BtSave").onclick = $("page3BtSave").onclick = $("page4BtSave").onclick = function(){
				saveRule(treeID);
			}
		}
		request.send();
 
    }
 
    function onPage4TreeNodeDoubleClick(eventObj) {
	    var treeNode = $T("page4Tree").getActiveTreeNode();
		Ajax({
			url : URL_GROUP_TO_USER_LIST + treeNode.getId(),
			onresult : function() { 
				var sourceListNode = this.getNodeValue(XML_GROUP_TO_USER_LIST_TREE);
				$T("page4Tree2", sourceListNode);
			}
		});	
    }

    function saveRule(cacheID) {
        // 校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if( !page1FormObj.checkForm() ) {
            switchToPhase(ws, "page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_RULE;
 
		// 策略基本信息
		var ruleInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_INFO);
		if(ruleInfoNode){
			var ruleInfoDataNode = ruleInfoNode.selectSingleNode(".//data");
			if(ruleInfoDataNode){
				flag = true;
				p.setXFormContent(ruleInfoDataNode);
			}
		}

		//转授角色
		var rule2RoleNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_ROLE_EXIST_TREE);
		if(rule2RoleNode) {
			var rule2RoleDataIDs = getTreeNodeIds(rule2RoleNode, "./treeNode//treeNode");
			if(rule2RoleDataIDs.length > 0) {
				flag = true;
				p.setContent(XML_RULE_TO_ROLE_IDS, rule2RoleDataIDs.join(","));
			}
		}

		//转授给用户
		var rule2UserNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_USER_EXIST_TREE);
		if(rule2UserNode){
			var rule2UserDataIDs = getTreeNodeIds(rule2UserNode, "./treeNode//treeNode");
			if(rule2UserDataIDs.length > 0) {
				flag = true;
				p.setContent(XML_RULE_TO_USER_IDS, rule2UserDataIDs.join(","));
			}
		}


		//转授给用户组
		var rule2GroupNode = Cache.XmlIslands.get(cacheID + "." + XML_RULE_TO_GROUP_EXIST_TREE);
		if( rule2GroupNode) {
			var rule2GroupDataIDs = getTreeNodeIds(rule2GroupNode, "./treeNode//treeNode");
			if(rule2GroupDataIDs.length > 0) {
				flag = true;
				p.setContent(XML_RULE_TO_GROUP_IDS, rule2GroupDataIDs.join(","));
			}
		}
	}

	if( flag ) {
		var request = new HttpRequest(p);
		
		//同步按钮状态
		syncButton([$$("page1BtSave"), $$("page2BtSave"), $$("page3BtSave"), $$("page4BtSave")], request);

		request.onsuccess = function(){	
			detachReminder(cacheID); // 解除提醒

			loadInitData();
			
			var ws = $("ws");
			ws.closeActiveTab();
		}

		request.send();
    }
	

    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();