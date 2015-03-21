package com.jinhe.tss.demo.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.exception.BusinessException;

@Service("XXService")
public class XXServiceImpl implements XXService {
	
    @Autowired XXDao XXDao;
    
    public XX getXX(Long id) {
        XX entity = XXDao.getEntity(id);
        XXDao.evict(entity);
        return entity;
    }
 
    @SuppressWarnings("unchecked")
    public List<XX> getAll() {
        return (List<XX>) XXDao.getEntities("from XX o order by o.decode");
    }
 
    public XX save(XX entity) {
        if ( entity.getId() == null ) {
            entity.setSeqNo(XXDao.getNextSeqNo(entity.getParentId()));
            XXDao.create(entity);
        }
        else {
        	XXDao.refreshEntity(entity);
        }
        return entity;
    }
    
    public XX delete(Long id) {
    	 XX entity = getXX(id);
         List<XX> list1 = XXDao.getChildrenById(id, XX.OPERATION_DELETE); // 一并删除子节点
         List<XX> list2 = XXDao.getChildrenById(id);
         
         if(list1.size() < list2.size()) {
         	throw new BusinessException("你的权限不足，无法删除整个枝。");
         }
         return XXDao.deleteXX(entity);
    }
 
    public void sort(Long startId, Long targetId, int direction) {
        XXDao.sort(startId, targetId, direction);
    }

    public void move(Long id, Long targetId) {
        List<XX> list  = XXDao.getChildrenById(id);
        for (XX temp : list) {
            if (temp.getId().equals(id)) { // 判断是否是移动节点（即被移动枝的根节点）
                temp.setSeqNo(XXDao.getNextSeqNo(targetId));
                temp.setParentId(targetId);
            }
 
            XXDao.moveEntity(temp);
        }
    }
}