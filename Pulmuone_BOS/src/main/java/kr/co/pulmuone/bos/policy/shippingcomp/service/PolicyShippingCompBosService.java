package kr.co.pulmuone.bos.policy.shippingcomp.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200908    박승현              최초작성
 *  1.1	   2020.12.29  이명수				 사용중인 택배사 전체 목록 추
 * =======================================================================
 * </PRE>
 */

public interface PolicyShippingCompBosService {

	ApiResult<?> getPolicyShippingCompList(PolicyShippingCompRequestDto dto);
	ApiResult<?> getPolicyShippingCompInfo(Long psShippingCompId);
	ApiResult<?> addPolicyShippingComp(PolicyShippingCompRequestDto dto);
	ApiResult<?> putPolicyShippingComp(PolicyShippingCompRequestDto dto);
	ApiResult<?> delPolicyShippingComp(PolicyShippingCompRequestDto dto);
	ApiResult<?> getPolicyShippingCompUseAllList();
	ApiResult<?> getDropDownPolicyShippingCompList();
}

