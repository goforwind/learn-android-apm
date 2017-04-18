package com.zxy.test.javaagent.rewriter.log;

public interface Log
{
    void info(String p0);
    
    void debug(String p0);
    
    void warning(String p0);
    
    void warning(String p0, Throwable p1);
    
    void error(String p0);
    
    void error(String p0, Throwable p1);
}