package kr.co.pulmuone.mall.comm.constant;

import kr.co.pulmuone.v1.comm.framework.dto.UploadOptionInfoDto;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

public enum MallStorageInfoEnum implements StorageInfoBaseType {

    /*
     * 도메인별 파일 업로드시 저장 경로 정보
     */
    UR_PUBLIC_STORAGE_INFO(StorageType.PUBLIC, MallDomainPrefixEnum.UR, //
            "ur/test", SubDateTimePath.YEAR_TO_DATE //
            , UploadOptionInfoDto.builder().build() //
    ) //
    , IL_PUBLIC_STORAGE_INFO(StorageType.PUBLIC, MallDomainPrefixEnum.IL, //
            "il/test", SubDateTimePath.YEAR_TO_DATE //
            , UploadOptionInfoDto.builder().build() //
    ) //
    , SN_PUBLIC_STORAGE_INFO(StorageType.PUBLIC, MallDomainPrefixEnum.SN, //
            "sn/test", SubDateTimePath.YEAR_TO_DATE //
            , UploadOptionInfoDto.builder().build() //
    ) //
    , CS_PUBLIC_STORAGE_INFO(StorageType.PUBLIC, MallDomainPrefixEnum.CS, //
            "cs/test", SubDateTimePath.YEAR_TO_DATE //
            , UploadOptionInfoDto.builder().build() //
    ) //
    , FB_PUBLIC_STORAGE_INFO(StorageType.PUBLIC, MallDomainPrefixEnum.FB, //
            "fb/test", SubDateTimePath.YEAR_TO_DATE //
            , UploadOptionInfoDto.builder().build() //
    ) //
    ;

    // --------------- 관련 업무 API 별 파일 2차 / 3차 저장경로 정보 End ---------------

    // --------------- 저장 타입별 파일 최상위 저장경로 정보 Start : StorageRootLocationInjector 사용하여 값 주입 ---------------
    private static String moduleName;
    private static String publicRootLocation;

    /*
     * 추후 인증시만 저장 가능한 private 저장경로 추가 예정
     */

    // --------------- 저장 타입별 파일 최상위 저장경로 End ---------------

    private StorageType storageType; // 저장 타입 : 타입별로 최상위 저장경로가 달라짐
    private MallDomainPrefixEnum domainPrefix; // 관련된 도메인 prefix
    private String subStoragePath; // 2차 하위 저장 경로 : 도메인별
    private SubDateTimePath subDateTimePath; // 3차 하위 저장 경로 : 날짜/시간별
    private UploadOptionInfoDto uploadOptionInfo; // 해당 업로드 Process 관련 추가 옵션 정보

    MallStorageInfoEnum(StorageType storageType, MallDomainPrefixEnum domainPrefix, String subStoragePath, SubDateTimePath subDateTimePath, UploadOptionInfoDto uploadOptionInfo) {
        this.storageType = storageType;
        this.domainPrefix = domainPrefix;
        this.subStoragePath = subStoragePath;
        this.subDateTimePath = subDateTimePath;
        this.uploadOptionInfo = uploadOptionInfo;
    }

    /*
     * 전달된 storageType 값에 따라 서버내 Root 저장경로 반환
     */
    public static String getRootStoragePath(String storageType) {

        if (storageType == null) {
            return null;
        }

        if (storageType.equalsIgnoreCase(StorageType.PUBLIC.name())) {
            return MallStorageInfoEnum.publicRootLocation;
        }

        /*
         * 추후 인증시만 저장 가능한 private 저장경로 추가 예정
         */

        return null;

    }

    /*
     * 현재 가동중인 모듈명 / 해당 enum 정보 참조하여 전체 하위 경로 반환
     *
     * - moduleName : 1차 하위경로, 모듈별 ( "BOS", "MALL" )
     *
     * - subStoragePath : 2차 하위경로, 도메인별
     *
     * - subDateTimePath : 3차 하위경로, 날짜/시간별
     */
    public static String getFullSubStoragePath(String storageType, String domainPrefix) {

        if (storageType == null || domainPrefix == null) {
            return null;
        }

        for (MallStorageInfoEnum storageInfoEnum : MallStorageInfoEnum.values()) {

            if (storageInfoEnum.getDomainPrefix().name().equalsIgnoreCase(domainPrefix.toUpperCase()) //
                    && storageType.equalsIgnoreCase(StorageType.PUBLIC.name())) {

                return moduleName //
                        + File.separator //
                        + storageInfoEnum.getSubStoragePath(storageInfoEnum) //
                        + File.separator //
                        + storageInfoEnum.getSubDateTimePath(storageInfoEnum) //
                        + File.separator;

            }

        }

        return null;
    }

    public MallDomainPrefixEnum getDomainPrefix() {
        return this.domainPrefix;
    }

    @Override
    public StorageType getStorageType() {
        return this.storageType;
    }

    @Override
    public String getSubStoragePath() {
        return this.subStoragePath;
    }

    @Override
    public SubDateTimePath getSubDateTimePath() {
        return this.subDateTimePath;
    }

    @Override
    public UploadOptionInfoDto getUploadOptionInfo() {
        return this.uploadOptionInfo;
    }

    /*
     * Enum 내에서 @Value("${...}") 값들을 조회 후 static 타입으로 세팅하는 Injector 클래스
     */
    @Component
    static class StorageRootLocationInjector implements InitializingBean {

        @Value("${module-name}")
        private String moduleNameValue; // 현재 가동중인 모듈명 : "BOS" 또는 "MALL"

        @Value("${app.storage.public.public-root-location}")
        private String publicRootLocation;

        @Override
        public void afterPropertiesSet() throws Exception {

            // 현재 가동중인 모듈명 : 1차 하위 경로로 사용
            MallStorageInfoEnum.moduleName = moduleNameValue;

            // 프로파일별로 public 저장소 경로를 다르게 세팅 : SystemUtil.getAbsolutePathByProfile(String path) 메서드 참조
            MallStorageInfoEnum.publicRootLocation = SystemUtil.getAbsolutePathByProfile(publicRootLocation);
        }

    }

}
