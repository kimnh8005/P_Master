package kr.co.pulmuone.v1.policy.holiday.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupListRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayRequestDto;

/**
 * <PRE>
 * 휴무일 관리 위한 service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200706    		이한미르          최초작성
 * =======================================================================
 * </PRE>
 */

public interface PolicyHolidayBiz {

	ApiResult<?> getHolidayList();
	ApiResult<?> saveHoliday(SaveHolidayRequestDto dto);
	ApiResult<?> getHolidayGroupList(GetHolidayGroupListRequestDto dto);
	ApiResult<?> getHolidayGroup(GetHolidayGroupRequestDto dto);
	ApiResult<?> addHolidayGroup(SaveHolidayGroupRequestDto dto) throws BaseException;
	ApiResult<?> putHolidayGroup(SaveHolidayGroupRequestDto dto);
	ApiResult<?> getAllHolidayList();

//	GetHolidayListResponseDto getHolidayList() throws Exception;
//
//	SaveHolidayResponseDto saveHoliday(SaveHolidayRequestDto dto) throws Exception;
//
//	GetHolidayGroupListResponseDto getHolidayGroupList(GetHolidayGroupListRequestDto dto) throws Exception;
//
//	GetHolidayGroupResponseDto getHolidayGroup(GetHolidayGroupRequestDto dto) throws Exception;
//
//	SaveHolidayGroupResponseDto addHolidayGroup(SaveHolidayGroupRequestDto dto) throws Exception;
//
//	SaveHolidayGroupResponseDto putHolidayGroup(SaveHolidayGroupRequestDto dto) throws Exception;
}

