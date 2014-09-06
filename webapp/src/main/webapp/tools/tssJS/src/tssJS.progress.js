;(function($){

	/*
	 *	大数据显示进度
	 *	参数：	string:url                      同步进度请求地址
				xmlNode:data                    XmlNode实例
				string:cancelUrl                取消进度请求地址
	 */
	var Progress = function(url, data, cancelUrl) {
		this.progressUrl = url;
		this.cancelUrl = cancelUrl;
		this.id = UniqueID.generator();
		this.refreshData(data);
	};

	Progress.prototype = {
		/* 更新数据 */
		refreshData: function(data) {
			this.percent      = $.XML.getText(data.querySelector("percent"));
			this.delay        = $.XML.getText(data.querySelector("delay"));
			this.estimateTime = $.XML.getText(data.querySelector("estimateTime"));
			this.code         = $.XML.getText(data.querySelector("code"));

			var feedback = data.querySelector("feedback");
			if( feedback ) {
				alert($.XML.getText(feedback));
			}
		},

		/* 开始执行  */
		start: function() {
			this.show();
			this.next();
		},

		/* 停止执行  */
		stop: function() {
			var pThis = this;
			$.ajax({
				url: this.cancelUrl + this.code,
				method: "DELETE",
				onsuccess: function() {
					pThis.hide();
					clearTimeout(pThis.timeout);
				}
			});
		},

		/* 显示进度  */
		show: function() {
			var pThis = this;

			var graph = $.createElement("div", "progressBar");
			$(graph).center().css("height", "25px").css("width", "500px").css("border", "1px solid #F8B3D0");

			var bar = $.createElement("strong");
			$(bar).css("display", "block").css("backgroundColor", "green").css("float", "left")
				.css("height:100%").css("textAlign", "center").css("width", this.percent + "%");	 
			$(bar).innerHTML = bar.style.width; 

			var info = $.createElement("strong");
			$(info).html("剩余时间:<span style='font-size:16px;font-weight:bold'>" + this.estimateTime + "</span>秒");

			var cancel = $.createElement("span");
			$(cancel).html("<a href='#' style='margin-top:30px;color:#5276A3;text-decoration:underline'>取 消</a>")
				.click(pThis.stop);

			graph.appendChild(bar);
			graph.appendChild(info);
			graph.appendChild(cancel);
			document.body.appendChild(graph);
		}

		/* 隐藏进度 */
		hide: function() {
			var barObj = $1(this.id);
			if( barObj ) {
				barObj.style.visibility = "hidden";
			}
		}

		/* 同步进度  */
		sync: function() {
			var pThis = this;
			$.ajax({
				url: this.progressUrl + this.code,
				method: "GET",
				async: false,
				onresult: function() {
					var data = this.getNodeValue("ProgressInfo");
					pThis.refreshData(data);
					pThis.show();
					pThis.next();
				},
				onexception: function() {
					pThis.hide();
				}
			});
		},

		/* 延时进行下一次同步  */
		next: function() {
			var pThis = this;

			var percent = parseInt(this.percent);
			var delay   = parseInt(this.delay) * 1000;
			if(100 > percent) {
				this.timeout = setTimeout(function() {
					pThis.sync();
				}, delay);
			}
			else if( this.oncomplete ) {
				setTimeout(function() {
					pThis.hide();
					pThis.oncomplete();
				}, 200);
			}
		}
	}

	$.Progress = Progress;

})(tssJS);
