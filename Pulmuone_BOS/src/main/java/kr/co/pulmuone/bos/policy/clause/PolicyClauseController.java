package kr.co.pulmuone.bos.policy.clause;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
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
import kr.co.pulmuone.v1.policy.clause.dto.PolicySaveClauseGroupRequestSaveDto;
import kr.co.pulmuone.v1.policy.clause.service.PolicyClauseBiz;
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
 *  1.0		20201104		석세동              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyClauseController {

	@Autowired
	private PolicyClauseBiz policyClauseBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
     * 약관그룸관리 목록
     *
     * @param
     * @return PolicyGetClauseGroupListRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/getClauseGroupList")
	@ApiOperation(value = "약관그룸관리 목록", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyGetClauseGroupListRequestDto.class)
	})
	public ApiResult<?> getClauseGroupList(PolicyGetClauseGroupListRequestDto dto) throws Exception{

		return policyClauseBiz.getClauseGroupList((PolicyGetClauseGroupListRequestDto) BindUtil.convertRequestToObject(request, PolicyGetClauseGroupListRequestDto.class));
	}

	/**
     * 약관그룸관리 저장
     *
     * @param
     * @return PolicySaveClauseGroupRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/saveClauseGroup")
	@ApiOperation(value = "약관그룸관리 저장", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicySaveClauseGroupRequestDto.class)
	})
	public ApiResult<?> saveClauseGroup(PolicySaveClauseGroupRequestDto dto) throws Exception {

		//binding data
		dto.setInsertRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getInsertData(), PolicySaveClauseGroupRequestSaveDto.class));
		dto.setUpdateRequestDtoList(BindUtil.convertJsonArrayToDtoList(dto.getUpdateData(), PolicySaveClauseGroupRequestSaveDto.class));

		return policyClauseBiz.saveClauseGroup(dto);
	}

	/**
     * 약관관리 약관그룹 목록
     *
     * @param
     * @return PolicyGetClauseGroupNameListRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/getClauseGroupNameList")
	@ApiOperation(value = "약관관리 약관그룹 목록", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyGetClauseGroupNameListRequestDto.class)
	})
	public ApiResult<?> getClauseGroupNameList(PolicyGetClauseGroupNameListRequestDto dto) throws Exception {

		return policyClauseBiz.getClauseGroupNameList((PolicyGetClauseGroupNameListRequestDto) BindUtil.convertRequestToObject(request, PolicyGetClauseGroupNameListRequestDto.class));
	}

	/**
     * 약관관리 약관관리 목록
     *
     * @param
     * @return PolicyGetClauseListRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/getClauseList")
	@ApiOperation(value = "약관관리 약관관리 목록", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyGetClauseListRequestDto.class)
	})
	public ApiResult<?> getClauseList(PolicyGetClauseListRequestDto dto) throws Exception {

		return policyClauseBiz.getClauseList(dto);
	}

	/*
	 * @PostMapping(value = "/admin/ps/clause/getView") public ApiResult<?>
	 * getClauseView(PolicyGetClauseViewRequestDto dto) throws Exception {
	 *
	 * return policyClauseBiz.getClauseView(dto); }
	 */

	/*
	 * @PostMapping(value = "/admin/ps/clause/getClause") public ApiResult<?>
	 * getClause(PolicyGetClauseRequestDto dto) throws Exception {
	 *
	 * return policyClauseBiz.getClause(dto); }
	 */

	/**
     * 약관관리 약관관리 저장
     *
     * @param
     * @return PolicyAddClauseRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/addClause")
	@ApiOperation(value = "약관관리 약관관리 저장", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyAddClauseRequestDto.class)
	})
	public ApiResult<?> addClause(PolicyAddClauseRequestDto dto) throws Exception {

		return policyClauseBiz.addClause(dto);
	}

	/**
     * 약관관리 약관관리 보기
     *
     * @param
     * @return PolicyGetClauseModifyViewRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/getClauseModifyView")
	@ApiOperation(value = "약관관리 약관관리 보기", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyGetClauseModifyViewRequestDto.class)
	})
	public ApiResult<?> getClauseModifyView(PolicyGetClauseModifyViewRequestDto dto) throws Exception {

		return policyClauseBiz.getClauseModifyView(dto);
	}

	/**
     * 약관관리 약관관리 수정
     *
     * @param
     * @return PolicyPutClauseRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/putClause")
	@ApiOperation(value = "약관관리 약관관리 수정", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPutClauseRequestDto.class)
	})
	public ApiResult<?> putClause(PolicyPutClauseRequestDto dto) throws Exception {

		return policyClauseBiz.putClause(dto);
	}

	/**
     * 약관관리 약관관리 삭제
     *
     * @param
     * @return PolicyDelClauseRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/clause/delClause")
	@ApiOperation(value = "약관관리 약관관리 삭제", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyDelClauseRequestDto.class)
	})
	public ApiResult<?> delClause(PolicyDelClauseRequestDto dto) throws Exception {

		return policyClauseBiz.delClause(dto);
	}


}
