package com.zxy.test.javaagent.rewriter;


import com.zxy.test.javaagent.rewriter.log.FileLogImpl;
import com.zxy.test.javaagent.rewriter.log.Log;
import com.zxy.test.javaagent.rewriter.util.Util;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.*;

import static com.zxy.test.javaagent.rewriter.Constants.INVOCATION_DISPATCHER_CLASS;
import static com.zxy.test.javaagent.rewriter.Constants.INVOCATION_DISPATCHER_FIELD_NAME;

/**
 * Created by zxy on 2017/3/28.
 */
public class MyAgent {

    public static String agentArgs;
    public static Map<String, String> agentOptions;

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        premain(agentArgs, instrumentation);
        System.out.println(" rewriter java agetnt! method agentmain method executed  ! ");
    }

    //-javaagent:myagent.jar=logfile=e:\log.txt;instrument=true;dumpclass=true
    //
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        MyAgent.agentOptions = Util.parseAgentArgs(agentArgs);
        MyAgent.agentArgs = agentArgs;
        Log log = new FileLogImpl(agentArgs, MyAgent.agentOptions.get("logfile"));
        log.info(" rewriter java agetnt! method premain method executed  ! ");
        try {
            createInvocationDispatcher(log);
            instrumentation.addTransformer(new PluginClassTransformer(log),true);
            //instrumentation.addTransformer(new DexClassTransformer(log), true);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //把InvocationDispatcher放在 Logger中的原因是以后这里的InvocationDispatcher实例要在processBuilder的start方法中调用
    //ProcessBuilder是通过boot classloader加载的，InvocationDispatcher是通过app classloader加载的
    //所以，为了让通过boot classloader加载的ProcessBuilder是通过boot调用通过app classloader加载的InvocationDispatcher，把实例放入logger中
    private static void createInvocationDispatcher(final Log log) throws Exception {
        final Field field = INVOCATION_DISPATCHER_CLASS.getDeclaredField(INVOCATION_DISPATCHER_FIELD_NAME);
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
        if (field.get(null) instanceof InvocationDispatcher) {
            log.info("Detected cached instrumentation.");
        } else {
            field.set(null, new InvocationDispatcher(log));
        }
    }





}
