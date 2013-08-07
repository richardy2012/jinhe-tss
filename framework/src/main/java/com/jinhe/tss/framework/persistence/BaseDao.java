package com.jinhe.tss.framework.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.EasyUtils;

/**
 * DAO的一些基本方法
 */
public abstract class BaseDao<T extends IEntity> implements IDao<T>{
    
    protected final Logger log = Logger.getLogger(getClass());
    
    @PersistenceContext 
    protected EntityManager em; //em.setFlushMode(FlushModeType.COMMIT);

    protected Class<T> type;
    
    public BaseDao(Class<T> type) {
        this.type = type;
    }
    
    public Class<T> getType() {
    	return type;
    }
    
    public void evict(Object o) {
        em.detach(o); // <==> getHibernateTemplate().evict(o);
    }

    public void flush() {
        em.flush();
    }
 
    public T create(T entity) {
        em.persist(entity);
        em.flush();
        em.refresh(entity);
        return entity;
    }
    
    public Object createObject(Object entity) {
        em.persist(entity);
        em.flush();
        em.refresh(entity);
        return entity;
    }
    
    public T createWithoutFlush(T entity) {
        em.persist(entity);
        return entity;
    }
    
    public Object createObjectWithoutFlush(Object entity) {
        em.persist(entity);
        return entity;
    }
    
    public Object update(Object entity) {
    	em.merge(entity);
        em.flush();
        return entity;
    }
    
    public Object updateWithoutFlush(Object entity) {
        em.merge(entity);
        return entity;
    }

    /**
     * 根据主键值删除对象记录
     * @param clazz
     * @param id
     */
    public Object delete(Class<?> clazz, Serializable id) {
        Object entity = em.find(clazz, id);
        em.remove(entity);
        return entity;
    }
    
    public T deleteById(Serializable id) {
        T entity = getEntity(id);
        this.delete(entity);
        return entity;
    }

    public Object delete(Object entity) {
        em.remove(entity);
        return entity;
    }

    public void deleteAll(Collection<?> c) {
        for (Iterator<?> it = c.iterator(); it.hasNext();) {
            delete(it.next());
        }
    }

    /**
     * 根据主键值获取对象，延迟方式
     * @param clazz
     * @param id
     * 
     * @return
     */
    public IEntity loadEntity(Class<?> clazz, Serializable id) {
        return (IEntity) em.find(clazz, id);
    }

    /**
     * 根据主键值获取对象 
     * @param clazz
     * @param id
     * 
     * @return
     */
    public IEntity getEntity(Class<?> clazz, Serializable id) {
        return (IEntity) em.find(clazz, id);
    }
    
    public T getEntity(Serializable id) {
        return em.find(type, id);
    }

    /**
     * 根据HQL语句和参数值获取对象列表.
     * 
     * @param hql
     * @return
     */
    public List<?> getEntities(String hql, Object...conditionValues) {
        Query query = em.createQuery(hql);
        if (conditionValues != null)
            for (int i = 0; i < conditionValues.length; i++) {
                Object param = conditionValues[i];
                if (param == null) {
                    throw new BusinessException("执行HQL为：" + hql  + " 查询的时候验证参数出错，第 " + (i + 1) + " 个参数值为null！");
                }
                query.setParameter(i + 1, param); // 从1开始，非0
            }
        List<?> results = query.getResultList();
        return results == null ? new ArrayList<Object>() : results;
    }

    /**
     * 根据HQL语句、参数名、参数值获取对象列表.
     * 
     * @param hql
     * @return
     */
    public List<?> getEntities(String hql, Object[] conditionNames, Object[] conditionValues) {
        Query query = em.createQuery(hql);
        if (conditionValues != null)
            for (int i = 0; i < conditionValues.length; i++) {
                Object param = conditionValues[i];
                if (param == null) {
                    throw new BusinessException("执行HQL为：" + hql  + " 查询的时候验证参数出错，第 " + (i + 1) + " 个参数值为null！");
                }
                
                if (param instanceof Object[])
                    query.setParameter((String) conditionNames[i], Arrays.asList((Object[]) param));
                else
                    query.setParameter((String) conditionNames[i], param);
            }
        List<?> results = query.getResultList();
        return results == null ? new ArrayList<Object>() : results;
    }
    
    /**
     * 根据查询条件和分页信息查询对象列表
     * 注：
     *    如果是多表查询，则子类中需要重写 Object[] getEntities(...)
     * @param condition
     * @param className
     * @param currentPageNum
     * @param pagesize
     * @return
     */
    public Object[] getEntities(QueryCondition condition, String className){
        return getEntities(condition, className, null);
    }
    
    public Object[] getEntities(QueryCondition condition, String className, String others) {
        int currentPageNum = condition.getCurrentPage(); 
        int pagesize = condition.getPagesize(); 
        
        int firstResult = pagesize * (currentPageNum - 1);

        HQLQueryGenerator generator = new HQLQueryGenerator(condition);
        generator.addQueryInfo("o", className + " o ");
        generator.setOthers(others);   // eg: order  by o.commitTime
        
        List<?> list = null;
        try {
            Query query = generator.getQuery(em);
            if(pagesize > 0 && currentPageNum >= 0){
                query.setFirstResult(firstResult);
                query.setMaxResults(pagesize);
            }            
            list = query.getResultList();
        } catch (Exception e) {
            throw new BusinessException("按分页取数据时出错！", e);
        }   
        return new Object[] { list, new Integer(getTotalRows(condition, className).intValue())};
    }
    
    /**
     * 或者符合查询条件的总记录数
     * @param condition
     * @param className
     * @return
     */
    private Integer getTotalRows(QueryCondition condition, String className) {
        String hql = "select count(o) from " + className + " o where " + condition.genHQLCondition();
        try {
            Query query = em.createQuery(hql);
            condition.genQueryCondition(query);
            return EasyUtils.convertObject2Integer(query.getSingleResult());
        } catch (Exception e) {
            throw new BusinessException("获取总记录数时出错！", e);
        }       
    }
    
    /**
     * 根据原生SQL查询
     * 
     * @param nativeSql
     * @param entityClazz
     * @param params
     * @return
     */
    public List<?> getEntitiesByNativeSql(String nativeSql, Class<?> entityClazz, Object...params) {
        Query query = em.createNativeQuery(nativeSql, entityClazz);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param == null) {
                    throw new BusinessException("执行原生SQL为：" + nativeSql + " 查询的时候验证参数出错，第 " + (i + 1) + " 个参数值为null!");
                }
                query.setParameter(i + 1, param);  // 从1开始，非0
            }
        }
        return query.getResultList();
    }
    
    public List<?> getEntitiesByNativeSql(String nativeSql, Object...params) {
        Query query = em.createNativeQuery(nativeSql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param == null) {
                    throw new BusinessException("执行原生SQL为：" + nativeSql + " 查询的时候验证参数出错，第 " + (i + 1) + " 个参数值为null!");
                }
                query.setParameter(i + 1, param);  // 从1开始，非0
            }
        }
        return query.getResultList();
    }
    
    /**
     * 执行HQL语句，一般为delete、update类型
     * @param hql
     */
    public void executeHQL(String hql, Object...params){
        Query query = em.createQuery(hql);
        if(params != null && params.length > 0) {
            for(int i = 0; i < params.length; i++){
                query.setParameter(i + 1, params[i]); // 从1开始，非0
            }
        }
        
        query.executeUpdate();
        em.flush();
        em.clear();        
    }
    
    public void executeHQL(String hql, String[] argNames, Object[] params){
        Query query = em.createQuery(hql);
        if(params != null && params.length > 0) {
            for(int i = 0; i < params.length; i++){
                query.setParameter(argNames[i], params[i]);
            }
        }
        
        query.executeUpdate();
        em.flush();
        em.clear();        
    }
    
    public void executeSQL(String sql, Object...params){
        Query query = em.createNativeQuery(sql);
        if(params != null && params.length > 0) {
            for(int i = 0; i < params.length; i++){
                query.setParameter(i + 1, params[i]); // 从1开始，非0
            }
        }
        
        query.executeUpdate();
        em.flush();
        em.clear();        
    }
    
    public void executeSQL(String sql, String[] argNames, Object[] params){
        Query query = em.createNativeQuery(sql);
        if(params != null && params.length > 0) {
            for(int i = 0; i < params.length; i++){
                query.setParameter(argNames[i], params[i]);
            }
        }
        
        query.executeUpdate();
        em.flush();
        em.clear();        
    }
    
    public void insertIds2TempTable(List<?> list) {
        clearTempTable();
        
        if( !EasyUtils.isNullOrEmpty(list) ) {
            for(Object id : list){
                Temp entity = new Temp();
                entity.setId(EasyUtils.convertObject2Long(id));
                createObjectWithoutFlush(entity);
            }
        }

    }
    
    public void insertIds2TempTable(List<? extends Object[]> list, int idIndex) {
        clearTempTable();
 
        if( !EasyUtils.isNullOrEmpty(list) ) {
            for(Object[] objs : list){
                Temp entity = new Temp();
                entity.setId((Long) objs[idIndex]);
                createObjectWithoutFlush(entity);
            }
        }
    }
    
    public void insertIds2TempTable(Object[] idArray) {
        clearTempTable();
        
        if(idArray != null && idArray.length > 0 ) {
            for(int i = 0; i < idArray.length; i++){
                Temp entity = new Temp();
                entity.setId(new Long(idArray[i].toString()));
                createObjectWithoutFlush(entity);
            }
        }
    }
    
    public void insertEntityIds2TempTable(List<? extends IEntity> list) {
        clearTempTable();
        
        if( !EasyUtils.isNullOrEmpty(list) ) {
            for(IEntity entity : list){
                Temp temp = new Temp();
                temp.setId((Long) entity.getId());
                createObjectWithoutFlush(temp);
            }
        }
    }
    
    public void clearTempTable() {
        deleteAll(getEntities("from Temp"));
    }
}
