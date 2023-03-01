package kr.co.pulmuone.v1.batch.order.comm;

import kr.co.pulmuone.v1.batch.order.dto.search.ReturnSalesOrderSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import kr.co.pulmuone.v1.batch.order.dto.search.DeliveryOrderSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.order.OrderErpService;
import kr.co.pulmuone.v1.comm.api.constant.SupplierTypes;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * API 배치 데이터 조회
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class ErpApiData {

    private final OrderErpService orderErpService;

    /**
     * 용인물류 일반배송 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getNormalDeliveryOrderList() throws BaseException {

        String[] goodsDeliveryType = { GoodsEnums.GoodsDeliveryType.NORMAL.getCode(), GoodsEnums.GoodsDeliveryType.RESERVATION.getCode() };
        DeliveryOrderSearchRequestDto params =  DeliveryOrderSearchRequestDto.builder()
                                                .urWarehouseId(ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode()) // 출고처: 용인물류
                                                .goodsDeliveryType(goodsDeliveryType) // 배송유형: 일반, 예약
                                                .orderStatusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode()) // 주문상태: 결제완료
                                                .orderDeadlineTime(ErpApiEnums.ErpOrderDeadlineTime.YONGIN_NORMAL.getCode()) // 용인물류 일반배송 마감시간 : 1800
                                                .build();

        return getDeliveryOrderList(params);
    }

    /**
     * 용인물류 일반배송 주문 리스트 조회 (외부몰 분리)
     * @param omSellersIdList
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getNormalDeliveryOrderList(List<String> omSellersIdList) throws BaseException {

        String[] goodsDeliveryType = { GoodsEnums.GoodsDeliveryType.NORMAL.getCode(), GoodsEnums.GoodsDeliveryType.RESERVATION.getCode() };
        DeliveryOrderSearchRequestDto params =  DeliveryOrderSearchRequestDto.builder()
                                                .urWarehouseId(ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode()) // 출고처: 용인물류
                                                .goodsDeliveryType(goodsDeliveryType) // 배송유형: 일반, 예약
                                                .orderStatusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode()) // 주문상태: 결제완료
                                                .orderDeadlineTime(ErpApiEnums.ErpOrderDeadlineTime.YONGIN_NORMAL.getCode()) // 용인물류 일반배송 마감시간 : 1800
                                                .omSellersIdList(omSellersIdList) // 판매처 리스트
                                                .build();

        return getDeliveryOrderList(params);
    }

    /**
     * 용인물류 새벽배송 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getDawnDeliveryOrderList() throws BaseException {

        String[] goodsDeliveryType = { GoodsEnums.GoodsDeliveryType.DAWN.getCode() };
        DeliveryOrderSearchRequestDto params =  DeliveryOrderSearchRequestDto.builder()
                                                .urWarehouseId(ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode()) // 출고처: 용인물류
                                                .goodsDeliveryType(goodsDeliveryType) // 배송유형: 새벽
                                                .orderStatusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode()) // 주문상태: 결제완료
                                                .orderDeadlineTime(ErpApiEnums.ErpOrderDeadlineTime.YONGIN_DAWN.getCode()) // 용인물류 일반배송 마감시간 : 1900
                                                .build();

        return getDeliveryOrderList(params);
    }

    /**
     * 용인물류 주문 리스트 조회
     * @param params
     * @return List<Map>
     * @throws BaseException
     */
    protected List<Map> getDeliveryOrderList(DeliveryOrderSearchRequestDto params) throws BaseException {

        List<DeliveryOrderListVo> dataList = orderErpService.selectDeliveryOrderList(params);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                                    map.put(indexKey.getAndIncrement(), entry.getValue());
                                                    return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 용인물류 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putDeliveryOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            DeliveryOrderListVo lineItem = (DeliveryOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            String oriSysSeq = BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
            orderErpService.putOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }

    }

    /**
     * 백암물류 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getCjOrderList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode(); // 출고처: 백암물류
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        List<CjOrderListVo> dataList = orderErpService.selectCjOrderList(urWarehouseId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                                    map.put(indexKey.getAndIncrement(), entry.getValue());
                                                    return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 백암물류 주문 리스트 조회 (외부몰 분리)
     * @param omSellersIdList
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getCjOrderList(List<String> omSellersIdList) throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode(); // 출고처: 백암물류
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        List<CjOrderListVo> dataList = orderErpService.selectCjOrderList(urWarehouseId, orderStatusCd, omSellersIdList);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 백암물류 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putCjOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            CjOrderListVo lineItem = (CjOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            String oriSysSeq = BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
            orderErpService.putOrderBatchCompleteUpdateNotSales(odOrderDetlId, oriSysSeq);

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }

    }

    /**
     * 올가 매장배송 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getOrgaStoreDeliveryOrderList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.ORGA_STORE.getCode(); // 출고처: 올가매장
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        List<OrgaStoreDeliveryOrderListVo> dataList = orderErpService.selectOrgaStoreDeliveryOrderList(urWarehouseId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                                    map.put(indexKey.getAndIncrement(), entry.getValue());
                                                    return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 올가 매장배송 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putOrgaStoreDeliveryOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            OrgaStoreDeliveryOrderListVo lineItem = (OrgaStoreDeliveryOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            String oriSysSeq = BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
            orderErpService.putOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }

    }

    /**
     * 올가 기타주문 리스트 조회
     * @param
     * @return List<OrgaEtcOrderListVo>
     * @throws BaseException
     */
    public List<OrgaEtcOrderListVo> getOrgaEtcOrderList() throws BaseException {

        String urWarehouseGroupId = ErpApiEnums.UrWarehouseGroupId.ACCOUNT_ORGA.getCode(); // 출고처그룹: 산지직송
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        return orderErpService.selectOrgaEtcOrderList(urWarehouseGroupId, orderStatusCd);
    }

    /**
     * 올가 기타주문 리스트 조회 (외부몰 분리)
     * @param
     * @return List<OrgaEtcOrderListVo>
     * @throws BaseException
     */
    public List<OrgaEtcOrderListVo> getOrgaEtcOrderList(List<String> omSellersIdList) throws BaseException {

        String urWarehouseGroupId = ErpApiEnums.UrWarehouseGroupId.ACCOUNT_ORGA.getCode(); // 출고처그룹: 산지직송
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        return orderErpService.selectOrgaEtcOrderList(urWarehouseGroupId, orderStatusCd, omSellersIdList);
    }

    /**
     * 성공 올가 기타주문 배치완료 업데이트
     * @param lineItem
     * @return void
     * @throws BaseException
     */
    public void putOrgaEtcOrderBatchCompleteUpdate(Object lineItem) throws BaseException {
        OrgaEtcOrderListVo lineVo = (OrgaEtcOrderListVo) lineItem;
        String odOrderDetlId = lineVo.getOdOrderDetlId();
        orderErpService.putOrderBatchCompleteUpdateNotSales(odOrderDetlId, null);
    }

    /**
     * 하이톡 택배배송 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getHitokNormalDeliveryOrderList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.HITOK_NORMAL.getCode(); // 출고처: 녹즙택배
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        List<HitokNormalDeliveryOrderListVo> dataList = orderErpService.selectHitokNormalDeliveryOrderList(urWarehouseId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                .entrySet().stream()
                .map(entry -> { Map map = new HashMap();
                    map.put(indexKey.getAndIncrement(), entry.getValue());
                    return map; })
                .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 하이톡 택배배송 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putHitokNormalOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            HitokNormalDeliveryOrderListVo lineItem = (HitokNormalDeliveryOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            String oriSysSeq = BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
            orderErpService.putOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }
    }

    /**
     * 하이톡 일일배송 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getHitokDailyDeliveryOrderList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.HITOK_DAILY.getCode(); // 출고처: 녹즙일배
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료
        String orderSchStatus = ErpApiEnums.ErpOrderSchStatus.ORDER.getCode(); // 스케줄 주문상태: 주문

        List<HitokDailyDeliveryOrderListVo> dataList = orderErpService.selectHitokDailyDeliveryOrderList(urWarehouseId, orderStatusCd, orderSchStatus);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 하이톡 일일배송 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putHitokDailyOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        String odOrderDetlIdBefore = "";
        for(int i=0; i<linelist.size(); i++) {
            HitokDailyDeliveryOrderListVo lineItem = (HitokDailyDeliveryOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            if( odOrderDetlId != null && !odOrderDetlIdBefore.equals(odOrderDetlId) ) {
                odOrderDetlIdBefore = odOrderDetlId;
                String oriSysSeq = BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
                orderErpService.putOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);
            }

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }

    }

    /**
     * 풀무원건강생활(LDS) 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getLohasDirectSaleOrderList() throws BaseException {

        String urWarehouseGroupId = ErpApiEnums.UrWarehouseGroupId.ACCOUNT_DS.getCode(); // 출고처그룹: DS택배
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        List<LohasDirectSaleOrderListVo> dataList = orderErpService.selectLohasDirectSaleOrderList(urWarehouseGroupId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 풀무원건강생활(LDS) 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putLohasDirectSaleOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            LohasDirectSaleOrderListVo lineItem = (LohasDirectSaleOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            String oriSysSeq = BatchConstants.ERP_LOHAS_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
            orderErpService.putOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }
    }

    /**
     * 베이비밀 일일배송 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getBabymealDailyOrderList() throws BaseException {

        String[] urWarehouseId = { ErpApiEnums.UrWarehouseId.BABYMEAL_D2_FRANCHISEE.getCode(), ErpApiEnums.UrWarehouseId.BABYMEAL_D4_FRANCHISEE.getCode() }; // 출고처: 베이비밀 D2(가맹점), 베이비밀 D4(가맹점)

        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        List<BabymealOrderListVo> dataList = orderErpService.selectBabymealDailyOrderList(urWarehouseId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 베이비밀 일일배송 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putBabymealOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            BabymealOrderListVo lineItem = (BabymealOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            String oriSysSeq = BatchConstants.ERP_BABYMEAL_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
            orderErpService.putOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }
    }

    /**
     * 베이비밀 택배배송 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getBabymealNormalOrderList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.BABYMEAL_D2_DELIVERY.getCode(); // 베이비밀 D2(도안택배)
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        List<BabymealNormalOrderListVo> dataList = orderErpService.selectBabymealNormalOrderList(urWarehouseId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 베이비밀 택배배송 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putBabymealNormalOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            BabymealNormalOrderListVo lineItem = (BabymealNormalOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            String oriSysSeq = BatchConstants.ERP_BABYMEAL_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo();
            orderErpService.putOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);

            // 대체배송 여부 저장
            if(lineItem.getAlternateDeliveryType() != null){
                orderErpService.putAlternateDeliveryOrder(odOrderDetlId, lineItem.getAlternateDeliveryType().getCode());
            }
        }
    }

    /**
     * 매출 주문 (풀무원식품) 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getSalesOrderFoodList() throws BaseException {

        String[] urWarehouseId = { ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode()
                                   , ErpApiEnums.UrWarehouseId.ORGA_STORE.getCode()
                                   , ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode() }; // 제외 출고처: 용인물류, 올가매장, 백암물류
        String urSupplierId = SupplierTypes.FOOD.getCode(); // 공급처: 풀무원식품
        String orderStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode(); // 취소완료

        List<SalesOrderListVo> dataList = orderErpService.selectSalesOrderList(urWarehouseId, urSupplierId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getHeaderType()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 매출 주문 (올가홀푸드) 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getSalesOrderOrgaList() throws BaseException {

        String[] urWarehouseId = { ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode(), ErpApiEnums.UrWarehouseId.ORGA_STORE.getCode() }; // 제외 출고처: 용인물류, 올가매장
        String urSupplierId = SupplierTypes.ORGA.getCode(); // 공급처: 올가홀푸드
        String orderStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode(); // 취소완료

        List<SalesOrderListVo> dataList = orderErpService.selectSalesOrderList(urWarehouseId, urSupplierId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 매출 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putSalesOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            SalesOrderListVo lineItem = (SalesOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();

            // 원주문의 ori_sys_seq 저장시 풀무원식품일 경우 HeaderType 이외 OdOrderId
            String oriSysType = SupplierTypes.FOOD.getCode().equals(lineItem.getUrSupplierId()) ? lineItem.getHeaderType() : lineItem.getOdOrderId();
            String oriSysSeq = BatchConstants.ERP_ORDER_KEY+oriSysType+"-"+lineItem.getNowDt();
            orderErpService.putSalesOrderBatchCompleteUpdate(odOrderDetlId, oriSysSeq);
        }
    }

    /**
     * 반품매출 주문 (풀무원식품) 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getReturnSalesOrderFoodList() throws BaseException {

        String[] orderStatusCd = { OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), OrderEnums.OrderStatus.DELIVERY_READY.getCode() };
        ReturnSalesOrderSearchRequestDto params = ReturnSalesOrderSearchRequestDto.builder()
                                                .urSupplierId(SupplierTypes.FOOD.getCode()) // 공급처: 풀무원식품
                                                .urWarehouseId(ErpApiEnums.UrWarehouseId.ORGA_STORE.getCode()) // 제외 출고처: 올가매장
                                                .orderStatusCd(orderStatusCd) // 제외 원주문상태: 결제완료, 배송준비중
                                                .build();

        List<ReturnSalesOrderListVo> dataList = orderErpService.selectReturnSalesOrderList(params);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getGoodsType()+"-"+vo.getHeaderType()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 반품매출 주문 (올가홀푸드)  리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getReturnSalesOrderOrgaList() throws BaseException {

        String[] orderStatusCd = { OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), OrderEnums.OrderStatus.DELIVERY_READY.getCode() };
        ReturnSalesOrderSearchRequestDto params = ReturnSalesOrderSearchRequestDto.builder()
                                                .urSupplierId(SupplierTypes.ORGA.getCode()) // 공급처: 올가홀푸드
                                                .urWarehouseId(ErpApiEnums.UrWarehouseId.ORGA_STORE.getCode()) // 제외 출고처: 올가매장
                                                .orderStatusCd(orderStatusCd) // 제외 원주문상태: 결제완료, 배송준비중
                                                .build();

        List<ReturnSalesOrderListVo> dataList = orderErpService.selectReturnSalesOrderList(params);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getGoodsType()+"-"+vo.getOdClaimId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 반품매출 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putReturnSalesOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            ReturnSalesOrderListVo lineItem = (ReturnSalesOrderListVo) linelist.get(i);
            String odClaimDetlId = lineItem.getOdClaimDetlId();
            orderErpService.putReturnSalesOrderBatchCompleteUpdate(odClaimDetlId);
        }
    }

    /**
     * 하이톡 택배배송 반품 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getHitokNormalDeliveryReturnOrderList() throws BaseException {

        String[] orderStatusCd = { OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), OrderEnums.OrderStatus.DELIVERY_READY.getCode() };
        ReturnSalesOrderSearchRequestDto params = ReturnSalesOrderSearchRequestDto.builder()
                                                .urWarehouseId(ErpApiEnums.UrWarehouseId.HITOK_NORMAL.getCode()) // 출고처: 녹즙택배
                                                .orderStatusCd(orderStatusCd) // 제외 원주문상태: 결제완료, 배송준비중
                                                .build();

        List<HitokDeliveryReturnOrderListVo> dataList = orderErpService.selectHitokNormalDeliveryReturnOrderList(params);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdClaimId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 하이톡 택배배송 반품 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putHitokReturnOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            HitokDeliveryReturnOrderListVo lineItem = (HitokDeliveryReturnOrderListVo) linelist.get(i);
            String odClaimDetlId = lineItem.getOdClaimDetlId();
            orderErpService.putReturnSalesOrderBatchCompleteUpdate(odClaimDetlId);
        }
    }

    /**
     * 풀무원건강생활(LDS) 반품 주문 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getLohasDirectSaleReturnOrderList() throws BaseException {

        String urWarehouseGroupId = ErpApiEnums.UrWarehouseGroupId.ACCOUNT_DS.getCode(); // 출고처그룹: DS택배
        String[] orderStatusCd = { OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(), OrderEnums.OrderStatus.DELIVERY_READY.getCode() };
        ReturnSalesOrderSearchRequestDto params = ReturnSalesOrderSearchRequestDto.builder()
                                                .urWarehouseGroupId(urWarehouseGroupId) // 출고처그룹: DS 택배
                                                .orderStatusCd(orderStatusCd)
                                                .build();

        List<LohasDirectSaleReturnOrderListVo> dataList = orderErpService.selectLohasDirectSaleReturnOrderList(params);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdClaimId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 성공 풀무원건강생활(LDS) 반품 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    public void putLohasDirectSaleReturnOrderBatchCompleteUpdate(List<?> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            LohasDirectSaleReturnOrderListVo lineItem = (LohasDirectSaleReturnOrderListVo) linelist.get(i);
            String odClaimDetlId = lineItem.getOdClaimDetlId();
            orderErpService.putReturnSalesOrderBatchCompleteUpdate(odClaimDetlId);
        }
    }

    /**
     * CJ(백암)물류 매출 주문 (풀무원식품) 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getCjSalesOrderFoodList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode(); // 출고처: 백암물류
        String urSupplierId = SupplierTypes.FOOD.getCode(); // 공급처: 풀무원식품
        String orderStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode(); // 취소완료

        List<SalesOrderListVo> dataList = orderErpService.selectCjSalesOrderList(urWarehouseId, urSupplierId, orderStatusCd);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getHeaderType()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 매출만 연동 주문 (풀무원식품) 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getSalesOnlyOrderFoodList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode(); // 출고처: 용인물류
        String urSupplierId = SupplierTypes.FOOD.getCode(); // 공급처: 풀무원식품

        List<SalesOrderListVo> dataList = orderErpService.selectSalesOnlyOrderList(urWarehouseId, urSupplierId);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getHeaderType()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

    /**
     * 매출만 연동 주문 (올가홀푸드) 리스트 조회
     * @param
     * @return List<Map>
     * @throws BaseException
     */
    public List<Map> getSalesOnlyOrderOrgaList() throws BaseException {

        String urWarehouseId = ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode(); // 출고처: 용인물류
        String urSupplierId = SupplierTypes.ORGA.getCode(); // 공급처: 올가홀푸드

        List<SalesOrderListVo> dataList = orderErpService.selectSalesOnlyOrderList(urWarehouseId, urSupplierId);

        AtomicInteger indexKey = new AtomicInteger(0);
        List<Map> headerGroupList = dataList.stream()
                                    .collect(Collectors.groupingBy((vo) -> vo.getOdOrderId()))
                                    .entrySet().stream()
                                    .map(entry -> { Map map = new HashMap();
                                        map.put(indexKey.getAndIncrement(), entry.getValue());
                                        return map; })
                                    .collect(Collectors.toList());

        return headerGroupList;
    }

}