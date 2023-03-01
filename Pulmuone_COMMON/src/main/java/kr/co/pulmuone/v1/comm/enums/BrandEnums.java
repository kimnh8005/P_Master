package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Java 에서 코드성 값을 사용해야 할때 여기에 추가해서 사용
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 7. 15.                jg          최초작성
 * =======================================================================
 * </PRE>
 */
public class BrandEnums {

    // 브랜드 파일 타입
    @Getter
    @RequiredArgsConstructor
    public enum BrandImageType implements CodeCommEnum {

        PC_MAIN("BRAND_LOGO_TYPE.PC_MAIN","브랜드 로고 PC 메인"),
        PC_MAIN_OVER("BRAND_LOGO_TYPE.PC_MAIN_OVER","브랜드 로고 PC 메인 Over"),
        PC_NO_COLOR("BRAND_LOGO_TYPE.PC_NO_COLOR","브랜드 로고 PC 흑백"),
        MOBILE_MAIN("BRAND_LOGO_TYPE.MOBILE_MAIN","브랜드 로고 Mobile 메인"),
        MOBILE_MAIN_OVER("BRAND_LOGO_TYPE.MOBILE_MAIN_OVER","브랜드 로고 Mobile 메인 Over"),
        MOBILE_NO_COLOR("BRAND_LOGO_TYPE.MOBILE_NO_COLOR","브랜드 로고 Mobile 흑백"),
    	TITLE_BANNER_PC("BRAND_LOGO_TYPE.TITLE_BANNER_PC","타이틀 배너 PC"),
    	TITLE_BANNER_MOBILE("BRAND_LOGO_TYPE.TITLE_BANNER_MOBILE","타이틀 배너 MOBILE");

        private final String code;
        private final String codeName;
    }

    //브랜드 로고 파일 구분
    @Getter
    @RequiredArgsConstructor
    public enum BrandLogoFileType implements CodeCommEnum {
        PC_MAIN_FILE("filePcMain","PC 브랜드 로고 메인 파일"),
        PC_MAIN_OVER_FILE("filePcMainOver","PC 브랜드 로고 메인 Over 파일"),
        PC_NO_COLOR_FILE("filePcCategory","PC 브랜드 로고 카테고리 파일"),
        MOBILE_MAIN_FILE("fileMobileMain","모바일 브랜드 로고 메인 파일"),
        MOBILE_MAIN_OVER_FILE("fileMobileMainOver","모바일 브랜드 로고 메인 Over 파일"),
        MOBILE_NO_COLOR_FILE("fileMobileCategory","모바일 브랜드 로고 카테고리 파일"),
    	TITLE_BANNER_PC_FILE("fileTitleBannerPc","타이틀 배너 PC 파일"),
    	TITLE_BANNER_MOBILE_FILE("fileTitleBannerMobile","타이틀 배너 Mobile 파일");

        private final String code;
        private final String codeName;
    }
}
