package com.jinhe.tss.framework.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.HibernateException;

/**
 * Query 语句生成器
 * 
 */
class HQLQueryGenerator {
	/**
	 * 格式为字符串 如：o.id
	 */
	private List<String> selects = new ArrayList<String>();
	
	/**
	 * 格式为一个数组 {表名（实体名）,别名}，如：{"User" ,"u"}
	 */
	private List<String> froms = new ArrayList<String>();  
	
    /**
     * 格式为字符串  如： "o.id <= :?"
     */
    private List<String> wheres = new ArrayList<String>();
    
	/**
	 * 查询条件列表，格式为一个数组  {名字, 值}
	 */
	private List<Object> conditionValues = new ArrayList<Object>();
	
	/**
	 * 用于设置其他的，如order by等
	 */
	private String others;
	
	/**
	 * 查询条件对象
	 */
	private QueryCondition condition;
	
	public HQLQueryGenerator() {
    }

    public HQLQueryGenerator(QueryCondition qyCondition) {
        this.condition = qyCondition;
    }

    private class SelectPart {
        private StringBuffer sb = new StringBuffer();

        public SelectPart() {
            for (Iterator<String> it = selects.iterator(); it.hasNext();) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append((String) it.next());
            }
        }

        public String toString() {
            if (sb.length() == 0) {
                return "";
            }
            return "select " + sb.toString();
        }
    }

    private class FromPart {
        private StringBuffer sb = new StringBuffer();

        public FromPart() {
            for (Iterator<String> it = froms.iterator(); it.hasNext();) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(it.next());
            }
        }

        public String toString() {
            return " from " + sb.toString();
        }
    }

    private class WherePart {
        private StringBuffer sb = new StringBuffer();

        public WherePart() {
            for (Iterator<String> it = wheres.iterator(); it.hasNext();) {
                if (sb.length() > 0) {
                    sb.append(" and ");
                }
                sb.append((String) it.next());
            }
            if (condition != null) {
                if (sb.length() > 0) {
                    sb.append(" and ");
                }
                sb.append(condition.genHQLCondition());
            }
        }

        public String toString() {
            if (sb.length() == 0) {
                return "";
            }
            return " where " + sb.toString() + " ";
        }
    }

    /**
     * 设置参数值到Query对象中
     * 
     * @param query
     * @param session
     * @param ht
     * @param tablesInfo
     * @return
     * @throws HibernateException
     */
    public Query getQuery(EntityManager em) throws HibernateException {
        String hql = genHql();

        Query query = em.createQuery(hql);

        for (int i = 0; i < conditionValues.size(); i++) {
            query.setParameter(i, conditionValues.get(i));
        }
        if (condition != null) {
            condition.genQueryCondition(query);
        }
        return query;
    }

    /**
     * 添加查询表table，和查询列selectStr
     * 
     * @param selectColumn
     * @param table
     */
    public void addQueryInfo(String selectColumn, String table) {
        if (table != null) {
            froms.add(table);
        }
        addQueryColumn(selectColumn);
    }

    public void addQueryColumn(String selectColumn) {
        if (selectColumn != null) {
            selects.add(selectColumn);
        }
    }

    /**
     * 添加其他的条件，例如order by
     * @param others
     */
    public void setOthers(String others) {
        if (others != null) {
            this.others = others;
        }
    }

    /**
     * 生成hql语句
     * @return
     */
    private String genHql() {
        String hql = new SelectPart().toString() + new FromPart() + new WherePart();
        if( this.others != null ) {
            hql += this.others;
        }
        return hql;
    }
}
