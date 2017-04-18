package com.zxy.test.javaagent.rewriter.util;

import com.zxy.test.javaagent.rewriter.MyAgent;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxy on 2017/4/12.
 */
public class Util {

    public static Map<String, String> parseAgentArgs(final String agentArgs) {
        if (agentArgs == null || "".equals(agentArgs.trim())) {
            return Collections.emptyMap();
        }
        final Map<String, String> options = new HashMap<String, String>();
        for (final String arg : agentArgs.split(";")) {
            final String[] keyValue = arg.split("=");
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
            options.put(keyValue[0], keyValue[1]);
        }
        return options;
    }


    public static String getAgentJarPath() throws URISyntaxException {
        return new File(MyAgent.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
    }

    public static String getProxyInvocationKey(final String className, final String methodName) {
        return className + "." + methodName;
    }




}
