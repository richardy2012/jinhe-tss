package com.jinhe.tss.framework.persistence;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

import com.jinhe.tss.util.BeanUtil;

/**
 * 查询条件抽象类。
 */
public abstract class QueryCondition  {
    
    protected int currentPage = 1;
    
    protected int pagesize = 50;

    /** condition.conditionsMap["'" + key + "'", value] */
    protected Map<String, Object> conditionsMap = new HashMap<String, Object>();
    
    protected void initConditionsMap() {
        BeanUtil.addBeanProperties2Map(this, conditionsMap);
    }

    /**
     * 给查询条件Query参数赋值
     * @param query
     */
    public void genQueryCondition(Query query) {
        for(String key : conditionsMap.keySet()){
            if(key.equals("currentPage") || key.equals("pagesize")) continue;
            
            Object value = conditionsMap.get(key);
            if ( isValueNullOrEmpty(value) ) {
                continue;
            }
            
            if ( value instanceof String && !key.startsWith("exact")) {
                query.setParameter(key,  "%" + value + "%");
                continue;
            }
            
            query.setParameter(key, value);
        }
    }
    
    public static boolean isValueNullOrEmpty(Object value) {
        if ( value == null ) {
            return true;
        }
        
        if( value instanceof String && "".equals(value)) {
            return true;
        }
        
        if( value instanceof Collection<?> && ((Collection<?>)value).isEmpty()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 生成查询条件
     * @return
     */
    public String genHQLCondition() {
        initConditionsMap();
        
        StringBuffer sb = new StringBuffer();
        sb.append(" 0 = 0 ");

        for(String key : conditionsMap.keySet()){
            if(key.equals("currentPage") || key.equals("pagesize")) continue;
            
            Object value = conditionsMap.get(key);
            if ( isValueNullOrEmpty(value) ) {
                continue;
            }
            
            if ( value instanceof String ) {
                if(key.startsWith("exact")) {
                    sb.append(" and o." + key + " = :" + key);
                    continue;
                } else {
                    sb.append(" and o." + key + " like :" + key);
                    continue;
                }
            }
            
            if ((value instanceof Collection)) {
                sb.append(" and o." + key + " in (:" + key + ")");
                continue;
            }
            
            // 如果是时间，则判断是否有: ****TimeFrom 。。。 ****TimeTo 。。。
            if ( value instanceof Date ) {
                if(key.endsWith("From")) {
                    sb.append(" and o." + key + " >= :" + key);
                } 
                else if(key.endsWith("To")) {
                    sb.append(" and o." + key + " <= :" + key);
                } 
                else {
                    sb.append(" and o." + key + " = :" + key);
                }
                continue;
            }
            
            sb.append(" and o." + key + " = :" + key);
        }

        return  sb.append(" ").toString();
    }
 
    public Map<String, Object> getConditionsMap() {
        return conditionsMap;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

}

