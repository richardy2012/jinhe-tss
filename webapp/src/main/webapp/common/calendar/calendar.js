
    /*
     *	标签名
     */
    _TAG_NAME_CALENDAR = "div";
    _TAG_NAME_TABLE = "table";
    _TAG_NAME_TBODY = "tbody";
    _TAG_NAME_TR = "tr";
    _TAG_NAME_TD = "td";
    _TAG_NAME_DIV = "div";
    _TAG_NAME_ARROW = "div";
    /*
     *	日历唯一编号名前缀
     */
    _UNIQUE_ID_CALENDAR_PREFIX = "calendar__id";
    _UNIQUE_ID_DEFAULT_PREFIX = "default__id";
    /*
     *	样式名称
     */
    _STYLE_NAME_CALENDAR = "calendar";
    /*
     *	特定字符
     */
    _STRING_NO_BREAK_SPACE = "&nbsp;";
    /*
     *	日期相关参数
     */
    _TOTAL_DAY_PER_WEEK = 7;
    _TOTAL_CALENDAR_ROWS = 6;




    /*
     *	对象名称：Calendar
     *	职责：负责生成日历型提示界面
     *
     */
    function Calendar(date){
        if(null==date){
            date = new Date();
        }
        this.begin = new Date("2000/1/1");
        this.end = new Date("2010/12/31");
        this.date = null;
        this.initDate = date;
        this.today = new Date();
        this.str = "";
        this.monthObj = null;
        this.yearObj = null;
        this.dayObj = null;
        this.tableObj = null;
        this.object = null;
        this.dateStyle = {};
        this.init();
    }
    /*
     *	函数说明：Calendar初始化
     *	参数：  
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.init = function(){
        this.onYearChange = function(){};
        this.onMonthChange = function(){};
        this.onDateChange = function(){};
        this.onDblClickDate = function(){};
    }
    /*
     *	函数说明：创建新日历
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-14
     *
     */
    Calendar.prototype.create = function(){
        this.uniqueID = UniqueID.generator(_UNIQUE_ID_CALENDAR_PREFIX);

        //年月日选择表格创建
        var table = Element.createElement(_TAG_NAME_TABLE);
        var tbody = Element.createElement(_TAG_NAME_TBODY);
        var tr = Element.createElement(_TAG_NAME_TR);

        table.appendChild(tbody);
        tbody.appendChild(tr);

        table.width = "100%";
        table.border = "0";
        table.cellSpacing = "0";
        table.cellPadding = "0";

        //月份选择创建
        var td = Element.createElement(_TAG_NAME_TD);
        tr.appendChild(td);
        td.width = "63";
        var str = [];
        str[str.length] = "<select>";
        str[str.length] = "<option value=\"1\">一月</option>";
        str[str.length] = "<option value=\"2\">二月</option>";
        str[str.length] = "<option value=\"3\">三月</option>";
        str[str.length] = "<option value=\"4\">四月</option>";
        str[str.length] = "<option value=\"5\">五月</option>";
        str[str.length] = "<option value=\"6\">六月</option>";
        str[str.length] = "<option value=\"7\">七月</option>";
        str[str.length] = "<option value=\"8\">八月</option>";
        str[str.length] = "<option value=\"9\">九月</option>";
        str[str.length] = "<option value=\"10\">十月</option>";
        str[str.length] = "<option value=\"11\">十一月</option>";
        str[str.length] = "<option value=\"12\">十二月</option>";
        str[str.length] = "</select>";
        td.innerHTML = str.join("");
        this.monthObj = td.firstChild;

        //年度选择创建
        var td = Element.createElement(_TAG_NAME_TD);
        tr.appendChild(td);
        td.width = "63";
        var str = [];
        str[str.length] = "<select>";
        var beginYear = this.begin.getYear();
        var endYear = this.end.getYear() + 1;
        for(var i=beginYear;i<endYear;i++){
            str[str.length] = "<option value=\""+i+"\">"+i+"年</option>";
        }
        str[str.length] = "</select>";
        td.innerHTML = str.join("");
        this.yearObj = td.firstChild;

        //日期显示创建
        var td = Element.createElement(_TAG_NAME_TD);
        tr.appendChild(td);
        var str = [];
        str[str.length] = "<div class=\"day\">&nbsp;</div>";
        td.innerHTML = str.join("");
        this.dayObj = td.firstChild;


        //日期表格创建
        var table = Element.createElement(_TAG_NAME_TABLE);
        var tbody = Element.createElement(_TAG_NAME_TBODY);
        var tr = Element.createElement(_TAG_NAME_TR);

        table.appendChild(tbody);
        tbody.appendChild(tr);

        table.width = "100%";
        table.border = "1";
        table.cellSpacing = "0";
        table.cellPadding = "0";
        tr.className = "calendarHeader";

        //日期表格表头部分
        for(var i=0;i<_TOTAL_DAY_PER_WEEK;i++){
            var td = Element.createElement(_TAG_NAME_TD);
            tr.appendChild(td);
            td.innerHTML = ["日","一","二","三","四","五","六"][i];
        }
        //日期表格表体部分
        for(var j=0;j<_TOTAL_CALENDAR_ROWS;j++){
            var tr = Element.createElement(_TAG_NAME_TR);
            tbody.appendChild(tr);
            tr.className = "calendarBody";
            for(var i=0;i<_TOTAL_DAY_PER_WEEK;i++){
                var td = Element.createElement(_TAG_NAME_TD);
                tr.appendChild(td);
                td.innerHTML = "&nbsp;";
            }
        }
        this.tableObj = table;
    }
    /*
     *	函数说明：将日历内容放到指定对象中
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.dockTo = function(object){
        this.create();
        this.gotoDate(this.initDate);
        this.attachEvents();

        this.object = object;
        this.object.innerHTML = "";
        this.object.appendChild(this.monthObj.parentNode.parentNode.parentNode.parentNode);
        this.object.appendChild(this.tableObj);
    }
    /*
     *	函数说明：释放日历实例
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.dispose = function(){
    }
    /*
     *	函数说明：显示指定日期
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.gotoDate = function(date){
        //是否改变
        var yearChange = false;
        var monthChange = false;
        var dateChange = false;
        if(null==this.date){
            yearChange = true;
            monthChange = true;
            dateChange = true;
        }else{
            var str1 = this.date.getYear();
            var str2 = date.getYear();
            if(str1!=str2){
                yearChange = true;
            }

            var str1 = this.date.getYear() + this.date.getMonth();
            var str2 = date.getYear() + date.getMonth();
            if(str1!=str2){
                monthChange = true;
            }

            var str1 = this.date.getYear() + this.date.getMonth() + this.date.getDate();
            var str2 = date.getYear() + date.getMonth() + date.getDate();
            if(str1!=str2){
                dateChange = true;
            }
        }

        this.date = date;

        var day = date.getDate();
        var month = date.getMonth();
        var year = date.getYear();

        this.dayObj.innerHTML = day;
        this.monthObj.selectedIndex = month;
        this.yearObj.selectedIndex = year - this.begin.getYear();

        this.refreshTable();

        //触发年份改变事件
        if(true==yearChange){
            this.onYearChange(date);
        }
        //触发月份改变事件
        if(true==monthChange){
            this.onMonthChange(date);
        }
        //触发日期改变事件
        if(true==dateChange){
            this.onDateChange(date);
        }
    }
    /*
     *	函数说明：刷新日期表格
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.refreshTable = function(){
        var month = this.date.getMonth();
        var year = this.date.getYear();
        //指定日期所处月份第一日
        var curMonthFirstDate = new Date(year+"/"+(month+1)+"/1");
        //指定日期所处月份最后一日
        var curMonthLastDate = new Date(year+"/"+(month+2)+"/0");

        //第一日是星期几
        var curMonthFirstDay = curMonthFirstDate.getDay();

        var cells = this.tableObj.getElementsByTagName(_TAG_NAME_TD);
        var beginIndex = _TOTAL_DAY_PER_WEEK;
        var offIndex = curMonthFirstDay;
        //当月日期部分
        for(var i=0;i<curMonthLastDate.getDate();i++){
            var cellIndex = i+beginIndex+offIndex;
            cells[cellIndex].innerHTML = i + 1;

            var className = "";
            if(i==this.date.getDate()-1){
                className = "active";
            }else if(i==this.today.getDate()-1 && this.today.getMonth()==this.date.getMonth() && this.today.getYear()==this.date.getYear()){
                className = "today";
            }

            //自定义样式
            var tempStr = year + "-" + month + "-" + (i + 1);
            var tempStyle = this.dateStyle[tempStr];
            if(null!=tempStyle){
                className += " " + tempStyle;
            }
            cells[cellIndex].className = className;
        }
        //上月日期部分
        var prevMonthLastDate = new Date(year+"/"+(month+1)+"/0");
        for(var i=0;i<offIndex;i++){
            var cellIndex = i+beginIndex;
            cells[cellIndex].innerHTML = prevMonthLastDate.getDate() - offIndex + i + 1;
            cells[cellIndex].className = "prev";
        }
        //下月日期部分
        var lastIndex = curMonthLastDate.getDate() + beginIndex + offIndex;
        for(var i=lastIndex;i<cells.length;i++){
            cells[i].innerHTML = i - lastIndex + 1;
            cells[i].className = "next";
        }
    }
    /*
     *	函数说明：绑定事件
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.attachEvents = function(){
        var thisObj = this;
        this.monthObj.onchange = function(eventObj){
            _Calendar_onMonthChange(eventObj,thisObj);
        };
        this.yearObj.onchange = function(eventObj){
            _Calendar_onYearChange(eventObj,thisObj);
        };
        this.tableObj.onclick = function(eventObj){
            _Calendar_onClick(eventObj,thisObj);
        };
        this.tableObj.ondblclick = function(eventObj){
            _Calendar_onDblClick(eventObj,thisObj);
        };
        this.tableObj.onselectstart = function(eventObj){
            if(null==eventObj){
                eventObj = window.event;
            }
            Event.cancel(eventObj);
        }
    }
    /*
     *	函数说明：设置指定日期单元格样式
     *	参数：
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.setDateStyle = function(date,style){
        var tempStr = date.getYear() + "-" + date.getMonth() + "-" + date.getDate();
        this.dateStyle[tempStr] = style;
        this.refreshTable();    
    }
    /*
     *	函数说明：以文本方式输出对象信息
     *	参数：	
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    Calendar.prototype.toString = function(){
        var str = [];
        str[str.length] = "[Calendar 对象]";
        str[str.length] = "date:" + this.date;
        return str.join("\r\n");

    }




    /*
     *	函数说明：选择月份
     *	参数：  event:eventObj      事件对象
                Calendar:instance   Calendar实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Calendar_onMonthChange(eventObj,instance){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = instance.monthObj;

        var year = instance.date.getYear();
        var date = instance.date.getDate();
        var month = srcElement.value;
        var newDate = new Date(year+"/"+month+"/"+date);

        instance.gotoDate(newDate);
    }
    /*
     *	函数说明：选择年份
     *	参数：  event:eventObj      事件对象
                Calendar:instance   Calendar实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Calendar_onYearChange(eventObj,instance){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = instance.yearObj;

        var month = instance.date.getMonth() + 1;
        var date = instance.date.getDate();
        var year = srcElement.value;
        var newDate = new Date(year+"/"+month+"/"+date);

        instance.gotoDate(newDate);
    }
    /*
     *	函数说明：选择日期
     *	参数：  event:eventObj      事件对象
                Calendar:instance   Calendar实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Calendar_onClick(eventObj,instance){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = Event.getSrcElement(eventObj);

        var cellIndex = srcElement.cellIndex;
        var rowIndex = srcElement.parentNode.rowIndex;
        var absCellIndex = rowIndex * _TOTAL_DAY_PER_WEEK + cellIndex;

        if(0<rowIndex){
            var month = instance.date.getMonth() + 1;
            var year = instance.date.getYear();
            var date = parseInt(srcElement.innerHTML,10);
            if(absCellIndex>(_TOTAL_DAY_PER_WEEK*_TOTAL_CALENDAR_ROWS/2+_TOTAL_DAY_PER_WEEK)){
                if(15>date){
                    month ++;
                }
            }else{
                if(21<date){
                    month --;
                }
            }
            var newDate = new Date(year+"/"+month+"/"+date);
            instance.gotoDate(newDate);
        }
    }
    /*
     *	函数说明：双击日期
     *	参数：  event:eventObj      事件对象
                Calendar:instance   Calendar实例
     *	返回值：
     *	作者：毛云
     *	日期：2006-4-13
     *
     */
    function _Calendar_onDblClick(eventObj,instance){
        if(null==eventObj){
            eventObj = window.event;
        }
        var srcElement = Event.getSrcElement(eventObj);

        var cellIndex = srcElement.cellIndex;
        var rowIndex = srcElement.parentNode.rowIndex;
        var absCellIndex = rowIndex * _TOTAL_DAY_PER_WEEK + cellIndex;

        if(0<rowIndex){
            var month = instance.date.getMonth() + 1;
            var year = instance.date.getYear();
            var date = parseInt(srcElement.innerHTML,10);
            if(absCellIndex>(_TOTAL_DAY_PER_WEEK*_TOTAL_CALENDAR_ROWS/2+_TOTAL_DAY_PER_WEEK)){
                if(15>date){
                    month ++;
                }
            }else{
                if(21<date){
                    month --;
                }
            }
            var newDate = new Date(year+"/"+month+"/"+date);
            instance.onDblClickDate(newDate,srcElement);
        }
    }