package kr.co.pulmuone.v1.policy.clause.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.policy.clause.PolicyClauseMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.clause.dto.GetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyAddClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyAddClauseResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyDelClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyDelClauseResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupListResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupNameListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupNameListResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseListResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseModifyViewRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseModifyViewResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseViewRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseViewResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyPutClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyPutClauseResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicySaveClauseGroupRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicySaveClauseGroupRequestSaveDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicySaveClauseGroupResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetClauseHistoryResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetClauseResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseGroupListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseGroupNameListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseModifyViewResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseViewResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyClauseService {

    @Autowired
    PolicyClauseMapper policyClauseMapper;

    /**
     * 회원가입 최신 약관 리스트
     *
     * @param
     * @return ResponseDto<GetLatestClauseResponseDto>
     * @throws Exception
     */
    protected ApiResult<?> getLatestJoinClauseList() throws Exception {

        List<GetLatestJoinClauseListResultVo> rows = policyClauseMapper.getLatestJoinClauseList();

        return ApiResult.ofNullable(rows);
    }

    /**
     * 최신 약관 내용 정보
     * @param
     * @return ResponseDto<GetLatestClauseResponseDto>
     * @throws Exception
     */
    protected ApiResult<?> getLatestClause(String psClauseGrpCd) throws Exception{

        GetClauseResultVo rows = policyClauseMapper.getLatestClause(psClauseGrpCd);

        return ApiResult.ofNullable(rows);
    }

    /**
     * 약관 변경이력  리스트
     * @param
     * @return	GetClauseHistoryListResponseDto
     * @throws Exception
     */
    protected ApiResult<?> getClauseHistoryList(String psClauseGrpCd) throws Exception{

        List<GetClauseHistoryResultVo> rows = policyClauseMapper.getClauseHistoryList(psClauseGrpCd);

        return ApiResult.ofNullable(rows);
    }



    /**
     * 버전별 약관 내용 정보
     * @param
     * @return
     * @throws Exception
     */
    protected ApiResult<?> getClause(GetClauseRequestDto getClauseRequestDto) throws Exception{

        GetClauseResultVo rows = policyClauseMapper.getClause(getClauseRequestDto);

        return ApiResult.ofNullable(rows);
    }


    /**
     * 구매 약관 리스트
     *
     * @param	GetClauseRequestDto
     * @return	GetLatestClauseResponseDto
     * @throws	Exception
     */
    protected ApiResult<?> getPurchaseTermsClauseList(GetClauseRequestDto getClauseRequestDto) throws Exception {

        List<GetLatestJoinClauseListResultVo> rows = policyClauseMapper.getPurchaseTermsClauseList(getClauseRequestDto);

        return ApiResult.ofNullable(rows);
    }

	protected PolicyGetClauseGroupListResponseDto getClauseGroupList(PolicyGetClauseGroupListRequestDto dto) throws Exception {
		PolicyGetClauseGroupListResponseDto result = new PolicyGetClauseGroupListResponseDto();

		int total = policyClauseMapper.getClauseGroupListCount(dto);	// total
		List<PolicyGetClauseGroupListResultVo> rows = policyClauseMapper.getClauseGroupList(dto);	// rows

		result.setTotal(total);
		result.setRows(rows);

		return result;
	}

	protected MessageCommEnum saveClauseGroup(PolicySaveClauseGroupRequestDto dto) throws Exception {
		List<PolicySaveClauseGroupRequestSaveDto> insertRequestDtoList = dto.getInsertRequestDtoList();
		List<PolicySaveClauseGroupRequestSaveDto> updateRequestDtoList = dto.getUpdateRequestDtoList();

		//데이터 저장
		if(!insertRequestDtoList.isEmpty()){
			for(PolicySaveClauseGroupRequestSaveDto vo : insertRequestDtoList) {
				if(StringUtil.isEmpty(vo.getPsClauseGroupCd()) || StringUtil.isEmpty(vo.getClauseGroupName()) || StringUtil.isEmpty(vo.getClauseTitle())) {
					return BaseEnums.CommBase.MANDATORY_MISSING;
				}

				if(policyClauseMapper.getDuplicateClauseGroupCount(vo) > 0 ) {
					return BaseEnums.CommBase.DUPLICATE_DATA;
				}
			}

			for(PolicySaveClauseGroupRequestSaveDto vo : insertRequestDtoList) {
				policyClauseMapper.addClauseGroup(vo);
			}

		}
		//데이터 수정
		if(!updateRequestDtoList.isEmpty()){
			for(PolicySaveClauseGroupRequestSaveDto vo : updateRequestDtoList) {
				if(StringUtil.isEmpty(vo.getPsClauseGroupCd()) || StringUtil.isEmpty(vo.getClauseGroupName()) || StringUtil.isEmpty(vo.getClauseTitle())) {
					return BaseEnums.CommBase.VALID_ERROR;
				}
			}


			for(PolicySaveClauseGroupRequestSaveDto vo : updateRequestDtoList) {
				policyClauseMapper.putClauseGroup(vo);
			}
		}

		return BaseEnums.Default.SUCCESS;
	}

	protected PolicyGetClauseGroupNameListResponseDto getClauseGroupNameList(PolicyGetClauseGroupNameListRequestDto dto) throws Exception {
		PolicyGetClauseGroupNameListResponseDto result = new PolicyGetClauseGroupNameListResponseDto();

		List<PolicyGetClauseGroupNameListResultVo> rows = policyClauseMapper.getClauseGroupNameList(dto);	// rows
		result.setRows(rows);

		return result;
	}

	protected PolicyGetClauseListResponseDto getClauseList(PolicyGetClauseListRequestDto dto) throws Exception {
		PolicyGetClauseListResponseDto result = new PolicyGetClauseListResponseDto();

		List<PolicyGetClauseListResultVo> rows = policyClauseMapper.getClauseList(dto);	// rows
		result.setRows(rows);

		return result;
	}

	protected PolicyGetClauseViewResponseDto getClauseView(PolicyGetClauseViewRequestDto dto) throws Exception {
		PolicyGetClauseViewResponseDto result = new PolicyGetClauseViewResponseDto();

		PolicyGetClauseViewResultVo vo = policyClauseMapper.getClauseView(dto);

		result.setRows(vo);

		return result;
	}

	protected PolicyGetClauseResponseDto getClause(PolicyGetClauseRequestDto dto) throws Exception {
		PolicyGetClauseResponseDto result = new PolicyGetClauseResponseDto();

		PolicyGetClauseResultVo vo = policyClauseMapper.getPolicyClause(dto);

		result.setRows(vo);

		return result;
	}

	protected PolicyAddClauseResponseDto addClause(PolicyAddClauseRequestDto dto) throws Exception{

		PolicyAddClauseResponseDto result = new PolicyAddClauseResponseDto();

        policyClauseMapper.addClause(dto);

		return result;
	}

	protected PolicyGetClauseModifyViewResponseDto getClauseModifyView(PolicyGetClauseModifyViewRequestDto dto) throws Exception {
		PolicyGetClauseModifyViewResponseDto result = new PolicyGetClauseModifyViewResponseDto();

		PolicyGetClauseModifyViewResultVo vo = policyClauseMapper.getClauseModifyView(dto);

		result.setRows(vo);

		return result;
	}

	protected PolicyPutClauseResponseDto putClause(PolicyPutClauseRequestDto dto) throws Exception{

		PolicyPutClauseResponseDto result = new PolicyPutClauseResponseDto();

		//1.PS_CLAUSE_HISTORY Table 에 데이터 Insert 처리
		policyClauseMapper.addClauseUpdateHistory(dto);

		//2.PS_CLAUSE TABLE DELETE_YN 구분 값 UPDATE
        policyClauseMapper.putUpdateClause(dto);

		return result;
	}

	protected PolicyDelClauseResponseDto delClause(PolicyDelClauseRequestDto dto) throws Exception{

		PolicyDelClauseResponseDto result = new PolicyDelClauseResponseDto();


//		1.PS_CLAUSE_HISTORY Table 에 데이터 Insert 처리
		policyClauseMapper.addClauseDeleteHistory(dto);

		//2.PS_CLAUSE TABLE DELETE_YN 구분 값 UPDATE
        //policyClauseMapper.putDeleteClause(dto);

		//2.PS_CLAUSE TABLE Table 데이터 Delete 처리
		policyClauseMapper.delClause(dto);


		return result;
	}
}
