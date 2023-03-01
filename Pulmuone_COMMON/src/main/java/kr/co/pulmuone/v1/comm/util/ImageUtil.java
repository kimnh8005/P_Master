package kr.co.pulmuone.v1.comm.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import kr.co.pulmuone.v1.comm.util.image.gif.GifUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.util.UriUtils;

public class ImageUtil {

    private ImageUtil() {
    }

    /*
     * 원본 이미지 경로와 동일한 경로에 리사이징된 이미지 파일 새로 생성
     *
     * 현재 품목 이미지 등록 가능 확장자 jpeg, jpg, gif 로 리사이징 가능하도록 구현함
     *
     * @@param filePath : 리사이징할 원본 이미지 파일의 전체 경로
     *
     * @@param originalFileName : 리사이징할 원본 이미지 파일명 ( 확장자 포함 )
     *
     * @@param imageNamePrefix : 리사이징하여 생성할 이미지명의 prefix => imageNamePrefix + originalFileName 형식으로 리사이징 이미지 생성함
     *
     * @@param width : 리사이징하여 생성할 이미지 가로 Size
     *
     * @@param height : 리사이징하여 생성할 이미지 세로 Size
     *
     */
    public static void createResizedImage(String filePath, String originalFileName, String imageNamePrefix, int width, int height) {

        String fileExtension = FilenameUtils.getExtension(originalFileName).toLowerCase(); // 이미지 파일 확장자

        // 리사이징할 원본 파일의 전체 경로 : 물리적 파일명은 미포함
        String fullFilePath = UriUtils.decode(filePath, "UTF-8"); // URI decoding

        // 리사이징할 원본 파일의 전체 경로에 해당하는 File 객체 생성
        File originalFile = FileSystems.getDefault().getPath(fullFilePath, originalFileName).toFile();

        // 리사이징하여 새로 생성할 File 객체 생성
        File resizedFile = FileSystems.getDefault().getPath(fullFilePath, imageNamePrefix + originalFileName).toFile();

        if ("gif".equals(fileExtension)) {
            int maxImageDimension = width; // same width and height
            try {
                GifUtil.gifInputToOutput(originalFile, resizedFile, maxImageDimension);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            inputToOutput(originalFile, resizedFile, width, height, fileExtension);
        }

    }

    public static void inputToOutput(File originalFile, File resizedFile, int width, int height, String fileExtension) {
        // 리사이징할 원본 파일의 InputStreamResource 객체 생성
        InputStreamResource originalFileResource = StorageUtil.getInputStreamResource(originalFile);

        // 이미지 파일 확장자에 매칭되는 imageWriter 생성
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(fileExtension).next();

        // imageWriteParam 생성
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        // jpeg, jpg 인 경우 최대 품질로 리사이징
        if (fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(1.0f); // Highest quality
            imageWriteParam.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
        }

        try {

            if (ImageIO.read(originalFile) == null) { // 해당 파일이 이미지 파일이 아닌 경우 예외 처리
                throw new RuntimeException("not image file");
            }

            // 리사이징된 BufferedImage 객체 생성
            BufferedImage resizedImage = getResizedImage(originalFileResource.getInputStream(), width, height);

            imageWriter.setOutput(ImageIO.createImageOutputStream(resizedFile));
            IIOImage outputImage = new IIOImage(resizedImage, null, null);

            // 리사이징된 파일 생성
            imageWriter.write(null, outputImage, imageWriteParam);
            imageWriter.dispose();

        } catch (IllegalStateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 가로, 세로 값 없이 size 하나만 전달 : 가로, 세로 모두 같은 size 값으로 createResizedImage 메서드 실행
    public static void createResizedImage(String filePath, String originalFileName, String imageNamePrefix, int size) {
        createResizedImage(filePath, originalFileName, imageNamePrefix, size, size);
    }

    // 리사이징된 BufferedImage 객체 생성
    private static BufferedImage getResizedImage(InputStream inputStream, int width, int height) throws IOException {

        BufferedImage inputImage = ImageIO.read(inputStream);

/*
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(inputImage, 0, 0, width, height, null);
        graphics2D.dispose();

        return outputImage;
*/
        // image resize 로직 변경
        Image imgTarget = inputImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		int pixels[] = new int[width * height];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, width, height, pixels, 0, width);
		try {
		    pg.grabPixels();
		} catch (InterruptedException e) {
		    throw new IOException(e.getMessage());
		}
		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		outputImage.setRGB(0, 0, width, height, pixels, 0, width);
		return outputImage;
    }

}
