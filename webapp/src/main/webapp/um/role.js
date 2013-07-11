	/*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "RoleGroupTree";
    XML_ROLE_INFO = "RoleInfo";
    XML_ROLE_TO_GROUP_TREE = "Role2GroupTree";
    XML_ROLE_TO_GROUP_EXIST_TREE = "Role2GroupExistTree";
    XML_ROLE_TO_USER_TREE = "Role2UserTree";
    XML_ROLE_TO_USER_EXIST_TREE = "Role2UserExistTree";
    XML_ROLE_TO_PERMISSION = "Role2Permission";
    XML_ROLE_LIST = "RoleList";
    XML_ROLE_GROUP_INFO = "RoleGroupInfo";
    XML_GROUP_TO_USER_LIST_TREE = "Group2UserListTree";
    XML_ROLE_TO_GROUP_IDS = "role2GroupIds";
    XML_ROLE_TO_USER_IDS = "role2UserIds";
	
    /*
     *	默认唯一编号名前缀
     */
    CACHE_ROLE_GROUP_DETAIL = "roleGroup__id";
    CACHE_VIEW_ROLE_GROUP_DETAIL = "viewRoleGroup__id";
    CACHE_ROLE_DETAIL = "role__id";
    CACHE_VIEW_ROLE_DETAIL = "viewRole__id";
	
	ICON = "../framework/images/";
 
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_SOURCE_TREE = "ums/role!getAllRole2Tree.action";
    URL_ROLE_DETAIL = "ums/role!getRoleInfoAndRelation.action";
    URL_SAVE_ROLE = "ums/role!saveRole.action";
    URL_ROLE_GROUP_DETAIL = "ums/role!getRoleGroupInfo.action";
    URL_SAVE_ROLE_GROUP = "ums/role!saveRoleGroupInfo.action";
    URL_STOP_NODE = "ums/role!disable.action";
    URL_DELETE_NODE = "ums/role!delete.action";
	URL_MOVE_NODE = "ums/role!move.action";
    URL_GROUP_TO_USER_LIST = "ums/role!getUserByGroupId.action";    
    URL_GET_OPERATION = "ums/role!getOperation.action";
	
	if(IS_TEST) {
	    URL_SOURCE_TREE = "data/role_tree.xml?";
		URL_ROLE_DETAIL = "data/role_detail.xml?";
		URL_SAVE_ROLE = "data/_success.xml?";
		URL_ROLE_GROUP_DETAIL = "data/rolegroup_detail.xml?";
		URL_SAVE_ROLE_GROUP = "data/_success.xml?";
		URL_STOP_NODE = "data/_success.xml?";
		URL_DELETE_NODE = "data/_success.xml?";
		URL_GROUP_TO_USER_LIST = "data/group2userlist.xml?";
		URL_MOVE_NODE = "data/_success.xml?";
		URL_GET_OPERATION = "data/operation.xml?";
	}
 
    function init() {
        initPaletteResize();
        initNaviBar("um.3");
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }

    function initMenus() {
        var item1 = {
            label:"新建角色组",
            callback:addNewRoleGroup,           
            visible:function() {return (isRoleGroup() || isRootNode()) && getOperation("2");}
        }
        var item2 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            visible:function() {return !isRootNode() && getOperation("2");}
        }
		var item12 = {
            label:"查看",
            callback:function() { editTreeNode(false); },
            icon:ICON + "view.gif",           
            visible:function() {return isRole() && getOperation("1");}
        }
        var item3 = {
            label:"编辑",
            callback:function() { editTreeNode(true); },
            icon:ICON + "edit.gif",           
            visible:function() {return !isRootNode() && getOperation("2");}
        }
        var item7 = {
            label:"停用",
            callback:function() { stopOrStartTreeNode("1"); },
            icon:ICON + "stop.gif",           
            visible:function() {return !isRootNode() && !isTreeNodeDisabled() && getOperation("2");}
        }
        var item8 = {
            label:"启用",
            callback:function() { stopOrStartTreeNode("0"); },
            icon:ICON + "start.gif",           
            visible:function() {return !isRootNode() && isTreeNodeDisabled() && getOperation("2");}
        }
        var item9 = {
            label:"新建角色",
            callback:addNewRole,           
            visible:function() {return (isRoleGroup() || isRootNode()) && getOperation("2");}
        }
        var item10 = {
            label:"给角色授权",
            icon:ICON + "role_permission.gif",
            callback:setRolePermission,           
            visible:function() {return isRole() && getOperation("2");}
        }
        var item11 = {
            label:"移动到...",
            callback:moveNodeTo,
            icon:ICON + "move.gif",            
            visible:function() {return !isRootNode() && getOperation("2");}
        }
        var item14 = {
            label:"授予角色",
            callback:setRole2Permission,            
            visible:function() {return !isRootNode() && getOperation("2");}
        }

        var menu1 = new Menu();
		menu1.addItem(item1);
        menu1.addItem(item9);
		menu1.addSeparator();
		menu1.addItem(item12);
        menu1.addItem(item3);
        menu1.addItem(item2);  
        menu1.addItem(item7);
        menu1.addItem(item8);
		menu1.addItem(item11);
        menu1.addSeparator();    
		menu1.addItem(item10); 
        menu1.addItem(item14);

        $$("tree").contextmenu = menu1;
    }

	function loadInitData() {
        var p = new HttpRequestParams();
        p.url = URL_SOURCE_TREE;

        var request = new HttpRequest(p);
        request.onresult = function() {
            var roleTreeNode = this.getNodeValue(XML_MAIN_TREE);
			$T("tree", roleTreeNode);
			
			var treeObj = $$("tree");

            treeObj.onTreeNodeActived = function(eventObj) {
				var treeTitleObj = $$("treeTitle");
				Focus.focus(treeTitleObj.firstChild.id);

				showTreeNodeInfo();
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj) {
				var treeNode = eventObj.treeNode;
				getTreeOperation(treeNode, function(_operation) {            
					if( !isRootNode() ) {
						var canEdit = checkOperation("4", _operation);
						editTreeNode(canEdit);
					}
				});
            }
            treeObj.onTreeNodeRightClick = function(eventObj) {
				var treeObj = $$("tree");
				var treeNode = eventObj.treeNode;

				showTreeNodeInfo();

				getTreeOperation(treeNode, function(_operation) {
					if(treeObj.contextmenu) {
						treeObj.contextmenu.show(eventObj.clientX, eventObj.clientY);                
					}
				});
            }
        }
        request.send();
    }
	
	/* 是否根节点 */
    function isRootNode(id) {
        id = id || getTreeNodeId();
        return ("-6" == id);
    }
	
	/* 获取节点类型(1角色组/0角色) */
	function isRole() {
		return getTreeAttribute("isGroup") == "0";
	}
	function isRoleGroup() {
		return getTreeAttribute("isGroup") == "1";
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
					while( curNode && "-6" != curNode.getAttribute("id")) {
						refreshTreeNodeState(curNode, state);
						curNode = curNode.getParent();
					}            
				}
				
				tree.reload(); 
			}
		});
    }
 
    function refreshTreeNodeState(xmlNode, state) {
        var isGroup = xmlNode.getAttribute("isGroup");
        var img = {
            "1": "role_group",
            "0": "role"
        };
        xmlNode.setAttribute("icon", ICON + img[isGroup] + (state=="0" ? "" : "_2") + ".gif");
		xmlNode.setAttribute("disabled", state);
    }	
	
    /*
     *	删除节点
     */
    function delTreeNode() {
        if( !confirm("您确定要删除吗？") )  return;
 
        var tree = $T("tree");
		var treeNode = tree.getActiveTreeNode();
		Ajax({
			url : URL_DELETE_NODE + treeNode.getId(),
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
	
	function moveNodeTo() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeNodeID = treeNode.getId();
		var targetId = window.showModalDialog("rolegrouptree.htm", {id:treeNodeID}, "dialogWidth:300px;dialogHeight:400px;");
		if( targetId) {
			Ajax({
				url : URL_MOVE_NODE + treeNodeID + "/" + targetId,
				onsuccess : function() {  // 移动树节点					
					var xmlNode = new XmlNode(treeNode.node);
					var parentNode = treeObj.getTreeNodeById(targetId);

					// 父节点停用则下溯
					var parentNodeState = parentNode.getAttribute("disabled");
					if("1" == parentNodeState) {
						refreshTreeNodeState(xmlNode, "1");
					}
					parentNode.node.appendChild(treeNode.node);
					parentNode.node.setAttribute("_open", "true");

					clearOperation(xmlNode);

					treeObj.reload();
				}
			});
		}
    }

    /*
     *	获取树操作权限
     *	参数：	treeNode:treeNode       treeNode实例
                function:callback       回调函数
     */
    function getTreeOperation(treeNode, callback) {
        var _operation = treeNode.getAttribute("_operation");
        if( isNullOrEmpty(_operation) ) { // 如果节点上还没有_operation属性，则发请求从后台获取信息
			Ajax({
				url : URL_GET_OPERATION + treeNode.getId(),
				onresult : function() {
					_operation = this.getNodeValue(XML_OPERATION);
					treeNode.setAttribute("_operation", _operation);

					if( callback ) {
						callback(_operation);
					}
				}
			});			
        } else {
            if( callback ) {
                callback(_operation);
            }
        }    
    }
  
    function editTreeNode(editable) {
        if( isRoleGroup() ) {
            editRoleGroupInfo(editable);
        } else {
            editRoleInfo(editable);
        }
    }

	function addNewRoleGroup() {
        var treeNode = $T("tree").getActiveTreeNode();
		var parentID = treeNode.getId();
		var treeName = "角色组";
		var treeID = DEFAULT_NEW_ID;

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadRoleGroupDetailData(treeID, true, parentID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, treeName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_ROLE_GROUP_DETAIL + treeID;
		var tab = ws.open(inf);
    }
	
    /*
     *	编辑角色组信息
     */
    function editRoleGroupInfo(editable) {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();
		var parentID = treeNode.getParent().getId();

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadRoleGroupDetailData(treeID, editable, parentID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		if( editable ) {
			inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
			inf.SID = CACHE_ROLE_GROUP_DETAIL + treeID;
		}else{
			inf.label = OPERATION_VIEW.replace(/\$label/i, treeName);
			inf.SID = CACHE_VIEW_ROLE_GROUP_DETAIL + treeID;
		}
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
	
    /*
     *	树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentID             父节点id
     */
    function loadRoleGroupDetailData(treeID, editable, parentID) {
		var p = new HttpRequestParams();
		p.url = URL_ROLE_GROUP_DETAIL + treeID + "/" + parentID;

		var request = new HttpRequest(p);
		request.onresult = function() {
			var roleGroupInfoNode = this.getNodeValue(XML_ROLE_GROUP_INFO);

			var roleGroupInfoNodeID = treeID + "." + XML_ROLE_GROUP_INFO;
			Cache.XmlDatas.add(roleGroupInfoNodeID, roleGroupInfoNode);
			Cache.Variables.add(treeID, [roleGroupInfoNodeID]);

			var xform = $X("page1Form", roleGroupInfoNode);
			xform.editable = editable == false ? "false" : "true";
			
			// 设置翻页按钮显示状态
			$$("page1BtPrev").style.display = "none";
			$$("page1BtNext").style.display = "none";

			//设置保存按钮操作
			var page1BtSaveObj = $$("page1BtSave");
			var page2BtSaveObj = $$("page2BtSave");
			page1BtSaveObj.disabled = page2BtSaveObj.disabled = (editable==false ? true : false)
			page1BtSaveObj.onclick = page2BtSaveObj.onclick = function() {
				saveRoleGroup(treeID, parentID);
			}
		}
		request.send();
    }

    function saveRoleGroup(cacheID, parentID) {
		var xform = $X("page1Form");	
		if( !xform.checkForm() ) return;
	
        var flag = false;

        var p = new HttpRequestParams();
        p.url = URL_SAVE_ROLE_GROUP;
 
		// 角色组基本信息
		var roleGroupInfoNode = Cache.XmlDatas.get(cacheID + "." + XML_ROLE_GROUP_INFO);
		if( roleGroupInfoNode ) {
			var roleGroupInfoDataNode = roleGroupInfoNode.selectSingleNode(".//data");
			if( roleGroupInfoDataNode ) {
				flag = true;
				p.setXFormContent(roleGroupInfoDataNode);
			}
		}        
 
        if( flag ) {
            var request = new HttpRequest(p);
            // 同步按钮状态
            syncButton([$$("page1BtSave"), $$("page2BtSave")], request);

            request.onresult = function() {
				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentID,treeNode);
 
				ws.closeActiveTab();
            }
            request.onsuccess = function() {
				// 更新树节点名称
				var name = xform.getData("name");
				modifyTreeNode(cacheID, "name", name, true);
            }
            request.send();
        }
    }
	
	
	var phases = [];
	phases[0] = {page:"page1", label:"基本信息"};
	phases[1] = {page:"page4", label:"用户"};
	phases[2] = {page:"page2", label:"用户组"};
	
	function addNewRole() {
        var treeNode = $T("tree").getActiveTreeNode();
		var parentID = treeNode.getId();
		var treeName = "角色";
		var treeID = DEFAULT_NEW_ID;

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadRoleDetailData(treeID, true, parentID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, treeName);
		inf.phases = phases;
		inf.callback = callback;
		inf.SID = CACHE_ROLE_DETAIL + treeID;
		var tab = ws.open(inf);
    }

    function editRoleInfo(editable) {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();
		var parentID = treeNode.getParent().getId();
 
		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadRoleDetailData(treeID, editable, treeID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		if( editable ) {
			inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
			inf.SID = CACHE_ROLE_DETAIL + treeID;
		}else{
			inf.label = OPERATION_VIEW.replace(/\$label/i, treeName);
			inf.SID = CACHE_VIEW_ROLE_DETAIL + treeID;
		}
		inf.defaultPage = "page1";
		inf.phases = phases;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
	
    /*
     *	树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentID             父节点id
     */
    function loadRoleDetailData(treeID, editable, parentID) {
		var p = new HttpRequestParams();
		p.url = URL_ROLE_DETAIL + treeID + "/" + parentID;

		var request = new HttpRequest(p);
		request.onresult = function() {
			var roleInfoNode = this.getNodeValue(XML_ROLE_INFO);
			var role2UserTreeNode = this.getNodeValue(XML_ROLE_TO_GROUP_TREE);
			var role2UserExsitInfo = this.getNodeValue(XML_ROLE_TO_USER_EXIST_TREE);
			var role2GroupTreeNode = this.getNodeValue(XML_ROLE_TO_GROUP_TREE);
			var role2GroupExsitInfo = this.getNodeValue(XML_ROLE_TO_GROUP_EXIST_TREE);

			var mainGroupNode = role2GroupTreeNode.selectSingleNode("//treeNode[id='-2']");
			if(mainGroupNode) {
				mainGroupNode.setAttribute("canselected", "0");
			}
			var assistantGroupNode = role2GroupTreeNode.selectSingleNode("//treeNode[id='-3']");
			if(assistantGroupNode) {
				assistantGroupNode.setAttribute("canselected", "0");
			}

			var roleInfoNodeID  = treeID + "." + XML_ROLE_INFO;
			Cache.XmlDatas.add(roleInfoNodeID, roleInfoNode);
			Cache.XmlDatas.add(treeID + "." + XML_ROLE_TO_USER_TREE, role2UserTreeNode);
			Cache.XmlDatas.add(treeID + "." + XML_ROLE_TO_USER_EXIST_TREE, role2UserExsitInfo);
			Cache.XmlDatas.add(treeID + "." + XML_ROLE_TO_GROUP_TREE, role2GroupTreeNode);
			Cache.XmlDatas.add(treeID + "." + XML_ROLE_TO_GROUP_EXIST_TREE, role2GroupExsitInfo);

			var page1FormObj = $X("page1Form", roleInfoNode);
            page1FormObj.editable = editable ? "true" : "false";

			attachReminder(roleInfoNodeID, page1FormObj);

			$T("page4Tree",  role2UserTreeNode);
			$T("page4Tree3", role2UserExsitInfo);
			$T("page2Tree",  role2GroupTreeNode);
			$T("page2Tree2", role2GroupExsitInfo);
			
			$$("page4Tree").onTreeNodeDoubleClick = function(eventObj){
                onPage4TreeNodeDoubleClick(eventObj);
            }
			
			 //设置翻页按钮显示状态
			$$("page1BtPrev").style.display = "none";
			$$("page4BtPrev").style.display = "";
			$$("page2BtPrev").style.display = "";
			$$("page1BtNext").style.display = "";
			$$("page4BtNext").style.display = "";
			$$("page2BtNext").style.display = "none";

			var disabled = editable==false;
			
			// 设置添加按钮操作
			var page2BtAddObj = $$("page2BtAdd");
			page2BtAddObj.disabled = disabled;
			page2BtAddObj.onclick = function() {
				addPage2TreeNode();
			}

			// 设置删除按钮操作
			var page2BtDelObj = $$("page2BtDel");
			page2BtDelObj.disabled = disabled;
			page2BtDelObj.onclick = function() {
				removeTreeNode($T("page2Tree2")); // 删除page2里tree节点
			}

			// 设置添加按钮操作
			var page4BtAddObj = $$("page4BtAdd");
			page4BtAddObj.disabled = disabled;
			page4BtAddObj.onclick = function() {
				 addPage4TreeNode();
			}

			// 设置删除按钮操作
			var page4BtDelObj = $$("page4BtDel");
			page4BtDelObj.disabled = disabled;
			page4BtDelObj.onclick = function() {
				 removeTreeNode($T("page4Tree3")); // 删除page4里tree节点
			}

			// 设置保存按钮操作
			var page1BtSaveObj = $$("page1BtSave");
			var page2BtSaveObj = $$("page2BtSave");
			var page4BtSaveObj = $$("page4BtSave");
			page1BtSaveObj.disabled = page2BtSaveObj.disabled = page4BtSaveObj.disabled = disabled;
			page1BtSaveObj.onclick = page2BtSaveObj.onclick = page4BtSaveObj.onclick = function() {
				saveRole(treeID, parentID);
			}
		}
		request.send();
    }
 
    /* 点击页4用户组树节点 */
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
 
    function saveRole(cacheID, parentID) {
        //校验page1Form数据有效性
        var page1FormObj = $X("page1Form");
        if( !page1FormObj.checkForm() ) {
            switchToPhase(ws, "page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_ROLE;
 
		//角色基本信息
		var roleInfoNode = Cache.XmlDatas.get(cacheID + "." + XML_ROLE_INFO);
		if(roleInfoNode) {
			var roleInfoDataNode = roleInfoNode.selectSingleNode(".//data");
			if(roleInfoDataNode) {
				flag = true;
				p.setXFormContent(roleInfoDataNode);
			}
		}

		//角色对用户
		var role2UserNode = Cache.XmlDatas.get(cacheID + "." + XML_ROLE_TO_USER_EXIST_TREE);
		if(role2UserNode) {
			var role2UserDataIDs = getTreeNodeIds(role2UserNode, "./treeNode//treeNode");
			if(role2UserDataIDs.length > 0) {
				flag = true;
				p.setContent(XML_ROLE_TO_USER_IDS, role2UserDataIDs.join(","));
			}
		}

		//角色对用户组
		var role2GroupNode = Cache.XmlDatas.get(cacheID + "." + XML_ROLE_TO_GROUP_EXIST_TREE);
		if(role2GroupNode) {
			var role2GroupDataIDs = getTreeNodeIds(role2GroupNode, "./treeNode//treeNode");
			if(role2GroupDataIDs.length > 0) {
				flag = true;
				p.setContent(XML_ROLE_TO_GROUP_IDS, role2GroupDataIDs.join(","));
			}
		}        

        if(flag) {
            var request = new HttpRequest(p);
			
            // 同步按钮状态
            syncButton([$$("page1BtSave"), $$("page2BtSave"), $$("page4BtSave")], request);

            request.onresult = function() {                   
				detachReminder(cacheID); //解除提醒

				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentID, treeNode);
				ws.closeActiveTab();
            }
            request.onsuccess = function() {                  
				detachReminder(cacheID);  //解除提醒
				
				var name = page1FormObj.getData("name");
				modifyTreeNode(cacheID, "name", name, true); //更新树节点名称
            }
            request.send();
        }
    }		
    
    /* 添加page2里tree节点 */
    function addPage2TreeNode() {
        var page2Tree2Obj = $T("page2Tree2");
        var page2TreeObj  = $T("page2Tree");
        var selectedNodes = page2TreeObj.getSelectedTreeNode(false);

        var reload = false;
        for(var i=0; i < selectedNodes.length; i++) {
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0, true, true);

            var id = curNode.getId();
            var sameAttributeTreeNode = hasSameAttributeTreeNode(page2Tree2Obj, "id", id);
            if( sameAttributeTreeNode == false ) {
                reload = true; // 至少有一行添加才刷新Tree

                var treeNode = page2Tree2Obj.getTreeNodeById("_rootId");
                if( treeNode ) {                   
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false); // 排除子节点
                    page2Tree2Obj.insertTreeNodeXml(cloneNode.toXml(), treeNode);
                }
            }
        }
        if( reload ) {
            page2Tree2Obj.reload();
        }
        page2TreeObj.reload();
    }
 
    /* 添加page4里tree节点 */
    function addPage4TreeNode() {
        var page4Tree2Obj = $T("page4Tree2");
        var page4Tree3Obj = $T("page4Tree3");
        var selectedNodes = page4Tree2Obj.getSelectedTreeNode();

        var reload = false;
        for(var i=0; i < selectedNodes.length; i++) {
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0, true, true);
 
            var id = curNode.getId();
            var sameAttributeTreeNode = hasSameAttributeTreeNode(page4Tree3Obj, "id", id);
            if("_rootId" != id && sameAttributeTreeNode == false) {          
                reload = true; // 至少有一行添加才刷新grid

                var treeNode = page4Tree3Obj.getTreeNodeById("_rootId");
                if(treeNode) {
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false); // 排除子节点
                    page4Tree3Obj.insertTreeNodeXml(cloneNode.toXml(), treeNode);
                }
            }
        }
        if( reload ) {
            page4Tree3Obj.reload();
        }
        page4Tree2Obj.reload();
    }

	
    /* 角色权限设置 */
    function setRolePermission() {
        var treeNode = $T("tree").getActiveTreeNode();		
		var title = "设置\"" + treeNode.getName() + "\"权限";
		var params = {
			roleId: treeNode.getId(),
			isRole2Resource: "1"
		};
		window.showModalDialog("setpermission.htm", {params:params, title:title, type:"role"},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
    }
    
    /* 授予角色 */
    function setRole2Permission() {
        var treeNode = $T("tree").getActiveTreeNode();
		var title = "授予\"" + treeNode.getName() + "\"角色";
		var params = {
			roleId: treeNode.getId(),
			resourceType: "2",
			applicationId: "tss",
			isRole2Resource: "0"
		};
		window.showModalDialog("setpermission.htm", {params:params, title:title, type:"role"},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
    }

    window.onload = init;

    //关闭页面自动注销
    logoutOnClose();