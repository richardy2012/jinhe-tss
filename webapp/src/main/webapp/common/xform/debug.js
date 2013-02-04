    var Debug = {};
    Debug.print = function(str,clear){

        var debugObj = window.document.getElementById("debug");
        if(null==debugObj){
            debugObj = window.document.createElement("textarea");
            debugObj.id = "debug";
            debugObj.readOnly = true;
            debugObj.cols = 80;
            debugObj.rows = 20;
            debugObj.style.border = "1px solid gray";

            var boxObj = window.document.createElement("div");
            boxObj.style.position = "absolute";
            boxObj.style.left = "200px";
            boxObj.style.top = "150px";
            boxObj.style.border = "1px solid gray";
            boxObj.style.paddingTop = "12px";
            boxObj.style.paddingLeft = "2px";
            boxObj.style.paddingRight = "2px";
            boxObj.style.paddingBottom = "2px";
            boxObj.style.backgroundColor = "#CCCCFF";

            var clearObj = window.document.createElement("div");
            clearObj.style.position = "absolute";
            clearObj.style.right = "3px";
            clearObj.style.top = "0px";
            clearObj.style.fontSize = "10px";
            clearObj.style.fontFamily = "Verdana";
            clearObj.style.cursor = "hand";
            clearObj.innerText = "clear";

            window.document.body.appendChild(boxObj);
            boxObj.appendChild(debugObj);
            boxObj.appendChild(clearObj);

            clearObj.onclick = function(){
                debugObj.value = "";
            }
            debugObj.onmousedown = function(){
                event.returnValue = false;
                event.cancelBubble = true;
            }
            debugObj.onblur = function(){
                boxObj.style.display = "none";
            }
            boxObj.onmousedown = function(){
                this.isMouseDown = true;
                this.setCapture();

                this.offX = event.clientX - this.offsetLeft;
                this.offY = event.clientY - this.offsetTop;
            }
            boxObj.onmouseup = function(){
                this.isMouseDown = false;
                this.releaseCapture();
            }
            boxObj.onmousemove = function(){
                if(true==this.isMouseDown){
                    this.style.left = event.clientX - this.offX;
                    this.style.top = event.clientY - this.offY;
                }
            }
            debugObj.focus();
        }else{
            debugObj.parentNode.style.display = "";
            debugObj.focus();
        }

        if(true==clear){
            debugObj.value = "";
        }
        debugObj.value += str + "\r\n";
        debugObj.scrollTop = debugObj.scrollHeight;
    }