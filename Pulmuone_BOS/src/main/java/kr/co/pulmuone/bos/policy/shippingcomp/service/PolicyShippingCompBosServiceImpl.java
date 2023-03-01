package kr.co.pulmuone.bos.policy.shippingcomp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;
import kr.co.pulmuone.v1.policy.shippingcomp.service.PolicyShippingCompBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전	:	작성일	:  작성자		:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		20200908	박승현		최초작성
 *  1.1		20200908	박승현		shipping -> shippingComp 로 변경(테이블명 PS_SHIPPING > PS_SHIPPING_COMP 변경)
 *  1.2		20200914	박승현		코드 신규 개발 가이드 적용
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class PolicyShippingCompBosServiceImpl implements PolicyShippingCompBosService {

	@Autowired
    private PolicyShippingCompBiz policyShippingCompBiz;

	/**
	 * 택배사 설정 조회
	 *
	 * @param PolicyShippingCompRequestDto
	 * @return PolicyShippingCompDto
	 * @throws 	Exception
	 */
	@Override
	public ApiResult<?> getPolicyShippingCompInfo(Long psShippingCompId) {
		return policyShippingCompBiz.getPolicyShippingCompInfo(psShippingCompId);
	}
	/**
     * 택배사 설정 목록 조회
     *
     * @param PolicyShippingCompRequestDto
     * @return PolicyShippingCompDto
     * @throws 	Exception
     */
	@Override
	public ApiResult<?> getPolicyShippingCompList(PolicyShippingCompRequestDto dto) {
		return policyShippingCompBiz.getPolicyShippingCompList(dto);
	}

	/**
     * 택배사 설정 신규 등록
     *
     * @param PolicyShippingCompRequestDto
     * @return BaseResponseDto
     * @throws 	Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BaseException.class)
	public ApiResult<?> addPolicyShippingComp(PolicyShippingCompRequestDto dto) {
		return policyShippingCompBiz.addPolicyShippingComp(dto);
	}
	/**
     * 택배사 설정 수정
     *
     * @param PolicyShippingCompRequestDto
     * @return BaseResponseDto
     * @throws 	Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BaseException.class)
	public ApiResult<?> putPolicyShippingComp(PolicyShippingCompRequestDto dto) {
		return policyShippingCompBiz.putPolicyShippingComp(dto);
	}
	/**
	 * 택배사 설정 삭제
	 *
	 * @param PolicyShippingCompRequestDto
	 * @return BaseResponseDto
	 * @throws 	Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BaseException.class)
	public ApiResult<?> delPolicyShippingComp(PolicyShippingCompRequestDto dto) {
		return policyShippingCompBiz.delPolicyShippingComp(dto);
	}

	@Override
	public ApiResult<?> getPolicyShippingCompUseAllList() {
		return policyShippingCompBiz.getPolicyShippingCompUseAllList();
	}

	/**
	 * 택배사 목록 조회
	 * @return ApiResult<?>
	 * @throws
	 */
	@Override
	public ApiResult<?> getDropDownPolicyShippingCompList() {
		return policyShippingCompBiz.getDropDownPolicyShippingCompList();
	}


}

