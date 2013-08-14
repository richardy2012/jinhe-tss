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
    URL_SETTOP_ARTICLE = "data/_success.xml";
    URL_SITE_DETAIL ="data/site1.xml";
    URL_SAVE_SITE = "data/_success.xml";
    URL_UPDATE_SITE = "data/_success.xml";
    URL_CHANNEL_DETAIL ="data/channel.xml";
    URL_SAVE_CHANNEL = "data/_success.xml";
    URL_UPDATE_CHANNEL = "data/_success.xml";
    URL_DEL_NODE = "data/_success.xml";
    URL_MOVE_NODE = "data/_success.xml";
    URL_SORT_NODE = "data/_success.xml";
	URL_STOP_NODE = "data/_success.xml";
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
 
    function init() { 
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
  
    function initMenus() { 
        var submenu1 = new Menu();//站点
        var submenu2 = new Menu();//栏目
        var submenu3 = new Menu();//文章
        var submenu4 = new Menu();//发布
        
        var item1 = {
            label:"新建站点",
            callback:addNewSite,
            visible:function() { return "_rootId"==getTreeAttribute("id") && true==getOperation("1");}
        }
        var item2 = {
            label:"新建栏目",
            callback:addNewChannel,
            visible:function() { return "_rootId"!=getTreeAttribute("id") && true==getOperation("2");}
        }
        var item3 = {
            label:"新建文章",
            callback:addNewArticle,
            visible:function() { return "0"==getTreeAttribute("isSite") && "_rootId"!=getTreeAttribute("id") && true==getOperation("3");}
            
        }

        var item4 = {
            label:"发布",
            callback:null,
            icon:ICON + "icon_publish_source.gif",
            visible:function() { return "_rootId"!=getTreeAttribute("id") && true==getOperation("4");},
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

        var item5 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            visible:function() { return "_rootId"!=getTreeAttribute("id") && true==getOperation("5");}
        }
        var item6 = {
            label:"删除",
            callback: delTreeNode(URL_DELETE_CHANNEL),
            icon:ICON + "del.gif",
            visible:function() { return "_rootId"!=getTreeAttribute("id") && true==getOperation("6");}
        }
        var item7 = { 
            label:"启用",
            callback:startSite,
            icon:ICON + "start.gif",
            visible:function() { return "0"!=getTreeAttribute("disabled") && "_rootId"!=getTreeAttribute("id") && true==getOperation("8");}
        }
        var item8 = {
            label:"停用",
            callback:stopSite,
            icon:ICON + "stop.gif",
            visible:function() { return "0"==getTreeAttribute("disabled") && "_rootId"!=getTreeAttribute("id") && true==getOperation("7");}
        }
        var item9 = {
            label:"移动到...",
            callback:moveChannelTo,
            icon:ICON + "move.gif",
            visible:function() { return "0"==getTreeAttribute("isSite") && true==getOperation("6");}
        }
         var item14 = {
            label:"搜索文章...",
            callback:searchArticle,
            icon:ICON + "search.gif",
            visible:function() { return "_rootId"!=getTreeAttribute("id") && true==getOperation("16");}
        }
        var item16 = {
            label:"浏览文章",
            callback:showArticleList,
            icon:ICON + "view_list.gif",
            visible:function() { return "0"==getTreeAttribute("isSite") && "_rootId"!=getTreeAttribute("id") && true==getOperation("14");}
        }
        var item17 = { 
            label:"启用站点",
            callback:startAll,
            icon:ICON + "start_all.gif",
            visible:function() { return "1"==getTreeAttribute("disabled") && "1"==getTreeAttribute("isSite") && true==getOperation("7");}
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
 
    function loadInitData() { 
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function() { 
            var _operation = this.getNodeValue(XML_OPERATION);

            var siteTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var tree = $T("tree", siteTreeNode); 

            var treeObj = $$("tree");
            treeObj.load(xmlIsland.node);

            treeObj.onTreeNodeActived = function(eventObj) { 
                onTreeNodeActived(eventObj);
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj) { 
                onTreeNodeDoubleClick(eventObj);
            }
            treeObj.onTreeNodeMoved = function(eventObj) { 
                sort(eventObj);
            }
            treeObj.onTreeNodeRightClick = function(eventObj) { 
                onTreeNodeRightClick(eventObj);
            }
        }
        request.send();
    }

    function onTreeNodeDoubleClick(eventObj) { 
        var treeNode = eventObj.treeNode;
        var id = treeNode.getId();
        var isSite = treeNode.getAttribute("isSite");
        getTreeOperation(treeNode, function(_operation) {
            if("_rootId" == id )  return;

			var canShowArticleList = checkOperation("1", _operation);                
			if("0"==isSite && canShowArticleList) { 
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
        var isSite = getTreeAttribute("isSite");
        if("1" == isSite) { 
            editSiteInfo();
        } else {
            editChannelInfo();
        }
    }
 
    function addNewSite() { 
        var treeID = DEFAULT_NEW_ID;
        var treeName = "站点";
        var callback = {};
        callback.onTabClose = function(eventObj) { 
            delCacheData(eventObj.tab.SID);
        };
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
		callback.onTabClose = function(eventObj) { 
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() { 
			setTimeout(function() { 
				loadSiteDetailData(treeID,editable);
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
				var siteInfoNode = this.getNodeValue(XML_SITE_INFO);
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
		callback.onTabClose = function(eventObj) { 
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() { 
			setTimeout(function() { 
				loadChannelDetailData(channelID, parentID);
			}, TIMEOUT_TAB_CHANGE);
		};
		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, channelName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_CHANNEL_DETAIL + channelID;
		var tab = ws.open(inf);
    }
 
    function editChannelInfo() { 
		var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		var callback = {};
		callback.onTabClose = function(eventObj) { 
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() { 
			setTimeout(function() { 
				loadChannelDetailData(treeID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
		inf.SID = CACHE_CHANNEL_DETAIL + treeID;
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadChannelDetailData(treeID,editable,parentID,kind,isNew,siteId) { 
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
					saveChannel(cacheID,parentID,kind,isNew);
				}
			}
		});
    }
 
    function saveChannel(cacheID, parentID) { 
        // 校验page1Form数据有效性
        var xform = $X("page1Form");
        if( !xform.checkForm() ) return;

        var p = new HttpRequestParams();
         p.url = URL_SAVE_CHANNEL;
        
        // 是否提交
        var flag = false;
 
		var channelInfoNode = Cache.XmlDatas.get(cacheID);
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
		var returnObj = window.showModalDialog("channelTree.htm", {id:treeNodeID, action:action}, "dialogWidth:300px;dialogHeight:400px;");
		if( returnObj ) { 
			targetId = returnObj.id;
			moveTreeNode(tree, treeNodeID, targetId); 
		}
    }
 
    function stopOrStartTreeNode(state) {
		var iconName = isSite() ? "cms/site" : "cms/channel";
		stopOrStartTreeNode(state, iconName);
	}

	function isSite() {
		return getTreeAttribute("isSite") == "1";
	}
	function isChannel() {
		return getTreeAttribute("isSite") == "0";
	}


    /* 显示文章列表 */
    function showArticleList(groupId) {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = groupId || treeNode.getId();

		var p = new HttpRequestParams();
		p.url = URL_USER_LIST + treeID + "/1";
		var request = new HttpRequest(p);
		request.onresult = function() {
			$G("grid", this.getNodeValue(XML_USER_LIST)); 
			var gridToolBar = $$("gridToolBar");

			var pageListNode = this.getNodeValue(XML_PAGE_INFO);			
			initGridToolBar(gridToolBar, pageListNode, function(page) {
				request.params.url = XML_USER_LIST + treeID + "/" + page;
				request.onresult = function() {
					$G("grid", this.getNodeValue(XML_USER_LIST)); 
				}				
				request.send();
			} );
			
			var gridElement = $$("grid"); 
			gridElement.onDblClickRow = function(eventObj) {
				editUserInfo();
			}
			gridElement.onRightClickRow = function() {
				$$("grid").contextmenu.show(event.clientX, event.clientY);
			}   
			gridElement.onScrollToBottom = function () {			
				var currentPage = gridToolBar.getCurrentPage();
				if(gridToolBar.getTotalPages() <= currentPage) return;

				var nextPage = parseInt(currentPage) + 1; 
				request.params.url = XML_USER_LIST + treeID + "/" + nextPage;
				request.onresult = function() {
					$G("grid").load(this.getNodeValue(XML_REPORT_DATA), true);
					initGridToolBar(gridToolBar, this.getNodeValue(XML_PAGE_INFO));
				}				
				request.send();
			}
		}
		request.send();
	}

    function addNewArticle() { 
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var channelId = treeNode.getId();
		var articleName = "文章";
		var articleId = DEFAULT_NEW_ID;
		var returnValue = window.showModalDialog("article.htm",{title:"新建文章",channelId:channelId,articleId:articleId},"dialogWidth:900px;dialogHeight:700px;status:yes");
		if( returnValue ) { 
			showArticleList(channelId); 
		}
    }
 
    function moveArticleTo() { 
        var articleId = $G("grid").getRowAttributeValue("id");;
        var action = "moveArticle";
        var returnObj = window.showModalDialog("channelTree.htm", {action:action}, "dialogWidth:300px;dialogHeight:400px;");
        if( returnObj ) { 
            var p = new HttpRequestParams();
            p.url = URL_MOVE_ARTICLE + articleId + "/" + returnObj.id;

			var request = new HttpRequest(p);
            request.onsuccess = function() { 
                // 删除当前栏目文章列表
				var channelId = getTreeNodeId();
				showArticleList(channelId); 
            }
            request.send();
        }
    }

    function delArticle() { 
        if( !confirm("您确定要删除吗？") ) { 
            return;
        }
        
		var articleId = getArticleAttribute("id");
		Ajax({
			url :  URL_DEL_ARTICLE + articleId,
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
			url : URL_SETTOP_ARTICLE + articleId + "/" + isTop,
			onsuccess : function() { 			
				var channelId = getTreeNodeId();
				showArticleList(channelId); 
			}
		});
    }
 
    /*
     *	函数说明：检测用户列表右键菜单项是否可见
     *	参数：	string:code     操作码
     *	返回值：
     */
    function getUserOperation(code) { 
        var flag = false;
        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex) { 
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
    function getGridOperation(rowIndex,callback) { 
        var gridObj = $("grid");
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var id = rowNode.getAttribute("channelId");
        var _operation = rowNode.getAttribute("_operation");

        if(null==_operation || ""==_operation) { //如果节点上还没有_operation属性，则发请求从后台获取信息
            var p = new HttpRequestParams();
            p.url = URL_GET_ARTICLE_OPERATION;
            p.setContent("channelId",id);

            var request = new HttpRequest(p);
            request.onresult = function() { 
                _operation = this.getNodeValue(XML_OPERATION);
                rowNode.setAttribute("_operation",_operation);

                if(null!=callback) { 
                    callback(_operation);
                }
            }
            request.send();
            
        }else{
            if(null!=callback) { 
                callback(_operation);
            }
        }
    }
 
  
    /*
     *	函数说明：发布文章
     *	参数：	string:type         区分完全发布“2”和增量发布“1”
     *	返回值：
     */
    function publishArticle(category) { 
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) { 
            var id = treeNode.getId();

            var p = new HttpRequestParams();
            
            var isSite = getTreeAttribute("isSite");
            if("1"==isSite) { 
                p.url = URL_SITE_PUBLISH_PROGRESS;
            }else{
                p.url = URL_CHANNEL_PUBLISH_PROGRESS;
            }
			p.setContent("channelId",id);
            p.setContent("category",category);
            var request = new HttpRequest(p);
            request.onresult = function() { 
                var data = this.getNodeValue("ProgressInfo");
                var url = "";
                if("1"==isSite) { 
	                url = URL_SITE_PUBLISH_PROGRESS;
	            }else{
	                url = URL_CHANNEL_PUBLISH_PROGRESS;
	            }
	            var progress = new Progress(URL_GET_PROGRESS,data,URL_CONCEAL_PROGRESS);
                progress.oncomplete = function() { 
                    //发布完成
					//如果当前grid显示为此文章所在栏目，则刷新grid
					var gridObj = $("grid");
					if(true==gridObj.hasData_Xml()) { 
						var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
						var tempChannelId = tempXmlIsland.getAttribute("channelId");
						if(tempChannelId==id) { 
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
    function searchArticle() { 

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) { 
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var type = treeNode.getAttribute("isSite");
            var cacheID = CACHE_SEARCH_ARTICLE + treeID;
            var condition = window.showModalDialog("searcharticle.htm",{type:type,channelId:treeID,title:"搜索\""+treeName+"\"下的文章"},"dialogWidth:250px;dialogHeight:250px;");
            if(null!=condition) { 
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
    function loadSearchGridData(cacheID,page,sortName,direction) { 
        var condition = Cache.Variables.get("condition");
        if(null!=condition) { 
            var p = new HttpRequestParams();
            p.url = URL_SEARCH_ARTICLE;

            var xmlReader = new XmlReader(condition.dataXml);
            var dataNode = new XmlNode(xmlReader.documentElement);
            p.setXFormContent(dataNode, condition.prefix);
            p.setContent("condition.page.pageNum", page);
            if(null!=sortName && null!=direction) { 
                p.setContent("condition.field", sortName);
                p.setContent("condition.orderType", direction);
            }

            var request = new HttpRequest(p);
            request.onresult = function() { 
                var articleListNode = this.getNodeValue(XML_ARTICLE_LIST);
                var articleListNodeID = cacheID+"."+XML_ARTICLE_LIST;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                //给文章grid数据根节点增加channelId等属性
                articleListNode.setAttribute("channelId","search");

                //给当前排序列加上_direction属性
                if(null!=sortName && null!=direction) { 
                    var column = articleListNode.selectSingleNode("//column[@name='" + sortName + "']");
                    if(null!=column) { 
                        column.setAttribute("_direction",direction);
                    }
                }

                Cache.XmlDatas.add(articleListNodeID,articleListNode);
                Cache.XmlDatas.add(pageListNodeID,pageListNode);
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
    function initSearchGrid(cacheID) { 
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function() { 
            loadGridDataFromCache(cacheID);
            loadGridEvents();

            //刷新工具条
            onInactiveRow();
        });
    
    }


    function initGridMenu() { 
        var gridObj = $("grid");
		var item1 = {
            label:"编辑",
            callback:editArticleInfo,
            icon:ICON + "edit.gif",
            enable:function() { return true},
            visible:function() { return "4"!=getArticleAttribute("status") && "5"!=getArticleAttribute("status") && "-1"!=getArticleAttribute("status") && "1"!=getArticleAttribute("state") && true==getUserOperation("a_5");}
        }
        var item2 = {
            label:"删除",
            callback:delArticle,
            icon:ICON + "del.gif",
            enable:function() { return true;},
            visible:function() { return true==getUserOperation("a_3");}
        }
        var item3 = {
            label:"移动到...",
            callback:moveArticleTo,
            icon:ICON + "move.gif",
            enable:function() { return true;},
            visible:function() { return "0"==getArticleAttribute("state") && true==getUserOperation("a_6");}
        }
        var item4 = {
            label:"置顶",
            callback:function() { 
                setTopArticle("1");
            },
            icon:ICON + "stick.gif",
            enable:function() { return true;},
            visible:function() { return "1"!=getArticleAttribute("isTop") && true==getUserOperation("a_17");}
        }
        var item5 = {
            label:"解除置顶",
            callback:function() { 
                setTopArticle("0");
            },
            icon:ICON + "unstick.gif",
            enable:function() { return true;},
            visible:function() { return "1"==getArticleAttribute("isTop") && true==getUserOperation("a_18");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
		menu1.addItem(item2);
		menu1.addItem(item3);
		menu1.addItem(item4);
		menu1.addItem(item5);
 
        gridObj.contextmenu = menu1;
    }

	function showArticleList() { 
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode) { 
            var id = treeNode.getId();
            initGrid(id);
        }
    }
 
    /*
     *	函数说明：grid初始化
     *	参数：	string:id   grid数据相关树节点id
     *	返回值：
     */
    function initGrid(id) { 
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function() { 
            loadGridEvents();
            loadGridData(id,"1");//默认第一页
        });
    }
 
    function loadGridEvents() { 
        var gridObj = $("grid");

        gridObj.onclickrow = function() { 
            onClickRow(event);
        }
        gridObj.ondblclickrow = function() { 
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
    function loadGridData(treeID,page,sortName,direction) { 
        var cacheID = CACHE_TREE_NODE_GRID + treeID;
		var p = new HttpRequestParams();
		p.url = URL_ARTICLE_LIST;
		p.setContent("channelId", treeID);
		p.setContent("page", page);
		if(null!=sortName && null!=direction) { 
			p.setContent("field", sortName);
			p.setContent("orderType", direction);
		}

		var request = new HttpRequest(p);
		request.onresult = function() { 
			var articleListNode = this.getNodeValue(XML_ARTICLE_LIST);
			var articleListNodeID = cacheID+"."+XML_ARTICLE_LIST;

			var pageListNode = this.getNodeValue(XML_PAGE_LIST);
			var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

			//给grid节点加上channelId属性表示栏目id
			articleListNode.setAttribute("channelId", treeID);

			//给当前排序列加上_direction属性
			if(null!=sortName && null!=direction) { 
				var column = articleListNode.selectSingleNode("//column[@name='" + sortName + "']");
				if(null!=column) { 
					column.setAttribute("_direction",direction);
				}
			}

			Cache.XmlDatas.add(articleListNodeID,articleListNode);
			Cache.XmlDatas.add(pageListNodeID,pageListNode);
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
    function loadGridDataFromCache(cacheID) { 
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_ARTICLE_LIST);
        if(null!=xmlIsland) { 
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
    function editArticleInfo(editable) { 
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowName = gridObj.getNamedNodeValue_Xml(rowIndex,"title");
        var rowID = rowNode.getAttribute("id");
        var articleType = rowNode.getAttribute("state");
        var workflowStatus = rowNode.getAttribute("status");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId) { 
            channelId = rowNode.getAttribute("channelId");
        }

        var returnValue = window.showModalDialog("article.htm",{title:"编辑文章",isNew:false,editable:editable,channelId:channelId,articleType:articleType,articleId:rowID,workflowStatus:workflowStatus},"dialogWidth:900px;dialogHeight:700px;status:yes");
        if(true==returnValue) { 

            //如果当前grid显示为此文章所在栏目，则刷新grid
            var gridObj = $("grid");
            if(true==gridObj.hasData_Xml()) { 
                var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                var tempChannelId = tempXmlIsland.getAttribute("channelId");
                if(tempChannelId==channelId) { 
                    loadGridData(tempChannelId,"1");//默认第一页

                    //刷新工具条
                    onInactiveRow();
                }
            }
        }
    }

	function onClickRow(eventObj) {    
        Focus.focus("gridTitle");
    }
 
    function onDblClickRow(eventObj) { 
        var rowIndex = eventObj.result.rowIndex_Xml;
        getGridOperation(rowIndex, function(_operation) { 
            //检测编辑权限
            var canEdit = checkOperation("a_5", _operation);

            var gridObj = $("grid");
            var rowNode = gridObj.getRowNode_Xml(rowIndex);
            var status = rowNode.getAttribute("status");
            var state = rowNode.getAttribute("state");

            if(true==canEdit && "4"!=status && "-1"!=status && "5"!=status && "1"!=state) { 
                editArticleInfo();            
            }
        });
    }
 
    function onRightClickRow(eventObj) { 
        var rowIndex = eventObj.result.rowIndex_Xml;
        var x = event.clientX;
        var y = event.clientY;

        getGridOperation(rowIndex,function(_operation) { 
            addWorkFlowMenu(x,y);  
        });
    }

    window.onload = init;

    //关闭页面自动注销
    logoutOnClose();