package com.zxy.agent.compile;

import com.zxy.test.javaagent.rewriter.MyAgent;
import com.zxy.test.javaagent.rewriter.log.FileLogImpl;
import com.zxy.test.javaagent.rewriter.log.Log;

import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;

/**
 * Created by zxy on 2017/4/12.
 */
public class RewriterAgent {

    public static void agentmain(String agentArgs,Instrumentation instrumentation) {
        premain(agentArgs, instrumentation);
        System.out.println(" rewriter java agetnt! method agentmain method executed  ! ");
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        MyAgent.premain(agentArgs,instrumentation);
    }



}
