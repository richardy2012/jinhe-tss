package com.jinhe.dm.report;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.util.BeanUtil;
 
public class ScriptParserFactory {
    
    protected static ScriptParser instance;
 
    public static ScriptParser getParser() {
        if (instance == null) {
            String configValue = Config.getAttribute("script_precheator");
            if (configValue != null) {
                instance = (ScriptParser) BeanUtil.newInstanceByName(configValue);
            } 
        }
        return instance;
    }
}
