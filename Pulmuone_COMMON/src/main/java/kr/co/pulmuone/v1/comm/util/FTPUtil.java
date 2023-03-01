package kr.co.pulmuone.v1.comm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FTPUtil {
	JSch jsch = null;
	private Session session = null;
	private Channel channel = null;
	private ChannelSftp sftpChannel = null;

	String cjHost;
	String cjUser;
	String cjPassword;

	public FTPUtil(String host, String user, String password) {
		this.cjHost = host;
		this.cjUser = user;
		this.cjPassword = password;
	}

	// SFTP 서버연결
    public void connect() throws JSchException {
        System.out.println("connecting cjUser ::: <{}>"+ cjUser);

        //JSch 객체를 생성한다.
        jsch = new JSch();

        //세션 객체를 생성한다(사용자 이름, 접속할 호스트, 포트를 인자로 전달한다.)
        session = jsch.getSession(cjUser, cjHost);

        //세션과 관련된 정보를 설정한다.
        session.setConfig("StrictHostKeyChecking", "no");

        //패스워드를 설정한다.
        session.setPassword(cjPassword);

        //접속한다.
        session.connect();

        //sftp 채널을 연다.
        channel = session.openChannel("sftp");

        //채널에 연결한다.
        channel.connect();

        //채널을 FTP용 채널 객체로 캐스팅한다.
        sftpChannel = (ChannelSftp) channel;
        System.out.println("커넥션 연결 OK !!!");
    }

    //접속해제
    public void disconnect() {
        System.out.println("session.isConnected() --> " + session.isConnected());
    	if(session.isConnected()){
            System.out.println("sftpChannel disconnecting Start...");
            sftpChannel.disconnect();
            System.out.println("channel disconnecting Start...");
            channel.disconnect();
            System.out.println("session disconnecting Start...");
            session.disconnect();
            System.out.println("disconnecting End...");
        }
    }

	// 단일 파일 업로드
    public void upload(String fileName, String remoteDir) throws Exception {
        FileInputStream fis = null;
        // 앞서 만든 접속 메서드를 사용해 접속한다.
        connect();
        try {
            // Change to output directory
            sftpChannel.cd(remoteDir);

            // Upload file
            File file = new File(fileName);
            // 입력 파일을 가져온다.
            fis = new FileInputStream(file);
            // 파일을 업로드한다.
            sftpChannel.put(fis, file.getName());

            fis.close();
            System.out.println("File uploaded successfully - "+ file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
    }

	// 단일 파일 다운로드
    public void download(String fileName, String localDir) throws Exception{
    	System.out.println("파일 다운로드 fileName :: " + fileName + ", localDir :: "+ localDir);
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        BufferedOutputStream bos = null;
        log.info("CJ SFTP 커넥션 연결 시작!!!!");
        connect();
        log.info("CJ SFTP 커넥션 연결 끝 !!!!!");
        try {
        	File folder = new File(localDir);
        	System.out.println("파일 다운로드 folder :: " + folder + ", folder.exists() :: "+ folder.exists());
            if (!folder.exists()) {
            	folder.mkdirs();
            }
            System.out.println("파일 다운로드 폴더 존재유무 :: "+ folder.exists());
            // Change to output directory
            String cdDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
            log.info("파일 다운로드cdDir :: <{}>", cdDir);

            sftpChannel.cd(cdDir);

            File file = new File(fileName);
            System.out.println("파일 다운로드 file :: "+ file);

            bis = new BufferedInputStream(sftpChannel.get(file.getName()));
            System.out.println("파일 다운로드 bis :: "+ bis);
            
            File newFile = new File(localDir + "/" + file.getName());
            System.out.println("파일 다운로드 newFile :: "+ newFile);

            // Download file
            os = new FileOutputStream(newFile);
            System.out.println("파일 다운로드 os :: "+ os);

            bos = new BufferedOutputStream(os);
            System.out.println("파일 다운로드 bos :: "+ bos);
            int readCount;

            while ((readCount = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
                //System.out.println("readCount :: " + readCount + " bis.read(buffer) ::: " + bis.read(buffer));
            }
            bos.flush();
            System.out.println("파일 다운로드 bos :: "+ bos);
            System.out.println("FTP UTIL downloaded successfully - "+ file.getAbsolutePath());

        }
        catch (Exception e) {
        	System.out.println("ftpUtil Error : " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            if(bos != null) {
                try { bos.close(); }
                catch(Exception e) { System.out.println("bos close Exception :: [" + e.getMessage() + "]"); }
            }
            if(os != null) {
                try { os.close(); }
                catch(Exception e) { System.out.println("os close Exception :: [" + e.getMessage() + "]"); }
            }
            if(bis != null) {
                try { bis.close(); }
                catch(Exception e) { System.out.println("bis close Exception :: [" + e.getMessage() + "]"); }
            }
            disconnect();
        }
    }

}
