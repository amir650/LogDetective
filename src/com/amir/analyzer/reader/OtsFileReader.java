package com.amir.analyzer.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.amir.analyzer.data.OtsLogData;
import com.amir.analyzer.data.SingleOtsLogRecord;


public class OtsFileReader
    extends LogFileReader {
    
    public OtsFileReader(final ZipFile zipFile, final ZipEntry zipEntry) {
        super(zipFile, zipEntry);
    }

    @Override
    public OtsLogData readLogFile() {
        InputStream inputStream = null;
        BufferedReader reader = null;
        final OtsLogData.Builder logDataBuilder = OtsLogData.newBuilder();
        try {                    
            final String fileName = this.zipEntry.getName();            
            if (fileName.equals("ots.log")) {
                inputStream = this.zipFile.getInputStream(this.zipEntry);
                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String currentLine = null;
                final List<SingleOtsLogRecord> logRecords = new ArrayList<SingleOtsLogRecord>();
                while ((currentLine = reader.readLine()) != null) {
                    final String pattern = "^(\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})"  //date
                            + ".*"
                            + "(SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST)" //severity
                            + "[ ]+([^:]+):(\\d+)" // location name, location line
                            + "[ ]+-?\\d*?[ ]+(.+)" // exception name, exception message
                            + "[ ]+ (Dump: (.+))?" // dump file
                            + ".*";
                    final Pattern regex = Pattern.compile(pattern);
                    final Matcher m = regex.matcher(currentLine);
                    if (m.matches()) {
                        final SingleOtsLogRecord.Builder singleLogRecordBuilder = SingleOtsLogRecord.newBuilder();
                        singleLogRecordBuilder.setTimeStamp(SingleOtsLogRecord.parseTimeStamp(m.group(1)));
                        singleLogRecordBuilder.setSeverity(SingleOtsLogRecord.parseSeverity(m.group(2)));
                        singleLogRecordBuilder.setClassName(m.group(3));
                        singleLogRecordBuilder.setLineNumber(SingleOtsLogRecord.parseLineNumber(m.group(4)));
                        logRecords.add(singleLogRecordBuilder.build());
                    }
                }
                logDataBuilder.setLogFileName(fileName);
                logDataBuilder.setLogRecords(logRecords);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return logDataBuilder.build();
    }
}
