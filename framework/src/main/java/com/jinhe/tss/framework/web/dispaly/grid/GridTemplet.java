package com.jinhe.tss.framework.web.dispaly.grid;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.cache.strategy.CacheConstants;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.XMLDocUtil;

/** 
 * <p> GridTemplet.java </p> 
 * 
 * Grid模板对象。
 * 格式如：
 *  <?xml version="1.0" encoding="UTF-8"?>
    <grid version="2">
        <declare sequence="true" header="radio">
            <column caption="" name="id" mode="string" display="none"/>
            <column caption="用户名" name="loginName" mode="string" align="center"/>
            <column caption="姓名"   name="userName"  mode="string" align="center"/>
        </declare>
        <data>
        </data>
    </grid>

 *  或者带band（区）的：
    <grid version="2">
        <declare sequence="true">
            <band name="curAppInfo" caption="applicationName">
                <column caption="用户ID" name="id" mode="string" display="none"/>
                <column caption="用户名" name="loginName" mode="string" align="center"/>
                <column caption="所在组ID" name="groupId" mode="string" align="center" display="none"/>
                <column caption="所在组" name="groupName" mode="string" align="center"/>
            </band>
            <band name="appInfo" caption="主用户组">
                <column caption="用户ID" name="appUserId" mode="string" align="center" display="none"/>
                <column caption="用户名" name="appLoginName" mode="string" align="center"/>
                <column caption="所在组ID" name="appGroupId" mode="string" align="center" display="none"/>
                <column caption="所在组" name="appGroupName" mode="string" align="center"/>
            </band>
        </declare>
        <data></data>
    </grid>
 * 
 */
public class GridTemplet {
    
    private static final String GIRD_DECLARE_COLUMN_XPATH = "/grid/declare//column";

    private static final String GIRD_DECLARE_XPATH = "/grid/declare";

    private static final String ATTRIBUTE_NAME = "@name";

    private static final String ATTRIBUTE_CALSS_TYPE = "@classType";

    private static final String ATTRIBUTE_IS_SUM = "@isSum";

    private static final String ATTRIBUTE_PATTERN = "@pattern";
    
    //  Grid模板缓存池
    private static Pool pool = JCache.getInstance().getCachePool(CacheConstants.GRID_TEMPLATE_POOL);

    private Document doc; // 模板文件

    public GridTemplet(String uri) {
        if (uri == null || "".equals(uri)) {
            throw new BusinessException("没有定义Gird模板文件的路径！uri = " + uri);
        }
        
        Cacheable obj = pool.getObject(uri);
        if (obj == null) {
            doc = XMLDocUtil.createDoc(uri);
            obj = pool.putObject(uri, doc);
        }
        doc = (Document) obj.getValue();
    }
    
    public GridTemplet(Document doc) {
        this.doc = doc;
    }

    public GridColumn[] getColumns() {
        List<Element> columns = XMLDocUtil.selectNodes(doc, GIRD_DECLARE_COLUMN_XPATH);
        if (columns == null) {
            return new GridColumn[0];
        }
        GridColumn[] items = new GridColumn[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            Node node = columns.get(i);
            items[i] = new GridColumn();
            items[i].setName(node.valueOf(ATTRIBUTE_NAME));
            items[i].setClassType(node.valueOf(ATTRIBUTE_CALSS_TYPE));
            items[i].setSum("true".equalsIgnoreCase(node.valueOf(ATTRIBUTE_IS_SUM)));
            items[i].setPattern(node.valueOf(ATTRIBUTE_PATTERN));
        }
        return items;
    }

    /**
     * 返回模板头部字符串： <grid …… <data>部分， 如：
        <grid version="2">
        <declare sequence="true" header="radio">
            <column caption="" name="id" mode="string" display="none"/>
            <column caption="用户名" name="loginName" mode="string" align="center"/>
        </declare>
        <data>
     * @return
     */
    public String getHeader() {
        StringBuffer sb = new StringBuffer(doc.asXML());
        int index = sb.lastIndexOf("</data>");
        if (index != -1) {
            sb.delete(index, sb.length());
        } else {
            index = sb.lastIndexOf("/>");
            sb.delete(index, sb.length());
            sb.append(">");
        }
        return sb.toString();
    }

    /**
     * 返回模板尾部字符串： </data> </grid>部分
     * 
     * @return
     */
    public String getFooter() {
        return "</data></grid>";
    }

    /**
     * 设置Column上的属性值。
     * setColumnAttribute("status", "editortext", "待审批");  ==> <column name="status" ... editortext="待审批"/>
     */
    public void setColumnAttribute(String columnName, String name, String value) {
        Element column = (Element) doc.selectSingleNode(GIRD_DECLARE_COLUMN_XPATH + "[@name='" + columnName + "']");
        column.addAttribute(name, value);
    }

    /**
     * 设置模板Declear节点属性值
     */
    public void setDeclareAttribute(String name, String value) {
        Element column = (Element) doc.selectSingleNode(GIRD_DECLARE_XPATH);
        column.addAttribute(name, value);
    }
}
