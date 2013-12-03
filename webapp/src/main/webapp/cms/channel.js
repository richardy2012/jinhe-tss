    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "ChannelTree";
    XML_ARTICLE_LIST = "ArticleList";
    XML_CHANNEL_INFO = "ChannelInfo";
 
    /* 默认唯一编号名前缀 */
 
    CACHE_CHANNEL_DETAIL = "channelDetail__id";
    CACHE_SITE_DETAIL    = "siteDetail__id";
 
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_SOURCE_TREE    = AUTH_PATH + "channel/list";
    URL_SITE_DETAIL    = AUTH_PATH + "channel/detail/site/"; //{siteId}
    URL_SAVE_SITE      = AUTH_PATH + "channel/site";
    URL_CHANNEL_DETAIL = AUTH_PATH + "channel/detail/"; // {id}/{parentId}
    URL_SAVE_CHANNEL   = AUTH_PATH + "channel";
    URL_DEL_NODE       = AUTH_PATH + "channel/";
    URL_MOVE_NODE      = AUTH_PATH + "channel/move/";    // {id}/{toParentId}
    URL_SORT_NODE      = AUTH_PATH + "channel/sort/";    // {id}/{targetId}/{direction}
	URL_STOP_NODE      = AUTH_PATH + "channel/disable/"; // {id}
    URL_GET_OPERATION  = AUTH_PATH + "channel/operations/";   // {resourceId}
	
	URL_ARTICLE_LIST   = AUTH_PATH + "article/list/"; // {channelId}/{page}
    URL_DEL_ARTICLE    = AUTH_PATH + "article/";
    URL_MOVE_ARTICLE   = AUTH_PATH + "article/move/"; // {articleId}/{channelId}
    URL_LOCK_ARTICLE   = AUTH_PATH + "article/list";
    URL_SETTOP_ARTICLE = AUTH_PATH + "article/top/";  // {id}
	URL_SEARCH_ARTICLE = AUTH_PATH + "article/search/result";  // 暂时没用到，日后加上查询功能

    URL_PUBLISH_PROGRESS = AUTH_PATH + "channel/publish/";   // {id}/{category}
	URL_SYNC_PROGRESS    = AUTH_PATH + "channel/progress/";  // GET
    URL_CONCEAL_PROGRESS = AUTH_PATH + "channel/progress/";  // DELETE

	URL_CREATE_INDEX     = AUTH_PATH + "timer/excute/";  // {siteId}/{strategyId}/{increment}

	if(IS_TEST) {
		URL_SOURCE_TREE    = "data/site_init.xml?";
		URL_SITE_DETAIL    = "data/siteDetail.xml?";
		URL_SAVE_SITE      = "data/_success.xml?";
		URL_CHANNEL_DETAIL = "data/channelDetail.xml?";
		URL_SAVE_CHANNEL   = "data/_success.xml?";
		URL_DEL_NODE       = "data/_success.xml?";
		URL_MOVE_NODE      = "data/_success.xml?";
		URL_SORT_NODE      = "data/_success.xml?";
		URL_STOP_NODE      = "data/_success.xml?";
		URL_GET_OPERATION  = "data/operation.xml?";

		URL_ARTICLE_LIST   = "data/article_list.xml?";
		URL_DEL_ARTICLE    = "data/_success.xml?";
		URL_MOVE_ARTICLE   = "data/_success.xml?";
		URL_LOCK_ARTICLE   = "data/_success.xml?";
		URL_SETTOP_ARTICLE = "data/_success.xml?";
		URL_SEARCH_ARTICLE = "data/articlelist.xml?";

		URL_PUBLISH_PROGRESS = "data/progress.xml?";
		URL_SYNC_PROGRESS    = "data/_success.xml?";
		URL_CONCEAL_PROGRESS = "data/_success.xml?";

		URL_CREATE_INDEX     = "data/_success.xml?";
	}
 
    function init() { 
        initPaletteResize();
        initNaviBar("cms.1");
        initMenus();
        initWorkSpace();
        initEvents();

        loadInitData();
    }

	function isSite() {
		return getTreeAttribute("isSite") == "1";
	}
	function isChannel() {
		return getTreeAttribute("isSite") == "0";
	}
  
    function initMenus() { 
        var item1 = {
            label:"新建站点",
            callback:addNewSite,
            visible:function() { return isTreeRoot() && getOperation("1");}
        }
        var item2 = {
            label:"新建栏目",
            callback:addNewChannel,
            visible:function() { return !isTreeRoot() && getOperation("2");}
        }
        var item3 = {
            label:"新建文章",
            callback:addNewArticle,
            visible:function() { return isChannel() && getOperation("3");}
        }

		var submenu4 = new Menu(); // 发布
        var item4 = {
            label:"发布",
            icon:ICON + "cms/publish_source.gif",
            visible:function() { return !isTreeRoot() && getOperation("4");},
            submenu:submenu4
        }
        var subitem4a = {
            label:"增量发布",
            callback:function() { 
                publishArticle(1);
            }
        }
        var subitem4b = {
            label:"完全发布",
            callback:function() { 
                publishArticle(2);
            }
        }
		submenu4.addItem(subitem4a);
        submenu4.addItem(subitem4b);

        var item5 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            visible:function() { return !isTreeRoot() && getOperation("5");}
        }
        var item6 = {
            label:"删除",
            callback: function() { delTreeNode(URL_DEL_NODE) },
            icon:ICON + "icon_del.gif",
            visible:function() { return !isTreeRoot() && getOperation("6");}
        }
        var item7 = { 
            label:"启用",
            callback: function() { stopOrStartTreeNode("0"); },
            icon:ICON + "start.gif",
            visible:function() { return isTreeNodeDisabled() && !isTreeRoot() && getOperation("7");}
        }
        var item8 = {
            label:"停用",
            callback: function() { stopOrStartTreeNode("1"); },
            icon:ICON + "stop.gif",
            visible:function() { return !isTreeNodeDisabled() && !isTreeRoot() && getOperation("7");}
        }
        var item9 = {
            label:"移动到...",
            callback:moveChannelTo,
            icon:ICON + "move.gif",
            visible:function() { return isChannel() && getOperation("6");}
        }
        var item10 = {
            label:"浏览文章",
            callback: function() { showArticleList(); },
            icon:ICON + "view_list.gif",
            visible:function() { return isChannel() && getOperation("1");}
        }         

		var submenu5 = new Menu(); // 即时建立索引
        var item11 = {
            label:"即时建索引",
            icon:ICON + "cms/time_tactic.gif",
            visible:function() { return isSite() && getOperation("4");},
            submenu:submenu5
        }
        var subitem5a = {
            label:"增量索引",
            callback:function() { 
                createLuceneIndex(2, 1);
            }
        }
        var subitem5b = {
            label:"重建索引",
            callback:function() { 
                createLuceneIndex(2, 0);
            }
        }
		submenu5.addItem(subitem5a);
        submenu5.addItem(subitem5b);

        var menu1 = new Menu();
		menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addItem(item5);
        menu1.addItem(item6);
		menu1.addItem(item7);
        menu1.addItem(item8);
        menu1.addItem(item9);
        menu1.addSeparator();
        menu1.addItem(item4);
		menu1.addItem(item10);
		menu1.addItem(item11);

        $$("tree").contextmenu = menu1;

		initGridMenu();
    }
 
    function loadInitData() { 
		Ajax({
			url : URL_SOURCE_TREE,
			onresult : function() { 
				var _operation = this.getNodeValue(XML_OPERATION);

				var siteTreeNode = this.getNodeValue(XML_MAIN_TREE);
				var tree = $T("tree", siteTreeNode); 

				var treeObj = tree.element;
				treeObj.onTreeNodeActived = function(eventObj) { 
					onTreeNodeActived(eventObj);
				}
				treeObj.onTreeNodeDoubleClick = function(eventObj) { 
					onTreeNodeDoubleClick(eventObj);
				}
				treeObj.onTreeNodeMoved = function(eventObj) { 
					sortTreeNode(URL_SORT_NODE, eventObj);
				}
				treeObj.onTreeNodeRightClick = function(eventObj) { 
					onTreeNodeRightClick(eventObj, true);
				}
			}
		});
    }

    function onTreeNodeDoubleClick(eventObj) { 
        var treeNode = eventObj.treeNode;
        var id = treeNode.getId();
        getTreeOperation(treeNode, function(_operation) {
            if("_rootId" == id )  return;

			var canShowArticleList = checkOperation("1", _operation);                
			if( isChannel() && canShowArticleList) { 
				showArticleList();
			} 
			else {
				var canEdit = checkOperation("5", _operation);
				if( canEdit ) {
					editTreeNode();                    
				}
			}            
        });
    }

	function editTreeNode() { 
        if( isChannel() ) { 
            editChannelInfo();
        } else {
			editSiteInfo();
        }
    }
 
    function addNewSite() { 
        var treeID = DEFAULT_NEW_ID;
        var treeName = "站点";
        var callback = {};
        callback.onTabChange = function() { 
            setTimeout(function() { 
                loadSiteDetailData(treeID);
            }, TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_ADD.replace(/\$label/i, treeName);
        inf.phases = null;
        inf.callback = callback;
        inf.SID = CACHE_SITE_DETAIL + treeID;
        var tab = ws.open(inf);
    }

    function editSiteInfo() { 
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		var callback = {};
		callback.onTabChange = function() { 
			setTimeout(function() { 
				loadSiteDetailData(treeID);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
		inf.SID = CACHE_SITE_DETAIL + treeID;
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadSiteDetailData(treeID) { 
		Ajax({
			url : URL_SITE_DETAIL + treeID,
			onresult : function() { 
				var siteInfoNode = this.getNodeValue(XML_CHANNEL_INFO);
				Cache.XmlDatas.add(treeID, siteInfoNode);

				var xform = $X("page1Form", siteInfoNode);

				// 设置翻页按钮显示状态
				$$("page1BtPrev").style.display = $$("page1BtNext").style.display = "none";

				// 设置保存按钮操作
				$$("page1BtSave").onclick = function() { 
					saveSite(treeID);
				}
			}
		});
    }
 
    function saveSite(treeID) {
        // 校验page1Form数据有效性
        var xform = $X("page1Form");
        if( !xform.checkForm() ) return;

        var p = new HttpRequestParams();
        p.url = URL_SAVE_SITE;

        //是否提交
        var flag = false;
 
		var siteInfoNode = Cache.XmlDatas.get(treeID);
		if( siteInfoNode ) {
			var dataNode = siteInfoNode.selectSingleNode(".//data");
			if( dataNode ) {
				flag = true;                    
				p.setXFormContent(dataNode);
			}
		}
 
        if( flag ) {
            var request = new HttpRequest(p);

            syncButton([ $$("page1BtSave")], request);

            request.onresult = function() { 
				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode("_rootId", treeNode);

				ws.closeActiveTab();
            }
            request.onsuccess = function() { 
				// 更新树节点名称
				var name = xform.getData("name");
				modifyTreeNode(treeID, "name", name, true);

				ws.closeActiveTab();
            }
            request.send();
        }
    }
 
    function addNewChannel() { 
        var treeNode = $T("tree").getActiveTreeNode();
		var parentID = treeNode.getId();
		var channelName = "栏目";
		var channelID = DEFAULT_NEW_ID;

		var callback = {};
		callback.onTabChange = function() { 
			setTimeout(function() { 
				loadChannelDetailData(channelID, parentID);
			}, TIMEOUT_TAB_CHANGE);
		};
		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, channelName);
		inf.callback = callback;
		inf.SID = CACHE_CHANNEL_DETAIL + channelID;
		var tab = ws.open(inf);
    }
 
    function editChannelInfo() { 
		var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		var callback = {};
		callback.onTabChange = function() { 
			setTimeout(function() { 
				loadChannelDetailData(treeID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
		inf.SID = CACHE_CHANNEL_DETAIL + treeID;
		inf.defaultPage = "page1";
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadChannelDetailData(treeID, parentID) { 
		var treeNode = $T("tree").getActiveTreeNode();
		Ajax({
			url : URL_CHANNEL_DETAIL + treeID + "/" + (parentID || 0),
			onresult : function() { 
				var channelInfoNode = this.getNodeValue(XML_CHANNEL_INFO);
				Cache.XmlDatas.add(treeID, channelInfoNode);

				var xform = $X("page1Form", channelInfoNode);

				// 设置翻页按钮显示状态
				$$("page1BtPrev").style.display = $$("page1BtNext").style.display = "none";

				// 设置保存按钮操作
				$$("page1BtSave").onclick = function() { 
					saveChannel(treeID, parentID);
				}
			}
		});
    }
 
    function saveChannel(treeID, parentID) { 
        // 校验page1Form数据有效性
        var xform = $X("page1Form");
        if( !xform.checkForm() ) return;

        var p = new HttpRequestParams();
         p.url = URL_SAVE_CHANNEL;
        
        // 是否提交
        var flag = false;
 
		var channelInfoNode = Cache.XmlDatas.get(treeID);
		if(channelInfoNode) { 
			var channelInfoDataNode = channelInfoNode.selectSingleNode(".//data");
			if(channelInfoDataNode) { 
				flag = true;
				p.setXFormContent(channelInfoDataNode);
			}
		}
 
        if( flag ) { 
            var request = new HttpRequest(p);
 
            syncButton([$$("page1BtSave")], request);

            request.onresult = function() { 
				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentID, treeNode);

				ws.closeActiveTab();
            }
            request.onsuccess = function() { // 更新树节点名称
				var name = xform.getData("name");
				modifyTreeNode(treeID, "name", name, true);

				ws.closeActiveTab();
            }
            request.send();
        }
    }
 
    function moveChannelTo() { 
        var tree = $T("tree");
		var treeNodeID = tree.getActiveTreeNode().getId();
		var action = "moveChannel";
		var returnObj = window.showModalDialog("channelTree.html", {id:treeNodeID, action:action}, "dialogWidth:300px;dialogHeight:400px;");
		if( returnObj ) { 
			targetId = returnObj.id;
			moveTreeNode(tree, treeNodeID, targetId); 
		}
    }

    function addNewArticle() { 
		var channelId = $T("tree").getActiveTreeNode().getId();
		var returnValue = window.showModalDialog("article.html",{title:"新建文章",channelId:channelId,articleId:null},"dialogWidth:900px;dialogHeight:700px;status:yes");
		if( returnValue ) { 
			showArticleList(channelId); 
		}
    }

    function editArticleInfo() { 
		var canEdit = getGridOperation("5");
		if( !canEdit ) { 
			return;            
		}

        var articleId = getArticleAttribute("id");
		var channelId = getArticleAttribute("channel.id");

        var returnValue = window.showModalDialog("article.html",{title:"编辑文章",channelId:channelId,articleId:articleId},"dialogWidth:900px;dialogHeight:700px;status:yes");
        if(returnValue) { 
			showArticleList(channelId);
        }
    }
 
    function moveArticleTo() { 
        var articleId = getArticleAttribute("id");;
        var action = "moveArticle";
        var returnObj = window.showModalDialog("channelTree.html", {action:action}, "dialogWidth:300px;dialogHeight:400px;");
        if( returnObj ) { 
			Ajax({
				url: URL_MOVE_ARTICLE + articleId + "/" + returnObj.id,
				onsuccess: function() {
					// 刷新当前栏目文章列表
					var channelId = getTreeNodeId();
					showArticleList(channelId); 
				}
			});
        }
    }

    function delArticle() { 
        if( !confirm("您确定要删除吗？") ) { 
            return;
        }
        
		var articleId = getArticleAttribute("id");
		Ajax({
			url : URL_DEL_ARTICLE + articleId,
			method: "DELETE",
			onsuccess : function() { 			
				var channelId = getTreeNodeId();
				showArticleList(channelId); 
			}
		});
    } 
 
    /* 获取文章属性 */
    function getArticleAttribute(name) { 
        return $G("grid").getRowAttributeValue(name);
    }

    /* 置顶文章 */
    function setTopArticle(isTop) { 
		var articleId = getArticleAttribute("id");
		Ajax({
			url : URL_SETTOP_ARTICLE + articleId,
			onsuccess : function() { 			
				var channelId = getTreeNodeId();
				showArticleList(channelId); 
			}
		});
    }
 
    /*
     *	检测用户列表右键菜单项是否可见
     *	参数：	string:code     操作码
     *	返回值：是够有权限
     */
    function getGridOperation(code) {
		var flag = false;
        var channelId = getArticleAttribute("channel.id");
		if( channelId ) {
			var channelNode = $T("tree").getTreeNodeById(channelId);
			var _operation = channelNode.getAttribute("_operation");
			flag = checkOperation(code, _operation);
		}
        return flag;
    }
  
    /*
     *	发布文章
     *	参数：	string:type     完全发布“2”; 增量发布“1”
     */
    function publishArticle(category) { 
		var channelId = getTreeNodeId();
		Ajax({
			url : URL_PUBLISH_PROGRESS + channelId + "/" + category,
			onresult : function() { 			
				var data = this.getNodeValue("ProgressInfo");
				var progress = new Progress(URL_SYNC_PROGRESS, data, URL_CONCEAL_PROGRESS);
				progress.oncomplete = function() { 
					// 发布完成：刷新grid
					showArticleList(channelId); 
				}
				progress.start();
			}
		});
    }

	function createLuceneIndex(strategyId, increment) {
		var siteId = getTreeNodeId();
		Ajax({
			url : URL_CREATE_INDEX + siteId + "/" + strategyId + "/" + increment,
			onresult : function() { 			
				var data = this.getNodeValue("ProgressInfo");
				var progress = new Progress(URL_SYNC_PROGRESS, data, URL_CONCEAL_PROGRESS);
				progress.oncomplete = function() { 
					alert("索引创建完成。"); 
				}
				progress.start();
			}
		});
	}

    function initGridMenu() { 
		var item1 = {
            label:"编辑",
            callback:editArticleInfo,
            icon:ICON + "edit.gif",
            visible:function() { return "1" == getArticleAttribute("status") && getGridOperation("5");}
        }
        var item2 = {
            label:"删除",
            callback:delArticle,
            icon:ICON + "icon_del.gif",
            visible:function() { return getGridOperation("3");}
        }
        var item3 = {
            label:"移动到...",
            callback:moveArticleTo,
            icon:ICON + "move.gif",
            visible:function() { return getGridOperation("6");}
        }
        var item4 = {
            label:"置顶",
            callback:function() { 
                setTopArticle("1");
            },
            icon:ICON + "stick.gif",
            visible:function() { return "1" != getArticleAttribute("isTop") && getGridOperation("5");}
        }
        var item5 = {
            label:"解除置顶",
            callback:function() { 
                setTopArticle("0");
            },
            icon:ICON + "unstick.gif",
            visible:function() { return "1" == getArticleAttribute("isTop") && getGridOperation("5");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
		menu1.addItem(item2);
		menu1.addItem(item3);
		menu1.addItem(item4);
		menu1.addItem(item5);
 
        $$("grid").contextmenu = menu1;
    }

    /* 显示文章列表 */
    function showArticleList(channelId) {
		channelId = channelId || getTreeNodeId();
		showGrid(URL_ARTICLE_LIST + channelId, XML_ARTICLE_LIST, editArticleInfo);
	}   
 

    window.onload = init;