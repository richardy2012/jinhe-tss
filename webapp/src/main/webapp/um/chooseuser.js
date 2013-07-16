    /*
     *	后台响应数据节点名称
     */
    XML_USER_LIST = "SourceList";
    XML_EXIST_USER_LIST = "ExistUserList";
    XML_SEARCH_USER = "SearchUser";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_MAIN_TREE = "tree__id";
    /*
     *	XMLHTTP请求地址汇总
     */
	URL_INIT = "data/chooseuser_init.xml";
    URL_SEARCH_USER = "data/chooseuser_search.xml";

    URL_INIT = "ums/message!getSearchUserInfo.action";
    URL_SEARCH_USER = "ums/message!searchUsers.action";

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
        var params = window.dialogArguments.params;

        var p = new HttpRequestParams();
        p.url = URL_INIT;

        for(var item in params){
            p.setContent(item,params[item]);
        }

        var request = new HttpRequest(p);
        request.onresult = function(){
            var existUserNode = this.getNodeValue(XML_EXIST_USER_LIST);
            var searchUserNode = this.getNodeValue(XML_SEARCH_USER);

            var existUserNodeID = XML_EXIST_USER_LIST;
            var searchUserNodeID = XML_SEARCH_USER;

            Cache.XmlIslands.add(existUserNodeID,existUserNode);
            Cache.XmlIslands.add(searchUserNodeID,searchUserNode);

            initChooseUserPages();

        }
        request.send();
    }
    /*
     *	函数说明：手工对应相关页加载数据
     *	参数：
     *	返回值：
     */
    function initChooseUserPages(){
        var xformObj = $("xform");
        Public.initHTC(xformObj,"isLoaded","oncomponentready",function(){
            loadChooseUserInfoFormData();

            //设置xform中搜索按钮操作
            var page5BtSearchObj = $("page5BtSearch");
            page5BtSearchObj.onclick = function(){
                searchUser();
            }
        });

        //设置添加按钮操作
        var btAddObj = $("btAdd");
        btAddObj.onclick = function(){
            addUser();
        }
        //设置删除按钮操作
        var btDelObj = $("btDel");
        btDelObj.onclick = function(){
            delUser();
        }

        var grid2Obj = $("grid2");
        Public.initHTC(grid2Obj,"isLoaded","onload",function(){
            loadExistUserListData();
        });
    }
    /*
     *	函数说明：手工对应xform加载数据
     *	参数：
     *	返回值：
     */
    function loadChooseUserInfoFormData(){
        var xmlIsland = Cache.XmlIslands.get(XML_SEARCH_USER);
        if(null!=xmlIsland){
            var xformObj = $("xform");
            xformObj.load(xmlIsland.node,null,"node");
        }
    }
    /*
     *	函数说明：手工对应可选grid加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadExistUserListData(){
        var grid2Obj = $("grid2");
        var xmlIsland = Cache.XmlIslands.get(XML_EXIST_USER_LIST);
        if(null!=xmlIsland){
            grid2Obj.load(xmlIsland.node,null,"node");
        }else{
            var xmlReader = new XmlReader("<grid><declare header=\"radio\"><column name=\"a\"/><column name=\"b\"/></declare><data></data></grid>");
            var emptyNode = new XmlNode(xmlReader.documentElement);
            grid2Obj.load(emptyNode.node,null,"node");
        }
    }
    /*
     *	函数说明：手工对应可选grid加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadSearchUserListData(){
        var gridObj = $("grid");
        var xmlIsland = Cache.XmlIslands.get(XML_USER_LIST);
        if(null!=xmlIsland){
            gridObj.load(xmlIsland.node,null,"node");
        }else{
            var xmlReader = new XmlReader("<grid><declare header=\"radio\"><column name=\"a\"/><column name=\"b\"/></declare><data></data></grid>");
            var emptyNode = new XmlNode(xmlReader.documentElement);
            gridObj.load(emptyNode.node,null,"node");
        }
    }
    /*
     *	函数说明：手工对应选择对应组
     *	参数：	string:groupId          组id
                string:groupName        组名
                string:groupType        组类型
     *	返回值：
     */
    function chooseGroup(){
        //弹出窗口数据从后台获取
        var xmlIsland = null;

        var group = getGroup();
        if(null!=group){
            var xformObj = $("xform");
            xformObj.updateDataExternal("groupId",group.id);
            xformObj.updateDataExternal("groupName",group.name);
        }

    }
    /*
     *	函数说明：弹出模态窗口选择用户组
     *	参数：	string:id               组/用户id
                XmlNode:xmlIsland       如果有该值，树数据不从后台取
                string:title            弹出窗口标题
                string:groupType        用户组类型
                string:applicationId    用户组所在应用
                string:type             1:添加组/2:添加用户/3:查看组
     *	返回值：
     */
    function getGroup(){
        var group = window.showModalDialog("messagegrouptree.htm",{title:"选择用户组"},"dialogWidth:300px;dialogHeight:400px;");
        return group;
    }

    /*
     *	函数说明：搜索手工对应可选用户
     *	参数：	
     *	返回值：
     */
    function searchUser(){
        var xformObj = $("xform");
        if(false==xformObj.checkForm()){
            return;
        }
        var p = new HttpRequestParams();
        p.url = URL_SEARCH_USER;

        var xmlIsland = Cache.XmlIslands.get(XML_SEARCH_USER);
        if(null!=xmlIsland){
            var dataNode = xmlIsland.selectSingleNode(".//data");
            if(null!=dataNode){
                var prefix = xmlIsland.selectSingleNode("./declare").getAttribute("prefix");
                p.setXFormContent(dataNode,prefix);
            }
        }

        var request = new HttpRequest(p);
        request.onresult = function(){
            var userListNode = this.getNodeValue(XML_USER_LIST);

            var userListNodeID = XML_USER_LIST;

            Cache.XmlIslands.add(userListNodeID,userListNode);

            loadSearchUserListData();
        }
        request.send();
    }
    /*
     *	函数说明：添加用户
     *	参数：
     *	返回值：
     */
    function addUser(){
        var gridObj = $("grid");
        var grid2Obj = $("grid2");
        var arr = gridObj.getSelectedRowsIndexArray_Xml();
        for(var i=0,iLen=arr.length;i<iLen;i++){
            var row = gridObj.getRowNode_Xml(arr[i]);
            row = row.cloneNode(false);
            row.removeAttribute("_checked");
            row.removeAttribute("class");
            var userId = row.getAttribute("userId");

            if(false == checkExistUser(userId)){
                gridObj.checkRow_Xml(arr[i],false,true);
                grid2Obj.addRowFromStr(row.xml,false,false);
            }
        }
        if(0 < arr.length){
            grid2Obj.reload();
        }
    }
    /*
     *	函数说明：检测是否已经存在该用户
     *	参数：
     *	返回值：
     */
    function checkExistUser(userId){
        var grid2Obj = $("grid2");
        var row = grid2Obj.getXmlDocument().selectSingleNode("//row[@userId='" + userId + "' and not(@_delete)]");
        var flag = row==null?false:true;
        return flag;
    }
    /*
     *	函数说明：删除用户
     *	参数：
     *	返回值：
     */
    function delUser(){
        var grid2Obj = $("grid2");
        var arr = grid2Obj.getSelectedRowsIndexArray_Xml();
        for(var i=0,iLen=arr.length;i<iLen;i++){
            grid2Obj.delRow_Xml(arr[i]);
        }    
    }
    /*
     *	函数说明：确定
     *	参数：
     *	返回值：
     */
    function ok(){

        var grid2Obj = $("grid2");
        var total = grid2Obj.getVisibleRowsLength();
        if(0 < total){
            var returnVal = [];
            for(var i=0;i<total;i++){
                var vIndex = grid2Obj.getRowIndexFromVisibleIndex(i);
                var gridRowNode = grid2Obj.getRowNode_Xml(vIndex);
                var userName = gridRowNode.getAttribute("userName")||"";
                var userId = gridRowNode.getAttribute("id")||"";
                var loginName = gridRowNode.getAttribute("loginName")||"";
                var groupName = gridRowNode.getAttribute("groupName")||"";

                returnVal[returnVal.length] = {
                    userName:userName,
                    loginName:loginName,
                    userId:userId,
                    groupName:groupName
                };
            }

            window.returnValue = returnVal;
            window.close();
        }else{
            alert("请至少选择一个用户");
        }
    }

    window.onload = init;