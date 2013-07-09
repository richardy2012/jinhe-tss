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
 
    function loadRuleDetailData(treeID, isNew){
        var cacheID = CACHE_RULE_DETAIL + treeID;
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_RULE_DETAIL;
            //如果是新增
            if(true==isNew){
                p.setContent("isNew", "1");
            }else{
                p.setContent("strategyId", treeID);
            }

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
                if(null!=GroupType1Node){
                    GroupType1Node.setAttribute("canselected","0");
                }
                var GroupType2Node = rule2GroupTreeNode.selectSingleNode("//treeNode[@groupType='2']");
                if(null!=GroupType2Node){
                    GroupType2Node.setAttribute("canselected","0");
                }

                var ruleInfoNodeID = cacheID+"."+XML_RULE_INFO;
                var rule2RoleTreeNodeID = cacheID+"."+XML_RULE_TO_ROLE_TREE;
                var rule2RoleExistTreeNodeID = cacheID+"."+XML_RULE_TO_ROLE_EXIST_TREE;
                var rule2UserTreeNodeID = cacheID+"."+XML_RULE_TO_USER_TREE;
                var rule2UserExistTreeNodeID = cacheID+"."+XML_RULE_TO_USER_EXIST_TREE;
                var rule2GroupTreeNodeID = cacheID+"."+XML_RULE_TO_GROUP_TREE;
                var rule2GroupExistTreeNodeID = cacheID+"."+XML_RULE_TO_GROUP_EXIST_TREE;

                Cache.XmlIslands.add(ruleInfoNodeID,ruleInfoNode);
                Cache.XmlIslands.add(rule2RoleTreeNodeID,rule2RoleTreeNode);
                Cache.XmlIslands.add(rule2RoleExistTreeNodeID,rule2RoleExistTreeNode);
                Cache.XmlIslands.add(rule2UserTreeNodeID,rule2UserTreeNode);
                Cache.XmlIslands.add(rule2UserExistTreeNodeID,rule2UserExistTreeNode);
                Cache.XmlIslands.add(rule2GroupTreeNodeID,rule2GroupTreeNode);
                Cache.XmlIslands.add(rule2GroupExistTreeNodeID,rule2GroupExistTreeNode);

                Cache.Variables.add(cacheID,[ruleInfoNodeID,rule2RoleTreeNodeID,rule2RoleExistTreeNodeID,rule2UserTreeNodeID,rule2UserExistTreeNodeID,rule2GroupTreeNodeID,rule2GroupExistTreeNodeID]);

                initRulePages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initRulePages(cacheID,editable,isNew);
        }
    }
 
    function initRulePages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadRuleInfoFormData(cacheID,editable);
        });

        var page3TreeObj = $("page3Tree");
        Public.initHTC(page3TreeObj,"isLoaded","oncomponentready",function(){
            loadRule2RoleTreeData(cacheID);
        });

        var page3Tree2Obj = $("page3Tree2");
        Public.initHTC(page3Tree2Obj,"isLoaded","oncomponentready",function(){
            loadRule2RoleExistTreeData(cacheID);
        });

        var page2TreeObj = $("page2Tree");
        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function(){
            loadRule2GroupTreeData(cacheID);
        });

        var page2Tree2Obj = $("page2Tree2");
        Public.initHTC(page2Tree2Obj,"isLoaded","oncomponentready",function(){
            loadRule2GroupExistTreeData(cacheID);
        });

        var page4TreeObj = $("page4Tree");
        Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function(){
            loadRule2UserTreeData(cacheID);
        });

        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page4Tree2Obj);
        });

        var page4Tree3Obj = $("page4Tree3");
        Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function(){
            loadRule2UserExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page2BtPrevObj = $("page2BtPrev");
        var page4BtPrevObj = $("page4BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page2BtNextObj = $("page2BtNext");
        var page4BtNextObj = $("page4BtNext");
        page1BtPrevObj.style.display = "none";
        page4BtPrevObj.style.display = "";
        page2BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page4BtNextObj.style.display = "";
        page2BtNextObj.style.display = "none";

        //设置搜索
        var page2BtSearchObj = $("page2BtSearch");
        var page2KeywordObj = $("page2Keyword");
        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置搜索
        var page4BtSearchObj = $("page4BtSearch");
        var page4KeywordObj = $("page4Keyword");
        attachSearchTree(page4TreeObj,page4BtSearchObj,page4KeywordObj);

        //设置搜索
        var page4BtSearch2Obj = $("page4BtSearch2");
        var page4Keyword2Obj = $("page4Keyword2");
        attachSearchTree(page4Tree2Obj,page4BtSearch2Obj,page4Keyword2Obj);

        //设置搜索
        var page3BtSearchObj = $("page3BtSearch");
        var page3KeywordObj = $("page3Keyword");
        attachSearchTree(page3TreeObj,page3BtSearchObj,page3KeywordObj);

        //设置添加按钮操作
        var page3BtAddObj = $("page3BtAdd");
        page3BtAddObj.disabled = editable==false?true:false;
        page3BtAddObj.onclick = function(){
            addPage3TreeNode();
        }

        //设置删除按钮操作
        var page3BtDelObj = $("page3BtDel");
        page3BtDelObj.disabled = editable==false?true:false;
        page3BtDelObj.onclick = function(){
            delPage3TreeNode();
        }

        //设置添加按钮操作
        var page2BtAddObj = $("page2BtAdd");
        page2BtAddObj.disabled = editable==false?true:false;
        page2BtAddObj.onclick = function(){
            addPage2TreeNode();
        }

        //设置删除按钮操作
        var page2BtDelObj = $("page2BtDel");
        page2BtDelObj.disabled = editable==false?true:false;
        page2BtDelObj.onclick = function(){
            delPage2TreeNode();
        }

        //设置添加按钮操作
        var page4BtAddObj = $("page4BtAdd");
        page4BtAddObj.disabled = editable==false?true:false;
        page4BtAddObj.onclick = function(){
             addPage4TreeNode();
        }

        //设置删除按钮操作
        var page4BtDelObj = $("page4BtDel");
        page4BtDelObj.disabled = editable==false?true:false;
        page4BtDelObj.onclick = function(){
             delPage4TreeNode();
        }

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page2BtSaveObj = $("page2BtSave");
        var page3BtSaveObj = $("page3BtSave");
        var page4BtSaveObj = $("page4BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page2BtSaveObj.disabled = editable==false?true:false;
        page3BtSaveObj.disabled = editable==false?true:false;
        page4BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page2BtSaveObj.onclick = page3BtSaveObj.onclick = page4BtSaveObj.onclick = function(){
            saveRule(cacheID,isNew);
        }
    }
 
    function loadRuleInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
 
    function loadRule2RoleTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_ROLE_TREE);
        if(null!=xmlIsland){
            var page3TreeObj = $("page3Tree");
            page3TreeObj.load(xmlIsland.node);
            page3TreeObj.research = true;
        }
    }
 
    function loadRule2RoleExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_ROLE_EXIST_TREE);
        if(null!=xmlIsland){
            var page3Tree2Obj = $("page3Tree2");
            page3Tree2Obj.load(xmlIsland.node);
        }
    }
 
    function loadRule2GroupTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_GROUP_TREE);
        if(null!=xmlIsland){
            var page2TreeObj = $("page2Tree");
            page2TreeObj.load(xmlIsland.node);
            page2TreeObj.research = true;
        }
    }
 
    function loadRule2GroupExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_GROUP_EXIST_TREE);
        if(null!=xmlIsland){
            var page2Tree2Obj = $("page2Tree2");
            page2Tree2Obj.load(xmlIsland.node);
        }
    }
 
    function loadRule2UserTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_USER_TREE);
        if(null!=xmlIsland){
            var page4TreeObj = $("page4Tree");
            page4TreeObj.load(xmlIsland.node);
            page4TreeObj.research = true;

            page4TreeObj.onTreeNodeDoubleClick = function(eventObj){
                onPage4TreeNodeDoubleClick(eventObj);
            }
        }
    }
 
    function loadRule2UserExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_USER_EXIST_TREE);
        if(null!=xmlIsland){
            var page4Tree3Obj = $("page4Tree3");
            page4Tree3Obj.load(xmlIsland.node);
        }
    }
 
    function onPage4TreeNodeDoubleClick(eventObj){
        var treeObj = $("page4Tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            initPage4Tree2(id);
        }
    }
 
    function initPage4Tree2(id){
        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","onload",function(){
            loadPage4Tree2Data(id);
        });
    }
 
    function loadPage4Tree2Data(treeID){
        var cacheID = CACHE_RULE_TO_USER_GRID + treeID;
        var treeGrid = Cache.Variables.get(cacheID);

            var p = new HttpRequestParams();
            p.url = URL_GROUP_TO_USER_LIST;
			p.setContent("groupId", treeID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var sourceListNode = this.getNodeValue(XML_GROUP_TO_USER_LIST_TREE);

                var sourceListNodeID = cacheID+"."+XML_GROUP_TO_USER_LIST_TREE;

                Cache.XmlIslands.add(sourceListNodeID,sourceListNode);
                Cache.Variables.add(cacheID,sourceListNodeID);

                loadPage4Tree2DataFromCache(cacheID);
            }
            request.send();

    }

    function loadPage4Tree2DataFromCache(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_USER_LIST_TREE);
        if(null!=xmlIsland){
            var page4Tree2Obj = $("page4Tree2");
            page4Tree2Obj.load(xmlIsland.node);
            page4Tree2Obj.research = true;
        }
    }

    function saveRule(cacheID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_RULE;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){
        
            //策略基本信息
            var ruleInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_INFO);
            if(null!=ruleInfoNode){
                var ruleInfoDataNode = ruleInfoNode.selectSingleNode(".//data");
                if(null!=ruleInfoDataNode){
                    flag = true;
                    
                    var prefix = ruleInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(ruleInfoDataNode,prefix);
                }
            }


            //转授角色
            var rule2RoleNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_ROLE_EXIST_TREE);
            if(null!=rule2RoleNode){
                var rule2RoleDataIDs = getTreeNodeIds(rule2RoleNode,"./treeNode//treeNode");
                if(0<rule2RoleDataIDs.length){
                    flag = true;
                    p.setContent(XML_RULE_TO_ROLE_IDS,rule2RoleDataIDs.join(","));
                }
            }


            //转授给用户
            var rule2UserNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_USER_EXIST_TREE);
            if(null!=rule2UserNode){
                var rule2UserDataIDs = getTreeNodeIds(rule2UserNode,"./treeNode//treeNode");
                if(0<rule2UserDataIDs.length){
                    flag = true;
                    p.setContent(XML_RULE_TO_USER_IDS,rule2UserDataIDs.join(","));
                }
            }


            //转授给用户组
            var rule2GroupNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_GROUP_EXIST_TREE);
            if(null!=rule2GroupNode){
                var rule2GroupDataIDs = getTreeNodeIds(rule2GroupNode,"./treeNode//treeNode");
                if(0<rule2GroupDataIDs.length){
                    flag = true;
                    p.setContent(XML_RULE_TO_GROUP_IDS,rule2GroupDataIDs.join(","));
                }
            }
        
        
        }

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            var page2BtSaveObj = $("page2BtSave");
            var page3BtSaveObj = $("page3BtSave");
            var page4BtSaveObj = $("page4BtSave");
            syncButton([page1BtSaveObj,page2BtSaveObj,page3BtSaveObj,page4BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);

                loadInitData();

                if(true==isNew){
                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }

            request.send();
        }
    }
  
    function delPage3TreeNode(){
        removeTreeNode($("page3Tree2"));
    }
 
    function addPage3TreeNode(){
        var page3Tree2Obj = $("page3Tree2");
        var page3TreeObj = $("page3Tree");
        var selectedNodes = page3TreeObj.getSelectedTreeNode();

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++){
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page3Tree2Obj,"id",id);
            if(false==sameAttributeTreeNode){
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page3Tree2Obj.getTreeNodeById("_rootId");
                if(null!=treeNode){
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page3Tree2Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload){
            page3Tree2Obj.reload();
        }
        page3TreeObj.reload();
    }
 
    function delPage2TreeNode(){
        removeTreeNode($("page2Tree2"));
    }
 
    function addPage2TreeNode(){
        var page2Tree2Obj = $("page2Tree2");
        var page2TreeObj = $("page2Tree");
        var selectedNodes = page2TreeObj.getSelectedTreeNode();

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++){
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page2Tree2Obj,"id",id);
            if(false==sameAttributeTreeNode){
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page2Tree2Obj.getTreeNodeById("_rootId");
                if(null!=treeNode){
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page2Tree2Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload){
            page2Tree2Obj.reload();
        }
        page2TreeObj.reload();
    }
 
    function delPage4TreeNode(){
        removeTreeNode($("page4Tree3"));
    }
 
    function addPage4TreeNode(){
        var page4Tree2Obj = $("page4Tree2");
        var page4Tree3Obj = $("page4Tree3");
        var selectedNodes = page4Tree2Obj.getSelectedTreeNode();

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++){
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page4Tree3Obj,"id",id);
            if("_rootId"!=id && false==sameAttributeTreeNode){
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page4Tree3Obj.getTreeNodeById("_rootId");
                if(null!=treeNode){
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page4Tree3Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload){
            page4Tree3Obj.reload();
        }
        page4Tree2Obj.reload();
    }
	

    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();