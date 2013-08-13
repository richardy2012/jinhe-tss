    /*
     *	后台响应数据节点名称
     */
	XML_SITE_INFO = "SiteInfo";
    XML_MAIN_TREE = "SiteTree";
    XML_ARTICLE_LIST = "ArticleList";
    XML_CHANNEL_INFO = "ChannelInfo";
    XML_CHANNEL_TREE = "ChannelTree";
 
    /* 默认唯一编号名前缀 */
 
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_CHANNEL_DETAIL = "channelDetail__id";
    CACHE_SITE_DETAIL    = "siteDetail__id";
    CACHE_SEARCH_ARTICLE = "searchArticle__id";
 
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/site_init.xml";
    URL_ARTICLE_LIST = "data/articlelist.xml";
    URL_DEL_ARTICLE = "data/_success.xml";
    URL_MOVE_ARTICLE = "data/_success.xml";
    URL_LOCK_ARTICLE = "data/_success.xml";
    URL_START_ARTICLE = "data/_success.xml";
    URL_RESHIP_ARTICLE = "data/_success.xml";
    URL_STICK_ARTICLE = "data/_success.xml";
    URL_SITE_DETAIL ="data/site1.xml";
    URL_SAVE_SITE = "data/_success.xml";
    URL_UPDATE_SITE = "data/_success.xml";
    URL_CHANNEL_DETAIL ="data/channel.xml";
    URL_SAVE_CHANNEL = "data/_success.xml";
    URL_UPDATE_CHANNEL = "data/_success.xml";
    URL_DEL_CHANNEL = "data/_success.xml";
    URL_MOVE_CHANNEL = "data/_success.xml";
    URL_SORT_CHANNEL = "data/_success.xml";
	URL_STOP_CHANNEL = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_ARTICLE_OPERATION = "data/operation1.xml";

    URL_SITE_PUBLISH_PROGRESS = "data/progress.xml";
    URL_CHANNEL_PUBLISH_PROGRESS = "data/progress.xml";
    URL_CANCEL_PUBLISH_PROGRESS = "data/_success.xml";
	URL_GET_PROGRESS = "data/_success.xml";
    URL_CONCEAL_PROGRESS = "data/_success.xml";

    URL_SEARCH_ARTICLE = "data/articlelist.xml";
    URL_SAVE_PUBLISH_ARTICLE = "data/_success.xml";
    URL_UPLOAD_FILE = "data/upload1.htm";
    URL_UPLOAD_SERVER_FILE = "data/upload2.htm";
 
    function init(){
        initPaletteResize();
        initListContainerResize();
		initUserInfo();
        initNaviBar("cms.1");
        initMenus();
        initBlocks();
        initWorkSpace();
        initEvents();
        initFocus();

        loadInitData();
    }
  
    function initMenus(){
        var submenu1 = new Menu();//站点
        var submenu2 = new Menu();//栏目
        var submenu3 = new Menu();//文章
        var submenu4 = new Menu();//发布
        
        var item1 = {
            label:"新建站点",
            callback:addNewSite,
            visible:function(){return "_rootId"==getTreeAttribute("id") && true==getOperation("1");}
        }
        var item2 = {
            label:"新建栏目",
            callback:addNewChannel,
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("2");}
        }
        var item3 = {
            label:"新建文章",
            callback:addNewArticle,
            visible:function(){return "0"==getTreeAttribute("isSite") && "_rootId"!=getTreeAttribute("id") && true==getOperation("3");}
            
        }

        var item4 = {
            label:"发布",
            callback:null,
            icon:ICON + "icon_publish_source.gif",
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("4");},
            submenu:submenu4
        }
        var subitem4a = {
            label:"增量发布",
            callback:function(){
                publishArticle(1);
            }
        }
        var subitem4b = {
            label:"完全发布",
            callback:function(){
                publishArticle(2);
            }
        }

        var item5 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("5");}
        }
        var item6 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("6");}
        }
        var item7 = { 
            label:"启用",
            callback:startSite,
            icon:ICON + "start.gif",
            visible:function(){return "0"!=getTreeAttribute("disabled") && "_rootId"!=getTreeAttribute("id") && true==getOperation("8");}
        }
        var item8 = {
            label:"停用",
            callback:stopSite,
            icon:ICON + "stop.gif",
            visible:function(){return "0"==getTreeAttribute("disabled") && "_rootId"!=getTreeAttribute("id") && true==getOperation("7");}
        }
        var item9 = {
            label:"移动到...",
            callback:moveChannelTo,
            icon:ICON + "move.gif",
            visible:function(){return "0"==getTreeAttribute("isSite") && true==getOperation("6");}
        }
         var item14 = {
            label:"搜索文章...",
            callback:searchArticle,
            icon:ICON + "search.gif",
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("16");}
        }
        var item16 = {
            label:"浏览文章",
            callback:showArticleList,
            icon:ICON + "view_list.gif",
            visible:function(){return "0"==getTreeAttribute("isSite") && "_rootId"!=getTreeAttribute("id") && true==getOperation("14");}
        }
        var item17 = { 
            label:"启用站点",
            callback:startAll,
            icon:ICON + "start_all.gif",
            visible:function(){return "1"==getTreeAttribute("disabled") && "1"==getTreeAttribute("isSite") && true==getOperation("7");}
        }
         
        
        submenu4.addItem(subitem4a);
        submenu4.addItem(subitem4b);

        var menu1 = new Menu();
        menu1.addItem(item17);
        menu1.addItem(item7);
        menu1.addItem(item8);
        menu1.addSeparator();
        menu1.addItem(item15);
        menu1.addItem(item5);
        menu1.addItem(item6);
        menu1.addItem(item9);
        menu1.addItem(item12);
        menu1.addItem(item10);
        menu1.addItem(item11);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addSeparator();
        menu1.addItem(item4);
        menu1.addItem(item13);
        menu1.addItem(item18);
        menu1.addItem(item19);
        menu1.addItem(item20);
        menu1.addSeparator();
        menu1.addItem(item14);
		menu1.addItem(item16);

        $$("tree").contextmenu = menu1;
    }
 
    function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function(){
            var _operation = this.getNodeValue(XML_OPERATION);

            var siteTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var tree = $T("tree", siteTreeNode); 

            initTree(siteTreeNodeID);
        }
        request.send();
    }

    function initGridMenu(){
        var gridObj = $("grid");
		var item1 = {
            label:"编辑",
            callback:editArticleInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true},
            visible:function(){return "4"!=getArticleAttribute("status") && "5"!=getArticleAttribute("status") && "-1"!=getArticleAttribute("status") && "1"!=getArticleAttribute("state") && true==getUserOperation("a_5");}
        }
        var item2 = {
            label:"删除",
            callback:delArticle,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("a_3");}
        }
        var item3 = {
            label:"移动到...",
            callback:moveArticleTo,
            icon:ICON + "move.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getArticleAttribute("state") && true==getUserOperation("a_6");}
        }
        var item4 = {
            label:"置顶",
            callback:function(){
                stickArticle("1");
            },
            icon:ICON + "stick.gif",
            enable:function(){return true;},
            visible:function(){return "1"!=getArticleAttribute("isTop") && true==getUserOperation("a_17");}
        }
        var item5 = {
            label:"解除置顶",
            callback:function(){
                stickArticle("0");
            },
            icon:ICON + "unstick.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getArticleAttribute("isTop") && true==getUserOperation("a_18");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
		menu1.addItem(item2);
		menu1.addItem(item3);
		menu1.addItem(item4);
		menu1.addItem(item5);
 
        gridObj.contextmenu = menu1;
    }
 
    /*
     *	函数说明：grid初始化
     *	参数：	string:id   grid数据相关树节点id
     *	返回值：
     */
    function initGrid(id){
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridEvents();
            loadGridData(id,"1");//默认第一页
        });
    }
 
    function loadGridEvents(){
        var gridObj = $("grid");

        gridObj.onclickrow = function(){
            onClickRow(event);
        }
        gridObj.ondblclickrow = function(){
            onDblClickRow(event);
        }
        gridObj.onrightclickrow = function() {
            onRightClickRow(event);
        }
        gridObj.oninactiverow = function() {
            onInactiveRow(event);
        }
        gridObj.onsortrow = function() {
            onSortRow(event);
        }
    }
    /*
     *	函数说明：grid加载数据
     *	参数：	string:treeID       grid数据相关树节点id
                string:page         页码
                string:sortName     排序字段
                string:direction    排序方向
     *	返回值：
     */
    function loadGridData(treeID,page,sortName,direction){
        var cacheID = CACHE_TREE_NODE_GRID + treeID;
		var p = new HttpRequestParams();
		p.url = URL_ARTICLE_LIST;
		p.setContent("channelId", treeID);
		p.setContent("page", page);
		if(null!=sortName && null!=direction){
			p.setContent("field", sortName);
			p.setContent("orderType", direction);
		}

		var request = new HttpRequest(p);
		request.onresult = function(){
			var articleListNode = this.getNodeValue(XML_ARTICLE_LIST);
			var articleListNodeID = cacheID+"."+XML_ARTICLE_LIST;

			var pageListNode = this.getNodeValue(XML_PAGE_LIST);
			var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

			//给grid节点加上channelId属性表示栏目id
			articleListNode.setAttribute("channelId", treeID);

			//给当前排序列加上_direction属性
			if(null!=sortName && null!=direction){
				var column = articleListNode.selectSingleNode("//column[@name='" + sortName + "']");
				if(null!=column){
					column.setAttribute("_direction",direction);
				}
			}

			Cache.XmlIslands.add(articleListNodeID,articleListNode);
			Cache.XmlIslands.add(pageListNodeID,pageListNode);
			Cache.Variables.add(cacheID,[articleListNodeID,pageListNodeID]);

			loadGridDataFromCache(cacheID);
		}
		request.send();
    }
    /*
     *	函数说明：grid从缓存加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadGridDataFromCache(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ARTICLE_LIST);
        if(null!=xmlIsland){
            var gridObj = $("grid");
            gridObj.load(xmlIsland.node,null,"node");

            Focus.focus("gridTitle");
        }
    }

 
    /*
     *	函数说明：显示文章详细信息
     *	参数：	boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function editArticleInfo(editable){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowName = gridObj.getNamedNodeValue_Xml(rowIndex,"title");
        var rowID = rowNode.getAttribute("id");
        var articleType = rowNode.getAttribute("state");
        var workflowStatus = rowNode.getAttribute("status");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId){
            channelId = rowNode.getAttribute("channelId");
        }

        var returnValue = window.showModalDialog("article.htm",{title:"编辑文章",isNew:false,editable:editable,channelId:channelId,articleType:articleType,articleId:rowID,workflowStatus:workflowStatus},"dialogWidth:900px;dialogHeight:700px;status:yes");
        if(true==returnValue){

            //如果当前grid显示为此文章所在栏目，则刷新grid
            var gridObj = $("grid");
            if(true==gridObj.hasData_Xml()){
                var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                var tempChannelId = tempXmlIsland.getAttribute("channelId");
                if(tempChannelId==channelId){
                    loadGridData(tempChannelId,"1");//默认第一页

                    //刷新工具条
                    onInactiveRow();
                }
            }
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
     *	函数说明：新建站点
     *	参数：	
     *	返回值：
     */
    function addNewSite(){
        var treeID = new Date().valueOf();
        var treeName = "站点";
        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadSiteDetailData(treeID,true,true);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_ADD.replace(/\$label/i,treeName);
        inf.phases = null;
        inf.callback = callback;
        inf.SID = CACHE_SITE_DETAIL + treeID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：编辑组信息
     *	参数：  boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function editSiteInfo(editable){
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
                    loadSiteDetailData(treeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName); 
                inf.SID = CACHE_VIEW_SITE_DETAIL + treeID;           
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_SITE_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：站点详细信息加载数据
     *	参数：	string:treeID           树节点id
                boolean:editable        是否可编辑(默认true)
                boolean:isNew           是否新增
     *	返回值：
     */
    function loadSiteDetailData(treeID,editable,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_SITE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_SITE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_SITE_DETAIL;
            if(true==isNew){            
                p.setContent("isNew", "1");
                p.setContent("isSite", "1");
            }else{
                p.setContent("siteId", treeID);            
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var siteInfoNode = this.getNodeValue(XML_SITE_INFO);
                var siteInfoNodeID = cacheID+"."+XML_SITE_INFO;

                Cache.XmlIslands.add(siteInfoNodeID,siteInfoNode);

                Cache.Variables.add(cacheID,[siteInfoNodeID]);

                initSitePages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initSitePages(cacheID,editable,isNew);
        }
    }
    /*
     *	函数说明：站点相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:editable        是否可编辑(默认true)
                boolean:isNew           是否新增
     *	返回值：
     */
    function initSitePages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadSiteInfoFormData(cacheID,editable);
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
            saveSite(cacheID,isNew);
        }
    }
    /*
     *	函数说明：站点信息xform加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function loadSiteInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_SITE_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");
            loadPage1FormEvents("site",cacheID);
        }
    }
    /*
     *	函数说明：page1Form绑定事件
     *	参数：	
     *	返回值：
     */
    function loadPage1FormEvents(formSource,cacheID){
        var page1FormObj = $("page1Form");
        page1FormObj.ondatachange = function(){
            //离开提醒
            attachReminder(cacheID);

            var name = event.result.name;
            var value = event.result.newValue;
            switch(formSource){                
                case "site"://站点
                    switch(name){
                        case "publishByTime":
                          page1FormObj.setColumnEditable("startHour","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("startMinute","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("perHour","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("perMinute","1"==value?"true":"false");
                          break;
                        case "publishByFTP":
                          page1FormObj.setColumnEditable("server","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("port","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("platform","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("user","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("password","1"==value?"true":"false");
                          break;
                    }
                    break;
                case "channel"://栏目
                    switch(name){                        
                        case "hot":
                          page1FormObj.setColumnEditable("hotRate","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("hotIcon","1"==value?"true":"false");
                          break;
                        case "new":
                          page1FormObj.setColumnEditable("newAmount","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("newUnit","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("newIcon","1"==value?"true":"false");
                          break;
                    }
                    break;
                case "article":
                    break;
            }
        }
    }
    /*
     *	函数说明：保存站点
     *	参数：	string:cacheID      缓存数据ID
                boolean:isNew       是否新增
     *	返回值：
     */
    function saveSite(cacheID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
		p.setContent("isSite", "1");
        if(isNew==true){
            p.url = URL_SAVE_SITE;
        }else{
            p.url = URL_UPDATE_SITE;
        }
        //是否提交
        var flag = false;
        
        var channelCache = Cache.Variables.get(cacheID);
        if(null!=channelCache){       

            //站点基本信息
            var siteInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_SITE_INFO);
            if(null!=siteInfoNode){
                var siteInfoDataNode = siteInfoNode.selectSingleNode(".//data");
                if(null!=siteInfoDataNode){
                    flag = true;                    

                    var prefix = siteInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(siteInfoDataNode,prefix);
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

                    var page1FormObj = $("page1Form");
                    //更新树节点名称
                    var id = cacheID.trim(CACHE_SITE_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：删除page3里grid的行
     *	参数：	
     *	返回值：
     */
    function delPage3GridRow(){
        var page3GridObj = $("page3Grid");
        var selectedIndex = page3GridObj.getSelectedRowsIndexArray_Xml();
        for(var i=0,iLen=selectedIndex.length;i<iLen;i++){
            var curRowIndex = selectedIndex[i];
            var refresh = i<iLen-1?false:true;
            page3GridObj.delRow_Xml(curRowIndex,null,refresh);
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
        var gridTitleObj = $("gridTitle");

        Focus.register(treeTitleObj.firstChild);
        Focus.register(statusTitleObj.firstChild);
        Focus.register(gridTitleObj);
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
        var gridTitleObj = $("gridTitle");
        
        Event.attachEvent(treeBtRefreshObj,"click",onClickTreeBtRefresh);
        Event.attachEvent(treeTitleBtObj,"click",onClickTreeTitleBt);
        Event.attachEvent(statusTitleBtObj,"click",onClickStatusTitleBt);
        Event.attachEvent(paletteBtObj,"click",onClickPaletteBt);

        Event.attachEvent(treeTitleObj,"click",onClickTreeTitle);
        Event.attachEvent(statusTitleObj,"click",onClickStatusTitle);
        Event.attachEvent(gridTitleObj,"click",onClickGridTitle);
    }
    /*
     *	函数说明：点击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeActived(eventObj){
        var treeTitleObj = $("treeTitle");
        Focus.focus(treeTitleObj.firstChild.id);

        showTreeNodeStatus({id:"ID",name:"名称",userName:"创建者",creationDate:"创建时间",modifiedUserName:"修改者",modifiedDate:"修改时间"});
 
    }
    /*
     *	函数说明：双击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeDoubleClick(eventObj){
        var treeNode = eventObj.treeNode;
        var id = getTreeAttribute("id");
        var isSite = getTreeAttribute("isSite");
        getTreeOperation(treeNode,function(_operation){
            if("_rootId"!=id){
                var canShowArticleList = checkOperation("14",_operation);
                var canView = checkOperation("12",_operation);
                var canEdit = checkOperation("5",_operation);

                if("0"==isSite && true == canShowArticleList){
                    showArticleList();
                }else{
                    if(true==canEdit){
                        editTreeNode();                    
                    }else if(true==canView){
                        editTreeNode(false);
                    }
                }
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

        showTreeNodeStatus({id:"ID",name:"名称",userName:"创建者",creationDate:"创建时间",modifiedUserName:"修改者",modifiedDate:"修改时间"});

        var x = eventObj.clientX;
        var y = eventObj.clientY;
        getTreeOperation(treeNode,function(_operation){
            if(null!=treeObj.contextmenu){
                treeObj.contextmenu.show(x,y);                
            }
        });
    }
    /*
     *	函数说明：拖动树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeMoved(eventObj){
        var movedTreeNode = eventObj.movedTreeNode;	
        sortChannelTo(eventObj)
    }
    /*
     *	函数说明：单击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onClickRow(eventObj){    
        Focus.focus("gridTitle");
    }
    /*
     *	函数说明：双击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onDblClickRow(eventObj){
        var rowIndex = eventObj.result.rowIndex_Xml;
        getGridOperation(rowIndex,function(_operation){
            //检测编辑权限
            var canEdit = checkOperation("a_5",_operation);
            var canView = checkOperation("a_12",_operation);

            var gridObj = $("grid");
            var rowNode = gridObj.getRowNode_Xml(rowIndex);
            var status = rowNode.getAttribute("status");
            var state = rowNode.getAttribute("state");

            if(true==canEdit && "4"!=status && "-1"!=status && "5"!=status && "1"!=state){
                editArticleInfo();            
            }else if(true==canView){
                editArticleInfo(false);
            }
        });
    }
    /*
     *	函数说明：右击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onRightClickRow(eventObj){
        var rowIndex = eventObj.result.rowIndex_Xml;
        var x = event.clientX;
        var y = event.clientY;

        getGridOperation(rowIndex,function(_operation){
            addWorkFlowMenu(x,y);  
        });
    }
 
 
    /*
     *	函数说明：显示文章列表
     *	参数：	                
     *	返回值：
     */
    function showArticleList(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            initGrid(id);
        }
    }
    /*
     *	函数说明：新建文章
     *	参数：	
     *	返回值：
     */
    function addNewArticle(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var channelId = treeNode.getId();
            var articleType = 0; // 普通文章

            var articleName = "文章";
            var articleId = new Date().valueOf();

            var returnValue = window.showModalDialog("article.htm",{title:"新建文章",isNew:true,editable:true,channelId:channelId,articleType:articleType,articleId:articleId,workflowStatus:"0"},"dialogWidth:900px;dialogHeight:700px;status:yes");
            if(true==returnValue){

                //如果当前grid显示为此文章所在栏目，则刷新grid
                var gridObj = $("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempChannelId = tempXmlIsland.getAttribute("channelId");
                    if(tempChannelId==channelId){
                        loadGridData(tempChannelId,"1");//默认第一页

                        //刷新工具条
                        onInactiveRow();
                    }
                }          
            }
        }
        
    }
    /*
     *	函数说明：删除栏目
     *	参数：	
     *	返回值：
     */
    function delChannel(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            
            var p = new HttpRequestParams();
            p.url = URL_DEL_CHANNEL;
            p.setContent("channelId",treeID);

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
     *	函数说明：删除站点
     *	参数：	
     *	返回值：
     */
    function delSite(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            
            var p = new HttpRequestParams();
            p.url = URL_DEL_SITE;
            p.setContent("siteId",treeID);

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
     *	函数说明：移动文章
     *	参数：	
     *	返回值：
     */
    function moveArticleTo(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var articleType = rowNode.getAttribute("state");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var oldChannelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==oldChannelId){
            oldChannelId = rowNode.getAttribute("channelId");
        }
        var action = "moveArticle";
        var xmlIsland = null;

        var channelID = window.showModalDialog("channeltree.htm",{id:oldChannelId,xmlIsland:xmlIsland,action:action},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=channelID){
            var p = new HttpRequestParams();
            p.url = URL_MOVE_ARTICLE;
            p.setContent("channelId",channelID.id);
            p.setContent("articleId",rowID);
            p.setContent("oldChannelId", oldChannelId);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var toolbarObj = $("gridToolBar");
                var curPage = toolbarObj.getCurrentPage();
                toolbarObj.gotoPage(curPage);

                //清除状态信息
                var block = Blocks.getBlock("statusContainer");
                if(null!=block){
                    block.clear();
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：移动栏目
     *	参数：	
     *	返回值：
     */
    function moveChannelTo(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var action = "moveChannel";
            var xmlIsland = null;

            var groupID = window.showModalDialog("channeltree.htm",{id:id,xmlIsland:xmlIsland,action:action},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=groupID){
                var p = new HttpRequestParams();
                p.url = URL_MOVE_CHANNEL;
                p.setContent("toChannelId", groupID.id);
                p.setContent("channelId", id);
                p.setContent("type", groupID.type);

                var request = new HttpRequest(p);
                request.onsuccess = function(){
                    //移动树节点
                    var curNode = treeObj.getTreeNodeById(id);
                    var xmlNode = new XmlNode(curNode.node);
                    var parentNode = treeObj.getTreeNodeById(groupID.id);
                    if(null!=parentNode){
                        var status = parentNode.getAttribute("disabled");
                        parentNode.node.appendChild(curNode.node);
                        parentNode.node.setAttribute("_open","true");

                        //目标父节点为站点或已停用栏目则更改移动节点状态
                        var flag = false;
                        var isSite = parentNode.getAttribute("isSite");
						 if("1"==status){//已停用
                                flag = true;
                            }
                        //更改移动节点状态
                        if(true==flag){
                            //设置当前节点状态
                            refreshTreeNodeState(xmlNode,status);

                            //下溯
                            var childs = xmlNode.selectNodes(".//treeNode");
                            for(var i=0,iLen=childs.length;i<iLen;i++){
                                refreshTreeNodeState(childs[i],status);
                            }
                        }

                        clearOperation(xmlNode);

                        treeObj.reload();
                    }
                }
                request.send();
            }
        }
    }
 
 
    /*
     *	函数说明：同一父节点下移动栏目
     *	参数：	
     *	返回值：
     */
    function sortChannelTo(eventObj){
        var treeObj = $("tree");
        var movedTreeNode = eventObj.movedTreeNode;
        var toTreeNode = eventObj.toTreeNode;
        var moveState = eventObj.moveState;
        var p = new HttpRequestParams();
        p.url = URL_SORT_CHANNEL;
        p.setContent("toChannelId", toTreeNode.getId());
        p.setContent("channelId", movedTreeNode.getId());
        p.setContent("direction", moveState); // -1目标上方,1目标下方

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //移动树节点
            treeObj.moveTreeNode(movedTreeNode, toTreeNode, moveState);
        }
        request.send();
    }
    /*
     *	函数说明：停用站点栏目
     *	参数：	
     *	返回值：
     */
    function stopSite(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var isSite = treeNode.getAttribute("isSite");

            var p = new HttpRequestParams();
            p.url = URL_STOP_SITE;
            p.setContent("id",id);
            p.setContent("isSite",isSite);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置停用状态
                var xmlNode = new XmlNode(treeNode.node);
                var treeNodes = xmlNode.selectNodes(".//treeNode");
                for(var i =0;i < treeNodes.length;i++){
                    refreshTreeNodeState(treeNodes[i],"1");
                }
                refreshTreeNodeState(xmlNode,"1");
                treeObj.reload();
 
            }
            request.send();
        }
    }
    /*
     *	函数说明：启用站点栏目
     *	参数：	
     *	返回值：
     */
    function startSite(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var isSite = treeNode.getAttribute("isSite");

            var p = new HttpRequestParams();
            p.url = URL_START_SITE;
            p.setContent("id",id);
            p.setContent("isSite",isSite);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var xmlNode = new XmlNode(treeNode.node);
                //设置停用状态
                while("1"!=isSite){
                    refreshTreeNodeState(xmlNode,"0");
                    xmlNode = xmlNode.getParent();
                    isSite = xmlNode.getAttribute("isSite");
                }
                refreshTreeNodeState(xmlNode,"0");
                treeObj.reload();
 
            }
            request.send();
        }
    }
    /*
     *	函数说明：启用全部
     *	参数：	
     *	返回值：
     */
    function startAll(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var isSite = treeNode.getAttribute("isSite");

            var p = new HttpRequestParams();
            p.url = URL_START_ALL;
            p.setContent("id",id);
            p.setContent("isSite",isSite);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var xmlNode = new XmlNode(treeNode.node);
                var treeNodes = xmlNode.selectNodes(".//treeNode");
                for(var i =0;i < treeNodes.length;i++){
                    refreshTreeNodeState(treeNodes[i],"0");
                }
                refreshTreeNodeState(xmlNode,"0");
                treeObj.reload();
 
            }
            request.send();
        }
    }
 
    /*
     *	函数说明：删除文章
     *	参数：	
     *	返回值：
     */
    function delArticle(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var state = rowNode.getAttribute("state");
        var channelId = rowNode.getAttribute("channelId");

        var p = new HttpRequestParams();
        p.url = URL_DEL_ARTICLE;
        p.setContent("articleId",rowID);
        p.setContent("articleType",state);
        p.setContent("channelId",channelId);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            var toolbarObj = $("gridToolBar");
            var curPage = toolbarObj.getCurrentPage();
            toolbarObj.gotoPage(curPage);
        }
        request.send();
    }
 
 
    /*
     *	函数说明：编辑树节点
     *	参数：	boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editTreeNode(editable){
        var isSite = getTreeAttribute("isSite");
        if("1"==isSite){
            editSiteInfo(editable);
        }else{
            editChannelInfo(editable);
        }
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
        var isSite = getTreeAttribute("isSite");
        if("1"==isSite){
            delSite();
        }else{
            delChannel();
        }
    
    }
 
    function addNewChannel(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){

            var kind = null;
            var isSite = getTreeAttribute("isSite");
            if("1"==isSite){
                kind = "site";
            }else{
                kind = "channel";
            }

            var parentID = treeNode.getId();
            var siteId = treeNode.getAttribute("siteId");
            var channelName = "栏目";
            var channelID = new Date().valueOf();

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadChannelDetailData(channelID,true,parentID,kind,true,siteId);
                },TIMEOUT_TAB_CHANGE);
            };
            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_ADD.replace(/\$label/i,channelName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_CHANNEL_DETAIL + channelID;
            var tab = ws.open(inf);
        }
    }
 
    function editChannelInfo(editable){
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
                    loadChannelDetailData(treeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                inf.SID = CACHE_VIEW_CHANNEL_DETAIL + treeID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_CHANNEL_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
 
    function loadChannelDetailData(treeID,editable,parentID,kind,isNew,siteId){
        if(false==editable){
            var cacheID = CACHE_VIEW_CHANNEL_DETAIL + treeID;
        }else{
            var cacheID = CACHE_CHANNEL_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_CHANNEL_DETAIL;
            if(true==isNew){
                p.setContent("isNew", "1");
                p.setContent("isSite", "0");
                p.setContent("siteId", siteId);
                p.setContent("parentId", parentID);
                p.setContent("kind", kind);
            }else{
                p.setContent("channelId", treeID);            
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var channelInfoNode = this.getNodeValue(XML_CHANNEL_INFO);

                var channelInfoNodeID = cacheID+"."+XML_CHANNEL_INFO;

                Cache.XmlIslands.add(channelInfoNodeID,channelInfoNode);

                Cache.Variables.add(cacheID,[channelInfoNodeID]);

                initChannelPages(cacheID,editable,parentID,kind,isNew);
            }
            request.send();
        }else{
            initChannelPages(cacheID,editable,parentID,kind,isNew);
        }
    }
 
    function initChannelPages(cacheID,editable,parentID,kind,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadChannelInfoFormData(cacheID,editable);
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
            saveChannel(cacheID,parentID,kind,isNew);
        }
    }
    /*
     *	函数说明：栏目信息xform加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function loadChannelInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_CHANNEL_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");
            loadPage1FormEvents("channel",cacheID);
        }
    }
    /*
     *	函数说明：保存栏目
     *	参数：	string:cacheID      缓存数据ID
                boolean:isNew       是否新增
     *	返回值：
     */
    function saveChannel(cacheID,parentID,kind,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }
        var p = new HttpRequestParams();
        p.setContent("isSite", "0");
        if(isNew==true){
            p.url = URL_SAVE_CHANNEL;
        }else {
            p.url = URL_UPDATE_CHANNEL;
        }
        
        //是否提交
        var flag = false;
        
        var channelCache = Cache.Variables.get(cacheID);
        if(null!=channelCache){       

            //栏目基本信息
            var channelInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_CHANNEL_INFO);
            if(null!=channelInfoNode){
                var channelInfoDataNode = channelInfoNode.selectSingleNode(".//data");
                if(null!=channelInfoDataNode){
                    flag = true;

                    var prefix = channelInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(channelInfoDataNode,prefix);

                    p.setContent("selectTreeId",parentID);
                    p.setContent("kind",kind);
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

                    var treeNode = this.getNodeValue(XML_CHANNEL_TREE).selectSingleNode("treeNode");
                    appendTreeNode(parentID,treeNode);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function(){
                if(true!=isNew){
                    //解除提醒
                    detachReminder(cacheID);

                    var page1FormObj = $("page1Form");

                    //更新树节点名称
                    var id = cacheID.trim(CACHE_CHANNEL_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
 
    /*
     *	函数说明：获取文章属性
     *	参数：	string:name         文章属性名
     *	返回值：string:value        文章属性值
     */
    function getArticleAttribute(name){
        var value = null;
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=rowIndex){
            var rowNode = gridObj.getRowNode_Xml(rowIndex);
            value = rowNode.getAttribute(name);
        }
        return value;
    }
     /*
     *	函数说明：置顶文章
     *	参数：	string:isTop        是否置顶(1是/0否)
     *	返回值：
     */
    function stickArticle(isTop){

        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");

            //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
            var channelId = gridObj.getXmlDocument().getAttribute("channelId");
            if("search"==channelId){
                channelId = curRowNode.getAttribute("channelId");
            }
            var isTop = curRowNode.getAttribute("isTop");

            var p = new HttpRequestParams();
            p.url = URL_STICK_ARTICLE;
            p.setContent("articleId",curRowID);
            p.setContent("isTop",isTop);
            p.setContent("channelId",channelId);

            var request = new HttpRequest(p);
            request.onsuccess = function(){

                gridObj.modifyNamedNode_Xml(curRowIndex,"isTop",isTop);

                var toolbarObj = $("gridToolBar");
                var curPage = toolbarObj.getCurrentPage();
                toolbarObj.gotoPage(curPage);

                //刷新工具条
                onInactiveRow();

            }
            request.send();
        }
    }
 
    /*
     *	函数说明：检测用户列表右键菜单项是否可见
     *	参数：	string:code     操作码
     *	返回值：
     */
    function getUserOperation(code){
        var flag = false;
        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var _operation = curRowNode.getAttribute("_operation");

            flag = checkOperation(code,_operation);
        }
        return flag;
    }
 
    /*
     *	函数说明：获取grid操作权限
     *	参数：	number:rowIndex         grid行号
                function:callback       回调函数
     *	返回值：
     */
    function getGridOperation(rowIndex,callback){
        var gridObj = $("grid");
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var id = rowNode.getAttribute("channelId");
        var _operation = rowNode.getAttribute("_operation");

        if(null==_operation || ""==_operation){//如果节点上还没有_operation属性，则发请求从后台获取信息
            var p = new HttpRequestParams();
            p.url = URL_GET_ARTICLE_OPERATION;
            p.setContent("channelId",id);

            var request = new HttpRequest(p);
            request.onresult = function(){
                _operation = this.getNodeValue(XML_OPERATION);
                rowNode.setAttribute("_operation",_operation);

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
     *	函数说明：发布文章
     *	参数：	string:type         区分完全发布“2”和增量发布“1”
     *	返回值：
     */
    function publishArticle(category){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();

            var p = new HttpRequestParams();
            
            var isSite = getTreeAttribute("isSite");
            if("1"==isSite){
                p.url = URL_SITE_PUBLISH_PROGRESS;
            }else{
                p.url = URL_CHANNEL_PUBLISH_PROGRESS;
            }
			p.setContent("channelId",id);
            p.setContent("category",category);
            var request = new HttpRequest(p);
            request.onresult = function(){
                var data = this.getNodeValue("ProgressInfo");
                var url = "";
                if("1"==isSite){
	                url = URL_SITE_PUBLISH_PROGRESS;
	            }else{
	                url = URL_CHANNEL_PUBLISH_PROGRESS;
	            }
	            var progress = new Progress(URL_GET_PROGRESS,data,URL_CONCEAL_PROGRESS);
                progress.oncomplete = function(){
                    //发布完成
					//如果当前grid显示为此文章所在栏目，则刷新grid
					var gridObj = $("grid");
					if(true==gridObj.hasData_Xml()){
						var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
						var tempChannelId = tempXmlIsland.getAttribute("channelId");
						if(tempChannelId==id){
							loadGridData(tempChannelId,"1");//默认第一页
						}
					}
                }
                progress.start();
            }
            request.send();
        }	
    }
    /*
     *	函数说明：搜索文章
     *	参数：	
     *	返回值：
     */
    function searchArticle(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var type = treeNode.getAttribute("isSite");
            var cacheID = CACHE_SEARCH_ARTICLE + treeID;
            var condition = window.showModalDialog("searcharticle.htm",{type:type,channelId:treeID,title:"搜索\""+treeName+"\"下的文章"},"dialogWidth:250px;dialogHeight:250px;");
            if(null!=condition){
                Cache.Variables.add("condition",condition);
                loadSearchGridData(cacheID,1);
            }
        }
    }
    /*
     *	函数说明：根据条件获取搜索结果
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
            p.url = URL_SEARCH_ARTICLE;

            var xmlReader = new XmlReader(condition.dataXml);
            var dataNode = new XmlNode(xmlReader.documentElement);
            p.setXFormContent(dataNode, condition.prefix);
            p.setContent("condition.page.pageNum", page);
            if(null!=sortName && null!=direction){
                p.setContent("condition.field", sortName);
                p.setContent("condition.orderType", direction);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var articleListNode = this.getNodeValue(XML_ARTICLE_LIST);
                var articleListNodeID = cacheID+"."+XML_ARTICLE_LIST;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                //给文章grid数据根节点增加channelId等属性
                articleListNode.setAttribute("channelId","search");

                //给当前排序列加上_direction属性
                if(null!=sortName && null!=direction){
                    var column = articleListNode.selectSingleNode("//column[@name='" + sortName + "']");
                    if(null!=column){
                        column.setAttribute("_direction",direction);
                    }
                }

                Cache.XmlIslands.add(articleListNodeID,articleListNode);
                Cache.XmlIslands.add(pageListNodeID,pageListNode);
                Cache.Variables.add(cacheID,[articleListNodeID,pageListNodeID]);

                
                initSearchGrid(cacheID);
            }
            request.send();;
        }
    }
    /*
     *	函数说明：初始化搜索用户grid
     *	参数：	string:cacheID      缓存数据id
     *	返回值：
     */
    function initSearchGrid(cacheID){
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridDataFromCache(cacheID);
            loadGridEvents();

            //刷新工具条
            onInactiveRow();
        });
    
    }
    /*
     *	函数说明：刷新树节点停用启用状态
     *	参数：	XmlNode:xmlNode         XmlNode实例
                string:state            停/启用状态
     *	返回值：
     */
    function refreshTreeNodeState(xmlNode,state){
        var isSite = xmlNode.getAttribute("isSite");
        if("1"==isSite){
            var img = "site";
        }else{
            var img = "channel";
        }
        xmlNode.setAttribute("disabled",state);
        xmlNode.setAttribute("icon",ICON + img + (state=="1"?"_2":"") + ".gif");    
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