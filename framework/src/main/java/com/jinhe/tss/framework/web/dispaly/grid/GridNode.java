package com.jinhe.tss.framework.web.dispaly.grid;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.DateUtil;
import com.jinhe.tss.util.MathUtil;
import com.jinhe.tss.util.XmlUtil;

/** 
 * 网格Grid行节点对象
 * 
 */
public class GridNode {
    
	public static final int TYPE_NULL         = -1; //此节点为空节点
	public static final int TYPE_SIMPLE_NODE  = 0;  //数据为一个符合IGridNode接口的对象
	public static final int TYPE_ARRAY        = 1;  //数据为一个数组
	public static final int TYPE_JAVA_BEAN    = 2;  //数据为一个 Java Bean 对象
	public static final int TYPE_COMPLEX_NODE = 3;  //数据为一个符合IComplexGridNode接口的对象

	private int dataType = TYPE_NULL; //数据类型
	
	private GridColumn[] columns = null; //节点属性名称数组

	private Object[] values; //节点属性值数组

	private GridNode parent; //父节点，可用于合计（将合计信息放置到父节点）
	
	private List<GridNode> children = new ArrayList<GridNode>(); //子节点：GridNode对象

	private List<GridAttributesMap> details = new ArrayList<GridAttributesMap>(); //节点详细信息

	private Map<String, Object> additionalAttibutes = new HashMap<String, Object>(); //附加属性
	
    public GridNode() { }

	/**
	 * 构造器
	 * 
	 * @param object
	 * @param dataType
	 * @throws Exception
	 */
	public GridNode(Object item, GridColumn[] columns, int dataType) {
		this.columns = columns;
		if (this.columns == null) {
			this.columns = new GridColumn[0];
		}
		
		// 解析数据
		this.dataType = dataType;
		switch (this.dataType) {
            case TYPE_SIMPLE_NODE:
                IGridNode node = (IGridNode) item;
                values = node.getAttributes(new GridAttributesMap(getColumnNames())).getValues();
                break;
            case TYPE_COMPLEX_NODE:
                IComplexGridNode complexNode = (IComplexGridNode) item;
                details = complexNode.getNodeDetails();
                values  = complexNode.getAttributes(new GridAttributesMap(getColumnNames())).getValues();
                break;
            case TYPE_ARRAY:
                throw new BusinessException("此类型的Grid数据尚未支持！");
            case TYPE_JAVA_BEAN:
                throw new BusinessException("此类型的Grid数据尚未支持！");
            default:
                throw new BusinessException("生成Gird的数据类型不符合要求！");
        }
	}

	/**
	 * 处理合计子节点数据的情况 将节点的值，累加到父节点上
	 * 
	 * @param values
	 */
	public void addValuesToParent(GridNode parent) {
		for (int i = 0; i < columns.length; i++) {
			addValueToParent(i, values[i], parent);
		}
	}

	/**
	 * 处理合计子节点数据的情况 将节点的值，累加到父节点上
	 */
	private void addValueToParent(int index, Object value, GridNode parent) {
		if (columns[index].isSum() && parent != null) {
			if (GridColumn.GRID_COLUMN_CLASS_TYPE_DOUBLE.equals(columns[index].getClassType())) {
				parent.addDoubleValue(index, (Double) value);
			}
			if (GridColumn.GRID_COLUMN_CLASS_TYPE_INTEGER.equals(columns[index].getClassType())) {
				parent.addIntegerValue(index, (Integer) value);
			}
		}
	}

	/**
	 * 对相应的值做相加操作。
	 * 递归操作，parent再把值加到自己的parent上。
	 * 
	 * @param index
	 * @param value
	 */
	private void addDoubleValue(int index, Double value) {
		if (!isValid()) return;
		
		values[index] = MathUtil.addDoubles((Double) values[index], value);
        if (parent != null) {
            parent.addDoubleValue(index, value);
        }
	}

	/**
	 * 对相应的值做相加操作。
	 * 递归操作，parent再把值加到自己的parent上。
	 * 
	 * @param index
	 * @param value
	 */
	public void addIntegerValue(int index, Integer value) {
	    if (!isValid()) return;
	    
	    values[index] = MathUtil.addInteger((Integer) values[index], value);
        if (parent != null) {
            parent.addIntegerValue(index, value);
        }
	}

	/**
	 * 增加子节点。并将child相应的列值累积到父节点上
	 * 
	 * @param child
	 */
	public void addChild(GridNode child) {
		this.children.add(child);
		child.parent = this;
		child.addValuesToParent(this);
	}

	/**
	 * 树型节点转换成xml
	 */
	public String toXml(String nodeName) {
		return toXml(nodeName, 0);
	}

	/**
	 * 逐层将GridNode输出为XML数据格式。
	 * 按层次递归调用，每深一层，level ++
	 * 
	 * @param nodeName
	 * @param level  层级
	 * @return
	 */
	public String toXml(String nodeName, int level) {
		StringBuffer sb = new StringBuffer();
		if (dataType != TYPE_NULL) {
			sb.append("<").append(nodeName);
			for (int index = 0; index < columns.length; index++) {
				sb.append(" ").append(columns[index].getName()).append("=\"").append(getFormatedValue(index)).append("\"");
			}
			if (additionalAttibutes.size() > 0) {
				for (Entry<String, Object> entry : additionalAttibutes.entrySet()) {
					sb.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
				}
			}
			sb.append(" _depth=\"").append(level).append("\">");
		}
		
		if (details != null) {
			sb.append(getDetailNodesXML());
		}
		
		for (GridNode childNode : children) {
			sb.append(childNode.toXml(nodeName, level + 1));
		}
		if (dataType != TYPE_NULL) {
			sb.append("</").append(nodeName).append(">");
		}
		return sb.toString();
	}

	/**
	 * 获取格式化的数据
	 * 
	 * @param index
	 * @return
	 */
	private String getFormatedValue(int index) {
		GridColumn column = columns[index];
		String pattern = column.getPattern();
		String classType = column.getClassType();
		
        if (GridColumn.GRID_COLUMN_CLASS_TYPE_DATE.equals(classType)) {
			if (pattern == null || "".equals(pattern)) {
				pattern = DateUtil.DEFAULT_DATE_PATTERN; 
			}
			return DateUtil.format((Date) values[index], pattern);
		}
        
		if (GridColumn.GRID_COLUMN_CLASS_TYPE_DOUBLE.equals(classType) || GridColumn.GRID_COLUMN_CLASS_TYPE_INTEGER.equals(classType)) {
		    return formatNumber(values[index], pattern);
		}
		
		return XmlUtil.toFormXml(values[index]);
	}
	
   /**
     * 根据给定的pattern格式化数字类数据为字符串
     */
    private static String formatNumber(Object value, String pattern) {
        if (value == null || Double.valueOf(value.toString()) == 0) {
            return "";  // null 和 0 都不显示
        } 
        
        if (pattern == null || "".equals(pattern)) {
            return value.toString();
        }
        return new DecimalFormat(pattern).format(value);
    }

	/**
	 * 获取节点详细信息xml
	 */
	private String getDetailNodesXML() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < details.size(); i++) {
			sb.append("<detail ");
			sb.append(details.get(i).toString());
			sb.append("/>");
		}
		return sb.toString();
	}

	/**
	 * 获取column.name 数组
	 */
	private String[] getColumnNames() {
		String[] names = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			names[i] = columns[i].getName();
		}
		return names;
	}

	/**
	 * 根据列名给本GridNode行的该列设值
	 */
	public boolean setColumnValue(String name, Object value) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getName().equals(name)) {
				values[i] = value;
				addValueToParent(i, value, this.parent);
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断此节点是否无效（空节点）
	 */
	private boolean isValid() {
		return dataType != TYPE_NULL;
	}
}
