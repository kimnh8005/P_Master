package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class StaticsEnums {

    @Getter
    @RequiredArgsConstructor
    public enum SaleSataticsMessage implements MessageCommEnum {
        // 판매통계
        SALE_STATICS_MANAGE_SUCCESS("0000", "정상처리 되었습니다."),
        SALE_STATICS_PARAM_NO_INPUT("1001", "입력정보가 존재하지 않습니다."),
        SALE_STATICS_PARAM_NO_SEARCH_TP("1002", "검색기준이 존재하지 않습니다.");

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OutboundSearchType implements CodeCommEnum {
        WAREHOUSE("WAREHOUSE", "출고처 기준"),
        SELLERS("SELLERS", "판매처 기준");

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OutboundSearchDateType implements CodeCommEnum {
        ORDER_DT("ORDER_DT", "주문일자"),
        IC_DT("IC_DT", "결제완료일자"),
        IF_DT("IF_DT", "주문I/F일자"),
        DI_DT("DI_DT", "배송중일자");

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum dataDownloadStaticsType implements CodeCommEnum {
        ONLINE_MEMBER_RESERVE_BALANCE("01", "적립금 회원 잔액", "남은적립금"),
        EMPLOYEE_MEMBER_RESERVE_BALANCE("02", "적립금 임직원 잔액", "남은적립금"),
        SETTLEMENT_POINT("03", "적립금 정산", "사용금액"),
        SETTLEMENT_COUPON("04", "쿠폰 정산", "사용금액"),
        INTERNAL_ACCOUNTING_COUPON_PAYMENT("05", "내부회계통제용 쿠폰 지급", "최대할인금액"),
        USE_COUPON_COST("06", "쿠폰비용 사용", "쿠폰비용"),
        USE_RESERVE_COST("07", "적립금비용 사용", "적립금사용"),
        EMPLOYEE_DISCOUNT_SUPPORT("08", "임직원 할인지원액", "사용한도액"),
        DISPOSAL_DATE_DISTRIBUTION_PERIOD("09", "용인물류 품목별 폐기 기준", "실재고"),
        CUSTOMER_PRICE_COST("10", "객단가", "주문금액");

        private final String code;
        private final String codeName;
        private final String sumTargetName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum categoryType implements CodeCommEnum {
        STANDARD("S", "표준 카테고리"),
        DISPLAY("D", "전시 카테고리"),
        ERP("E", "ERP");

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum searchType implements CodeCommEnum {
        ORDER_DATE("ODR", "주문일"),
        PAYMENT_DATE("PAY", "결제일"),
        SALES_DATE("SAL", "매출일");

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum useType implements CodeCommEnum {
        PC("pc", "PC"),
        MOBILE("mobile", "Mobile"),
        APP("app","App");

        private final String code;
        private final String codeName;
    }

}
