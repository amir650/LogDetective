package com.amir.analyzer.reader;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.amir.analyzer.data.UnrecognizedLogData;

public class UnrecognizedFileReader
    extends LogFileReader {

    public UnrecognizedFileReader(final ZipFile zipFile, final ZipEntry zipEntry) {
        super(zipFile, zipEntry);
    }

    @Override
    public UnrecognizedLogData readLogFile() {

        final String fileName = this.zipEntry.getName();
        final UnrecognizedLogData.Builder logDataBuilder = UnrecognizedLogData.newBuilder();
        logDataBuilder.setLogFileName(fileName);

        return logDataBuilder.build();

    }

}
