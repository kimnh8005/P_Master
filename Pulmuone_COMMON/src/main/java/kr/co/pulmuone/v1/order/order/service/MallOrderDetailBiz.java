package kr.co.pulmuone.v1.order.order.service;

import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingInfoByOdOrderIdResultVo;
import kr.co.pulmuone.v1.order.order.dto.mall.*;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

public interface MallOrderDetailBiz {
	/** 주문상세 리스트  조회 */
	public ApiResult<?> getOrderDetail(Long odOrderId);

	/** 주문상세 리스트  조회 */
	public MallOrderDetailListResponseDto getOrderDetailResponseDto(Long odOrderId, boolean isGroupByOdShippingPriceId);

	/** 주문 상담원 결제 상세 조회 */
	public ApiResult<?> getDirectOrderDetail(Long odOrderId) throws Exception;

	/** 주문 상담원 결제 요청 */
	public ApiResult<?> applyPaymentDirectOrder(MallApplyPaymentDirectOrderRequestDto reqDto) throws Exception;

	/** 주문 클레임 상세 리스트  조회 */
	public ApiResult<?> getClaimDetail(Long odOrderId);

	/** 주문 상세 결제 정보 */
	MallOrderDetailPayResultDto getOrderDetailPayInfo(long odOrderId);

	/** 주문상세 배송지  조회 */
	MallOrderDetailShippingZoneDto getOrderDetailShippingInfo(long odOrderId);

	/** 주문상세 리스트  조회 */
	List<MallOrderDetailGoodsDto> getOrderDetailGoodsList(long odOrderId);

	/** 주문배송지PK로 주문정보 조회 */
	List<OrderDetailByOdShippingZondIdResultDto> getOrderDetailInfoByOdShippingZoneId(Long odShippingZoneId);

	/** 주문PK로 배송정책별 주문정보 조회 */
	List<ShippingInfoByOdOrderIdResultVo> getShippingInfoByOdOrderId(Long odOrderId) throws Exception;

	/** 주문PK로 상품PK별 주문정보 조회 */
	List<ShippingInfoByOdOrderIdResultVo> getOrderDetailInfoByOdOrderId(Long odOrderId) throws Exception;

	/** 주문상세PK로 주문정보 조회 */
	MallOrderDto getOrderInfoByOdOrderDetlId(Long odOrderDetlId);

	/** 주문 정보 조회 */
	MallOrderDto getOrder(long odOrderId, String urUserId, String guestCi);

	/** 배송가능여부 체크 위한 주문정보 조회 */
	List<HashMap> getOrderInfoForShippingPossibility(Long odOrderId, Long odOrderDetlId);
}