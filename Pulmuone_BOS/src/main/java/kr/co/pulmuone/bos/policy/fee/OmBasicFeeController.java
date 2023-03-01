package kr.co.pulmuone.bos.policy.fee;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;
import kr.co.pulmuone.v1.policy.fee.service.OmBasicFeeBiz;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
* <PRE>
* Forbiz Korea
* 기본 수수료 관리 Controller
* </PRE>
*
*/

@RestController
public class OmBasicFeeController {

	@Autowired
	private OmBasicFeeBiz omBasicFeeBiz;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 기본 수수료 목록 조회
	 *
	 * @param omBasicFeeListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ps/fee/getOmBasicFeeList")
	@ApiOperation(value = "기본 수수료 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OmBasicFeeListResponseDto.class)
	})
	public ApiResult<?> getOmBasicFeeList(OmBasicFeeListRequestDto omBasicFeeListRequestDto) throws Exception {
        return omBasicFeeBiz.getOmBasicFeeList(BindUtil.bindDto(request, OmBasicFeeListRequestDto.class));
	}


	/**
	 * 기본 수수료 목록 조회(리스트박스 조회용)
	 *
	 * @param omBasicFeeListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/ps/fee/searchOmBasicFeeList")
	@ApiOperation(value = "기본 수수료 목록 조회(리스트박스 조회용)")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OmBasicFeeListResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> searchOmBasicFeeList(OmBasicFeeListRequestDto omBasicFeeListRequestDto) throws Exception {
        return omBasicFeeBiz.getOmBasicFeeList(omBasicFeeListRequestDto);
	}


    /**
     * 기본 수수료 상세 조회
     *
     * @param omBasicFeeRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@PostMapping(value = "/admin/ps/fee/getOmBasicFee")
	@ApiOperation(value = "기본 수수료 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetBrandResponseDto.class)
	})
	public ApiResult<?> getOmBasicFee(OmBasicFeeRequestDto omBasicFeeRequestDto) throws Exception {
        return omBasicFeeBiz.getOmBasicFee(omBasicFeeRequestDto);
	}

	/**
	 * 기본 수수료 이력 목록 조회
	 *
	 * @param omBasicFeeHistListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ps/fee/getOmBasicHistFeeList")
	@ApiOperation(value = "기본 수수료 이력 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OmBasicFeeHistListResponseDto.class)
	})
	public ApiResult<?> getOmBasicHistFeeList(OmBasicFeeHistListRequestDto omBasicFeeHistListRequestDto) throws Exception {
		return omBasicFeeBiz.getOmBasicHistFeeList(BindUtil.bindDto(request, OmBasicFeeHistListRequestDto.class));
	}

	/**
     * 기본 수수료 등록
     *
     * @param omBasicFeeVo
     * @return ApiResult<?>
     * @throws Exception
     */
	@PostMapping(value = "/admin/ps/fee/addOmBasicFee")
	@ApiOperation(value = "기본 수수료 등록", httpMethod = "POST")
    public ApiResult<?> addOmBasicFee(@RequestBody OmBasicFeeVo omBasicFeeVo) throws Exception {
        return omBasicFeeBiz.addOmBasicFee(omBasicFeeVo);
    }

    /**
     * 기본 수수료 수정
     *
     * @param omBasicFeeVo
     * @return ApiResult<?>
     * @throws Exception
     */
    @PostMapping(value = "/admin/ps/fee/putOmBasicFee")
    @ApiOperation(value = "기본 수수료 수정", httpMethod = "POST")
    public ApiResult<?> putOmBasicFee(@RequestBody OmBasicFeeVo omBasicFeeVo) throws Exception {
        return omBasicFeeBiz.putOmBasicFee(omBasicFeeVo);
    }

}