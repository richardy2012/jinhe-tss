package com.jinhe.tss.framework.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * DAO的一些基本方法
 * 
 * @param <T>
 */
public interface IDao<T extends IEntity> {
	
	Class<T> getType();

    /** 使PO成为游离状态，即非托管状态 （detached entity） */
    void evict(Object o);

    /** 刷新缓存，将一级缓存的托管实体（attached entity）更新到数据库 */
    void flush();

    /**
     * 保存对象：新增或者修改
     * @param obj
     * @return
     */
    T create(T obj);
    
    T createWithoutFlush(T entity);
    
    Object createObject(Object entity);
    
    Object createObjectWithoutFlush(Object entity);

    /**
     * 更新实体，merge  
     * @param entity
     */
    Object update(Object entity);

    Object updateWithoutFlush(Object entity);

    /**
     * 根据主键值删除对象记录
     * @param clazz
     * @param id
     */
    Object delete(Class<?> clazz, Serializable id);

    T deleteById(Serializable id);
    
    Object delete(Object entity);

    void deleteAll(Collection<?> c);

    /**
     * 根据主键值获取对象，延迟方式
     * @param clazz
     * @param id
     * @return
     */
    IEntity loadEntity(Class<?> clazz, Serializable id);

    /**
     * 根据主键值获取对象
     * @param clazz
     * @param id
     * @return
     */
    IEntity getEntity(Class<?> clazz, Serializable id);
    T getEntity(Serializable id);
    
    /**
     * 根据HQL语句和参数值获取对象列表
     * @param hql
     * @return List,never return null
     */
    List<?> getEntities(String hql, Object...conditionValues);
    List<?> getEntities(String hql, Object[] conditionNames, Object[] conditionValues);
    
    /**
     * 根据原生SQL查询
     * 
     * @param nativeSql
     * @param entityClazz
     * @param params
     * @return
     */
    List<?> getEntitiesByNativeSql(String nativeSql, Class<?> entityClazz, Object...params);
    List<?> getEntitiesByNativeSql(String nativeSql, Object...params);
 
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
    Object[] getEntities(QueryCondition condition, String className);
    Object[] getEntities(QueryCondition condition, String className, String others);

    /**
     * 执行HQL语句，一般为delete、update类型
     * @param hql
     */
    void executeHQL(String hql, Object...params);
    void executeHQL(String hql, String[] argNames, Object[] params);
    
    /**
     * 执行SQL语句，一般为delete、update类型
     * @param hql
     */
    void executeSQL(String sql, Object...params);
    void executeSQL(String sql, String[] argNames, Object[] params);
    
    /**
     * 将ID列表插入临时表，以用于查询时候关联使用。用于替代 IN 查询。
     */
    void insertIds2TempTable(List<?> list);
    void insertIds2TempTable(List<? extends Object[]> list, int idIndex);
    void insertIds2TempTable(Object[] idArray);
    void insertEntityIds2TempTable(List<? extends IEntity> list);
    void clearTempTable();

}
