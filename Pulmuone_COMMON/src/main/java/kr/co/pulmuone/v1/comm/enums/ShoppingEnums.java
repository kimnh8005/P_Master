package kr.co.pulmuone.v1.comm.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class ShoppingEnums {

    // 찜
    @Getter
    @RequiredArgsConstructor
    public enum ShoppingBasic implements MessageCommEnum {
        NEED_LOGIN("NEED_LOGIN", "로그인필요")/**/
        ;

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AddCartInfo implements MessageCommEnum {
    	NO_GOODS("NO_GOODS", "상품 정보가 존재하지 않습니다."),
    	NO_GOODS_STATUS_ON_SALE("NO_GOODS_STATUS_ON_SALE", "상품 판매중 상태 아님"),
    	GOODS_LACK_STOCK("GOODS_LACK_STOCK", "상품 재고 수량 보다 장바구니 수량이 더 큰 경우"),
    	NO_ADD_GOODS_STATUS_ON_SALE("NO_ADD_GOODS_STATUS_ON_SALE", "추가 구성 상품 판매중 상태 아님"),
    	ADD_GOODS_LACK_STOCK("ADD_GOODS_LACK_STOCK", "추가 구성 상품 재고 수량 보다 장바구니 수량이 더 큰 경우"),
    	FAIL_VALIDATION("FAIL_VALIDATION", "필수 옵션 및 선택적 필수 옵션 정보 없음"),
    	LACK_LIMIT_MIN_CNT("LACK_LIMIT_MIN_CNT", "최소구매수량보다 적음"),
    	OVER_LIMIT_MAX_CNT("OVER_LIMIT_MAX_CNT", "최대구매수량보다 많음"),
        ORDER_CREATE_FAIL_VALIDATION("ORDER_CREATE_FAIL_VALIDATION", "일반, 폐기임박, 묶음 상품유형만 추가 가능합니다."),
        ORDER_CREATE_NO_GOODS_STATUS_ON_SALE("NO_GOODS_STATUS_ON_SALE", "판매중지 상품은 추가할 수 없습니다."),
        ORDER_CREATE_NO_ADD("ORDER_CREATE_NO_ADD", " 상품은 추가할 수 없습니다."),
        EXIST_DAILY_GOODS("EXIST_DAILY_GOODS", "동일상품이 이미 장바구니에 담겨있습니다."),
        EXIST_DELIVERY_NOT_POSSIBLE_GOODS("EXIST_DELIVERY_NOT_POSSIBLE_GOODS", "배송 불가한 상품이 있습니다."),
        WAREHOUSE_UNDELIVERABLE_AREA("WAREHOUSE_UNDELIVERABLE_AREA", "출고처 배송불가 상품이 포함 되어 있습니다."),
        ;

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AddSimpleCart implements MessageCommEnum {
    	NO_GOODS("NO_GOODS", "상품정보 없음"),
    	EXIST_OPTION("EXIST_OPTION", "옵션 선택 상품")
        ;

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OrderPageInfo implements MessageCommEnum {
    	NO_CART_DATA("NO_CART_DATA", "장바구니 정보 없음"),
    	NOT_BUY_GOODS_INCLUDE("NOT_BUY_GOODS_INCLUDE", "구매불가 상품포함"),
    	IMPOSSIBLE_STORE_SCHEDULE("IMPOSSIBLE_STORE_SCHEDULE", "구매 불가 회차"),
        ;

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ApplyPayment implements MessageCommEnum {
    	SUCCESS("SUCCESS", "성공"),
    	NO_CART_LIST("NO_CART_LIST", "장바구니 정보 없음"),
    	NON_MEMBER_NOT_POINT("NON_MEMBER_NOT_POINT", "비회원 포인트 사용 불가"),
    	ORVER_AVAILABLE_POINT("ORVER_AVAILABLE_POINT", "가용 포인트 보다 많은 포인트 사용"),
    	NOT_BUY_GOODS("NOT_BUY_GOODS", "구매 불가 상품"),
    	GOODS_LACK_STOCK("GOODS_LACK_STOCK", "상품 재고 수량 보다 장바구니 수량이 더 큰 경우"),
    	IMPOSSIBLE_PRESENT("IMPOSSIBLE_PRESENT", "선물하기 불가 상품포함"),
    	LACK_LIMIT_MIN_CNT("LACK_LIMIT_MIN_CNT", "최소 구매수량보다 적음"),
    	OVER_LIMIT_MAX_CNT("OVER_LIMIT_MAX_CNT", "일별&기간별 구매수량 초과"),
    	REQUIRED_NON_MEMBER_EMAIL("REQUIRED_NON_MEMBER_EMAIL", "비회원 이메일 정보 없음"),
    	REQUIRED_NON_MEMBER_CERTIFICATION("REQUIRED_NON_MEMBER_CERTIFICATION", "비회원 본인인증 정보 없음"),
    	NON_MEMBER_UNDER_14_AGE_NOT_ALLOW("NON_MEMBER_UNDER_14_AGE_NOT_ALLOW", "비회원 14세 미만은 주문불가"),
    	REQUIRED_REFUND_ACCOUNT("REQUIRED_REFUND_ACCOUNT", "환불계좌 정보 없음"),
    	REQUIRED_REGULAR_PAYMENT("REQUIRED_REGULAR_PAYMENT", "정기배송 결제 정보 없음"),
    	REQUIRED_REGULAR_INFO("REQUIRED_REGULAR_INFO", "정기배송 정보 없음"),
    	NON_MEMBER_NOT_REGULAR("NON_MEMBER_NOT_REGULAR", "비회원 정기결제 정보 이용 안됨"),
    	FAIL_VALIDATION_COUPON("FAIL_VALIDATION_COUPON", "쿠폰 검증 실패"),
    	USE_DUPLICATE_COUPON("USE_DUPLICATE_COUPON", "중복 쿠폰 사용"),
    	FAIL_VALIDATION_GIFT("FAIL_VALIDATION_GIFT", "증정품 검증 실패"),
    	FAIL_CREATE_ORDER("FAIL_CREATE_ORDER", "주문 생성 실패"),
    	REQUIRED_SHIPPING_ZONE("REQUIRED_SHIPPING_ZONE", "배송지 정보 없음"),
    	FAIL_FREE("FAIL_FREE", "무료결제 실패"),
    	FAIL_FREE_UPDATE("FAIL_FREE_UPDATE", "무료결제 데이터 처리 실패"),
    	EMPTY_SESSION("EMPTY_SESSION", "세션정보 없음"),
    	REQUIRED_STORE_DELIVERY_TYPE("REQUIRED_STORE_DELIVERY_TYPE", "매장배송 배송유형 정보 없음"),
    	REQUIRED_STORE_SCHEDULE_DATE("REQUIRED_STORE_SCHEDULE_DATE", "매장배송 도착일 정보 없음"),
    	REQUIRED_STORE_SCHEDULE_ID("REQUIRED_STORE_SCHEDULE_ID", "매장배송 회차 정보 없음"),
    	REQUIRED_RENTAL_MEMBER_NAME("REQUIRED_RENTAL_MEMBER_NAME", "렌탈 구매자명 정보 없음"),
    	REQUIRED_RENTAL_MEMBER_MOBILE("REQUIRED_RENTAL_MEMBER_MOBILE", "렌탈 구매자 휴대 전화 정보 없음"),
    	REQUIRED_RENTAL_MEMBER_EMAIL("REQUIRED_RENTAL_MEMBER_EMAIL", "렌탈 구매자 이메일 정보 없음"),
        FAIL_UPDATE_STOCK("FAIL_UPDATE_STOCK", "재고처리 차감 실패"),
        IMPOSSIBLE_STORE_SCHEDULE("IMPOSSIBLE_STORE_SCHEDULE", "구매 불가 회차"),
        FAIL_RECEIVER_ADDRSS2_LENGTH("FAIL_RECEIVER_ADDRSS2_LENGTH", "상세주소는 최대 30자까지 입력 가능합니다."),
        FAIL_RECEIVER_ADDRSS2_LENGTH_BOS("FAIL_RECEIVER_ADDRSS2_LENGTH_BOS", "상세주소는 최대 50자까지 입력 가능합니다."),
        REQUIRED_INCORPOREITY_RECEVE_NAME("REQUIRED_INCORPOREITY_RECEVE_NAME", "무형 받는사람명 정보 없음"),
        REQUIRED_INCORPOREITY_RECEVE_MOBILE("REQUIRED_INCORPOREITY_RECEVE_MOBILE", "무형 받는사람 휴대 전화 정보 없음"),
        ;

        private final String code;
        private final String message;
    }

    @Getter
	@RequiredArgsConstructor
	public enum CartType implements CodeCommEnum {
    	NORMAL("CART_TYPE.NORMAL", "일반배송", Arrays.asList("DELIVERY_TYPE.NORMAL","DELIVERY_TYPE.RESERVATION","DELIVERY_TYPE.DAILY")),
    	REGULAR("CART_TYPE.REGULAR", "정기배송", Arrays.asList("DELIVERY_TYPE.REGULAR")),
    	SHOP("CART_TYPE.SHOP", "매장배송", Arrays.asList("DELIVERY_TYPE.SHOP_DELIVERY", "DELIVERY_TYPE.SHOP_PICKUP")),
    	RENTAL("CART_TYPE.RENTAL", "렌탈", Arrays.asList("DELIVERY_TYPE.RENTAL")),
    	INCORPOREITY("CART_TYPE.INCORPOREITY", "무형", Arrays.asList("DELIVERY_TYPE.INCORPOREITY")),
        ;

        private final String code;
        private final String codeName;
        private final List<String> deliveryTypeList;

        public static CartType findByCode(String code) {
			return Arrays.stream(CartType.values())
		            .filter(cartType -> cartType.getCode().equals(code))
		            .findAny()
		            .orElse(null);

        }

        public static CartType findByDeliveryCode(String deliveryCcode) {
			return Arrays.stream(CartType.values())
		            .filter(cartType -> cartType.hasDeliveryCode(deliveryCcode))
		            .findAny()
		            .orElse(null);

        }

        public boolean hasDeliveryCode(String deliveryCcode) {
        	return deliveryTypeList.stream()
                    .anyMatch(deliveryType -> deliveryType.equals(deliveryCcode));
        }
    }

    @Getter
	@RequiredArgsConstructor
	public enum DeliveryType implements CodeCommEnum {
    	NORMAL("DELIVERY_TYPE.NORMAL", "일반배송","SALE_TYPE.NORMAL"),
    	RESERVATION("DELIVERY_TYPE.RESERVATION", "예약배송", "SALE_TYPE.RESERVATION"),
    	REGULAR("DELIVERY_TYPE.REGULAR", "정기배송", "SALE_TYPE.REGULAR"),
    	DAILY("DELIVERY_TYPE.DAILY", "일일배송", "SALE_TYPE.DAILY"),
    	RENTAL("DELIVERY_TYPE.RENTAL", "렌탈", ""),
    	SHOP_DELIVERY("DELIVERY_TYPE.SHOP_DELIVERY", "매장배송", "SALE_TYPE.SHOP"),
    	SHOP_PICKUP("DELIVERY_TYPE.SHOP_PICKUP", "매장픽업", "SALE_TYPE.SHOP"),
    	INCORPOREITY("DELIVERY_TYPE.INCORPOREITY", "무형", ""),
        ;

        private final String code;
        private final String codeName;
        private final String saleType;

        public static DeliveryType findByCode(String code) {
			return Arrays.stream(DeliveryType.values())
		            .filter(deliveryType -> deliveryType.getCode().equals(code))
		            .findAny()
		            .orElse(null);
        }

    }


    //구매 약관코드
    @Getter
	@RequiredArgsConstructor
	public enum ClauseGroupCode implements CodeCommEnum {
    	REGULAR_DELIVERY_TERMS("REGULAR_DELIVERY_TERMS", "정기배송 이용약관"),
    	PURCHASE_TERMS("PURCHASE_TERMS", "구매약관"),
    	PRIVACY_POLICY("PRIVACY_POLICY", "개인정보처리방침"),
    	NON_MEMBER_PRIVACY_POLICY("NON_MEMBER_PRIVACY_POLICY", "비회원 개인정보 수집 및 이용동의")
        ;

        private final String code;
        private final String codeName;
    }

    //장바구니 배송지 코드
    @Getter
	@RequiredArgsConstructor
	public enum CartShippingAddress implements CodeCommEnum {
    	ADD("ADD", "장바구니 배송지 추가"),
    	PUT("PUT", "장바구니 배송지 수정"),
    	SELECT("SELECT", "장바구니 배송지 선택")
        ;

        private final String code;
        private final String codeName;
    }

    // 주문 사용 가능 쿠폰 조회
    @Getter
    @RequiredArgsConstructor
    public enum GetCouponPageInfo implements MessageCommEnum {
    	EMPLOYEE_NOT_ALLOW("EMPLOYEE_NOT_ALLOW", "임직원 쿠폰 이용 불가")
        ;

        private final String code;
        private final String message;
    }

    //카트 프로모션 코드
    @Getter
    @RequiredArgsConstructor
    public enum CartPromotionType implements CodeCommEnum {
        EXHIBIT_SELECT("CART_PROMOTION_TP.EXHIBIT_SELECT", "기획전 유형 골라담기(균일가)"),
        GREENJUICE_SELECT("CART_PROMOTION_TP.GREENJUICE_SELECT", "녹즙, 내맘대로 주문")
        ;

        private final String code;
        private final String codeName;
    }

    //임직원 구매 여부
    @Getter
    @RequiredArgsConstructor
    public enum CartEmployeeYn implements CodeCommEnum {
		EMPLOY_POINT_DISCOUNT("Y", "임직원 포인트 기준으로 계산"),
        IGNORE_EMPLOY_POINT_DISCOUNT("S", "임직원 포인트 관계 없이 할인 계산"),
        NO_EMPLOY_DISCOUNT("N", "임직원 구매 안함")
        ;

        private final String code;
        private final String codeName;
    }

    //회원 구매 권한 타입
    @Getter
    @RequiredArgsConstructor
    public enum BuyPurchaseType implements CodeCommEnum {
    	BUY_POSSIBLE("BUY_PURCHASE_TYPE.BUY_POSSIBLE", "구매가능"),
    	NOT_BUY_BUYER("BUY_PURCHASE_TYPE.NOT_BUY_BUYER", "회원구매불가"),
    	NOT_BUY_EMPLOYEE("BUY_PURCHASE_TYPE.NOT_BUY_EMPLOYEE", "임직원구매불가"),
    	BUY_IMPOSSIBLE("BUY_PURCHASE_TYPE.BUY_IMPOSSIBLE", "구매불가")
        ;

        private final String code;
        private final String codeName;
    }

    //증정품 지급상태
    @Getter
    @RequiredArgsConstructor
    public enum GiftState implements CodeCommEnum {
        X("X", "증정품 재고 부족 + 모든 증정품 재고 없음"),
        S("S", "증정품 재고 부족 + 다른 증정품 재고 있음")
        ;

        private final String code;
        private final String codeName;
    }
}