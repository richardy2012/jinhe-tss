    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "ResourceTree";
    XML_CONTEXT_PATH = "ContextPath";

    /*
     *	XMLHTTP请求地址汇总
     */
   	URL_INIT           = "data/resource_init.xml?";
    URL_SAVE_UPLOAD    = "data/_success.xml?";
    URL_RENAME         = "data/_success.xml?";
    URL_DEL_FILES      = "data/_success.xml?";
    URL_DOWNLOAD_FILES = "data/download.zip?";
    URL_ADD_FOLDER     = "data/_success.xml?";

    function init() {
        loadFileTree();
    }

	var params;

    function loadFileTree(contextPath, filter) {
        params =  window.dialogArguments ? window.dialogArguments.params : {};

        var p = new HttpRequestParams();
        p.url = URL_INIT;
        for(var item in params) {
            p.setContent(item, params[item]);
        }
        if(contextPath) {
            p.setContent("contextPath", contextPath);
        }
        if(filter) {
            p.setContent("filter", filter);
        }

        var request = new HttpRequest(p);
        request.onresult = function() {
            var dataXmlNode = this.getNodeValue(XML_MAIN_TREE);
            var contextPath = this.getNodeValue(XML_CONTEXT_PATH);

            var treeObj = $T("tree", dataXmlNode);

			treeObj.element.onTreeNodeDoubleClick = function(eventObj) {
                var treeNode = eventObj.treeNode;
                var id   = treeNode.getId();
                var name = treeNode.getName();
                var isFolder = treeNode.getAttribute("isFolder");
                if("-1" == id) {
                    var newPath = getParentContextPath(); // 回到夫目录
                    loadFileTree(newPath);
                } else if("1" == isFolder) {
                    var newPath = getFolderContextPath(name); // 进入子目录
                    loadFileTree(newPath);
                }
            }

			// 显示相对路径
            var boxObj = $$("contextPathBox");
			boxObj.value = boxObj.title = contextPath;
			boxObj.scrollLeft = boxObj.scrollWidth;
        }
        request.send();   
    }
 
    /* 获取上级文件夹相对路径 */
    function getParentContextPath() {
        var boxObj = $$("contextPathBox");
        var contextPath = boxObj.value;
        var newPath = contextPath.replace(/[^//]*\/?$/,"");
        if(  newPath == "" ) {
            return null
        }
        return newPath;
    }

    /* 获取当前文件夹相对路径  */
    function getFolderContextPath(name) {
        var boxObj = $$("contextPathBox");
        var contextPath = boxObj.value;
        return contextPath + "/" + name + "/";
    }
 
    function upload() {
        var contextPath =  $$("contextPathBox").value;
		var url = URL_SAVE_UPLOAD + "?" + contextPath
		var importDiv = createImportDiv("", null, url);
		Element.show(importDiv);
    }

    /* 删除资源文件 */
    function delFiles() {
        var selectNodes =  $T("tree").getSelectedTreeNode();

        if(0 == selectNodes.length) {
             return alert("请先选择要删除的文件");
        }
        if(0 < selectNodes.length && !confirm("您确定要删除吗？")) {
            return;
        }

		var fileNames = [];
        var folderNames = [];

        for(var i=0; i < selectNodes.length; i++) {
            var selectNode = selectNodes[i];
            var id   = selectNode.getId();
            var name = selectNode.getName();
            var isFolder = selectNode.getAttribute("isFolder");
            if("-1" != id) {
                if("1" == isFolder) {
                    folderNames[folderNames.length] = name;                
                } else {
                    fileNames[fileNames.length] = name;
                }
            }
        }
        if(0 == fileNames.length && 0 == folderNames.length) {
            return;
        }

        var contextPath = $$("contextPathBox").value;

        var p = new HttpRequestParams();
        p.url = URL_DEL_FILES;
        p.setContent("contextPath", contextPath);
        p.setContent("fileNames", fileNames.join(","));
        p.setContent("folderNames", folderNames.join(","));

        var request = new HttpRequest(p);
        request.onsuccess = function() {
            removeTreeNode($T("tree"), ["-1"]);
        }
        request.send();
    }

    /* 重命名资源文件  */
    function renameFile() {
        var selectNodes = $T("tree").getSelectedTreeNode(false);
        var len = selectNodes.length;
        if(0 == len) {
            return alert("请先选择要重命名的文件");
        }
		if(1 < len) {
			return alert("一次只能对一个文件重命名");
        }
		
		var selectNode = selectNodes[0];
		var id = selectNode.getId();
		if("-1" == id) {
			return;
		}

		var name = selectNode.getName();
		var newName = prompt("请输入新的文件（夹）名称", name);
		if( newName && "" != newName) {
			var p = new HttpRequestParams();
			p.url = URL_RENAME;
			p.setContent("fileName", name);
			p.setContent("newFileName", newName);

			var contextPath =  $$("contextPathBox").value;
			p.setContent("contextPath",contextPath);

			var request = new HttpRequest(p);
			request.onsuccess = function() {
				modifyTreeNode(id, "name", newName,true);
			}
			request.send();
		}
    }

    /* 下载资源文件  */
    function downloadFiles() {
        var fileNames = [];
        var folderNames = [];
        var selectNodes = $T("tree").getSelectedTreeNode();
        if(0 == selectNodes.length) {
            return alert("请先选择要下载的文件");
         }
        for(var i=0; i < selectNodes.length; i++) {
            var selectNode = selectNodes[i];
            var id = selectNode.getId();
            var name = selectNode.getName();

            var isFolder = selectNode.getAttribute("isFolder");
            if("-1" != id) {
                if("1" == isFolder) {
                    folderNames[folderNames.length] = name;                
                } else {
                    fileNames[fileNames.length] = name;
                }
            }
        }

        var contextPath = $$("contextPathBox").value;

        fileNames = fileNames.join(",");
        folderNames = folderNames.join(",");

        var url = URL_DOWNLOAD_FILES;
        url += "?contextPath=" + contextPath;
        url += "&fileNames=" + fileNames;
        url += "&folderNames=" + folderNames;

		var frameName = createExportFrame();
        window.frames[frameName].location.href = url;
    }

    /* 新建文件夹  */
    function addFolder() {
        var contextPath = $$("contextPathBox").value;
        var name = prompt("请输入文件夹名称");
        if( name && "" != name ) {
            var p = new HttpRequestParams();
            p.url = URL_ADD_FOLDER;
            p.setContent("contextPath", contextPath);
            p.setContent("newFileName", name);

            var request = new HttpRequest(p);
            request.onsuccess = function() {
                loadFileTree(contextPath);
            }
            request.send();
        }
    }

    /* 过滤文件  */
    function filterFiles() {
        var filter = $$("filterBox").value;
        var contextPath = $$("contextPathBox").value;
        loadFileTree(contextPath, filter);
    }

    window.onload = init;