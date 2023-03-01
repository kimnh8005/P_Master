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
public class SendEnums {

    // Email 템플릿 설정
    @Getter
    @RequiredArgsConstructor
    public enum Email implements MessageCommEnum {
        SUCCESS("SUCCESS", "성공")					// 기존에 사용하던 코드
        , FAIL("FAIL", "관리자에게 문의하세요.")		// 기존에 사용하던 코드
        , VALID_ERROR("VALID_ERROR", "데이터가 유효하지 않습니다.")	// 신규 추가 코드
        , DUPLICATE_DATA("DUPLICATE_DATA", "중복된 데이터가 존재합니다.")	// 신규 추가 코드
        , FOREIGN_KEY_DATA("FOREIGN_KEY_DATA", "다른테이블에서 사용중입니다.")	// 신규 추가 코드
        , PROGRAM_ERROR("PROGRAM_ERROR", "프로그램 오류입니다.")	// 신규 추가 코드
        , MENU_ERROR("MENU_ERROR", "메뉴 오류입니다.")	// 신규 추가 코드
        , MANDATORY_MISSING("MANDATORY_MISSING", "필수값을 입력해주세요.")	//신규 추가 코드
        , NEED_LOGIN("NEED_LOGIN", "로그인이 필요합니다.")	//신규 추가 코드
        ;

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AppPushSendType implements CodeCommEnum {
        TEXT("APP_PUSH_SEND_TYPE.TEXT", "텍스트"),
        TEXT_IMAGE("APP_PUSH_SEND_TYPE.TEXT_IMAGE", "텍스트+이미지");

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AppPushValidation implements MessageCommEnum {
        SUCCESS("SUCCESS", "성공")
        , NO_USER_SELECTED("NO_USER_SELECTED", "선택한 회원이 없습니다.")
        , REQUIRED_ATTACHMENT("REQUIRED_ATTACHMENT","푸시발송타입이 텍스트 이미지 일 경우 첨부파일은 필수입니다.")
        , REQUIRED_RESERVATION_DATE("REQUIRED_RESERVATION_DATE","발송구분이 예약 일 경우 예약일시는 필수입니다.")
        ;

        private final String code;
        private final String message;

    }

    // 사용여부
    @Getter
    public enum UseYn {
        Y("1", true),
        N("0", false);

        private final String numberCode;
        private final boolean primitiveCode;

        UseYn(String numberCode, boolean primitiveCode){
            this.numberCode = numberCode;
            this.primitiveCode = primitiveCode;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum AppOsType implements CodeCommEnum {
        IOS("APP_OS_TYPE.IOS", "IOS"),
        ANDROID("APP_OS_TYPE.ANDROID", "Android");

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PushSendType implements CodeCommEnum {
        ADVERT("PUSH_SEND_TYPE.ADVERT", "광고"),
        NOTICE("PUSH_SEND_TYPE.NOTICE", "공지");

        private final String code;
        private final String codeName;
    }


    @Getter
    @RequiredArgsConstructor
    public enum SendTemplateCode implements CodeCommEnum {
        BOS_FIND_PASSWORD("bosFindPassword", "관리자 비밀번호 재설정"),
        BUYER_FIND_PASSWORD("buyerFindPassword", "구매자 비밀번호 재설정"),
        SIGN_UP_COMPLETED("signUpCompleted", "회원가입 완료"),
        BOS_SIGN_UP_COMPLETED("bosSignUpCompleted", "관리자 가입 완료"),
        USER_DROP_INFO("userDropInfo", "회원탈퇴 완료"),
        BUYER_STOP_CONVERT_INFO("buyerStopConvertInfo", "정지회원 전환 안내"),
        BUYER_NORMAL_CONVERT_INFO("buyerNormalConvertInfo", "정상회원 전환 안내"),
        EMPLOYEE_CERTIFICATION_INFO("employeeCertificationInfo", "임직원회원 인증"),
        USER_DORMANT_COMPLETED("userDormantCompleted", "휴면계정 전환 완료"),
        USER_DORMANT_EXPECTED("userDormantExpected", "휴면계정 전환 예정"),
        MARKETING_INFO("maketingInfo", "마케팅 정보 수신동의"),
        POINT_EXPECT_EXPIRED("pointExpectExpired", "적립금 소멸 예정 안내"),
        ONETOONE_QNA_ADD_COMPLETED("onetooneQnaAddCompleted", "1:1문의 접수완료"),
        ONETOONE_QNA_ANSWER_COMPLETED("onetooneQnaAnswerCompleted", "1:1문의 답변 완료"),
        PRODUCT_QNA_ANSWER_COMPLETED("productQnaAnswerCompleted", "상품문의 답변 완료"),
        ORDER_RECEIVED_COMPLETED("orderReceivedCompleted", "주문 접수 완료"),
        ORDER_PAYMENT_COMPLETED("orderPaymentCompleted", "주문 결제 완료"),
        ORDER_PRESENT_MASSEGE_SEND("orderPresentMassegeSend", "선물하기 받는사람 MSG 보내기"),
        ORDER_PRESENT_REJECT("orderPresentReject", "선물하기 거절 보내는 사람 MSG 보내기"),
        ORDER_PRESENT_EXPIRED("orderPresentExpired", "선물하기 만료"),
        ORDER_DEPOSIT_COMPLETED("orderDepositCompleted", "주문 입금 완료"),
        ORDER_REGULAR_PAYMENT_COMPLETE("orderRegularPaymentComplete", "정기 주문 결제 완료"),
        ORDER_GOODS_DELIVERY("orderGoodsDelivery", "상품 발송"),
        ORDER_CANCEL_COMPLETED("orderCancelCompleted", "주문 취소 완료"),
        ORDER_CANCEL_BEFORE_DEPOSIT("orderCancelBeforeDeposit", "주문 취소(입금 전 취소)"),
        ORDER_RETURN_COMPLETED("orderReturnCompleted", "주문 반품 완료"),
        ORDER_REGULAR_APPLY_COMPLETED("orderRegularApplyCompleted", "정기배송 신청 완료"),
        ORDER_REGULAR_PAYMENT_FAIL_FIRST("orderRegularPaymentFailFirst", "정기배송 결제 실패(1차)"),
        ORDER_REGULAR_PAYMENT_FAIL_SECOND("orderRegularPaymentFailSecond", "정기배송 결제 실패(2차)"),
        ORDER_REGULAR_PAYMENT_FAIL_FOURTH("orderRegularPaymentFailFourth", "정기배송 결제 실패(4차)"),
        ORDER_REGULAR_CANCEL_COMPLETED("orderRegularCancelCompleted", "정기배송 취소 완료"),
        ORDER_REGULAR_GOODS_SKIP_COMPLETED("orderRegularGoodsSkipCompleted", "정기배송 상품 건너뛰기 완료"),
        ORDER_REGULAR_REQ_ROUND_SKIP_COMPLETED("orderRegularReqRoundSkipCompleted", "정기배송 회차 건너뛰기 완료"),
        ORDER_REGULAR_CREATION_COMPLETED("orderRegularCreationCompleted", "정기배송 주문 생성 완료"),
        ORDER_REGULAR_EXPIRE_EXPECTED("orderRegularExpireExpected", "정기배송 만료 예정"),
        ORDER_REGULAR_EXPIRED("orderRegularExpired", "정기배송 만료"),
        ORDER_DAILY_GREENJUICE_END("orderDailyGreenJuiceEnd", "녹즙 일일배송 종료"),
        ORDER_REGULAR_GOODS_PRICE_CHANGE("orderRegularGoodsPriceChange", "정기배송 상품금액 변동 안내"),
        GOODS_STOCK_DISPOSAL("goodsStockDisposal", "임박/폐기 품목 안내"),
        BOS_ORDER_STATUS_NOTIFICATION("bosOrderStatusNotification", "BOS 주문상태 알림"),
        DIRECT_SHIPPING_UNREGISTERED_INVOICE_NOTIFICATION("directShippingUnregisteredInvoiceNotification", "직접배송 미등록 송장 알림"),
        BOS_COLLECTIONMALL_INTERFACE_FAIL_NOTIFICATION("bosCollectionMallInterfaceFailNotification", "BOS 수집몰 연동 실패 알림"),
        BOS_ORGA_CAUTION_ORDER_NOTIFICATION("BosOrgaCautionOrderNotification", "BOS 올가 식품안전팀 주의주문 발생 알림"),
        PAY_ADDITIONAL_SHIPPING_PRICE("PayAdditionalShippingPrice", "부분취소 배송비 추가결제 가상계좌 발급"),
    	ORDER_SHOP_PICKUP_GOODS_DELIVERY("orderShopPickupGoodsDelivery", "매장상품 준비"),
        GOODS_RESTOCK_INFO("goodsRestockInfo", "상품 재입고 안내"),
        BOS_TWO_FACTOR_AUTHENTIFICATION("bosTwoFactorAuthentification", "관리자 이차인증"),
        REWARD_APPLY_COMPENSATION("rewardApplyCompensation", "보상제 답변 확인중"),
        REWARD_APPLY_COMPLETE("rewardApplyComplete", "보상제 처리완료(보상완료)"),
        REWARD_APPLY_DENIED_COMPLETE("rewardApplyDeniedComplete", "보상제 처리완료(보상불가)"),
        ;

        private final String code;
        private final String codeName;
    }

    /*
    Naver Cloud Platform - 메시지 발송 - 응답 값 정의
    참고 : https://apidocs.ncloud.com/ko/ai-application-service/sens/sms_v2/
     */
    @Getter
    @RequiredArgsConstructor
    public enum SendNcpSmsResponseCode implements CodeCommEnum {
        ACCEPT("202", "Accept (요청 완료)"),
        BAD_REQUEST("400", "Bad Request"),
        UNAUTHORIZED("401", "Unauthorized"),
        FORBIDDEN("403", "Forbidden"),
        NOT_FOUND("404", "Not Found"),
        TOO_MANY_REQUESTS("429", "Too Many Requests"),
        INTERNAL_SERVER_ERROR("500", "Internal Server Error")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SendNcpSmsType implements CodeCommEnum {
        SMS("SMS", "SMS"),
        LMS("LMS", "LMS"),
        MMS("MMS", "MMS")
        ;

        private final String code;
        private final String codeName;
    }

    // 메시지 발송 대상 유형 ST_COMN_CODE.ST_COMN_CODE_MST_CD
    @Getter
    @RequiredArgsConstructor
    public enum SendTargetType implements CodeCommEnum {
        FULFILLMENT("CM_IF_FAIL_MAIL_LIST", "풀필먼트팀 리스트"),
        CAUTION_ORDER_MAIL_LIST("CAUTION_ORDER_MAIL_LIST", "올가 식품안전팀 메일 수신 대상자")
        ;

        private final String code;
        private final String codeName;
    }

    // 올가 식품안전팀 주의 주문 조건1 - 수취인명, 배송지 상세주소 대상
    @Getter
    @RequiredArgsConstructor
    public enum OrgaCautionOrderTargetByName implements CodeCommEnum {
        TARGET1("TARGET1", "식품"),
        TARGET2("TARGET2", "농산"),
        TARGET3("TARGET3", "축산"),
        TARGET4("TARGET4", "수산"),
        TARGET5("TARGET5", "시청"),
        TARGET6("TARGET6", "보건"),
        TARGET7("TARGET7", "연구"),
        TARGET8("TARGET8", "검사"),
        TARGET9("TARGET9", "소비"),
        TARGET10("TARGET10", "방송"),
        TARGET11("TARGET11", "검찰"),
        TARGET12("TARGET12", "지검"),
        ;

        private final String code;
        private final String codeName;
    }

    // 올가 식품안전팀 주의 주문 조건2 - 주문자 email 주소
    @Getter
    @RequiredArgsConstructor
    public enum OrgaCautionOrderTargetByEmail implements CodeCommEnum {
        TARGET1("TARGET1", "go.kr"),
        TARGET2("TARGET2", "or.kr")
        ;

        private final String code;
        private final String codeName;
    }

}
