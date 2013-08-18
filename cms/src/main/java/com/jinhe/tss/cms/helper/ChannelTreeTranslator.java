package com.jinhe.tss.cms.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.util.EasyUtils;

/** 
 * 树栏目是否可选 转换器
 */
public class ChannelTreeTranslator implements ITreeTranslator {
    
    private List<String> canAddChannelIds = new ArrayList<String>();
    private Long channelId;

    public ChannelTreeTranslator(String canAddChannels, Long channelId) {
        if ( !EasyUtils.isNullOrEmpty(canAddChannels) ) {
			canAddChannelIds = Arrays.asList(canAddChannels.split(","));
		}
        this.channelId = channelId;
    }
    
    public Map<String, Object> translate(Map<String, Object> attributes) {
        if( !canAddChannelIds.contains(attributes.get("id").toString()) )
        	attributes.put("canselected", "0");
        
        if( CMSConstants.TRUE.equals(attributes.get("isSite")) )
            attributes.put("canselected", "0");
        
        if( channelId != null && channelId.equals(attributes.get("id")) )
            attributes.put("canselected", "0");
        
        return attributes;
    }
}

