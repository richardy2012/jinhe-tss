
    



    /*
     *	对象名称：ControllerButton
     *	职责：负责生成控制器按钮
     *
     */
    function ControllerButton(type,imgSrc){
		this.type = type;
        this.imgSrc = imgSrc;
        this.uniqueID = null;
        this.object = null;
        this.link = null;
        this.enable = true;
        this.init();
    }
    /*
     *	函数说明：初始化控制器按钮
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    ControllerButton.prototype.init = function(){
        this.create();
    }
    /*
     *	函数说明：创建控制器按钮
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    ControllerButton.prototype.create = function(){
        var img = _display.createElement(_WORKSPACE_TAG_NAME_IMG);
        var object = _display.createElement(_WORKSPACE_TAG_NAME_CONTROLLER_BT,_WORKSPACE_NAMESPACE);

        img.src = (element.baseurl||"") + this.imgSrc;
        img._target = object;
		img.width = _SIZE_IMG;
		img.height = _SIZE_IMG;
        object.appendChild(img);

        this.object = object;
        this.uniqueID = object.uniqueID;
    }
    /*
     *	函数说明：将按钮插入指定容器
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    ControllerButton.prototype.dockTo = function(container){
        container.appendChild(this.object);
    }
    /*
     *	函数说明：释放按钮实例
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    ControllerButton.prototype.dispose = function(){
		_display.buttons[this.uniqueID] = null;
		delete _display.buttons[this.uniqueID];

        this.object.removeNode(true);

        this.imgSrc = null;
        this.uniqueID = null;
        this.object = null;
        this.link = null;
    }
    /*
     *	函数说明：将按钮与要执行的操作关联
     *	参数：	Function:func   要执行的操作
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    ControllerButton.prototype.linkTo = function(func){
        this.link = func;
    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    ControllerButton.prototype.toString = function(){
        var str = [];
        str[str.length] = "[ControllerButton 对象]";
        str[str.length] = "uniqueID = \"" + this.uniqueID+ "\"";
        str[str.length] = "link = \"" + this.link+ "\"";
        return str.join("\r\n");
    }
    /*
     *	函数说明：将按钮与要执行的操作关联
     *	参数：	Function:func   要执行的操作
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-12
     *
     */
    ControllerButton.prototype.click = function(){
        this.link();
    }
    /*
     *	函数说明：高亮按钮
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    ControllerButton.prototype.active = function(){
        if(true==this.enable){
            this.object.className = _CLASS_NAME_CONTROLLER_BT_ACTIVE;
        }
    }
    /*
     *	函数说明：低亮按钮
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    ControllerButton.prototype.inactive = function(){
        if(true==this.enable){
            this.object.className = _CLASS_NAME_NO_CLASS;
        }
    }
    /*
     *	函数说明：反白按钮
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-11
     *
     */
    ControllerButton.prototype.invert = function(){
        if(true==this.enable){
            this.object.className = _CLASS_NAME_CONTROLLER_BT_INVERT;
        }
    }
    /*
     *	函数说明：设置按钮是否允许操作
     *	参数：	boolean:enable      是否允许
     *	返回值：
     *	作者：毛云
     *	日期：2006-7-1
     *
     */
    ControllerButton.prototype.setEnable = function(enable){
        this.enable = enable;
        this.object.className = enable?_CLASS_NAME_NO_CLASS:_CLASS_NAME_CONTROLLER_BT_DISABLED;
    }