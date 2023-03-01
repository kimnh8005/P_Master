package kr.co.pulmuone.v1.order.claim.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세정보 조회 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국       최초작성
 * =======================================================================
 * </PRE>
 */
public interface ClaimRequestBiz {

    /**
     * @Desc 주문클레임 신청 화면에서 사유 목록 조회
     * @return
     */
    ApiResult<?> getOrderClaimReasonList(PolicyClaimMallRequestDto dto);

    /**
     * @Desc 프론트에서 주문상태가 배송중, 배송완료 일 때 반품신청 대상 상품 목록
     * @return
     */
    ApiResult<?> getOrderClaimInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception;

    /**
     * @Desc 녹즙 클레임 신청 조회
     * @return
     */
    ApiResult<?> getOrderGreenJuiceClaimView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception;

    /**
     * @Desc 녹즙 클레임 반품 스케쥴 배송정보 조회
     * @return
     */
    ApiResult<?> getOrderGreenJuiceClaimReturnsScheduleView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception;

    /**
     * @Desc 녹즙 클레임 재배송 스케쥴 도착예정일 정보 조회
     * @return
     */
    ApiResult<?> getOrderGreenJuiceClaimExchangeScheduleView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception;

    /**
     * 주문클레임 재배송 상품 금액 정보 조회
     * @param orderClaimReShippingPaymentInfoRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> getOrderClaimReShippingGoodsPriceInfo(OrderClaimReShippingPaymentInfoRequestDto orderClaimReShippingPaymentInfoRequestDto) throws Exception;

    /**
     * 주문클레임 마스터 조회
     * @param orderClaimViewRequestDto
     * @return OrderClaimMasterInfoDto
     */
    OrderClaimMasterInfoDto getOrderClaimMasterInfo(OrderClaimViewRequestDto orderClaimViewRequestDto);

    /**
     * @Desc 클레임정보 클레임번호에 의한 BOS클레임사유 조회
     * @return
     */
    ApiResult<?> getOrderClaimBosClaimReasonView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception;

    /**
    * 추가배송비 정보 조회
     * @return
     * @throws Exception
     */
    MallOrderClaimAddPaymentResult getOrderClaimAddShippingPriceInfo(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * 추가 배송비 결제 금액 조회
     * @param mallOrderClaimAddPaymentRequest
     * @return
     */
    ApiResult<?> getOrderClaimAddShippingPrice(MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest) throws Exception;

    /**
     * 추가 배송비 결제 요청
     * @param mallOrderClaimAddPaymentRequest
     * @return
     * @throws Exception
     */
    ApiResult<?> applyOrderClaimAddShippingPrice(MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest) throws Exception;

    /**
     * 쇼핑몰 사유 목록 조회
     * @param policyClaimMallRequestDto
     * @return
     * @throws Exception
     */
    public ApiResult<?> getPolicyClaimMallListForMall(PolicyClaimMallRequestDto policyClaimMallRequestDto) throws Exception;
}
