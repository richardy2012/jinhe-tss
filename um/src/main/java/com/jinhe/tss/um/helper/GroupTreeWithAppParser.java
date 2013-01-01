package com.jinhe.tss.um.helper;

import java.util.HashMap;
import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;

/**
 * 其他用户组树解析器
 */
public class GroupTreeWithAppParser extends GroupTreeParser {

    public TreeNode parse(Object data) {
        Object[] objs = (Object[]) data;
        
        TreeNode root = new TreeNode();
        Map<Long, TreeNode> treeNodeMap = new HashMap<Long, TreeNode>();
        
        // 解析其它用户组，挂到所属的应用节点下面
        parseOtherGroups2Tree(objs, 0, root, treeNodeMap);
        
        // 其它用户组根节点设置为不可选
        if(root.getChildren().size() > 0) {
            TreeNode otherGroupRoot = root.getChildren().get(0);
            otherGroupRoot.setAttribute("canselected", "0");
        }
        
        return root;
    }
}
