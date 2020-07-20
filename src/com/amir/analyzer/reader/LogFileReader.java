package com.amir.analyzer.reader;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.amir.analyzer.data.LogData;

public abstract class LogFileReader {

    final ZipFile zipFile;
    final ZipEntry zipEntry;

    LogFileReader(final ZipFile zipFile, final ZipEntry zipEntry) {
        this.zipFile = zipFile;
        this.zipEntry = zipEntry;
    }

    public abstract LogData readLogFile();

}
