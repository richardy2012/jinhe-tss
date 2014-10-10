package com.jinhe.tss.portal.helper;

import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.portal.entity.Structure;

/**
 * 创建门户结构导航菜单时，用以弹出选择。
 */
public class PSTreeTranslator4CreateMenu implements ITreeTranslator {
	
	private final int type;

	public PSTreeTranslator4CreateMenu(int type) {
		this.type = type;
	}

	public Map<String, Object> translate(Map<String, Object> attributes) {
	    Object psType = attributes.get("type");
	    switch(type) {
	    case 3:
	        if(psType.equals(Structure.TYPE_PORTAL)) {
	            attributes.put(TreeNode.TREENODE_ATTR_CANSELECTED, "0"); 
	        }
	        break;
	    case 2:
	        if(psType.equals(Structure.TYPE_PORTAL) || psType.equals(Structure.TYPE_PAGE)) {
	            attributes.put(TreeNode.TREENODE_ATTR_CANSELECTED, "0"); 
	        }
	        break;
	    case 1:  // 此处为菜单替换方式中目标版面项，可以选择版面或者页面
	        if(psType.equals(Structure.TYPE_PORTAL) || psType.equals(Structure.TYPE_PORTLET_INSTANCE)) {
	            attributes.put(TreeNode.TREENODE_ATTR_CANSELECTED, "0"); 
	        }
	        break;
	    case 0: // 只有portlet instance可选
	        if( !psType.equals(Structure.TYPE_PORTLET_INSTANCE) ) {
	            attributes.put(TreeNode.TREENODE_ATTR_CANSELECTED, "0"); 
	        }
	        break;
	    }
	    return attributes;
	}
}
