package com.haotian.analysis.ui.config;

public class ThreadItem {
    private final String threadEncId;
    private final String shortName;
    private final String threadName;
    private final String threadDetails;
    private final String threadState;
    private final String threadDecId;
    private final String threadHexId;

    private String num;

    public ThreadItem(String threadEncId, String name, String details, String threadState, String threadDecId, String threadHexId) {
        this.threadEncId = threadEncId;
        this.threadName = name;
        int beginIdx = name.indexOf("[") + 1;
        int endIdx = name.lastIndexOf("]");
        shortName = name.substring(beginIdx, endIdx);
        this.threadDetails = details;
        this.threadState = threadState;
        this.threadDecId = threadDecId;
        this.threadHexId = threadHexId;
    }

    public String getThreadState() {
        return threadState;
    }

    public String getThreadDecId() {
        return threadDecId;
    }

    public String getThreadHexId() {
        return threadHexId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getShortName() {
        return shortName;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getThreadDetails() {
        return threadDetails;
    }

    @Override
    public int hashCode() {
        return this.threadName.hashCode() + this.threadEncId.hashCode();
    }

    public String getThreadEncId() {
        return threadEncId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ThreadItem && this.threadName.equals(((ThreadItem) obj).threadName) && this.threadEncId.equals(((ThreadItem) obj).threadEncId);
    }

    @Override
    public String toString() {
        return this.threadName;
    }
}
