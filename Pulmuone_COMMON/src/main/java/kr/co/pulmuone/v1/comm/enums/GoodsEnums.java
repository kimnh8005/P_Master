package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

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
public class GoodsEnums {

    // 상품
    @Getter
    @RequiredArgsConstructor
    public enum Goods implements MessageCommEnum {
    	NO_GOODS("NO_GOODS", "상품정보 없음"),
    	LIMIT_DISPLAY("LIMIT_DISPLAY", "판매 허용범위 제한 상품"),
    	LIMIT_PURCHASE("LIMIT_PURCHASE", "구매 허용범위 제한 상품"),
    	NOT_VIEW_GOODS_TYPE("NOT_VIEW_GOODS_TYPE", "상세접근 불가 상품")
    	;

        private final String code;
        private final String message;
    }

    // 판매유형
    @Getter
    @RequiredArgsConstructor
    public enum SaleType implements CodeCommEnum{
    	SHOP("SALE_TYPE.SHOP", "매장"),
    	DAILY("SALE_TYPE.DAILY", "일일"),
    	NORMAL("SALE_TYPE.NORMAL", "일반"),
    	REGULAR("SALE_TYPE.REGULAR", "정기"),
    	RESERVATION("SALE_TYPE.RESERVATION", "예약"),
    	NOT_FOR_SALE("SALE_TYPE.NOT_FOR_SALE", "비매품")
    	;

        private final String code;
        private final String codeName;

    }

    // 판매상태
    @Getter
    @RequiredArgsConstructor
    public enum SaleStatus implements CodeCommEnum{
    	SAVE("SALE_STATUS.SAVE", "저장"),
    	WAIT("SALE_STATUS.WAIT", "판매대기"),
    	ON_SALE("SALE_STATUS.ON_SALE", "판매중"),
    	STOP_SALE("SALE_STATUS.STOP_SALE", "판매중지"),
    	STOP_PERMANENT_SALE("SALE_STATUS.STOP_PERMANENT_SALE", "영구판매중지"),
    	OUT_OF_STOCK_BY_SYSTEM("SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM", "품절(시스템)"),
    	OUT_OF_STOCK_BY_MANAGER("SALE_STATUS.OUT_OF_STOCK_BY_MANAGER", "품절(관리자)")
    	;

        private final String code;
        private final String codeName;

        public static SaleStatus findByCode(String code) {
            return Arrays.stream(SaleStatus.values())
                    .filter(SaleStatus -> SaleStatus.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 외부몰 판매상태
    @Getter
    @RequiredArgsConstructor
    public enum GoodsOutmallSaleStat implements CodeCommEnum{
        SAVE("GOODS_OUTMALL_SALE_STAT.NONE", "해당없음"),
        ON_SALE("GOODS_OUTMALL_SALE_STAT.ON_SALE", "판매중"),
        STOP_SALE("GOODS_OUTMALL_SALE_STAT.STOP_SALE", "판매중지"),
        ;

        private final String code;
        private final String codeName;

        public static GoodsOutmallSaleStat findByCode(String code) {
            return Arrays.stream(GoodsOutmallSaleStat.values())
                    .filter(GoodsOutmallSaleStat -> GoodsOutmallSaleStat.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 판매유형
    @Getter
    @RequiredArgsConstructor
    public enum GoodsType implements CodeCommEnum{
    	ADDITIONAL("GOODS_TYPE.ADDITIONAL", "추가"),
    	DAILY("GOODS_TYPE.DAILY", "일일"),
    	DISPOSAL("GOODS_TYPE.DISPOSAL", "폐기임박"),
    	GIFT("GOODS_TYPE.GIFT", "증정"),
    	INCORPOREITY("GOODS_TYPE.INCORPOREITY", "무형"),
    	NORMAL("GOODS_TYPE.NORMAL", "일반"),
    	PACKAGE("GOODS_TYPE.PACKAGE", "묶음"),
    	RENTAL("GOODS_TYPE.RENTAL", "렌탈"),
    	SHOP_ONLY("GOODS_TYPE.SHOP_ONLY", "매장전용"),
        GIFT_FOOD_MARKETING("GOODS_TYPE.GIFT_FOOD_MARKETING", "증정(식품마케팅)")
    	;

        private final String code;
        private final String codeName;

        public static GoodsType findByCode(String code) {
            return Arrays.stream(GoodsType.values())
                    .filter(GoodsType -> GoodsType.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }

    }

    // 배송유형
    @Getter
    @RequiredArgsConstructor
    public enum GoodsDeliveryType implements CodeCommEnum{
        NORMAL("GOODS_DELIVERY_TYPE.NORMAL", "일반"),
        DAWN("GOODS_DELIVERY_TYPE.DAWN", "새벽"),
        SHOP("GOODS_DELIVERY_TYPE.SHOP","매장"),
        DAILY("GOODS_DELIVERY_TYPE.DAILY","일일"),
        REGULAR("GOODS_DELIVERY_TYPE.REGULAR","정기"),
        RESERVATION("GOODS_DELIVERY_TYPE.RESERVATION","예약"),
        NO_DELIVERY("GOODS_DELIVERY_TYPE.NO_DELIVERY","배송안함"),
        ;

        private final String code;
        private final String codeName;
    }

    // 배송타입
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryType implements CodeCommEnum{
        NORMAL("NORMAL", "일반"),
        DAWN("DAWN", "새벽"),
        DAILY("DAILY","일일"),
        SHOP("SHOP", "매장")
        ;

        private final String code;
        private final String codeName;

    }


    // 조건배송비구분
    @Getter
    @RequiredArgsConstructor
    public enum ConditionType implements CodeCommEnum{
        TYPE1("CONDITION_TYPE.1", "무료배송비"),
        TYPE2("CONDITION_TYPE.2", "고정배송비"),
        TYPE3("CONDITION_TYPE.3", "결제금액별배송비"),
        TYPE4("CONDITION_TYPE.4", "수량별배송비"),
        TYPE5("CONDITION_TYPE.5", "상품1개단위별배송비")
        ;

        private final String code;
        private final String codeName;

    }


    // 일일상품 유형
    @Getter
    @RequiredArgsConstructor
    public enum GoodsDailyType implements CodeCommEnum{
    	BABYMEAL("GOODS_DAILY_TP.BABYMEAL", "베이비밀"),
    	EATSSLIM("GOODS_DAILY_TP.EATSSLIM", "잇슬림"),
    	GREENJUICE("GOODS_DAILY_TP.GREENJUICE", "풀무원 녹즙")
        ;

        private final String code;
        private final String codeName;

    }

    // 상품가격처리구분
    @Getter
    @RequiredArgsConstructor
    public enum GoodsPriceProcType implements CodeCommEnum{
      ITEM ("GOODS_PRICE_PROC_TP.ITEM" , "품목"),
      GOODS("GOODS_PRICE_PROC_TP.GOODS", "상품")
      ;
      private final String code;
      private final String codeName;
    }

    // 상품가격호출구분
    @Getter
    @RequiredArgsConstructor
    public enum GoodsPriceCallType implements CodeCommEnum{
      BATCH ("GOODS_PRICE_CALL_TP.BATCH" , "배치"),
      ONLINE("GOODS_PRICE_CALL_TP.ONLINE", "온라인")
      ;
      private final String code;
      private final String codeName;
    }

    // 묶음상품 이미지 유형
    @Getter
    @RequiredArgsConstructor
    public enum GoodsPackageImageType implements CodeCommEnum{
    	MIXED("GOODS_PACKAGE_IMG_TP.MIXED", "묶음/개별상품 조합"),
    	NORMAL_GOODS("GOODS_PACKAGE_IMG_TP.NORMAL_GOODS", "개별상품 전용"),
    	PACKAGE_GOODS("GOODS_PACKAGE_IMG_TP.PACKAGE_GOODS", "묶음상품 전용"),
        ;

        private final String code;
        private final String codeName;

    }


    // 최대 구매수량 유형
    @Getter
    @RequiredArgsConstructor
    public enum PurchaseLimitMaxType implements CodeCommEnum{
    	DAY1("PURCHASE_LIMIT_MAX_TP.1DAY", "1일 기준"),
    	DURATION("PURCHASE_LIMIT_MAX_TP.DURATION", "기간별 설정"),
    	UNLIMIT("PURCHASE_LIMIT_MAX_TP.UNLIMIT", "제한없음"),
        ;

        private final String code;
        private final String codeName;

    }


    // 상품할인 유형
    @Getter
    @RequiredArgsConstructor
    public enum GoodsDiscountType implements CodeCommEnum{
    	NONE           ("GOODS_DISCOUNT_TP.NONE"           , "NONE"),
        PRIORITY       ("GOODS_DISCOUNT_TP.PRIORITY"       , "우선"),
        ERP_EVENT      ("GOODS_DISCOUNT_TP.ERP_EVENT"      , "ERP행사"),
        IMMEDIATE      ("GOODS_DISCOUNT_TP.IMMEDIATE"      , "즉시"),
        NOT_APPLICABLE ("GOODS_DISCOUNT_TP.NOT_APPLICABLE" , "적용불가"),
        PACKAGE        ("GOODS_DISCOUNT_TP.PACKAGE"        , "기본"),
        EMPLOYEE       ("GOODS_DISCOUNT_TP.EMPLOYEE"       , "임직원"),
        REGULAR_DEFAULT("GOODS_DISCOUNT_TP.REGULAR_DEFAULT", "정기기본할인"),
        REGULAR_ADD    ("GOODS_DISCOUNT_TP.REGULAR_ADD"    , "정기추가할인"),
        GOODS_COUPON   ("GOODS_DISCOUNT_TP.GOODS_COUPON"   , "상품쿠폰"),
        CART_COUPON    ("GOODS_DISCOUNT_TP.CART_COUPON"    , "장바구니쿠폰"),
        EXHIBIT_SELECT ("GOODS_DISCOUNT_TP.EXHIBIT_SELECT" , "균일가골라담기"),
        ADD_GOODS      ("GOODS_DISCOUNT_TP.ADD_GOODS"      , "추가구성상품할인"),
        O2O_SHOP       ("GOODS_DISCOUNT_TP.O2O_SHOP"       , "O2O매장특가"),
        ;

        private final String code;
        private final String codeName;

        public static GoodsDiscountType findByCode(String code) {
			return Arrays.stream(GoodsDiscountType.values())
		            .filter(goodsDiscountType -> goodsDiscountType.getCode().equals(code))
		            .findAny()
		            .orElse(null);
        }
    }


    // 상품할인 방법
    @Getter
    @RequiredArgsConstructor
    public enum GoodsDiscountMethodType implements CodeCommEnum{
    	FIXED_PRICE("GOODS_DISCOUNT_METHOD_TP.FIXED_PRICE", "고정가할인"),
    	FIXED_RATE("GOODS_DISCOUNT_METHOD_TP.FIXED_RATE", "정률할인")
      ;

        private final String code;
        private final String codeName;

        public static GoodsDiscountMethodType findByCode(String code) {
			return Arrays.stream(GoodsDiscountMethodType.values())
		            .filter(GoodsDiscountMethodType -> GoodsDiscountMethodType.getCode().equals(code))
		            .findAny()
		            .orElse(null);
        }

    }


    // 쿠폰 할인 방식
    @Getter
    @RequiredArgsConstructor
    public enum CouponDiscountStatus implements CodeCommEnum{
    	FIXED_DISCOUNT("COUPON_DISCOUNT_STATUS.FIXED_DISCOUNT", "정액"),
    	PERCENTAGE_DISCOUNT("COUPON_DISCOUNT_STATUS.PERCENTAGE_DISCOUNT", "정률"),
        ;

        private final String code;
        private final String codeName;

    }

    // 재고관리 기준 타입
    @Getter
    @RequiredArgsConstructor
    public enum StockDeadlineType implements CodeCommEnum {
        PERCENT("percent", "%"),
        DATE("date", "일");

        private final String code;
        private final String codeName;

    }


    // 식단주기 유형(녹즙)
    @Getter
    @RequiredArgsConstructor
    public enum GoodsCycleTypeByGreenJuice implements CodeCommEnum{
    	DAY1_PER_WEEK("GOODS_CYCLE_TP.1DAY_PER_WEEK", "주1일", "1", "1"),
    	DAYS3_PER_WEEK("GOODS_CYCLE_TP.3DAYS_PER_WEEK", "주3일", "3", "3"),
    	DAYS4_PER_WEEK("GOODS_CYCLE_TP.4DAYS_PER_WEEK", "주4일", "4", "4"),
    	DAYS5_PER_WEEK("GOODS_CYCLE_TP.5DAYS_PER_WEEK", "주5일", "5", "5"),
    	DAYS6_PER_WEEK("GOODS_CYCLE_TP.6DAYS_PER_WEEK", "주6일", "6", "5"), // 주6일이여도 선택가능한 요일은 5개
        ;

        private final String code;
        private final String codeName;
        private final String dayQty; // 요일 개수
        private final String selectableDay; // 미니카트에서 선택가능한 요일 개수

        public static GoodsCycleTypeByGreenJuice findByCode(String code) {
        	return Arrays.stream(GoodsCycleTypeByGreenJuice.values())
        					.filter(goodsCycleTypeDayQty -> goodsCycleTypeDayQty.getCode().equals(code))
        					.findAny()
        					.orElse(DAYS6_PER_WEEK);
        }
    }


    // 식단주기 기간 개수(녹즙)
    @Getter
    @RequiredArgsConstructor
    public enum GoodsCycleTermTypeQtyByGreenJuice implements CodeCommEnum{
    	WEEK4("GOODS_CYCLE_TERM_TP.WEEK4", "4", "4주"),
        WEEK8("GOODS_CYCLE_TERM_TP.WEEK8", "8", "8주"),
        WEEK12("GOODS_CYCLE_TERM_TP.WEEK12", "12", "12주"),
        WEEK16("GOODS_CYCLE_TERM_TP.WEEK16", "16", "16주"),
        WEEK20("GOODS_CYCLE_TERM_TP.WEEK20", "20", "20주"),
        WEEK24("GOODS_CYCLE_TERM_TP.WEEK24", "24", "24주"),
        ;

        private final String code;
        private final String codeName; // 식단 주기 개수
        private final String weekName; // 식단 주기명

        public static GoodsCycleTermTypeQtyByGreenJuice findByCode(String code) {
        	return Arrays.stream(GoodsCycleTermTypeQtyByGreenJuice.values())
        					.filter(goodsCycleTermTypeQty -> goodsCycleTermTypeQty.getCode().equals(code))
        					.findAny()
        					.orElse(WEEK24);
        }

    }


    // 요일코드(녹즙)
    @Getter
    @RequiredArgsConstructor
    public enum WeekCodeByGreenJuice implements CodeCommEnum{
    	MON("WEEK_CD.MON", "월", 2, 1),
    	TUE("WEEK_CD.TUE", "화", 3, 2),
    	WED("WEEK_CD.WED", "수", 4, 3),
    	THU("WEEK_CD.THU", "목", 5, 4),
    	FRI("WEEK_CD.FRI", "금", 6, 5),
    	SAT("WEEK_CD.SAT", "토", 7, 6),
    	SUN("WEEK_CD.SUN", "일", 1, 7)
        ;

        private final String code;
        private final String codeName;
        private final int dayNum;
        private final int localDateDayNum;

		public static WeekCodeByGreenJuice findByCode(String code) {
			return Arrays.stream(WeekCodeByGreenJuice.values()).filter(weekCodeByGreenJuice -> weekCodeByGreenJuice.getCode().equals(code)).findAny()
					.orElse(null);
		}
    }


    // 식단주기 유형(베이비밀,잇슬림)
    @Getter
    @RequiredArgsConstructor
    public enum GoodsCycleType implements CodeCommEnum{
    	DAY1_PER_WEEK("GOODS_CYCLE_TP.1DAY_PER_WEEK", "주1일", "1", "" , Arrays.asList()),
    	DAYS3_PER_WEEK("GOODS_CYCLE_TP.3DAYS_PER_WEEK", "주3일", "3", "월/수/금", Arrays.asList("WEEK_CD.MON","WEEK_CD.WED","WEEK_CD.FRI")),
    	DAYS4_PER_WEEK("GOODS_CYCLE_TP.4DAYS_PER_WEEK", "주4일", "4", "월~목", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU")),
    	DAYS5_PER_WEEK("GOODS_CYCLE_TP.5DAYS_PER_WEEK", "주5일", "5", "월~금", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU","WEEK_CD.FRI")),
    	DAYS6_PER_WEEK("GOODS_CYCLE_TP.6DAYS_PER_WEEK", "주6일", "6", "월~토", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU","WEEK_CD.FRI")),
    	DAYS7_PER_WEEK("GOODS_CYCLE_TP.7DAYS_PER_WEEK", "주7일", "7", "월~일", Arrays.asList("WEEK_CD.MON","WEEK_CD.TUE","WEEK_CD.WED","WEEK_CD.THU","WEEK_CD.FRI"))
        ;

        private final String code;
        private final String codeName;
        private final String typeQty;
        private final String weekText;
        private final List<String> weekCodeList;

        public static GoodsCycleType findByCode(String code) {
        	return Arrays.stream(GoodsCycleType.values())
        			.filter(goodsCycleType -> goodsCycleType.getCode().equals(code))
        			.findAny()
        			.orElse(null);
        }

        public static GoodsCycleType findByCodeName(String codeName) {
            return Arrays.stream(GoodsCycleType.values())
                    .filter(goodsCycleType -> goodsCycleType.getCodeName().equals(codeName))
                    .findAny()
                    .orElse(null);
        }
    }


    // 식단주기 기간 유형(베이비밀,잇슬림)
    @Getter
    @RequiredArgsConstructor
    public enum GoodsCycleTermType implements CodeCommEnum{
    	WEEK1("GOODS_CYCLE_TERM_TP.WEEK1", "1주", "1",10),
    	WEEK2("GOODS_CYCLE_TERM_TP.WEEK2", "2주","2",10),
    	WEEK4("GOODS_CYCLE_TERM_TP.WEEK4", "4주","4",10),
    	WEEK8("GOODS_CYCLE_TERM_TP.WEEK8", "8주","8",20),
    	WEEK12("GOODS_CYCLE_TERM_TP.WEEK12", "12주","12",30),
    	WEEK16("GOODS_CYCLE_TERM_TP.WEEK16", "16주","16",30),
    	WEEK20("GOODS_CYCLE_TERM_TP.WEEK20", "20주","20",30),
    	WEEK24("GOODS_CYCLE_TERM_TP.WEEK24", "24주","24",30),
        ;

        private final String code;
        private final String codeName;
        private final String typeQty;
        private final int changeScheduleMaxDate; // 도착일 변경 가능 최대 일자

        public static GoodsCycleTermType findByCode(String code) {
        	return Arrays.stream(GoodsCycleTermType.values())
        			.filter(goodsCycleTermType -> goodsCycleTermType.getCode().equals(code))
        			.findAny()
        			.orElse(null);
        }
    }

    // 배송불가지역 유형
    @Getter
    @RequiredArgsConstructor
    public enum UndeliverableAreaType implements CodeCommEnum{
    	NONE("UNDELIVERABLE_AREA_TP.NONE", "없음"),
    	A1("UNDELIVERABLE_AREA_TP.A1", "도서산간(1권역)"),
    	A2("UNDELIVERABLE_AREA_TP.A2", "제주(2권역)"),
    	A1_A2("UNDELIVERABLE_AREA_TP.A1_A2", "1권역/2권역"),
        ;

        private final String code;
        private final String codeName;

    }


    // 스토어 배송유형
    @Getter
    @RequiredArgsConstructor
    public enum StoreDeliveryType implements CodeCommEnum{
    	PICKUP("STORE_DELIVERY_TYPE.PICKUP"	, "매장픽업"		,"매장픽업"),
    	DIRECT("STORE_DELIVERY_TYPE.DIRECT"	, "매장배송"		,"매장배송"),
    	HOME("STORE_DELIVERY_TYPE.HOME"		, "홈배송"		,"가정집"),
    	OFFICE("STORE_DELIVERY_TYPE.OFFICE"	, "오피스배송"		,"사무실"),
    	HD_OD("STORE_DELIVERY_TYPE.HD_OD"	, "홈/오피스배송"	,"홈/오피스배송")
        ;

        private final String code;
        private final String codeName;
        private final String deliveryName;

        public static StoreDeliveryType findByCode(String code) {
        	return Arrays.stream(StoreDeliveryType.values())
        			.filter(storeDeliveryType -> storeDeliveryType.getCode().equals(code))
        			.findAny()
        			.orElse(null);
        }

    }


    // 스토어 배송 간격
    @Getter
    @RequiredArgsConstructor
    public enum StoreDeliveryInterval implements CodeCommEnum{
    	EVERY("STORE_DELIVERY_INTERVAL.EVERY", "매일"),
    	TWO_DAYS("STORE_DELIVERY_INTERVAL.TWO_DAYS", "격일"),
    	NON_DELV("STORE_DELIVERY_INTERVAL.NON_DELV", "미배송"),
    	PARCEL("STORE_DELIVERY_INTERVAL.PARCEL", "택배")
        ;

        private final String code;
        private final String codeName;

    }


    // 스토어 녹즙 - 배송가능 품목 유형
    @Getter
    @RequiredArgsConstructor
    public enum StoreDeliverableItem implements CodeCommEnum{
    	ALL("STORE_DELIVERABLE_ITEM.ALL", "전체"),
    	FD("STORE_DELIVERABLE_ITEM.FD", "녹즙"),
    	DM("STORE_DELIVERABLE_ITEM.DM", "DM"),
        ;

        private final String code;
        private final String codeName;

    }

    // 일괄배송 배송 세트 수량
    @Getter
    @RequiredArgsConstructor
    public enum GoodsDailyBulkQtyType implements CodeCommEnum{
    	SETS2("GOODS_BULK_TP.2SETS", "2", 2),
    	SETS3("GOODS_BULK_TP.3SETS", "3", 3),
    	SETS6("GOODS_BULK_TP.6SETS", "6", 6),
    	SETS7("GOODS_BULK_TP.7SETS", "7", 7),
        ;

        private final String code;
        private final String codeName;
        private final int setCnt;

        public static GoodsDailyBulkQtyType findByCode(String code) {
        	return Arrays.stream(GoodsDailyBulkQtyType.values())
        					.filter(goodsDailyBulkQty -> goodsDailyBulkQty.getCode().equals(code))
        					.findAny()
        					.orElse(null);
        }
    }

    // 디바이스 타입
    @Getter
    @RequiredArgsConstructor
    public enum DeviceType implements CodeCommEnum{
    	PC("PC", "피시"),
    	MOBILE("MOBILE", "모바일"),
    	APP("APP", "앱"),
    	;

        private final String code;
        private final String codeName;

    }

    //구매허용 Enum : PURCHASE_TARGET_TP 대응
    @Getter
    @RequiredArgsConstructor
    public enum GoodsPurchaseTypes {
    	ALL("PURCHASE_TARGET_TP.ALL", "전체"),
    	MEMBER("PURCHASE_TARGET_TP.MEMBER", "일반"),
    	EMPLOYEE("PURCHASE_TARGET_TP.EMPLOYEE", "임직원 전용"),
    	NONMEMBER("PURCHASE_TARGET_TP.NONMEMBER", "비회원")
    	;

    	private final String code;
    	private final String name;
    }

    //판매허용범위 Enum : GOODS_DISPLAY_TYPE 대응
    @Getter
    @RequiredArgsConstructor
    public enum GoodsDisplayTypes {
    	NONE("GOODS_DISPLAY_TYPE.NONE", "전체"),
    	APP("GOODS_DISPLAY_TYPE.APP", "앱"),
    	WEB_MOBILE("GOODS_DISPLAY_TYPE.WEB_MOBILE", "PC 모바일"),
    	WEB_PC("GOODS_DISPLAY_TYPE.WEB_PC", "PC 웹")
    	;

    	private final String code;
    	private final String name;
    }

    //상품 등록/ 수정에 따른 Message 처리
    @Getter
    @RequiredArgsConstructor
    public enum GoodsAddStatusTypes implements MessageCommEnum {
    	ADD_GOODS_FAIL                            ("1000", "상품 생성시 오류가 발생하였습니다.")
        , MODIFY_GOODS_FAIL                       ("1002", "상품 수정시 오류가 발생하였습니다.")
        , DUPLICATE_GOODS                         ("2000", "이미 등록된 상품이 있습니다.")
        , NONE_EMPLOYEE_DISCOUNT_RATIO            ("1003", "해당 품목의 브랜드에 따른 임직원 할인율이 존재하지 않습니다.")
        , LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_PERIOD           ("LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_PERIOD", "기존에 등록된 할인기간과 겹칩니다.")
        , LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_START_TIME       ("LOCAL_DEFINE_INVALID_GOODS_DISCOUNT_START_TIME", "할인 시작일은 현재 이후만 가능합니다.")
        , DELETE_GOODS_RESERVATION_OPTION_FAIL       ("DELETE_GOODS_RESERVATION_OPTION_FAIL", "삭제 불가한 예약상품이 있습니다. 예약 회차를 다시 확인해주세요.")
        ;

    	private final String code;
    	private final String message;
    }

    // 상품 이미지명의 사이즈별 prefix
    @Getter
    @RequiredArgsConstructor
    public enum GoodsImagePrefixBySize implements CodeCommEnum { //

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

    // 상품 이미지명의 사이즈별 prefix
    @Getter
    @RequiredArgsConstructor
    public enum GoodsDiscountExcelUploadErrMsg implements MessageCommEnum {

    	GOODS_ID_EMPTY("GOODS_ID_EMPTY", "상품코드없음"),
    	VALUE_EMPTY("VALUE_EMPTY", "값이 없음"),
    	FORMAT_BAD("FORMAT_BAD", "날짜형식이 맞지않음"),
    	VALUE_BAD("VALUE_BAD", "값이 맞지 않음"),
    	DISCOUNT_OVER_VAL("DISCOUNT_OVER_VAL", "할인율값이 맞지 않습니다."),
    	PRICE_OVER_VAL("PRICE_OVER_VAL", "정상가보다 가격이 높습니다."),
    	GOODS_PACKAGE("GOODS_PACKAGE", "묶음상품입니다."),
    	INTEGER_BAD_FORMAT("INTEGER_BAD_FORMAT", "숫자형식이아님"),
    	START_DT_BEFORE("START_DT_BEFORE", "시작일이 현재일보다 과거입니다."),
    	DISCOUNT_VAL_NOT_MINUS("DISCOUNT_VAL_NOT_MINUS", "값이 음수입니다."),
    	START_DT_OVER_END_DATE("START_DT_OVER_END_DATE", "시작일이 종료일보다 늦습니다."),
    	GOODS_ID_DISCOUNT_OVERLAP("GOODS_ID_DISCOUNT_OVERLAP", "할인기간이 중복됩니다."),
		APPR_DUPLICATE("APPR_DUPLICATE", "이미 승인요청 중인 내역이 있습니다. 승인요청중에는 할인 추가가 불가능 합니다."),;

    	private final String code;
  		private final String message;
    }

    // 몰인몰 구분
    @Getter
    @RequiredArgsConstructor
    public enum MallDiv implements CodeCommEnum {
        PULMUONE("MALL_DIV.PULMUONE", "풀무원"),
        ORGA("MALL_DIV.ORGA", "올가"),
        EATSLIM("MALL_DIV.EATSLIM", "잇슬림"),
        BABYMEAL("MALL_DIV.BABYMEAL", "베이비밀");

        private final String code;
        private final String codeName;
    }

    // 상품마스터(IL_GOODS) 변경내역 기준컬럼 및 컬럼COMMENT
    @Getter
    @RequiredArgsConstructor
    public enum GoodsColumnComment implements CodeCommEnum{
    	MD_RECOMMEND_YN("mdRecommendYn", "MD추천(Y:추천)"),
    	GOODS_NM("goodsName", "상품명"),
    	PACKAGE_UNIT_DISP_YN("packageUnitDisplayYn", "표장용량 구성정보 노출여부(Y:노출)"),
    	PROMOTION_NM("promotionName", "프로모션명"),
    	PROMOTION_NM_START_DT("promotionNameStartDate", "프로모션 시작일"),
    	PROMOTION_NM_END_DT("promotionNameEndDate", "프로모션 종료일"),
    	GOODS_DESC("goodsDesc", "상품설명"),
    	SEARCH_KYWRD("searchKeyword", "검색키워드"),
    	PURCHASE_MEMBER_YN("purchaseMemberYn", "회원 구매여부(Y:회원 구매가능)"),
    	PURCHASE_EMPLOYEE_YN("purchaseEmployeeYn", "임직원 구매여부(Y:구매가능)"),
    	PURCHASE_NONMEMBER_YN("purchaseNonmemberYn", "비회원 구매여부(Y:비회원 구매가능)"),
    	DISP_WEB_PC_YN("displayWebPcYn", "WEB PC 전시여부(Y:전시)"),
    	DISP_WEB_MOBILE_YN("displayWebMobileYn", "WEB MOBILE 전시여부(Y:전시)"),
    	DISP_APP_YN("displayAppYn", "APP 전시여부(Y:전시)"),
    	DISP_YN("displayYn", "전시여부(Y:전시)"),
    	SALE_START_DT("saleStartDate", "판매 시작일"),
    	SALE_END_DT("saleEndDate", "판매 종료일"),
    	SALE_STATUS("saleStatus", "판매상태 공통코드(SALE_STATUS)"),
    	GOODS_OUTMALL_SALE_STAT("goodsOutmallSaleStatus", "상품 외부몰 판매 상태 공통코드(GOODS_OUTMALL_SALE_STAT)"),
    	SALE_TP("saleType", "판매유형 공통코드(SALE_TYPE)"),
    	GREEN_JUICE_CLEANSE_OPT_YN("greenJuiceCleanseOptYn", "녹즙 클렌즈 옵션 사용여부(Y:사용)"),
    	AUTO_DISP_SIZE_YN("autoDisplaySizeYn", "단위별 용량정보 자동 표기(Y:자동표기)"),
    	SIZE_ETC("sizeEtc", "단위별 용량정보(자동 표기안함 일때 사용)"),
    	GOODS_DAILY_TP("goodsDailyType", "일일상품 유형(GOODS_DAILY_TP : GREENJUICE/BABYMEAL/EATSSLIM )"),
    	GOODS_DAILY_ALLERGY_YN("goodsDailyAllergyYn", "(일일상품)알러지 식단 포함여부(Y:포함)"),
    	GOODS_DAILY_BULK_YN("goodsDailyBulkYn", "(일일상품)일괄배달 허용여부(Y:허용)"),
    	SALE_SHOP_YN("saleShopYn", "매장판매 여부(Y:매장판매)"),
    	COUPON_USE_YN("couponUseYn", "개별 쿠폰 적용여부(Y:적용)"),
    	LIMIT_MIN_CNT("limitMinimumCnt", "최소구매수량"),
    	LIMIT_MAX_DURATION("limitMaximumDuration", "최대구매수량 산정기간"),
    	LIMIT_MAX_CNT("limitMaximumCnt", "최대구매수량"),
    	GOODS_PACKAGE_VIDEO_AUTOPLAY_YN("goodsPackageVideoAutoplayYn", "(묶음상품)비디오 자동재생 유무(Y:자동재생)"),
    	GOODS_PACKAGE_VIDEO_URL("goodsPackageVideoUrl", "(묶음상품)동영상 URL"),
    	NOTICE_BELOW_1_IMG_URL("noticeBelow1ImageUrl", "상세 상단공지 이미지 URL"),
    	NOTICE_BELOW_1_START_DT("noticeBelow1StartDate", "상세 상단공지 시작일"),
    	NOTICE_BELOW_1_END_DT("noticeBelow1EndDate", "상세 상단공지 종료일"),
    	NOTICE_BELOW_2_IMG_URL("noticeBelow2ImageUrl", "상세 하단공지 이미지 URL"),
    	NOTICE_BELOW_2_START_DT("noticeBelow2StartDate", "상세 하단공지 시작일"),
    	NOTICE_BELOW_2_END_DT("noticeBelow2EndDate", "상세 하단공지 종료일"),
    	GOODS_MEMO("goodsMemo", "상품 메모")
    	;

    	private final String code;
    	private final String codeName;

    	public static GoodsColumnComment findByComment(String code) {
    		return Arrays.stream(GoodsColumnComment.values())
    				.filter(goodsColumnComment -> goodsColumnComment.getCode().equals(code))
    				.findAny()
    				.orElse(null);
    	}
    }

    // 상품 마스터 이외(상품 카테고리, 추가 상품 등) 변경내역 기준(테이블명, 컬럼, 컬럼COMMENT)
    @Getter
    @RequiredArgsConstructor
    public enum GoodsEtcColumnComment implements ChangeLogEnum{
    	// 상품 카테고리
    	IL_CTGRY_ID("IL_GOODS_CTGRY" ,"ilCtgryId", "categoryFullName", "카테고리ID"),
    	// 출고처별 배송 정책
    	ORIG_IL_SHIPPING_TMPL_ID("IL_GOODS_SHIPPING_TEMPLATE" ,"itemWarehouseShippingTemplateList", "shppingTemplateName", "배송비 탬플릿 SEQ"),
    	// 추가상품 리스트
    	TARGET_GOODS_ID("IL_GOODS_ADDITIONAL_GOODS_MAPPING", "targetGoodsId", "goodsName", "추가 상품 ID"),
    	// 추천상품 리스트(추후 작업 진행 예정)
    	RECOMMEND_TARGET_GOODS_ID("IL_GOODS_RECOMMEND", "targetGoodsId", "goodsName", "추천 상품 ID"),
    	// 일일상품 > 식단 주기 > 식단주기 유형(식단주기는 2가지 데이터를 저장해야 하기 때문에 tableName을 두종류로 분리)
    	GOODS_CYCLE_TP("IL_GOODS_DAILY_CYCLE", "ilGoodsDailyCycleId", "goodsCycleType", "식단주기 유형 공통코드"),
    	// 일일상품 > 식단 주기 > 식단주기 기간 유형(식단주기는 2가지 데이터를 저장해야 하기 때문에 tableName을 두종류로 분리)
    	GOODS_CYCLE_TERM_TP("IL_GOODS_DAILY_CYCLE_TERM", "ilGoodsDailyCycleId", "goodsCycleTermType", "식단주기 기간 유형 공통코드"),
    	// 일일상품 > 일괄배달 설정
    	GOODS_BULK_TP("IL_GOODS_DAILY_BULK", "ilGoodsDailyBulkId", "goodsBulkType", "일괄배달 유형"),
        // 풀무원샵 상품코드
        IF_GOODS_MAPPING("IF_GOODS_MAPPING", "goodsNo", "goodsNo", "풀무원샵 상품코드"),
    	;

    	private final String tableName;
    	private final String idColumn;
    	private final String dataColumn;
    	private final String comment;

    	public static GoodsEtcColumnComment findByInfo(String tableName) {
    		return Arrays.stream(GoodsEtcColumnComment.values())
    				.filter(goodsEtcColumnComment -> goodsEtcColumnComment.getTableName().equals(tableName))
    				.findAny()
    				.orElse(null);
    	}
    }

  //상품 승인 Proc 진행에 따른 처리
    @Getter
    @RequiredArgsConstructor
    public enum GoodsApprProcStatus implements MessageCommEnum {
    	APPR_DUPLICATE                            ("1000", "이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
    	CLIENT_APPR_DUPLICATE                     ("1001", "거래처에서 이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
    	NONE_GOODS_ID                             ("1999", "상품이 존재하지 않습니다."),
    	NOT_DIFFERENT_GOODS                       ("1005", "변경된 상품 내역이 없습니다."),
    	NONE_APPR_USERS                           ("1006", "승인관리자 정보가 없습니다."),
    	CLINET_APPR_REQUEST                       ("1007", "승인요청 되었습니다."),
    	NONE_APPR                                 ("2000", "승인 관리자 정보가 존재하지 않습니다. 승인 관리자를 추가해 주세요."),
    	ADMIN_DIFFERENT_GOODS                     ("2001", "업데이트 된 내용이 존재하여, 상세정보를 다시 불러옵니다. 수정된 정보 확인 후 재 등록바랍니다."),
    	NO_AUTH										("2002", "권한이 없습니다.")
        ;

    	private final String code;
    	private final String message;
    }

	//상품 할인 승인 Proc 진행에 따른 처리
	@Getter
	@RequiredArgsConstructor
	public enum GoodsDiscountApprProcStatus implements MessageCommEnum {
		PRIORITY_APPR_DUPLICATE                   ("1000", "이미 승인요청 중인 우선할인 내역이 있습니다. 승인요청중에는 할인 추가가 불가능 합니다."),
		IMMEDIATE_APPR_DUPLICATE                   ("1001", "이미 승인요청 중인 즉시할인 내역이 있습니다. 승인요청중에는 할인 추가가 불가능 합니다."),
		EMPLOYEE_APPR_DUPLICATE                   ("1002", "이미 승인요청 중인 임직원 개별할인 내역이 있습니다. 승인요청중에는 할인 추가가 불가능 합니다."),
		PACKAGE_APPR_DUPLICATE                   ("1003", "이미 승인요청 중인 묶음상품 기본 판매가 내역이 있습니다. 승인요청중에는 할인 추가가 불가능 합니다."),
		NOT_HEADQUART                             ("1998", "관리자 권한이 아닙니다."),
		NONE_GOODS_ID                             ("1999", "상품이 존재하지 않습니다."),
		NONE_APPR_USERS                           ("1006", "승인관리자 정보가 없습니다."),
		CLINET_APPR_REQUEST                       ("1007", "승인요청 되었습니다."),
		NONE_APPR                                 ("2000", "승인 관리자 정보가 존재하지 않습니다. 승인 관리자를 추가해 주세요."),
		DELETE_APPR                               ("9000", "할인 삭제 처리가 완료 되었습니다."),
		CANCEL_PREVIOUS                           ("9001", "이미 삭제처리 되었습니다."),
		DENIED_PREVIOUS                           ("9001", "관리자가 반려 처리 하였습니다."),
		OVER_PERIOD                               ("3000", "이미 진행 중인 할인 승인 내역 입니다.")
		;

		private final String code;
		private final String message;
	}

	// 선물하기 허용 여부
    @Getter
    @RequiredArgsConstructor
    public enum PresentYn implements CodeCommEnum{
        Y("Y", "허용"),
        N("N", "비허용"),
        NA("NA", "미대상"),
        ;

        private final String code;
        private final String codeName;

    }

    //출고처 코드
    @Getter
    @RequiredArgsConstructor
    public enum GoodsWarehouseCode implements CodeCommEnum {

          WAREHOUSE_YONGIN_CODE("WAREHOUSE_YONGIN_ID", "용인")
        , WAREHOUSE_BAEKAM_CODE("WAREHOUSE_BAEKAM_ID", "백암");

        private final String code;
        private final String codeName;

    }

    //출고처 코드
    @Getter
    @RequiredArgsConstructor
    public enum GoodsImageDetailYn implements CodeCommEnum {

        UPDATE_Y("Y", "수정")
        , UPDATE_N("N", "미수정");

        private final String code;
        private final String codeName;
    }

    // 식단 컨텐츠 등록/수정 메시지코드
    @Getter
    @RequiredArgsConstructor
    public enum MealContsCode implements MessageCommEnum {
        OVERLAP_MEAL_CONTS_CD("OVERLAP_MEAL_CONTS_CD", "이미 등록된 식단품목코드입니다."),
        ;

        private final String code;
        private final String message;
    }
}