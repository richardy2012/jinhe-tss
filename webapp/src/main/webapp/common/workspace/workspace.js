    /*
     *	自定义标签名（不含命名空间）
     */
    _WORKSPACE_NAMESPACE = "WorkSpace";
    _WORKSPACE_TAG_NAME_PAGE = "Page";
    _WORKSPACE_TAG_NAME_PAGE_STEP = "PageStep";
    _WORKSPACE_TAG_NAME_TAB = "Tab";
    _WORKSPACE_TAG_NAME_TAB_BOX = "TabBox";
    _WORKSPACE_TAG_NAME_TAB_CONTROLLER = "TabCtrl";
    _WORKSPACE_TAG_NAME_PHASE = "Phase";
    _WORKSPACE_TAG_NAME_PHASE_BOX = "PhaseBox";
    _WORKSPACE_TAG_NAME_PHASE_CONTROLLER = "PhaseCtrl";
    _WORKSPACE_TAG_NAME_ICON = "Icon";
    _WORKSPACE_TAG_NAME_CONTROLLER_BT = "CtrlBt";
    _WORKSPACE_TAG_NAME_NOBR = "nobr";
    _WORKSPACE_TAG_NAME_DIV = "div";
    _WORKSPACE_TAG_NAME_IMG = "img";
    _WORKSPACE_TAG_NAME_TABLE = "table";

    /*
     *	点击Tab到展现内容的时间差(ms)
     */
    _TIMEOUT_TAB_CLICK = 150;
    _TIMEOUT_TAB_CLICK_NAME = "ontabclick";

    /*
     *	点击Phase到展现内容的时间差(ms)
     */
    _TIMEOUT_PHASE_CLICK = 150;
    _TIMEOUT_PHASE_CLICK_NAME = "onphaseclick";

    /*
     *	onresize的时间差(ms)
     */
    _TIMEOUT_RESIZE = 20;
    _TIMEOUT_RESIZE_NAME = "onresize";

    /*
     *	事件名称
     */
    _EVENT_NAME_ON_TAB_CHANGE = "onTabChange";
    _EVENT_NAME_ON_TAB_CLOSE  = "onTabClose";
    _EVENT_NAME_ON_TAB_CLOSE_ALL = "onTabCloseAll";
    _EVENT_NAME_ON_PHASE_CHANGE = "onPhaseChange";

    /*
     *	文字信息
     */
    _INFO_CLOSE = "关闭";

    /*
     *	图片文件
     */
    _IMG_TAB_FIRST = "icon/bt_tab_first.gif";
    _IMG_TAB_PREV  = "icon/bt_tab_prev.gif";
    _IMG_TAB_NEXT  = "icon/bt_tab_next.gif";
    _IMG_TAB_LAST  = "icon/bt_tab_last.gif";
    _IMG_PHASE_FIRST = "icon/bt_phase_first.gif";
    _IMG_PHASE_PREV  = "icon/bt_phase_prev.gif";
    _IMG_PHASE_NEXT  = "icon/bt_phase_next.gif";
    _IMG_PHASE_LAST  = "icon/bt_phase_last.gif";

    /*
     *	控制按钮类型
     */
    _TYPE_TAB_CONTROLLER_BT = 1;
    _TYPE_PHASE_CONTROLLER_BT = 2;

    /*
     *	尺寸
     */
    _SIZE_TAB_WIDTH = 100;
    _SIZE_TAB_MARGIN_LEFT = 0;
    _SIZE_PHASE_HEIGHT = 73;
    _SIZE_PHASE_MARGIN_TOP = 0;
    _SIZE_IMG = 14;
    _SIZE_PHASE_CONTROLLER_HEIGHT = 70;

    /*
     *	样式名
     */
    _CLASS_NAME_BOX_HAS_TAB = "hasTab";
    _CLASS_NAME_TAB_BOX_HAS_TAB = "hasTab";
    _CLASS_NAME_NO_CLASS = "";
    _CLASS_NAME_CONTROLLER_BT_ACTIVE = "active";
    _CLASS_NAME_CONTROLLER_BT_INVERT = "invert";
    _CLASS_NAME_CONTROLLER_BT_DISABLED = "disabled";
    _CLASS_NAME_TAB_ACTIVE = "active";
    _CLASS_NAME_PHASE_ACTIVE = "active";
    _CLASS_NAME_RIGHT_BOX = "rightBox";

	
	/*
     *	对象名称：Display
     *	职责：负责生成整体界面显示
     *
     */
    function Display(element) {
		this.element = element;
	
        this.tabBox;
        this.tabController;
        this.phaseBox;
        this.phaseController;
        this.rightBox;

        this.tabs = {};
        this.buttons = {};
        this.pages = {};

        // 初始化
        this.getAllPages();
        this.hideAllPages();
        this.createUI();
    }
    /*
     *	获取所有子页面
     */
    Display.prototype.getAllPages = function() {
        var childs = this.element.getElementsByTagName(_WORKSPACE_TAG_NAME_PAGE);
        for(var i=0; i < childs.length; i++) {
            var curNode = childs[i];
            this.pages[curNode.id || curNode.uniqueID] = new Page(curNode);
        }
    }
    /*
     *	隐藏所有子页面
     */
    Display.prototype.hideAllPages = function() {
        for(var item in this.pages) {
            var curPage = this.pages[item];
            if(curPage.isActive) {
                curPage.hide();
            }
        }
    }
    /*
     *	创建界面展示
     */
    Display.prototype.createUI = function() {
        this.createTabBox();
        this.createRightBox();
        this.createPhaseBox();
		this.hideRightBox();
    }
    /*
     *	创建Tab标签的容器
     */
    Display.prototype.createTabBox = function() {
        var tabBox = this.createElement(_WORKSPACE_TAG_NAME_TAB_BOX,_WORKSPACE_NAMESPACE);
        var nobr   = this.createElement(_WORKSPACE_TAG_NAME_NOBR);

        tabBox.appendChild(nobr);
        this.element.appendChild(tabBox);
		
        var refChild = element.firstChild;
        if(refChild != tabBox) {
            element.insertBefore(tabBox, refChild); // 插入到第一个
        }

        this.tabBox = tabBox;
    }
    /*
     *	设置有Tab标签的容器样式
     *	参数：boolean:hasTab     容器是否有Tab
     */
    Display.prototype.setHasTabStyle = function(hasTab) {
        this.tabBox.className = hasTab ? _CLASS_NAME_TAB_BOX_HAS_TAB : _CLASS_NAME_NO_CLASS;
    }
    /*
     *	刷新Tab标签控制器
     */
    Display.prototype.refreshTabController = function() {
        if( this.tabController == null) {
            this.createTabController();
            this.createTabControllerButtons();
        }
        this.refreshTabControllerButtons();
    }
    /*
     *	创建Tab标签控制器
     */
    Display.prototype.createTabController = function() {
        var tabController = this.createElement(_WORKSPACE_TAG_NAME_TAB_CONTROLLER, _WORKSPACE_NAMESPACE);

        this.element.appendChild(tabController);
        var refChild = element.childNodes[1];
        if(tabController != refChild && refChild != null) {
            element.insertBefore(tabController, refChild);
        }

        this.tabController = tabController;
    }
    /*
     *	清除Tab标签控制器
     */
    Display.prototype.clearTabController = function() {
        for(var item in this.buttons) {
            var button = this.buttons[item];
            if(button.type == _TYPE_TAB_CONTROLLER_BT) {
                button.dispose();
            }
        }

        if( this.tabController != null) {
            this.tabController.removeNode(true);
            this.tabController = null;
        }
    }
    /*
     *	创建Tab标签控制器按钮
     */
    Display.prototype.createTabControllerButtons = function() {
        var firstBt = new ControllerButton(_TYPE_TAB_CONTROLLER_BT, _IMG_TAB_FIRST);
        var prevBt  = new ControllerButton(_TYPE_TAB_CONTROLLER_BT, _IMG_TAB_PREV);
        var nextBt  = new ControllerButton(_TYPE_TAB_CONTROLLER_BT, _IMG_TAB_NEXT);
        var lastBt  = new ControllerButton(_TYPE_TAB_CONTROLLER_BT, _IMG_TAB_LAST);

        this.buttons[firstBt.uniqueID] = firstBt;
        this.buttons[prevBt.uniqueID] = prevBt;
        this.buttons[nextBt.uniqueID] = nextBt;
        this.buttons[lastBt.uniqueID] = lastBt;

        firstBt.dockTo(this.tabController);
        prevBt.dockTo(this.tabController);
        nextBt.dockTo(this.tabController);
        lastBt.dockTo(this.tabController);

        firstBt.linkTo(function() {
            _display.tabBox.scrollLeft = 0;
        });
        prevBt.linkTo(function() {
            _display.tabBox.scrollLeft -= _SIZE_TAB_WIDTH + _SIZE_TAB_MARGIN_LEFT;
        });
        nextBt.linkTo(function() {
            _display.tabBox.scrollLeft += _SIZE_TAB_WIDTH + _SIZE_TAB_MARGIN_LEFT;
        });
        lastBt.linkTo(function() {
            _display.tabBox.scrollLeft = _display.tabBox.scrollWidth;
        });
    }
    /*
     *	刷新Tab标签控制器按钮
     */
    Display.prototype.refreshTabControllerButtons = function() {
        var flag = (this.tabBox.scrollWidth > this.tabBox.offsetWidth);
        for(var item in this.buttons) {
            var button = this.buttons[item];
            if(button.type == _TYPE_TAB_CONTROLLER_BT) {
                button.setEnable(flag);
            }
        }
    }
    /*
     *	创建右侧容器
     */
    Display.prototype.createRightBox = function() {
        var rightBox = this.createElement(_WORKSPACE_TAG_NAME_TABLE);
        rightBox.cellSpacing = 0;
        rightBox.cellPadding = 0;
        rightBox.border = 0;
        rightBox.className = _CLASS_NAME_RIGHT_BOX;

        for(var i=0; i < 2; i++) {
          var row = rightBox.insertRow();
          row.insertCell();
        }
        rightBox.rows(1).height = _SIZE_PHASE_CONTROLLER_HEIGHT;

        element.appendChild(rightBox);
        var refChild = element.childNodes[1];
        if(rightBox!=refChild && null!=refChild) {
            element.insertBefore(rightBox,refChild);
        }

        this.rightBox = rightBox;
    }
    /*
     *	显示右侧容器
     */
    Display.prototype.showRightBox = function() {
        this.rightBox.style.display = "inline";
    }
    /*
     *	隐藏右侧容器
     */
    Display.prototype.hideRightBox = function() {
        this.rightBox.style.display = "none";
    }
	
    /*
     *	创建纵向Tab标签的容器
     */
    Display.prototype.createPhaseBox = function() {
        var phaseBox = this.createElement(_WORKSPACE_TAG_NAME_PHASE_BOX, _WORKSPACE_NAMESPACE);
        this.rightBox.rows(0).cells(0).appendChild(phaseBox);
        this.phaseBox = phaseBox;
    }
    /*
     *	刷新Phase标签控制器
     */
    Display.prototype.refreshPhaseController = function() {
        if( this.phaseController == null ) {
            this.createPhaseController();
            this.createPhaseControllerButtons();
        }
        this.refreshPhaseControllerButtons();
		this.showRightBox();
    }
    /*
     *	创建Phase标签控制器
     */
    Display.prototype.createPhaseController = function() {
        var phaseController = this.createElement(_WORKSPACE_TAG_NAME_PHASE_CONTROLLER, _WORKSPACE_NAMESPACE);
        this.rightBox.rows(1).cells(0).appendChild(phaseController);
        this.phaseController = phaseController;
    }
    /*
     *	清除Phase标签控制器
     */
    Display.prototype.clearPhaseController = function() {
        for(var item in this.buttons) {
            var button = this.buttons[item];
            if(button.type == _TYPE_PHASE_CONTROLLER_BT) {
                button.dispose();
            }
        }

        if(this.phaseController != null) {
            this.phaseController.removeNode(true);
            this.phaseController = null;
        }
		this.hideRightBox();
    }
    /*
     *	创建Phase标签控制器按钮
     */
    Display.prototype.createPhaseControllerButtons = function() {
        var firstBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT, _IMG_PHASE_FIRST);
        var prevBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT, _IMG_PHASE_PREV);
        var nextBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT, _IMG_PHASE_NEXT);
        var lastBt = new ControllerButton(_TYPE_PHASE_CONTROLLER_BT, _IMG_PHASE_LAST);

        this.buttons[firstBt.uniqueID] = firstBt;
        this.buttons[prevBt.uniqueID] = prevBt;
        this.buttons[nextBt.uniqueID] = nextBt;
        this.buttons[lastBt.uniqueID] = lastBt;

        firstBt.dockTo(this.phaseController);
        prevBt.dockTo(this.phaseController);
        nextBt.dockTo(this.phaseController);
        lastBt.dockTo(this.phaseController);

        firstBt.linkTo(function() {
            _display.phaseBox.scrollTop = 0;
        });
        prevBt.linkTo(function() {
            _display.phaseBox.scrollTop -= _SIZE_PHASE_HEIGHT + _SIZE_PHASE_MARGIN_TOP;
        });
        nextBt.linkTo(function() {
            _display.phaseBox.scrollTop += _SIZE_PHASE_HEIGHT + _SIZE_PHASE_MARGIN_TOP;
        });
        lastBt.linkTo(function() {
            _display.phaseBox.scrollTop = _display.phaseBox.scrollHeight;
        });
    }
    /*
     *	刷新Phase标签控制器按钮
     */
    Display.prototype.refreshPhaseControllerButtons = function() {
        var flag = (this.phaseBox.scrollHeight > this.phaseBox.offsetHeight);
        for(var item in this.buttons) {
            var button = this.buttons[item];
            if(button.type == _TYPE_PHASE_CONTROLLER_BT) {
                button.setEnable(flag);
            }
        }
    }
	
    /*
     *	打开一个新子页
     *	参数：	object:inf 配置参数
     *	返回值：Tab:tab    Tab标签实例
     */
    Display.prototype.open = function(inf) {
        var tab = this.getTabBySID(inf.SID);
		
		// 不存在同一数据源tab则新建
        if(null == tab) {             
            tab = new Tab(inf.label, inf.phases, inf.callback);
            this.tabs[tab.uniqueID] = tab;

			var page = this.getPage(inf.defaultPage);
            tab.linkTo(page);
            tab.dockTo(this.tabBox.firstChild);
            tab.SID = inf.SID;//标记数据源

            this.refreshTabController();
            this.setHasTabStyle(true);
        }
        _ontabchange(tab, this.getActiveTab());

		return tab;
    }
    /*
     *	根据id查找子页
     *	参数：	string:id		子页id
     */
    Display.prototype.getPage = function(id) {
        return this.pages[id];
    }
    /*
     *	显示子页面
     *	参数：	Page:page		Page实例
     */
    Display.prototype.showPage = function(page) {
        this.hideAllPages();
        page.show();
    }
    /*
     *	关闭（隐藏）子页面
     *	参数：	Page:page		Page实例
     */
    Display.prototype.hidePage = function(page) {
        page.hide();
    }
    /*
     *	创建带命名空间的对象
     *	参数：	string:tagName		对象标签名
                string:ns			命名空间
     *	返回值：object	html对象
     */
    Display.prototype.createElement = function(tagName, ns) {
        var obj;
        if(null == ns) {
            obj = document.createElement(tagName);
        } 
		else {
            var tempDiv = document.createElement("DIV");
            tempDiv.innerHTML = "<" + ns + ":" + tagName + "/>";
            obj = tempDiv.firstChild.cloneNode(false);
            tempDiv.removeNode(true);
        }
        return obj;

    }
    /*
     *	以文本方式输出对象信息
     */
    Display.prototype.toString = function() {
        var str = [];
        str[str.length] = "[Display 对象]";
        return str.join("\r\n");
    }
    /*
     *	切换到指定Tab页
     *	参数：Tab:tab   Tab实例
     */
    Display.prototype.switchToTab = function(tab) {
        if( tab != null ) {
            //tab.click();
            _ontabchange(tab, this.getActiveTab());
        } 
		else {
            this.clearTabController();
            this.clearPhaseController();
            this.setHasTabStyle(false);
        }
        this.refreshTabControllerButtons();
    }
    /*
     *	获得第一个Tab
     */
    Display.prototype.getFirstTab = function() {
        for(var item in this.tabs) {
            return this.tabs[item];
        }
    }
    /*
     *	获得最后一个Tab
     */
    Display.prototype.getLastTab = function() {
        for(var item in this.tabs) {
        }
        return this.tabs[item];
    }
    /*
     *	低亮所有标签
     */
    Display.prototype.inactiveAllTabs = function() {
        for(var item in this.tabs) {
            var curTab = this.tabs[item];
            curTab.inactive();
        }
    }
    /*
     *	获取当前激活标签
     */
    Display.prototype.getActiveTab = function() {
        for(var item in this.tabs) {
            var tab = this.tabs[item];
            if( tab.isActive ) {
                return tab;
            }
        }
    }
	/*
     *	根据SID获取Tab
     *	参数：  string:SID 
     */
    Display.prototype.getTabBySID = function(SID) {
        for(var item in this.tabs) {
            if(SID == this.tabs[item].SID) {
                return this.tabs[item];
            }
        }
    }
    /*
     *	激活上一个Tab标签
     */
    Display.prototype.prevTab = function() {       
        var tempTabs = [];
        var activeTabIndex;
        for(var item in this.tabs) {
            var curTab = this.tabs[item];
            if( curTab.isActive ) {
                activeTabIndex = tempTabs.length;
            }
            tempTabs[tempTabs.length] = curTab;
        }
		
		var tab;
        if(0 == activeTabIndex) { // 当前激活的是第一个Tab
            tab = tempTabs[activeTabIndex];
        } else { 
            tab = tempTabs[activeTabIndex - 1];
        }
        this.switchToTab(tab);
    }
    /*
     *	激活下一个Tab标签
     */
    Display.prototype.nextTab = function() {      
        var tempTabs = [];
        var activeTabIndex;
        for(var item in this.tabs) {
            var curTab = this.tabs[item];
            if( curTab.isActive ) {
                activeTabIndex = tempTabs.length;
            }
            tempTabs[tempTabs.length] = curTab;
        }
		
		var tab;
        if(tempTabs.length - 1 == activeTabIndex) { // 当前激活的是最后一个Tab
           tab = tempTabs[activeTabIndex];
        } else {
            tab = tempTabs[activeTabIndex + 1 ];
        }
        this.switchToTab(tab);
    }

	

    /*
     *	对象名称：ControllerButton
     *	职责：负责生成控制器按钮
     *
     */
    function ControllerButton(type, imgSrc) {
		this.type = type;
        this.imgSrc = imgSrc;
        this.uniqueID;
        this.object;
        this.link;
        this.enable = true;
        this.init();
    }
    /*
     *	初始化控制器按钮
     */
    ControllerButton.prototype.init = function() {
        var img    = _display.createElement(_WORKSPACE_TAG_NAME_IMG);
        var object = _display.createElement(_WORKSPACE_TAG_NAME_CONTROLLER_BT, _WORKSPACE_NAMESPACE);

        img.src = (element.baseurl || "") + this.imgSrc;
        img._target = object;
		img.width  = _SIZE_IMG;
		img.height = _SIZE_IMG;
        object.appendChild(img);

        this.object = object;
        this.uniqueID = object.uniqueID;
    }
    /*
     *	将按钮插入指定容器
     */
    ControllerButton.prototype.dockTo = function(container) {
        container.appendChild(this.object);
    }
    /*
     *	释放按钮实例
     */
    ControllerButton.prototype.dispose = function() {
		delete _display.buttons[this.uniqueID];

        this.object.removeNode(true);

        this.imgSrc = null;
        this.uniqueID = null;
        this.object = null;
        this.link = null;
    }
    /*
     *	将按钮与要执行的操作关联
     *	参数：	Function:func   要执行的操作
     */
    ControllerButton.prototype.linkTo = function(func) {
        this.link = func;
    }
    /*
     *	以文本方式输出对象信息
     */
    ControllerButton.prototype.toString = function() {
        var str = [];
        str[str.length] = "[ControllerButton 对象]";
        str[str.length] = "uniqueID = \"" + this.uniqueID+ "\"";
        str[str.length] = "link = \"" + this.link+ "\"";
        return str.join("\r\n");
    }
    /*
     *	将按钮与要执行的操作关联
     *	参数：	Function:func   要执行的操作
     */
    ControllerButton.prototype.click = function() {
        this.link();
    }
    /*
     *	高亮按钮
     */
    ControllerButton.prototype.active = function() {
        if(this.enable) {
            this.object.className = _CLASS_NAME_CONTROLLER_BT_ACTIVE;
        }
    }
    /*
     *	低亮按钮
     */
    ControllerButton.prototype.inactive = function() {
        if(!this.enable) {
            this.object.className = _CLASS_NAME_NO_CLASS;
        }
    }
    /*
     *	反白按钮
     */
    ControllerButton.prototype.invert = function() {
        if(this.enable) {
            this.object.className = _CLASS_NAME_CONTROLLER_BT_INVERT;
        }
    }
    /*
     *	设置按钮是否允许操作
     *	参数：	boolean:enable      是否允许

     */
    ControllerButton.prototype.setEnable = function(enable) {
        this.enable = enable;
        this.object.className = enable ? _CLASS_NAME_NO_CLASS : _CLASS_NAME_CONTROLLER_BT_DISABLED;
    }
	
	/*
     *	对象名称：Page
     *	职责：负责管理单个子页面的显示隐藏控制
     */
    function Page(obj) {
        this.isActive = ("none" == obj.currentStyle.display ? false : true);
        this.object = obj;
        this.id = obj.id;		
    }
    /*
     *	Page隐藏
     */
    Page.prototype.hide = function() {
        this.object.style.display = "none";
        this.isActive = false;
    }
    /*
     *	Page显示
     */
    Page.prototype.show = function() {
        this.object.style.display = "block";
        this.object.scrollTop = 0;
        this.object.scrollLeft = 0;
        this.isActive = true;
    }
    /*
     *	以文本方式输出对象信息
     */
    Page.prototype.toString = function() {
        var str = [];
        str[str.length] = "[Page 对象]";
        str[str.length] = "id = \"" + this.id + "\"";
        str[str.length] = "visible = \"" + this.isActive + "\"";
        return str.join("\r\n");
    }
    /*
     *	根据id获取Page实例
     *	参数：	string:id     
     */
    Page.getInstance = function(id) {
        return _display.pages[id];
    }

	/*
     *	对象名称：Tab
     *	职责：负责生成水平标签页
     */
    function Tab(label, phases, callback) {
        this.label = label;
		this.callback = callback;
		this.link ;
        this.phases = {};
        this.phasesParams = phases;
        this.isActive = false;
		
        this.object = _display.createElement(_WORKSPACE_TAG_NAME_TAB,_WORKSPACE_NAMESPACE);
        this.uniqueID = object.uniqueID;
         
		var closeIcon = _display.createElement(_WORKSPACE_TAG_NAME_ICON, _WORKSPACE_NAMESPACE);
        closeIcon.title = _INFO_CLOSE;
        closeIcon._tab = this;

		this.object.appendChild(closeIcon);
		this.object.appendChild(div);
		
		var div = _display.createElement(_WORKSPACE_TAG_NAME_DIV);
        div.innerText = this.label;
        div.title = this.label;
		div.noWrap = true;
        div._target = this.object;
 
		this.createContextMenu();
    }
    /*
     *	创建右键菜单
     */
    Tab.prototype.createContextMenu = function() {
        if( window.Menu != null ) {
            var oThis = this;
            var menu = new Menu();
            var item = {
                label: "关闭",
                callback: function() { _ontabclose(oThis); }
            }
            menu.addItem(item);
            menu.attachTo(this.object,"contextmenu");;
        }
    }
    /*
     *	关闭标签
     */
    Tab.prototype.close = function() {
        if(null != this.link && this == _display.getActiveTab()) {
            this.hideLink();
        }
        this.dispose();

        var firstTab = _display.getFirstTab();
        _display.switchToTab(firstTab);
    }
    /*
     *	释放标签实例
     */
    Tab.prototype.dispose = function() {
        if(this == _display.getActiveTab()) {
            this.clearPhases();
        }

        delete _display.tabs[this.uniqueID];

        this.object.removeNode(true);

        this.label = null;
        this.object = null;
        this.uniqueID = null;
        this.link = null;
        this.phases = {};
        this.phasesParams = null;
    }
    /*
     *	点击标签
     */
    Tab.prototype.click = function() {
        _display.inactiveAllTabs();
        this.active();
 
        if(null != this.link) {
            this.showLink();
            this.refreshPhases();
        }
    }
    /*
     *	显示关联子页面
     */
    Tab.prototype.showLink = function() {
        _display.showPage(this.link);
    }
    /*
     *	关闭（隐藏）关联子页面
     */
    Tab.prototype.hideLink = function() {
        _display.hidePage(this.link);
    }
    /*
     *	高亮标签
     */
    Tab.prototype.active = function() {
        this.object.className = _CLASS_NAME_TAB_ACTIVE;
        this.isActive = true;
    }
    /*
     *	低亮标签
     */
    Tab.prototype.inactive = function() {
        this.object.className = _CLASS_NAME_NO_CLASS;
        this.isActive = false;
    }
    /*
     *	将标签与Page对象关联
     */
    Tab.prototype.linkTo = function(pageInstance) {
        this.link = pageInstance;
    }
    /*
     *	将标签插入指定容器
     *	参数：	object:container		HTML容器对象
     */
    Tab.prototype.dockTo = function(container) {
        container.appendChild(this.object);
    }
    /*
     *	以文本方式输出对象信息
     */
    Tab.prototype.toString = function() {
        var str = [];
        str[str.length] = "[Tab 对象]";
        str[str.length] = "uniqueID = \"" + this.uniqueID+ "\"";
        str[str.length] = "label = \"" + this.label+ "\"";
        return str.join("\r\n");
    }
    /*
     *	切换到指定Tab页
     *	参数：Phase:phase       Phase实例
              或者string:pageId     Page实例id
     */
    Tab.prototype.switchToPhase = function(phase) {
        if(null == phase) return;

		switch(typeof(phase)) {
		case "object":
			phase.click();
			break;
		case "string":                    
			var temp = this.getPhaseByPage(phase);
			if(null != temp) {
				temp.click();
			}
			break;            
	}
    /*
     *	刷新纵向标签
     */
    Tab.prototype.refreshPhases = function() {
        this.clearPhases();
        this.createPhases();

        if(null != this.phasesParams) {
            _display.refreshPhaseController();
        } else {
            _display.clearPhaseController();
        }
    }
    /*
     *	清除纵向标签
     */
    Tab.prototype.clearPhases = function() {
        for(var item in this.phases) {
            var phase = this.phases[item];
            phase.dispose();
        }
        _display.phaseBox.innerHTML = "";
    }
    /*
     *	创建纵向标签
     *	参数：	object:phases	纵向标签配置
     */
    Tab.prototype.createPhases = function() {
		if( this.phasesParams == null ) return;
		
		for(var i=0; i < this.phasesParams.length; i++) {
			var param = this.phasesParams[i];
            var label = param.label;
            var pageId = param.page;
            var phase = new Phase(label);
            var page = Page.getInstance(pageId);

            phase.linkTo(page);
            phase.dockTo(_display.phaseBox);
            if(pageId == this.link.id) {
                phase.active();
            }

            this.phases[phase.uniqueID] = phase;
		}
    }
    /*
     *	低亮所有Phase标签
     */
    Tab.prototype.inactiveAllPhases = function() {
        for(var item in this.phases) {
            var curPhase = this.phases[item];
            curPhase.inactive();
        }
    }
    /*
     *	获取激活纵向标签
     */
    Tab.prototype.getActivePhase = function() {
        for(var item in this.phases) {
            var curPhase = this.phases[item];
            if( curPhase.isActive ) {
                return curPhase;
            }
        }
    }
    /*
     *	根据pageId获取纵向标签
     */
    Tab.prototype.getPhaseByPage = function(pageId) {
        for(var item in this.phases) {
            var curPhase = this.phases[item];
            if(pageId == curPhase.link.id) {
                return curPhase;
            }
        }
    }
    /*
     *	激活上一个纵向标签
     */
    Tab.prototype.prevPhase = function() {       
        var tempPhases = [];
        var activePhaseIndex = null;
        for(var item in this.phases) {
            var curPhase = this.phases[item];
            if( curPhase.isActive ) {
                activePhaseIndex = tempPhases.length;
            }
            tempPhases[tempPhases.length] = curPhase;
        }
		
		var phase;
        if(0 == activePhaseIndex) { //当前激活的是第一个Phase
			phase = tempPhases[activePhaseIndex];
        } else {
            phase = tempPhases[activePhaseIndex-1];
        }
        this.switchToPhase(phase);
    }
    /*
     *	激活下一个纵向标签
     */
    Tab.prototype.nextPhase = function() {
        var tempPhases = [];
        var activePhaseIndex;
        for(var item in this.phases) {
            var curPhase = this.phases[item];
            if( curPhase.isActive ) {
                activePhaseIndex = tempPhases.length;
            }
            tempPhases[tempPhases.length] = curPhase;
        }
		
		var phase;
        if(tempPhases.length-1 == activePhaseIndex) {//当前激活的是最后一个Phase
            phase = tempPhases[activePhaseIndex];
        } else {
            phase = tempPhases[activePhaseIndex+1];
        }
        this.switchToPhase(phase);
    }
    /*
     *	执行回调函数
     *	参数：  string:eventName        事件名称
                object:params           回调函数可用参数
     */
    Tab.prototype.execCallBack = function(eventName, params) {
		if( this.callback != null) {
			Public.executeCommand(this.callback[eventName], params);
		}
    }
    /*
     *	切换到指定Tab页
     *	参数：Phase:phase       Phase实例
            或string:pageId     Page实例id
     */
    Tab.prototype.closePhase = function(pageId) {
		var phase = this.getPhaseByPage(pageId);
		if( phase ) {
			phase.dispose();
		}
    }
    /*
     *	根据id获取Tab实例
     *	参数：  string:uniqueID 
     */
    Tab.getInstance = function(uniqueID) {
        return _display.tabs[uniqueID];
    }
	
	/*
	 *	对象名称：Phase
	 *	职责：负责生成右侧纵向标签页
	 */
    function Phase(label) {
        this.label = label;
        this.link;
        this.isActive = false;
        
        this.object = _display.createElement(_WORKSPACE_TAG_NAME_PHASE,_WORKSPACE_NAMESPACE);
		this.uniqueID = this.object.uniqueID;
		
		var div = _display.createElement(_WORKSPACE_TAG_NAME_DIV);
        div.innerText = this.label;
        div.title = this.label;
		div.noWrap = true;
        div._target = this.object;.
		
		this.object.appendChild(div);       
    }
    /*
     *	将标签与Page对象关联
     */
    Phase.prototype.linkTo = function(pageInstance) {
        this.link = pageInstance;
    }
    /*
     *	将标签插入指定容器
     */
    Phase.prototype.dockTo = function(container) {
        container.appendChild(this.object);
    }
    /*
     *	释放纵向标签实例
     */
    Phase.prototype.dispose = function() {
		var curActiveTab = _display.getActiveTab();
        delete curActiveTab.phases[this.uniqueID];

        this.object.removeNode(true);

        this.label = null;
        this.object = null;
        this.uniqueID = null;
        this.link = null;
    }
    /*
     *	点击标签
     */
    Phase.prototype.click = function() {
		var curActiveTab = _display.getActiveTab();
        curActiveTab.inactiveAllPhases();

        this.active();
        this.scrollToView();

        var thisObj = this;

        //避免切换太快时显示内容跟不上响应
        clearTimeout( Event.timeout[_TIMEOUT_PHASE_CLICK_NAME] );
		
        Event.timeout[_TIMEOUT_PHASE_CLICK_NAME] = setTimeout( function() {
            if( thisObj.link ) {
                thisObj.showLink();
            }
        }, _TIMEOUT_PHASE_CLICK );
    }
	
    /*
     *	将控制标签显示在可见区域内
     */
    Phase.prototype.scrollToView = function() {
		var tempTop = this.object.offsetTop;
		var tempBottom = this.object.offsetTop + this.object.offsetHeight;
        var areaTop = _display.phaseBox.scrollTop;
        var areaBottom = areaTop + _display.phaseBox.offsetHeight;
        if(tempTop < areaTop) {
            _display.phaseBox.scrollTop = tempTop;
        }
		else if(tempBottom > areaBottom) {
            _display.phaseBox.scrollTop = tempBottom - _display.phaseBox.offsetHeight;
        }
    }
    /*
     *	显示关联子页面
     */
    Phase.prototype.showLink = function() {
        _display.showPage(this.link);

        var curActiveTab = _display.getActiveTab();
		curActiveTab.linkTo(this.link);
    }
    /*
     *	关闭（隐藏）关联子页面
     */
    Phase.prototype.hideLink = function() {
        _display.hidePage(this.link);
    }
    /*
     *	高亮纵向标签
     */
    Phase.prototype.active = function() {
        this.object.className = _CLASS_NAME_PHASE_ACTIVE;
        this.isActive = true;
    }
    /*
     *	低亮纵向标签
     */
    Phase.prototype.inactive = function() {
        this.object.className = _CLASS_NAME_NO_CLASS;
        this.isActive = false;
    }
    /*
     *	以文本方式输出对象信息
     */
    Phase.prototype.toString = function() {
        var str = [];
        str[str.length] = "[Phase 对象]";
        str[str.length] = "uniqueID = \"" + this.uniqueID+ "\"";
        str[str.length] = "label = \"" + this.label+ "\"";
        return str.join("\r\n");
    }
    /*
     *	根据id获取Phase实例
     */
    Phase.getInstance = function(uniqueID) {
		var curActiveTab = _display.getActiveTab();
        return curActiveTab.phases[uniqueID];
    }
	
	
	
    /*
     *	对象名称：Event
     *	职责：负责控制所有自定义事件
     */
    var Event = {};
    Event.timeout = {};
	
    /*
     *	触发事件
     *	参数：	string:eventName		事件名
     *          object:params           事件参数
     */
    Event.fire = function(eventName, params) {
        var flag = true;
        var oEvent = createEventObject();
        for(var item in params) {
            oEvent[item] = params[item];
        }
        var eventObj = this.getElementByName(eventName);
        if(null != eventObj) {
            oEvent.returnValue = true;
            eventObj.fire(oEvent);
            if(false == oEvent.returnValue) {
                flag = false;
            }
        }
        return flag;
    }
    /*
     *	根据事件名称获取PUBLIC:EVENT标签对象
     *	参数：	string:eventName		事件名
     */
    Event.getElementByName = function(eventName) {
        var eventObj = this[eventName];
        if(null==eventObj) {
            this.refresh();
            eventObj = this[eventName];
        }
        return eventObj;
    }
    /*
     *	获取所有PUBLIC:EVENT标签对象
     */
    Event.refresh = function() {
        var tags = document.getElementsByTagName("EVENT");
        for(var i=0;i<tags.length;i++) {
            var eventObj = tags[i];
            var eventName = eventObj.getAttribute("name");
            this[eventName] = eventObj;
        }
    }
    /*
     *	根据事件名称获取PUBLIC:EVENT标签对象的id
     *	参数：	string:eventName		事件名
     *	返回值：string:id               PUBLIC:EVENT标签对象的id
     */
    Event.getId = function(eventName) {
        var eventId = null;
        if(null==this[eventName]) {
            this.refresh();
        }
        if(null!=this[eventName]) {
            eventId = this[eventName].id;
        }
        return eventId;
    }
    /*
     *	鼠标点击操作
     */
    function _onclick() {
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target) {
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE) {
            switch(tagName) {
                //点击Tab标签，显示内容
                case _WORKSPACE_TAG_NAME_TAB:
                    var tab = Tab.getInstance(srcElement.uniqueID);
                    var curActiveTab = _display.getActiveTab();
                    _ontabchange(tab,curActiveTab);
                    break;
                //点击Phase标签，显示内容
                case _WORKSPACE_TAG_NAME_PHASE:
                    var tab = _display.getActiveTab();
                    var phase = tab.phases[srcElement.uniqueID];
                    var curActivePhase = tab.getActivePhase();
                    _onphasechange(phase,curActivePhase);
                    break;
                //点击关闭按钮，关闭Tab
                case _WORKSPACE_TAG_NAME_ICON:
                    var tab = srcElement._tab;
                    _ontabclose(tab);
                    break;
                //点击控制器按钮，执行关联操作
                case _WORKSPACE_TAG_NAME_CONTROLLER_BT:
                    var bt = _display.buttons[srcElement.uniqueID];
                    _onbtclick(bt);
                    break;
                //点击Page
                case _WORKSPACE_TAG_NAME_PAGE:
                    var page = Page.getInstance(srcElement.id);
                    //alert(page)
                    break;
                default:
                    break;
            }
        }
    }
    /*
     *	鼠标双击操作
     */
    function _ondblclick() {
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target) {
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;
        if(scopeName==_WORKSPACE_NAMESPACE) {
            switch(tagName) {
                case _WORKSPACE_TAG_NAME_TAB:
                    var tab = Tab.getInstance(srcElement.uniqueID);
                    _ontabclose(tab);
                    break;
                case _WORKSPACE_TAG_NAME_PAGE:
                    var page = Page.getInstance(srcElement.id);
                    //alert(page)
                    break;
                default:
                    break;
            }
        }
    }
    /*
     *	Tab切换事件（虚拟）
     *	参数：	Tab:toTab       Tab实例
                Tab:fromTab     Tab实例
     */
    function _ontabchange(toTab,fromTab) {        
        //延时执行单击切换页操作，避免与双击关闭页冲突
        clearTimeout(Event.timeout[_TIMEOUT_TAB_CLICK_NAME]);
        Event.timeout[_TIMEOUT_TAB_CLICK_NAME] = setTimeout(function() {

            if(toTab!=fromTab) {
                var params = {};
                params.tab = toTab;
                params.lastTab = fromTab;

                //执行toTab页上定义的回调方法
                toTab.execCallBack(_EVENT_NAME_ON_TAB_CHANGE,params);
                
                var isSuccess = Event.fire(_EVENT_NAME_ON_TAB_CHANGE,params);
                if(true==isSuccess) {
                    toTab.click();
                }
            }
        },_TIMEOUT_TAB_CLICK);
    }
    /*
     *	Tab关闭事件（虚拟）
     *	参数：	Tab:tab       Tab实例
     */
    function _ontabclose(tab) {
        //双击关闭时不执行单击切换页操作
        clearTimeout(Event.timeout[_TIMEOUT_TAB_CLICK_NAME]);

        var params = {};
        params.tab = tab;

        //执行tab页上定义的回调方法
        tab.execCallBack(_EVENT_NAME_ON_TAB_CLOSE,params);

        var isSuccess = Event.fire(_EVENT_NAME_ON_TAB_CLOSE,params);
        if(true==isSuccess) {
            tab.close();

            var firstTab = _display.getFirstTab();
            if(null==firstTab) {
                Event.fire(_EVENT_NAME_ON_TAB_CLOSE_ALL);
            }
        }
    }
    /*
     *	Phase切换事件（虚拟）
     *	参数：	Phase:toPhase       Phase实例
                Phase:fromPhase     Phase实例
     */
    function _onphasechange(toPhase,fromPhase) {
        if(toPhase!=fromPhase) {
            var params = {};
            params.phase = toPhase;
            params.lastPhase = fromPhase;
            var isSuccess = Event.fire(_EVENT_NAME_ON_PHASE_CHANGE,params);
            if(true==isSuccess) {
                toPhase.click();
            }
        }
    }
    /*
     *	ControllerButton点击事件（虚拟）
     *	参数：	ControllerButton:bt       ControllerButton实例
     */
    function _onbtclick(bt) {
        var params = {};
        params.bt = bt;
        bt.click();
    }
    /*
     *	鼠标悬停操作
     */
    function _onmouseover() {
        var srcElement = event.srcElement;
        if(null==srcElement) {
            return;
        }
        if(null!=srcElement._target) {
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE) {
            switch(tagName) {
                //点击控制器按钮，执行关联操作
                case _WORKSPACE_TAG_NAME_CONTROLLER_BT:
                    var bt = _display.buttons[srcElement.uniqueID];
                    _onbtover(bt);
                    break;
                default:
                    break;
            }
        }
    }
    /*
     *	鼠标离开操作
     */
    function _onmouseout() {
        var srcElement = event.srcElement;
        if(null==srcElement) {
            return;
        }
        if(null!=srcElement._target) {
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE) {
            switch(tagName) {
                //点击控制器按钮，执行关联操作
                case _WORKSPACE_TAG_NAME_CONTROLLER_BT:
                    var bt = _display.buttons[srcElement.uniqueID];
                    _onbtout(bt);
                    break;
                default:
                    break;
            }
        }
    }
    /*
     *	鼠标按下操作
     */
    function _onmousedown() {
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target) {
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE) {
            switch(tagName) {
                //点击控制器按钮，执行关联操作
                case _WORKSPACE_TAG_NAME_CONTROLLER_BT:
                    var bt = _display.buttons[srcElement.uniqueID];
                    _onbtdown(bt);
                    break;
                default:
                    break;
            }
        }
    }
    /*
     *	鼠标松开操作
     */
    function _onmouseup() {
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target) {
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE) {
            switch(tagName) {
                //点击控制器按钮，执行关联操作
                case _WORKSPACE_TAG_NAME_CONTROLLER_BT:
                    var bt = _display.buttons[srcElement.uniqueID];
                    _onbtup(bt);
                    break;
                default:
                    break;
            }
        }
    }
    /*
     *	尺寸改变
     */
    function _onresize() {
        clearTimeout(Event.timeout[_TIMEOUT_RESIZE_NAME]);
        Event.timeout[_TIMEOUT_RESIZE_NAME] = setTimeout(function() {
            _display.refreshTabControllerButtons();
            _display.refreshPhaseControllerButtons();
        },_TIMEOUT_RESIZE);
    }
    /*
     *	ControllerButton鼠标悬停事件（虚拟）
     *	参数：	ControllerButton:bt       ControllerButton实例
     */
    function _onbtover(bt) {
        bt.active();
    }
    /*
     *	ControllerButton鼠标离开事件（虚拟）
     *	参数：	ControllerButton : bt 
     */
    function _onbtout(bt) {
        bt.inactive();
    }
    /*
     *	ControllerButton鼠标松开事件（虚拟）
     *	参数：	ControllerButton : bt 
     */
    function _onbtup(bt) {
        bt.active();
    }
    /*
     *	ControllerButton鼠标按下事件（虚拟）
     *	参数：	ControllerButton : bt
     */
    function _onbtdown(bt) {
        bt.invert();
    }

	
    /* 控件绑定事件 */
    element.onclick = _onclick;
    element.ondblclick = _ondblclick;
    element.onmouseover = _onmouseover;
    element.onmouseout = _onmouseout;
    element.onmousedown = _onmousedown;
    element.onmouseup = _onmouseup;
    element.onresize = _onresize;
	
	
	
/*     控件名称：标签式工作区

    功能说明：	1、动态创建Tab标签
                2、动态创建纵向Tab标签
                3、Tab标签控制子页面显示
                4、双击Tab标签可关闭 */

// <PUBLIC:METHOD NAME="closePhase"/>
// <PUBLIC:METHOD NAME="closeActiveTab"/>
// <PUBLIC:METHOD NAME="getActiveTab"/>
// <PUBLIC:METHOD NAME="nextPhase"/>
// <PUBLIC:METHOD NAME="nextTab"/>
// <PUBLIC:METHOD NAME="open"/>
// <PUBLIC:METHOD NAME="prevPhase"/>
// <PUBLIC:METHOD NAME="prevTab"/>
// <PUBLIC:METHOD NAME="test"/>
// <PUBLIC:ATTACH EVENT="oncontentready" ONEVENT="setTimeout(init,10)"/>
// <PUBLIC:EVENT NAME="onTabChange" ID="event_ontabchange"/>
// <PUBLIC:EVENT NAME="onTabClose" ID="event_ontabclose"/>
// <PUBLIC:EVENT NAME="onTabCloseAll" ID="event_ontabcloseall"/>
// <PUBLIC:EVENT NAME="onPhaseChange" ID="event_onphasechange"/>
// <PUBLIC:EVENT NAME="onload" ID="event_onload"/>
	
	 /*
     *	Display对象实例
     *
     */
    var _display;

    /*
     *	初始化控件
     */
    function init() {
        _display = new Display();

        //触发onload事件
        Event.fire("onload");
        element.isLoaded = true;
    }
    /*
     *	打开子页面
     */
    function open(inf) {
        return _display.open(inf);
    }
    /*
     *	获取当前激活标签
     */
    function getActiveTab() {
        return _display.getActiveTab();
    }
    /*
     *	关闭当前激活标签
     */
    function closeActiveTab() {
        var tab = this.getActiveTab();
        if(null!=tab) {
            // tab.close();
            _ontabclose(tab);
        }
    }
    /*
     *	激活上一个Tab标签
     */
    function prevTab() {
        return _display.prevTab();
    }
    /*
     *	激活下一个Tab标签
     */
    function nextTab() {
        return _display.nextTab();
    }
    /*
     *	激活上一个Phase标签
     */
    function prevPhase() {
        var tab = this.getActiveTab();
        if( tab ) {
            return tab.prevPhase();
        }
    }
    /*
     *	激活下一个Phase标签
     */
    function nextPhase() {
        var tab = this.getActiveTab();
        if( tab ) {
            return tab.nextPhase();
        }
    }
    /*
     *	关闭指定Phase标签
     */
    function closePhase(pageId) {
        var tab = this.getActiveTab();
        if( tab ) {
            tab.closePhase(pageId);
        }
    }