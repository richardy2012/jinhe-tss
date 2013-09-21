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
    CACHE_MAIN_TREE = "menu_tree_";

    /*
     *	XMLHTTP请求地址汇总
     */
    URL_SOURCE_TREE      = "data/menu_tree.xml";
    URL_SOURCE_DETAIL    = "data/menu_detail.xml";
    URL_SOURCE_SAVE      = "data/_success.xml";
    URL_DEL_MENU         = "data/_success.xml";
    URL_DELETE_NODE      = "data/_success.xml";
    URL_SOURCE_SORT      = "data/_success.xml";
    URL_SOURCE_MOVE      = "data/_success.xml";
    URL_GET_OPERATION    = "data/operation.xml";
	URL_GET_PS_TREE      = "data/stuctrue_tree.xml";
	URL_FRESH_MENU_CACHE = "data/_success.xml";
 
    function init(){
        initPaletteResize();
        initUserInfo();
        initNaviBar("portal.3");
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }
 
    function initMenus(){
        var item1 = {
            label:"新建菜单",
            callback:addNewMenu,
            visible:function(){return "1"==getTreeNodeType() && getOperation("2");}
        }
        var item2 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            visible:function(){return getOperation("2");}
        }
        var item3 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            visible:function(){return getOperation("2");}
        }
        var item4 = {
            label:"移动到...",
            callback:moveMenuItemTo,
            icon:ICON + "move.gif",
            visible:function(){return "1" != getTreeNodeType() && getOperation("2");}
        }
		var item6 = {
            label:"刷新菜单缓存",
            callback:flushMenuCache,
            visible:function(){return "1" == getTreeNodeType() && getOperation("2");}
        }

        var item5 = {
            label:"新建菜单项",
            visible:function(){return getOperation("2");}
        }
        var submenu = new Menu();
		subItem3 = {
            label:"门户内部链接",
            callback:function(){addNewMenuItem("3")},
            visible:function(){return true;}        
        }
        subItem4 = {
            label:"普通方式",
            callback:function(){addNewMenuItem("4")},
            visible:function(){return true;}        
        }
        subItem5 = {
            label:"局部替换方式",
            callback:function(){addNewMenuItem("5")},
            visible:function(){return true;}        
        }
        subItem6 = {
            label:"行为方式",
            callback:function(){addNewMenuItem("6")},
            visible:function(){return true;}        
        }
		subItem7 = {
            label:"CMS栏目方式",
            callback:function(){addNewMenuItem("7")},
            visible:function(){return true;}        
        }

        submenu.addItem(subItem4);
		submenu.addItem(subItem3);
        submenu.addItem(subItem6);
		submenu.addItem(subItem5);
		submenu.addItem(subItem7);
        item5.submenu = submenu;

        var menu1 = new Menu();
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addItem(item6);
		menu1.addItem(item4);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item5);

        $$("tree").contextmenu = menu1;
    }
 
    function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_SOURCE_TREE;

        var request = new HttpRequest(p);
        request.onresult = function(){
            var _operation = this.getNodeValue(XML_OPERATION);

            var menuTreeNode = this.getNodeValue(XML_MAIN_TREE);

            Cache.XmlIslands.add(CACHE_MAIN_TREE, menuTreeNode);

            var treeObj = $T("tree", menuTreeNode);

            treeObj.onTreeNodeActived = function(eventObj){
                Focus.focus( $$("treeTitle").firstChild.id );
				showTreeNodeInfo();
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj){
                onTreeNodeDoubleClick(eventObj);
            }
            treeObj.onTreeNodeRightClick = function(eventObj){
                onTreeNodeRightClick(eventObj);
            }
            treeObj.onTreeNodeMoved = function(eventObj){
                onTreeNodeMoved(eventObj);
            }
            
        }
        request.send();
    }
 
    function onTreeNodeDoubleClick(eventObj){
        var treeNode = eventObj.treeNode;
        getTreeOperation(treeNode, function(_operation){
            var hasPermission = checkOperation("2", _operation);
            if( hasPermission ) {
                editTreeNode();            
            }
        });
    }
 
    function onTreeNodeRightClick(eventObj){
        var treeObj = $T("tree");
        var treeNode = eventObj.treeNode;

        showTreeNodeInfo();

        getTreeOperation(treeNode, function(_operation) {
            if(tree.element.contextmenu){
                treeObj.element.contextmenu.show(eventObj.clientX, eventObj.clientY);                
            }
            loadToolBar(_operation);
        });
    }
 
    function onTreeNodeMoved(eventObj){
		sortTreeNode(URL_SOURCE_SORT, eventObj);
    }
 
    function editMenuInfo(){
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		var callback = {};
		callback.onTabChange = function(){
			setTimeout(function(){
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

    /*
     *	菜单节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentId             父节点id
                boolean:isNew               是否新增
     *	返回值：
     */
    function loadMenuDetailData(treeID, type) {
		var p = new HttpRequestParams();
		p.url = URL_SOURCE_DETAIL + treeID + "/";
		p.setContent("id", treeID);

		//如果是新增
		if(isNew){
			p.setContent("isNew", 1);
			p.setContent("portalId", portalId);
			p.setContent("parentId", id);
			p.setContent("type", 1);
		}else{
		  p.setContent("type", getTreeNodeType());
		}
		
		var request = new HttpRequest(p);
		request.onresult = function(){
			var menuInfoNode = this.getNodeValue(XML_MENU_INFO);

			var menuInfoNodeID = cacheID+"."+XML_MENU_INFO;

			Cache.XmlIslands.add(menuInfoNodeID,menuInfoNode);

			Cache.Variables.add(cacheID,[menuInfoNodeID]);

			initMenuPages(cacheID,editable,id,isNew);
		}
		request.send();
    }
    /*
     *	菜单相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                string:treeID               父节点id
                boolean:isNew               是否新增
     *	返回值：
     */
    function initMenuPages(cacheID,editable,id,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadMenuInfoFormData(cacheID,editable);
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = function(){
            saveMenu(cacheID,id,isNew);
        }
    }
    /*
     *	菜单信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadMenuInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_MENU_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	保存菜单
     *	参数：	string:cacheID      缓存数据id
                string:treeID       父节点id
                boolean:isNew       是否新增
     *	返回值：
     */
    function saveMenu(cacheID,parentId,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SOURCE_SAVE;

        //是否提交
        var flag = false;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){       

            //菜单基本信息
            var menuInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_MENU_INFO);

            if(null!=menuInfoNode){
                var menuInfoDataNode = menuInfoNode.selectSingleNode(".//data");
                if(null!=menuInfoDataNode){
                    flag = true;

                    var prefix = menuInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(menuInfoDataNode,prefix);
                }
            }
        }

        if(flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            syncButton([page1BtSaveObj],request);

            request.onresult = function(){
                if(isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    appendTreeNode(parentId,treeNode);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function(){
                if(true!=isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    //更新树节点名称
                    var id = cacheID.trim(CACHE_MENU_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
 
    function addNewMenu(){
        
        var treeName = "菜单";
        var treeID = new Date().valueOf();
        
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var parentId = treeNode.getAttribute("id");
            var portalId = treeNode.getAttribute("portalId");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadMenuDetailData(treeID,true,id, portalId,true);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_ADD.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_MENU_DETAIL + treeID;
            var tab = ws.open(inf);
        }
    }
 
    /*
     *	删除树节点
     *	参数：
     *	返回值：
     */
    function delTreeNode(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }
        if("2"==getTreeNodeType()){
            delMenu();
        }else{
            delMenuItem();
        }
    }
 
    function getTreeNodeType(){
        return getTreeAttribute("type");
    }
 
    function editTreeNode(editable){
        if("1"==getTreeNodeType()){
            editMenuInfo(editable);
        }else{
            editMenuItemInfo(editable);
        }
    }
 
    function addNewMenuItem(type){
        
        var treeName = "菜单项";
        var treeID = new Date().valueOf();
        
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var parentId = treeNode.getAttribute("id");
            var portalId = treeNode.getAttribute("portalId");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadMenuItemDetailData(treeID,true,parentId,portalId,type,true);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_ADD.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_MENU_ITEM_DETAIL + treeID;
            var tab = ws.open(inf);
        }
    }
 
    function editMenuItemInfo(editable){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadMenuItemDetailData(treeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                inf.SID = CACHE_VIEW_MENU_ITEM_DETAIL + treeID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_MENU_ITEM_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
 
    function loadMenuItemDetailData(treeID,editable,parentId,portalId,type,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_MENU_ITEM_DETAIL + treeID;
        }else{
            var cacheID = CACHE_MENU_ITEM_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_MENU_ITEM_DETAIL;
           
            //如果是新增
            if(isNew){
                p.setContent("isNew", 1);
                p.setContent("parentId", parentId);
                p.setContent("portalId", portalId);
                p.setContent("type", type);
            }else{
               p.setContent("id", treeID);
               p.setContent("type", getTreeNodeType());
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var menuItemInfoNode = this.getNodeValue(XML_MENU_ITEM_INFO);

                var menuItemInfoNodeID = cacheID+"."+XML_MENU_ITEM_INFO;

                Cache.XmlIslands.add(menuItemInfoNodeID,menuItemInfoNode);

                Cache.Variables.add(cacheID,[menuItemInfoNodeID]);

                initMenuItemPages(cacheID,editable,parentId,isNew);
            }
            request.send();
        }else{
            initMenuItemPages(cacheID,editable,parentId,isNew);
        }
    }
 
    function initMenuItemPages(cacheID,editable,parentId,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadMenuItemInfoFormData(cacheID,editable);
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = function(){
            saveMenuItem(cacheID,parentId,isNew);
        }
    }
    /*
     *	菜单项信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadMenuItemInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_MENU_ITEM_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
 
    function saveMenuItem(cacheID,parentId,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_MENU_ITEM_DETAIL;

        //是否提交
        var flag = false;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){       

            //菜单项基本信息
            var menuItemInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_MENU_ITEM_INFO);

            if(null!=menuItemInfoNode){
                var menuItemInfoDataNode = menuItemInfoNode.selectSingleNode(".//data");
                if(null!=menuItemInfoDataNode){
                    flag = true;

                    var prefix = menuItemInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(menuItemInfoDataNode,prefix);
                }
            }
        }

        if(flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            syncButton([page1BtSaveObj],request);

            request.onresult = function(){
                if(isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    appendTreeNode(parentId,treeNode);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function(){
                if(true!=isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    //更新树节点名称
                    var id = cacheID.trim(CACHE_MENU_ITEM_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
    /*
     *	弹出窗口选择显示内容
     *	参数：	string:contentName      xform列名
                string:contentId        xform列名
                string:type             弹出窗口显示数据类型
     *	返回值：
     */
    function getContent(contentName,contentId,type){
        var action = URL_GET_PS_TREE;

		var page1FormObj = $("page1Form");
        var portalId = page1FormObj.getData("portalId");
        var params = {
            action:"portlet",
            portalId:portalId,
            type:type
        };

        var portlet = window.showModalDialog("sitetree.htm",{params:params,title:"请选择菜单项对应内容",action:action},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=portlet){
            page1FormObj.updateDataExternal(contentId, portlet.id);
            page1FormObj.updateDataExternal(contentName, portlet.name);
        }
    }

    /*
     *	弹出窗口选择栏目ID
     *	返回值：
     */
	function getChannel(){
        var action = URL_GET_PS_TREE;

		var page1FormObj = $("page1Form");

        var channel = window.showModalDialog("channeltree.htm",{title:"请选择菜单项对应栏目"},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=channel){
			page1FormObj.updateDataExternal('name', channel.name);
            page1FormObj.updateDataExternal('url', "${common.articleListUrl}&channelId=" + channel.id);
            page1FormObj.updateDataExternal('description', "本菜单项对应栏目为：" + channel.name);
        }
    }
    /*
     *	移动菜单项节点
     *	参数：	
     *	返回值：
     */
    function moveMenuItemTo(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var name = treeNode.getName();
            var portalId = treeNode.getAttribute("portalId");

            var action = "pms/navigator!getNavigators4Tree.action";
            var params = {
                id:id,
                portalId:portalId,
                action:"moveTo"
            };

            var menu = window.showModalDialog("sitetree.htm",{params:params,title:"将\""+name+"\"移动到",action:action},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=menu){
                var p = new HttpRequestParams();
                p.url = URL_SOURCE_MOVE;
                p.setContent("targetId",menu.id);
                p.setContent("id",id);
                p.setContent("portalId",portalId);

                var request = new HttpRequest(p);
                request.onsuccess = function(){
                    //移动树节点
                    var curNode = treeObj.getTreeNodeById(id);
                    var parentNode = treeObj.getTreeNodeById(menu.id);
                    parentNode.node.appendChild(curNode.node);
                    parentNode.node.setAttribute("_open","true");

                    var xmlNode = new XmlNode(curNode.node);
                    clearOperation(xmlNode);

                    treeObj.reload();
                }
                request.send();
            }
        }
    }
       
    /*
     *	获取树操作权限
     *	参数：	treeNode:treeNode       treeNode实例
                function:callback       回调函数
     *	返回值：
     */
    function getTreeOperation(treeNode,callback){
        var id = treeNode.getId();
        var _operation = treeNode.getAttribute("_operation");

        if(null==_operation || ""==_operation){//如果节点上还没有_operation属性，则发请求从后台获取信息
            var p = new HttpRequestParams();
            p.url = URL_GET_OPERATION;
            p.setContent("resourceId",id);

            var request = new HttpRequest(p);
            request.onresult = function(){
                _operation = this.getNodeValue(XML_OPERATION);
                treeNode.setAttribute("_operation",_operation);

                if(null!=callback){
                    callback(_operation);
                }
            }
            request.send();            
        }else{
            if(null!=callback){
                callback(_operation);
            }
        }    
    }

	/*
     *	刷新菜单缓存
     */
    function flushMenuCache(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var p = new HttpRequestParams();
        p.url = URL_FRESH_MENU_CACHE;
        if(null!=treeNode){
            var treeID = treeNode.getId();
            p.setContent("key",treeID);
            p.setContent("code","menu_pool");
            var request = new HttpRequest(p);

            request.send();	
        }
    }


    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();