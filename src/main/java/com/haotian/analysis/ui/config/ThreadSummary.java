package com.haotian.analysis.ui.config;

public class ThreadSummary {
    private final String threadEncId;
    private final String threadFile;

    public ThreadSummary(String threadEncId, String threadFile) {
        this.threadEncId = threadEncId;
        this.threadFile = threadFile;
    }

    public String getThreadEncId() {
        return threadEncId;
    }

    public String getThreadFile() {
        return threadFile;
    }

    @Override
    public int hashCode() {
        return this.threadEncId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ThreadSummary && this.threadEncId.equals(((ThreadSummary) obj).threadEncId);
    }

    @Override
    public String toString() {
        return this.threadFile;
    }
}
