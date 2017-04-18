package com.zxy.test.javaagent.rewriter;

import org.objectweb.asm.MethodVisitor;

import org.objectweb.asm.commons.AdviceAdapter;

public abstract  class BaseMethodVisitor extends AdviceAdapter {
        protected final String methodName;
        protected final BytecodeBuilder builder;

        protected BaseMethodVisitor(final MethodVisitor mv, final int access, final String methodName, final String desc) {
            super(mv, access, methodName, desc);
            this.builder = new BytecodeBuilder(this);
            this.methodName = methodName;
        }

        @Override
        public void visitEnd() {
//            super.visitAnnotation(Type.getDescriptor(InstrumentedMethod.class), false);
            super.visitEnd();
        }
    }