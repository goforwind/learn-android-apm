//package com.zxy.test.javaagent.rewriter;
//
//import com.zxy.test.javaagent.rewriter.log.Log;
//import org.objectweb.asm.ClassAdapter;
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.Type;
//
//import java.lang.instrument.IllegalClassFormatException;
//import java.net.URISyntaxException;
//import java.security.ProtectionDomain;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by zxy on 2017/4/14.
// */
//public class DexClassTransformer implements ZxyClassFileTransformer {
//
//    private Log log;
//    private final Map<String, ClassVisitorFactory> classVisitors;
//
//    public DexClassTransformer(final Log log) throws URISyntaxException {
//        super();
//        final String agentJarPath;
//        try {
//            agentJarPath = Util.getAgentJarPath();
//        } catch (URISyntaxException e) {
//            log.error("Unable to get the path to the BlueWare class rewriter jar", e);
//            throw e;
//        }
//        this.log = log;
//        //这里对各种打包方式的关键类的关键方法进行插码！
//        this.classVisitors = new HashMap<String, ClassVisitorFactory>() {
//            {
//
//            }
//        };
//    }
//
//    @Override
//    public byte[] transform(final ClassLoader classLoader, final String className, final Class<?> clazz, final ProtectionDomain protectionDomain, final byte[] bytes) throws IllegalClassFormatException {
//        final ClassVisitorFactory factory = this.classVisitors.get(className);
//        this.log.error(" transform class name =>  " + className);
//        if (factory != null) {
//            if (clazz != null && !factory.isRetransformOkay()) {
//                this.log.error("Cannot instrument " + className);
//                return null;
//            }
//            this.log.debug("Patching " + className);
//            try {
//                final ClassReader cr = new ClassReader(bytes);
//                final ClassWriter cw = new PatchedClassWriter(3, classLoader);
//                final ClassAdapter adapter = factory.create(cw);
//                cr.accept(adapter, ClassReader.SKIP_FRAMES);
//                final byte[] transformedBytes = cw.toByteArray();
//                return transformedBytes;
//            } catch (Exception ex2) {
//                this.log.debug("Error transforming class " + className);
//            }
//        } else {
//            this.log.error(" ClassVisitorFactory is null ");
//        }
//        return null;
//    }
//
//
//    @Override
//    public boolean modifies(Class<?> p0) {
//        return false;
//    }
//}
