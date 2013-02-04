
    /*
     *	标签名
     */
    _TAG_NAME_BALLOON = "div";
    _TAG_NAME_TABLE = "table";
    _TAG_NAME_TBODY = "tbody";
    _TAG_NAME_TR = "tr";
    _TAG_NAME_TD = "td";
    _TAG_NAME_DIV = "div";
    _TAG_NAME_ARROW = "div";
    /*
     *	气球唯一编号名前缀
     */
    _UNIQUE_ID_BALLOON_PREFIX = "balloon__id";
    _UNIQUE_ID_DEFAULT_PREFIX = "default__id";
    /*
     *	样式名称
     */
    _STYLE_NAME_BALLOON = "balloon";
    _STYLE_NAME_BALLOON_CONTENT = "content";
    _STYLE_NAME_BALLOON_ARROW_1 = "arrow_1";
    _STYLE_NAME_BALLOON_ARROW_2 = "arrow_2";
    _STYLE_NAME_BALLOON_ARROW_3 = "arrow_3";
    _STYLE_NAME_BALLOON_ARROW_4 = "arrow_4";
    /*
     *	特定字符
     */
    _STRING_NO_BREAK_SPACE = "&nbsp;";
    /*
     *	尺寸
     */
    _SIZE_BALLOON_ARROW_HEIGHT = 15;
    _SIZE_BALLOON_CONTENT_WIDTH = 210;
    _SIZE_BALLOON_CONTENT_HEIGHT = 50;
    _SIZE_BALLOON_ARROW_OFFX = 10;
    _SIZE_BALLOON_ARROW_OFFY = 10;
    /*
     *	文件路径
     */
    _SRC_ARROW_1 = "balloon_01.gif";
    _SRC_ARROW_2 = "balloon_02.gif";
    _SRC_ARROW_3 = "balloon_03.gif";
    _SRC_ARROW_4 = "balloon_04.gif";





    /*
     *	对象名称：Balloons（全局静态对象）
     *	职责：负责管理所有气球提示界面
     *
     */
    var Balloons = {};
    Balloons.nextDepth = 1000;
    Balloons.singleInstance = true;
    Balloons.items = {};

    /*
     *	函数说明：获取下一个层次
     *	参数：  
     *	返回值：number:nextPath     下一个层次
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Balloons.getNextDepth = function(){
        var nextDepth = this.nextDepth;
        this.nextDepth++;
        return nextDepth;
    }
    /*
     *	函数说明：创建一个新气球
     *	参数：  string:content  气球显示的内容
     *	返回值：Balloon实例
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloons.create = function(content){
        //如果只允许单个实例则先进行清除
        if(true==this.singleInstance){
            this.dispose();
        }

        var balloon = new Balloon(content);
        this.items[balloon.uniqueID] = balloon;

        return balloon;
    }
    /*
     *	函数说明：清除所有气球实例
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloons.dispose = function(){
        for(var item in this.items){
            var curBalloon = this.items[item];
            curBalloon.dispose();
        }
        this.items = {};
    }
    /*
     *	函数说明：统计所有气球数量
     *	参数：
     *	返回值：number:count	气球数量
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloons.count = function(){
        var count = 0;
        for(var item in this.items){
            count++;
        }
        return count;
    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloons.toString = function(){
        var str = [];
        str[str.length] = "[Balloons 对象]";
        str[str.length] = "singleInstance:" + this.singleInstance;
        str[str.length] = "items:" + this.count();
        return str.join("\r\n");

    }




    /*
     *	对象名称：Balloon
     *	职责：负责生成气球型提示界面
     *
     */
    function Balloon(content){
        this.content = content;
        this.uniqueID = null;
        this.object = null;
        this.delay = null;
        this.timeout = null;
        this.init();
    }
    /*
     *	函数说明：Balloon初始化
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloon.prototype.init = function(){
        this.create();
        this.attachEvents();
    }
    /*
     *	函数说明：创建新气球
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Balloon.prototype.create = function(){
        this.uniqueID = UniqueID.generator(_UNIQUE_ID_BALLOON_PREFIX);

        var table = document.createElement(_TAG_NAME_TABLE);
        var tbody = document.createElement(_TAG_NAME_TBODY);
        var div = document.createElement(_TAG_NAME_DIV);
        for(var i=0;i<3;i++){
            var tr = document.createElement(_TAG_NAME_TR);
            var td = document.createElement(_TAG_NAME_TD);
            if(1==i){
                div.innerHTML = this.content;
                td.className = _STYLE_NAME_BALLOON_CONTENT;
                td.appendChild(div);
            }else{
                //td.innerHTML = _STRING_NO_BREAK_SPACE;
            }
            tr.appendChild(td);
            tbody.appendChild(tr);
        }
        table.appendChild(tbody);

        table.border = 0;
        table.cellSpacing = 0;
        table.cellPadding = 0;

        var object = document.createElement(_TAG_NAME_BALLOON);
        object.className = _STYLE_NAME_BALLOON;
        object.appendChild(table);

        this.object = object;
        this.object.id = this.uniqueID;
    }
    /*
     *	函数说明：定位气球
     *	参数：  number:x		坐标x
                number:y		坐标y
				number:delay	延时
				------------------------------------
				object:x		作为参考点的目标对象
				number:y		延时
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Balloon.prototype.dockTo = function(x,y,delay){
        if(typeof(x)==_TYPE_OBJECT && null!=x.parentNode){
            var obj = x;
            var delay = y;
            var x = Element.absLeft(obj) + _SIZE_BALLOON_ARROW_OFFX;
            var y = Element.absTop(obj) + _SIZE_BALLOON_ARROW_OFFY;
            this.dockTo(x,y,delay);

        }else if(typeof(x)==_TYPE_NUMBER){
            if(null!=this.object){
                var type = 1;
                if((x+_SIZE_BALLOON_CONTENT_WIDTH)>(document.body.clientWidth+document.body.scrollLeft)){
                    x -= _SIZE_BALLOON_CONTENT_WIDTH;
                    type += 1;
                }
                if((y-_SIZE_BALLOON_CONTENT_HEIGHT-_SIZE_BALLOON_ARROW_HEIGHT)<document.body.scrollTop){
                    type += 2;
                }else{
                    y -= _SIZE_BALLOON_CONTENT_HEIGHT + _SIZE_BALLOON_ARROW_HEIGHT;            
                }

                this.bringToTop();
                this.addArrow(type);
                this.moveTo(x,y);
                this.duration(delay);

                document.body.appendChild(this.object);
				Element.hideConflict(this.object);
            }
        }
    }
    /*
     *	函数说明：移动气球
     *	参数：  number:x  坐标x
                number:y    坐标y
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Balloon.prototype.moveTo = function(x,y){
        if(null!=this.object){
            this.object.style.left = x;
            this.object.style.top = y;
        }
    }
    /*
     *	函数说明：将气球置于最顶上
     *	参数：  
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Balloon.prototype.bringToTop = function(){
        if(null!=this.object){
            this.object.style.zIndex = Balloons.getNextDepth();
        }
    }
    /*
     *	函数说明：设置气球持续时间
     *	参数：  number:delay    持续时间(ms)
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Balloon.prototype.duration = function(delay){
        clearTimeout(this.timeout);
        this.delay = delay;
        if(null!=delay){
            var thisObj = this;
            this.timeout = setTimeout(function(){
                thisObj.dispose();
            },delay);
        }
    }
    /*
     *	函数说明：添加气球箭头
     *	参数：  number:x  坐标x
                number:y    坐标y
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Balloon.prototype.addArrow = function(type){
        var arrow = document.createElement(_TAG_NAME_ARROW);
        switch(type){
            case 1:
            default:
                //arrow.src = _SRC_ARROW_1;
                arrow.className = _STYLE_NAME_BALLOON_ARROW_1;

                var td = this.object.firstChild.firstChild.childNodes[2].childNodes[0];
                td.height = _SIZE_BALLOON_ARROW_HEIGHT;
                td.innerHTML = _STRING_NO_BREAK_SPACE;
                td.appendChild(arrow);
                td.insertBefore(arrow,td.firstChild);
                break;
            case 2:
                //arrow.src = _SRC_ARROW_2;
                arrow.className = _STYLE_NAME_BALLOON_ARROW_2;

                var td = this.object.firstChild.firstChild.childNodes[2].childNodes[0];
                td.height = _SIZE_BALLOON_ARROW_HEIGHT;
                td.align = "right";
                td.innerHTML = _STRING_NO_BREAK_SPACE;
                td.appendChild(arrow);
                break;
            case 3:
                //arrow.src = _SRC_ARROW_3;
                arrow.className = _STYLE_NAME_BALLOON_ARROW_3;

                var td = this.object.firstChild.firstChild.childNodes[0].childNodes[0];
                td.height = _SIZE_BALLOON_ARROW_HEIGHT;
                td.innerHTML = _STRING_NO_BREAK_SPACE;
                td.appendChild(arrow);
                td.insertBefore(arrow,td.firstChild);
                break;
            case 4:
                //arrow.src = _SRC_ARROW_4;
                arrow.className = _STYLE_NAME_BALLOON_ARROW_4;

                var td = this.object.firstChild.firstChild.childNodes[0].childNodes[0];
                td.height = _SIZE_BALLOON_ARROW_HEIGHT;
                td.align = "right";
                td.innerHTML = _STRING_NO_BREAK_SPACE;
                td.appendChild(arrow);
                break;
        }
    }
    /*
     *	函数说明：释放气球实例
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloon.prototype.dispose = function(){
        delete Balloons.items[this.uniqueID];
		Element.showConflict(this.object);
        Element.removeNode(this.object);

        for(var item in this){
            delete this[item];
        }
		
		Event.detachEvent(document,"mousedown",_Balloon_Document_onMouseDown);
    }
    /*
     *	函数说明：绑定事件
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloon.prototype.attachEvents = function(){
        if(null!=this.object){
            this.object.onmousedown = _Balloon_onMouseDown;
            this.object.onmouseup = _Balloon_onMouseUp;
            this.object.onmousemove = _Balloon_onMouseMove;
            this.object.oncontextmenu = _Balloon_onContextMenu;

            Event.attachEvent(document,"mousedown",_Balloon_Document_onMouseDown);
        }
    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Balloon.prototype.toString = function(){
        var str = [];
        str[str.length] = "[Balloon 对象]";
        str[str.length] = "uniqueID:" + this.uniqueID;
        str[str.length] = "content:" + this.content;
        return str.join("\r\n");

    }





    /*
     *	函数说明：整个文档对象鼠标按下
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_Document_onMouseDown(eventObj){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = this;
        Balloons.dispose();
    }
    /*
     *	函数说明：鼠标按下
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_onMouseDown(eventObj){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = this;
        if(true==Event.fireOnScrollBar(eventObj)){
            Event.cancelBubble(eventObj);
        }else{
            _Balloon_onBalloonCapture(srcElement,eventObj);
        }
    }
    /*
     *	函数说明：鼠标松开
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_onMouseUp(eventObj){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = this;

        _Balloon_onBalloonRelease(srcElement,eventObj);
    }
    /*
     *	函数说明：鼠标移动
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_onMouseMove(eventObj){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = this;

        _Balloon_onBalloonMove(srcElement,eventObj);
    }
    /*
     *	函数说明：鼠标右键点击
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_onContextMenu(eventObj){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = this;

        _Balloon_onBalloonClose(srcElement,eventObj);
    }
    /*
     *	函数说明：鼠标按下气球（虚拟事件）
     *	参数：  object:srcElement   HTML对象
                event:eventObj      事件对象
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_onBalloonCapture(srcElement,eventObj){
        srcElement.isMouseDown = true;
        srcElement.offsetX = eventObj.clientX - srcElement.offsetLeft;
        srcElement.offsetY = eventObj.clientY - srcElement.offsetTop;
        Event.setCapture(srcElement,Event.MOUSEMOVE|Event.MOUSEUP);
        Event.cancelBubble(eventObj);

        var id = srcElement.id;
        var balloon = Balloons.items[id];

        balloon.bringToTop();
    }
    /*
     *	函数说明：鼠标松开气球（虚拟事件）
     *	参数：  object:srcElement   HTML对象
                event:eventObj      事件对象
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_onBalloonRelease(srcElement,eventObj){
        srcElement.isMouseDown = false;
        Event.releaseCapture(srcElement,Event.MOUSEMOVE|Event.MOUSEUP);
    }
    /*
     *	函数说明：鼠标拖动气球（虚拟事件）
     *	参数：  object:srcElement   HTML对象
                event:eventObj      事件对象
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    function _Balloon_onBalloonMove(srcElement,eventObj){
        if(true==srcElement.isMouseDown){
            var x = eventObj.clientX - srcElement.offsetX;
            var y = eventObj.clientY - srcElement.offsetY;
            var id = srcElement.id;
            var balloon = Balloons.items[id];

            balloon.moveTo(x,y);
        }
    }
    /*
     *	函数说明：鼠标右键点击气球（虚拟事件）
     *	参数：  object:srcElement   HTML对象
                event:eventObj      事件对象
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Balloon_onBalloonClose(srcElement,eventObj){
        var id = srcElement.id;
        Event.cancel(eventObj);
        
        var balloon = Balloons.items[id];
        balloon.dispose();
    }