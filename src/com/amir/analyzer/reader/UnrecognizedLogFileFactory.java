package com.amir.analyzer.reader;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnrecognizedLogFileFactory
    implements LogFileReaderFactory {

    public UnrecognizedFileReader createFileReader(final ZipFile zipFile, final ZipEntry zipEntry) {
        return new UnrecognizedFileReader(zipFile, zipEntry);
    }

}
