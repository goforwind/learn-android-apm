package com.zxy.test.javaagent.rewriter.log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class FileLogImpl implements Log
{
    private final PrintWriter writer;
    
    public FileLogImpl(String agentArgs, final String logFileName) {
        super();
        try {
            this.writer = new PrintWriter(new FileOutputStream(logFileName));
            this.writer.write("[ agent args => ] " + agentArgs + "\n");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void writeln(final String level, final String message) {
        this.writer.write("[" + level + "] " + message + "\n");
        this.writer.flush();
    }
    
    @Override
    public void info(final String message) {
        this.writeln("info", message);
    }
    
    @Override
    public void debug(final String message) {
        this.writeln("debug", message);
    }
    
    @Override
    public void warning(final String message) {
        this.writeln("warn", message);
    }
    
    @Override
    public void warning(final String message, final Throwable cause) {
        this.writeln("warn", message);
        cause.printStackTrace(this.writer);
        this.writer.flush();
    }
    
    @Override
    public void error(final String message) {
        this.writeln("error", message);
    }
    
    @Override
    public void error(final String message, final Throwable cause) {
        this.writeln("error", message);
        cause.printStackTrace(this.writer);
        this.writer.flush();
    }
}
