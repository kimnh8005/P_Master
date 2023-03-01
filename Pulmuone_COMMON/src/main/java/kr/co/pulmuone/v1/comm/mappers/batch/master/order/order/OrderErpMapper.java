package kr.co.pulmuone.v1.comm.mappers.batch.master.order.order;

import kr.co.pulmuone.v1.batch.order.dto.search.DeliveryOrderSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.dto.search.ReturnSalesOrderSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import kr.co.pulmuone.v1.batch.order.etc.dto.TrackingNumberOrderInfoDto;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.TrackingNumberDetailVo;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.UnreleasedDetailVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Mapper
 * </PRE>
 */

@Mapper
public interface OrderErpMapper {

    /**
     * 용인물류 주문 리스트 조회
     * @param params
     * @return List<DeliveryOrderListVo>
     * @throws
     */
    List<DeliveryOrderListVo> selectDeliveryOrderList(DeliveryOrderSearchRequestDto params) throws BaseException;

    /**
     * 백암물류 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<CjOrderListVo>
     * @throws
     */
    List<CjOrderListVo> selectCjOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 백암물류 주문 리스트 조회 (외부몰 분리)
     * @param urWarehouseId
     * @param orderStatusCd
     * @param omSellersIdList
     * @return List<CjOrderListVo>
     * @throws
     */
    List<CjOrderListVo> selectCjOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("orderStatusCd") String orderStatusCd, @Param("omSellersIdList") List<String> omSellersIdList) throws BaseException;

    /**
     * 올가 매장배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<OrgaStoreDeliveryOrderListVo>
     * @throws
     */
    List<OrgaStoreDeliveryOrderListVo> selectOrgaStoreDeliveryOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 올가 기타주문 리스트 조회
     * @param urWarehouseGroupId
     * @param orderStatusCd
     * @return List<OrgaEtcOrderListVo>
     * @throws
     */
    List<OrgaEtcOrderListVo> selectOrgaEtcOrderList(@Param("urWarehouseGroupId") String urWarehouseGroupId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 올가 기타주문 리스트 조회 (외부몰 분리)
     * @param urWarehouseGroupId
     * @param orderStatusCd
     * @return List<OrgaEtcOrderListVo>
     * @throws
     */
    List<OrgaEtcOrderListVo> selectOrgaEtcOrderList(@Param("urWarehouseGroupId") String urWarehouseGroupId, @Param("orderStatusCd") String orderStatusCd, @Param("omSellersIdList") List<String> omSellersIdList) throws BaseException;

    /**
     * 하이톡 택배배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<HitokNormalDeliveryOrderListVo>
     * @throws
     */
    List<HitokNormalDeliveryOrderListVo> selectHitokNormalDeliveryOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 하이톡 일일배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @param orderSchStatus
     * @return List<HitokDeliveryOrderListVo>
     * @throws
     */
    List<HitokDailyDeliveryOrderListVo> selectHitokDailyDeliveryOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("orderStatusCd") String orderStatusCd, @Param("orderSchStatus") String orderSchStatus) throws BaseException;

    /**
     * 풀무원건강생활(LDS) 주문 리스트 조회
     * @param urWarehouseGroupId
     * @param orderStatusCd
     * @return List<LohasDirectSaleOrderListVo>
     * @throws
     */
    List<LohasDirectSaleOrderListVo> selectLohasDirectSaleOrderList(@Param("urWarehouseGroupId") String urWarehouseGroupId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 베이비밀 일일배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<BabymealOrderListVo>
     * @throws
     */
    List<BabymealOrderListVo> selectBabymealDailyOrderList(@Param("urWarehouseId") String[] urWarehouseId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 베이비밀 택배배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<BabymealNormalOrderListVo>
     * @throws
     */
    List<BabymealNormalOrderListVo> selectBabymealNormalOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 매출 주문 리스트 조회
     * @param urWarehouseId
     * @param urSupplierId
     * @param orderStatusCd
     * @return List<SalesOrderListVo>
     * @throws
     */
    List<SalesOrderListVo> selectSalesOrderList(@Param("urWarehouseId") String[] urWarehouseId, @Param("urSupplierId") String urSupplierId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 반품매출 주문 리스트 조회
     * @param params
     * @return List<ReturnSalesOrderListVo>
     * @throws
     */
    List<ReturnSalesOrderListVo> selectReturnSalesOrderList(ReturnSalesOrderSearchRequestDto params) throws BaseException;

    /**
     * 하이톡 택배배송 반품 주문 리스트 조회
     * @param params
     * @return List<HitokDeliveryReturnOrderListVo>
     * @throws
     */
    List<HitokDeliveryReturnOrderListVo> selectHitokNormalDeliveryReturnOrderList(ReturnSalesOrderSearchRequestDto params) throws BaseException;

    /**
     * 풀무원건강생활(LDS) 반품 주문 리스트 조회
     * @param params
     * @return List<LohasDirectSaleReturnOrderListVo>
     * @throws
     */
    List<LohasDirectSaleReturnOrderListVo> selectLohasDirectSaleReturnOrderList(ReturnSalesOrderSearchRequestDto params) throws BaseException;

    /**
     * 매출만 연동 주문 리스트 조회
     * @param urWarehouseId
     * @param urSupplierId
     * @return List<SalesOrderListVo>
     * @throws
     */
    List<SalesOrderListVo> selectSalesOnlyOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("urSupplierId") String urSupplierId) throws BaseException;

    /**
     * 성공 주문 배치완료 업데이트 (매출 O)
     * @param orderStatusCd
     * @param odOrderDetlId
     * @param oriSysSeq
     * @return void
     * @throws
     */
    void putOrderBatchCompleteUpdate(@Param("orderStatusCd") String orderStatusCd, @Param("odOrderDetlId") String odOrderDetlId, @Param("oriSysSeq") String oriSysSeq) throws BaseException;

    /**
     * 대체배송 주문상세 업데이트
     * @param odOrderDetlId
     * @throws BaseException
     */
    void putAlternateDeliveryOrder(@Param("odOrderDetlId") String odOrderDetlId, @Param("alternateDeliveryTp") String alternateDeliveryTp) throws BaseException;

    /**
     * 성공 주문 배치완료 업데이트 (매출 X)
     * @param orderStatusCd
     * @param odOrderDetlId
     * @param oriSysSeq
     * @return void
     * @throws
     */
    void putOrderBatchCompleteUpdateNotSales(@Param("orderStatusCd") String orderStatusCd, @Param("odOrderDetlId") String odOrderDetlId, @Param("oriSysSeq") String oriSysSeq) throws BaseException;

    /**
     * 성공 매출주문 배치완료 업데이트
     * @param orderStatusCd
     * @param odOrderDetlId
     * @param oriSysSeq
     * @return void
     * @throws
     */
    void putSalesOrderBatchCompleteUpdate(@Param("orderStatusCd") String orderStatusCd, @Param("odOrderDetlId") String odOrderDetlId, @Param("oriSysSeq") String oriSysSeq) throws BaseException;

    /**
     * 성공 반품매출 주문 배치완료 업데이트
     * @param odClaimDetlId
     * @return void
     * @throws
     */
    void putReturnSalesOrderBatchCompleteUpdate(@Param("odClaimDetlId") String odClaimDetlId) throws BaseException;

    /**
     * 주문상세번호 조회
     * @param trackingNumberDetailInfo
     * @return int
     * @throws
     */
    TrackingNumberOrderInfoDto getodOrderDetlId(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException;

    /**
     * 송장번호 삭제
     * @param trackingNumberDetailInfo
     * @return int
     * @throws
     */
	int delOrderTrackingNumber(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException;

	/**
     * 송장번호 등록
     * @param trackingNumberDetailInfo
     * @return int
     * @throws
     */
	int addOrderTrackingNumber(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException;

    /**
     * 가맹점 코드 업데이트
     * @param trackingNumberDetailInfo
     * @return int
     * @throws
     */
    int putOrderStoCd(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException;

    /**
     * 스케쥴 관리 배송완료 여부 업데이트
     * @param trackingNumberDetailInfo
     * @return int
     * @throws
     */
    int putOrderDailyScheduleDeliveryComplete(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException;

    /**
     * 배송중 업데이트
     * @param odOrderDetlId
     * @param createId
     * @param orderStatusCd
     * @return int
     * @throws
     */
    int putOrderDetlDeliveryIng(@Param("odOrderDetlId") long odOrderDetlId, @Param("createId") long createId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;


    /**
     * 주문 상태 이력 등록
     * @param orderDetlHistVo
     * @return
     * @throws BaseException
     */
    int addOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo) throws BaseException;

	/**
     * 일일배송 스케줄 송장번호 수정
     * @param trackingNumberDetailInfo
     * @return int
     * @throws
     */
	int putOrderDailyScheduleTrakingNumber(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException;

	/**
     * 미출정보 등록
     * @param unreleasedDetailVo
     * @return int
     * @throws
     */
	int addOrderUnreleased(UnreleasedDetailVo unreleasedDetailVo) throws Exception;

	/**
     * 미출 정보 주문번호 내 반품 승인건 조회
     * @param unreleasedDetailVo
     * @return int
     * @throws
     */
	int getOrderClaimReturnsIngCnt(UnreleasedDetailVo unreleasedDetailVo) throws Exception;

	/**
     * 미출 정보 주문번호 내 반품 승인 클레임 정보 조회
     * @param unreleasedDetailVo
     * @return int
     * @throws
     */
	List<OrderClaimRegisterRequestDto> getOrderClaimReturnsIngInfoList(UnreleasedDetailVo unreleasedDetailVo) throws Exception;

	/**
     * 미출 정보 주문번호 내 반품 승인 클레임 상세 정보 조회
     * @param orderClaimItem
     * @return int
     * @throws
     */
	List<OrderClaimGoodsInfoDto> getOrderClaimReturnsIngDetlInfoList(OrderClaimRegisterRequestDto orderClaimItem) throws Exception;

	/**
     * 반품 승인 클레임 정보 반품 요청 상태 변경 처리
     * @param orderClaimItem
     * @return int
     * @throws
     */
	int putOrderClaimDetlInfoClaimStatusCd(OrderClaimRegisterRequestDto orderClaimItem) throws Exception;

    /**
     * CJ(백암)물류 매출 주문 리스트 조회
     * @param urWarehouseId
     * @param urSupplierId
     * @param orderStatusCd
     * @return List<SalesOrderListVo>
     * @throws
     */
    List<SalesOrderListVo> selectCjSalesOrderList(@Param("urWarehouseId") String urWarehouseId, @Param("urSupplierId") String urSupplierId, @Param("orderStatusCd") String orderStatusCd) throws BaseException;

    /**
     * 취소요청 존재시 클레임 거부사유 업데이트
     * @param odOrderDetlId
     * @return int
     * @throws
     */
    int putClaimRejectReasonMsg(@Param("odClaimId") long odClaimId, @Param("rejectReasonMsg") String rejectReasonMsg) throws BaseException;

    /**
     * 취소요청 존재시 거부 업데이트
     * @param odOrderDetlId
     * @return int
     * @throws
     */
    int putCancelRequestDenial(@Param("odOrderDetlId") long odOrderDetlId, @Param("orderStatusCd") String orderStatusCd, @Param("updateOrderStatusCd") String updateOrderStatusCd) throws BaseException;

    /**
     * 연동 판매처 리스트
     * @return String
     * @throws
     */
    List<String> getErpOutMallIfSellerList(@Param("erpInterFaceYn") String erpInterFaceYn) throws BaseException;
}
