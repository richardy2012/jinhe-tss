    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "MessageTree";
    XML_MESSAGE_LIST = "MessageList";
    XML_GRID_SEARCH = "GridSearch";
    XML_SEARCH_MESSAGE = "SearchUserList";
    XML_OPERATION = "Operation";
    XML_PAGE_LIST = "PageList";

    XML_MESSAGE_INFO = "MessageInfo";
    XML_REPLY_INFO = "MessageInfo";
    XML_FORWARD_INFO = "MessageInfo";

    XML_GROUP_INFO = "GroupInfo";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_VIEW_GRID_ROW_DETAIL = "viewRow__id";
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_VIEW_TREE_NODE_DETAIL = "viewTreeNode__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_SEARCH_MESSAGE = "searchMessage__id";
    CACHE_REPLY_DETAIL = "reply__id";
    CACHE_FORWARD_DETAIL = "forward__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新建$label";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    OPERATION_REPLY = "回复\"$label\"";
    OPERATION_FORWARD = "转发\"$label\"";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/message_init.xml";
    URL_MESSAGE_LIST = "data/message_list.xml";
    URL_MESSAGE_DETAIL = "data/message.xml";
 
    URL_MESSAGE_SEARCH = "data/gridsearch.xml";
    URL_SAVE_MESSAGE = "data/_success.xml";
    URL_SAVE_GROUP = "data/_success.xml";
    URL_SORT_MESSAGE = "data/_success.xml";
    URL_SORT_MESSAGE_CROSS_PAGE = "data/_success.xml";
    URL_DEL_MESSAGE = "data/_success.xml";
    URL_SEARCH_MESSAGE = "data/message_list.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_MESSAGE_OPERATION = "data/operation.xml";
    URL_REPLY_DETAIL = "data/message_reply.xml";
    URL_SAVE_REPLY = "data/_success.xml";
    URL_FORWARD_DETAIL = "data/message_forward.xml";
    URL_SAVE_FORWARD = "data/_success.xml";

    
    URL_INIT = "data/message_init.xml";
    URL_MESSAGE_LIST = "ums/message!getMessageList.action";
    URL_MESSAGE_DETAIL = "ums/message!getMessageInfo.action";
 
    URL_MESSAGE_SEARCH = "data/gridsearch.xml";
    URL_SAVE_MESSAGE = "ums/message!saveMessage.action";
    URL_SAVE_GROUP = "data/_success.xml";
    URL_SORT_MESSAGE = "data/_success.xml";
    URL_SORT_MESSAGE_CROSS_PAGE = "data/_success.xml";
    URL_DEL_MESSAGE = "ums/message!deleteMessage.action";
    URL_SEARCH_MESSAGE = "data/message_list.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_MESSAGE_OPERATION = "data/operation.xml";
    URL_REPLY_DETAIL = "data/message_reply.xml";
    URL_SAVE_REPLY = "data/_success.xml";
    URL_FORWARD_DETAIL = "data/message_forward.xml";
    URL_SAVE_FORWARD = "data/_success.xml";

 
    function init(){
        initPaletteResize();
        initListContainerResize();
        initNaviBar();
        initMenus();
        initBlocks();
        initWorkSpace();
        initEvents();
        initFocus();

        loadInitData();
    }
    /*
     *	函数说明：页面初始化加载数据(包括工具条、树)
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
        initTreeMenu();
        initGridMenu();
    }
 
    function initTreeMenu(){
        var item1 = {
            label:"写新消息",
            callback:addNewMessage,
            visible:function(){return "1"==getTreeId();}
        }
        var item2 = {
            label:"浏览消息",
            callback:showMessageList,
            icon:ICON + "view_list.gif",
            visible:function(){return "1"!=getTreeId();}
        }
 

        var treeObj = $("tree");

        var menu1 = new Menu();
        menu1.addItem(item1);
 
        menu1.addItem(item2);
 

        treeObj.contextmenu = menu1;
    }
 
    function initGridMenu(){
        var gridObj = $("grid");
        var item1 = {
            label:"查看",
            callback:function(){
                editMessageInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return ("2"==getMessageBoxId() || "4"==getMessageBoxId()) && true==getMessageOperation("m3");}
        }
        var item2 = {
            label:"编辑",
            callback:editMessageInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "3"==getMessageBoxId() && true==getMessageOperation("m2");}
        }
        var item3 = {
            label:"删除",
            callback:delMessage,
            icon:ICON + "del.gif",
            enable:function(){return gridObj.canDelete();},
            visible:function(){return true==getMessageOperation("m1");}
        }
        var item4 = {
            label:"回复",
            callback:replyMessage,
            icon:ICON + "reply.gif",
            enable:function(){return true;},
            visible:function(){return "2"==getMessageBoxId() && true==getMessageOperation("m4");}
        }
        var item5 = {
            label:"转发",
            callback:forwardMessage,
            icon:ICON + "forward.gif",
            enable:function(){return true;},
            visible:function(){return "2"==getMessageBoxId() && true==getMessageOperation("m5");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addSeparator();
        menu1.addItem(item4);
        menu1.addItem(item5);
		
        gridObj.contextmenu = menu1;
    }
 
    function initGrid(id){
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridEvents();
            loadGridData(id,"1");//默认第1页
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
        gridObj.onrightclickrow = function(){
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
     *	参数：	string:treeID               grid数据相关树节点id
                string:page                 页码
                string:sortName             排序字段
                string:direction            排序方向
     *	返回值：
     */
    function loadGridData(treeID,page,sortName,direction){
        var cacheID = CACHE_TREE_NODE_GRID + treeID;
        var treeGrid = Cache.Variables.get(cacheID);
 
            var p = new HttpRequestParams();
            p.url = URL_MESSAGE_LIST;
            p.setContent("boxId", treeID);
            p.setContent("page", page);
            if(null!=sortName && null!=direction){
                p.setContent("field", sortName);
                p.setContent("orderType", direction);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var msgListNode = this.getNodeValue(XML_MESSAGE_LIST);
                var msgListNodeID = cacheID+"."+XML_MESSAGE_LIST;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                //给消息grid数据根节点增加boxId等属性
                msgListNode.setAttribute("boxId",treeID);

                //给当前排序列加上_direction属性
                if(null!=sortName && null!=direction){
                    var column = msgListNode.selectSingleNode("//column[@name='" + sortName + "']");
                    if(null!=column){
                        column.setAttribute("_direction",direction);
                    }
                }

                Cache.XmlIslands.add(msgListNodeID,msgListNode);
                Cache.XmlIslands.add(pageListNodeID,pageListNode);
                Cache.Variables.add(cacheID,[msgListNodeID,pageListNodeID]);

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
        //重新创建grid工具条
        createGridToolBar(cacheID);

        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_MESSAGE_LIST);
        if(null!=xmlIsland){
            var gridObj = $("grid");
            gridObj.load(xmlIsland.node,null,"node");

            Focus.focus("gridTitle");
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
                var gridObj = $("grid");

                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempBoxId = tempXmlIsland.getAttribute("boxId");
                    var sortName = tempXmlIsland.getAttribute("sortName");
                    var direction = tempXmlIsland.getAttribute("direction");
                    if("search"!=tempBoxId){
                        //清除该组消息grid缓存
                        delCacheData(CACHE_TREE_NODE_GRID + tempBoxId);

                        loadGridData(tempBoxId,page,sortName,direction);

                        //刷新工具条
                        onInactiveRow();
                    }else{
                        loadSearchGridData(cacheID,page,sortName,direction,tempBoxId);
                    }
                }            
            });
        }
    }
 
    function editMessageInfo(editable){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var title = gridObj.getNamedNodeValue_Xml(rowIndex,"title");
        var id = rowNode.getAttribute("id");
        var boxId = gridObj.getXmlDocument().getAttribute("boxId");

        var phases = null;

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadMessageDetailData(id,editable,false);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        if(false==editable){
            inf.label = OPERATION_VIEW.replace(/\$label/i,title);
            inf.SID = CACHE_VIEW_GRID_ROW_DETAIL + id;
        }else{
            inf.label = OPERATION_EDIT.replace(/\$label/i,title);
            inf.SID = CACHE_GRID_ROW_DETAIL + id;
        }
        inf.defaultPage = "page1";
        inf.phases = phases;
        inf.callback = callback;
        var tab = ws.open(inf);
        
    }
 
    function loadMessageDetailData(id,editable,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_GRID_ROW_DETAIL + id;
        }else{
            var cacheID = CACHE_GRID_ROW_DETAIL + id;
        }
        var msgDetail = Cache.Variables.get(cacheID);
        if(null==msgDetail){
            var p = new HttpRequestParams();
            p.url = URL_MESSAGE_DETAIL;
            //如果是新增
            if(true==isNew){
                p.setContent("isNew", "true");
            }else{
                p.setContent("id", id);
				p.setContent("boxId", getTreeId());
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var msgInfoNode = this.getNodeValue(XML_MESSAGE_INFO);

                var msgInfoNodeID = cacheID+"."+XML_MESSAGE_INFO;

                Cache.XmlIslands.add(msgInfoNodeID,msgInfoNode);

                Cache.Variables.add(cacheID,[msgInfoNodeID]);

                initMessagePages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initMessagePages(cacheID,editable,isNew);
        }
    }
 
    function initMessagePages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadMessageInfoFormData(cacheID,editable);
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page1BtSaveAndSendObj = $("page1BtSaveAndSend");
        var page1BtSendObj = $("page1BtSend");
        page1BtSaveObj.style.display = editable==false?"none":"";
        page1BtSaveAndSendObj.style.display = editable==false?"none":"";
        page1BtSendObj.style.display = editable==false?"none":"";
        page1BtSaveObj.onclick = function(){
            saveMessage(cacheID,"save",isNew);
        }
        page1BtSaveAndSendObj.onclick = function(){
            saveMessage(cacheID,"saveAndSend",isNew);
        }
        page1BtSendObj.onclick = function(){
            saveMessage(cacheID,"send",isNew);
        }
    }
    /*
     *	函数说明：消息信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadMessageInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_MESSAGE_INFO);
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
 
    function onTreeNodeDoubleClick(eventObj){
        var treeNode = eventObj.treeNode;
        var id = getTreeId();
        getTreeOperation(treeNode,function(_operation){
                var canShowMsgList = checkOperation("6",_operation);
                var canAddNew = checkOperation("1",_operation);

                if("1"==id && true==canAddNew){
                    addNewMessage();
                }else if("1"!=id && true==canShowMsgList){
                    showMessageList();
                }
        });
    }
 
    function onTreeNodeRightClick(eventObj){
        var treeObj = $("tree");
        var treeNode = eventObj.treeNode;

        showTreeNodeStatus({id:"ID",name:"名称",creatorName:"创建者",createTime:"创建时间",updatorName:"修改者",updateTime:"修改时间"});

        var x = eventObj.clientX;
        var y = eventObj.clientY;
        if( treeObj.contextmenu ) {
			treeObj.contextmenu.show(x,y);                
		}
    }
 
    function onClickRow(eventObj){    
        Focus.focus("gridTitle");

        var rowIndex = eventObj.result.rowIndex_Xml;
        showMessageStatus(rowIndex);

        //防止因为载入工具条数据而导致不响应双击事件
        clearTimeout(window._toolbarTimeout);
        window._toolbarTimeout = setTimeout(function(){
            loadMessageToolBarData(rowIndex);
        },0);
    }
 
    function onDblClickRow(eventObj){
        var rowIndex = eventObj.result.rowIndex_Xml;
        var boxId = getMessageBoxId();
        getGridOperation(rowIndex,function(_operation){
            //检测编辑权限
            var edit = checkOperation("m2",_operation);
            var view = checkOperation("m3",_operation);

            if(true==edit && "3"==boxId){
                editMessageInfo(true);
            }else if(true==view && ("2"==boxId || "4"==boxId)){
                editMessageInfo(false);
            }
        });
    }
 
    function onRightClickRow(eventObj){
        var gridObj = $("grid");
        var rowIndex = eventObj.result.rowIndex_Xml;
        var x = event.clientX;
        var y = event.clientY;

        getGridOperation(rowIndex,function(_operation){
            gridObj.contextmenu.show(x,y);
            loadToolBar(_operation);
        });
    }
 
    /*
     *	函数说明：保存消息
     *	参数：	string:cacheID          缓存数据ID
                string:mode             模式(save仅保存/saveAndSend保存并发送/send仅发送)
                boolean:isNew           是否新建
     *	返回值：
     */
    function saveMessage(cacheID,mode,isNew){
        //校验page1Form,page8Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_MESSAGE;
        p.setContent("mode",mode);

        //是否提交
        var flag = false;
        
        var userCache = Cache.Variables.get(cacheID);
        if(null!=userCache){

            //消息基本信息
            var userInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_MESSAGE_INFO);
            if(null!=userInfoNode){
                var userInfoDataNode = userInfoNode.selectSingleNode(".//data");
                if(null!=userInfoDataNode){
                    flag = true;

                    var prefix = userInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(userInfoDataNode,prefix);
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

                //清除该组消息grid缓存
                delCacheData(CACHE_TREE_NODE_GRID + "3");//草稿箱
                delCacheData(CACHE_TREE_NODE_GRID + "4");//发件箱

                //如果当前grid显示为此消息所在组，则刷新grid
                var gridObj = $("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempBoxId = tempXmlIsland.getAttribute("boxId");
                    var cond1 = "3"==tempBoxId && ("save"==mode || "sendAndSave"==mode);
                    var cond2 = "4"==tempBoxId && ("send"==mode || "sendAndSave"==mode);
                    if(true==cond1 || true==cond2){
                        loadGridData(tempBoxId,"1");//默认第1页

                        //刷新工具条
                        onInactiveRow();
                    }
                }

                if(true == isNew){
                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：显示消息列表
     *	参数：	                
     *	返回值：
     */
    function showMessageList(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            initGrid(id);
        }
    }
    /*
     *	函数说明：新建消息
     *	参数：	
     *	返回值：
     */
    function addNewMessage(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var name = "消息";
            var id = new Date().valueOf();

            var phases = null;

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadMessageDetailData(id,true,true);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_ADD.replace(/\$label/i,name);
            inf.phases = phases;
            inf.callback = callback;
            inf.SID = CACHE_GRID_ROW_DETAIL + id;
            var tab = ws.open(inf);
        }
    }
 
    function getTreeId(){
        return getTreeAttribute("id");   
    }
 
    function delMessage(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");

        var p = new HttpRequestParams();
        p.url = URL_DEL_MESSAGE;
        p.setContent("id",rowID);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //从grid上删除
            gridObj.delRow_Xml(rowIndex,true,true);

            //刷新工具条
            onInactiveRow();
        }
        request.send();
    }
 
   
  
    /*
     *	函数说明：获取当前邮件属于哪个文件夹
 
     */
    function getMessageBoxId(){
        var gridObj = $("grid");
        var boxId = gridObj.getXmlDocument().getAttribute("boxId");
        return boxId;
    }
    /*
     *	函数说明：回复消息
 
     */
    function replyMessage(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var title = gridObj.getNamedNodeValue_Xml(rowIndex,"title");
        var id = rowNode.getAttribute("id");
        var boxId = gridObj.getXmlDocument().getAttribute("boxId");

        var phases = null;

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadReplyDetailData(id);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.label = OPERATION_REPLY.replace(/\$label/i,title);
        inf.SID = CACHE_REPLY_DETAIL + id;
        inf.defaultPage = "page1";
        inf.phases = phases;
        inf.callback = callback;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：消息详细信息加载数据
 
     */
    function loadReplyDetailData(id){
        var cacheID = CACHE_REPLY_DETAIL + id;
        var msgDetail = Cache.Variables.get(cacheID);
        if(null==msgDetail){
            var p = new HttpRequestParams();
            p.url = URL_MESSAGE_DETAIL;

            p.setContent("id", id);
			p.setContent("type", "reply");

            var request = new HttpRequest(p);
            request.onresult = function(){
                var replyInfoNode = this.getNodeValue(XML_REPLY_INFO);
                var replyInfoNodeID = cacheID+"."+XML_REPLY_INFO;

                Cache.XmlIslands.add(replyInfoNodeID,replyInfoNode);
                Cache.Variables.add(cacheID,[replyInfoNodeID]);

                initReplyPages(cacheID);
            }
            request.send();
        }else{
            initReplyPages(cacheID);
        }
    }
    /*
     *	函数说明：回复相关页加载数据
 
     */
    function initReplyPages(cacheID){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadReplyInfoFormData(cacheID);
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page1BtSaveAndSendObj = $("page1BtSaveAndSend");
        var page1BtSendObj = $("page1BtSend");
        page1BtSaveObj.style.display = "";
        page1BtSaveAndSendObj.style.display = "";
        page1BtSendObj.style.display = "";
        page1BtSaveObj.onclick = function(){
            saveReply(cacheID,"save");
        }
        page1BtSaveAndSendObj.onclick = function(){
            saveReply(cacheID,"saveAndSend");
        }
        page1BtSendObj.onclick = function(){
            saveReply(cacheID,"send");
        }
    }
    /*
     *	函数说明：回复信息xform加载数据
 
     */
    function loadReplyInfoFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_REPLY_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = "true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	函数说明：保存消息
     *	参数：	string:cacheID          缓存数据ID
                string:mode             模式(save仅保存/saveAndSend保存并发送/send仅发送)
     *	返回值：
     */
    function saveReply(cacheID,mode){
        //校验page1Form,page8Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_REPLY;
        p.setContent("mode",mode);

        //是否提交
        var flag = false;
        
        var userCache = Cache.Variables.get(cacheID);
        if(null!=userCache){

            //消息基本信息
            var userInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_REPLY_INFO);
            if(null!=userInfoNode){
                var userInfoDataNode = userInfoNode.selectSingleNode(".//data");
                if(null!=userInfoDataNode){
                    flag = true;

                    var prefix = userInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(userInfoDataNode,prefix);
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

                //清除该组消息grid缓存
                delCacheData(CACHE_TREE_NODE_GRID + "3");//草稿箱
                delCacheData(CACHE_TREE_NODE_GRID + "4");//发件箱

                //如果当前grid显示为此消息所在组，则刷新grid
                var gridObj = $("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempBoxId = tempXmlIsland.getAttribute("boxId");
                    var cond1 = "3"==tempBoxId && ("save"==mode || "sendAndSave"==mode);
                    var cond2 = "4"==tempBoxId && ("send"==mode || "sendAndSave"==mode);
                    if(true==cond1 || true==cond2){
                        loadGridData(tempBoxId,"1");//默认第1页

                        //刷新工具条
                        onInactiveRow();
                    }
                }

                var ws = $("ws");
                ws.closeActiveTab();
            }
            request.send();
        }
    }
    /*
     *	函数说明：转发消息
     *	参数：
     *	返回值：
     */
    function forwardMessage(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var title = gridObj.getNamedNodeValue_Xml(rowIndex,"title");
        var id = rowNode.getAttribute("id");
        var boxId = gridObj.getXmlDocument().getAttribute("boxId");

        var phases = null;

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadForwardDetailData(id);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.label = OPERATION_FORWARD.replace(/\$label/i,title);
        inf.SID = CACHE_FORWARD_DETAIL + id;
        inf.defaultPage = "page1";
        inf.phases = phases;
        inf.callback = callback;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：转发详细信息加载数据
     *	参数：	string:id               消息id
     *	返回值：
     */
    function loadForwardDetailData(id){
        var cacheID = CACHE_FORWARD_DETAIL + id;
        var msgDetail = Cache.Variables.get(cacheID);
        if(null==msgDetail){
            var p = new HttpRequestParams();
            p.url = URL_MESSAGE_DETAIL;

            p.setContent("id", id);
			p.setContent("type", "forward");

            var request = new HttpRequest(p);
            request.onresult = function(){
                var fwInfoNode = this.getNodeValue(XML_FORWARD_INFO);
                var fwInfoNodeID = cacheID+"."+XML_FORWARD_INFO;

                Cache.XmlIslands.add(fwInfoNodeID,fwInfoNode);
                Cache.Variables.add(cacheID,[fwInfoNodeID]);

                initForwardPages(cacheID);
            }
            request.send();
        }else{
            initForwardPages(cacheID);
        }
    }
    /*
     *	函数说明：转发相关页加载数据
     *	参数：	string:cacheID          缓存数据id
     *	返回值：
     */
    function initForwardPages(cacheID){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadForwardInfoFormData(cacheID);
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        var page1BtSaveAndSendObj = $("page1BtSaveAndSend");
        var page1BtSendObj = $("page1BtSend");
        page1BtSaveObj.style.display = "";
        page1BtSaveAndSendObj.style.display = "";
        page1BtSendObj.style.display = "";
        page1BtSaveObj.onclick = function(){
            saveForward(cacheID,"save");
        }
        page1BtSaveAndSendObj.onclick = function(){
            saveForward(cacheID,"saveAndSend");
        }
        page1BtSendObj.onclick = function(){
            saveForward(cacheID,"send");
        }
    }
    /*
     *	函数说明：转发信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function loadForwardInfoFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_FORWARD_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = "true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	函数说明：保存消息
     *	参数：	string:cacheID          缓存数据ID
                string:mode             模式(save仅保存/saveAndSend保存并发送/send仅发送)
     *	返回值：
     */
    function saveForward(cacheID,mode){
        //校验page1Form,page8Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_FORWARD;
        p.setContent("mode",mode);

        //是否提交
        var flag = false;
        
        var userCache = Cache.Variables.get(cacheID);
        if(null!=userCache){

            //消息基本信息
            var userInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_FORWARD_INFO);
            if(null!=userInfoNode){
                var userInfoDataNode = userInfoNode.selectSingleNode(".//data");
                if(null!=userInfoDataNode){
                    flag = true;

                    var prefix = userInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(userInfoDataNode,prefix);
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

                //清除该组消息grid缓存
                delCacheData(CACHE_TREE_NODE_GRID + "3");//草稿箱
                delCacheData(CACHE_TREE_NODE_GRID + "4");//发件箱

                //如果当前grid显示为此消息所在组，则刷新grid
                var gridObj = $("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempBoxId = tempXmlIsland.getAttribute("boxId");
                    var cond1 = "3"==tempBoxId && ("save"==mode || "sendAndSave"==mode);
                    var cond2 = "4"==tempBoxId && ("send"==mode || "sendAndSave"==mode);
                    if(true==cond1 || true==cond2){
                        loadGridData(tempBoxId,"1");//默认第1页

                        //刷新工具条
                        onInactiveRow();
                    }
                }

                var ws = $("ws");
                ws.closeActiveTab();
            }
            request.send();
        }
    }
 
    function chooseUser(){
        var returnVal = window.showModalDialog("chooseuser.htm", {title:"请选择用户..."},"dialogWidth:700px;dialogHeight:500px;");
        if(null != returnVal){
            var receiver = [];
            var receiverId = [];

            for(var i=0,iLen=returnVal.length;i<iLen;i++){
                var userName = returnVal[i].userName;
                var userId = returnVal[i].userId;

                receiver[receiver.length] = userName;
                receiverId[receiverId.length] = userId;
            }

            var page1FormObj = $("page1Form");
            page1FormObj.updateDataExternal("receiver",receiver.join(","));
            page1FormObj.updateDataExternal("receiverIds",receiverId.join(","));
        }
    }



    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();