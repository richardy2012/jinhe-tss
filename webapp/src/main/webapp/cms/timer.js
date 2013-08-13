    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "TacticTree";
    XML_INDEX_TACTIC_INFO = "IndexTacticInfo";
    XML_TIME_TACTIC_INFO = "TimeTacticInfo";
    XML_OPERATION = "Operation";
    XML_SEQUENCE_TREE = "SequenceTree";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_TIME_TACTIC = "treeNode__id";
    CACHE_VIEW_TIME_TACTIC = "viewTreeNode__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_INDEX_TACTIC = "indexTactic__id";
    CACHE_VIEW_INDEX_TACTIC = "viewIndexTactic__id";
    CACHE_SEQUENCE_TREE = "sequenceTree__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新建$label";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/indextactic_init.xml";
    URL_INDEX_PUBLISH_TACTIC = "data/indextactic.xml";
    URL_SAVE_INDEX_PUBLISH_TACTIC = "data/_success.xml";
    URL_UPDATE_INDEX_PUBLISH_TACTIC="data/_success.xml";
    URL_DEL_INDEX_PUBLISH_TACTIC = "data/_success.xml";
    URL_TIME_TACTIC = "data/timetactic.xml";
    URL_SAVE_TIME_TACTIC = "data/_success.xml";
    URL_UPDATE_TIME_TACTIC ="data/_success.xml";
    URL_DEL_TIME_TACTIC = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_START_TIME_TACTIC = "data/_success.xml";
    URL_STOP_TIME_TACTIC = "data/_success.xml";
    URL_START_INDEX_PUBLISH_TACTIC = "data/_success.xml";
    URL_STOP_INDEX_PUBLISH_TACTIC = "data/_success.xml";
    URL_INSTANT_INDEX = "data/progress.xml";
	URL_GET_PROGRESS_INDEX = "data/_success.xml";
    URL_CANCEL_INSTANT_INDEX = "data/_success.xml";

    URL_INIT = "timer!initTacticIndex.action";
    URL_INDEX_PUBLISH_TACTIC = "timer!getTacticIndexAndPublish.action";
    URL_SAVE_INDEX_PUBLISH_TACTIC = "timer!addTacticIndexAndPublish.action";
    URL_UPDATE_INDEX_PUBLISH_TACTIC= "timer!updateTacticIndexAndPublish.action";
    URL_DEL_INDEX_PUBLISH_TACTIC = "timer!removeTacticIndexAndPublish.action";
    URL_TIME_TACTIC = "timer!getTacticTime.action";
    URL_SAVE_TIME_TACTIC = "timer!addTacticTime.action";
    URL_UPDATE_TIME_TACTIC = "timer!updateTacticTime.action";
    URL_DEL_TIME_TACTIC = "timer!removeTacticTime.action";
    URL_GET_OPERATION = "data/operation.xml";
    URL_START_TIME_TACTIC = "timer!startTacticTime.action";
    URL_STOP_TIME_TACTIC = "timer!stopTacticTime.action";
    URL_START_INDEX_PUBLISH_TACTIC = "timer!startTacticIndexAndPublish.action";
    URL_STOP_INDEX_PUBLISH_TACTIC = "timer!stopTacticIndexAndPublish.action";
    URL_INSTANT_INDEX = "timer!instantTactic.action";
	URL_GET_PROGRESS_INDEX = "timer!getProgress.action";
    URL_CANCEL_INSTANT_INDEX = "timer!doConceal.action"; //"data/_success.xml";

    /*
     *	延时
     */
    TIMEOUT_TAB_CHANGE = 200;
    TIMEOUT_ARTICLE_TYPE_SEARCH = 200;
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
        initNaviBar("cms.3");
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

            var articleTypeTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var articleTypeTreeNodeID = CACHE_MAIN_TREE;

            Cache.XmlIslands.add(articleTypeTreeNodeID,articleTypeTreeNode);

            loadToolBar(_operation);
            initTree(articleTypeTreeNodeID);
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

            //策略
            str[str.length] = "    <button id=\"b1\" code=\"1\" icon=\"" + ICON + "start.gif\" label=\"启用\" cmd=\"startTreeNode()\" enable=\"'_rootId'!=getTreeAttribute('id') &amp;&amp; '0'!=getTreeAttribute('status')\"/>";
            str[str.length] = "    <button id=\"b2\" code=\"1\" icon=\"" + ICON + "stop.gif\" label=\"停用\" cmd=\"stopTreeNode()\" enable=\"'_rootId'!=getTreeAttribute('id') &amp;&amp; '0'==getTreeAttribute('status')\"/>";
            str[str.length] = "    <button id=\"b3\" code=\"1\" icon=\"" + ICON + "view.gif\" label=\"查看\" cmd=\"editTreeNode(false)\" enable=\"'_rootId'!=getTreeAttribute('id')\"/>";
            str[str.length] = "    <button id=\"b4\" code=\"1\" icon=\"" + ICON + "edit.gif\" label=\"编辑\" cmd=\"editTreeNode()\" enable=\"'_rootId'!=getTreeAttribute('id')\"/>";
            str[str.length] = "    <button id=\"b5\" code=\"1\" icon=\"" + ICON + "del.gif\" label=\"删除\" cmd=\"delTreeNode()\" enable=\"'_rootId'!=getTreeAttribute('id')\"/>";
            str[str.length] = "    <button id=\"b6\" code=\"1\" icon=\"" + ICON + "new_time_tactic.gif\" label=\"新建时间策略\" cmd=\"addNewTimeTactic()\" enable=\"'_rootId'==getTreeAttribute('id')\"/>";
            str[str.length] = "    <button id=\"b7\" code=\"1\" icon=\"" + ICON + "new_index_tactic.gif\" label=\"新建索引策略\" cmd=\"addNewIndexTactic('1')\" enable=\"'0'==getTreeAttribute('type')\"/>";
            str[str.length] = "    <button id=\"b8\" code=\"1\" icon=\"" + ICON + "new_publish_tactic.gif\" label=\"新建发布策略\" cmd=\"addNewIndexTactic('2')\" enable=\"'0'==getTreeAttribute('type')\"/>";
            str[str.length] = "    <button id=\"b10\" code=\"1\" icon=\"" + ICON + "new_expire_tactic.gif\" label=\"新建文章过期策略\" cmd=\"addNewIndexTactic('3')\" enable=\"'0'==getTreeAttribute('type')\"/>";
			str[str.length] = "    <button id=\"b9\" code=\"1\" icon=\"" + ICON + "instant.gif\" label=\"即时功能\" cmd=\"instantIndex()\" enable=\"'0'==getTreeAttribute('status') &amp;&amp; '_rootId'!=getTreeAttribute('id') &amp;&amp; '0'!=getTreeAttribute('type')\"/>";
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

        //时间策略相关
        var item1 = {
            label:"新建时间策略",
            callback:addNewTimeTactic,
            enable:function(){return true;},
            visible:function(){return "_rootId"==getTreeAttribute("id") && true==getOperation("1");}
        }

        //索引策略相关
        var item2 = {
            label:"新建策略",
            callback:null,
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("type") && true==getOperation("1");}
        }

        //公共
        var item3 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("1");}
        }
        var item4 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("1");}
        }
        var item5 = { 
            label:"启用",
            callback:startTreeNode,
            icon:ICON + "start.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeAttribute("status") && "_rootId"!=getTreeAttribute("id") && true==getOperation("1");}
        }
        var item6 = {
            label:"停用",
            callback:stopTreeNode,
            icon:ICON + "stop.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("status") && "_rootId"!=getTreeAttribute("id") && true==getOperation("1");}
        }
        var item7 = {
            label:"即时功能",
            callback:instantIndex,
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("status") && "_rootId" !=getTreeAttribute('id') && "0"!=getTreeAttribute("type") && true==getOperation("1");}
        }
        var item8 = {
            label:"查看",
            callback:function(){
                editTreeNode(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("1");}
        }
		var item9 = {
            label:"增量创建索引",
            callback:incrementCreateIndex,
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("status") && "_rootId" !=getTreeAttribute('id') && "1"==getTreeAttribute("type") && true==getOperation("1");}
        }

        var submenu1 = new Menu();
        var subitem1 = {
            label:"索引策略",
            callback:function(){
                addNewIndexTactic("1");
            },
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var subitem2 = {
            label:"发布策略",
            callback:function(){
                addNewIndexTactic("2");
            },
            enable:function(){return true;},
            visible:function(){return true;}
        }
		var subitem3 = {
            label:"文章过期策略",
            callback:function(){
                addNewIndexTactic("3");
            },
            enable:function(){return true;},
            visible:function(){return true;}
        }
        submenu1.addItem(subitem1);
        submenu1.addItem(subitem2);
		submenu1.addItem(subitem3);
        item2.submenu = submenu1;


        var menu1 = new Menu();
        menu1.addItem(item5);
        menu1.addItem(item6);
        menu1.addSeparator();
        menu1.addItem(item8);
        menu1.addItem(item3);
        menu1.addItem(item4);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item7);
		menu1.addItem(item9);

        var treeObj = $("tree");
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
     *	函数说明：新建索引/发布策略
     *	参数：	
     *	返回值：
     */
    function addNewIndexTactic(type){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var parentId = treeNode.getId();
        
            if("1"==type){
                var name = "索引策略";
            }else if("2"==type){
                var name = "发布策略";
            }else if("3"==type){
				var name = "文章过期策略";
			}
            var id = new Date().valueOf();

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadIndexTacticData(id,true,parentId,type,true);
                },TIMEOUT_TAB_CHANGE);
            };

            var phases = [];
            phases[0] = {page:"page1",label:"基本信息"};
            phases[1] = {page:"page2",label:"ID序列"};

            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_ADD.replace(/\$label/i,name);
            inf.phases = phases;
            inf.callback = callback;
            inf.SID = CACHE_INDEX_TACTIC + id;
            var tab = ws.open(inf);
        }
        
    }
    /*
     *	函数说明：显示索引策略详细信息
     *	参数：	boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editIndexTacticInfo(editable){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeId = treeNode.getId();
            var treeName = treeNode.getName();
			var type = treeNode.getAttribute("type");

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadIndexTacticData(treeId,editable,treeId,type);
                },TIMEOUT_TAB_CHANGE);
            };

            var phases = [];
            phases[0] = {page:"page1",label:"基本信息"};
            phases[1] = {page:"page2",label:"ID序列"};

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                inf.SID = CACHE_VIEW_INDEX_TACTIC + treeId;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_INDEX_TACTIC + treeId;
            }
            inf.defaultPage = "page1";
            inf.phases = phases;
            inf.callback = callback;
            var tab = ws.open(inf);
        }        
    }
    /*
     *	函数说明：索引/发布策略详细信息加载数据
     *	参数：	string:typeId               索引/发布策略id
                boolean:editable            是否可编辑(默认true)
                string:parentId             父节点id
                string:type                 策略类型(1索引/2发布)
                boolean:isNew               是否新增
     *	返回值：
     */
    function loadIndexTacticData(treeId,editable,parentId,type,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_INDEX_TACTIC + treeId;
        }else{
            var cacheID = CACHE_INDEX_TACTIC + treeId;
        }
        var dpropertyDetail = Cache.Variables.get(cacheID);
        if(null==dpropertyDetail){
            var p = new HttpRequestParams();
            p.url = URL_INDEX_PUBLISH_TACTIC;
            //如果是新增
            if(true==isNew){
                p.setContent("condition.tacticId", "0");
                p.setContent("condition.type",type);
            }else{
                p.setContent("condition.tacticId", treeId);
				p.setContent("condition.type",type);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var indexTacticInfoNode = this.getNodeValue(XML_INDEX_TACTIC_INFO);				
                var indexTacticInfoNodeID = cacheID+"."+XML_INDEX_TACTIC_INFO;

                var sequenceTreeNode = this.getNodeValue(XML_SEQUENCE_TREE);
                var sequenceTreeNodeID = cacheID+"."+XML_SEQUENCE_TREE;
                
                Cache.XmlIslands.add(indexTacticInfoNodeID,indexTacticInfoNode);
                Cache.XmlIslands.add(sequenceTreeNodeID,sequenceTreeNode);

                Cache.Variables.add(cacheID,[indexTacticInfoNodeID,sequenceTreeNodeID]);

                initIndexTacticsPages(cacheID,editable,parentId,type,isNew);
            }
            request.send();
        }else{
            initIndexTacticsPages(cacheID,editable,parentId,type,isNew);
        }
    }
    /*
     *	函数说明：索引/发布策略相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                string:parentId             父节点id
				string:type				    策略类型
                boolean:isNew               是否新增
     *	返回值：
     */
    function initIndexTacticsPages(cacheID,editable,parentId,type,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadIndexTacticsInfoFormData(cacheID,editable);
        });

        var page2TreeObj = $("page2Tree");
        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function(){
            loadSequenceTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page2BtPrevObj = $("page2BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page2BtNextObj = $("page2BtNext");
        page1BtPrevObj.style.display = "none";
        page2BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page2BtNextObj.style.display = "none";

        //设置搜索
        var page2BtSearchObj = $("page2BtSearch");
        var page2KeywordObj = $("page2Keyword");
        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page2BtSaveObj = $("page2BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page2BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page2BtSaveObj.onclick = function(){
            saveIndexTactic(cacheID,parentId,type,isNew);
        }

    }
    /*
     *	函数说明：索引/发布策略信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadIndexTacticsInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_INDEX_TACTIC_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
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

        showTreeNodeStatus({id:"ID",name:"名称" });

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
        var id = getTreeAttribute("id");
        getTreeOperation(treeNode,function(_operation){
            var canEdit = checkOperation("1",_operation);
            if("_rootId"!=id){
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

        showTreeNodeStatus({id:"ID",name:"名称" });

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
     *	函数说明：保存索引/发布策略
     *	参数：	string:cacheID          缓存数据ID
                string:parentId         父节点id
				string:type				策略类型
                boolean:isNew           是否新增
     *	返回值：
     */
    function saveIndexTactic(cacheID,parentId,type,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        if(isNew == true){
            p.url = URL_SAVE_INDEX_PUBLISH_TACTIC;
        }else{
            p.url = URL_UPDATE_INDEX_PUBLISH_TACTIC;
        }

        //是否提交
        
        var flag = false;

        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){

            //索引/发布策略基本信息
            var indexTacticInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_INDEX_TACTIC_INFO);
            if(null!=indexTacticInfoNode){
                var indexTacticInfoDataNode = indexTacticInfoNode.selectSingleNode(".//data");
                if(null!=indexTacticInfoDataNode){
                    flag = true;

                    var prefix = indexTacticInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(indexTacticInfoDataNode,prefix);
                    p.setContent("condition.parentId",parentId);
					p.setContent("condition.type",type);
                }
            }

            //id序列
            var sequenceTreeNode = Cache.XmlIslands.get(cacheID+"."+XML_SEQUENCE_TREE);
            if(null!=sequenceTreeNode){
                var condition = "@checktype='1' and @id!='_rootId' and @isSite='0'";
                var sequenceTreeIDs = getTreeNodeIds(sequenceTreeNode,"./treeNode//treeNode[" + condition + "]");
                if(0 < sequenceTreeIDs.length){
                    p.setContent("condition.strategy.tacticIndex", sequenceTreeIDs.join(","));
                }
            }
        }
        
        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            var page2BtSaveObj = $("page2BtSave");
            syncButton([page1BtSaveObj,page2BtSaveObj],request);

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

                    var page1FormObj = $("page1Form");
                    //更新树节点名称
                    var id = cacheID.trim(CACHE_INDEX_TACTIC);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
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
     *	函数说明：编辑树节点
     *	参数：	boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editTreeNode(editable){
        var type = getTreeAttribute("type");
        if("0"==type){
            editTimeTacticInfo(editable);
        }else{
            editIndexTacticInfo(editable);
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
        var type = getTreeAttribute("type");
        if("0"==type){
            delTimeTactic();
        }else{
            delIndexTactic();
        }
    
    }
    /*
     *	函数说明：新建时间策略
     *	参数：	
     *	返回值：
     */
    function addNewTimeTactic(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var name = "时间策略";
            var id = new Date().valueOf();

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadTimeTacticData(id,true,true);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_ADD.replace(/\$label/i,name);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_TIME_TACTIC + id;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：编辑时间策略信息
     *	参数：  boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editTimeTacticInfo(editable){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeId = treeNode.getId();
            var treeName = treeNode.getName();

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadTimeTacticData(treeId,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                inf.SID = CACHE_VIEW_TIME_TACTIC + treeId;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_TIME_TACTIC + treeId;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：时间策略详细信息加载数据
     *	参数：	string:treeId               树节点id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
     *	返回值：
     */
    function loadTimeTacticData(treeId,editable,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_TIME_TACTIC + treeId;
        }else{
            var cacheID = CACHE_TIME_TACTIC + treeId;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_TIME_TACTIC;
            if(true==isNew){
                p.setContent("condition.tacticId", "0");
            }else{
                p.setContent("condition.tacticId", treeId);            
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var timeTacticInfoNode = this.getNodeValue(XML_TIME_TACTIC_INFO);
                var timeTacticInfoNodeID = cacheID+"."+XML_TIME_TACTIC_INFO;

                Cache.XmlIslands.add(timeTacticInfoNodeID,timeTacticInfoNode);

                Cache.Variables.add(cacheID,[timeTacticInfoNodeID]);

                initTimeTacticPages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initTimeTacticPages(cacheID,editable,isNew);
        }
    }
    /*
     *	函数说明：时间策略相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
     *	返回值：
     */
    function initTimeTacticPages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadTimeTacticInfoFormData(cacheID,editable);
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
            saveTimeTactic(cacheID,isNew);
        }
    }
    /*
     *	函数说明：时间策略信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadTimeTacticInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_TIME_TACTIC_INFO);
        if(null!=xmlIsland){
            var data = xmlIsland.selectSingleNode("data");
            var frequency = data.getCDATA("frequency");
            refreshDayColumn(cacheID,frequency);

            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            page1FormObj.ondatachange = function(){
                //2007-3-1 离开提醒
                attachReminder(cacheID);

                var name = event.result.name;
                var value = event.result.newValue;
                switch(name){
                    case "custom":
                        refreshColumnEditable(value);
                        if("0"==value){
                            var exp = getTacticExpression();
                            page1FormObj.updateDataExternal("tacticIndex",exp);
                        }
                        break;
                    case "frequency":
                        refreshDayColumn(cacheID,value);
                        page1FormObj.reload();

                        var exp = getTacticExpression();
                        page1FormObj.updateDataExternal("tacticIndex",exp);
                        break;
                    case "day":
                    case "time":
                        var exp = getTacticExpression();
                        page1FormObj.updateDataExternal("tacticIndex",exp);
                        break;
                }
            }

            //有值则可编辑，无值不可编辑
            var tacticIndex = page1FormObj.getData("tacticIndex");
            if(null!=tacticIndex && ""!=tacticIndex){
                page1FormObj.updateDataExternal("custom","1",true);
            }else{
                page1FormObj.updateDataExternal("custom","0",true);
            }
        }
    }
    /*
     *	函数说明：刷新列是否可编辑
     *	参数：	string:custom           是否自定义设置
     *	返回值：
     */
    function refreshColumnEditable(custom){
        var page1FormObj = $("page1Form");
        if("1"==custom){
            page1FormObj.setColumnEditable("tacticIndex","true");
            page1FormObj.setColumnEditable("frequency","false");
            page1FormObj.setColumnEditable("day","false");
            page1FormObj.setColumnEditable("time","false");
        }else{
            page1FormObj.setColumnEditable("tacticIndex","false");
            page1FormObj.setColumnEditable("frequency","true");
            page1FormObj.setColumnEditable("day","true");
            page1FormObj.setColumnEditable("time","true");                       
        }    
    }
    /*
     *	函数说明：生成时间策略表达式
     *	参数：	
     *	返回值：
     */
    function getTacticExpression(){
        var page1FormObj = $("page1Form");
        var frequency = page1FormObj.getData("frequency");
        var day = page1FormObj.getData("day");
        var time = page1FormObj.getData("time");
        var exp = "";

        //时分秒
        exp += "0 0 " + time;
        switch(frequency){
            //每天
            case "1":
                exp += " * * ?";
                break;
            //每周
            case "2":
                exp += " ? * " + day;
                break;
            //每月
            case "3":
                exp += " " + day + " * ?";
                break;
        }

        return exp;    
    }
    /*
     *	函数说明：根据时间策略频率刷新日期列表
     *	参数：	string:cacheID          缓存数据id
                string:frequency        频率
     *	返回值：
     */
    function refreshDayColumn(cacheID,frequency){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_TIME_TACTIC_INFO);
        if(null!=xmlIsland){
            var day = xmlIsland.selectSingleNode("declare/column[@name='day']");
            switch(frequency){
                case "1":
                default:
                    day.setAttribute("editorvalue","1");
                    day.setAttribute("editortext","当天");
                    break;
                case "2":
                    day.setAttribute("editorvalue","1|2|3|4|5|6|7");
                    day.setAttribute("editortext","周一|周二|周三|周四|周五|周六|周日");
                    break;
                case "3":
                    day.setAttribute("editorvalue","1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|W");
                    day.setAttribute("editortext","1号|2号|3号|4号|5号|6号|7号|8号|9号|10号|11号|12号|13号|14号|15号|16号|17号|18号|19号|20号|21号|22号|23号|24号|25号|26号|27号|28号|29号|30号|31号|月底");
                    break;
            }
        }
    }
    /*
     *	函数说明：id序列tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadSequenceTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_SEQUENCE_TREE);
        if(null!=xmlIsland){
            var page2TreeObj = $("page2Tree");
            page2TreeObj.load(xmlIsland.node);
            page2TreeObj.research = true;

            //设置默认半选状态
            var ids = page2TreeObj.getIds(false,true);
            page2TreeObj.loadDefaultCheckedByIds(ids,true,true);

            page2TreeObj.onChange = function(eventObj){
                var treeNode = eventObj.treeNode;
                var id = treeNode.getId();
                var checkType = treeNode.getAttribute("checktype");

                var childs = treeNode.node.selectNodes(".//treeNode");
                for(var i=0,iLen=childs.length;i<iLen;i++){
                    childs[i].setAttribute("checktype",checkType);
                }
                page2TreeObj.reload();
            }
        }
    }
    /*
     *	函数说明：保存时间策略
     *	参数：	string:cacheID      缓存数据ID
                boolean:isNew       是否新增
     *	返回值：
     */
    function saveTimeTactic(cacheID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        if(isNew==true){
            p.url = URL_SAVE_TIME_TACTIC;
        }else{
            p.url = URL_UPDATE_TIME_TACTIC;
        }
        

        //是否提交
        var flag = false;
        
        var timeCache = Cache.Variables.get(cacheID);
        if(null!=timeCache){       

            //时间策略基本信息
            var timeInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_TIME_TACTIC_INFO);
            if(null!=timeInfoNode){
                var timeInfoDataNode = timeInfoNode.selectSingleNode(".//data");
                if(null!=timeInfoDataNode){
                    flag = true;

                    var prefix = timeInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(timeInfoDataNode,prefix);
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
                    var id = cacheID.trim(CACHE_TIME_TACTIC);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：删除时间策略
     *	参数：	
     *	返回值：
     */
    function delTimeTactic(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeId = treeNode.getId();
            
            var p = new HttpRequestParams();
            p.url = URL_DEL_TIME_TACTIC;
            p.setContent("condition.tacticId",treeId);

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
     *	函数说明：删除索引/发布策略
     *	参数：	
     *	返回值：
     */
    function delIndexTactic(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeId = treeNode.getId();
            
            var p = new HttpRequestParams();
            p.url = URL_DEL_INDEX_PUBLISH_TACTIC;
            p.setContent("condition.tacticId",treeId);

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
     *	函数说明：停用树节点
     *	参数：	
     *	返回值：
     */
    function stopTreeNode(){
        var type = getTreeAttribute("type");
        if("0"==type){
            stopTimeTactic();
        }else{
            stopIndexTactic();
        }
    }
    /*
     *	函数说明：启用树节点
     *	参数：	
     *	返回值：
     */
    function startTreeNode(){
        var type = getTreeAttribute("type");
        if("0"==type){
            startTimeTactic();
        }else{
            startIndexTactic();
        }
    }
    /*
     *	函数说明：启用时间策略节点
     *	参数：	
     *	返回值：
     */
    function startTimeTactic(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var p = new HttpRequestParams();
            p.url = URL_START_TIME_TACTIC;
            p.setContent("condition.tacticId",id);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置停用状态
                var xmlNode = new XmlNode(treeNode.node);
                var treeNodes = xmlNode.selectNodes(".//treeNode");
                for(var i =0;i < treeNodes.length;i++){
                    refreshTreeNodeState(treeNodes[i],"0");
                }
                refreshTreeNodeState(xmlNode,"0");
                treeObj.reload();

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：停用时间策略节点
     *	参数：	
     *	返回值：
     */
    function stopTimeTactic(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var p = new HttpRequestParams();
            p.url = URL_STOP_TIME_TACTIC;
            p.setContent("condition.tacticId",id);

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

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：启用索引/发布策略节点
     *	参数：	
     *	返回值：
     */
    function startIndexTactic(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var p = new HttpRequestParams();
            p.url = URL_START_INDEX_PUBLISH_TACTIC;
            p.setContent("condition.tacticId",id);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置停用状态
                var xmlNode = new XmlNode(treeNode.node);
                refreshTreeNodeState(xmlNode,"0");

                xmlNode = xmlNode.getParent();
                refreshTreeNodeState(xmlNode,"0");

                treeObj.reload();

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：停用索引/发布策略节点
     *	参数：	
     *	返回值：
     */
    function stopIndexTactic(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var p = new HttpRequestParams();
            p.url = URL_STOP_INDEX_PUBLISH_TACTIC;
            p.setContent("condition.tacticId",id);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置停用状态
                var xmlNode = new XmlNode(treeNode.node);
                refreshTreeNodeState(xmlNode,"1");
                treeObj.reload();

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：即时索引功能
     *	参数：	
     *	返回值：
     */
    function instantIndex(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_INSTANT_INDEX;
            p.setContent("condition.tacticId",id);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var data = this.getNodeValue("ProgressInfo");
	            var progress = new Progress(URL_GET_PROGRESS_INDEX,data,URL_CANCEL_INSTANT_INDEX);
                progress.oncomplete = function(){
                    //发布完成
                }
                progress.start();
            }
            request.send();
        }
    }
	
	/*
     *	函数说明：手动增量创建索引功能
     *	参数：	
     *	返回值：
     */
    function incrementCreateIndex(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_INSTANT_INDEX;
            p.setContent("condition.tacticId", id);
			p.setContent("condition.increment", "1");

            var request = new HttpRequest(p);
            request.onresult = function(){
                var data = this.getNodeValue("ProgressInfo");
	            var progress = new Progress(URL_GET_PROGRESS_INDEX,data,URL_CANCEL_INSTANT_INDEX);
                progress.oncomplete = function(){
                    //完成
                }
                progress.start();
            }
            request.send();
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
        switch(type){
            case "0":
                var img = "time_tactic";
                break;
            case "1":
                var img = "index_tactic";
                break;
            case "2":
                var img = "publish_tactic";
                break;
            case "3":
                var img = "expire_tactic";
                break;
        }
        xmlNode.setAttribute("status",state);
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