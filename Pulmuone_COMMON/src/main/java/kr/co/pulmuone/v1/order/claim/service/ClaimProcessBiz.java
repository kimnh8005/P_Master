package kr.co.pulmuone.v1.order.claim.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.OdAddPaymentReqInfo;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayRequestDto;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
public interface ClaimProcessBiz {

    /**
     * 취소/반품 처리
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addOrderClaim(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * 취소/반품 처리 - Non Transaction
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addOrderClaimNonTransaction(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * 추가결제 후 취소 / 반품 처리
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addAddPaymentAfterOrderClaimRegister(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;


    /**
     * 취소/반품 처리
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> orderClaimInfo(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;


    /**
     * 추가 결제 처리
     * @param requestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addPayment(OrderClaimRegisterRequestDto requestDto) throws Exception;

    /**
     * 비인증 결제 처리
     * @param orderClaimCardPayRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addCardPayOrderCreate(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception;

    /**
     * 무통장 입금
     * @param orderClaimCardPayRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addBankBookOrderCreate(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception;

    /**
     * 주문PK, 쿠폰발급PK로 주문클레임 상세 할인금액 조회
     * @param odOrderId
     * @param pmCouponIssueId
     * @param odClaimDetlIds
     * @return int
     */
    int getOrderClaimDetlDiscountPrice(Long odOrderId, Long pmCouponIssueId, Long odClaimId, List<Long> odClaimDetlIds) throws Exception;

    /**
     * 추가 결제 정보 조회
     * @param odAddPaymentReqInfoId
     * @return
     * @throws Exception
     */
    OdAddPaymentReqInfo getOdAddPaymentReqInfo(long odAddPaymentReqInfoId) throws Exception;

    /**
     * 녹즙 취소/반품 처리
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addOrderClaimGreenJuice(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * 주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> putOrderClaimDetlBosClaimReason(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * MALL > 상담접수상태의 렌탈상품 취소
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addOrderClaimRental(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * 주문클레임정보 업데이트 처리
     * @param mallOrderClaimAddPaymentResult
     * @return
     */
    OrderClaimRegisterResponseDto putOrderClaimInfo(MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult) throws Exception;

    /**
     * CS 환불 정보 등록
     * @param orderCSRefundRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addOrderCSRefundRegister(OrderCSRefundRegisterRequestDto orderCSRefundRegisterRequestDto) throws Exception;

    /**
     * 주문 클레임 CS환불승인상태 변경
     * @param orderCSRefundRegisterRequest
     * @return
     */
    ApiResult<?> putOrderClaimCsRefundApproveCd(OrderCSRefundRegisterRequestDto orderCSRefundRegisterRequest) throws Exception;

    /**
     * CS 환불 승인 처리
     * @param orderCSRefundRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> procOrderCSRefundApprove(OrderCSRefundRegisterRequestDto orderCSRefundRegisterRequestDto) throws Exception;

    /**
     * 클레임 OD_PAYMENT 환불 정보 Set
     * @param odOrderId
     * @param odClaimId
     * @param claimStatusCd
     * @throws Exception
     */
    void setClaimRefundPaymentInfo(long odOrderId, long odClaimId, String claimStatusCd) throws Exception;

    /**
     * 추가 결제 클레임 정보 조회
     * @param pgObject
     * @return
     */
    OrderClaimAddPaymentInfoDto getAddPaymentClaimInfo(Object pgObject) throws Exception;

    /**
     * 추가 결제 클레임 정보 업데이트
     * @param orderClaimAddPaymentInfoDto
     */
    void putAddPaymentClaimInfo(OrderClaimAddPaymentInfoDto orderClaimAddPaymentInfoDto) throws Exception;

    /**
     * 클레임 거부 처리
     * @param claimStatusTp
     * @param claimStatusCd
     * @param requestDto
     * @param isClaimSave
     * @return
     * @throws Exception
     */
    boolean procClaimDenyDefer(String claimStatusTp, String claimStatusCd, OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception;

    /**
     * 쿠폰 정보 조회
     * @param goodsCouponDto
     * @return
     * @throws Exception
     */
    List<OrderClaimCouponInfoDto> getCouponInfoList(OrderClaimViewRequestDto goodsCouponDto) throws Exception;

    /**
     * 주문클레임 상세 할인정보 시퀀스 조회
     * @return
     * @throws Exception
     */
    long getOdClaimDetlDiscountId() throws Exception;
}
