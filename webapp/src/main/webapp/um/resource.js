	/*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "AppSource";
    XML_APPLICATION_DETAIL = "AppDetail";
    XML_SOURCE_TYPE_INFO = "TypeInfo";
    XML_PERMISSION_OPTION_INFO = "PermissionOption";
    XML_IMPORT_APPLICATION = "ImportApplication";

	/*
     *	默认唯一编号名前缀
     */
    CACHE_APPLICATION_DETAIL = "app__id";
    CACHE_VIEW_APPLICATION_DETAIL = "viewApp__id";
    CACHE_VIEW_SOURCE_TYPE = "viewSourceType__id";
    CACHE_IMPORT_APPLICATION = "import__id";

    OPERATION_IMPORT = "导入\"$label\"";

	/*
     *	XMLHTTP请求地址汇总
     */
    URL_INIT = "ums/appResource!getAllApplication2Tree.action";
    URL_APPLICATION_DETAIL = "ums/appResource!getApplicationInfo.action";
    URL_SAVE_APPLICATION = "ums/appResource!editApplication.action";
    URL_RESOURCE_TYPE = "ums/appResource!getResourceTypeInfo.action";
    URL_IMPORT_TEMPLATE = "ums/resourceRegister!getImportTemplate.action";
    URL_IMPORT = "ums/resourceRegister!applicationRegisterByXML.action";
	
	if(IS_TEST) {
		URL_INIT = "data/resource_tree.xml";
		URL_APPLICATION_DETAIL = "data/application.xml";
		URL_SAVE_APPLICATION = "data/_success.xml";
		URL_RESOURCE_TYPE = "data/resourcetype.xml";
		URL_IMPORT_TEMPLATE = "data/importapplication.xml";
		URL_IMPORT = "data/_success.xml";
	}
  
    function init() {
        initPaletteResize();
        initListContainerResize();
        initNaviBar("um.2");
        initMenus();
        initBlocks();
        initWorkSpace();

        loadInitData();
    }
  
    function initMenus() {
	    var item1 = {
            label:"查看",
            callback:function() {
                editTreeNode(false);
            },
            icon:ICON + "view.gif",
            enable:function() {return true;},
            visible:function() {return "-1" != getTreeNodeId() && "-2" != getTreeNodeId();}
        }
        var item2 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            enable:function() {return true;},
            visible:function() {return checkTreeNodeEditable();}
        }
		var item3 = {
            label:"新建应用",
            callback:createOtherApplication,
            enable:function() {return true;},
            visible:function() {return "-2" == getTreeNodeId();}
        }
        var item4 = {
            label:"导入",
            callback:importApplication,
            icon:ICON + "import.gif",
            enable:function() {return true;},
            visible:function() {return "1"==getNodeType() && ("-1"==getTreeNodeId() || "-2"==getTreeNodeId());}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
		menu1.addItem(item3)
        menu1.addItem(item4);
 
        $$("tree").contextmenu = menu1;
    }
 
    function initBlocks() {
        var paletteObj = $$("palette");
        Blocks.create(paletteObj);

        var treeContainerObj = $$("treeContainer");
        Blocks.create(treeContainerObj, treeContainerObj.parentNode);   
    }

	function loadInitData() {
        var p = new HttpRequestParams();
        p.url = URL_INIT;

        var request = new HttpRequest(p);
        request.onresult = function() {
            var mainTreeNode = this.getNodeValue(XML_MAIN_TREE);

			var tree = $T("tree", mainTreeNode);
			var treeElement = $$("tree");

            treeElement.onTreeNodeDoubleClick = function(eventObj) {
                var nodeType = getNodeType();
				var editable = checkTreeNodeEditable();
		 
				switch(nodeType) {
					case "1":
						editApplication(editable);          
						break;
					case "2":
						viewResourceType();
						break;
				}
            }
            treeElement.onTreeNodeRightClick = function(eventObj) {
                var treeObj = $$("tree");
				if( treeObj.contextmenu ) {
					treeObj.contextmenu.show(eventObj.clientX, eventObj.clientY);                
				}
            }
        }
        request.send();
    }
 
	function createOtherApplication() {
		editApplication(true, true);
	}
 
    function editApplication(editable, isNew) {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();
		var parentId = treeNode.getParent().getId();

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadAppDetailData(treeID, editable, isNew ? parentId : null);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		if( editable ) {
			inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
			inf.SID = CACHE_APPLICATION_DETAIL + treeID;
		} else {
			inf.label = OPERATION_VIEW.replace(/\$label/i, treeName);
			inf.SID = CACHE_VIEW_APPLICATION_DETAIL + treeID;
		}
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadAppDetailData(cacheID, editable, parentID) {
		var p = new HttpRequestParams();
		p.url = URL_APPLICATION_DETAIL;
		p.setContent("appId", cacheID);            

		var request = new HttpRequest(p);
		request.onresult = function() {
			var appInfoNode = this.getNodeValue(XML_APPLICATION_DETAIL);
			Cache.XmlDatas.add(cacheID, appInfoNode);

			var xform = $X("page1Form", appInfoNode);
			xform.editable = editable == false ? "false" : "true";
			
			// 设置保存按钮操作
			var page1BtSaveObj = $$("page1BtSave");
			page1BtSaveObj.disabled = editable==false?true:false;
			page1BtSaveObj.onclick = function() {
				saveApp(cacheID, parentID);
			}
		}
		request.send();           
    }
 
    function saveApp(cacheID, parentID) {
        var p = new HttpRequestParams();
        p.url = URL_SAVE_APPLICATION;

        // 是否提交
        var flag = false;

        // 应用基本信息
        var appInfoNode = Cache.XmlDatas.get(cacheID);
        if( appInfoNode ) {
            var appInfoDataNode = appInfoNode.selectSingleNode(".//data");
            if( appInfoDataNode ) {
                flag = true;
                p.setXFormContent(appInfoDataNode);
            }
        }

        if( flag ) {
            var request = new HttpRequest(p);
            //同步按钮状态
            syncButton([$$("page1BtSave")], request);

            request.onresult = function() { // 新增
				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentID, treeNode);

				ws.closeActiveTab();
            }
            request.onsuccess = function() { // 修改，更新树节点名称
				var name = $X("page1Form").getData("name");
				modifyTreeNode(cacheID, "name", name, true);
            }
            request.send();
        }
    }
 
    function viewResourceType() {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadTypeData(treeID);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_VIEW.replace(/\$label/i,treeName);
		inf.SID = CACHE_VIEW_SOURCE_TYPE + treeID;
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadTypeData(treeID) { 
		var p = new HttpRequestParams();
		p.url = URL_RESOURCE_TYPE;
		p.setContent("typeId", treeID);            

		var request = new HttpRequest(p);
		request.onresult = function() {
			var typeInfoNode = this.getNodeValue(XML_SOURCE_TYPE_INFO);

			var xform = $X("page1Form", typeInfoNode);
			xform.editable = "false";
			
			// 设置保存按钮操作
			$$("page1BtSave").disabled = true;
		}
		request.send();
    }
 
    function getNodeType() {
        return getTreeAttribute("nodeType");
    }
 
    function getApplicationId() {
        return getTreeAttribute("applicationId");
    }
	
    /* 检测树节点是否可编辑 */
    function checkTreeNodeEditable() {
        var flag = false;
        switch(getNodeType()) {
            case "1":
                if("-1" != getTreeNodeId() && "-2" != getTreeNodeId() && "tss" != getApplicationId()) {
                    flag = true;
                }
                break;
            case "2":
            case "3":
				flag = false;
                break;
        }
        return flag;   
    }
 
    /*
     *	编辑树节点
     */
    function editTreeNode(editable) {
        switch(getNodeType()) {
            case "1":
                editApplication(editable);
                break;
            case "2":
                viewResourceType(editable);
                break;
        }
    }
 
    function importApplication() {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeID   = treeNode.getId();
		var treeName = treeNode.getName();
		var applicationType = treeNode.getAttribute("applicationType");

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadImportDetailData(treeID, applicationType);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_IMPORT.replace(/\$label/i, treeName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_IMPORT_APPLICATION + treeID;
		var tab = ws.open(inf);
    }
    
     /*
     *	导入详细信息加载数据
     *	参数：	string:treeID               树节点id
                string:applicationType      应用类型
     *	返回值：
     */
    function loadImportDetailData(treeID, applicationType) {
		var p = new HttpRequestParams();
		p.url = URL_IMPORT_TEMPLATE;
		p.setContent("id", treeID);
		p.setContent("applicationType",applicationType);

		var request = new HttpRequest(p);
		request.onresult = function() {
			var importInfoNode = this.getNodeValue(XML_IMPORT_APPLICATION);

			importInfoNode.setAttribute("action", URL_IMPORT);//action地址
			var frameName = createUploadFrame();
			importInfoNode.setAttribute("target", frameName);//提交iframe名
			importInfoNode.setAttribute("enctype", "multipart/form-data");
			importInfoNode.setAttribute("method", "POST");

			var importInfoNodeID = cacheID + "." + XML_IMPORT_APPLICATION;

			initImportPages(cacheID,applicationType);
		}
		request.send();

		//设置保存按钮操作
        var page1BtSaveObj = $$("page1BtSave");
        page1BtSaveObj.disabled = false;
        page1BtSaveObj.onclick = function() {
            saveImport();
        }
    }
 
    function saveImport() {
        var page1FormObj = $$("page1Form");	
        var fileName = page1FormObj.getData("filePath");
        if (fileName == null || fileName == "") {
            return alert("请选择导入文件!");
        }
        else {
            var fileLength = fileName.length;
			var subfix = fileName.substring(fileLength-4, fileLength);
            if( subfix != ".xml" ) {
                return alert("请选择.xml文件导入!");
            }
            else{
                return page1FormObj.submit();
            }
        }
    }
 
    function getFilePath(path) {
        var page1FormObj = $$("page1Form");
        page1FormObj.updateDataExternal("filePath", path);
    }
	

    window.onload = init;

	//关闭页面自动注销
    logoutOnClose();