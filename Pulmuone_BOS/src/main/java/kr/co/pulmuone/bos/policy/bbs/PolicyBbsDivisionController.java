package kr.co.pulmuone.bos.policy.bbs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsDivisionBiz;
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
 *  1.0		20200916		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyBbsDivisionController {


	@Autowired
	private PolicyBbsDivisionBiz policyBbsDivisionBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 게시판분류 설정 조회
	 * @param psShippingCompId
	 * @return ApiResult
	 * @throws Exception
	 */
    @GetMapping(value = "/admin/policy/bbs/getPolicyBbsDivisionInfo")
	@ApiOperation(value = "게시판분류 설정 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "csCategoryId", value = "게시판분류 설정 PK", required = true, dataType = "Long")})
	public ApiResult<?> getPolicyBbsDivisionInfo(@RequestParam(value = "csCategoryId", required = true) Long csCategoryId) throws Exception{
		return policyBbsDivisionBiz.getPolicyBbsDivisionInfo(csCategoryId);
	}

	/**
	 * 게시판분류 설정 목록 조회
	 * @param PolicyBbsDivisionDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/bbs/getPolicyBbsDivisionList")
	@ApiOperation(value = "게시판분류 설정 목록 조회", httpMethod = "POST")
	public ApiResult<?> getPolicyBbsDivisionList(PolicyBbsDivisionDto dto) throws Exception{
		return policyBbsDivisionBiz.getPolicyBbsDivisionList((PolicyBbsDivisionDto)BindUtil.convertRequestToObject(request, PolicyBbsDivisionDto.class));
	}
	/**
	 * 게시판분류 설정 상위 분류 코드 조회
	 * @param bbsTp
	 * @return ApiResult
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/policy/bbs/getPolicyBbsDivisionParentCategoryList")
	@ApiOperation(value = "게시판분류 설정 상위 분류 코드 조회", httpMethod = "GET")
	public ApiResult<?> getPolicyBbsDivisionParentCategoryList(@RequestParam(value = "bbsTp", required = true) String bbsTp) throws Exception{
		if (StringUtil.isEmpty(bbsTp)) return ApiResult.success();
		return policyBbsDivisionBiz.getPolicyBbsDivisionParentCategoryList(bbsTp);
	}

	/**
	 * 게시판분류 설정 신규 등록
	 * @param PolicyBbsDivisionDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/bbs/addPolicyBbsDivision")
	@ApiOperation(value = "게시판분류 설정 신규 등록", httpMethod = "POST")
	public ApiResult<?> addPolicyBbsDivision(PolicyBbsDivisionDto dto)throws Exception{
		return policyBbsDivisionBiz.addPolicyBbsDivision(dto);
	}

	/**
	 * 게시판분류 설정 수정
	 * @param PolicyBbsDivisionDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/bbs/putPolicyBbsDivision")
	@ApiOperation(value = "게시판분류 설정 수정", httpMethod = "POST")
	public ApiResult<?> putPolicyBbsDivision(PolicyBbsDivisionDto dto)throws Exception{
		return policyBbsDivisionBiz.putPolicyBbsDivision(dto);
	}
	/**
	 * 게시판분류 설정 삭제
	 * @param PolicyBbsDivisionDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/bbs/delPolicyBbsDivision")
	@ApiOperation(value = "게시판분류 설정 삭제", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "csCategoryId", value = "게시판분류 설정 PK", required = true, dataType = "Long")})
	public ApiResult<?> delPolicyBbsDivision(@RequestParam(value = "csCategoryId", required = true) Long csCategoryId)throws Exception{
		return policyBbsDivisionBiz.delPolicyBbsDivision(csCategoryId);
	}
}

