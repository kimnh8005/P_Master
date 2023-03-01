package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.*;

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

public interface SystemBasicClassificationBiz {
	GetClassificationListResponseDto getClassificationList(GetClassificationListParamDto dto) throws Exception;
	GetClassificationResponseDto getClassification(Long stClassificationId) throws Exception;
	ApiResult<?> addClassification(SaveClassificationRequestDto dto) throws Exception;
	ApiResult<?> putClassification(SaveClassificationRequestDto dto) throws Exception;
	ApiResult<?> delClassification(Long id) throws Exception;
	GetTypeListResponseDto getTypeList() throws Exception;
}

