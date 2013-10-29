    var _TEMP_CODE = new Date().getTime();
    
	/*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "SourceTree";
    XML_SITE_INFO = "DetailInfo";
    XML_PREVIEW = "html";
    XML_COMPONENT_PARAMETERS = "ComponentParams";
    XML_THEME_MANAGE = "ThemeList";
    XML_CACHE_MANAGE = "CacheManage";
 
    /*
     *	默认唯一编号名前缀
     */
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_THEME_MANAGE = "themeManage__id";
    CACHE_CACHE_MANAGE = "cacheManage__id";
 
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_SOURCE_TREE   = AUTH_PATH + "portal/list";
    URL_SOURCE_DETAIL = AUTH_PATH + "portal/";
    URL_SOURCE_SAVE   = AUTH_PATH + "portal";
    URL_DELETE_NODE   = AUTH_PATH + "portal/";
    URL_STOP_NODE     = AUTH_PATH + "portal/disable/";
    URL_SORT_NODE     = AUTH_PATH + "portal/sort/";
    URL_VIEW_SITE     = AUTH_PATH + "portal/preview/";
    URL_GET_COMPONENT_PARAMS =  AUTH_PATH + "component/params/";     // {id}
	URL_GET_COMPONENT_TREE =  AUTH_PATH + "component/enabledlist/";  // {type}
    URL_THEME_MANAGE      = AUTH_PATH + "portal/theme/list/";
    URL_RENAME_THEME      = AUTH_PATH + "portal/theme/rename/"; // {themeId}/{name}  PUT
    URL_DEL_THEME         = AUTH_PATH + "portal/theme/";
    URL_COPY_THEME        = AUTH_PATH + "portal/theme/";  // {themeId}/{name} POST
    URL_PREVIEW_THEME     = AUTH_PATH + "portal/preview/"; // {portalId} ? themeId=?
    URL_SET_DEFAULT_THEME = AUTH_PATH + "portal/theme/default/";  // {themeId}
    URL_GET_OPERATION     = AUTH_PATH + "portal/operations/";
    URL_FLUSH_CACHE       = AUTH_PATH + "portal/cache/";
    URL_CACHE_MANAGE      = AUTH_PATH + "portal/cache/";
    URL_GET_FLOW_RATE     = AUTH_PATH + "portal/flowrate/";

	URL_SYNC_PROGRESS        = AUTH_PATH + "portal/release/progress/";  // GET
    URL_CANCEL_SYNC_PROGRESS = AUTH_PATH + "portal/release/progress/";  // DELETE
	URL_STATIC_ISSUE_PORATL  = AUTH_PATH + "portal/release/";
	URL_STATIC_ISSUE_PAGE    = AUTH_PATH + "portal/release/singlepage";
	URL_REMOTE_ISSUE         = AUTH_PATH + "portal/release/remote/";
 
	if(IS_TEST) {
		URL_SOURCE_TREE   = "data/structure_tree.xml?";
		URL_SOURCE_DETAIL = "data/structure_detail.xml?";
		URL_SOURCE_SAVE   = "data/_success.xml?";
		URL_DELETE_NODE   = "data/_success.xml?";
		URL_STOP_NODE     = "data/_success.xml?";
		URL_SORT_NODE     = "data/_success.xml?";
		URL_VIEW_SITE     = "portal!previewPortal.action";
		URL_GET_COMPONENT_PARAMS = "data/structure-params.xml?";
		URL_GET_COMPONENT_TREE = "data/component_tree.xml?";
		URL_THEME_MANAGE      = "data/theme_list.xml?";
		URL_RENAME_THEME      = "data/_success.xml?";
		URL_DEL_THEME         = "data/_success.xml?";
		URL_COPY_THEME        = "data/theme_copy.xml?";
		URL_PREVIEW_THEME     = "data/_success.xml?";
		URL_SET_DEFAULT_THEME = "data/_success.xml?";
		URL_GET_OPERATION     = "data/operation.xml?";
		URL_FLUSH_CACHE       = "data/_success.xml?";
		URL_CACHE_MANAGE      = "data/cachemanage.xml?";
		URL_GET_FLOW_RATE     = "data/page_flow_rate.xml?";

		URL_SYNC_PROGRESS        = "data/progress.xml?";
		URL_CANCEL_SYNC_PROGRESS = "data/_success.xml?";
		URL_STATIC_ISSUE_PORATL  = "data/_success.xml?";
		URL_STATIC_ISSUE_PAGE    = "data/_success.xml?";
		URL_REMOTE_ISSUE         = "data/_success.xml?";
	}

    function init() {
        initPaletteResize();
        initNaviBar("portal.1");
        initMenus();
        initBlocks();
        initWorkSpace(false);
        initEvents();
        initFocus();

        loadInitData();
    }
	 
    function getStructureType() {
        return getTreeAttribute("type");
    }

	function isPortalNode() {
		return getStructureType() == "0";
	}
	function isPageNode() {
		return getStructureType() == "1";
	}
	function isSectionNode() {
		return getStructureType() == "2";
	}
	function isPortletNode() {
		return getStructureType() == "3";
	}

    function initMenus() {
        var item1 = {
            label:"新建门户",
            callback:function() { 
				addNewStructure("0"); 
			},
            visible:function() {return isTreeRoot() && getOperation("4");}
        }
        var item2 = {
            label:"新建页面",
            callback:function() {
				addNewStructure("1");
			},
            visible:function() {return isPortalNode() && getOperation("4");}
        }
        var item3 = {
            label:"新建版面",
            callback:function() {
				addNewStructure("2");
			},
            visible:function() {return (isPageNode() || isSectionNode()) && getOperation("4");}
        }
        var item4 = {
            label:"新建portlet实例",
            callback:function() {
				addNewStructure("3");
			},
            visible:function() {return (isPageNode() || isSectionNode()) && getOperation("4");}
        }
        var item5 = {
            label:"删除",
            callback:function() { delTreeNode() },
            icon:ICON + "icon_del.gif",
            visible:function() {return !isTreeRoot() && getOperation("3");}
        }
        var item6 = {
            label:"编辑",
            callback:editStructure,
            icon:ICON + "edit.gif",
            visible:function() {return !isTreeRoot() && getOperation("2");}
        }
        var item7 = {
            label:"停用",
            callback: function() { stopOrStartTreeNode("1"); },
            icon:ICON + "stop.gif",
            visible:function() {return !isTreeRoot() && !isTreeNodeDisabled() && getOperation("6");}
        }
        var item8 = {
            label:"启用",
            callback: function() { stopOrStartTreeNode("0"); },
            icon:ICON + "start.gif",
            visible:function() {return !isTreeRoot() && isTreeNodeDisabled() && getOperation("7");}
        }
        var item9 = {
            label:"预览",
            callback:preview,
            icon:ICON + "preview.gif",
            visible:function() {return !isTreeRoot()  && getOperation("1");}
        }
        var item10 = {
            label:"主题管理",
            callback:themeManage,
            icon:ICON + "portal/theme.gif",
            visible:function() {return isPortalNode() && getOperation("2");}
        }
        var item11 = {
            label:"缓存管理",
            callback:cacheManage,
            icon:ICON + "cache.gif",
            visible:function() {return isPortalNode() && getOperation("2");}
        }
		var item17 = {
            label:"资源管理",
            callback:function() {resourceManage();},
            visible:function() {return isPortalNode() && getOperation("2");}
        }
        var item12 = {
            label:"查看页面流量",
            callback:showPageFlowRate,
            visible:function() {return isPortalNode();}
        }
		var item13 = {
            label:"门户静态发布",
            callback:staticRelease,
            visible:function() {return isPortalNode() && getOperation("2");}
        }
		var item14 = {
            label:"远程发布",
            callback:function() {
               remoteRelease("false");
            },
			icon:ICON + "up.gif",
            visible:function() {return isPortalNode() && getOperation("2");}
        }
		var item15 = {
            label:"远程发布(完全覆盖)",
			callback:function() {
                remoteRelease("true");
            },            
			icon:ICON + "up.gif",
            visible:function() {return isPortalNode() && getOperation("2");}
        }
	    var item16 = {
            label:"页面静态发布",
            callback:staticReleaseSinglePage,
            visible:function() {return isPortalNode() && getOperation("2");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
		menu1.addItem(item4);
        menu1.addItem(item5);
        menu1.addItem(item6);
		menu1.addItem(item7);
        menu1.addItem(item8);
		menu1.addItem(item17);
        menu1.addSeparator();
        menu1.addItem(item9);
		menu1.addItem(item12);
		menu1.addItem(item10);
		menu1.addItem(item11);
        menu1.addSeparator();
		menu1.addItem(item13);
		menu1.addItem(item16);
		menu1.addItem(item14);
		menu1.addItem(item15);
		       
        $$("tree").contextmenu = menu1;
    }

	function loadInitData() {
        var onresult = function() {
            var sourceTreeNode = this.getNodeValue(XML_MAIN_TREE);
            var tree = $T("tree", sourceTreeNode);

            tree.element.onTreeNodeActived = function(eventObj) {
                onTreeNodeActived(eventObj);
            }
            tree.element.onTreeNodeDoubleClick = function(eventObj) {
                onTreeNodeDoubleClick(eventObj);
            }
            tree.element.onTreeNodeMoved = function(eventObj) {
				sortTreeNode(URL_SORT_NODE, eventObj);
            }
            tree.element.onTreeNodeRightClick = function(eventObj) {
                 onTreeNodeRightClick(eventObj, true);
            }
        }

		Ajax({url: URL_SOURCE_TREE, onresult: onresult});
    }
  
    function onTreeNodeDoubleClick(eventObj) {
        var treeNode = eventObj.treeNode;
        var id = getTreeNodeId();
        getTreeOperation(treeNode, function(_operation) {
            var canEdit = checkOperation("2", _operation);
            if("_rootId" != id && canEdit) {
                editStructure();
            }
        });
    }

	function addNewStructure(treeType) {
        var treeID = DEFAULT_NEW_ID;
		var treeName;
        switch(treeType) {
            case "0":
              treeName = "门户";
              break;
            case "1":
              treeName = "页面";
              break;
            case "2":
              treeName = "版面";
              break;
            case "3":
              treeName = "portlet";
              break;
        }

        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var parentID = treeNode.getId();

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadStructureDetailData(treeID, parentID, treeType);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page1";
		inf.label = OPERATION_ADD.replace(/\$label/i, treeName);
		inf.callback = callback;
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
		var tab = ws.open(inf);
    }
 
    function editStructure() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeID   = treeNode.getId();
		var treeName = treeNode.getName();
		var treeType = treeNode.getAttribute("type");

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadStructureDetailData(treeID, null, treeType);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_EDIT.replace(/\$label/i, treeName);
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
		inf.defaultPage = "page1";
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadStructureDetailData(treeID, parentID, treeType) {
		var p = new HttpRequestParams();
		p.url = URL_SOURCE_DETAIL + treeID;

		if(parentID == "_rootId") {
			parentID = 0;
		}

		// 如果是新增
		if( treeID == DEFAULT_NEW_ID ) {
			p.setContent("type", treeType);
			p.setContent("parentId", parentID);
		} 

		var request = new HttpRequest(p);
		request.onresult = function() {
			var dataXmlNode = this.getNodeValue(XML_SITE_INFO);

			// 根据树节点type属性，预先处理xform数据岛
			preProcessXml(dataXmlNode, treeType);

			Cache.XmlDatas.add(treeID, dataXmlNode);

			var page1FormObj = $X("page1Form", dataXmlNode);

			// 离开提醒
			attachReminder(treeID, page1FormObj);

			// 设置翻页按钮显示状态
			$$("page1BtPrev").style.display = "none";
			$$("page1BtNext").style.display = "none";

			//设置保存按钮操作
			$$("page1BtSave").onclick = function() {
				saveStructure(treeID, parentID);
			}
		}
		request.send();
    }

    function saveStructure(treeID, parentID) {
        var page1FormObj = $X("page1Form");
        if( !page1FormObj.checkForm() ) {
            switchToPhase(ws, "page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SOURCE_SAVE;

        // 是否提交
        var flag = false;
      
		// 门户基本信息
		var dataXmlNode = Cache.XmlDatas.get(treeID);
		var dataElement = dataXmlNode.selectSingleNode(".//data");
		if(dataElement) {
			dataElement = dataElement.cloneNode(true);
			var rowNode = dataElement.selectSingleNode("row");

			// 门户、页面节点需要拼接supplement属性：将css,js部分拼合成一个xml文档
			var type = dataXmlNode.getAttribute("type");
			if("0"==type || "1"==type) {
				var rootName = ("0" == type? "portal" : "page");
				var name    = rowNode.getCDATA("name") || "";
				var js      = rowNode.getCDATA("js") || "";
				var jsCode  = rowNode.getCDATA("jsCode") || "";
				var css     = rowNode.getCDATA("css") || "";
				var cssCode = rowNode.getCDATA("cssCode") || "";
				
				var str = [];
				str[str.length] = "<" + rootName + ">";
				str[str.length] = "<property>";
				str[str.length] = "  <name>" + name + "</name>";
				str[str.length] = "  <description><![CDATA[]]></description>";
				str[str.length] = "</property>";
				str[str.length] = "<script>";
				str[str.length] = "  <file><![CDATA[" + js + "]]></file>";
				str[str.length] = "  <code><![CDATA[" + jsCode + "]]></code>";
				str[str.length] = "</script>";
				str[str.length] = "<style>";
				str[str.length] = "  <file><![CDATA[" + css + "]]></file>";
				str[str.length] = "  <code><![CDATA[" + cssCode + "]]></code>";
				str[str.length] = "</style>";
				str[str.length] = "</" + rootName + ">";

				rowNode.setCDATA("supplement",str.join(""));
			} 
			else {
				rowNode.removeCDATA("supplement");
			}
			rowNode.removeCDATA("js");
			rowNode.removeCDATA("jsCode");
			rowNode.removeCDATA("css");
			rowNode.removeCDATA("cssCode");

			flag = true;

			p.setXFormContent(dataElement);
			p.setContent("code", _TEMP_CODE);
		}

        if(flag) {
            var request = new HttpRequest(p);

            syncButton([$$("page1BtSave")], request); // 同步按钮状态

            request.onresult = function() {
				detachReminder(treeID); // 解除提醒

				var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
				appendTreeNode(parentID,treeNode);

				ws.closeActiveTab();
            }
            request.onsuccess = function() {
				detachReminder(treeID); // 解除提醒

				// 更新树节点名称
				var name = page1FormObj.getData("name");
				modifyTreeNode(treeID, "name", name, true);

				ws.closeActiveTab();
            }
            request.send();
        }		
    }
 
    /*
     *	根据树节点type属性，预先处理xform数据岛
     */
    function preProcessXml(xmlIsland, treeType) {
        // 在根节点上加type属性，用于saveStructure时判断
        xmlIsland.setAttribute("type", treeType);

        // 清除有showType属性，但与当前treeType不匹配的节点
        var showTypeNodes = xmlIsland.selectNodes(".//*[@showType]");
        for(var i=0; i < showTypeNodes.length; i++) {
            var curNode = showTypeNodes[i];
            var showType = curNode.getAttribute("showType").split(",");
            var flag = true;
            for(var j=0; j < showType.length; j++) {
                if(treeType == showType[j]) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                curNode.removeNode();			
            }
        }

        //控制配置按钮可见性
        var rowNode = xmlIsland.selectSingleNode(".//row");
        var definerName   = rowNode.getCDATA("definer.name")||"";
        var decoratorName = rowNode.getCDATA("decorator.name")||"";

        var page1BtConfigDefinerNode   = xmlIsland.selectSingleNode(".//*[@id='page1BtConfigDefiner']");
        var page1BtConfigDecoratorNode = xmlIsland.selectSingleNode(".//*[@id='page1BtConfigDecorator']");

        var parameters = rowNode.getCDATA("parameters")||"";
        if("" != parameters) {
            var xmlReader = new XmlReader(parameters);
            var xmlNode = new XmlNode(xmlReader.documentElement);
            var portletParams = xmlNode.selectSingleNode("portlet/@*");
            var layoutParams = xmlNode.selectSingleNode("layout/@*");
            var decoratorParams = xmlNode.selectSingleNode("decorator/@*");

            if(page1BtConfigDefinerNode) {
                switch(treeType) {
                    case "0":
                    case "1":
                    case "2":
                        if(null == layoutParams) {
                            page1BtConfigDefinerNode.setAttribute("disabled", "true");                
                        }
                        break;
                    case "3":
                        if(null == portletParams) {
                            page1BtConfigDefinerNode.setAttribute("disabled", "true");
                        }
                        break;
                }
            }
            if( page1BtConfigDecoratorNode && (""==decoratorName || null == decoratorParams)) {
                page1BtConfigDecoratorNode.setAttribute("disabled", "true");
            }
        } 
		else {
            if(page1BtConfigDefinerNode) {
                page1BtConfigDefinerNode.setAttribute("disabled", "true"); 
            }
            if(page1BtConfigDecoratorNode) {
                page1BtConfigDecoratorNode.setAttribute("disabled", "true");
            }
        }


        var definerNode = xmlIsland.selectSingleNode(".//column[@name='definer.name']");
        var decoratorNode = xmlIsland.selectSingleNode(".//column[@name='decorator.name']");
        var rowNode = xmlIsland.selectSingleNode(".//data/row");

        var componentType;
        switch(treeType) {
            case "0":
            case "1":
            case "2":
				componentType = "1";
                definerNode.setAttribute("caption", "布局器");
                decoratorNode.setAttribute("caption", "修饰器");
                break;
            case "3":
				componentType = "3";
                definerNode.setAttribute("caption", "Portlet");
                decoratorNode.setAttribute("caption", "修饰器");
                break;
        }

		// 根据treeType，给definerNode, decoratorNode节点设置不同属性
        var layoutCmd = definerNode.getAttribute("cmd");
        definerNode.setAttribute("cmd", layoutCmd.replace(/\${definerType}/i, componentType));

        // 门户、页面类型节点需要预解析supplement属性
        switch(treeType) {
            case "0":
            case "1":
                // 预解析supplement，分别设置到js,css,jsCode和cssCode上
                var supplement = rowNode.getCDATA("supplement") || "";
                var xmlReader = new XmlReader(supplement);

                if(xmlReader.documentElement) {
                    var supplementNode = new XmlNode(xmlReader.documentElement);
                    var jsNode = supplementNode.selectSingleNode("./script/file/node()");
                    var cssNode = supplementNode.selectSingleNode("./style/file/node()");
                    var jsCodeNode = supplementNode.selectSingleNode("./script/code/node()");
                    var cssCodeNode = supplementNode.selectSingleNode("./style/code/node()");

                    if(jsNode) {
                        rowNode.setCDATA("js", jsNode.nodeValue);
                    }
                    if(cssNode) {
                        rowNode.setCDATA("css", cssNode.nodeValue);
                    }
                    if(jsCodeNode) {
                        rowNode.setCDATA("jsCode", jsCodeNode.nodeValue);
                    }
                    if(cssCodeNode) {
                        rowNode.setCDATA("cssCode", cssCodeNode.nodeValue);
                    }
                }
                rowNode.removeCDATA("supplement");

                break;
            case "2":
            case "3":
                break;
        }
    }
 
    function preview() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var portalID = treeNode.getAttribute("portalId");
		var type = treeNode.getAttribute("type");

		var url	= URL_VIEW_SITE + portalID;
		if("0" != type) {
			var treeNodeID = treeNode.getId();
			url += "?pageId=" + treeNodeID;
		}

		window.open(url);
    }
 
	function getComponent(type, idField, nameField, parametersName) {
        var page1FormObj = $X("page1Form");
		var serviceUrl = URL_GET_COMPONENT_TREE + type;

        var component = window.showModalDialog("commontree.html", {title:"请选择组件", service:serviceUrl}, "dialogWidth:300px;dialogHeight:400px;");
        if(component) {
            page1FormObj.updateDataExternal(idField,   component.id);
            page1FormObj.updateDataExternal(nameField, component.name);

            // 加载布局器配置项
			Ajax({
				url: URL_GET_COMPONENT_PARAMS + component.id,
				onresult: function() {
					var newNode = this.getNodeValue(XML_COMPONENT_PARAMETERS);
					updateParameters(newNode);
					
					// 是否允许进行配置
					if(type == "2") {
						$$("page1BtConfigDecorator").disabled = ( 0 == newNode.attributes.length );
					}
					else {
						$$("page1BtConfigDefiner").disabled   = ( 0 == newNode.attributes.length );
					}
					
				}
			});
        }
    }
 
    /*
     *	将parameters字符串解析为xml对象
     *	参数：	string:parameters       xml字符串
     *	返回值：XmlNode:xmlNode         XmlNode实例
     */
    function parseParameters(parameters) {
        var xmlNode = loadXmlToNode(parameters);
        if(xmlNode == null) {
			xmlNode = loadXmlToNode("<ComponentParams/>");
        }

		xmlNode = new XmlNode(xmlNode);
        return xmlNode;    
    }

    /*
     *	更新布局器、修饰器的配置参数节点
     *	参数：	string:parametersName   xform列名
                XmlNode:newNode         XmlNode实例
     *	返回值：
     */
    function updateParameters(newNode) {
		var type = newNode.nodeName;

		var page1FormObj = $X("page1Form");
		var parameters = page1FormObj.getData("parameters")||"";
		var xmlNode = parseParameters(parameters);
		var oldNode = xmlNode.selectSingleNode("./" + type);

		if(oldNode) {
			var attributes = oldNode.attributes;
			for(var i=0; i < attributes.length; i++) {
			   oldNode.removeAttribute(attributes[0].nodeName);
			}

			var attributes = newNode.attributes;
			for(var i=0; i < attributes.length; i++) {
			   oldNode.setAttribute(attributes[i].nodeName, attributes[i].nodeValue);
			}

			if(oldNode.firstChild) {
				var oldText = new XmlNode(oldNode.firstChild);
				oldText.removeNode();
			}
			if(newNode.firstChild) {
				var newText = new XmlNode(newNode.firstChild);
				oldNode.appendChild(newText);
			}
		} 
		else {
			xmlNode.appendChild(newNode);
		}

		// 更新xform中的parameters值
		page1FormObj.updateDataExternal("parameters", xmlNode.toXml());
    }

    /*
     *	更改布局器、修饰器、portlet的配置
     *	参数：	string:paramsType       类型(布局器、修饰器、portlet)
                string:id               xform列名
                string:name             xform列名
     */
    function configParams(paramsType, id, name) {
        var page1FormObj = $X("page1Form");
        var nameValue  = page1FormObj.getData(name) || "";
        var idValue    = page1FormObj.getData(id) || "";
        var parameters = page1FormObj.getData("parameters") || "";

        var xmlNode = parseParameters(parameters);
        var oldParamsNode = xmlNode.selectSingleNode("./" + paramsType);
        var oldText = new XmlNode(oldParamsNode.firstChild);

		var newParams = window.showModalDialog("structure-params.html", 
			{id:idValue, params:oldParamsNode, title:"设置【\"" + nameValue + "】\"的参数"},"dialogWidth:250px;dialogHeight:250px;");
		if(newParams) {
			var rowReader = new XmlReader(newParams);
			var rowNode = new XmlNode(rowReader.documentElement);

			var newParamsReader = new XmlReader("<" + paramsType + "/>");
			var newParamsNode = new XmlNode(newParamsReader.documentElement);
			var newText = oldText.cloneNode(true);

			// 从row节点复制到新参数节点
			var childs = rowNode.selectNodes("*");
			for(var i=0; i < childs.length; i++) {
				newParamsNode.setAttribute(childs[i].nodeName, childs[i].text);
			}
			newParamsNode.appendChild(newText);

			updateParameters(newParamsNode);
		}
    }
 
    /* 主题管理  */
    function themeManage() {
        var treeNode = $T("tree").getActiveTreeNode();
		var treeName = treeNode.getName();
		var portalId = treeNode.getAttribute("portalId");

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadThemeManageData(portalId);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page2";
		inf.label = OPERATION_SETTING.replace(/\$label/i, treeName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_THEME_MANAGE + portalId;
		var tab = ws.open(inf);
    }

    /*
     *	主题管理详细信息加载数据
     *	参数： string:portalId     portalId
     */
    function loadThemeManageData(portalId) {
		var onresult = function() {
			var themeManageNode = this.getNodeValue(XML_THEME_MANAGE);
			var page2Tree = $T("page2Tree", themeManageNode);

			page2Tree.element.onTreeNodeRightClick = function(eventObj) {
				page2Tree.element.contextmenu.show(eventObj.clientX, eventObj.clientY);
			}
	 
			initThemeTreeMenu(portalId);

			//设置翻页/保存按钮显示状态
			$$("page2BtPrev").style.display = "none";
			$$("page2BtNext").style.display = "none";
			$$("page2BtSave").style.display = "none";
		}

		Ajax({
			url: URL_THEME_MANAGE + portalId,
			onresult: onresult
		});
    }
 
    function initThemeTreeMenu(portalId) {
        var item1 = {
            label:"更名",
            callback:changeThemeName,
            visible:function() {return !isThemeRootNode();}
        }
        var item2 = {
            label:"删除",
            callback:delTheme,
            icon:ICON + "icon_del.gif",
            visible:function() {return !isThemeRootNode();}
        }
        var item3 = {
            label:"复制",
            callback:copyTheme,
            icon:ICON + "copy.gif",
            visible:function() {return !isThemeRootNode();}
        }
        var item4 = {
            label:"预览",
            callback:function() {
                previewTheme(portalId);
            },
            icon:ICON + "preview.gif",
            visible:function() {return !isThemeRootNode();}
        }
        var item5 = {
            label:"设为默认",
            callback:setDefaultTheme,
            visible:function() {return !isThemeRootNode() && "1" != getThemeAttribute("isDefault");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addSeparator();
        menu1.addItem(item4);
        menu1.addItem(item5);

        $$("page2Tree").contextmenu = menu1;
    }
 
    function getThemeId() {
        var treeNode = $T("page2Tree").getActiveTreeNode();
        if(treeNode) {
            return treeNode.getId();
        }
		return null;
    }

	function isThemeRootNode() {
		return "_rootId" == getThemeId(); 
	}

    function getThemeAttribute(attrName) {
        var treeNode = $T("page2Tree").getActiveTreeNode();
        if(treeNode) {
            return treeNode.getAttribute(attrName);
        }
        return null;
    }

    /* 修改主题名 */
    function changeThemeName() {
        var page2TreeObj = $T("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var newName = "";
			var oncemore = false;
            while("" == newName) {
				if(oncemore) {
					alert("请输入至少一个字符，并且不能使用空格(包括全角空格）");
				}
                newName = prompt("请输入新主题名", treeName, "重新命名\"" + treeName + "\"为", null, 50) || "";
                newName = newName.replace(/[\s　]/g, "");       
				oncemore = true;
            }

            if(newName && treeName != newName) {
				Ajax({
					url: URL_RENAME_THEME + treeID + "/" + newName,
					method: "PUT",
					onsuccess: function() {
						treeNode.setAttribute("name", newName);
						page2TreeObj.reload();
					}
				});
            }
        }
    }

    /* 删除主题 */
    function delTheme() {
        var page2TreeObj = $T("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
			Ajax({
				url: URL_DEL_THEME + treeID,
				method: "DELETE",
				onsuccess: function() {
					page2TreeObj.removeTreeNode(treeNode);
				}
			});
        }
    }

    /* 复制主题  */
    function copyTheme(portalId) {
        var page2TreeObj = $T("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
			var newName =  treeName + "_2";

			Ajax({
				url:URL_COPY_THEME + treeID + "/" + newName,
				onresult: function() {
					var themeNode = this.getNodeValue(XML_THEME_MANAGE).selectSingleNode("treeNode");
					var rootNode = page2TreeObj.getTreeNodeById("_rootId");
					page2TreeObj.insertTreeNodeXml(themeNode.toXml(), rootNode);
				}
			});
        }
    }

    /* 预览主题 */
    function previewTheme(portalId) {
        var treeNode = $T("page2Tree").getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var url = URL_PREVIEW_THEME + portalId + "?themeId=" + treeID;
            window.open(url);
        }
    }

    /* 设置默认主题 */
    function setDefaultTheme() {
        var page2TreeObj = $T("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID  = treeNode.getId();
            var treeName = treeNode.getName();
 
            var onsuccess = function() {
                // 先清除前次默认主题名称
                var rootNode = page2TreeObj.getTreeNodeById("_rootId");
                var defaultThemeNode = new XmlNode(rootNode.node).selectSingleNode(".//treeNode[@isDefault='1']");
                if(defaultThemeNode) {
                    var name = defaultThemeNode.getAttribute("name");
                    defaultThemeNode.setAttribute("icon", ICON + "portal/theme.gif");
                    defaultThemeNode.setAttribute("isDefault", "0");
                }

                // 修改当前节点名称及属性
                treeNode.setAttribute("icon", ICON + "portal/default_theme.gif");
                treeNode.setAttribute("isDefault", "1");

                page2TreeObj.reload();                
            }

            Ajax({
				url: URL_SET_DEFAULT_THEME + treeID,
				method: "PUT",
				onsuccess: onsuccess
			});
        }
    }

    /* 缓存管理  */
    function cacheManage() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeID = treeNode.getId();
		var treeName = treeNode.getName();
		var portalId = treeNode.getAttribute("portalId");

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadCacheManageData(treeID, portalId);
			}, TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page3";
		inf.label = OPERATION_SETTING.replace(/\$label/i, treeName);
		inf.callback = callback;
		inf.SID = CACHE_CACHE_MANAGE + treeID;
		var tab = ws.open(inf);
    }

    /* 缓存管理详细信息加载数据 */
    function loadCacheManageData(treeID, portalId) {
		var onresult = function() {
			var cacheManageNode = this.getNodeValue(XML_CACHE_MANAGE);

			var listObj = $$("page3CacheList");
			var str = [];
            str[str.length] = "<table border=\"0\" cellspacing=\"\" cellpadding=\"3\">";

            var cacheItems = cacheManageNode.selectNodes("cacheItem");
            for(var i=0; i < cacheItems.length; i++) {
                var cacheItem = cacheItems[i];
                var name = cacheItem.getAttribute("name");
                var id = cacheItem.getAttribute("id");
                str[str.length] = "<tr><td class=\"t\" width=\"200\">" + name + 
					"</td><td class=\"t\"><input type=\"button\" class=\"btWeak\" value=\"刷新\" onclick=\"flushCache('" + id + "','" + portalId + "')\"/></td></tr>";
            }
            str[str.length] = "</table>";

            listObj.innerHTML = str.join("\r\n");

			// 设置按钮显示状态
			$$("page3BtPrev").style.display = "none";
			$$("page3BtNext").style.display = "none";
			$$("page3BtSave").style.display = "none";
		}
		
		Ajax({
			url:  URL_CACHE_MANAGE + portalId,
			onresult: onresult
		});
    }

	/* 刷新缓存  */
    function flushCache(themeId, portalId) {
		Ajax({
			url:  URL_FLUSH_CACHE + portalId + "/" + themeId,
			method: "DELETE"
		});
    }
  
    /* 查看页面流量  */
    function showPageFlowRate() {
        var treeNode = $T("tree").getActiveTreeNode();
		var portalId = treeNode.getAttribute("portalId");
		var url = URL_GET_FLOW_RATE + portalId;
		window.showModalDialog("commongrid.html", {service: url, nodename: "PageFlowRate", title:"查看页面流量"} , 
			"dialogWidth:400px;dialogHeight:400px;resizable:yes");
    }

	/* 组件资源管理 */
    function resourceManage() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var name = treeNode.getName();
		var code = treeNode.getAttribute("code");

		var params = {
			code:code
		};
	
		window.showModalDialog("filemanager.html", {params:params, title:"\"" + name + "\"相关资源管理"},"dialogWidth:400px;dialogHeight:400px;");
    }

	/********************************************************************************************************************
     ************************************************** 以下为静态发布相关 *********************************************	
     ********************************************************************************************************************/

    /* 静态发布整个门户站点 */
    function staticRelease() {
        var treeNode = $T("tree").getActiveTreeNode();
		var portalId = treeNode.getAttribute("portalId");

		Ajax({
			url: URL_STATIC_ISSUE_PORATL + portalId,
			onresult: function() {
				var thisObj = this;
				var data = this.getNodeValue("ProgressInfo");
				var progress = new Progress(URL_SYNC_PROGRESS, data, URL_CANCEL_SYNC_PROGRESS);
				progress.oncomplete = function() {
					// remoteRelease("false"); // 触发远程上传
				}
				progress.start();
			}
		});
    }

	/* 静态发布门户里指定页面 */
    function staticReleaseSinglePage() {
        var pageUrl = prompt("请输入要发布的页面地址");
		if(pageUrl == null || "" == pageUrl) {
			return alert("页面地址不能为空");
		}

		Ajax({
			url: URL_STATIC_ISSUE_PAGE,
			params: {"pageUrl": pageUrl}
		});
    }
   
	/* 远程发布 */
	function remoteRelease(override) {
		Ajax({
			url: URL_REMOTE_ISSUE + override
		});
    }


    window.onload = init;