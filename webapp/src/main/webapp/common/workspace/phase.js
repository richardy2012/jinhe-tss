	/*
	 *	对象名称：Phase
	 *	职责：负责生成右侧纵向标签页
	 *
	 */
    function Phase(label){
        this.label = label;
        this.object = null;
        this.uniqueID = null;
        this.link = null;
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
    Phase.prototype.init = function(){
        this.create();
    }
    /*
     *	函数说明：创建新标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Phase.prototype.create = function(){
        var div = _display.createElement(_WORKSPACE_TAG_NAME_DIV);
        var object = _display.createElement(_WORKSPACE_TAG_NAME_PHASE,_WORKSPACE_NAMESPACE);

        div.innerText = this.label;
        div.title = this.label;
		div.noWrap = true;
        div._target = object;
		object.appendChild(div);

        this.object = object;
        this.uniqueID = object.uniqueID;
    }
    /*
     *	函数说明：将标签与Page对象关联
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Phase.prototype.linkTo = function(pageInstance){
        this.link = pageInstance;
    }
    /*
     *	函数说明：将标签插入指定容器
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Phase.prototype.dockTo = function(container){
        container.appendChild(this.object);
    }
    /*
     *	函数说明：释放纵向标签实例
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Phase.prototype.dispose = function(){
		var curActiveTab = _display.getActiveTab();
        curActiveTab.phases[this.uniqueID] = null;
        delete curActiveTab.phases[this.uniqueID];

        this.object.removeNode(true);

        this.label = null;
        this.object = null;
        this.uniqueID = null;
        this.link = null;
    }
    /*
     *	函数说明：点击标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Phase.prototype.click = function(){
		var curActiveTab = _display.getActiveTab();
        curActiveTab.inactiveAllPhases();

        this.active();
        this.scrollToView();

        var thisObj = this;

        //避免切换太快时显示内容跟不上响应
        clearTimeout(Event.timeout[_TIMEOUT_PHASE_CLICK_NAME]);
        Event.timeout[_TIMEOUT_PHASE_CLICK_NAME] = setTimeout(function(){
            if(null!=thisObj.link){
                thisObj.showLink();
            }
        },_TIMEOUT_PHASE_CLICK);
    }
    /*
     *	函数说明：将控制标签显示在可见区域内
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-7-28
     *
     */
    Phase.prototype.scrollToView = function(){
		var tempTop = this.object.offsetTop;
		var tempBottom = this.object.offsetTop + this.object.offsetHeight;
        var areaTop = _display.phaseBox.scrollTop;
        var areaBottom = areaTop + _display.phaseBox.offsetHeight;
        if(tempTop<areaTop){
            _display.phaseBox.scrollTop = tempTop;
        }else if(tempBottom>areaBottom){
            _display.phaseBox.scrollTop = tempBottom - _display.phaseBox.offsetHeight;
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
    Phase.prototype.showLink = function(){
        _display.showPage(this.link);

        var curActiveTab = _display.getActiveTab();
		curActiveTab.linkTo(this.link);
    }
    /*
     *	函数说明：关闭（隐藏）关联子页面
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Phase.prototype.hideLink = function(){
        _display.hidePage(this.link);
    }
    /*
     *	函数说明：高亮纵向标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Phase.prototype.active = function(){
        this.object.className = _CLASS_NAME_PHASE_ACTIVE;
        this.isActive = true;
    }
    /*
     *	函数说明：低亮纵向标签
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Phase.prototype.inactive = function(){
        this.object.className = _CLASS_NAME_NO_CLASS;
        this.isActive = false;
    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Phase.prototype.toString = function(){
        var str = [];
        str[str.length] = "[Phase 对象]";
        str[str.length] = "uniqueID = \"" + this.uniqueID+ "\"";
        str[str.length] = "label = \"" + this.label+ "\"";
        return str.join("\r\n");
    }
    /*
     *	函数说明：根据id获取Phase实例
     *	参数：	string:uniqueID     
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    Phase.getInstance = function(uniqueID){
		var curActiveTab = _display.getActiveTab();
        return curActiveTab.phases[uniqueID];
    }