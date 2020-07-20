package com.amir.analyzer.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleOtsLogRecord {

    private final Severity severity;
    private final long timeStamp;
    private final String className;
    private final int lineNumber;
    private final String logMessage;
    
    private SingleOtsLogRecord(final Severity severity,
                            final long timeStamp,
                            final String className,
                            final int lineNumber,
                            final String logMessage) {
        this.severity = severity;
        this.timeStamp = timeStamp;
        this.className = className;
        this.lineNumber = lineNumber;
        this.logMessage = logMessage;
    }

    public Severity getSeverity() {
        return this.severity;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public String getClassName() {
        return this.className;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getLogMessage() {
        return this.logMessage;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    enum Severity {
        INFO,
        SEVERE,
        UNKNOWN
    }

    public static class Builder {

        private Severity severity;
        private long timeStamp;
        private String className;
        private int lineNumber;
        private String logMessage;

        public Builder setSeverity(final Severity severity) {
            this.severity = severity;
            return this;
        }

        public Builder setTimeStamp(final long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder setClassName(final String className) {
            this.className = className;
            return this;
        }

        public Builder setLineNumber(final int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder setLogMessage(final String logMessage) {
            this.logMessage = logMessage;
            return this;
        }

        public SingleOtsLogRecord build() {

            return new SingleOtsLogRecord(this.severity, this.timeStamp, this.className, this.lineNumber, this.logMessage);
        }

    }

    public static long parseTimeStamp(final String timeStampString) {
        final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy HH:mm:ss");

        long timeStamp = 0;
        try {
            final Date date = sdf.parse(timeStampString);
            timeStamp = date.getTime();
        }
        catch (final ParseException e) {
            e.printStackTrace();
        }

        return timeStamp;
    }

    public static Severity parseSeverity(final String severityString) {

        if(severityString.equals("INFO")) {
            return Severity.INFO;            
        }
        else if (severityString.equals("SEVERE")) {
            return Severity.SEVERE;
        }
        
        return Severity.UNKNOWN;
    
    }

    public static int parseLineNumber(final String lineNumberString) {

        return Integer.parseInt(lineNumberString);

    }

}
