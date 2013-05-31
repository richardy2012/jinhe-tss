package com.jinhe.tss.framework.component.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.EasyUtils;

public class ParamServiceImpl implements ParamService {

    @Autowired private ParamDao paramDao;

    public Param saveParam(Param param) {
        if (null == param.getId()) {
            param.setSeqNo(paramDao.getNextSeqNo(param.getParentId()));
            judgeLegit(param, ParamConstants.SAVE_FLAG);
            paramDao.create(param);
        }
        else {
            judgeLegit(param, ParamConstants.EDIT_FLAG);
            paramDao.update(param);
        }

        // 刷新参数缓存 ，如果保存的是缓存项，则需要手动的刷新其所属的参数节点
        ParamManager.remove(param.getCode());
        return param;
    }

    /**
     * <p>
     * 字段重复判断。 （区分参数组、参数、参数项的概念） 不同参数的code不可以相同，必须帮助每个参数 的code值对整个参数表中的“参数”唯一
     * </p>
     * 
     * @param param
     */
    private void judgeLegit(Param param, int flag) {
        // 如果保存的是参数（区分参数组、参数、参数项的概念），则要保证code值对所有“参数”唯一
        Integer type = param.getType();
        if (ParamConstants.NORMAL_PARAM_TYPE.equals(type)) {
            String hql = "select p.id from Param p where p.type = ? and p.code = ?";
            List<?> list = paramDao.getEntities(hql, new Object[] { ParamConstants.NORMAL_PARAM_TYPE, param.getCode() });
            if (list.size() > flag) {
                throw new BusinessException("相同参数名称（指CODE）已经存在，请更改参数名称后再保存!");
            }
            return;
        }

        String hql = "select p.id from Param p where p.parentId=" + param.getParentId();
        if (ParamConstants.GROUP_PARAM_TYPE.equals(param.getType())) { // 参数组不能同名
            hql += " and p.name='" + param.getName() + "'";
        }
        else if (ParamConstants.NORMAL_PARAM_TYPE.equals(param.getType())) {
            if (EasyUtils.isNullOrEmpty(param.getName())) {
                param.setName(param.getCode());
            }
            hql += " and (p.name='" + param.getName() + "' or p.code='" + param.getCode() + "')";
        }
        else {
            if (EasyUtils.isNullOrEmpty(param.getText())) {
                param.setText(param.getValue());
            }
            Param parentParam = paramDao.getEntity(param.getParentId());
            param.setModality(parentParam.getModality());
            hql += " and (p.value='" + param.getValue() + "' or p.text='" + param.getText() + "')";
        }

        if (paramDao.getEntities(hql).size() > flag) {
            throw new BusinessException("字段重复! + " + hql);
        }
    }

    public void startOrStop(Long paramId, Integer disabled) {
        List<?> datas = ParamConstants.TRUE.equals(disabled) ? paramDao.getChildrenById(paramId) : paramDao.getParentsById(paramId);
        for (int i = 0; i < datas.size(); i++) {
            Param param = (Param) datas.get(i);
            param.setDisabled(disabled);
            paramDao.updateWithoutFlush(param);
        }
        paramDao.flush();
    }

    public void delete(Long id) {
        // 一并删除子节点
        List<?> params = paramDao.getChildrenById(id);
        for(Object entity : params) {
            Param param = (Param)entity;
            paramDao.delete(param);
            
            // 刷新参数缓存，如果删除的是缓存项，则需要手动的刷新其所属的参数节点
            ParamManager.remove(param.getCode());
        }
    }

    public List<?> getAllParams() {
        return paramDao.getAllParam();
    }

    public Param getParam(Long id) {
        Param param = paramDao.getEntity(id);
        paramDao.evict(param);
        return param;
    }

    public void sortParam(Long paramId, Long toParamId, int direction) {
        paramDao.sort(paramId, toParamId, direction);
    }

    public List<?> copyParam(Long paramId, Long toParamId) {
        Param param = paramDao.getEntity(paramId);
 
        if (null == toParamId) {
            return copyParamLocal(param);
        } else {
            return copyParamTo(param, paramDao.getEntity(toParamId));
        }
    }

    private List<?> copyParamLocal(Param copyParam) {
        List<?> params = paramDao.getChildrenById(copyParam.getId());
        Map<Long, Long> paramMapping = new HashMap<Long, Long>(); // 复制出来的新节点 与 被复制源节点 建立一一对应关系（ID 对 ID）
        for (int i = 0; i < params.size(); i++) {
            Param param = (Param) params.get(i);
            Long sourceParamId = param.getId();
           
            paramDao.evict(param);
            param.setId(null);
            if (sourceParamId.compareTo(copyParam.getId()) == 0) { // 复制指定节点
                if (ParamConstants.GROUP_PARAM_TYPE.equals(copyParam.getType())) {
                    param.setName(ParamConstants.COPY_PREFIX + copyParam.getName());
                }
                else if (ParamConstants.NORMAL_PARAM_TYPE.equals(copyParam.getType())) {
                    param.setCode(ParamConstants.COPY_PREFIX + copyParam.getCode());
                    param.setName(ParamConstants.COPY_PREFIX + copyParam.getName());
                }
                else {
                    param.setText(ParamConstants.COPY_PREFIX + copyParam.getText());
                }
                param.setSeqNo(paramDao.getNextSeqNo(copyParam.getParentId()));
            }
            // 复制指定节点的儿孙节点
            else {
                param.setParentId( paramMapping.get(param.getParentId()) );
            }
            
            paramDao.create(param);
            paramMapping.put(sourceParamId, param.getId());
        }
        return params;
    }

    private List<?> copyParamTo(Param copyParam, Param toParam) {
        Long copyParamId = copyParam.getId();
        List<?> params = paramDao.getChildrenById(copyParamId);
        Map<Long, Long> paramMapping = new HashMap<Long, Long>(); // 复制出来的新节点 与 被复制源节点 建立一一对应关系（ID 对 ID）
        for (int i = 0; i < params.size(); i++) {
            Param param = (Param) params.get(i);
            Long sourceParamId = param.getId();
            
            paramDao.evict(param);
            param.setId(null);
            if (sourceParamId.compareTo(copyParamId) == 0) {
                param.setParentId(toParam.getId());
                param.setSeqNo(paramDao.getNextSeqNo(toParam.getId()));
            }
            else {
                param.setParentId(paramMapping.get(param.getParentId()));
            }

            if (ParamConstants.TRUE.equals(toParam.getDisabled())) {
                param.setDisabled(toParam.getDisabled()); // 如果目标根节点是停用状态，则所有新复制出来的节点也一律为停用状态
            }

            judgeLegit(param, ParamConstants.EDIT_FLAG);
            
            paramDao.create(param);
            paramMapping.put(sourceParamId, param.getId());
        }
        return params;
    }

    public void move(Long paramId, Long toParamId) {
        List<?> params = paramDao.getChildrenById(paramId);
        Param toParam = paramDao.getEntity(toParamId);
        for (int i = 0; i < params.size(); i++) {
            Param param = (Param) params.get(i);
            if (param.getId().equals(paramId)) { // 判断是否是移动节点（即被移动枝的根节点）
                param.setSeqNo(paramDao.getNextSeqNo(toParamId));
                param.setParentId(toParamId);
            }
            
            if (ParamConstants.TRUE.equals(toParam.getDisabled())) {
                param.setDisabled(ParamConstants.TRUE); // 如果目标根节点是停用状态，则所有新复制出来的节点也一律为停用状态
            }
            
            judgeLegit(param, ParamConstants.EDIT_FLAG);
            paramDao.update(param);
        }
    }

    public Object[] getCanAddParams(Integer mode) {
        List<?> paramAndGroups = paramDao.getCanAddParamsAndGroups(mode);
        List<?> paramIds = paramDao.getCanAddParams(mode);
        return new Object[] { paramAndGroups, EasyUtils.list2Str(paramIds) };
    }

    public Object[] getCanAddGroups() {
        return new Object[] { paramDao.getCanAddGroups(), null };
    }

    /* ********************************************* 以下供ParamManager调用 ************************************ */
    
    public Param getParam(String code) {
        return paramDao.getParamByCode(code);
    }

    public String getSimpleParamValue(String code) {
        Param param = paramDao.getParamByCode(code);
        if (!ParamConstants.SIMPLE_PARAM_MODE.equals(param.getModality())) {
            throw new BusinessException("不是简单参数!");
        }
        return param.getValue();
    }

    public List<Param> getComboParam(String code) {
        Param param = paramDao.getParamByCode(code);
        if (!ParamConstants.COMBO_PARAM_MODE.equals(param.getModality())) {
            throw new BusinessException("不是下拉型参数!");
        }
        return paramDao.getChildrenByDecode(param.getDecode());
    }

    public List<Param> getTreeParam(String code) {
        Param param = paramDao.getParamByCode(code);
        if (!ParamConstants.TREE_PARAM_MODE.equals(param.getModality())) {
            throw new BusinessException("不是树型参数!");
        }
        return paramDao.getChildrenByDecode(param.getDecode());
    }
}
