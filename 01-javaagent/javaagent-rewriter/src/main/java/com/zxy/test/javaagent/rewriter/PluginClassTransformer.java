package com.zxy.test.javaagent.rewriter;


import com.zxy.test.javaagent.rewriter.log.Log;
import com.zxy.test.javaagent.rewriter.util.FileUtil;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import java.net.URISyntaxException;

import java.security.ProtectionDomain;
import java.util.Map;

/**
 * Created by zxy on 2017/4/14.
 */
public class PluginClassTransformer extends ZxyClassFileTransformer {
    private static final String START_METHOD_NAME = "start";
    private static final String PROCESS_BUILDER_CLASS_NAME = "java/lang/ProcessBuilder";

    public PluginClassTransformer(Log log) throws URISyntaxException {
       super(log);
    }

    @Override
    protected void addClassVisitorFactory(Map<String, ClassVisitorFactory> classVisitors) {
        //修改processBuilder#start的代码
        this.classVisitors.put(PROCESS_BUILDER_CLASS_NAME, new ClassVisitorFactory(true) {
            public ClassAdapter create(final ClassVisitor cv) {
                return createProcessBuilderClassAdapter(cv, log);
            }
        });
    }

    private static ClassAdapter createProcessBuilderClassAdapter(final ClassVisitor cw, final Log log) {
        return new ClassAdapter(cw) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if (START_METHOD_NAME.equals(name)) {
                    mv = new SkipInstrumentedMethodsMethodVisitor(new BaseMethodVisitor(mv, access, name, desc) {
                        @Override
                        protected void onMethodEnter() {
                            //调用InvocationDispatcher动态代理来调用InvocationDispatcher的实例
                            //loadArray是加载参数
                            this.builder.loadInvocationDispatcher().loadInvocationDispatcherKey(getProxyInvocationKey(PROCESS_BUILDER_CLASS_NAME, this.methodName)).loadArray(new Runnable() {
                                @Override
                                public void run() {
                                    loadThis();
                                    //传入的参数是调用command();的返回值
                                    invokeVirtual(Type.getObjectType(PROCESS_BUILDER_CLASS_NAME), new Method("command", "()Ljava/util/List;"));
                                }
                            }).invokeDispatcher();
                        }
                    });
                }
                return mv;
            }
        };
    }

    public static String getProxyInvocationKey(final String className, final String methodName) {
        return className + "." + methodName;
    }

    @Override
    public void transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] originBytes, byte[] transformedBytes) {
        if(PROCESS_BUILDER_CLASS_NAME.equals(className)) {
            //dump processBuilder class file .
            if(Constants.DEBUG && MyAgent.agentOptions.get("dumpclass")!=null) {
                FileUtil.dumpClassFile(transformedBytes,dumpClassDir(className),log);
            }
        }
    }
}
