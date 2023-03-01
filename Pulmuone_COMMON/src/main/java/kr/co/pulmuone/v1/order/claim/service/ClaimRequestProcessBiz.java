package kr.co.pulmuone.v1.order.claim.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.claim.dto.ClaimRequestProcessDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimPriceInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCardPayRequestDto;


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
public interface ClaimRequestProcessBiz {

    /**
     * 환불 금액 정보 조회
     * @return
     * @throws Exception
     */
    OrderClaimPriceInfoDto getRefundPriceInfo(OrderClaimRegisterRequestDto requestDto) throws Exception;

    /**
     * 취소/반품 처리
     * @param orderClaimRegisterRequestDto
     * @return
     * @throws Exception
     */
    ClaimRequestProcessDto setOrderClaimRequest(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception;

    /**
     * 추가 결제 처리
     * @param requestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addPayment(OrderClaimRegisterRequestDto requestDto) throws Exception;

    /**
     * 비인증 결제 처리
     * @param orderCardPayRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addCardPayOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception;

    /**
     * 무통장 입금
     * @param orderCardPayRequestDto
     * @return
     * @throws Exception
     */
    ApiResult<?> addBankBookOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception;

    /**
     * 클레임 요청 처리
     * @param claimStatusTp
     * @param claimStatusCd
     * @param requestDto
     * @param isClaimSave
     * @return ClaimRequestProcessDto
     * @throws Exception
     */
    ClaimRequestProcessDto claimRequestProcess(String claimStatusTp, String claimStatusCd, OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception;
}
