package com.jinhe.tss.framework.persistence;

import java.util.List;

import com.jinhe.tss.framework.persistence.entityaop.IDecodable;

/**
 * 支持树形结构的实体操作的DAO。
 */
public interface ITreeSupportDao<T extends IDecodable> extends IDao<T> {

    /**
     * 读取同一层下节点的下一序号（当前最大序号 + 1）
     * @param entityName
     * @param parentId
     * @return
     */
    Integer getNextSeqNo(Long parentId);
    Integer getNextSeqNo(String entityName, Long parentId);
    Integer getNextSeqNo(Long parentId, String parentIdName );
    Integer getNextSeqNo(String entityName, Long parentId, String parentIdName);

    /**
     * 读取排序时移动节点和目标节点之间的节点列表。
     * @param entityName
     * @param parentId
     * @param sourceOrder
     * @param targetOrder
     * @return
     */
    List<T> getRelationsNodeWhenSort(Long parentId, Integer sourceOrder, Integer targetOrder);

    /**
     *  读取指定节点的所有子节点，不包括指定节点自身
     * @param entityName
     * @param decode
     * @return
     */
    List<T> getChildrenByDecode(String decode);

    /**
     * 读取指定节点的所有子节点，连同指定节点一并返回
     * @param id
     * @return
     */
    List<T> getChildrenById(Long id);
    List<?> getChildrenById(String entityName, Long id);
    
    /**
     * 读取指定节点的所有子节点，不包含指定节点
     * @param id
     * @return
     */
    List<T> getChildrenExcludeSelfById(Long id);
    List<T> getChildrenExcludeSelfByDocode(String decode);

    /**
     * 读取指定节点的所有父节点，连同指定节点一并返回
     * @param id
     * @return
     */
    List<T> getParentsById(Long id);
    List<?> getParentsById(String entityName, Long id);
    
    /**
     * 读取指定节点（id）到截止节点（breakId）之间的所有父节点，连同指定节点和截止节点一并返回。
     * @param id
     * @param breakId 截止节点的ID
     * @return
     */
    List<T> getParentsById(Long id, Long breakId);
    List<?> getParentsById(String entityName, Long id, Long breakId);
    
    /**
     * 对同层节点进行排序。
     * @param id 排序节点
     * @param targetId  排序节点目标位置的节点
     * @param direction -1：向上移动  1：向下移动
     * @return
     */
    List<T> sort(Long id, Long targetId, int direction);
    
    /**
     * 保存树形结构的对象，并维护其decode值。
     * @param entity
     */
    void saveDecodeableEntity(T entity);

}
