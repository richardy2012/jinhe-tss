package com.jinhe.tss.cms.helper.translator;

import java.util.Map;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;

/**
 * 将站点节点设置为不可选
 */
public class ExcludeSiteNodeTranslator implements ITreeTranslator {
	
	public Map<String, Object> translate(Map<String, Object> attributes) {
		if (attributes.get("isSite").equals(CMSConstants.TRUE)) {
			attributes.put("canselected", "0");
		}
		
		return attributes;
	}
	
}