    /*
     *	后台响应数据节点名称
     */
    XML_MAIN_TREE = "ArticleTree";
    /*
     *	默认唯一编号名前缀
     */
    CACHE_MAIN_TREE = "tree__id";
    /*
     *	已发布文章流程状态常量
     */
    PUBLISHED_ARTICLE_STATUS = 4;
    /*
     *	XMLHTTP请求地址汇总
     */
    URL_ARTICLE_DETAIL = "data/article.xml";
    URL_NEW_ARTICLE_DETAIL = "data/article.xml";
    URL_UPLOAD_FILE = "data/upload1.htm";
    URL_UPLOAD_SERVER_FILE = "data/upload2.htm";
    URL_SAVE_ARTICLE = "data/_success.xml";
    URL_SAVE_PUBLISH_ARTICLE = "data/_success.xml";

    URL_ARTICLE_DETAIL = "article!getArticleInfo.action";
    URL_NEW_ARTICLE_DETAIL = "article!initArticleInfo.action";
    URL_UPLOAD_FILE = "upload!execute.action";
    URL_UPLOAD_SERVER_FILE = "upload!execute.action";
    URL_SAVE_ARTICLE = "article!saveArticleInfo.action";
    URL_SAVE_PUBLISH_ARTICLE = "article!saveArticleInfo.action";

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
        var isNew = window.dialogArguments.isNew;
        var editable = window.dialogArguments.editable;
        var channelId = window.dialogArguments.channelId;
        var articleType = window.dialogArguments.articleType;
        var articleId = window.dialogArguments.articleId;
        var workflowStatus = window.dialogArguments.workflowStatus;

        var p = new HttpRequestParams();
        //如果是新增
        if(true==isNew){
            p.url = URL_NEW_ARTICLE_DETAIL;
            p.setContent("channelId", channelId);
            p.setContent("articleKind",articleType);
        }else{
            p.url = URL_ARTICLE_DETAIL;
            p.setContent("articleId", articleId);
            p.setContent("channelId", articleType);
        }

        var request = new HttpRequest(p);
        request.onresult = function(){
            var articleInfoNode = this.getNodeValue("ArticleInfo");
            var articleContentNode = this.getNodeValue("ArticleContent");
            var attachsUploadNode = this.getNodeValue("AttachsUpload");
            var attachsListNode = this.getNodeValue("AttachsList");
            var preparePublishNode = this.getNodeValue("PreparedPublish");

            //分发、转载、导入文章禁止编辑保存
            setArticlePagesEditable(articleType,workflowStatus,editable);

            var page1FormObj = $("page1Form");
            Public.initHTC(page1FormObj,"isLoaded","oncomponentready",function(){
                loadArticleInfoFormData(articleInfoNode);
            });

            var page2EditorObj = $("page2Editor");
            Public.initHTC(page2EditorObj,"isLoaded","onload",function(){
                loadArticleContentData(articleContentNode);
            });

            var page3FormObj = $("page3Form");
            Public.initHTC(page3FormObj,"isLoaded","oncomponentready",function(){
                loadAttachsUploadFormData(attachsUploadNode);
            });

            var page3GridObj = $("page3Grid");
            Public.initHTC(page3GridObj,"isLoaded","onload",function(){
                loadAttachsListGridData(attachsListNode);
            });

            $("title1").onclick = collapse;
            $("title2").onclick = collapse;
            $("title3").onclick = collapse;
            $("page3BtDel").onclick  = delPage3GridRow;
            $("btSave").onclick  = saveArticle;
            $("btSaveAndPublish").onclick  = saveAndPublishArticle;
        }
        request.send();
    }
    /*
     *	函数说明：设置文章相关页是否禁止编辑保存功能
     *	参数：	string:articleType          文章类型(0表示普通文章/1分发/2转载/3导入)
                boolean:editable            是否可编辑(默认true)
                string:workflowStatus       流程状态
     *	返回值：
     */
    function setArticlePagesEditable(articleType,workflowStatus,editable){
        var page1FormObj = $("page1Form");
        var page3FormObj = $("page3Form");

        var btSaveObj = $("btSave");
        var btSaveAndPublishObj = $("btSaveAndPublish");

        var page3BtDel = $("page3BtDel");

        workflowStatus = parseInt(workflowStatus);
        if(("0"!=articleType && "2"!=articleType)|| PUBLISHED_ARTICLE_STATUS<=workflowStatus || -1==workflowStatus || false==editable){
			page1FormObj.editable = "false";
            page3FormObj.editable = "false";
            btSaveObj.disabled = true;
            btSaveAndPublishObj.disabled = true;
            page3BtDel.disabled = true;
        }else{
            page1FormObj.editable = "true";
            page3FormObj.editable = "true";
            btSaveObj.disabled = false;
            btSaveAndPublishObj.disabled = false;
            page3BtDel.disabled = false;
        }    
    }
    /*
     *	函数说明：展开折叠
     *	参数：	
     *	返回值：
     */
    function collapse(obj,display){
        if(null==obj){
            obj = this;
            var display  = obj.parentNode.nextSibling.style.display=="none"?"":"none";
        }
        var text = display=="none"?"3":"6";

        obj.parentNode.nextSibling.style.display = display;
        obj.childNodes[1].innerHTML = text;

        if(display=="" && obj.id=="title3"){
            obj.parentNode.parentNode.parentNode.parentNode.scrollTop = Element.absTop(obj);
        }
    }
    /*
     *	函数说明：文章信息xform加载数据
     *	参数：	XmlNode:data       XmlNode实例
     *	返回值：
     */
    function loadArticleInfoFormData(data){
        if(null!=data){
            var page1FormObj = $("page1Form");
            page1FormObj.load(data.node,null,"node");
            //loadPage1FormEvents("article",cacheID);
        }
    }
    /*
     *	函数说明：文章正文editor加载数据
     *	参数：	XmlNode:data       XmlNode实例
     *	返回值：
     */
    function loadArticleContentData(data){
        if(null!=data){
            var page2EditorObj = $("page2Editor");
            page2EditorObj.load(data);
        }
    }
    /*
     *	函数说明：图片附件上传xform加载数据
     *	参数：	XmlNode:data       XmlNode实例
     *	返回值：
     */
    function loadAttachsUploadFormData(data){
        if(null!=data){
            var page3FormObj = $("page3Form");
            page3FormObj.load(data.node,null,"node");

            page3FormObj.ondatachange = function(){
                var name = event.result.name;
                var newValue = event.result.newValue;

                var targetName = null;
                var reload = false;

                switch(name){
                    case "switch1":
                        targetName = "switch2";

                        //设置本地附件可编辑
                        page3FormObj.setColumnEditable("file","true");
                        page3FormObj.setColumnEditable("serverAttach","false");

                        //清空服务器附件
                        page3FormObj.updateDataExternal("serverAttach","");

                        reload = true;
                        break;
                    case "switch2":
                        targetName = "switch1";

                        //设置服务器附件可编辑
                        page3FormObj.setColumnEditable("file","false");
                        page3FormObj.setColumnEditable("serverAttach","true");

                        //清空本地附件
                        page3FormObj.updateDataExternal("file","");

                        reload = true;
                        break;
                    default:
                        //2007-3-1 离开提醒
                        //attachReminder(cacheID);
                        break;
                }

                if(null!=targetName){
                    page3FormObj.updateDataExternal(targetName,"0",false);
                }
                if(true==reload){
                    page3FormObj.reload();                
                }
            }
            if("false"!=page3FormObj.editable){
                page3FormObj.updateDataExternal("switch1","1");
            }
        }
    }
    /*
     *	函数说明：上传文件
     *	参数： 
     *	返回值：
     */
    function upload(){
        var page3FormObj = $("page3Form");
        var file = page3FormObj.getData("file")||"";
        var serverAttach = page3FormObj.getData("serverAttach")||"";
        var switch1 = page3FormObj.getData("switch1");
        var switch2 = page3FormObj.getData("switch2");
        var type = page3FormObj.getData("type");

        var flag = true;
        var errorColumn = null;
        var errorInfo = "";        

        if("1"==switch1){
            if(""==file){
                if(null==errorColumn){
                    errorColumn = "file";
                    errorInfo = "未选择文件";
                }
                flag = false;
            }else if("1"==type){
                var ext = file.substring(file.lastIndexOf(".")+1).toLowerCase();
                if(-1==("gif,jpg,bmp,png").indexOf(ext)){
                    errorColumn = "file";
                    errorInfo = "不支持的图片格式(gif,jpg,bmp,png)，请重新选择";
                    //文件后缀错误，强制抛异常
                    flag = false;
                }            
            }
        }else{
            if(""==serverAttach){
                if(null==errorColumn){
                    errorColumn = "serverAttach";
                    errorInfo = "未输入文件路径";
                }
                flag = false;
            }else if("1"==type){
                var ext = serverAttach.substring(serverAttach.lastIndexOf(".")+1).toLowerCase();
                if(-1==("gif,jpg,bmp,png").indexOf(ext)){
                    errorColumn = "serverAttach";
                    errorInfo = "不支持的图片格式(gif,jpg,bmp,png)，请重新选择";
                    //文件后缀错误，强制抛异常
                    flag = false;
                }            
            }
        }
        if(false==flag){
            page3FormObj.showCustomErrorInfo(errorColumn,errorInfo);
            return;
        }

        if(true==page3FormObj.checkForm()){
            var page3FormObj = $("page3Form");
            var articleId = page3FormObj.getData("id");
            if(articleId==null) {
                articleId = "0";
            }
            //var channelId = page3FormObj.getData("channelId");
			var channelId = window.dialogArguments.channelId;
            page3FormObj.updateDataExternal("channelId", channelId);
            page3FormObj.updateDataExternal("articleId", articleId);

            if("1"==switch1){
                page3FormObj.upload(URL_UPLOAD_FILE);
            }else{
                page3FormObj.upload(URL_UPLOAD_SERVER_FILE);
            }
        }
    }
    /*
     *	函数说明：清空上传表单
     *	参数： 
     *	返回值：
     */
    function resetUpload(){
        var page3FormObj = $("page3Form");
        page3FormObj.updateDataExternal("name","");
        page3FormObj.updateDataExternal("file","");
        page3FormObj.updateDataExternal("serverAttach","");
        page3FormObj.reload();
    }
    /*
     *	函数说明：图片附件grid加载数据
     *	参数：	XmlNode:data       XmlNode实例
     *	返回值：
     */
    function loadAttachsListGridData(data){
        if(null!=data){
            var page3GridObj = $("page3Grid");
            page3GridObj.load(data.node,null,"node");
        }
    }
    /*
     *	函数说明：删除page3里grid的行
     *	参数：	
     *	返回值：
     */
    function delPage3GridRow(){
        var page3GridObj = $("page3Grid");
        var selectedIndex = page3GridObj.getSelectedRowsIndexArray_Xml();
        for(var i=0,iLen=selectedIndex.length;i<iLen;i++){
            var curRowIndex = selectedIndex[i];
            var refresh = i<iLen-1?false:true;
            page3GridObj.delRow_Xml(curRowIndex,null,refresh);
        }
    }
    /*
     *	函数说明：添加附件
     *	参数： 
     *	返回值：
     */
    function addAttachments(seqNo, type, uploadName,articleId) {
        Public.hideWaitingLayer();

        var page3FormObj = $("page3Form");
        var name = page3FormObj.getData("name");

        var file = page3FormObj.getData("file");
        if(file != null && file != "") {
            var fileExtendName = file.substring(file.lastIndexOf(".")+1).toLowerCase();
            var fileName = file.substring(file.lastIndexOf("\\")+1, file.lastIndexOf("."));
            var page3GridObj = $("page3Grid");
            var rowIndex = page3GridObj.addRow();
            page3GridObj.modifyNamedNode_Xml(rowIndex, "seqNo", seqNo);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "articleId", articleId);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "name", name);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "fileName", fileName);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "fileExt", fileExtendName);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "uploadName", uploadName);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "type", type);
        }
        var serverAttach = page3FormObj.getData("serverAttach");
        if(serverAttach != null && serverAttach != "") {
            var fileExtendName = serverAttach.substring(serverAttach.lastIndexOf(".")+1).toLowerCase();
            var fileName = serverAttach.substring(serverAttach.lastIndexOf("\\")+1, serverAttach.lastIndexOf("."));
            var page3GridObj = $("page3Grid");
            var rowIndex = page3GridObj.addRow();
            page3GridObj.modifyNamedNode_Xml(rowIndex, "seqNo", seqNo);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "articleId", articleId);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "name", name);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "fileName", fileName);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "fileExt", fileExtendName);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "uploadName", uploadName);
            page3GridObj.modifyNamedNode_Xml(rowIndex, "type", type);
        }
    }
    /*
     *	函数说明：保存文章
     *	返回值：
     */
    function saveArticle(){
        var isNew = window.dialogArguments.isNew;
        var editable = window.dialogArguments.editable;
        var channelId = window.dialogArguments.channelId;
        var articleType = window.dialogArguments.articleType;
        var articleId = window.dialogArguments.articleId;
        var workflowStatus = window.dialogArguments.workflowStatus;

        var page1FormObj = $("page1Form");
        var page2EditorObj = $("page2Editor");
        var page3FormObj = $("page3Form");
        var page3GridObj = $("page3Grid");

        //校验page1Form数据有效性
        if(false==page1FormObj.checkForm()){
            collapse($("title1"),"");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_ARTICLE;
              
        p.setContent("channelId", channelId);
        //文章基本信息
        var articleInfoNode = new XmlNode(page1FormObj.getXmlDocument());
        if(null!=articleInfoNode){
            var articleInfoDataNode = articleInfoNode.selectSingleNode(".//data");
            if(null!=articleInfoDataNode){
                flag = true;

                var prefix = articleInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                p.setXFormContent(articleInfoDataNode,prefix);
            }
        }

		//获取临时文章id
		var attachsUploadNode = new XmlNode(page3FormObj.getXmlDocument());
		var attachsUploadRowNode = attachsUploadNode.selectSingleNode(".//data//row");
		var articleId = attachsUploadRowNode.getCDATA("id");
		p.setContent("articleId",articleId);

		//文章正文
		var articleContent = "";
		var page2EditorObj = $("page2Editor");
		if(true==page2EditorObj.isLoaded){
			articleContent = page2EditorObj.getSourceHTML();
		}else{
			
		}
		p.setContent("ArticleContent",articleContent);

		//文章图片附件
		var attachsListNode = new XmlNode(page3GridObj.getXmlDocument());
		if(null!=attachsListNode){
			var attachsListDataIDs = [];
			var condition = "";
			if(true == isNew) {
				condition = "[@_new and not(@_delete)]";
			} else {
				condition = "[@_delete='true']";
			}
			var attachsListDataNodes = attachsListNode.selectNodes(".//data//row"+condition);
			for(var i=0,iLen=attachsListDataNodes.length;i<iLen;i++){
				var curNode = attachsListDataNodes[i];
				var curNodeID = curNode.getAttribute("seqNo");
				if(null!=curNodeID){
					attachsListDataIDs.push(parseInt(curNodeID));
				}
			}
			if(0<attachsListDataIDs.length){
				p.setContent("attachList",attachsListDataIDs.join(","));
			}
		}

        var request = new HttpRequest(p);
        //同步按钮状态
        var btSaveObj = $("btSave");
        syncButton([btSaveObj],request);

        request.onsuccess = function(){
            window.returnValue = true;
            window.close();
        }
        request.send();
    }
    /*
     *	函数说明：保存并发布文章
     *	返回值：
     */
    function saveAndPublishArticle(){
        var isNew = window.dialogArguments.isNew;
        var editable = window.dialogArguments.editable;
        var channelId = window.dialogArguments.channelId;
        var articleType = window.dialogArguments.articleType;
        var articleId = window.dialogArguments.articleId;
        var workflowStatus = window.dialogArguments.workflowStatus;

        var page1FormObj = $("page1Form");
        var page2EditorObj = $("page2Editor");
        var page3FormObj = $("page3Form");
        var page3GridObj = $("page3Grid");

        //校验page1Form数据有效性
        if(false==page1FormObj.checkForm()){
            collapse($("title1"),"");
            return;
        }

        var p = new HttpRequestParams();
        p.url = URL_SAVE_PUBLISH_ARTICLE;
          
		p.setContent("isCommit", "true");
        p.setContent("channelId", channelId);
        //文章基本信息
        var articleInfoNode = new XmlNode(page1FormObj.getXmlDocument());
        if(null!=articleInfoNode){
            var articleInfoDataNode = articleInfoNode.selectSingleNode(".//data");
            if(null!=articleInfoDataNode){
                var prefix = articleInfoNode.selectSingleNode("./declare").getAttribute("prefix");
                p.setXFormContent(articleInfoDataNode,prefix);
            }
        }

		//获取临时文章id
		var attachsUploadNode = new XmlNode(page3FormObj.getXmlDocument());
		var attachsUploadRowNode = attachsUploadNode.selectSingleNode(".//data//row");
		var articleId = attachsUploadRowNode.getCDATA("id");
		p.setContent("articleId",articleId);

		//文章正文
		var articleContent = "";
		var page2EditorObj = $("page2Editor");
		if(true==page2EditorObj.isLoaded){
			articleContent = page2EditorObj.getSourceHTML();
		}else{

		}
		p.setContent("ArticleContent",articleContent);
	  
		//文章图片附件
		var attachsListNode = new XmlNode(page3GridObj.getXmlDocument());
		if(null!=attachsListNode){
			var attachsListDataIDs = [];
			var condition = "";
			if(true == isNew) {
				condition = "[@_new and not(@_delete)]";
			} else {
				condition = "[@_delete='true']";
			}
			var attachsListDataNodes = attachsListNode.selectNodes(".//data//row"+condition);
			for(var i=0,iLen=attachsListDataNodes.length;i<iLen;i++){
				var curNode = attachsListDataNodes[i];
				var curNodeID = curNode.getAttribute("seqNo");
				if(null!=curNodeID){
					attachsListDataIDs.push(parseInt(curNodeID));
				}
			}
			if(0<attachsListDataIDs.length){
				p.setContent("attachList",attachsListDataIDs.join(","));
			}
		}

        var request = new HttpRequest(p);
        //同步按钮状态
        var btSaveAndPublishObj = $("btSaveAndPublish");
        syncButton([btSaveAndPublishObj],request);

        request.onsuccess = function(){
            window.returnValue = true;
            window.close();
        }
        request.send();
    }
    /*
     *	函数说明：编辑器插入图片
     *	参数：	
     *	返回值：
     */
    function insertImage(){
        var page3GridObj = $("page3Grid");
        var attachs = new XmlNode(page3GridObj.getXmlDocument()).cloneNode(true);
        var returnValue = window.showModalDialog("files.htm",{attachs:attachs,type:"image"},"dialogWidth:500px;dialogHeight:400px;status:no; help:no;resizable:no;unadorned:yes");
        if(null!=returnValue){
            var editObj = $("page2Editor");
            editObj.insertHTML("<img src=\"" + returnValue[0].src + "\">","",true);
        }
    }
    /*
     *	函数说明：编辑器插入flash
     *	参数：	
     *	返回值：
     */
    function insertFlash(){
        var page3GridObj = $("page3Grid");
        var attachs = new XmlNode(page3GridObj.getXmlDocument()).cloneNode(true);
        var returnValue = window.showModalDialog("files.htm",{attachs:attachs,type:"flash"},"dialogWidth:500px;dialogHeight:400px;status:no; help:no;resizable:no;unadorned:yes");
        if(null!=returnValue){
            var str = [];
            str[str.length] = "<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" ";
            str[str.length] = "id=\"flashplayer\" ";
            str[str.length] = "codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" ";
            str[str.length] = "width=\"400\" ";
            str[str.length] = "height=\"300\">";
            str[str.length] = "<param name=\"movie\" value=\"" + returnValue[0].src + "\">";
            str[str.length] = "<param name=\"quality\" value=\"high\">";
            str[str.length] = "<embed src=\"" + returnValue[0].src + "\" ";
            str[str.length] = "quality=\"high\" ";
            str[str.length] = "pluginspage=\"http://www.macromedia.com/go/getflashplayer\" ";
            str[str.length] = "type=\"application/x-shockwave-flash\" ";
            str[str.length] = "width=\"400\" ";
            str[str.length] = "height=\"300\">";
            str[str.length] = "</embed>";
            str[str.length] = "</object>";

            var editObj = $("page2Editor");
            editObj.insertHTML(str.join(""),"",true);
        }
    }
    /*
     *	函数说明：编辑器插入视频
     *	参数：	
     *	返回值：
     */
    function insertVideo(){
        var page3GridObj = $("page3Grid");
        var attachs = new XmlNode(page3GridObj.getXmlDocument()).cloneNode(true);
        var returnValue = window.showModalDialog("files.htm",{attachs:attachs,type:"video"},"dialogWidth:500px;dialogHeight:400px;status:no; help:no;resizable:no;unadorned:yes");
        if(null!=returnValue){
            var str = [];
            str[str.length] = "<object id=\"MediaPlayer\"";
            str[str.length] = "width=\"400\"";
            str[str.length] = "height=\"300\"";
            str[str.length] = "classid=\"CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6\"";
            str[str.length] = "codebase=\"http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701\"";
            str[str.length] = "type=\"application/x-oleobject\">";

            str[str.length] = "<param name=\"URL\" value=\"" + returnValue[0].src + "\">";
            str[str.length] = "<param name=DisplaySize value=0>";
            str[str.length] = "<param name=AllowChangeDisplaySize value=\"False\">";
            str[str.length] = "<param name=ShowControls VALUE=1>";
            str[str.length] = "<param name=ShowDisplay VALUE=0>";
            str[str.length] = "<param name=ShowStatusBar VALUE=1>";
            str[str.length] = "<param name=AutoStart VALUE=1>";
            
            str[str.length] = "<embed name=\"MediaPlayer\"";
            str[str.length] = "width=\"400\"";
            str[str.length] = "height=\"300\"";
            str[str.length] = "type=\"application/x-mplayer2\"";
            str[str.length] = "pluginspage=\"http://www.microsoft.com/Windows/Downloads/Contents/Products/MediaPlayer/\"";
            str[str.length] = "src=\"" + returnValue[0].src + "\"";
            str[str.length] = "AllowChangeDisplaySize=\"False\"";
            str[str.length] = "ShowControls=\"1\"";
            str[str.length] = "AutoStart=\"1\"";
            str[str.length] = "ShowDisplay=\"0\">";
            str[str.length] = "</embed>";
            str[str.length] = "</object>";

            var editObj = $("page2Editor");
            editObj.insertHTML(str.join(""),"",true);
        }
    }
    /*
     *	函数说明：编辑器插入服务器文件链接
     *	参数：	
     *	返回值：
     */
    function insertFile(){
        var page3GridObj = $("page3Grid");
        var attachs = new XmlNode(page3GridObj.getXmlDocument()).cloneNode(true);
        var returnValue = window.showModalDialog("files.htm",{attachs:attachs,type:"file"},"dialogWidth:500px;dialogHeight:400px;status:no; help:no;resizable:no;unadorned:yes");
        if(null!=returnValue){
            var str = "<a href=\"" + returnValue[0].src + "\" target=\"_blank\">";
            var editObj = $("page2Editor");
            editObj.insertHTML(str,"</a>",false);
        }
    }

    window.onload = init;