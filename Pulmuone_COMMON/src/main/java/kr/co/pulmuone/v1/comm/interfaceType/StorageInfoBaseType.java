package kr.co.pulmuone.v1.comm.interfaceType;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

import kr.co.pulmuone.v1.comm.framework.dto.UploadOptionInfoDto;

public interface StorageInfoBaseType {

    public static final String DOMAIN_PREFIX_KEY_NAME = "domain";
    public static final String STORAGE_TYPE_KEY_NAME = "storageType";

    public static final String UPLOAD_RESULT_RETURN_KEY = "addFile";

    // -- 허용가능 공통 확장자
    public static String[] SYSTEM_PERMIT_FILE_EXTENSION = new String[] { //
            "txt", "hwp", "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "odp" //
            , "jpg", "jpeg", "gif", "png", "bmp" //
    };

    public StorageType getStorageType();

    public String getSubStoragePath();

    public SubDateTimePath getSubDateTimePath();

    public UploadOptionInfoDto getUploadOptionInfo();

    /*
     * 파일 저장시 1차 하위 경로 반환 : 도메인별
     */
    public default String getSubStoragePath(StorageInfoBaseType storageInfoEnum) {

        return storageInfoEnum.getSubStoragePath().replaceAll("/", Matcher.quoteReplacement(File.separator));

    }

    /*
     * 파일 저장시 2차 하위 경로 반환 : 날짜/시간별
     */
    public default String getSubDateTimePath(StorageInfoBaseType storageInfoEnum) {

        String dateTimePathStr = null;

        // 날짜/시간별 정보 미지정 enum 인 경우 "년/월/일" 형식을 반환
        if (storageInfoEnum.getSubDateTimePath() == null) {

            dateTimePathStr = ZonedDateTime.now(ZoneId.of("Asia/Seoul")) //
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return dateTimePathStr.replaceAll(SubDateTimePath.DATETIME_SEPARATOR, Matcher.quoteReplacement(File.separator));

        }

        dateTimePathStr = ZonedDateTime.now(ZoneId.of("Asia/Seoul")) //
                .format(DateTimeFormatter.ofPattern(storageInfoEnum.getSubDateTimePath().getDateTimeFormat()));

        return dateTimePathStr.replaceAll(SubDateTimePath.DATETIME_SEPARATOR, Matcher.quoteReplacement(File.separator));

    }

    // --------------- SubDateTimePath Enum ---------------

    public enum SubDateTimePath { // 날짜/시간별 2차 하위경로 지정 : 저장시 년, 월, 일 .. 순서대로 디렉토리 생성됨

        NONE("") // 날짜/시간별 저장하지 않음 (2차 하위경로 없음)
        , YEAR_TO_DATE("yyyy-MM-dd") // 년, 월, 일
        , YEAR_TO_HOUR("yyyy-MM-dd-HH") // 년, 월, 일, 시간
        , YEAR_TO_MINUTE("yyyy-MM-dd-HH-mm") // 년, 월, 일, 시간, 분
        ;

        // 시간/날짜 구분자 : enum 값 선언시 반드시 해당 구분자를 사용할 것!!
        public static final String DATETIME_SEPARATOR = "-";

        private String dateTimeFormat;

        SubDateTimePath(String dateTimeFormat) {
            this.dateTimeFormat = dateTimeFormat;
        }

        public String getDateTimeFormat() {
            return this.dateTimeFormat;
        }

    }

    // --------------- StorageType Enum ---------------

    public enum StorageType {
        PUBLIC // 외부에서 별도 인증절차 없이 접근 가능한 public 성격의 파일 업로드
        , PRIVATE // 인증 후 접근 가능한 private 성격의 파일 업로드
    }

    // --------------- StorageType Enum ---------------
    public enum SaveResult {
        SUCCESS // 업로드 성공
        , NO_MATCHING_UPLOAD_INFO // 매칭되는 관련 업로드 설정 정보 없음
        , FAILURE; // 업로드 실패
    }

}
