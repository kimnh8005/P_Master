package kr.co.pulmuone.bos.policy.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.excel.dto.PolicyExcelTmpltDto;
import kr.co.pulmuone.v1.policy.excel.dto.vo.PolicyExcelTmpltVo;
import kr.co.pulmuone.v1.policy.excel.service.PolicyExcelTmpltBiz;
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
 *  1.0		20200924		박승현              최초작성
 *  1.1		20201126		박승현		startLine 시작행 SPEC OUT
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyExcelTmpltController {


	@Autowired
	private PolicyExcelTmpltBiz policyExcelTmpltBiz;

	/**
	 * 엑셀양식관리 설정 조회
	 * @param psExcelTemplateId
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/excel/getPolicyExcelTmpltInfo")
	@ApiOperation(value = "엑셀양식관리 설정 조회", httpMethod = "POST")
    @ApiImplicitParams({ @ApiImplicitParam(name = "psExcelTemplateId", value = "엑셀양식관리 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyExcelTmpltVo.class)
	})
	public ApiResult<?> getPolicyExcelTmpltInfo(@RequestParam(value = "psExcelTemplateId", required = true) String psExcelTemplateId) throws Exception{
		return policyExcelTmpltBiz.getPolicyExcelTmpltInfo(psExcelTemplateId);
	}
	/**
	 * 엑셀양식관리 양식목록 조회
	 * @param excelTemplateTp
	 * @param excelTemplateUseTp
	 * @return ApiResult
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/policy/excel/getPolicyExcelTmpltList")
	@ApiOperation(value = "엑셀양식관리 양식목록 조회", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyExcelTmpltDto.class)
	})
	public ApiResult<?> getPolicyExcelTmpltList(@RequestParam(value = "excelTemplateTp", required = true) String excelTemplateTp,
			@RequestParam(value = "excelTemplateUseTp", required = false) String excelTemplateUseTp,
			@RequestParam(value = "accountFormYn", required = false) String accountFormYn) throws Exception{
		if (StringUtil.isEmpty(excelTemplateTp)) return ApiResult.fail();
		PolicyExcelTmpltVo vo = new PolicyExcelTmpltVo();
		vo.setExcelTemplateTp(excelTemplateTp);
		vo.setExcelTemplateUseTp(excelTemplateUseTp);
		vo.setAccountFormYn(accountFormYn);
		return policyExcelTmpltBiz.getPolicyExcelTmpltList(vo);
	}
	/**
	 * 엑셀양식관리 설정 신규 등록
	 * @param PolicyExcelTmpltDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/excel/addPolicyExcelTmplt")
	@ApiOperation(value = "엑셀양식관리 설정 신규 등록", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> addPolicyExcelTmplt(PolicyExcelTmpltDto dto)throws Exception{
		return policyExcelTmpltBiz.addPolicyExcelTmplt(dto);
	}

	/**
	 * 엑셀양식관리 설정 수정
	 * @param PolicyExcelTmpltDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/excel/putPolicyExcelTmplt")
	@ApiOperation(value = "엑셀양식관리 설정 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putPolicyExcelTmplt(PolicyExcelTmpltDto dto)throws Exception{
		return policyExcelTmpltBiz.putPolicyExcelTmplt(dto);
	}
	/**
	 * 엑셀양식관리 설정 삭제
	 * @param psExcelTemplateId
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/excel/delPolicyExcelTmplt")
	@ApiOperation(value = "엑셀양식관리 설정 삭제", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "psExcelTemplateId", value = "엑셀양식관리 PK", required = true, dataType = "String")})
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> delPolicyExcelTmplt(@RequestParam(value = "psExcelTemplateId", required = true) String psExcelTemplateId) throws Exception{
		return policyExcelTmpltBiz.delPolicyExcelTmplt(psExcelTemplateId);
	}
}

