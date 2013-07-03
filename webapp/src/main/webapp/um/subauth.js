    /*
     *	后台响应数据节点名称
     */
    XML_DEFAULT_TOOLBAR = "DefaultToolBar";
    XML_TOOLBAR = "ToolBar";
    XML_MAIN_TREE = "RuleTree";
    XML_RULE_INFO = "RuleInfo";
    XML_RULE_TO_ROLE_TREE = "Rule2RoleTree";
    XML_RULE_TO_ROLE_EXIST_TREE = "Rule2RoleExistTree";
    XML_RULE_TO_GROUP_TREE = "Rule2GroupTree";
    XML_RULE_TO_GROUP_EXIST_TREE = "Rule2GroupExistTree";
    XML_RULE_TO_USER_TREE = "Rule2UserTree";
    XML_RULE_TO_USER_EXIST_TREE = "Rule2UserExistTree";
    XML_SEARCH_PERMISSION = "SearchPermission";
    XML_RESOURCE_TYPE = "ResourceType";

    XML_GROUP_TO_USER_LIST_TREE = "Group2UserListTree";
    XML_RULE_TO_GROUP_IDS = "rule2GroupIds";
    XML_RULE_TO_USER_IDS = "rule2UserIds";
    XML_RULE_TO_ROLE_IDS = "rule2RoleIds";
    XML_OPERATION = "Operation";

    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_RULE_PERMISSION = "rulePermission__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_RULE_DETAIL = "rule__id";
    CACHE_VIEW_RULE_DETAIL = "viewRule__id";
    CACHE_RULE_TO_USER_GRID = "rule2User__id";
    CACHE_SEARCH_PERMISSION = "searchPermission__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新增\"$label\"";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    OPERATION_PERMISSION = "设置\"$label\"权限";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/reassign_init.xml";
    URL_RULE_DETAIL = "data/rule1.xml";
    URL_SAVE_RULE = "data/_success.xml";
    URL_STOP_RULE = "data/_success.xml";
    URL_START_RULE = "data/_success.xml";
    URL_DEL_RULE = "data/_success.xml";
    URL_GROUP_TO_USER_LIST = "data/rule2userlist.xml";
    URL_GET_OPERATION = "data/operation.xml";

    URL_INIT = "ums/rule!getSubAuthorizeStrategys2Tree.action";
    URL_RULE_DETAIL = "ums/rule!getSubAuthorizeStrategyInfo.action";
    URL_SAVE_RULE = "ums/rule!saveSubAuthorizeStrategy.action";
    URL_STOP_RULE = "ums/rule!disable.action";
    URL_START_RULE = "ums/rule!disable.action";
    URL_DEL_RULE = "ums/rule!delete.action";
    URL_GROUP_TO_USER_LIST = "ums/role!getUsersByGroupId.action";
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
        initNaviBar("ums.4");
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
            str[str.length] = "    <button id=\"b1\" code=\"ur1\" icon=\"" + ICON + "start.gif\" label=\"启用\" cmd=\"startRule()\" enable=\"'-1'!=getTreeNodeId() &amp;&amp; '1'==getRuleState()\"/>";
            str[str.length] = "    <button id=\"b2\" code=\"ur1\" icon=\"" + ICON + "stop.gif\" label=\"停用\" cmd=\"stopRule()\" enable=\"'-1'!=getTreeNodeId() &amp;&amp; '0'==getRuleState()\"/>";
            str[str.length] = "    <button id=\"b3\" code=\"ur4\" icon=\"" + ICON + "view.gif\" label=\"查看\" cmd=\"editRuleInfo(false)\" enable=\"'-1'!=getTreeNodeId()\"/>";
            str[str.length] = "    <button id=\"b4\" code=\"ur4\" icon=\"" + ICON + "edit.gif\" label=\"编辑\" cmd=\"editRuleInfo()\" enable=\"'-1'!=getTreeNodeId()\"/>";
            str[str.length] = "    <button id=\"b5\" code=\"ur3\" icon=\"" + ICON + "del.gif\" label=\"删除\" cmd=\"delRule()\" enable=\"'-1'!=getTreeNodeId()\"/>";
            str[str.length] = "    <button id=\"b6\" code=\"ur2\" icon=\"" + ICON + "new_rule.gif\" label=\"新建转授策略\" cmd=\"addNewRule()\" enable=\"'-1'==getTreeNodeId()\"/>";
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
            label:"停用",
            callback:stopRule,
            icon:ICON + "stop.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && "0"==getRuleState() && true==getOperation("ur1");}
        }
        var item2 = {
            label:"启用",
            callback:startRule,
            icon:ICON + "start.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && "1"==getRuleState() && true==getOperation("ur1");}
        }
        var item3 = {
            label:"新建转授策略",
            callback:addNewRule,
            enable:function(){return true;},
            visible:function(){return "-1"==getTreeNodeId() && true==getOperation("ur2");}
        }
        var item4 = {
            label:"删除",
            callback:delRule,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && true==getOperation("ur3");}
        }
        var item5 = {
            label:"编辑",
            callback:editRuleInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && true==getOperation("ur4");}
        }
        var item6 = {
            label:"查看",
            callback:function(){
                editRuleInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return "-1"!=getTreeNodeId() && true==getOperation("ur4");}
        }

        var treeObj = $("tree");

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addSeparator();
        menu1.addItem(item6);
        menu1.addItem(item5);
        menu1.addItem(item4);
        menu1.addSeparator();
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
    function editRuleInfo(editable){

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
            phases[1] = {page:"page3",label:"角色"};
            phases[2] = {page:"page4",label:"用户"};
            phases[3] = {page:"page2",label:"用户组"};

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadRuleDetailData(treeNodeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeNodeName);
                inf.SID = CACHE_VIEW_RULE_DETAIL + treeNodeID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeNodeName);
                inf.SID = CACHE_RULE_DETAIL + treeNodeID;
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
    function loadRuleDetailData(treeID,editable,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_RULE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_RULE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_RULE_DETAIL;
            //如果是新增
            if(true==isNew){
                p.setContent("isNew", "1");
            }else{
                p.setContent("strategyId", treeID);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var ruleInfoNode = this.getNodeValue(XML_RULE_INFO);
                var rule2RoleTreeNode = this.getNodeValue(XML_RULE_TO_ROLE_TREE);
                var rule2RoleExistTreeNode = this.getNodeValue(XML_RULE_TO_ROLE_EXIST_TREE);
                var rule2UserTreeNode = this.getNodeValue(XML_RULE_TO_GROUP_TREE);
                var rule2UserExistTreeNode = this.getNodeValue(XML_RULE_TO_USER_EXIST_TREE);
                var rule2GroupTreeNode = this.getNodeValue(XML_RULE_TO_GROUP_TREE);
                var rule2GroupExistTreeNode = this.getNodeValue(XML_RULE_TO_GROUP_EXIST_TREE);

                var GroupType1Node = rule2GroupTreeNode.selectSingleNode("//treeNode[@groupType='1']");
                if(null!=GroupType1Node){
                    GroupType1Node.setAttribute("canselected","0");
                }
                var GroupType2Node = rule2GroupTreeNode.selectSingleNode("//treeNode[@groupType='2']");
                if(null!=GroupType2Node){
                    GroupType2Node.setAttribute("canselected","0");
                }

                var ruleInfoNodeID = cacheID+"."+XML_RULE_INFO;
                var rule2RoleTreeNodeID = cacheID+"."+XML_RULE_TO_ROLE_TREE;
                var rule2RoleExistTreeNodeID = cacheID+"."+XML_RULE_TO_ROLE_EXIST_TREE;
                var rule2UserTreeNodeID = cacheID+"."+XML_RULE_TO_USER_TREE;
                var rule2UserExistTreeNodeID = cacheID+"."+XML_RULE_TO_USER_EXIST_TREE;
                var rule2GroupTreeNodeID = cacheID+"."+XML_RULE_TO_GROUP_TREE;
                var rule2GroupExistTreeNodeID = cacheID+"."+XML_RULE_TO_GROUP_EXIST_TREE;

                Cache.XmlIslands.add(ruleInfoNodeID,ruleInfoNode);
                Cache.XmlIslands.add(rule2RoleTreeNodeID,rule2RoleTreeNode);
                Cache.XmlIslands.add(rule2RoleExistTreeNodeID,rule2RoleExistTreeNode);
                Cache.XmlIslands.add(rule2UserTreeNodeID,rule2UserTreeNode);
                Cache.XmlIslands.add(rule2UserExistTreeNodeID,rule2UserExistTreeNode);
                Cache.XmlIslands.add(rule2GroupTreeNodeID,rule2GroupTreeNode);
                Cache.XmlIslands.add(rule2GroupExistTreeNodeID,rule2GroupExistTreeNode);

                Cache.Variables.add(cacheID,[ruleInfoNodeID,rule2RoleTreeNodeID,rule2RoleExistTreeNodeID,rule2UserTreeNodeID,rule2UserExistTreeNodeID,rule2GroupTreeNodeID,rule2GroupExistTreeNodeID]);

                initRulePages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initRulePages(cacheID,editable,isNew);
        }
    }
    /*
     *	函数说明：用户组相关xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                boolean:isNew               是否新增
     *	返回值：
     */
    function initRulePages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadRuleInfoFormData(cacheID,editable);
        });

        var page3TreeObj = $("page3Tree");
        Public.initHTC(page3TreeObj,"isLoaded","oncomponentready",function(){
            loadRule2RoleTreeData(cacheID);
        });

        var page3Tree2Obj = $("page3Tree2");
        Public.initHTC(page3Tree2Obj,"isLoaded","oncomponentready",function(){
            loadRule2RoleExistTreeData(cacheID);
        });

        var page2TreeObj = $("page2Tree");
        Public.initHTC(page2TreeObj,"isLoaded","oncomponentready",function(){
            loadRule2GroupTreeData(cacheID);
        });

        var page2Tree2Obj = $("page2Tree2");
        Public.initHTC(page2Tree2Obj,"isLoaded","oncomponentready",function(){
            loadRule2GroupExistTreeData(cacheID);
        });

        var page4TreeObj = $("page4Tree");
        Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function(){
            loadRule2UserTreeData(cacheID);
        });

        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page4Tree2Obj);
        });

        var page4Tree3Obj = $("page4Tree3");
        Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function(){
            loadRule2UserExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page2BtPrevObj = $("page2BtPrev");
        var page4BtPrevObj = $("page4BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page2BtNextObj = $("page2BtNext");
        var page4BtNextObj = $("page4BtNext");
        page1BtPrevObj.style.display = "none";
        page4BtPrevObj.style.display = "";
        page2BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page4BtNextObj.style.display = "";
        page2BtNextObj.style.display = "none";

        //设置搜索
        var page2BtSearchObj = $("page2BtSearch");
        var page2KeywordObj = $("page2Keyword");
        attachSearchTree(page2TreeObj,page2BtSearchObj,page2KeywordObj);

        //设置搜索
        var page4BtSearchObj = $("page4BtSearch");
        var page4KeywordObj = $("page4Keyword");
        attachSearchTree(page4TreeObj,page4BtSearchObj,page4KeywordObj);

        //设置搜索
        var page4BtSearch2Obj = $("page4BtSearch2");
        var page4Keyword2Obj = $("page4Keyword2");
        attachSearchTree(page4Tree2Obj,page4BtSearch2Obj,page4Keyword2Obj);

        //设置搜索
        var page3BtSearchObj = $("page3BtSearch");
        var page3KeywordObj = $("page3Keyword");
        attachSearchTree(page3TreeObj,page3BtSearchObj,page3KeywordObj);

        //设置添加按钮操作
        var page3BtAddObj = $("page3BtAdd");
        page3BtAddObj.disabled = editable==false?true:false;
        page3BtAddObj.onclick = function(){
            addPage3TreeNode();
        }

        //设置删除按钮操作
        var page3BtDelObj = $("page3BtDel");
        page3BtDelObj.disabled = editable==false?true:false;
        page3BtDelObj.onclick = function(){
            delPage3TreeNode();
        }

        //设置添加按钮操作
        var page2BtAddObj = $("page2BtAdd");
        page2BtAddObj.disabled = editable==false?true:false;
        page2BtAddObj.onclick = function(){
            addPage2TreeNode();
        }

        //设置删除按钮操作
        var page2BtDelObj = $("page2BtDel");
        page2BtDelObj.disabled = editable==false?true:false;
        page2BtDelObj.onclick = function(){
            delPage2TreeNode();
        }

        //设置添加按钮操作
        var page4BtAddObj = $("page4BtAdd");
        page4BtAddObj.disabled = editable==false?true:false;
        page4BtAddObj.onclick = function(){
             addPage4TreeNode();
        }

        //设置删除按钮操作
        var page4BtDelObj = $("page4BtDel");
        page4BtDelObj.disabled = editable==false?true:false;
        page4BtDelObj.onclick = function(){
             delPage4TreeNode();
        }

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page2BtSaveObj = $("page2BtSave");
        var page3BtSaveObj = $("page3BtSave");
        var page4BtSaveObj = $("page4BtSave");
        page1BtSaveObj.disabled = editable==false?true:false;
        page2BtSaveObj.disabled = editable==false?true:false;
        page3BtSaveObj.disabled = editable==false?true:false;
        page4BtSaveObj.disabled = editable==false?true:false;
        page1BtSaveObj.onclick = page2BtSaveObj.onclick = page3BtSaveObj.onclick = page4BtSaveObj.onclick = function(){
            saveRule(cacheID,isNew);
        }
    }
    /*
     *	函数说明：策略信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadRuleInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	函数说明：转授角色tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadRule2RoleTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_ROLE_TREE);
        if(null!=xmlIsland){
            var page3TreeObj = $("page3Tree");
            page3TreeObj.load(xmlIsland.node);
            page3TreeObj.research = true;
        }
    }
    /*
     *	函数说明：转授角色tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadRule2RoleExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_ROLE_EXIST_TREE);
        if(null!=xmlIsland){
            var page3Tree2Obj = $("page3Tree2");
            page3Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	函数说明：转授给用户组tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadRule2GroupTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_GROUP_TREE);
        if(null!=xmlIsland){
            var page2TreeObj = $("page2Tree");
            page2TreeObj.load(xmlIsland.node);
            page2TreeObj.research = true;
        }
    }
    /*
     *	函数说明：转授给用户组tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadRule2GroupExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_GROUP_EXIST_TREE);
        if(null!=xmlIsland){
            var page2Tree2Obj = $("page2Tree2");
            page2Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	函数说明：转授给用户tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadRule2UserTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_USER_TREE);
        if(null!=xmlIsland){
            var page4TreeObj = $("page4Tree");
            page4TreeObj.load(xmlIsland.node);
            page4TreeObj.research = true;

            page4TreeObj.onTreeNodeDoubleClick = function(eventObj){
                onPage4TreeNodeDoubleClick(eventObj);
            }
        }
    }
    /*
     *	函数说明：转授给用户tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadRule2UserExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_USER_EXIST_TREE);
        if(null!=xmlIsland){
            var page4Tree3Obj = $("page4Tree3");
            page4Tree3Obj.load(xmlIsland.node);
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
                editRuleInfo(canEdit);
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
     *	函数说明：点击页4用户组树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onPage4TreeNodeDoubleClick(eventObj){
        var treeObj = $("page4Tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            initPage4Tree2(id);
        }
    }
    /*
     *	函数说明：page4Tree2初始化
     *	参数：	string:id   相关树节点id
     *	返回值：
     */
    function initPage4Tree2(id){
        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","onload",function(){
            loadPage4Tree2Data(id);
        });
    }
    /*
     *	函数说明：tree加载数据
     *	参数：	string:treeID   相关树节点id
     *	返回值：
     */
    function loadPage4Tree2Data(treeID){
        var cacheID = CACHE_RULE_TO_USER_GRID + treeID;
        var treeGrid = Cache.Variables.get(cacheID);
//        if(null==treeGrid){
            var p = new HttpRequestParams();
            p.url = URL_GROUP_TO_USER_LIST;
			p.setContent("groupId", treeID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var sourceListNode = this.getNodeValue(XML_GROUP_TO_USER_LIST_TREE);

                var sourceListNodeID = cacheID+"."+XML_GROUP_TO_USER_LIST_TREE;

                Cache.XmlIslands.add(sourceListNodeID,sourceListNode);
                Cache.Variables.add(cacheID,sourceListNodeID);

                loadPage4Tree2DataFromCache(cacheID);
            }
            request.send();
//        }else{        
//            loadPage4Tree2DataFromCache(cacheID);
//        }
    }
    /*
     *	函数说明：tree从缓存加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadPage4Tree2DataFromCache(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_GROUP_TO_USER_LIST_TREE);
        if(null!=xmlIsland){
            var page4Tree2Obj = $("page4Tree2");
            page4Tree2Obj.load(xmlIsland.node);
            page4Tree2Obj.research = true;
        }
    }
    /*
     *	函数说明：保存策略
     *	参数：	string:cacheID      缓存数据ID
     *	返回值：
     */
    function saveRule(cacheID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_RULE;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache){
        
            //策略基本信息
            var ruleInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_INFO);
            if(null!=ruleInfoNode){
                var ruleInfoDataNode = ruleInfoNode.selectSingleNode(".//data");
                if(null!=ruleInfoDataNode){
                    flag = true;
                    
                    var prefix = ruleInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(ruleInfoDataNode,prefix);
                }
            }


            //转授角色
            var rule2RoleNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_ROLE_EXIST_TREE);
            if(null!=rule2RoleNode){
                var rule2RoleDataIDs = getTreeNodeIds(rule2RoleNode,"./treeNode//treeNode");
                if(0<rule2RoleDataIDs.length){
                    flag = true;
                    p.setContent(XML_RULE_TO_ROLE_IDS,rule2RoleDataIDs.join(","));
                }
            }


            //转授给用户
            var rule2UserNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_USER_EXIST_TREE);
            if(null!=rule2UserNode){
                var rule2UserDataIDs = getTreeNodeIds(rule2UserNode,"./treeNode//treeNode");
                if(0<rule2UserDataIDs.length){
                    flag = true;
                    p.setContent(XML_RULE_TO_USER_IDS,rule2UserDataIDs.join(","));
                }
            }


            //转授给用户组
            var rule2GroupNode = Cache.XmlIslands.get(cacheID+"."+XML_RULE_TO_GROUP_EXIST_TREE);
            if(null!=rule2GroupNode){
                var rule2GroupDataIDs = getTreeNodeIds(rule2GroupNode,"./treeNode//treeNode");
                if(0<rule2GroupDataIDs.length){
                    flag = true;
                    p.setContent(XML_RULE_TO_GROUP_IDS,rule2GroupDataIDs.join(","));
                }
            }
        
        
        }

        if(true==flag){
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            var page2BtSaveObj = $("page2BtSave");
            var page3BtSaveObj = $("page3BtSave");
            var page4BtSaveObj = $("page4BtSave");
            syncButton([page1BtSaveObj,page2BtSaveObj,page3BtSaveObj,page4BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);

                loadInitData();

                if(true==isNew){
                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
			/*
            request.onresult = function(){
                if(true==isNew){
                    var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    appendTreeNode("_rootId",treeNode);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function(){
                if(true!=isNew){
                    //更新树节点名称
                    var id = cacheID.trim(CACHE_RULE_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
			*/
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
     *	函数说明：获取策略状态
     *	参数：	
     *	返回值：
     */
    function getRuleState(){
        return getTreeAttribute("disabled");
    }
    /*
     *	函数说明：停用策略
     *	参数：	
     *	返回值：
     */
    function stopRule(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeNodeID = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_STOP_RULE;
            p.setContent("strategyId",treeNodeID);
            p.setContent("disabled","1");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置停用状态
                refreshTreeNodeState(treeNode,"1");
                treeObj.reload();

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：启用策略
     *	参数：	
     *	返回值：
     */
    function startRule(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeNodeID = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_START_RULE;
            p.setContent("strategyId",treeNodeID);
            p.setContent("disabled","0");

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                //设置停用状态
                refreshTreeNodeState(treeNode,"0");
                treeObj.reload();

                //刷新工具条
                loadToolBarData(treeNode);
            }
            request.send();
        }
    }
    /*
     *	函数说明：新建策略
     *	参数：	
     *	返回值：
     */
    function addNewRule(){
        var userName = "策略";
        var userID = new Date().valueOf();

        var phases = [];
        phases[0] = {page:"page1",label:"基本信息"};
        phases[1] = {page:"page3",label:"角色"};
        phases[2] = {page:"page4",label:"用户"};
        phases[3] = {page:"page2",label:"用户组"};

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadRuleDetailData(userID,true,true);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_ADD.replace(/\$label/i,userName);
        inf.phases = phases;
        inf.callback = callback;
        inf.SID = CACHE_RULE_DETAIL + userID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：删除策略
     *	参数：	
     *	返回值：
     */
    function delRule(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeNodeID = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_DEL_RULE;
            p.setContent("strategyId",treeNodeID);

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
     *	函数说明：删除page3里tree节点
     *	参数：	
     *	返回值：
     */
    function delPage3TreeNode(){
        removeTreeNode($("page3Tree2"));
    }
    /*
     *	函数说明：添加page3里tree节点
     *	参数：	
     *	返回值：
     */
    function addPage3TreeNode(){
        var page3Tree2Obj = $("page3Tree2");
        var page3TreeObj = $("page3Tree");
        var selectedNodes = page3TreeObj.getSelectedTreeNode();

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++){
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page3Tree2Obj,"id",id);
            if(false==sameAttributeTreeNode){
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page3Tree2Obj.getTreeNodeById("_rootId");
                if(null!=treeNode){
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page3Tree2Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload){
            page3Tree2Obj.reload();
        }
        page3TreeObj.reload();
    }
    /*
     *	函数说明：删除page2里tree节点
     *	参数：	
     *	返回值：
     */
    function delPage2TreeNode(){
        removeTreeNode($("page2Tree2"));
    }
    /*
     *	函数说明：添加page2里tree节点
     *	参数：	
     *	返回值：
     */
    function addPage2TreeNode(){
        var page2Tree2Obj = $("page2Tree2");
        var page2TreeObj = $("page2Tree");
        var selectedNodes = page2TreeObj.getSelectedTreeNode();

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++){
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page2Tree2Obj,"id",id);
            if(false==sameAttributeTreeNode){
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page2Tree2Obj.getTreeNodeById("_rootId");
                if(null!=treeNode){
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page2Tree2Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload){
            page2Tree2Obj.reload();
        }
        page2TreeObj.reload();
    }
    /*
     *	函数说明：删除page4里tree节点
     *	参数：	
     *	返回值：
     */
    function delPage4TreeNode(){
        removeTreeNode($("page4Tree3"));
    }
    /*
     *	函数说明：添加page4里tree节点
     *	参数：	
     *	返回值：
     */
    function addPage4TreeNode(){
        var page4Tree2Obj = $("page4Tree2");
        var page4Tree3Obj = $("page4Tree3");
        var selectedNodes = page4Tree2Obj.getSelectedTreeNode();

        var reload = false;
        for(var i=0,iLen=selectedNodes.length;i<iLen;i++){
            var curNode = selectedNodes[i];
            curNode.setSelectedState(0,true,true);

            var groupName = curNode.getName();
            var id = curNode.getId();

            var sameAttributeTreeNode = hasSameAttributeTreeNode(page4Tree3Obj,"id",id);
            if("_rootId"!=id && false==sameAttributeTreeNode){
                //至少有一行添加才刷新grid
                reload = true;

                var treeNode = page4Tree3Obj.getTreeNodeById("_rootId");
                if(null!=treeNode){
                    //排除子节点
                    var cloneNode = new XmlNode(curNode.node).cloneNode(false);
                    page4Tree3Obj.insertTreeNodeXml(cloneNode.toXml(),treeNode);
                }
            }
        }
        if(true==reload){
            page4Tree3Obj.reload();
        }
        page4Tree2Obj.reload();
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
     *	函数说明：刷新树节点停用启用状态
     *	参数：	treeNode:treeNode       treeNode实例
                string:state            停/启用状态
     *	返回值：
     */
    function refreshTreeNodeState(treeNode,state){
        treeNode.setAttribute("disabled",state);
        treeNode.setAttribute("icon",ICON + "rule"+(state=="0"?"":"_2")+".gif");       
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