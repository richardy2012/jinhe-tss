    /*
     *	对象名称：Page
     *	职责：负责管理单个子页面的显示隐藏控制
     *
     */
    function Page(obj){
        this.isActive = ("none"==obj.currentStyle.display?false:true);
        this.object = obj;
        this.id = obj.id;
		this.init();
    }
    /*
     *	函数说明：Page初始化
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-24
     *
     */
    Page.prototype.init = function(){
    }
    /*
     *	函数说明：Page隐藏
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Page.prototype.hide = function(){
        this.object.style.display = "none";
        this.isActive = false;
    }
    /*
     *	函数说明：Page显示
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Page.prototype.show = function(){
        this.object.style.display = "block";
        this.object.scrollTop = 0;
        this.object.scrollLeft = 0;
        this.isActive = true;
    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Page.prototype.toString = function(){
        var str = [];
        str[str.length] = "[Page 对象]";
        str[str.length] = "id = \"" + this.id + "\"";
        str[str.length] = "visible = \"" + this.isActive + "\"";
        return str.join("\r\n");
    }
    /*
     *	函数说明：根据id获取Page实例
     *	参数：	string:id     
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-10
     *
     */
    Page.getInstance = function(id){
        return _display.pages[id];
    }