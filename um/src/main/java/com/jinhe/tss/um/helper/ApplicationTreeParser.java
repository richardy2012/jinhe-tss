package com.jinhe.tss.um.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.ITreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.util.EasyUtils;

/**
 * 应用系统，资源类型树型结构解析器。
 * 注：此处只显示了平台应用及其所属的资源类型和权限操作选项，
 *    其他应用不在本视图里显示（在其他用户组根节点下显示）。
 */
public class ApplicationTreeParser implements ITreeParser {
 
	public TreeNode parse(Object data) {
		Object[] datas = (Object[]) data;
		List<?> apps = (List<?>) datas[0];
		List<?> resourceTypes = (List<?>) datas[1];
		List<?> operations = (List<?>) datas[2];
		
		TreeNode root = new TreeNode();
		if ( EasyUtils.isNullOrEmpty(apps) ) {
		    return root;
		}
		
		Application app = new Application();
		app.setId(Long.valueOf(UMConstants.PLATFORM_SYSTEM_APP));
		app.setName(UMConstants.PLATFORM_SYSTEM_NAME);
		app.setApplicationType(UMConstants.PLATFORM_SYSTEM_APP);		
		TreeNode platformParent = new TreeNode(app);	
		
		Map<ResourceType, TreeNode> rtTreeNodeMap = new LinkedHashMap<ResourceType, TreeNode>();
		if (resourceTypes != null && operations != null) {
			for (Object resourceTypeObj : resourceTypes) {
				ResourceType resourceType = (ResourceType) resourceTypeObj;
				TreeNode resourceTypeNode = new TreeNode(resourceType);
				rtTreeNodeMap.put(resourceType, resourceTypeNode);
				
				for (Object operationObj : operations) {
					Operation operation = (Operation) operationObj;
					TreeNode operationNode = new TreeNode(operation);
					if (resourceType.getApplicationId().equals(operation.getApplicationId())
							&& resourceType.getResourceTypeId().equals(operation.getResourceTypeId())) {
						resourceTypeNode.addChild(operationNode);
					}  
				}
			}
		}
		
		for (Object appObj : apps) {
			Application application = (Application) appObj;
			TreeNode appNode = new TreeNode(application);
			
			for(ResourceType resourceType : rtTreeNodeMap.keySet()) {
			    if (application.getApplicationId().equals(resourceType.getApplicationId())) {
			        appNode.addChild(rtTreeNodeMap.get(resourceType));
			    }
			}
			
			if (UMConstants.PLATFORM_SYSTEM_APP.equals(application.getApplicationType())) {
				platformParent.addChild(appNode);
			} 
		}
			
		root.addChild(platformParent);
		return root;
	}
}