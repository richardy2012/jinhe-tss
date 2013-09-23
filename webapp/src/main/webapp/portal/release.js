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

    function init(){
        initPaletteResize();
        initUserInfo();
        initToolBar();
        initNaviBar("portal.4");
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }
  
    function initMenus(){
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

        treeObj.contextmenu = menu1;
    }
 
    function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function(){
            var _operation = this.getNodeValue(XML_OPERATION);

            var groupTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var groupTreeNodeID = CACHE_MAIN_TREE;

            Cache.XmlIslands.add(groupTreeNodeID,groupTreeNode);

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
        request.send();
    }
 
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
 
    function loadTreeDetailData(treeID,editable,isNew){
 
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

                var page1FormObj = $X("page1Form", layoutInfoNode);
				attachReminder(treeID, page1FormObj);

				//设置保存按钮操作
				$$("page1BtSave").onclick = function() {
					saveMenu(treeID, parentId);
				}
            }
            request.send();
       
    }
 
 
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
 
    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();