// accordion
;(function($) {

	var Accordion = function($el, multiple) {
		this.el = $el || {};
		this.multiple = multiple || false;

		var links = this.el.find('.link'), oThis = this;
		links.click( function() {
			oThis.dropdown(this);
		} );
	}

	Accordion.prototype.open = function(link) {
		// 计算高度，默认submenu的高度为 36 * 8 = 288
		var $parent = $(link.parentNode);
		var $submenu = $parent.find(".submenu");
		var liSize = $submenu.find("li").size();

		$parent.addClass("open");
		$submenu.addClass("open");
		$submenu.css("height", (liSize * 36) + "px");			
	}

	Accordion.prototype.openFirst = function() {
		var oThis = this;
		this.el.find('.link').each(function(i, link) {
			if( !$(link.parentNode).hasClass("hidden") ) {
				oThis.open(link);
				return false;
			}
		});
	}

	Accordion.prototype.dropdown = function(link) {
		var $el = this.el,
			$li = $(link),
			$parent = $(link.parentNode);

		$el.find('li').removeClass('open');

		var $submenu = $parent.find('.submenu');

		// 已经打开的则关闭掉
		if( $submenu.hasClass("open") ) {
			$submenu.removeClass("open").css("height", "0px");
		}
		else {
			if (!this.multiple) { // 如不允许打开多个，先关闭所有已经打开的
				$el.find('.submenu').removeClass('open').css("height", "0px");
			};

			this.open(link);
		}
	}	

	$.fn.extend({
		accordion : function(multiple) {
			return new Accordion(this, false);
		}
	})

})(tssJS);


// init
;(function($) {
	$.extend({ 
		openMenu: function(li) {
			var $li = $(li);
			var mid = $li.attr("mid");
			if( !mid ) return;

			$(".link").each(function(i, link) {
				$link = $(link);
				if( $link.hasClass(mid) ) {
					$(link.parentNode).removeClass("hidden");
				} else {
					$(link.parentNode).addClass("hidden");
				}
			});

			$("header li").removeClass("active");
			$li.addClass("active");
			accordion.openFirst();

			hideOther();
		},

		openReport: function(a) {
			var $a  = $(a);
			var rid = $a.attr("rid");
			if( !rid ) return;

			var id = "rp_" + rid, iframeId = "iframe_" + rid;
			var $ul = $("footer ul");
			var $lis = $ul.find("li");
			var maxVisible = Math.floor( (document.body.offsetWidth - 225)/135 );

			$ul.find("li").removeClass("active");
			$(".other ul li").removeClass("active");
			$("section .main iframe").hide();

			var $li = $("#" + id);
			if( !$li.length ) {
				var li = $.createElement("li", "", id);
				li.a = a;
				li.index = $lis.length;

				// 插入到footer ul				 
				if( li.index < maxVisible ) {
					$ul.appendChild(li);
				}
				else {
					var ul = $ul[0];
					ul.insertBefore(li, $lis[maxVisible]);
				}

				$li = $(li);
				$li.html("<span>" + $a.html() + "</span><i>X</>");

				// 创建一个iframe，嵌入报表
				var iframeEl = $.createElement("iframe", "", iframeId);
				$("section .main").appendChild(iframeEl);

				// TODO 换成report_portlet.html?id=rid
				$(iframeEl).attr("frameborder", 0).attr("src", "test.html");

				// 添加事件
				$li.find("span").click(function() {
					$.openReport(a);
				});
				$li.find("i").click( function() {
					$li.remove();
					$(iframeEl).remove();

					// 如果关闭的是active li，则需要先切换至第一个li
					if( $li.hasClass("active") ) {
						var first = $ul.find("li")[0];
						first && first.a && $.openReport(first.a);
					}
				} );
			}

			$("#" + iframeId).show();
			$li.addClass("active");
			hideOther();

			// 如果li在不可见区域，则使之可见
			var li = $li[0];
			if( li.index >= maxVisible ) {
				var ul = $ul[0];
				ul.insertBefore(li, $lis[maxVisible]);
			}
		}
	});

	// TODO 先过滤报表的权限
	var accordion = $('#ad1').accordion(false);

	$("footer .switch").toggle(
		function() {
			$("header").hide();
			$("section>.left").hide();
			$("section .main").css("width", "100%");
			$("section").css("padding-bottom", "30px");
		},
		function() {
			$("header").show();
			$("section>.left").show();
			$("section .main").css("width", "83%");
			$("section").css("padding-bottom", "66px");
		}
	);

	$("header li").each(function(i, li) {
		$li = $(li);
		$li.attr("onclick", "$.openMenu(this)");

		if(i == 0) {
			$.openMenu(li);
		}
	});

	$(".submenu a").each(function(i, a) {
		$a = $(a);
		$a.attr("href", "javascript:void(0);");
		$a.attr("onclick", "$.openReport(this)");

		// 初次加载时，默认打开第一个首页
		if(i == 0) {
			$.openReport(a);
		}
	});

	// 右下角三条杠功能实现
	function hideOther() {
		$(".other").hide(); 

		var $lis = $(".other ul li[id]");
		$lis.each(function(i, li) {
			$("footer ul").appendChild( li );
		});
	}
	$("footer>div").toggle(
		function() { 
			$(".other").show(true); 

			var maxVisible = Math.floor( (document.body.offsetWidth - 225)/135 );
			var $lis = $("footer ul li");
			$lis.each(function(i, li) {
				if(i > maxVisible) $(".other ul").appendChild( li );
			});
		}, hideOther);

	$(".other li.b1").click(function() {
		window.location.href = "/tss/login.html";
	});
	$(".other li.b2").click(function() {
		window.location.href = "/tss/index.portal";
	});
	$(".other li.bc").click(function() {
		$("footer ul li").remove();
		$(".other ul li[id]").remove();
		$("section .main iframe").remove();
		hideOther();
	});

})(tssJS);