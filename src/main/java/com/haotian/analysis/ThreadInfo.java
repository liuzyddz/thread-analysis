package com.haotian.analysis;

import java.util.ArrayList;
import java.util.List;

public class ThreadInfo {
    private String threadName;
    private List<String> stack = new ArrayList<>();
    private boolean matched = false;

    private String threadDecId;
    private String threadHexId;
    private String threadState;

    public void initDefault() {
        if (this.threadDecId == null) {
            this.threadDecId = "";
        }
        if (this.threadHexId == null) {
            this.threadHexId = "";
        }
        if (this.threadState == null) {
            this.threadState = "";
        }
    }

    public String getStackStrace() {
        StringBuilder stackTrace = new StringBuilder();
        for (String line : stack) {
            stackTrace.append(line).append("\n");
        }
        return stackTrace.toString();
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void addStackTrace(String line) {
        this.stack.add(line);
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public String getThreadDecId() {
        return threadDecId;
    }

    public void setThreadDecId(String threadDecId) {
        this.threadDecId = threadDecId;
    }

    public String getThreadHexId() {
        return threadHexId;
    }

    public void setThreadHexId(String threadHexId) {
        this.threadHexId = threadHexId;
    }

    public String getThreadState() {
        return threadState;
    }

    public void setThreadState(String threadState) {
        this.threadState = threadState;
    }
}
