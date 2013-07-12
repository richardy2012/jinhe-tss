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
        initToolBar();
        initNaviBar("ums.1");
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
	
    
	function getGridOperation(rowIndex, callback){		
		var rowIndex = $$("grid").selectRowIndex; 
		var row = $G("grid").getRowByIndex(rowIndex);
		var groupId = row.getAttribute("groupId");  

		var groupNode = $T("tree").getTreeNodeById("_rootId");
		getTreeOperation(groupNode, callback);
    }

		
   
    /* 显示用户列表 */
    function showUserList(){
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();

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

 
    function addNewUser(){
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
		p.url = URL_USER_DETAIL;
		//如果是新增
		if(true==isNew){
			p.setContent("userId", "-10");
			p.setContent("groupId", groupId);
			p.setContent("applicationId", applicationId);
			p.setContent("disabled", disabled);
		}else{
			p.setContent("userId", userID);
			p.setContent("groupId", groupId);
		}

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

		}
		request.send();
 
    }
    /*
     *	用户相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:isNew           是否新增
                string:groupId          用户组id
     *	返回值：
     */
    function initUserPages(cacheID,editable,isNew,groupId){
        var page1FormObj = $$("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadUserInfoFormData(cacheID,editable);
        });

        var page8FormObj = $$("page8Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadAuthenticateInfoFormData(cacheID,editable);
        });

        var page2TreeObj = $$("page2Tree");
        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function(){
            loadUser2GroupTreeData(cacheID); // XML_USER_TO_GROUP_TREE
        });

        var page2Tree2Obj = $$("page2Tree2");
        Public.initHTC(page2Tree2Obj,"isLoaded","oncomponentready",function(){
            loadUser2GroupExistTreeData(cacheID); // XML_USER_TO_GROUP_EXIST_TREE

            //标记当前page2Tree2是主(辅助)用户组
            page2Tree2Obj.groupType = "1";
        });

        var page3TreeObj = $$("page3Tree");
        Public.initHTC(page3TreeObj,"isLoaded","oncomponentready",function(){
            loadUser2RoleTreeData(cacheID); // XML_USER_TO_ROLE_TREE
        });

        var page3Tree2Obj = $$("page3Tree2");
        Public.initHTC(page3Tree2Obj,"isLoaded","oncomponentready",function(){
            loadUser2RoleExistTreeData(cacheID); // XML_USER_TO_ROLE_EXIST_TREE
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $$("page1BtPrev");
        var page8BtPrevObj = $$("page8BtPrev");
        var page2BtPrevObj = $$("page2BtPrev");
        var page3BtPrevObj = $$("page3BtPrev");
        var page1BtNextObj = $$("page1BtNext");
        var page8BtNextObj = $$("page8BtNext");
        var page2BtNextObj = $$("page2BtNext");
        var page3BtNextObj = $$("page3BtNext");
        page1BtPrevObj.style.display = "none";
        page8BtPrevObj.style.display = "";
        page2BtPrevObj.style.display = "";
        page3BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page8BtNextObj.style.display = "";
        page2BtNextObj.style.display = "";
        page3BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page1BtSaveObj = $$("page1BtSave");
        var page8BtSaveObj = $$("page8BtSave");
        var page2BtSaveObj = $$("page2BtSave");
        var page3BtSaveObj = $$("page3BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page8BtSaveObj.disabled = editable==false?true:false;
        page2BtSaveObj.disabled = editable==false?true:false;
        page3BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page8BtSaveObj.onclick = page2BtSaveObj.onclick = page3BtSaveObj.onclick = function(){
            saveUser(cacheID,isNew,groupId);
        }

        //设置搜索
        var page2BtSearchObj = $$("page2BtSearch");
        var page2KeywordObj = $$("page2Keyword");
        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置搜索
        var page3BtSearchObj = $$("page3BtSearch");
        var page3KeywordObj = $$("page3Keyword");
        attachSearchTree(page3TreeObj,page3BtSearchObj,page3KeywordObj);

        //设置添加按钮操作
        var page2BtAddObj = $$("page2BtAdd");
        page2BtAddObj.disabled = editable==false?true:false;
        page2BtAddObj.onclick = function(){
            addTreeNode(page2TreeObj,page2Tree2Obj,function(treeNode){
                var result = {
                    "error":false,
                    "message":"",
                    "stop":true
                };
                var groupType = treeNode.getAttribute("groupType");
                if("1"==groupType && true==hasSameAttributeTreeNode(page2Tree2Obj,"groupType",groupType)){
                    result.error = true;
                    result.message = "一个用户对应主用户组只允许一个";
                    result.stop = true;
                }
                return result;
            });
        }

        //设置添加按钮操作
        var page3BtAddObj = $$("page3BtAdd");
        page3BtAddObj.disabled = editable==false?true:false;
        page3BtAddObj.onclick = function(){
            addTreeNode(page3TreeObj,page3Tree2Obj,function(treeNode){
                var result = {
                    "error":false,
                    "message":"",
                    "stop":true
                };
                if("1"==treeNode.getAttribute("isGroup")){
                    result.error = true;
                    result.message = null;
                    result.stop = false;
                }
                return result;
            });
        }

        //设置删除按钮操作
        var page2BtDelObj = $$("page2BtDel");
        page2BtDelObj.disabled = editable==false?true:false;
        page2BtDelObj.onclick = function(){
            removeTreeNode(page2Tree2Obj);
        }

        //设置删除按钮操作
        var page3BtDelObj = $$("page3BtDel");
        page3BtDelObj.disabled = editable==false?true:false;
        page3BtDelObj.onclick = function(){
             removeTreeNode(page3Tree2Obj);
        }
    }
    /*
     *	用户信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadUserInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_USER_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $$("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            page1FormObj.ondatachange = function(){
                //2007-3-1 离开提醒
                attachReminder(cacheID);
 
            }
        }
    }
	
	/*
     *	认证信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadAuthenticateInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_AUTHENTICATE_INFO);
        if(null!=xmlIsland){
            var page8FormObj = $$("page8Form");
            page8FormObj.editable = editable==false?"false":"true";
            page8FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page8FormObj);
        }
    }
	
  
 
    /*
     *	保存用户
     *	参数：	string:cacheID          缓存数据ID
                boolean:isNew           是否新增
                string:groupId          用户组id
     *	返回值：
     */
    function saveUser(cacheID,isNew,groupId){
        //校验page1Form,page8Form数据有效性
        var page1FormObj = $$("page1Form");
        var page8FormObj = $$("page8Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }
        if(false==page8FormObj.checkForm()){
            switchToPhase(ws,"page8");
            return;
        }
        if("0" == page1FormObj.securityLevel){
            switchToPhase(ws,"page1");
            showPasswordSecurityLevel(page1FormObj);
            return;
        }

        //校验用户对组page2Tree2数据有效性
        var page2Tree2Obj = $$("page2Tree2");
        var user2GroupNode = new XmlNode(page2Tree2Obj.getTreeNodeById("_rootId").node);
        var groupType1TreeNode = user2GroupNode.selectSingleNode(".//treeNode[@groupType='1']");
        if(null==groupType1TreeNode){
            switchToPhase(ws,"page2");
            var balloon = Balloons.create("至少要有一个主用户组");
            balloon.dockTo(page2Tree2Obj);
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_USER;

        //是否提交
        var flag = false;
        
        var userCache = Cache.Variables.get(cacheID);
        if(null!=userCache){

            //用户基本信息
            var userInfoNode = Cache.XmlDatas.get(cacheID+"."+XML_USER_INFO);
            if(null!=userInfoNode){
                var userInfoDataNode = userInfoNode.selectSingleNode(".//data");
                if(null!=userInfoDataNode){
                    flag = true;

                    var prefix = userInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(userInfoDataNode,prefix);
                }
            }

            //认证基本信息
            var authenticateInfoNode = Cache.XmlDatas.get(cacheID+"."+XML_AUTHENTICATE_INFO);
            if(null!=authenticateInfoNode){
                var authenticateInfoDataNode = authenticateInfoNode.selectSingleNode(".//data");
                if(null!=userInfoDataNode){
                    flag = true;

                    var prefix = authenticateInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(authenticateInfoDataNode,prefix);
                }
            }

            //用户对用户组
            var user2GroupNode = Cache.XmlDatas.get(cacheID+"."+XML_USER_TO_GROUP_EXIST_TREE);
            if(null!=user2GroupNode){
                var user2GroupDataIDs = getTreeNodeIds(user2GroupNode);
                if(0<user2GroupDataIDs.length){
                    flag = true;
                    p.setContent(XML_USER_TO_GROUP_EXIST_TREE,user2GroupDataIDs.join(","));

					//主用户组id
					var mainGroupId = groupType1TreeNode.getAttribute("id");
                    p.setContent("mainGroupId",mainGroupId);
                }
            }

            //用户对角色
            var user2RoleNode = Cache.XmlDatas.get(cacheID+"."+XML_USER_TO_ROLE_EXIST_TREE);
            if(null!=user2RoleNode){
                var user2RoleDataIDs = getTreeNodeIds(user2RoleNode);
                if(0<user2RoleDataIDs.length){
                    flag = true;
                    p.setContent(XML_USER_TO_ROLE_EXIST_TREE,user2RoleDataIDs.join(","));
                }
            }
        }

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $$("page1BtSave");
            var page2BtSaveObj = $$("page2BtSave");
            var page3BtSaveObj = $$("page3BtSave");
            var page8BtSaveObj = $$("page8BtSave");
            syncButton([page1BtSaveObj,page2BtSaveObj,page3BtSaveObj,page8BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);

                //清除该组用户grid缓存
                delCacheData(CACHE_TREE_NODE_GRID + groupId);

                //如果当前grid显示为此用户所在组，则刷新grid
                var gridObj = $$("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempGroupId = tempXmlIsland.getAttribute("groupId");
                    if(tempGroupId==groupId){
                        var tempApplicationId = tempXmlIsland.getAttribute("applicationId");
                        var tempGroupType = tempXmlIsland.getAttribute("groupType");

                        loadGridData(tempGroupId,tempApplicationId,tempGroupType,"1");//默认第1页

                        //刷新工具条
                        onInactiveRow();
                    }
                }

                if(true==isNew){
                    var ws = $$("ws");
                    ws.closeActiveTab();
                }
            }
            request.send();;
        }
    }

 
    function moveUserTo(){
        var gridObj = $$("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var userID = rowNode.getAttribute("id");
        var userName = rowNode.getAttribute("userName");
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        var applicationID = gridObj.getXmlDocument().getAttribute("applicationId");
        var groupID = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupID){
			if(null!=rowIndex){
				var curRowNode = gridObj.getRowNode_Xml(rowIndex);
				groupID = curRowNode.getAttribute("groupId");
			}
        }

        //弹出窗口数据从后台获取
        var xmlIsland = null;

        var group = getGroup(groupID,xmlIsland,"移动\""+userName+"\"到",groupType,applicationID,"2");
        if(null!=group){
            var p = new HttpRequestParams();
            p.url = URL_MOVE_USER;
            p.setContent("toGroupId",group.id);
            p.setContent("userId",userID);
            p.setContent("groupId",groupID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //从grid上删除
                gridObj.delRow_Xml(rowIndex);

                //刷新工具条
                onInactiveRow();
            }
            request.send();
        }
    }

 
    /*
     *	获取用户状态
     */
    function getUserState(){
        var userState = null;
        var gridObj = $$("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            userState = curRowNode.getAttribute("disabled");
        }
        return userState;   
    }
    /*
     *	停用用户
     */
    function stopUser() {
        var gridObj = $$("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");

            var p = new HttpRequestParams();
            p.url = URL_STOP_USER;
            p.setContent("userId",curRowID);
            p.setContent("accountState","1");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //成功后设置状态
                gridObj.modifyNamedNode_Xml(curRowIndex,"disabled","1");
                gridObj.modifyNamedNode_Xml(curRowIndex,"icon",ICON + "user_2.gif");
 
            }
            request.send();
        }
    }
    /*
     *	启用用户
     */
    function startUser(){
        var gridObj = $$("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");
            var groupID = gridObj.getXmlDocument().getAttribute("groupId");
            if("search"==groupID){
                groupID = curRowNode.getAttribute("groupId");
            }

            var p = new HttpRequestParams();
            p.url = URL_START_USER;
            p.setContent("userId",curRowID);
            p.setContent("groupId",groupID);
            p.setContent("accountState","0");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //成功后设置状态
                gridObj.modifyNamedNode_Xml(curRowIndex,"disabled","0");
                gridObj.modifyNamedNode_Xml(curRowIndex,"icon",ICON + "user.gif");

                //启用组
                var treeObj = $$("tree");
                var treeNode = treeObj.getTreeNodeById(groupID);
                if(null!=treeNode){
                    var xmlNode = new XmlNode(treeNode.node);
                    refreshGroupStates(xmlNode,"0");
                }
 
            }
            request.send();
        }
    }

 
   
 
    function delUser(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }
        var gridObj = $$("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var groupID = gridObj.getXmlDocument().getAttribute("groupId");
        if("search"==groupID){
            groupID = rowNode.getAttribute("groupId");
        }
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");

        var p = new HttpRequestParams();
        p.url = URL_DEL_USER;
        p.setContent("userId",rowID);
        p.setContent("groupId",groupID);
        p.setContent("groupType",groupType);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //从grid上删除
            gridObj.delRow_Xml(rowIndex,true,true);

            //刷新工具条
            onInactiveRow();
        }
        request.send();
    }
 
  
    /*
     *	搜索用户
     */
    function searchUser(){
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var groupType = treeNode.getAttribute("groupType");
            var applicationID = treeNode.getAttribute("applicationId");
            var cacheID = CACHE_SEARCH_USER + treeID;

            var condition = window.showModalDialog("searchuser.htm",{groupId:treeID,groupType:groupType,applicationId:applicationID,title:"搜索\""+treeName+"\"下的用户"},"dialogWidth:250px;dialogHeight:250px;");
            if(null!=condition){
                Cache.Variables.add("condition",condition);
                loadSearchGridData(cacheID,1);
            }
        }
    }
    /*
     *	根据条件获取搜索结果
     *	参数：	string:cacheID      缓存数据id
                string:page         页码
                string:sortName     排序字段
                string:direction    排序方向
     *	返回值：
     */
    function loadSearchGridData(cacheID,page,sortName,direction){
        var condition = Cache.Variables.get("condition");
        if(null!=condition){
            var p = new HttpRequestParams();
            p.url = URL_SEARCH_USER;

            var xmlReader = new XmlReader(condition.dataXml);
            var dataNode = new XmlNode(xmlReader.documentElement);
            p.setXFormContent(dataNode,condition.prefix);
            p.setContent("page",page);
            if(null!=sortName && null!=direction){
                p.setContent("field", sortName);
                p.setContent("orderType", direction);
            }

            var row = dataNode.selectSingleNode("row");
            var groupType = row.getCDATA("groupType");
            var applicationID = row.getCDATA("applicationId");

			p.setContent("applicationId", applicationID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var sourceListNode = this.getNodeValue(XML_USER_LIST);
                var sourceListNodeID = cacheID+"."+XML_USER_LIST;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                //给用户grid数据根节点增加groupType,applicationId等属性
                sourceListNode.setAttribute("groupType",groupType);
                sourceListNode.setAttribute("groupId","search");//搜索用户情况比较特殊，所以置特殊值用以区分
                sourceListNode.setAttribute("applicationId",applicationID);

                //给当前排序列加上_direction属性
                if(null!=sortName && null!=direction){
                    var column = sourceListNode.selectSingleNode("//column[@name='" + sortName + "']");
                    if(null!=column){
                        column.setAttribute("_direction",direction);
                    }
                }

                Cache.XmlDatas.add(sourceListNodeID,sourceListNode);
                Cache.XmlDatas.add(pageListNodeID,pageListNode);
                Cache.Variables.add(cacheID,[sourceListNodeID,pageListNodeID]);

                
                initSearchGrid(cacheID);
            }
            request.send();;
        }
    }
    /*
     *	初始化搜索用户grid
     *	参数：	string:cacheID      缓存数据id
     *	返回值：
     */
    function initSearchGrid(cacheID){
        var gridObj = $$("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridDataFromCache(cacheID);
            loadGridEvents();

            //刷新工具条
            onInactiveRow();
        });    
    }
    /*
     *	更新page4Tree的数据
     *	参数：	string:cacheID            缓存数据id
                string:applicationId      应用id
     *	返回值：
     */
    function updatePage4TreeData(cacheID,applicationId){
        var group2UserTreeNode = Cache.XmlDatas.get(CACHE_MAIN_TREE).cloneNode(false);
        var group2UserTreeNodeID = cacheID+"."+XML_GROUP_TO_USER_TREE;
        Cache.XmlDatas.add(group2UserTreeNodeID,group2UserTreeNode);

        //只保留同一应用系统组
        var mainTreeNode = Cache.XmlDatas.get(CACHE_MAIN_TREE);
        var applicationNode = mainTreeNode.selectSingleNode(".//treeNode[@applicationId='"+applicationId+"']");
        if(null!=applicationNode){
            applicationNode = applicationNode.cloneNode(true);
            group2UserTreeNode.appendChild(applicationNode);
        }

        //重新载入树
        var page4TreeObj = $$("page4Tree");
        Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function(){
            loadGroup2UserTreeData(cacheID);
        });

        //切换应用后，清除page4Tree2数据
        var page4Tree2Obj = $$("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page4Tree2Obj);
        });

        //切换应用后，清除page4Tree3数据
        var page4Tree3Obj = $$("page4Tree3");
        Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page4Tree3Obj);
        });
    }
 
    /*
     *	弹出模态窗口选择用户组
     *	参数：	string:id               组/用户id
                XmlNode:xmlIsland       如果有该值，树数据不从后台取
                string:title            弹出窗口标题
                string:groupType        用户组类型
                string:applicationId    用户组所在应用
                string:type             1:添加组/2:添加用户/3:查看组
     *	返回值：
     */
    function getGroup(id,xmlIsland,title,groupType,applicationId,type){
        var group = window.showModalDialog("grouptree.htm",{id:id,xmlIsland:xmlIsland,title:title,groupType:groupType,applicationId:applicationId,type:type},"dialogWidth:300px;dialogHeight:400px;");
        return group;
    }

 
 
    /*
     *	检测用户列表右键菜单项是否可见
     */
    function getUserOperation(code){
        var flag = false;
        var gridObj = $$("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var _operation = curRowNode.getAttribute("_operation");

            var reg = new RegExp("(^"+code+",)|(^"+code+"$)|(,"+code+",)|(,"+code+"$)","gi");
            if(true==reg.test(_operation)){
                flag = true;
            }
        }
        return flag;
    }
 
  
    /*
     *	综合查询(用户授权查询)
     */
    function generalSearchPermission(){
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
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
    }
 
    /*
     *	搜索用户授权信息
     */
    function searchPermission(cacheID){
        var p = new HttpRequestParams();
        p.url = URL_GENERAL_SEARCH_PERMISSION_LIST;

        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_GENERAL_SEARCH_PERMISSION);
        if(null!=xmlIsland){
            var dataNode = xmlIsland.selectSingleNode("./data");
            if(null!=dataNode){
                var prefix = xmlIsland.selectSingleNode("./declare").getAttribute("prefix");
                p.setXFormContent(dataNode,prefix);
            }
        }

        var request = new HttpRequest(p);
        request.onresult = function(){
            var permissionListNode = this.getNodeValue(XML_GENERAL_SEARCH_PERMISSION_LIST);
            var permissionListNodeID = cacheID+"."+XML_GENERAL_SEARCH_PERMISSION_LIST;

            Cache.XmlDatas.add(permissionListNodeID,permissionListNode);
            Cache.Variables.add(cacheID,[permissionListNodeID]);

            loadPermissionListData(cacheID);
        }
        request.send();
    }
    /*
     *	载入授权列表数据
     */
    function loadPermissionListData(cacheID){
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_GENERAL_SEARCH_PERMISSION_LIST);
        if(null!=xmlIsland){
            createPermissionList(xmlIsland);
        }else{
            clearPermissionList();
        }
    }
    /*
     *	创建授权列表数据
     */
    function createPermissionList(xmlIsland){
        var page7Box = $$("page7Box");
        var str = getPermissionListStr(xmlIsland);
        page7Box.innerHTML = str;
    }
    /*
     *	生成列表html
     */
    function getPermissionListStr(xmlIsland){

        var str = [];
        var userNodes = xmlIsland.selectNodes("./treeNode");

        //遍历用户
        for(var i=0,iLen=userNodes.length;i<iLen;i++){
            var name = userNodes[i].getAttribute("name");
            str[str.length] = "<table class=\"hFull\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\">";
            str[str.length] = "<tr><td colspan=\"4\" style=\"text-decoration:underline;font-weight:bold;\">" + name + "</td></tr>";


            //遍历资源类型以生成表格的行
            var sourceNodes = userNodes[i].selectNodes("./treeNode/treeNode/treeNode");
            for(var j=0,jLen=sourceNodes.length;j<jLen;j++){
                var sourceNode = sourceNodes[j];
                var typeNode = sourceNode.getParent();
                var appNode = typeNode.getParent();


                var name = sourceNode.getAttribute("name");
                str[str.length] = "<tr>";

                if(sourceNode.equals(appNode.getFirstChild().getFirstChild())){
                    var rowspan = appNode.selectNodes("./treeNode/treeNode").length;
                    str[str.length] = "<td rowspan=\"" + rowspan + "\" width=\"10%\">" + appNode.getAttribute("name") + "</td>";                
                }
                if(sourceNode.equals(typeNode.getFirstChild())){
                    var rowspan = typeNode.selectNodes("./treeNode").length;
                    str[str.length] = "<td rowspan=\"" + rowspan + "\" width=\"10%\">" + typeNode.getAttribute("name") + "</td>";
                }
                str[str.length] = "<td width=\"30%\">" + name + "</td>";




                //遍历权限选项
                var optionStr = [];
                var optionNodes = sourceNode.selectNodes("./treeNode");
                for(var k=0,kLen=optionNodes.length;k<kLen;k++){
                    optionStr[optionStr.length] = optionNodes[k].getAttribute("name");
                }
                str[str.length] = "<td width=\"50%\">" + optionStr.join(",") + "</td>";



                str[str.length] = "</tr>";
            }


            str[str.length] = "</table>";
        }

        return str.join("");
    }
    /*
     *	清除授权列表数据
     */
    function clearPermissionList(){
        var page7Box = $$("page7Box");
        page7Box.innerHTML = "";
    }


    /*
     *	综合查询(用户转授查询)
     */
    function generalSearchReassign(){
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var groupId = treeNode.getId();
            var groupName = treeNode.getName();
            
            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadGeneralSearchRessignData(groupId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page6";
            inf.label = "用户转授" + OPERATION_SEARCH.replace(/\$label/i,groupName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_GENERAL_SEARCH_REASSIGN + groupId;
            var tab = ws.open(inf);
        }
    }
    /*
     *	综合查询加载数据
     */
    function loadGeneralSearchRessignData(groupId){
        var cacheID = CACHE_GENERAL_SEARCH_REASSIGN + groupId;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_GENERAL_SEARCH_REASSIGN;

            p.setContent("groupId", groupId);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var generalSearchGridNode = this.getNodeValue(XML_GENERAL_SEARCH_REASSIGN);

                var generalSearchGridNodeID = cacheID+"."+XML_GENERAL_SEARCH_REASSIGN;

                Cache.XmlDatas.add(generalSearchGridNodeID,generalSearchGridNode);

                Cache.Variables.add(cacheID,[generalSearchGridNodeID]);

                initGeneralSearchReassignPages(cacheID);
            }
            request.send();
        }else{
            initGeneralSearchReassignPages(cacheID);
        }
    }
    /*
     *	综合查询相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initGeneralSearchReassignPages(cacheID){
        var page6GridObj = $$("page6Grid");
        Public.initHTC(page6GridObj,"isLoaded","onload",function(){
            loadGeneralSearchReassignGridData(cacheID);
        });
    }
    /*
     *	grid加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGeneralSearchReassignGridData(cacheID){
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_GENERAL_SEARCH_REASSIGN);
        if(null!=xmlIsland){
            var page6GridObj = $$("page6Grid");
            page6GridObj.load(xmlIsland.node,null,"node");
        }
    }


    /*
     *	综合查询(用户角色查询)
     */
    function generalSearchRole(){
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var groupId = treeNode.getId();
            var groupName = treeNode.getName();
            
            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadGeneralSearchRoleData(groupId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page6";
            inf.label = "用户角色" + OPERATION_SEARCH.replace(/\$label/i,groupName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_GENERAL_SEARCH_ROLE + groupId;
            var tab = ws.open(inf);
        }
    }
 
    function loadGeneralSearchRoleData(groupId){
        var cacheID = CACHE_GENERAL_SEARCH_ROLE + groupId;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_GENERAL_SEARCH_ROLE;

            p.setContent("groupId", groupId);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var generalSearchGridNode = this.getNodeValue(XML_GENERAL_SEARCH_ROLE);

                var generalSearchGridNodeID = cacheID+"."+XML_GENERAL_SEARCH_ROLE;

                Cache.XmlDatas.add(generalSearchGridNodeID,generalSearchGridNode);

                Cache.Variables.add(cacheID,[generalSearchGridNodeID]);

                initGeneralSearchRolePages(cacheID);
            }
            request.send();
        }else{
            initGeneralSearchRolePages(cacheID);
        }
    }
    /*
     *	综合查询相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initGeneralSearchRolePages(cacheID){
        var page6GridObj = $$("page6Grid");
        Public.initHTC(page6GridObj,"isLoaded","onload",function(){
            loadGeneralSearchRoleGridData(cacheID);
        });
    }
    /*
     *	grid加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGeneralSearchRoleGridData(cacheID){
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_GENERAL_SEARCH_ROLE);
        if(null!=xmlIsland){
            var page6GridObj = $$("page6Grid");
            page6GridObj.load(xmlIsland.node,null,"node");
        }
    }
 
    function getUserGroupType(){
        var gridObj = $$("grid");
        var groupType = gridObj.getXmlDocument().getAttribute("groupType");
        return groupType;
    }



    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();