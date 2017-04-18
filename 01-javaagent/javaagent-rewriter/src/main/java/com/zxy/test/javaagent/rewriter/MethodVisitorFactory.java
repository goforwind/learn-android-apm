package com.zxy.test.javaagent.rewriter;

import org.objectweb.asm.MethodVisitor;

interface MethodVisitorFactory
{
    MethodVisitor create(MethodVisitor p0, int p1, String p2, String p3);
}