package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class DisplayEnums {

    //컨텐츠 유형
    @Getter
    @RequiredArgsConstructor
    public enum ContentsType implements CodeCommEnum {
        TEXT("DP_CONTENTS_TP.TEXT", "TEXT"),
        HTML("DP_CONTENTS_TP.HTML", "HTML"),
        BANNER("DP_CONTENTS_TP.BANNER", "BANNER"),
        CATEGORY("DP_CONTENTS_TP.CATEGORY", "CATEGORY"),
        BRAND("DP_CONTENTS_TP.BRAND", "BRAND"),
        GOODS("DP_CONTENTS_TP.GOODS", "GOODS"),
        NONE("DP_CONTENTS_TP.NONE", "NONE")
        ;

        private final String code;
        private final String codeName;
    }

    //유저 유형
    @Getter
    @RequiredArgsConstructor
    public enum UserType implements CodeCommEnum {
        NONE("NONE", "비회원"),
        NORMAL("NORMAL", "회원"),
        EMPLOYEE("EMPLOYEE", "임직원")
        ;

        private final String code;
        private final String codeName;
    }

    //디바이스 유형
    @Getter
    @RequiredArgsConstructor
    public enum DeviceType implements CodeCommEnum {
        PC("PC", "PC"),
        MOBILE("MOBILE", "MOBILE"),
        APP("APP", "APP")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum LayoutMessageEnums implements MessageCommEnum {
        MALL_CATEGORY_DATA_EMPTY("MALL_CATEGORY_DATA_EMPTY", "몰 카테고리 데이터가 없습니다.")
        ;

        private final String code;
        private final String message;
    }

    // 전시코너유형
    @Getter
    @RequiredArgsConstructor
    public enum DpCornerTp implements CodeCommEnum {
        PAGE      ("DP_CONRNER_TP.PAGE"     , "페이지코너별")
      , CATEGORY  ("DP_CONRNER_TP.CATEGORY" , "카테고리코너별")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시페이지유형
    @Getter
    @RequiredArgsConstructor
    public enum PageTp implements CodeCommEnum {
        PAGE      ("PAGE_TP.PAGE"     , "페이지")
      , CATEGORY  ("PAGE_TP.CATEGORY" , "카테고리")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시페이지검색유형
    @Getter
    @RequiredArgsConstructor
    public enum PageSchTp implements CodeCommEnum {
        INVENTORY ("PAGE_SCH_TP.INVENTORY", "페이지별인벤토리검색")
      , GROUP     ("PAGE_SCH_TP.GROUP"    , "페이지별그룹검색")
      ;
      private final String code;
      private final String codeName;
    }

    // 전시인벤토리검색유형
    @Getter
    @RequiredArgsConstructor
    public enum DpInventorySearchTp implements CodeCommEnum {
      CORNER  ("DP.INVENTORY_SEARCH_TP.CORNER" , "코너별")
      , GROUP   ("DP.INVENTORY_SEARCH_TP.GROUP"  , "그룹별")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시상품검색유형
    @Getter
    @RequiredArgsConstructor
    public enum DpGoodsSearchTp implements CodeCommEnum {
        ERP_ITEM_CD   ("DP_GOODS_SEARCH_TP.ERP_ITEM_CD"  , "ERP품목코드")
      , ITEM_BARCODE  ("DP_GOODS_SEARCH_TP.ITEM_BARCODE" , "품목바코드")
      , GOODS_CD      ("DP_GOODS_SEARCH_TP.GOODS_CD"     , "상품코드")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시대상유형
    @Getter
    @RequiredArgsConstructor
    public enum DpTargetTp implements CodeCommEnum {
        ALL       ("DP_TARGET_TP.ALL"      , "전체")
      , NORMAL    ("DP_TARGET_TP.NORMAL"   , "일반")
      , EMPLOYEE  ("DP_TARGET_TP.EMPLOYEE" , "입직원")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시범위유형
    @Getter
    @RequiredArgsConstructor
    public enum DpRangeTp implements CodeCommEnum {
        ALL     ("DP_RANGE_TP.ALL"      , "전체")
      , PC    	("DP_RANGE_TP.PC"   	, "pc")
      , MOBILE  ("DP_RANGE_TP.MOBILE" 	, "web/app")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시팝업유형
    @Getter
    @RequiredArgsConstructor
    public enum DpPopupTp implements CodeCommEnum {
        HTML    ("DP_POPUP_TP.HTML"   , "HTML")
      , IMAGE   ("DP_POPUP_TP.IMAGE"  , "IMAGE")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시파일유형
    @Getter
    @RequiredArgsConstructor
    public enum DpFileTp implements CodeCommEnum {
        CONTENTS  ("DP_FILE_TP.CONTENTS"   , "HTML")
      , POPUP     ("DP_FILE_TP.POPUP"  , "IMAGE")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시컨텐츠유형
    @Getter
    @RequiredArgsConstructor
    public enum DpContentsTp implements CodeCommEnum {
        NONE      ("DP_CONTENTS_TP.CONTENTS" , "없음")
      , TEXT      ("DP_CONTENTS_TP.TEXT"     , "텍스트")
      , HTML      ("DP_CONTENTS_TP.HTML"     , "HTML")
      , BANNER    ("DP_CONTENTS_TP.BANNER"   , "배너")
      , BRAND     ("DP_CONTENTS_TP.BRAND"    , "브랜드")
      , CATEGORY  ("DP_CONTENTS_TP.CATEGORY" , "카테고리")
      , GOODS     ("DP_CONTENTS_TP.GOODS"    , "상품")
      ;

      private final String code;
      private final String codeName;
    }

    // 전시노출조건유형
    @Getter
    @RequiredArgsConstructor
    public enum DpCondTp implements CodeCommEnum {
        ALL         ("DP_CONTDl_TP.ALL"        , "전체")
      , DIRECT_REG  ("DP_CONTDl_TP.DIRECT_REG" , "직접등록")
      , AUTO_REG    ("DP_CONTDl_TP.AUTO_REG"   , "자동")
      ;

      private final String code;
      private final String codeName;
    }

    //sort code
    @Getter
    @RequiredArgsConstructor
    public enum GoodsSortCode implements CodeCommEnum {
        DEFAULT("DEFAULT", "기본값"),
        NEW("NEW", "신상품순"),
        LOW_PRICE("LOW_PRICE", "낮은가격순"),
        HIGH_PRICE("HIGH_PRICE", "높은가격순"),
        POPULARITY("POPULARITY", "인기순"),
        EMPLOYEE_LOW_PRICE("EMPLOYEE_LOW_PRICE", "임직원가_낮은가격순"),
        EMPLOYEE_HIGH_PRICE("EMPLOYEE_HIGH_PRICE", "임직원가_높은가격순")
        ;

        private final String code;
        private final String codeName;
    }

    //DP_COND_TP
    @Getter
    @RequiredArgsConstructor
    public enum DpCondType implements CodeCommEnum {
        AUTO("DP_COND_TP.AUTO", "자동"),
        MANUAL("DP_COND_TP.MANUAL", "수동")
        ;

        private final String code;
        private final String codeName;
    }

    //DP_SORT_TP
    @Getter
    @RequiredArgsConstructor
    public enum DpSortType implements CodeCommEnum {
        MANUAL("DP_SORT_TP.MANUAL", "직접등록"),
        BEST("DP_SORT_TP.BEST", "인기순"),
        NEW("DP_SORT_TP.NEW", "신상품순")
        ;

        private final String code;
        private final String codeName;
    }

}
