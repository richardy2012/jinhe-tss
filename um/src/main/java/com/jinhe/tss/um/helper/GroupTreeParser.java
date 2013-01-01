package com.jinhe.tss.um.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.ITreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.util.EasyUtils;

/**
 * <p>
 * 用户组，应用系统树型结构解析器
 * </p>
 */
@SuppressWarnings("unchecked")
public class GroupTreeParser implements ITreeParser {
 
	public TreeNode parse(Object data) {
		Object[] objs = (Object[]) data;
		List<Group> mainAndAssistantGroups = (List<Group>) objs[0];
		
		TreeNode root = new TreeNode();
		Map<Long, TreeNode> treeNodeMap = new HashMap<Long, TreeNode>();
		
		// 解析主用户组和辅助用户组
		if ( !EasyUtils.isNullOrEmpty(mainAndAssistantGroups) ) {
	        for (Group group : mainAndAssistantGroups) {
	            TreeNode item = new TreeNode(group);
	            treeNodeMap.put(group.getId(), item);
	        }
	        
	        parserGroup(root, (List<Group>) mainAndAssistantGroups, treeNodeMap);
		}
		
		// 解析其它用户组，挂到所属的应用节点下面
		parseOtherGroups2Tree(objs, 1, root, treeNodeMap);
		
		return root;
	}

    protected void parseOtherGroups2Tree(Object[] data, int startIndex, TreeNode root, Map<Long, TreeNode> treeNodeMap) {
        Group otherAppGroupRoot = (Group) data[startIndex++];
        List<Group> otherGroups = (List<Group>) data[startIndex++];
        List<Application> apps  =  (List<Application>) data[startIndex];
        
        if ( otherAppGroupRoot == null ) return;
        
		TreeNode otherGroupRoot = new TreeNode(otherAppGroupRoot);
		root.addChild(otherGroupRoot);
		
		if (apps == null || otherGroups == null)  return;
		
		for ( Application app : apps ) {
        	TreeNode appNode = new TreeNode(app);
        	otherGroupRoot.addChild(appNode);
        	
        	List<Group> otherGroupsUnderApp = new ArrayList<Group>(); // 当前应用下的用户组列表
        	treeNodeMap.clear();
        	for (Group group : otherGroups) {
        		// 筛选出属于当前应用的所有用户组
        		if (app.getApplicationId().equals(group.getApplicationId())) {
					otherGroupsUnderApp.add(group);
					treeNodeMap.put(group.getId(), new TreeNode(group));
				}
        	}
        	parserGroup(appNode, otherGroupsUnderApp, treeNodeMap);
        }
    }

	/**
	 * 解析用户组节点， 挂靠到各自的父节点下
	 * @param root 应用的根节点
	 * @param groups 
	 * @param treeNodeMap
	 */
    protected void parserGroup(TreeNode root, List<Group> groups, Map<Long, TreeNode> treeNodeMap) {
		for ( Group group : groups ) {
			TreeNode groupParent = (TreeNode) treeNodeMap.get(group.getParentId());
		    TreeNode treeNode = (TreeNode) treeNodeMap.get(group.getId());
		    if (groupParent == null) {
		    	root.addChild(treeNode);
		    } else {
		    	groupParent.addChild(treeNode);
		    }
		}
	}
}
