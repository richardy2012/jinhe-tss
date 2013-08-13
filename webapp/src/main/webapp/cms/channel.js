    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "SiteTree";
    XML_ARTICLE_LIST = "ArticleList";
    XML_ARTICLE_SEARCH = "ArticleSearch";
    XML_PAGE_LIST = "PageList";

    XML_ARTICLE_INFO = "ArticleInfo";
    XML_CHANNEL_INFO = "ChannelInfo";
    XML_CHANNEL_TREE = "ChannelTree";
    XML_SITE_INFO = "SiteInfo";
    XML_FILTER_INFO = "FilterInfo";

    XML_ATTACHS_LIST = "AttachsList";
    XML_ATTACHS_UPLOAD = "AttachsUpload";

    XML_ARTICLE_CONTENT = "ArticleContent";
    XML_ARTICLE_WORK_FLOW = "ArticleWorkFlow";

    XML_DISTRIBUTE_TO_EXIST_TREE = "ChannelDistributeTo";
    XML_DISTRIBUTE_FROM_EXIST_TREE = "ChannelDistributeFrom";
    XML_OPERATION = "Operation";
    XML_DISTRIBUTE_SOURCE = "DistributeSource";
    XML_DISTRIBUTE_ARTICLE_TREE = "DistributeArticleTree";
    XML_DISTRIBUTE_ARTICLE_EXIST_TREE = "DistributeArticleExistTree";
    XML_ASSOCIATE_ARTICLE_TREE = "AssociateArticleTree";
    XML_ASSOCIATE_ARTICLE_EXIST_TREE = "AssociateArticleExistTree";
    XML_ASSOCIATE_ARTICLE_LIST = "AssociateArticleList";
    XML_PREPARE_PUBLISH = "PreparedPublish";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_GRID_ROW_DETAIL = "row__id";
    CACHE_TREE_NODE_GRID = "treeNodeGrid__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_TOOLBAR = "toolbar__id";
    CACHE_ARTICLE_DETAIL = "article__id";
    CACHE_VIEW_ARTICLE_DETAIL = "viewArticle__id";
    CACHE_CHANNEL_DETAIL = "channelDetail__id";
    CACHE_VIEW_CHANNEL_DETAIL = "viewChannelDetail__id";
    CACHE_CHANNEL_DISTRIBUTE = "channelDistribute__id";
    CACHE_SITE_DETAIL = "siteDetail__id";
    CACHE_VIEW_SITE_DETAIL = "viewSiteDetail__id";
    CACHE_DISTRIBUTE_SOURCE = "distributeSource__id";
    CACHE_DISTRIBUTE_ARTICLE = "distributeArticle__id";
    CACHE_ASSOCIATE_ARTICLE = "associateArticle__id";
    CACHE_ASSOCIATE_ARTICLE_LIST = "associateArticleList__id";
    CACHE_SEARCH_ARTICLE = "searchArticle__id";
    CACHE_FILTER_DETAIL = "filter__id";
    /*
     *	名称
     */
    OPERATION_ADD = "新建$label";
    OPERATION_VIEW = "查看\"$label\"";
    OPERATION_DEL = "删除\"$label\"";
    OPERATION_EDIT = "编辑\"$label\"";
    OPERATION_DISTRIBUTE_SOURCE = "查看\"$label\"来源";
    OPERATION_DISTRIBUTE = "分发\"$label\"";
    OPERATION_ASSOCIATE = "关联\"$label\"";
    OPERATION_FILTER = "设置\"$label\"过滤信息";
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "data/site_init.xml";
    URL_ARTICLE_LIST = "data/articlelist.xml";
    URL_ARTICLE_SEARCH = "data/gridsearch.xml";
    URL_ARTICLE_DETAIL = "data/article.xml";
    URL_NEW_ARTICLE_DETAIL = "data/article.xml";
    URL_SAVE_ARTICLE = "data/_success.xml";
    URL_DEL_ARTICLE = "data/_success.xml";
    URL_MOVE_ARTICLE = "data/_success.xml";
    URL_STOP_ARTICLE = "data/_success.xml";
    URL_START_ARTICLE = "data/_success.xml";
    URL_COPY_ARTICLE = "data/_success.xml";
    URL_UP_DOWN_ARTICLE = "data/_success.xml";
    URL_UP_DOWN_ARTICLE_CROSS_PAGE = "data/_success.xml";
    URL_DISTRIBUTE_ARTICLE = "data/distributearticle.xml";
    URL_SAVE_DISTRIBUTE_ARTICLE = "data/_success.xml";
    URL_ASSOCIATE_ARTICLE = "data/associatearticle.xml";
    URL_ASSOCIATE_ARTICLE_LIST = "data/associatearticlelist.xml";
    URL_SAVE_ASSOCIATE_ARTICLE = "data/_success.xml";
    URL_RESHIP_ARTICLE = "data/_success.xml";
    URL_STICK_ARTICLE = "data/_success.xml";
    URL_SITE_DETAIL ="data/site1.xml";
    URL_SAVE_SITE = "data/_success.xml";
    URL_UPDATE_SITE = "data/_success.xml";
    URL_DEL_SITE = "data/_success.xml";
    URL_START_SITE = "data/_success.xml";
    URL_STOP_SITE = "data/_success.xml";
    URL_COPY_SITE = "data/copysite.xml";
    URL_CHANNEL_DETAIL ="data/channel.xml";
    URL_SAVE_CHANNEL = "data/_success.xml";
    URL_UPDATE_CHANNEL = "data/_success.xml";
    URL_DEL_CHANNEL = "data/_success.xml";
    URL_MOVE_CHANNEL = "data/_success.xml";
    URL_SORT_CHANNEL = "data/_success.xml";
    URL_COPY_CHANNEL = "data/_success.xml";
    URL_GET_WORKFLOW_STATUS = "data/workflowstatus.xml";
    URL_CHANNEL_DISTRIBUTE = "data/channeldistribute.xml";
    URL_SAVE_CHANNEL_DISTRIBUTE = "data/_success.xml";
    URL_GET_OPERATION = "data/operation.xml";
    URL_GET_ARTICLE_OPERATION = "data/operation1.xml";
    URL_DISTRIBUTE_SOURCE = "data/distributesource.xml";
    URL_SITE_PUBLISH_PROGRESS = "data/progress.xml";
    URL_CHANNEL_PUBLISH_PROGRESS = "data/progress.xml";
    URL_CANCEL_PUBLISH_PROGRESS = "data/_success.xml";
    URL_SEARCH_ARTICLE = "data/articlelist.xml";
    URL_SAVE_PUBLISH_ARTICLE = "data/_success.xml";
    URL_UPLOAD_FILE = "data/upload1.htm";
    URL_UPLOAD_SERVER_FILE = "data/upload2.htm";
    URL_START_ALL = "data/_success.xml";
    URL_FILTER_DETAIL = "data/filter.xml";
    URL_SAVE_FILTER = "data/_success.xml";
	URL_GET_PROGRESS = "data/_success.xml";
    URL_CONCEAL_PROGRESS = "data/_success.xml";

    URL_INIT = "site!getSiteAll.action";
    URL_ARTICLE_LIST = "article!getChannelArticles.action";
    URL_ARTICLE_SEARCH = "data/gridsearch.xml";
    URL_ARTICLE_DETAIL = "article!getArticleInfo.action";
    URL_NEW_ARTICLE_DETAIL = "article!initArticleInfo.action";
    URL_DEL_ARTICLE = "article!deleteArticle.action";
    URL_MOVE_ARTICLE = "article!moveArticle.action";
    URL_STOP_ARTICLE = "article!lockingArticle.action";
    URL_START_ARTICLE = "article!unLockingArticle.action";
    URL_COPY_ARTICLE = "article!copyArticle.action";
    URL_UP_DOWN_ARTICLE = "article!moveArticleDownOrUp.action";
    URL_UP_DOWN_ARTICLE_CROSS_PAGE = "article!moveArticleDownOrUpCross.action";
    URL_DISTRIBUTE_ARTICLE = "article!getDistributeArticleRelationShip.action";
    URL_SAVE_DISTRIBUTE_ARTICLE = "article!saveDistributeArticleRelationShip.action";
    URL_ASSOCIATE_ARTICLE = "article!getArticleExistRelationship.action";
    URL_ASSOCIATE_ARTICLE_LIST = "article!getPageArticlesByChannel.action";
    URL_SAVE_ASSOCIATE_ARTICLE = "article!relatedArticleTo.action";
    URL_RESHIP_ARTICLE = "article!reshipArticle.action";
    URL_STICK_ARTICLE = "article!doOrUndoTopArticle.action";
    URL_SITE_DETAIL ="site!getSiteDetail.action";
    URL_SAVE_SITE = "site!saveSite.action";
    URL_UPDATE_SITE = "site!updateSite.action";
    URL_DEL_SITE = "site!deleteSite.action";
    URL_START_SITE = "site!startSite.action";
    URL_STOP_SITE = "site!stopSite.action";
    URL_COPY_SITE = "site!copySite.action";
    URL_CHANNEL_DETAIL ="channel!getChannelDetail.action";
    URL_SAVE_CHANNEL = "channel!saveChannel.action";
    URL_UPDATE_CHANNEL = "channel!updateChannel.action";
    URL_DEL_CHANNEL = "channel!deleteChannel.action";
    URL_MOVE_CHANNEL = "channel!moveChannel.action";
    URL_SORT_CHANNEL = "channel!sortChannel.action";
    URL_COPY_CHANNEL = "channel!copyChannel.action";
    URL_GET_WORKFLOW_STATUS = "article!getWorkFlowStatus.action";
    URL_CHANNEL_DISTRIBUTE = "channel!getChannelRelationShip.action";
    URL_SAVE_CHANNEL_DISTRIBUTE = "channel!saveChannelRelationShip.action";
    URL_GET_OPERATION = "site!getOperatorByResourceId.action";
    URL_GET_ARTICLE_OPERATION = "article!getArticleOperation.action";
    URL_DISTRIBUTE_SOURCE = "article!getDistributeArticleSource.action";
    URL_SITE_PUBLISH_PROGRESS = "channel!publishSite.action";
    URL_CHANNEL_PUBLISH_PROGRESS = "channel!publishChannel.action";
    URL_CANCEL_PUBLISH_PROGRESS = "channel!doConceal.action";
    URL_SEARCH_ARTICLE = "article!getArticleList.action";
    URL_UPLOAD_FILE = "upload!execute.action";
    URL_UPLOAD_SERVER_FILE = "upload!execute.action";
    URL_START_ALL = "site!startAll.action";
    URL_FILTER_DETAIL = "site!initWordsFilterXForm.action";
    URL_SAVE_FILTER = "site!saveWordsFilterInfo.action";
    URL_GET_PROGRESS = "channel!getProgress.action";
    URL_CONCEAL_PROGRESS = "channel!doConceal.action";

    /*
     *	延时
     */
    TIMEOUT_TAB_CHANGE = 200;
    TIMEOUT_ARTICLE_SEARCH = 200;
    /*
     *	icon路径
     */
    ICON = "../platform/images/icon/";
    /*
     *	已发布文章流程状态常量
     */
    PUBLISHED_ARTICLE_STATUS = 4;

    var toolbar = null;

    /*
     *	函数说明：页面初始化
     *	参数：	
     *	返回值：
     */
    function init(){
        initPaletteResize();
        initListContainerResize();
//        initUserInfo();
        initToolBar();
        initNaviBar("cms.1");
        initMenus();
        initBlocks();
        initWorkSpace();
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

            var siteTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var siteTreeNodeID = CACHE_MAIN_TREE;

            Cache.XmlIslands.add(siteTreeNodeID,siteTreeNode);

            loadToolBar(_operation);
            initTree(siteTreeNodeID);
        }
        request.send();
    }

    /*
     *	函数说明：菜单初始化
     *	参数：	
     *	返回值：
     */
    function initMenus(){
        var submenu1 = new Menu();//站点
        var submenu2 = new Menu();//栏目
        var submenu3 = new Menu();//文章
        var submenu4 = new Menu();//发布
        
        //站点相关
        var item1 = {
            label:"新建站点",
            callback:addNewSite,
            enable:function(){return true;},
            visible:function(){return "_rootId"==getTreeAttribute("id") && true==getOperation("1");}
        }
        //栏目相关
        var item2 = {
            label:"新建栏目",
            callback:addNewChannel,
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("2");}
        }
        //文章相关
        var item3 = {
            label:"新建文章",
            callback:addNewArticle,
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("isSite") && "_rootId"!=getTreeAttribute("id") && true==getOperation("3");}
            
        }
        //发布相关
        var item4 = {
            label:"发布",
            callback:null,
            icon:ICON + "icon_publish_source.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("4");},
            submenu:submenu4
        }
        var subitem4a = {
            label:"增量发布",
            callback:function(){
                publishArticle(1);
            },
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var subitem4b = {
            label:"完全发布",
            callback:function(){
                publishArticle(2);
            },
            enable:function(){return true;},
            visible:function(){return true;}
        }

        //公共
        var item5 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("5");}
        }
        var item6 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("6");}
        }
        var item7 = { 
            label:"启用",
            callback:startSite,
            icon:ICON + "start.gif",
            enable:function(){return true;},
            visible:function(){return "0"!=getTreeAttribute("disabled") && "_rootId"!=getTreeAttribute("id") && true==getOperation("8");}
        }
        var item8 = {
            label:"停用",
            callback:stopSite,
            icon:ICON + "stop.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("disabled") && "_rootId"!=getTreeAttribute("id") && true==getOperation("7");}
        }
        var item9 = {
            label:"移动到...",
            callback:moveChannelTo,
            icon:ICON + "move.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("isSite") && true==getOperation("6");}
        }
        var item10 = {
            label:"复制到...",
            callback:copyChannelTo,
            icon:ICON + "copy_to.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("isSite") && true==getOperation("12");}
        }
        var item11 = {
            label:"复制站点",
            callback:copySite,
            icon:ICON + "copy.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getTreeAttribute("isSite") && true==getOperation("p_1");}
        }
        var item12 = {
            label:"复制",
            callback:copyChannel,
            icon:ICON + "copy.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("isSite") && true==getOperation("p_2");}
        }
        var item13 = {
            label:"分发设置",
            callback:distributeSetting,
            icon:ICON + "distribute.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("isSite") && true==getOperation("15");}
        }
        var item14 = {
            label:"搜索文章...",
            callback:searchArticle,
            icon:ICON + "search.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("16");}
        }
        var item15 = {
            label:"查看",
            callback:function(){
                editTreeNode(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("12");}
        }
        var item16 = {
            label:"浏览文章",
            callback:showArticleList,
            icon:ICON + "view_list.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getTreeAttribute("isSite") && "_rootId"!=getTreeAttribute("id") && true==getOperation("14");}
        }
        var item17 = { 
            label:"启用全部",
            callback:startAll,
            icon:ICON + "start_all.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getTreeAttribute("disabled") && "1"==getTreeAttribute("isSite") && true==getOperation("7");}
        }
        var item18 = { 
            label:"评分统计...",
            callback:function(){
                scoreCount("site");
            },
            icon:ICON + "score.gif",
            enable:function(){return true;},
            visible:function(){return "_rootId"!=getTreeAttribute("id") && true==getOperation("17");}
        }
        var item19 = { 
            label:"非法信息过滤",
            callback:editFilterInfo,
            icon:ICON + "filter.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getTreeAttribute('isSite') && true==getOperation("18");}
        }
        var item20 = { 
            label:"新闻抓取",
            callback:newsGather,
            icon:ICON + "radar.gif",
            enable:function(){return true;},
            visible:function(){return true;}
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

        var treeObj = $("tree");
        //menu1.attachTo(treeObj,"contextmenu");
        treeObj.contextmenu = menu1;
    }
    /*
     *	函数说明：Grid菜单初始化
     *	参数：	
     *	返回值：
     */
    function initGridMenu(){
        var gridObj = $("grid");
        var item1 = {
            label:"锁定",
            callback:lockingArticle,
            icon:ICON + "lock.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getArticleAttribute("state") && "0"!=getArticleAttribute("status") && true==getUserOperation("2");}
        }
        var item2 = {
            label:"解锁",
            callback:unLockingArticle,
            icon:ICON + "unlock.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getArticleAttribute("state") && "0"==getArticleAttribute("status") && true==getUserOperation("2");}
        }
        var item4 = {
            label:"删除",
            callback:delArticle,
            icon:ICON + "del.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("a_3");}
        }
        var item5 = {
            label:"移动到...",
            callback:moveArticleTo,
            icon:ICON + "move.gif",
            enable:function(){return true;},
            visible:function(){return "0"==getArticleAttribute("state") && true==getUserOperation("a_6");}
        }
        var item6 = {
            label:"下移",
            callback:moveArticleDown,
            icon:ICON + "down.gif",
            enable:function(){return true;},
            visible:function(){return true==canMoveArticleDown() && true==getUserOperation("a_12");}
        }
        var item7 = {
            label:"上移",
            callback:moveArticleUp,
            icon:ICON + "up.gif",
            enable:function(){return true;},
            visible:function(){return true==canMoveArticleUp() && true==getUserOperation("a_12");}
        }
        var item8 = {
            label:"隐藏列...",
            callback:function(){gridObj.hideCols();},
            icon:ICON + "hide_col.gif",
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var item9 = {
            label:"搜索...",
            callback:function(){gridObj.search();},
            icon:ICON + "search.gif",
            enable:function(){return true;},
            visible:function(){return true;}
        }
        var item10 = {
            label:"分发文章",
            callback:distributeArticle,
            icon:ICON + "distribute.gif",
            enable:function(){return true;},
            visible:function(){return "1"!=getArticleAttribute("state") && true==getUserOperation("a_19");}
        }
        var item11 = {
            label:"转载文章",
            callback:reshipArticle,
            icon:ICON + "reship_article.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("a_20");}
        }
        var item12 = {
            label:"复制",
            callback:copyArticle,
            icon:ICON + "copy.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("a_3");}
        }
        var item13 = {
            label:"复制到...",
            callback:copyArticleTo,
            icon:ICON + "copy_to.gif",
            enable:function(){return true;},
            visible:function(){return true==getUserOperation("a_12");}
        }
        var item14 = {
            label:"置顶",
            callback:function(){
                stickArticle("1");
            },
            icon:ICON + "stick.gif",
            enable:function(){return true;},
            visible:function(){return "1"!=getArticleAttribute("isTop") && true==getUserOperation("a_17");}
        }
        var item15 = {
            label:"解除置顶",
            callback:function(){
                stickArticle("0");
            },
            icon:ICON + "unstick.gif",
            enable:function(){return true;},
            visible:function(){return "1"==getArticleAttribute("isTop") && true==getUserOperation("a_18");}
        }
        var item16 = {
            label:"查看来源",
            callback:showDistributeSource,
            enable:function(){return true;},
            visible:function(){return "0"!=getArticleAttribute("state") && true==getUserOperation("a_12");}
        }
        var item17 = {
            label:"相关文章",
            callback:associateArticle,
            enable:function(){return true;},
            visible:function(){return "1"!=getArticleAttribute("state") && true==getUserOperation("a_12");}
        }
        var item18 = {
            label:"恢复默认顺序",
            callback:restoreDefaultSort,
            enable:function(){return true;},
            visible:function(){return true==canRestoreDefaultSort();}
        }
        var item19 = {
            label:"查看",
            callback:function(){
                editArticleInfo(false);
            },
            icon:ICON + "view.gif",
            enable:function(){return true},
            visible:function(){return true==getUserOperation("a_12");}
        }
        var item20 = {
            label:"编辑",
            callback:editArticleInfo,
            icon:ICON + "edit.gif",
            enable:function(){return true},
            visible:function(){return "4"!=getArticleAttribute("status") && "5"!=getArticleAttribute("status") && "-1"!=getArticleAttribute("status") && "1"!=getArticleAttribute("state") && true==getUserOperation("a_5");}
        }
        var item21 = {
            label:"评分统计...",
            callback:function(){
                scoreCount("article");
            },
            icon:ICON + "score.gif",
            enable:function(){return true},
            visible:function(){return true==getUserOperation("a_12");}
        }
        var item22 = {
            label:"评论管理...",
            callback:commentManage,
            icon:ICON + "comment.gif",
            enable:function(){return true},
            visible:function(){return true==getUserOperation("a_13");}
        }

        var menu1 = new Menu();
//        menu1.addItem(item1);
//        menu1.addItem(item2);
//        menu1.addSeparator();
        menu1.addItem(item19);
        menu1.addItem(item20);
        menu1.addItem(item4);
        menu1.addItem(item12);
        menu1.addItem(item13);
        menu1.addSeparator();
        menu1.addItem(item5);
        menu1.addItem(item6);
        menu1.addItem(item7);
        menu1.addItem(item14);
        menu1.addItem(item15);
        menu1.addSeparator();
        menu1.addItem(item10);
        menu1.addItem(item16);
        menu1.addItem(item11);
        menu1.addItem(item17);
        menu1.addItem(item21);
        menu1.addItem(item22);
        menu1.addSeparator();
        menu1.addItem(item18);
        menu1.addItem(item8);
        menu1.addItem(item9);

        gridObj.contextmenu = menu1;
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
     *	函数说明：grid初始化
     *	参数：	string:id   grid数据相关树节点id
     *	返回值：
     */
    function initGrid(id){
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridEvents();
            loadGridData(id,"1");//默认第一页
        });
    }
    /*
     *	函数说明：grid绑定事件
     *	参数：	
     *	返回值：
     */
    function loadGridEvents(){
        var gridObj = $("grid");

        gridObj.onclickrow = function(){
            onClickRow(event);
        }
        gridObj.ondblclickrow = function(){
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
    function loadGridData(treeID,page,sortName,direction){
        var cacheID = CACHE_TREE_NODE_GRID + treeID;
		var p = new HttpRequestParams();
		p.url = URL_ARTICLE_LIST;
		p.setContent("channelId", treeID);
		p.setContent("page", page);
		if(null!=sortName && null!=direction){
			p.setContent("field", sortName);
			p.setContent("orderType", direction);
		}

		var request = new HttpRequest(p);
		request.onresult = function(){
			var articleListNode = this.getNodeValue(XML_ARTICLE_LIST);
			var articleListNodeID = cacheID+"."+XML_ARTICLE_LIST;

			var pageListNode = this.getNodeValue(XML_PAGE_LIST);
			var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

			//给grid节点加上channelId属性表示栏目id
			articleListNode.setAttribute("channelId", treeID);

			//给当前排序列加上_direction属性
			if(null!=sortName && null!=direction){
				var column = articleListNode.selectSingleNode("//column[@name='" + sortName + "']");
				if(null!=column){
					column.setAttribute("_direction",direction);
				}
			}

			Cache.XmlIslands.add(articleListNodeID,articleListNode);
			Cache.XmlIslands.add(pageListNodeID,pageListNode);
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
    function loadGridDataFromCache(cacheID){
        //重新创建grid工具条
        createGridToolBar(cacheID);

        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ARTICLE_LIST);
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
                    var tempChannelId = tempXmlIsland.getAttribute("channelId");
                    var sortName = tempXmlIsland.getAttribute("sortName");
                    var direction = tempXmlIsland.getAttribute("direction");
                    if("search"!=tempChannelId){
                        //清除该栏目文章grid缓存
                        delCacheData(CACHE_TREE_NODE_GRID + tempChannelId);

                        loadGridData(tempChannelId,page,sortName,direction);
                        
                        //刷新工具条
                        onInactiveRow();
                    }else{
                        loadSearchGridData(cacheID,page,sortName,direction);
                    }
                }            
            });
        }
    }
    /*
     *	函数说明：显示文章状态信息
     *	参数：	number:rowIndex     grid数据行号
     *	返回值：
     */
    function showArticleStatus(rowIndex){
        if(null==rowIndex){
            var rowName = "-";
            var rowUserName = "-";
            var rowModifiedUserName = "-";
            var rowCreationDate = "-";
            var rowModifiedDate = "-";
        }else{
            var gridObj = $("grid");
            var rowNode = gridObj.getRowNode_Xml(rowIndex);
            var rowId = rowNode.getAttribute("id");
            var rowName = rowNode.getAttribute("name");
            var rowUserName = rowNode.getAttribute("creatorName");
            var rowModifiedUserName = rowNode.getAttribute("updatorName");
            var rowCreationDate = rowNode.getAttribute("createTime");
            var rowModifiedDate = rowNode.getAttribute("updateTime");
        }

        var block = Blocks.getBlock("statusContainer");
        if(null!=block){
            block.open();
            block.writeln("ID",rowId);
            block.writeln("名称",rowName);
            block.writeln("创建者",rowUserName);
            block.writeln("修改者",rowModifiedUserName);
            block.writeln("创建日期",rowCreationDate);
            block.writeln("修改日期",rowModifiedDate);
            block.close();
        }
    }
    /*
     *	函数说明：显示文章详细信息
     *	参数：	boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function editArticleInfo(editable){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowName = gridObj.getNamedNodeValue_Xml(rowIndex,"title");
        var rowID = rowNode.getAttribute("id");
        var articleType = rowNode.getAttribute("state");
        var workflowStatus = rowNode.getAttribute("status");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId){
            channelId = rowNode.getAttribute("channelId");
        }

        var returnValue = window.showModalDialog("article.htm",{title:"编辑文章",isNew:false,editable:editable,channelId:channelId,articleType:articleType,articleId:rowID,workflowStatus:workflowStatus},"dialogWidth:900px;dialogHeight:700px;status:yes");
        if(true==returnValue){

            //如果当前grid显示为此文章所在栏目，则刷新grid
            var gridObj = $("grid");
            if(true==gridObj.hasData_Xml()){
                var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                var tempChannelId = tempXmlIsland.getAttribute("channelId");
                if(tempChannelId==channelId){
                    loadGridData(tempChannelId,"1");//默认第一页

                    //刷新工具条
                    onInactiveRow();
                }
            }
        }
    }
 
    /*
     *	函数说明：文章相关页加载数据
     *	参数：	string:cacheID              缓存数据id
                boolean:editable            是否可编辑(默认true)
                string:channelID            栏目id
                string:articleType          文章类型(0表示普通文章/1分发/2转载/3导入)
                string:workflowStatus       流程状态
                boolean:isNew               是否新增
     *	返回值：
     */
    function initArticlePages(cacheID,editable,channelID,articleType,workflowStatus,isNew){
        //分发、转载、导入文章禁止编辑保存
        setArticlePagesEditable(articleType,workflowStatus,editable);

        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadArticleInfoFormData(cacheID);
        });

        var page2EditorObj = $("page2Editor");
        Public.initHTC(page2EditorObj,"isLoaded","onload",function(){
            loadArticleContentData(cacheID);
        });

        var page3FormObj = $("page3Form");
        Public.initHTC(page3FormObj,"isLoaded","oncomponentready",function(){
            loadAttachsUploadFormData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page2BtPrevObj = $("page2BtPrev");
        var page3BtPrevObj = $("page3BtPrev");
        var page1BtNextObj = $("page1BtNext");
        var page2BtNextObj = $("page2BtNext");
        var page3BtNextObj = $("page3BtNext");
        page1BtPrevObj.style.display = "none";
        page2BtPrevObj.style.display = "";
        page3BtPrevObj.style.display = "";
        page1BtNextObj.style.display = "";
        page2BtNextObj.style.display = "";
        page3BtNextObj.style.display = "none";

        //设置删除按钮操作
        var page3BtDel = $("page3BtDel");
        page3BtDel.onclick  = delPage3GridRow;
    }
    /*
     *	函数说明：设置文章相关页是否禁止编辑保存功能
     *	参数：	string:articleType          文章类型(0表示普通文章/1分发/2转载/3导入)
                boolean:editable            是否可编辑(默认true)
                string:workflowStatus       流程状态
     *	返回值：
     */
    function setArticlePagesEditable(articleType,workflowStatus,editable){
        var page1FormObj = $("page1Form");
        var page3FormObj = $("page3Form");
        var page1BtSaveObj = $("page1BtSave");
        var page2BtSaveObj = $("page2BtSave");
        var page3BtSaveObj = $("page3BtSave");

        var page3BtDel = $("page3BtDel");

        workflowStatus = parseInt(workflowStatus);
        if(("0"!=articleType && "2"!=articleType)|| PUBLISHED_ARTICLE_STATUS<=workflowStatus || -1==workflowStatus || false==editable){
            page1FormObj.editable = "false";
            page3FormObj.editable = "false";
            page1BtSaveObj.disabled = true;
            page2BtSaveObj.disabled = true;
            page3BtSaveObj.disabled = true;
            page3BtDel.disabled = true;
        }else{
            page1FormObj.editable = "true";
            page3FormObj.editable = "true";
            page1BtSaveObj.disabled = false;
            page2BtSaveObj.disabled = false;
            page3BtSaveObj.disabled = false;
            page3BtDel.disabled = false;
        }    
    }
    /*
     *	函数说明：文章信息xform加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadArticleInfoFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ARTICLE_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.load(xmlIsland.node,null,"node");
            loadPage1FormEvents("article",cacheID);
        }
    }
    /*
     *	函数说明：将当前文章正文内容更新到对应缓存中
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function updateArticleContentData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ARTICLE_CONTENT);
        if(null!=xmlIsland){
            var page2EditorObj = $("page2Editor");
            if(true==page2EditorObj.isLoaded){
                var html = page2EditorObj.getSourceHTML();
                Cache.XmlIslands.add(cacheID+"."+XML_ARTICLE_CONTENT,html);
            }
        }
    }
    /*
     *	函数说明：文章正文editor加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadArticleContentData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ARTICLE_CONTENT);
        if(null!=xmlIsland){
            var page2EditorObj = $("page2Editor");
            page2EditorObj.load(xmlIsland);
        }
    }
    /*
     *	函数说明：图片附件上传xform加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadAttachsUploadFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ATTACHS_UPLOAD);
        if(null!=xmlIsland){
            var page3FormObj = $("page3Form");
            page3FormObj.load(xmlIsland.node,null,"node");

            page3FormObj.ondatachange = function(){
                var name = event.result.name;
                var newValue = event.result.newValue;

                var targetName = null;
                var reload = false;

                switch(name){
                    case "switch1":
                        targetName = "switch2";

                        //设置本地附件可编辑
                        page3FormObj.setColumnEditable("file","true");
                        page3FormObj.setColumnEditable("serverAttach","false");

                        //清空服务器附件
                        page3FormObj.updateDataExternal("serverAttach","");

                        reload = true;
                        break;
                    case "switch2":
                        targetName = "switch1";

                        //设置服务器附件可编辑
                        page3FormObj.setColumnEditable("file","false");
                        page3FormObj.setColumnEditable("serverAttach","true");

                        //清空本地附件
                        page3FormObj.updateDataExternal("file","");

                        reload = true;
                        break;
                    default:
                        //2007-3-1 离开提醒
                        attachReminder(cacheID);
                        break;
                }

                if(null!=targetName){
                    page3FormObj.updateDataExternal(targetName,"0",false);
                }
                if(true==reload){
                    page3FormObj.reload();                
                }
            }
            if("false"!=page3FormObj.editable){
                page3FormObj.updateDataExternal("switch1","1");
            }
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
            treeObj.onTreeNodeMoved = function(eventObj){
                onTreeNodeMoved(eventObj);
            }
            treeObj.onTreeNodeRightClick = function(eventObj){
                onTreeNodeRightClick(eventObj);
            }
        }    
    }
    /*
     *	函数说明：新建站点
     *	参数：	
     *	返回值：
     */
    function addNewSite(){
        var treeID = new Date().valueOf();
        var treeName = "站点";
        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(){
            setTimeout(function(){
                loadSiteDetailData(treeID,true,true);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_ADD.replace(/\$label/i,treeName);
        inf.phases = null;
        inf.callback = callback;
        inf.SID = CACHE_SITE_DETAIL + treeID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：编辑组信息
     *	参数：  boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function editSiteInfo(editable){
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
                    loadSiteDetailData(treeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName); 
                inf.SID = CACHE_VIEW_SITE_DETAIL + treeID;           
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_SITE_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：站点详细信息加载数据
     *	参数：	string:treeID           树节点id
                boolean:editable        是否可编辑(默认true)
                boolean:isNew           是否新增
     *	返回值：
     */
    function loadSiteDetailData(treeID,editable,isNew){
        if(false==editable){
            var cacheID = CACHE_VIEW_SITE_DETAIL + treeID;
        }else{
            var cacheID = CACHE_SITE_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_SITE_DETAIL;
            if(true==isNew){            
                p.setContent("isNew", "1");
                p.setContent("isSite", "1");
            }else{
                p.setContent("siteId", treeID);            
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var siteInfoNode = this.getNodeValue(XML_SITE_INFO);
                var siteInfoNodeID = cacheID+"."+XML_SITE_INFO;

                Cache.XmlIslands.add(siteInfoNodeID,siteInfoNode);

                Cache.Variables.add(cacheID,[siteInfoNodeID]);

                initSitePages(cacheID,editable,isNew);
            }
            request.send();
        }else{
            initSitePages(cacheID,editable,isNew);
        }
    }
    /*
     *	函数说明：站点相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:editable        是否可编辑(默认true)
                boolean:isNew           是否新增
     *	返回值：
     */
    function initSitePages(cacheID,editable,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadSiteInfoFormData(cacheID,editable);
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
            saveSite(cacheID,isNew);
        }
    }
    /*
     *	函数说明：站点信息xform加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function loadSiteInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_SITE_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");
            loadPage1FormEvents("site",cacheID);
        }
    }
    /*
     *	函数说明：page1Form绑定事件
     *	参数：	
     *	返回值：
     */
    function loadPage1FormEvents(formSource,cacheID){
        var page1FormObj = $("page1Form");
        page1FormObj.ondatachange = function(){
            //离开提醒
            attachReminder(cacheID);

            var name = event.result.name;
            var value = event.result.newValue;
            switch(formSource){                
                case "site"://站点
                    switch(name){
                        case "publishByTime":
                          page1FormObj.setColumnEditable("startHour","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("startMinute","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("perHour","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("perMinute","1"==value?"true":"false");
                          break;
                        case "publishByFTP":
                          page1FormObj.setColumnEditable("server","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("port","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("platform","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("user","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("password","1"==value?"true":"false");
                          break;
                    }
                    break;
                case "channel"://栏目
                    switch(name){                        
                        case "hot":
                          page1FormObj.setColumnEditable("hotRate","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("hotIcon","1"==value?"true":"false");
                          break;
                        case "new":
                          page1FormObj.setColumnEditable("newAmount","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("newUnit","1"==value?"true":"false");
                          page1FormObj.setColumnEditable("newIcon","1"==value?"true":"false");
                          break;
                    }
                    break;
                case "article":
                    break;
            }
        }
    }
    /*
     *	函数说明：保存站点
     *	参数：	string:cacheID      缓存数据ID
                boolean:isNew       是否新增
     *	返回值：
     */
    function saveSite(cacheID,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }

        var p = new HttpRequestParams();
		p.setContent("isSite", "1");
        if(isNew==true){
            p.url = URL_SAVE_SITE;
        }else{
            p.url = URL_UPDATE_SITE;
        }
        //是否提交
        var flag = false;
        
        var channelCache = Cache.Variables.get(cacheID);
        if(null!=channelCache){       

            //站点基本信息
            var siteInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_SITE_INFO);
            if(null!=siteInfoNode){
                var siteInfoDataNode = siteInfoNode.selectSingleNode(".//data");
                if(null!=siteInfoDataNode){
                    flag = true;                    

                    var prefix = siteInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(siteInfoDataNode,prefix);
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
                    var id = cacheID.trim(CACHE_SITE_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：删除page3里grid的行
     *	参数：	
     *	返回值：
     */
    function delPage3GridRow(){
        var page3GridObj = $("page3Grid");
        var selectedIndex = page3GridObj.getSelectedRowsIndexArray_Xml();
        for(var i=0,iLen=selectedIndex.length;i<iLen;i++){
            var curRowIndex = selectedIndex[i];
            var refresh = i<iLen-1?false:true;
            page3GridObj.delRow_Xml(curRowIndex,null,refresh);
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
        var gridTitleObj = $("gridTitle");

        Focus.register(treeTitleObj.firstChild);
        Focus.register(statusTitleObj.firstChild);
        Focus.register(gridTitleObj);
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
        var gridTitleObj = $("gridTitle");
        
        Event.attachEvent(treeBtRefreshObj,"click",onClickTreeBtRefresh);
        Event.attachEvent(treeTitleBtObj,"click",onClickTreeTitleBt);
        Event.attachEvent(statusTitleBtObj,"click",onClickStatusTitleBt);
        Event.attachEvent(paletteBtObj,"click",onClickPaletteBt);

        Event.attachEvent(treeTitleObj,"click",onClickTreeTitle);
        Event.attachEvent(statusTitleObj,"click",onClickStatusTitle);
        Event.attachEvent(gridTitleObj,"click",onClickGridTitle);
    }
    /*
     *	函数说明：点击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeActived(eventObj){
        var treeTitleObj = $("treeTitle");
        Focus.focus(treeTitleObj.firstChild.id);

        showTreeNodeStatus({id:"ID",name:"名称",userName:"创建者",creationDate:"创建时间",modifiedUserName:"修改者",modifiedDate:"修改时间"});

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
        var isSite = getTreeAttribute("isSite");
        getTreeOperation(treeNode,function(_operation){
            if("_rootId"!=id){
                var canShowArticleList = checkOperation("14",_operation);
                var canView = checkOperation("12",_operation);
                var canEdit = checkOperation("5",_operation);

                if("0"==isSite && true == canShowArticleList){
                    showArticleList();
                }else{
                    if(true==canEdit){
                        editTreeNode();                    
                    }else if(true==canView){
                        editTreeNode(false);
                    }
                }
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

        showTreeNodeStatus({id:"ID",name:"名称",userName:"创建者",creationDate:"创建时间",modifiedUserName:"修改者",modifiedDate:"修改时间"});

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
     *	函数说明：拖动树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeMoved(eventObj){
        var movedTreeNode = eventObj.movedTreeNode;	
        sortChannelTo(eventObj)
    }
    /*
     *	函数说明：单击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onClickRow(eventObj){    
        Focus.focus("gridTitle");

        var rowIndex = eventObj.result.rowIndex_Xml;
        showArticleStatus(rowIndex);

        //防止因为载入工具条数据而导致不响应双击事件
        clearTimeout(window._toolbarTimeout);
        window._toolbarTimeout = setTimeout(function(){
            loadArticleToolBarData(rowIndex);
        },0);
    }
    /*
     *	函数说明：双击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onDblClickRow(eventObj){
        var rowIndex = eventObj.result.rowIndex_Xml;
        getGridOperation(rowIndex,function(_operation){
            //检测编辑权限
            var canEdit = checkOperation("a_5",_operation);
            var canView = checkOperation("a_12",_operation);

            var gridObj = $("grid");
            var rowNode = gridObj.getRowNode_Xml(rowIndex);
            var status = rowNode.getAttribute("status");
            var state = rowNode.getAttribute("state");

            if(true==canEdit && "4"!=status && "-1"!=status && "5"!=status && "1"!=state){
                editArticleInfo();            
            }else if(true==canView){
                editArticleInfo(false);
            }
        });
    }
    /*
     *	函数说明：右击grid行
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onRightClickRow(eventObj){
        var rowIndex = eventObj.result.rowIndex_Xml;
        var x = event.clientX;
        var y = event.clientY;

        getGridOperation(rowIndex,function(_operation){
            addWorkFlowMenu(x,y);
            loadToolBar(_operation);        
        });
    }
    /*
     *	函数说明：单击grid空白处
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onInactiveRow(eventObj){
        var treeTitleObj = $("treeTitle");
        Focus.focus(treeTitleObj.firstChild.id);

        showTreeNodeStatus({id:"ID",name:"名称",userName:"创建者",creationDate:"创建时间",modifiedUserName:"修改者",modifiedDate:"修改时间"});

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        //防止因为载入工具条数据而导致不响应双击事件
        clearTimeout(window._toolbarTimeout);
        window._toolbarTimeout = setTimeout(function(){
            loadToolBarData(treeNode);
        },0);
    }
    /*
     *	函数说明：单击grid表头排序
     *	参数：	event:eventObj     事件对象
     *	返回值：
     */
    function onSortRow(eventObj){
        var name = eventObj.result.name;
        var direction = eventObj.result.direction;

        eventObj.returnValue = false;

        var gridObj = $("grid");
        var xmlIsland = new XmlNode(gridObj.getXmlDocument());
        xmlIsland.setAttribute("sortName",name);
        xmlIsland.setAttribute("direction",direction);

        var toolbarObj = $("gridToolBar");
        var curPage = toolbarObj.getCurrentPage();
        toolbarObj.gotoPage(curPage);
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
     *	函数说明：显示文章列表
     *	参数：	                
     *	返回值：
     */
    function showArticleList(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            initGrid(id);
        }
    }
    /*
     *	函数说明：新建文章
     *	参数：	
     *	返回值：
     */
    function addNewArticle(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var channelId = treeNode.getId();
            var articleType = 0; // 普通文章

            var articleName = "文章";
            var articleId = new Date().valueOf();

            var returnValue = window.showModalDialog("article.htm",{title:"新建文章",isNew:true,editable:true,channelId:channelId,articleType:articleType,articleId:articleId,workflowStatus:"0"},"dialogWidth:900px;dialogHeight:700px;status:yes");
            if(true==returnValue){

                //如果当前grid显示为此文章所在栏目，则刷新grid
                var gridObj = $("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempChannelId = tempXmlIsland.getAttribute("channelId");
                    if(tempChannelId==channelId){
                        loadGridData(tempChannelId,"1");//默认第一页

                        //刷新工具条
                        onInactiveRow();
                    }
                }          
            }
        }
        
    }
    /*
     *	函数说明：删除栏目
     *	参数：	
     *	返回值：
     */
    function delChannel(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            
            var p = new HttpRequestParams();
            p.url = URL_DEL_CHANNEL;
            p.setContent("channelId",treeID);

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
     *	函数说明：删除站点
     *	参数：	
     *	返回值：
     */
    function delSite(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            
            var p = new HttpRequestParams();
            p.url = URL_DEL_SITE;
            p.setContent("siteId",treeID);

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
     *	函数说明：移动文章
     *	参数：	
     *	返回值：
     */
    function moveArticleTo(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var articleType = rowNode.getAttribute("state");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var oldChannelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==oldChannelId){
            oldChannelId = rowNode.getAttribute("channelId");
        }
        var action = "moveArticle";
        var xmlIsland = null;

        var channelID = window.showModalDialog("channeltree.htm",{id:oldChannelId,xmlIsland:xmlIsland,action:action},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=channelID){
            var p = new HttpRequestParams();
            p.url = URL_MOVE_ARTICLE;
            p.setContent("channelId",channelID.id);
            p.setContent("articleId",rowID);
            p.setContent("oldChannelId", oldChannelId);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var toolbarObj = $("gridToolBar");
                var curPage = toolbarObj.getCurrentPage();
                toolbarObj.gotoPage(curPage);

                //清除状态信息
                var block = Blocks.getBlock("statusContainer");
                if(null!=block){
                    block.clear();
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：移动栏目
     *	参数：	
     *	返回值：
     */
    function moveChannelTo(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var action = "moveChannel";
            var xmlIsland = null;

            var groupID = window.showModalDialog("channeltree.htm",{id:id,xmlIsland:xmlIsland,action:action},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=groupID){
                var p = new HttpRequestParams();
                p.url = URL_MOVE_CHANNEL;
                p.setContent("toChannelId", groupID.id);
                p.setContent("channelId", id);
                p.setContent("type", groupID.type);

                var request = new HttpRequest(p);
                request.onsuccess = function(){
                    //移动树节点
                    var curNode = treeObj.getTreeNodeById(id);
                    var xmlNode = new XmlNode(curNode.node);
                    var parentNode = treeObj.getTreeNodeById(groupID.id);
                    if(null!=parentNode){
                        var status = parentNode.getAttribute("disabled");
                        parentNode.node.appendChild(curNode.node);
                        parentNode.node.setAttribute("_open","true");

                        //目标父节点为站点或已停用栏目则更改移动节点状态
                        var flag = false;
                        var isSite = parentNode.getAttribute("isSite");
						 if("1"==status){//已停用
                                flag = true;
                            }
                        //更改移动节点状态
                        if(true==flag){
                            //设置当前节点状态
                            refreshTreeNodeState(xmlNode,status);

                            //下溯
                            var childs = xmlNode.selectNodes(".//treeNode");
                            for(var i=0,iLen=childs.length;i<iLen;i++){
                                refreshTreeNodeState(childs[i],status);
                            }
                        }

                        clearOperation(xmlNode);

                        treeObj.reload();
                    }
                }
                request.send();
            }
        }
    }
        /*
     *	函数说明：复制站点
     *	参数：	
     *	返回值：
     */
    function copySite(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var p = new HttpRequestParams();
            p.url = URL_COPY_SITE;   
            p.setContent("siteId",id);

            var request = new HttpRequest(p);
            request.onresult = function(){
                //站点树与栏目树分开两个名称节点，需要在客户端重新拼成完整站点树
                var siteNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
                var channelNode = this.getNodeValue(XML_CHANNEL_TREE);
                if(null!=channelNode){
                    var channelNodes = channelNode.selectNodes("treeNode");
                    for(var i=0,iLen=channelNodes.length;i<iLen;i++){
                        siteNode.appendChild(channelNodes[i]);
                    }
                }
                appendTreeNode("_rootId",siteNode);
            }   
            request.send();
        }
    }
    /*
     *	函数说明：复制栏目到
     *	参数：	
     *	返回值：
     */
    function copyChannelTo(){
        var treeObj	= $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var action = "copyChannel";
            var xmlIsland = null;

            var groupID = window.showModalDialog("channeltree.htm",{id:id,xmlIsland:xmlIsland,action:action},"dialogWidth:300px;dialogHeight:400px;");
            if(null!=groupID){
                var	p =	new	HttpRequestParams();
                p.url =	URL_COPY_CHANNEL;
                p.setContent("toChannelId",groupID.id);
                p.setContent("channelId",id);
                p.setContent("type",groupID.type);
                p.setContent("action","copyChannelTo");

                var	request	= new HttpRequest(p);
                request.onresult = function(){
                    var treeNode = this.getNodeValue(XML_CHANNEL_TREE).selectSingleNode("treeNode");
                    appendTreeNode(groupID.id,treeNode);
                }
                request.send();
            }  
        }
    }
    /*
     *	函数说明：复制栏目
     *	参数：	
     *	返回值：
     */
    function copyChannel(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var xmlNode = new XmlNode(treeNode.node);			  
            var parentNode = xmlNode.getParent();
            var parentNodeId = parentNode.getAttribute("id");
            var type = null;		  
            if(parentNode.getAttribute("type")!=null){
                type = "1";
            }else{
                type = "2";
            }
            var p = new HttpRequestParams();
            p.url = URL_COPY_CHANNEL;   
            p.setContent("toChannelId",parentNodeId);
            p.setContent("channelId",id);
            p.setContent("type",type);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var treeNode = this.getNodeValue(XML_CHANNEL_TREE).selectSingleNode("treeNode");
                appendTreeNode(parentNodeId,treeNode);
            }
            request.send();
        }
    }

    /*
     *	函数说明：同一父节点下移动栏目
     *	参数：	
     *	返回值：
     */
    function sortChannelTo(eventObj){
        var treeObj = $("tree");
        var movedTreeNode = eventObj.movedTreeNode;
        var toTreeNode = eventObj.toTreeNode;
        var moveState = eventObj.moveState;
        var p = new HttpRequestParams();
        p.url = URL_SORT_CHANNEL;
        p.setContent("toChannelId", toTreeNode.getId());
        p.setContent("channelId", movedTreeNode.getId());
        p.setContent("direction", moveState); // -1目标上方,1目标下方

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            //移动树节点
            treeObj.moveTreeNode(movedTreeNode, toTreeNode, moveState);
        }
        request.send();
    }
    /*
     *	函数说明：停用站点栏目
     *	参数：	
     *	返回值：
     */
    function stopSite(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var isSite = treeNode.getAttribute("isSite");

            var p = new HttpRequestParams();
            p.url = URL_STOP_SITE;
            p.setContent("id",id);
            p.setContent("isSite",isSite);

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
     *	函数说明：启用站点栏目
     *	参数：	
     *	返回值：
     */
    function startSite(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var isSite = treeNode.getAttribute("isSite");

            var p = new HttpRequestParams();
            p.url = URL_START_SITE;
            p.setContent("id",id);
            p.setContent("isSite",isSite);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                var xmlNode = new XmlNode(treeNode.node);
                //设置停用状态
                while("1"!=isSite){
                    refreshTreeNodeState(xmlNode,"0");
                    xmlNode = xmlNode.getParent();
                    isSite = xmlNode.getAttribute("isSite");
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
     *	函数说明：启用全部
     *	参数：	
     *	返回值：
     */
    function startAll(){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            var isSite = treeNode.getAttribute("isSite");

            var p = new HttpRequestParams();
            p.url = URL_START_ALL;
            p.setContent("id",id);
            p.setContent("isSite",isSite);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
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
     *	函数说明：锁定文章
     *	参数：	
     *	返回值：
     */
    function lockingArticle(){

        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");

            var p = new HttpRequestParams();
            p.url = URL_STOP_ARTICLE;
            p.setContent("articleId",curRowID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                gridObj.modifyNamedNode_Xml(curRowIndex,"status","0");
            }
            request.send();
        }
    }
    /*
     *	函数说明：启用文章
     *	参数：	
     *	返回值：
     */
    function unLockingArticle(){

        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");

            var p = new HttpRequestParams();
            p.url = URL_START_ARTICLE;
            p.setContent("articleId",curRowID);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                gridObj.modifyNamedNode_Xml(curRowIndex,"status","1");
            }
            request.send();
        }
    }
    /*
     *	函数说明：删除文章
     *	参数：	
     *	返回值：
     */
    function delArticle(){
        if(true!=confirm("您确定要删除吗？")){
            return;
        }
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var state = rowNode.getAttribute("state");
        var channelId = rowNode.getAttribute("channelId");

        var p = new HttpRequestParams();
        p.url = URL_DEL_ARTICLE;
        p.setContent("articleId",rowID);
        p.setContent("articleType",state);
        p.setContent("channelId",channelId);

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            var toolbarObj = $("gridToolBar");
            var curPage = toolbarObj.getCurrentPage();
            toolbarObj.gotoPage(curPage);
        }
        request.send();
    }
    /*
     *	函数说明：文章下移一行
     *	参数：	
     *	返回值：
     */
    function moveArticleDown(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var articleId = gridObj.getNamedNodeValue_Xml(rowIndex, "id");
        var channelId = gridObj.getNamedNodeValue_Xml(rowIndex, "channelId");

        var visibleIndex = gridObj.getVisibleIndexFromRowIndex(rowIndex);
        var visibleNextIndex = visibleIndex + 1;
        var len = gridObj.getVisibleRowsLength();
        
        var toolbarObj = $("gridToolBar");
        var totalpages = toolbarObj.getTotalPages();
        var curPage = toolbarObj.getCurrentPage();

        var p = new HttpRequestParams();
        if(len-1>visibleIndex){
            var nextRowIndex = gridObj.getRowIndexFromVisibleIndex(visibleNextIndex);
            var newarticleId = gridObj.getNamedNodeValue_Xml(nextRowIndex, "id");

            p.url = URL_UP_DOWN_ARTICLE;
            p.setContent("articleId",articleId);
            p.setContent("channelId", channelId);
            p.setContent("toArticleId", newarticleId);
        }else{//当前页最后一行下移时
            var nextRowIndex = null;

            p.url = URL_UP_DOWN_ARTICLE_CROSS_PAGE;
            p.setContent("articleId",articleId);
            p.setContent("channelId", channelId);
            p.setContent("page", curPage);
            p.setContent("direction", "1");
        }

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            if(null!=nextRowIndex){
                gridObj.moveRow_Xml([rowIndex],nextRowIndex);
                loadArticleToolBarData(nextRowIndex);
            }else{//当前页最后一行下移时
                toolbarObj.gotoPage(curPage);
            }
        }
        request.send();
    }
    /*
     *	函数说明：文章上移一行
     *	参数：	
     *	返回值：
     */
    function moveArticleUp(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var articleId = gridObj.getNamedNodeValue_Xml(rowIndex, "id");
        var channelId = gridObj.getNamedNodeValue_Xml(rowIndex, "channelId");

        var visibleIndex = gridObj.getVisibleIndexFromRowIndex(rowIndex);
        var visibleNextIndex = visibleIndex - 1;

        var toolbarObj = $("gridToolBar");
        var curPage = toolbarObj.getCurrentPage();

        var p = new HttpRequestParams();
        if(0<visibleIndex){
            var nextRowIndex = gridObj.getRowIndexFromVisibleIndex(visibleNextIndex);
            var newarticleId = gridObj.getNamedNodeValue_Xml(nextRowIndex, "id");

            p.url = URL_UP_DOWN_ARTICLE;
            p.setContent("articleId", articleId);
            p.setContent("channelId", channelId);
            p.setContent("toArticleId", newarticleId);
        }else{//当前页第一行上移时
            var nextRowIndex = null;

            p.url = URL_UP_DOWN_ARTICLE_CROSS_PAGE;
            p.setContent("articleId", articleId);
            p.setContent("channelId", channelId);
            p.setContent("page", curPage);
            p.setContent("direction", "-1");
        }

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            if(null!=nextRowIndex){
                gridObj.moveRow_Xml([rowIndex],nextRowIndex);
                loadArticleToolBarData(nextRowIndex);
            }else{//当前页第一行上移时
                toolbarObj.gotoPage(curPage);
            }
        }
        request.send();
    }
    /*
     *	函数说明：文章是否可以下移一行
     *	参数：	
     *	返回值：
     */
    function canMoveArticleDown(){
        var flag = false;
        var gridObj = $("grid");
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"!=channelId){
            var sortType = gridObj.getCurrentSortType();
            if("default"==sortType){
                var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
                var visibleIndex = gridObj.getVisibleIndexFromRowIndex(rowIndex);
                var len = gridObj.getVisibleRowsLength();
                if(len-1>visibleIndex){
                    flag = true;
                }else{
                    var toolbarObj = $("gridToolBar");
                    var totalpages = toolbarObj.getTotalPages();
                    var curPage = toolbarObj.getCurrentPage();
                    if(curPage<totalpages){
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
    /*
     *	函数说明：文章是否可以上移一行
     *	参数：	
     *	返回值：
     */
    function canMoveArticleUp(){
        var flag = false;
        var gridObj = $("grid");
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"!=channelId){
            var sortType = gridObj.getCurrentSortType();
            if("default"==sortType){
                var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
                var visibleIndex = gridObj.getVisibleIndexFromRowIndex(rowIndex);
                if(0<visibleIndex){
                    flag = true;
                }else{
                    var toolbarObj = $("gridToolBar");
                    var curPage = toolbarObj.getCurrentPage();
                    if(curPage>1){
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
    /*
     *	函数说明：编辑树节点
     *	参数：	boolean:editable            是否可编辑(默认true)
     *	返回值：
     */
    function editTreeNode(editable){
        var isSite = getTreeAttribute("isSite");
        if("1"==isSite){
            editSiteInfo(editable);
        }else{
            editChannelInfo(editable);
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
        var isSite = getTreeAttribute("isSite");
        if("1"==isSite){
            delSite();
        }else{
            delChannel();
        }
    
    }
    /*
     *	函数说明：新建栏目
     *	参数：	
     *	返回值：
     */
    function addNewChannel(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){

            var kind = null;
            var isSite = getTreeAttribute("isSite");
            if("1"==isSite){
                kind = "site";
            }else{
                kind = "channel";
            }

            var parentID = treeNode.getId();
            var siteId = treeNode.getAttribute("siteId");
            var channelName = "栏目";
            var channelID = new Date().valueOf();

            var callback = {};
            callback.onTabClose = function(eventObj){
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function(){
                setTimeout(function(){
                    loadChannelDetailData(channelID,true,parentID,kind,true,siteId);
                },TIMEOUT_TAB_CHANGE);
            };
            var inf = {};
            inf.defaultPage = "page1";
            inf.label = OPERATION_ADD.replace(/\$label/i,channelName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_CHANNEL_DETAIL + channelID;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：编辑栏目信息
     *	参数：  boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function editChannelInfo(editable){
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
                    loadChannelDetailData(treeID,editable);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            if(false==editable){
                inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
                inf.SID = CACHE_VIEW_CHANNEL_DETAIL + treeID;
            }else{
                inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
                inf.SID = CACHE_CHANNEL_DETAIL + treeID;
            }
            inf.defaultPage = "page1";
            inf.phases = null;
            inf.callback = callback;
            var tab = ws.open(inf);
        }
    }
    /*
     *	函数说明：栏目详细信息加载数据
     *	参数：	string:treeID           树节点id
                boolean:editable        是否可编辑(默认true)
                string:parentID         父节点id
                string:siteId           站点id
                boolean:isNew           是否新增
     *	返回值：
     */
    function loadChannelDetailData(treeID,editable,parentID,kind,isNew,siteId){
        if(false==editable){
            var cacheID = CACHE_VIEW_CHANNEL_DETAIL + treeID;
        }else{
            var cacheID = CACHE_CHANNEL_DETAIL + treeID;
        }
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_CHANNEL_DETAIL;
            if(true==isNew){
                p.setContent("isNew", "1");
                p.setContent("isSite", "0");
                p.setContent("siteId", siteId);
                p.setContent("parentId", parentID);
                p.setContent("kind", kind);
            }else{
                p.setContent("channelId", treeID);            
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var channelInfoNode = this.getNodeValue(XML_CHANNEL_INFO);

                var channelInfoNodeID = cacheID+"."+XML_CHANNEL_INFO;

                Cache.XmlIslands.add(channelInfoNodeID,channelInfoNode);

                Cache.Variables.add(cacheID,[channelInfoNodeID]);

                initChannelPages(cacheID,editable,parentID,kind,isNew);
            }
            request.send();
        }else{
            initChannelPages(cacheID,editable,parentID,kind,isNew);
        }
    }
    /*
     *	函数说明：栏目相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:editable        是否可编辑(默认true)
                boolean:isNew           是否新增
     *	返回值：
     */
    function initChannelPages(cacheID,editable,parentID,kind,isNew){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadChannelInfoFormData(cacheID,editable);
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
            saveChannel(cacheID,parentID,kind,isNew);
        }
    }
    /*
     *	函数说明：栏目信息xform加载数据
     *	参数：	string:cacheID          缓存数据id
                boolean:editable        是否可编辑(默认true)
     *	返回值：
     */
    function loadChannelInfoFormData(cacheID,editable){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_CHANNEL_INFO);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.editable = editable==false?"false":"true";
            page1FormObj.load(xmlIsland.node,null,"node");
            loadPage1FormEvents("channel",cacheID);
        }
    }
    /*
     *	函数说明：保存栏目
     *	参数：	string:cacheID      缓存数据ID
                boolean:isNew       是否新增
     *	返回值：
     */
    function saveChannel(cacheID,parentID,kind,isNew){
        //校验page1Form数据有效性
        var page1FormObj = $("page1Form");
        if(false==page1FormObj.checkForm()){
            switchToPhase(ws,"page1");
            return;
        }
        var p = new HttpRequestParams();
        p.setContent("isSite", "0");
        if(isNew==true){
            p.url = URL_SAVE_CHANNEL;
        }else {
            p.url = URL_UPDATE_CHANNEL;
        }
        
        //是否提交
        var flag = false;
        
        var channelCache = Cache.Variables.get(cacheID);
        if(null!=channelCache){       

            //栏目基本信息
            var channelInfoNode = Cache.XmlIslands.get(cacheID+"."+XML_CHANNEL_INFO);
            if(null!=channelInfoNode){
                var channelInfoDataNode = channelInfoNode.selectSingleNode(".//data");
                if(null!=channelInfoDataNode){
                    flag = true;

                    var prefix = channelInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                    p.setXFormContent(channelInfoDataNode,prefix);

                    p.setContent("selectTreeId",parentID);
                    p.setContent("kind",kind);
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

                    var treeNode = this.getNodeValue(XML_CHANNEL_TREE).selectSingleNode("treeNode");
                    appendTreeNode(parentID,treeNode);

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
                    var id = cacheID.trim(CACHE_CHANNEL_DETAIL);
                    var name = page1FormObj.getData("name");
                    modifyTreeNode(id,"name",name,true);
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：获取图标
     *	参数：	string:name     xform列名
     *	返回值：
     */
    function getIcon(name){
        var page1FormObj = $("page1Form");
        page1FormObj.updateDataExternal(name,ICON + "icon_admin.gif");
    }

    /*
     *	函数说明：选择文章种类
     *	参数： 
     *	返回值：
     */
    function chooseArticleType() {
        var xmlIsland = null;
        var returnValue = window.showModalDialog("atgrouptree.htm",{xmlIsland:xmlIsland, type:1},"dialogWidth:300px;dialogHeight:400px;");
        if(returnValue != null) {
            page1Form.updateDataExternal("articleTypeName", returnValue.name);
            page1Form.updateDataExternal("articleType", returnValue.id);
        }
    }
    /*
     *	函数说明：选择文章流程
     *	参数： 
     *	返回值：
     */
    function chooseWorkFlow() {
        var xmlIsland = null;
        var returnValue = window.showModalDialog("atgrouptree.htm",{xmlIsland:xmlIsland, type:2},"dialogWidth:300px;dialogHeight:400px;");
        if(returnValue != null) {
            page1Form.updateDataExternal("workflowName", returnValue.name);
            page1Form.updateDataExternal("workflowId", returnValue.id);
        }
    }

    /*
     *	函数说明：获取文章属性
     *	参数：	string:name         文章属性名
     *	返回值：string:value        文章属性值
     */
    function getArticleAttribute(name){
        var value = null;
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=rowIndex){
            var rowNode = gridObj.getRowNode_Xml(rowIndex);
            value = rowNode.getAttribute(name);
        }
        return value;
    }
    /*
     *	函数说明：复制文章
     *	参数：	
     *	返回值：
     */
    function copyArticle(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId){
            channelId = rowNode.getAttribute("channelId");
        }

        var p = new HttpRequestParams();
        p.url = URL_COPY_ARTICLE;   
        p.setContent("articleId",rowID);
        p.setContent("channelId",channelId);
		p.setContent("copyTo",0);
        var request = new HttpRequest(p);
        request.onsuccess = function(){
            loadGridData(channelId,"1");//默认第一页

            //刷新工具条
            onInactiveRow();
        }
        request.send();

    }
    /*
     *	函数说明：复制文章到
     *	参数：	
     *	返回值：
     */
    function copyArticleTo(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId){
            channelId = rowNode.getAttribute("channelId");
        }

        var action = "copyArticle";
        var xmlIsland = null;

        var groupID = window.showModalDialog("channeltree.htm",{id:channelId,xmlIsland:xmlIsland,action:action},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=groupID){
            var p = new HttpRequestParams();
            p.url = URL_COPY_ARTICLE;   
            p.setContent("articleId",rowID);
            p.setContent("channelId",groupID.id);
			p.setContent("copyTo",1);
            var request = new HttpRequest(p);
            request.onsuccess = function(){
            }
            request.send();
        }  
    }
    /*
     *	函数说明：转载文章
     *	参数：	
     *	返回值：
     */
    function reshipArticle(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var articleType = rowNode.getAttribute("state");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var oldChannelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==oldChannelId){
            oldChannelId = rowNode.getAttribute("channelId");
        }

        var action = "reshipArticle";
        var xmlIsland = null;

        var channelID = window.showModalDialog("channeltree.htm",{id:oldChannelId,xmlIsland:xmlIsland,action:action},"dialogWidth:300px;dialogHeight:400px;");
        if(null!=channelID){
            var p = new HttpRequestParams();
            p.url = URL_RESHIP_ARTICLE;
            p.setContent("channelId",channelID.id);
            p.setContent("articleId",rowID);
            p.setContent("oldChannelId", oldChannelId);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
            }
            request.send();
        }
    }
    /*
     *	函数说明：置顶文章
     *	参数：	string:isTop        是否置顶(1是/0否)
     *	返回值：
     */
    function stickArticle(isTop){

        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var curRowID = curRowNode.getAttribute("id");

            //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
            var channelId = gridObj.getXmlDocument().getAttribute("channelId");
            if("search"==channelId){
                channelId = curRowNode.getAttribute("channelId");
            }
            var isTop = curRowNode.getAttribute("isTop");

            var p = new HttpRequestParams();
            p.url = URL_STICK_ARTICLE;
            p.setContent("articleId",curRowID);
            p.setContent("isTop",isTop);
            p.setContent("channelId",channelId);

            var request = new HttpRequest(p);
            request.onsuccess = function(){

                gridObj.modifyNamedNode_Xml(curRowIndex,"isTop",isTop);

                var toolbarObj = $("gridToolBar");
                var curPage = toolbarObj.getCurrentPage();
                toolbarObj.gotoPage(curPage);

                //刷新工具条
                onInactiveRow();

            }
            request.send();
        }
    }
    /*
     *	函数说明：栏目相关分发设置
     *	参数：	
     *	返回值：
     */
    function distributeSetting(){
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
                    loadChannelDistributeData(treeID);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page4";
            inf.label = OPERATION_DISTRIBUTE.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_CHANNEL_DISTRIBUTE + treeID;
            var tab = ws.open(inf);
        }    
    }
    /*
     *	函数说明：栏目分发设置加载数据
     *	参数：	string:treeID     树节点id
     *	返回值：
     */
    function loadChannelDistributeData(treeID){
        var cacheID = CACHE_CHANNEL_DISTRIBUTE + treeID;
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail){
            var p = new HttpRequestParams();
            p.url = URL_CHANNEL_DISTRIBUTE;
            p.setContent("channelId", treeID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var distributeTreeNode = this.getNodeValue(XML_MAIN_TREE);
                var distributeToTreeNode = this.getNodeValue(XML_DISTRIBUTE_TO_EXIST_TREE);
                var distributeFromTreeNode = this.getNodeValue(XML_DISTRIBUTE_FROM_EXIST_TREE);

                var distributeTreeNodeID = cacheID+"."+XML_MAIN_TREE;
                var distributeToTreeNodeID = cacheID+"."+XML_DISTRIBUTE_TO_EXIST_TREE;
                var distributeFromTreeNodeID = cacheID+"."+XML_DISTRIBUTE_FROM_EXIST_TREE;

                Cache.XmlIslands.add(distributeTreeNodeID,distributeTreeNode);
                Cache.XmlIslands.add(distributeToTreeNodeID,distributeToTreeNode);
                Cache.XmlIslands.add(distributeFromTreeNodeID,distributeFromTreeNode);

                Cache.Variables.add(cacheID,[distributeTreeNodeID,distributeToTreeNodeID,distributeFromTreeNodeID]);

                initChannelDistributePages(cacheID,treeID);
            }
            request.send();
        }else{
            initChannelDistributePages(cacheID,treeID);
        }    
    }
    /*
     *	函数说明：栏目分发设置相关页加载数据
     *	参数：	string:cacheID      缓存数据id
                string:channelId    栏目id
     *	返回值：
     */
    function initChannelDistributePages(cacheID,channelId){
        var page4TreeObj = $("page4Tree");
        Public.initHTC(page4TreeObj,"isLoaded","oncomponentready",function(){
            loadChannelDistributeTreeData(cacheID);
        });

        var page4Tree2Obj = $("page4Tree2");
        Public.initHTC(page4Tree2Obj,"isLoaded","oncomponentready",function(){
            loadChannelDistributeToTreeData(cacheID);
        });

        var page4Tree3Obj = $("page4Tree3");
        Public.initHTC(page4Tree3Obj,"isLoaded","oncomponentready",function(){
            loadChannelDistributeFromTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page4BtPrevObj = $("page4BtPrev");
        var page4BtNextObj = $("page4BtNext");
        page4BtPrevObj.style.display = "none";
        page4BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page4BtSaveObj = $("page4BtSave");
        page4BtSaveObj.onclick = function(){
            saveChannelDistribute(cacheID,channelId);
        }

        //设置搜索
        var page4BtSearchObj = $("page4BtSearch");
        var page4KeywordObj = $("page4Keyword");
        attachSearchTree(page4TreeObj,page4BtSearchObj,page4KeywordObj);

        //设置添加按钮操作
        var page4BtAddObj = $("page4BtAdd");
        page4BtAddObj.onclick = function(){
             addTreeNode(page4TreeObj,page4Tree2Obj);
        }

        //设置删除按钮操作
        var page4BtDelObj = $("page4BtDel");
        page4BtDelObj.onclick = function(){
             removeTreeNode(page4Tree2Obj);
        }

        //设置添加按钮操作
        var page4BtAdd2Obj = $("page4BtAdd2");
        page4BtAdd2Obj.onclick = function(){
             addTreeNode(page4TreeObj,page4Tree3Obj);
        }

        //设置删除按钮操作
        var page4BtDel2Obj = $("page4BtDel2");
        page4BtDel2Obj.onclick = function(){
             removeTreeNode(page4Tree3Obj);
        }
    }
    /*
     *	函数说明：栏目分发设置tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadChannelDistributeTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_MAIN_TREE);
        if(null!=xmlIsland){
            var page4TreeObj = $("page4Tree");
            page4TreeObj.load(xmlIsland.node);
            page4TreeObj.research = true;
        }
    }
    /*
     *	函数说明：栏目分发设置tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadChannelDistributeToTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_TO_EXIST_TREE);
        if(null!=xmlIsland){
            var page4Tree2Obj = $("page4Tree2");
            page4Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	函数说明：栏目分发设置tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadChannelDistributeFromTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_FROM_EXIST_TREE);
        if(null!=xmlIsland){
            var page4Tree3Obj = $("page4Tree3");
            page4Tree3Obj.load(xmlIsland.node);
        }
    }
    /*
     *	函数说明：保存栏目分发设置
     *	参数：	string:cacheID      缓存数据ID
                string:channelId    栏目id
     *	返回值：
     */
    function saveChannelDistribute(cacheID,channelId){

        var p = new HttpRequestParams();
        p.url = URL_SAVE_CHANNEL_DISTRIBUTE;
        p.setContent("channelId",channelId);
        
        var distributeCache = Cache.Variables.get(cacheID);
        if(null!=distributeCache){   

            //分发至
            var distributeToNode = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_TO_EXIST_TREE);
            if(null!=distributeToNode){
                var distributeToDataIDs = getTreeNodeIds(distributeToNode,"./treeNode//treeNode");
                p.setContent("distributeToIds",distributeToDataIDs.join(","));
            }

            //分发自
            var distributeFromNode = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_FROM_EXIST_TREE);
            if(null!=distributeFromNode){
                var distributeFromDataIDs = getTreeNodeIds(distributeFromNode,"./treeNode//treeNode");
                p.setContent("distributeFromIds",distributeFromDataIDs.join(","));
            }

            var request = new HttpRequest(p);
            //同步按钮状态
            var page4BtSaveObj = $("page4BtSave");
            syncButton([page4BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);

                //如果当前grid显示为此文章所在栏目，则刷新grid
                var gridObj = $("grid");
                if(true==gridObj.hasData_Xml()){
                    var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
                    var tempChannelId = tempXmlIsland.getAttribute("channelId");
                    if(tempChannelId==channelId){
                        loadGridData(tempChannelId,"1");//默认第一页

                        //刷新工具条
                        onInactiveRow();
                    }
                }
            }
            request.send();
        }
    }
    /*
     *	函数说明：检测用户列表右键菜单项是否可见
     *	参数：	string:code     操作码
     *	返回值：
     */
    function getUserOperation(code){
        var flag = false;
        var gridObj = $("grid");
        var curRowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        if(null!=curRowIndex){
            var curRowNode = gridObj.getRowNode_Xml(curRowIndex);
            var _operation = curRowNode.getAttribute("_operation");

            flag = checkOperation(code,_operation);
        }
        return flag;
    }
    /*
     *	函数说明：载入文章工具条
     *	参数：	
     *	返回值：
     */
    function loadArticleToolBarData(rowIndex){
        if(null==rowIndex){
            loadToolBar("p1,p2");
            return;
        }

        getGridOperation(rowIndex,loadToolBar);
    }
    /*
     *	函数说明：获取grid操作权限
     *	参数：	number:rowIndex         grid行号
                function:callback       回调函数
     *	返回值：
     */
    function getGridOperation(rowIndex,callback){
        var gridObj = $("grid");
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var id = rowNode.getAttribute("channelId");
        var _operation = rowNode.getAttribute("_operation");

        if(null==_operation || ""==_operation){//如果节点上还没有_operation属性，则发请求从后台获取信息
            var p = new HttpRequestParams();
            p.url = URL_GET_ARTICLE_OPERATION;
            p.setContent("channelId",id);

            var request = new HttpRequest(p);
            request.onresult = function(){
                _operation = this.getNodeValue(XML_OPERATION);
                rowNode.setAttribute("_operation",_operation);

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
    /*
     *	函数说明：查看分发来源
     *	参数：	
     *	返回值：
     */
    function showDistributeSource(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];

        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowName = gridObj.getNamedNodeValue_Xml(rowIndex,"title");
        var rowID = rowNode.getAttribute("id");
        var articleType = rowNode.getAttribute("state");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId){
            channelId = rowNode.getAttribute("channelId");
        }

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(eventObj){
            setTimeout(function(){
                loadDistributeSourceData(rowID, channelId);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page1";
        inf.label = OPERATION_DISTRIBUTE_SOURCE.replace(/\$label/i,rowName);
        inf.phases = null;
        inf.callback = callback;
        inf.SID = CACHE_DISTRIBUTE_SOURCE + rowID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：分发来源加载数据
     *	参数：	string:articleID        文章id
                string:channelID        栏目ID
     *	返回值：
     */
    function loadDistributeSourceData(articleID,channelID){
        var cacheID = CACHE_DISTRIBUTE_SOURCE + articleID;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_DISTRIBUTE_SOURCE;
            p.setContent("articleId", articleID);
            p.setContent("channelId", channelID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var distributeSourceNode = this.getNodeValue(XML_DISTRIBUTE_SOURCE);

                var distributeSourceNodeID = cacheID+"."+XML_DISTRIBUTE_SOURCE;

                Cache.XmlIslands.add(distributeSourceNodeID,distributeSourceNode);

                Cache.Variables.add(cacheID,[distributeSourceNodeID]);

                initDistributeSourcePages(cacheID,channelID);
            }
            request.send();
        }else{
            initDistributeSourcePages(cacheID,channelID);
        }
    }
    /*
     *	函数说明：分发来源相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                string:channelID        栏目id
     *	返回值：
     */
    function initDistributeSourcePages(cacheID,channelID){
        var page1FormObj = $("page1Form");
        Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
            loadDistributeSourceFormData(cacheID);
        });

        //设置翻页按钮显示状态
        var page1BtPrevObj = $("page1BtPrev");
        var page1BtNextObj = $("page1BtNext");
        page1BtPrevObj.style.display = "none";
        page1BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page1BtSaveObj = $("page1BtSave");
        page1BtSaveObj.onclick = function(){
        }
    }
    /*
     *	函数说明：分发来源xform加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadDistributeSourceFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_SOURCE);
        if(null!=xmlIsland){
            var page1FormObj = $("page1Form");
            page1FormObj.load(xmlIsland.node,null,"node");

            //2007-3-1 离开提醒
            attachReminder(cacheID,page1FormObj);
        }
    }
    /*
     *	函数说明：分发文章
     *	参数：	
     *	返回值：
     */
    function distributeArticle(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var rowName = rowNode.getAttribute("title");
        var articleType = rowNode.getAttribute("state");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId){
            channelId = rowNode.getAttribute("channelId");
        }

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(eventObj){
            setTimeout(function(){
                loadDistributeArticleData(rowID, channelId);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page5";
        inf.label = OPERATION_DISTRIBUTE.replace(/\$label/i,rowName);
        inf.phases = null;
        inf.callback = callback;
        inf.SID = CACHE_DISTRIBUTE_ARTICLE + rowID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：分发文章加载数据
     *	参数：	string:articleID        文章id
                string:channelID        栏目ID
     *	返回值：
     */
    function loadDistributeArticleData(articleID,channelID){
        var cacheID = CACHE_DISTRIBUTE_ARTICLE + articleID;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_DISTRIBUTE_ARTICLE;
            p.setContent("articleId", articleID);
            p.setContent("channelId", channelID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var distributeArticleTreeNode = this.getNodeValue(XML_DISTRIBUTE_ARTICLE_TREE);
                var distributeArticleExistTreeNode = this.getNodeValue(XML_DISTRIBUTE_ARTICLE_EXIST_TREE);

                var distributeArticleTreeNodeID = cacheID+"."+XML_DISTRIBUTE_ARTICLE_TREE;
                var distributeArticleExistTreeNodeID = cacheID+"."+XML_DISTRIBUTE_ARTICLE_EXIST_TREE;

                Cache.XmlIslands.add(distributeArticleTreeNodeID,distributeArticleTreeNode);
                Cache.XmlIslands.add(distributeArticleExistTreeNodeID,distributeArticleExistTreeNode);

                Cache.Variables.add(cacheID,[distributeArticleTreeNodeID,distributeArticleExistTreeNodeID]);

                initDistributeArticlePages(cacheID,articleID,channelID);
            }
            request.send();
        }else{
            initDistributeArticlePages(cacheID,articleID,channelID);
        }
    }
    /*
     *	函数说明：分发文章相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                string:articleID        文章id
                string:channelID        栏目id
     *	返回值：
     */
    function initDistributeArticlePages(cacheID,articleID,channelID){

        var page5TreeObj = $("page5Tree");
        Public.initHTC(page5TreeObj,"isLoaded","oncomponentready",function(){
            loadDistributeArticleTreeData(cacheID);
        });
        var page5Tree2Obj = $("page5Tree2");
        Public.initHTC(page5Tree2Obj,"isLoaded","oncomponentready",function(){
            loadDistributeArticleExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page5BtPrevObj = $("page5BtPrev");
        var page5BtNextObj = $("page5BtNext");
        page5BtPrevObj.style.display = "none";
        page5BtNextObj.style.display = "none";

        //设置搜索
        var page5BtSearchObj = $("page5BtSearch");
        var page5KeywordObj = $("page5Keyword");
        attachSearchTree(page5TreeObj,page5BtSearchObj,page5KeywordObj);

        //设置添加按钮操作
        var page5BtAddObj = $("page5BtAdd");
        page5BtAddObj.onclick = function(){
            addTreeNode(page5TreeObj,page5Tree2Obj);
        }

        //设置删除按钮操作
        var page5BtDelObj = $("page5BtDel");
        page5BtDelObj.onclick = function(){
             removeTreeNode(page5Tree2Obj);
        }

        //设置保存按钮操作
        var page5BtSaveObj = $("page5BtSave");
        page5BtSaveObj.onclick = function(){
            saveDistributeArticle(cacheID,articleID,channelID);
        }
    }
    /*
     *	函数说明：分发文章tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadDistributeArticleTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_ARTICLE_TREE);
        if(null!=xmlIsland){
            var page5TreeObj = $("page5Tree");
            page5TreeObj.load(xmlIsland.node);
            page5TreeObj.research = true;
        }
    }
    /*
     *	函数说明：分发文章tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadDistributeArticleExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_ARTICLE_EXIST_TREE);
        if(null!=xmlIsland){
            var page5Tree2Obj = $("page5Tree2");
            page5Tree2Obj.load(xmlIsland.node);
        }
    }
    /*
     *	函数说明：保存分发文章
     *	参数：	string:cacheID          缓存数据ID
                string:articleID        文章id
                string:channelID        栏目id
     *	返回值：
     */
    function saveDistributeArticle(cacheID,articleID,channelID){

        var p = new HttpRequestParams();
        p.url = URL_SAVE_DISTRIBUTE_ARTICLE;

        p.setContent("articleId",articleID);
        p.setContent("channelId",channelID);
        
        var cacheData = Cache.Variables.get(cacheID);
        if(null!=cacheData){

            //分发文章
            var distributeArticleNode = Cache.XmlIslands.get(cacheID+"."+XML_DISTRIBUTE_ARTICLE_EXIST_TREE);
            if(null!=distributeArticleNode){
                var distributeToChannelIds = getTreeNodeIds(distributeArticleNode,"./treeNode//treeNode");
                if( 0 < distributeToChannelIds.length){
                    p.setContent("distributeToChannelIds", distributeToChannelIds.join(","));
                }
            }

            var request = new HttpRequest(p);
            //同步按钮状态
            var page5BtSaveObj = $("page5BtSave");
            syncButton([page5BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);
            }
            request.send();
        }
    }
    /*
     *	函数说明：关联文章
     *	参数：	
     *	返回值：
     */
    function associateArticle(){
        var gridObj = $("grid");
        var rowIndex = gridObj.getCurrentRowIndex_Xml()[0];
        var rowNode = gridObj.getRowNode_Xml(rowIndex);
        var rowID = rowNode.getAttribute("id");
        var rowName = rowNode.getAttribute("title");
        var articleType = rowNode.getAttribute("state");

        //如果grid显示不是搜索结果，则从grid节点取栏目id，否则直接从每行上取
        var channelId = gridObj.getXmlDocument().getAttribute("channelId");
        if("search"==channelId){
            channelId = rowNode.getAttribute("channelId");
        }

        var callback = {};
        callback.onTabClose = function(eventObj){
            delCacheData(eventObj.tab.SID);
        };
        callback.onTabChange = function(eventObj){
            setTimeout(function(){
                loadAssociateArticleData(rowID, channelId);
            },TIMEOUT_TAB_CHANGE);
        };

        var inf = {};
        inf.defaultPage = "page6";
        inf.label = OPERATION_ASSOCIATE.replace(/\$label/i,rowName);
        inf.phases = null;
        inf.callback = callback;
        inf.SID = CACHE_ASSOCIATE_ARTICLE + rowID;
        var tab = ws.open(inf);
    }
    /*
     *	函数说明：关联文章详细信息加载数据
     *	参数：	string:articleID        文章id
                string:channelID        栏目ID
     *	返回值：
     */
    function loadAssociateArticleData(articleID,channelID){
        var cacheID = CACHE_ASSOCIATE_ARTICLE + articleID;
        var cacheData = Cache.Variables.get(cacheID);
        if(null==cacheData){
            var p = new HttpRequestParams();
            p.url = URL_ASSOCIATE_ARTICLE;
            p.setContent("articleId", articleID);
            p.setContent("channelId", channelID);

            var request = new HttpRequest(p);
            request.onresult = function(){
                var associateArticleNode = this.getNodeValue(XML_ASSOCIATE_ARTICLE_TREE);
                var associateArticleExistNode = this.getNodeValue(XML_ASSOCIATE_ARTICLE_EXIST_TREE);

                var associateArticleNodeID = cacheID+"."+XML_ASSOCIATE_ARTICLE_TREE;
                var associateArticleExistNodeID = cacheID+"."+XML_ASSOCIATE_ARTICLE_EXIST_TREE;

                Cache.XmlIslands.add(associateArticleNodeID,associateArticleNode);
                Cache.XmlIslands.add(associateArticleExistNodeID,associateArticleExistNode);

                Cache.Variables.add(cacheID,[associateArticleNodeID,associateArticleExistNodeID]);

                initAssociateArticlePages(cacheID,articleID,channelID);
            }
            request.send();
        }else{
            initAssociateArticlePages(cacheID,articleID,channelID);
        }
    }
    /*
     *	函数说明：关联文章相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                string:articleID        文章id
                string:channelID        栏目id
     *	返回值：
     */
    function initAssociateArticlePages(cacheID,articleID,channelID){

        var page6TreeObj = $("page6Tree");
        Public.initHTC(page6TreeObj,"isLoaded","oncomponentready",function(){
            loadAssociateArticleTreeData(cacheID,articleID);
        });

        var page6Tree2Obj = $("page6Tree2");
        Public.initHTC(page6Tree2Obj,"isLoaded","oncomponentready",function(){
            clearTreeData(page6Tree2Obj);
        });

        var page6Tree3Obj = $("page6Tree3");
        Public.initHTC(page6Tree3Obj,"isLoaded","oncomponentready",function(){
            loadAssociateArticleExistTreeData(cacheID);
        });

        //设置翻页按钮显示状态
        var page6BtPrevObj = $("page6BtPrev");
        var page6BtNextObj = $("page6BtNext");
        page6BtPrevObj.style.display = "none";
        page6BtNextObj.style.display = "none";

        //设置搜索
        var page6BtSearchObj = $("page6BtSearch");
        var page6KeywordObj = $("page6Keyword");
        attachSearchTree(page6TreeObj,page6BtSearchObj,page6KeywordObj);

        //设置搜索
        var page6BtSearch2Obj = $("page6BtSearch2");
        var page6Keyword2Obj = $("page6Keyword2");
        attachSearchTree(page6Tree2Obj,page6BtSearch2Obj,page6Keyword2Obj);

        //设置添加按钮操作
        var page6BtAddObj = $("page6BtAdd");
        page6BtAddObj.onclick = function(){
            addTreeNode(page6Tree2Obj,page6Tree3Obj);
        }

        //设置删除按钮操作
        var page6BtDelObj = $("page6BtDel");
        page6BtDelObj.onclick = function(){
             removeTreeNode(page6Tree3Obj);
        }

        //设置保存按钮操作
        var page6BtSaveObj = $("page6BtSave");
        page6BtSaveObj.onclick = function(){
            saveAssociateArticle(cacheID,articleID,channelID);
        }
    }
    /*
     *	函数说明：关联文章tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadAssociateArticleTreeData(cacheID,articleID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ASSOCIATE_ARTICLE_TREE);
        if(null!=xmlIsland){
            var page6TreeObj = $("page6Tree");
            page6TreeObj.load(xmlIsland.node);
            page6TreeObj.research = true;

            page6TreeObj.onTreeNodeDoubleClick = function(eventObj){
                onPage6TreeNodeDoubleClick(eventObj,articleID);
            }
        }
    }
    /*
     *	函数说明：关联文章tree加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadAssociateArticleExistTreeData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ASSOCIATE_ARTICLE_EXIST_TREE);
        if(null!=xmlIsland){
            var page6Tree3Obj = $("page6Tree3");
            page6Tree3Obj.load(xmlIsland.node);
        }
    }
    /*
     *	函数说明：点击page6Tree树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onPage6TreeNodeDoubleClick(eventObj,articleID){
        var treeObj = $("page6Tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();
            initPage6Tree2(id,articleID);
        }
    }
    /*
     *	函数说明：page6Tree2初始化
     *	参数：	string:id					相关树节点id
     *	返回值：
     */
    function initPage6Tree2(id,articleID){
        var page6Tree2Obj = $("page6Tree2");
        Public.initHTC(page6Tree2Obj,"isLoaded","oncomponentready",function(){
            loadPage6Tree2Data(id,articleID);
        });
    }
    /*
     *	函数说明：grid加载数据
     *	参数：	string:treeID				相关树节点id
     *	返回值：
     */
    function loadPage6Tree2Data(treeID,articleID){
        var cacheID = CACHE_ASSOCIATE_ARTICLE_LIST + treeID;
        var treeGrid = Cache.Variables.get(cacheID);
		var p = new HttpRequestParams();
		p.url = URL_ASSOCIATE_ARTICLE_LIST;
		p.setContent("channelId", treeID);
		p.setContent("articleId", articleID);

		var request = new HttpRequest(p);
		request.onresult = function(){
			var articleListNode = this.getNodeValue(XML_ASSOCIATE_ARTICLE_LIST);
			var articleListNodeID = cacheID+"."+XML_ASSOCIATE_ARTICLE_LIST;

			Cache.XmlIslands.add(articleListNodeID,articleListNode);
			Cache.Variables.add(cacheID,[articleListNodeID]);

			loadpage6Tree2DataFromCache(cacheID);
		}
		request.send();
    }
    /*
     *	函数说明：tree从缓存加载数据
     *	参数：	string:cacheID   grid数据相关树节点id
     *	返回值：
     */
    function loadpage6Tree2DataFromCache(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID+"."+XML_ASSOCIATE_ARTICLE_LIST);
        if(null!=xmlIsland){
            var page6Tree2Obj = $("page6Tree2");
            page6Tree2Obj.load(xmlIsland.node);
            page6Tree2Obj.research = true;
        }
    }
    /*
     *	函数说明：保存关联文章
     *	参数：	string:cacheID          缓存数据ID
                string:articleID        文章id
                string:channelID        栏目id
     *	返回值：
     */
    function saveAssociateArticle(cacheID,articleID,channelID){

        var p = new HttpRequestParams();
        p.url = URL_SAVE_ASSOCIATE_ARTICLE;

        p.setContent("articleId",articleID);
        
        var cacheData = Cache.Variables.get(cacheID);
        if(null!=cacheData){

            //关联文章
            var associateArticleNode = Cache.XmlIslands.get(cacheID+"."+XML_ASSOCIATE_ARTICLE_EXIST_TREE);
            if(null!=associateArticleNode){
                var associateArticleDataIDs = getTreeNodeIds(associateArticleNode,"./treeNode//treeNode");
                if(0<associateArticleDataIDs.length){
                    p.setContent("associateArticleIds",associateArticleDataIDs.join(","));
                }
            }

            var request = new HttpRequest(p);
            //同步按钮状态
            var page6BtSaveObj = $("page6BtSave");
            syncButton([page6BtSaveObj],request);

            request.onsuccess = function(){
                //解除提醒
                detachReminder(cacheID);
            }
            request.send();
        }
    }
    /*
     *	函数说明：发布文章
     *	参数：	string:type         区分完全发布“2”和增量发布“1”
     *	返回值：
     */
    function publishArticle(category){
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var id = treeNode.getId();

            var p = new HttpRequestParams();
            
            var isSite = getTreeAttribute("isSite");
            if("1"==isSite){
                p.url = URL_SITE_PUBLISH_PROGRESS;
            }else{
                p.url = URL_CHANNEL_PUBLISH_PROGRESS;
            }
			p.setContent("channelId",id);
            p.setContent("category",category);
            var request = new HttpRequest(p);
            request.onresult = function(){
                var data = this.getNodeValue("ProgressInfo");
                var url = "";
                if("1"==isSite){
	                url = URL_SITE_PUBLISH_PROGRESS;
	            }else{
	                url = URL_CHANNEL_PUBLISH_PROGRESS;
	            }
	            var progress = new Progress(URL_GET_PROGRESS,data,URL_CONCEAL_PROGRESS);
                progress.oncomplete = function(){
                    //发布完成
					//如果当前grid显示为此文章所在栏目，则刷新grid
					var gridObj = $("grid");
					if(true==gridObj.hasData_Xml()){
						var tempXmlIsland = new XmlNode(gridObj.getXmlDocument());
						var tempChannelId = tempXmlIsland.getAttribute("channelId");
						if(tempChannelId==id){
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
    function searchArticle(){

        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(null!=treeNode){
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var type = treeNode.getAttribute("isSite");
            var cacheID = CACHE_SEARCH_ARTICLE + treeID;
            var condition = window.showModalDialog("searcharticle.htm",{type:type,channelId:treeID,title:"搜索\""+treeName+"\"下的文章"},"dialogWidth:250px;dialogHeight:250px;");
            if(null!=condition){
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
    function loadSearchGridData(cacheID,page,sortName,direction){
        var condition = Cache.Variables.get("condition");
        if(null!=condition){
            var p = new HttpRequestParams();
            p.url = URL_SEARCH_ARTICLE;

            var xmlReader = new XmlReader(condition.dataXml);
            var dataNode = new XmlNode(xmlReader.documentElement);
            p.setXFormContent(dataNode, condition.prefix);
            p.setContent("condition.page.pageNum", page);
            if(null!=sortName && null!=direction){
                p.setContent("condition.field", sortName);
                p.setContent("condition.orderType", direction);
            }

            var request = new HttpRequest(p);
            request.onresult = function(){
                var articleListNode = this.getNodeValue(XML_ARTICLE_LIST);
                var articleListNodeID = cacheID+"."+XML_ARTICLE_LIST;

                var pageListNode = this.getNodeValue(XML_PAGE_LIST);
                var pageListNodeID = cacheID+"."+XML_PAGE_LIST;

                //给文章grid数据根节点增加channelId等属性
                articleListNode.setAttribute("channelId","search");

                //给当前排序列加上_direction属性
                if(null!=sortName && null!=direction){
                    var column = articleListNode.selectSingleNode("//column[@name='" + sortName + "']");
                    if(null!=column){
                        column.setAttribute("_direction",direction);
                    }
                }

                Cache.XmlIslands.add(articleListNodeID,articleListNode);
                Cache.XmlIslands.add(pageListNodeID,pageListNode);
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
    function initSearchGrid(cacheID){
        var gridObj = $("grid");
        Public.initHTC(gridObj,"isLoaded","onload",function(){
            loadGridDataFromCache(cacheID);
            loadGridEvents();

            //刷新工具条
            onInactiveRow();
        });
    
    }
    /*
     *	函数说明：刷新树节点停用启用状态
     *	参数：	XmlNode:xmlNode         XmlNode实例
                string:state            停/启用状态
     *	返回值：
     */
    function refreshTreeNodeState(xmlNode,state){
        var isSite = xmlNode.getAttribute("isSite");
        if("1"==isSite){
            var img = "site";
        }else{
            var img = "channel";
        }
        xmlNode.setAttribute("disabled",state);
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