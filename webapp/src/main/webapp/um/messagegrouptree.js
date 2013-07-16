    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "GroupTree";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_MAIN_TREE = "tree__id";
    /*
     *	XMLHTTP请求地址汇总
     */
	URL_INIT = "data/group_tree.xml";

    URL_INIT = "ums/message!getGroupTree.action";

    /*
     *	函数说明：页面初始化
     *	参数：	
     *	返回值：
     */
    function init(){
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
			var groupTreeNode = this.getNodeValue(XML_MAIN_TREE);

			var groupTreeNodeID = CACHE_MAIN_TREE;

			Cache.XmlIslands.add(groupTreeNodeID,groupTreeNode);

			initTree(groupTreeNodeID);
		}
		request.send();
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
        }    
    }
    /*
     *	函数说明：点击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeActived(eventObj){
    }
    /*
     *	函数说明：双击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onTreeNodeDoubleClick(eventObj){
        getGroup();
    }
    /*
     *	函数说明：获得用户组节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function getGroup(){        
        var treeObj = $("tree");
        var activeTreeNode = treeObj.getActiveTreeNode();
        if(null!=activeTreeNode){
            var id = activeTreeNode.getId();
            var name = activeTreeNode.getName();
            var type = activeTreeNode.getAttribute("groupType");
            var appId = "";
            if(null==type || ""==type){
                id = "-4";
                appId = activeTreeNode.getId();
            }
            window.returnValue = {id:id,name:name,type:type,appId:appId};
            window.close();
        }
    }

    window.onload = init;