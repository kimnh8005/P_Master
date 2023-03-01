package kr.co.pulmuone.v1.policy.clause.service;

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






public interface PolicyClauseBiz {

    /**
     * 회원가입 최신 약관 리스트
     *
     * @param
     * @return ResponseDto<GetLatestClauseResponseDto>
     * @throws Exception
     */
    ApiResult<?> getLatestJoinClauseList() throws Exception;

    /**
     * 최신 약관 내용 정보
     * @param
     * @return ResponseDto<GetLatestClauseResponseDto>
     * @throws Exception
     */
    ApiResult<?> getLatestClause(String psClauseGrpCd) throws Exception;

    /**
     * 약관 변경이력  리스트
     * @param
     * @return	GetClauseHistoryListResponseDto
     * @throws Exception
     */
    ApiResult<?> getClauseHistoryList(String psClauseGrpCd) throws Exception;

    /**
     * 버전별 약관 내용 정보
     * @param
     * @return
     * @throws Exception
     */
    ApiResult<?> getClause(GetClauseRequestDto getClauseRequestDto) throws Exception;

    /**
     * 구매 약관 리스트
     *
     * @param	GetClauseRequestDto
     * @return	GetLatestClauseResponseDto
     * @throws	Exception
     */
    ApiResult<?> getPurchaseTermsClauseList(GetClauseRequestDto getClauseRequestDto) throws Exception ;

    ApiResult<?> getClauseGroupList(PolicyGetClauseGroupListRequestDto dto) throws Exception  ;

    ApiResult<?> saveClauseGroup(PolicySaveClauseGroupRequestDto dto) throws Exception  ;

    ApiResult<?> getClauseGroupNameList(PolicyGetClauseGroupNameListRequestDto convertRequestToObject) throws Exception  ;

    ApiResult<?> getClauseList(PolicyGetClauseListRequestDto dto) throws Exception;

    ApiResult<?> getClauseView(PolicyGetClauseViewRequestDto dto) throws Exception;

    ApiResult<?> getClause(PolicyGetClauseRequestDto dto) throws Exception;


    ApiResult<?> addClause(PolicyAddClauseRequestDto dto) throws Exception;

    ApiResult<?> getClauseModifyView(PolicyGetClauseModifyViewRequestDto dto) throws Exception;

    ApiResult<?> putClause(PolicyPutClauseRequestDto dto) throws Exception;

    ApiResult<?> delClause(PolicyDelClauseRequestDto dto) throws Exception;
}
