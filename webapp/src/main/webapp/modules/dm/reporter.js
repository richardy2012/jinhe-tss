/* 后台响应数据节点名称 */
XML_SOURCE_TREE = "SourceTree";
XML_REPORT_DATA = "ReportData";
XML_SOURCE_INFO = "SourceInfo";

PAGESIZE = 100;

/* XMLHTTP请求地址汇总 */
URL_REPORT_DATA    = NO_AUTH_PATH + "data/";
URL_REPORT_JSON    = NO_AUTH_PATH + "api/json/";
URL_REPORT_EXPORT  = NO_AUTH_PATH + "data/export/";

if(IS_TEST) {
    URL_REPORT_DATA    = "data/report_data.xml?";
    URL_REPORT_JSON    = "data/report_data.json?";
    URL_REPORT_EXPORT  = "data/_success.xml?";  
}

function isReportGroup() {
    return "0" == getTreeAttribute("type");
}

function isReport() {
    return !isTreeRoot() && !isReportGroup();
}

function showGridChart(displayUri, hiddenTree) {
    if(displayUri) {
        $("#grid").hide();
        $("#gridTitle").hide();

        if(hiddenTree) {
            closePalette(); // 关闭左栏
        }
        $("#chatFrame").show().attr("src", displayUri);
    }
    else {
        $("#grid").show();
        $("#gridTitle").show();
        $("#gridTitle .title").html("报表【" + getTreeNodeName() + "】查询结果");
    }
}

var globalValiable = {}; // 用来存放传递给iframe页面的信息

function showReport() {
    var treeNode = getActiveTreeNode();
    var treeID = treeNode.id;
    var displayUri  = (treeNode.getAttribute("displayUri") || "").trim().replace('|', '&'); 
    var paramConfig = (treeNode.getAttribute("param") || "").trim(); 

    globalValiable.title = treeNode.name;

    // 判断当前报表是否专门配置了展示页面
    if( displayUri.length > 0 ) {
        // 如果还配置了参数，则由report页面统一生成查询Form，查询后再打开展示页面里。
        if(paramConfig.length > 0) {
            createQueryForm(treeID, paramConfig, function(dataNode) {
                sendAjax(dataNode);
            });
        }
        else {
            $("#searchForm").html(""); // 删除查询框（其它报表的）
            if(displayUri.indexOf("?type=") > 0) {
                sendAjax(); // 使用通用模板的，有可能此处是不带任何参数的SQL查询
            } 
            else {
                showGridChart(displayUri, true); // 直接打开展示页面
            }
        }
    } 
    else {
        createQueryForm(treeID, paramConfig); // 生成查询Form
    }   

    function sendAjax(searchFormDataNode) {
        var url = getServiceUrl(treeID, displayUri); // 根据服务地址取出数据放在全局变量里
        $.ajax({
            url : url,
            method : "POST",
            formNode : searchFormDataNode,
            type : "json",
            waiting : true,
            ondata : function() { 
                globalValiable.queryParams = this.params;
                globalValiable.data = this.getResponseJSON();
                
                // 数据在iframe里展示
                showGridChart(displayUri, true);
            }
        });
    }
}

function searchReport(treeID, download) {       
    var xform = $.F("searchForm");  
    if( xform && !xform.checkForm() ) return;

    $("#searchFormDiv").hide();
    var searchFormXML = $.cache.XmlDatas["searchFormXML"];

    if(download) {
        var queryString = "?";
        if( searchFormXML ) {
            var nodes = searchFormXML.querySelectorAll("data row *");
            $.each(nodes, function(i, node) {
                if( queryString.length > 1 ) {
                    queryString += "&";
                }
                queryString +=  node.nodeName + "=" + $.XML.getText(node);
            });
        }
        
        // 为防止一次性查询出太多数据导致OOM，限制每次最多只能导出50万行，超出则提示进行分批查询
        $("#downloadFrame").attr( "src", encodeURI(URL_REPORT_EXPORT + treeID + "/1/500000" + queryString) );
        return;
    }
 
    var request = new $.HttpRequest();
    request.url = URL_REPORT_DATA + treeID + "/1/" + PAGESIZE;
    request.waiting = true;
    if( searchFormXML ) {
        var dataNode = searchFormXML.querySelector("data");
        request.setFormContent(dataNode);
    }
    
    request.onresult = function() {
        showGridChart();

        var grid = $.G("grid", this.getNodeValue(XML_REPORT_DATA)); 
        var gridToolBar = $1("gridToolBar");

        var pageListNode = this.getNodeValue(XML_PAGE_INFO);        
        var callback = function(page) {
            request.url = URL_REPORT_DATA + treeID + "/" + page + "/" + PAGESIZE;
            request.onresult = function() {
                $.G("grid", this.getNodeValue(XML_REPORT_DATA)); 
            }               
            request.send();
        };

        $.initGridToolBar(gridToolBar, pageListNode, callback);
        
        grid.el.onScrollToBottom = function () {            
            var currentPage = gridToolBar.getCurrentPage();
            if(currentPage <= gridToolBar.getTotalPages() ) {
                var nextPage = parseInt(currentPage) + 1; 
                request.url = URL_REPORT_DATA + treeID + "/" + nextPage + "/" + PAGESIZE;
                request.onresult = function() {
                    $.G("grid").load(this.getNodeValue(XML_REPORT_DATA), true);
                    $.initGridToolBar(gridToolBar, this.getNodeValue(XML_PAGE_INFO), callback);
                }               
                request.send();
            }
        }
    }
    request.send();
} 

function getServiceUrl(treeID, displayUri) {
    $.Query.init(displayUri);
    var url = $.Query.get("service") || (URL_REPORT_JSON + treeID); // 优先使用展示地址里指定的模板地址
    $.Query.init();

    return url;
}

function createQueryForm(treeID, paramConfig, callback) {
    if( $.cache.Variables["treeNodeID_SF"] == treeID && $.funcCompare($.cache.Variables["callback_SF"], callback) ) {
        Element.show($1("searchFormDiv"));  // 如果上一次打开的和本次打开的是同一报表的查询框，则直接显示
        return;
    }

    var buttonBox = [];
    buttonBox[buttonBox.length] = "        <TR>";
    buttonBox[buttonBox.length] = "          <TD colspan='2' height='46'><div class='buttonBox'>";
    buttonBox[buttonBox.length] = "             <input type='button' class='btStrong' id='btSearch' value='查询'/> - ";
    buttonBox[buttonBox.length] = "             <input type='button' class='btStrongL' id='btDownload' value='查询并导出'/> - ";
    buttonBox[buttonBox.length] = "             <input type='button' class='btWeak' id='btCloseSearchForm' value='关闭'/>";
    buttonBox[buttonBox.length] = "          </div></TD>";
    buttonBox[buttonBox.length] = "        </TR>";

    var searchForm = $.json2Form("searchForm", paramConfig, buttonBox.join(""));

    Element.show($1("searchFormDiv"));
    $("#reportName").html("查询报表【" + getTreeNodeName() + "】");

    $.cache.XmlDatas["searchFormXML"] = searchForm.template.sourceXML;
    $.cache.Variables["treeNodeID_SF"] = treeID;
    $.cache.Variables["callback_SF"] = callback;
    
    $1("btSearch").onclick = function () {
        if(callback) {
            if( searchForm.checkForm() ) {
                $("#searchFormDiv").hide();
                var searchFormXML = $.cache.XmlDatas["searchFormXML"];
                var dataNode = searchFormXML.querySelector("data");
                callback(dataNode); // 在回调函数里读取数据并展示
            }
        } 
        else {
            searchReport(treeID, false);
        }
    }
    $1("btDownload").onclick = function () {
        searchReport(treeID, true);
    }
    $1("btCloseSearchForm").onclick = function () {
        $("#searchFormDiv").hide();
    }
}

// ------------------------------------------------- 多级下拉选择联动 ------------------------------------------------
/*
 *  多级下拉选择联动
 *  参数： nextIndex    下一级联动参数的序号（1->n）
        serviceID       下一级联动的service地址             
        currParam       当前联动参数的序号
        currParamValue  当前联动参数的值
 */
function getNextLevelOption(nextIndex, serviceID, currParam, currParamValue) {
    if(nextIndex == null || serviceID == null || currParam == null || $.isNullOrEmpty(currParamValue)) return;

    var dreg = /^[1-9]+[0-9]*]*$/;
    var paramElementId = "param" + nextIndex;
 
    var paramName = dreg.test(currParam) ? "param" + currParam : currParam;
    
    // serviceID maybe is ID of report, maybe a serviceUrl
    var url = dreg.test(serviceID) ? '../../display/json/' + serviceID : serviceID;

    $.getNextLevelOption($.F("searchForm"), paramName, currParamValue, url, paramElementId);
}