package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ErpApiEnums {

    @Getter
    @RequiredArgsConstructor
    public enum ErpInterfaceId implements CodeCommEnum {
        CUST_ORDER_INSERT_INTERFACE_ID("IF_CUSTORD_INP", "주문/취소"),
        CUST_ORDER_SEARCH_INTERFACE_ID("IF_CUSTORD_SRCH", "주문/취소 조회"),
        CUST_ORDER_FLAG_INTERFACE_ID("IF_CUSTORD_FLAG", "주문/취소 조회 완료"),
        ORDER_ETC_INSERT_INTERFACE_ID("IF_ORDETC_INP", "기타주문"),
        TRACKINGNUMBER_SEARCH_INTERFACE_ID("IF_DLV_SRCH", "송장 조회"),
        TRACKINGNUMBER_FLAG_INTERFACE_ID("IF_DLV_FLAG", "송장 조회완료"),
        TRACKINGNUMBER_ETC_SEARCH_INTERFACE_ID("IF_ORDETC_DLV_SRCH", "기타송장 조회"),
        TRACKINGNUMBER_ETC_FLAG_INTERFACE_ID("IF_ORDETC_DLV_FLAG", "기타송장 조회완료"),
        UNRELEASED_SEARCH_INTERFACE_ID("IF_MIS_SRCH", "미출 조회"),
        UNRELEASED_FLAG_INTERFACE_ID("IF_MIS_FLAG", "미출 조회완료")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpServiceType implements CodeCommEnum {
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
        ERP_HITOK_DAILY_DELIVERY_RETRUN_ORDER("ERP_SERVICE_TYPE.ERP_HITOK_DAILY_DELIVERY_RETRUN_ORDER", "하이톡 일일배송 반품 주문"),
        ERP_HITOK_DAILY_DELIVERY_REDELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_HITOK_DAILY_DELIVERY_RETRUN_ORDER", "하이톡 일일배송 재배송 주문"),
        ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER("ERP_SERVICE_TYPE.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER", "풀무원건강생활(LDS) 반품 주문"),
        ERP_CJ_SALES_ORDER_FOOD("ERP_SERVICE_TYPE.ERP_CJ_SALES_ORDER_FOOD", "CJ(백암)물류 매출 주문 (풀무원식품)"),
        ERP_EATSSLIM_ORDER("ERP_SERVICE_TYPE.ERP_EATSSLIM_ORDER", "잇슬림 일일배송 주문"),
        ERP_EATSSLIM_NORMAL_DELIVERY_ORDER("ERP_SERVICE_TYPE.ERP_EATSSLIM_NORMAL_DELIVERY_ORDER", "잇슬림 택배배송 주문"),
        ERP_SALES_ONLY_ORDER_FOOD("ERP_SERVICE_TYPE.ERP_SALES_ORDER_FOOD", "매출만 연동 주문 (풀무원식품)"),
        ERP_SALES_ONLY_ORDER_ORGA("ERP_SERVICE_TYPE.ERP_SALES_ORDER_ORGA", "매출만 연동 주문 (올가홀푸드)")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum UrWarehouseId implements CodeCommEnum {
        YONGIN_LOGISTICS("WAREHOUSE_YONGIN_ID", "용인물류"),
        BAEKAM_LOGISTICS("WAREHOUSE_BAEKAM_ID", "백암물류"),
        ORGA_STORE("WAREHOUSE_STORE_ID", "올가매장"),
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
    public enum UrWarehouseGroupId implements CodeCommEnum {
        OWN("WAREHOUSE_GROUP.OWN", "온라인사업부"),
        ACCOUNT_ORGA("WAREHOUSE_GROUP.ACCOUNT_Orga", "산지직송"),
        ACCOUNT("WAREHOUSE_GROUP.ACCOUNT", "관계사"),
        ACCOUNT_DS("WAREHOUSE_GROUP.ACCOUNT_DS", "DS택배"),
        ACCOUNT_FDD("WAREHOUSE_GROUP.ACCOUNT_FDD", "FDD택배"),
        ACCOUNT_PDM("WAREHOUSE_GROUP.ACCOUNT_PDM", "PDM택배"),
        ACCOUNT_DIRECT("WAREHOUSE_GROUP.ACCOUNT_DIRECT", "직배")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpShpCode implements CodeCommEnum {
        PULMUONE("01", "풀무원"),
        ORGA("02", "올가")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpDlvGrpType implements CodeCommEnum {
        NORMAL_ORDER("일반", "일반주문"),
        DELIVERY_ORDER("택배", "택배주문"),
        SPECIAL_ORDER("특판", "특판주문")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrderDeadlineTime implements CodeCommEnum {
        YONGIN_NORMAL("1800", "18:00"),
        YONGIN_DAWN("1900", "19:00")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpLineCategoriCode implements CodeCommEnum {
        LINE_ORDER("ORDER", "주문코드"),
        LINE_RETURN("RETURN", "반품코드")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpFoodOrderLineType implements CodeCommEnum {
        LINE_STANDARD("STANDARD", "일반거래"),
        LINE_SHIP_ONLY("SHIP-ONLY", "미거래-증정품"),
        LINE_RETURN("RETURN", "반품")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrgaOrderLineType implements CodeCommEnum {
        NO_LINE_NORMAL("NO_일반매출", "일반매출"),
        NO_LINE_NORMAL_GIFT("NO_일반매출_증정", "일반매출 증정"),
        NO_LINE_NORMAL_RETURN("NO_일반매출_반품", "일반매출 반품"),
        NO_LINE_NORMAL_DIRECT("NO_일반매출_직송", "일반매출 직송"),
        NO_LINE_NORMAL_DIRECT_CANCEL("NO_일반매출_직송_취소", "일반매출 직송 취소"),
        NO_LINE_VIRTUAL_WAREHOUSE_RELEASE("NO_가상창고출고", "가상창고출고")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrderPoType implements CodeCommEnum {
        NORMAL_DELIVERY("50", "일반배송"),
        DAWN_DELIVERY("190", "새벽배송"),
        DIRECT_DELIVERY("100", "신지직송")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpSalesChannelType implements CodeCommEnum {
        OUTMALL_ORDER("MIM", "외부몰주문"),
        OUTMALL_EXCEPT_ORDER("일반", "외부몰제외주문")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpFoodOrderLineId implements CodeCommEnum {
        ORDER_LINE_ID("1214", "주문ID"),
        RETURN_LINE_ID("1002", "반품ID"),
        SALES_LINE_ID("1001", "매출ID")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrgaOrderLineId implements CodeCommEnum {
        ORDER_LINE_ID("1646", "주문ID"),
        RETURN_LINE_ID("1650", "반품ID"),
        DIRECT_DELIVERY_ORDER_LINE_ID("1647", "산지직송주문ID"),
        DIRECT_DELIVERY_RETURN_LINE_ID("1652", "산지직송반품ID")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrderType implements CodeCommEnum {
        NORMAL_ORDER("일반주문", "일반주문"),
        NO_SHOPPING_MALL("NO_쇼핑몰", "쇼핑몰"),
        NO_STORE("NO_매장", "매장"),
        NO_BY_ORGA("NO_BYORGA", "BYORGA"),
        NO_SPECIAL_EDITION("NO_특판", "특판"),
        NON_ORDER("미거래주문", "증정")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpFoodOrderSource implements CodeCommEnum {
        PULMUONE_ESHOP("풀무원이샵", "식품자사몰"),
        PULMUONE_ESHOP_DELIVERY("풀무원이샵(직송)", "식품자사몰(직송)"),
        OUTMALL("외부몰", "식품외부몰"),
        OUTMALL_DELIVERY("외부몰(직송)", "식품외부몰(직송)")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrgaOrderSource implements CodeCommEnum {
        SHOPPING_MALL("쇼핑몰", "쇼핑몰"),
        SPECIAL_EDITION("특판", "특판")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrderClass implements CodeCommEnum {
        SALES_ORDER("SALES ORDER", "판매"),
        TRANSFER_ORDER("TRANSFER ORDER", "발주")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpPackingType implements CodeCommEnum {
        NORMAL_PACKING("일반포장", "일반포장"),
        ECO_PACKING("Eco포장", "에코박스"),
        SPECIAL_PACKING("특수포장", "포장비")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpSaleFg implements CodeCommEnum {
        SALES_ORDER("1", "판매"),
        RETURN_ORDER("-1", "반품")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpDlvOrdFg implements CodeCommEnum {
        NORMAL_WAITING("0", "일반/대기"),
        STORE_DELIVERY("2", "매장배송"),
        STORE_QUICK_DELIVERY("3", "매장퀵배송"),
        STORE_PICK_UP("4", "매장픽업")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpHitokDlvGrp implements CodeCommEnum {
        DAILY_DELIVERY("0", "일배"),
        NORMAL_DELIVERY("1", "택배")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpHitokOrdTypLin implements CodeCommEnum {
        ORDER("OT01", "주문"),
        PRESENTATION("OT02", "증정"),
        EVENT("OT03", "행사")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpShiPriCd implements CodeCommEnum {
        YONGIN_DELIVERY("D-1", "용인출고"),
        DIRECT_DELIVERY("직송", "직송")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpHitokOrdStuCd implements CodeCommEnum {
        NEW_ORDER("DA01", "신규"),
        RE_ORDER("DA02", "재주문")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrderSchStatus implements CodeCommEnum {
        ORDER("1", "주문"),
        CANCEL("2", "취소")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpLdsDlvGrp implements CodeCommEnum {
        FRANCHISEE_DELIVERY("1", "가맹점택배"),
        CUSTOMER_DELIVERY("2", "고객택배")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpBabymealDlvGrp implements CodeCommEnum {
        FRANCHISEE("0", "가맹점"),
        DELIVERY("1", "택배")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpBabymealOnDlvCd implements CodeCommEnum {
        DAILY_DELIVERY("0001", "일배배송"),
        BATCH_DELIVERY("0002", "일괄배송")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpBabymealDlvMt implements CodeCommEnum {
        BATCH_DELIVERY("0", "일괄배송"),
        DAYS7_PER_WEEK("0003", "매일(월~금,금3개)"),
        DAYS6_PER_WEEK("0001", "매일(월~금,금2개)"),
        DAYS3_PER_WEEK("0002", "주3회(월수금)")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpRtnRsnDes implements CodeCommEnum {
        RETURN_ORDER("반품", "반품"),
        BROKEN_PACKAGING("포장 파손", "포장 파손"),
        CIRCULATION_PURCHASE("유통 매입차이", "유통 매입차이")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PolicyShippingComp implements CodeCommEnum {
        CJ("001", "CJ대한통운"),
        LOTTE("002", "롯데택배"),
        HANJIN("003", "한진택배"),
        POST("004", "우체국택배")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpPrtnChnl implements CodeCommEnum {
        HOME("UH01", "홈"),
        OFFICE("UH02", "오피스")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpGoodsCycleTp implements CodeCommEnum {
        FIXING("P", "고정"),
        NON_FIXING("S", "비고정")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum OrderChangeTp implements CodeCommEnum {
        NOT_USE("ORDER_CHANGE_TP.NOT_USE", "사용안함"),
        ORDER_IF("ORDER_CHANGE_TP.ORDER_IF", "주문 I/F"),
        ORDER_CHANGE("ORDER_CHANGE_TP.ORDER_CHANGE", "배송준비중 변경")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpOrderHpnCd implements CodeCommEnum {
        WEB_CUSTOMER("0067", "웹-고객"),
        MANAGER("0068", "관리자주문"),
        WEB_EMPLOYEE("0069", "웹-임직원"),
        MOBILE_CUSTOMER("0070", "모바일-고객"),
        MOBILE_EMPLOYEE("0071", "모바일-임직원")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpDelvType implements CodeCommEnum {
        DAILY_DELIVERY("0001", "일배"),
        NORMAL_DELIVERY("0002", "택배")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpCalcType implements CodeCommEnum {
        SLASE("S", "판매가 기준"),
        SUPPLY("B", "공급가 기준")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpBatchExecFl implements CodeCommEnum {
        YES("Y", "실행"),
        NO("N", "미실행"),
        COPY("C", "주문복사를 통한 매출연동")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum BosClaimId implements CodeCommEnum {
        MISS("BOS_CLAIM_MISS_ID", "BOS 클레임 사유 - 미출")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpHitokDailyDeliveryPatterntDay implements CodeCommEnum {
        NONE_DELIVERY("0", "배송없음"),
        DELIVERY("1", "배송있음"),
        SATURDAY_DELIVERY("2", "토요일배송있음")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpFoodOrdTyp implements CodeCommEnum {
        STORE_RETURNS("1", "매장반품"),
        BUSINESS_RETURN("2", "영업환품"),
        TAKEOVER_REJECT("3", "인수거절")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpHitokReturnType implements CodeCommEnum {
        RETURN("RETURN", "반품"),
        REDELIVERY("REDELIVERY", "재배송")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpCjStcTypType implements CodeCommEnum {
        NORMAL("NN", "정상"),
        IMMINENT_EVENT("AC", "임박,행사")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErpSalYn implements CodeCommEnum {
        SALES_ORDER("Y", "판매"),
        RETURN_ORDER("N", "반품")
        ;

        private final String code;
        private final String codeName;
    }

}
