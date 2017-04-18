package com.zxy.test.javaagent.rewriter;


import com.zxy.test.javaagent.rewriter.log.Log;
import com.zxy.test.javaagent.rewriter.util.Util;


import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.net.URISyntaxException;

import java.util.*;

import static com.zxy.test.javaagent.rewriter.Constants.*;
import static com.zxy.test.javaagent.rewriter.util.Util.getProxyInvocationKey;

public class InvocationDispatcher implements InvocationHandler {

    private static final String DISABLE_INSTRUMENTATION_SYSTEM_PROPERTY = "zxy.instrumentation.disabled";
    protected final Log log;
    private final Map<String, InvocationHandler> invocationHandlers;
    private boolean writeDisabledMessage;
    private final String agentJarPath;
    private boolean disableInstrumentation;

    public InvocationDispatcher(final Log log) throws ClassNotFoundException, URISyntaxException {
        super();
        this.writeDisabledMessage = true;
        this.disableInstrumentation = false;
        this.log = log;
        this.agentJarPath = Util.getAgentJarPath();
        this.invocationHandlers = Collections.unmodifiableMap(new HashMap<String, InvocationHandler>() {
            {
                put(getProxyInvocationKey(Constants.PROCESS_BUILDER_CLASS_NAME, START_METHOD_NAME), new InvocationHandler() {
                    @Override
                    public Object invoke(final Object proxy, final java.lang.reflect.Method method, final Object[] args) throws Throwable {
                        final List<Object> list = (List<Object>) args[0];
                        final String command = (String) list.get(0);
                        final File commandFile = new File(command);
                        if (InvocationDispatcher.this.isInstrumentationDisabled()) {
                            log.info("Instrumentation disabled, no agent present.  Command: " + commandFile.getName());
                            log.debug("Execute: " + list.toString());
                            return null;
                        }
                        String javaagentString = null;
                        if (DX_COMMAND_NAMES.contains(commandFile.getName().toLowerCase())) {
                            javaagentString = "-Jjavaagent:" + InvocationDispatcher.this.agentJarPath;
                        } else if (JAVA_NAMES.contains(commandFile.getName().toLowerCase())) {
                            javaagentString = "-javaagent:" + InvocationDispatcher.this.agentJarPath;
                        }
                        if (javaagentString != null) {
                            if (MyAgent.agentArgs != null) {
                                javaagentString = javaagentString + "=" + MyAgent.agentArgs;
                            }
                            list.add(1, this.quoteProperty(javaagentString));
                        }
                        log.debug("Execute: " + list.toString());
                        return null;
                    }

                    private String quoteProperty(final String string) {
                        if (System.getProperty("os.name").toLowerCase().contains("win")) {
                            return "\"" + string + "\"";
                        }
                        return string;
                    }
                });
            }
        });
    }

    private boolean isInstrumentationDisabled() {
        return this.disableInstrumentation || System.getProperty(DISABLE_INSTRUMENTATION_SYSTEM_PROPERTY) != null;
    }


    @Override
    public Object invoke(final Object proxy, final java.lang.reflect.Method method, final Object[] args) throws Throwable {
        final InvocationHandler handler = this.invocationHandlers.get((String) proxy);
        if (handler == null) {
            this.log.error("Unknown invocation type: " + proxy + ".  Arguments: " + Arrays.asList(args));
            return null;
        }
        try {
            return handler.invoke(proxy, method, args);
        } catch (Throwable t) {
            this.log.error("Error:" + t.getMessage(), t);
            return null;
        }
    }
}