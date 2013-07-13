    /*
     *	后台响应数据节点名称
     */
    XML_DEFAULT_TOOLBAR = "DefaultToolBar";
    XML_TOOLBAR = "ToolBar";
    XML_MAIN_TREE = "RuleTree";
    XML_PASSWORD_INFO = "PasswordInfo";
    XML_OPERATION = "Operation";

    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_PASSWORD_PERMISSION = "rulePermission__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_PASSWORD_DETAIL = "rule__id";
    CACHE_VIEW_PASSWORD_DETAIL = "viewRule__id";
    CACHE_PASSWORD_TO_USER_GRID = "rule2User__id";
    CACHE_SEARCH_PERMISSION = "searchPermission__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新增\"$label\"";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/password_init.xml";
    URL_PASSWORD_DETAIL = "data/password.xml";
    URL_SAVE_PERMISSION = "data/_success.xml";
    URL_SAVE_PASSWORD = "data/_success.xml";
    URL_STOP_PASSWORD = "data/_success.xml";
    URL_START_PASSWORD = "data/_success.xml";
    URL_DEL_PASSWORD = "data/_success.xml";
    URL_GROUP_TO_USER_LIST = "data/rule2userlist.xml";
    URL_GET_OPERATION = "data/operation.xml";

    URL_INIT = "ums/passwordrule!getAllRules.action";
    URL_PASSWORD_DETAIL = "ums/passwordrule!getRuleInfo.action";
    URL_SAVE_PASSWORD = "ums/passwordrule!saveRule.action";
    URL_STOP_PASSWORD = "ums/rule!disable.action";
    URL_START_PASSWORD = "ums/rule!disable.action";
    URL_DEL_PASSWORD = "ums/passwordrule!deleteRule.action";
    URL_GROUP_TO_USER_LIST = "ums/role!getUserByGroupId.action";
    URL_GET_OPERATION = "data/operation.xml";

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
        initToolBar();
        initNaviBar("ums.5");
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

            var ruleTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var ruleTreeNodeID = CACHE_MAIN_TREE;

            Cache.XmlIslands.add(ruleTreeNodeID,ruleTreeNode);

            loadToolBar(_operation);
            initTree(ruleTreeNodeID);
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

            //应用资源
            str[str.length] = "    <button id=\"b1\" code=\"1\" icon=\"" + ICON + "view.gif\" label=\"查看\" cmd=\"editPasswordInfo(false)\" enable=\"'-1'!=getTreeNodeId()\"/>";
            str[str.length] = "    <button id=\"b2\" code=\"2\" icon=\"" + ICON + "edit.gif\" label=\"编辑\" cmd=\"editPasswordInfo()\" enable=\"'-1'!=getTreeNodeId()\"/>";
            str[str.length] = "    <button id=\"b3\" code=\"3\" icon=\"" + ICON + "del.gif\" label=\"删除\" cmd=\"delPassword()\" enable=\"'-1'!=getTreeNodeId() &amp;&amp; '1'!=getTreeNodeId()\"/>";
            str[str.length] = "    <button id=\"b4\" code=\"4\" icon=\"" + ICON + "new_pwd.gif\" label=\"新建密码策略\" cmd=\"addNewPassword()\" enable=\"'-1'==getTreeNodeId()\"/>";
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
            label:"新建密码策略",
            callback:addNewPassword,
            enable:function(){return true;},
            visible:function(){return "-1"==getTreeNodeId() && true==getOperation("ur2");}
        }
        var item2 = {
            label:"删除",
            callback:delPassword,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && "1"!=getTreeNodeId() && true==getOperation("ur3");}
        }
        var item3 = {
            label:"编辑",
            callback:editPasswordInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && true==getOperation("ur4");}
        }
        var item4 = {
            label:"查看",
            callback:function(){
                editPasswordInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && true==getOperation("ur4");}
        }

        var treeObj = $("tree");

        var menu1 = new Menu();
        menu1.addItem(item4);
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item1);

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
     *	参数：	
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
     *	函数说明：编辑策略信息
     *	参数：  boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editPasswordInfo(editable){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeNodeID = treeNode.getId();
            if(isNaN(treeNodeID)){// 根节点不能编辑
                return;
            }
            var treeNodeName = treeNode.getName();

            var phases = [];
            phases[0] = {page:"page1",label:"基本信息"};

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadPasswordDetailData(treeNodeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeNodeName);
                inf.SID = CACHE_VIEW_PASSWORD_DETAIL + treeNodeID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeNodeName);
                inf.SID = CACHE_PASSWORD_DETAIL + treeNodeID;
            }
            inf.defaultPage = "page1";
            inf.phases = phases;
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
    function loadPasswordDetailData(treeID,editable,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_PASSWORD_DETAIL + treeID;
        }else{
            var cacheID = CACHE_PASSWORD_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_PASSWORD_DETAIL;
            //如果是新增
            if(true==isNew){
                p.setContent("isNew", "1");
            }else{
                p.setContent("id", treeID);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var ruleInfoNode = this.getNodeValue(XML_PASSWORD_INFO);

                var ruleInfoNodeID = cacheID+"."+XML_PASSWORD_INFO;

                Cache.XmlIslands.add(ruleInfoNodeID,ruleInfoNode);

                Cache.Variables.add(cacheID,[ruleInfoNodeID]);

                initPasswordPages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initPasswordPages(cacheID,editable,isNew);
        }
    }
    /*
     *	函数说明：用户组相关xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
     *	返回值：
     */
    function initPasswordPages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadPasswordInfoFormData(cacheID,editable);
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
            savePassword(cacheID,isNew);
        }
    }
    /*
     *	函数说明：策略信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadPasswordInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_PASSWORD_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
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

        showTreeNodeStatus({id:"ID",name:"名称",creatorName:"创建者",createTime:"创建时间",updatorName:"修改者",updateTime:"修改时间"});

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
        getTreeOperation(treeNode,function(_operation){
            var canAddNewRule = checkOperation("ur2",_operation);
            var canEdit = checkOperation("ur4",_operation);
            if("-1"!=id){
                editPasswordInfo(canEdit);
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

        showTreeNodeStatus({id:"ID",name:"名称",creatorName:"创建者",createTime:"创建时间",updatorName:"修改者",updateTime:"修改时间"});

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
     *	函数说明：保存策略
     *	参数：	string:cacheID      缓存数据ID
     *	返回值：
     */
    function savePassword(cacheID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_PASSWORD;
      
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){
        
            //策略基本信息
            var ruleInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_PASSWORD_INFO);
            if(null!=ruleInfoNode){
                var ruleInfoDataNode = ruleInfoNode.selectSingleNode(".//data");
                if(null!=ruleInfoDataNode){
                    flag = true;
                    
                    var prefix = ruleInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(ruleInfoDataNode,prefix);
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

                loadInitData();

                if(true==isNew){
                    var ws = $("ws");
                    ws.closeActiveTab();
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
     *	函数说明：新建策略
     *	参数：	
     *	返回值：
     */
    function addNewPassword(){
        var userName = "策略";
        var userID = new Date().valueOf();

        var phases = [];
        phases[0] = {page:"page1",label:"基本信息"};

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadPasswordDetailData(userID,true,true);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_ADD.replace(/\$label/i,userName);
        inf.phases = phases;
        inf.callback = callback;
        inf.SID = CACHE_PASSWORD_DETAIL + userID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：删除策略
     *	参数：	
     *	返回值：
     */
    function delPassword(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeNodeID = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_DEL_PASSWORD;
            p.setContent("id",treeNodeID);

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
     *	函数说明：获取节点id
     *	参数：  
     *	返回值：string:id   树节点id
     */
    function getTreeNodeId(){
        var id = getTreeAttribute("id");
        if(isNaN(id)){
            id = "-1";
        }
        return id;
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