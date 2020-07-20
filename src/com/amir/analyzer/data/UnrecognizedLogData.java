package com.amir.analyzer.data;

import com.amir.analyzer.visualizer.UnrecognizedLogVisualizer;
import com.amir.analyzer.visualizer.Visualizer;

public class UnrecognizedLogData
    implements LogData {

    final String logFileName;
    final Visualizer visualizer;

    private UnrecognizedLogData(final String logFileName) {
        this.logFileName = logFileName;
        this.visualizer = new UnrecognizedLogVisualizer();
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

        private Builder() {
        }

        public Builder setLogFileName(final String logFileName) {
            this.logFileName = logFileName;
            return this;
        }

        public UnrecognizedLogData build() {
            return new UnrecognizedLogData(this.logFileName);
        }

    }
}
