package com.amir.analyzer.reader;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class OtsLogFileFactory
    implements LogFileReaderFactory {

    public OtsFileReader createFileReader(final ZipFile zipFile, final ZipEntry zipEntry) {
        return new OtsFileReader(zipFile, zipEntry);
    }

}
