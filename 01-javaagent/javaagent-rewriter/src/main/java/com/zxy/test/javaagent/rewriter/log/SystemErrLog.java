package com.zxy.test.javaagent.rewriter.log;

import java.util.Map;

final class SystemErrLog implements Log
{
    private final Map<String, String> agentOptions;
    
    public SystemErrLog(final Map<String, String> agentOptions) {
        super();
        this.agentOptions = agentOptions;
    }
    
    @Override
    public void info(final String message) {
        System.out.println("[zxy.info] " + message);
    }
    
    @Override
    public void debug(final String message) {
        if (this.agentOptions.get("debug") != null) {
            System.out.println("[zxy.debug] " + message);
        }
    }
    
    @Override
    public void warning(final String message) {
        System.err.println("[zxy.warn] " + message);
    }
    
    @Override
    public void warning(final String message, final Throwable cause) {
        System.err.println("[zxy.warn] " + message);
        cause.printStackTrace(System.err);
    }
    
    @Override
    public void error(final String message) {
        System.err.println("[zxy.error] " + message);
    }
    
    @Override
    public void error(final String message, final Throwable cause) {
        System.err.println("[zxy.error] " + message);
        cause.printStackTrace(System.err);
    }
}
