    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "GroupTree";
    XML_USER_LIST = "SourceList";
    XML_GRID_SEARCH = "GridSearch";
    XML_SEARCH_USER = "SearchUserList";
 
    XML_USER_INFO = "UserInfo";
    XML_AUTHENTICATE_INFO = "AuthenticateInfo";
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
 
 
    XML_GENERAL_SEARCH_PERMISSION = "GeneralSearchPermissionInfo";
    XML_GENERAL_SEARCH_PERMISSION_LIST = "GeneralSearchPermissionList";
    XML_GENERAL_SEARCH_REASSIGN = "GeneralSearchUserStrategyInfoGrid";
    XML_GENERAL_SEARCH_ROLE = "GeneralSearchRoleGrid";
 
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_VIEW_GRID_ROW_DETAIL = "viewRow__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_VIEW_TREE_NODE_DETAIL = "viewTreeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_GROUP_TO_USER_GRID = "group2User__id";
    CACHE_SEARCH_USER = "searchUser__id";
    CACHE_GENERAL_SEARCH_PERMISSION = "generalSearchPermission__id";
    CACHE_GENERAL_SEARCH_PERMISSION_LIST = "generalSearchPermissionList__id";
    CACHE_GENERAL_SEARCH_REASSIGN = "generalSearchReassign__id";
    CACHE_GENERAL_SEARCH_ROLE = "generalSearchRole__id";
 
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/group_init.xml";
    URL_USER_LIST = "data/grid.xml";
    URL_USER_DETAIL = "data/user1.xml";
    URL_GROUP_DETAIL = "data/group1.xml";
    URL_SAVE_USER = "data/_success.xml";
    URL_SAVE_GROUP = "data/_success.xml";
    URL_DEL_GROUP = "data/_success.xml";
    URL_MOVE_USER = "data/_success.xml";
    URL_START_GROUP = "data/_success.xml";
    URL_STOP_GROUP = "data/_success.xml";
    URL_SORT_GROUP = "data/_success.xml";
    URL_STOP_USER = "data/_success.xml";
    URL_START_USER = "data/_success.xml";
    URL_SORT_USER = "data/_success.xml";
    URL_SORT_USER_CROSS_PAGE = "data/_success.xml";
    URL_SYNC_GROUP = "data/progress.xml";
    URL_SYNC_PROGRESS = "data/progress.xml";
    URL_CANCEL_SYNC_PROGRESS = "data/_success.xml";
    URL_GROUP_TO_USER_LIST = "data/userlist.xml";
    URL_DEL_USER = "data/_success.xml";
    URL_SEARCH_USER = "data/grid.xml";
    URL_RESET_PASSWORD = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_USER_OPERATION = "data/operation.xml";
    URL_GENERAL_SEARCH_PERMISSION = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_PERMISSION_LIST = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_REASSIGN = "data/group_general_search.xml";
    URL_GENERAL_SEARCH_ROLE = "data/group_general_search.xml";
 

    URL_INIT = "ums/group!getAllGroup2Tree.action";
    URL_USER_LIST = "ums/user!getUsersByGroupId.action";
    URL_USER_DETAIL = "ums/user!getUserInfoAndRelation.action";
    URL_GROUP_DETAIL = "ums/group!getGroupInfoAndRelation.action";
    URL_SAVE_USER = "ums/user!saveUser.action";
    URL_SAVE_GROUP = "ums/group!editGroup.action";
    URL_DEL_GROUP = "ums/group!deleteGroup.action";
    URL_MOVE_USER = "ums/user!moveUser.action";
    URL_START_GROUP = "ums/group!startOrStopGroup.action";
    URL_STOP_GROUP = "ums/group!startOrStopGroup.action";
    URL_SORT_GROUP = "ums/group!sortGroup.action";
    URL_STOP_USER = "ums/user!startOrStopUser.action";
    URL_START_USER = "ums/user!startOrStopUser.action";
    URL_SORT_USER = "ums/user!sortUser.action";
    URL_SORT_USER_CROSS_PAGE = "ums/user!sortUserCrossPage.action";
    URL_SYNC_GROUP = "ums/syncdata!syncData.action";
    URL_SYNC_PROGRESS = "ums/syncdata!getProgress.action";
    URL_CANCEL_SYNC_PROGRESS = "ums/syncdata!doConceal.action";
    URL_GROUP_TO_USER_LIST = "ums/user!getSelectedUsersByGroupId.action";
    URL_DEL_USER = "ums/user!deleteUser.action";
    URL_SEARCH_USER = "ums/user!searchUser.action";
    URL_RESET_PASSWORD = "ums/user!initPassword.action";
    URL_GET_OPERATION = "ums/group!getOperation.action";
    URL_GET_USER_OPERATION = "ums/user!getOperation.action";
    URL_GENERAL_SEARCH_PERMISSION_LIST = "ums/generalSearch!searchPermission.action";
    URL_GENERAL_SEARCH_REASSIGN = "ums/generalSearch!searchUserStrategyInfo.action";
    URL_GENERAL_SEARCH_ROLE = "ums/generalSearch!searchRolesInfo.action";
 
 
    function init(){
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
    function isMainGroup(){
        return getTreeAttribute("groupType") == "1";
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
            callback:function() { stopOrStartTreeNode("1"); },
            icon:ICON + "stop.gif",
            visible:function(){return editable() && isTreeNodeDisabled();}
        }
        var item2 = {
            label:"启用",
            callback:function() { stopOrStartTreeNode("0"); },
            icon:ICON + "start.gif",
            visible:function(){return editable() && !isTreeNodeDisabled();}
        }
        var item3 = {
            label:"编辑",
            callback:editGroupInfo,
            icon:ICON + "edit.gif",
            visible:function(){return editable();}
        }
        var item4 = {
            label:"删除",
            callback:delGroup,
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
            callback:showUserList,
            icon:ICON + "view_list.gif",
            visible:function(){return !isTreeRoot() && isMainGroup() && getOperation("1"); }
        }
        var item8 = {
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
        var subitem12_4_3 = {
            label:"用户权限",
            callback:generalSearchPermission
        }
        var subitem12_4_4 = {
            label:"用户转授",
            callback:generalSearchReassign
        }
        var subitem12_4_5 = {
            label:"用户角色",
            callback:generalSearchRole
        }

        var submenu12_4 = new Menu();
        submenu12_4.addItem(subitem12_4_3);
        submenu12_4.addItem(subitem12_4_4);
        submenu12_4.addItem(subitem12_4_5);
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
            callback:stopUser,
            icon:ICON + "stop.gif",
            visible:function(){return getUserOperation("u5") && "0"==getUserState();}
        }
        var item2 = {
            label:"启用",
            callback:startUser,
            icon:ICON + "start.gif",
            visible:function() { return getUserOperation("u4") && "1"==getUserState();}
        }
        var item3 = {
            label:"编辑",
            callback:editUserInfo,
            icon:ICON + "edit.gif",
            visible:function(){return getUserOperation("u2");}
        }
        var item4 = {
            label:"删除",
            callback:delUser,
            icon:ICON + "del.gif",
            enable:function(){return gridObj.canDelete();},
            visible:function(){return getUserOperation("u1");}
        }
        var item5 = {
            label:"移动到...",
            callback:moveUserTo,
            icon:ICON + "move.gif",
            visible:function(){return getUserOperation("u1");}
        }
        var item9 = {
            label:"查看",
            callback:function() {
                editUserInfo(false);
            },
            icon:ICON + "view.gif",
            visible:function(){return getUserOperation("u3");}
        }
 
        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item9);
        menu1.addItem(item3);
        menu1.addItem(item4);
        menu1.addItem(item5);
 
        gridObj.contextmenu = menu1;
    }
 
     function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function(){
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
                showUserList();
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
        request.send();
    }
 
	function sort(eventObj) {
		var movedNode  = eventObj.movedTreeNode;
		var targetNode = eventObj.toTreeNode;
		var direction  = eventObj.moveState; // -1: 往上, 1: 往下
		var movedNodeID = movedNode.getId();
		
		if("-2" == movedNodeID || "-3" == movedNodeID ) {
			alert("不能移动此节点!");
			return;
		}

		Ajax({
			url : URL_SORT_SOURCE + movedNodeID + "/" + targetNode.getId() + "/" + direction,
			onsuccess : function() { 
				 $T("tree").moveTreeNode(movedNode, targetNode, direction);
			}
		});
	}
 
    function stopOrStartTreeNode(state) {		
		var tree = $T("tree");
		var treeNode = tree.getActiveTreeNode();
		Ajax({
			url : URL_STOP_NODE + treeNode.getId() + "/" + state,
			onsuccess : function() { 
				// 刷新父子树节点停用启用状态: 启用上溯，停用下溯
				var curNode = new XmlNode(treeNode.node);
				refreshTreeNodeState(curNode, state);
		
				if("1"==state) {
					var childNodes = curNode.selectNodes(".//treeNode");
					for(var i=0; i < childNodes.length; i++) {                
						refreshTreeNodeState(childNodes[i], state);
					}
				} else if ("0" == state) {
					while( curNode && curNode.getAttribute("id") > 0 ) {
						refreshTreeNodeState(curNode, state);
						curNode = curNode.getParent();
					}            
				}
				
				tree.reload(); 
			}
		});
    }
 
    function refreshTreeNodeState(xmlNode, state) {
        xmlNode.setAttribute("disabled", state);
        xmlNode.setAttribute("icon", ICON + "user_group" + (state == "0" ? "" : "_2 ") + ".gif");
    }
	
	function delGroup() {
		if( !confirm("您确定要删除吗？") ) return;
		
		var tree = $T("tree");
		var treeNode = tree.getActiveTreeNode();
		Ajax({
			url : URL_DEL_GROUP + treeNode.getId(),
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
			url : URL_RESET_PASSWORD + treeNode.getId() + "/" + password
		});	     
    }
 
    function editGroupInfo(newGroupID) {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = newGroupID || treeNode.getId();
		var treeName = newGroupID ? "用户组" : treeNode.getName();
		var groupType = treeNode.getAttribute("groupType");
		var parentID = newGroupID ? treeID : treeNode.getParent().getId();

		var phases = [];
		phases[0] = {page:"page1",label:"基本信息"};
		phases[1] = {page:"page3",label:"角色"};
		
		var callback = {};
		callback.onTabClose = function(eventObj){
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function(){
				loadGroupDetailData(treeID, editable, parentID, groupType);
			}, TIMEOUT_TAB_CHANGE);
		};
		
		var inf = {};
		inf.defaultPage = "page1";
		inf.callback = callback;
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
					
		if( !isMainGroup() ) { 
			phases[2] = {page:"page4", label:"用户"};
		}
		inf.phases = phases;
		var tab = ws.open(inf);         
    }
 
    function addNewGroup() {
		editGroupInfo(DEFAULT_NEW_ID);
    }
	  
    /*
     *	树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentID             父节点id
     *	返回值：
     */
    function loadGroupDetailData(treeID, parentID, groupType) {
		var p = new HttpRequestParams();
		p.url = URL_GROUP_DETAIL + treeID + "/" + parentID + "/" + groupType;
 
		var request = new HttpRequest(p);
		request.onresult = function(){
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
			var page3BtAddObj = $$("page3BtAdd");
			page3BtAddObj.disabled = editable==false?true:false;
			page3BtAddObj.onclick = function() {
				addTreeNode(page3TreeObj, page3Tree2Obj, function(treeNode){
					var result = {
						"error": false,
						"message": "",
						"stop": true
					};
					if( isMainGroup() ) {
						result.error = true;
						result.message = null;
						result.stop = false;
					}
					return result;
				});
			}

			// 设置添加按钮操作
			$$("page4BtAdd").onclick = function(){
				addTreeNode(page4Tree2Obj, page4Tree3Obj);
			}

			// 设置删除按钮操作
			$$("page3BtDel").onclick = function(){
				 removeTreeNode(page3Tree2Obj);
			}
			$$("page4BtDel").onclick = function(){
				 removeTreeNode(page4Tree3Obj);
			}
			
			var groupInfoNode = this.getNodeValue(XML_GROUP_INFO);
			var group2UserTreeNode = Cache.XmlDatas.get(CACHE_MAIN_TREE).cloneNode();
			var group2UserGridNode = this.getNodeValue(XML_GROUP_TO_USER_EXIST_TREE);
			var group2RoleTreeNode = this.getNodeValue(XML_GROUP_TO_ROLE_TREE);
			var group2RoleGridNode = this.getNodeValue(XML_GROUP_TO_ROLE_EXIST_TREE);
 
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_INFO, groupInfoNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_USER_TREE, group2UserTreeNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_USER_EXIST_TREE, group2UserGridNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_ROLE_TREE, group2RoleTreeNode);
			Cache.XmlDatas.add(treeID + "." + XML_GROUP_TO_ROLE_EXIST_TREE, group2RoleGridNode);
				
			var page1FormObj = $X("page1Form", groupInfoNode);
            page1FormObj.editable = editable ? "true" : "false";

			attachReminder(roleInfoNodeID, page1FormObj);
 
			var page3Tree  = $T("page3Tree",  group2RoleTreeNode);
			var page3Tree2 = $T("page3Tree2", group2RoleGridNode);
 
			if( !isMainGroup() ) { // 辅助用户组
				var page4Tree3 = $T("page4Tree3", group2UserGridNode);
				var page4Tree  = $T("page4Tree",  group2UserTreeNode);
				$$("page4Tree").onTreeNodeDoubleClick = function(eventObj) {
					var treeNode = page4Tree.getActiveTreeNode();
					Ajax({
						url : URL_GROUP_TO_USER_LIST + treeNode.getId(),
						onresult : function() { 
							var sourceListNode = this.getNodeValue(XML_GROUP_TO_USER_LIST_TREE);
							$T("page4Tree2", sourceListNode);
						}
					});	
				}				
			}
		}
		request.send();
    }
 
    /*
     *	保存用户组
     *	参数：	string:treeID
                string:parentID     父节点id
                string:groupType    组类型
     *	返回值：
     */
    function saveGroup(treeID, parentID, groupType){
        var page1FormObj = $X("page1Form");
        if( !page1FormObj.checkForm() ) {
            switchToPhase(ws, "page1");
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
			var progress = new Progress(URL_SYNC_PROGRESS,data,URL_CANCEL_SYNC_PROGRESS);
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

		p.url = URL_USER_LIST + treeID + "/1/" + PAGESIZE;
		var request = new HttpRequest(p);
		request.onresult = function() {
			$G("grid", this.getNodeValue(XML_USER_LIST)); 
			var gridToolBar = $$("gridToolBar");

			var pageListNode = this.getNodeValue(XML_PAGE_INFO);			
			initGridToolBar(gridToolBar, pageListNode, function(page) {
				request.params.url = XML_USER_LIST + treeID + "/" + page + "/" + PAGESIZE;
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
				request.params.url = XML_USER_LIST + treeID + "/" + nextPage + "/" + PAGESIZE;
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

		loadUserInfo(operationName, DEFAULT_NEW_ID, "用户", groupId);
    }
 
    function editUserInfo() {
        var rowIndex = $$("grid").selectRowIndex; 
		var row = $G("grid").getRowByIndex(rowIndex);
		var rowID = row.getAttribute("id");   
		var rowName = row.getAttribute("userName");   
 
		loadUserInfo(operationName, rowID, rowName);
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
		p.url = URL_USER_DETAIL + userID + "/" + groupId;
 
		var request = new HttpRequest(p);
		request.onresult = function(){
			var userInfoNode = this.getNodeValue(XML_USER_INFO);
			var authenticateInfoNode = this.getNodeValue(XML_AUTHENTICATE_INFO);
			var user2GroupExistTreeNode = this.getNodeValue(XML_USER_TO_GROUP_EXIST_TREE);
			var user2GroupTreeNode = Cache.XmlDatas.get(CACHE_MAIN_TREE).cloneNode();
			var user2RoleTreeNode = this.getNodeValue(XML_USER_TO_ROLE_TREE);
			var user2RoleGridNode = this.getNodeValue(XML_USER_TO_ROLE_EXIST_TREE);
 
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
				saveUser(cacheID, groupId);
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
					if( groupType == "1" && hasSameAttributeTreeNode(page2Tree2Obj,"groupType",groupType)){
						result.error = true;
						result.message = "一个用户对应主用户组只允许一个";
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
				removeTreeNode(page2Tree2Obj);
			}
			$$("page3BtDel").onclick = function(){
				 removeTreeNode(page3Tree2Obj);
			}
		}
		request.send();
    }

    function saveUser(cacheID, groupId){
        var page1FormObj = $X("page1Form");
        var page8FormObj = $X("page8Form");
        if( !page1FormObj.checkForm() ) {
            switchToPhase(ws, "page1");
            return;
        }
        if( !page8FormObj.checkForm() ){
            switchToPhase(ws, "page8");
            return;
        }

        // 校验用户对组page2Tree2数据有效性
        var page2Tree2 = $T("page2Tree2");
        var user2GroupNode = new XmlNode(page2Tree2.getTreeNodeById("_rootId").node);
        var groupType1TreeNode = user2GroupNode.selectSingleNode(".//treeNode[@groupType='1']");
        if( groupType1TreeNode == null ) {
            switchToPhase(ws, "page2");
            var balloon = Balloons.create("至少要有一个主用户组");
            balloon.dockTo(page2Tree2.element);
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_USER;

        // 是否提交
        var flag = false;
 
		// 用户基本信息
		var userInfoNode = Cache.XmlDatas.get(cacheID+"."+XML_USER_INFO);
		if(userInfoNode) {			
			flag = true;
			var userInfoDataNode = userInfoNode.selectSingleNode(".//data");
			p.setXFormContent(userInfoDataNode);
		}

		//认证基本信息
		var authenticateInfoNode = Cache.XmlDatas.get(cacheID+"."+XML_AUTHENTICATE_INFO);
		if(authenticateInfoNode) {
			var authenticateInfoDataNode = authenticateInfoNode.selectSingleNode(".//data");
			p.setXFormContent(authenticateInfoDataNode);
		}

		//用户对用户组
		var user2GroupNode = Cache.XmlDatas.get(cacheID+"."+XML_USER_TO_GROUP_EXIST_TREE);
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
		var user2RoleNode = Cache.XmlDatas.get(cacheID+"."+XML_USER_TO_ROLE_EXIST_TREE);
		if(user2RoleNode) {
			var user2RoleDataIDs = getTreeNodeIds(user2RoleNode);
			if( user2RoleDataIDs.length > 0) {
				p.setContent(XML_USER_TO_ROLE_EXIST_TREE, user2RoleDataIDs.join(","));
			}
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
 
    /* 获取用户状态 */
    function getUserState(){
		var rowIndex = $$("grid").selectRowIndex; 
        if(rowIndex) {
            var row = $G("grid").getRowByIndex(rowIndex);
            return row.getAttribute("disabled");
		}
        return null;   
    }
 
    function stopOrStartUser(state) {
        var rowIndex = $$("grid").selectRowIndex; 
        if(rowIndex) {
            var row = $G("grid").getRowByIndex(rowIndex);
            var userID = row.getAttribute("id");

            var p = new HttpRequestParams();
            p.url = URL_STOP_USER + userID + "/" + state;

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                // 成功后设置状态
                $G("grid").modifyRow(curRowIndex, "disabled", state);
                $G("grid").modifyRow(curRowIndex, "icon", ICON + "user_2.gif");
				
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
 
    function delUser(){
        if( !confirm("您确定要删除吗？")) return;
		
		var rowIndex = $$("grid").selectRowIndex; 
        if(rowIndex) {
            var row = $G("grid").getRowByIndex(rowIndex);
			var userID = row.getAttribute("id");  
			
			Ajax({
			url : URL_DEL_USER + userID,
			method : "DELETE",
			onsuccess : function() { 
				 // 从grid上删除
				$G("grid").deleteRow(rowIndex);
			}
		});	
    }
 
    /* 搜索用户 */
    function searchUser(){
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		window.showModalDialog("searchuser.htm", {groupId:treeID,title:"搜索\"" + treeName + "\"下的用户"}, "dialogWidth:250px;dialogHeight:250px;");
    }
 

  
    /*
     *	综合查询(用户授权查询)
     */
    function generalSearchPermission(){
        var treeNode = $T("tree").getActiveTreeNode();
		var groupId = treeNode.getId();
		var groupName = treeNode.getName();
		
		var callback = {};
		callback.onTabClose = function(eventObj){
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function(){
				loadGeneralSearchPermissionData(groupId);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page7";
		inf.label = "用户权限" + OPERATION_SEARCH.replace(/\$label/i,groupName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_GENERAL_SEARCH_PERMISSION + groupId;
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
				p.url = URL_GENERAL_SEARCH_REASSIGN;
				p.setContent("groupId", groupId);

				var request = new HttpRequest(p);
				request.onresult = function() {
					var generalSearchGridNode = this.getNodeValue(XML_GENERAL_SEARCH_REASSIGN);
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
		inf.SID = CACHE_GENERAL_SEARCH_REASSIGN + groupId;
		var tab = ws.open(inf);
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
				p.url = URL_GENERAL_SEARCH_ROLE;
				p.setContent("groupId", groupId);

				var request = new HttpRequest(p);
				request.onresult = function(){
					var generalSearchGridNode = this.getNodeValue(XML_GENERAL_SEARCH_ROLE);
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
		inf.SID = CACHE_GENERAL_SEARCH_ROLE + groupId;
		var tab = ws.open(inf);
    }
 

    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();