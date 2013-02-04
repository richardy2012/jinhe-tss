    /*
     *	对象名称：Event
     *	职责：负责控制所有自定义事件
     *
     */
    var Event = {};
    Event.timeout = {};
    /*
     *	函数说明：触发事件
     *	参数：	string:eventName		事件名
     *          object:params           事件参数
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Event.fire = function(eventName,params){
        var flag = true;
        var oEvent = createEventObject();
        for(var item in params){
            oEvent[item] = params[item];
        }
        var eventObj = this.getElementByName(eventName);
        if(null!=eventObj){
            oEvent.returnValue = true;
            eventObj.fire(oEvent);
            if(false==oEvent.returnValue){
                flag = false;
            }
        }
        return flag;
    }
    /*
     *	函数说明：根据事件名称获取PUBLIC:EVENT标签对象
     *	参数：	string:eventName		事件名
     *	返回值：eventObj                PUBLIC:EVENT标签对象
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Event.getElementByName = function(eventName){
        var eventObj = this[eventName];
        if(null==eventObj){
            this.refresh();
            eventObj = this[eventName];
        }
        return eventObj;
    }
    /*
     *	函数说明：获取所有PUBLIC:EVENT标签对象
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Event.refresh = function(){
        var tags = document.getElementsByTagName("EVENT");
        for(var i=0;i<tags.length;i++){
            var eventObj = tags[i];
            var eventName = eventObj.getAttribute("name");
            this[eventName] = eventObj;
        }
    }
    /*
     *	函数说明：根据事件名称获取PUBLIC:EVENT标签对象的id
     *	参数：	string:eventName		事件名
     *	返回值：string:id               PUBLIC:EVENT标签对象的id
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Event.getId = function(eventName){
        var eventId = null;
        if(null==this[eventName]){
            this.refresh();
        }
        if(null!=this[eventName]){
            eventId = this[eventName].id;
        }
        return eventId;
    }
    /*
     *	函数说明：鼠标点击操作
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    function _onclick(){
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target){
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE){
            switch(tagName){
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
     *	函数说明：鼠标双击操作
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    function _ondblclick(){
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target){
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;
        if(scopeName==_WORKSPACE_NAMESPACE){
            switch(tagName){
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
     *	函数说明：Tab切换事件（虚拟）
     *	参数：	Tab:toTab       Tab实例
                Tab:fromTab     Tab实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _ontabchange(toTab,fromTab){        
        //延时执行单击切换页操作，避免与双击关闭页冲突
        clearTimeout(Event.timeout[_TIMEOUT_TAB_CLICK_NAME]);
        Event.timeout[_TIMEOUT_TAB_CLICK_NAME] = setTimeout(function(){

            if(toTab!=fromTab){
                var params = {};
                params.tab = toTab;
                params.lastTab = fromTab;

                //执行toTab页上定义的回调方法
                toTab.execCallBack(_EVENT_NAME_ON_TAB_CHANGE,params);
                
                var isSuccess = Event.fire(_EVENT_NAME_ON_TAB_CHANGE,params);
                if(true==isSuccess){
                    toTab.click();
                }
            }
        },_TIMEOUT_TAB_CLICK);
    }
    /*
     *	函数说明：Tab关闭事件（虚拟）
     *	参数：	Tab:tab       Tab实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _ontabclose(tab){
        //双击关闭时不执行单击切换页操作
        clearTimeout(Event.timeout[_TIMEOUT_TAB_CLICK_NAME]);

        var params = {};
        params.tab = tab;

        //执行tab页上定义的回调方法
        tab.execCallBack(_EVENT_NAME_ON_TAB_CLOSE,params);

        var isSuccess = Event.fire(_EVENT_NAME_ON_TAB_CLOSE,params);
        if(true==isSuccess){
            tab.close();

            var firstTab = _display.getFirstTab();
            if(null==firstTab){
                Event.fire(_EVENT_NAME_ON_TAB_CLOSE_ALL);
            }
        }
    }
    /*
     *	函数说明：Phase切换事件（虚拟）
     *	参数：	Phase:toPhase       Phase实例
                Phase:fromPhase     Phase实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _onphasechange(toPhase,fromPhase){
        if(toPhase!=fromPhase){
            var params = {};
            params.phase = toPhase;
            params.lastPhase = fromPhase;
            var isSuccess = Event.fire(_EVENT_NAME_ON_PHASE_CHANGE,params);
            if(true==isSuccess){
                toPhase.click();
            }
        }
    }
    /*
     *	函数说明：ControllerButton点击事件（虚拟）
     *	参数：	ControllerButton:bt       ControllerButton实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _onbtclick(bt){
        var params = {};
        params.bt = bt;
        bt.click();
    }
    /*
     *	函数说明：鼠标悬停操作
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    function _onmouseover(){
        var srcElement = event.srcElement;
        if(null==srcElement){
            return;
        }
        if(null!=srcElement._target){
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE){
            switch(tagName){
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
     *	函数说明：鼠标离开操作
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    function _onmouseout(){
        var srcElement = event.srcElement;
        if(null==srcElement){
            return;
        }
        if(null!=srcElement._target){
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE){
            switch(tagName){
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
     *	函数说明：鼠标按下操作
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _onmousedown(){
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target){
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE){
            switch(tagName){
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
     *	函数说明：鼠标松开操作
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _onmouseup(){
        var srcElement = event.srcElement;
        if(null!=srcElement && null!=srcElement._target){
            srcElement = srcElement._target;
        }
        var tagName = srcElement.tagName;
        var scopeName = srcElement.scopeName;

        if(scopeName==_WORKSPACE_NAMESPACE){
            switch(tagName){
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
     *	函数说明：尺寸改变
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-7-1
     *
     */
    function _onresize(){
        clearTimeout(Event.timeout[_TIMEOUT_RESIZE_NAME]);
        Event.timeout[_TIMEOUT_RESIZE_NAME] = setTimeout(function(){
            _display.refreshTabControllerButtons();
            _display.refreshPhaseControllerButtons();
        },_TIMEOUT_RESIZE);
    }
    /*
     *	函数说明：ControllerButton鼠标悬停事件（虚拟）
     *	参数：	ControllerButton:bt       ControllerButton实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _onbtover(bt){
        bt.active();
    }
    /*
     *	函数说明：ControllerButton鼠标离开事件（虚拟）
     *	参数：	ControllerButton:bt       ControllerButton实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _onbtout(bt){
        bt.inactive();
    }
    /*
     *	函数说明：ControllerButton鼠标松开事件（虚拟）
     *	参数：	ControllerButton:bt       ControllerButton实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _onbtup(bt){
        bt.active();
    }
    /*
     *	函数说明：ControllerButton鼠标按下事件（虚拟）
     *	参数：	ControllerButton:bt       ControllerButton实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    function _onbtdown(bt){
        bt.invert();
    }
    /*
     *	函数说明：取消事件
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    function _cancel(){
        event.returnValue = false;
        return false;
    }





    /*
     *	说明：控件绑定事件
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    element.onclick = _onclick;
    element.ondblclick = _ondblclick;
//    element.onselectstart = _cancel;
    element.onmouseover = _onmouseover;
    element.onmouseout = _onmouseout;
    element.onmousedown = _onmousedown;
    element.onmouseup = _onmouseup;
    element.onresize = _onresize;