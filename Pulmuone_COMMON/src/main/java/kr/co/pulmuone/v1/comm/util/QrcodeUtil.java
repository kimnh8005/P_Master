package kr.co.pulmuone.v1.comm.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class QrcodeUtil {

	/**
	 * QR코드를 생성한다.
	 * @param url QR 작성 URL
	 * @param width QR 이미지 가로 사이즈
	 * @param height QR 이미지 세로 사이즈
	 * @param file_path 생성할 파일의 디렉토리 경로
	 * @param file_name 생성할 파일의 파일명
	 */
	public static void MakeQR(String url,int width, int height, String file_path, String file_name){

		try {

			File file = null;

			file = new File(file_path);
			if(!file.exists()) {
				file.mkdirs();
			}

			QRCodeWriter writer = new QRCodeWriter();
			url = new String(url.getBytes("UTF-8"), "ISO-8859-1");
			BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);

			//QR코드 색상
			int qrColor = 0xFF2e4e96;
			MatrixToImageConfig config = new MatrixToImageConfig(qrColor,0xFFFFFFFF);

			BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

			ImageIO.write(qrImage, "PNG", new File(file_path+file_name));

			} catch(Exception e) {
				e.printStackTrace();
			}
	}

}
