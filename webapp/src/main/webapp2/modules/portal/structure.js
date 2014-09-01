/* 后台响应数据节点名称 */
XML_MAIN_TREE = "SourceTree";
XML_SITE_INFO = "DetailInfo";
XML_COMPONENT_PARAMETERS = "ComponentParams";
XML_THEME_MANAGE = "ThemeList";
XML_CACHE_MANAGE = "CacheManage";

/* 默认唯一编号名前缀  */
CACHE_TREE_NODE_DETAIL = "_treeNode_id_";
CACHE_THEME_MANAGE = "_theme_id_";
CACHE_CACHE_MANAGE = "_cache_id_";

/* XMLHTTP请求地址汇总 */
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
	URL_GET_OPERATION     = "data/_operation.xml?";
	URL_FLUSH_CACHE       = "data/_success.xml?";
	URL_CACHE_MANAGE      = "data/cachemanage.xml?";
	URL_GET_FLOW_RATE     = "data/page_flow_rate.xml?";
}

function init() {
    initPaletteResize();
    initMenus();
    initWorkSpace(false);
    initEvents();

    loadInitData();
}
 
function getStructureType() { return getTreeAttribute("type"); }
function isPortalNode()  { return getStructureType() == "0"; }
function isPageNode()    { return getStructureType() == "1"; }
function isSectionNode() { return getStructureType() == "2"; }
function isPortletNode() { return getStructureType() == "3"; }

function initMenus() {
    ICON = "images/";
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
        icon:ICON + "icon_edit.gif",
        visible:function() {return !isTreeRoot() && getOperation("2");}
    }
    var item7 = {
        label:"停用",
        callback: function() { stopOrStartTreeNode("1"); },
        icon:ICON + "icon_stop.gif",
        visible:function() {return !isTreeRoot() && !isTreeNodeDisabled() && getOperation("6");}
    }
    var item8 = {
        label:"启用",
        callback: function() { stopOrStartTreeNode("0"); },
        icon:ICON + "icon_start.gif",
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
        icon:ICON + "theme.gif",
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

    var menu1 = new $.Menu();
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
	       
    $1("tree").contextmenu = menu1;
}

function loadInitData() {
    var onresult = function() {
        var tree = $.T("tree", this.getNodeValue(XML_MAIN_TREE));
        tree.onTreeNodeMoved = function(ev) { sortTreeNode(URL_SORT_NODE, ev); }
        tree.onTreeNodeRightClick = function(ev) { onTreeNodeRightClick(ev, true); }
        tree.onTreeNodeDoubleClick = function(ev) {
            var treeNode = getActiveTreeNode();
            getTreeOperation(treeNode, function(_operation) {
                var canEdit = checkOperation("2", _operation);
                if("_root" != treeNode.id && canEdit) {
                    editStructure();
                }
            });
        }
    }

	$.ajax({url: URL_SOURCE_TREE, onresult: onresult});
}

function addNewStructure(treeType) {
    var treeID = DEFAULT_NEW_ID;
    var names = ["门户", "页面", "版面", "portlet"];
	var treeName = names[parseInt(treeType)];

    var tree = $.T("tree");
    var treeNode = tree.getActiveTreeNode();
	var parentID = treeNode.id;

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
	ws.open(inf);
}

function editStructure() {
    var tree = $.T("tree");
    var treeNode = tree.getActiveTreeNode();
	var treeID   = treeNode.id;
	var treeName = treeNode.name;
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
	ws.open(inf);
}

function loadStructureDetailData(treeID, parentID, treeType) {
	var request = new $.HttpRequest();
	request.url = URL_SOURCE_DETAIL + treeID;

	// 如果是新增
	if( treeID == DEFAULT_NEW_ID ) {
		request.addParam("type", treeType);
		request.addParam("parentId", parentID == "_root" ? "0" : parentID);
	} 

	request.onresult = function() {
		var dataXmlNode = this.getNodeValue(XML_SITE_INFO);

		// 根据树节点type属性，预先处理xform数据岛
		preProcessXml(dataXmlNode, treeType);

		$.cache.XmlDatas[treeID] = dataXmlNode;

		var page1Form = $.F("page1Form", dataXmlNode);

		// 离开提醒
		attachReminder(treeID, page1Form);

		$1("page1BtSave").onclick = function() {
			saveStructure(treeID, parentID);
		}
	}
	request.send();
}

function saveStructure(treeID, parentID) {
    var page1Form = $.F("page1Form");
    if( !page1Form.checkForm() ) {
        switchToPhase(ws, "page1");
        return;
    }

    var p = new HttpRequestParams();
    p.url = URL_SOURCE_SAVE;

    // 是否提交
    var flag = false;
  
	// 门户基本信息
	var dataXmlNode = $.cache.XmlDatas[treeID);
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
		p.setContent("code", $.now());
	}

    if(flag) {
        var request = new $.HttpRequest();

        syncButton([$1("page1BtSave")], request); // 同步按钮状态

        request.onresult = function() {
			detachReminder(treeID); // 解除提醒

			var treeNode = this.getNodeValue(XML_MAIN_TREE).selectSingleNode("treeNode");
			appendTreeNode(parentID, treeNode);

			ws.closeActiveTab();
        }
        request.onsuccess = function() {
			detachReminder(treeID); // 解除提醒

			// 更新树节点名称
			var name = page1Form.getData("name");
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
    var treeNode = getActiveTreeNode();
	var portalID = treeNode.getAttribute("portalId");

	var url	= URL_VIEW_SITE + portalID;
	if( !isPortalNode() ) {
		url += "?pageId=" + treeNode.id;
	}
	window.open(url);
}

function getComponent(type, idField, nameField, parametersName) {
    var page1Form = $.F("page1Form");
	var url = URL_GET_COMPONENT_TREE + type;

    popupTree(url, "SourceTree", {}, function(component){
        page1Form.updateDataExternal(idField, component.id);
        page1Form.updateDataExternal(nameField, component.name);

        // 加载布局器配置项
        $.ajax({
            url: URL_GET_COMPONENT_PARAMS + component.id,
            onresult: function() {
                var newNode = this.getNodeValue(XML_COMPONENT_PARAMETERS);
                updateParameters(newNode);
                
                // 是否允许进行配置
                var disabled = ( 0 == newNode.attributes.length );
                if(type == "2") {
                    $1("page1BtConfigDecorator").disabled = disabled;
                } else {
                    $1("page1BtConfigDefiner").disabled = disabled;
                }
                
            }
        });
    });
}

/* 将parameters字符串解析为xml对象 */
function parseParameters(parameters) {
    parameters = parameters || "<" + XML_COMPONENT_PARAMETERS + "/>";
    return $.XML.toNode(parameters);    
}

/*
 *	更新布局器、修饰器的配置参数节点
 *	参数：	string:parametersName   xform列名
            XmlNode:newNode         XmlNode实例
 */
function updateParameters(newNode) {
	var type = newNode.nodeName;

	var page1Form = $.F("page1Form");
	var parameters = page1Form.getData("parameters")||"";
	var xmlNode = parseParameters(parameters);
	var oldNode = xmlNode.querySelector(type);

	if(oldNode) {
        $.each(oldNode.attributes, function(i, attr) {
            oldNode.removeAttribute(attr.nodeName);
        });

        $.each(newNode.attributes, function(i, attr) {
            oldNode.setAttribute(attr.nodeName, attr.value);
        });

		if(oldNode.firstChild) {
			var oldText = oldNode.firstChild;
			oldText.removeNode();
		}
		if(newNode.firstChild) {
			var newText = newNode.firstChild;
			oldNode.appendChild(newText);
		}
	} 
	else {
		xmlNode.appendChild(newNode);
	}

	// 更新xform中的parameters值
	page1Form.updateDataExternal("parameters", $.XML.toXml(xmlNode));
}

/*
 *	更改布局器、修饰器、portlet的配置
 *	参数：	string:paramsType       类型(布局器、修饰器、portlet)
            string:id               xform列名
            string:name             xform列名
 */
function configParams(paramsType, id, name) {
    var page1Form = $.F("page1Form");
    var nameValue  = page1Form.getData(name) || "";
    var idValue    = page1Form.getData(id) || "";
    var parameters = page1Form.getData("parameters") || "";

    var xmlNode = parseParameters(parameters.revertEntity());
    var oldParamsNode = xmlNode.selectSingleNode("./" + paramsType);
    var oldText = new XmlNode(oldParamsNode.firstChild);

    var title = "设置【\"" + nameValue + "】\"的参数";
	var newParams = window.showModalDialog("structure-params.html", {paramsXML:oldParamsNode, title:title}, 
        "dialogWidth:250px;dialogHeight:250px;");

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
    var treeNode = $.T("tree").getActiveTreeNode();
	var treeName = treeNode.name;
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
	inf.callback = callback;
	inf.SID = CACHE_THEME_MANAGE + portalId;
	ws.open(inf);
}

/* 主题管理详细信息加载数据 */
function loadThemeManageData(portalId) {
	var onresult = function() {
		var themeManageNode = this.getNodeValue(XML_THEME_MANAGE);
		var page2Tree = $.T("page2Tree", themeManageNode);

		page2tree.onTreeNodeRightClick = function(ev) {
			page2tree.contextmenu.show(ev.clientX, ev.clientY);
		}
 
		initThemeTreeMenu(portalId);

		$("page2BtSave").hide();
	}

	$.ajax({
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

    var menu1 = new $.Menu();
    menu1.addItem(item1);
    menu1.addItem(item2);
    menu1.addItem(item3);
    menu1.addSeparator();
    menu1.addItem(item4);
    menu1.addItem(item5);

    $1("page2Tree").contextmenu = menu1;
}

function getThemeId() {
    var treeNode = $.T("page2Tree").getActiveTreeNode();
    if(treeNode) {
        return treeNode.id;
    }
	return null;
}

function isThemeRootNode() {
	return "_root" == getThemeId(); 
}

function getThemeAttribute(attrName) {
    var treeNode = $.T("page2Tree").getActiveTreeNode();
    if(treeNode) {
        return treeNode.getAttribute(attrName);
    }
    return null;
}

/* 修改主题名 */
function changeThemeName() {
    var page2tree = $.T("page2Tree");
    var treeNode = page2tree.getActiveTreeNode();
    if(treeNode) {
        var treeID = treeNode.id;
        var treeName = treeNode.name;

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
			$.ajax({
				url: URL_RENAME_THEME + treeID + "/" + newName,
				method: "PUT",
				onsuccess: function() {
					treeNode.setAttribute("name", newName);
                    modifyTreeNode(treeID, "name", newName, "page2tree");
				}
			});
        }
    }
}

/* 删除主题 */
function delTheme() {
    var page2tree = $.T("page2Tree");
    var treeNode = page2tree.getActiveTreeNode();
    if(treeNode) {
        var treeID = treeNode.id;
		$.ajax({
			url: URL_DEL_THEME + treeID,
			method: "DELETE",
			onsuccess: function() {
				page2tree.removeTreeNode(treeNode);
			}
		});
    }
}

/* 复制主题  */
function copyTheme(portalId) {
    var page2tree = $.T("page2Tree");
    var treeNode = page2tree.getActiveTreeNode();
    if(treeNode) {
        var treeID = treeNode.id;
        var treeName = treeNode.name;
		var newName =  treeName + "_2";

		$.ajax({
			url: URL_COPY_THEME + treeID + "/" + newName,
			onresult: function() {
				var xmlNode = this.getNodeValue(XML_THEME_MANAGE).querySelector("treeNode");
				var rootNode = page2tree.getTreeNodeById("_root");
                appendTreeNode("_root", xmlNode);
			}
		});
    }
}

/* 预览主题 */
function previewTheme(portalId) {
    var treeNode = $.T("page2Tree").getActiveTreeNode();
    if(treeNode) {
        var url = URL_PREVIEW_THEME + portalId + "?themeId=" + treeNode.id;
        window.open(url);
    }
}

/* 设置默认主题 */
function setDefaultTheme() {
    var page2tree = $.T("page2Tree");
    var treeNode = page2tree.getActiveTreeNode();
    if(treeNode) {
        var treeID  = treeNode.id;
        var treeName = treeNode.name;

        var onsuccess = function() {
            // 先清除前次默认主题名称
            var rootNode = page2tree.getTreeNodeById("_root");
            var defaultThemeNode = new XmlNode(rootNode.node).selectSingleNode(".//treeNode[@isDefault='1']");
            if(defaultThemeNode) {
                var name = defaultThemeNode.getAttribute("name");
                defaultThemeNode.setAttribute("icon", ICON + "portal/theme.gif");
                defaultThemeNode.setAttribute("isDefault", "0");
            }

            // 修改当前节点名称及属性
            treeNode.setAttribute("icon", ICON + "portal/default_theme.gif");
            treeNode.setAttribute("isDefault", "1");

            page2tree.reload();                
        }

        $.ajax({
			url: URL_SET_DEFAULT_THEME + treeID,
			method: "PUT",
			onsuccess: onsuccess
		});
    }
}

/* 缓存管理  */
function cacheManage() {
    var tree = $.T("tree");
    var treeNode = tree.getActiveTreeNode();
	var treeID = treeNode.id;
	var treeName = treeNode.name;
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
	ws.open(inf);
}

/* 缓存管理详细信息加载数据 */
function loadCacheManageData(treeID, portalId) {
	var onresult = function() {
		var cacheManageNode = this.getNodeValue(XML_CACHE_MANAGE);

		var listObj = $1("page3CacheList");
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
		$("page3BtSave").hide();
	}
	
	$.ajax({
		url:  URL_CACHE_MANAGE + portalId,
		onresult: onresult
	});
}

/* 刷新缓存  */
function flushCache(themeId, portalId) {
	$.ajax({
		url:  URL_FLUSH_CACHE + portalId + "/" + themeId,
		method: "DELETE"
	});
}

/* 查看页面流量  */
function showPageFlowRate() {
    var treeNode = $.T("tree").getActiveTreeNode();
	var portalId = treeNode.getAttribute("portalId");
	var url = URL_GET_FLOW_RATE + portalId;
    popupGrid(url, "PageFlowRate", "查看页面流量", {});
}

/* 组件资源管理 */
function resourceManage() {
    var tree = $.T("tree");
    var treeNode = tree.getActiveTreeNode();
	var name = treeNode.name;
	var code = treeNode.getAttribute("code");

	var params = {
		code:code
	};

	window.showModalDialog("filemanager.html", {params:params, title:"\"" + name + "\"相关资源管理"},"dialogWidth:500px;dialogHeight:400px;");
}


window.onload = init;