    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "PublishTree";

    XML_PUBLISH_INFO = "PublishInfo";
    XML_OPERATION = "Operation";
    XML_THEME_LIST = "ThemeList";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_VIEW_TREE_NODE_DETAIL = "viewTreeNode__id";
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
	URL_INIT = "data/publish_init.xml";
    URL_PUBLISH_DETAIL = "data/publish1.xml";
    URL_SAVE_PUBLISH = "data/_success.xml";
    URL_DEL_PUBLISH = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_THEME_LIST = "data/themelist.xml";
	URL_STATIC_ISSUE = "data/_success.xml";

    URL_INIT = "pms/portal!getAllIssues4Tree.action";
    URL_PUBLISH_DETAIL = "pms/portal!getIssueInfoById.action";
    URL_SAVE_PUBLISH = "pms/portal!saveIssue.action";
    URL_DEL_PUBLISH = "pms/portal!removeIssue.action";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_THEME_LIST = "pms/portal!getThemesByPortal.action";
	URL_STATIC_ISSUE = "pms/portal!staticIssuePortal.action";
    /*
     *	延时
     */
    TIMEOUT_TAB_CHANGE = 200;
    /*
     *	icon路径
     */
    ICON = "../platform/images/icon/";

    var toolbar = null;

    /*
     *	函数说明：页面初始化
     *	参数：	
     *	返回值：
     */
    function init(){
        initPaletteResize();
//        initUserInfo();
        initToolBar();
        initNaviBar("pms.6");
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
    /*
     *	函数说明：工具条加载数据
     *	参数：	string:_operation      操作权限
     *	返回值：
     */
    function loadToolBar(_operation){
        var xmlIsland = Cache.XmlIslands.get(CACHE_TOOLBAR);
        if(null==xmlIsland){//还没有就创建

            var str = [];
            str[str.length] = "<toolbar>";

            //公共
            str[str.length] = "    <button id=\"a1\" code=\"p1\" icon=\"" + ICON + "icon_pre.gif\" label=\"上页\" cmd=\"ws.prevTab()\" enable=\"true\"/>";
            str[str.length] = "    <button id=\"a2\" code=\"p2\" icon=\"" + ICON + "icon_next.gif\" label=\"下页\" cmd=\"ws.nextTab()\" enable=\"true\"/>";
            str[str.length] = "    <separator/>";

            //发布管理
            str[str.length] = "    <button id=\"b1\" code=\"ppublish3\" icon=\"" + ICON + "view.gif\" label=\"查看\" cmd=\"editPublishInfo(false)\" enable=\"'_rootId'!=getTreeId()\"/>";
            str[str.length] = "    <button id=\"b2\" code=\"ppublish3\" icon=\"" + ICON + "edit.gif\" label=\"编辑\" cmd=\"editPublishInfo()\" enable=\"'_rootId'!=getTreeId()\"/>";
            str[str.length] = "    <button id=\"b3\" code=\"ppublish2\" icon=\"" + ICON + "del.gif\" label=\"删除\" cmd=\"delPublish()\" enable=\"'_rootId'!=getTreeId()\"/>";
            str[str.length] = "    <button id=\"b4\" code=\"ppublish1\" icon=\"" + ICON + "new_url.gif\" label=\"新建访问地址\" cmd=\"addNewPublish()\" enable=\"'_rootId'==getTreeId()\"/>";
            str[str.length] = "</toolbar>";

            var xmlReader = new XmlReader(str.join("\r\n"));
            var xmlNode = new XmlNode(xmlReader.documentElement);

            Cache.XmlIslands.add(CACHE_TOOLBAR,xmlNode);
            xmlIsland = xmlNode;

            //载入工具条
            toolbar.loadXML(xmlIsland);
        }

        //控制显示
        var buttons = xmlIsland.selectNodes("./button");
        for(var i=0,iLen=buttons.length;i<iLen;i++){
            var curButton = buttons[i];
            var id = curButton.getAttribute("id");
            var code = curButton.getAttribute("code");
            var enableStr = curButton.getAttribute("enable");

            var reg = new RegExp("(^"+code+",)|(^"+code+"$)|(,"+code+",)|(,"+code+"$)","gi");
            var visible = false;
            if("string"==typeof(_operation)){
                visible = (true==reg.test(_operation)?true:false);
            }
            toolbar.setVisible(id,visible);

            if(true==visible){
                var enable = Public.execCommand(enableStr);
                toolbar.enable(id,enable);
            }
        }
    }
    /*
     *	函数说明：菜单初始化
     *	参数：	
     *	返回值：
     */
    function initMenus(){
        initTreeMenu();
    }
    /*
     *	函数说明：树菜单初始化
     *	参数：	
     *	返回值：
     */
    function initTreeMenu(){
        var item1 = {
            label:"新建访问地址",
            callback:addNewPublish,
            enable:function(){return true;},
            visible:function(){return "_rootId"==getTreeId() && true==getOperation("ppublish1");}
        }
        var item2 = {
            label:"删除",
            callback:delPublish,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeId() &&true==getOperation("ppublish2");}
        }
        var item3 = {
            label:"编辑",
            callback:editPublishInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeId() &&true==getOperation("ppublish3");}
        }
        var item4 = {
            label:"查看",
            callback:function(){
                editPublishInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeId() &&true==getOperation("ppublish3");}
        }
		var item5 = {
            label:"发布为静态页面",
            callback:staticIssue,
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeId() && true==getOperation("ppublish3");}
        }

        var treeObj = $("tree");

        var menu1 = new Menu();
        menu1.addItem(item4);
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item1);
		menu1.addItem(item5);

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

        showTreeNodeStatus({id:"ID",name:"名称"});

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
        var id = getTreeId();
        getTreeOperation(treeNode,function(_operation){
            var canAddNewPublish = checkOperation("ppublish1",_operation);
            var canEdit = checkOperation("ppublish3",_operation);
            if("_rootId"!=id){
                editPublishInfo(canEdit);            
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

        showTreeNodeStatus({id:"ID",name:"名称"});

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
     *	函数说明：编辑发布信息
     *	参数：  boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editPublishInfo(editable){
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
                    loadTreeDetailData(treeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                inf.SID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
     *	返回值：
     */
    function loadTreeDetailData(treeID,editable,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_TREE_NODE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_PUBLISH_DETAIL;

			p.setContent("id", treeID);
            //如果是新增
            if(true==isNew){
                p.setContent("isNew", 1);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var layoutInfoNode = this.getNodeValue(XML_PUBLISH_INFO);

                var layoutInfoNodeID = cacheID+"."+XML_PUBLISH_INFO;

                Cache.XmlIslands.add(layoutInfoNodeID,layoutInfoNode);

                Cache.Variables.add(cacheID,[layoutInfoNodeID]);

                initPublishPages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initPublishPages(cacheID,editable,isNew);
        }
    }
    /*
     *	函数说明：portlet相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
     *	返回值：
     */
    function initPublishPages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadPublishInfoFormData(cacheID,editable);
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = function(){
            savePublish(cacheID,isNew);
        }
    }
    /*
     *	函数说明：portlet信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadPublishInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_PUBLISH_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            var portalName = page1FormObj.getData("portalName");
            if(null!=portalName && ""!=portalName){
                page1FormObj.setColumnEditable("pageName","true");
            }else{
                page1FormObj.setColumnEditable("pageName","false");
            }
            
            page1FormObj.ondatachange = function(){
                //2007-3-1 离开提醒
                attachReminder(cacheID);

                var name = event.result.name;
                var value = event.result.newValue;
                if("portalName"==name){
                    updateThemeColumn(cacheID,value);
                }
            
            }
        }
    }
    /*
     *	函数说明：保存发布
     *	参数：	string:cacheID      缓存数据id
                boolean:isNew       是否新增
     *	返回值：
     */
    function savePublish(cacheID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_PUBLISH;

        //是否提交
        var flag = false;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){       

            //portlet基本信息
            var publishInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_PUBLISH_INFO);
            if(null!=publishInfoNode){
                var publishInfoDataNode = publishInfoNode.selectSingleNode(".//data");
                if(null!=publishInfoDataNode){
                    flag = true;

                    var prefix = publishInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(publishInfoDataNode,prefix);
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
                    appendTreeNode("_rootId",treeNode);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function(){
                if(true!=isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    //更新树节点名称
                    var id = cacheID.trim(CACHE_TREE_NODE_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：新建portlet
     *	参数：
     *	返回值：
     */
    function addNewPublish(){
        var treeName = "发布";
        var treeID = new Date().valueOf();

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadTreeDetailData(treeID,true,true);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_ADD.replace(/\$label/i,treeName);
        inf.phases = null;
        inf.callback = callback;
        inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：选择发布路径
     *	参数：
     *	返回值：
     */
    function getPath(name){
        var page1FormObj = $("page1Form");
        page1FormObj.updateDataExternal(name,"path_id1");
    }
    /*
     *	函数说明：获取节点id
     *	参数：	
     *	返回值：string:id       节点id
     */
    function getTreeId(){
        return getTreeAttribute("id");
    }
    /*
     *	函数说明：删除发布
     *	参数：	
     *	返回值：
     */
    function delPublish(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_DEL_PUBLISH;

            p.setContent("id",treeID);

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
     *	函数说明：获取门户
     *	参数：	string:portalId         xform列名
                string:portalName       xform列名
     *	返回值：
     */
    function getPortal(portalId,portalName){
        var page1FormObj = $("page1Form");

        var action = "pms/portal!getActivePortals4Tree.action";
        //var action = "data/sitetree_init.xml";
        var params = {
            action:"portal"
        };

        var portal = window.showModalDialog("sitetree.htm",{params:params,title:"请选择门户",action:action},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=portal){            
            page1FormObj.updateDataExternal("pageId","");
            page1FormObj.updateDataExternal("pageCode","");
            page1FormObj.updateDataExternal("pageName","");
            page1FormObj.updateDataExternal("themeId","");

            page1FormObj.updateDataExternal(portalId,portal.portalId);
            page1FormObj.updateDataExternal(portalName,portal.name,true);
        }
    }
    /*
     *	函数说明：获取版面
     *	参数：	string:portalId         xform列名
                string:pageId           xform列名
                string:pageCode         xform列名
                string:pageName         xform列名
     *	返回值：
     */
    function getPage(portalId,pageId,pageCode,pageName){
        var page1FormObj = $("page1Form");
        var id = page1FormObj.getData(portalId);

        var page = window.showModalDialog("pagetree.htm",{title:"请选择版面",id:id},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=page){
            page1FormObj.updateDataExternal(pageId,page.id);
            page1FormObj.updateDataExternal(pageCode,page.code);
            page1FormObj.updateDataExternal(pageName,page.name);
        }
    }
    /*
     *	函数说明：获取门户对应主题列表
     *	参数：	string:cacheID              缓存数据id
                string:portalId             门户id
     *	返回值：
     */
    function updateThemeColumn(cacheID,portalId){
        var page1FormObj = $("page1Form");
        var portalId = page1FormObj.getData("portalId");

        var p = new HttpRequestParams();
        p.url = URL_GET_THEME_LIST;
        p.setContent("portalId",portalId);

        var request = new HttpRequest(p);
        request.onresult = function(){
            var column = this.getNodeValue(XML_THEME_LIST);
            var name = column.getAttribute("name");
            
            var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_PUBLISH_INFO);
            if(null!=xmlIsland){
                var oldColumn = xmlIsland.selectSingleNode(".//column[@name='"+name+"']");
                var attributes = column.attributes;
                for(var i=0,iLen=attributes.length;i<iLen;i++){
                    oldColumn.setAttribute(attributes[i].nodeName,attributes[i].nodeValue);
                }
                loadPublishInfoFormData(cacheID);
            }
        }
        request.send();
    }

    /*
     *	函数说明：静态发布门户，只发布当前页
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
    function staticIssue(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var p = new HttpRequestParams();
        p.url = URL_STATIC_ISSUE;
        var issueID = treeNode.getAttribute("id");
        p.setContent("id", issueID);
		p.setContent("type", 2);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
        }
        request.send();
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



    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();