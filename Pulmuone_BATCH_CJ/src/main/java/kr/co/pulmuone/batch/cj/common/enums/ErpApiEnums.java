package kr.co.pulmuone.batch.cj.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ErpApiEnums {

    @Getter
    @RequiredArgsConstructor
    public enum ErpServiceType {
        ERP_NORMAL_DELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_NORMAL_DELIVERY_ORDER", "용인물류 일반배송주문"),
        ERP_DAWN_DELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_DAWN_DELIVERY_ORDER", "용인물류 새벽배송주문"),
        ERP_CJ_ORDER("ERP_SERVICE_TYPE.ERP_CJ_ORDER", "백암물류 주문"),
        ERP_ORGA_STORE_DELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_ORGA_STORE_DELIVERY_ORDER", "올가 매장배송주문"),
        ERP_ORGA_ETC_ORDER("ERP_SERVICE_TYPE.ERP_ORGA_ETC_ORDER", "올가 기타주문"),
        ERP_HITOK_NORMAL_DELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_HITOK_NORMAL_DELIVERY_ORDER", "하이톡 택배배송 주문"),
        ERP_HITOK_DAILY_DELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_HITOK_DAILY_DELIVERY_ORDER", "하이톡 일일배송 주문"),
        ERP_LOHAS_DIRECT_SALE_ORDER("ERP_SERVICE_TYPE.ERP_LOHAS_DIRECT_SALE_ORDER", "풀무원건강생활(LDS) 주문"),
        ERP_BABYMEAL_DAILY_ORDER("ERP_SERVICE_TYPE.ERP_BABYMEAL_DAILY_ORDER", "베이비밀 일일배송 주문"),
        ERP_BABYMEAL_NORMAL_ORDER("ERP_SERVICE_TYPE.ERP_BABYMEAL_NORMAL_ORDER", "베이비밀 택배배송 주문"),
        ERP_SALES_ORDER_FOOD("ERP_SERVICE_TYPE.ERP_SALES_ORDER_FOOD", "매출 주문 (풀무원식품)"),
        ERP_SALES_ORDER_ORGA("ERP_SERVICE_TYPE.ERP_SALES_ORDER_ORGA", "매출 주문 (올가홀푸드)"),
        ERP_RETURN_SALES_ORDER_FOOD("ERP_SERVICE_TYPE.ERP_RETURN_SALES_ORDER_FOOD", "반품매출 주문 (풀무원식품)"),
        ERP_RETURN_SALES_ORDER_ORGA("ERP_SERVICE_TYPE.ERP_RETURN_SALES_ORDER_ORGA", "반품매출 주문 (올가홀푸드)"),
        ERP_HITOK_NORMAL_DELIVERY_RETRUN_ORDER("ERP_SERVICE_TYPE.ERP_HITOK_NORMAL_DELIVERY_RETRUN_ORDER", "하이톡 택배배송 반품 주문"),
        ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER("ERP_SERVICE_TYPE.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER", "풀무원건강생활(LDS) 반품 주문"),
        ERP_CJ_SALES_ORDER_FOOD("ERP_SERVICE_TYPE.ERP_CJ_SALES_ORDER_FOOD", "CJ(백암)물류 매출 주문 (풀무원식품)"),
        ERP_EATSSLIM_ORDER("ERP_SERVICE_TYPE.ERP_EATSSLIM_ORDER", "잇슬림 일일배송 주문"),
        ERP_EATSSLIM_NORMAL_DELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_EATSSLIM_NORMAL_DELIVERY_ORDER", "잇슬림 택배배송 주문")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum UrWarehouseId {
        YONGIN_LOGISTICS("WAREHOUSE_YONGIN_ID", "용인물류"),
        BAEKAM_LOGISTICS("WAREHOUSE_BAEKAM_ID", "백암물류"),
        ORGA_STORE("WAREHOUSE_ORGA_STORE_ID", "올가매장"),
        HITOK_NORMAL("WAREHOUSE_HITOK_NORMAL_ID", "녹즙 택배"),
        HITOK_DAILY("WAREHOUSE_HITOK_DAILY_ID", "녹즙 일배"),
        DOAN_D1("WAREHOUSE_DOAN_D1_ID", "도안D1"),
        DEOKSANG_3PL("WAREHOUSE_DEOKSANG_3PL_ID", "덕상리3PL"),
        BABYMEAL_D2_FRANCHISEE("WAREHOUSE_BABYMEAL_D2_FRANCHISEE_ID", "베이비밀 D2(가맹점)"),
        BABYMEAL_D4_FRANCHISEE("WAREHOUSE_BABYMEAL_D4_FRANCHISEE_ID", "베이비밀 D4(가맹점)"),
        BABYMEAL_D2_DELIVERY("WAREHOUSE_BABYMEAL_D2_DELIVERY_ID", "베이비밀 D2(도안택배)"),
        EATSSLIM_D3_FRANCHISEE("WAREHOUSE_EATSSLIM_D3_FRANCHISEE_ID", "잇슬림 D3(가맹점)"),
        EATSSLIM_D2_3PL("WAREHOUSE_EATSSLIM_D2_3PL_ID", "잇슬림 D2(3PL택배)"),
        EATSSLIM_D3_DELIVERY("WAREHOUSE_EATSSLIM_D3_DELIVERY_ID", "잇슬림 D3(도안택배)")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpDelvType {
        DAILY_DELIVERY("0001", "일배"),
        NORMAL_DELIVERY("0002", "택배")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrderHpnCd {
        WEB_CUSTOMER("0001", "웹-고객"),
        MANAGER("0002", "관리자주문"),
        WEB_EMPLOYEE("0003", "웹-임직원"),
        MOBILE_CUSTOMER("0023", "모바일-고객"),
        MOBILE_EMPLOYEE("0024", "모바일-임직원")
        ;

        private final String code;
        private final String codeName;
    }
}
