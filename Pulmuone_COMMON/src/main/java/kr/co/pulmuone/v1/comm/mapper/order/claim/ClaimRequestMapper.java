package kr.co.pulmuone.v1.comm.mapper.order.claim;

import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 정보 상세 정보 조회 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface ClaimRequestMapper {

	OrderClaimPriceInfoDto getOrderClaimAddPaymentShippingPrice(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<PolicyClaimMallVo> getOrderClaimReasonList(PolicyClaimMallRequestDto dto);

	List<OrderClaimBosReasonResponseDto> getOrderClaimBosReasonList();

	List<OrderClaimSupplyReasonResponseDto> getOrderClaimSupplyReasonList();

	OrderClaimCustomerReasonResponseDto getOrderClaimCustomerReasonInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	OrderMasterInfoDto getOrderMasterInfo(long odOrderId);

	long getOrderUrUserId(long odOrderId);

	List<OrderClaimGoodsInfoDto> getOrderClaimGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimGoodsInfoDto> getOrderDetlClaimGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimGoodsInfoDto> getClaimGreenJuiceGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimGoodsInfoDto> getClaimGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	OrderClaimPaymentInfoDto getOrderClaimPaymentInfo(long odOrderId);

	OrderClaimPriceInfoDto getOrderClaimPriceInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	OrderClaimPriceInfoDto getClaimPriceInfo(long odClaimId);

	OrderClaimPriceInfoDto getOrderClaimGoodsChangePriceInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimCouponInfoDto> getOrderClaimCouponInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimCouponInfoDto> getClaimCouponInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimCouponInfoDto> getOrderClaimGoodsChangeCouponInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimCouponInfoDto> getClaimGoodsChangeCouponInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	OrderClaimAccountInfoDto getOrderClaimAccountInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimAttcInfoDto> getOrderClaimAttcInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	OrderClaimSendShippingInfoDto getOrderClaimSendShippingInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	OrderClaimSendShippingInfoDto getSendShippingInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimShippingInfoDto> getOrderClaimShippingInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimShippingInfoDto> getReceiveShippingInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimShippingInfoDto> getOdClaimShopPickupInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimDetlDtInfoDto> getOrderClaimDetlDtInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	OrderClaimMasterInfoDto getOrderClaimMasterInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimDetlDtInfoDto> getCancelCompleteTargetList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimRefundMaseterTargetDto> getRefundMasterTargetList(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto);

	List<OrderClaimRefundDetailTargetDto> getRefundDetailTargetList(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto);

	OrderClaimReShippingPaymentInfoDto getOrderClaimGoodsPaymentInfo(@Param(value = "odOrderId") long odOrderId, @Param(value = "odOrderDetlId") long odOrderDetlId, @Param(value = "claimCnt") int claimCnt);

	OrderClaimReShippingPaymentInfoDto getOrderClaimReShippingGoodsPaymentInfo(@Param(value = "odOrderId") long odOrderId, @Param(value = "odOrderDetlId") long odOrderDetlId, @Param(value = "claimCnt") int claimCnt);

	List<OrderClaimGoodsInfoDto> getClaimReqGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimGoodsInfoDto> getUndeliveredInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	List<OrderClaimTargetGoodsListDto> getOrderClaimTargetClaimGoodsList(OrderClaimViewRequestDto claimViewReqDto);

	List<OrderClaimTargetGoodsListDto> getOrderClaimTargetGoodsList(OrderClaimViewRequestDto claimViewReqDto);

	List<OrderClaimTargetGoodsListDto> getOrderClaimTargetGreenJuiceGoodsList(OrderClaimViewRequestDto claimViewReqDto);

	List<OrderClaimGoodsInfoDto> getOrderGreenJuiceClaimReturnsScheduleView(OrderClaimViewRequestDto claimViewReqDto);

	/**
	 * 클레임 환불 정보 조회
	 * @param odClaimId
	 * @return
	 */
	OrderClaimPriceInfoDto getClaimRefundInfo(long odClaimId);

	/**
	 * 클레임 상세 추가배송비 목록 조회
	 * @param odClaimId
	 * @return
	 */
	List<OrderClaimAddPaymentShippingPriceDto> getClaimRefundAddPaymentList(long odClaimId);

	/**
	 * 클레임 상세 환불 정보 조회
	 * @param odClaimId
	 * @return
	 */
	List<OrderClaimGoodsPriceInfoDto> getClaimDetlRefundList(long odClaimId);

	/**
	 * 자기 주문 건 확인
	 * @param orderClaimViewRequestDto
	 * @return
	 */
	int getSelfOrderCnt(OrderClaimViewRequestDto orderClaimViewRequestDto);

	/**
	 * 클레임번호에 의한 BOS클레임사유 조회
	 * @param orderClaimViewRequestDto
	 * @return
	 */
	List<OrderClaimGoodsInfoDto> getBosCalimReasonGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto);

	/**
	 * 추가 배송비 결제 금액 조회
	 * @param orderClaimRegisterRequestDto
	 * @return
	 */
	MallOrderClaimAddPaymentResult getOrderClaimAddShippingPrice(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto);

	/**
	 * 반품 추가 배송비 정보 조회
	 * @param odClaimId
	 * @return
	 */
	List<OrderClaimAddShippingPaymentInfoDto> getOrderReturnsShippingPrice(long odClaimId);

	/**
	 * 클레임 신청 상품 정보로 클레임 귀책 구분 조회
	 * @param orderClaimGoodsInfoDto
	 * @return
	 */
	String getOdClaimTargetTpByOrderClaimGoodsInfo(OrderClaimGoodsInfoDto orderClaimGoodsInfoDto);

	/**
	 * 클레임 처리 기획전 정보 얻기
	 * @param evExhibitId
	 * @return
	 */
	List<OrderClaimGiftGoodsInfoDto> getOdClaimTargetEvExhibitInfo(@Param(value = "evExhibitId") long evExhibitId);

	/**
	 * 대체배송 상품 조회시 본상품 배송정보 조회
	 * @param odOrderDetlId
	 * @return
	 */
	OrderClaimGoodsInfoDto getOdOrderDetlGoodsDeliveryType(@Param(value = "odOrderDetlId") long odOrderDetlId);
}
