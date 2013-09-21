    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "ResourceTree";
    XML_CONTEXT_PATH = "ContextPath";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_MAIN_TREE = "tree__id";
    CACHE_UPLOAD_FORM = "upload__id";
    CACHE_FILE_LIST = "file__id";
    /*
     *	XMLHTTP请求地址汇总
     */
   	URL_INIT = "data/resource_init.xml";
    URL_SAVE_UPLOAD = "data/_success.xml";
    URL_RENAME = "data/_success.xml";
    URL_DEL_FILES = "data/_success.xml";
    URL_DOWNLOAD_FILES = "data/download.zip";
    URL_ADD_FOLDER = "data/_success.xml";

    URL_INIT = "pms/file!listAvailableFiles.action";
    URL_SAVE_UPLOAD = "pms/file!upload.action";
    URL_RENAME = "pms/file!renameFile.action";
    URL_DEL_FILES = "pms/file!deleteFile.action";
    URL_DOWNLOAD_FILES = "pms/file!download.action";
    URL_ADD_FOLDER = "pms/file!addDir.action";

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
        loadUploadForm();
        loadFileTree();
    }
    /*
     *	函数说明：上传文件表单加载数据
     *	参数：	
     *	返回值：
     */
    function loadUploadForm(){

        var str = [];
        str[str.length] = "<xform>";
        str[str.length] = "    <declare>	";
        str[str.length] = "        <column name=\"file\" caption=\"上传文件\" mode=\"file\" clickOnly=\"false\"/>";
        str[str.length] = "        <column name=\"id\" caption=\"\" mode=\"hidden\" editable=\"false\"/>";
        str[str.length] = "        <column name=\"name\" caption=\"\" mode=\"hidden\" editable=\"false\"/>";
        str[str.length] = "    </declare>";
        str[str.length] = "    <layout>";
        str[str.length] = "        <TR>";
        str[str.length] = "            <TD>";
        str[str.length] = "                <input binding=\"file\" type=\"text\" style=\"width:190px\"/>";
        str[str.length] = "                &amp;nbsp;<input id=\"btUpload\" type=\"button\" value=\"上传\" class=\"btWeak\" style=\"margin-left:-6\" onclick=\"upload()\"/>";
        str[str.length] = "            </TD>";
        str[str.length] = "        </TR>";
        str[str.length] = "    </layout>";
        str[str.length] = "    <data>";
        str[str.length] = "    </data>";
        str[str.length] = "</xform>";

        var xmlReader = new XmlReader();
        xmlReader.loadXML(str.join(""));

        var uploadFormNode = new XmlNode(xmlReader.documentElement);
        var uploadFormNodeID = CACHE_UPLOAD_FORM;

        Cache.XmlIslands.add(uploadFormNodeID,uploadFormNode);

        initUploadForm(uploadFormNodeID);
    
    }
    /*
     *	函数说明：初始化上传文件xform
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function initUploadForm(cacheID){
        var uploadFormObj = $("uploadForm");
        Public.initHTC(uploadFormObj,"isLoaded","oncomponentready",function(){
            loadUploadFormData(cacheID);
        });
    }
    /*
     *	函数说明：上传文件xform加载数据
     *	参数：	string:cacheID     缓存数据id
     *	返回值：
     */
    function loadUploadFormData(cacheID){
        var xmlIsland = Cache.XmlIslands.get(cacheID);
        if(null!=xmlIsland){
            var uploadFormObj = $("uploadForm");
            uploadFormObj.load(xmlIsland.node,null,"node");
        }
    }
    /*
     *	函数说明：文件树加载数据
     *	参数：	string:contextPath      相对路径
                string:filter           过滤
     *	返回值：
     */
    function loadFileTree(contextPath,filter){
        var params = window.dialogArguments.params;

        var p = new HttpRequestParams();
        p.url = URL_INIT;
        for(var item in params){
            p.setContent("paramsMap['" + item + "']",params[item]);
        }
        if(null!=contextPath){
            p.setContent("contextPath",contextPath);
        }
        if(null!=filter){
            p.setContent("filter",filter);
        }

        var request = new HttpRequest(p);
        request.onresult = function(){
            var treeNode = this.getNodeValue(XML_MAIN_TREE);
            var treeNodeID = CACHE_MAIN_TREE;

            var contextPath = this.getNodeValue(XML_CONTEXT_PATH);

            Cache.XmlIslands.add(treeNodeID,treeNode);

            initTree(treeNodeID);
            refreshContextPath(contextPath);
        }
        request.send();

        var btUploadObj = $("btUpload");
        if(null != btUploadObj){
            btUploadObj.disabled = false;
        }    
    }
    /*
     *	函数说明：显示相对路径
     *	参数：	
     *	返回值：
     */
    function refreshContextPath(contextPath){
        var boxObj = $("contextPathBox");
        boxObj.value = boxObj.title = contextPath;
        boxObj.scrollLeft = boxObj.scrollWidth;
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
            var multiple = window.dialogArguments.multiple;
            treeObj.load(xmlIsland.node);

            treeObj.onTreeNodeDoubleClick = function(eventObj){
                var treeNode = eventObj.treeNode;
                var id = treeNode.getId();
                var name = treeNode.getName();
                var isFolder = treeNode.getAttribute("isFolder");
                if("-1"==id){
                    var newPath = getParentContextPath();
                    loadFileTree(newPath);
                }else if("1"==isFolder){
                    var newPath = getFolderContextPath(name);
                    loadFileTree(newPath);
                }
            }
        }
    }
    /*
     *	函数说明：获取上级文件夹相对路径
     *	参数：  
     *	返回值：
     */
    function getParentContextPath(){
        var boxObj = $("contextPathBox");
        var contextPath = boxObj.value;
        var newPath = contextPath.replace(/[^//]*\/?$/,"");
        if(""==newPath){
            newPath = "/"
        }
        return newPath;
    }
    /*
     *	函数说明：获取当前文件夹相对路径
     *	参数：  
     *	返回值：
     */
    function getFolderContextPath(name){
        var boxObj = $("contextPathBox");
        var contextPath = boxObj.value;
        var newPath = contextPath + "/" + name + "/";
        return newPath;
    }
    /*
     *	函数说明：上传
     *	参数： 
     *	返回值：
     */
    function upload(){
        var uploadFormObj = $("uploadForm");
        var fileName = uploadFormObj.getData("file");
        if (fileName==null || fileName==""){
            uploadFormObj.showCustomErrorInfo("file","请选择上传文件");
        }else {
            var boxObj = $("contextPathBox");
            var contextPath = boxObj.value;

            var params = window.dialogArguments.params;
            var id = params.id;
            var type = params.type;
            var url = URL_SAVE_UPLOAD + "?id=" + id + "&paramsMap['type']=" + type + "&contextPath=" + contextPath;
            for(var item in params){
                url = url + "&paramsMap['" + item + "']=" + params[item];
            }
            uploadFormObj.upload(url);

            var btUploadObj = $("btUpload");
            if(null != btUploadObj){
                btUploadObj.disabled = true;
            }
        }
    }
    /*
     *	函数说明：删除资源文件
     *	参数：
     *	返回值：
     */
    function delFiles(){

        var treeObj = $("tree");
        var fileNames = [];
        var folderNames = [];
        var selectNodes = treeObj.getSelectedTreeNode();

        if(0==selectNodes.length){
            alert("请先选择要删除的文件");
            return;
        }
        if(0<selectNodes.length && true!=confirm("您确定要删除吗？")){
            return;
        }
        for(var i=0,iLen=selectNodes.length;i<iLen;i++){
            var selectNode = selectNodes[i];
            var id = selectNode.getId();
            var name = selectNode.getName();
            var isFolder = selectNode.getAttribute("isFolder");
            if("-1"!=id){
                if("1"==isFolder){
                    folderNames[folderNames.length] = name;                
                }else{
                    fileNames[fileNames.length] = name;
                }
            }
        }
        if(0==fileNames.length && 0==folderNames.length){
            return;
        }

        var boxObj = $("contextPathBox");
        var contextPath = boxObj.value;

        var p = new HttpRequestParams();
        p.url = URL_DEL_FILES;
        p.setContent("contextPath",contextPath);
        p.setContent("fileNames",fileNames.join(","));
        p.setContent("folderNames",folderNames.join(","));

        var request = new HttpRequest(p);
        request.onsuccess = function(){
            removeTreeNode(treeObj,["-1"]);
        }
        request.send();
    }
    /*
     *	函数说明：重命名资源文件
     *	参数：
     *	返回值：
     */
    function renameFile(){
        var treeObj = $("tree");
        var selectNodes = treeObj.getSelectedTreeNode(false);
        var len = selectNodes.length;
        if(0 == len){
            alert("请先选择要重命名的文件");
            return;
        }else if(1 < len){
			alert("一次只能对一个文件重命名");
			return;
        }else if(0<len){
            var selectNode = selectNodes[0];
            var id = selectNode.getId();
            if("-1"==id){
                if(1==selectNodes.length){
                    return;
                }else{
                    var selectNode = selectNodes[1];
                    var id = selectNode.getId();
                }
            }

            var name = selectNode.getName();
            var isFolder = selectNode.getAttribute("isFolder");
            var newName = prompt("请输入新的" + ("1"==isFolder?"文件夹":"文件") + "名称",name,"重命名" + ("1"==isFolder?"文件夹":"文件") + "\"" + name + "\"",null,50);
            if(null!=newName && ""!=newName){

                var p = new HttpRequestParams();
                p.url = URL_RENAME;
                p.setContent("id",id);
                p.setContent("fileName",name);
                p.setContent("newFileName",newName);
                p.setContent("isFolder",isFolder);

                var boxObj = $("contextPathBox");
                var contextPath = boxObj.value;
                p.setContent("contextPath",contextPath);

                var request = new HttpRequest(p);
                request.onsuccess = function(){
                    modifyTreeNode(id,"name",newName,true);
                }
                request.send();
            }
        }
    }
    /*
     *	函数说明：下载资源文件
     *	参数：
     *	返回值：
     */
    function downloadFiles(){
        var treeObj = $("tree");
        var fileNames = [];
        var folderNames = [];
        var selectNodes = treeObj.getSelectedTreeNode();
        if(0==selectNodes.length){
            alert("请先选择要下载的文件");
            return;
        }
        for(var i=0,iLen=selectNodes.length;i<iLen;i++){
            var selectNode = selectNodes[i];
            var id = selectNode.getId();
            var name = selectNode.getName();

            var isFolder = selectNode.getAttribute("isFolder");
            if("-1"!=id){
                if("1"==isFolder){
                    folderNames[folderNames.length] = name;                
                }else{
                    fileNames[fileNames.length] = name;
                }
            }
        }

        var boxObj = $("contextPathBox");
        var contextPath = boxObj.value;

        fileNames = fileNames.join(",");
        folderNames = folderNames.join(",");

        createDownloadWindow(contextPath,fileNames,folderNames);
    }
    /*
     *	函数说明：创建下载新窗口
     *	参数：
     *	返回值：
     */
    function createDownloadWindow(contextPath,fileNames,folderNames){
        var url = URL_DOWNLOAD_FILES;
        url += "?contextPath=" + contextPath;
        url += "&fileNames=" + fileNames;
        url += "&folderNames=" + folderNames;

        window.frames["downloadFrame"].location.href = url;
    }
    /*
     *	函数说明：新建文件夹
     *	参数：
     *	返回值：
     */
    function addFolder(){
        var boxObj = $("contextPathBox");
        var contextPath = boxObj.value;

        var name = prompt("请输入文件夹名称","undefined-1","新建文件夹");
        if(null!=name && ""!=name){
            var p = new HttpRequestParams();
            p.url = URL_ADD_FOLDER;
            p.setContent("contextPath",contextPath);
            p.setContent("newFileName",name);

            var request = new HttpRequest(p);
            request.onsuccess = function(){
                loadFileTree(contextPath);
            }
            request.send();
        }
    }
    /*
     *	函数说明：过滤文件
     *	参数：
     *	返回值：
     */
    function filterFiles(){
        var boxObj = $("filterBox");
        var filter = boxObj.value;

        var boxObj = $("contextPathBox");
        var contextPath = boxObj.value;

        loadFileTree(contextPath,filter);
    }

    window.onload = init;