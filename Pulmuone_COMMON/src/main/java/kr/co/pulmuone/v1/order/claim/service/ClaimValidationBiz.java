package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.order.claim.dto.ClaimValidationDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 validation Interface
 * </PRE>
 */
public interface ClaimValidationBiz {

    /**
     * 클레임 validation 처리
     * @param claimStatusCd
     * @param requestDto
     * @return ClaimValidationDto
     * @throws Exception
     */
    ClaimValidationDto claimValidationProcess(String claimStatusCd, OrderClaimRegisterRequestDto requestDto) throws Exception;

}
