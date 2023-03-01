package kr.co.pulmuone.bos.base.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import kr.co.pulmuone.bos.comm.constant.BosDomainPrefixEnum;
import kr.co.pulmuone.bos.comm.constant.BosStorageInfoEnum;
import kr.co.pulmuone.v1.comm.framework.dto.MultiFileUploadRequestDto;
import kr.co.pulmuone.v1.comm.framework.dto.ServerUrlDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadResultDto;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.StorageType;
import kr.co.pulmuone.v1.comm.exception.UserException;
import kr.co.pulmuone.v1.comm.util.StorageUtil;

@RestController
public class StorageController {

    // 파일 다운로드시 Response Header 에 인코딩된 파일명을 지정할 Key 값
    private static final String DOWNLOAD_FILE_NAME_RESPONSE_HEADER_KEY = "file-name";

    @Value("${app.storage.public.public-storage-url}")
    private String publicStorageUrl; // public 저장소 접근 url

    @Value("${app.storage.public.public-url-path}")
    private String publicUrlPath; // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 ( CORS 회피용 )

    @Value("${resource.server.url.bos}")
    private String bosUrl;

    @Value("${resource.server.url.mall}")
    private String mallUrl;

    @Value("${resource.server.url.image}")
    private String imageUrl;


    @PostMapping(value = "/comn/fileUpload")
    @ResponseBody
    @ApiOperation(value = "파일 업로드 API", notes = "외부에서 별도 인증절차 없이 접근 가능한 public 성격의 파일 업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UploadResultDto.class)
    })
    public ApiResult<?> uploadMultiplePublicFiles(MultiFileUploadRequestDto multiFileUploadRequestDto) {

        /* BosStorageInfoEnum 참조하여 최상위 저장 디렉토리 경로 산정 */
        String rootStoragePath = BosStorageInfoEnum.getRootStoragePath( //
                multiFileUploadRequestDto.getStorageType() //
        );
        /* BosStorageInfoEnum 참조하여 하위 디렉토리 ( 1차 (모듈별) / 2차 (도메인별) / 3차 (시간별) ) 경로 산정 */
        String fullSubStoragePath = BosStorageInfoEnum.getFullSubStoragePath( //
                multiFileUploadRequestDto.getStorageType(), multiFileUploadRequestDto.getDomainPrefix() //
        );


        return ApiResult.success(StorageUtil.saveMultiplePublicFiles(multiFileUploadRequestDto, rootStoragePath, fullSubStoragePath));

    }

    /*
     * publicStorageUrl : 프로파일별 public 저장소 접근 URL 반환
     *
     * (local) http://localhost:${server.port}/${app.storage.public.public-url-path}/ 반환
     *
     * (dev) https://sdev0.pulmuone.app/ 반환
     *
     * publicUrlPath : CORS 회피 위해 API 서버로 저장소 접근시 url 경로, 모든 프로파일에서 "/pulmuone/public" 으로 동일함
     *
     */
    @GetMapping(value = "/comn/getPublicStorageUrl")
    @ApiOperation(value = "프로파일별 public 저장소 접근 URL 정보", notes = "CORS 회피 위해 API 서버로 저장소 접근시 url 경로 정보, 모든 프로파일에서 /pulmuone/public 으로 동일함")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UploadResultDto.class)
    })
    public ApiResult<?> getPublicStorageUrl() {

        UploadResultDto uploadResultDto = UploadResultDto.builder() //
                .publicStorageUrl(this.publicStorageUrl) //
                .publicUrlPath(this.publicUrlPath) //
                .build();

        return ApiResult.success(uploadResultDto);
    }

    /*
     * 추후 인증시만 접근 가능한 private 성격의 파일 업로드 API 구현 예정
     */

    /*
     * 추후 BosStorageInfoEnum 을 참조하지 않고 화면에서 별도 전송한 저장 경로로 저장되는 파일 업로드 API 구현 예정
     */

    /*
     * public 저장소의 파일 다운로드 API
     */
    @GetMapping(value = "/comn/fileDownload")
    @ApiOperation(value = "파일 다운로드", notes = "public 저장소의 파일 다운로드 API")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = Resource.class)
    })
    public ResponseEntity<Resource> downloadPublicFile( //
            @RequestParam(value = "filePath") String filePath // 다운로드할 파일의 하위 경로
            , @RequestParam(value = "physicalFileName") String physicalFileName // 업로드시 저장된 물리적 파일명
            , @RequestParam(value = "originalFileName") String originalFileName // 원본 파일명 또는 다운로드시 지정할 파일명
    ) {

        // Public 저장소의 최상위 저장 디렉토리 경로
        String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());

        // 다운로드할 파일의 전체 경로 : 물리적 파일명은 미포함
        String fullFilePath = publicRootStoragePath + //
                UriUtils.decode(filePath, "UTF-8") // URI decoding
        ;

        // mineType, mediaType 체크 시작
        MediaType mediaType = StorageUtil.getMediaType(fullFilePath, physicalFileName);

        // 다운로드할 파일의 전체 경로에 해당하는 File 객체 생성
        File file = new File(fullFilePath + File.separator + physicalFileName);

        // 다운로드할 파일의 InputStreamResource 객체 생상
        InputStreamResource resource = StorageUtil.getInputStreamResource(file);

        // 프론트로 전달할 다운로드 파일명 인코딩 처리 : 프론트에서 다시 디코딩
        String encodedDownloadFileName = StorageUtil.getEncodedFileName(originalFileName);

        /*
         * response 생성 / return
         */
        HttpHeaders responseHeaders = new HttpHeaders();

        // 인코딩된 다운로드 파일명을 Response Header 에 지정
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION //
                , "attachment;filename=\"" + encodedDownloadFileName + "\";charset=\"UTF-8\"");
        responseHeaders.set(DOWNLOAD_FILE_NAME_RESPONSE_HEADER_KEY, encodedDownloadFileName);

        // Response Header 에서 노출할 Header Key 지정
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, DOWNLOAD_FILE_NAME_RESPONSE_HEADER_KEY);

        return ResponseEntity.ok() //
                .headers(responseHeaders) //
                .contentType(mediaType) //
                .contentLength(file.length()) //
                .body(resource);

    }

    /*
     * public 저장소의 파일 삭제 API
     */
    @DeleteMapping("/comn/fileDelete")
    @ApiOperation(value = "저장소의 파일 삭제", notes = "public 저장소의 파일 삭제 API")
    public boolean deletePublicFile( //
            @RequestParam(value = "filePath") String filePath // 삭제할 파일의 하위 경로
            , @RequestParam(value = "physicalFileName") String physicalFileName // 업로드시 저장된 물리적 파일명
    ) throws UserException {

        // Public 저장소의 최상위 저장 디렉토리 경로
        String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());

        // 삭제할 파일의 전체 경로 : 물리적 파일명은 미포함
        String fullFilePath = publicRootStoragePath + //
                UriUtils.decode(filePath, "UTF-8") // URI decoding
        ;

        Path path = FileSystems.getDefault().getPath(fullFilePath, physicalFileName);
        boolean isDeletedSuccess = false; // 파일 삭제 성공 여부

        try {

            Files.delete(path); // 해당 path 의 파일 삭제
            isDeletedSuccess = true;

        } catch (NoSuchFileException e) {

            throw new UserException("해당 경로에 삭제할 파일이 없습니다.", e);

        } catch (IOException e) {

            throw new UserException("파일 삭제 중 에러가 발생하였습니다.", e);

        }

        return isDeletedSuccess;
    }


    /*
     * serverUrl : 같은 레벨 서버 URL 반환
     *
     * (local,devl)	bos: https://dev0shopbos.pulmuone.online
     * 				mall: https://dev0shop.pulmuone.online
     * 				image: https://sdev0.pulmuone.app 반홤
     *
     * (qa) bos: https://qashopbos.pulmuone.online
     * 		mall: https://qashop.pulmuone.online
     * 		image: https://sqa.pulmuone.app 반환
     *
     */
    @GetMapping(value = "/comn/getServerUrl")
    @ApiOperation(value = "같은 레벨 서버 접근 URL 정보")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ServerUrlDto.class)
    })
    public ApiResult<?> getServerUrl() {

    	ServerUrlDto serverUrlDto = ServerUrlDto.builder()
    								.bosUrl(this.bosUrl)
    								.mallUrl(this.mallUrl)
    								.imageUrl(this.imageUrl)
    								.build();

        return ApiResult.success(serverUrlDto);
    }

}
