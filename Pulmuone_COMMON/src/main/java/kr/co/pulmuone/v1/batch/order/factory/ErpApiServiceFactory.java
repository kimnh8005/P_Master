package kr.co.pulmuone.v1.batch.order.factory;

import kr.co.pulmuone.v1.batch.order.comm.ErpApiData;
import kr.co.pulmuone.v1.batch.order.comm.ErpApiQuarterHeader;
import kr.co.pulmuone.v1.batch.order.comm.ErpApiQuarterLine;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * API 배치 ServiceFactory
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class ErpApiServiceFactory {

    private final ErpApiData erpApiData;

    private final ErpApiQuarterLine erpApiQuarterLine;

    private final ErpApiQuarterHeader erpApiQuarterHeader;

    /**
     * ERP INTERFACE ID
     * @param erpServiceType
     * @return String
     * @throws
     */
    public String getErpInterfaceId(ErpApiEnums.ErpServiceType erpServiceType) {

        String erpInterfaceId = "";

        // 용인물류 일반배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 용인물류 새벽배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_DAWN_DELIVERY_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 백암물류 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 올가 매장배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_STORE_DELIVERY_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 올가 기타주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_ETC_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.ORDER_ETC_INSERT_INTERFACE_ID.getCode();
        }
        // 하이톡 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 하이톡 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 풀무원건강생활(LDS) 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 베이비밀 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_DAILY_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 베이비밀 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_NORMAL_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_FOOD)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_ORGA)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 반품매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_FOOD)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 반품매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_ORGA)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 하이톡 택배배송 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_RETRUN_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 풀무원건강생활(LDS) 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // CJ(백암)물류 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_SALES_ORDER_FOOD)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 매출만 연동 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_FOOD)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }
        // 매출만 연동 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_ORGA)) {
            erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();
        }

        return erpInterfaceId;
    }

    /**
     * 주문 데이터 리스트 조회
     * @param erpServiceType
     * @return List<?>
     * @throws BaseException
     */
    public List<?> getErpOrderList(ErpApiEnums.ErpServiceType erpServiceType) throws BaseException {

        // 용인물류 일반배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER)) {
            return erpApiData.getNormalDeliveryOrderList();
        }
        // 용인물류 새벽배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_DAWN_DELIVERY_ORDER)) {
            return erpApiData.getDawnDeliveryOrderList();
        }
        // 백암물류 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_ORDER)) {
            return erpApiData.getCjOrderList();
        }
        // 올가 매장배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_STORE_DELIVERY_ORDER)) {
            return erpApiData.getOrgaStoreDeliveryOrderList();
        }
        // 올가 기타주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_ETC_ORDER)) {
            return erpApiData.getOrgaEtcOrderList();
        }
        // 하이톡 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_ORDER)) {
            return erpApiData.getHitokNormalDeliveryOrderList();
        }
        // 하이톡 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_ORDER)) {
            return erpApiData.getHitokDailyDeliveryOrderList();
        }
        // 풀무원건강생활(LDS) 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_ORDER)) {
            return erpApiData.getLohasDirectSaleOrderList();
        }
        // 베이비밀 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_DAILY_ORDER)) {
            return erpApiData.getBabymealDailyOrderList();
        }
        // 베이비밀 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_NORMAL_ORDER)) {
            return erpApiData.getBabymealNormalOrderList();
        }
        // 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_FOOD)) {
            return erpApiData.getSalesOrderFoodList();
        }
        // 매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_ORGA)) {
            return erpApiData.getSalesOrderOrgaList();
        }
        // 반품매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_FOOD)) {
            return erpApiData.getReturnSalesOrderFoodList();
        }
        // 반품매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_ORGA)) {
            return erpApiData.getReturnSalesOrderOrgaList();
        }
        // 하이톡 택배배송 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_RETRUN_ORDER)) {
            return erpApiData.getHitokNormalDeliveryReturnOrderList();
        }
        // 풀무원건강생활(LDS) 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER)) {
            return erpApiData.getLohasDirectSaleReturnOrderList();
        }
        // CJ(백암)물류 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_SALES_ORDER_FOOD)) {
            return erpApiData.getCjSalesOrderFoodList();
        }
        // 매출만 연동 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_FOOD)) {
            return erpApiData.getSalesOnlyOrderFoodList();
        }
        // 매출만 연동 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_ORGA)) {
            return erpApiData.getSalesOnlyOrderOrgaList();
        }

        return null;
    }

    /**
     * 주문 데이터 리스트 조회 (외부몰 분리)
     * @param erpServiceType
     * @param omSellersIdList
     * @return List<?>
     * @throws BaseException
     */
    public List<?> getErpOrderList(ErpApiEnums.ErpServiceType erpServiceType, List<String> omSellersIdList) throws BaseException {

        // 용인물류 일반배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER)) {
            return erpApiData.getNormalDeliveryOrderList(omSellersIdList);
        }
        // 백암물류 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_ORDER)) {
            return erpApiData.getCjOrderList(omSellersIdList);
        }
        // 올가 기타주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_ETC_ORDER)) {
            return erpApiData.getOrgaEtcOrderList(omSellersIdList);
        }

        return null;
    }

    /**
     * 주문 line 생성
     * @param erpServiceType
     * @param linelist
     * @return List<?>
     * @throws
     */
    public List<?> getErpApiLineList(ErpApiEnums.ErpServiceType erpServiceType, List<?> linelist) {

        // 용인물류 일반배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER)) {
            return erpApiQuarterLine.getNormalOrderLineList(linelist);
        }
        // 용인물류 새벽배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_DAWN_DELIVERY_ORDER)) {
            return erpApiQuarterLine.getDawnOrderLineList(linelist);
        }
        // 백암물류 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_ORDER)) {
            return erpApiQuarterLine.getCjOrderLineList(linelist);
        }
        // 올가 매장배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_STORE_DELIVERY_ORDER)) {
            return erpApiQuarterLine.getOrgaStoreDeliveryOrderLineList(linelist);
        }
        // 하이톡 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_ORDER)) {
            return erpApiQuarterLine.getHitokNormalDeliveryOrderLineList(linelist);
        }
        // 하이톡 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_ORDER)) {
            return erpApiQuarterLine.getHitokDailyDeliveryOrderLineList(linelist);
        }
        // 풀무원건강생활(LDS) 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_ORDER)) {
            return erpApiQuarterLine.getLohasDirectSaleOrderLineList(linelist);
        }
        // 베이비밀 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_DAILY_ORDER)) {
            return erpApiQuarterLine.getBabymealDailyOrderLineList(linelist);
        }
        // 베이비밀 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_NORMAL_ORDER)) {
            return erpApiQuarterLine.getBabymealNormalOrderLineList(linelist);
        }
        // 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_FOOD)) {
            return erpApiQuarterLine.getSalesOrderFoodLineList(linelist);
        }
        // 매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_ORGA)) {
            return erpApiQuarterLine.getSalesOrderOrgaLineList(linelist);
        }
        // 반품매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_FOOD)) {
            return erpApiQuarterLine.getReturnSalesOrderFoodLineList(linelist);
        }
        // 반품매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_ORGA)) {
            return erpApiQuarterLine.getReturnSalesOrderOrgaLineList(linelist);
        }
        // 하이톡 택배배송 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_RETRUN_ORDER)) {
            return erpApiQuarterLine.getHitokNormalDeliveryReturnOrderLineList(linelist);
        }
        // 풀무원건강생활(LDS) 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER)) {
            return erpApiQuarterLine.getLohasDirectSaleReturnOrderLineList(linelist);
        }
        // CJ(백암)물류 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_SALES_ORDER_FOOD)) {
            return erpApiQuarterLine.getSalesOrderFoodLineList(linelist);
        }
        // 매출만 연동 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_FOOD)) {
            return erpApiQuarterLine.getSalesOrderFoodLineList(linelist);
        }
        // 매출만 연동 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_ORGA)) {
            return erpApiQuarterLine.getSalesOrderOrgaLineList(linelist);
        }

        return null;
    }

    /**
     * 주문 header 생성
     * @param erpServiceType
     * @param headerItem
     * @param lineBindList
     * @return List<?>
     * @throws
     */
    public List<?> getErpApiHeaderList(ErpApiEnums.ErpServiceType erpServiceType, Object headerItem, List<?> lineBindList) {

        // 용인물류 일반배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER)) {
            return erpApiQuarterHeader.getNormalDeliveryHeaderList(headerItem, lineBindList);
        }
        // 용인물류 새벽배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_DAWN_DELIVERY_ORDER)) {
            return erpApiQuarterHeader.getDawnDeliveryHeaderList(headerItem, lineBindList);
        }
        // 백암물류 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_ORDER)) {
            return erpApiQuarterHeader.getCjHeaderList(headerItem, lineBindList);
        }
        // 올가 매장배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_STORE_DELIVERY_ORDER)) {
            return erpApiQuarterHeader.getOrgaStoreDeliveryHeaderList(headerItem, lineBindList);
        }
        // 올가 기타주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_ETC_ORDER)) {
            return erpApiQuarterHeader.getOrgaEtcHeaderList(headerItem);
        }
        // 하이톡 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_ORDER)) {
            return erpApiQuarterHeader.getHitokNormalOrderHeaderList(headerItem, lineBindList);
        }
        // 하이톡 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_ORDER)) {
            return erpApiQuarterHeader.getHitokDailyOrderHeaderList(headerItem, lineBindList);
        }
        // 풀무원건강생활(LDS) 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_ORDER)) {
            return erpApiQuarterHeader.getLohasDirectSaleOrderHeaderList(headerItem, lineBindList);
        }
        // 베이비밀 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_DAILY_ORDER)) {
            return erpApiQuarterHeader.getBabymealOrderHeaderList(headerItem, lineBindList);
        }
        // 베이비밀 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_NORMAL_ORDER)) {
            return erpApiQuarterHeader.getBabymealNormalOrderHeaderList(headerItem, lineBindList);
        }
        // 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_FOOD)) {
            return erpApiQuarterHeader.getSalesOrderFoodHeaderList(headerItem, lineBindList);
        }
        // 매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_ORGA)) {
            return erpApiQuarterHeader.getSalesOrderOrgaHeaderList(headerItem, lineBindList);
        }
        // 반품매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_FOOD)) {
            return erpApiQuarterHeader.getReturnSalesOrderFoodHeaderList(headerItem, lineBindList);
        }
        // 반품매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_ORGA)) {
            return erpApiQuarterHeader.getReturnSalesOrderOrgaHeaderList(headerItem, lineBindList);
        }
        // 하이톡 택배배송 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_RETRUN_ORDER)) {
            return erpApiQuarterHeader.getHitokReturnOrderHeaderList(headerItem, lineBindList);
        }
        // 풀무원건강생활(LDS) 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER)) {
            return erpApiQuarterHeader.getLohasDirectSaleReturnOrderHeaderList(headerItem, lineBindList);
        }
        // CJ(백암)물류 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_SALES_ORDER_FOOD)) {
            return erpApiQuarterHeader.getSalesOrderFoodHeaderList(headerItem, lineBindList);
        }
        // 매출만 연동 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_FOOD)) {
            return erpApiQuarterHeader.getSalesOrderFoodHeaderList(headerItem, lineBindList);
        }
        // 매출만 연동 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_ORGA)) {
            return erpApiQuarterHeader.getSalesOrderOrgaHeaderList(headerItem, lineBindList);
        }

        return null;
    }

    /**
     * 성공 시 주문 배치완료 한건 업데이트
     * @param erpServiceType
     * @param lineList
     * @return void
     * @throws BaseException
     */
    public void putErpOrderBatchCompleteOneUpdate(ErpApiEnums.ErpServiceType erpServiceType, Object lineItem) throws BaseException {
        // 올가 기타주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_ETC_ORDER)) {
            erpApiData.putOrgaEtcOrderBatchCompleteUpdate(lineItem);
        }
    }

    /**
     * 성공 시 주문 배치완료 업데이트
     * @param erpServiceType
     * @param lineList
     * @return void
     * @throws BaseException
     */
    public void putErpOrderBatchCompleteUpdate(ErpApiEnums.ErpServiceType erpServiceType, List<?> lineList) throws BaseException {

        // 용인물류 일반배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER)) {
            erpApiData.putDeliveryOrderBatchCompleteUpdate(lineList);
        }
        // 용인물류 새벽배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_DAWN_DELIVERY_ORDER)) {
            erpApiData.putDeliveryOrderBatchCompleteUpdate(lineList);
        }
        // 백암물류 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_ORDER)) {
            erpApiData.putCjOrderBatchCompleteUpdate(lineList);
        }
        // 올가 매장배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_ORGA_STORE_DELIVERY_ORDER)) {
            erpApiData.putOrgaStoreDeliveryOrderBatchCompleteUpdate(lineList);
        }
        // 하이톡 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_ORDER)) {
            erpApiData.putHitokNormalOrderBatchCompleteUpdate(lineList);
        }
        // 하이톡 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_ORDER)) {
            erpApiData.putHitokDailyOrderBatchCompleteUpdate(lineList);
        }
        // 풀무원건강생활(LDS) 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_ORDER)) {
            erpApiData.putLohasDirectSaleOrderBatchCompleteUpdate(lineList);
        }
        // 베이비밀 일일배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_DAILY_ORDER)) {
            erpApiData.putBabymealOrderBatchCompleteUpdate(lineList);
        }
        // 베이비밀 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_NORMAL_ORDER)) {
            erpApiData.putBabymealNormalOrderBatchCompleteUpdate(lineList);
        }
        // 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_FOOD)) {
            erpApiData.putSalesOrderBatchCompleteUpdate(lineList);
        }
        // 매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ORDER_ORGA)) {
            erpApiData.putSalesOrderBatchCompleteUpdate(lineList);
        }
        // 반품매출 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_FOOD)) {
            erpApiData.putReturnSalesOrderBatchCompleteUpdate(lineList);
        }
        // 반품매출 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_ORGA)) {
            erpApiData.putReturnSalesOrderBatchCompleteUpdate(lineList);
        }
        // 하이톡 택배배송 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_HITOK_NORMAL_DELIVERY_RETRUN_ORDER)) {
            erpApiData.putHitokReturnOrderBatchCompleteUpdate(lineList);
        }
        // 풀무원건강생활(LDS) 반품 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER)) {
            erpApiData.putLohasDirectSaleReturnOrderBatchCompleteUpdate(lineList);
        }
        // CJ(백암)물류 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_CJ_SALES_ORDER_FOOD)) {
            erpApiData.putSalesOrderBatchCompleteUpdate(lineList);
        }
        // 매출 주문 (풀무원식품)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_FOOD)) {
            erpApiData.putSalesOrderBatchCompleteUpdate(lineList);
        }
        // 매출 주문 (올가홀푸드)
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_ORGA)) {
            erpApiData.putSalesOrderBatchCompleteUpdate(lineList);
        }

    }

}
