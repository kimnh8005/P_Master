package kr.co.pulmuone.v1.policy.fee.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.fee.dto.OmLogisticsFeeHistListRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.OmLogisticsFeeListRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.OmLogisticsFeeRequestDto;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeVo;

/**
 * <PRE>
 * Forbiz Korea
 * 물류 수수료 관리 Biz
 * </PRE>
 *
 */

public interface OmLogisticsFeeBiz {

	/**
	 * 물류 수수료 목록 조회
	 *
	 * @param omLogisticsFeeListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> getOmLogisticsFeeList(OmLogisticsFeeListRequestDto omLogisticsFeeListRequestDto) throws Exception;

	/**
	 * 물류 수수료 상세 조회
	 *
	 * @param omLogisticsFeeRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> getOmLogisticsFee(OmLogisticsFeeRequestDto omLogisticsFeeRequestDto) throws Exception;

	/**
	 * 물류 수수료 이력 목록 조회
	 *
	 * @param omLogisticsFeeHistListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> getOmLogisticsFeeHistList(OmLogisticsFeeHistListRequestDto omLogisticsFeeHistListRequestDto) throws Exception;

	/**
	 * 물류 수수료 등록
	 *
	 * @param omLogisticsFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> addOmLogisticsFee(OmLogisticsFeeVo omLogisticsFeeVo) throws Exception;

	/**
	 * 물류 수수료 수정
	 *
	 * @param omLogisticsFeeVo
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	ApiResult<?> putOmLogisticsFee(OmLogisticsFeeVo omLogisticsFeeVo) throws Exception;

}
