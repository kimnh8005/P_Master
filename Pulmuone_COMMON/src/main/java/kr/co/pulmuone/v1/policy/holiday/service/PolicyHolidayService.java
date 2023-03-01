package kr.co.pulmuone.v1.policy.holiday.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.policy.holiday.PolicyHolidayMapper;
import kr.co.pulmuone.v1.comm.mapper.policy.payment.PolicyPaymentGatewayMapper;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupListRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupListResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayGroupResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.GetHolidayListResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayGroupRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayGroupResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveHolidayRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.SaveholidayGroupDateListRequestDto;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayGroupListResultVo;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayGroupResultVo;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayListResultVo;

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
 *  1.0		20201019		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyHolidayService {



	@Autowired
	private final PolicyHolidayMapper policyHolidayMapper;

	/**
     * 휴무일 관리 휴무일 목록
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
    protected GetHolidayListResponseDto getHolidayList() {
    	GetHolidayListResponseDto result 	= new GetHolidayListResponseDto();
    	PageMethod.startPage(1, 100);
		List<GetHolidayListResultVo> rows 	= policyHolidayMapper.getHolidayList();

		result.setRows(rows);
		return result;
    }

    /**
	 * 휴무일 관리 휴무일 저장
	 * @param
	 * @return
	 * @throws Exception
	 */
    protected int saveHoliday(SaveHolidayRequestDto dto) {

    	policyHolidayMapper.deleteHoliday();
    	return policyHolidayMapper.addHoliday(dto);
    }

    /**
	 * 휴일그룹 관리 리스트 조회
	 * @param GetHolidayGroupListRequestDto
	 * @return GetHolidayGroupListResponseDto
	 * @throws Exception
	 */
    protected GetHolidayGroupListResponseDto getHolidayGroupList(GetHolidayGroupListRequestDto dto) {

    	GetHolidayGroupListResponseDto result 	= new GetHolidayGroupListResponseDto();

    	List<GetHolidayGroupListResultVo> rows = policyHolidayMapper.getHolidayGroupList(dto);
		int total = policyHolidayMapper.getHolidayGroupListCount(dto);

		result.setRows(rows);
		result.setTotal(total);

		return result;
    }

    /**
	 * 휴일그룹 관리 상세 조회
	 * @param GetHolidayGroupRequestDto
	 * @return GetHolidayGroupResponseDto
	 * @throws Exception
	 */
    protected GetHolidayGroupResponseDto getHolidayGroup(GetHolidayGroupRequestDto dto) {

		GetHolidayGroupResponseDto result 	= new GetHolidayGroupResponseDto();
		List<GetHolidayGroupResultVo> rows 	= policyHolidayMapper.getHolidayGroup(dto);
		result.setRows(rows);

		return result;
    }


    /**
	 * 휴일그룹 관리 휴일그룹 등록
	 * @param SaveHolidayGroupRequestDto
	 * @return SaveHolidayGroupResponseDto
	 * @throws Exception
	 */
    protected SaveHolidayGroupResponseDto addHolidayGroup(SaveHolidayGroupRequestDto dto) {

    	SaveHolidayGroupResponseDto result 	= new SaveHolidayGroupResponseDto();

		if(policyHolidayMapper.validateHolidayGroupName(dto) > 0) {
			result.setRETURN_CODE(BaseEnums.CommBase.DUPLICATE_DATA.getCode());
			result.setRETURN_MSG(BaseEnums.CommBase.DUPLICATE_DATA.getMessage());
		} else {
			policyHolidayMapper.addHolidayGroup(dto);
			if(dto.getInsertRequestDtoList().size() != 0) {
				policyHolidayMapper.addHolidayGroupAddDate(dto);
			}
		}

		return result;
    }

    /**
	 * 휴일그룹 관리 휴일그룹 수정
	 * @param SaveHolidayGroupRequestDto
	 * @return SaveHolidayGroupResponseDto
	 * @throws Exception
	 */
    protected SaveHolidayGroupResponseDto putHolidayGroup(SaveHolidayGroupRequestDto dto) {

    	SaveHolidayGroupResponseDto result 	= new SaveHolidayGroupResponseDto();

		if(policyHolidayMapper.validateHolidayGroupName(dto) > 0) {
			result.setRETURN_CODE(BaseEnums.CommBase.DUPLICATE_DATA.getCode());
			result.setRETURN_MSG(BaseEnums.CommBase.DUPLICATE_DATA.getMessage());
		} else {
			policyHolidayMapper.putHolidayGroup(dto);

			if(dto.getDeleteRequestDtoList().size() != 0) {
				policyHolidayMapper.delHolidayGroupAddDate(dto);
			}

			if(dto.getInsertRequestDtoList().size() != 0) {
				policyHolidayMapper.addHolidayGroupAddDate(dto);
			}
		}

    	return result;
    }

	/**
     * 전체 휴일 목록 조회
     *
     * @return GetHolidayListResponseDto
     */
    protected GetHolidayListResponseDto getAllHolidayList() {
    	GetHolidayListResponseDto result 	= new GetHolidayListResponseDto();
		List<GetHolidayListResultVo> rows 	= policyHolidayMapper.getHolidayList();
		result.setRows(rows);
		return result;
    }

}
