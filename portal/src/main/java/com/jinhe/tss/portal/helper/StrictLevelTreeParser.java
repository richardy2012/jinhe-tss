package com.jinhe.tss.portal.helper;

import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.portal.PortalConstants;

/** 
 * 严格的多层树型解析器。
 * 
 * 如果某个节点的父节点丢失，则其子节点全部丢失。
 * 
 * @see com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser
 */
public class StrictLevelTreeParser extends LevelTreeParser {
    
    protected void composeTree(TreeNode root, ILevelTreeNode entity, Map<Long, TreeNode> treeNodeMap) {
        Long parentId = entity.getParentId();
       
        TreeNode parent   = treeNodeMap.get(parentId);
        TreeNode treeNode = treeNodeMap.get(entity.getId());
        
        if( PortalConstants.ROOT_ID.equals( parentId ) ) {
            root.addChild(treeNode);
        }
        else if(parent != null){
            parent.addChild(treeNode);
        }
    }

}
