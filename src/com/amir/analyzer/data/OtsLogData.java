package com.amir.analyzer.data;

import java.util.List;

import com.amir.analyzer.visualizer.OtsLogVisualizer;
import com.amir.analyzer.visualizer.Visualizer;

public class OtsLogData
    implements LogData {

    private final String logFileName;
    private final List<SingleOtsLogRecord> logRecords;
    private final Visualizer visualizer;

    private OtsLogData(final String logFileName, final List<SingleOtsLogRecord> logRecords) {
        this.logFileName = logFileName;
        this.logRecords = logRecords;
        this.visualizer = new OtsLogVisualizer();
    }

    @Override
    public Visualizer getVisualizer() {
        return this.visualizer;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        String logFileName;
        List<SingleOtsLogRecord> logRecords;

        private Builder() {
        }

        public Builder setLogFileName(final String logFileName) {
            this.logFileName = logFileName;
            return this;
        }

        public Builder setLogRecords(final List<SingleOtsLogRecord> logRecords) {
            this.logRecords = logRecords;
            return this;
        }

        public OtsLogData build() {
            return new OtsLogData(this.logFileName, this.logRecords);
        }

    }

}
