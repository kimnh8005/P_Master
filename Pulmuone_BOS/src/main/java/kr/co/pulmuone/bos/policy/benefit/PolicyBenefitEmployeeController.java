package kr.co.pulmuone.bos.policy.benefit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeBrandGroupDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeBrandGroupSaveDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeGroupDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeGroupSaveDto;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeVo;
import kr.co.pulmuone.v1.policy.benefit.service.PolicyBenefitEmployeeBiz;
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
 *  1.0		20201030		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyBenefitEmployeeController {

	@Autowired
	private PolicyBenefitEmployeeBiz policyBenefitEmployeeBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
     * 임직원 혜택관리그룹 목록 조회
     *
     * @param
     * @return PolicyBenefitEmployeeGroupDto
     *
     */
	@PostMapping(value = "/admin/policy/benefit/getPolicyBenefitEmployeeGroupList")
	@ApiOperation(value = "임직원 혜택관리그룹 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyBenefitEmployeeGroupDto.class)
	})
	public ApiResult<?> getPolicyBenefitEmployeeGroupList(){
		return policyBenefitEmployeeBiz.getPolicyBenefitEmployeeGroupList();
	}
	/**
	 * 임직원 혜택관리그룹 목록  최근 업데이트 일자
	 *
	 * @param
	 * @return PolicyBenefitEmployeeVo
	 */
	@GetMapping(value = "/admin/policy/benefit/getLastModifyDatePolicyBenefitEmployeeGroup")
	@ApiOperation(value = "임직원 혜택관리그룹 목록  최근 업데이트 일자", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyBenefitEmployeeVo.class)
	})
	public ApiResult<?> getLastModifyDatePolicyBenefitEmployeeGroup(){
		return policyBenefitEmployeeBiz.getLastModifyDatePolicyBenefitEmployeeGroup();
	}
	/**
     * 임직원 혜택관리그룹 저장
     *
     * @param PolicyBenefitEmployeeGroupSaveDto
     * @return int
     */
	@PostMapping(value = "/admin/policy/benefit/putPolicyBenefitEmployeeGroup")
	@ApiOperation(value = "임직원 할인율 브랜드 그룹 저장", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putPolicyBenefitEmployeeGroup(PolicyBenefitEmployeeGroupSaveDto dto) throws Exception{
		dto.convertDataList();
		return policyBenefitEmployeeBiz.putPolicyBenefitEmployeeGroup(dto);
	}
	/**
     * 임직원 할인율 브랜드 그룹 목록 조회
     *
     * @param
     * @return PolicyBenefitEmployeeBrandGroupDto
     */
	@PostMapping(value = "/admin/policy/benefit/getPolicyBenefitEmployeeBrandGroupList")
	@ApiOperation(value = "임직원 할인율 브랜드 그룹 목록 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "searchType", value = "조회조건", dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyBenefitEmployeeBrandGroupDto.class)
	})
	public ApiResult<?> getPolicyBenefitEmployeeBrandGroupList(String searchType) {
		return policyBenefitEmployeeBiz.getPolicyBenefitEmployeeBrandGroupList(searchType);
	}
	/**
	 * 임직원 할인율 브랜드 그룹 목록 최근 업데이트 일자
	 *
	 * @param
	 * @return PolicyBenefitEmployeeBrandGroupVo
	 */
	@GetMapping(value = "/admin/policy/benefit/getLastModifyDatePolicyBenefitEmployeeBrandGroup")
	@ApiOperation(value = "임직원 할인율 브랜드 그룹 목록 최근 업데이트 일자", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyBenefitEmployeeBrandGroupVo.class)
	})
	public ApiResult<?> getLastModifyDatePolicyBenefitEmployeeBrandGroup(){
		return policyBenefitEmployeeBiz.getLastModifyDatePolicyBenefitEmployeeBrandGroup();
	}
	/**
	 * 임직원 혜택그룹에 등록된 할인율 브랜드 그룹
	 *
	 * @param
	 * @return PolicyBenefitEmployeeBrandGroupVo
	 */
	@PostMapping(value = "/admin/policy/benefit/getRegistDiscMasterPolicyBenefitEmployeeBrandGroup")
	@ApiOperation(value = "임직원 혜택그룹에 등록된 할인율 브랜드 그룹", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "psEmplDiscBrandGrpId", value = "임직원 할인율 브랜드 그룹 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyBenefitEmployeeBrandGroupVo.class)
	})
	public ApiResult<?> getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(@RequestParam(value = "psEmplDiscBrandGrpId", required = true) String psEmplDiscBrandGrpId){
		return policyBenefitEmployeeBiz.getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId);
	}
	/**
     * 임직원 할인율 브랜드 그룹 저장
     *
     * @param PolicyBenefitEmployeeBrandGroupSaveDto
     * @return ApiResult
	 * @throws Exception
     */
	@PostMapping(value = "/admin/policy/benefit/putPolicyBenefitEmployeeBrandGroup")
	@ApiOperation(value = "임직원 할인율 브랜드 그룹 저장", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putPolicyBenefitEmployeeBrandGroup(PolicyBenefitEmployeeBrandGroupSaveDto dto) throws Exception{
		dto.convertDataList();
		return policyBenefitEmployeeBiz.putPolicyBenefitEmployeeBrandGroup(dto);
	}

	/**
	 * 임직원 할인율 브랜드 그룹 삭제
	 * @param psEmplDiscBrandGrpId
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/benefit/delPolicyBenefitEmployeeBrandGroup")
	@ApiOperation(value = "임직원 할인율 브랜드 그룹 삭제", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "psEmplDiscBrandGrpId", value = "임직원 할인율 브랜드 그룹 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> delPolicyBenefitEmployeeBrandGroup(@RequestParam(value = "psEmplDiscBrandGrpId", required = true) String psEmplDiscBrandGrpId) throws Exception{
		return policyBenefitEmployeeBiz.delPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId);
	}

}

