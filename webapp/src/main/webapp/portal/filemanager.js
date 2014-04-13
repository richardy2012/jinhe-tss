    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "ResourceTree";
    XML_CONTEXT_PATH = "ContextPath";

    /*
     *	XMLHTTP请求地址汇总
     */
   	URL_SOURCE_TREE    = AUTH_PATH + "portal/file/list";
    URL_RENAME         = AUTH_PATH + "portal/file";
    URL_DEL_FILES      = AUTH_PATH + "portal/file";
    URL_DOWNLOAD_FILES = AUTH_PATH + "portal/file";
    URL_ADD_FOLDER     = AUTH_PATH + "portal/file";

	if(IS_TEST) {
		URL_SOURCE_TREE    = "data/resource_init.xml?";
		URL_RENAME         = "data/_success.xml?";
		URL_DEL_FILES      = "data/_success.xml?";
		URL_DOWNLOAD_FILES = "data/download.zip?";
		URL_ADD_FOLDER     = "data/_success.xml?";
	}

    var params;
    function init() {
        params =  window.dialogArguments ? window.dialogArguments.params : {};

        loadFileTree();
    }

    function loadFileTree(contextPath, filter) {
		if(contextPath) {
            params.contextPath = contextPath;
        }
        if(filter) {
			params.filter = filter;
        }

		Ajax({
			url: URL_SOURCE_TREE,
			params: params,
			onresult: function() {
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
		});
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
        return contextPath + "/" + name;
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
		Ajax({
			url: URL_DEL_FILES,
			method: "DELETE",
			params: {"contextPath": contextPath, "fileNames": fileNames.join(","), "folderNames": folderNames.join(",")},
			onsuccess: function() {
				removeTreeNode($T("tree"), ["-1"]);
			}
		});
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
			var contextPath =  $$("contextPathBox").value;
			Ajax({
				url: URL_RENAME,
				method: "PUT",
				params: {"contextPath": contextPath, "fileName": name, "newFileName": newName},
				onsuccess: function() {
					modifyTreeNode(id, "name", newName, true);
				}
			});
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
        var name = prompt("请输入文件夹名称", "");
        if( name && "" != name ) {
			Ajax({
				url: URL_RENAME,
				method: "POST",
				params: {"contextPath": contextPath, "newFileName": name},
				onsuccess: function() {
					loadFileTree(contextPath);
				}
			});
        }
    }

    /* 过滤文件  */
    function filterFiles() {
        var filter = $$("filterBox").value;
        var contextPath = $$("contextPathBox").value;
        loadFileTree(contextPath, filter);
    }

	function upload() {
        var contextPath =  $$("contextPathBox").value;
		var url = URL_UPLOAD_FILE + "?contextPath=" + contextPath
		url += "&afterUploadClass=com.jinhe.tss.portal.helper.MovePortalFile";
		var importDiv = createImportDiv("", null, url);
		Element.show(importDiv);
    }

    window.onload = init;