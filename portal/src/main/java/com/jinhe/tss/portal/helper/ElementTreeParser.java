package com.jinhe.tss.portal.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.portal.entity.ElementGroup;

/** 
 * 门户元素和组的树型解析器。 两层结构：组 和 元素
 */
public class ElementTreeParser implements ITreeParser {
   
    public TreeNode parse(Object data) {
        @SuppressWarnings("unchecked")
        List<ILevelTreeNode> list = (List<ILevelTreeNode>)data;  // 继承ILevelTreeNode接口的实体列表
        
        TreeNode root = new TreeNode();
        if( list == null || list.isEmpty() ) {
            return root;
        }

        Map<Long, TreeNode> groupNodeMap = new HashMap<Long, TreeNode>();
        for(ILevelTreeNode entity : list) {
            if(entity instanceof ElementGroup) { // 组节点
                groupNodeMap.put(entity.getId(), new TreeNode(entity));
            }
        }
        
        for(ILevelTreeNode entity : list) {
            if( !(entity instanceof ElementGroup) ) { // 元素节点
                Long parentId = entity.getParentId();
                TreeNode parent   = groupNodeMap.get(parentId);
                
                if(parent != null){
                    parent.addChild(new TreeNode(entity));
                }
            }
        }
        
        for(TreeNode groupNode : groupNodeMap.values()) {
            root.addChild(groupNode);
        }
        
        return root;
    }

     
}
