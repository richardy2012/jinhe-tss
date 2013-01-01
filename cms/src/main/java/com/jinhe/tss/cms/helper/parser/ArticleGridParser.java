package com.jinhe.tss.cms.helper.parser;

import java.util.List;

import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.framework.web.dispaly.grid.GridNode;
import com.jinhe.tss.framework.web.dispaly.grid.GridParser;
import com.jinhe.tss.util.EasyUtils;

/** 
 * 文章列表Grid解析器，设置置顶标记
 */
public class ArticleGridParser extends GridParser {

    public GridNode parse(Object data, int dataType) {
		GridNode root = new GridNode();
		List<?> dataList = (List<?>)data;
		
        if( !EasyUtils.isNullOrEmpty(dataList) ){
			for ( Object temp : dataList ) {
                Article article = (Article) temp;
			    GridNode gridNode = new GridNode(article, super.columns, dataType);
			    gridNode.setColumnValue("isTop", article.getIsTop()); //置顶

				root.addChild(gridNode);
			}
        }
		return root;
    }
}