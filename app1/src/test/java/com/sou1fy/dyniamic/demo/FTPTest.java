package com.sou1fy.dyniamic.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

@Slf4j
public class FTPTest {
    public static void main(String[] args) {
        FTPClient ftp = new FTPClient();
        ftp.setConnectTimeout(30 * 1000);
        ftp.setDataTimeout(60 * 1000);
        try {
            ftp.connect("192.8.8.102", 9999);
            ftp.login("usr01", "abc123");
            int reply;
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            ftp.setBufferSize(1024);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            boolean workingDirectory = ftp.changeWorkingDirectory("/");
            if (!workingDirectory) {
                log.error("\\ 目录不存在");
                System.exit(1);
            }
            FTPFile[] ftpFiles = ftp.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.isFile()) {
                    String ftpFileName = ftpFile.getName();
                    loadFile(ftp, ftpFileName);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadFile(FTPClient ftp, String fileName) throws IOException {
        FileOutputStream outputStream = new FileOutputStream("E:\\ftp_loacl\\" + fileName);
        ftp.retrieveFile("\\" + fileName, outputStream);
    }
}
