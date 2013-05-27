
    URL_CORE = "../../";

	ICON = "images/";
    
    /* 后台响应数据节点名称  */
    XML_MAIN_TREE = "ParamTree";
    XML_PARAM_LIST = "ParamList";
    XML_PARAM_INFO = "ParamInfo";
    
	/* 默认唯一编号名前缀  */
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_VIEW_TREE_NODE_DETAIL = "viewTreeNode__id";
   
    /* XMLHTTP请求地址汇总 */
	URL_INIT = URL_CORE + "../param!get2Tree.action";
    URL_PARAM_DETAIL = URL_CORE + "../param!getParamInfo.action";
    URL_TREENODE_DEL = URL_CORE + "../param!delParam.action";
    URL_TREENODE_DISABLE = "../../../param!startOrStopParam.action";
    URL_SAVE_PARAM = URL_CORE + "../param!saveParam.action";
    URL_SORT_PARAM = "../../../param!sortParam.action";
    URL_FLUSH_PARAM_CACHE  = "../../../pms/param!flushParamCache.action";
    URL_COPY_PARAM = "../../../param!copyParam.action";
    URL_COPY_PARAM_TO = "../../../param!copyParam.action";
    URL_MOVE_PARAM_TO = "../../../param!moveParam.action";
	
	if(IS_TEST) {
		URL_INIT = "data/param_init.xml";
		URL_PARAM_DETAIL = "data/param1.xml";
		URL_TREENODE_DEL = "data/_success.xml";
		URL_TREENODE_DISABLE = "data/_success.xml";
		URL_SAVE_PARAM = "data/_success.xml";
		URL_SORT_PARAM = "data/_success.xml";
		URL_FLUSH_PARAM_CACHE  = "data/_success.xml";
		URL_COPY_PARAM = "data/_success.xml";
		URL_COPY_PARAM_TO = "data/_success.xml";
		URL_MOVE_PARAM_TO = "data/_success.xml";
	}
 
   

    var toolbar

    /*
     *	页面初始化
     *	参数：	
     *	返回值：
     */
    function init() {
        initPaletteResize();
        initUserInfo();
        initToolBar();
        initNaviBar();
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }
 
    function loadToolBar(_operation) {
		var str = [];
		str[str.length] = "<toolbar>";

		//公共
		str[str.length] = "    <button id=\"a1\" code=\"p1\" icon=\"" + ICON + "icon_pre.gif\" label=\"上页\" cmd=\"ws.prevTab()\" enable=\"true\"/>";
		str[str.length] = "    <button id=\"a2\" code=\"p2\" icon=\"" + ICON + "icon_next.gif\" label=\"下页\" cmd=\"ws.nextTab()\" enable=\"true\"/>";
		str[str.length] = "    <separator/>";

		//参数管理
		str[str.length] = "    <button id=\"b1\" code=\"2\" icon=\"" + ICON + "start.gif\" label=\"启用\" cmd=\"enableParam()\" enable=\"'_rootId'!=getTreeNodeId() &amp;&amp; '0'!=getTreeNodeDisabled()\"/>";
		str[str.length] = "    <button id=\"b2\" code=\"2\" icon=\"" + ICON + "stop.gif\" label=\"停用\" cmd=\"disableParam()\" enable=\"'_rootId'!=getTreeNodeId() &amp;&amp; '0'==getTreeNodeDisabled()\"/>";
		str[str.length] = "    <button id=\"b4\" code=\"1\" icon=\"" + ICON + "view.gif\" label=\"查看\" cmd=\"editParamInfo(false)\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
		str[str.length] = "    <button id=\"b5\" code=\"2\" icon=\"" + ICON + "edit.gif\" label=\"编辑\" cmd=\"editParamInfo()\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
		str[str.length] = "    <button id=\"b6\" code=\"2\" icon=\"" + ICON + "del.gif\" label=\"删除\" cmd=\"delParam()\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
		str[str.length] = "    <button id=\"b7\" code=\"2\" icon=\"" + ICON + "copy.gif\" label=\"复制\" cmd=\"copyParam()\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
		str[str.length] = "    <button id=\"b8\" code=\"2\" icon=\"" + ICON + "copy_to.gif\" label=\"复制到...\" cmd=\"copyParamTo()\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
		str[str.length] = "    <button id=\"b9\" code=\"2\" icon=\"" + ICON + "move.gif\" label=\"移动到...\" cmd=\"moveParamTo()\" enable=\"'_rootId'!=getTreeNodeId()\"/>";
		str[str.length] = "    <button id=\"b10\" code=\"2\" icon=\"" + ICON + "new_param_group.gif\" label=\"新建参数组\" cmd=\"addNewParam('0')\" enable=\"'0'==getTreeNodeType() || '_rootId'==getTreeNodeId()\"/>";
		str[str.length] = "</toolbar>";

		_loadToolBar(_operation, str.join("\r\n"));
    }
 
    function initMenus() {
        var item1 = {
            label:"新建参数",
            callback:null,
            enable:function() {return true;},
            visible:function() {return "0" == getTreeNodeType();}
        }
        var item2 = {
            label:"删除",
            callback:delParam,
            icon:ICON + "del.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId" != getTreeNodeId();}
        }
        var item3 = {
            label:"编辑",
            callback:editParamInfo,
            icon:ICON + "edit.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId" != getTreeNodeId();}
        }
        var item4 = {
            label:"启用",
            callback:enableParam,
            icon:ICON + "start.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId" != getTreeNodeId() && "0" != getTreeNodeDisabled();}
        }
        var item5 = {
            label:"停用",
            callback:disableParam,
            icon:ICON + "stop.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId"!=getTreeNodeId() && "0"==getTreeNodeDisabled();}
        }
        var item7 = {
            label:"新建参数组",
            callback:function() {
                addNewParam("0");
            },
            enable:function() {return true;},
            visible:function() {return ("0"==getTreeNodeType() || "_rootId"==getTreeNodeId());}
        }
        var item9 = {
            label:"复制",
            callback:copyParam,
            icon:ICON + "copy.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId"!=getTreeNodeId();}
        }
        var item11 = {
            label:"复制到...",
            callback:copyParamTo,
            icon:ICON + "copy_to.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId"!=getTreeNodeId();}
        }
        var item12 = {
            label:"移动到...",
            callback:moveParamTo,
            icon:ICON + "move.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId"!=getTreeNodeId();}
        }
        var item13 = {
            label:"查看",
            callback:function() {
                editParamInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId"!=getTreeNodeId() && true==getOperation("1");}
        }
        var item14 = {
            label:"新建参数项",
            callback:function() {
                addNewParam("2");
            },
            enable:function() {return true;},
            visible:function() {return (("1"==getTreeNodeMode() && "1"==getTreeNodeType()) || "2"==getTreeNodeMode());}
        }
		var item15 = {
            label:"刷新参数缓存",
            callback:function() {
                flushParamCache();
            },
            enable:function() {return true;},
            visible:function() {return "1"==getTreeNodeType();}
        }


        //新建参数子菜单
        var subitem1_1 = {
            label:"简单型",
            callback:function() {
                addNewParam("1", "0");
            },
            enable:function() {return true;},
            visible:function() {return true;}
        }
        var subitem1_2 = {
            label:"下拉型",
            callback:function() {
                addNewParam("1", "1");
            },
            enable:function() {return true;},
            visible:function() {return true;}
        }
        var subitem1_3 = {
            label:"树型",
            callback:function() {
                addNewParam("1", "2");
            },
            enable:function() {return true;},
            visible:function() {return true;}
        }
        var submenu1 = new Menu();
        submenu1.addItem(subitem1_1);
        submenu1.addItem(subitem1_2);
        submenu1.addItem(subitem1_3);
        item1.submenu = submenu1;        

        var menu1 = new Menu();
        menu1.addItem(item4);
        menu1.addItem(item5);
        menu1.addSeparator();
        menu1.addItem(item13);
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addItem(item9);
        menu1.addItem(item11);
        menu1.addItem(item12);
        menu1.addSeparator();
        menu1.addItem(item7);
        menu1.addItem(item1);
        menu1.addItem(item14);

		menu1.addSeparator();
		menu1.addItem(item15);

        var treeObj = $("tree");
        treeObj.contextmenu = menu1;
    }

	function loadInitData() {
		Ajax({
			url : URL_INIT,
			onresult : function() { 
				$T("tree", this.getNodeValue(XML_MAIN_TREE);

				var treeObj = $("tree");
				treeObj.load(xmlIsland.node);

				treeObj.onTreeNodeActived = function(eventObj) {
					Focus.focus($("treeTitle").firstChild.id);
					showTreeNodeInfo();
					loadToolBar();
				}
				treeObj.onTreeNodeDoubleClick = function(eventObj) {
					if( isTreeRoot() ) {
						editParamInfo();
					}
				}
				treeObj.onTreeNodeMoved = function(eventObj) {
					sortParamTo(eventObj);
				}
				treeObj.onTreeNodeRightClick = function(eventObj) {
					$("tree").contextmenu.show(eventObj.clientX, eventObj.clientY); 
					loadToolBar();
					showTreeNodeInfo();
				}	 

				loadToolBar();
			}
		});
    }
 

    /* 编辑参数信息 */
    function editParamInfo(editable) {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if( treeNode ) {
            var treeID   = treeNode.getId();
            var treeName = treeNode.getName();
            var type = getTreeNodeType();
			var mode = getTreeNodeMode();

            var callback = {};
            callback.onTabClose = function(eventObj) {
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function() {
                setTimeout(function() {
                    loadTreeDetailData(treeID, editable, treeID, type, false, mode);
                }, TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(editable) {
				inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
                inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
            } else {
                inf.label = OPERATION_VIEW.replace(/\$label/i, treeName);
                inf.SID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }

    /*
     *	树节点数据详细信息加载数据
     *	参数：	string:treeID               树节点id
                boolean:editable            是否可编辑(默认true)
                string:parentID             父节点id
                boolean:isNew               是否新增
                boolean:type                节点类型(0参数组/1参数/2参数项)
     */
    function loadTreeDetailData(treeID, editable, parentID, type, isNew, mode) {
        if(false==editable) {
            var cacheID = CACHE_VIEW_TREE_NODE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_TREE_NODE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail) {
            var p = new HttpRequestParams();
            p.url = URL_PARAM_DETAIL;

            p.setContent("paramId", treeID);
            p.setContent("type", type);
            //如果是新增
            if(true==isNew) {
                p.setContent("isNew", 1);
				p.setContent("parentId", parentID);
            }
			if("1"==type) {
				p.setContent("mode", mode);
			}

            var request = new HttpRequest(p);
            request.onresult = function() {
                var paramInfoNode = this.getNodeValue(XML_PARAM_INFO);
                var paramInfoNodeID = cacheID+"."+XML_PARAM_INFO;

                Cache.XmlDatas.add(paramInfoNodeID,paramInfoNode);
                Cache.Variables.add(cacheID,[paramInfoNodeID]);

                initParamPages(cacheID,editable,parentID,isNew,type);
            }
            request.send();
        }else{
            initParamPages(cacheID,editable,parentID,isNew,type);
        }
    }

    /*
     *	参数相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                string:parentID             父节点id
                boolean:isNew               是否新增
                boolean:type                节点类型(0参数组/1参数/2参数项)
     */
    function initParamPages(cacheID,editable,parentID,isNew,type) {
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function() {
            loadParamInfoFormData(cacheID,editable);
        });

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.onclick = function() {
            saveParam(cacheID,parentID,isNew,type);
        }
    }

    /*
     *	参数信息xform加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
     */
    function loadParamInfoFormData(cacheID,editable) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_PARAM_INFO);
        if(null!=xmlIsland) {
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }

    /*
     *	保存参数
     *	参数：	string:cacheID          缓存数据id
                string:parentID         父节点id
                boolean:isNew           是否新增
                boolean:type            节点类型(0参数组/1参数/2参数项)
     */
    function saveParam(cacheID,parentID,isNew,type) {
        var page1FormObj = $("page1Form");	
        if( !page1FormObj.checkForm() ) {
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_PARAM;
        p.setContent("type",type);

        //是否提交
        var flag = false;
        
        var groupCache = Cache.Variables.get(cacheID);
        if(null!=groupCache) {       

            //参数基本信息
            var paramInfoNode = Cache.XmlDatas.get(cacheID+"."+XML_PARAM_INFO);
            if(null!=paramInfoNode) {
                var paramInfoDataNode = paramInfoNode.selectSingleNode(".//data");
                if(null!=paramInfoDataNode) {
                    flag = true;

                    var prefix = paramInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(paramInfoDataNode,prefix);
                }
            }
        }

        if(true==flag) {
            var request = new HttpRequest(p);
            //同步按钮状态
            var page1BtSaveObj = $("page1BtSave");
            syncButton([page1BtSaveObj],request);

            request.onresult = function() {
                if(true==isNew) {
                    //解除提醒
                    detachReminder(cacheID);

                    var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    appendTreeNode(parentID,treeNode);

                    var ws = $("ws");
                    ws.closeActiveTab();
                }
            }
            request.onsuccess = function() {
                if(true!=isNew) {
                    //解除提醒
                    detachReminder(cacheID);
				
                    //更新树节点名称
					var id = cacheID.trim(CACHE_TREE_NODE_DETAIL);
					if("1" == type) {
						var name = page1FormObj.getData("name");
						if("" == name || null==name) {
							name = page1FormObj.getData("code");
						}
						modifyTreeNode(id,"name",name,true);
					} else if("2"==type) {
                        var text = page1FormObj.getData("text");
						if("" == text || null==text) {
							text = page1FormObj.getData("value");
						}
						modifyTreeNode(id,"name",text,true);
                    }
                }
            }
            request.send();
        }
    }
 
    function delParam() {
        if(true!=confirm("您确定要删除吗？")) {
            return;
        }
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if (null!=treeNode) {
            var id = treeNode.getId();
            var type = treeNode.getAttribute("type");

            var p = new HttpRequestParams();
            p.url = URL_TREENODE_DEL;

            p.setContent("paramId",id);
            p.setContent("type",type);

            var request = new HttpRequest(p);
            request.onsuccess = function() {
                var parentNode = treeNode.getParent();
                if(null!=parentNode) {
                    treeObj.setActiveTreeNode(parentNode.getId());
                }
                //从树上删除
                treeObj.removeTreeNode(treeNode);
            }
            request.send();
        }
    }
 
    function enableParam() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            var type = treeNode.getAttribute("type");

            var p = new HttpRequestParams();
            p.url = URL_TREENODE_DISABLE;

            p.setContent("paramId",id);
            p.setContent("type",type);
            p.setContent("disabled","0");

            var request = new HttpRequest(p);
            request.onsuccess = function() {
                var xmlNode = new XmlNode(treeNode.node);
                refreshTreeNodeStates(xmlNode,"0");

                //刷新工具条
                loadToolBar();
            }
            request.send();	
        }

    }

    /* 停用  */
    function disableParam() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            var type = treeNode.getAttribute("type");

            var p = new HttpRequestParams();
            p.url = URL_TREENODE_DISABLE;

            p.setContent("paramId",id);
            p.setContent("type",type);
            p.setContent("disabled","1");

            var request = new HttpRequest(p);
            request.onsuccess = function() {
                var xmlNode = new XmlNode(treeNode.node);
                refreshTreeNodeStates(xmlNode,"1");

                //刷新工具条
                loadToolBar();
            }
            request.send();	
        }
    }

	function flushParamCache() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();

            var p = new HttpRequestParams();
            p.url = URL_FLUSH_PARAM_CACHE;

            p.setContent("paramId",id);

            var request = new HttpRequest(p);
 
            request.send();	
        }
    }

    /*
     *	新建参数
     *	参数：  string:type         节点类型(0参数组/1参数/2参数项)
                string:mode         参数项类型(0简单型/1下拉型/2树型)
     */
    function addNewParam(type, mode) {
        switch(type) {
            case "0":
                var treeName = "参数组";
                break;
            case "1":
                var treeName = "参数";
                break;
            case "2":
                var treeName = "参数项";
                break;
        }
        var treeID = new Date().valueOf();

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var parentID = treeNode.getId();

            var callback = {};
            callback.onTabClose = function(eventObj) {
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function() {
                setTimeout(function() {
                    loadTreeDetailData(treeID,true,parentID,type,true,mode);
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
 
    function getTreeNodeId() {
        return getTreeAttribute("id");
    }
 
    function getTreeNodeDisabled() {
        return getTreeAttribute("disabled");
    }
 
    function getTreeNodeType() {
        return getTreeAttribute("type");
    }
 
    function getTreeNodeMode() {
        return getTreeAttribute("mode");
    }

    /*
     *	刷新树节点停用启用状态
     *	参数：	treeNode:treeNode       treeNode实例
                string:state            停/启用状态
     */
    function refreshTreeNodeState(treeNode,state) {
        if(null==state) {
            state = treeNode.getAttribute("disabled");
        }
        var type = treeNode.getAttribute("type");
        var mode = treeNode.getAttribute("mode");
        switch(type) {
            case "0":
                var img = "param_group";
                break;
            case "1":
                if("0"==mode) {
                    var img = "param_simple";
                }else if("1"==mode) {
                    var img = "param_combo";
                }else{
                    var img = "param_tree";
                }
                break;
            case "2":
                var img = "param_item";
                break;
        }
        treeNode.setAttribute("disabled",state);
        treeNode.setAttribute("icon",ICON + img + (state=="1"?"_2":"") + ".gif");       
    }

    /*
     *	刷新级联树节点停用启用状态
     *	参数：	XmlNode:curNode         XmlNode实例
                string:state            停/启用状态
     */
    function refreshTreeNodeStates(curNode, state) {
        refreshTreeNodeState(curNode,state);

        if("0" == state) {//启用，上溯
            while(null != curNode && "_rootId" != curNode.getAttribute("id")) {
                refreshTreeNodeState(curNode,state);

                curNode = curNode.getParent();
            }        
        }else if("1" == state) {//停用，下溯
            var childNodes = curNode.selectNodes(".//treeNode");
            for(var i=0,iLen=childNodes.length;i<iLen;i++) {                
                refreshTreeNodeState(childNodes[i],state);
            }
        }

        var treeObj = $("tree");
        treeObj.reload(); 
    }
 
    function copyParam() {
        var treeObj = $("tree"); 
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            var type = treeNode.getAttribute("type");
            var parentID = treeNode.getParent().getId();

            var p = new HttpRequestParams();
            p.url = URL_COPY_PARAM;

            p.setContent("paramId",id);
            p.setContent("type",type);

            var request = new HttpRequest(p);
            request.onresult = function() {
                var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                appendTreeNode(parentID,treeNode);
            }
            request.send();
        }
    }
 
    function copyParamTo() {
        var treeObj = $("tree"); 
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            var name = treeNode.getName();
            var type = treeNode.getAttribute("type");
            var parentID = treeNode.getParent().getId();
			var parentMode = treeNode.getParent().getAttribute("mode");

            var params = {
                id:id,
			    type:type,
				parentID:parentID,
				mode:parentMode,
                action:"copyTo"
            };

            var group = window.showModalDialog("paramtree.htm",{params:params,title:"将\""+name+"\"复制到"},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=group) {

                var p = new HttpRequestParams();
                p.url = URL_COPY_PARAM_TO;
                p.setContent("paramId",id);
                p.setContent("toParamId",group.id);

                var request = new HttpRequest(p);
                request.onresult = function() {
                    var newNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                    appendTreeNode(group.id,newNode);
                }
                request.send();
            }
        }
    }
 
    function moveParamTo() {
        var treeObj = $("tree"); 
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) {
            var id = treeNode.getId();
            var name = treeNode.getName();
            var type = treeNode.getAttribute("type");
            var parentID = treeNode.getParent().getId();
			var parentMode = treeNode.getParent().getAttribute("mode");

            var params = {
                id:id,
			    type:type,
				parentID:parentID,
				mode:parentMode,
                action:"moveTo"
            };

            var group = window.showModalDialog("paramtree.htm",{params:params,title:"将\""+name+"\"移动到"},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=group) {

                var p = new HttpRequestParams();
                p.url = URL_MOVE_PARAM_TO;
                p.setContent("paramId",id);
                p.setContent("toParamId",group.id);

                var request = new HttpRequest(p);
                request.onsuccess = function() {
                    //移动树节点
                    var parentNode = treeObj.getTreeNodeById(group.id);
                    var parentDisabled = parentNode.getAttribute("disabled");
                    parentNode.node.appendChild(treeNode.node);

                    var xmlNode = new XmlNode(treeNode.node);
                    refreshTreeNodeStates(xmlNode,parentDisabled);
                    clearOperation(xmlNode);

                    treeObj.reload();
                }
                request.send();
            }
        }
    }

	function sortParamTo(eventObj) {
        var treeObj = $T("tree");
        var movedTreeNode = eventObj.movedTreeNode;
        var toTreeNode = eventObj.toTreeNode;
        var moveState = eventObj.moveState;
        var moveGroupType = movedTreeNode.getAttribute("parentId");
        var moveParamType = movedTreeNode.getAttribute("groupId");
        var toGroupType = toTreeNode.getAttribute("parentId");
        var toParamType = toTreeNode.getAttribute("groupId");

        var p = new HttpRequestParams();
        p.url = URL_SORT_PARAM;
        p.setContent("targetId",toTreeNode.getId());
        p.setContent("paramId",movedTreeNode.getId());
        p.setContent("direction",moveState);//-1目标上方,1目标下方

        var request = new HttpRequest(p);
        request.onsuccess = function() {
            // 移动树节点
            treeObj.moveTreeNode(movedTreeNode, toTreeNode, moveState);
        }
        request.send();
    }
	

    window.onload = init;

	//关闭页面自动注销
    window.attachEvent("onunload", function() {
        if(10000<window.screenTop || 10000<window.screenLeft) {
            logout();
        }
	});