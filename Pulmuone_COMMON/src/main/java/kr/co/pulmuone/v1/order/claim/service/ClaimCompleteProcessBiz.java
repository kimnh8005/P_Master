package kr.co.pulmuone.v1.order.claim.service;


import kr.co.pulmuone.v1.approval.auth.dto.ApprovalCsRefundRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.ClaimCompleteProcessDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimOutmallPaymentInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;

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
public interface ClaimCompleteProcessBiz {

    /**
     * 취소/반품 완료처리
     * @param requestDto
     * @param isClaimSave
     * @return ClaimCompleteProcessDto
     * @throws Exception
     */
    ClaimCompleteProcessDto setOrderClaimComplete(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave, OrderClaimOutmallPaymentInfoDto agentTypeInfo) throws Exception;

    /**
     * 재배송 처리
     * @param requestDto
     * @return
     * @throws Exception
     */
    ClaimCompleteProcessDto setOrderClaimRedeliveryComplete(OrderClaimRegisterRequestDto requestDto) throws Exception;

    /**
     * CS환불 처리
     * @param requestDto
     * @return
     * @throws Exception
     */
    ClaimCompleteProcessDto setOrderClaimCSRefundApprove(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception;

    /**
     * 클레임 추가결제 PAYMENT 정보 저장
     * @param orderPaymentVo
     * @return void
     * @throws Exception
     */
    ApiResult<?> addPayment(OrderClaimRegisterRequestDto orderPaymentVo) throws Exception;

    /**
     * 클레임 완료 처리
     * @param claimStatusTp
     * @param claimStatusCd
     * @param requestDto
     * @param isClaimSave
     * @return ClaimCompleteProcessDto
     * @throws Exception
     */
    ClaimCompleteProcessDto claimCompleteProcess(String claimStatusTp, String claimStatusCd, OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception;

    /**
     * 클레임 철회 처리
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> calimRestore(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * 추가결제 취소 처리
     * @param odClaimId
     * @param prevStatusCd
     * @param frontTp
     * @throws Exception
     */
    void addShippingPaymentCancel(long odClaimId, String prevStatusCd, int frontTp) throws Exception;

    /**
     * 주문 CS환불 승인 리스트
     * @param requestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> getApprovalCsRefundList(ApprovalCsRefundRequestDto requestDto) throws Exception;

    /**
     * CS환불 승인 요청철회
     * @param odCsIdList
     * @return
     * @throws Exception
     */
    ApiResult<?> putCancelRequestApprovalCsRefund(List<String> odCsIdList) throws Exception;

    /**
     * CS환불 승인
     * @param reqApprStat
     * @param odCsIdList
     * @param statusComment
     * @return
     * @throws Exception
     */
    ApiResult<?> putApprovalProcessCsRefund(String reqApprStat, List<String> odCsIdList, String statusComment) throws Exception;

    /**
     * CS환불 승인 폐기처리
     * @param ApprovalCsRefundRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> putDisposalApprovalCsRefund(ApprovalCsRefundRequestDto approvalCsRefundRequestDto) throws Exception;

    /**
     * 주문 클레임 CS환불승인상태 변경
     * @param orderClaimCSRefundStatusUpdateDto
     * @return
     */
//    ApiResult<?> putOrderClaimCsRefundApproveCd(OrderClaimCSRefundStatusUpdateDto orderClaimCSRefundStatusUpdateDto) throws Exception;

    /**
     * 관리자 현금영수증발행 주문건 취소처리
     * @param orderClaimRegisterReqDto
     * @return
     */
    ApiResult<?> receiptCancel(OrderClaimRegisterRequestDto orderClaimRegisterReqDto) throws Exception;
}
