package com.zxy.test.javaagent.rewriter;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import static com.zxy.test.javaagent.rewriter.Constants.INVOCATION_DISPATCHER_CLASS;
import static com.zxy.test.javaagent.rewriter.Constants.INVOCATION_DISPATCHER_FIELD_NAME;
import static com.zxy.test.javaagent.rewriter.Constants.PRINT_TO_INFO_LOG;


public  class BytecodeBuilder implements Opcodes {
        private final GeneratorAdapter mv;

        public BytecodeBuilder(final GeneratorAdapter adapter) {
            super();
            this.mv = adapter;
        }

        public BytecodeBuilder loadNull() {
            this.mv.visitInsn(1);
            return this;
        }

        public BytecodeBuilder loadInvocationDispatcher() {
            this.mv.visitLdcInsn(Type.getType(INVOCATION_DISPATCHER_CLASS));
            this.mv.visitLdcInsn(INVOCATION_DISPATCHER_FIELD_NAME);
            this.mv.invokeVirtual(Type.getType(Class.class), new Method("getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;"));
            this.mv.dup();
            this.mv.visitInsn(4);
            this.mv.invokeVirtual(Type.getType(Field.class), new Method("setAccessible", "(Z)V"));
            this.mv.visitInsn(1);
            this.mv.invokeVirtual(Type.getType(Field.class), new Method("get", "(Ljava/lang/Object;)Ljava/lang/Object;"));
            return this;
        }

        public BytecodeBuilder loadArgumentsArray(final String methodDesc) {
            final Method method = new Method("dummy", methodDesc);
            this.mv.push(method.getArgumentTypes().length);
            final Type objectType = Type.getType(Object.class);
            this.mv.newArray(objectType);
            for (int i = 0; i < method.getArgumentTypes().length; ++i) {
                this.mv.dup();
                this.mv.push(i);
                this.mv.loadArg(i);
                this.mv.arrayStore(objectType);
            }
            return this;
        }

        /**
         * 构造对象数组, new Object[2]{className, localVar5}
         */
        public BytecodeBuilder loadArgumentsArray4Robolectric() {
            this.mv.push(2);
            final Type objectType = Type.getType(Object.class);
            this.mv.newArray(objectType);
            this.mv.dup();
            this.mv.push(0);
            this.mv.loadArg(0);
            this.mv.arrayStore(objectType);

            this.mv.dup();
            this.mv.push(1);
            this.mv.loadLocal(5);
            this.mv.arrayStore(objectType);

            return this;
        }


        public BytecodeBuilder loadArray(final Runnable... r) {
            this.mv.push(r.length);
            final Type objectType = Type.getObjectType("java/lang/Object");
            this.mv.newArray(objectType);
            for (int i = 0; i < r.length; ++i) {
                this.mv.dup();
                this.mv.push(i);
                r[i].run();
                this.mv.arrayStore(objectType);
            }
            return this;
        }

        public BytecodeBuilder printToInfoLogFromBytecode(final String message) {
            this.loadInvocationDispatcher();
            this.mv.visitLdcInsn(PRINT_TO_INFO_LOG);
            this.mv.visitInsn(1);
            this.loadArray(new Runnable() {
                @Override
                public void run() {
                    BytecodeBuilder.this.mv.visitLdcInsn(message);
                }
            });
            this.invokeDispatcher();
            return this;
        }

        public BytecodeBuilder invokeDispatcher() {
            return this.invokeDispatcher(true);
        }

        public BytecodeBuilder invokeDispatcher(final boolean popReturnOffStack) {
            this.mv.invokeInterface(Type.getType(InvocationHandler.class),
                    new Method("invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;"));
            if (popReturnOffStack) {
                this.mv.pop();
            }
            return this;
        }

        public BytecodeBuilder loadInvocationDispatcherKey(final String key) {
            this.mv.visitLdcInsn(key);
            this.mv.visitInsn(1);
            return this;
        }
    }