package kr.co.pulmuone.bos.policy.shippingcomp;

import io.swagger.annotations.*;
import kr.co.pulmuone.bos.policy.shippingcomp.service.PolicyShippingCompBosService;
import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminResponseDefaultDto;
import kr.co.pulmuone.v1.api.ezadmin.service.EZAdminBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompDto;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전	:	작성일	:  작성자		:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		20200908	박승현		최초작성
 *  1.1		20200908	박승현		shipping -> shippingComp 로 변경(테이블명 PS_SHIPPING > PS_SHIPPING_COMP 변경)
 *  1.2		20200914	박승현		코드 신규 개발 가이드 적용
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyShippingCompController {


	@Autowired
	private PolicyShippingCompBosService policyShippingCompBosService;
	@Autowired
	private EZAdminBiz ezAdminBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 택배사 설정 조회
	 * @param psShippingCompId
	 * @return ApiResult
	 * @throws Exception
	 */
    @GetMapping(value = "/admin/policy/shippingcomp/getPolicyShippingCompInfo")
	@ApiOperation(value = "택배사 설정 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "psShippingCompId", value = "택배사 설정 PK", required = true, dataType = "Long")})
	public ApiResult<?> getPolicyShippingCompInfo(@RequestParam(value = "psShippingCompId", required = true) Long psShippingCompId) throws Exception{
		return policyShippingCompBosService.getPolicyShippingCompInfo(psShippingCompId);
	}

	/**
	 * 택배사 설정 목록 조회
	 * @param PolicyShippingCompRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/shippingcomp/getPolicyShippingCompList")
	@ApiOperation(value = "택배사 설정 목록 조회", httpMethod = "POST")
	public ApiResult<?> getPolicyShippingCompList(PolicyShippingCompRequestDto dto) throws Exception{
		return policyShippingCompBosService.getPolicyShippingCompList((PolicyShippingCompRequestDto)BindUtil.convertRequestToObject(request, PolicyShippingCompRequestDto.class));
	}

	/**
	 * 택배사 설정 신규 등록
	 * @param PolicyShippingCompRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/shippingcomp/addPolicyShippingComp")
	@ApiOperation(value = "택배사 설정 신규 등록", httpMethod = "POST")
	public ApiResult<?> addPolicyShippingComp(PolicyShippingCompRequestDto dto)throws Exception{
		return policyShippingCompBosService.addPolicyShippingComp(dto);
	}

	/**
	 * 택배사 설정 수정
	 * @param PolicyShippingCompRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/shippingcomp/putPolicyShippingComp")
	@ApiOperation(value = "택배사 설정 수정", httpMethod = "POST")
	public ApiResult<?> putPolicyShippingComp(PolicyShippingCompRequestDto dto)throws Exception{
		return policyShippingCompBosService.putPolicyShippingComp(dto);
	}
	/**
	 * 택배사 설정 삭제
	 * @param PolicyShippingCompRequestDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/shippingcomp/delPolicyShippingComp")
	@ApiOperation(value = "택배사 설정 삭제", httpMethod = "POST")
	public ApiResult<?> delPolicyShippingComp(PolicyShippingCompRequestDto dto)throws Exception{
		return policyShippingCompBosService.delPolicyShippingComp(dto);
	}

	/**
     * 택배사 외부몰 코드 목록 조회 EZ Admin
     * @param
     * @return EZAdminResponseDefaultDto
     *
     */
	@PostMapping(value = "/admin/policy/shippingcomp/getEZAdminEtcTransInfo")
	@ApiOperation(value = "택배사 외부몰 코드 목록 조회 EZ Admin", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 0000, message = "response data", response = EZAdminResponseDefaultDto.class)
	})
	public ApiResult<?> getEZAdminEtcTransinfo(){
		return ezAdminBiz.getEtcInfo("transinfo");
	}


	/**
	 * 택배사 설정 사용중인 전체 목록 조회
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/shippingcomp/getPolicyShippingCompUseAllList")
	@ApiOperation(value = "택배사 설정 사용중인 전체 목록 조회", httpMethod = "POST", notes = "택배사 설정 사용중인 전체 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyShippingCompDto.class)
	})
	public ApiResult<?> getPolicyShippingCompUseAllList() throws Exception{
		return policyShippingCompBosService.getPolicyShippingCompUseAllList();
	}

	/**
	 * 택배사 목록 조회
	 * @return ApiResult<?>
	 * @throws
	 */
	@RequestMapping(value = "/admin/policy/shippingcomp/getDropDownPolicyShippingCompList")
	@ApiOperation(value = "택배사 목록 조회", httpMethod = "POST", notes = "택배사 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyShippingCompDto.class)
	})
	public ApiResult<?> getDropDownPolicyShippingCompList() {
		return policyShippingCompBosService.getDropDownPolicyShippingCompList();
	}
}

