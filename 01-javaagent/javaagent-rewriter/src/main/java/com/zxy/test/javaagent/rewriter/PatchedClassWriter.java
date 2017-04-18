package com.zxy.test.javaagent.rewriter;

import org.objectweb.asm.ClassWriter;

/**
 * 比ClassWriter多了主要增加了判断类关系的逻辑
 */
class PatchedClassWriter extends ClassWriter
{
    private final ClassLoader classLoader;
    
    public PatchedClassWriter(final int flags, final ClassLoader classLoader) {
        super(flags);
        this.classLoader = classLoader;
    }
    
    @Override
    protected String getCommonSuperClass(final String type1, final String type2) {//判断继承关系
        Class c;
        Class d;
        try {
            c = Class.forName(type1.replace('/', '.'), true, this.classLoader);
            d = Class.forName(type2.replace('/', '.'), true, this.classLoader);
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        if (c.isAssignableFrom(d)) {//判断c类是否和D类相同或者d是C的父类或者c实现了d接口
            return type1;
        }
        if (d.isAssignableFrom(c)) {//判断d类是否和c类相同或者c是d的父类或者d实现了c接口
            return type2;
        }
        if (c.isInterface() || d.isInterface()) {
            return "java/lang/Object";
        }
        do {
            c = c.getSuperclass();
        } while (!c.isAssignableFrom(d));
        return c.getName().replace('.', '/');
    }
}