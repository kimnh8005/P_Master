package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.batch.order.dto.search.ReturnSalesOrderSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import kr.co.pulmuone.v1.batch.order.dto.search.DeliveryOrderSearchRequestDto;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.OrderErpMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderErpService {

    private final OrderErpMapper orderErpMapper;

    /**
     * 용인물류 주문 리스트 조회
     * @param params
     * @return List<DeliveryOrderListVo>
     * @throws
     */
    public List<DeliveryOrderListVo> selectDeliveryOrderList(DeliveryOrderSearchRequestDto params) throws BaseException {
        return orderErpMapper.selectDeliveryOrderList(params);
    }

    /**
     * 백암물류 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<CjOrderListVo>
     * @throws
     */
    public List<CjOrderListVo> selectCjOrderList(String urWarehouseId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectCjOrderList(urWarehouseId, orderStatusCd, null);
    }

    /**
     * 백암물류 주문 리스트 조회 (외부몰 분리)
     * @param urWarehouseId
     * @param orderStatusCd
     * @param omSellersIdList
     * @return List<CjOrderListVo>
     * @throws
     */
    public List<CjOrderListVo> selectCjOrderList(String urWarehouseId, String orderStatusCd, List<String> omSellersIdList) throws BaseException {
        return orderErpMapper.selectCjOrderList(urWarehouseId, orderStatusCd, omSellersIdList);
    }

    /**
     * 올가 매장배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<OrgaStoreDeliveryOrderListVo>
     * @throws
     */
    public List<OrgaStoreDeliveryOrderListVo> selectOrgaStoreDeliveryOrderList(String urWarehouseId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectOrgaStoreDeliveryOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 올가 기타주문 리스트 조회
     * @param urWarehouseGroupId
     * @param orderStatusCd
     * @return List<OrgaEtcOrderListVo>
     * @throws
     */
    public List<OrgaEtcOrderListVo> selectOrgaEtcOrderList(String urWarehouseGroupId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectOrgaEtcOrderList(urWarehouseGroupId, orderStatusCd, null);
    }

    /**
     * 올가 기타주문 리스트 조회 (외부몰 분리)
     * @param urWarehouseGroupId
     * @param orderStatusCd
     * @param omSellersIdList
     * @return List<OrgaEtcOrderListVo>
     * @throws
     */
    public List<OrgaEtcOrderListVo> selectOrgaEtcOrderList(String urWarehouseGroupId, String orderStatusCd, List<String> omSellersIdList) throws BaseException {
        return orderErpMapper.selectOrgaEtcOrderList(urWarehouseGroupId, orderStatusCd, omSellersIdList);
    }

    /**
     * 하이톡 택배배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<HitokNormalDeliveryOrderListVo>
     * @throws
     */
    public List<HitokNormalDeliveryOrderListVo> selectHitokNormalDeliveryOrderList(String urWarehouseId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectHitokNormalDeliveryOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 하이톡 일일배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<HitokDeliveryOrderListVo>
     * @throws
     */
    public List<HitokDailyDeliveryOrderListVo> selectHitokDailyDeliveryOrderList(String urWarehouseId, String orderStatusCd, String orderSchStatus) throws BaseException {
        return orderErpMapper.selectHitokDailyDeliveryOrderList(urWarehouseId, orderStatusCd, orderSchStatus);
    }

    /**
     * 풀무원건강생활(LDS) 주문 리스트 조회
     * @param urWarehouseGroupId
     * @param orderStatusCd
     * @return List<LohasDirectSaleOrderListVo>
     * @throws
     */
    public List<LohasDirectSaleOrderListVo> selectLohasDirectSaleOrderList(String urWarehouseGroupId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectLohasDirectSaleOrderList(urWarehouseGroupId, orderStatusCd);
    }

    /**
     * 베이비밀 일일배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<BabymealOrderListVo>
     * @throws
     */
    public List<BabymealOrderListVo> selectBabymealDailyOrderList(String[] urWarehouseId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectBabymealDailyOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 베이비밀 택배배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<BabymealNormalOrderListVo>
     * @throws
     */
    public List<BabymealNormalOrderListVo> selectBabymealNormalOrderList(String urWarehouseId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectBabymealNormalOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 매출 주문 리스트 조회
     * @param urWarehouseId
     * @param urSupplierId
     * @param orderStatusCd
     * @return List<SalesOrderListVo>
     * @throws
     */
    public List<SalesOrderListVo> selectSalesOrderList(String[] urWarehouseId, String urSupplierId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectSalesOrderList(urWarehouseId, urSupplierId, orderStatusCd);
    }

    /**
     * 반품매출 주문 리스트 조회
     * @param params
     * @return List<ReturnSalesOrderListVo>
     * @throws
     */
    public List<ReturnSalesOrderListVo> selectReturnSalesOrderList(ReturnSalesOrderSearchRequestDto params) throws BaseException {
        return orderErpMapper.selectReturnSalesOrderList(params);
    }

    /**
     * 하이톡 택배배송 반품 주문 리스트 조회
     * @param params
     * @return List<HitokDeliveryReturnOrderListVo>
     * @throws
     */
    public List<HitokDeliveryReturnOrderListVo> selectHitokNormalDeliveryReturnOrderList(ReturnSalesOrderSearchRequestDto params) throws BaseException {
        return orderErpMapper.selectHitokNormalDeliveryReturnOrderList(params);
    }

    /**
     * 풀무원건강생활(LDS) 반품 주문 리스트 조회
     * @param params
     * @return List<LohasDirectSaleReturnOrderListVo>
     * @throws
     */
    public List<LohasDirectSaleReturnOrderListVo> selectLohasDirectSaleReturnOrderList(ReturnSalesOrderSearchRequestDto params) throws BaseException {
        return orderErpMapper.selectLohasDirectSaleReturnOrderList(params);
    }

    /**
     * 성공 주문 배치완료 업데이트 (매출 O)
     * @param odOrderDetlId
     * @param oriSysSeq
     * @return void
     * @throws
     */
    public void putOrderBatchCompleteUpdate(String odOrderDetlId, String oriSysSeq) throws BaseException {
        String orderStatusCd = OrderEnums.OrderStatus.DELIVERY_READY.getCode();
        orderErpMapper.putOrderBatchCompleteUpdate(orderStatusCd, odOrderDetlId, oriSysSeq);
        // 이력 등록
        addOrderDetailStatusHist(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());
    }

    /**
     * 대체배송 주문상세정보 업데이트
     *
     * @param odOrderDetlId
     * @throws BaseException
     */
    public void putAlternateDeliveryOrder(String odOrderDetlId, String alternateDeliveryTp) throws BaseException {
        orderErpMapper.putAlternateDeliveryOrder(odOrderDetlId, alternateDeliveryTp);
    }

    /**
     * 성공 주문 배치완료 업데이트 (매출 X)
     * @param odOrderDetlId
     * @param oriSysSeq
     * @return void
     * @throws
     */
    public void putOrderBatchCompleteUpdateNotSales(String odOrderDetlId, String oriSysSeq) throws BaseException {
        String orderStatusCd = OrderEnums.OrderStatus.DELIVERY_READY.getCode();
        orderErpMapper.putOrderBatchCompleteUpdateNotSales(orderStatusCd, odOrderDetlId, oriSysSeq);
        // 이력 등록
        addOrderDetailStatusHist(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());
    }

    /**
     * 주문 상태 이력 등록
     * @param odOrderDetlId
     * @param createId
     * @param orderStatusCd
     * @return int
     * @throws BaseException
     */
    protected int addOrderDetailStatusHist(long odOrderDetlId, long createId, String orderStatusCd) throws BaseException {
        OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(OrderEnums.OrderStatus.DELIVERY_READY.getCode());

        OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
                                        .odOrderDetlId(odOrderDetlId)
                                        .statusCd(orderStatusCd)
                                        .histMsg(orderDetailStatusHistMsg.getMessage())
                                        .createId(createId)
                                        .build();

        return orderErpMapper.addOrderDetailStatusHist(orderDetlHistVo);
    }

    /**
     * 성공 매출주문 배치완료 업데이트
     * @param odOrderDetlId
     * @param oriSysSeq
     * @return void
     * @throws
     */
    public void putSalesOrderBatchCompleteUpdate(String odOrderDetlId, String oriSysSeq) throws BaseException {
        String orderStatusCd = OrderEnums.OrderStatus.DELIVERY_READY.getCode();
        orderErpMapper.putSalesOrderBatchCompleteUpdate(orderStatusCd, odOrderDetlId, oriSysSeq);
    }

    /**
     * 성공 반품매출 주문 배치완료 업데이트
     * @param odClaimDetlId
     * @return void
     * @throws
     */
    public void putReturnSalesOrderBatchCompleteUpdate(String odClaimDetlId) throws BaseException {
        orderErpMapper.putReturnSalesOrderBatchCompleteUpdate(odClaimDetlId);
    }

    /**
     * CJ(백암)물류 매출 주문 리스트 조회
     * @param urWarehouseId
     * @param urSupplierId
     * @param orderStatusCd
     * @return List<SalesOrderListVo>
     * @throws
     */
    public List<SalesOrderListVo> selectCjSalesOrderList(String urWarehouseId, String urSupplierId, String orderStatusCd) throws BaseException {
        return orderErpMapper.selectCjSalesOrderList(urWarehouseId, urSupplierId, orderStatusCd);
    }

    /**
     * 매출만 연동 주문 리스트 조회
     * @param urWarehouseId
     * @param urSupplierId
     * @return List<SalesOrderListVo>
     * @throws
     */
    public List<SalesOrderListVo> selectSalesOnlyOrderList(String urWarehouseId, String urSupplierId) throws BaseException {
        return orderErpMapper.selectSalesOnlyOrderList(urWarehouseId, urSupplierId);
    }

    /**
     * 연동 셀러코드 리스트
     * @return List<String>
     * @throws
     */
    public List<String> getErpOutMallIfSellerList(String erpInterFaceYn) throws BaseException {
        return orderErpMapper.getErpOutMallIfSellerList(erpInterFaceYn);
    }

}
