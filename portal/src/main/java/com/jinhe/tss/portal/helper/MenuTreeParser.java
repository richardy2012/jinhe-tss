package com.jinhe.tss.portal.helper;

import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.portal.PortalConstants;

public class MenuTreeParser  extends LevelTreeParser {
 
    protected void composeTree(TreeNode root, ILevelTreeNode entity, Map<Long, TreeNode> treeNodeMap) {
        Long parentId = entity.getParentId();
        TreeNode treeNode = treeNodeMap.get(entity.getId());
        
        if(PortalConstants.ROOT_ID.equals(parentId)) {
            root.addChild(treeNode);
        } 
        else {
            TreeNode parent = treeNodeMap.get(parentId);
            parent.addChild(treeNode);
        }
    }

}


