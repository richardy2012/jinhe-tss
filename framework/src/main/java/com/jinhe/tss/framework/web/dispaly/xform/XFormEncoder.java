package com.jinhe.tss.framework.web.dispaly.xform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.IDataEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * 将数据添加到XForm模板。
 */
public class XFormEncoder implements IDataEncoder {

    static final String XFORM_NODE_NAME = "xform";
    static final String XFORM_DATA_NODE_NAME = "data";
    static final String XFORM_DATA_NODE_XPATH = "/xform/data";
    static final String XFROM_DECLARE_COLUMN_XPATH = "/xform/declare//column";
    static final String XFORM_DATA_ROW_NODENAME = "row";

    Document document;

    /**
     * 将数据添加到XForm模板
     * @param templetURL 
     *          模板路径
     */
    public XFormEncoder(String templetURL) {
        this(templetURL, new HashMap<String, Object>());
    }
    
    /**
     * 将实体中的数据设置到对应模板中
     * @param templetURL 
     *          模板路径
     * @param entity
     *          XForm数据实体
     */
    public XFormEncoder(String templetURL, IXForm entity) {
        this(templetURL, entity == null ? null : entity.getAttributesForXForm());
    }
 
    /**
     * 将数据添加到XForm模板
     * @param templetURL 
     *          模板路径
     * @param attributesMap
     *          数据
     */
    public XFormEncoder(String templetURL, Map<String, Object> attributesMap) {
        XFormTemplet templet = new XFormTemplet(templetURL);
        document = templet.getTemplet();
        
        // 给data节点添加数据
        if(attributesMap != null) {
            Element dataNode = (Element) document.selectSingleNode(XFORM_DATA_NODE_XPATH);
            if( dataNode == null ) {
                dataNode = new DefaultElement(XFORM_DATA_NODE_NAME);
                document.getRootElement().add(dataNode);
            }
            dataNode.clearContent(); // 增加row节点时先删除原先的row节点
            dataNode.add(XMLDocUtil.map2DataNode(attributesMap, XFORM_DATA_ROW_NODENAME));
        }
    }
    
    /**
     * 将实体中的数据设置到对应模板中
     * @param template
     *           模板内容，注意区别上面的模板路径
     * @param entity
     *           XForm数据实体
     * @param isByColumn
     */
    public XFormEncoder(String template, IXForm entity, boolean isByColumn) {
        this(XMLDocUtil.dataXml2Doc(template), entity.getAttributesForXForm(), isByColumn, true);
    }
 
    public XFormEncoder(Document doc, IXForm entity, boolean isByColumn, boolean isClearOldData) {
        this(doc, entity.getAttributesForXForm(), isByColumn, isClearOldData);
    }

    /**
     * 将实体中的数据设置到对应模板中
     * @param doc
     * @param attributesMap
     * @param isByColumn
     *                根据column列上的属性名过滤属性Map, column上没定义的去掉
     * @param isClearOldData  
     *                是否清除原先的row节点
     */
    public XFormEncoder(Document doc, Map<String, Object> attributesMap, boolean isByColumn, boolean isClearOldData) {
        document = doc;
        
        /* 根据column列上的属性名过滤属性Map, column上没定义的去掉。注意是新建一个新的Map，不去修改传入的Map。*/
        if (isByColumn) {
            Map<String, Object> tempMap = new HashMap<String, Object>();
            List<Element> list = XMLDocUtil.selectNodes(document, "//declare/column");
            for (Element element : list) {
                String key = element.attributeValue("name");
                tempMap.put(key, attributesMap.get(key));
            }
            
            attributesMap = tempMap;
        }
        
        Element dataNode = (Element) document.selectSingleNode(XFORM_DATA_NODE_XPATH);
        if( dataNode == null ) {
            dataNode = new DefaultElement(XFORM_DATA_NODE_NAME);
            document.getRootElement().add(dataNode);
        }
        
        // 如果设定为不清除原先的row节点,则要先将原有row节点下的数据取出统一放入attributesMap中,然后将row节点删除
        if ( !isClearOldData ) {
            Element rowElement = (Element) dataNode.selectSingleNode(XFORM_DATA_ROW_NODENAME);
            Map<String, String> oldValues = XMLDocUtil.dataNodes2Map(rowElement);
           
            // 注意两map的先后顺序,必须是attributesMap后加入，如此可以覆盖旧的row节点上数据
            for (String key : attributesMap.keySet()) {
                Object value = attributesMap.get(key);
                if (value != null) {
                    oldValues.put(key, value.toString());
                }
            }
            
            attributesMap.clear();
            attributesMap.putAll(oldValues);
        }
        
        //增加row节点时先删除原先的row节点
        dataNode.clearContent();
        dataNode.add(XMLDocUtil.map2DataNode(attributesMap, XFORM_DATA_ROW_NODENAME));
    }
 
    /**
     * 设置Column上的属性值
     * 
     * @param columnName 节点名称
     * @param name       属性名称
     * @param value      属性值
     */
    public void setColumnAttribute(String columnName, String name, String value) {
        Element column = (Element) document.selectSingleNode(XFROM_DECLARE_COLUMN_XPATH + "[@name='" + columnName + "']");
        if(column == null){
            throw new BusinessException("名为：" + columnName + "的字段在XFORM模板里不存在，设置属性失败，请联系管理员编辑模板！");
        }
        column.addAttribute(name, value);
    }

    public String toXml() {
        return document.asXML();
    }

    public void print(XmlPrintWriter out) {
        out.append(document.asXML());
    }
}
