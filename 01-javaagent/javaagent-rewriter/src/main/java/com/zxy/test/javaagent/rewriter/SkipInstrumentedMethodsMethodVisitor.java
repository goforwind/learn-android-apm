package com.zxy.test.javaagent.rewriter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class SkipInstrumentedMethodsMethodVisitor extends MethodAdapter
{
    public SkipInstrumentedMethodsMethodVisitor(final MethodVisitor mv) {
        super(mv);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        if (Type.getDescriptor(InstrumentedMethod.class).equals(desc)) {
            throw new SkipException();
        }
        return super.visitAnnotation(desc, visible);
    }
}