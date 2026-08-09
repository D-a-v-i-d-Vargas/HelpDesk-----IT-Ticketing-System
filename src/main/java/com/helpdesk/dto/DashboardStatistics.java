package com.helpdesk.dto;

public class DashboardStatistics {
    private long total;
    private long open;
    private long inProgress;
    private long resolved;

    public DashboardStatistics(long total, long open, long inProgress, long resolved) {
        this.total = total;
        this.open = open;
        this.inProgress = inProgress;
        this.resolved = resolved;
    }

    public long getTotal() {
        return total;
    }

    public long getOpen() {
        return open;
    }

    public long getInProgress() {
        return inProgress;
    }

    public long getResolved() {
        return resolved;
    }
}
