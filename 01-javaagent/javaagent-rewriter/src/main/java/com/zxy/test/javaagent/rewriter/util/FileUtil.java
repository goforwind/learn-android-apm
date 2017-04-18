package com.zxy.test.javaagent.rewriter.util;

import com.zxy.test.javaagent.rewriter.log.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarFile;

/**
 * Created by zxy on 2017/4/17.
 */
public class FileUtil {


    public static void dumpClassFile(byte[] bytes, String fileName, Log log) {
        FileOutputStream fileWriter = null;
        try {
            fileWriter = new FileOutputStream(new File(fileName));
            fileWriter.write(bytes);
            fileWriter.flush();
        } catch (IOException e) {
            log.error("failed  dump class  "+ fileName, e);
        } finally {
            close(fileWriter);
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
