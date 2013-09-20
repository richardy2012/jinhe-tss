    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "MenuTree";

    XML_MENU_INFO = "MenuInfo";
    XML_MENU_ITEM_INFO = "MenuItemInfo";
    XML_EXPORT_INFO = "Export";
    XML_OPERATION = "Operation";

    /*
     *	默认唯一编号名前缀
     */
    CACHE_MENU_DETAIL = "menuDetail__id";
    CACHE_VIEW_MENU_DETAIL = "viewMenuDetail__id";
    CACHE_MENU_ITEM_DETAIL = "menuItemDetail__id";
    CACHE_VIEW_MENU_ITEM_DETAIL = "viewMenuItemDetail__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新增$label";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/menu_init.xml";
    URL_MENU_DETAIL = "data/menu1.xml";
    URL_MENU_ITEM_DETAIL = "data/menu1.xml";
    URL_SAVE_MENU_DETAIL = "data/_success.xml";
    URL_SAVE_MENU_ITEM_DETAIL = "data/_success.xml";
    URL_DEL_MENU = "data/_success.xml";
    URL_DEL_MENU_ITEM = "data/_success.xml";
    URL_SORT_MENU = "data/_success.xml";
    URL_MOVE_MENU_ITEM = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
	URL_GET_PS_TREE = "data/sitetree_init.xml";
	URL_REFLUSH_MENU_CACHE = "data/_success.xml";
 
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
    /*
     *	函数说明：页面初始化加载数据(包括工具条、树)
     *	参数：	
     *	返回值：
     */
    function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function(){
            var _operation = this.getNodeValue(XML_OPERATION);

            var groupTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var groupTreeNodeID = CACHE_MAIN_TREE;

            Cache.XmlIslands.add(groupTreeNodeID,groupTreeNode);

            loadToolBar(_operation);
            initTree(groupTreeNodeID);
        }
        request.send();
    }
 
    function initMenus(){
        var item1 = {
            label:"新建菜单",
            callback:addNewMenu,
            enable:function(){return true;},
            visible:function(){return "0"==getTreeNodeType() && true==getOperation("2");}
        }
        var item2 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeNodeType() && true==getOperation("2");}
        }
        var item3 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeNodeType() && true==getOperation("2");}
        }
        var item5 = {
            label:"新建菜单项",
            callback:null,
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeNodeType() && true==getOperation("2");}
        }
        var item6 = {
            label:"移动到...",
            callback:moveMenuItemTo,
            icon:ICON + "move.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeNodeType() && "1"!=getTreeNodeType() && true==getOperation("2");}
        }
        var item7 = {
            label:"查看",
            callback:function(){
                editTreeNode(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeNodeType() && true==getOperation("2");}
        }
		var item8 = {
            label:"刷新菜单缓存",
            callback:flushMenuCache,
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeNodeType() && "1"==getTreeNodeType() && true==getOperation("2");}
        }

        var submenu = new Menu();
		subItem3 = {
            label:"门户内部链接",
            callback:function(){addNewMenuItem("3")},
            enable:function(){return true;},
            visible:function(){return true;}        
        }
        subItem4 = {
            label:"普通方式",
            callback:function(){addNewMenuItem("4")},
            enable:function(){return true;},
            visible:function(){return true;}        
        }
        subItem5 = {
            label:"局部替换方式",
            callback:function(){addNewMenuItem("5")},
            enable:function(){return true;},
            visible:function(){return true;}        
        }
        subItem6 = {
            label:"行为方式",
            callback:function(){addNewMenuItem("6")},
            enable:function(){return true;},
            visible:function(){return true;}        
        }
		subItem7 = {
            label:"CMS栏目方式",
            callback:function(){addNewMenuItem("7")},
            enable:function(){return true;},
            visible:function(){return true;}        
        }

        submenu.addItem(subItem4);
		submenu.addItem(subItem3);
        submenu.addItem(subItem6);
		submenu.addItem(subItem5);
		submenu.addItem(subItem7);
        item5.submenu = submenu;

        var treeObj = $("tree");

        var menu1 = new Menu();
        menu1.addItem(item7);
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addItem(item6);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item5);

	    menu1.addSeparator();
        menu1.addItem(item8);

        //menu1.attachTo(treeObj,"contextmenu");
        treeObj.contextmenu = menu1;
    }
    /*
     *	函数说明：区块初始化
     *	参数：	
     *	返回值：
     */
    function initBlocks(){
        var paletteObj = $("palette");
        Blocks.create(paletteObj);

        var treeContainerObj = $("treeContainer");
        Blocks.create(treeContainerObj,treeContainerObj.parentNode);

        var statusContainerObj = $("statusContainer");
        Blocks.create(statusContainerObj,statusContainerObj.parentNode,false);

        //状态信息区实例继承WritingBlock可写功能
        var block = Blocks.getBlock("statusContainer");
        if(null!=block){
            block.inherit(WritingBlock);
        }     
    }
    /*
     *	函数说明：显示用户状态信息
     *	参数：	number:rowIndex     grid数据行号
     *	返回值：
     */
    function showUserStatus(rowIndex){
        var gridObj = $("grid");
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowName = gridObj.getNamedNodeValue_Xml(rowIndex,"userName");
        var rowID = rowNode.getAttribute("id");
        var rowPermission = rowNode.getAttribute("permission");

        var block = Blocks.getBlock("statusContainer");
        if(null!=block){
            block.open();
            block.writeln("名称",rowName);
            block.writeln("ID",rowID);
            block.writeln("权限",rowID);
            block.close();
        }
    }
    /*
     *	函数说明：资源树初始化
     *	参数：	string:cacheID      缓存数据ID
     *	返回值：
     */
    function initTree(cacheID){
        var treeObj = $("tree");
        Public.initHTC(treeObj,"isLoaded","oncomponentready",function(){
            initTreeData(cacheID);
        });
    }
    /*
     *	函数说明：资源树加载数据
     *	参数：
     *	返回值：
     */
    function initTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID);
        if(null!=xmlIsland){
            var treeObj = $("tree");
            treeObj.load(xmlIsland.node);

            treeObj.onTreeNodeActived = function(eventObj){
                onTreeNodeActived(eventObj);
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
    }
    /*
     *	函数说明：聚焦初始化
     *	参数：	
     *	返回值：
     */
    function initFocus(){
        var treeTitleObj = $("treeTitle");
        var statusTitleObj = $("statusTitle");

        Focus.register(treeTitleObj.firstChild);
        Focus.register(statusTitleObj.firstChild);
    }
    /*
     *	函数说明：事件绑定初始化
     *	参数：	
     *	返回值：
     */
    function initEvents(){
        var treeBtRefreshObj = $("treeBtRefresh");
        var treeTitleBtObj = $("treeTitleBt");
        var statusTitleBtObj = $("statusTitleBt");
        var paletteBtObj = $("paletteBt");

        var treeTitleObj = $("treeTitle");
        var statusTitleObj = $("statusTitle");
        
        Event.attachEvent(treeBtRefreshObj,"click",onClickTreeBtRefresh);
        Event.attachEvent(treeTitleBtObj,"click",onClickTreeTitleBt);
        Event.attachEvent(statusTitleBtObj,"click",onClickStatusTitleBt);
        Event.attachEvent(paletteBtObj,"click",onClickPaletteBt);

        Event.attachEvent(treeTitleObj,"click",onClickTreeTitle);
        Event.attachEvent(statusTitleObj,"click",onClickStatusTitle);
    }
    /*
     *	函数说明：点击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeActived(eventObj){
        var treeTitleObj = $("treeTitle");
        Focus.focus(treeTitleObj.firstChild.id);

        showTreeNodeStatus({id:"ID",name:"名称",creator:"创建者",createTime:"创建时间",modifier:"修改者",modifyTime:"修改时间"});

        var treeNode = eventObj.treeNode;
        //防止因为载入工具条数据而导致不响应双击事件
        clearTimeout(window._toolbarTimeout);
        window._toolbarTimeout = setTimeout(function(){
            loadToolBarData(treeNode);
        },0);
    }
    /*
     *	函数说明：双击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeDoubleClick(eventObj){
        var treeNode = eventObj.treeNode;
        var id = getTreeNodeId();
        var type = getTreeNodeType();
        getTreeOperation(treeNode,function(_operation){
            var canAddNewMenu = checkOperation("pm1",_operation);
            var canEdit = checkOperation("pm3",_operation);
            if("_rootId"!=id && "0"!=type){
                editTreeNode(canEdit);            
            }
        });
    }
    /*
     *	函数说明：右击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeRightClick(eventObj){
        var treeObj = $("tree");
        var treeNode = eventObj.treeNode;

        showTreeNodeStatus({id:"ID",name:"名称",creator:"创建者",createTime:"创建时间",modifier:"修改者",modifyTime:"修改时间"});

        var x = eventObj.clientX;
        var y = eventObj.clientY;
        getTreeOperation(treeNode,function(_operation){
            if(null!=treeObj.contextmenu){
                treeObj.contextmenu.show(x,y);                
            }
            loadToolBar(_operation);
        });
    }
    /*
     *	函数说明：拖动树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeMoved(eventObj){
        sortMenuTo(eventObj);
    }
    /*
     *	函数说明：工具条载入数据
     *	参数：	treeNode:treeNode       treeNode实例
     *	返回值：
     */
    function loadToolBarData(treeNode){
        if(null!=treeNode){
            getTreeOperation(treeNode,function(_operation){
                loadToolBar(_operation);
            });
        }
    }
    /*
     *	函数说明：编辑菜单信息
     *	参数：  boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editMenuInfo(editable){
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
                    loadMenuDetailData(treeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};            
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                inf.SID = CACHE_VIEW_MENU_DETAIL + treeID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_MENU_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：菜单节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentId             父节点id
                boolean:isNew               是否新增
     *	返回值：
     */
    function loadMenuDetailData(treeID,editable,id,portalId,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_MENU_DETAIL + treeID;
        }else{
            var cacheID = CACHE_MENU_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_MENU_DETAIL;
            p.setContent("id", treeID);
            //如果是新增
            if(true==isNew){
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
        }else{
            initMenuPages(cacheID,editable,id,isNew);
        }
    }
    /*
     *	函数说明：菜单相关页加载数据
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
     *	函数说明：菜单信息xform加载数据
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
     *	函数说明：保存菜单
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
        p.url = URL_SAVE_MENU_DETAIL;

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

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            syncButton([page1BtSaveObj],request);

            request.onresult = function(){
                if(true==isNew){
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
    /*
     *	函数说明：新建菜单
     *	参数：
     *	返回值：
     */
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
     *	函数说明：选择导入文件
     *	参数：
     *	返回值：
     */
    function getFile(name){
        var page1FormObj = $("page1Form");
        page1FormObj.updateDataExternal(name,"file_id1");
    }
    /*
     *	函数说明：删除树节点
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
    /*
     *	函数说明：删除菜单
     *	参数：
     *	返回值：
     */
    function delMenu(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var p = new HttpRequestParams();
            var id = treeNode.getId();

            p.url = URL_DEL_MENU;		
            p.setContent("id",id);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var parentNode = treeNode.getParent();
                if(null!=parentNode){
                    treeObj.setActiveTreeNode(parentNode.getId());
                }
                //从树上删除
                treeObj.removeTreeNode(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：删除菜单项
     *	参数：
     *	返回值：
     */
    function delMenuItem(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var p = new HttpRequestParams();
            var id = treeNode.getId();

            p.url = URL_DEL_MENU_ITEM;		
            p.setContent("id",id);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var parentNode = treeNode.getParent();
                if(null!=parentNode){
                    treeObj.setActiveTreeNode(parentNode.getId());
                }
                //从树上删除
                treeObj.removeTreeNode(treeNode);
            }
            request.send();
        }
    }

    /*
     *	函数说明：同一父节点下移动Menu节点
     *	参数：	
     *	返回值：
     */
    function sortMenuTo(eventObj){
        var treeObj = $("tree");
        var movedTreeNode = eventObj.movedTreeNode;
        var toTreeNode = eventObj.toTreeNode;
        var moveState = eventObj.moveState;

        //只允许菜单项排序
        if("0"==movedTreeNode.getAttribute("type") || "1"==movedTreeNode.getAttribute("type")){
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SORT_MENU;
        p.setContent("targetId",toTreeNode.getId());
        p.setContent("id",movedTreeNode.getId());
        p.setContent("direction",moveState);//-1目标上方,1目标下方

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //移动树节点
            treeObj.moveTreeNode(movedTreeNode, toTreeNode, moveState);
        }
        request.send();
    }
    /*
     *	函数说明：获取节点id
     *	参数：  
     *	返回值：string:id   树节点id
     */
    function getTreeNodeId(){
        return getTreeAttribute("id");
    }
    /*
     *	函数说明：获取节点type
     *	参数：  
     *	返回值：string:type   树节点type
     */
    function getTreeNodeType(){
        return getTreeAttribute("type");
    }
    /*
     *	函数说明：获取节点disabled
     *	参数：  
     *	返回值：string:disabled   树节点disabled
     */
    function getTreeNodeDisabled(){
        return getTreeAttribute("disabled");
    }
    /*
     *	函数说明：编辑树节点
     *	参数：	boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editTreeNode(editable){
        if("1"==getTreeNodeType()){
            editMenuInfo(editable);
        }else{
            editMenuItemInfo(editable);
        }
    }
    /*
     *	函数说明：刷新树节点停用启用状态
     *	参数：	treeNode:treeNode       treeNode实例
                string:state            停/启用状态
     *	返回值：
     */
    function refreshTreeNodeState(treeNode,state){
        treeNode.setAttribute("disabled",state);
        treeNode.setAttribute("icon",ICON + "icon_menu" + (state=="1"?"_stop":"") + ".gif");       
    }

    /*
     *	函数说明：新建菜单项
     *	参数：  string:type     
     *	返回值：
     */
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
    /*
     *	函数说明：编辑菜单项信息
     *	参数：  boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
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
    /*
     *	函数说明：菜单项详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentId             父节点id
                string:type                 菜单项类型
                boolean:isNew               是否新增
     *	返回值：
     */
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
            if(true==isNew){
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
    /*
     *	函数说明：菜单项相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                string:treeID               父节点id
                boolean:isNew               是否新增
     *	返回值：
     */
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
     *	函数说明：菜单项信息xform加载数据
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
    /*
     *	函数说明：保存菜单
     *	参数：	string:cacheID      缓存数据id
                string:treeID       父节点id
                boolean:isNew       是否新增
     *	返回值：
     */
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

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            syncButton([page1BtSaveObj],request);

            request.onresult = function(){
                if(true==isNew){
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
     *	函数说明：弹出窗口选择显示内容
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
     *	函数说明：弹出窗口选择栏目ID
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
     *	函数说明：移动菜单项节点
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
                p.url = URL_MOVE_MENU_ITEM;
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
     *	函数说明：获取树操作权限
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
     *	函数说明：刷新菜单缓存
     *	参数：
     *	返回值：
     */
    function flushMenuCache(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var p = new HttpRequestParams();
        p.url = URL_REFLUSH_MENU_CACHE;
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