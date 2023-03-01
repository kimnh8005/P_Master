package kr.co.pulmuone.v1.user.psuserbasic.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.psuserbasic.dto.PsUserBasicRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 기초설정 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200820    강윤경              최초작성
 * =======================================================================
 * </PRE>
 */

public interface UserPsUserBasicBiz {

	ApiResult<?> getPsUserBasic() throws Exception;

	ApiResult<?> putPsUserBasic(PsUserBasicRequestDto psUserBasicRequestDto) throws Exception;


}

