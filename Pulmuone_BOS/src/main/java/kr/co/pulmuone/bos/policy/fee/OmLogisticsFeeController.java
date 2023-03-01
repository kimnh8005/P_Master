package kr.co.pulmuone.bos.policy.fee;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeVo;
import kr.co.pulmuone.v1.policy.fee.service.OmLogisticsFeeBiz;
import kr.co.pulmuone.v1.user.brand.dto.GetBrandResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
* <PRE>
* Forbiz Korea
* 물류 수수료 관리 Controller
* </PRE>
*
*/

@RestController
public class OmLogisticsFeeController {

	@Autowired
	private OmLogisticsFeeBiz omLogisticsFeeBiz;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 물류 수수료 목록 조회
	 *
	 * @param omLogisticsFeeListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ps/fee/getOmLogisticsFeeList")
	@ApiOperation(value = "물류 수수료 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OmLogisticsFeeListResponseDto.class)
	})
	public ApiResult<?> getOmLogisticsFeeList(OmLogisticsFeeListRequestDto omLogisticsFeeListRequestDto) throws Exception {
        return omLogisticsFeeBiz.getOmLogisticsFeeList(BindUtil.bindDto(request, OmLogisticsFeeListRequestDto.class));
	}

    /**
     * 물류 수수료 상세 조회
     *
     * @param omLogisticsFeeRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@PostMapping(value = "/admin/ps/fee/getOmLogisticsFee")
	@ApiOperation(value = "물류 수수료 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetBrandResponseDto.class)
	})
	public ApiResult<?> getOmLogisticsFee(OmLogisticsFeeRequestDto omLogisticsFeeRequestDto) throws Exception {
        return omLogisticsFeeBiz.getOmLogisticsFee(omLogisticsFeeRequestDto);
	}

	/**
	 * 물류 수수료 이력 목록 조회
	 *
	 * @param omLogisticsFeeHistListRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ps/fee/getOmLogisticsFeeHistList")
	@ApiOperation(value = "물류 수수료 이력 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OmLogisticsFeeHistListResponseDto.class)
	})
	public ApiResult<?> getOmLogisticsFeeHistList(OmLogisticsFeeHistListRequestDto omLogisticsFeeHistListRequestDto) throws Exception {
		return omLogisticsFeeBiz.getOmLogisticsFeeHistList(BindUtil.bindDto(request, OmLogisticsFeeHistListRequestDto.class));
	}

	/**
     * 물류 수수료 등록
     *
     * @param omLogisticsFeeVo
     * @return ApiResult<?>
     * @throws Exception
     */
	@PostMapping(value = "/admin/ps/fee/addOmLogisticsFee")
	@ApiOperation(value = "물류 수수료 등록", httpMethod = "POST")
    public ApiResult<?> addOmLogisticsFee(@RequestBody OmLogisticsFeeVo omLogisticsFeeVo) throws Exception {
        return omLogisticsFeeBiz.addOmLogisticsFee(omLogisticsFeeVo);
    }

    /**
     * 물류 수수료 수정
     *
     * @param omLogisticsFeeVo
     * @return ApiResult<?>
     * @throws Exception
     */
    @PostMapping(value = "/admin/ps/fee/putOmLogisticsFee")
    @ApiOperation(value = "물류 수수료 수정", httpMethod = "POST")
    public ApiResult<?> putOmLogisticsFee(@RequestBody OmLogisticsFeeVo omLogisticsFeeVo) throws Exception {
        return omLogisticsFeeBiz.putOmLogisticsFee(omLogisticsFeeVo);
    }

}