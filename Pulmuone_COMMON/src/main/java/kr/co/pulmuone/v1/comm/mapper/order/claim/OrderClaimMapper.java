package kr.co.pulmuone.v1.comm.mapper.order.claim;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalCsRefundRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 27.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderClaimMapper {

	/**
	 * 주문 결제 금액, 배송지 정보 조회
	 * @param odOrderId
	 * @return
	 */
	OrderClaimPayShippingDto getOrderPayShippingInfo(@Param("odOrderId") long odOrderId);

	/**
	 * 주문 출고처 기준 정보 조회
	 * @param odOrderId
	 * @return
	 */
	List<OrderDetlWarehouseShippingDto> getOrderDetlWarehouseShippingList(@Param("odOrderId") long odOrderId);

	/**
	 * 주문PK, 쿠폰발급PK로 주문클레임 상세 할인금액 조회
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	int getOrderClaimDetlDiscountPrice(@Param("odOrderId") Long odOrderId, @Param("pmCouponIssueId") Long pmCouponIssueId);


	int getOrderClaimUserOrderCnt(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto);

	/**
	 * 클레임 상세 리스트 조회
	 * @param odClaimId
	 * @return
	 */
	List<OrderClaimRestoreDto> getOrderClaimDetlList(@Param("odClaimId") long odClaimId);

	/**
	 * 주문 클레임 상태 변경
	 * @param odClaimId
	 * @param targetStatusCd
	 * @return
	 */
	int putOrderClaimDetlClaimStatusCd(@Param("odClaimId") long odClaimId, @Param("targetStatusCd") String targetStatusCd);

	/**
	 * 주문 상세 취소 가능 수량 계산
	 * @param odClaimId
	 * @param claimStatusCd
	 * @return
	 */
	int putOrderDetlCancelCnt(@Param("odClaimId") long odClaimId, @Param("claimStatusCd") String claimStatusCd);

	/**
	 * 거부사유 등록
	 * @param requestDto
	 * @return
	 */
	int putRejectReasonMsg(OrderClaimRegisterRequestDto requestDto);

	/**
	 * 주문상세 취소 할인정보 미사용
	 * @param odClaimId
	 * @return
	 */
	int putOdClaimDetlDiscountNoUse(@Param("odClaimId") long odClaimId, @Param("odClaimDetlIds") List<Long> odClaimDetlIds);

	/**
	 * 주문상세 취소 미사용
	 * @param odClaimId
	 * @param claimStatusCd
	 * @return int
	 */
	int putOdClaimDetlNoUse(@Param("odClaimId") long odClaimId, @Param("claimStatusCd") String claimStatusCd);

	/**
	 * 주문 클레임 추가 결제 정보 마스터 조회
	 * @param orderClaimRestoreRequestDto
	 * @return
	 */
	List<OrderClaimRestoreResultDto> getOrderClaimAddPaymentMaster(OrderClaimRestoreRequestDto orderClaimRestoreRequestDto);


	/**
	 * 주문유형 조회
	 * @param orderClaimRegisterRequestDto
	 * @return
	 */
	OrderClaimOutmallPaymentInfoDto getOrderAgentType(long odOrderId);

	/**
	 * 주문 CS환불 승인 리스트
	 * @param orderClaimRestoreRequestDto
	 * @return
	 */
	Page<ApprovalCsRefundListDto> getApprovalCsRefundList(ApprovalCsRefundRequestDto requestDto);

	/**
	 * CS환불 승인 요청철회
	 * @param approvalVo
	 * @return
	 */
	int putCancelRequestApprovalCsRefund(ApprovalStatusVo approvalVo);

	/**
	 * CS환불 승인 폐기처리
	 * @param approvalVo
	 * @return
	 */
	int putDisposalApprovalCsRefund(ApprovalStatusVo approvalVo);

	/**
	 * CS환불 승인
	 * @param approvalVo
	 * @return
	 */
	int putApprovalProcessCsRefund(ApprovalStatusVo approvalVo);

	/**
	 * CS환불 승인 히스토리 등록
	 * @param history
	 * @return
	 */
	int addClaimStatusHistory(ApprovalStatusVo history);

	/**
	 * 주문 클레임 CS환불승인상태 변경
	 * @param orderClaimCSRefundStatusUpdateDto
	 * @return
	 */
//	int putOrderClaimCsRefundApproveCd(OrderClaimCSRefundStatusUpdateDto orderClaimCSRefundStatusUpdateDto);

	/**
	 * 클레임 귀책구분 업데이트
	 * @param updateClaimVo
	 * @return
	 */
	int putOrderClaimTargetTp(ClaimVo updateClaimVo);

	/**
	 * 현금영수증 발행정보 조회(관리자가 BOS에서 발급한 건에 한함)
	 * @param odOrderId
	 * @return OrderClaimCashReceiptDto
	 */
	OrderClaimCashReceiptDto getOrderCashReceiptByBos(long odOrderId);

	/**
	 * 현금영수증 재발행 금액 조회
	 * @param odOrderId
	 * @param tid
	 * @return OrderPaymentVo
	 */
	OrderPaymentVo getOrderCashReceiptReissuePrice(@Param("odOrderId")long odOrderId, @Param("tid")String tid);

	/**
	 * 현금 영수증 취소값 업데이트
	 * @param odOrderCashReceiptId
	 */
	int putOrderCashReceiptCancelYn(long odOrderCashReceiptId);

}
