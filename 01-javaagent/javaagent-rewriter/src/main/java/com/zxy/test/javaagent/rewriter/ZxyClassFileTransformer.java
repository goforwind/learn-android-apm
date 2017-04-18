package com.zxy.test.javaagent.rewriter;

import com.zxy.test.javaagent.rewriter.log.Log;
import com.zxy.test.javaagent.rewriter.util.Util;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;


import java.io.File;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URISyntaxException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxy on 2017/4/14.
 */
public abstract class ZxyClassFileTransformer  implements BlueWareClassTransformer  {

    protected Log log;
    protected final Map<String, ClassVisitorFactory> classVisitors;
    protected String agentJarPath;

    public ZxyClassFileTransformer(Log log) throws URISyntaxException {
        this.log = log;
        try {
            agentJarPath = Util.getAgentJarPath();
        } catch (URISyntaxException e) {
            log.error("Unable to get the path to the BlueWare class rewriter jar", e);
            throw e;
        }
        //这里对各种打包方式的关键类的关键方法进行插码！
        this.classVisitors = new HashMap<String, ClassVisitorFactory>();
        addClassVisitorFactory(classVisitors);
    }

    protected abstract void addClassVisitorFactory(Map<String, ClassVisitorFactory> classVisitors);


    @Override
    public boolean modifies(final Class<?> clazz) {
        final Type t = Type.getType(clazz);
        return this.classVisitors.containsKey(t.getInternalName());
    }


    @Override
    public byte[] transform( final ClassLoader classLoader, final String className, final Class<?> clazz, final ProtectionDomain protectionDomain, final byte[] bytes ) throws IllegalClassFormatException {
        final ClassVisitorFactory factory = this.classVisitors.get(className);
        if (factory != null) {
            if (clazz != null && !factory.isRetransformOkay()) {
                this.log.error("Cannot instrument " + className);
                return null;
            }
            this.log.debug("Patching " + className);
            try {
                final ClassReader cr = new ClassReader(bytes);
                final ClassWriter cw = new PatchedClassWriter(3, classLoader);
                final ClassAdapter adapter = factory.create(cw);
                cr.accept(adapter, ClassReader.SKIP_FRAMES);
                byte [] b = cw.toByteArray();
                transform(classLoader,className,clazz,protectionDomain,bytes,b);
            } catch (SkipException ex2) {
                this.log.debug("Error transforming class " + className);
            } catch (Exception ex) {
                this.log.error("Error transforming class " + className, ex);
            }
        }
        return null;
    }

    /**
     * @param classLoader
     * @param className
     * @param clazz
     * @param protectionDomain
     * @param originBytes
     * @param transformedBytes
     */
    public  void transform(final ClassLoader classLoader, final String className,
                                   final Class<?> clazz, final ProtectionDomain protectionDomain,
                                   final byte[] originBytes,
                                   final byte[] transformedBytes ){
        //do nothing !
    }


    protected String dumpClassDir(String className) {
        return this.agentJarPath + File.separator + className.replace('/', '.').replace('\\', '.') +".class";
    }
}
