package com.overWorkGathering.main.utils;

import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.Vector;

public class FTPUtil {

    private Session session = null;
    private Channel channel = null;
    private ChannelSftp channelSftp = null;
    FTPClient ftpClient = null;

    // param( host server ip, username, password ) 생성자
    public FTPUtil(String host, int port, String user, String pwd, String privateKey) throws Exception {
        JSch jSch = new JSch();

        try {
            if(privateKey != null) {//개인키가 존재한다면
                jSch.addIdentity(privateKey);
            }
            session = jSch.getSession(user, host, port);

            if(privateKey == null && pwd != null) {//개인키가 없다면 패스워드로 접속
                session.setPassword(pwd);
            }

            // 프로퍼티 설정
            java.util.Properties config = new java.util.Properties();
            session.setConfig("StrictHostKeyChecking", "no"); // 접속 시 hostkeychecking 여부
            session.connect();
            //sftp로 접속
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        channelSftp = (ChannelSftp) channel;
    }

    /**
     * 디렉토리 생성
     *
     * @param dir 이동할 주소
     * @param mkdirName 생성할 디렉토리명
     */
    public void mkdir(String dir, String mkdirName) {
        if (!this.exists(dir + "/" + mkdirName)) {
            try {
                channelSftp.cd(dir);
                channelSftp.mkdir(mkdirName);
            } catch (SftpException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 디렉토리( or 파일) 존재 여부
     * @param path 디렉토리 (or 파일)
     * @return
     */
    public boolean exists(String path) {
        Vector res = null;
        try {
            res = channelSftp.ls(path);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
        }
        return res != null && !res.isEmpty();
    }

    /**
     * 파일 업로드
     *
     * @param dir 저장할 디렉토리
     * @param file 저장할 파일
     * @return 업로드 여부
     */
    public boolean uploadFile(String dir, File file) {
        boolean isUpload = false;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            channelSftp.cd(dir);
            channelSftp.put(in, file.getName());
            // 업로드했는지 확인
            if (this.exists(dir +"/"+file.getName())) {
                isUpload = true;
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isUpload;
    }

    /**
     * 파일 다운로드
     *
     * @param dir 다운로드 할 디렉토리
     * @param downloadFileName 다운로드 할 파일
     * @param path 다운로드 후 로컬에 저장될 경로(파일명)
     */
    public void download(String dir, String downloadFileName, String path) {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buffer = new byte[1024 * 8];

        try {
            channelSftp.cd(dir);
            Vector<?> fileList = channelSftp.ls(dir);

            if(!fileList.isEmpty()){
                in = channelSftp.get(downloadFileName);

                out = new FileOutputStream(new File(path + "/" + downloadFileName));
                int i;

                while ((i = in.read(buffer)) != -1) {
                    out.write(buffer, 0, i);
                }
            }
        } catch (SftpException sfte) {
            sfte.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch(Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 파일 삭제
     *
     * @param dir 삭제할 파일경로
     * @param fileName 삭제할 파일
     * @return 삭제 여부
     */
    public boolean deleteFile(String dir, String fileName) {
        boolean isUpload = false;
        FileInputStream in = null;
        try {
            channelSftp.cd(dir);
            channelSftp.rm(fileName);
            // 삭제됐는지 확인
            if (this.exists(dir +"/"+fileName)) {
                isUpload = true;
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isUpload;
    }

    /**
     * 연결 종료
     */
    public void disconnect() {
        channelSftp.quit();
        session.disconnect();
    }
}
