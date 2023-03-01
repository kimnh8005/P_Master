package kr.co.pulmuone.v1.batch.order.comm;

import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 QuarterHeader
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class ErpApiQuarterHeader {

    private final ErpApiHeader erpApiHeader;

    /**
     * 용인물류 일반배송 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getNormalDeliveryHeaderList(Object headerItem, List<?> lineBindList) {

        DeliveryOrderListVo deliveryOrderListVo = (DeliveryOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getNormalDeliveryHeader(deliveryOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 용인물류 새벽배송 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getDawnDeliveryHeaderList(Object headerItem, List<?> lineBindList) {

        DeliveryOrderListVo deliveryOrderListVo = (DeliveryOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getDawnDeliveryHeader(deliveryOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 백암물류 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getCjHeaderList(Object headerItem, List<?> lineBindList) {

        CjOrderListVo cjOrderListVo = (CjOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getCjHeader(cjOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 올가 매장배송 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getOrgaStoreDeliveryHeaderList(Object headerItem, List<?> lineBindList) {

        OrgaStoreDeliveryOrderListVo orgaStoreDeliveryOrderListVo = (OrgaStoreDeliveryOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getOrgaStoreDeliveryHeader(orgaStoreDeliveryOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 올가 기타주문 Header 생성
     * @param headerItem
     * @return List<?>
     * @throws
     */
    public List<?> getOrgaEtcHeaderList(Object headerItem) {

        OrgaEtcOrderListVo orgaEtcOrderListVo = (OrgaEtcOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getOrgaEtcHeader(orgaEtcOrderListVo));

        return headerList;
    }

    /**
     * 하이톡 택배주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getHitokNormalOrderHeaderList(Object headerItem, List<?> lineBindList) {

        HitokNormalDeliveryOrderListVo hitokNormalDeliveryOrderListVo = (HitokNormalDeliveryOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getHitokNormalOrderHeader(hitokNormalDeliveryOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 하이톡 일배주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getHitokDailyOrderHeaderList(Object headerItem, List<?> lineBindList) {

        HitokDailyDeliveryOrderListVo hitokDailyDeliveryOrderListVo = (HitokDailyDeliveryOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getHitokkDailyOrderHeader(hitokDailyDeliveryOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 풀무원건강생활(LDS) 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getLohasDirectSaleOrderHeaderList(Object headerItem, List<?> lineBindList) {

        LohasDirectSaleOrderListVo lohasDirectSaleOrderListVo = (LohasDirectSaleOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getLohasDirectSaleOrderHeader(lohasDirectSaleOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 베이비밀 일일배송 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getBabymealOrderHeaderList(Object headerItem, List<?> lineBindList) {

        BabymealOrderListVo babymealOrderListVo = (BabymealOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getBabymealOrderHeader(babymealOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 베이비밀 택배배송 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getBabymealNormalOrderHeaderList(Object headerItem, List<?> lineBindList) {

        BabymealNormalOrderListVo babymealNormalOrderListVo = (BabymealNormalOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getBabymealNormalOrderHeader(babymealNormalOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 매출 주문 (풀무원식품) Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getSalesOrderFoodHeaderList(Object headerItem, List<?> lineBindList) {

        SalesOrderListVo salesOrderListVo = (SalesOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getSalesOrderFoodHeader(salesOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 매출 주문 (올가홀푸드) Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getSalesOrderOrgaHeaderList(Object headerItem, List<?> lineBindList) {

        SalesOrderListVo salesOrderListVo = (SalesOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getSalesOrderOrgaHeader(salesOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 반품매출 주문 (풀무원식품) Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getReturnSalesOrderFoodHeaderList(Object headerItem, List<?> lineBindList) {

        ReturnSalesOrderListVo returnSalesOrderListVo = (ReturnSalesOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getReturnSalesOrderFoodHeader(returnSalesOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 반품매출 주문 (올가홀푸드) Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getReturnSalesOrderOrgaHeaderList(Object headerItem, List<?> lineBindList) {

        ReturnSalesOrderListVo returnSalesOrderListVo = (ReturnSalesOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getReturnSalesOrderOrgaHeader(returnSalesOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 하이톡 반품 주문 Header 생성 (택배,일배 공통)
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getHitokReturnOrderHeaderList(Object headerItem, List<?> lineBindList) {

        HitokDeliveryReturnOrderListVo hitokDeliveryReturnOrderListVo = (HitokDeliveryReturnOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getHitokReturnOrderHeader(hitokDeliveryReturnOrderListVo, lineBindList));

        return headerList;
    }

    /**
     * 풀무원건강생활(LDS) 반품 주문 Header 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getLohasDirectSaleReturnOrderHeaderList(Object headerItem, List<?> lineBindList) {

        LohasDirectSaleReturnOrderListVo  lohasDirectSaleReturnOrderListVo = (LohasDirectSaleReturnOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(erpApiHeader.getLohasDirectSaleReturnOrderHeader(lohasDirectSaleReturnOrderListVo, lineBindList));

        return headerList;
    }

}
