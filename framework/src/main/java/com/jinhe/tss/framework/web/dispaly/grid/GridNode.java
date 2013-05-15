package com.jinhe.tss.framework.web.dispaly.grid;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.DateUtil;
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

	private int dataType = TYPE_NULL; //数据类型
	
	private GridColumn[] columns; //节点属性名称数组

	private Object[] values; //节点属性值数组

	private List<GridNode> children = new ArrayList<GridNode>(); //子节点：GridNode对象

    public GridNode() {
    }

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
            case TYPE_ARRAY:
                throw new BusinessException("此类型的Grid数据尚未支持！");
            case TYPE_JAVA_BEAN:
                throw new BusinessException("此类型的Grid数据尚未支持！");
            default:
                throw new BusinessException("生成Gird的数据类型不符合要求！");
        }
	}

	/**
	 * 增加子节点。
	 * 
	 * @param child
	 */
	public void addChild(GridNode child) {
		this.children.add(child);
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
			sb.append(" _depth=\"").append(level).append("\">");
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
				return true;
			}
		}
		return false;
	}
}
