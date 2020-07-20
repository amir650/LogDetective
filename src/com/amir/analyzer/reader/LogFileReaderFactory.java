package com.amir.analyzer.reader;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public interface LogFileReaderFactory {

    public LogFileReader createFileReader(ZipFile zipFile, ZipEntry zipEntry);

}
