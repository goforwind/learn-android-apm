package com.zxy.test.javaagent.hello;

import java.lang.instrument.Instrumentation;

/**
 * Created by zxy on 2017/3/28.
 */
public class TestAgent  {

    public static void agentmain(String agentArgs,Instrumentation instrumentation) {
        premain(agentArgs, instrumentation);
        System.out.println(" hello java agetnt! method agentmain method executed  ! ");
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println(" hello java agetnt! method premain method executed  ! ");

    }




}
