    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "MenuTree";
    XML_MENU_INFO = "MenuInfo";
    XML_MENU_ITEM_INFO = "MenuItemInfo";

    /*
     *	默认唯一编号名前缀
     */
    CACHE_MENU_DETAIL = "menuDetail_";
    CACHE_MENU_ITEM_DETAIL = "menuItemDetail_";

    /*
     *	XMLHTTP请求地址汇总
     */
    URL_SOURCE_TREE      = "/" + AUTH_PATH + "navigator/list";
    URL_SOURCE_DETAIL    = "/" + AUTH_PATH + "navigator/";
    URL_SOURCE_SAVE      = "/" + AUTH_PATH + "navigator";
    URL_DELETE_NODE      = "/" + AUTH_PATH + "navigator/";
    URL_SORT_NODE        = "/" + AUTH_PATH + "navigator/sort/";
    URL_MOVE_NODE        = "/" + AUTH_PATH + "navigator/move/";
	URL_STOP_NODE        = "/" + AUTH_PATH + "navigator/disable/";
    URL_GET_OPERATION    = "/" + AUTH_PATH + "navigator/operations/";
	URL_GET_PS_TREE      = "/" + AUTH_PATH + "navigator/pstree/";
	URL_GET_CHANNEL_TREE = "/" + AUTH_PATH + "channel/list";
	URL_FRESH_MENU_CACHE = "/" + AUTH_PATH + "navigator/cache/";
	URL_GET_MENU_TREE    = "/" + AUTH_PATH + "navigator/tree/";

	if(IS_TEST) {
		URL_SOURCE_TREE      = "data/menu_tree.xml?";
		URL_SOURCE_DETAIL    = "data/menu_detail.xml?";
		URL_SOURCE_SAVE      = "data/_success.xml?";
		URL_DELETE_NODE      = "data/_success.xml?";
		URL_SORT_NODE        = "data/_success.xml?";
		URL_MOVE_NODE        = "data/_success.xml?";
		URL_STOP_NODE        = "data/_success.xml?";
		URL_GET_OPERATION    = "data/operation.xml?";
		URL_GET_PS_TREE      = "data/structure_tree.xml?";
		URL_GET_CHANNEL_TREE = "data/channel_tree.xml?";
		URL_FRESH_MENU_CACHE = "data/_success.xml?";
		URL_GET_MENU_TREE    = "data/menu_tree.xml?";
	}
 
    function init() {
        initPaletteResize();
        initNaviBar("portal.3");
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }
 
    function initMenus() {
        var item1 = {
            label:"新建菜单",
            callback: function() { addNewMenu("1"); },
            visible: function() {return "1"==getTreeNodeType() && getOperation("2");}
        }
        var item2 = {
            label:"删除",
            callback: function() { delTreeNode() },
            icon:ICON + "icon_del.gif",
            visible: function() {return getOperation("2");}
        }
        var item3 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            visible: function() {return getOperation("2");}
        }
        var item4 = {
            label:"移动到...",
            callback:moveTo,
            icon:ICON + "move.gif",
            visible: function() {return "1" != getTreeNodeType() && getOperation("2");}
        }
		var item6 = {
            label:"刷新菜单缓存",
            callback:flushMenuCache,
            visible: function() {return "1" == getTreeNodeType() && getOperation("2");}
        }
		var item7 = {
            label:"启用",
            callback: function() { stopOrStartTreeNode("0"); },
            icon:ICON + "start.gif",
            visible:function() {return isTreeNodeDisabled();}
        }
        var item8 = {
            label:"停用",
            callback: function() { stopOrStartTreeNode("1"); },
            icon:ICON + "stop.gif",
            visible:function() {return !isTreeNodeDisabled();}
        }

		var item12 = {
            label:"新建普通链接",
			callback: function() { addNewMenu("4"); },
            visible: function() {return getOperation("2");}
        }
        var item5 = {
            label:"新建门户导航",
            visible: function() {return getOperation("2") && !isNullOrEmpty(getTreeAttribute("portalId"));}
        }

        var submenu = new Menu();
		subItem3 = {
            label:"门户内部链接",
            callback: function() { addNewMenu("3"); }
        }
        subItem5 = {
            label:"局部替换方式",
            callback: function() { addNewMenu("5"); }
        }
        subItem6 = {
            label:"行为方式",
            callback: function() { addNewMenu("6"); }
        }
		subItem7 = {
            label:"CMS栏目",
            callback: function() { addNewMenu("7"); }
        }

		submenu.addItem(subItem3);
		submenu.addItem(subItem7);
        submenu.addItem(subItem6);
		submenu.addItem(subItem5);
        item5.submenu = submenu;

        var menu1 = new Menu();
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addItem(item6);
		menu1.addItem(item4);
		menu1.addItem(item7);
		menu1.addItem(item8);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item5);
		menu1.addItem(item12);

        $$("tree").contextmenu = menu1;
    }

	function getTreeNodeType() {
        return getTreeAttribute("type");
    }
 
    function loadInitData() {
		Ajax({
			url: URL_SOURCE_TREE,
			onresult: function() {
				var dataXmlNode = this.getNodeValue(XML_MAIN_TREE);
				var tree = $T("tree", dataXmlNode);

				tree.element.onTreeNodeActived = function(eventObj) {
					onTreeNodeActived(eventObj);
				}
				tree.element.onTreeNodeDoubleClick = function(eventObj) {
					onTreeNodeDoubleClick(eventObj);
				}
				tree.element.onTreeNodeRightClick = function(eventObj) {
					onTreeNodeRightClick(eventObj, true);
				}
				tree.element.onTreeNodeMoved = function(eventObj) {
					sortTreeNode(URL_SORT_NODE, eventObj);
				}
			}
		});
    }
 
    function onTreeNodeDoubleClick(eventObj) {
        var treeNode = eventObj.treeNode;
        getTreeOperation(treeNode, function(_operation) {
            var hasPermission = checkOperation("2", _operation);
            if( hasPermission ) {
                editTreeNode();            
            }
        });
    }

	function addNewMenu(type) {
        var treeName = "菜单";
        var treeID = DEFAULT_NEW_ID;
        
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var parentId = treeNode.getId();
		var portalId = treeNode.getAttribute("portalId");

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadMenuDetailData(treeID, type, parentId);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, treeName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_MENU_DETAIL + treeID;
		var tab = ws.open(inf);
    }
 
    function editTreeNode() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadMenuDetailData(treeID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};            
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
		inf.SID = CACHE_MENU_DETAIL + treeID;
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadMenuDetailData(treeID, type, parentId) {
		var params = {};
		if(type ) {
			params.type = type;
		}
		if(parentId) {
			params.parentId = parentId;
		}
	
		Ajax({
			url: URL_SOURCE_DETAIL + treeID,
			params: params,
			onresult: function() {
				var menuInfoNode = this.getNodeValue(XML_MENU_INFO);
				Cache.XmlDatas.add( treeID + "." + XML_MENU_INFO, menuInfoNode );

				var page1FormObj = $X("page1Form", menuInfoNode);
				attachReminder(treeID, page1FormObj);

				//设置保存按钮操作
				$$("page1BtSave").onclick = function() {
					saveMenu(treeID, parentId);
				}
			}
		});
    }
 
    function saveMenu(cacheID, parentId) {
        var page1FormObj = $X("page1Form");
        if( !page1FormObj.checkForm() ) {
            return;
        }
     
		// 菜单基本信息
		var menuInfoNode = Cache.XmlDatas.get(cacheID + "." + XML_MENU_INFO);
		var menuInfoDataNode = menuInfoNode.selectSingleNode(".//data");
		if(menuInfoDataNode) {
			var p = new HttpRequestParams();
			p.url = URL_SOURCE_SAVE;
			p.setXFormContent(menuInfoDataNode);

            var request = new HttpRequest(p);

            syncButton([$$("page1BtSave")], request); // 同步按钮状态

            request.onresult = function() {
				detachReminder(cacheID); // 解除提醒

				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentId, treeNode);

				ws.closeActiveTab();
            }
            request.onsuccess = function() {
				detachReminder(cacheID); // 解除提醒

				// 更新树节点名称
				var name = page1FormObj.getData("name");
				modifyTreeNode(cacheID, "name", name, true);

				ws.closeActiveTab();
            }
            request.send();
        }
    }
 
 
    /*
     *	弹出窗口选择显示内容
     *	参数：	string:contentName      xform列名
                string:contentId        xform列名
                string:type             弹出窗口显示数据类型
     */
    function getContent(contentName, contentId, type) {
		var page1FormObj = $X("page1Form");
        var portalId = page1FormObj.getData("portalId");
		if( portalId == null ) {
			return;
		}

		var url = URL_GET_PS_TREE + portalId + "/" + type;
        var returnObj = window.showModalDialog("commontree.html", {title:"请选择菜单项对应内容", service:url},"dialogWidth:300px;dialogHeight:400px;");
        if(returnObj) {
            page1FormObj.updateDataExternal(contentId, returnObj.id);
            page1FormObj.updateDataExternal(contentName, returnObj.name);
        }
    }

    /*  选择菜单对应的栏目ID  */
	function getChannel() {
        var url = URL_GET_CHANNEL_TREE;
        var returnObj = window.showModalDialog("commontree.html", {title:"请选择菜单项对应栏目", service:url, nodename:"ChannelTree"},"dialogWidth:300px;dialogHeight:400px;");
        if(returnObj) {
			var page1FormObj = $X("page1Form");
			page1FormObj.updateDataExternal('name', returnObj.name);
            page1FormObj.updateDataExternal('url', "${common.articleListUrl}&channelId=" + returnObj.id);
            page1FormObj.updateDataExternal('description', "本菜单项对应栏目为：" + returnObj.name);
        }
    }
 
    function moveTo() {
        var tree = $T("tree");
        var treeNode = tree.getActiveTreeNode();
		var id = treeNode.getId();
		var name = treeNode.getName();

		var url = URL_GET_MENU_TREE + id;
		var returnObj = window.showModalDialog("commontree.html", {title:"将\"" + name + "\"移动到", service:url, nodename:"MenuTree"}, "dialogWidth:300px;dialogHeight:400px;");
		if(returnObj) {
			 moveTreeNode(tree, id, returnObj.id)
		}
    }
 
	/* 刷新菜单缓存  */
    function flushMenuCache() {
		Ajax({
			url: URL_FRESH_MENU_CACHE + getTreeNodeId()
		});
    }

    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();