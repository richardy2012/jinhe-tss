package com.jinhe.tss.framework.test;

public interface IH2DBServer {

    void stopServer();
    
    boolean isPrepareed();
    
    void setPrepareed(boolean isPrepareed);
    
}
