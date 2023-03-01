package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.util.Arrays;

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
 *  1.1    2020. 12. 14.               이명수        주문상태 추가 (OrderStatus)
 *  1.2    2020. 12. 17.               이명수        주문등록경로 추가 (OrderRoute)
 * =======================================================================
 * </PRE>
 */
public class OrderEnums {


    @Getter
    @RequiredArgsConstructor
    public enum OrderErrMsg implements MessageCommEnum {

        VALUE_EMPTY("-1", "해당 주문정보가 존재하지 않음"),
        ONE_ORDER_STATE_CHANGE("ONE_ORDER_STATE_CHANGE", "주문상태 변경 불가한 주문입니다."),
        MANY_ORDER_STATE_CHANGE("MANY_ORDER_STATE_CHANGE", "주문상태 변경 불가한 주문이 포함 되어있습니다.")
        ;
        private final String code;
        private final String message;
    }


    // 주문생성 결과
    @Getter
    @RequiredArgsConstructor
    public enum OrderRegistrationResult implements CodeCommEnum {
        SUCCESS("ORDER_REG_RESULT.SUCCESS", "성공"),
        PART_SUCCESS("ORDER_REG_RESULT.PART_SUCCESS", "부분성공"),
        FAIL("ORDER_REG_RESULT.FAIL", "실패"),
        CALIM_RESTORE_NOT_MINE("ORDER_REG_RESULT.CALIM_RESTORE_NOT_MINE", "잘못 된 접근입니다."),
        CALIM_RESTORE_SUCCESS("ORDER_REG_RESULT.CALIM_RESTORE_CD_SUCCESS", "클레임 주문 상태가 {0} 상태로 완료 처리 되었습니다."),
        CALIM_RESTORE_FAIL("ORDER_REG_RESULT.CALIM_RESTORE_FAIL", "클레임 주문 {0} 상태 처리 중 에러가 발생하였습니다."),
        CALIM_RESTORE_SOURCE_CD_FAIL("ORDER_REG_RESULT.CALIM_RESTORE_SOURCE_CD_FAIL", "클레임 주문 상태가 {0} 상태가 아닙니다."),
        CALIM_RESTORE_TARGET_CD_FAIL("ORDER_REG_RESULT.CALIM_RESTORE_TARGET_CD_FAIL", "클레임 주문 {0} 상태가 존재 합니다."),
        CALIM_RESTORE_CNT_FAIL("ORDER_REG_RESULT.CALIM_RESTORE_CNT_FAIL", "클레임 주문 수량을 확인해주세요."),
        CALIM_STATUS_FAIL("ORDER_REG_RESULT.CALIM_STATUS_FAIL", "클레임 주문 상태를 확인해주세요."),
        CALIM_STATUS_DIFF_FAIL("ORDER_REG_RESULT.CALIM_STATUS_FAIL", "주문 상태가 변경되어 취소가 불가합니다."),
        CALIM_DIRECT_PAYMENT_UPDATE_FAIL("ORDER_REG_RESULT.CALIM_DIRECT_PAYMENT_UPDATE_FAIL", "추가배송비 직접 결제 처리 중 에러가 발생하였습니다."),
        ;

        private final String code;
        private final String codeName;

    }

    // 주문등록경로
    @Getter
    @RequiredArgsConstructor
    public enum OrderRoute implements CodeCommEnum {
        FRONT_CART("ORDER_ROUTE.FRONT_CART", "장바구니주문"),
        BOS_CREATE("ORDER_ROUTE.BOS_CREATE", "주문생성 업로드"),
        BOS_SELLER("ORDER_ROUTE.BOS_SELLER", "판매처별 업로드"),
        BOS_COPY("ORDER_ROUTE.BOS_COPY", "주문복사"),
        EASY_ADMIN("ORDER_ROUTE.EASY_ADMIN", "이지어드민 주문"),
        REGULAR_DELI("ORDER_ROUTE.REGULAR_DELI", "정기배송 주문"),
        COLLECTION_MALL("ORDER_ROUTE.COLLECTION_MALL", "수집몰 엑셀 업로드 주문"),
        ;

        private final String code;
        private final String codeName;
    }

    // 주문 리스트 엑셀 다운로드
    @Getter
    @RequiredArgsConstructor
    public enum OrderExcelNm implements CodeCommEnum {
    	ODL("ODL", "주문리스트"),
        ODD("ODD", "주문상세리스트"),
        IC("IC", "결제완료 상세리스트"),
        DR("DR", "배송준비중 상세리스트"),
        DI("DI", "배송중 상세리스트"),
        CA("CA", "취소요청 상세리스트"),
        RA("RA", "반품 상세리스트"),
        FA("FA", "환불 상세리스트"),
        CS("CS", "CS환불 리스트"),
        MI("MI", "미출 주문 상세리스트"),
        REGULAR("REGULAR", "정기배송주문신청리스트"),
        ;

        private final String code;
        private final String codeName;
        }
    // 주문등록경로
    @Getter
    @RequiredArgsConstructor
    public enum OrderStatusDetailType implements CodeCommEnum {
    	BOS("ORDER_STATUS_DELI_TP.BOS", "관리자(BOS)"),
    	BOX("ORDER_STATUS_DELI_TP.BOX", "출출박스"),
    	DAILY("ORDER_STATUS_DELI_TP.DAILY", "일일배송"),
    	DAWN("ORDER_STATUS_DELI_TP.DAWN", "새벽"),
    	NORMAL("ORDER_STATUS_DELI_TP.NORMAL", "일반"),
    	RENTAL("ORDER_STATUS_DELI_TP.RENTAL", "렌탈"),
    	SHOP_DELIVERY("ORDER_STATUS_DELI_TP.SHOP_DELIVERY", "매장배송"),
    	SHOP_PICKUP("ORDER_STATUS_DELI_TP.SHOP_PICKUP", "매장픽업"),
        ;

        private final String code;
        private final String codeName;
    }

    // 주문상태
    @Getter
    @RequiredArgsConstructor
    public enum OrderStatus implements CodeCommEnum {

        INCOM_READY("IR", "입금대기중"),
        INCOM_BEFORE_CANCEL_COMPLETE("IB", "입금전 취소"),
        INCOM_COMPLETE("IC", "결제완료"),
        DELIVERY_READY("DR", "배송준비중"),
        DELIVERY_ING("DI", "배송중"),
        DELIVERY_COMPLETE("DC", "배송완료"),
        BUY_FINALIZED("BF", "구매확정"),
        CANCEL_APPLY("CA", "취소요청"),
        CANCEL_COMPLETE("CC", "취소완료"),
        CANCEL_DENY_DEFE("CE", "취소거부"),
        CANCEL_WITHDRAWAL("CW", "취소철회"),
        EXCHANGE_COMPLETE("EC", "재배송"),
        RETURN_APPLY("RA", "반품요청"),
        RETURN_ING("RI", "반품승인"),
        RETURN_DEFER("RF", "반품보류"),
        RETURN_COMPLETE("RC", "반품완료"),
        RETURN_DENY_DEFER("RE", "반품거부"),
        RETURN_WITHDRAWAL("RW", "반품철회"),
        CUSTOMER_SERVICE_REFUND("CS", "CS환불"),
        REFUND_APPLY("FA", "환불요청"),
        REFUND_COMPLETE("FC", "환불완료"),
        CLAIM_REQUEST("CR", "클레임요청"),
        CLAIM_APPROVED("CL", "클레임승인"),

        DELIVERY_DT_CHANGE("DDC", "도착예정일변경"),
        OUTMALL_ORDER_CREATE("OOC", "외부몰 주문 생성"),
        STORE_DELIVERY("SDI", "가맹점 배송중")

        ;

        private final String code;
        private final String codeName;

        public static OrderStatus findByCode(String code) {
 			return Arrays.stream(OrderStatus.values())
 		            .filter(paymentType -> paymentType.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }
    }

    // 정기배송 배송 주문서 생성 여부
    @Getter
    @RequiredArgsConstructor
    public enum RegularOrderCreateYn implements CodeCommEnum{
        CREATE_YN_Y("Y", "주문서생성"),
        CREATE_YN_N("N", "주문서미생성"),
        ;

        private final String code;
        private final String codeName;
    }

    // 정기배송 배송 신청 상태
    @Getter
    @RequiredArgsConstructor
    public enum RegularStatus implements CodeCommEnum{
    	APPLY("REGULAR_STATUS_CD.APPLY", "신청"),
    	ING("REGULAR_STATUS_CD.ING", "진행중"),
    	CANCEL("REGULAR_STATUS_CD.CANCEL", "해지"),
    	END("REGULAR_STATUS_CD.END", "종료"),
        ;

        private final String code;
        private final String codeName;
    }

    // 정기배송 신청 상세 상태
    @Getter
    @RequiredArgsConstructor
    public enum RegularDetailStatus implements CodeCommEnum{
    	APPLY("REGULAR_DETL_STATUS_CD.APPLY", "신청"),
    	SKIP("REGULAR_DETL_STATUS_CD.SKIP", "건너뛰기"),
    	CANCEL_SELLER("REGULAR_DETL_STATUS_CD.CANCEL_SELLER", "해지(판매자귀책)"),
    	CANCEL_BUYER("REGULAR_DETL_STATUS_CD.CANCEL_BUYER", "해지(구매자귀책)"),
        ;

        private final String code;
        private final String codeName;
    }

	// 정기배송 배송 주기
    @Getter
    @RequiredArgsConstructor
    public enum RegularShippingCycle implements CodeCommEnum{
    	WEEK1("REGULAR_CYCLE_TYPE.WEEK1", "1주에 한 번", 1),
    	WEEK2("REGULAR_CYCLE_TYPE.WEEK2", "2주에 한 번", 2),
    	WEEK3("REGULAR_CYCLE_TYPE.WEEK3", "3주에 한 번", 3),
    	WEEK4("REGULAR_CYCLE_TYPE.WEEK4", "4주에 한 번", 4),
        ;

        private final String code;
        private final String codeName;
        private final int weeks;

        public static RegularShippingCycle findByCode(String code) {
        	return Arrays.stream(RegularShippingCycle.values())
        			.filter(regularCycleType -> regularCycleType.getCode().equals(code))
        			.findAny()
        			.orElse(null);
        }
    }

    // 정기배송 배송 기간
    @Getter
    @RequiredArgsConstructor
    public enum RegularShippingCycleTerm implements CodeCommEnum{
    	MONTH1("REGULAR_CYCLE_TERM_TYPE.MONTH1", "1개월", 1),
    	MONTH2("REGULAR_CYCLE_TERM_TYPE.MONTH2", "2개월", 2),
    	MONTH3("REGULAR_CYCLE_TERM_TYPE.MONTH3", "3개월", 3),
    	MONTH4("REGULAR_CYCLE_TERM_TYPE.MONTH4", "4개월", 4),
    	MONTH5("REGULAR_CYCLE_TERM_TYPE.MONTH5", "5개월", 5),
    	MONTH6("REGULAR_CYCLE_TERM_TYPE.MONTH6", "6개월", 6),
        ;

        private final String code;
        private final String codeName;
        private final int month;

        public static RegularShippingCycleTerm findByCode(String code) {
        	return Arrays.stream(RegularShippingCycleTerm.values())
        			.filter(regularCycleTermType -> regularCycleTermType.getCode().equals(code))
        			.findAny()
        			.orElse(null);
        }
    }

    // 정기배송 요일코드
    @Getter
    @RequiredArgsConstructor
    public enum RegularWeekCd implements CodeCommEnum{
    	MON("WEEK_CD.MON", "월", DayOfWeek.MONDAY),
    	TUE("WEEK_CD.TUE", "화", DayOfWeek.TUESDAY),
    	WED("WEEK_CD.WED", "수", DayOfWeek.WEDNESDAY),
    	THU("WEEK_CD.THU", "목", DayOfWeek.THURSDAY),
    	FRI("WEEK_CD.FRI", "금", DayOfWeek.FRIDAY),
    	SAT("WEEK_CD.SAT", "토", DayOfWeek.SATURDAY),
    	SUN("WEEK_CD.SUN", "일", DayOfWeek.SUNDAY)
        ;

        private final String code;
        private final String codeName;
        private final DayOfWeek days;

        public static RegularWeekCd findByCode(String code) {
        	return Arrays.stream(RegularWeekCd.values())
        			.filter(regularWeekCd -> regularWeekCd.getCode().equals(code))
        			.findAny()
        			.orElse(null);
        }
    }

    // 정기배송 신청상태코드
    @Getter
    @RequiredArgsConstructor
    public enum RegularStatusCd implements CodeCommEnum{
    	APPLY("REGULAR_STATUS_CD.APPLY"		, "신청"),
    	ING("REGULAR_STATUS_CD.ING"			, "진행중"),
    	CANCEL("REGULAR_STATUS_CD.CANCEL"	, "해지"),
    	END("REGULAR_STATUS_CD.END"			, "종료"),
        ;

        private final String code;
        private final String codeName;
    }

    // 정기배송상세 신청상태코드
    @Getter
    @RequiredArgsConstructor
    public enum RegularDetlStatusCd implements CodeCommEnum{
    	APPLY("REGULAR_DETL_STATUS_CD.APPLY"				, "신청"),
    	SKIP("REGULAR_DETL_STATUS_CD.SKIP"					, "건너뛰기"),
    	CANCEL_BUYER("REGULAR_DETL_STATUS_CD.CANCEL_BUYER"	, "해지(구매자귀책)"),
    	CANCEL_SELLER("REGULAR_DETL_STATUS_CD.CANCEL_SELLER", "해지(판매자귀책)"),
    	;

    	private final String code;
    	private final String codeName;
    }

    // 정기배송상세 상품 판매상태코드
    @Getter
    @RequiredArgsConstructor
    public enum RegularDetlSaleStatusCd implements CodeCommEnum{
        ON_SALE("REGULAR_GOODS_SALE_STATUS.ON_SALE"             , "판매중"),
        OUT_OF_STOCK("REGULAR_GOODS_SALE_STATUS.OUT_OF_STOCK"   , "일시품절"),
        STOP_SALE("REGULAR_GOODS_SALE_STATUS.STOP_SALE"         , "판매중지"),
    	;

    	private final String code;
    	private final String codeName;
    }

    // 정기배송 처리구분코드
    @Getter
    @RequiredArgsConstructor
    public enum RegularReqGbnCd implements CodeCommEnum{
    	FR("REGULAR_REQ_GBN_CD.FR"		, "최초신청"),
    	GA("REGULAR_REQ_GBN_CD.GA"		, "상품추가"),
    	TE("REGULAR_REQ_GBN_CD.TE"		, "기간연장"),
    	RS("REGULAR_REQ_GBN_CD.RS"		, "회차건너뛰기"),
    	RSC("REGULAR_REQ_GBN_CD.RSC"	, "회차건너뛰기철회"),
    	GS("REGULAR_REQ_GBN_CD.GS"		, "상품건너뛰기"),
    	GSC("REGULAR_REQ_GBN_CD.GSC"	, "상품건너뛰기철회"),
    	GC("REGULAR_REQ_GBN_CD.GC"		, "상품해지"),
    	OC("REGULAR_REQ_GBN_CD.OC"		, "주문생성"),
    	IC("REGULAR_REQ_GBN_CD.IC"		, "결제완료"),
    	IF("REGULAR_REQ_GBN_CD.IF"		, "결제실패"),
    	RC("REGULAR_REQ_GBN_CD.RC"		, "신청정보변경"),
    	DC("REGULAR_REQ_GBN_CD.DC"		, "배송정보변경"),
    	;

    	private final String code;
    	private final String codeName;
    }

    // 정기배송 처리상태코드
    @Getter
    @RequiredArgsConstructor
    public enum RegularReqStatusCd implements CodeCommEnum{
    	PC("REGULAR_REQ_GBN_CD.PC"		, "처리완료"),
    	;

    	private final String code;
    	private final String codeName;
    }

    // 정기배송 배치 타입 정보
    @Getter
    @RequiredArgsConstructor
    public enum RegularOrderBatchTypeCd implements CodeCommEnum{
    	CREATE_ORDER("REGULAR_ORDER_BATCH_TYPE.CREATE_ORDER", "정기배송 주문생성 배치"),
    	REGULAR_PAYMENT("REGULAR_ORDER_BATCH_TYPE.REGULAR_PAYMENT", "정기배송 주문 결제 배치"),
    	;

    	private final String code;
    	private final String codeName;
    }

    // 정기배송 신청 정보 변경 구분
    @Getter
    @RequiredArgsConstructor
    public enum RegularOrderReqInfoChangeCd implements CodeCommEnum{
    	GOODS_CYCLE_TERM_TP("GOODS_CYCLE_TERM_TP", "배송기간변경"),
    	GOODS_CYCLE_TP("GOODS_CYCLE_TP", "배송주기변경"),
    	WEEK_CD("WEEK_CD", "요일변경"),
    	;

    	private final String code;
    	private final String codeName;
    }

    // 정기배송 오류코드
    @Getter
    @RequiredArgsConstructor
    public enum RegularOrderErrorCd implements MessageCommEnum{
    	SUCCESS("0000", "정상"),
    	GOODS_CANCEL_FAIL_NONE("3001", "상품 구독 해지 대상이 존재하지 않습니다."),
    	GOODS_ADD_FAIL_NONE("4001", "추가 가능한 상품이 존재하지 않습니다."),
    	GOODS_CYCLE_TERM_EXTENSION_FAIL_NONE("5001", "기간 연장 처리 건이 존재하지 않습니다."),
    	GOODS_SKIP_FAIL_NONE("6001", "상품 건너뛰기가 실패 했습니다."),
    	GOODS_SKIP_CANCEL_FAIL_NONE("7001", "상품 건너뛰기 철회가 실패 했습니다."),
    	GOODS_REQ_INFO_FAIL_NONE("8001", "신청정보 변경이 실패 했습니다."),
    	GOODS_REQ_INFO_TERM_CHANGE_1("8002", "신청기간 변경이 불가능 합니다.<br/>[남아있는 회차정보 없음]"),
    	GOODS_REQ_INFO_TERM_CHANGE_2("8003", "신청기간 변경이 불가능 합니다.<br/>[해당 신청기간에 선택할 수 있는 배송주기가 아님]"),
    	GOODS_REQ_INFO_TERM_CHANGE_3("8003", "신청기간 변경이 불가능 합니다.<br/>[변경하려는 신청 기간에 배송 가능 주기가 존재하지 않음]"),
    	GOODS_REQ_INFO_TERM_CHANGE_4("8004", "배송주기 변경이 불가능 합니다.<br/>[신청기간에 따른 배송 주기 선택범위를 벗어났습니다.]"),
    	;

    	private final String code;
    	private final String message;

        public static RegularOrderErrorCd findByCode(String code) {
        	return Arrays.stream(RegularOrderErrorCd.values())
        			.filter(regularOrderErrorCd -> regularOrderErrorCd.getCode().equals(code))
        			.findAny()
        			.orElse(null);
        }
    }

    // 현금 영수증
    @Getter
    @RequiredArgsConstructor
    public enum CashReceipt implements CodeCommEnum{
    	DEDUCTION("CASH_RECEIPT.DEDUCTION", "소득공재용", "0", "1"),
    	PROOF("CASH_RECEIPT.PROOF", "지출증빙", "1", "2"),
        USER("CASH_RECEIPT.USER", "사용자발급", "00", "00");

        private final String code;
        private final String codeName;
        private final String kcpCode;
        private final String inicisCode;

        public static CashReceipt findByCode(String code) {
            return Arrays.stream(CashReceipt.values())
                    .filter(cashReceipt -> cashReceipt.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 결제 종류
    @Getter
    @RequiredArgsConstructor
    public enum PaymentType implements CodeCommEnum{
    	CARD("PAY_TP.CARD", "카드"),
        VIRTUAL_BANK("PAY_TP.VIRTUAL_BANK", "가상계좌"),
        BANK("PAY_TP.BANK", "계좌이체"),
    	FREE("PAY_TP.FREE", "무료결제"),
        COLLECTION("PAY_TP.COLLECTION", "외부몰결제"),
        DIRECT("PAY_TP.DIRECT", "직접결제"),
		KAKAOPAY("PAY_TP.KAKAOPAY", "카카오페이"),
    	PAYCO("PAY_TP.PAYCO", "페이코"),
    	NAVERPAY("PAY_TP.NAVERPAY", "네이버페이"),
    	SSPAY("PAY_TP.SSPAY", "삼성페이")
        ;

        private final String code;
        private final String codeName;

        public static PaymentType findByCode(String code) {
 			return Arrays.stream(PaymentType.values())
 		            .filter(paymentType -> paymentType.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }

        public static boolean isSimplePay(String code) {
        	return PaymentType.KAKAOPAY.getCode().equals(code) || PaymentType.PAYCO.getCode().equals(code)
        			|| PaymentType.NAVERPAY.getCode().equals(code) || PaymentType.SSPAY.getCode().equals(code);
        }
    }

    // 정기 결제 카드 등록시 에러 코드 정의
    @Getter
    @RequiredArgsConstructor
    public enum ApplyRegularBatchKey implements MessageCommEnum {
        NEED_LOGIN("NEED_LOGIN", "로그인필요")
        ;

        private final String code;
        private final String message;
    }

    // 주문 완료 가상계좌 정보 조회
    @Getter
    @RequiredArgsConstructor
    public enum virtualAccount implements MessageCommEnum {
        NOT_SAME_USER("NOT_SAME_USER", "본인 주문 아님")
        ;

        private final String code;
        private final String message;
    }

    // 일괄송장 엑셀업로드 에러메시지
    @Getter
    @RequiredArgsConstructor
    public enum OrderBulkTrackingExcelUploadErrMsg implements MessageCommEnum {

    	VALUE_EMPTY("VALUE_EMPTY", "필수값이 없음"),
    	ORDER_DETL_ID_EMPTY("ORDER_DETL_ID_EMPTY", "주문상세 번호가 유효하지 않음"),
    	LOGISTICS_CD_EMPTY("LOGISTICS_CD_EMPTY", "택배사 코드가 유효하지 않음"),
        ORDER_IF("ORDER_IF", "주문I/F 연동 대상이라서 불가"),
        NOT_CHANGEABLE_ORDER_STATUS("NOT_CHANGEABLE_ORDER_STATUS", "변경가능한 상태가 아님");

    	private final String code;
  		private final String message;
    }

    // 주문상태 G : 결제, F : 환불 , A : 추가
    @Getter
    @RequiredArgsConstructor
    public enum PayType implements CodeCommEnum {
        G("G", "결제"),
        F("F", "환불"),
        A("A", "추가"),
        ;

        private final String code;
        private final String codeName;

    }

    // 택배사코드 CJ : CJ택배, LOTTE : LOTTE택배
    @Getter
    @RequiredArgsConstructor
    public enum LogisticsCd implements CodeCommEnum {
        CJ("93", 		"CJ택배", "CJ"),
        LOTTE("178", 	"LOTTE택배", "LOTTE"),
        ;

        private final String code;
        private final String codeName;
        private final String logisticsCode;

    }

    // 클레임 버튼 정의
    @Getter
    @RequiredArgsConstructor
    public enum ClaimButton implements CodeCommEnum {
        CANCEL("CAN", 		"취소"),
        REDELIVERY("RDE", 	"재배송"),
        RETURN("RET", 		"반품"),
        CANAPPROVAL("CAR", 	"취소승인"),
        CANREFUSE("CRF", 	"취소거부"),
        RETAPPROVAL("RAR", 	"반품승인"),
        RETREFUSE("RRF", 	"반품거부"),
        RETCOMPLETE("RCL", 	"반품완료"),
        RETDEFER("RDF", 	"반품보류"),
        DLVCOMPLETE("DCL", 	"배송완료"),
        ;

        private final String code;
        private final String codeName;

        public static ClaimButton findByCode(String code) {
 			return Arrays.stream(ClaimButton.values())
 		            .filter(paymentType -> paymentType.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }
    }

    // 주문상세 상태 이력 메세지
    @Getter
    @RequiredArgsConstructor
    public enum OrderDetailStatusHistMsg implements MessageCommEnum {
        INCOM_COMPLETE_MSG("IC", "결제완료 상태변경"),
        DELIVERY_READY_MSG("DR", "배송준비중 상태변경"),
        DELIVERY_ING_MSG("DI", "[{0}] 송장번호 ({1}) 입력"),
        STORE_DELIVERY_ING_MSG("SDI", "[{0}] 가맹점 ({1}) 확정"),
        DELIVERY_COMPLETE_MSG("DC", "배송완료 상태변경"),
        BUY_FINALIZED_MSG("BF", "구매확정 상태변경"),
        CANCEL_WITHDRAWAL_MSG("CW", "취소철회 상태변경"),
        RETURN_WITHDRAWAL_MSG("RW", "반품철회 상태변경"),
        DELIVERY_DT_CHANGE("DDC", "도착예정일 변경"),
        OUTMALL_ORDER_CREATE("OOC", "외부몰 주문 생성"),
        DELIVERY_ING_CANCEL_DENIAL("CE", "배송중으로 인한 취소거부"),
        CANCEL_DENY_DEFE("CE", "이미 출고되어 취소가 불가합니다."),
        DELIVERY_DT_CHANGE_MSG("DDC", "{0} 주문I/F : {1} / 도착예정일 : {2} ➜\r\n{3} 주문I/F : {4} / 도착예정일 : {5}")
        ;
        private final String code;
        private final String message;

        public static OrderDetailStatusHistMsg findByCode(String code) {
            return Arrays.stream(OrderDetailStatusHistMsg.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // CJ새벽배송 권역 입력 삭제 구분
    @Getter
    @RequiredArgsConstructor
    public enum DwanDeliveryAreaTp implements MessageCommEnum {
        AREA_INSERT("추가", "추가"),
        AREA_DELETE("삭제", "삭제"),
        ;
        private final String code;
        private final String message;

    }

    // 주문복사 에러메세지
    @Getter
    @RequiredArgsConstructor
    public enum OrderCopyErrMsg implements MessageCommEnum {
    	REQUIRED_OD_ORDER_ID("-1", "주문번호 필수"),
    	REQUIRED_ORDER_COPY_LIST("-1", "주문 복사 정보 필수"),
        ORDER_GOODS_COPY_ERROR("-1", "주문 상품 복사 실패")
    	;
    	private final String code;
  		private final String message;
    }

    // CS환불구분
    @Getter
    @RequiredArgsConstructor
    public enum CSRefundTp implements CodeCommEnum {
		PAYMENT_PRICE_REFUND("CS_REFUND_TP.PAYMENT_PRICE_REFUND", "결제금액 환불"),
		POINT_PRICE_REFUND("CS_REFUND_TP.POINT_PRICE_REFUND", "적립금 환불")
        ;
        private final String code;
        private final String codeName;
    }

    // 상품 뎁스
    @Getter
    @RequiredArgsConstructor
    public enum OrderGoodsDepth implements CodeCommEnum {
        ORDER_GOODS_FIRST("ORDER_GOODS_DEPTH.ORDER_GOODS_FIRST", 1,"일반상품, 묶음상품, 골라담기상품"),
        ORDER_GOODS_SECOND("ORDER_GOODS_DEPTH.ORDER_GOODS_SECOND", 2,"묶음 구성상품, 추가구성상품 등"),
        ORDER_GOODS_THIRD("ORDER_GOODS_DEPTH.ORDER_GOODS_THIRD", 3,"재배송 상품")
        ;
        private final String code;
        private final int depthNum;
        private final String codeName;
    }


    // 정렬 방식
    @Getter
    @RequiredArgsConstructor
    public enum OrderOrderByType implements CodeCommEnum {
        ORDER_BY_ORDER("order", "정상주문"),
        ORDER_BY_CLAIM("claim", "클레임주문"),
        ORDER_BY_UNDELIVERED("undelivered", "미출")
        ;
        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum BundleYn implements CodeCommEnum {
        BUNDLE_Y("Y", "합배송"),
        BUNDLE_N("N", "합배송 불가")
        ;
        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PartCancelYn implements CodeCommEnum {
        PART_CANCEL_Y("Y", "부분취소 가능"),
        PART_CANCEL_N("N", "부분취소 불가능")
        ;
        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum TaxYn implements CodeCommEnum {
        TAX_YN_Y("Y", "과세"),
        TAX_YN_N("N", "비과세")
        ;
        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OrderIfType implements CodeCommEnum {
    	ORDER_IF("ORDER_IF_TYPE.ORDER_IF", "주문 I/F", "N"),
    	SAL_IF("ORDER_IF_TYPE.SAL_IF", "매출만 연동", "C")
        ;
        private final String code;
        private final String codeName;
        private final String attr1;
    }

    @Getter
    @RequiredArgsConstructor
    public enum MissEtc implements CodeCommEnum {
        UNPROCESS_MISS("U", "미처리 내역만 조회"),
        RETURN_MISS("R", "반품 미출")
        ;
        private final String code;
        private final String codeName;
    }

    // 주문 선물하기 상태
    @Getter
    @RequiredArgsConstructor
    public enum PresentOrderStatus implements CodeCommEnum {
    	WAIT("PRESENT_ORDER_STATUS.WAIT", "대기"),
    	REJECT("PRESENT_ORDER_STATUS.REJECT", "거절"),
    	EXPIRED("PRESENT_ORDER_STATUS.EXPIRED", "유효기간만료"),
    	RECEIVE_COMPLET("PRESENT_ORDER_STATUS.RECEIVE_COMPLET", "받기완료"),
    	CANCEL("PRESENT_ORDER_STATUS.CANCEL", "취소"),
    	;

    	private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OrderCreateProdServer implements CodeCommEnum {
        PROD_Y("Y", "상용서버"),
        PROD_N("N", "상용서버아님")
        ;
        private final String code;
        private final String codeName;
    }

    // 선물하기 에러 코드
    @Getter
    @RequiredArgsConstructor
    public enum OrderPresentErrorCode implements MessageCommEnum {
    	SUCCESS("SUCCESS", "성공"),
    	EMPTY_ORDER_PRESENT_DATA("EMPTY_ORDER_PRESENT_DATA", "선물확인 데이터 없음"),
    	FAIL_ORDER_PRESENT_AUTH("FAIL_ORDER_PRESENT_AUTH", "선물인증 실패"),
    	FAIL_ORDER_PRESENT_AUTH_5_CNT("FAIL_ORDER_PRESENT_AUTH_5_CNT", "5회 연속 인증 실패"),
    	FAIL_ORDER_PRESENT_AUTH_CAPTCHA("FAIL_ORDER_PRESENT_AUTH_CAPTCHA", "캡차 인증 실패"),
    	FAIL_AUTH_PRESENT_ID("FAIL_AUTH_PRESENT_ID", "선물하기 ID 및 주문PK 인증 실패"),
    	FAIL_SHIPPING_IMPOSSIBLE("FAIL_SHIPPING_IMPOSSIBLE", "배송불가 지역"),
    	FAIL_ADD_SHIPPING_PRICE("FAIL_ADD_SHIPPING_PRICE", "추가 배송비로 인한 배송 불가"),
    	FAIL_IMPOSSIBLE_STATUS_RECEIVE_COMPLET("FAIL_IMPOSSIBLE_STATUS_RECEIVE_COMPLET", "이미 받은 상품"),
    	FAIL_IMPOSSIBLE_STATUS_REJECT("FAIL_IMPOSSIBLE_STATUS_REJECT", "이미 거절된 상품"),
    	FAIL_IMPOSSIBLE_STATUS_EXPIRED("FAIL_IMPOSSIBLE_STATUS_EXPIRED", "만료된 상품"),
    	FAIL_IMPOSSIBLE_STATUS_CANCEL("FAIL_IMPOSSIBLE_STATUS_CANCEL", "이미 취소된 상품"),
    	FAIL_REJECT("FAIL_REJECT", "선물하기 거절 실패"),
    	FAIL_RECEIVE("FAIL_RECEIVE", "선물받기 실패"),
    	FAIL_RE_SEND_MESSAGE_IMPOSSIBLE_STATUS("FAIL_RE_SEND_MESSAGE_IMPOSSIBLE_STATUS", "메시지 재발송 가능 상태 아님"),
    	FAIL_CANCEL_IMPOSSIBLE_STATUS("FAIL_CANCEL_IMPOSSIBLE_STATUS", "선물하기 취소 상태 아님"),
    	OVER_WAREHOUSE_CUTOFF_TIME("OVER_WAREHOUSE_CUTOFF_TIME", "선택하신 도착예정일의 주문이 마감되었습니다."),
    	NO_SEARCH_ARRIVAL_SCHEDULED("NO_SEARCH_ARRIVAL_SCHEDULED", "Request 배송 스케줄 정보 조회 안됨"),
        ;
        private final String code;
        private final String message;
    }

    // 주문상세 배송상태 변경 타입
    @Getter
    @RequiredArgsConstructor
    public enum OrderDetailDeliveryStatusType implements CodeCommEnum {
        INSERT("INSERT", "등록"),
        UPDATE("UPDATE", "수정")
        ;
        private final String code;
        private final String codeName;
    }

    // 녹즙 동기화 배치 타입 정보
    @Getter
    @RequiredArgsConstructor
    public enum GreenJuiceSyncBatchTypeCd implements CodeCommEnum{
        GREENJUICE_SYNC("GREEN_JUICE_SYNC_BATCH_TYPE.GREENJUICE_SYNC", "녹즙 동기화 배치"),
        ;

        private final String code;
        private final String codeName;
    }

    // 렌탈 주문상태
    @Getter
    @RequiredArgsConstructor
    public enum RentalOrderStatus implements CodeCommEnum {

        INCOM_COMPLETE("IC", "상담접수"),
        DELIVERY_READY("DR", "상담준비중"),
        DELIVERY_ING("DI", "계약완료"),
        DELIVERY_COMPLETE("DC", "계약완료"),
        BUY_FINALIZED("BF", "계약완료"),
        CANCEL_COMPLETE("CC", "상담종료")
        ;

        private final String code;
        private final String codeName;

    }

    @Getter
    @RequiredArgsConstructor
    public enum OrderCreateErrMsg implements MessageCommEnum {

        VIRTUAL_BANK_PG_ERROR("VIRTUAL_BANK_PG_ERROR", "가상계좌 PG승인 실패"),
        ;
        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AccessInformation implements CodeCommEnum {
        FRONT_DOOR_PASSWORD("ACCESS_INFORMATION.FRONT_DOOR_PASSWORD", "공동현관 비밀번호 입력"),
        FREE_ACCESS("ACCESS_INFORMATION.FREE_ACCESS", "자유출입가능"),
        CALL_SECURITY_OFFICE("ACCESS_INFORMATION.CALL_SECURITY_OFFICE", "경비실 호출"),
        DELIVERY_SECURITY_OFFICE("ACCESS_INFORMATION.DELIVERY_SECURITY_OFFICE", "경비실로 배송"),
        ETC("ACCESS_INFORMATION.ETC", "기타")
        ;
        private final String code;
        private final String codeName;
        public static AccessInformation findByCode(String code) {
 			return Arrays.stream(AccessInformation.values())
 		            .filter(accessInformation -> accessInformation.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum DeliveryMsgType implements CodeCommEnum {
        ABSENCE("DELIVERY_MSG_TYPE.ABSENCE", "부재 시 문앞에 놓아주세요"),
        SECURITY("DELIVERY_MSG_TYPE.SECURITY", "경비실에맡겨주세요"),
        INPUT("DELIVERY_MSG_TYPE.INPUT", "직접입력"),
        ;
        private final String code;
        private final String codeName;
        public static DeliveryMsgType findByCode(String code) {
 			return Arrays.stream(DeliveryMsgType.values())
 		            .filter(deliveryMsgType -> deliveryMsgType.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }
    }
}