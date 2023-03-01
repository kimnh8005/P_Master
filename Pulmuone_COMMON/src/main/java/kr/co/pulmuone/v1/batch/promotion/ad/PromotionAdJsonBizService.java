package kr.co.pulmuone.v1.batch.promotion.ad;

import kr.co.pulmuone.v1.batch.promotion.ad.dto.vo.SamsungAdVo;
import kr.co.pulmuone.v1.comm.enums.AdvertisingEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.promotion.PromotionAdvertisingBatchMapper;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionAdJsonBizService {

    private final PromotionAdvertisingBatchMapper promotionAdvertisingMapper;

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

    @Value("${resource.server.url.image}")
    private String imageUrl;

    public void runMakeJson() throws Exception {

        makeCache();
    }

    /**
     * 공통 저장경로 + path getter
     *
     * @return String
     */
    private String getCacheLocalFilePath(String path) {
        return Paths.get(publicRootLocation + File.separator + moduleName + File.separator + cacheFilePath + File.separator + "advertising" + File.separator + path).toString();
    }

    /**
     * API 결과 File 생성
     */
    protected void makeCache() throws Exception {
        List<SamsungAdVo> samsungAdVo = promotionAdvertisingMapper.getAdExternalGoodsList(AdvertisingEnums.AdvertCompany.SAMSUNG.getCode(), apiDomain, imageUrl);

        String filePath = getCacheLocalFilePath(AdvertisingEnums.AdvertCompany.SAMSUNG.getCode() + ".json");

        addFile(filePath, JsonUtil.serializeWithPrettyPrinting(samsungAdVo));
    }

    /**
     * File 저장
     *
     * @param filePathName String
     * @param fileValue    String
     */
    private void addFile(String filePathName, String fileValue) throws IOException {
        Path pathFile = Paths.get(filePathName);

        // 업로드 경로 존재여부 확인 후 생성
        File uploadFolder = new File(pathFile.getParent().toString());
        if (!uploadFolder.exists()) { // 업로드 경로가 없는 경우
            uploadFolder.mkdirs();
        }

        File convertedFile = new File(filePathName);
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(fileValue.getBytes());
        fos.close();
    }
}
