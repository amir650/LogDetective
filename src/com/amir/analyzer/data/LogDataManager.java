package com.amir.analyzer.data;

import java.util.ArrayList;
import java.util.List;

public class LogDataManager {

    private static final LogDataManager INSTANCE = new LogDataManager();
    final List<LogData> allLogs;

    private LogDataManager() {
        this.allLogs = new ArrayList<>();
    }

    public static LogDataManager getInstance() {
        return INSTANCE;
    }

    public void addLog(final LogData log) {
        this.allLogs.add(log);
    }

}
