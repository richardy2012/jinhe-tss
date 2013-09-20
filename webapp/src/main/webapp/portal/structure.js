    var _TEMP_CODE = new Date().getTime();
    /*
     *	后台响应数据节点名称
     */
    XML_DEFAULT_TOOLBAR = "DefaultToolBar";
    XML_TOOLBAR = "ToolBar";
    XML_MAIN_TREE = "SiteTree";

    XML_SITE_INFO = "SiteInfo";
    XML_PREVIEW = "html";
    XML_UPLOAD_INFO = "upload";
    XML_LAYOUT_PARAMETERS_INFO = "LayoutParameters";
    XML_DECORATOR_PARAMETERS_INFO = "DecoratorParameters";
    XML_PORTLET_PARAMETERS_INFO = "PortletParameters";
    XML_THEME_MANAGE = "ThemeManage";
    XML_CACHE_MANAGE = "CacheManage";
    XML_OPERATION = "Operation";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_VIEW_TREE_NODE_DETAIL = "viewTreeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_UPLOAD_DETAIL = "upload__id";
    CACHE_THEME_MANAGE = "themeManage__id";
    CACHE_CACHE_MANAGE = "cacheManage__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新增$label";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    OPERATION_SETTING = "设置\"$label\"";
    OPERATION_IMPORT = "门户导入";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/site_init.xml";
    URL_SITE_DETAIL = "data/site1.xml";
    URL_SAVE_SITE = "data/_success.xml";
    URL_DEL_SITE = "data/_success.xml";
    URL_STOP_SITE = "data/_success.xml";
    URL_START_SITE = "data/_success.xml";
    URL_MOVE_SITE = "data/_success.xml";
    URL_SORT_SITE = "data/_success.xml";
    URL_COPY_SITE = "data/copysite.xml";
    URL_COPY_SITE_TO = "data/copysite.xml";
    URL_VIEW_SITE = "portal!previewPortal.action";
    URL_GET_LAYOUT_PARAMETERS = "data/layoutparameters.xml";
    URL_GET_DECORATOR_PARAMETERS = "data/decoratorparameters.xml";
    URL_GET_PORTLET_PARAMETERS = "data/portletparameters.xml";
    URL_THEME_MANAGE = "data/thememanage.xml";
    URL_RENAME_THEME = "data/_success.xml";
    URL_DEL_THEME = "data/_success.xml";
    URL_COPY_THEME = "data/copytheme.xml";
    URL_PREVIEW_THEME = "data/_success.xml";
    URL_SET_DEFAULT_THEME = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_FLUSH_CACHE = "data/_success.xml";
    URL_CACHE_MANAGE = "data/cachemanage.xml";
    URL_IMPORT_SITE = "data/upload1.htm";
    URL_EXPORT_SITE = "data/download.zip";
    URL_UPLOAD_DETAIL = "data/importsite.xml";

//    URL_INIT = "pms/portal!getAllPortals4Tree.action";
//    URL_SITE_DETAIL = "pms/portal!getPortalStructureInfo.action";
//    URL_SAVE_SITE = "pms/portal!save.action";
//    URL_DEL_SITE = "pms/portal!delete.action";
//    URL_STOP_SITE = "pms/portal!disable.action";
//    URL_START_SITE = "pms/portal!disable.action";
//    URL_MOVE_SITE = "pms/portal!move.action";
//    URL_SORT_SITE = "pms/portal!order.action";
//    URL_COPY_SITE = "pms/portal!copyPortal.action";
//    URL_COPY_SITE_TO = "pms/portal!copyTo.action";
//    URL_VIEW_SITE = "portal!previewPortal.action";
//    URL_GET_LAYOUT_PARAMETERS = "pms/layout!getDefaultParams4Xml.action";
//    URL_GET_DECORATOR_PARAMETERS = "pms/decorator!getDefaultParams4Xml.action";
//    URL_GET_PORTLET_PARAMETERS = "pms/portlet!getDefaultParams4Xml.action";
//    URL_THEME_MANAGE = "pms/portal!getThemes4Tree.action";
//    URL_RENAME_THEME = "pms/portal!renameTheme.action";
//    URL_DEL_THEME =  "pms/portal!removeTheme.action";
//    URL_COPY_THEME = "pms/portal!saveThemeAs.action";
//    URL_PREVIEW_THEME = "portal!previewPortal.action";
//    URL_SET_DEFAULT_THEME = "pms/portal!specifyDefaultTheme.action";
//    URL_GET_OPERATION = "pms/portal!getOperationsByResource.action";
//    URL_FLUSH_CACHE = "pms/portal!flushCache.action";
//    URL_CACHE_MANAGE = "pms/portal!cacheManage.action";
//    URL_IMPORT_SITE = "pms/portal!importPortal.action";
//    URL_EXPORT_SITE = "pms/portal!exportPortal.action";
//    URL_UPLOAD_DETAIL = "data/importsite.xml";


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
        initNaviBar("pms.1");
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

            //站点管理
            str[str.length] = "    <button id=\"b1\" code=\"7\" icon=\"" + ICON + "start.gif\" label=\"启用\" cmd=\"startSite()\" enable=\"'1'==getSiteState()\"/>";
            str[str.length] = "    <button id=\"b2\" code=\"6\" icon=\"" + ICON + "stop.gif\" label=\"停用\" cmd=\"stopSite()\" enable=\"'0'==getSiteState()\"/>";
            str[str.length] = "    <button id=\"b3\" code=\"1\" icon=\"" + ICON + "view.gif\" label=\"查看\" cmd=\"editSiteInfo(false)\" enable=\"'_rootId'!=getSiteId()\"/>";
            str[str.length] = "    <button id=\"b4\" code=\"2\" icon=\"" + ICON + "edit.gif\" label=\"编辑\" cmd=\"editSiteInfo()\" enable=\"'_rootId'!=getSiteId()\"/>";
            str[str.length] = "    <button id=\"b5\" code=\"3\" icon=\"" + ICON + "del.gif\" label=\"删除\" cmd=\"delSite()\" enable=\"'_rootId'!=getSiteId()\"/>";
            str[str.length] = "    <button id=\"b6\" code=\"p_4\" icon=\"" + ICON + "copy.gif\" label=\"复制\" cmd=\"copySite()\" enable=\"'_rootId'!=getSiteId()\"/>";
            str[str.length] = "    <button id=\"b7\" code=\"1\" icon=\"" + ICON + "copy_to.gif\" label=\"复制到...\" cmd=\"copySiteTo()\" enable=\"'0'!=getSiteType() &amp;&amp; '_rootId'!=getSiteId()\"/>";
            str[str.length] = "    <button id=\"b8\" code=\"3\" icon=\"" + ICON + "move.gif\" label=\"移动到...\" cmd=\"moveSiteTo()\" enable=\"'0'!=getSiteType() &amp;&amp; '_rootId'!=getSiteId()\"/>";
            str[str.length] = "    <button id=\"b9\" code=\"4\" icon=\"" + ICON + "new_site.gif\" label=\"新建门户\" cmd=\"addNewSite('0')\" enable=\"'_rootId'==getSiteId()\"/>";
            str[str.length] = "    <button id=\"b10\" code=\"4\" icon=\"" + ICON + "new_page.gif\" label=\"新建页面\" cmd=\"addNewSite('1')\" enable=\"'0'==getSiteType()\"/>";
            str[str.length] = "    <button id=\"b11\" code=\"4\" icon=\"" + ICON + "new_section.gif\" label=\"新建版面\" cmd=\"addNewSite('2')\" enable=\"'3'!=getSiteType() &amp;&amp; '0'!=getSiteType() &amp;&amp; '_rootId'!=getSiteId()\"/>";
            str[str.length] = "    <button id=\"b12\" code=\"4\" icon=\"" + ICON + "new_portlet.gif\" label=\"新建portlet实例\" cmd=\"addNewSite('3')\" enable=\"'1'==getSiteType() || '2'==getSiteType()\"/>";
            str[str.length] = "    <button id=\"b14\" code=\"2\" icon=\"" + ICON + "preview.gif\" label=\"预览\" cmd=\"preview()\" enable=\"'_rootId'!=getSiteId(); \"/>";
            str[str.length] = "    <button id=\"b15\" code=\"2\" icon=\"" + ICON + "theme.gif\" label=\"主题管理\" cmd=\"themeManage()\" enable=\"'0'==getSiteType()\"/>";
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
            label:"新建门户",
            callback:function(){addNewSite("0");},
            enable:function(){return true;},
            visible:function(){return "_rootId"==getSiteId() && true==getOperation("4");}
        }
        var item2 = {
            label:"新建页面",
            callback:function(){addNewSite("1");},
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("4");}
        }
        var item3 = {
            label:"新建版面",
            callback:function(){addNewSite("2");},
            enable:function(){return true;},
            visible:function(){return "3"!=getSiteType() && "0"!=getSiteType() && "_rootId"!=getSiteId() && true==getOperation("4");}
        }
        var item4 = {
            label:"新建portlet实例",
            callback:function(){addNewSite("3");},
            enable:function(){return true;},
            visible:function(){return ("1"==getSiteType() || "2"==getSiteType()) && true==getOperation("4");}
        }
        var item6 = {
            label:"复制",
            callback:copySite,
            icon:ICON + "copy.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getSiteId() && true==getOperation("p_4");}
        }
        var item7 = {
            label:"删除",
            callback:delSite,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getSiteId() && true==getOperation("3");}
        }
        var item8 = {
            label:"编辑",
            callback:editSiteInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getSiteId() && true==getOperation("2");}
        }
        var item9 = {
            label:"停用",
            callback:stopSite,
            icon:ICON + "stop.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getSiteState() && true==getOperation("6");}
        }
        var item10 = {
            label:"启用",
            callback:startSite,
            icon:ICON + "start.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getSiteState() && true==getOperation("7");}
        }
        var item11 = {
            label:"移动到...",
            callback:moveSiteTo,
            icon:ICON + "move.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getSiteType() && "_rootId"!=getSiteId() && true==getOperation("3");}
        }
        var item12 = {
            label:"预览",
            callback:preview,
            icon:ICON + "preview.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getSiteId()  && true==getOperation("2");}
        }
        var item13 = {
            label:"主题管理",
            callback:themeManage,
            icon:ICON + "theme.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("2");}
        }
        var item14 = {
            label:"复制到...",
            callback:copySiteTo,
            icon:ICON + "copy_to.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getSiteType() && "_rootId"!=getSiteId() && true==getOperation("1");}
        }
         var item15 = {
            label:"缓存管理",
            callback:cacheManage,
            icon:ICON + "cache.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("1");}
        }
        var item16 = {
            label:"查看",
            callback:function(){
                editSiteInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getSiteId() && true==getOperation("1");}
        }
        var item17 = {
            label:"资源管理",
            callback:function(){resourceManage();},
            icon:ICON + "resource.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("2");}
        }
        var item18 = {
            label:"启用门户",
            callback:startSite,
            icon:ICON + "start.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && "1"==getSiteState() && true==getOperation("6");}
        }
        var item19 = {
            label:"门户静态发布",
            callback:staticIssue,
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("2");}
        }
        var item20 = {
            label:"门户导出",
            callback:exportSite,
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("2");}
        }
        var item21 = {
            label:"门户导入",
            callback:importSite,
            enable:function(){return true;},
            visible:function(){return "_rootId"==getSiteId() && true==getOperation("4");}
        }
        var item22 = {
            label:"查看页面流量",
            callback:showPageView,
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("1");}
        }
        var item23 = {
            label:"授予角色",
            callback:setPortalPermission,
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getSiteId() && true==getOperation("2");}
        }
		var item24 = {
            label:"远程发布",
            callback:function(){
               remoteIssue("0");
            },
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("2");}
        }
		var item25 = {
            label:"远程发布(完全覆盖)",
			callback:function(){
                remoteIssue("1");
            },
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("2");}
        }
	    var item26 = {
            label:"页面静态发布",
            callback:staticIssueOnePage,
            enable:function(){return true;},
            visible:function(){return "0"==getSiteType() && true==getOperation("2");}
        }

        var treeObj = $("tree");

        var menu1 = new Menu();
        menu1.addItem(item9);
        menu1.addItem(item10);
        menu1.addItem(item18);
        menu1.addSeparator();
        menu1.addItem(item23);
        menu1.addSeparator();
        menu1.addItem(item12);
        menu1.addItem(item16);
        menu1.addItem(item8);
        menu1.addItem(item7);
        menu1.addItem(item6);
        menu1.addItem(item14);
        menu1.addItem(item11);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addItem(item4);
//      menu1.addItem(item5);
        menu1.addSeparator();
        menu1.addItem(item13);
        menu1.addItem(item15);
        menu1.addItem(item17);
        menu1.addSeparator();
        menu1.addItem(item19);
		menu1.addItem(item26);
		menu1.addItem(item24);
		menu1.addItem(item25);
//      menu1.addItem(item20);
//      menu1.addItem(item21);
        menu1.addItem(item22);

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
        if(null!=block && "_rootId"!=rowID){
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
            treeObj.onTreeNodeMoved = function(eventObj){
                onTreeNodeMoved(eventObj);
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
        var id = getSiteId();
        getTreeOperation(treeNode,function(_operation){
            var canAddNewSite = checkOperation("1",_operation);
            var canView = checkOperation("1",_operation);
            var canEdit = checkOperation("2",_operation);
            if("_rootId"!=id){
                if(true == canEdit){
                    editSiteInfo();
                }else if(true == canView){
                    editSiteInfo(false);                
                }
            }
        });
    }
    /*
     *	函数说明：拖动树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeMoved(eventObj){
        sortSiteTo(eventObj);
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
     *	函数说明：编辑门户信息
     *	参数：  boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editSiteInfo(editable){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var treeType = treeNode.getAttribute("type");

            var portalNode = treeNode;
            if("0"!=treeType){//如果不是门户节点，则取父节点
                portalNode = treeNode.getParent();
            }
            var portalName = portalNode.getAttribute("code");
            var portalID = portalNode.getAttribute("portalId");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadTreeDetailData(treeID,editable,treeID,treeType,portalID,portalName);
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
                string:parentID             父节点id
                string:treeType             树节点类型(菜单，布局等)
                string:portalID             门户id
                string:portalName           门户名
                boolean:isNew               是否新增
     *	返回值：
     */
    function loadTreeDetailData(treeID,editable,parentID,treeType,portalID,portalName,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_TREE_NODE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_SITE_DETAIL;

            //如果是新增
            if(true==isNew){
                p.setContent("isNew", 1);
                p.setContent("type", treeType);
                //如果新增的是门户
                if(treeType == "0"){
                    p.setContent("parentId", "0");
                }else{
                    p.setContent("parentId", parentID);
                    p.setContent("portalId", portalID);
                }
            }else{
                p.setContent("id", treeID);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var siteInfoNode = this.getNodeValue(XML_SITE_INFO);

                //根据树节点type属性，预先处理xform数据岛
                preProcessXml(siteInfoNode,treeType);

                var siteInfoNodeID = cacheID+"."+XML_SITE_INFO;

                Cache.XmlIslands.add(siteInfoNodeID,siteInfoNode);

                Cache.Variables.add(cacheID,[siteInfoNodeID]);

                initSitePages(cacheID,editable,portalID,portalName,isNew,parentID);
            }
            request.send();
        }else{
            initSitePages(cacheID,editable,portalID,portalName,isNew,parentID);
        }
    }
    /*
     *	函数说明：门户相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                string:portalID             门户id
                string:portalName           门户名
                boolean:isNew               是否新增
     *	返回值：
     */
    function initSitePages(cacheID,editable,portalID,portalName,isNew,parentID){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadSiteInfoFormData(cacheID,editable);
            page1FormObj.portalID = portalID;
            page1FormObj.portalName = portalName;
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page1BtNextObj = $("page1BtNext");
        page1BtPrevObj.style.display = "none";
        page1BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = function(){
            saveSite(cacheID,parentID,isNew);
        }
    }
    /*
     *	函数说明：门户信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadSiteInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_SITE_INFO);
        if(null!=xmlIsland){

            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	函数说明：保存门户
     *	参数：	string:cacheID          缓存数据id
                string:parentID         父节点id
                boolean:isNew           是否新增
     *	返回值：
     */
    function saveSite(cacheID,parentID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_SITE;

        //是否提交
        var flag = false;
        var siteCache = Cache.Variables.get(cacheID);
        if(null!=siteCache){       
            //门户基本信息
            var siteInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_SITE_INFO);
            if(null!=siteInfoNode){
                var siteInfoDataNode = siteInfoNode.selectSingleNode(".//data");
                if(null!=siteInfoDataNode){
                    siteInfoDataNode = siteInfoDataNode.cloneNode(true);

                    var rowNode = siteInfoDataNode.selectSingleNode("row");

                    //门户、页面节点需要拼接supplement属性
                    var type = siteInfoNode.getAttribute("type");
                    if("0"==type || "1"==type){
                        //将css,js部分拼合成一个xml文档
                        var name = rowNode.getCDATA("name")||"";
                        var js = rowNode.getCDATA("js")||"";
                        var jsCode = rowNode.getCDATA("jsCode")||"";
                        var css = rowNode.getCDATA("css")||"";
                        var cssCode = rowNode.getCDATA("cssCode")||"";
                        var rootName = ("0"==type?"portal":"page");

                        var str = [];
                        //str[str.length] = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
                        str[str.length] = "<" + rootName + ">";
                        str[str.length] = "<property>";
                        str[str.length] = "<name>" + name + "</name>";
                        str[str.length] = "<description>";
                        str[str.length] = "<![CDATA[]]>";
                        str[str.length] = "</description>";
                        str[str.length] = "</property>";
                        str[str.length] = "<script>";
                        str[str.length] = "<file>";
                        str[str.length] = "<![CDATA[" + js + "]]>";
                        str[str.length] = "</file>";
                        str[str.length] = "<code>";
                        str[str.length] = "<![CDATA[" + jsCode + "]]>";
                        str[str.length] = "</code>";
                        str[str.length] = "</"+"script>";
                        str[str.length] = "<style>";
                        str[str.length] = "<file>";
                        str[str.length] = "<![CDATA[" + css + "]]>";
                        str[str.length] = "</file>";
                        str[str.length] = "<code>";
                        str[str.length] = "<![CDATA[" + cssCode + "]]>";
                        str[str.length] = "</code>";
                        str[str.length] = "</style>";
                        str[str.length] = "</" + rootName + ">";

                        rowNode.setCDATA("supplement",str.join(""));
                        rowNode.removeCDATA("js");
                        rowNode.removeCDATA("jsCode");
                        rowNode.removeCDATA("css");
                        rowNode.removeCDATA("cssCode");

                    }else{
                        rowNode.removeCDATA("supplement");
                        rowNode.removeCDATA("js");
                        rowNode.removeCDATA("jsCode");
                        rowNode.removeCDATA("css");
                        rowNode.removeCDATA("cssCode");
                    }

                    flag = true;

                    var prefix = siteInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(siteInfoDataNode,prefix);

                    p.setContent("code", _TEMP_CODE);

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
                    appendTreeNode(parentID,treeNode);

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
     *	函数说明：新建门户
     *	参数：string:treeType   树节点类型(菜单，布局等)
     *	返回值：
     */
    function addNewSite(treeType){
        var treeName;
        var treeID = new Date().valueOf();
        switch(treeType){
            case "0":
              treeName = "门户";
              break;
            case "1":
              treeName = "页面";
              break;
            case "2":
              treeName = "版面";
              break;
            case "3":
              treeName = "portlet实例";
              break;
        }

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var parentID = treeNode.getId();
            var portalName = treeNode.getAttribute("code");
            var portalID = treeNode.getAttribute("portalId");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadTreeDetailData(treeID,true,parentID,treeType,portalID,portalName,true);
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
    }
    /*
     *	函数说明：删除门户节点
     *	参数：	
     *	返回值：
     */
    function delSite(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_DEL_SITE;

            p.setContent("id",treeID);
            p.setContent("deleted","1");

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
     *	函数说明：获取门户节点状态
     *	参数：	
     *	返回值：
     */
    function getSiteState(){
        var treeNodeState = null;
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            treeNodeState = treeNode.getAttribute("disabled");
        }
        return treeNodeState;   
    }
    /*
     *	函数说明：停用门户节点
     *	参数：	
     *	返回值：
     */
    function stopSite(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var type = treeNode.getAttribute("type");

            var p = new HttpRequestParams();
            p.url = URL_STOP_SITE;
            p.setContent("id",id);
            p.setContent("disabled","1");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置当前节点状态
                var xmlNode = new XmlNode(treeNode.node);
                refreshTreeNodeState(xmlNode,"1");

                //一律下溯
                var childs = xmlNode.selectNodes(".//treeNode");
                for(var i=0,iLen=childs.length;i<iLen;i++){
                    refreshTreeNodeState(childs[i],"1");
                }

                treeObj.reload();

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：启用门户节点
     *	参数：	
     *	返回值：
     */
    function startSite(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var type = treeNode.getAttribute("type");

            var p = new HttpRequestParams();
            p.url = URL_START_SITE;
            p.setContent("id",id);
            p.setContent("disabled","0");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置当前节点状态
                var xmlNode = new XmlNode(treeNode.node);
                refreshTreeNodeState(xmlNode,"0");

                //根据当前节点类型，确定上下溯方式
                if("0"==type){//门户一律下溯
                    var childs = xmlNode.selectNodes(".//treeNode");
                    for(var i=0,iLen=childs.length;i<iLen;i++){
                        refreshTreeNodeState(childs[i],"0");
                    }
                }else{//其他节点上溯
                    var parentNode = xmlNode.getParent();
                    while("_rootId"!=parentNode.getAttribute("id")){
                        refreshTreeNodeState(parentNode,"0");
                        parentNode = parentNode.getParent();
                    }
                }

                treeObj.reload();

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：移动门户节点
     *	参数：	
     *	返回值：
     */
    function moveSiteTo(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var name = treeNode.getName();
            var portalId = treeNode.getAttribute("portalId");

            
            var action = "pms/portal!getActivePortalStructures4Tree.action";
            var params = {
                id:id,
                action:"moveTo"
            };

            var site = window.showModalDialog("sitetree.htm",{params:params,title:"将\""+name+"\"移动到",action:action},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=site){
                var p = new HttpRequestParams();
                p.url = URL_MOVE_SITE;
                p.setContent("targetId",site.id);
                p.setContent("id",id);
                p.setContent("portalId",site.portalId);

                var request = new HttpRequest(p);
                request.onsuccess = function(){
                    //移动树节点
                    var curNode = treeObj.getTreeNodeById(id);
                    var xmlNode = new XmlNode(curNode.node);
                    curNode.setAttribute("portalId",site.portalId);
                    var parentNode = treeObj.getTreeNodeById(site.id);
                    parentNode.node.appendChild(curNode.node);
                    parentNode.node.setAttribute("_open","true");

                    //如果目标节点停用,则当前节点也要停用
                    if("1"==parentNode.getAttribute("disabled")){

                        //设置当前节点状态
                        refreshTreeNodeState(xmlNode,"1");

                        //下溯
                        var childs = xmlNode.selectNodes(".//treeNode");
                        for(var i=0,iLen=childs.length;i<iLen;i++){
                            refreshTreeNodeState(childs[i],"1");
                        }
                    }

                    clearOperation(xmlNode);

                    treeObj.reload();
                }
                request.send();
            }
        }
    }
    /*
     *	函数说明：同一父节点下移动门户节点
     *	参数：	
     *	返回值：
     */
    function sortSiteTo(eventObj){
        var treeObj = $("tree");
        var movedTreeNode = eventObj.movedTreeNode;
        var toTreeNode = eventObj.toTreeNode;
        var moveState = eventObj.moveState;

        var p = new HttpRequestParams();
        p.url = URL_SORT_SITE;
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
     *	函数说明：根据树节点type属性，预先处理xform数据岛
     *	参数：	XmlNode:xmlIsland   XmlNode对象实例
                string:treeType     树节点type类型
     *	返回值：
     */
    function preProcessXml(xmlIsland,treeType){
        //在根节点上加type属性，用于saveSite时判断
        xmlIsland.setAttribute("type",treeType);

        //清除有showType属性，但与当前treeType不匹配的节点
        var showTypeNodes = xmlIsland.selectNodes(".//*[@showType]");
        for(var i=0,iLen=showTypeNodes.length;i<iLen;i++){
            var curNode = showTypeNodes[i];
            var showType = curNode.getAttribute("showType").split(",");
            var flag = true;
            for(var j=0,jLen=showType.length;j<jLen;j++){
                if(treeType==showType[j]){
                    flag = false;
                    break;
                }
            }
            if(true==flag){
                curNode.removeNode();			
            }
        }

        //控制配置按钮可见性
        var rowNode = xmlIsland.selectSingleNode(".//row");
        var definerName = rowNode.getCDATA("definerName")||"";
        var decoratorName = rowNode.getCDATA("decoratorName")||"";

        var page1BtConfigDefinerNode = xmlIsland.selectSingleNode(".//*[@id='page1BtConfigDefiner']");
        var page1BtConfigDecoratorNode = xmlIsland.selectSingleNode(".//*[@id='page1BtConfigDecorator']");

        var parameters = rowNode.getCDATA("parameters")||"";
        if(""!=parameters){
            var xmlReader = new XmlReader(parameters);
            var xmlNode = new XmlNode(xmlReader.documentElement);
            var portletParams = xmlNode.selectSingleNode("portlet/@*");
            var layoutParams = xmlNode.selectSingleNode("layout/@*");
            var decoratorParams = xmlNode.selectSingleNode("decorator/@*");

            if(null!=page1BtConfigDefinerNode){
                switch(treeType){
                    case "0":
                    case "1":
                    case "2":
                        if(null==layoutParams){
                            page1BtConfigDefinerNode.setAttribute("disabled","true");                
                        }
                        break;
                    case "3":
                        if(null==portletParams){
                            page1BtConfigDefinerNode.setAttribute("disabled","true");
                        }
                        break;
                }
            }
            if(null!=page1BtConfigDecoratorNode && (""==decoratorName || null==decoratorParams)){
                page1BtConfigDecoratorNode.setAttribute("disabled","true");
            }
        }else{
            if(null!=page1BtConfigDefinerNode){
                page1BtConfigDefinerNode.setAttribute("disabled","true"); 
            }
            if(null!=page1BtConfigDecoratorNode){
                page1BtConfigDecoratorNode.setAttribute("disabled","true");
            }
        
        }


        var definerNode = xmlIsland.selectSingleNode(".//column[@name='definerName']");
        var decoratorNode = xmlIsland.selectSingleNode(".//column[@name='decoratorName']");
        var rowNode = xmlIsland.selectSingleNode(".//data/row");

        //根据treeType，给definerNode,decoratorNode节点设置不同属性
        var layoutCmd = definerNode.getAttribute("cmd");
        definerNode.setAttribute("cmd",layoutCmd.replace(/\${definerType}/i,treeType));
        switch(treeType){
            case "0":
            case "1":
            case "2":
                definerNode.setAttribute("caption","布局");
                decoratorNode.setAttribute("caption","修饰");
                break;
            case "3":
                definerNode.setAttribute("caption","Portlet");
                decoratorNode.setAttribute("caption","修饰");
                break;
        }

        //门户、页面类型节点需要预解析supplement属性
        switch(treeType){
            case "0":
            case "1":
                //预解析supplement，分别设置到js,css,jsCode和cssCode上
                var supplement = rowNode.getCDATA("supplement");
                if(null==supplement){
                    supplement = "";
                }

                var xmlReader = new XmlReader(supplement);

                if(null!=xmlReader.documentElement){
                    var supplementNode = new XmlNode(xmlReader.documentElement);
                    var jsNode = supplementNode.selectSingleNode("./script/file/node()");
                    var cssNode = supplementNode.selectSingleNode("./style/file/node()");
                    var jsCodeNode = supplementNode.selectSingleNode("./script/code/node()");
                    var cssCodeNode = supplementNode.selectSingleNode("./style/code/node()");

                    if(null!=jsNode){
                        var js = jsNode.nodeValue;
                        rowNode.setCDATA("js",js);
                    }
                    if(null!=cssNode){
                        var css = cssNode.nodeValue;
                        rowNode.setCDATA("css",css);
                    }
                    if(null!=jsCodeNode){
                        var jsCode = jsCodeNode.nodeValue;
                        rowNode.setCDATA("jsCode",jsCode);
                    }
                    if(null!=cssCodeNode){
                        var cssCode = cssCodeNode.nodeValue;
                        rowNode.setCDATA("cssCode",cssCode);
                    }
                }
                rowNode.removeCDATA("supplement");

                break;
            case "2":
            case "3":
                break;
        }

    }
    /*
     *	函数说明：复制门户节点
     *	参数：	
     *	返回值：
     */
    function copySite(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_COPY_SITE;

            p.setContent("id",id);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var siteNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                var parentNode = treeNode.getParent();

                treeObj.insertTreeNodeXml(siteNode.toXml(),parentNode);
                
            }
            request.send();
        }
    }
    /*
     *	函数说明：复制门户节点到
     *	参数：	
     *	返回值：
     */
    function copySiteTo(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var name = treeNode.getName();
            var portalId = treeNode.getAttribute("portalId");

            var action = "pms/portal!getActivePortalStructures4Tree.action";
            var params = {
                id:id,
                action:"copyTo"
            };

            var site = window.showModalDialog("sitetree.htm",{params:params,title:"将\""+name+"\"复制到",action:action},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=site){

                var p = new HttpRequestParams();
                p.url = URL_COPY_SITE_TO;

                p.setContent("targetId",site.id);
                p.setContent("id",id);
                p.setContent("portalId",site.portalId);

                var request = new HttpRequest(p);
                request.onresult = function(){
                    var siteNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    var parentNode = treeObj.getTreeNodeById(site.id);
                    treeObj.insertTreeNodeXml(siteNode.toXml(),parentNode);
                }
                request.send();
            }
        }
    }
    /*
     *	函数说明：获取节点类型
     *	参数：	
     *	返回值：string:siteType       节点类型
     */
    function getSiteType(){
        var siteType = null;    
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            siteType = treeNode.getAttribute("type");
        }
        return siteType;
    }
    /*
     *	函数说明：获取节点id
     *	参数：	
     *	返回值：string:id       节点id
     */
    function getSiteId(){
        var id = null;    
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            id = treeNode.getId();
        }
        return id;
    }
    /*
     *	函数说明：预览
     *	参数：	
     *	返回值：
     */
    function preview(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeNodePortalID = treeNode.getAttribute("portalId");
            var url	= URL_VIEW_SITE + "?portalId=" + treeNodePortalID;
            var siteType = treeNode.getAttribute("type");
            if("0"!=siteType){
                var treeNodeID = treeNode.getId();
                url += "&id=" + treeNodeID;
            }
            window.open(url);
        }
    }
    /*
     *	函数说明：获取缓存
     *	参数：	string:cachePolicyId      xform列名
                string:cachePolicyName    xform列名
     *	返回值：
     */
    function getCachePolicy(cachePolicyId,cachePolicyName){
        var page1FormObj = $("page1Form");
        page1FormObj.updateDataExternal(cachePolicyId,"12");
        page1FormObj.updateDataExternal(cachePolicyName,"测试缓存策略");    
    }
    /*
     *	函数说明：根据definerType决定要执行的方法
     *	参数：	string:definerType       节点类型
                string:definerId         xform列名
                string:definerName       xform列名
                string:parametersName    xform列名
     *	返回值：
     */
    function getDefiner(definerType,definerId,definerName,parametersName){
        switch(definerType){
            case "0":
            case "1":
            case "2":
                getLayout(definerId,definerName,parametersName);
                break;
            case "3":
                getPortlet(definerId,definerName,parametersName);
                break;
        }
    }
    /*
     *	函数说明：获取布局器
     *	参数：	string:definerId         xform列名
                string:definerName       xform列名
                string:parametersName    xform列名
     *	返回值：
     */
    function getLayout(definerId,definerName,parametersName){
        var page1FormObj = $("page1Form");

        var layout = window.showModalDialog("layouttree.htm",{title:"请选择布局器"},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=layout){
            page1FormObj.updateDataExternal(definerId,layout.id);
            page1FormObj.updateDataExternal(definerName,layout.name);

            //加载布局器配置项
            loadLayoutParameters(layout.id,parametersName);
        }
    }
    /*
     *	函数说明：获取修饰器
     *	参数：	string:decoratorId      xform列名
                string:decoratorName    xform列名
                string:parametersName    xform列名
     *	返回值：
     */
    function getDecorator(decoratorId,decoratorName,parametersName){
        var page1FormObj = $("page1Form");

        var decorator = window.showModalDialog("decoratortree.htm",{title:"请选择修饰器"},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=decorator){
            page1FormObj.updateDataExternal(decoratorId,decorator.id);
            page1FormObj.updateDataExternal(decoratorName,decorator.name);

            //加载修饰器配置项
            loadDecoratorParameters(decorator.id,parametersName);
        } 
    }
    /*
     *	函数说明：获取portlet
     *	参数：	string:definerId         xform列名
                string:definerName       xform列名
                string:parametersName    xform列名
     *	返回值：
     */
    function getPortlet(definerId,definerName,parametersName){
        var page1FormObj = $("page1Form");

        var portlet = window.showModalDialog("portlettree.htm",{title:"请选择Portlet"},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=portlet){
            page1FormObj.updateDataExternal(definerId,portlet.id);
            page1FormObj.updateDataExternal(definerName,portlet.name);

            //加载portlet配置项
            loadPortletParameters(portlet.id,parametersName);
        }
    }
    /*
     *	函数说明：获取js
     *	参数：	string:name      xform列名
     *	返回值：
     */
    function getJs(name){
        var page1FormObj = $("page1Form");
        var id = page1FormObj.portalID;
        var code = page1FormObj.code;
        
        var newFiles = window.showModalDialog("remotefiles.htm",{id:id,code:code,tempCode:_TEMP_CODE,type:"js",title:"请选择或者上传新的js文件"},"dialogWidth:400px;dialogHeight:400px;");
        if(null!=newFiles){
            page1FormObj.updateDataExternal(name,newFiles);
        }
    }
    /*
     *	函数说明：获取css
     *	参数：	string:name      xform列名
     *	返回值：
     */
    function getCss(name){
        var page1FormObj = $("page1Form");
        var id = page1FormObj.portalID;
        var code = page1FormObj.code;
        
        var newFiles = window.showModalDialog("remotefiles.htm",{id:id,code:code,tempCode:_TEMP_CODE,type:"css",title:"请选择或者上传新的css文件"},"dialogWidth:400px;dialogHeight:400px;");
        if(null!=newFiles){
            page1FormObj.updateDataExternal(name,newFiles);
        }
    }
    /*
     *	函数说明：布局器加载数据
     *	参数：	string:layoutID         布局器id
                string:parametersName   xform列名
     *	返回值：
     */
    function loadLayoutParameters(layoutID,parametersName){
        var page1FormObj = $("page1Form");
        var parameters = page1FormObj.getData(parametersName);

        var p = new HttpRequestParams();
        p.url = URL_GET_LAYOUT_PARAMETERS;
        p.setContent("layoutId",layoutID);

        var request = new HttpRequest(p);
        request.onresult = function(){
            var layoutParametersNode = this.getNodeValue(XML_LAYOUT_PARAMETERS_INFO);

            updateParameters(parametersName,layoutParametersNode);
            
            //允许进行配置
            var page1BtConfigDefinerObj = $("page1BtConfigDefiner");
            page1BtConfigDefinerObj.disabled = 0==layoutParametersNode.attributes.length;
        }
        request.send();
    }
    /*
     *	函数说明：修饰器加载数据
     *	参数：	string:decoratorID      修饰器id
                string:parametersName   xform列名
     *	返回值：
     */
    function loadDecoratorParameters(decoratorID,parametersName){
        var page1FormObj = $("page1Form");
        var parameters = page1FormObj.getData(parametersName);

        var p = new HttpRequestParams();
        p.url = URL_GET_DECORATOR_PARAMETERS;
        p.setContent("decoratorId",decoratorID);

        var request = new HttpRequest(p);
        request.onresult = function(){
            var decoratorParametersNode = this.getNodeValue(XML_DECORATOR_PARAMETERS_INFO);

            updateParameters(parametersName,decoratorParametersNode);
            
            //允许进行配置
            var page1BtConfigDecoratorObj = $("page1BtConfigDecorator");
            page1BtConfigDecoratorObj.disabled = 0==decoratorParametersNode.attributes.length;
        }
        request.send();
    }
    /*
     *	函数说明：portlet加载数据
     *	参数：	string:portletID      修饰器id
                string:parametersName   xform列名
     *	返回值：
     */
    function loadPortletParameters(portletID,parametersName){
        var page1FormObj = $("page1Form");
        var parameters = page1FormObj.getData(parametersName);

        var p = new HttpRequestParams();
        p.url = URL_GET_PORTLET_PARAMETERS;
        p.setContent("id", portletID);

        var request = new HttpRequest(p);
        request.onresult = function(){
            var portletParametersNode = this.getNodeValue(XML_PORTLET_PARAMETERS_INFO);

            updateParameters(parametersName,portletParametersNode);
            
            //允许进行配置
            var page1BtConfigDefinerObj = $("page1BtConfigDefiner");
            page1BtConfigDefinerObj.disabled = 0==portletParametersNode.attributes.length;
        }
        request.send();
    }
    /*
     *	函数说明：将parameters字符串解析为xml对象
     *	参数：	string:parameters       xml字符串
     *	返回值：XmlNode:xmlNode         XmlNode实例
     */
    function parseParameters(parameters){
        //将parameters字符串解析为xml对象
        var xmlReader = new XmlReader();
        xmlReader.loadXML(parameters);
        if(null==xmlReader.documentElement){
            xmlReader.loadXML("<params/>");
        }
        var xmlNode = new XmlNode(xmlReader.documentElement);
        return xmlNode;    
    }
    /*
     *	函数说明：更新布局器、修饰器的配置参数节点
     *	参数：	string:parametersName   xform列名
                XmlNode:newNode         XmlNode实例
     *	返回值：
     */
    function updateParameters(parametersName,newNode){
        var page1FormObj = $("page1Form");
        var parameters = page1FormObj.getData(parametersName)||"";

        var xmlNode = parseParameters(parameters);
        var type = newNode.nodeName;
        var oldNode = xmlNode.selectSingleNode("./"+type);
        if(null!=oldNode){
            var attributes = oldNode.attributes;
            for(var i=0,iLen=attributes.length;i<iLen;i++){
               oldNode.removeAttribute(attributes[0].nodeName);
            }

            var attributes = newNode.attributes;
            for(var i=0,iLen=attributes.length;i<iLen;i++){
               oldNode.setAttribute(attributes[i].nodeName,attributes[i].nodeValue);
            }

            if(null!=oldNode.firstChild){
                var oldText = new XmlNode(oldNode.firstChild);
                oldText.removeNode();
            }
            if(null!=newNode.firstChild){
                var newText = new XmlNode(newNode.firstChild);
                oldNode.appendChild(newText);
            }
        }else{
            xmlNode.appendChild(newNode);
        }

        //更新xform中的parameters值
        page1FormObj.updateDataExternal(parametersName,xmlNode.toXml());
    }
    /*
     *	函数说明：更改布局器、修饰器、portlet的配置
     *	参数：	string:paramsType       类型(布局器、修饰器、portlet)
                string:id               xform列名
                string:name             xform列名
                string:parametersName   xform列名
     *	返回值：
     */
    function configParams(paramsType,id,name,parametersName){
        var page1FormObj = $("page1Form");
        var nameValue = page1FormObj.getData(name)||"";
        var idValue = page1FormObj.getData(id)||"";
        var parameters = page1FormObj.getData(parametersName)||"";

        var xmlNode = parseParameters(parameters);
        var oldParamsNode = xmlNode.selectSingleNode("./"+paramsType);
        var oldText = new XmlNode(oldParamsNode.firstChild);
        if(""!=nameValue && ""!=idValue){
            var newParams = window.showModalDialog("configparams.htm",{id:idValue,params:oldParamsNode,type:paramsType,title:"更改\""+nameValue+"\"的配置"},"dialogWidth:250px;dialogHeight:250px;");
            if(null!=newParams){
                var rowReader = new XmlReader(newParams);
                var rowNode = new XmlNode(rowReader.documentElement);

                var newParamsReader = new XmlReader("<"+paramsType+"/>");
                var newParamsNode = new XmlNode(newParamsReader.documentElement);
                var newText = oldText.cloneNode(true);

                //从row节点复制到新参数节点
                var childs = rowNode.selectNodes("*");
                for(var i=0,iLen=childs.length;i<iLen;i++){
                    var name = childs[i].nodeName;
                    var value = childs[i].text;
                    newParamsNode.setAttribute(name,value);
                }
                newParamsNode.appendChild(newText);
                updateParameters(parametersName,newParamsNode);
            }
        }
    }
    /*
     *	函数说明：刷新树节点停用启用状态
     *	参数：	XmlNode:xmlNode         XmlNode实例
                string:state            停/启用状态
     *	返回值：
     */
    function refreshTreeNodeState(xmlNode,state){
        var type = xmlNode.getAttribute("type");
        var img = {
            "0":"portal",
            "1":"page",
            "2":"section",
            "3":"portlet_instance"
        }
        xmlNode.setAttribute("disabled",state);
        xmlNode.setAttribute("icon",ICON + img[type]+(state=="1"?"_2":"") + ".gif");       
    }
    /*
     *	函数说明：主题管理
     *	参数：	
     *	返回值：
     */
    function themeManage(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var portalId = treeNode.getAttribute("portalId");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadThemeManageData(treeID, portalId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page2";
            inf.label = OPERATION_SETTING.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_THEME_MANAGE + treeID;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：主题管理详细信息加载数据
     *	参数：	string:treeID       树节点id
                string:portalId     portalId
     *	返回值：
     */
    function loadThemeManageData(treeID, portalId){
        var cacheID = CACHE_THEME_MANAGE + treeID;
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_THEME_MANAGE;
            p.setContent("id", treeID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var themeManageNode = this.getNodeValue(XML_THEME_MANAGE);
                var themeManageNodeID = cacheID+"."+XML_THEME_MANAGE;

                Cache.XmlIslands.add(themeManageNodeID,themeManageNode);
                Cache.Variables.add(cacheID,[themeManageNodeID]);

                initThemeManagePages(cacheID,treeID, portalId);
            }
            request.send();
        }else{
            initThemeManagePages(cacheID,treeID, portalId);
        }
    }
    /*
     *	函数说明：主题管理相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                string:treeID           树节点id
     *	返回值：
     */
    function initThemeManagePages(cacheID,treeID, portalId){
        var page2TreeObj = $("page2Tree");
        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function(){
            loadThemeManageTreeData(cacheID, portalId);
            initThemeManageTreeMenu(treeID, portalId);
        });

        //设置搜索
        var page2BtSearchObj = $("page2BtSearch");
        var page2KeywordObj = $("page2Keyword");
        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置翻页按钮显示状态
        var page2BtPrevObj = $("page2BtPrev");
        var page2BtNextObj = $("page2BtNext");
        page2BtPrevObj.style.display = "none";
        page2BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page2BtSaveObj = $("page2BtSave");
        page2BtSaveObj.style.display = "none";
    }
    /*
     *	函数说明：主题管理tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadThemeManageTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_THEME_MANAGE);
        if(null!=xmlIsland){
            var page2TreeObj = $("page2Tree");
            page2TreeObj.load(xmlIsland.node);
            page2TreeObj.research = true;

            page2TreeObj.onTreeNodeRightClick = function(eventObj){
                onPage2TreeNodeRightClick(eventObj);
            }
        }
    }
    /*
     *	函数说明：主题管理tree加载数据
     *	参数：	string:siteID           门户节点id
     *	返回值：
     */
    function initThemeManageTreeMenu(siteID, portalId){
        var item1 = {
            label:"更名",
            callback:function(){
                changeThemeName();
            },
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getThemeTreeId();}
        }
        var item2 = {
            label:"删除",
            callback:function(){
                delTheme(portalId);
            },
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getThemeTreeId();}
        }
        var item3 = {
            label:"复制",
            callback:function(){
                copyTheme(portalId);
            },
            icon:ICON + "copy.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getThemeTreeId();}
        }
        var item4 = {
            label:"预览",
            callback:function(){
                previewTheme(portalId);
            },
            icon:ICON + "preview.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getThemeTreeId();}
        }
        var item5 = {
            label:"设为默认",
            callback:function(){
                setDefaultTheme(portalId);
            },
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getThemeTreeId() && "1"!=getThemeAttribute("isDefault");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addSeparator();
        menu1.addItem(item4);
        menu1.addItem(item5);

        var page2TreeObj = $("page2Tree");
        page2TreeObj.contextmenu = menu1;
    }
    /*
     *	函数说明：右击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onPage2TreeNodeRightClick(eventObj){
        var page2TreeObj = $("page2Tree");
        if(null!=page2TreeObj.contextmenu){
            page2TreeObj.contextmenu.show(eventObj.clientX,eventObj.clientY);
        }
    }
    /*
     *	函数说明：获取主题树节点id
     *	参数：	
     *	返回值：
     */
    function getThemeTreeId(){
        var id = null;
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(null!=treeNode){
            id = treeNode.getId();
        }
        return id;
    }
    /*
     *	函数说明：修改主题名
     *	参数：	string:siteID           门户节点id
     *	返回值：
     */
    function changeThemeName(){
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var newName = prompt("请输入新主题名",treeName,"重新命名\""+treeName+"\"为",null,50);
            newName = newName.replace(/[\s　]/g,"");
            while(""==newName){
                alert("请输入至少一个字符，并且不能使用空格(包括全角空格）");
                newName = prompt("请输入新主题名",treeName,"重新命名\""+treeName+"\"为",null,50);
                newName = newName.replace(/[\s　]/g,"");            
            }

            if(null!=newName && treeName!=newName){
                var p = new HttpRequestParams();
                p.url = URL_RENAME_THEME;
                p.setContent("themeId",treeID);
                p.setContent("name", newName);

                var request = new HttpRequest(p);
                request.onsuccess = function(){
                    treeNode.setAttribute("name",newName);
                    page2TreeObj.reload();
                }
                request.send();
            }
        }
    }
    /*
     *	函数说明：删除主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function delTheme(portalId){
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_DEL_THEME;
            p.setContent("portalId",portalId);
            p.setContent("themeId",treeID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                page2TreeObj.removeTreeNode(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：复制主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function copyTheme(portalId){
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_COPY_THEME;
            p.setContent("portalId",portalId);
            p.setContent("themeId",treeID);
            p.setContent("name","副本_" + treeName);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var themeNode = this.getNodeValue(XML_THEME_MANAGE).selectSingleNode("treeNode");
                var rootNode = page2TreeObj.getTreeNodeById("_rootId");

                page2TreeObj.insertTreeNodeXml(themeNode.toXml(),rootNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：预览主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function previewTheme(portalId){
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var url = URL_PREVIEW_THEME + "?themeId=" + treeID + "&portalId=" + portalId;

            window.open(url,"previewTheme","");
        }
    }
    /*
     *	函数说明：设置默认主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function setDefaultTheme(portalId){
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_SET_DEFAULT_THEME;
            p.setContent("portalId",portalId);
            p.setContent("themeId",treeID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //先清除前次默认主题名称
                var rootNode = page2TreeObj.getTreeNodeById("_rootId");
                var defaultThemeNode = new XmlNode(rootNode.node).selectSingleNode(".//treeNode[@isDefault='1']");
                if(null!=defaultThemeNode){
                    var name = defaultThemeNode.getAttribute("name");
                    defaultThemeNode.setAttribute("icon",ICON + "theme.gif");
                    defaultThemeNode.setAttribute("isDefault","0");
                }

                //修改当前节点名称及属性
                treeNode.setAttribute("icon",ICON + "default_theme.gif");
                treeNode.setAttribute("isDefault","1");

                page2TreeObj.reload();                
            }
            request.send();
        }
    }
    /*
     *	函数说明：获取主题属性
     *	参数：	string:attrName           主题属性名
     *	返回值：
     */
    function getThemeAttribute(attrName){
        var value = null;
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(null!=treeNode){
            value = treeNode.getAttribute(attrName);
        }
        return value;
    }
    /*
     *	函数说明：刷新缓存
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
    function flushCache(id,portalId){
        var p = new HttpRequestParams();
        p.url = URL_FLUSH_CACHE;
        p.setContent("portalId",portalId);
        p.setContent("themeId",id);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
        }
        request.send();
    }
    /*
     *	函数说明：缓存管理
     *	参数：	
     *	返回值：
     */
    function cacheManage(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var portalId = treeNode.getAttribute("portalId");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadCacheManageData(treeID, portalId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page3";
            inf.label = OPERATION_SETTING.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_CACHE_MANAGE + treeID;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：缓存管理详细信息加载数据
     *	参数：	string:treeID       树节点id
                string:portalId     portalId
     *	返回值：
     */
    function loadCacheManageData(treeID, portalId){
        var cacheID = CACHE_CACHE_MANAGE + treeID;
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_CACHE_MANAGE;
            p.setContent("portalId", portalId);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var cacheManageNode = this.getNodeValue(XML_CACHE_MANAGE);
                var cacheManageNodeID = cacheID+"."+XML_CACHE_MANAGE;

                Cache.XmlIslands.add(cacheManageNodeID,cacheManageNode);
                Cache.Variables.add(cacheID,[cacheManageNodeID]);

                initCacheManagePages(cacheID,treeID, portalId);
            }
            request.send();
        }else{
            initCacheManagePages(cacheID,treeID, portalId);
        }
    }
    /*
     *	函数说明：缓存管理相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                string:treeID           树节点id
                string:portalId         portalId
     *	返回值：
     */
    function initCacheManagePages(cacheID,treeID, portalId){
        var page3CacheListObj = $("page3CacheList");
        createCacheList(page3CacheListObj,cacheID,treeID, portalId);

        //设置翻页按钮显示状态
        var page3BtPrevObj = $("page3BtPrev");
        var page3BtNextObj = $("page3BtNext");
        page3BtPrevObj.style.display = "none";
        page3BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page3BtSaveObj = $("page3BtSave");
        page3BtSaveObj.style.display = "none";
    }
    /*
     *	函数说明：创建缓存管理列表
     *	参数：	Element:listObj         列表对象
                string:cacheID          缓存数据id
                string:treeID           树节点id
                string:portalId         portalId
     *	返回值：
     */
    function createCacheList(listObj,cacheID,treeID, portalId){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_CACHE_MANAGE);
        if(null!=xmlIsland){
            var str = [];
            str[str.length] = "<table border=\"0\" cellspacing=\"\" cellpadding=\"3\">";
            str[str.length] = "<tr class=\"th\"><td width=\"200\">缓存项</td><td>&nbsp;</td></tr>";

            var cacheItems = xmlIsland.selectNodes("cacheItem");
            for(var i=0,iLen=cacheItems.length;i<iLen;i++){
                var cacheItem = cacheItems[i];
                var name = cacheItem.getAttribute("name");
                var id = cacheItem.getAttribute("id");
                str[str.length] = "<tr><td class=\"t\">" + name + "</td><td class=\"t\"><input type=\"button\" class=\"btWeak\" value=\"刷新\" onclick=\"flushCache('" + id + "','" + portalId + "')\"/></td></tr>";
            }
            str[str.length] = "</table>";

            listObj.innerHTML = str.join("\r\n");
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
     *	函数说明：资源管理
     *	参数：	
     *	返回值：
     */
    function resourceManage(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getAttribute("portalId");
            var name = treeNode.getName();
            var code = treeNode.getAttribute("code");

            var params = {
                type:"site",
                code:code,
                id:id
            };
        
            window.showModalDialog("resource.htm",{params:params,title:"\""+name+"\"相关资源管理"},"dialogWidth:400px;dialogHeight:400px;");
        }
    }

    /*
     *	函数说明：导入门户
     *	参数：	
     *	返回值：
     */
    function importSite(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeName = "门户导入";
            var treeID = treeNode.getAttribute("id");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadUploadDetailData(treeID);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_IMPORT.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_UPLOAD_DETAIL + treeID;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：上传portlet详细信息加载数据
     *	参数：	string:treeID     树节点id/组编号
     *	返回值：
     */
    function loadUploadDetailData(treeID){
        var cacheID = CACHE_UPLOAD_DETAIL + treeID;
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_UPLOAD_DETAIL;
            var request = new HttpRequest(p);
            request.onresult = function(){
                var siteInfoNode = this.getNodeValue(XML_SITE_INFO);
                var siteInfoNodeID = cacheID+"."+XML_SITE_INFO;

                Cache.XmlIslands.add(siteInfoNodeID,siteInfoNode);
                Cache.Variables.add(cacheID,[siteInfoNodeID]);

                initUploadPages(cacheID);
            }
            request.send();
        }else{
            initUploadPages(cacheID);
        }
    }
    /*var page1FormObj = $("page1Form");
     *	函数说明：portlet相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initUploadPages(cacheID){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadImportInfoFormData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page1BtNextObj = $("page1BtNext");
        page1BtPrevObj.style.display = "none";
        page1BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.disabled = false;
        page1BtSaveObj.onclick = function(){
            saveUpload(cacheID);
        }
    }

    /*
     *	函数说明：Portlet导入xform加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadImportInfoFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_SITE_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = "true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	函数说明：保存上传
     *	参数： 
     *	返回值：
     */
    function saveUpload(){
        var page1FormObj = $("page1Form");
        var fileName = page1FormObj.getData("file");
        if (fileName==null || fileName==""){
            page1FormObj.showCustomErrorInfo("file","请选择导入文件");
        }else {
            var page1BtSaveObj = $("page1BtSave");
            page1BtSaveObj.disabled = true;
            
            page1FormObj.upload(URL_IMPORT_SITE,null,function(response){
                if("Success" == response.type){
                    alert(response.msg,response.description);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }else if("Error" == response.type){
                    alert(response.msg,response.description);

                    page1BtSaveObj.disabled = false;
                }
            });

        }
    }

    /*
     *	函数说明：导出门户
     *	参数：	string:portalId         portalId
     *	返回值：
     */
    function exportSite(){
        var frameName = createExportFrame();
        var frameObj = window.frames[frameName];
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var treeID = treeNode.getAttribute("portalId");
        frameObj.location.href = URL_EXPORT_SITE + "?portalId=" + treeID;
    }
    
    /*
     *	函数说明：创建导出用iframe
     *	参数：  
     *	返回值：
     */
    function createExportFrame(){
        var frameName = "exportFrame";
        var frameObj = $(frameName);
        if(null==frameObj){
            frameObj = document.createElement("<iframe name='"+frameName+"' id='"+frameName+"' src='about:blank' style='display:;width:100px;height:100px'></iframe>");
            document.body.appendChild(frameObj);
        }
        return frameName;
    }
    /*
     *	函数说明：查看页面流量
     *	参数：	
     *	返回值：
     */
    function showPageView(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var portalId = treeNode.getAttribute("portalId");
            var name = treeNode.getName();
            var code = treeNode.getAttribute("code");

            var params = {
                type:"site",
                code:code,
                portalId:portalId
            };
        
            window.showModalDialog("pageview.htm",{params:params,title:"查看\""+name+"\"页面流量"},"dialogWidth:400px;dialogHeight:400px;resizable:yes");
        }
    }
    /*
     *	函数说明：授予角色
     *	参数：	
     *	返回值：
     */
    function setPortalPermission(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var name = treeNode.getName();
            var resourceType = "1";
            var type = "portal";
            var title = "授予\"" + name + "\"角色";
            var params = {
                roleId:id,
                resourceType:resourceType,
				applicationId:"pms",
                isRole2Resource:"0"
            };
            window.showModalDialog("setpermission.htm",{params:params,title:title,type:type},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
        }
    }

	/********************************************************************************************************************
     ************************************************** 以下为静态发布相关 *********************************************	
     ********************************************************************************************************************/
	URL_SYNC_PROGRESS = "data/progress.xml";
    URL_CANCEL_SYNC_PROGRESS = "data/_success.xml";
	URL_STATIC_ISSUE_PORATL = "data/_success.xml";
	URL_STATIC_ISSUE_PAGE = "data/_success.xml";
	URL_REMOTE_ISSUE = "data/_success.xml";

    URL_SYNC_PROGRESS = "publish!getProgress.action";
    URL_CANCEL_SYNC_PROGRESS = "publish!doConceal.action";
	URL_STATIC_ISSUE_PORATL = "pms/publish!staticIssuePortal.action";
	URL_STATIC_ISSUE_PAGE = "pms/publish!staticIssuePortalPage.action";
	URL_REMOTE_ISSUE = "publish!ftpUpload2RemoteServer.action";

    /*
     *	函数说明：静态发布整个门户站点
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
    function staticIssue(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var p = new HttpRequestParams();
        p.url = URL_STATIC_ISSUE_PORATL;
        var portalId = treeNode.getAttribute("portalId");
        p.setContent("id", portalId);
		p.setContent("type", 1);

        var request = new HttpRequest(p);

		request.onsuccess = function(){
		}

		request.onresult = function(){
			var thisObj = this;
			var data = this.getNodeValue("ProgressInfo");
			var progress = new Progress(URL_SYNC_PROGRESS,data,URL_CANCEL_SYNC_PROGRESS);
			progress.oncomplete = function(){
				// 触发远程上传
				// remoteIssue("0");
			}
			progress.start();
		}
        request.send();
    }

	/*
     *	函数说明：静态发布门户里指定页面
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
    function staticIssueOnePage(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var p = new HttpRequestParams();
        p.url = URL_STATIC_ISSUE_PAGE;
        var pageUrl = prompt("请输入要发布的页面地址");
		if(pageUrl == null || "" == pageUrl){
			return alert("页面地址不能为空");
		}
        p.setContent("pageUrl", pageUrl);

        var request = new HttpRequest(p);
		request.onresult = function(){
			var thisObj = this;
			var data = this.getNodeValue("ProgressInfo");
			var progress = new Progress(URL_SYNC_PROGRESS,data,URL_CANCEL_SYNC_PROGRESS);
			progress.oncomplete = function(){
				// 触发远程上传
				// remoteIssue("0");
			}
			progress.start();
		}
        request.send();
    }
   
	/*
     *	函数说明：远程发布
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
	function remoteIssue(override){
        var p = new HttpRequestParams();
        p.url = URL_REMOTE_ISSUE;
		p.setContent("override", override);

        var request = new HttpRequest(p);

		request.onsuccess = function(){
		}
        request.send();
    }

    window.onload = init;

    //关闭页面自动注销
    logoutOnClose();