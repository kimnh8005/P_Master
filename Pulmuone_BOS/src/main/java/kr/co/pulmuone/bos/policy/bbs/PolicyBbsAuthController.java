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
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsAuthDto;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsAuthBiz;
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
 *  1.0		20200922		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyBbsAuthController {


	@Autowired
	private PolicyBbsAuthBiz policyBbsAuthBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 게시판권한 설정 조회
	 * @param psShippingCompId
	 * @return ApiResult
	 * @throws Exception
	 */
    @GetMapping(value = "/admin/policy/bbs/getPolicyBbsAuthInfo")
	@ApiOperation(value = "게시판권한 설정 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "csBbsConfigId", value = "게시판권한 설정 PK", required = true, dataType = "Long")})
	public ApiResult<?> getPolicyBbsAuthInfo(@RequestParam(value = "csBbsConfigId", required = true) Long csBbsConfigId) throws Exception{
		return policyBbsAuthBiz.getPolicyBbsAuthInfo(csBbsConfigId);
	}

	/**
	 * 게시판권한 설정 목록 조회
	 * @param PolicyBbsAuthDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/bbs/getPolicyBbsAuthList")
	@ApiOperation(value = "게시판권한 설정 목록 조회", httpMethod = "POST")
	public ApiResult<?> getPolicyBbsAuthList(PolicyBbsAuthDto dto) throws Exception{
		return policyBbsAuthBiz.getPolicyBbsAuthList((PolicyBbsAuthDto)BindUtil.convertRequestToObject(request, PolicyBbsAuthDto.class));
	}
	/**
	 * 게시판권한 설정 분류 코드 조회
	 * @param bbsTp
	 * @return ApiResult
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/policy/bbs/getPolicyBbsAuthCategoryList")
	@ApiOperation(value = "게시판권한 설정 분류 코드 조회", httpMethod = "GET")
	public ApiResult<?> getPolicyBbsAuthCategoryList(@RequestParam(value = "bbsTp", required = true) String bbsTp) throws Exception{
		if (StringUtil.isEmpty(bbsTp)) return ApiResult.success();
		return policyBbsAuthBiz.getPolicyBbsAuthCategoryList(bbsTp);
	}

	/**
	 * 게시판권한 설정 신규 등록
	 * @param PolicyBbsAuthDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/bbs/addPolicyBbsAuth")
	@ApiOperation(value = "게시판권한 설정 신규 등록", httpMethod = "POST")
	public ApiResult<?> addPolicyBbsAuth(PolicyBbsAuthDto dto)throws Exception{
		return policyBbsAuthBiz.addPolicyBbsAuth(dto);
	}

	/**
	 * 게시판권한 설정 수정
	 * @param PolicyBbsAuthDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/bbs/putPolicyBbsAuth")
	@ApiOperation(value = "게시판권한 설정 수정", httpMethod = "POST")
	public ApiResult<?> putPolicyBbsAuth(PolicyBbsAuthDto dto)throws Exception{
		return policyBbsAuthBiz.putPolicyBbsAuth(dto);
	}
	/**
	 * 게시판권한 설정 삭제
	 * @param PolicyBbsAuthDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/bbs/delPolicyBbsAuth")
	@ApiOperation(value = "게시판권한 설정 삭제", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "csBbsConfigId", value = "게시판권한 설정 PK", required = true, dataType = "Long")})
	public ApiResult<?> delPolicyBbsAuth(@RequestParam(value = "csBbsConfigId", required = true) Long csBbsConfigId)throws Exception{
		return policyBbsAuthBiz.delPolicyBbsAuth(csBbsConfigId);
	}
}

