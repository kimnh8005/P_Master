package kr.co.pulmuone.v1.comm.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PrintUtil {

	/* 접속정보 */
	public static boolean DEBUG = true;
	private static final String logDirectory = "/kgc/logs";

	/**
	 * 메세지를 출력해준다.
	 *
	 * @param message  메세지
	 * @param task     구분
	 */
	public PrintUtil( String message, String task ){

		if( DEBUG ){

	        try{

	        	// IP 와 비교하여 디버깅 아이피와 같을 경우 파일 로그를 남긴다.
        		File file = new File( logDirectory + File.separator + task );

				if(!file.exists()) file.mkdirs();

				// 로그 파일명 : 경로 + 금일날짜.log
				StringBuffer    fileLogName = new StringBuffer( logDirectory + File.separator + task );
								fileLogName.append( File.separator );
								fileLogName.append( "[" + task + "] " + DateUtil.getCurrentDate("yyyyMMdd") + ".log" );


				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	        	StringBuffer log = new StringBuffer();
	        	log.append("[").append( sdf2.format(new Date()) ).append("] ").append(" : ").append( message );

	            BufferedWriter  bw = new BufferedWriter( new FileWriter(fileLogName.toString(), true) );
					            bw.write( log.toString() );
					            bw.newLine();
					            bw.close();


	        }catch(Exception e){
	            e.printStackTrace();
	        }
		}

	}

}
