package kr.co.pulmuone.v1.policy.origin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.AddPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.AddPolicyOriginResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DelPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DelPolicyOriginResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DuplicatePolicyOriginCountParamDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginListResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginTypeListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginTypeListResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.PutPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.PutPolicyOriginResponseDto;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginListResultVo;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * 원산지관리 PolicyOriginBizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20201023    	  최윤지           최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class PolicyOriginBizImpl implements PolicyOriginBiz {

	@Autowired
	PolicyOriginService policyOriginService;

	/**
	 * 원산지 목록 조회
	 * @param	getPolicyOriginListRequestDto
	 * @return	ApiResult
	 */
	@Override
	public ApiResult<?> getOriginList(GetPolicyOriginListRequestDto getPolicyOriginListRequestDto) {
		GetPolicyOriginListResponseDto getPolicyOriginListResponseDto = new GetPolicyOriginListResponseDto();

		//int total = policyOriginService.getOriginListCount(getPolicyOriginListRequestDto);	// total
		Page<GetPolicyOriginListResultVo> getPolicyOriginListResultList = policyOriginService.getOriginList(getPolicyOriginListRequestDto);	// rows

		getPolicyOriginListResponseDto.setTotal(getPolicyOriginListResultList.getTotal());
		getPolicyOriginListResponseDto.setRows(getPolicyOriginListResultList.getResult());

		return ApiResult.success(getPolicyOriginListResponseDto);
	}


	/**
	 * 원산지 구분 목록 조회
	 * @param getPolicyOriginTypeListRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getOriginTypeList(GetPolicyOriginTypeListRequestDto getPolicyOriginTypeListRequestDto) {
		GetPolicyOriginTypeListResponseDto getPolicyOriginTypeListResponseDto = new GetPolicyOriginTypeListResponseDto();

		getPolicyOriginTypeListResponseDto.setRows(policyOriginService.getOriginTypeList(getPolicyOriginTypeListRequestDto));

		return ApiResult.success(getPolicyOriginTypeListResponseDto);
	}


	/**
	 * 원산지 등록
	 * @param addPolicyOriginRequestDto
	 * @return ApiResult
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addOrigin(AddPolicyOriginRequestDto addPolicyOriginRequestDto) throws Exception{
		AddPolicyOriginResponseDto addPolicyOriginResponseDto = new AddPolicyOriginResponseDto();
		// 중복체크
		DuplicatePolicyOriginCountParamDto duplicatePolicyOriginCountParamDto = new DuplicatePolicyOriginCountParamDto();

		duplicatePolicyOriginCountParamDto.setSystemCommonCodeId(addPolicyOriginRequestDto.getOriginType() + "." + addPolicyOriginRequestDto.getOriginCode());

        if(policyOriginService.duplicateOriginCount(duplicatePolicyOriginCountParamDto) > 0) {
            throw new BaseException(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {
            addPolicyOriginRequestDto.setSystemCommonCodeId(duplicatePolicyOriginCountParamDto.getSystemCommonCodeId());
            policyOriginService.addOrigin(addPolicyOriginRequestDto);
        }

		return ApiResult.success(addPolicyOriginResponseDto);
	}


	/**
	 * 원산지 수정
	 * @param putPolicyOriginRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> putOrigin(PutPolicyOriginRequestDto putPolicyOriginRequestDto){
        PutPolicyOriginResponseDto putPolicyOriginResponseDto = new PutPolicyOriginResponseDto();

        policyOriginService.putOrigin(putPolicyOriginRequestDto);

        return ApiResult.success(putPolicyOriginResponseDto);
	}


	/**
	 * 원산지 삭제
	 * @param delPolicyOriginRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> delOrigin(DelPolicyOriginRequestDto delPolicyOriginRequestDto){
		DelPolicyOriginResponseDto delPolicyOriginResponseDto = new DelPolicyOriginResponseDto();

		policyOriginService.delOrigin(delPolicyOriginRequestDto);

		return ApiResult.success(delPolicyOriginResponseDto);
	}


	/**
	 * 원산지 상세조회
	 * @param getPolicyOriginRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getOrigin(GetPolicyOriginRequestDto getPolicyOriginRequestDto) {
		GetPolicyOriginResponseDto getPolicyOriginResponseDto = new GetPolicyOriginResponseDto();
		GetPolicyOriginResultVo getPolicyOriginResultVo = policyOriginService.getOrigin(getPolicyOriginRequestDto);

		getPolicyOriginResponseDto.setRows(getPolicyOriginResultVo);

		return ApiResult.success(getPolicyOriginResponseDto);
	}

	/**
	 * 원산지 목록 조회 엑셀다운로드
	 * @param GetPolicyOriginListRequestDto
	 * @return ExcelDownloadDto
	 */
	@Override
    public ExcelDownloadDto getOriginListExportExcel(GetPolicyOriginListRequestDto dto) {
        return policyOriginService.getOriginListExportExcel(dto);
    }
}