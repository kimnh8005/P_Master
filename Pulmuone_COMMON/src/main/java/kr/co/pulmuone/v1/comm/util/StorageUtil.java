package kr.co.pulmuone.v1.comm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.comm.framework.dto.MultiFileUploadRequestDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadResultDto;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.SaveResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StorageUtil {

    @Value("${app.storage.public.public-storage-url}")
    private String publicStorageUrlValue; // public 저장소 접근 url
    private static String publicStorageUrl;

    @Value("${app.storage.public.public-url-path}")
    private String publicUrlPathValue; // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 ( CORS 회피용 )
    private static String publicUrlPath;

    @Autowired
    StorageUtil( //
            // public 저장소 접근 url
            @Value("${app.storage.public.public-storage-url}") String publicStorageUrlValue //
            // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 ( CORS 회피용 )
            , @Value("${app.storage.public.public-url-path}") String publicUrlPathValue //
    ) {

        StorageUtil.publicStorageUrl = publicStorageUrlValue; // @Value 로 조회한 값을 static 변수에 세팅
        StorageUtil.publicUrlPath = publicUrlPathValue; // @Value 로 조회한 값을 static 변수에 세팅

    }

    /*
     * Public 성격의 파일 다중 업로드 처리
     */
    public static UploadResultDto saveMultiplePublicFiles(MultiFileUploadRequestDto multiFileUploadRequestDto, String rootStoragePath, String fullSubStoragePath) {

        // 최상위 저장경로 또는 하위 저장경로가 null : SaveResult.NO_MATCHING_UPLOAD_INFO 로 처리
        if (rootStoragePath == null || fullSubStoragePath == null) {

            for (UploadFileDto uploadFileDto : multiFileUploadRequestDto.getUploadFileDtoList()) {
                uploadFileDto.setSaveResult(SaveResult.NO_MATCHING_UPLOAD_INFO);
            }

            return UploadResultDto.builder() //
                    .uploadFileDtoList(multiFileUploadRequestDto.getUploadFileDtoList()) //
                    .build();

        }

        // ( 서버내 root 경로 + 1차 하위경로 + 2차 하위경로 ) 를 모두 합한 전체 경로를 업로드 경로 dto 에 설정
        multiFileUploadRequestDto.setFullStoragePath(rootStoragePath + fullSubStoragePath);

        // 업로드 경로 존재여부 확인 후 생성
        File uploadFolder = new File(multiFileUploadRequestDto.getFullStoragePath());

        if (!uploadFolder.exists()) { // 업로드 경로가 없는 경우

            try {

                uploadFolder.mkdirs(); // mkdir 아님

            } catch (Exception e) {
                log.error("public upload directory mkdir failed", e);
                throw new RuntimeException(e);
            }

        }

        // 해당 업로드 경로에 실제 파일 업로드
        for (UploadFileDto uploadFileDto : multiFileUploadRequestDto.getUploadFileDtoList()) {

            // 물리적 파일명 : 20글자 UUID + "." + 원본 파일 확장자
            String physicalFileName = getPhysicalFileName() + "." + uploadFileDto.getFileExtension();

            File convertedFile = new File(multiFileUploadRequestDto.getFullStoragePath() + physicalFileName);

            try {
                uploadFileDto.getMultipartFile().transferTo(convertedFile); // 실제 파일 저장

                // 저장된 하위 경로 => 저장된 파일의 접근 URL 이기도 함, 파일 구분자를 모두 "/" 로 치환
                uploadFileDto.setServerSubPath(fullSubStoragePath.replaceAll(Matcher.quoteReplacement(File.separator), "/"));

                uploadFileDto.setPhysicalFileName(convertedFile.getName()); // 실제 저장된 물리적 파일명
                uploadFileDto.setFileSize(convertedFile.length()); // 실제 저장된 파일 size
                uploadFileDto.setSaveResult(SaveResult.SUCCESS);

            } catch (IllegalStateException | IOException e) {
                uploadFileDto.setSaveResult(SaveResult.FAILURE);
                log.error("public upload file save failure", e);
            }

        }

        return UploadResultDto.builder() //
                .publicStorageUrl(publicStorageUrl) // public 저장소 접근 url
                .publicUrlPath(publicUrlPath) // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 ( CORS 회피용 )
                .uploadFileDtoList(multiFileUploadRequestDto.getUploadFileDtoList()) //
                .build();

    }

    /*
     * 서버에 저장할 물리적 파일명 반환
     */
    private static String getPhysicalFileName() {
        // 20 글자 UUID 반환
        return UidUtil.randomUUID().toString().replaceAll("-", "").substring(0, 20).toUpperCase();
    }

    /*
     * 해당 파일 경로에 있는 파일의 MediaType 반환
     */
    public static MediaType getMediaType(String fullFilePath, String physicalFileName) {

        // mineType, mediaType 체크 시작
        String mineType = URLConnection.guessContentTypeFromName( //
                fullFilePath + File.separator + physicalFileName //
        );

        MediaType mediaType = null;

        // mineType 이 null 인 경우 : "application/octet-stream" 으로 contentType 설정
        if (mineType == null || mineType.equals("") || mineType.equals("null")) {

            mediaType = MediaType.APPLICATION_OCTET_STREAM;

        } else {

            mediaType = MediaType.parseMediaType(mineType);

        }

        return mediaType;

    }

    /*
     * 해당 파일의 InputStreamResource 반환
     */
    public static InputStreamResource getInputStreamResource(File file) {

        InputStreamResource resource = null;

        if (file.exists()) { // 해당 경로에 다운로드 요청된 파일 존재시

            try {

                resource = new InputStreamResource(new FileInputStream(file));

            } catch (FileNotFoundException e) {
                log.error("file loading falied");
                throw new RuntimeException("파일 로딩 중 에러가 발생하였습니다.", e);
            }

        } else { // 파일 미존재시

            log.error("file not exists");
            throw new RuntimeException("해당 경로에 파일이 존재하지 않습니다.");
        }

        return resource;

    }

    /*
     * 인코딩된 파일명 반환
     */
    public static String getEncodedFileName(String originalFileName) {

        String encodedFileName = null;

        try {

            encodedFileName = java.net.URLEncoder.encode(originalFileName, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            throw new RuntimeException("다운로드할 파일명 인코딩 중 에러가 발생하였습니다.", e);
        }

        return encodedFileName;

    }

}
