
    /*
     *	后台响应数据节点名称
     */
    XML_GROUP_INFO = "GroupInfo";


    /*
     *	默认唯一编号名前缀
     */
    CACHE_TREE_NODE_GROUP_DETAIL = "treeNodeGroup__id";
 
	 /*
     *	XMLHTTP请求地址汇总
     */
	URL_SOURCE_TREE      = "data/source_tree.xml";
    URL_SAVE_GROUP       = "data/_success.xml";
    URL_SOURCE_SORT      = "data/_success.xml";
    URL_SOURCE_DETAIL    = "data/component_detail.xml";
    URL_DELETE_NODE      = "data/_success.xml";
    URL_SOURCE_DISABLE   = "data/_success.xml";
    URL_SOURCE_SAVE      = "data/_success.xml";
    URL_IMPORT_COMPONENT = "data/_success.xml";
    URL_EXPORT_COMPONENT = "data/_success.xml";
 
    function init() {
        initPaletteResize();
        initUserInfo();
        initToolBar();
        initNaviBar("portal.3");
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }
 
    function loadInitData() {
        var p = new HttpRequestParams();
        p.url = URL_SOURCE_TREE;

        var request = new HttpRequest(p);
        request.onresult = function() {
            var _operation = this.getNodeValue(XML_OPERATION);

            var groupTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var groupTreeNodeID = CACHE_MAIN_TREE;

            Cache.XmlDatas.add(groupTreeNodeID, groupTreeNode);
 
            var treeObj = $T("tree", groupTreeNode);

            treeObj.onTreeNodeActived = function(eventObj) {
				Focus.focus($$("treeTitle").firstChild.id);
				showTreeNodeInfo();
            }
            treeObj.onTreeNodeDoubleClick = function(eventObj) {
                var id = getTreeNodeId();
				if("_rootId" != id && !isGroup()) {
					 editComponentInfo();            
				}
            }
            treeObj.onTreeNodeRightClick = function(eventObj) {
                showTreeNodeInfo();
				$T("tree").contextmenu.show(eventObj.clientX, eventObj.clientY);
            }
            treeObj.onTreeNodeMoved = function(eventObj) {
                sortTreeNode(URL_SOURCE_SORT, eventObj);
            }
        }
        request.send();
    }
 
    function initMenus() {
        var item1 = {
            label:"新建",
            callback:addNewComponent,
            enable:function() {return true;},
            visible:function() {return null!=getTreeNodeType() && true==getOperation("pd1");}
        }
        var item2 = {
            label:"删除",
            callback:delTreeNode,
            icon:ICON + "del.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId"!=getTreeNodeId() && true==getOperation("pd2");}
        }
        var item3 = {
            label:"编辑",
            callback:editTreeNode,
            icon:ICON + "edit.gif",
            enable:function() {return true;},
            visible:function() {return "_rootId"!=getTreeNodeId() && true==getOperation("pd3");}
        }
        var item4 = {
            label:"启用",
            callback: function() { stopOrStartTreeNode("0"); },
            icon:ICON + "start.gif",
            enable:function() {return true;},
            visible:function() {return null!=getGroupId() && isTreeNodeDisabled() && true==getOperation("pd4");}
        }
        var item5 = {
            label:"停用",
            callback: function() { stopOrStartTreeNode("1"); },
            icon:ICON + "stop.gif",
            enable:function() {return true;},
            visible:function() {return null!=getGroupId() && !isTreeNodeDisabled() && true==getOperation("pd4");}
        }
        var item6 = {
            label:"新建组",
            callback:function() {
                addNewGroup("2");
            },
            enable:function() {return true;},
            visible:function() {return (null!=getTreeNodeType() || "_rootId"==getTreeNodeId()) && true==getOperation("pd5");}
        }
        var item7 = {
            label:"导出",
            callback:exportComponent,
            icon:ICON + "export.gif",
            enable:function() {return true;},
            visible:function() {return null!=getGroupId() && true==getOperation("pd6");}
        }
        var item8 = {
            label:"导入",
            callback:importComponent,
            icon:ICON + "import.gif",
            enable:function() {return true;},
            visible:function() {return null!=getTreeNodeType() && true==getOperation("pd7");}
        }
        var item14 = {
            label:"资源管理",
            callback:function() {resourceManage();},
            icon:ICON + "resource.gif",
            enable:function() {return true;},
            visible:function() {return null!=getGroupId() && true==getOperation("pd3");}
        }
		var item15 = {
            label:"预览",
            callback:function() {previewElement(2);},
            icon:ICON + "preview.gif",
            enable:function() {return true;},
            visible:function() {return null!=getGroupId() && true==getOperation("2");}
        }
        var treeObj = $T("tree");

        var menu1 = new Menu();
		menu1.addItem(item15);
        menu1.addItem(item4);
        menu1.addItem(item5);
        menu1.addSeparator();
        menu1.addItem(item10);
        menu1.addSeparator();
        menu1.addItem(item13);
        menu1.addItem(item3);
        menu1.addItem(item2);
        menu1.addItem(item9);
        menu1.addItem(item11);
        menu1.addItem(item12);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item6);
        menu1.addSeparator();
        menu1.addItem(item7);
        menu1.addItem(item8);
        menu1.addItem(item14);
        treeObj.contextmenu = menu1;
    }

    function getComponentType() {
        return getTreeAttribute("type");
    }

	function isComponentGroup() {
		return getTreeAttribute("isGroup") == "true";
	}

	function getGroupId() {
        return getTreeAttribute("groupId");
    }

	function addNewComponent() {
        var treeName = "组件";
        var treeID = DEFAULT_NEW_ID;

        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var parentID = treeNode.getId();

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadTreeDetailData(treeID, parentID);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, treeName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
		var tab = ws.open(inf);
    }

    function editComponentInfo() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadTreeDetailData(treeID, getGroupId());
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }

    function loadTreeDetailData(treeID, parentID) {
		var p = new HttpRequestParams();
		p.url = URL_SOURCE_DETAIL +　treeID + "/" + parentID;

		var request = new HttpRequest(p);
		request.onresult = function() {
			var componentInfoNode = this.getNodeValue(XML_Component_INFO);

			preProcessXml(componentInfoNode);

			Cache.XmlDatas.add(treeID, componentInfoNode);

			var page1FormObj = $X("page1Form", componentInfoNode);
			attachReminder(treeID, page1FormObj);

			//设置保存按钮操作
			$$("page1BtSave").onclick = function() {
				saveComponent(treeID, parentID);
			}
		}
		request.send();
    }

	/*
     *	预先处理xform数据岛.
	 *  预解析definition，分别设置到script,style,prototypeStyle,html,events和parameters上
     */
    function preProcessXml(dataNode) {
        var rowNode = dataNode.selectSingleNode(".//data/row");
        var definition = rowNode.getCDATA("definition") || "";

        var xmlReader = new XmlReader(definition);
        if(xmlReader.documentElement) {
            var definitionNode = new XmlNode(xmlReader.documentElement);
            var scriptNode = definitionNode.selectSingleNode("./script/node()");
            var styleNode = definitionNode.selectSingleNode("./style/node()");
            var prototypeStyleNode = definitionNode.selectSingleNode("./prototypeStyle/node()");
            var htmlNode = definitionNode.selectSingleNode("./html/node()");
            var eventsNode = definitionNode.selectNodes("./events/*");
            var parametersNode = definitionNode.selectNodes("./parameters/*");

            if(scriptNode) {
                rowNode.setCDATA("script", scriptNode.nodeValue);
            }
            if(styleNode) {
                rowNode.setCDATA("style", styleNode.nodeValue);
            }
            if(prototypeStyleNode) {
                rowNode.setCDATA("prototypeStyle", prototypeStyleNode.nodeValue);
            }
            if(htmlNode) {
                rowNode.setCDATA("html", htmlNode.nodeValue);
            }
            if(eventsNode) {
                var events = [];
                for(var i=0; i < eventsNode.length; i++) {
                    var curNode = eventsNode[i];
                    events[i] = curNode.getAttribute("event") + "=" + curNode.getAttribute("onevent");
                }
                rowNode.setCDATA("events", events.join("\r\n"));
            }
            if(parametersNode) {
                var parameters = [];
                for(var i=0;i < parametersNode.length; i++) {
                    var curNode = parametersNode[i];
                    parameters[i] = curNode.getAttribute("name") + "=" + curNode.getAttribute("defaultValue");
                }
                rowNode.setCDATA("parameters", parameters.join("\r\n"));
            }
        }
    }
 
    /*
     *	保存修饰
     *	参数：	string:cacheID      缓存数据id
                string:parentID     父节点id
     *	返回值：
     */
    function saveComponent(cacheID, parentID) {
        var page1FormObj = $("page1Form");
        if( !page1FormObj.checkForm()) {
            return;
        }

		var events     = page1FormObj.getData("events");
		if( true == (/^\=/.test(events)) ) {
			page1FormObj.showCustomErrorInfo("events", "请按\"事件名=操作\"格式书写");
			return;
		} 

		var parameters = page1FormObj.getData("parameters");
		if( true==(/^\=/.test(parameters)) ) {
			page1FormObj.showCustomErrorInfo("parameters", "请按\"参数名=参数值\"格式书写");
			return;
		}

        var p = new HttpRequestParams();
        p.url = URL_SOURCE_SAVE;

        //是否提交
        var flag = false;
 
		//修饰基本信息
		var componentInfoNode = Cache.XmlDatas.get(cacheID);
		if(componentInfoNode) {
			var componentInfoDataNode = componentInfoNode.selectSingleNode(".//data");
			if(ComponentInfoDataNode) {
				componentInfoDataNode = componentInfoDataNode.cloneNode(true);

				var rowNode = componentInfoDataNode.selectSingleNode("row");

				// 拼接definition属性
				var name           = rowNode.getCDATA("name") || "";
				var version        = rowNode.getCDATA("version") || "";
				var script         = rowNode.getCDATA("script") || "";
				var style          = rowNode.getCDATA("style") || "";
				var prototypeStyle = rowNode.getCDATA("prototypeStyle") || "";
				var html           = rowNode.getCDATA("html") || "";
				var events         = rowNode.getCDATA("events") || "";
				var parameters     = rowNode.getCDATA("parameters") || "";
				var description    = rowNode.getCDATA("description") || "";
				var rootName       = "Component";

				var str = [];
				str[str.length] = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
				str[str.length] = "<" + rootName + ">";
				str[str.length] = "<property>";
				str[str.length] = "<name>" + name.convertEntity() + "</name>";
				str[str.length] = "<version>" + version.convertEntity() + "</version>";
				str[str.length] = "<description>";
				str[str.length] = "<![CDATA[" + description + "]]>";
				str[str.length] = "</description>";
				str[str.length] = "</property>";
				str[str.length] = "<script>";
				str[str.length] = "<![CDATA[" + script + "]]>";
				str[str.length] = "</"+"script>";
				str[str.length] = "<style>";
				str[str.length] = "<![CDATA[" + style + "]]>";
				str[str.length] = "</style>";
				str[str.length] = "<prototypeStyle>";
				str[str.length] = "<![CDATA[" + prototypeStyle + "]]>";
				str[str.length] = "</prototypeStyle>";
				str[str.length] = "<html>";
				str[str.length] = "<![CDATA[" + html + "]]>";
				str[str.length] = "</html>";

				str[str.length] = "<events>";
				events = events.split("\n");
				for(var i =0,iLen=events.length;i<iLen;i++) {
					var curEvent = events[i].replace(/(^\s*)|(\s*$)/g,"");//去掉每行前后多余空格
					curEvent = curEvent.convertEntity();//转换特殊字符为实体
					var curEventName = curEvent.substring(0,curEvent.indexOf("="));
					var curEventValue = curEvent.substring(curEvent.indexOf("=")+1);
					str[str.length] = "<attach event=\""+curEventName+"\" onevent=\""+curEventValue+"\"/>";
				}
				str[str.length] = "</events>";

				str[str.length] = "<parameters>";
				parameters = parameters.split("\n");
				for(var i =0,iLen=parameters.length;i<iLen;i++) {
					var curParam = parameters[i].replace(/(^\s*)|(\s*$)/g,"");//去掉每行前后多余空格
					curParam = curParam.convertEntity();//转换特殊字符为实体
					var curParamName = curParam.substring(0,curParam.indexOf("="));
					var curParamValue = curParam.substring(curParam.indexOf("=")+1);
					str[str.length] = "<param name=\""+curParamName+"\" defaultValue=\""+curParamValue+"\"/>";
				}
				str[str.length] = "</parameters>";
				str[str.length] = "</" + rootName + ">";

				rowNode.setCDATA("definition", str.join(""));
				rowNode.removeCDATA("script");
				rowNode.removeCDATA("style");
				rowNode.removeCDATA("prototypeStyle");
				rowNode.removeCDATA("html");
				rowNode.removeCDATA("events");
				rowNode.removeCDATA("parameters");

				flag = true;
				p.setXFormContent(componentInfoDataNode);
			}
		}

        if(flag) {
            var request = new HttpRequest(p);
            
			//同步按钮状态
            var page1BtSaveObj = $$("page1BtSave");
            syncButton([page1BtSaveObj], request);

            request.onresult = function() {
				// 解除提醒
				detachReminder(cacheID);

				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentID,treeNode);

				ws.closeActiveTab();
            }
            request.onsuccess = function() {
				// 解除提醒
				detachReminder(cacheID);

				//更新树节点名称
				var name = page1FormObj.getData("name");
				modifyTreeNode(cacheID, "name", name, true);
            }
            request.send();
        }
    }
 
    function renameGroup() {
		var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var id = treeNode.getId();

        var groupName = prompt("请输入组名称");
	}
 
	function addNewGroup() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var parentID = treeNode.getId();

        var groupName = prompt("请输入组名称");
    }
	
    /*
     *	预览组件
     */
	function previewElement() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var id = treeNode.getId();
		var url	= "elementGroup!previewElement.action?id=" + id;
		window.open(url);
    }

	/*
     *	更改布局器、组件、portlet的参数配置文件
     *	参数：	int:type       类型(1:布局器、2:组件、3:portlet)
     */
    function editParamsTemplate(type) {
        var page1FormObj = $X("page1Form");
        var id         = page1FormObj.getData("id") ;
		var name       = page1FormObj.getData("name") || "";
        var parameters = page1FormObj.getData("parameters") || "";

        if(id) {
            window.showModalDialog("configparams_edit.htm", {id:id,params:parameters,type:type,title:"配置参数模板"},"dialogWidth:700px;dialogHeight:480px;");
        } else {
			alert("请先保存组件后再配置其参数模板");
		}
    }

    function upload() {
        var page1FormObj = $("page1Form");
        var fileName = page1FormObj.getData("filePath");
        if (fileName==null || fileName=="") {
            return alert("请选择导入文件!");
        }
        else {
            var fileLength = fileName.length;
            if(fileName.substring(fileLength-4,fileLength)!=".zip" && fileName.substring(fileLength-4,fileLength)!=".xml") {
                return alert("请选择zip压缩文件或者XML文件导入!");
            }
            else{
                return page1FormObj.submit();
            }
        }
    }

	function importComponent() {
        var treeName = "组件";

        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();

		var treeID;
		var id = treeNode.getAttribute("id");
		var groupId = treeNode.getAttribute("groupId");

		if(null == groupId) {
			treeID = id;
		}else{
			treeID = groupId;
		}

		var callback = {};
		callback.onTabClose = function(eventObj) {
			delCacheData(eventObj.tab.SID);
		};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadUploadDetailData(treeID);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_IMPORT.replace(/\$label/i,treeName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_UPLOAD_DETAIL + treeID;
		var tab = ws.open(inf);
    }

    function exportComponent() {
        var frameName = createExportFrame();
        var frameObj = window.frames[frameName];

        frameObj.location.href = URL_EXPORT_COMPONENT + getTreeNodeId();
    }

    /*
     *	创建导出用iframe
     */
    function createExportFrame() {
        var frameName = "exportFrame";
        var frameObj = $$(frameName);
        if( frameObj == null ) {
            frameObj = document.createElement("<iframe name='" + frameName + "' id='" + frameName + "' src='about:blank' style='display:none'></iframe>");
            document.body.appendChild(frameObj);
        }
        return frameName;
    }
    
    /*
     *	组件资源管理
     */
    function resourceManage() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var name = treeNode.getName();
		var code = treeNode.getAttribute("code");

		var params = {
			code:code,
			type:getComponentType()
		};
	
		window.showModalDialog("filemanager.html", {params:params, title:"\"" + name + "\"相关资源管理"},"dialogWidth:400px;dialogHeight:400px;");
    }
	 
	window.onload = init;

	// 关闭页面自动注销
    logoutOnClose();