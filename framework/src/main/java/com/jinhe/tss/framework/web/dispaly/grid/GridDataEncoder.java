package com.jinhe.tss.framework.web.dispaly.grid;

import org.dom4j.Document;

import com.jinhe.tss.framework.web.dispaly.IDataEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;

/** 
 * Grid对象：生成Grid控件所需要的xml数据
 * 
 */
public class GridDataEncoder implements IDataEncoder {
    
    private static final String GRID_DATA_ROW_NODE_NAME = "row";

    private GridTemplet templet; //模板对象

    private GridParser parser; //解析器

    private Object data; //源数据

    private int dataType; //数据类型
 
    public GridDataEncoder(Object data, String uri) {
        this(data, uri, new SimpleGridParser());
    }
 
    public GridDataEncoder(Object data, Document doc) {
        this(data, doc, new SimpleGridParser());
    }
 
    public GridDataEncoder(Object data, Document doc, GridParser parser) {
        templet = new GridTemplet(doc);
        this.data = data;
        this.dataType = GridNode.TYPE_SIMPLE_NODE;
        this.parser = parser;
        parser.setColumns(templet.getColumns());
    }
 
    public GridDataEncoder(Object data, String uri, GridParser parser) {
        templet = new GridTemplet(uri);
        this.data = data;
        this.dataType = GridNode.TYPE_SIMPLE_NODE;
        this.parser = parser;
        parser.setColumns(templet.getColumns());
    }

    /**
     * 获取xml数据
     */
    public String toXml() {
        StringBuffer sb = new StringBuffer();
        sb.append(templet.getHeader());
        GridNode node = parser.parse(data, dataType);
        if (node != null) {
            sb.append(node.toXml(GRID_DATA_ROW_NODE_NAME));
        }
        sb.append(templet.getFooter());
        return sb.toString();
    }

    /**
     * 获取模板对象
     */
    public GridTemplet getTemplet() {
        return templet;
    }

    public void print(XmlPrintWriter out) {
        out.append(templet.getHeader());
        
        GridNode node = parser.parse(data, dataType);
        if (node != null) {
            out.append(node.toXml(GRID_DATA_ROW_NODE_NAME));
        }
        
        out.append(templet.getFooter());
    }
}
