    var _TEMP_CODE = new Date().getTime();
    
	/*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "SourceTree";
    XML_SITE_INFO = "DetailInfo";
    XML_PREVIEW = "html";
    XML_UPLOAD_INFO = "upload";
    XML_COMPONENT_PARAMETERS = "ComponentParams";
    XML_THEME_MANAGE = "ThemeManage";
    XML_CACHE_MANAGE = "CacheManage";
 
    /*
     *	默认唯一编号名前缀
     */
    CACHE_TREE_NODE_DETAIL = "treeNode__id";
    CACHE_MAIN_TREE = "tree__id";
    CACHE_THEME_MANAGE = "themeManage__id";
    CACHE_CACHE_MANAGE = "cacheManage__id";
    /*
     *	名称
     */
    OPERATION_SETTING = "设置\"$label\"";

    /*
     *	XMLHTTP请求地址汇总
     */
    URL_SOURCE_TREE   = "data/site_init.xml";
    URL_SOURCE_DETAIL = "data/site1.xml";
    URL_SOURCE_SAVE   = "data/_success.xml";
    URL_DELETE_NODE   = "data/_success.xml";
    URL_STOP_NODE     = "data/_success.xml";
    URL_SORT_NODE     = "data/_success.xml";
    URL_VIEW_SITE     = "portal!previewPortal.action";
    URL_GET_COMPONENT_PARAMETERS    = "data/layoutparameters.xml";
    URL_THEME_MANAGE      = "data/thememanage.xml";
    URL_RENAME_THEME      = "data/_success.xml";
    URL_DEL_THEME         = "data/_success.xml";
    URL_COPY_THEME        = "data/copytheme.xml";
    URL_PREVIEW_THEME     = "data/_success.xml";
    URL_SET_DEFAULT_THEME = "data/_success.xml";
    URL_GET_OPERATION     = "data/operation.xml";
    URL_FLUSH_CACHE       = "data/_success.xml";
    URL_CACHE_MANAGE      = "data/cachemanage.xml";
    URL_IMPORT_SITE       = "data/upload1.htm";
 
    function init() {
        initPaletteResize();
        initUserInfo();
        initToolBar();
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

    function initMenus() {
        var item1 = {
            label:"新建门户",
            callback:function() {addNewStructure("0");},
            
            visible:function() {return "_rootId"==getTreeNodeId() && true==getOperation("4");}
        }
        var item2 = {
            label:"新建页面",
            callback:function() {addNewStructure("1");},
            
            visible:function() {return "0"==getStructureType() && true==getOperation("4");}
        }
        var item3 = {
            label:"新建版面",
            callback:function() {addNewStructure("2");},
            
            visible:function() {return "3"!=getStructureType() && "0"!=getStructureType() && "_rootId"!=getTreeNodeId() && true==getOperation("4");}
        }
        var item4 = {
            label:"新建portlet实例",
            callback:function() {addNewStructure("3");},
            
            visible:function() {return ("1"==getStructureType() || "2"==getStructureType()) && true==getOperation("4");}
        }
        var item7 = {
            label:"删除",
            callback:delSite,
            icon:ICON + "del.gif",
            
            visible:function() {return "_rootId"!=getTreeNodeId() && true==getOperation("3");}
        }
        var item8 = {
            label:"编辑",
            callback:editStructure,
            icon:ICON + "edit.gif",
            
            visible:function() {return "_rootId"!=getTreeNodeId() && true==getOperation("2");}
        }
        var item9 = {
            label:"停用",
            callback:stopSite,
            icon:ICON + "stop.gif",
            
            visible:function() {return !isTreeNodeDisabled() && true==getOperation("6");}
        }
        var item10 = {
            label:"启用",
            callback:startSite,
            icon:ICON + "start.gif",
            
            visible:function() {return isTreeNodeDisabled() && true==getOperation("7");}
        }
        var item12 = {
            label:"预览",
            callback:preview,
            icon:ICON + "preview.gif",
            
            visible:function() {return "_rootId"!=getTreeNodeId()  && true==getOperation("2");}
        }
        var item13 = {
            label:"主题管理",
            callback:themeManage,
            icon:ICON + "theme.gif",
            
            visible:function() {return "0"==getStructureType() && true==getOperation("2");}
        }
         var item15 = {
            label:"缓存管理",
            callback:cacheManage,
            icon:ICON + "cache.gif",
            
            visible:function() {return "0"==getStructureType() && true==getOperation("1");}
        }
        var item16 = {
            label:"查看",
            callback:function() {
                editStructure(false);
            },
            icon:ICON + "view.gif",
            
            visible:function() {return "_rootId"!=getTreeNodeId() && true==getOperation("1");}
        }
        var item17 = {
            label:"资源管理",
            callback:function() {resourceManage();},
            icon:ICON + "resource.gif",
            
            visible:function() {return "0"==getStructureType() && true==getOperation("2");}
        }
        var item18 = {
            label:"启用门户",
            callback:startSite,
            icon:ICON + "start.gif",
            
            visible:function() {return "0"==getStructureType() && isTreeNodeDisabled() && true==getOperation("6");}
        }
        var item19 = {
            label:"门户静态发布",
            callback:staticIssue,
            
            visible:function() {return "0"==getStructureType() && true==getOperation("2");}
        }

        var item22 = {
            label:"查看页面流量",
            callback:showPageView,
            
            visible:function() {return "0"==getStructureType() && true==getOperation("1");}
        }
		var item24 = {
            label:"远程发布",
            callback:function() {
               remoteIssue("0");
            },
            
            visible:function() {return "0"==getStructureType() && true==getOperation("2");}
        }
		var item25 = {
            label:"远程发布(完全覆盖)",
			callback:function() {
                remoteIssue("1");
            },
            
            visible:function() {return "0"==getStructureType() && true==getOperation("2");}
        }
	    var item26 = {
            label:"页面静态发布",
            callback:staticIssueOnePage,
            
            visible:function() {return "0"==getStructureType() && true==getOperation("2");}
        }

        var menu1 = new Menu();
        menu1.addItem(item9);
        menu1.addItem(item10);
        menu1.addItem(item18);
        menu1.addSeparator();
        menu1.addItem(item23);
        menu1.addSeparator();
        menu1.addItem(item12);
        menu1.addItem(item16);
        menu1.addItem(item8);
        menu1.addItem(item7);
        menu1.addItem(item6);
        menu1.addItem(item14);
        menu1.addItem(item11);
        menu1.addSeparator();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addItem(item4);
//      menu1.addItem(item5);
        menu1.addSeparator();
        menu1.addItem(item13);
        menu1.addItem(item15);
        menu1.addItem(item17);
        menu1.addSeparator();
        menu1.addItem(item19);
		menu1.addItem(item26);
		menu1.addItem(item24);
		menu1.addItem(item25);
//      menu1.addItem(item20);
//      menu1.addItem(item21);
        menu1.addItem(item22);

        $$("tree").contextmenu = menu1;
    }

	function loadInitData() {
        var p = new HttpRequestParams();
        p.url = URL_SOURCE_TREE;

        var request = new HttpRequest(p);
        request.onresult = function() {
            var sourceTreeNode = this.getNodeValue(XML_MAIN_TREE);

            Cache.XmlDatas.add(CACHE_MAIN_TREE, sourceTreeNode);

            var tree = $T("tree", sourceTreeNode);

            tree.element.onTreeNodeActived = function(eventObj) {
                onTreeNodeActived(eventObj);
            }
            tree.element.onTreeNodeDoubleClick = function(eventObj) {
                onTreeNodeDoubleClick(eventObj);
            }
            tree.element.onTreeNodeMoved = function(eventObj) {
                onTreeNodeMoved(eventObj);
            }
            tree.element.onTreeNodeRightClick = function(eventObj) {
                sortTreeNode(URL_SORT_NODE, eventObj);
            }
        }
        request.send();
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
 
    function editStructure() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeID   = treeNode.getId();
		var treeName = treeNode.getName();
		var treeType = treeNode.getAttribute("type");

		var portalNode = treeNode;
		if("0" != treeType) { // 如果不是门户节点，则取父节点
			portalNode = treeNode.getParent();
		}
		var portalName = portalNode.getAttribute("code");
		var portalID = portalNode.getAttribute("portalId");

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadStructureDetailData(treeID,treeID,treeType,portalID,portalName);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.label = OPERATION_EDIT.replace(/\$label/i,treeName);
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
		inf.defaultPage = "page1";
		inf.phases = null;
		inf.callback = callback;
		var tab = ws.open(inf);
    }
 
    function loadStructureDetailData(treeID, parentID, treeType) {
		var p = new HttpRequestParams();
		p.url = URL_SOURCE_DETAIL + treeID;

		// 如果是新增
		if( treeID == DEFAULT_NEW_ID ) {
			p.setContent("type", treeType);
			p.setContent("parentId", parentID);
		} 

		var request = new HttpRequest(p);
		request.onresult = function() {
			var siteInfoNode = this.getNodeValue(XML_SITE_INFO);

			// 根据树节点type属性，预先处理xform数据岛
			preProcessXml(siteInfoNode,treeType);

			Cache.XmlDatas.add(cacheID, siteInfoNode);

			var page1FormObj = $X("page1Form", siteInfoNode);

			// 离开提醒
			attachReminder(cacheID, page1FormObj);

			// 设置翻页按钮显示状态
			$$("page1BtPrev").style.display = "none";
			$$("page1BtNext").style.display = "none";

			//设置保存按钮操作
			$$("page1BtSave").onclick = function() {
				saveStructure(cacheID,parentID,isNew);
			}
		}
		request.send();
    }

    function saveStructure(cacheID, parentID) {
        var page1FormObj = $("page1Form");
        if( !page1FormObj.checkForm() ) {
            switchToPhase(ws, "page1");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SOURCE_SAVE;

        //是否提交
        var flag = false;
      
		//门户基本信息
		var siteInfoNode = Cache.XmlDatas.get(cacheID);
		var siteInfoDataNode = siteInfoNode.selectSingleNode(".//data");
		if(siteInfoDataNode) {

			siteInfoDataNode = siteInfoDataNode.cloneNode(true);
			var rowNode = siteInfoDataNode.selectSingleNode("row");

			// 门户、页面节点需要拼接supplement属性：将css,js部分拼合成一个xml文档
			var type = siteInfoNode.getAttribute("type");
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
				str[str.length] = "<name>" + name + "</name>";
				str[str.length] = "<description>";
				str[str.length] = "<![CDATA[]]>";
				str[str.length] = "</description>";
				str[str.length] = "</property>";
				str[str.length] = "<script>";
				str[str.length] = "<file>";
				str[str.length] = "<![CDATA[" + js + "]]>";
				str[str.length] = "</file>";
				str[str.length] = "<code>";
				str[str.length] = "<![CDATA[" + jsCode + "]]>";
				str[str.length] = "</code>";
				str[str.length] = "</"+"script>";
				str[str.length] = "<style>";
				str[str.length] = "<file>";
				str[str.length] = "<![CDATA[" + css + "]]>";
				str[str.length] = "</file>";
				str[str.length] = "<code>";
				str[str.length] = "<![CDATA[" + cssCode + "]]>";
				str[str.length] = "</code>";
				str[str.length] = "</style>";
				str[str.length] = "</" + rootName + ">";

				rowNode.setCDATA("supplement",str.join(""));
			} else {
				rowNode.removeCDATA("supplement");
			}
			rowNode.removeCDATA("js");
			rowNode.removeCDATA("jsCode");
			rowNode.removeCDATA("css");
			rowNode.removeCDATA("cssCode");

			flag = true;

			p.setXFormContent(siteInfoDataNode);
			p.setContent("code",  _TEMP_CODE);
		}

        if(flag) {
            var request = new HttpRequest(p);

            //同步按钮状态
            syncButton([$$("page1BtSave")], request);

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

				// 更新树节点名称
				var name = page1FormObj.getData("name");
				modifyTreeNode(cacheID, "name", name, true);
            }
            request.send();
        }		
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
              treeName = "portlet实例";
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
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_TREE_NODE_DETAIL + treeID;
		var tab = ws.open(inf);
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

        // 根据treeType，给definerNode, decoratorNode节点设置不同属性
        var layoutCmd = definerNode.getAttribute("cmd");
        definerNode.setAttribute("cmd", layoutCmd.replace(/\${definerType}/i, treeType));

        switch(treeType) {
            case "0":
            case "1":
            case "2":
                definerNode.setAttribute("caption", "布局");
                decoratorNode.setAttribute("caption", "修饰");
                break;
            case "3":
                definerNode.setAttribute("caption", "Portlet");
                decoratorNode.setAttribute("caption", "修饰");
                break;
        }

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


    /*
     *	根据definerType决定要执行的方法
     */
    function getDefiner(definerType, definerId, definerName, parametersName) {
        switch(definerType) {
            case "0":
            case "1":
            case "2":
                getLayout(definerId,definerName,parametersName);
                break;
            case "3":
                getPortlet(definerId,definerName,parametersName);
                break;
        }
    }

    /*
     *	获取布局器
     */
    function getLayout(definerId, definerName, parametersName) {
        var page1FormObj = $("page1Form");

        var layout = window.showModalDialog("layouttree.htm", {title:"请选择布局器"}, "dialogWidth:300px;dialogHeight:400px;");
        if(layout) {
            page1FormObj.updateDataExternal(definerId, layout.id);
            page1FormObj.updateDataExternal(definerName, layout.name);

            // 加载布局器配置项
            loadLayoutParameters(layout.id,parametersName);
        }
    }

    /*
     *	获取修饰器
     */
    function getDecorator(decoratorId,decoratorName,parametersName) {
        var page1FormObj = $("page1Form");

        var decorator = window.showModalDialog("decoratortree.htm", {title:"请选择修饰器"}, "dialogWidth:300px;dialogHeight:400px;");
        if(decorator) {
            page1FormObj.updateDataExternal(decoratorId, decorator.id);
            page1FormObj.updateDataExternal(decoratorName, decorator.name);

            // 加载修饰器配置项
            loadDecoratorParameters(decorator.id,parametersName);
        } 
    }

    /*
     *	获取portlet
     */
    function getPortlet(definerId,definerName,parametersName) {
        var page1FormObj = $("page1Form");

        var portlet = window.showModalDialog("portlettree.htm", {title:"请选择Portlet"}, "dialogWidth:300px;dialogHeight:400px;");
        if(portlet) {
            page1FormObj.updateDataExternal(definerId,portlet.id);
            page1FormObj.updateDataExternal(definerName,portlet.name);

            //加载portlet配置项
            loadPortletParameters(portlet.id,parametersName);
        }
    }
 
    /*
     *	布局器加载数据
     */
    function loadLayoutParameters(layoutID) {
        var p = new HttpRequestParams();
        p.url = URL_GET_COMPONENT_PARAMETERS + id;

        var request = new HttpRequest(p);
        request.onresult = function() {
		   var newNode = this.getNodeValue(XML_COMPONENT_PARAMETERS);
           updateParameters(newNode);
            
            // 允许进行配置
            $$("page1BtConfigDefiner").disabled   = ( 0 == newNode.attributes.length );
			$$("page1BtConfigDecorator").disabled = ( 0 == newNode.attributes.length );
        }
        request.send();
    }
 
    /*
     *	将parameters字符串解析为xml对象
     *	参数：	string:parameters       xml字符串
     *	返回值：XmlNode:xmlNode         XmlNode实例
     */
    function parseParameters(parameters) {
        var xmlReader = new XmlReader();
        xmlReader.loadXML(parameters);
        if(null == xmlReader.documentElement) {
            xmlReader.loadXML("<params/>");
        }
        var xmlNode = new XmlNode(xmlReader.documentElement);
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

		var page1FormObj = $("page1Form");
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
        var page1FormObj = $("page1Form");
        var nameValue  = page1FormObj.getData(name)||"";
        var idValue    = page1FormObj.getData(id)||"";
        var parameters = page1FormObj.getData("parameters") || "";

        var xmlNode = parseParameters(parameters);
        var oldParamsNode = xmlNode.selectSingleNode("./" + paramsType);
        var oldText = new XmlNode(oldParamsNode.firstChild);

		var newParams = window.showModalDialog("configparams.htm", {id:idValue,params:oldParamsNode,type:paramsType,title:"更改\""+nameValue+"\"的配置"},"dialogWidth:250px;dialogHeight:250px;");
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
 
    /*
     *	主题管理
     */
    function themeManage() {
        var treeObj = $T("tree");
        var treeNode = treeObj.getActiveTreeNode();
		var treeID   = treeNode.getId();
		var treeName = treeNode.getName();
		var portalId = treeNode.getAttribute("portalId");

		var callback = {};
		callback.onTabChange = function() {
			setTimeout(function() {
				loadThemeManageData(treeID, portalId);
			},TIMEOUT_TAB_CHANGE);
		};

		var inf = {};
		inf.defaultPage = "page2";
		inf.label = OPERATION_SETTING.replace(/\$label/i, treeName);
		inf.phases = null;
		inf.callback = callback;
		inf.SID = CACHE_THEME_MANAGE + treeID;
		var tab = ws.open(inf);
    }

    /*
     *	主题管理详细信息加载数据
     *	参数：	string:treeID       树节点id
                string:portalId     portalId
     */
    function loadThemeManageData(treeID, portalId) {
		var p = new HttpRequestParams();
		p.url = URL_THEME_MANAGE + treeID;

		var request = new HttpRequest(p);
		request.onresult = function() {
			var themeManageNode = this.getNodeValue(XML_THEME_MANAGE);
			Cache.XmlDatas.add(treeID + "." + XML_THEME_MANAGE, themeManageNode);

			var page2TreeObj = $T("page2Tree");
			page2TreeObj.load(xmlIsland.node);
			page2TreeObj.research = true;

			page2TreeObj.onTreeNodeRightClick = function(eventObj) {
				onPage2TreeNodeRightClick(eventObj);
			}
	 
			initThemeTreeMenu(treeID, portalId);

			//设置翻页/保存按钮显示状态
			$$("page2BtPrev").style.display = "none";
			$$("page2BtNext").style.display = "none";
			$$("page2BtSave").style.display = "none";
		}
		request.send();
    }
 
    function initThemeTreeMenu(siteID, portalId) {
        var item1 = {
            label:"更名",
            callback:function() {
                changeThemeName();
            },
            visible:function() {return "_rootId"!=getThemeTreeId();}
        }
        var item2 = {
            label:"删除",
            callback:function() {
                delTheme(portalId);
            },
            icon:ICON + "del.gif",
            visible:function() {return "_rootId"!=getThemeTreeId();}
        }
        var item3 = {
            label:"复制",
            callback:function() {
                copyTheme(portalId);
            },
            icon:ICON + "copy.gif",
            visible:function() {return "_rootId"!=getThemeTreeId();}
        }
        var item4 = {
            label:"预览",
            callback:function() {
                previewTheme(portalId);
            },
            icon:ICON + "preview.gif",
            visible:function() {return "_rootId"!=getThemeTreeId();}
        }
        var item5 = {
            label:"设为默认",
            callback:function() {
                setDefaultTheme(portalId);
            },
            visible:function() {return "_rootId"!=getThemeTreeId() && "1"!=getThemeAttribute("isDefault");}
        }

        var menu1 = new Menu();
        menu1.addItem(item1);
        menu1.addItem(item2);
        menu1.addItem(item3);
        menu1.addSeparator();
        menu1.addItem(item4);
        menu1.addItem(item5);

        var page2TreeObj = $("page2Tree");
        page2TreeObj.contextmenu = menu1;
    }
    /*
     *	右击树节点
     *	参数：	Object:eventObj     模拟事件对象
     *	返回值：
     */
    function onPage2TreeNodeRightClick(eventObj) {
        var page2TreeObj = $("page2Tree");
        if(page2TreeObj.contextmenu) {
            page2TreeObj.contextmenu.show(eventObj.clientX,eventObj.clientY);
        }
    }
    /*
     *	获取主题树节点id
     *	参数：	
     *	返回值：
     */
    function getThemeTreeId() {
        var id = null;
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            id = treeNode.getId();
        }
        return id;
    }
    /*
     *	修改主题名
     *	参数：	string:siteID           门户节点id
     *	返回值：
     */
    function changeThemeName() {
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var newName = prompt("请输入新主题名",treeName,"重新命名\""+treeName+"\"为",null,50);
            newName = newName.replace(/[\s　]/g,"");
            while(""==newName) {
                alert("请输入至少一个字符，并且不能使用空格(包括全角空格）");
                newName = prompt("请输入新主题名",treeName,"重新命名\""+treeName+"\"为",null,50);
                newName = newName.replace(/[\s　]/g,"");            
            }

            if(newName && treeName!=newName) {
                var p = new HttpRequestParams();
                p.url = URL_RENAME_THEME;
                p.setContent("themeId",treeID);
                p.setContent("name", newName);

                var request = new HttpRequest(p);
                request.onsuccess = function() {
                    treeNode.setAttribute("name",newName);
                    page2TreeObj.reload();
                }
                request.send();
            }
        }
    }
    /*
     *	删除主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function delTheme(portalId) {
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_DEL_THEME;
            p.setContent("portalId",portalId);
            p.setContent("themeId",treeID);

            var request = new HttpRequest(p);
            request.onsuccess = function() {
                page2TreeObj.removeTreeNode(treeNode);
            }
            request.send();
        }
    }
    /*
     *	复制主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function copyTheme(portalId) {
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_COPY_THEME;
            p.setContent("portalId",portalId);
            p.setContent("themeId",treeID);
            p.setContent("name","副本_" + treeName);

            var request = new HttpRequest(p);
            request.onresult = function() {
                var themeNode = this.getNodeValue(XML_THEME_MANAGE).selectSingleNode("treeNode");
                var rootNode = page2TreeObj.getTreeNodeById("_rootId");

                page2TreeObj.insertTreeNodeXml(themeNode.toXml(),rootNode);
            }
            request.send();
        }
    }
    /*
     *	预览主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function previewTheme(portalId) {
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var url = URL_PREVIEW_THEME + "?themeId=" + treeID + "&portalId=" + portalId;

            window.open(url,"previewTheme","");
        }
    }
    /*
     *	设置默认主题
     *	参数：	string:portalId           portalId
     *	返回值：
     */
    function setDefaultTheme(portalId) {
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();

            var p = new HttpRequestParams();
            p.url = URL_SET_DEFAULT_THEME;
            p.setContent("portalId",portalId);
            p.setContent("themeId",treeID);

            var request = new HttpRequest(p);
            request.onsuccess = function() {
                //先清除前次默认主题名称
                var rootNode = page2TreeObj.getTreeNodeById("_rootId");
                var defaultThemeNode = new XmlNode(rootNode.node).selectSingleNode(".//treeNode[@isDefault='1']");
                if(defaultThemeNode) {
                    var name = defaultThemeNode.getAttribute("name");
                    defaultThemeNode.setAttribute("icon",ICON + "theme.gif");
                    defaultThemeNode.setAttribute("isDefault","0");
                }

                //修改当前节点名称及属性
                treeNode.setAttribute("icon",ICON + "default_theme.gif");
                treeNode.setAttribute("isDefault","1");

                page2TreeObj.reload();                
            }
            request.send();
        }
    }
    /*
     *	获取主题属性
     *	参数：	string:attrName           主题属性名
     *	返回值：
     */
    function getThemeAttribute(attrName) {
        var value = null;
        var page2TreeObj = $("page2Tree");
        var treeNode = page2TreeObj.getActiveTreeNode();
        if(treeNode) {
            value = treeNode.getAttribute(attrName);
        }
        return value;
    }

    /*
     *	刷新缓存
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
    function flushCache(id,portalId) {
        var p = new HttpRequestParams();
        p.url = URL_FLUSH_CACHE;
        p.setContent("portalId",portalId);
        p.setContent("themeId",id);

        var request = new HttpRequest(p);
        request.onsuccess = function() {
        }
        request.send();
    }
    /*
     *	缓存管理
     *	参数：	
     *	返回值：
     */
    function cacheManage() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(treeNode) {
            var treeID = treeNode.getId();
            var treeName = treeNode.getName();
            var portalId = treeNode.getAttribute("portalId");

            var callback = {};
            callback.onTabClose = function(eventObj) {
                delCacheData(eventObj.tab.SID);
            };
            callback.onTabChange = function() {
                setTimeout(function() {
                    loadCacheManageData(treeID, portalId);
                },TIMEOUT_TAB_CHANGE);
            };

            var inf = {};
            inf.defaultPage = "page3";
            inf.label = OPERATION_SETTING.replace(/\$label/i,treeName);
            inf.phases = null;
            inf.callback = callback;
            inf.SID = CACHE_CACHE_MANAGE + treeID;
            var tab = ws.open(inf);
        }
    }
    /*
     *	缓存管理详细信息加载数据
     *	参数：	string:treeID       树节点id
                string:portalId     portalId
     *	返回值：
     */
    function loadCacheManageData(treeID, portalId) {
        var cacheID = CACHE_CACHE_MANAGE + treeID;
        var treeDetail = Cache.Variables.get(cacheID);
        if(null==treeDetail) {
            var p = new HttpRequestParams();
            p.url = URL_CACHE_MANAGE;
            p.setContent("portalId", portalId);

            var request = new HttpRequest(p);
            request.onresult = function() {
                var cacheManageNode = this.getNodeValue(XML_CACHE_MANAGE);
                var cacheManageNodeID = cacheID+"."+XML_CACHE_MANAGE;

                Cache.XmlDatas.add(cacheManageNodeID,cacheManageNode);
                Cache.Variables.add(cacheID,[cacheManageNodeID]);

                initCacheManagePages(cacheID,treeID, portalId);
            }
            request.send();
        }else{
            initCacheManagePages(cacheID,treeID, portalId);
        }
    }
    /*
     *	缓存管理相关页加载数据
     *	参数：	string:cacheID          缓存数据id
                string:treeID           树节点id
                string:portalId         portalId
     *	返回值：
     */
    function initCacheManagePages(cacheID,treeID, portalId) {
        var page3CacheListObj = $("page3CacheList");
        createCacheList(page3CacheListObj,cacheID,treeID, portalId);

        //设置翻页按钮显示状态
        var page3BtPrevObj = $("page3BtPrev");
        var page3BtNextObj = $("page3BtNext");
        page3BtPrevObj.style.display = "none";
        page3BtNextObj.style.display = "none";

        //设置保存按钮操作
        var page3BtSaveObj = $("page3BtSave");
        page3BtSaveObj.style.display = "none";
    }
    /*
     *	创建缓存管理列表
     *	参数：	Element:listObj         列表对象
                string:cacheID          缓存数据id
                string:treeID           树节点id
                string:portalId         portalId
     *	返回值：
     */
    function createCacheList(listObj,cacheID,treeID, portalId) {
        var xmlIsland = Cache.XmlDatas.get(cacheID+"."+XML_CACHE_MANAGE);
        if(xmlIsland) {
            var str = [];
            str[str.length] = "<table border=\"0\" cellspacing=\"\" cellpadding=\"3\">";
            str[str.length] = "<tr class=\"th\"><td width=\"200\">缓存项</td><td>&nbsp;</td></tr>";

            var cacheItems = xmlIsland.selectNodes("cacheItem");
            for(var i=0,iLen=cacheItems.length;i<iLen;i++) {
                var cacheItem = cacheItems[i];
                var name = cacheItem.getAttribute("name");
                var id = cacheItem.getAttribute("id");
                str[str.length] = "<tr><td class=\"t\">" + name + "</td><td class=\"t\"><input type=\"button\" class=\"btWeak\" value=\"刷新\" onclick=\"flushCache('" + id + "','" + portalId + "')\"/></td></tr>";
            }
            str[str.length] = "</table>";

            listObj.innerHTML = str.join("\r\n");
        }
    }
 
    /*
     *	资源管理
     *	参数：	
     *	返回值：
     */
    function resourceManage() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(treeNode) {
            var id = treeNode.getAttribute("portalId");
            var name = treeNode.getName();
            var code = treeNode.getAttribute("code");

            var params = {
                type:"site",
                code:code,
                id:id
            };
        
            window.showModalDialog("resource.htm",{params:params,title:"\""+name+"\"相关资源管理"},"dialogWidth:400px;dialogHeight:400px;");
        }
    }

  
    /*
     *	查看页面流量
     *	参数：	
     *	返回值：
     */
    function showPageView() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(treeNode) {
            var portalId = treeNode.getAttribute("portalId");
            var name = treeNode.getName();
            var code = treeNode.getAttribute("code");

            var params = {
                type:"site",
                code:code,
                portalId:portalId
            };
        
            window.showModalDialog("pageview.htm",{params:params,title:"查看\""+name+"\"页面流量"},"dialogWidth:400px;dialogHeight:400px;resizable:yes");
        }
    }
    /*
     *	授予角色
     *	参数：	
     *	返回值：
     */
    function setPortalPermission() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        if(treeNode) {
            var id = treeNode.getId();
            var name = treeNode.getName();
            var resourceType = "1";
            var type = "portal";
            var title = "授予\"" + name + "\"角色";
            var params = {
                roleId:id,
                resourceType:resourceType,
				applicationId:"pms",
                isRole2Resource:"0"
            };
            window.showModalDialog("setpermission.htm",{params:params,title:title,type:type},"dialogWidth:700px;dialogHeight:500px;resizable:yes");
        }
    }

	/********************************************************************************************************************
     ************************************************** 以下为静态发布相关 *********************************************	
     ********************************************************************************************************************/
	URL_SYNC_PROGRESS = "data/progress.xml";
    URL_CANCEL_SYNC_PROGRESS = "data/_success.xml";
	URL_STATIC_ISSUE_PORATL = "data/_success.xml";
	URL_STATIC_ISSUE_PAGE = "data/_success.xml";
	URL_REMOTE_ISSUE = "data/_success.xml";

    URL_SYNC_PROGRESS = "publish!getProgress.action";
    URL_CANCEL_SYNC_PROGRESS = "publish!doConceal.action";
	URL_STATIC_ISSUE_PORATL = "pms/publish!staticIssuePortal.action";
	URL_STATIC_ISSUE_PAGE = "pms/publish!staticIssuePortalPage.action";
	URL_REMOTE_ISSUE = "publish!ftpUpload2RemoteServer.action";

    /*
     *	静态发布整个门户站点
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
    function staticIssue() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var p = new HttpRequestParams();
        p.url = URL_STATIC_ISSUE_PORATL;
        var portalId = treeNode.getAttribute("portalId");
        p.setContent("id", portalId);
		p.setContent("type", 1);

        var request = new HttpRequest(p);

		request.onsuccess = function() {
		}

		request.onresult = function() {
			var thisObj = this;
			var data = this.getNodeValue("ProgressInfo");
			var progress = new Progress(URL_SYNC_PROGRESS,data,URL_CANCEL_SYNC_PROGRESS);
			progress.oncomplete = function() {
				// 触发远程上传
				// remoteIssue("0");
			}
			progress.start();
		}
        request.send();
    }

	/*
     *	静态发布门户里指定页面
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
    function staticIssueOnePage() {
        var treeObj = $("tree");
        var treeNode = treeObj.getActiveTreeNode();
        var p = new HttpRequestParams();
        p.url = URL_STATIC_ISSUE_PAGE;
        var pageUrl = prompt("请输入要发布的页面地址");
		if(pageUrl == null || "" == pageUrl) {
			return alert("页面地址不能为空");
		}
        p.setContent("pageUrl", pageUrl);

        var request = new HttpRequest(p);
		request.onresult = function() {
			var thisObj = this;
			var data = this.getNodeValue("ProgressInfo");
			var progress = new Progress(URL_SYNC_PROGRESS,data,URL_CANCEL_SYNC_PROGRESS);
			progress.oncomplete = function() {
				// 触发远程上传
				// remoteIssue("0");
			}
			progress.start();
		}
        request.send();
    }
   
	/*
     *	远程发布
     *	参数：	string:id               缓存项id
                string:portalId         portalId
     *	返回值：
     */
	function remoteIssue(override) {
        var p = new HttpRequestParams();
        p.url = URL_REMOTE_ISSUE;
		p.setContent("override", override);

        var request = new HttpRequest(p);
 
        request.send();
    }

    window.onload = init;

    //关闭页面自动注销
    logoutOnClose();