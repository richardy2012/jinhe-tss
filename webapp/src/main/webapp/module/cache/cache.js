
    /*
     *	核心包相对路径
     */
    URL_CORE = "../../";

    /*
     *	后台响应数据节点名称
     */
    XML_DEFAULT_TOOLBAR = "DefaultToolBar";
    XML_TOOLBAR = "ToolBar";
    XML_MAIN_TREE = "CacheTree";

    XML_CACHE_INFO = "CacheInfo";
    XML_CACHE_OPTION = "CacheOption";
    XML_OPTION_INFO = "OptionInfo";
    XML_OPERATION = "Operation";
    XML_PAGE_LIST = "PageList";
    XML_HIT_RATE = "PoolHitRate";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_OPTION_DETAIL = "option__id";
    /*
     *	名称
     */
    OPERATION_EDIT = "维护\"$label\"";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/cache_init.xml";
    URL_CACHE_DETAIL = "data/cache1.xml";
    URL_SAVE_CACHE = "data/_success.xml";
    URL_CACHE_CLEAR = "data/_success.xml";
    URL_CACHE_INIT = "data/_success.xml";
    URL_VIEW_CACHED_ITEM = "data/option1.xml";
    URL_SAVE_OPTION = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_REFRESH_ITEM = "data/item.xml";

    URL_INIT = "../../../pms/cache!getAllCacheStrategy4Tree.action";
    URL_CACHE_DETAIL = "../../../pms/cache!getCacheStrategyInfo.action";
    URL_SAVE_CACHE = "../../../pms/cache!modifyCacheStrategy.action";
    URL_CACHE_CLEAR = "../../../pms/cache!releaseCache.action";
    URL_CACHE_INIT = "../../../pms/cache!initPool.action";
    URL_VIEW_CACHED_ITEM = "../../../pms/cache!viewCachedItem.action";
    URL_SAVE_OPTION = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_REFRESH_ITEM = "../../../pms/cache!refresh.action";
    /*
     *	延时
     */
    TIMEOUT_TAB_CHANGE = 200;
    /*
     *	icon路径
     */
    ICON = "images/";

    var toolbar = null;

    /*
     *	函数说明：页面初始化
     *	参数：	
     *	返回值：
     */
    function init(){
        initPaletteResize();
        initListContainerResize();
        //initUserInfo();
        initToolBar();
        initNaviBar("mod.1");
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
            str[str.length] = "    <button id=\"b1\" code=\"2\" icon=\"" + ICON + "edit.gif\" label=\"维护\" cmd=\"editCacheTacticsInfo()\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
            str[str.length] = "    <button id=\"b2\" code=\"2\" icon=\"" + ICON + "init.gif\" label=\"初始化缓存池\" cmd=\"initCache()\" enable=\"'_rootId'!=getTreeNodeId() &amp;&amp; '1'==getTreeNodeReleased()\"/>";
            str[str.length] = "    <button id=\"b3\" code=\"2\" icon=\"" + ICON + "clear.gif\" label=\"清空缓存池\" cmd=\"clearCache()\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
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
            label:"维护",
            callback:editCacheTacticsInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeNodeId() && true==getOperation("2");}
        }
        var item2 = {
            label:"初始化缓存池",
            callback:initCache,
            icon:ICON + "init.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeNodeId() && "1"==getTreeNodeReleased()  && true==getOperation("2");}
        }
        var item3 = {
            label:"清空缓存池",
            callback:clearCache,
            icon:ICON + "clear.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeNodeId()  && true==getOperation("2");}
        }

        var treeObj = $("tree");

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addSeparator();
        menu1.addItem(item2);
        menu1.addItem(item3);

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
        if("_rootId"!=getTreeNodeId()){
            editCacheTacticsInfo();
        }
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
     *	函数说明：编辑缓存信息
     *	参数：
     *	返回值：
     */
    function editCacheTacticsInfo(){
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
                    loadTreeDetailData(treeID,"1");
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                string:page                 页码
                function:callback           回调(如果不定义则调用initCacheTacticsPages)
     *	返回值：
     */
    function loadTreeDetailData(treeID,page,callback){
        var cacheID = CACHE_TREE_NODE_DETAIL + treeID;
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_CACHE_DETAIL;

            p.setContent("code", treeID);
            p.setContent("page", page);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var cacheInfoNode = this.getNodeValue(XML_CACHE_INFO);
                var cacheInfoNodeID = cacheID+"."+XML_CACHE_INFO;

                var cacheOptionNode = this.getNodeValue(XML_CACHE_OPTION);
                var cacheOptionNodeID = cacheID+"."+XML_CACHE_OPTION;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                var hitRateNode = this.getNodeValue(XML_HIT_RATE);
                var hitRateNodeID = cacheID+"."+XML_HIT_RATE;

                //2007-1-12 增加回调方法，为了实现单独刷新grid
                if(null==callback){

                    //给缓存项grid数据根节点增加cacheId属性
                    cacheOptionNode.setAttribute("cacheId",treeID);

                    Cache.XmlIslands.add(cacheInfoNodeID,cacheInfoNode);
                    Cache.XmlIslands.add(cacheOptionNodeID,cacheOptionNode);
                    Cache.XmlIslands.add(pageListNodeID,pageListNode);
                    Cache.XmlIslands.add(hitRateNodeID,hitRateNode);

                    Cache.Variables.add(cacheID,[cacheInfoNodeID,cacheOptionNodeID,pageListNodeID,hitRateNodeID]);

                    initCacheTacticsPages(cacheID);
                }else{

                    //给缓存项grid数据根节点增加cacheId属性
                    cacheOptionNode.setAttribute("cacheId",treeID);

//                    Cache.XmlIslands.add(cacheInfoNodeID,cacheInfoNode);
                    Cache.XmlIslands.add(cacheOptionNodeID,cacheOptionNode);
                    Cache.XmlIslands.add(pageListNodeID,pageListNode);
                    Cache.XmlIslands.add(hitRateNodeID,hitRateNode);

                    Cache.Variables.add(cacheID,[cacheInfoNodeID,cacheOptionNodeID,pageListNodeID,hitRateNodeID]);

                    callback(cacheID);
                }
            }
            request.send();
        }else{
            if(null==callback){
                initCacheTacticsPages(cacheID);
            }else{
                callback(cacheID);
            }
        }
    }
    /*
     *	函数说明：缓存相关页加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initCacheTacticsPages(cacheID){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadCacheTacticsInfoFormData(cacheID);
        });

        var page1GridObj = $("page1Grid");
        Public.initHTC(page1GridObj,"isLoaded","onload",function(){
            loadCacheOptionGridData(cacheID);
            initGridMenu();
            loadGridEvents();
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.onclick = function(){
            saveCacheTactics(cacheID);
        }

        //设置点击率
        var page1HitRateObj = $("page1HitRate");
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_HIT_RATE);
        if(null!=xmlIsland){
            page1HitRateObj.innerHTML = "『 池命中率：" + xmlIsland + " 』";
        }
    }
    /*
     *	函数说明：缓存信息xform加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadCacheTacticsInfoFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_CACHE_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	函数说明：缓存项grid加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadCacheOptionGridData(cacheID){
        //重新创建grid工具条
        createGridToolBar(cacheID);

        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_CACHE_OPTION);
        if(null!=xmlIsland){
            var page1GridObj = $("page1Grid");
            page1GridObj.load(xmlIsland.node,null,"node");
        }
    }
    /*
     *	函数说明：创建grid工具条
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function createGridToolBar(cacheID){
        var toolbarObj = $("gridToolBar");

        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_PAGE_LIST);
        if(null==xmlIsland){
            toolbarObj.innerHTML = "";
        }else{
            initGridToolBar(toolbarObj,xmlIsland,function(page){
                var gridBtRefreshObj = $("gridBtRefresh");
                var gridObj = $("page1Grid");

                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempCacheId = tempXmlIsland.getAttribute("cacheId");

                    //清除该组用户grid缓存
                    delCacheData(CACHE_TREE_NODE_DETAIL + tempCacheId,false);

                    loadTreeDetailData(tempCacheId,page,function(cacheID){
                        Public.initHTC(gridObj,"isLoaded","onload",function(){
                            loadCacheOptionGridData(cacheID);
                            initGridMenu();
                            loadGridEvents();
                        });

                        //设置点击率
                        var page1HitRateObj = $("page1HitRate");
                        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_HIT_RATE);
                        if(null!=xmlIsland){
                            page1HitRateObj.innerHTML = "『 池命中率：" + xmlIsland + " 』";
                        }
                    });

                    //刷新工具条
                    onInactiveRow();
                }
            });
        }
    }
    /*
     *	函数说明：Grid菜单初始化
     *	参数：	
     *	返回值：
     */
    function initGridMenu(){
        var gridObj = $("page1Grid");
        var item1 = {
            label:"查看缓存项信息",
            callback:showItemInfo,
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var item2 = {
            label:"刷新本缓存项",
            callback:refreshCurrentRow,
            icon:ICON + "refresh.gif",
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var item3 = {
            label:"隐藏列...",
            callback:function(){gridObj.hideCols();},
            icon:ICON + "hide_col.gif",
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var item4 = {
            label:"搜索...",
            callback:function(){gridObj.search();},
            enable:function(){return true;},
            visible:function(){return true;}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item3);
        menu1.addItem(item4);

        gridObj.contextmenu = menu1;
    }
    /*
     *	函数说明：grid绑定事件
     *	参数：	
     *	返回值：
     */
    function loadGridEvents(){
        var gridObj = $("page1Grid");

        gridObj.onclickrow = function(){
            onClickRow(event);
        }
        gridObj.ondblclickrow = function(){
            onDblClickRow(event);
        }
        gridObj.onrightclickrow = function(){
            onRightClickRow(event);
        }
        gridObj.oninactiverow = function() {
            onInactiveRow(event);
        }
    
    }
    /*
     *	函数说明：单击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onClickRow(eventObj){
        var rowIndex = eventObj.result.rowIndex_Xml;
        showItemStatus(rowIndex);
    }
    /*
     *	函数说明：双击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onDblClickRow(eventObj){
    }
    /*
     *	函数说明：右击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onRightClickRow(eventObj){
        var gridObj = $("page1Grid");
        var rowIndex = eventObj.result.rowIndex_Xml;
        var x = event.clientX;
        var y = event.clientY;

        gridObj.contextmenu.show(x,y);
    }
    /*
     *	函数说明：单击grid空白处
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onInactiveRow(eventObj){
        var treeTitleObj = $("treeTitle");
        Focus.focus(treeTitleObj.firstChild.id);

        showTreeNodeStatus({id:"ID",name:"名称"});

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        //防止因为载入工具条数据而导致不响应双击事件
        clearTimeout(window._toolbarTimeout);
        window._toolbarTimeout = setTimeout(function(){
            loadToolBarData(treeNode);
        },0);
    }
    /*
     *	函数说明：保存缓存
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function saveCacheTactics(cacheID){
        var page1FormObj = $("page1Form");
        var check = page1FormObj.checkForm();
        if(check==false){
            return
        }
        var p = new HttpRequestParams();
        p.url = URL_SAVE_CACHE;

        //是否提交
        var flag = false;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){       

            //缓存基本信息
            var cacheInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_CACHE_INFO);
            if(null!=cacheInfoNode){
                var cacheInfoDataNode = cacheInfoNode.selectSingleNode(".//data");
                if(null!=cacheInfoDataNode){
                    flag = true;

                    var prefix = cacheInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(cacheInfoDataNode,prefix);
                }
            }   

            //缓存项信息
            var cacheOptionNode = Cache.XmlIslands.get(cacheID+"."+XML_CACHE_OPTION);
            if(null!=cacheOptionNode){
                var cacheOptionDataStr = [];
                var condition = "[(@_new='true' and (not(@_delete='true') or not(@_delete))) or (@_delete='true' and (not(@_new='true') or not(@_new))) or (@_modify='true')]";
                var cacheOptionDataNodes = cacheOptionNode.selectNodes(".//data//row"+condition);
                
                for(var i=0,iLen=cacheOptionDataNodes.length;i<iLen;i++){
                    var curNode = cacheOptionDataNodes[i];
                    cacheOptionDataStr.push(curNode.toXml());
                }
                if(0<cacheOptionDataStr.length){
                    flag = true;
                    p.setContent(XML_CACHE_OPTION,"<data>"+cacheOptionDataStr.join("")+"</data>");
                }
            }
        }

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            syncButton([page1BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);

                var toolbarObj = $("gridToolBar");
                var curPage = toolbarObj.getCurrentPage();
                toolbarObj.gotoPage(curPage);
            }
            request.send();
        }
    }
    
    /*
     *	函数说明：初始化缓存池
     *	参数：
     *	返回值：
     */
    function initCache(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if (null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_CACHE_INIT;
            p.setContent("code",treeID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                refreshTreeNodeState(treeNode,"0");
                treeObj.reload();

                delCacheData(CACHE_TREE_NODE_DETAIL + treeID);
                loadTreeDetailData(treeID,"1");
            }
            request.send();
        }
    }
    
    /*
     *	函数说明：清空缓存池
     *	参数：
     *	返回值：
     */
    function clearCache(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if (null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_CACHE_CLEAR;
            p.setContent("code",treeID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                refreshTreeNodeState(treeNode,"1");
                treeObj.reload();

                delCacheData(CACHE_TREE_NODE_DETAIL + treeID);
                loadTreeDetailData(treeID,"1");
            }
            request.send();
        }
    }

   function refreshTreeNodeState(treeNode,state){
        if(null==state){
            state = treeNode.getAttribute("released");
        }
        treeNode.setAttribute("released",state);    
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
     *	函数说明：获取节点released
     *	参数：  
     *	返回值：string:type   树节点type
     */
    function getTreeNodeReleased(){
        return getTreeAttribute("released");
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
     *	函数说明：查看缓存项信息
     *	参数：	
     *	返回值：
     */
    function showItemInfo(){
        var gridObj = $("page1Grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];              
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var id = curRowNode.getAttribute("id");
            var key = curRowNode.getAttribute("key");
            var code = curRowNode.getAttribute("code");

            var url = URL_VIEW_CACHED_ITEM + "?key=" + key + "&code=" + code;
            window.open(url,"查看缓存项信息","");
        }
    }
    /*
     *	函数说明：显示用户状态信息
     *	参数：	number:rowIndex     grid数据行号
     *	返回值：
     */
    function showItemStatus(rowIndex){
        if(null==rowIndex){
            var id = "-";
            var key = "-";
        }else{
            var gridObj = $("page1Grid");
            var rowNode = gridObj.getRowNode_Xml(rowIndex);
            var id = rowNode.getAttribute("id");
            var key = rowNode.getAttribute("key");
        }

        var block = Blocks.getBlock("statusContainer");
        if(null!=block){
            block.open();
            block.writeln("ID",id);
            block.writeln("key",key);
            block.close();
        }
    }
    /*
     *	函数说明：刷新单条数据
     *	参数：	
     *	返回值：
     */
    function refreshCurrentRow(){
        var gridObj = $("page1Grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var rowNode = gridObj.getRowNode_Xml(curRowIndex);
            var id = rowNode.getAttribute("id");
            var key = rowNode.getAttribute("key");
            var code = rowNode.getAttribute("code");

            var p = new HttpRequestParams();
            p.url = URL_REFRESH_ITEM;
            p.setContent("key",id);
            p.setContent("code",code);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                gridObj.delRow_Xml(curRowIndex);
            }
            request.onresult = function(){
                var row = this.getNodeValue(XML_CACHE_OPTION);
                var columns = gridObj.getAllColumnNames();
                for(var i=0,iLen=columns.length;i<iLen;i++){
                    var name = columns[i];
                    var value = row.getAttribute(name);
                    if(null!=value){
                        gridObj.modifyNamedNode_Xml(curRowIndex,name,value);
                    }
                }
            }
            request.send();
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



    window.onload = init;

	//关闭页面自动注销
    window.attachEvent("onunload", function(){
        if(10000<window.screenTop || 10000<window.screenLeft){
            logout();
        }
	});