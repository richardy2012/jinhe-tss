    /*
     *	对象名称：Display
     *	职责：负责生成整体界面显示
     *
     */
    function Display(){
        this.tabBox = null;
        this.tabController = null;
        this.phaseBox = null;
        this.phaseController = null;
        this.rightBox = null;

        this.tabs = {};
        this.buttons = {};
        this.pages = {};

        this.init();
    }
    /*
     *	函数说明：Display初始化
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.init = function(){
        this.getAllPages();
        this.hideAllPages();
        this.createUI();
    }
    /*
     *	函数说明：获取所有子页面
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.getAllPages = function(){
        var childs = element.getElementsByTagName(_WORKSPACE_TAG_NAME_PAGE);
        for(var i=0;i<childs.length;i++){
            var curNode = childs[i];
            var curNodeId = curNode.id||curNode.uniqueID;
            this.pages[curNodeId] = new Page(curNode);
        }
    }
    /*
     *	函数说明：隐藏所有子页面
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.hideAllPages = function(){
        for(var item in this.pages){
            var curPage = this.pages[item];
            if(true==curPage.isActive){
                curPage.hide();
            }
        }
    }
    /*
     *	函数说明：创建界面展示
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.createUI = function(){
        this.createTabBox();
        this.createRightBox();
        this.createPhaseBox();
		this.hideRightBox();
    }
    /*
     *	函数说明：创建Tab标签的容器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.createTabBox = function(){
        var tabBox = this.createElement(_WORKSPACE_TAG_NAME_TAB_BOX,_WORKSPACE_NAMESPACE);
        var nobr = this.createElement(_WORKSPACE_TAG_NAME_NOBR);

        tabBox.appendChild(nobr);
        element.appendChild(tabBox);
        var refChild = element.firstChild;
        if(refChild!=tabBox){
            element.insertBefore(tabBox,refChild);
        }

        this.tabBox = tabBox;
    }
    /*
     *	函数说明：设置有Tab标签的容器样式
     *	参数：boolean:hasTab     容器是否有Tab
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.setHasTabStyle = function(hasTab){
        if(true==hasTab){
            this.tabBox.className = _CLASS_NAME_TAB_BOX_HAS_TAB;
        }else{
            this.tabBox.className = _CLASS_NAME_NO_CLASS;
        }
    }
    /*
     *	函数说明：刷新Tab标签控制器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.refreshTabController = function(){
        if(null==this.tabController){
            this.createTabController();
            this.createTabControllerButtons();
        }
        this.refreshTabControllerButtons();
    }
    /*
     *	函数说明：创建Tab标签控制器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.createTabController = function(){
        var tabController = this.createElement(_WORKSPACE_TAG_NAME_TAB_CONTROLLER,_WORKSPACE_NAMESPACE);

        element.appendChild(tabController);
        var refChild = element.childNodes[1];
        if(tabController!=refChild && null!=refChild){
            element.insertBefore(tabController,refChild);
        }

        this.tabController = tabController;
    }
    /*
     *	函数说明：清除Tab标签控制器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.clearTabController = function(){
        for(var item in this.buttons){
            var curCtrlBt = this.buttons[item];
            if(curCtrlBt.type==_TYPE_TAB_CONTROLLER_BT){
                curCtrlBt.dispose();
            }
        }

        if(null!=this.tabController){
            this.tabController.removeNode(true);
            this.tabController = null;
        }
    }
    /*
     *	函数说明：创建Tab标签控制器按钮
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.createTabControllerButtons = function(){
        var firstBt = new ControllerButton(_TYPE_TAB_CONTROLLER_BT,_IMG_TAB_FIRST);
        var prevBt = new ControllerButton(_TYPE_TAB_CONTROLLER_BT,_IMG_TAB_PREV);
        var nextBt = new ControllerButton(_TYPE_TAB_CONTROLLER_BT,_IMG_TAB_NEXT);
        var lastBt = new ControllerButton(_TYPE_TAB_CONTROLLER_BT,_IMG_TAB_LAST);

        this.buttons[firstBt.uniqueID] = firstBt;
        this.buttons[prevBt.uniqueID] = prevBt;
        this.buttons[nextBt.uniqueID] = nextBt;
        this.buttons[lastBt.uniqueID] = lastBt;

        firstBt.dockTo(this.tabController);
        prevBt.dockTo(this.tabController);
        nextBt.dockTo(this.tabController);
        lastBt.dockTo(this.tabController);

        firstBt.linkTo(function(){
            _display.tabBox.scrollLeft = 0;
        });
        prevBt.linkTo(function(){
            _display.tabBox.scrollLeft -= _SIZE_TAB_WIDTH + _SIZE_TAB_MARGIN_LEFT;
        });
        nextBt.linkTo(function(){
            _display.tabBox.scrollLeft += _SIZE_TAB_WIDTH + _SIZE_TAB_MARGIN_LEFT;
        });
        lastBt.linkTo(function(){
            _display.tabBox.scrollLeft = _display.tabBox.scrollWidth;
        });
    }
    /*
     *	函数说明：刷新Tab标签控制器按钮
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.refreshTabControllerButtons = function(){
        var flag = (this.tabBox.scrollWidth>this.tabBox.offsetWidth);
        for(var item in this.buttons){
            var curCtrlBt = this.buttons[item];
            if(curCtrlBt.type==_TYPE_TAB_CONTROLLER_BT){
                curCtrlBt.setEnable(flag);
            }
        }
    }
    /*
     *	函数说明：创建右侧容器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.createRightBox = function(){
        var rightBox = this.createElement(_WORKSPACE_TAG_NAME_TABLE);
        rightBox.cellSpacing = 0;
        rightBox.cellPadding = 0;
        rightBox.border = 0;
        rightBox.className = _CLASS_NAME_RIGHT_BOX;

        for(var i=0;i<2;i++){
          var row = rightBox.insertRow();
          row.insertCell();
        }
        rightBox.rows(1).height = _SIZE_PHASE_CONTROLLER_HEIGHT;

        element.appendChild(rightBox);
        var refChild = element.childNodes[1];
        if(rightBox!=refChild && null!=refChild){
            element.insertBefore(rightBox,refChild);
        }

        this.rightBox = rightBox;
    }
    /*
     *	函数说明：显示右侧容器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.showRightBox = function(){
        this.rightBox.style.display = "inline";
    }
    /*
     *	函数说明：隐藏右侧容器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.hideRightBox = function(){
        this.rightBox.style.display = "none";
    }
    /*
     *	函数说明：创建纵向Tab标签的容器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.createPhaseBox = function(){
        var phaseBox = this.createElement(_WORKSPACE_TAG_NAME_PHASE_BOX,_WORKSPACE_NAMESPACE);

        this.rightBox.rows(0).cells(0).appendChild(phaseBox);

        this.phaseBox = phaseBox;
    }
    /*
     *	函数说明：刷新Phase标签控制器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.refreshPhaseController = function(){
        if(null==this.phaseController){
            this.createPhaseController();
            this.createPhaseControllerButtons();
        }
        this.refreshPhaseControllerButtons();
		this.showRightBox();
    }
    /*
     *	函数说明：创建Phase标签控制器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    Display.prototype.createPhaseController = function(){
        var phaseController = this.createElement(_WORKSPACE_TAG_NAME_PHASE_CONTROLLER,_WORKSPACE_NAMESPACE);

        this.rightBox.rows(1).cells(0).appendChild(phaseController);

        this.phaseController = phaseController;
    }
    /*
     *	函数说明：清除Phase标签控制器
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Display.prototype.clearPhaseController = function(){
        for(var item in this.buttons){
            var curCtrlBt = this.buttons[item];
            if(curCtrlBt.type==_TYPE_PHASE_CONTROLLER_BT){
                curCtrlBt.dispose();
            }
        }

        if(null!=this.phaseController){
            this.phaseController.removeNode(true);
            this.phaseController = null;
        }
		this.hideRightBox();
    }
    /*
     *	函数说明：创建Phase标签控制器按钮
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.createPhaseControllerButtons = function(){
        var firstBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT,_IMG_PHASE_FIRST);
        var prevBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT,_IMG_PHASE_PREV);
        var nextBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT,_IMG_PHASE_NEXT);
        var lastBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT,_IMG_PHASE_LAST);

        this.buttons[firstBt.uniqueID] = firstBt;
        this.buttons[prevBt.uniqueID] = prevBt;
        this.buttons[nextBt.uniqueID] = nextBt;
        this.buttons[lastBt.uniqueID] = lastBt;

        firstBt.dockTo(this.phaseController);
        prevBt.dockTo(this.phaseController);
        nextBt.dockTo(this.phaseController);
        lastBt.dockTo(this.phaseController);

        firstBt.linkTo(function(){
            _display.phaseBox.scrollTop = 0;
        });
        prevBt.linkTo(function(){
            _display.phaseBox.scrollTop -= _SIZE_PHASE_HEIGHT + _SIZE_PHASE_MARGIN_TOP;
        });
        nextBt.linkTo(function(){
            _display.phaseBox.scrollTop += _SIZE_PHASE_HEIGHT + _SIZE_PHASE_MARGIN_TOP;
        });
        lastBt.linkTo(function(){
            _display.phaseBox.scrollTop = _display.phaseBox.scrollHeight;
        });
    }
    /*
     *	函数说明：刷新Phase标签控制器按钮
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.refreshPhaseControllerButtons = function(){
        var flag = (this.phaseBox.scrollHeight>this.phaseBox.offsetHeight);
        for(var item in this.buttons){
            var curCtrlBt = this.buttons[item];
            if(curCtrlBt.type==_TYPE_PHASE_CONTROLLER_BT){
                curCtrlBt.setEnable(flag);
            }
        }
    }
    /*
     *	函数说明：打开一个新子页
     *	参数：	object:inf      配置参数
     *	返回值：Tab:tab    Tab标签实例
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.open = function(inf){
        var id = inf.defaultPage;
        var label = inf.label;
        var phases = inf.phases;
        var callback = inf.callback;
        var SID = inf.SID;

        var curActiveTab = this.getActiveTab();

        var tab = this.getTabBySID(SID);
        if(null==tab){//不存在同一数据源tab则新建
            var page = this.getPage(id);
            tab = new Tab(label,phases,callback);
            this.tabs[tab.uniqueID] = tab;

            tab.linkTo(page);
            tab.dockTo(this.tabBox.firstChild);
            tab.SID = SID;//标记数据源

            this.refreshTabController();
            this.setHasTabStyle(true);
        }
        _ontabchange(tab,curActiveTab);

		return tab;
    }
    /*
     *	函数说明：根据id查找子页
     *	参数：	string:id		子页id
     *	返回值：Page实例
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.getPage = function(id){
        return this.pages[id];
    }
    /*
     *	函数说明：显示子页面
     *	参数：	Page:page		Page实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Display.prototype.showPage = function(page){
        this.hideAllPages();
        page.show();
    }
    /*
     *	函数说明：关闭（隐藏）子页面
     *	参数：	Page:page		Page实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Display.prototype.hidePage = function(page){
        page.hide();
    }
    /*
     *	函数说明：创建带命名空间的对象
     *	参数：	string:tagName		对象标签名
                string:ns			命名空间
     *	返回值：object	html对象
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.createElement = function(tagName,ns){
        var obj = null;
        if(null==ns){
            obj = document.createElement(tagName);
        }else{
            var tempDiv = document.createElement("DIV");
            tempDiv.innerHTML = "<"+ns+":"+tagName+"/>";
            obj = tempDiv.firstChild.cloneNode(false);
            tempDiv.removeNode(true);
        }
        return obj;

    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.toString = function(){
        var str = [];
        str[str.length] = "[Display 对象]";
        return str.join("\r\n");
    }
    /*
     *	函数说明：切换到指定Tab页
     *	参数：Tab:tab   Tab实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.switchToTab = function(tab){
        if(null!=tab){
            //tab.click();
            _ontabchange(tab,this.getActiveTab());
        }else{
            this.clearTabController();
            this.clearPhaseController();
            this.setHasTabStyle(false);
        }
        this.refreshTabControllerButtons();
    }
    /*
     *	函数说明：获得第一个Tab
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.getFirstTab = function(){
        var tab = null;
        for(var item in this.tabs){
            tab = this.tabs[item];
            break;
        }
        return tab;
    }
    /*
     *	函数说明：获得最后一个Tab
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.getLastTab = function(){
        var tab = null;
        for(var item in this.tabs){
        }
        tab = this.tabs[item];
        return tab;
    }
    /*
     *	函数说明：低亮所有标签
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Display.prototype.inactiveAllTabs = function(){
        for(var item in this.tabs){
            var curTab = this.tabs[item];
            curTab.inactive();
        }
    }
    /*
     *	函数说明：获取当前激活标签
     *	参数：	
     *	返回值：tab    Tab标签实例
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Display.prototype.getActiveTab = function(){
        var tab = null;
        for(var item in this.tabs){
            var curTab = this.tabs[item];
            if(true==curTab.isActive){
                tab = curTab;
                break;
            }
        }
        return tab;
    }
    /*
     *	函数说明：激活上一个Tab标签
     *	参数：	boolean:wrap    是否循环
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Display.prototype.prevTab = function(wrap){
        var tab = null;
        var tempTabs = [];
        var activeTabIndex = null;
        for(var item in this.tabs){
            var curTab = this.tabs[item];
            if(true==curTab.isActive){
                activeTabIndex = tempTabs.length;
            }
            tempTabs[tempTabs.length] = curTab;
        }
        if(0==activeTabIndex){//当前激活的是第一个Tab，如果允许循环，则取最后一个
            if(true==wrap){
                tab = tempTabs[tempTabs.length-1];
            }else{
                tab = tempTabs[activeTabIndex];
            }
        }else{
            tab = tempTabs[activeTabIndex-1];
        }
        this.switchToTab(tab);
    }
    /*
     *	函数说明：激活下一个Tab标签
     *	参数：	boolean:wrap    是否循环
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Display.prototype.nextTab = function(wrap){
        var tab = null;
        var tempTabs = [];
        var activeTabIndex = null;
        for(var item in this.tabs){
            var curTab = this.tabs[item];
            if(true==curTab.isActive){
                activeTabIndex = tempTabs.length;
            }
            tempTabs[tempTabs.length] = curTab;
        }
        if(tempTabs.length-1==activeTabIndex){//当前激活的是第一个Tab，如果允许循环，则取最后一个
            if(true==wrap){
                tab = tempTabs[0];
            }else{
                tab = tempTabs[activeTabIndex];
            }
        }else{
            tab = tempTabs[activeTabIndex+1];
        }
        this.switchToTab(tab);
    }
    /*
     *	函数说明：根据SID获取Tab
     *	参数：  string:SID      用于区分同一数据源的唯一id
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Display.prototype.getTabBySID = function(SID){
        var tab = null;
        for(var item in this.tabs){
            if(SID==this.tabs[item].SID){
                tab = this.tabs[item];
                break;
            }
        }
        return tab;
    }