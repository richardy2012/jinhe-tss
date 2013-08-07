    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "GroupTree";
    XML_USER_LIST = "SourceList";
 
    XML_USER_INFO = "UserInfo";
    XML_AUTHENTICATE_INFO = "AuthInfo";
    XML_USER_TO_GROUP_TREE = "User2GroupTree";
    XML_USER_TO_GROUP_EXIST_TREE = "User2GroupExistTree";
    XML_USER_TO_ROLE_TREE = "User2RoleTree";
    XML_USER_TO_ROLE_EXIST_TREE = "User2RoleExistTree";

    XML_GROUP_INFO = "GroupInfo";
    XML_GROUP_TO_USER_TREE = "Group2UserTree";
    XML_GROUP_TO_USER_LIST_TREE = "Group2UserListTree";
    XML_GROUP_TO_USER_EXIST_TREE = "Group2UserExistTree";
    XML_GROUP_TO_ROLE_TREE = "Group2RoleTree";
    XML_GROUP_TO_ROLE_EXIST_TREE = "Group2RoleExistTree";
 
    XML_SEARCH_SUBAUTH = "SUBAUTH_RESULT";
    XML_SEARCH_ROLE = "ROLE_RESULT";
 
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_SEARCH_SUBAUTH = "CACHE_SEARCH_SUBAUTH";
    CACHE_SEARCH_ROLE = "CACHE_SEARCH_ROLE";
 
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT          = "/" + AUTH_PATH + "group/list";
    URL_USER_LIST     = "/" + AUTH_PATH + "user/list/";    // user/list/{groupId}/{page}
    URL_USER_DETAIL   = "/" + AUTH_PATH + "user/detail/";  // user/detail/{groupId}/{userId}
    URL_GROUP_DETAIL  = "/" + AUTH_PATH + "group/detail/"; // group/detail/{parentId}/{id}/{type}
    URL_SAVE_USER     = "/" + AUTH_PATH + "user";   // POST
    URL_SAVE_GROUP    = "/" + AUTH_PATH + "group";  // POST
    URL_DELETE_GROUP  = "/" + AUTH_PATH + "group/"; 
	URL_DEL_USER      = "/" + AUTH_PATH + "user/";
    URL_STOP_GROUP    = "/" + AUTH_PATH + "group/disable/"; 
    URL_SORT_GROUP    = "/" + AUTH_PATH + "group/sort/";
    URL_STOP_USER     = "/" + AUTH_PATH + "user/disable/";
	URL_GET_OPERATION = "/" + AUTH_PATH + "group/operations/"; 
    URL_USER_TREE     = "/" + AUTH_PATH + "user/tree/"; // user/tree/{groupId}
    URL_INIT_PASSWORD = "/" + AUTH_PATH + "user/initpwd/"; // user/initpwd/{groupId}/{userId}/{password}

    URL_SEARCH_SUBAUTH= "/" + AUTH_PATH + "search/subauth/";
    URL_SEARCH_ROLE   = "/" + AUTH_PATH + "search/roles/";
	URL_SYNC_GROUP    = "/" + AUTH_PATH + "group/sync/";
    URL_SYNC_PROGRESS = "/" + AUTH_PATH + "group/progress/";  // {code} GET
    URL_CANCEL_SYNC   = "/" + AUTH_PATH + "group/progress/";  // {code} DELETE
	
	if(IS_TEST) {
		URL_INIT = "data/group_tree.xml?";
		URL_USER_LIST = "data/user_grid.xml?";
		URL_USER_DETAIL = "data/user_detail.xml?";
		URL_GROUP_DETAIL = "data/group_detail.xml?";
		URL_SAVE_USER = "data/_success.xml?";
		URL_SAVE_GROUP = "data/_success.xml?";
		URL_DELETE_GROUP = "data/_success.xml?";
		URL_STOP_GROUP = "data/_success.xml?";
		URL_SORT_GROUP = "data/_success.xml?";
		URL_STOP_USER = "data/_success.xml?";
		URL_USER_TREE = "data/userlist.xml?";
		URL_DEL_USER = "data/_success.xml?";
		URL_INIT_PASSWORD = "data/_success.xml?";
		URL_GET_OPERATION = "data/operation.xml?";
		URL_SEARCH_SUBAUTH = "data/group_search_subauth.xml?";
		URL_SEARCH_ROLE = "data/group_search_role.xml?";
		URL_SYNC_GROUP = "data/progress.xml?";
		URL_SYNC_PROGRESS = "data/progress.xml?";
		URL_CANCEL_SYNC = "data/_success.xml?";
	}
 
 
    function init() {
        initPaletteResize();
        initListContainerResize();
        initNaviBar("um.1");
        initMenus();
        initBlocks();
        initWorkSpace();
        initEvents();
        initFocus();

        loadInitData();
    }

    function initMenus(){
        initTreeMenu();
        initGridMenu();
    }
	
	/* 是否为主用户组 */
    function isMainGroup(treeNode) {
        return (treeNode ? treeNode.getAttribute("groupType") : getTreeAttribute("groupType")) == "1";
    }
	
	/* 是否自注册用户组节点 */
    function isSelfRegisterGroup(id){
        if( id == null ) {
            var treeNode = $T("tree").getActiveTreeNode();
            if( treeNode ) {
                id = treeNode.getId();
            }            
        }
        return ("-7"==id || "-8"==id || "-9"==id);
    }
	
	function editable() {
		return !isTreeRoot() && getTreeNodeId() > 0 && getOperation("2");
	}

    function initTreeMenu(){
        var item1 = {
            label:"停用",
            callback:function() { stopOrStartTreeNode(URL_STOP_GROUP, "1", "user_group"); },
            icon:ICON + "stop.gif",
            visible:function(){return editable() && !isTreeNodeDisabled();}
        }
        var item2 = {
            label:"启用",
            callback:function() { stopOrStartTreeNode(URL_STOP_GROUP, "0", "user_group"); },
            icon:ICON + "start.gif",
            visible:function(){return editable() && isTreeNodeDisabled();}
        }
        var item3 = {
            label:"编辑",
            callback:editGroupInfo,
            icon:ICON + "edit.gif",
            visible:function(){return editable();}
        }
        var item4 = {
            label:"删除",
            callback:function() { delTreeNode(URL_DELETE_GROUP); },
            icon:ICON + "del.gif",
            visible:function(){return editable();}
        }
        var item6 = {
            label:"新建用户组",
            callback:addNewGroup,
            visible:function(){return !isSelfRegisterGroup() && getOperation("2");}
        }
        var item7 = {
            label:"新建用户",
            callback:addNewUser,
            visible:function(){return !isTreeRoot() && isMainGroup() && editable();}
        }
        var item8 = {
            label:"浏览用户",
            callback:function() { showUserList(); },
            icon:ICON + "view_list.gif",
            visible:function(){return !isTreeRoot() && isMainGroup() && getOperation("1"); }
        }
        var item9 = {
            label:"搜索用户...",
            callback:searchUser,
            icon:ICON + "search.gif",
            visible:function() {return !isTreeRoot() && isMainGroup() && getOperation("1");}
        }

        var item12 = {
            label:"高级功能",
            callback:null,
            visible:function() {return isMainGroup() && editable();}
        }
        var subitem12_1 = {
            label:"初始化密码...",
            callback:resetPassword,
            icon:ICON + "init_password.gif"
        }
		var subitem12_2 = {
            label:"用户同步",
            callback:function() { syncGroup() }
        }
        var subitem12_4 = {
            label:"综合查询",
            callback:null,
            icon:ICON + "search.gif"
        }
        var subitem12_4_1 = {
            label:"用户角色",
            callback:generalSearchRole
        }
		 var subitem12_4_2 = {
            label:"用户转授",
            callback:generalSearchReassign
        }

        var submenu12_4 = new Menu();
        submenu12_4.addItem(subitem12_4_1);
        submenu12_4.addItem(subitem12_4_2);
        subitem12_4.submenu = submenu12_4;

        var submenu12 = new Menu();
        submenu12.addItem(subitem12_1);
		submenu12.addItem(subitem12_2);
        submenu12.addItem(subitem12_4);
        item12.submenu = submenu12;
 
        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
		menu1.addItem(item3);
		menu1.addItem(item4);
		menu1.addItem(item6);
        menu1.addSeparator();
        menu1.addItem(item7);
        menu1.addItem(item8);
        menu1.addItem(item9);
        menu1.addSeparator();
        menu1.addItem(item12);

        $$("tree").contextmenu = menu1;
    }
 
    function initGridMenu(){
        var gridObj = $$("grid");
        var item1 = {
            label:"停用",
            callback:function() { stopOrStartUser("1"); },
            icon:ICON + "stop.gif",
            visible:function() { return getUserOperation("2") && "0" == getUserState(); }
        }
        var item2 = {
            label:"启用",
            callback:function() { stopOrStartUser("0"); },
            icon:ICON + "start.gif",
            visible:function() { return getUserOperation("2") && "1" == getUserState(); }
        }
        var item3 = {
            label:"编辑",
            callback:editUserInfo,
            icon:ICON + "edit.gif",
            visible:function() { return getUserOperation("2"); }
        }
        var item4 = {
            label:"删除",
            callback: function() { delGridRow(URL_DEL_USER); },
            icon:ICON + "del.gif",
            visible:function() { return getUserOperation("2"); }
        }
 
        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addItem(item4);
 
        gridObj.contextmenu = menu1;
    }
 
    function loadInitData() {
        var onresult = function(){
            var groupTreeNode = this.getNodeValue(XML_MAIN_TREE);
			Cache.XmlDatas.add(CACHE_MAIN_TREE, groupTreeNode);
            $T("tree", groupTreeNode);
			
			var treeObj = $$("tree");
			treeObj.onTreeNodeActived = function(eventObj){
                var treeTitleObj = $$("treeTitle");
				Focus.focus(treeTitleObj.firstChild.id);
				showTreeNodeInfo();
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj){
				var treeNode = eventObj.treeNode;
				if( isMainGroup(treeNode) ) {
					showTreeNodeInfo();
					getTreeOperation(treeNode, function(_operation) {
						showUserList();
					});
				}
            }
            treeObj.onTreeNodeMoved = function(eventObj){
                sort(eventObj);
            }
            treeObj.onTreeNodeRightClick = function(eventObj){
                var treeObj = $$("tree");
				var treeNode = eventObj.treeNode;

				showTreeNodeInfo();
				getTreeOperation(treeNode, function(_operation) {
					if( treeObj.contextmenu ) {
						treeObj.contextmenu.show(eventObj.clientX, eventObj.clientY);                
					}
				});
            }
        }
        
		Ajax({url : URL_INIT, method : "GET", onresult : onresult});
    }
 
	function sort(eventObj) {
		var movedNode  = eventObj.movedTreeNode;
		var movedNodeID = movedNode.getId();		
		if("-2" == movedNodeID || "-3" == movedNodeID ) {
			alert("不能移动此节点!");
			return;
		}

		sortTreeNode(URL_SORT_GROUP, eventObj);
	}
 
	/* 初始化密码  */
    function resetPassword(){
        var treeNode = $T("tree").getActiveTreeNode();
		var password = "";
		var first = true;
		while(password == "") {
			if( !first ) {
				alert("密码不能为空");
			}
			password = prompt("请输入新密码", "", "初始化'" + treeNode.getName() + "'的密码", true);
			first = false;
		}
 
		Ajax({
			url : URL_INIT_PASSWORD + treeNode.getId() + "/0/" + password
		});	     
    }
 
    function editGroupInfo(newGroupID) { 
		var isAddGroup = DEFAULT_NEW_ID == newGroupID;
	
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID   = isAddGroup ? newGroupID : treeNode.getId();
		var treeName = isAddGroup ? "用户组" : treeNode.getName();		
		var parentID = isAddGroup ? treeNode.getId() : treeNode.getParent().getId();		
		var groupType = treeNode.getAttribute("groupType");

		var phases = [];
		phases[0] = {page:"page1",label:"基本信息"};
		if( isMainGroup() ) { 
			phases[1] = {page:"page3",label:"角色"};
		} else {
			phases[1] = {page:"page4",label:"用户"};
			phases[2] = {page:"page3",label:"角色"};
		}		
		
		var callback = {};
		callback.onTabClose = function(eventObj){
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function(){
				loadGroupDetailData(treeID, parentID, groupType);
			}, TIMEOUT_TAB_CHANGE);
		};
		
		var inf = {};
		inf.defaultPage = "page1";
		inf.callback = callback;
		inf.label = (isAddGroup ? OPERATION_ADD : OPERATION_EDIT).replace(/\$label/i, treeName);
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
					
		inf.phases = phases;
		var tab = ws.open(inf);         
    }
 
    function addNewGroup() {
		editGroupInfo(DEFAULT_NEW_ID);
    }
	  
    /*
     *	树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                string:parentID             父节点id
     */
    function loadGroupDetailData(treeID, parentID, groupType) {
		var p = new HttpRequestParams();
		p.url = URL_GROUP_DETAIL + parentID + "/" + treeID + "/" + groupType;
 
		var request = new HttpRequest(p);
		request.onresult = function(){
			var groupInfoNode = this.getNodeValue(XML_GROUP_INFO);
			var group2UserTreeNode = Cache.XmlDatas.get(CACHE_MAIN_TREE).cloneNode(true);
			var group2UserGridNode = this.getNodeValue(XML_GROUP_TO_USER_EXIST_TREE);
			var group2RoleTreeNode = this.getNodeValue(XML_GROUP_TO_ROLE_TREE);
			var group2RoleGridNode = this.getNodeValue(XML_GROUP_TO_ROLE_EXIST_TREE);
 
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_INFO, groupInfoNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_USER_TREE, group2UserTreeNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_USER_EXIST_TREE, group2UserGridNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_ROLE_TREE, group2RoleTreeNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_ROLE_EXIST_TREE, group2RoleGridNode);
				
			var page1FormObj = $X("page1Form", groupInfoNode);
			attachReminder(page1FormObj.element.id, page1FormObj);
 
			var page3Tree  = $T("page3Tree",  group2RoleTreeNode);
			var page3Tree2 = $T("page3Tree2", group2RoleGridNode);
 
			if( !isMainGroup() ) { // 辅助用户组
				var page4Tree3 = $T("page4Tree3", group2UserGridNode);
				var page4Tree  = $T("page4Tree",  group2UserTreeNode);
				$$("page4Tree").onTreeNodeDoubleClick = function(eventObj) {
					var treeNode = page4Tree.getActiveTreeNode();
					Ajax({
						url : URL_USER_TREE + treeNode.getId(),
						onresult : function() { 
							var sourceListNode = this.getNodeValue(XML_GROUP_TO_USER_LIST_TREE);
							$T("page4Tree2", sourceListNode);
						}
					});	
				}				
			}
			
			// 设置翻页按钮显示状态
			$$("page1BtPrev").style.display = "none";
			$$("page4BtPrev").style.display = "";
			$$("page3BtPrev").style.display = "";
			$$("page1BtNext").style.display = "";
			$$("page4BtNext").style.display = "";
			$$("page3BtNext").style.display = "none";

			// 设置保存按钮操作
			$$("page1BtSave").onclick = $$("page4BtSave").onclick = $$("page3BtSave").onclick = function(){
				saveGroup(treeID, parentID, groupType);
			}

			// 设置添加按钮操作
			$$("page3BtAdd").onclick = function() {
				addTreeNode(page3Tree, page3Tree2);
			}

			// 设置添加按钮操作
			$$("page4BtAdd").onclick = function(){
				addTreeNode($T("page4Tree2"), page4Tree3);
			}

			// 设置删除按钮操作
			$$("page3BtDel").onclick = function(){
				 removeTreeNode(page3Tree2);
			}
			$$("page4BtDel").onclick = function(){
				 removeTreeNode(page4Tree3);
			}
		}
		request.send();
    }
 
    /*
     *	保存用户组
、   */
    function saveGroup(treeID, parentID, groupType){
        var page1FormObj = $X("page1Form");
        if( !page1FormObj.checkForm() ) {
            ws.switchToPhase("page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_GROUP;

        //是否提交
        var flag = false;
 
		//用户组基本信息
		var groupInfoNode = Cache.XmlDatas.get(treeID + "." + XML_GROUP_INFO);
		if( groupInfoNode ) {
			flag = true;
			var groupInfoDataNode = groupInfoNode.selectSingleNode(".//data");
			p.setXFormContent(groupInfoDataNode);
		}

		// 用户组对用户
		if( !isMainGroup() ) {
			var group2UserNode = Cache.XmlDatas.get(treeID + "." + XML_GROUP_TO_USER_EXIST_TREE);
			if(group2UserNode) {
				var group2UserDataIDs = getTreeNodeIds(group2UserNode);
				if(group2UserDataIDs.length > 0) {
					p.setContent(XML_GROUP_TO_USER_EXIST_TREE, group2UserDataIDs.join(","));
				}
			}
		}

		// 用户组对角色
		var group2RoleNode = Cache.XmlDatas.get(treeID + "." + XML_GROUP_TO_ROLE_EXIST_TREE);
		if( group2RoleNode) {
			var group2RoleDataIDs = getTreeNodeIds(group2RoleNode);
			if( group2RoleDataIDs.length > 0) {
				p.setContent(XML_GROUP_TO_ROLE_EXIST_TREE, group2RoleDataIDs.join(","));
			}
		}
 
        if( flag ) {
            var request = new HttpRequest(p);
			
            // 同步按钮状态
            syncButton([$$("page1BtSave"), $$("page4BtSave"), $$("page3BtSave")], request);

            request.onresult = function() {
				detachReminder(treeID); // 解除提醒

				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentID, treeNode);

				ws.closeActiveTab();
            }
            request.onsuccess = function() {
				detachReminder(treeID); // 解除提醒

				// 更新树节点名称
				var name = page1FormObj.getData("name");
				modifyTreeNode(treeID, "name", name, true);
            }
            request.send();
        }
    }
	
	/* 同步用户组 */
    function syncGroup(applicationId) {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeNodeID = treeNode.getId();

		var p = new HttpRequestParams();
		p.url = URL_SYNC_GROUP;

		var dbGroupId = treeNode.getAttribute("dbGroupId");
		p.setContent("applicationId", applicationId);
		p.setContent("groupId", treeNodeID);
		p.setContent("dbGroupId", dbGroupId);

		var request = new HttpRequest(p);

		request.onresult = function(){
			var data = this.getNodeValue("ProgressInfo");
			var progress = new Progress(URL_SYNC_PROGRESS,data,URL_CANCEL_SYNC);
			progress.oncomplete = function(){
				loadInitData();
			}
			progress.start();
		}
		request.send();
    }
	
    function getUserOperation(code) {
        var flag = false;
        var rowIndex = $$("grid").selectRowIndex; 
        if(rowIndex) {
            var row = $G("grid").getRowByIndex(rowIndex);
			var groupId = row.getAttribute("groupId");  
			var groupNode = $T("tree").getTreeNodeById(groupId);
            var _operation = groupNode.getAttribute("_operation");
			flag = checkOperation(code, _operation);
        }
        return flag;
    }
 
    /* 显示用户列表 */
    function showUserList(groupId) {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = groupId || treeNode.getId();

		var p = new HttpRequestParams();
		p.url = URL_USER_LIST + treeID + "/1";
		var request = new HttpRequest(p);
		request.onresult = function() {
			$G("grid", this.getNodeValue(XML_USER_LIST)); 
			var gridToolBar = $$("gridToolBar");

			var pageListNode = this.getNodeValue(XML_PAGE_INFO);			
			initGridToolBar(gridToolBar, pageListNode, function(page) {
				request.params.url = XML_USER_LIST + treeID + "/" + page;
				request.onresult = function() {
					$G("grid", this.getNodeValue(XML_USER_LIST)); 
				}				
				request.send();
			} );
			
			var gridElement = $$("grid"); 
			gridElement.onDblClickRow = function(eventObj) {
				editUserInfo();
			}
			gridElement.onRightClickRow = function() {
				$$("grid").contextmenu.show(event.clientX, event.clientY);
			}   
			gridElement.onScrollToBottom = function () {			
				var currentPage = gridToolBar.getCurrentPage();
				if(gridToolBar.getTotalPages() <= currentPage) return;

				var nextPage = parseInt(currentPage) + 1; 
				request.params.url = XML_USER_LIST + treeID + "/" + nextPage;
				request.onresult = function() {
					$G("grid").load(this.getNodeValue(XML_REPORT_DATA), true);
					initGridToolBar(gridToolBar, this.getNodeValue(XML_PAGE_INFO));
				}				
				request.send();
			}
		}
		request.send();
	}

 
    function addNewUser() {
        var treeNode = $T("tree").getActiveTreeNode();
		var groupId = treeNode.getId();

		loadUserInfo(OPERATION_ADD, DEFAULT_NEW_ID, "用户", groupId);
    }
 
    function editUserInfo() {
        var rowIndex = $$("grid").selectRowIndex; 
		var row = $G("grid").getRowByIndex(rowIndex);
		var rowID = row.getAttribute("id");   
		var rowName = row.getAttribute("userName");   
 
		loadUserInfo(OPERATION_EDIT, rowID, rowName);
    }
	
	function loadUserInfo(operationName, rowID, rowName, groupId) {
		var phases = [];
		phases[0] = {page:"page1",label:"基本信息"};
		phases[1] = {page:"page8",label:"认证信息"};
		phases[2] = {page:"page2",label:"用户组"};
		phases[3] = {page:"page3",label:"角色"};

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function(){
				loadUserDetailData(rowID, groupId);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = operationName.replace(/\$label/i, rowName || "用户");
		inf.SID = CACHE_GRID_ROW_DETAIL + rowID;
		inf.defaultPage = "page1";
		inf.phases = phases;
		inf.callback = callback;
		var tab = ws.open(inf);
	}
 
    function loadUserDetailData(userID, groupId) {
		var p = new HttpRequestParams();
		p.url = URL_USER_DETAIL + groupId + "/" + userID;
 
		var request = new HttpRequest(p);
		request.onresult = function(){
			var userInfoNode = this.getNodeValue(XML_USER_INFO);
			var authenticateInfoNode = this.getNodeValue(XML_AUTHENTICATE_INFO);
			var user2GroupExistTreeNode = this.getNodeValue(XML_USER_TO_GROUP_EXIST_TREE);
			var user2GroupTreeNode = Cache.XmlDatas.get(CACHE_MAIN_TREE).cloneNode(true);
			var user2RoleTreeNode = this.getNodeValue(XML_USER_TO_ROLE_TREE);
			var user2RoleGridNode = this.getNodeValue(XML_USER_TO_ROLE_EXIST_TREE);
			
			// 过滤掉辅助用户组 和系统级用户组 和 角色组
			disableTreeNodes(user2GroupTreeNode, "//treeNode[@groupType='2']");
			disableTreeNodes(user2GroupTreeNode, "//treeNode[@id < 0]");
			disableTreeNodes(user2RoleTreeNode, "//treeNode[@isGroup='1']");
 
			Cache.XmlDatas.add(userID + "." + XML_USER_INFO, userInfoNode);
			Cache.XmlDatas.add(userID + "." + XML_AUTHENTICATE_INFO, authenticateInfoNode);
			Cache.XmlDatas.add(userID + "." + XML_USER_TO_GROUP_EXIST_TREE, user2GroupExistTreeNode);
			Cache.XmlDatas.add(userID + "." + XML_USER_TO_GROUP_TREE, user2GroupTreeNode);
			Cache.XmlDatas.add(userID + "." + XML_USER_TO_ROLE_TREE, user2RoleTreeNode);
			Cache.XmlDatas.add(userID + "." + XML_USER_TO_ROLE_EXIST_TREE, user2RoleGridNode);
			
			var page1FormObj = $X("page1Form", userInfoNode);
			attachReminder(page1FormObj.element.id, page1FormObj);
			
			var page8FormObj = $X("page8Form", authenticateInfoNode);
			attachReminder(page8FormObj.element.id, page1FormObj);
			
			var page3Tree  = $T("page3Tree",  user2RoleTreeNode);
			var page3Tree2 = $T("page3Tree2", user2RoleGridNode);
			var page2Tree  = $T("page2Tree",  user2GroupTreeNode);
			var page2Tree2 = $T("page2Tree2", user2GroupExistTreeNode);
			
            page2Tree2.groupType = "1"; // 标记当前page2Tree2是主(辅助)用户组

			// 设置翻页按钮显示状态
			$$("page1BtPrev").style.display = "none";
			$$("page8BtPrev").style.display = "";
			$$("page2BtPrev").style.display = "";
			$$("page3BtPrev").style.display = "";
			$$("page1BtNext").style.display = "";
			$$("page8BtNext").style.display = "";
			$$("page2BtNext").style.display = "";
			$$("page3BtNext").style.display = "none";

			//设置保存按钮操作
			$$("page1BtSave").onclick = $$("page8BtSave").onclick = $$("page2BtSave").onclick = $$("page3BtSave").onclick = function(){
				saveUser(userID, groupId);
			}

			// 设置添加按钮操作
			$$("page2BtAdd").onclick = function(){
				addTreeNode(page2Tree, page2Tree2, function(treeNode){
					var result = {
						"error":false,
						"message":"",
						"stop":true
					};
					var groupType = treeNode.getAttribute("groupType");
					if( groupType == "1" && hasSameAttributeTreeNode(page2Tree2, "groupType", groupType)){
						result.error = true;
						result.message = "一个用户对应主用户组只允许一个";
						result.stop = true;
					}
					if( groupType == "2"){
						result.error = true;
						result.message = "此处只能选择主用户组下的组织";
						result.stop = true;
					}
					return result;
				});
			}
			$$("page3BtAdd").onclick = function(){
				addTreeNode(page3Tree, page3Tree2, function(treeNode){
					var result = {
						"error":false,
						"message":"",
						"stop":true
					};
					if( treeNode.getAttribute("isGroup") == "1"){
						result.error = true;
						result.message = null;
						result.stop = false;
					}
					return result;
				});
			}

			// 设置删除按钮操作
			$$("page2BtDel").onclick = function(){
				removeTreeNode(page2Tree2);
			}
			$$("page3BtDel").onclick = function(){
				 removeTreeNode(page3Tree2);
			}
		}
		request.send();
    }

    function saveUser(userID, groupId){
        var page1FormObj = $X("page1Form");
        var page8FormObj = $X("page8Form");
        if( !page1FormObj.checkForm() ) {
            ws.switchToPhase("page1");
            return;
        }
        if( !page8FormObj.checkForm() ){
            ws.switchToPhase("page8");
            return;
        }

        // 校验用户对组page2Tree2数据有效性
        var page2Tree2 = $T("page2Tree2");
        var user2GroupNode = new XmlNode(page2Tree2.getTreeNodeById("_rootId").node);
        var groupType1TreeNode = user2GroupNode.selectSingleNode(".//treeNode[@groupType='1']");
        if( groupType1TreeNode == null ) {
            ws.switchToPhase("page2");
            var balloon = Balloons.create("至少要有一个主用户组");
            balloon.dockTo(page2Tree2.element);
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_USER;

        // 是否提交
        var flag = false;
 
		// 用户基本信息
		var userInfoNode = Cache.XmlDatas.get(userID + "." + XML_USER_INFO);
		if(userInfoNode) {			
			flag = true;
			var userInfoDataNode = userInfoNode.selectSingleNode(".//data");
			p.setXFormContent(userInfoDataNode);
		}

		//认证基本信息
		var authenticateInfoNode = Cache.XmlDatas.get(userID + "." + XML_AUTHENTICATE_INFO);
		if(authenticateInfoNode) {
			var authenticateInfoDataNode = authenticateInfoNode.selectSingleNode(".//data");
			p.setXFormContent(authenticateInfoDataNode);
		}

		//用户对用户组
		var user2GroupNode = Cache.XmlDatas.get(userID + "." + XML_USER_TO_GROUP_EXIST_TREE);
		if(user2GroupNode) {
			var user2GroupDataIDs = getTreeNodeIds(user2GroupNode);
			if( user2GroupDataIDs.length > 0 ) {
				p.setContent(XML_USER_TO_GROUP_EXIST_TREE, user2GroupDataIDs.join(","));

				// 主用户组id
				var mainGroupId = groupType1TreeNode.getAttribute("id");
				p.setContent("mainGroupId", mainGroupId);
			}
		}

		//用户对角色
		var user2RoleNode = Cache.XmlDatas.get(userID + "." + XML_USER_TO_ROLE_EXIST_TREE);
		if(user2RoleNode) {
			var user2RoleDataIDs = getTreeNodeIds(user2RoleNode);
			if( user2RoleDataIDs.length > 0) {
				p.setContent(XML_USER_TO_ROLE_EXIST_TREE, user2RoleDataIDs.join(","));
			}
		}

		if( flag ) {
			var request = new HttpRequest(p);
			
			//同步按钮状态
			syncButton([$$("page1BtSave"), $$("page2BtSave"), $$("page3BtSave"), $$("page8BtSave")], request);

			request.onsuccess = function(){
				// 解除提醒
				detachReminder(page1FormObj.element.id);
				detachReminder(page8FormObj.element.id);

				// 如果当前grid显示为此用户所在组，则刷新grid
				showUserList(groupId);
	 
				ws.closeActiveTab();
			}
			request.send();
		}
	}
 
    /* 获取用户状态 */
    function getUserState(){
		var rowIndex = $$("grid").selectRowIndex; 
        if(rowIndex) {
            var row = $G("grid").getRowByIndex(rowIndex);
            return row.getAttribute("userstate");
		}
        return null;   
    }
 
    function stopOrStartUser(state) {
        var rowIndex = $$("grid").selectRowIndex; 
        if(rowIndex) {
			var grid = $G("grid");
            var row = grid.getRowByIndex(rowIndex);
            var userID = row.getAttribute("id");

            var p = new HttpRequestParams();
            p.url = URL_STOP_USER + userID + "/" + state;

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                // 成功后设置状态
                grid.modifyRow(row, "userstate", state);
                grid.modifyRow(row, "icon", ICON + "user_2.gif");
				
				if (state == "0") {
					// 启用组
					var treeNode = $T("tree").getTreeNodeById(groupID);
					if(treeNode) {
						var xmlNode = new XmlNode(treeNode.node);
						refreshGroupStates(xmlNode, "0");
					}
				}
            }
            request.send();
        }
    }  
 
    /* 搜索用户 */
    function searchUser(){
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		window.showModalDialog("searchuser.htm", {groupId:treeID,title:"搜索\"" + treeName + "\"下的用户"}, "dialogWidth:250px;dialogHeight:250px;");
    }

 
    /* 综合查询(用户角色查询) */
    function generalSearchRole(){
        var treeNode = $T("tree").getActiveTreeNode();  
		var groupId = treeNode.getId();
		var groupName = treeNode.getName();
		
		var callback = {};
		callback.onTabClose = function(eventObj){
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function(){
				var p = new HttpRequestParams();
				p.url = URL_SEARCH_ROLE;
				p.setContent("groupId", groupId);

				var request = new HttpRequest(p);
				request.onresult = function(){
					var generalSearchGridNode = this.getNodeValue(XML_SEARCH_ROLE);
					$G("page6Grid", generalSearchGridNode);
				}
				request.send();
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page6";
		inf.label = "用户角色" + OPERATION_SEARCH.replace(/\$label/i,groupName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_SEARCH_ROLE + groupId;
		var tab = ws.open(inf);
    }
	
	/* 综合查询(用户转授查询) */
    function generalSearchReassign(){
        var treeNode = $T("tree").getActiveTreeNode();
		var groupId = treeNode.getId();
		var groupName = treeNode.getName();
		
		var callback = {};
		callback.onTabClose = function(eventObj){
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function() {
				var p = new HttpRequestParams();
				p.url = URL_SEARCH_SUBAUTH;
				p.setContent("groupId", groupId);

				var request = new HttpRequest(p);
				request.onresult = function() {
					var generalSearchGridNode = this.getNodeValue(XML_SEARCH_SUBAUTH);
					$G("page6Grid", generalSearchGridNode);
				}
				request.send();
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page6";
		inf.label = "用户转授" + OPERATION_SEARCH.replace(/\$label/i,groupName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_SEARCH_SUBAUTH + groupId;
		var tab = ws.open(inf);
    }
 

    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();