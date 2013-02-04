    /*
     *	对象名称：Tab
     *	职责：负责生成水平标签页
     *
     */
    function Tab(label,phases,callback){
        this.label = label;
		this.callback = callback;
        this.object = null;
        this.uniqueID = null;
        this.link = null;
        this.phases = {};
        this.phasesParams = phases;
        this.isActive = false;
        this.init();
    }
    /*
     *	函数说明：初始化标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.init = function(){
        this.create();
		this.createContextMenu();
    }
    /*
     *	函数说明：创建新标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.create = function(){
        var closeIcon = _display.createElement(_WORKSPACE_TAG_NAME_ICON,_WORKSPACE_NAMESPACE);
        var div = _display.createElement(_WORKSPACE_TAG_NAME_DIV);
        var object = _display.createElement(_WORKSPACE_TAG_NAME_TAB,_WORKSPACE_NAMESPACE);

        div.innerText = this.label;
        div.title = this.label;
		div.noWrap = true;
        div._target = object;

        closeIcon.title = _INFO_CLOSE;
        closeIcon._tab = this;

		object.appendChild(closeIcon);
		object.appendChild(div);

        this.object = object;
        this.uniqueID = object.uniqueID;
    }
    /*
     *	函数说明：创建右键菜单
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.createContextMenu = function(){
        if(null!=window.Menu){
            var oThis = this;
            var menu = new Menu();
            var item = {
                label:"关闭",
                callback:function(){_ontabclose(oThis);}
            }
            menu.addItem(item);
            menu.attachTo(this.object,"contextmenu");;
        }
    }
    /*
     *	函数说明：关闭标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.close = function(){
        if(null!=this.link && this==_display.getActiveTab()){
            this.hideLink();
        }
        this.dispose();

        var firstTab = _display.getFirstTab();
        _display.switchToTab(firstTab);
    }
    /*
     *	函数说明：释放标签实例
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.dispose = function(){
        if(this==_display.getActiveTab()){
            this.clearPhases();
        }

        _display.tabs[this.uniqueID] = null;
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
     *	函数说明：点击标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.click = function(){
        var thisObj = this;

        _display.inactiveAllTabs();
        thisObj.active();

        if(null!=thisObj.link){
            thisObj.showLink();
            thisObj.refreshPhases();
        }
    }
    /*
     *	函数说明：显示关联子页面
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.showLink = function(){
        _display.showPage(this.link);
    }
    /*
     *	函数说明：关闭（隐藏）关联子页面
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.hideLink = function(){
        _display.hidePage(this.link);
    }
    /*
     *	函数说明：高亮标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.active = function(){
        this.object.className = _CLASS_NAME_TAB_ACTIVE;
        this.isActive = true;
    }
    /*
     *	函数说明：低亮标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.inactive = function(){
        this.object.className = _CLASS_NAME_NO_CLASS;
        this.isActive = false;
    }
    /*
     *	函数说明：将标签与Page对象关联
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.linkTo = function(pageInstance){
        this.link = pageInstance;
    }
    /*
     *	函数说明：将标签插入指定容器
     *	参数：	object:container		HTML容器对象
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.dockTo = function(container){
        container.appendChild(this.object);
    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.prototype.toString = function(){
        var str = [];
        str[str.length] = "[Tab 对象]";
        str[str.length] = "uniqueID = \"" + this.uniqueID+ "\"";
        str[str.length] = "label = \"" + this.label+ "\"";
        return str.join("\r\n");
    }
    /*
     *	函数说明：切换到指定Tab页
     *	参数：Phase:phase       Phase实例
              或者string:pageId     Page实例id
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.switchToPhase = function(phase){
        if(null!=phase){
            switch(typeof(phase)){
                case "object":
                    phase.click();
                    break;
                case "string":                    
                    var phase = this.getPhaseByPage(phase);
                    if(null!=phase){
                        phase.click();
                    }
                    break;            
            }
        }
    }
    /*
     *	函数说明：刷新纵向标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.refreshPhases = function(){
        this.clearPhases();
        this.createPhases();

        if(null!=this.phasesParams){
            _display.refreshPhaseController();
        }else{
            _display.clearPhaseController();
        }
    }
    /*
     *	函数说明：清除纵向标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.clearPhases = function(){
        for(var item in this.phases){
            var phase = this.phases[item];

            phase.dispose();
        }
        _display.phaseBox.innerHTML = "";
    }
    /*
     *	函数说明：创建纵向标签
     *	参数：	object:phases	纵向标签配置
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-24
     *
     */
    Tab.prototype.createPhases = function(){
		for(var i=0;null!=this.phasesParams && i<this.phasesParams.length;i++){
			var curPhaseParam = this.phasesParams[i];
            var label = curPhaseParam.label;
            var pageId = curPhaseParam.page;
            var phase = new Phase(label);
            var page = Page.getInstance(pageId);

            phase.linkTo(page);
            phase.dockTo(_display.phaseBox);
            if(pageId==this.link.id){
                phase.active();
            }

            this.phases[phase.uniqueID] = phase;
		}
    }
    /*
     *	函数说明：低亮所有Phase标签
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.inactiveAllPhases = function(){
        for(var item in this.phases){
            var curPhase = this.phases[item];
            curPhase.inactive();
        }
    }
    /*
     *	函数说明：获取激活纵向标签
     *	参数：	
     *	返回值：Phase:phase	纵向标签实例
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.getActivePhase = function(){
        var phase = null;
        for(var item in this.phases){
            var curPhase = this.phases[item];
            if(true==curPhase.isActive){
                phase = curPhase;
                break;
            }
        }
        return phase;
    }
    /*
     *	函数说明：根据pageId获取纵向标签
     *	参数：	string:pageId       page页id
     *	返回值：Phase:phase	纵向标签实例
     *	作者：毛云
     *	日期：2006-5-22
     *
     */
    Tab.prototype.getPhaseByPage = function(pageId){
        var phase = null;
        for(var item in this.phases){
            var curPhase = this.phases[item];
            if(pageId==curPhase.link.id){
                phase = curPhase;
                break;
            }
        }
        return phase;
    }
    /*
     *	函数说明：激活上一个纵向标签
     *	参数：	boolean:wrap    是否循环
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.prevPhase = function(wrap){
        var phase = null;
        var tempPhases = [];
        var activePhaseIndex = null;
        for(var item in this.phases){
            var curPhase = this.phases[item];
            if(true==curPhase.isActive){
                activePhaseIndex = tempPhases.length;
            }
            tempPhases[tempPhases.length] = curPhase;
        }
        if(0==activePhaseIndex){//当前激活的是第一个Phase，如果允许循环，则取最后一个
            if(true==wrap){
                phase = tempPhases[tempPhases.length-1];
            }else{
                phase = tempPhases[activePhaseIndex];
            }
        }else{
            phase = tempPhases[activePhaseIndex-1];
        }
        this.switchToPhase(phase);
    }
    /*
     *	函数说明：激活下一个纵向标签
     *	参数：	boolean:wrap    是否循环
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.nextPhase = function(wrap){
        var phase = null;
        var tempPhases = [];
        var activePhaseIndex = null;
        for(var item in this.phases){
            var curPhase = this.phases[item];
            if(true==curPhase.isActive){
                activePhaseIndex = tempPhases.length;
            }
            tempPhases[tempPhases.length] = curPhase;
        }
        if(tempPhases.length-1==activePhaseIndex){//当前激活的是第一个Phase，如果允许循环，则取最后一个
            if(true==wrap){
                phase = tempPhases[0];
            }else{
                phase = tempPhases[activePhaseIndex];
            }
        }else{
            phase = tempPhases[activePhaseIndex+1];
        }
        this.switchToPhase(phase);
    }
    /*
     *	函数说明：执行回调函数
     *	参数：  string:eventName        事件名称
                object:params           回调函数可用参数
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-24
     *
     */
    Tab.prototype.execCallBack = function(eventName,params){
		if(null!=this.callback){
			//var eventName = "on" + event.type;
			Public.execCommand(this.callback[eventName],params);
		}
    }
    /*
     *	函数说明：切换到指定Tab页
     *	参数：Phase:phase       Phase实例
              或者string:pageId     Page实例id
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Tab.prototype.closePhase = function(pageId){
        if(null!=pageId){
            var phase = this.getPhaseByPage(pageId);
            if(null!=phase){
                phase.dispose();
            }
        }
    }
    /*
     *	函数说明：根据id获取Tab实例
     *	参数：  string:uniqueID     tab页唯一id
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Tab.getInstance = function(uniqueID){
        return _display.tabs[uniqueID];
    }