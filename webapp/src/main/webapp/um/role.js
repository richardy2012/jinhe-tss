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
    XML_SEARCH_PERMISSION = "SearchPermission";
    XML_RESOURCE_TYPE = "ResourceType";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_ROLE_PERMISSION = "rolePermission__id";
    CACHE_ROLE_GROUP_DETAIL = "roleGroup__id";
    CACHE_VIEW_ROLE_GROUP_DETAIL = "viewRoleGroup__id";
    CACHE_ROLE_DETAIL = "role__id";
    CACHE_VIEW_ROLE_DETAIL = "viewRole__id";
    CACHE_ROLE_TO_USER_GRID = "role2User__id";
    CACHE_SEARCH_PERMISSION = "searchPermission__id";
 
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_SOURCE_TREE = "data/role_init.xml";
    URL_SOURCE_DETAIL = "data/role1.xml";
    URL_SAVE_ROLE = "data/_success.xml";
    URL_ROLE_LIST = "data/rolelist.xml";
    URL_ROLE_GROUP_DETAIL = "data/rolegroup1.xml";
    URL_SAVE_ROLE_GROUP = "data/_success.xml";
    URL_STOP_ROLE_GROUP = "data/_success.xml";
    URL_START_ROLE_GROUP = "data/_success.xml";
    URL_STOP_ROLE = "data/_success.xml";
    URL_START_ROLE = "data/_success.xml";
    URL_DEL_ROLE_GROUP = "data/_success.xml";
    URL_DEL_ROLE = "data/_success.xml";
    URL_GROUP_TO_USER_LIST = "data/role2userlist.xml";
    URL_SORT_NODE = "data/_success.xml";
    URL_MOVE_NODE = "data/_success.xml";
    URL_GET_RESOURCE_TYPE = "data/resourcetype.xml";
    URL_GET_OPERATION = "data/operation.xml";

    URL_SOURCE_TREE = "ums/role!getAllRole2Tree.action";
    URL_SOURCE_DETAIL = "ums/role!getRoleInfoAndRelation.action";
    URL_SAVE_ROLE = "ums/role!saveRole.action";
    URL_ROLE_LIST = "data/rolelist.xml";
    URL_ROLE_GROUP_DETAIL = "ums/role!getRoleGroupInfo.action";
    URL_SAVE_ROLE_GROUP = "ums/role!saveRoleGroupInfo.action";
    URL_STOP_ROLE_GROUP = "ums/role!disable.action";
    URL_START_ROLE_GROUP = "ums/role!disable.action";
    URL_STOP_ROLE = "ums/role!disable.action";
    URL_START_ROLE = "ums/role!disable.action";
    URL_DEL_ROLE_GROUP = "ums/role!delete.action";
    URL_DEL_ROLE = "ums/role!delete.action";
    URL_GROUP_TO_USER_LIST = "ums/role!getUserByGroupId.action";
    URL_SORT_NODE = "ums/role!sort.action";
    URL_MOVE_NODE = "ums/role!move.action";
    URL_GET_RESOURCE_TYPE = "ums/role!getResourceTypes.action";
    URL_GET_OPERATION = "ums/role!getOperation.action";
 
    /*
     *	icon路径
     */
    ICON = "../framework/images/";
 

    /*
     *	页面初始化
     */
    function init() {
        initPaletteResize();
        initToolBar();
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
            callback:stopTreeNode,
            icon:ICON + "stop.gif",           
            visible:function() {return !isRootNode() && "0"==getTreeNodeState() && getOperation("2");}
        }
        var item8 = {
            label:"启用",
            callback:startTreeNode,
            icon:ICON + "start.gif",           
            visible:function() {return !isRootNode() && "1"==getTreeNodeState() && getOperation("2");}
        }
        var item9 = {
            label:"新建角色",
            callback:addNewRole,           
            visible:function() {return (isRoleGroup() || isRootNode()) && getOperation("2");}
        }
        var item10 = {
            label:"角色权限设置",
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
        menu1.addItem(item7);
        menu1.addItem(item8);
        menu1.addSeparator();
        menu1.addItem(item14);
        menu1.addSeparator();
        menu1.addItem(item12);
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item10);
        menu1.addItem(item11);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item9);

        var treeObj = $$("tree");
        treeObj.contextmenu = menu1;
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
                onTreeNodeActived(eventObj);
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj) {
                onTreeNodeDoubleClick(eventObj);
            }
            treeObj.onTreeNodeRightClick = function(eventObj) {
                onTreeNodeRightClick(eventObj);
            }
        }
        request.send();
    }
 
    function stopOrStartTreeNode(state) {		
		var tree = $T("tree");
		var treeNode = tree.getActiveTreeNode();
		Ajax({
			url : URL_STOP_ROLE_GROUP + treeNode.getId() + "/" + state,
			onsuccess : function() { 
				var xmlNode = new XmlNode(treeNode.node);
				refreshTreeNodeStates(xmlNode, state);
			}
		});
    }
	
	/*
     *	刷新父子树节点停用启用状态: 启用上溯，停用下溯
     */
    function refreshTreeNodeStates(curNode, state) {
        refreshTreeNodeState(curNode, state);
		
        if("1"==state) {
            var childNodes = curNode.selectNodes(".//treeNode");
            for(var i=0; i < childNodes.length; i++) {                
                refreshTreeNodeState(childNodes[i], state);
            }
        } else if ("0" == state) {
            while( curNode && "actionSet" != curNode.node.nodeName) {
                refreshTreeNodeState(curNode, state);
                curNode = curNode.getParent();
            }            
        }
    }
 
    function refreshTreeNodeState(xmlNode, state) {
        var isGroup = xmlNode.getAttribute("isGroup");
        var img = {
            "1":"role_group",
            "0":"role"
        };
        xmlNode.setAttribute("icon", ICON + img[isGroup] + (state=="0" ? "" : "_2") + ".gif");
		xmlNode.setAttribute("disabled", state);
    }	
	
    /*
     *	删除节点
     */
    function delTreeNode() {
        if( !confirm("您确定要删除吗？") ) {
            return;
        }
 
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeNodeID = treeNode.getId();

		var p = new HttpRequestParams();
		p.url = URL_DEL_ROLE_GROUP;
		p.setContent("roleId", treeNodeID);

		var request = new HttpRequest(p);
		request.onsuccess = function() {
			var parentNode = treeNode.getParent();
			if( parentNode ) {
				treeObj.setActiveTreeNode(parentNode.getId());
			}
			treeObj.removeTreeNode(treeNode); // 从树上删除
		}
		request.send();
    }
	
	/*
     *	编辑节点
     */
    function editTreeNode(editable) {
        if( isRoleGroup() ) {
            editRoleGroupInfo(editable);
        } else {
            editRoleInfo(editable);
        }
    }

	function addNewRoleGroup() {
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
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
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
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
		p.url = URL_ROLE_GROUP_DETAIL + treeId + "/" + parentID;

		var request = new HttpRequest(p);
		request.onresult = function() {
			var roleGroupInfoNode = this.getNodeValue(XML_ROLE_GROUP_INFO);

			var roleGroupInfoNodeID = treeID + "." + XML_ROLE_GROUP_INFO;
			Cache.XmlDatas.add(roleGroupInfoNodeID, roleGroupInfoNode);
			Cache.Variables.add(treeID, [roleGroupInfoNodeID]);

			var xform = $X("page1Form", roleGroupInfoNode);
			xform.editable = editable == false? "false" : "true";
			
			// 设置翻页按钮显示状态
			$("page1BtPrev").style.display = "none";
			$("page1BtNext").style.display = "none";

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
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
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
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
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
    function loadRoleDetailData(treeID,editable,parentID,isNew,disabled) {
        if(false==editable) {
            var cacheID = CACHE_VIEW_ROLE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_ROLE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail) {
            var treeObj = $$("tree");
            var treeNode = treeObj.getActiveTreeNode();
            if(null!=treeNode) {
                var parentRoleId;
                var isGroup = treeNode.getAttribute("isGroup");
                if(isGroup != null) {
                    if(isGroup=="1") {
                        parentRoleId = treeNode.getId();
                    }else{
                        parentRoleId = treeNode.getAttribute("parentRoleId");
                    }
                }else{
                        parentRoleId = 0;
                }

                var p = new HttpRequestParams();
                p.url = URL_SOURCE_DETAIL;
                //如果是新增
                if(true==isNew) {
                    p.setContent("isNew", "1");
                    p.setContent("parentRoleId", parentRoleId);    
                    p.setContent("disabled", disabled);    
                }else{
                    p.setContent("roleId", treeID);            
                }

                var request = new HttpRequest(p);
                request.onresult = function() {
                    var roleInfoNode = this.getNodeValue(XML_ROLE_INFO);
                    var role2UserTreeNode = this.getNodeValue(XML_ROLE_TO_GROUP_TREE);
                    var role2UserGridNode = this.getNodeValue(XML_ROLE_TO_USER_EXIST_TREE);
                    var role2GroupTreeNode = this.getNodeValue(XML_ROLE_TO_GROUP_TREE);
                    var role2GroupGridNode = this.getNodeValue(XML_ROLE_TO_GROUP_EXIST_TREE);

                    var GroupType1Node = role2GroupTreeNode.selectSingleNode("//treeNode[@groupType='1']");
                    if(null!=GroupType1Node) {
                        GroupType1Node.setAttribute("canselected","0");
                    }
                    var GroupType2Node = role2GroupTreeNode.selectSingleNode("//treeNode[@groupType='2']");
                    if(null!=GroupType2Node) {
                        GroupType2Node.setAttribute("canselected","0");
                    }

                    var roleInfoNodeID = cacheID+"."+XML_ROLE_INFO;
                    var role2UserTreeNodeID = cacheID+"."+XML_ROLE_TO_USER_TREE;
                    var role2UserGridNodeID = cacheID+"."+XML_ROLE_TO_USER_EXIST_TREE;
                    var role2GroupTreeNodeID = cacheID+"."+XML_ROLE_TO_GROUP_TREE;
                    var role2GroupGridNodeID = cacheID+"."+XML_ROLE_TO_GROUP_EXIST_TREE;

                    Cache.XmlDatas.add(roleInfoNodeID,roleInfoNode);
                    Cache.XmlDatas.add(role2UserTreeNodeID,role2UserTreeNode);
                    Cache.XmlDatas.add(role2UserGridNodeID,role2UserGridNode);
                    Cache.XmlDatas.add(role2GroupTreeNodeID,role2GroupTreeNode);
                    Cache.XmlDatas.add(role2GroupGridNodeID,role2GroupGridNode);

                    Cache.Variables.add(cacheID,[roleInfoNodeID,role2UserTreeNodeID,role2UserGridNodeID,role2GroupTreeNodeID,role2GroupGridNodeID]);

                    initRolePages(cacheID,editable,parentID,isNew);
                }
                request.send();
            }
        }else{
            initRolePages(cacheID,editable,parentID,isNew);
        }
    }

    function initRolePages(cacheID,editable,parentID,isNew) {
        var page1FormObj = $$("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function() {
            loadRoleInfoFormData(cacheID,editable);// 角色信息
        });

        var page2TreeObj = $$("page2Tree");
        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function() {
            loadRole2GroupTreeData(cacheID);// 角色对用户组
        });

        var page2Tree2Obj = $$("page2Tree2");
        Public.initHTC(page2Tree2Obj,"isLoaded","oncomponentready",function() {
            loadRole2GroupExistTreeData(cacheID);
        });

        var page4TreeObj = $$("page4Tree");
        Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function() {
            loadRole2UserTreeData(cacheID);// 角色对用户
        });

        var page4Tree2Obj = $$("page4Tree2");

        var page4Tree3Obj = $$("page4Tree3");
        Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function() {
            loadRole2UserExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $$("page1BtPrev");
        var page2BtPrevObj = $$("page2BtPrev");
        var page4BtPrevObj = $$("page4BtPrev");
        var page1BtNextObj = $$("page1BtNext");
        var page2BtNextObj = $$("page2BtNext");
        var page4BtNextObj = $$("page4BtNext");
        page1BtPrevObj.style.display = "none";
        page4BtPrevObj.style.display = "";
        page2BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page4BtNextObj.style.display = "";
        page2BtNextObj.style.display = "none";

        //设置搜索按钮操作
        var page2BtSearchObj = $$("page2BtSearch");
        var page2KeywordObj = $$("page2Keyword");
        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置搜索
        var page4BtSearchObj = $$("page4BtSearch");
        var page4KeywordObj = $$("page4Keyword");
        attachSearchTree(page4TreeObj,page4BtSearchObj,page4KeywordObj);

        //设置搜索
        var page4BtSearch2Obj = $$("page4BtSearch2");
        var page4Keyword2Obj = $$("page4Keyword2");
        attachSearchTree(page4Tree2Obj,page4BtSearch2Obj,page4Keyword2Obj);

        //设置添加按钮操作
        var page2BtAddObj = $$("page2BtAdd");
        page2BtAddObj.disabled = editable==false?true:false;
        page2BtAddObj.onclick = function() {
            addPage2TreeNode();
        }

        //设置删除按钮操作
        var page2BtDelObj = $$("page2BtDel");
        page2BtDelObj.disabled = editable==false?true:false;
        page2BtDelObj.onclick = function() {
            delPage2TreeNode();
        }

        //设置添加按钮操作
        var page4BtAddObj = $$("page4BtAdd");
        page4BtAddObj.disabled = editable==false?true:false;
        page4BtAddObj.onclick = function() {
             addPage4TreeNode();
        }

        //设置删除按钮操作
        var page4BtDelObj = $$("page4BtDel");
        page4BtDelObj.disabled = editable==false?true:false;
        page4BtDelObj.onclick = function() {
             delPage4TreeNode();
        }

        //设置保存按钮操作
        var page1BtSaveObj = $$("page1BtSave");
        var page2BtSaveObj = $$("page2BtSave");
        var page4BtSaveObj = $$("page4BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page2BtSaveObj.disabled = editable==false?true:false;
        page4BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page2BtSaveObj.onclick = page4BtSaveObj.onclick = function() {
            saveRole(cacheID,parentID,isNew);
        }
    }
    /*
     *	角色信息xform加载数据
     */
    function loadRoleInfoFormData(cacheID,editable) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_INFO);
        if(null!=xmlIsland) {
            var page1FormObj = $$("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	角色对用户组tree加载数据
     */
    function loadRole2GroupTreeData(cacheID) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_TO_GROUP_TREE);
        if(null!=xmlIsland) {
            var page2TreeObj = $$("page2Tree");
            page2TreeObj.load(xmlIsland.node);
            page2TreeObj.research = true;
        }
    }
    /*
     *	角色对用户组tree加载数据
     */
    function loadRole2GroupExistTreeData(cacheID) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_TO_GROUP_EXIST_TREE);
        if(null!=xmlIsland) {
            var page2Tree2Obj = $$("page2Tree2");
            page2Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	角色对用户tree加载数据
     */
    function loadRole2UserTreeData(cacheID) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_TO_USER_TREE);
        if(null!=xmlIsland) {
            var page4TreeObj = $$("page4Tree");
            page4TreeObj.load(xmlIsland.node);
            page4TreeObj.research = true;

            page4TreeObj.onTreeNodeDoubleClick = function(eventObj) {
                onPage4TreeNodeDoubleClick(eventObj);
            }
        }
    }
    /*
     *	角色对用户tree加载数据
     */
    function loadRole2UserExistTreeData(cacheID) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_TO_USER_EXIST_TREE);
        if(null!=xmlIsland) {
            var page4Tree3Obj = $$("page4Tree3");
            page4Tree3Obj.load(xmlIsland.node);
        }
    }

    function initFocus() {
        var treeTitleObj = $$("treeTitle");
        var statusTitleObj = $$("statusTitle");

        Focus.register(treeTitleObj.firstChild);
        Focus.register(statusTitleObj.firstChild);
    }

    function initEvents() {
        var treeBtRefreshObj = $$("treeBtRefresh");
        var treeTitleBtObj = $$("treeTitleBt");
        var statusTitleBtObj = $$("statusTitleBt");
        var paletteBtObj = $$("paletteBt");

        var treeTitleObj = $$("treeTitle");
        var statusTitleObj = $$("statusTitle");
        
        Event.attachEvent(treeBtRefreshObj,"click",onClickTreeBtRefresh);
        Event.attachEvent(treeTitleBtObj,"click",onClickTreeTitleBt);
        Event.attachEvent(statusTitleBtObj,"click",onClickStatusTitleBt);
        Event.attachEvent(paletteBtObj,"click",onClickPaletteBt);

        Event.attachEvent(treeTitleObj,"click",onClickTreeTitle);
        Event.attachEvent(statusTitleObj,"click",onClickStatusTitle);
    }
    /*
     *	点击树节点
     */
    function onTreeNodeActived(eventObj) {    
        var treeTitleObj = $$("treeTitle");
        Focus.focus(treeTitleObj.firstChild.id);

        showTreeNodeInfo();
    }
    /*
     *	双击树节点
     */
    function onTreeNodeDoubleClick(eventObj) {
        var treeNode = eventObj.treeNode;
        getTreeOperation(treeNode, function(_operation) {
            var canEdit = checkOperation("4", _operation);
            if( !isRootNode() ) {
                editTreeNode(canEdit);
            }
        });
    }
    /*
     *	右击树节点
     */
    function onTreeNodeRightClick(eventObj) {
        var treeObj = $$("tree");
        var treeNode = eventObj.treeNode;

        showTreeNodeInfo();

        var x = eventObj.clientX;
        var y = eventObj.clientY;
        getTreeOperation(treeNode, function(_operation) {
            if(treeObj.contextmenu) {
                treeObj.contextmenu.show(x, y);                
            }
        });
    }
    /*
     *	点击页4用户组树节点
     */
    function onPage4TreeNodeDoubleClick(eventObj) {
        var treeObj = $$("page4Tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            initPage4Tree2(id);
        }
    }
    /*
     *	page4Tree2初始化
     */
    function initPage4Tree2(id) {
        var page4Tree2Obj = $$("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponenetready",function() {
            loadPage4Tree2Data(id);
        });
    }
    /*
     *	tree加载数据
     */
    function loadPage4Tree2Data(treeID) {
        var cacheID = CACHE_ROLE_TO_USER_GRID + treeID;
        var treeGrid = Cache.Variables.get(cacheID);
        if(null==treeGrid) {
            var p = new HttpRequestParams();
            p.url = URL_GROUP_TO_USER_LIST;
            p.setContent("groupId", treeID);

            var request = new HttpRequest(p);
            request.onresult = function() {
                var sourceListNode = this.getNodeValue(XML_GROUP_TO_USER_LIST_TREE);
                var sourceListNodeID = cacheID+"."+XML_GROUP_TO_USER_LIST_TREE;

                Cache.XmlDatas.add(sourceListNodeID,sourceListNode);
                Cache.Variables.add(cacheID,sourceListNodeID);

                loadPage4Tree2DataFromCache(cacheID);
            }
            request.send();
        }else{        
            loadPage4Tree2DataFromCache(cacheID);
        }
    }
    /*
     *	tree从缓存加载数据
     */
    function loadPage4Tree2DataFromCache(cacheID) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_GROUP_TO_USER_LIST_TREE);
        if(null!=xmlIsland) {
            var page4Tree2Obj = $$("page4Tree2");
            page4Tree2Obj.load(xmlIsland.node);
            page4Tree2Obj.research = true;
        }
    }
    /*
     *	保存角色
     */
    function saveRole(cacheID,parentID,isNew) {
        //校验page1Form数据有效性
        var page1FormObj = $$("page1Form");
        if(false==page1FormObj.checkForm()) {
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_ROLE;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache) {
        
            //角色基本信息
            var roleInfoNode = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_INFO);
            if(null!=roleInfoNode) {
                var roleInfoDataNode = roleInfoNode.selectSingleNode(".//data");
                if(null!=roleInfoDataNode) {
                    flag = true;

                    var prefix = roleInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(roleInfoDataNode,prefix);
                }
            }


            //角色对用户
            var role2UserNode = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_TO_USER_EXIST_TREE);
            if(null!=role2UserNode) {
                var role2UserDataIDs = getTreeNodeIds(role2UserNode,"./treeNode//treeNode");
                if(0<role2UserDataIDs.length) {
                    flag = true;
                    p.setContent(XML_ROLE_TO_USER_IDS,role2UserDataIDs.join(","));
                }
            }


            //角色对用户组
            var role2GroupNode = Cache.XmlDatas.get(cacheID+"."+XML_ROLE_TO_GROUP_EXIST_TREE);
            if(null!=role2GroupNode) {
                var role2GroupDataIDs = getTreeNodeIds(role2GroupNode,"./treeNode//treeNode");
                if(0<role2GroupDataIDs.length) {
                    flag = true;
                    p.setContent(XML_ROLE_TO_GROUP_IDS,role2GroupDataIDs.join(","));
                }
            }        
        }

        if(true==flag) {
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $$("page1BtSave");
            var page2BtSaveObj = $$("page2BtSave");
            var page4BtSaveObj = $$("page4BtSave");
            syncButton([page1BtSaveObj,page2BtSaveObj,page4BtSaveObj],request);

            request.onresult = function() {
                if(true==isNew) {
                    //解除提醒
                    detachReminder(cacheID);

                    var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    appendTreeNode(parentID,treeNode);

                    var ws = $$("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function() {
                if(true!=isNew) {
                    //解除提醒
                    detachReminder(cacheID);

                    //更新树节点名称
                    var id = cacheID.trim(CACHE_ROLE_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
		

    /*
     *	删除page2里tree节点
     */
    function delPage2TreeNode() {
        removeTreeNode($("page2Tree2"));
    }
    /*
     *	添加page2里tree节点
     */
    function addPage2TreeNode() {
        var page2Tree2Obj = $$("page2Tree2");
        var page2TreeObj = $$("page2Tree");
        var selectedNodes = page2TreeObj.getSelectedTreeNode(false);

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++) {
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page2Tree2Obj,"id",id);
            if(false==sameAttributeTreeNode) {
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page2Tree2Obj.getTreeNodeById("_rootId");
                if(null!=treeNode) {
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page2Tree2Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload) {
            page2Tree2Obj.reload();
        }
        page2TreeObj.reload();
    }
    /*
     *	删除page4里tree节点
     */
    function delPage4TreeNode() {
        removeTreeNode($("page4Tree3"));
    }
    /*
     *	添加page4里tree节点
     */
    function addPage4TreeNode() {
        var page4Tree2Obj = $$("page4Tree2");
        var page4Tree3Obj = $$("page4Tree3");
        var selectedNodes = page4Tree2Obj.getSelectedTreeNode();

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++) {
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page4Tree3Obj,"id",id);
            if("_rootId"!=id && false==sameAttributeTreeNode) {
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page4Tree3Obj.getTreeNodeById("_rootId");
                if(null!=treeNode) {
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page4Tree3Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload) {
            page4Tree3Obj.reload();
        }
        page4Tree2Obj.reload();
    }
 
    /* 获取节点类型(1角色组/0角色) */
	function isRole() {
		return getTreeAttribute("isGroup") == "0";
	}

	function isRoleGroup() {
		return getTreeAttribute("isGroup") == "1";
	}

    /* 获取节点停启用状态 */
    function getTreeNodeState() {
        return getTreeAttribute("disabled");
    }
	
    /*
     *	角色权限设置
     */
    function setRolePermission() {
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            var name = treeNode.getName();
            
            var title = "设置\"" + name + "\"权限";
            var params = {
                roleId:id,
                isRole2Resource:"1"
            };

            window.showModalDialog("setpermission.htm",{params:params,title:title,type:"role"},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
        }
    }
 
    function moveNodeTo() {
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var treeNodeID = treeNode.getId();
            var isGroup = treeNode.getAttribute("isGroup");

            var targetId = window.showModalDialog("rolegrouptree.htm",{id:treeNodeID,isGroup:isGroup,xmlIsland:null},"dialogWidth:300px;dialogHeight:400px;");

            if(null!=targetId) {
                var p = new HttpRequestParams();
                p.url = URL_MOVE_NODE;
                p.setContent("roleId",treeNodeID);
                p.setContent("targetId",targetId);
                //p.setContent("moveState",2);//2表示跨层次移动

                var request = new HttpRequest(p);
                request.onsuccess = function() {
                    //移动树节点
                    var curNode = treeObj.getTreeNodeById(treeNodeID);
                    var xmlNode = new XmlNode(curNode.node);
                    var parentNode = treeObj.getTreeNodeById(targetId);

                    //父节点停用则下溯
                    var parentNodeState = parentNode.getAttribute("disabled");
                    if("1"==parentNodeState) {
                        //设置停用状态
                        refreshTreeNodeState(xmlNode,"1");
                    }
                    parentNode.node.appendChild(curNode.node);
                    parentNode.node.setAttribute("_open","true");

                    clearOperation(xmlNode);

                    treeObj.reload();
                }
                request.send();
            }
        }
    }

    /*
     *	是否根节点
     */
    function isRootNode(id) {
        if(null==id) {
            var treeObj = $$("tree");
            var treeNode = treeObj.getActiveTreeNode();
            if(null!=treeNode) {
                id = treeNode.getId();
            }            
        }
        var flag = ("-6"==id);
        return flag;
    }
    /*
     *	获取树操作权限
     *	参数：	treeNode:treeNode       treeNode实例
                function:callback       回调函数
     */
    function getTreeOperation(treeNode,callback) {
        var id = treeNode.getId();
        var _operation = treeNode.getAttribute("_operation");

        if(null==_operation || ""==_operation) {//如果节点上还没有_operation属性，则发请求从后台获取信息
            var p = new HttpRequestParams();
            p.url = URL_GET_OPERATION;
            p.setContent("roleId",id);

            var request = new HttpRequest(p);
            request.onresult = function() {
                _operation = this.getNodeValue(XML_OPERATION);
                treeNode.setAttribute("_operation",_operation);

                if(null!=callback) {
                    callback(_operation);
                }
            }
            request.send();            
        }else{
            if(null!=callback) {
                callback(_operation);
            }
        }    
    }
  
    /*
     *	授予角色
     */
    function setRole2Permission() {
        var treeObj = $$("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            var name = treeNode.getName();
            var resourceType = "5";  // getTreeAttribute("resourceTypeId")
            var type = "role";

            var title = "授予\"" + name + "\"角色";
            var params = {
                roleId:id,
                resourceType:resourceType,
                applicationId:"tss",
                isRole2Resource:"0"
            };
            window.showModalDialog("setpermission.htm",{params:params,title:title,type:type},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
        }
    }

    window.onload = init;

    //关闭页面自动注销
    logoutOnClose();