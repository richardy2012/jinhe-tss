package com.jinhe.tss.framework.sso.identity;

import com.jinhe.tss.framework.sso.DoNothingLoginCustomizer;
import com.jinhe.tss.framework.sso.LoginCustomizerFactory;
 

public class MockLoginCustomizerFactory extends LoginCustomizerFactory {

    public static void init() {
        customizer = new DoNothingLoginCustomizer();
    }
}
