    /*
     *	后台响应数据节点名称
     */
    XML_SEARCH_PERMISSION = "SearchPermission";
    XML_RESOURCE_TYPE = "ResourceType";
    XML_SET_PERMISSION = "setPermission";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_MAIN_TREE = "tree__id";
    /*
     *	XMLHTTP请求地址汇总
     */
	URL_INIT = "data/setpermission_init.xml";
    URL_GET_RESOURCE_TYPE = "data/resourcetype.xml";
    URL_PERMISSION = "data/setpermission.xml";
    URL_SAVE_ROLE_PERMISSION = "data/_success.xml";

    URL_INIT = "ums/role!initSetPermission.action";
    URL_GET_RESOURCE_TYPE = "ums/role!getResourceTypes.action";
    URL_PERMISSION = "ums/role!getPermissionMatrix.action";
    URL_SAVE_ROLE_PERMISSION = "ums/role!savePermission.action";

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
        var type = window.dialogArguments.type;
        var params = window.dialogArguments.params;

        var p = new HttpRequestParams();
        p.url = URL_INIT;

        for(var item in params){
            p.setContent(item,params[item]);
        }

        var request = new HttpRequest(p);
        request.onresult = function(){
            var searchPermissionNode = this.getNodeValue(XML_SEARCH_PERMISSION);
            var searchPermissionNodeID = XML_SEARCH_PERMISSION;

            if("0" == params["isRole2Resource"]){
                //设置用户、用户组权限，自动隐藏应用系统和资源类型字段
                if("group"==type || "user"==type || "app"==type || "role"==type){
                    var hideCells = searchPermissionNode.selectNodes("layout/TR/TD[.//*/@binding='applicationId' or .//*/@binding='resourceType']");
                    for(var i=0,iLen=hideCells.length;i<iLen;i++){
                        hideCells[i].setAttribute("style","display:none");
                    }
                }
            }

            Cache.XmlIslands.add(searchPermissionNodeID,searchPermissionNode);

            initSearchPermissionPages();
        }
        request.send();
    }
    /*
     *	函数说明：权限查询相关页加载数据
     *	参数：	
     *	返回值：
     */
    function initSearchPermissionPages(){

        var xformObj = $("xform");
        Public.initHTC(xformObj,"isLoaded","oncomponentready",function(){
            loadSearchPermissionData();
            //默认执行选择应用操作
//            setTimeout(function(){
//                var app = xformObj.getData("applicationId");
//                updateSearchPermissionColumn(cacheID,app);
//            },1000);
        });

        var treeObj = $("tree");
        Public.initHTC(treeObj,"isLoaded","oncomponentready",function(){
            loadPermissionData();
        });
    }
    /*
     *	函数说明：权限查询xform加载数据
     *	参数：	
     *	返回值：
     */
    function loadSearchPermissionData(){
        var xmlIsland = Cache.XmlIslands.get(XML_SEARCH_PERMISSION);
        if(null!=xmlIsland){
            var xformObj = $("xform");
            xformObj.load(xmlIsland.node,null,"node");

            xformObj.ondatachange = function(){
                var name = event.result.name;
                var value = event.result.newValue;
                if("applicationId"==name){
                    updateSearchPermissionColumn(value);
                }
            }

            //设置查询按钮操作
            var btSearchObj = $("page3BtSearch");
            btSearchObj.onclick = function(){
                searchPermission();
            }
        }
    }
    /*
     *	函数说明：级联资源类型下拉菜单
     *	参数：	string:applicationID        应用id
     *	返回值：
     */
    function updateSearchPermissionColumn(applicationID){
        var p = new HttpRequestParams();
        p.url = URL_GET_RESOURCE_TYPE;
        p.setContent("applicationId",applicationID);
        var request = new HttpRequest(p);
        request.onresult = function(){
            var resourceTypeNode = this.getNodeValue(XML_RESOURCE_TYPE);
            var name = resourceTypeNode.getAttribute("name");
            
            var xmlIsland = Cache.XmlIslands.get(XML_SEARCH_PERMISSION);
            if(null!=xmlIsland){
                var oldColumn = xmlIsland.selectSingleNode(".//column[@name='"+name+"']");
                var attributes = resourceTypeNode.attributes;
                for(var i=0,iLen=attributes.length;i<iLen;i++){
                    oldColumn.setAttribute(attributes[i].nodeName,attributes[i].nodeValue);
                }
                loadSearchPermissionData();
            }
        }
        request.send();
    }
    /*
     *	函数说明：查询权限
     *	参数：	
     *	返回值：
     */
    function searchPermission(){
        var xformObj = $("xform");
        var applicationID = xformObj.getData("applicationId")||"";
        var resourceType = xformObj.getData("resourceType")||"";
        var permissionRank = xformObj.getData("permissionRank")||"";
        var isRole2Resource = xformObj.getData("isRole2Resource")||"";

        var roleID = xformObj.getData("roleId")||"";
        var p = new HttpRequestParams();
        p.url = URL_PERMISSION;
        p.setContent("applicationId", applicationID);
        p.setContent("resourceType", resourceType);
        p.setContent("permissionRank", permissionRank);
        p.setContent("roleId", roleID);
        p.setContent("isRole2Resource", isRole2Resource);

        var request = new HttpRequest(p);
        request.onresult = function(){
            
            var role2PermissionNode = this.getNodeValue(XML_SET_PERMISSION);
            var role2PermissionNodeID = XML_SET_PERMISSION;

            //给树节点加搜索条件属性值，以便保存时能取回
            role2PermissionNode.setAttribute("applicationId", applicationID);
            role2PermissionNode.setAttribute("resourceType", resourceType);
            role2PermissionNode.setAttribute("permissionRank", permissionRank);
            role2PermissionNode.setAttribute("roleId", roleID);
            role2PermissionNode.setAttribute("isRole2Resource", isRole2Resource);

            Cache.XmlIslands.add(role2PermissionNodeID,role2PermissionNode);

            initPermissionPages();
        }
        request.send();
    }
    /*
     *	函数说明：权限相关页加载数据
     *	参数：	
     *	返回值：
     */
    function initPermissionPages(){
        var treeObj = $("tree");
        Public.initHTC(treeObj,"isLoaded","oncomponentready",function(){
            loadPermissionData();
        });
    }
    /*
     *	函数说明：权限信息tree加载数据
     *	参数：	
     *	返回值：
     */
    function loadPermissionData(){
        var xmlIsland = Cache.XmlIslands.get(XML_SET_PERMISSION);
        if(null!=xmlIsland){
            var treeObj = $("tree");
            treeObj.load(xmlIsland.node);
        }else{
            var treeObj = $("tree");
            var xmlReader = new XmlReader("<actionSet></actionSet>");
            var emptyTreeNode = new XmlNode(xmlReader.documentElement);
            treeObj.load(emptyTreeNode.node);
        }

        treeObj.onExtendNodeChange = function(eventObj){
            onExtendNodeChange(eventObj);
        }
    }
    /*
     *	函数说明：保存权限
     *	参数：	string:cacheID      缓存数据ID
     *	返回值：
     */
    function savePermission(){
        var flag = false;

        var p = new HttpRequestParams();
        p.url = URL_SAVE_ROLE_PERMISSION;

        //用户对权限选项
        var role2PermissionNode = Cache.XmlIslands.get(XML_SET_PERMISSION);
        if(null!=role2PermissionNode){
            role2PermissionNode = role2PermissionNode.cloneNode(true);

            //取回搜索条件，加入到提交数据
            var applicationID = role2PermissionNode.getAttribute("applicationId");
            var resourceType = role2PermissionNode.getAttribute("resourceType");
            var permissionRank = role2PermissionNode.getAttribute("permissionRank");
            var roleID = role2PermissionNode.getAttribute("roleId");
            var isRole2Resource = role2PermissionNode.getAttribute("isRole2Resource");
            p.setContent("applicationId", applicationID);
            p.setContent("resourceType", resourceType);
            p.setContent("permissionRank", permissionRank);
            p.setContent("roleId", roleID);
            p.setContent("isRole2Resource", isRole2Resource);

            var nodesStr = [];
            var optionIDs = [];
            var role2PermissionOptions = role2PermissionNode.selectNodes(".//options/option");

            //获取option的id名
            for(var i=0,iLen=role2PermissionOptions.length;i<iLen;i++){
                var curOption = role2PermissionOptions[i];
                var curOptionID = curOption.selectSingleNode("operationId").text;
                optionIDs.push(curOptionID);
            }

            var role2PermissionDataNodes = role2PermissionNode.selectNodes(".//treeNode");
            for(var i=0,iLen=role2PermissionDataNodes.length;i<iLen;i++){
                var curNode = role2PermissionDataNodes[i];
                var curNodeID = curNode.getAttribute("id");
                var curNodeStr = "";
                //按照option的顺序获取值，并拼接字符串
                for(var j=0,jLen=optionIDs.length;j<jLen;j++){
                    var curNodeOption = curNode.getAttribute(optionIDs[j]);

                    //2007-4-19 父节点是2(所有子节点)的，则子节点不需要传2
                    if("2"==curNodeOption){
                        var curParentNode = curNode.getParent();
                        var curParentNodeOption = curParentNode.getAttribute(optionIDs[j]);
                        if("2"==curParentNodeOption){
                            curNodeOption = "0";
                        }
                    }

                    curNodeStr += curNodeOption||"0";
                }
                //整行全部标记至少有一个为1或者2才允许提交
                if("0"==isRole2Resource || true==/(1|2)/.test(curNodeStr)){
                    nodesStr.push(curNodeID + "|" + curNodeStr);                
                }
            }

            //即使一行数据也没有，也要执行提交
            flag = true;
            p.setContent(XML_SET_PERMISSION,nodesStr.join(","));
        }

        if(true==flag){
            var request = new HttpRequest(p);
            request.onsuccess = function(){
            }
            request.send();
        }
    }
    /*
     *  函数说明：点击更改权限
     *            选中状态：1仅此选中/2当前及所有子节点选中/0未选
     *            纵向依赖：2选中上溯，取消下溯/3选中下溯，取消上溯
     *  参数：	
     *  返回值：
     */
    function onExtendNodeChange(eventObj){
        var treeObj = $("tree");

        var treeNode = eventObj.treeNode;
		var curState = eventObj.defaultValue;
		var nextState = eventObj.newValue;
		var id = eventObj.optionId;
        var shiftKey = eventObj.shiftKey;

        var option = new XmlNode(treeObj.getOptionById(id));
        var dependParent = option.selectSingleNode("dependParent");
        if(null!=dependParent){
            dependParent = dependParent.text.replace(/^\s*|\s*$/g,"");
        }

        if(curState!=nextState){
            //纵向依赖3选中时，直接转入目标状态2(所有子节点)
            if("3"==dependParent && "1"==nextState){
                treeNode.changeExtendSelectedState(id,shiftKey,"2");
                eventObj.returnValue = false;//阻止原先设置为1的操作
                return;
            }

            //横向依赖
            if("1"==nextState){
                //仅此选中时同时选中依赖项
                setDependSelectedState(treeNode,id,nextState);
            }else if("2"==nextState){
                //所有子节点选中时同时选中依赖项
                setDependSelectedState(treeNode,id,nextState);
            }else if("0"==nextState){
                //取消时同时取消被依赖项
                setDependedSelectedState(treeNode,id,nextState);
            }

            //纵向依赖
            if(null!=dependParent){
                if(("2"==dependParent && "1"==nextState) || ("3"==dependParent && "0"==nextState)){
                    //纵向依赖2选中或者3取消时，上溯
                    setParentSelectedState(treeNode,id,nextState);
                }else if("2"==dependParent && "2"==nextState){
                    //纵向依赖2选中，上溯
                    setParentSelectedState(treeNode,id,"1");
                }else if(("2"==dependParent && "0"==nextState) || ("3"==dependParent && "1"==nextState)){
                    //纵向依赖2取消或者3选中时，下溯
                    setChildsSelectedState(treeNode,id,nextState);
                }
            }

            //当前节点目标状态是2(所有子节点)时，下溯
            if("2"==nextState){
                setChildsSelectedState(treeNode,id,nextState);
            }

            //当前节点目标状态是0或者1，则设置父节点仅此
            if("0"==nextState || "1"==nextState){
                setParentSingleState(treeNode,id);
            }

            //同时按下shift键时
            if(true==shiftKey){
                setChildsSelectedState(treeNode,id,nextState,shiftKey);
            }

        }
//        eventObj.returnValue = false;    
    }
	/*
	 * 设置横向依赖项选中状态
	 * 参数：	treeNode:treeNode       节点对象
                string:id               当前项id
                string:nextState        目标状态
	 * 返回：	
	 */
	function setDependSelectedState(treeNode,id,nextState){
        var treeObj = $("tree");
        var curOption = new XmlNode(treeObj.getOptionById(id));

        var curIds = curOption.selectSingleNode("dependId");
        if(null!=curIds){
            curIds = curIds.text.replace(/^\s*|\s*$/g,"");
            if(""!=curIds){
                curIds = curIds.split(",");

                for(var i=0,iLen=curIds.length;i<iLen;i++){
                    var curId = curIds[i];
                    var curState = treeNode.getAttribute(curId);

                    if(nextState!=curState && ("2"!=curState || "1"!=nextState)){
                        //目标状态与当前状态不同(如果当前已经是2，而目标是1则不执行)
                        treeNode.changeExtendSelectedState(curId,null,nextState);
                    }
                }
            }
        }
    }
	/*
	 * 设置横向被依赖项选中状态
	 * 参数：	treeNode:treeNode       节点对象
                string:id               当前项id
                string:nextState        目标状态
	 * 返回：	
	 */
	function setDependedSelectedState(treeNode,id,nextState){
        var treeObj = $("tree");
        var curOption = new XmlNode(treeObj.getOptionById(id));

        var curIds = curOption.selectSingleNode("dependedId");
        if(null!=curIds){
            curIds = curIds.text.replace(/^\s*|\s*$/g,"");
            if(""!=curIds){
                curIds = curIds.split(",");

                for(var j=0,jLen=curIds.length;j<jLen;j++){
                    var curId = curIds[j];
                    var curState = treeNode.getAttribute(curId);

                    if(nextState!=curState){
                        treeNode.changeExtendSelectedState(curId,null,nextState);
                    }
                }
            }
        }
    }
	/*
	 * 设置父节点依赖项选中状态
	 * 参数：	treeNode:treeNode       节点对象
                string:id               当前项id
                string:nextState        目标状态
	 * 返回：	
	 */
	function setParentSelectedState(treeNode,id,nextState){
        var treeObj = $("tree");

        var parentNode = treeNode.getParent();
        if(null!=parentNode && "treeNode"==parentNode.node.nodeName){
            parentNode.changeExtendSelectedState(id,null,nextState);
        }
    }
	/*
	 * 设置子节点依赖项选中状态
	 * 参数：	treeNode:treeNode       节点对象
                string:id               当前项id
                string:nextState        目标状态
                boolean:shiftKey        是否按下shift键
	 * 返回：	
	 */
	function setChildsSelectedState(treeNode,id,nextState,shiftKey){
        var treeObj = $("tree");

        var childs = treeNode.node.selectNodes("treeNode");
        if(0<childs.length){
            for(var i=0,iLen=childs.length;i<iLen;i++){
                var child = childs[i];
                var childId = child.getAttribute("id");
                if(null!=childId){
                    var childNode = treeObj.getTreeNodeById(childId);
                    if(null!=childNode){
                        childNode.changeExtendSelectedState(id,shiftKey,nextState);
                    }
                }
            }
            
        }
    }
	/*
	 * 设置父节点仅此
	 * 参数：	treeNode:treeNode       节点对象
                string:id               当前项id
                string:nextState        目标状态
	 * 返回：	
	 */
	function setParentSingleState(treeNode,id){
        var treeObj = $("tree");

        var parentNode = treeNode.getParent();
        if(null!=parentNode && "treeNode"==parentNode.node.nodeName){
            var curState = parentNode.getAttribute(id);
            if("2"==curState){
                parentNode.changeExtendSelectedState(id,null,"1");
            }
        }
    }


    function test(){
        var treeObj = $("tree");
        var treeNode = treeObj.getTreeNodeById("r1");
        alert(treeNode.node.xml)
    
    }


    window.onload = init;