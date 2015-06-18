<div class="shadow-box-noshadow search-wrap-list container" id="${id}">
 <ul class="search-wrap-list-ul">
 
 </ul>
</div>

function search() {
    //var ${id} = $1("${id}"); 
    //${id}.load(params, "${common.articlePageUrl}");

    var searchStr = $.Query.get("searchStr") || '西湖';
    var siteId    = $.Query.get("siteId") || 1;
    var page      = $.Query.get("page") || 1;
    var pageSize  = 20;

    // 创建正则，解决中文问题
    searchStr = unescape(searchStr.replace(/\|/gim, "%"));

    $.ajax({
		url: "/tss/auth/article/search",
		params: {searchStr:searchStr, siteId:siteId, page:page, pageSize:pageSize},
		method: "POST",
		waiting: true,
		onresult: function(){
			var resultXml = this.getNodeValue("ArticleList");
			var totalPage = $("totalPageNum", resultXml).text();
            var articleNodes = $("item", resultXml);
            if( articleNodes.length ) {
                articleNodes.each( function(i, articleNode){
                    var id =  $("id", articleNode).text();
                    var title =  $("title", articleNode).text();
                    var author =  $("author", articleNode).text();
                    var summary =  $("summary", articleNode).text();
                    var issueDate =  $("issueDate", articleNode).text();

                    var li = $.createElement("li");
                    $(".search-wrap-list-ul").appendChild(li);
                    
                    var html =     
                        '<h2><a href="${common.articlePageUrl}&articleId=' + id + '" target="_blank">${item.title}</a></h2>' + 
                        '<div class="mob-author"><span class="author">${item.author}</span><time class="time">${item.issueDate}</time></div>' + 
                        '<div class="mob-summay"> 摘要：${item.summary}... <a href="${common.articlePageUrl}&articleId=${item.id}" target="_blank">[详细]</a></div>';
                    $(li).html(html);
                });

                // setPage
                // articlePageUrl + "/" + id
            }
		}
	});
}
 
 
