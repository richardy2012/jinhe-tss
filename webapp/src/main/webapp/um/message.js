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
 
    function loadInitData(){
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function(){   
            var groupTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var tree = $T("tree", groupTreeNode);
			
			$$("tree").onTreeNodeRightClick = function(eventObj) {
				if( tree.contextmenu ) {
					tree.contextmenu.show(eventObj.clientX, eventObj.clientY);                
				}
            }
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
            visible:function(){return "1"==getTreeNodeId();}
        }
        var item2 = {
            label:"浏览消息",
            callback:showMessageList,
            icon:ICON + "view_list.gif",
            visible:function(){return "1"!=getTreeNodeId();}
        }
 
        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
 
        $$("tree").contextmenu = menu1;
    }
 
    function initGridMenu() {
        var item1 = {
            label:"查看",
            callback:function() {
                viewMessage();
            },
            icon:ICON + "view.gif"
        }
        var item2 = {
            label:"删除",
            callback:function() {
                delGridRow(URL_DEL_MESSAGE);
            },
            icon:ICON + "del.gif"
        }
        var item3 = {
            label:"回复",
            callback:replyMessage,
            icon:ICON + "reply.gif"
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
		
		var gridObj =  $$("grid");
        gridObj.contextmenu = menu1;
 
        gridObj.ondblclickrow = function(){
            viewMessage();
        }
        gridObj.onrightclickrow = function(){
            $$("grid").contextmenu.show(event.clientX, event.clientY);
        }
    }
 
    function addNewMessage() {
		var id = DEFAULT_NEW_ID;

		var callback = {};
		callback.onTabClose = function(eventObj){
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function(){
			setTimeout(function(){
				loadMessageDetailData(id);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, "消息");
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_GRID_ROW_DETAIL + id;
		ws.open(inf);
    }
	
    function viewMessage(editable) {
        var rowIndex = $$("grid").selectRowIndex; 
		var row = $G("grid").getRowByIndex(rowIndex);
		var id = row.getAttribute("id");   
        var title = row.getAttribute("title");  
 
        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadMessageDetailData(id);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
		inf.label = OPERATION_VIEW.replace(/\$label/i, title);
		inf.SID = CACHE_VIEW_GRID_ROW_DETAIL + id;

        inf.defaultPage = "page1";
        inf.phases = null;
        inf.callback = callback;
        ws.open(inf);
        
    }
 
    function loadMessageDetailData(id) {
		var p = new HttpRequestParams();
		p.url = URL_MESSAGE_DETAIL;
		p.setContent("id", id);

		var request = new HttpRequest(p);
		request.onresult = function() {
			var msgInfoNode = this.getNodeValue(XML_MESSAGE_INFO);
			Cache.XmlDatas.add(id + "." + XML_MESSAGE_INFO, replyInfoNode);
			
			$X("page1Form", msgInfoNode);		
			$$("page1BtSend").style.display = "none";
			
			$$("page1BtSave").onclick = function(){
				saveMessage(id);
			}
		}
		request.send();
    }
 
    function saveMessage( id ){
        var page1FormObj = $("page1Form");
        if( !page1FormObj.checkForm() ) return;

        var p = new HttpRequestParams();
        p.url = URL_SAVE_MESSAGE;
 
        var flag = false;
 
		var userInfoNode = Cache.XmlDatas.get(id + "." + XML_MESSAGE_INFO);
		if( userInfoNode ) {
			flag = true;
			p.setXFormContent(userInfoNode.selectSingleNode(".//data"));
		}

        if( flag ) {
            var request = new HttpRequest(p);
            syncButton([$("page1BtSave")], request);

            request.onsuccess = function() {
				ws.closeActiveTab();
            }
            request.send();
        }
    }
 
    function showMessageList() {
        var treeNode = $T("tree").getActiveTreeNode();
		
		var p = new HttpRequestParams();
		p.url = URL_MESSAGE_LIST;
		p.setContent("boxId", treeNode.getId());

		var request = new HttpRequest(p);
		request.onresult = function(){
			$G("grid", this.getNodeValue(XML_MESSAGE_LIST));
		}
		request.send();
    }

 
    function replyMessage() {
        var rowIndex = $$("grid").selectRowIndex; 
		var row = $G("grid").getRowByIndex(rowIndex);
		var id = row.getAttribute("id");   
        var title = row.getAttribute("title");   
 
        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadReplyDetailData(id);
            }, TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.label = OPERATION_REPLY.replace(/\$label/i, title);
        inf.SID = CACHE_REPLY_DETAIL + id;
        inf.defaultPage = "page1";
        inf.phases = null;
        inf.callback = callback;
        var tab = ws.open(inf);
    }
 
    function loadReplyDetailData(id) {
		var p = new HttpRequestParams();
		p.url = URL_MESSAGE_DETAIL;

		p.setContent("id", id);

		var request = new HttpRequest(p);
		request.onresult = function(){
			var replyInfoNode = this.getNodeValue(XML_REPLY_INFO);

			Cache.XmlDatas.add(id + "." + XML_REPLY_INFO, replyInfoNode);
			$X("page1Form", replyInfoNode);

			$$("page1BtSave").style.display = "none";	
			
			var page1BtSendObj = $$("page1BtSend");			
			page1BtSendObj.style.display = "";
			page1BtSendObj.onclick = function(){
				saveReply(id, "send");
			}
		}
		request.send();
    }
 
    function saveReply(id, mode){
        var page1FormObj = $X("page1Form");
        if( !page1FormObj.checkForm() ) return;

        var p = new HttpRequestParams();
        p.url = URL_SAVE_REPLY;
        
        var flag = false; // 是否提交
 
		var userInfoNode = Cache.XmlDatas.get(id + "." + XML_REPLY_INFO);
		if( userInfoNode ) {
			flag = true;
			p.setXFormContent(userInfoNode.selectSingleNode(".//data"));
		}

        if ( flag ) {
            var request = new HttpRequest(p);
            syncButton([$$("page1BtSave")], request);

            request.onsuccess = function() {
                ws.closeActiveTab();
            }
            request.send();
        }
    }
 
 
    function chooseUser(){
        var returnVal = window.showModalDialog("chooseuser.htm", {title:"请选择用户..."},"dialogWidth:700px;dialogHeight:500px;");
        if( returnVal ) {
            var receiver = [];
            var receiverId = [];

            for(var i=0; i < returnVal.length; i++) {
                receiver[i]   = returnVal[i].userName;
                receiverId[i] = returnVal[i].userId;
            }

            var page1FormObj = $X("page1Form");
            page1FormObj.updateDataExternal("receiver", receiver.join(","));
            page1FormObj.updateDataExternal("receiverIds", receiverId.join(","));
        }
    }


    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();