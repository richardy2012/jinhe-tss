package com.jinhe.tss.portal.permission;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.engine.model.Node;
import com.jinhe.tss.portal.engine.model.PortalNode;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.filter.IPermissionFilter;
import com.jinhe.tss.um.permission.filter.PermissionTag;

/**
 * 过滤门户节点结构树。
 */
public class PermissionFilter4Portal implements IPermissionFilter {

	@Override
	public void doFilter(Object args[], Object returnValue, PermissionTag tag, PermissionHelper helper) {
		Node node = (Node) returnValue;
		String method = (String) args[2];
		
        String operation;
        if("maintain".equals(method)) {
            operation = PortalConstants.PORTAL_EDIT_OPERRATION;
        }
        else if("view".equals(method)) {
            operation = PortalConstants.PORTAL_VIEW_OPERRATION;
        } 
        else if("browse".equals(method)) {
            operation = PortalConstants.PORTAL_BROWSE_OPERRATION;
        } 
        else {
            operation = PortalConstants.PORTAL_BROWSE_OPERRATION;
        }
		
        List<Long> permitedResourceIds = helper.getResourceIdsByOperation(tag.application(), PortalConstants.PORTAL_RESOURCE_TYPE, operation);
        
        doFiltrate(node, permitedResourceIds);
	}
	
	/**
     * 过滤门户节点结构树，递归过程
     * @param node
     * @param permitedResouceIds
     */
    private void doFiltrate(Node node, List<Long> permitedResouceIds){
        if(node instanceof PortalNode && !permitedResouceIds.contains(node.getId())){
        	 throw new BusinessException("您对当前门户【" + node.getName()+ "】没有浏览访问权限！");
        }
        
        Set<Node> children = node.getChildren();
        for(Iterator<Node> it = children.iterator(); it.hasNext();){
        	Node child = it.next();
            if( !permitedResouceIds.contains(child.getId()) ){
                it.remove();
                continue;
            }
            doFiltrate(child, permitedResouceIds);
        }
    }

}
