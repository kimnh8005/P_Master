package kr.co.pulmuone.bos.policy.holiday;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupListRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayListResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveholidayDateListRequestDto;
import kr.co.pulmuone.v1.policy.holiday.service.PolicyHolidayBiz;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentGatewayDto;
import lombok.RequiredArgsConstructor;

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
 *  1.0    20200706			이한미르		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class PolicyHolidayController {

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private PolicyHolidayBiz policyHolidayBiz;

	/**
     * 휴무일 관리 휴일 리스트 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
	@PostMapping(value = "/admin/policy/holiday/getHolidayList")
	@ApiOperation(value = "휴무일 관리 휴일 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPaymentGatewayDto.class)
	})
	public ApiResult<?> getHolidayList() throws Exception{
		return policyHolidayBiz.getHolidayList();
	}

	/**
	 * 휴무일 관리 휴일 저장
	 * @param SaveHolidayRequestDto
	 * @return SaveHolidayResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/holiday/addHoliday")
	@ApiOperation(value = "휴무일 관리 휴일 저장", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> addHoliday(SaveHolidayRequestDto dto) throws Exception{
		//binding data
        dto.setInsertHolidayRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getInsertHolidayData(), SaveholidayDateListRequestDto.class));

		return policyHolidayBiz.saveHoliday(dto);
	}

	/*********************** 휴일그룹관리 *************************/

	/**
	 * 휴일그룹관리 리스트 조회
	 * @param GetHolidayGroupListRequestDto
	 * @return GetHolidayGroupListResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/holidayGroup/getHolidayGroupList")
	@ApiOperation(value = "휴일그룹관리 리스트 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetHolidayGroupListRequestDto.class)
	})
	public ApiResult<?> getHolidayGroupList(GetHolidayGroupListRequestDto dto) throws Exception {

		return policyHolidayBiz.getHolidayGroupList((GetHolidayGroupListRequestDto) BindUtil.convertRequestToObject(request, GetHolidayGroupListRequestDto.class));

	}


	/**
	 * 휴일그룹관리 상세 조회
	 * @param GetHolidayGroupRequestDto
	 * @return GetHolidayGroupResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/holidayGroup/getHolidayGroup")
	@ApiOperation(value = "휴일그룹관리 상세 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetHolidayGroupRequestDto.class)
	})
	public ApiResult<?> getHolidayGroup(GetHolidayGroupRequestDto dto) throws Exception{
		return policyHolidayBiz.getHolidayGroup(dto);
	}


	/**
	 * 휴일그룹관리 휴일그룹 등록
	 * @param SaveHolidayGroupRequestDto
	 * @return SaveHolidayGroupResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/holidayGroup/addHolidayGroup")
	@ApiOperation(value = "휴일그룹관리 휴일그룹 등록", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SaveHolidayGroupRequestDto.class)
	})
	public ApiResult<?> getHolidayGroup(SaveHolidayGroupRequestDto dto) throws Exception {
		return policyHolidayBiz.addHolidayGroup(dto);
	}

	/**
	 * 휴일그룹관리 휴일그룹 수정
	 * @param SaveHolidayGroupRequestDto
	 * @return SaveHolidayGroupResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/holidayGroup/putHolidayGroup")
	@ApiOperation(value = "휴일그룹관리 휴일그룹 수정", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SaveHolidayGroupRequestDto.class)
	})
	public ApiResult<?> putHolidayGroup(SaveHolidayGroupRequestDto dto) throws Exception{
		return policyHolidayBiz.putHolidayGroup(dto);
	}

	/**
     * 전체 휴일 목록 조회
     *
     * @return GetHolidayListResponseDto
     */
	@PostMapping(value = "/admin/policy/holiday/getAllHolidayList")
	@ApiOperation(value = "전체 휴일 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetHolidayListResponseDto.class)
	})
	public ApiResult<?> getAllHolidayList() throws Exception{
		return policyHolidayBiz.getAllHolidayList();
	}

}

