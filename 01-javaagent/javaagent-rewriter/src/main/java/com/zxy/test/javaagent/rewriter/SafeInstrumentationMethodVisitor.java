package com.zxy.test.javaagent.rewriter;

import org.objectweb.asm.MethodVisitor;

public abstract  class SafeInstrumentationMethodVisitor extends BaseMethodVisitor {
    private static final String SET_INSTRUMENTATION_DISABLED_FLAG = "SET_INSTRUMENTATION_DISABLED_FLAG";
        protected SafeInstrumentationMethodVisitor(final MethodVisitor mv, final int access, final String methodName, final String desc) {
            super(mv, access, methodName, desc);
        }

        @Override
        protected final void onMethodExit(final int opcode) {
            this.builder.loadInvocationDispatcher().loadInvocationDispatcherKey(SET_INSTRUMENTATION_DISABLED_FLAG).loadNull().invokeDispatcher();
            super.onMethodExit(opcode);
        }
    }