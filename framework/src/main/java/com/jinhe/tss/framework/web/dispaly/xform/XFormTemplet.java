package com.jinhe.tss.framework.web.dispaly.xform;

import org.dom4j.Document;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * XForm模板文件。
 */
public class XFormTemplet {

    // 模板缓存池
    public final static String XFORM_TEMPLATE_POOL = "xform_template";
    
    static Pool pool = JCache.getInstance().getCachePool(XFORM_TEMPLATE_POOL);

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
