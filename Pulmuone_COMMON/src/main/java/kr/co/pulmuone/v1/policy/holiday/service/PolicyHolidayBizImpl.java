package kr.co.pulmuone.v1.policy.holiday.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupListRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupListResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayGroupResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveholidayGroupDateListRequestDto;

/**
 * <PRE>
 * 휴무일 관리 위한 impl
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
@Service
public class PolicyHolidayBizImpl implements PolicyHolidayBiz {

	@Autowired
	PolicyHolidayService policyHolidayService;


	/**
	 * 휴무일 관리 리스트 조회
	 * @param
	 * @return GetHolidayListResponseDto
	 * @throws Exception
	 */
	public ApiResult<?> getHolidayList() {
		return ApiResult.success(policyHolidayService.getHolidayList());
	}

	/**
	 * 휴무일 관리 휴무일 저장
	 * @param SaveHolidayRequestDto
	 * @return SaveHolidayResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> saveHoliday(SaveHolidayRequestDto dto){
		policyHolidayService.saveHoliday(dto);
		return null;
	}

	/**
	 * 휴일그룹 관리 리스트 조회
	 * @param GetHolidayGroupListRequestDto
	 * @return GetHolidayGroupListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getHolidayGroupList(GetHolidayGroupListRequestDto dto){

		GetHolidayGroupListResponseDto result 	= new GetHolidayGroupListResponseDto();
		result = policyHolidayService.getHolidayGroupList(dto);
		return ApiResult.success(result);
	}

	/**
	 * 휴일그룹 관리 상세 조회
	 * @param GetHolidayGroupRequestDto
	 * @return GetHolidayGroupResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getHolidayGroup(GetHolidayGroupRequestDto dto){
		GetHolidayGroupResponseDto result 	= new GetHolidayGroupResponseDto();

		result 	= policyHolidayService.getHolidayGroup(dto);
		return ApiResult.success(result);
	}


	/**
	 * 휴일그룹 관리 휴일그룹 등록
	 * @param SaveHolidayGroupRequestDto
	 * @return SaveHolidayGroupResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?>  addHolidayGroup(SaveHolidayGroupRequestDto dto) throws BaseException{

		SaveHolidayGroupResponseDto result 	= new SaveHolidayGroupResponseDto();

		try {
			//binding data
			dto.setInsertRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getHolidayDateList(), SaveholidayGroupDateListRequestDto.class));
		} catch (Exception e) {
			  return ApiResult.result(BaseEnums.Default.EXCEPTION_ISSUED);
		}
		result = policyHolidayService.addHolidayGroup(dto);

		return ApiResult.success(result);
	}


	/**
	 * 휴일그룹 관리 휴일그룹 수정
	 * @param SaveHolidayGroupRequestDto
	 * @return SaveHolidayGroupResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putHolidayGroup(SaveHolidayGroupRequestDto dto) {
		SaveHolidayGroupResponseDto result 	= new SaveHolidayGroupResponseDto();

		try {
			//binding data
			dto.setInsertRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getHolidayDateList(), SaveholidayGroupDateListRequestDto.class));
			dto.setDeleteRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getDeletedDateList(), SaveholidayGroupDateListRequestDto.class));

			result = policyHolidayService.putHolidayGroup(dto);
		} catch (Exception e) {
			return ApiResult.result(BaseEnums.Default.EXCEPTION_ISSUED);
		}

		return ApiResult.success(result);
	}

	/**
	 * 전체 휴무일 리스트 조회
	 * @return GetHolidayListResponseDto
	 * @throws Exception
	 */
	public ApiResult<?> getAllHolidayList() {
		return ApiResult.success(policyHolidayService.getAllHolidayList());
	}

}

