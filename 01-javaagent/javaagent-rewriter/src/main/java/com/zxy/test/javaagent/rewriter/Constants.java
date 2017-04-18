package com.zxy.test.javaagent.rewriter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by zxy on 2017/4/14.
 */
public class Constants {
    public static final  boolean DEBUG = true;
    public static final String DISABLE_INSTRUMENTATION_SYSTEM_PROPERTY = "zxy.instrumentation.disabled";
    public static final String INVOCATION_DISPATCHER_FIELD_NAME = "treeLock";
    public static Class INVOCATION_DISPATCHER_CLASS = Logger.class;

    public static final String SET_INSTRUMENTATION_DISABLED_FLAG = "SET_INSTRUMENTATION_DISABLED_FLAG";
    public static final String PRINT_TO_INFO_LOG = "PRINT_TO_INFO_LOG";
    public static final String DEXER_MAIN_CLASS_NAME = "com/android/dx/command/dexer/Main";
    public static final String ANT_DEX_EXEC_TASK = "com/android/ant/DexExecTask";
    public static final String ECLIPSE_BUILD_HELPER = "com/android/ide/eclipse/adt/internal/build/BuildHelper";
    public static final String MAVEN_DEX_MOJO = "com/jayway/maven/plugins/android/phase08preparepackage/DexMojo";
    public static final String PROCESS_BUILDER_CLASS_NAME = "java/lang/ProcessBuilder";
    public static final String PROCESS_CLASS_METHOD_NAME = "processClass";
    public static final String EXECUTE_DX_METHOD_NAME = "executeDx";
    public static final String PRE_DEX_LIBRARIES_METHOD_NAME = "preDexLibraries";
    public static final String START_METHOD_NAME = "start";

    public static  Set<String> DX_COMMAND_NAMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("dx", "dx.bat")));
    public static  Set<String>  JAVA_NAMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("java", "java.exe")));
    public static Set<String>  AGENT_JAR_NAMES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("zxy.android.jar", "zxy-android-agent.jar")));


}
