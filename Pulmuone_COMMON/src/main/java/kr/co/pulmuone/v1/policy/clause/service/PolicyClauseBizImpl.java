package kr.co.pulmuone.v1.policy.clause.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.clause.dto.GetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyAddClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyDelClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseGroupNameListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseListRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseModifyViewRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyGetClauseViewRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicyPutClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.PolicySaveClauseGroupRequestDto;


@Service
public class PolicyClauseBizImpl implements PolicyClauseBiz {

    @Autowired
    PolicyClauseService policyClauseService;

    /**
     * 회원가입 최신 약관 리스트
     *
     * @param
     * @return ResponseDto<GetLatestClauseResponseDto>
     * @throws Exception
     */
    public ApiResult<?> getLatestJoinClauseList() throws Exception {

        return policyClauseService.getLatestJoinClauseList();
    }

    /**
     * 최신 약관 내용 정보
     * @param
     * @return ResponseDto<GetLatestClauseResponseDto>
     * @throws Exception
     */
    @Override
    public ApiResult<?> getLatestClause(String psClauseGrpCd) throws Exception {

        return policyClauseService.getLatestClause(psClauseGrpCd);
    }

    /**
     * 약관 변경이력  리스트
     * @param
     * @return	GetClauseHistoryListResponseDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> getClauseHistoryList(String psClauseGrpCd) throws Exception {

        return policyClauseService.getClauseHistoryList(psClauseGrpCd);
    }

    /**
     * 버전별 약관 내용 정보
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public ApiResult<?> getClause(GetClauseRequestDto getClauseRequestDto) throws Exception {

        return policyClauseService.getClause(getClauseRequestDto);
    }


    /**
     * 구매 약관 리스트
     *
     * @param	GetClauseRequestDto
     * @return	GetLatestClauseResponseDto
     * @throws	Exception
     */
    public ApiResult<?> getPurchaseTermsClauseList(GetClauseRequestDto getClauseRequestDto) throws Exception {

        return policyClauseService.getPurchaseTermsClauseList(getClauseRequestDto);
    }

    public ApiResult<?> getClauseGroupList(PolicyGetClauseGroupListRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.getClauseGroupList(dto));
    }

    public ApiResult<?> saveClauseGroup(PolicySaveClauseGroupRequestDto dto) throws Exception {

        return ApiResult.result(policyClauseService.saveClauseGroup(dto));
    }

    public ApiResult<?> getClauseGroupNameList(PolicyGetClauseGroupNameListRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.getClauseGroupNameList(dto));
    }

    public ApiResult<?> getClauseList(PolicyGetClauseListRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.getClauseList(dto));
    }

    public ApiResult<?> getClauseView(PolicyGetClauseViewRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.getClauseView(dto));
    }

    public ApiResult<?> getClause(PolicyGetClauseRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.getClause(dto));
    }

    public ApiResult<?> addClause(PolicyAddClauseRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.addClause(dto));
    }

    public ApiResult<?> getClauseModifyView(PolicyGetClauseModifyViewRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.getClauseModifyView(dto));
    }

    public ApiResult<?> putClause(PolicyPutClauseRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.putClause(dto));
    }

    public ApiResult<?> delClause(PolicyDelClauseRequestDto dto) throws Exception {

        return ApiResult.success(policyClauseService.delClause(dto));
    }
}
