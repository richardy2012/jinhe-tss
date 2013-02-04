
    /*
     *	浏览器类型
     */
    _BROWSER_IE = "IE";
    _BROWSER_FF = "FF";
    _BROWSER_OPERA = "OPERA";
    _BROWSER = _BROWSER_IE;	
    /*
     *	对象类型
     */
    _TYPE_NUMBER = "number";
    _TYPE_OBJECT = "object";
    _TYPE_FUNCTION = "function";
    _TYPE_STRING = "string";
    _TYPE_BOOLEAN = "boolean";
    /*
     *	默认唯一编号名前缀
     */
    _UNIQUE_ID_DEFAULT_PREFIX = "default__id";






    /*
     *	对象名称：Public（全局静态对象）
     *	职责：负责公共函数
     *
     */
    var Public = {};
    /*
     *
     * 函数说明：浏览器识别
     * 返回值：
     * 作者：毛云
     * 日期：2006-4-18
     *
     */
    Public.checkBrowser = function(){
        var ua = navigator.userAgent.toUpperCase();
        if(ua.indexOf("MSIE")!=-1){
            _BROWSER = _BROWSER_IE;
        }else if(ua.indexOf("FIREFOX")!=-1){
            _BROWSER = _BROWSER_FF;
        }else if(ua.indexOf("OPERA")!=-1){
            _BROWSER = _BROWSER_OPERA;
        }
    }
    Public.checkBrowser();
    /*
     *
     * 函数说明：执行方法
     * 参数：	string/function:callback	回调方法定义
                any:param					任何类型参数
     * 返回值：	类型不定:returnValue		返回值
     * 作者：毛云
     * 日期：2006-4-18
     *
     */
    Public.execCommand = function(callback,param){
        var returnValue = null;
        try{
            switch(typeof(callback)){
                case _TYPE_STRING:
                    returnValue = eval(callback);
                    break;
                case _TYPE_FUNCTION:
                    returnValue = callback(param);
                    break;
                case _TYPE_BOOLEAN:
                    returnValue = callback;
                    break;
            }
        }catch(e){
            returnValue = false;
        }
        return returnValue;
    }
    /*
     *
     * 函数说明：初始化htc类型控件
     * 参数：	Object:obj			htc绑定的HTML对象
                string:flag			检测htc加载完成的特定属性
                string:eventName	加载完成的事件名称
                function:callback	回调方法
     * 返回值：	
     * 作者：毛云
     * 日期：2006-4-18
     *
     */
    Public.initHTC = function (obj,flag,eventName,callback){
        if(obj==null || flag==null || callback==null){
            alert("需要的参数为空，请检查")
            return;
        }
        if(obj[flag]!=true){
            obj[eventName] = function(){
                this[eventName] = null;
                Public.execCommand(callback);
            }
        }else{
            Public.execCommand(callback);
        }
        
    }






    /*
     *	对象名称：UniqueID（全局静态对象）
     *	职责：负责生成对象唯一编号（为了兼容FF）
     *
     */
    var UniqueID = {};
    UniqueID.key = 0;
    /*
     *	函数说明：创建一个唯一编号
     *	参数：	string:prefix		唯一编号名称前缀
     *	返回值：string:uniqueID     唯一编号
     *	作者：毛云
     *	日期：2006-4-18
     *
     */
    UniqueID.generator = function(prefix){
        var uid = String(prefix||_UNIQUE_ID_DEFAULT_PREFIX) + String(this.key);
        this.key++;
        return uid;
    }




    /*
     *	对象名称：Focus（全局静态对象）
     *	职责：负责管理所有注册进来对象的聚焦操作
     *
     */
    var Focus = {};
    Focus.items = {};
    Focus.lastID = null;
    /*
     *	函数说明：注册对象
     *	参数：	object:focusObj		需要聚焦的HTML对象
     *	返回值：string:id			用于取回聚焦HTML对象的id
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Focus.register = function(focusObj){
        var id = focusObj.id;
        //如果id不存在则自动生成一个
        if(null==id || ""==id){
            id = UniqueID.generator();
            focusObj.id = id;
        }
        this.items[id] = focusObj;

        this.focus(id);

        return id;
    }
    /*
     *	函数说明：聚焦对象
     *	参数：	object:focusObj		需要聚焦的HTML对象
     *	返回值：string:id			用于取回聚焦HTML对象的id
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Focus.focus = function(id){
        var focusObj = this.items[id];
        if(null!=focusObj && id!=this.lastID){
            if(null!=this.lastID){
                this.blurItem(this.lastID);
            }
            
            this.focusItem(id);
            this.lastID = id;
        }
    }
    /*
     *	函数说明：施加聚焦效果
     *	参数：	string:id			需要聚焦的HTML对象
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Focus.focusItem = function(id){
        var focusObj = this.items[id];
        if(null!=focusObj){
            focusObj.style.filter = "";
        }
    }
    /*
     *	函数说明：施加失焦效果
     *	参数：	string:id			需要聚焦的HTML对象
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Focus.blurItem = function(id){
        var focusObj = this.items[id];
        if(null!=focusObj){
            focusObj.style.filter = "alpha(opacity=50) gray()";
        }
    }
    /*
     *	函数说明：释放对象
     *	参数：	object:focusObj		需要聚焦的HTML对象
     *	返回值：string:id			用于取回聚焦HTML对象的id
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Focus.unregister = function(id){
        var focusObj = this.items[id];
        if(null!=focusObj){
            delete this.items[id];
        }
        if(id==this.lastID){
            
        }
    }




    /*
     *	对象名称：Cache（全局静态对象）
     *	职责：负责缓存页面上全局数据信息
     *
     */
    var Cache = {};
    Cache.Variables = new Collection();
    Cache.XmlIslands = new Collection();

    /*
     *	对象名称：Collection
     *	职责：负责存取集合成员
     *
     */
    function Collection(){
        this.items = {};
    }
    /*
     *	函数说明：添加成员
     *	参数：	string:id       成员id
                any:item        集合成员
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Collection.prototype.add = function(id,item){
        this.items[id] = item;
    }
    /*
     *	函数说明：删除成员
     *	参数：	string:id		要删除的成员id
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Collection.prototype.del = function(id){
        delete this.items[id];
    }
    /*
     *	函数说明：清空所有成员
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Collection.prototype.clear = function(){
        this.items = {};
    }
    /*
     *	函数说明：获取成员
     *	参数：	string:id       要获取的成员id
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-22
     *
     */
    Collection.prototype.get = function(id){
        return this.items[id];
    }
    /*
     *	函数说明：原型继承
     *	参数：	function:Class		将被继承的类
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-23
     *
     */
	Collection.prototype.inherit = function(Class){
		var inheritClass = new Class();
		for(var item in inheritClass){
			this[item] = inheritClass[item];
		}
	}







    /*
     *	函数说明：扩展数组，增加数组项
     *	参数：	any:item		将被增加的项
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-23
     *
     */
    Array.prototype.push = function(item){
        this[this.length] = item;
    }