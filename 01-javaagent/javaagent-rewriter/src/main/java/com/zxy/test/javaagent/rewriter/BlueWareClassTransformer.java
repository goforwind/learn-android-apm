package com.zxy.test.javaagent.rewriter;

import java.lang.instrument.ClassFileTransformer;
/**
 * Created by zxy on 2017/4/14.
 */
public interface BlueWareClassTransformer extends ClassFileTransformer {

    boolean modifies(Class<?> p0);

}
