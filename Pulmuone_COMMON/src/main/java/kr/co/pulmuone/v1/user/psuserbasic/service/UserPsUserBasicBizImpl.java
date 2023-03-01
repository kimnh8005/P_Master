package kr.co.pulmuone.v1.user.psuserbasic.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import kr.co.pulmuone.v1.user.psuserbasic.dto.PsUserBasicRequestDto;
import kr.co.pulmuone.v1.user.psuserbasic.dto.PsUserBasicResponseDto;

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

@Service
public class UserPsUserBasicBizImpl implements UserPsUserBasicBiz {


	@Autowired
	private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

	/**
	 * 기초설정  조회
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getPsUserBasic() throws Exception {
		PsUserBasicResponseDto psUserBasicResponseDto = new PsUserBasicResponseDto();

		//결과
		List<GetEnvironmentListResultVo> result =  new ArrayList<GetEnvironmentListResultVo>();

		GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
		getEnvironmentListRequestDto.setSearchEnvironmentKey("UR_PW_CYCLE_DAY");
		result.add((systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto)));

		//비밀번호 실패 제한 횟수
		getEnvironmentListRequestDto.setSearchEnvironmentKey("UR_LOGIN_FAIL_COUNT"); //BOS 동시 로그인 가능여부
		result.add((systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto)));

		psUserBasicResponseDto.setGetEnvironmentListResultVo(result);

		return ApiResult.success(psUserBasicResponseDto);
	}


	/**
	 * 기초설정  수정
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putPsUserBasic(PsUserBasicRequestDto psUserBasicRequestDto) throws Exception {
		PsUserBasicResponseDto psUserBasicResponseDto = new PsUserBasicResponseDto();

		SaveEnvironmentRequestSaveDto saveEnvironmentRequestSaveDto = new SaveEnvironmentRequestSaveDto();
		saveEnvironmentRequestSaveDto.setStEnvId(psUserBasicRequestDto.getUrLoginFailCountStEnvId());
		saveEnvironmentRequestSaveDto.setEnvironmentValue(psUserBasicRequestDto.getUrLoginFailCount());
		systemBasicEnvironmentBiz.putEnvironmentEnvVal(saveEnvironmentRequestSaveDto);

		saveEnvironmentRequestSaveDto.setStEnvId(psUserBasicRequestDto.getUrPwCycleDayStEnvId());
		saveEnvironmentRequestSaveDto.setEnvironmentValue(psUserBasicRequestDto.getUrPwCycleDay());
		systemBasicEnvironmentBiz.putEnvironmentEnvVal(saveEnvironmentRequestSaveDto);

		return ApiResult.success(psUserBasicResponseDto);
	}



}

