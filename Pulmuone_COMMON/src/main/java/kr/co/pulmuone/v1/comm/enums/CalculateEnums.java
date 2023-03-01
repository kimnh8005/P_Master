package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CalculateEnums {

    @Getter
    @RequiredArgsConstructor
    public enum ErpInterfaceId implements CodeCommEnum {
        CUST_SETTLE_EMPLOYEE_INTERFACE_ID("IF_EMP_INVOICE_INP", "임직원지원금 AP 송장 발행 입력")
        , SALES_CONFIRM_SEARCH_INTERFACE_ID("IF_SAL_SRCH", "매출 확정 조회")
        , SALES_CONFIRM_FLAG_INTERFACE_ID("IF_SAL_FLAG", "매출 확정완료 조회")
        ;

        private final String code;
        private final String codeName;
    }

}
