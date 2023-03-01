package kr.co.pulmuone.v1.comm.constants;

import java.util.Arrays;
import java.util.List;

public final class DisplayConstants {
    private DisplayConstants() {
    }

    //공통
    public static final String CONTENTS_LEVEL1 = "1";                          // 공통 - 전시컨텐츠 레벨1
    public static final String CONTENTS_LEVEL2 = "2";                          // 공통 - 전시컨텐츠 레벨2
    public static final String CONTENTS_LEVEL3 = "3";                          // 공통 - 전시컨텐츠 레벨3

    //MALL
    public static final String NOW_SALE_CODE = "21-Main-Nowsale-Gds";           // 지금 할인하는상품 에서만 구매개수 조회
    public static final String NOW_SALE_ORDER_CNT_HIDDEN = "NOW_SALE_ORDER_CNT_HIDDEN.Y";           // 지금 할인하는상품 구매개수 숨김 여부(ST_COMN_CODE_ID = 'NOW_SALE_ORDER_CNT_HIDDEN.Y')
    public static final String LOHAS_BANNER_CODE = "21-LOHAS-Billboard-Bn";     // Lohas 배너목록 조회
    public static final String MAIN_BILLBOARD = "21-Main-Billboard-Bn";         // 메인빌보드 예외처리 대상
    public static final String KEYWORDS = "21-KeyWords";                        //요즘 많이 찾는 상품  - 페이지 코드
    public static final String KEYWORDS_GOODS = "21-Keywords-List-Gds";         //요즘 많이 찾는 상품 목록 - 인벤토리 코드
    public static final String BRAND_INVENTORY_CODE = "21-Main-BrandList-Gds";  //메인 브랜드 목록
    public static final String ORGA_STORE_INVENTORY_CODE = "21-OShopD-mainb";   //올가 전용관 배너 정보
    public static final String PAGE_CODE_PULMUONE_GNB = "21-GNB";               // 샵풀무원 GNB
    public static final String INVENTORY_CODE_KEYWORD = "21-GNB-ADSearch-Txt";  // 샵풀무원 GNB - 프모모션 키워드
    public static final String PAGE_CODE_ORGA_GNB = "21-OGNB";                  // 올가 GNB
    public static final String PAGE_CODE_ORGA_MAIN = "21-OMain";                // 올가 MAIN
    public static final String PAGE_CODE_ORGA_FLYER = "21-OLeaflet";                // 올가 전단행사
    public static final String PAGE_CODE_ORGA_FLYER_GDS = "21-OLeaflet-List-Gds";   // 올가 전단행사 상품
    public static final String PAGE_CODE_ORGA_FLYER_ALL_TITLE = "전체";   // 올가 전단행사 전체브랜드 명

    public static final String SHOP_LOGO_IMAGE_FILE_PATH_KEY = "SHOP_LOGO_IMAGE_FILE_PATH"; // 파일경로
    public static final String SHOP_LOGO_IMAGE_FILE_NAME_KEY = "SHOP_LOGO_IMAGE_FILE_NAME"; // 파일명

    public static final String SHOP_LOGO_DETAIL_IMAGE_FILE_PATH_KEY = "SHOP_LOGO_DETAIL_IMAGE_FILE_PATH";   //이미지 파일경로
    public static final String SHOP_LOGO_DETAIL_IMAGE_FILE_NAME_KEY = "SHOP_LOGO_DETAIL_IMAGE_FILE_NAME";   //이미지 파일명

    //BATCH
    public static final int BATCH_CONTENTS_SORT_DEFAULT = 500;              // BATCH - 공통 - 정렬 순번 시작
    public static final String BATCH_TITLE_NAME = "자동상품";                 // BATCH - 공통 - 전시컨텐츠 입력용 타이틀이름

    public static final String BATCH_BRAND_INVENTORY_CODE = "21-Main-BrandList-Gds";    //BATCH - 샵풀무원 - 브랜드 인벤토리 코드
    public static final int BATCH_BRAND_LIMIT = 20;                                     //BATCH - 샵풀무원 - 브랜드 상품 등록 수량

    public static final String BATCH_NOW_SALE_INVENTORY_CODE = "21-Nowsale-ADGds-Gds";  //BATCH - 샵풀무원 - 지금세일 인벤토리 코드
    public static final int BATCH_NOW_SALE_LIMIT = 20;                                  //BATCH - 샵풀무원 - 지금세일 상품 등록 수량

    public static final String BATCH_LOHAS_INVENTORY_CODE = "21-LOHAS-List-Gds";        //BATCH - 샵풀무원 - 로하스 인벤토리 코드
    public static final int BATCH_LOHAS_LIMIT = 16;                                     //BATCH - 샵풀무원 - 로하스 상품 등록 수량

    public static final String BATCH_ORGA_MAIN_NEW_INVENTORY_CODE = "21-OMain-New-Gds"; //BATCH - 올가메인 - 신상품 인벤토리 코드
    public static final int BATCH_ORGA_MAIN_NEW_LIMIT = 20;                             //BATCH - 올가메인 - 신상품 상품 등록 수량

    public static final String BATCH_ORGA_MAIN_PB_INVENTORY_CODE = "21-OMain-21-OPB-Gds";   //BATCH - 올가메인 - PB 인벤토리 코드
    public static final int BATCH_ORGA_MAIN_PB_LIMIT = 12;                                  //BATCH - 올가메인 - PB 상품 등록 수량

    public static final String BATCH_ORGA_MAIN_DIRECT_INVENTORY_CODE = "21-OMain-DirectDelv-Gds";   //BATCH - 올가메인 - 산지직송 인벤토리 코드
    public static final int BATCH_ORGA_MAIN_DIRECT_LIMIT = 16;                                      //BATCH - 올가메인 - 산지직송 상품 등록 수량
    public static final long BATCH_CATEGORY_FRUIT = 4893;                                           //BATCH - 산지직송 과일 대 카테고리 PK
    public static final long BATCH_CATEGORY_VEGETABLE = 5080;                                       //BATCH - 산지직송 야채 대 카테고리 PK
    public static final long BATCH_CATEGORY_FISH = 5136;                                            //BATCH - 산지직송 수산 대 카테고리 PK
    public static final long BATCH_CATEGORY_BEEF = 5135;                                            //BATCH - 산지직송 정육 대 카테고리 PK

    public static final String BATCH_ORGA_PB_INVENTORY_CODE = "21-OPB-List-Gds";                //BATCH - 올가 - PB 인벤토리 코드
    public static final int BATCH_ORGA_PB_LIMIT = 100;                                          //BATCH - 올가 - PB 상품 등록 수량

    public static final String BATCH_ORGA_DIRECT_INVENTORY_CODE = "21-ODirectDelv-List-Gds";    //BATCH - 올가 - 산지직송 인벤토리 코드
    public static final int BATCH_ORGA_DIRECT_LIMIT = 100;                                      //BATCH - 올가 - 산지직송 상품 등록 수량

}
