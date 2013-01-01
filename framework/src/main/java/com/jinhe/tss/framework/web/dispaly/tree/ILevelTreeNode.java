package com.jinhe.tss.framework.web.dispaly.tree;

/** 
 * 多层树节点接口。
 */
public interface ILevelTreeNode extends ITreeNode {

    /**
     * 获取父节点编号
     * @return
     */
    Long getParentId();

    /**
     * 获取自身节点编号
     * @return
     */
    Long getId();

}
