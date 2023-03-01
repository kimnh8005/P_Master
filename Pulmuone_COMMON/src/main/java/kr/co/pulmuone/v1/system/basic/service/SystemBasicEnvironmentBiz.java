package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;

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
 *  1.0    20200529    오영민              최초작성
 * =======================================================================
 * </PRE>
 */

@SuppressWarnings({"rawtypes"})
public interface SystemBasicEnvironmentBiz {

	/** 환경설정 */
	GetEnvironmentListResponseDto getEnvironmentList(GetEnvironmentListRequestDto dto) throws Exception;
	
	ApiResult<?> saveEnvironment(SaveEnvironmentRequestDto dto) throws Exception;

	GetEnvironmentListResultVo getEnvironment(GetEnvironmentListRequestDto dto) throws Exception;

	GetEnvironmentListResponseDto putEnvironmentEnvVal(SaveEnvironmentRequestSaveDto saveEnvironmentRequestSaveDto) throws Exception;

}

