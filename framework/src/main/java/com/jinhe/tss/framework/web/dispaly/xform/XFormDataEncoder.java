package com.jinhe.tss.framework.web.dispaly.xform;

import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.jinhe.tss.framework.web.dispaly.IDataEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * <p> XForm数据编码器：</p>
 * <p>
 * 将实现IXForm接口的对象或Map对象的数据转换成XForm数据节点的XML字符串
 * </p>
 * 
 */
public class XFormDataEncoder implements IDataEncoder {
	
	private static final String XFORM_DATA_ROW_NODENAME = "row";
	private static final String XFORM_DATA_NODENAME = "data";

	private String dataXml;

	public XFormDataEncoder(IXForm entity) {
		dataXml = encode(entity);
	}

	/**
	 * 将实体中的数据转换为XML格式，输出格式为： <data><row id="" name="" ..../></data>
	 * 
	 * @param entity
	 * @return
	 */
	public String encode(IXForm entity) {
		Map<String, Object> attributesMap = entity.getAttributesForXForm();
		
		Element dataNode = new DefaultElement(XFORM_DATA_NODENAME);
		Element rowNode = XMLDocUtil.map2DataNode(attributesMap, XFORM_DATA_ROW_NODENAME);
		dataNode.add(rowNode);
		
		return dataNode.asXML();
	}

	public String toXml() {
		return dataXml;
	}

	public void print(XmlPrintWriter out) {
		out.append(dataXml);
	}

}
