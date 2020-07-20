package com.amir.analyzer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.amir.analyzer.data.LogData;
import com.amir.analyzer.data.LogDataManager;
import com.amir.analyzer.reader.LogFileReader;
import com.amir.analyzer.reader.LogFileReaderFactory;
import com.amir.analyzer.reader.OtsLogFileFactory;
import com.amir.analyzer.reader.UnrecognizedLogFileFactory;

public class MainWindow
    extends JFrame {

    private JList list;
    private final DefaultListModel listModel;
    private static JMenuBar menuBar;
    private final ProgressMonitor readZipProgressMonitor;
    private final ProgressMonitor downloadProgressMonitor;
    private ZipFileReaderTask zipFileReaderTask;
    private ZipFileDownloaderTask zipFileDownloaderTask;
    private static final String TITLE = "Workday Log Analyzer";
    private static final Dimension FRAME_DIMENSION = new Dimension(1000, 500);

    public MainWindow() {
        super(TITLE);
        this.readZipProgressMonitor = new ProgressMonitor(this, "Reading log", "", 0, 100);
        this.downloadProgressMonitor = new ProgressMonitor(this, "Downloading log", "", 0, 100);
        menuBar = new JMenuBar();
        menuBar.add(fileMenu());
        ///
        this.listModel = new DefaultListModel();
        this.listModel.addElement("Jane Doe");
        this.listModel.addElement("John Smith");
        this.listModel.addElement("Kathy Green");
        //
        setJMenuBar(menuBar);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_DIMENSION);
        //center the frame
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        validate();
    }

    private JMenu fileMenu() {

        final JMenu file_menu = new JMenu("File");
        final JMenuItem openMenuItem = new JMenuItem("Open Log");

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser chooser = new JFileChooser(".");
                // select only zip files and add "Zip Files" option             
                chooser.setFileFilter(new FileNameExtensionFilter("Zip Files", "zip", "gz"));
                chooser.setDialogTitle("Select Zip File");
                if (chooser.showOpenDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION) {
                    MainWindow.this.readZipProgressMonitor.setProgress(0);
                    MainWindow.this.zipFileReaderTask = new ZipFileReaderTask(chooser.getSelectedFile());
                    MainWindow.this.zipFileReaderTask.addPropertyChangeListener(new ZipFileReaderProgressListener());
                    MainWindow.this.zipFileReaderTask.execute();
                }
            }
        });

        final JMenuItem connectMenuItem = new JMenuItem("Connect to Dev");

        connectMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                MainWindow.this.downloadProgressMonitor.setProgress(0);
                MainWindow.this.zipFileDownloaderTask = new ZipFileDownloaderTask();
                MainWindow.this.zipFileDownloaderTask.addPropertyChangeListener(new ZipFileDownloadProgressListener());
                MainWindow.this.zipFileDownloaderTask.execute();
            }
        });

        final JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);

        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
                dispose();
            }
        });

        file_menu.add(openMenuItem);
        file_menu.add(connectMenuItem);
        file_menu.add(exitMenuItem);

        return file_menu;

    }

    class ZipFileReaderProgressListener
        implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {

            if (MainWindow.this.readZipProgressMonitor.isCanceled()) {
                System.out.println("cancelled = " + MainWindow.this.zipFileReaderTask.cancel(true));
            }
            else if (evt.getPropertyName().equals("progress")) {
                final int progress = (Integer) evt.getNewValue();
                MainWindow.this.readZipProgressMonitor.setProgress(progress);
                final String message = String.format("Completed %d%%.\n", progress);
                MainWindow.this.readZipProgressMonitor.setNote(message);
            }
        }

    }

    class ZipFileReaderTask
        extends SwingWorker<Void, Void> {

        ZipFile zipFile;

        public ZipFileReaderTask(final File reportFile) {

            try {
                this.zipFile = new ZipFile(reportFile);
            }
            catch (final ZipException e) {
                e.printStackTrace();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public Void doInBackground() {

            final Enumeration<? extends ZipEntry> entries = this.zipFile.entries();
            final int numFiles = this.zipFile.size();
            int currentFileNum = 0;
            int progress = 0;
            setProgress(progress);

            while (entries.hasMoreElements() && !isCancelled()) {
                final ZipEntry zipEntry = entries.nextElement();
                if (!zipEntry.isDirectory()) {
                    final LogFileReader reader = createLogReader(this.zipFile, zipEntry);
                    final LogData log = reader.readLogFile();
                    LogDataManager.getInstance().addLog(log);
                }
                currentFileNum++;
                progress = (100 * currentFileNum / numFiles);
                setProgress(progress);
                try {
                    Thread.sleep(5);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                this.zipFile.close();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void done() {
            MainWindow.this.readZipProgressMonitor.setNote("");
        }
    }

    class ZipFileDownloadProgressListener
        implements PropertyChangeListener {

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (MainWindow.this.downloadProgressMonitor.isCanceled()) {
                System.out.println("cancelled = " + MainWindow.this.zipFileDownloaderTask.cancel(true));
            }
            else if (evt.getPropertyName().equals("progress")) {
                final int progress = (Integer) evt.getNewValue();
                MainWindow.this.downloadProgressMonitor.setProgress(progress);
                final String message = String.format("Completed %d%%.\n", progress);
                MainWindow.this.downloadProgressMonitor.setNote(message);
            }
        }

    }

    class ZipFileDownloaderTask
        extends SwingWorker<File, Void> {

        public ZipFileDownloaderTask() {
        }

        @Override
        public File doInBackground() {

            File logFile = null;
            FileOutputStream out = null;

            try {
                final LoginDialog logIn = new LoginDialog(MainWindow.this);
                logIn.setVisible(true);
                final BasicAuthenticator authenticator = new BasicAuthenticator(new URL(
                        "https://dev-jmx.megaleo.com/ots/oms/log"), logIn.getUsername(), logIn.getPassword());
                final URLConnection uc = authenticator.establishConnection();
                final String contentType = uc.getContentType();
                final int contentLength = uc.getContentLength();
                if (contentType.startsWith("text/") || contentLength == -1) {
                    throw new IOException("This is not a binary file.");
                }
                final InputStream raw = uc.getInputStream();
                final InputStream in = new BufferedInputStream(raw);
                final byte[] data = new byte[contentLength];
                int bytesRead = 0;
                int offset = 0;
                int progress = 0;
                setProgress(progress);
                while (offset < contentLength) {
                    bytesRead = in.read(data, offset, data.length - offset);
                    if (bytesRead == -1) {
                        break;
                    }
                    offset += bytesRead;
                    progress = (int) ((100L * offset) / contentLength);
                    setProgress(progress);
                }
                in.close();
                if (offset != contentLength) {
                    throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
                }
                final String fileName = "log.zip";
                logFile = new File(fileName);
                out = new FileOutputStream(logFile);
                out.write(data);
                out.flush();
            }
            catch (final MalformedURLException e1) {
                e1.printStackTrace();
            }
            catch (final IOException e2) {
                e2.printStackTrace();
            }
            finally {
                try {
                    out.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }

            return logFile;
        }

        @Override
        public void done() {
            MainWindow.this.downloadProgressMonitor.setNote("");
            try {
                final File logFile = MainWindow.this.zipFileDownloaderTask.get();
                MainWindow.this.zipFileReaderTask = new ZipFileReaderTask(logFile);
                MainWindow.this.zipFileReaderTask.addPropertyChangeListener(new ZipFileReaderProgressListener());
                MainWindow.this.zipFileReaderTask.execute();
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
            catch (final ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    

    private LogFileReader createLogReader(final ZipFile zipFile, final ZipEntry zipEntry) {
        
        LogFileReaderFactory logFileReaderFactory = null;
        final String fileName = zipEntry.getName();
        
        if (fileName.equals("ots.log")) {
            logFileReaderFactory = new OtsLogFileFactory();
        }
        else {
            logFileReaderFactory = new UnrecognizedLogFileFactory();
        }

        return logFileReaderFactory.createFileReader(zipFile, zipEntry);
    }
    

}
