package com.zxy.test.javaagent.rewriter;

import com.zxy.test.javaagent.rewriter.log.Log;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Method;

import java.util.Map;

class ClassAdapterBase extends ClassAdapter
{
    final Map<Method, MethodVisitorFactory> methodVisitors;
    private final Log log;
    
    public ClassAdapterBase(final Log log, final ClassVisitor cv, final Map<Method, MethodVisitorFactory> methodVisitors) {
        super(cv);
        this.methodVisitors = methodVisitors;
        this.log = log;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        final MethodVisitorFactory factory = this.methodVisitors.get(new Method(name, desc));
//        if (factory != null) {
//            return new SkipInstrumentedMethodsMethodVisitor(factory.create(mv, access, name, desc));
//        }
        return mv;
    }
}