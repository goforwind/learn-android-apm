package com.zxy.test.javaagent.rewriter;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

abstract class ClassVisitorFactory
{
    private final boolean retransformOkay;
    
    public ClassVisitorFactory(final boolean retransformOkay) {
        super();
        this.retransformOkay = retransformOkay;
    }
    
    public boolean isRetransformOkay() {
        return this.retransformOkay;
    }
    
    abstract ClassAdapter create(final ClassVisitor p0);
}