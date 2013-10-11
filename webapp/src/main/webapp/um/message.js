    /* 后台响应数据节点名称 */
    XML_MAIN_TREE = "MessageTree";
    XML_MESSAGE_LIST = "MessageList";
    XML_MESSAGE_INFO = "MessageInfo";
    XML_REPLY_INFO = "ReplyInfo";
 
    /* 默认唯一编号名前缀 */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_VIEW_GRID_ROW_DETAIL = "viewRow__id";
	CACHE_REPLY_MESSAGE_DETAIL = "reply__id";
 
    /* 名称 */
    OPERATION_REPLY = "回复\"$label\"";
 
    /* XMLHTTP请求地址汇总 */
    URL_INIT           = "data/message_init.xml?";
    URL_MESSAGE_LIST   = "/" + AUTH_PATH + "message/list/";  // {boxType}
    URL_MESSAGE_DETAIL = "/" + AUTH_PATH + "message/";  // {id}
	URL_MESSAGE_REPLY  = "/" + AUTH_PATH + "message/reply/"; // {id}
    URL_SAVE_MESSAGE   = "/" + AUTH_PATH + "message"; // PUT
    URL_DEL_MESSAGE    = "/" + AUTH_PATH + "message/"; // {id} DELETE

	if(IS_TEST) {
		URL_INIT = "data/message_init.xml?";
		URL_MESSAGE_LIST = "data/message_list.xml?";
		URL_MESSAGE_DETAIL = "data/message.xml?";
		URL_MESSAGE_REPLY = "data/message_reply.xml?";
		URL_SAVE_MESSAGE = "data/_success.xml?";
		URL_DEL_MESSAGE = "data/_success.xml?";
	}
 
    function init(){
        initPaletteResize();
        initListContainerResize();
        initNaviBar();
        initMenus();

        initWorkSpace();
		Blocks.create($$("palette"));

        loadInitData();
    }
 
    function loadInitData(){
		Ajax({
			url : URL_INIT,
			method : "GET", 
			onresult : function() {  // 移动树节点					
				var tree = $T("tree", this.getNodeValue(XML_MAIN_TREE));
				
				var treeElement = tree.element;
				treeElement.onTreeNodeRightClick = function(eventObj) {
					if( treeElement.contextmenu ) {
						treeElement.contextmenu.show(eventObj.clientX, eventObj.clientY);                
					}
				}
			}
		});
    }
 
    function initMenus(){
        initTreeMenu();
        initGridMenu();
    }
 
    function initTreeMenu(){
        var item1 = {
            label:"写新消息",
            callback:addNewMessage,
            visible:function(){return  "1" == getTreeNodeId();}
        }
        var item2 = {
            label:"浏览消息",
            callback:showMessageList,
            icon:ICON + "view_list.gif",
            visible:function(){return  "1" != getTreeNodeId();}
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
            icon:ICON + "icon_del.gif"
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
		callback.onTabChange = function(){
			setTimeout(function(){
				loadMessage(id);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, "消息");
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_GRID_ROW_DETAIL + id;
		ws.open(inf);

		$$("page1BtSend").style.display = "";
    }
	
    function viewMessage(editable) {
		var id = $G("grid").getRowAttributeValue("id");   
        var title = $G("grid").getRowAttributeValue("title");  
 
        var callback = {};
        callback.onTabChange = function(){
            setTimeout(function(){
                loadMessage(id);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
		inf.label = OPERATION_VIEW.replace(/\$label/i, title);
		inf.SID = CACHE_VIEW_GRID_ROW_DETAIL + id;

        inf.defaultPage = "page1";
        inf.phases = null;
        inf.callback = callback;
        ws.open(inf);
        
		$$("page1BtSend").style.display = "none";
    }

	function replyMessage() {
		var id = $G("grid").getRowAttributeValue("id");   
        var title = $G("grid").getRowAttributeValue("title");   

        var callback = {};
        callback.onTabChange = function(){
            setTimeout(function(){
                loadReplyMessage(id);
            }, TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.label = OPERATION_REPLY.replace(/\$label/i, title);
        inf.SID = CACHE_REPLY_MESSAGE_DETAIL + id;
        inf.defaultPage = "page1";
        inf.phases = null;
        inf.callback = callback;
        var tab = ws.open(inf);
    }
 
    function loadMessage(id) {
		Ajax({
			url : URL_MESSAGE_DETAIL + id,
			onresult : function() {  // 移动树节点					
				var msgInfoNode = this.getNodeValue(XML_MESSAGE_INFO);
				Cache.XmlDatas.add(id + "." + XML_MESSAGE_INFO, msgInfoNode);
				
				$X("page1Form", msgInfoNode);		

				$$("page1BtSend").onclick = function(){
					saveMessage(id);
				}
			}
		});
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
            syncButton([$("page1BtSend")], request);

            request.onsuccess = function() {
				ws.closeActiveTab();
            }
            request.send();
        }
    }
 
    function showMessageList() {
        var treeNode = $T("tree").getActiveTreeNode();
		Ajax({
			url : URL_MESSAGE_LIST + treeNode.getId(),
			onresult : function() {  // 移动树节点					
				$G("grid", this.getNodeValue(XML_MESSAGE_LIST));
			}
		});
    }
 
    function loadReplyMessage(id) {
		Ajax({
			url :URL_MESSAGE_REPLY + id,
			onresult : function() {  // 移动树节点					
				var replyInfoNode = this.getNodeValue(XML_REPLY_INFO);

				Cache.XmlDatas.add(id + "." + XML_REPLY_INFO, replyInfoNode);
				$X("page1Form", replyInfoNode);
				
				$$("page1BtSend").style.display = "";	
				var page1BtSendObj = $$("page1BtSend");			
				page1BtSendObj.style.display = "";
				page1BtSendObj.onclick = function(){
					saveReply(id, "send");
				}
			}
		});
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