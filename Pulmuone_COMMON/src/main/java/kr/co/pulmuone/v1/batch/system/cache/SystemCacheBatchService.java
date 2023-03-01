package kr.co.pulmuone.v1.batch.system.cache;

import kr.co.pulmuone.v1.batch.system.cache.dto.CallApiResponseDto;
import kr.co.pulmuone.v1.batch.system.cache.dto.vo.SystemCacheVo;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.system.SystemCacheBatchMapper;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemCacheBatchService {
    private final SystemCacheBatchMapper systemCacheBatchMapper;

    @Value("${system_cache.api_domain}")
    private String apiDomain;

    @Value("${system_cache.cache_file_path}")
    private String cacheFilePath;

    @Value("${system_cache.cache_file_name}")
    private String cacheFileName;

    @Value("${module-name}")
    private String moduleName; // 현재 가동중인 모듈명

    @Value("${app.storage.public.public-root-location}")
    private String publicRootLocation;

    public void runMakeCache() throws IOException, BaseException {
        makeCache();
        makeCacheInfo();
    }

    /**
     * 공통 Temp 저장경로 + path getter
     */
    private String getCacheLocalTempFilePath(String path) {
        return Paths.get(publicRootLocation + File.separator + moduleName + File.separator + cacheFilePath + File.separator + "temp" + File.separator + path).toString();
    }

    /**
     * 공통 저장경로 + path getter
     *
     * @return String
     */
    private String getCacheLocalFilePath(String path) {
        return Paths.get(publicRootLocation + File.separator + moduleName + File.separator + cacheFilePath + File.separator + path).toString();
    }

    /**
     * path getter
     *
     * @return String
     */
    private String getCacheFilePath(String path) {
        return Paths.get(moduleName + File.separator + cacheFilePath + File.separator + path).toString();
    }

    /**
     * API 결과 File 생성
     */
    protected void makeCache() throws IOException, BaseException {
        Integer maxCount = systemCacheBatchMapper.getSystemCacheCount();
        if (maxCount == null || maxCount == 0) return;

        int page = 0;
        int limit = BatchConstants.BATCH_API_CACHE_COUNT;

        while (page <= maxCount) {
            List<SystemCacheVo> systemCacheVos = systemCacheBatchMapper.getSystemCache(page, limit);
            for (SystemCacheVo vo : systemCacheVos) {
                String tempFilePath = getCacheLocalTempFilePath(vo.getFilePathName());
                String filePath = getCacheLocalFilePath(vo.getFilePathName());
                String apiResult = processCallApi(vo.getApiUrl());

                if (apiResult.equals("FALSE")) continue;
                if (vo.getCacheData().equals(apiResult)) continue;

                addFile(tempFilePath, filePath, apiResult);               // make cache file
                systemCacheBatchMapper.putSystemCache(vo.getStApiCacheId(), apiResult);        // Update cache time
            }
            page += limit;
        }
    }

    /**
     * Cache 데이터 정보 File 생성
     */
    protected void makeCacheInfo() throws IOException {
        List<SystemCacheVo> systemCacheVos = systemCacheBatchMapper.getSystemCacheInfo();

        // List To Map
        Map<String, String> fileMap = systemCacheVos.stream()
                .collect(Collectors.toMap(
                        SystemCacheVo::getApiUrl,
                        vo -> getCacheFilePath(vo.getFilePathName())
                        )
                );

        addFile(getCacheLocalTempFilePath(cacheFileName), getCacheLocalFilePath(cacheFileName), JsonUtil.serializeWithPrettyPrinting(fileMap));
    }

    /**
     * API 통신 호출 - 실패시 재시도 진행
     *
     * @param apiUrl String
     * @return String
     */
    private String processCallApi(String apiUrl) throws BaseException {
        int cnt = 0;
        int limit = BatchConstants.BATCH_FAIL_RETRY_COUNT;

        String result = "";
        String errorMessage = "";
        while (true) {
            if (cnt >= limit) {
                throw new BaseException(errorMessage);
            }

            CallApiResponseDto responseDto = callApi(apiUrl);
            if (BaseEnums.Default.SUCCESS.equals(responseDto.getResponseEnum())) {  // 성공
                result = responseDto.getMessage();
                break;
            } else {
                errorMessage = responseDto.getMessage();
            }

            cnt++;
        }

        return result;
    }

    /**
     * API 통신
     *
     * @param apiUrl String
     * @return String
     */
    private CallApiResponseDto callApi(String apiUrl) {
        // API 호출 설정
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(apiDomain + apiUrl).build();

        // API 호출
        ResponseEntity<String> responseValue;
        try {
            responseValue = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            return CallApiResponseDto.builder()
                    .responseEnum(SystemEnums.CacheBatchError.CALL_API_FAIL)
                    .message(e.getMessage())
                    .build();
        }

        // 응답 값 검증
        if (responseValue.getStatusCodeValue() != 200) {
            return CallApiResponseDto.builder()
                    .responseEnum(SystemEnums.CacheBatchError.CALL_API_CODE_FAIL)
                    .message(SystemEnums.CacheBatchError.CALL_API_CODE_FAIL.getMessage())
                    .build();
        }
        String result = responseValue.getBody();
        if (result == null || !result.contains("\"code\":\"0000\"")) {
            return CallApiResponseDto.builder()
                    .responseEnum(SystemEnums.CacheBatchError.CALL_API_GET_FAIL)
                    .message(result)
                    .build();
        }

        // 성공
        return CallApiResponseDto.builder()
                .responseEnum(BaseEnums.Default.SUCCESS)
                .message(result)
                .build();
    }

    /**
     * File 저장
     *
     */
    private void addFile(String tempFilePathName, String realFilePathName, String fileValue) throws IOException {
        Path pathFile = Paths.get(tempFilePathName);

        // 업로드 경로 존재여부 확인 후 생성
        File uploadFolder = new File(pathFile.getParent().toString());
        if (!uploadFolder.exists()) { // 업로드 경로가 없는 경우
            uploadFolder.mkdirs();
        }
        File tempFile = new File(tempFilePathName);
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(fileValue.getBytes());
        fos.close();

        FileUtils.copyFile(tempFile, FileUtils.getFile(realFilePathName));
    }
}
