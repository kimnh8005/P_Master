package kr.co.pulmuone.batch.erp.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class SystemEnums {

    // ITGC 대상 메뉴관리
    @Getter
    @RequiredArgsConstructor
    public enum ItgcMenu {
        BOS_ACCOUNT("BOS_ACCOUNT", "BOS 계정관리", 1247L),
        ADMIN_AUTH("ADMIN_AUTH", "관리자 권한관리", 1248L),
        MENU_AUTH("MENU_AUTH", "메뉴권한관리", 1250L),
        BATCH("BATCH", "배치", 0L);

        private final String code;
        private final String codeName;
        private final Long stMenuId;
    }

    // ITGC 구분
    @Getter
    @RequiredArgsConstructor
    public enum ItgcType {
        ACCOUNT_ADD("ITGC_TP.ACCOUNT_ADD", "계정등록"),
        PERSONAL_INFO("ITGC_TP.PERSONAL_INFO", "개인정보 열람권한"),
        AUTH("ITGC_TP.AUTH", "권한설정"),
        ROLE_ADD("ITGC_TP.ROLE_ADD", "역할생성"),
        AUTH_GET("ITGC_TP.AUTH_GET", "권한설정 조회"),
        AUTH_ADD("ITGC_TP.AUTH_ADD", "권한설정 저장"),
        AUTH_DEL("ITGC_TP.AUTH_DEL", "권한설정 삭제"),
        AUTH_EXCEL_DOWN("ITGC_TP.AUTH_EXCEL_DOWN", "권한설정 엑셀다운로드"),
        SUPER_ROLE("ITGC_TP.SUPER_ROLE", "Super 역할 그룹 생성"),
        ADMIN_DROP("ITGC_TP.ADMIN_DROP", "퇴사관리자");

        private final String code;
        private final String codeName;
    }

    // ITGC 구분 - 상세
    @Getter
    @RequiredArgsConstructor
    public enum ItgcDetailType {
        ACCOUNT_ADD("ACCOUNT_ADD", "등록"),
        PERSONAL_INFO("PERSONAL_INFO", "변경상태"),
        AUTH("AUTH", "역할그룹명"),
        ROLE_ADD("ROLE_ADD", "역할그룹"),
        MENU("MENU", "메뉴페이지"),
        ADMIN_DROP("ADMIN_DROP", "퇴사관리자");

        private final String code;
        private final String codeName;
    }

    // ITGC 등록 구분
    @Getter
    @RequiredArgsConstructor
    public enum ItgcAddType {
        ADD("ITGC_ADD_TP.ADD", "추가"),
        DEL("ITGC_ADD_TP.DEL", "삭제");

        private final String code;
        private final String codeName;
    }

}