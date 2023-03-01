package kr.co.pulmuone.v1.comm.api.constant;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErpApiInfo {

    /*
     * ERP API 정보 : 인터페이스 ID 로 구분
     */

    /* 품목 */
    // glCls=_in('06','07') : ERP 품목 정보 API 조회시 통합몰에서 활용하는 ( 06:제품, 07:상품 ) 데이터만 검색
    IF_GOODS_SRCH("/itf/goods", RequestMethod.GET, "glCls=_in('06','07')", "상품정보 조회") //
    , IF_GOODS_FLAG("/itf/goods", RequestMethod.PUT, "target=header-ITF", "상품정보 완료") //
    , IF_NUTRI_SRCH("/itf/nutri", RequestMethod.GET, "target=header-ITF", "상품영양정보 조회") //
    , IF_PRICE_SRCH("/itf/price", RequestMethod.GET, "", "상품가격 조회") //
    , IF_GOODS_UPD("/itf/goods", RequestMethod.PUT, "target=header-ITF", "품목조회 완료 업데이트") //
    , IF_PRICE_FLAG("/itf/price", RequestMethod.PUT, "target=header-ITF", "상품가격조회 완료") //

    /* 재고 */
    , IF_STOCK_SRCH("/itf/stock", RequestMethod.GET, "target=header-ITF", "재고수량 조회") //하루전 기준으로 조회
    , IF_STOCK_FLAG("/itf/stock", RequestMethod.PUT, "target=header-ITF", "재고수량 조회 완료")
    , IF_STOCK_3PL_SRCH("/itf/stock3pl", RequestMethod.GET, "target=header-ITF_MALL", "3PL재고수량 조회") //하루전 기준으로 조회
    , IF_STOCK_3PL_FLAG("/itf/stock3pl", RequestMethod.PUT, "target=header-ITF_MALL", "3PL재고수량 조회 완료")

    /* 구매발주 */
    , IF_PURCHASE_INP("/itf/purchase", RequestMethod.POST, "", "구매발주 입력") //구매발주 입력
    , IF_PURCHASESCH_SRCH("/itf/purchasesch", RequestMethod.GET, "target=header-ITF", "올가R2발주스케쥴 조회")

    /* 올가 오프라인 발주상태 조회 및 완료 */
    , IF_ORGAOFFLINE_SRCH("/itf/orgaOfflinePo", RequestMethod.GET, "target=header-ITF", "올가오프라인발주상태 조회")
    , IF_ORGAOFFLINE_FLAG("/itf/orgaOfflinePo", RequestMethod.PUT, "target=header-ITF", "올가오프라인발주상태 조회 완료")

    /* 주문|취소 */
    , IF_DLV_SRCH("/itf/dlv", RequestMethod.GET, "target=line-ITF_DLV", "송장 조회") //주문|취소 > 송장
    , IF_DLV_FLAG("/itf/dlv", RequestMethod.PUT, "target=line-ITF_DLV", "송장 조회 완료") //주문|취소 > 송장
    , IF_ORDETC_DLV_SRCH("/itf/ordetcDlv", RequestMethod.GET, "target=header-ITF_DLV", "기타송장 조회") //주문|취소 > 기타송장
    , IF_ORDETC_DLV_FLAG("/itf/ordetcDlv", RequestMethod.PUT, "target=header-ITF_DLV", "기타송장 조회 완료") //주문|취소 > 기타송장
    , IF_MIS_SRCH("/itf/mis", RequestMethod.GET, "target=line-ITF_MIS", "미출 조회") //주문|취소 > 미출
    , IF_MIS_FLAG("/itf/mis", RequestMethod.PUT, "target=line-ITF_MIS", "미출 조회 완료") //주문|취소 > 미출
    , IF_SAL_SRCH("/itf/sal", RequestMethod.GET, "target=line-ITF_SET", "매출 확정 조회") //주문|취소 > 매출 확정
    , IF_SAL_FLAG("/itf/sal", RequestMethod.PUT, "target=line-ITF_SET", "매출 확정 조회 완료") //주문|취소 > 매출 확정 완료
    , IF_CUSTORD_INP("/itf/custord", RequestMethod.POST, "", "주문|취소 입력") //주문|취소 > 주문|취소 입력
    , IF_CUSTORD_SRCH("/itf/custord", RequestMethod.GET, "target=line-ITF_ORD", "주문|취소 조회") //주문|취소 > 주문|취소 조회
    , IF_CUSTORD_FLAG("/itf/custord", RequestMethod.PUT, "target=line-ITF_ORD", "주문|취소 조회 완료") //주문|취소 > 주문|취소 조회 완료
    , IF_ORDETC_INP("/itf/ordetc", RequestMethod.POST, "", "기타주문 입력")

    /* 정산 */
    , IF_EMP_INVOICE_INP("/itf/employeeBenefitAp", RequestMethod.POST, "", "임직원지원금 AP 송장 발행 입력") // 임직원지원금 AP 송장 발행 입력

    /* 납품처 */
    , IF_SHIPTO_SRCH("/itf/shipto", RequestMethod.GET, "target=header-ITF", "납품처 조회") //납품처조회

    /* 매장 정보*/
    , IF_STORE_SRCH("/itf/store", RequestMethod.GET, "target=header-ITF", "매장정보 조회") //매장정보 조회
    , IF_STORE_FLAG("/itf/store", RequestMethod.GET, "target=header-ITF", "매장정보 조회 완료") //매장정보 조회 완료

    /* 매장배송관리 정보*/
    , IF_STORE_DELIVER_SRCH("/itf/storeDeliver", RequestMethod.GET, "target=header-ITF", "매장배송관리정보 조회") //매장배송관리정보 조회
    , IF_STORE_DELIVER_FLAG("/itf/storeDeliver", RequestMethod.PUT, "target=header-ITF", "매장배송관리정보 조회 완료") //매장배송관리정보 조회 완료

    /* 매장주문시간 */
    , IF_STORE_ORDTIME_SRCH("/itf/storeOrdtime", RequestMethod.GET, "target=header-ITF", "매장주문시간 조회") //매장주문시간 조회
    , IF_STORE_ORDTIME_FLAG("/itf/storeOrdtime", RequestMethod.PUT, "target=header-ITF", "매장주문시간 조회 완료") //매장주문시간 조회 완료

	/* 배송권역정보 */
    , IF_DLVZONE_SRCH("/itf/dlvzone", RequestMethod.GET, "target=header-ITF", "배송권역정보 조회") //배송권역정보 조회
    , IF_DLVZONE_FLAG("/itf/dlvzone", RequestMethod.PUT, "target=header-ITF", "배송권역정보 조회 완료") //배송권역정보 조회 완료

    /* 휴무일정보 */
    , IF_HOLIDAY_SRCH("/itf/holiday", RequestMethod.GET, "target=header-ITF", "휴무일정보 조회") //휴무일정보 조회
    , IF_HOLIDAY_FLAG("/itf/holiday", RequestMethod.PUT, "target=header-ITF", "휴무일정보 조회 완료") //휴무일정보 조회 완료

    /* 3PL_상품정보_입력 */

    , IF_GOODS_3PL_INP("/itf/goods3pl", RequestMethod.POST, "", "3PL상품정보입력") //3PL상품정보 입력
    , IF_GOODS_3PL_UPD("/itf/goods3pl", RequestMethod.PUT, "", "3PL상품정보수정") //3PL상품정보 수정

    /* 올가 매장정보상품 조회 */
    , IF_ORGASHOP_STOCK_SRCH("/itf/orgashopStock", RequestMethod.GET, "target=header-ITF", "올가 매장정보상품 조회") //올가 매장정보상품 조회
    , IF_ORGASHOP_STOCK_FLAG("/itf/orgashopStock", RequestMethod.POST, "target=header-ITF", "올가 매장정보상품 조회 완료") //올가 매장정보상품 조회 완료
    ;

    /*
     * API 호출시 고정 param 구분자
     */
    private final String firstSeparator = "&"; // 고정 param 1차 구분자
    private final String secondSeparator = "="; // 고정 param 2차 구분자

    private final String erpApiUri; // ERP API 의 URL
    private final RequestMethod requestMethod; // 통신방식
    private final String fixedValue; // 고정값 ( 조회시 인터페이스 처리용 )
    private final String erpApiDescription; // 설명

    /*
     * 해당 API 호출시 필수로 세팅해야 할 고정 param 들을 담은 Map 반환
     *
     * => 고정값이 존재하지 않는 API 인 경우 empty HashMap 반환
     */
    public Map<String, String> getFixedValueMap() {

        Map<String, String> fixedValueMap = new HashMap<>();

        // 해당 API 호출시 필수로 포함해야 할 고정값 존재시
        if (!StringUtil.isEmpty(this.fixedValue)) {

            String[] firstSeparatorArray = this.fixedValue.split(firstSeparator); // 1차 구분자로 split
            String[] secondSeparatorArray = null;

            if (firstSeparatorArray.length > 0) {

                for (String fixedValueStr : firstSeparatorArray) {

                    secondSeparatorArray = fixedValueStr.split(secondSeparator); // 2차 구분자로 split

                    if (secondSeparatorArray.length == 2) { // [ (key), (value) ] 형식으로2개의 값이 존재해야 함
                        fixedValueMap.put(secondSeparatorArray[0], secondSeparatorArray[1]);
                    }

                }

            }

            return fixedValueMap;

        } else { // 고정값이 존재하지 않는 API 인 경우 empty HashMap 반환

            return fixedValueMap;

        }

    }

}
