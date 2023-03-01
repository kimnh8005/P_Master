package kr.co.pulmuone.v1.policy.fee.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeHistListRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;

/**
 * <PRE>
 * Forbiz Korea
 * 기본 수수료 관리 Biz
 * </PRE>
 *
 */

public interface OmBasicFeeBiz {

	/**
	 * 기본 수수료 목록 조회
	 *
	 * @param omBasicFeeListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> getOmBasicFeeList(OmBasicFeeListRequestDto omBasicFeeListRequestDto) throws Exception;

	/**
	 * 기본 수수료 상세 조회
	 *
	 * @param omBasicFeeRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> getOmBasicFee(OmBasicFeeRequestDto omBasicFeeRequestDto) throws Exception;

	/**
	 * 기본 수수료 이력 목록 조회
	 *
	 * @param omBasicFeeHistListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> getOmBasicHistFeeList(OmBasicFeeHistListRequestDto omBasicFeeHistListRequestDto) throws Exception;

	/**
	 * 기본 수수료 등록
	 *
	 * @param omBasicFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> addOmBasicFee(OmBasicFeeVo omBasicFeeVo) throws Exception;

	/**
	 * 기본 수수료 수정
	 *
	 * @param omBasicFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> putOmBasicFee(OmBasicFeeVo omBasicFeeVo) throws Exception;

	/**
	 * 기본 수수료 판매처그룹 수정
	 *
	 * @param omBasicFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> putOmBasicFeeSellersGroup(OmBasicFeeVo omBasicFeeVo) throws Exception;


}
