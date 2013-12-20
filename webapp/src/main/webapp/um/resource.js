	/* 后台响应数据节点名称 */
    XML_MAIN_TREE = "AppSource";
    XML_APPLICATION_DETAIL = "AppDetail";
    XML_SOURCE_TYPE_INFO = "ResourceTypeDetail";

	/* XMLHTTP请求地址汇总 */
    URL_INIT		  = AUTH_PATH + "resource/apps";
    URL_APP_DETAIL    = AUTH_PATH + "resource/app/";
    URL_SAVE_APP      = AUTH_PATH + "resource/app";  // POST
    URL_RESOURCE_TYPE = AUTH_PATH + "resource/resourceType/";
	
	if(IS_TEST) {
		URL_INIT          = "data/resource_tree.xml?";
		URL_APP_DETAIL    = "data/application.xml?";
		URL_SAVE_APP      = "data/_success.xml?";
		URL_RESOURCE_TYPE = "data/resourcetype.xml?";
	}
  
    function init() {
        initPaletteResize();
        initNaviBar("um.2");
        initMenus();

        loadInitData();
    } 
  
    function initMenus() {
	    var item1 = {
            label:"查看",
            callback:function() { editTreeNode(false); },
            icon:ICON + "view.gif",
            visible: function() {return "-1" != getTreeNodeId() && "-2" != getTreeNodeId();}
        }
        var item2 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            visible: function() {return checkTreeNodeEditable();}
        }
		var item3 = {
            label:"新建应用",
            callback: function() { getOtherApplication(true); },
            visible: function() {return "-2" == getTreeNodeId();}
        }
        var item4 = {
            label:"导入",
            callback: importApplication,
            icon:ICON + "import.gif",
            visible: function() {return "1" == getNodeType() && "-1" == getTreeNodeId() ;}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
		menu1.addItem(item3)
        menu1.addItem(item4);
 
        $$("tree").contextmenu = menu1;
    }

	function loadInitData() {
		Ajax({
			url : URL_INIT,
			onresult : function() { 
				var mainTreeNode = this.getNodeValue(XML_MAIN_TREE);
				var tree = $T("tree", mainTreeNode);
				tree.element.onTreeNodeDoubleClick = function(eventObj) {
					var editable = checkTreeNodeEditable();
					editTreeNode(editable);
				}
				tree.element.onTreeNodeRightClick = function(eventObj) {
					onTreeNodeRightClick(eventObj);
				}
			}
		});	
    }
 
    // 平台应用不能编辑
    function getOtherApplication(isCreate) {
		var treeID  = isCreate ? DEFAULT_NEW_ID : getTreeNodeId();
       
		Ajax({
			url : URL_APP_DETAIL + treeID,
			method : "GET",
			onresult : function() { 
				var appDetailNode = this.getNodeValue(XML_APPLICATION_DETAIL);
				Cache.XmlDatas.add(treeID, appDetailNode);

				var xform = $X("page1Form", appDetailNode);
				
				// 设置保存按钮操作
				$$("appSaveBt").onclick = function() {
					saveApp(treeID);
				}
			}
		});	
    }
 
    function saveApp(treeID) {
        var p = new HttpRequestParams();
        p.url = URL_SAVE_APP;

        // 是否提交
        var flag = false;

        // 应用基本信息
        var appInfoNode = Cache.XmlDatas.get(treeID);
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
            syncButton([$$("appSaveBt")], request);

            request.onresult = function() { // 新增
				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode("-2", treeNode);
            }
            request.onsuccess = function() { // 修改，更新树节点名称
				var name = $X("page1Form").getData("name");
				modifyTreeNode(treeID, "name", name, true);
            }
            request.send();
        }
    }
 
    function viewResourceType() {
		Ajax({
			url : URL_RESOURCE_TYPE + getTreeNodeId(),
			method : "GET",
			onresult : function() {
				var typeInfoNode = this.getNodeValue(XML_SOURCE_TYPE_INFO);

				var xform = $X("page1Form", typeInfoNode);
				xform.editable = "false";
			}
		});	
    }
 
    function getNodeType() {
        return getTreeAttribute("nodeType");
    }

    function getApplicationType() {
        return getTreeAttribute("applicationType");
    }
	
    /* 检测树节点是否可编辑 */
    function checkTreeNodeEditable() {
        var flag = false;
        switch(getNodeType()) {
            case "1":
                if(getApplicationType() == ""-2 &&  getTreeNodeId() != "-2") {
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
 
    function editTreeNode(editable) {
        switch(getNodeType()) {
            case "1":
            	if(editable) {
            		getOtherApplication(false);
            	}
                break;
            case "2":
                viewResourceType();
                break;
        }
    }
 
    function importApplication() {
		function checkFileWrong(subfix) {
			return subfix != ".xml";
		}

		var url = URL_UPLOAD_FILE + "?afterUploadClass=com.jinhe.tss.um.servlet.ImportAppConfig";
		var importDiv = createImportDiv("只支持XML文件格式导入", checkFileWrong, url);
		Element.show(importDiv);
	}

    window.onload = init;