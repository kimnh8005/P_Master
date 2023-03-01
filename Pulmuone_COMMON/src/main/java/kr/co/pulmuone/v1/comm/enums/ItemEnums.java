package kr.co.pulmuone.v1.comm.enums;

import java.util.Arrays;
import java.util.List;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsColumnComment;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsEtcColumnComment;
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
 *  1.0    2020. 10. 20.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */
public class ItemEnums {

    // 품목 관련 return Message
    @Getter
    @RequiredArgsConstructor
    public enum Item implements MessageCommEnum {

        SEARCH_OPTION_NOT_MATCHED("SEARCH_OPTION_NOT_MATCHED", "매칭되는 검색옵션 없음") //
        , MASTER_ITEM_TYPE_NOT_MATCHED("MASTER_ITEM_TYPE_NOT_MATCHED", "매칭되는 마스터 품목 유형 없음") //
        , ERP_API_COMMUNICATION_FAILED("ERP_API_COMMUNICATION_FAILED", "ERP API 통신 실패") //
        , MORE_THAN_TWO_DATA_SEARCHED_BY_ITEM_CODE("MORE_THAN_TWO_DATA_SEARCHED_BY_ITEM_CODE", "품목 코드로 2건 이상의 데이터 조회됨") //
        , INVALID_ERP_ITEM_LINK_VALUE("INVALID_ERP_ITEM_LINK_VALUE", "부정확한 ERP 연동 여부") //
        , INVALID_ITEM_NUTRITION_DETAIL_ORDER("INVALID_ITEM_NUTRITION_DETAIL_ORDER", "부정확한 상품 영양정보 순서") //
        , PRICE_APPLY_START_DATE_NOT_EXIST("PRICE_APPLY_START_DATE_NOT_EXIST", "가격 정보 시작일 없음") //
        , STANDARD_PRICE_NOT_EXIST("STANDARD_PRICE_NOT_EXIST", "품목 원가 없음") //
        , RECOMMENDED_PRICE_NOT_EXIST("RECOMMENDED_PRICE_NOT_EXIST", "품목 정상가 없음") //
        , ERP_ITEM_UPDATE_INTERFACE_API_CALL_FAILED("ERP_ITEM_UPDATE_INTERFACE_API_CALL_FAILED", "ERP API 품목 조회 완료 API 통신 실패") //
        , ITEM_INFO_NOT_EXIST("ITEM_INFO_NOT_EXIST", "품목 정보가 존재하지 않습니다.") //
        , ITEM_REGIST("ITEM_REGIST", "등록완료") //
        , ITEM_O2O_EMPTY("ITEM_O2O_EMPTY", "매장품목유형 값이 없습니다.") //
        , ITEM_COMMON("ITEM_COMMON", "공통품목") //
        , ITEM_PRICE_EMPTY("ITEM_PRICE_EMPTY", "필수가격정보 누락") //
        , ITEM_SHOPONLY("ITEM_SHOPONLY", "매장전용품목") //
        , ITEM_020_NONE("ITEM_020_NONE", "미지정상품") //
        , ITEM_FOODMERCE_NONE("ITEM_FOODMERCE_NONE", "선택불가") //
        ;

        private final String code;
        private final String message;

    }

    // 품목/재고 관리 - 마스터 품목 관리 - 마스터 품목 등록 => ERP 품목 검색 팝업 상세검색 옵션
    @Getter
    @RequiredArgsConstructor
    public enum ErpItemSearchOption implements CodeCommEnum {

        SEARCH_BY_CODE("searchByCode", "ERP 품목코드로 검색") //
        , SEARCH_BY_BARCODE("searchByBarcode", "ERP 품목바코드로 검색") //
        , SEARCH_BY_NAME("searchByName", "ERP 품목명으로 검색") //

        ;

        public final String code;
        private final String codeName;

    }

    // 품목 이미지명의 사이즈별 prefix
    @Getter
    @RequiredArgsConstructor
    public enum ItemImagePrefixBySize implements CodeCommEnum { //

        PREFIX_640("PREFIX_640", "640*640 사이즈 파일명 Prefix", "640_", 640) // 640*640 사이즈 파일명 Prefix
        , PREFIX_320("PREFIX_320", "320*320 사이즈 파일명 Prefix", "320_", 320) // 320*320 사이즈 파일명 Prefix
        , PREFIX_216("PREFIX_216", "216*216 사이즈 파일명 Prefix", "216_", 216) // 216*216 사이즈 파일명 Prefix
        , PREFIX_180("PREFIX_180", "180*180 사이즈 파일명 Prefix", "180_", 180) // 180*180 사이즈 파일명 Prefix
        , PREFIX_75("PREFIX_75", "75*75 사이즈 파일명 Prefix", "75_", 75) // 75*75 사이즈 파일명 Prefix
        ;

        public final String code;
        private final String codeName;
        private final String prefix;
        private final int imageSize;

    }

    @Getter
    @RequiredArgsConstructor
    public enum MasterItemTypes implements CodeCommEnum {

        /*
         * 마스터 품목 유형
         */

        COMMON("MASTER_ITEM_TYPE.COMMON", "공통") //
        , SHOP_ONLY("MASTER_ITEM_TYPE.SHOP_ONLY", "매장") //
        , INCORPOREITY("MASTER_ITEM_TYPE.INCORPOREITY", "무형") //
        , RENTAL("MASTER_ITEM_TYPE.RENTAL", "렌탈") //
        ;

        public final String code;
        private final String codeName;

    }

    // 상품정보제공고시 상세 메시지 : IL_SPEC_FIELD 테이블의 SPEC_FIELD_CD ( 상품정보제공고시항목 코드 ) 에서 사용
    @Getter
    @RequiredArgsConstructor
    public enum SpecFieldCode implements CodeCommEnum {

        SPEC_FIELD_01( //
                "SPEC_FIELD_01" //
                , "제조연월일/유통기한 또는 품질유지기한" //
                , "수령일 기준 {*유통기간별 출고기한} 일 이내 제조(생산) 제품이 배달됩니다." //
                , Arrays.asList("{*유통기간별 출고기한}") //
                , true //
        ) //

        , SPEC_FIELD_02( //
                "SPEC_FIELD_02" //
                , "소비자상담 관련 전화번호" //
                , "{*소비자상담 관련 전화번호}" //
                , Arrays.asList("{*소비자상담 관련 전화번호}") //
                , false //
        ) //

        ;

        public final String code;
        private final String codeName; // 상품정보 제공고시 항목명

        private final String specFieldDetailMessage; // 상품정보 제공고시 기본 상세 메시지

        private final List<String> variableStringList; // 값으로 치환활 변수 문자열 목록

        // 화면에서 사용자가 해당 항목 수정 가능 여부
        // 값 true : 화면에서 사용자가 수정 가능 && 화면의 데이터에 따라 동적으로 수정 메시지 조회
        // 값 false : 수정 불가능 => 상품정보 제공고시 항목 조회시 수정 가능 여부 false 로 세팅됨
        private final boolean canModified;

        /*
         * 기본 상세 메시지에서 param 으로 받은 값 목록으로 치환된 수정 메시지 반환
         */
        public String getModifiedDetailMessage(List<String> valueList) {

            // 값으로 치환활 변수 문자열 목록과 param 으로 받은 변수 목록의 길이가 다른 경우 null 반환
            if (variableStringList.size() != valueList.size()) {
                return null;
            }

            String modifiedMessage = this.getSpecFieldDetailMessage(); // 수정된 메시지 : 최소 기본 상세 메시지로 세팅

            for (int i = 0; i < valueList.size(); i++) {

                // 변수 문자열 목록을 param 으로 받은 값으로 치환
                modifiedMessage = modifiedMessage.replace(this.getVariableStringList().get(i), valueList.get(i));

            }

            return modifiedMessage;

        }

    }

    // 배송불가지역 Enum : 공통코드 UNDELIVERABLE_AREA_TP, 배송불가지역 Response Dto 인 UndeliverableAreaResponseDto 클래스에 대응
    @Getter
    @RequiredArgsConstructor
    public enum UndeliverableAreaTypes implements CodeCommEnum {

        NONE("UNDELIVERABLE_AREA_TP.NONE", "없음"), // 배송불가지역 없음
        A1("UNDELIVERABLE_AREA_TP.A1", "도서산간(1권역)"), // 도서산간(1권역) 배송 불가
        A2("UNDELIVERABLE_AREA_TP.A2", "제주(2권역)"), // 제주(2권역) 배송 불가
        A1_A2("UNDELIVERABLE_AREA_TP.A1_A2", "1권역/2권역") // 1권역/2권역 모두 배송 불가
        ;

        private final String code;
        private final String codeName;

        /*
         * 품목 등록시 IL_ITEM 에 저장할 배송불가지역 코드값 정의 메서드
         */
        public static String getUndeliverableAreaTypeCode( //
                boolean islandShippingYn // 도서산간지역 (1권역) 배송여부 ( true : 배송가능 )
                , boolean jejuShippingYn // 제주지역 (2권역) 배송여부 ( true : 배송가능 )
        ) {

            if (islandShippingYn && jejuShippingYn) { // 1권역, 2권역 모두 배송 가능시

                return NONE.getCode();

            } else if (islandShippingYn) { // 1권역 배송 가능, 2권역 배송 불가

                return A2.getCode();

            } else if (jejuShippingYn) { // 1권역 배송 불가, 2권역 배송 가능

                return A1.getCode();

            } else { // 1권역, 2권역 모두 배송 불가

                return A1_A2.getCode();

            }

        }

    }

    @Getter
    @RequiredArgsConstructor
    public enum ItemPoReqeust implements MessageCommEnum {

        /*
         * 마스터 품목 유형
         */

        GOODS_VALUE_NONE("ItemPoReqeust.IL_GOODS_ID_NONE", "상품코드값없음") //
        , GOODS_ID_EMPTY("ItemPoReqeust.GOODS_ID_EMPTY", "상품정보없음") //
        , GOODS_ID_FORMAT_ERROR("ItemPoReqeust.GOODS_ID_FORMAT_ERROR", "상품코드 형식 오류") //
        , PO_EVENT_QTY_NONE("ItemPoReqeust.PO_EVENT_QTY_NONE", "행사발주수량값 없음") //
        , PO_EVENT_QTY_MIN_ERROR("ItemPoReqeust.PO_EVENT_QTY_MIN_ERROR", "행사발주수량값 음수 오류") //
        , PO_EVENT_QTY_PARSE_ERROR("ItemPoReqeust.PO_EVENT_QTY_PARSE_ERROR", "행사발주수량값 형식 오류") //
        , PO_SCHEDULE_DT_NONE("ItemPoReqeust.PO_SCHEDULE_DT_NONE", "발주예정일 값 없음") //
        , PO_SCHEDULE_DT_MIN_ERROR("ItemPoReqeust.PO_SCHEDULE_DT_MIN_ERROR", "발주예정일 값 오류") //
        , PO_SCHEDULE_DT_FORMAT_ERROR("ItemPoReqeust.PO_SCHEDULE_DT_FORMAT_ERROR", "발주예정일 형식 오류") //
        , RECEVING_REQ_DT_NONE("ItemPoReqeust.RECEVING_REQ_DT_NONE", "입고요청일 값 없음") //
        , RECEVING_REQ_DT_MIN_ERROR("ItemPoReqeust.RECEVING_REQ_DT_MIN_ERROR", "입고요청일 값 오류") //
        , RECEVING_REQ_DT_FORMAT_ERROR("ItemPoReqeust.RECEVING_REQ_DT_FORMAT_ERROR", "입고요청일 형식 오류") //
        , RECEVING_REQ_DT_DAY_OF_WEEK_ERROR("ItemPoReqeust.RECEVING_REQ_DT_DAY_OF_WEEK_ERROR", "입고요청일 날짜 안맞음") //
        , EVENT_START_DT_NONE("ItemPoReqeust.EVENT_START_DT_NONE", "행사시작일 값 없음") //
        , EVENT_START_DT_MIN_ERROR("ItemPoReqeust.EVENT_START_DT_MIN_ERROR", "행사시작일 값 오류") //
        , EVENT_START_DT_FORMAT_ERROR("ItemPoReqeust.EVENT_START_DT_FORMAT_ERROR", "행사시작일 형식 오류") //
        , EVENT_END_DT_NONE("ItemPoReqeust.EVENT_END_DT_NONE", "행사종료일 값 없음") //
        , EVENT_END_DT_MIN_ERROR("ItemPoReqeust.EVENT_END_DT_MIN_ERROR", "행사종료일 값 오류") //
        , EVENT_END_DT_FORMAT_ERROR("ItemPoReqeust.EVENT_END_DT_FORMAT_ERROR", "행사종료일 형식 오류") //
        , EVENT_START_DT_LATER_THAN_END_DT("ItemPoReqeust.EVENT_START_DT_LATER_THAN_END_DT", "행사시작일이 행사종료일보다 늦음") //
        , SELLERS_ID_EMPTY("ItemPoReqeust.SELLERS_ID_EMPTY", "판매처코드값 없음") //
        , SELLERS_ID_NONE("ItemPoReqeust.SELLERS_ID_NONE", "판매처코드 정보 없음") //
        ;

    	private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PoDay implements CodeCommEnum {

    	 PO_DAY_MONDAY("PO_DAY.MONDAY", "월")
        ,PO_DAY_TUESDAY("PO_DAY.TUESDAY", "화")
        ,PO_DAY_WEDNESDAY("PO_DAY.WEDNESDAY", "수")
        ,PO_DAY_THURSDAY("PO_DAY.THURSDAY", "목")
        ,PO_DAY_FRIDAY("PO_DAY.FRIDAY", "금")
        ,PO_DAY_SATURDAY("PO_DAY.SATURDAY", "토")
        ,PO_DAY_SUNDAY("PO_DAY.SUNDAY", "일")
    	;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PoType implements CodeCommEnum {

    	PO_TYPE_PRODUCTION("PO_TYPE.PRODUCTION", "생산발주")
        ;

        private final String code;
        private final String codeName;
    }

    // 품목마스터(IL_ITEM) 변경내역 기준컬럼 및 컬럼COMMENT
    @Getter
    @RequiredArgsConstructor
    public enum ItemColumnComment implements CodeCommEnum{
    	ERP_ITEM_NM("erpItemNm", "ERP 품목명"),
    	ITEM_BARCODE("itemBarcode", "BOS 품목 바코드"),
    	ERP_ITEM_BARCODE("erpItemBarCode", "ERP 품목 바코드"),
    	ITEM_NM("itemNm", "마스터 품목명"),
    	ERP_CTGRY_LV1_ID("erpCategoryLevel1Id", "ERP 카테고리 - 대분류"),
    	ERP_CTGRY_LV2_ID("erpCategoryLevel2Id", "ERP 카테고리 - 중분류"),
    	ERP_CTGRY_LV3_ID("erpCategoryLevel3Id", "ERP 카테고리 - 소분류"),
    	ERP_CTGRY_LV4_ID("erpCategoryLevel4Id", "ERP 카테고리 - 세분류"),
    	CATEGORY_STANDARD_FIRST_ID("bosCategoryStandardFirstId", "표준 카테고리 - 대분류"),
    	CATEGORY_STANDARD_SECOND_ID("bosCategoryStandardSecondId", "표준 카테고리 - 중분류"),
    	CATEGORY_STANDARD_THIRD_ID("bosCategoryStandardThirdId", "표준 카테고리 - 소분류"),
    	CATEGORY_STANDARD_FOURTH_ID("bosCategoryStandardFourthId", "표준 카테고리 - 세분류"),
    	UR_SUPPLIER_ID("urSupplierId", "공급업체"),
    	ITEM_GRP("bosItemGroup", "BOS 상품군"),
    	ERP_STORAGE_METHOD("erpStorageMethod", "ERP 공급업체"),
    	STORAGE_METHOD_TP("storageMethodType", "BOS 공급업체"),
    	ORIGIN_TP("originType", "BOS 원산지"),
    	ORIGIN_DETL("originDetail", "BOS 원산지상세"),
    	ERP_ORIGIN_NM("erpOriginName", "ERP 원산지"),
    	DISTRIBUTION_PERIOD("distributionPeriod", "BOS 유통기간"),
    	ERP_DISTRIBUTION_PERIOD("erpDistributionPeriod", "ERP 유통기간"),
    	BOX_WIDTH("boxWidth", "BOS 박스체적 - 가로"),
    	BOX_DEPTH("boxDepth", "BOS 박스체적 - 세로"),
    	BOX_HEIGHT("boxHeight", "BOS 박스체적 - 높이"),
    	ERP_BOX_WIDTH("erpBoxWidth", "ERP 박스체적 - 가로"),
    	ERP_BOX_DEPTH("erpBoxDepth", "ERP 박스체적 - 세로"),
    	ERP_BOX_HEIGHT("erpBoxHeight", "ERP 박스체적 - 높이"),
        ITEM_DISP_WEIGHT("itemDispHeight", "단품표시중량"),
        ITEM_REAL_HEIGHT("itemRealHeight", "단품실중량"),
    	PCS_PER_BOX("piecesPerBox", "BOS 박스입수량"),
    	ERP_PCS_PER_BOX("erpPiecesPerBox", "ERP 박스입수량"),
    	OMS("unitOfMeasurement", "ERP 박스입수량"),
    	SIZE_PER_PACKAGE("sizePerPackage", "포장단위별 용량"),
    	SIZE_UNIT("sizeUnit", "용량단위"),
    	SIZE_UNIT_ETC("sizeUnitEtc", "용량단위 (기타)"),
    	PDM_GROUP_CD("pdmGroupCode", "PDM 그룹코드"),
    	TAX_YN("taxYn", "과세구분"),
    	NUTRITION_DISP_YN("nutritionDisplayYn", "영양정보 표시여부(Y:표시)"),
    	NUTRITION_DISP_DEFAULT("nutritionDisplayDefalut", "영양정보 기본정보(NUTRITION_DISP_Y 값이 N일때 노출되는 항목)"),
    	EXTINCTION_YN("extinctionYn", "단종여부"),
    	RETURN_PERIOD("returnPeriod", "반품가능기간"),
    	ERP_CAN_PO_YN("erpCanPoYn", "ERP 발주가능여부"),
    	UNDELIVERABLE_AREA_TP("undeliverableAreaType", "배송불가지역"),
    	VIDEO_URL("videoUrl", "동영상 URL"),
    	VIDEO_AUTOPLAY_YN("videoAutoplayYn", "비디오 자동재생 유무")
    	;

    	private final String code;
    	private final String codeName;

    	public static ItemColumnComment findByComment(String code) {
    		return Arrays.stream(ItemColumnComment.values())
    				.filter(itemColumnComment -> itemColumnComment.getCode().equals(code))
    				.findAny()
    				.orElse(null);
    	}
    }

    // 품목 마스터 이외(상품정보 제공고고 등) 변경내역 기준(테이블명, 컬럼, 컬럼COMMENT)
    @Getter
    @RequiredArgsConstructor
    public enum ItemEtcColumnComment implements ChangeLogEnum{

    	// 상품정보 제공고시
    	IL_ITEM_SPEC("IL_ITEM_SPEC_VALUE", "ilSpecFieldId", "specFieldValue", "상품정보제공고시항목 PK"),
    	// 영양정보 - 단위정보
    	IL_ITEM_NUTRITION_UNIT("IL_ITEM_NUTRITION_UNIT", "nutritionCode", "nutritionUnit", "영양성분 단위"),
    	// 영양정보 - 분류명
    	IL_ITEM_NUTRITION_NM("IL_ITEM_NUTRITION_NM", "nutritionCode", "nutritionCodeName", "영양성분명"),

    	// 인증정보
    	IL_ITEM_CERTIFICATION("IL_ITEM_CERTIFICATION", "ilCertificationId", "certificationDescription", "인증정보"),

    	// 인증정보
    	IL_ITEM_WAREHOUSE("IL_ITEM_WAREHOUSE", "urSupplierWarehouseId", "urSupplierWarehouseId", "공급처_출고처 PK");

    	private final String tableName;
    	private final String idColumn;
    	private final String dataColumn;
    	private final String comment;

    	public static ItemEtcColumnComment findByInfo(String tableName) {
    		return Arrays.stream(ItemEtcColumnComment.values())
    				.filter(itemEtcColumnComment -> itemEtcColumnComment.getTableName().equals(tableName))
    				.findAny()
    				.orElse(null);
    	}
    }

    @Getter
    @RequiredArgsConstructor
    public enum ItemApprProcStatus implements MessageCommEnum {
    	APPR_DUPLICATE                            ("1000", "이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
    	CLIENT_APPR_DUPLICATE                     ("1001", "거래처에서 이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
    	NONE_ITEM_CD                              ("1999", "품목이 존재하지 않습니다."),
    	NOT_DIFFERENT_ITEM                        ("1005", "변경된 품목 내역이 없습니다."),
    	NONE_APPR_USERS                           ("1006", "승인관리자 정보가 없습니다."),
    	CLINET_APPR_REQUEST                       ("1007", "승인요청 되었습니다."),
    	NONE_APPR                                 ("2000", "승인 관리자 정보가 존재하지 않습니다. 승인 관리자를 추가해 주세요."),
    	ADMIN_DIFFERENT_ITEM                      ("2001", "업데이트 된 내용이 존재하여, 상세정보를 다시 불러옵니다. 수정된 정보 확인 후 재 등록바랍니다.")
        ;

    	private final String code;
    	private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ItemStatusTp implements MessageCommEnum {
    	SAVE                                    ("ITEM_STATUS_TP.SAVE", "저장"),
    	REGISTER                                ("ITEM_STATUS_TP.REGISTER", "등록"),
    	DISPOSAL                                ("ITEM_STATUS_TP.DISPOSAL", "폐기")
        ;

    	private final String code;
    	private final String message;
    }

    //가격 관리 유형
    @Getter
    @RequiredArgsConstructor
    public enum priceManageType implements CodeCommEnum {
        A("A", "원가/정상가"), // 원가/정상가 모두 관리자가 관리
        R("R", "정상가"), // 원가는 ERP, 정상가는 관리자
        N("N", "없음"); // 원가/정상가 모두 ERP가격을 사용

        private final String code;
        private final String codeName;
    }

}
