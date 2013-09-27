package com.jinhe.tss.framework.web.dispaly.xform;

import org.dom4j.Document;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.cache.CacheLife;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * XForm模板文件。
 */
public class XFormTemplet {

    // 模板缓存池
    static Pool pool = JCache.getInstance().getPool(CacheLife.NODEAD.toString());

    /**
     * XForm数据文件Docment对象
     */
    private Document doc;

    public XFormTemplet(String uri) {
        if ( EasyUtils.isNullOrEmpty(uri) ) {
            throw new RuntimeException("没有定义xform模板文件的路径！");
        }
        
        Cacheable obj = pool.getObject(uri);
        if (obj == null) {
            obj = pool.putObject(uri, XMLDocUtil.createDoc(uri));
        }
        doc = (Document) obj.getValue();
    }
 
    public Document getTemplet() {
        return (Document) doc.clone();
    }

}
