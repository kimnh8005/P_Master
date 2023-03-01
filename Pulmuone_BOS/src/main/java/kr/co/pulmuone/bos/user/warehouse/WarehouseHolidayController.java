package kr.co.pulmuone.bos.user.warehouse;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.warehouse.dto.SaveWarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseHolidayResultVo;
import kr.co.pulmuone.v1.user.warehouse.service.WarehouseHolidayBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 출고처 휴일 설정
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20201109		       안치열            최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class WarehouseHolidayController {

	@Autowired
	private WarehouseHolidayBiz warehouseHolidayBiz;

	@Autowired
	private HttpServletRequest request;


	@PostMapping(value = "/admin/ur/warehouse/getWarehouseHolidayList")
	@ApiOperation(value = "출고처 휴일설정 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {

		return warehouseHolidayBiz.getWarehouseHolidayList((WarehouseHolidayRequestDto) BindUtil.convertRequestToObject(request, WarehouseHolidayRequestDto.class));
	}


	@PostMapping(value = "/admin/ur/warehouse/getWarehouseSetList")
	@ApiOperation(value = "출고처 설정 대상 목록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getWarehouseSetList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {

		return warehouseHolidayBiz.getWarehouseSetList((WarehouseHolidayRequestDto) BindUtil.convertRequestToObject(request, WarehouseHolidayRequestDto.class));
	}

	@PostMapping(value = "/admin/ur/warehouse/getWarehouseHolidayDetail")
	@ApiOperation(value = "출고처 설정 대상 수정 목록 ")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getWarehouseHolidayDetail(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
		return warehouseHolidayBiz.getWarehouseHolidayDetail((WarehouseHolidayRequestDto) BindUtil.convertRequestToObject(request, WarehouseHolidayRequestDto.class));
	}

	@PostMapping(value = "/admin/ur/warehouse/getConfirmWarehouseHolidayList")
	@ApiOperation(value = "출고처 설정  확정 목록 ")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getConfirmWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
		return warehouseHolidayBiz.getConfirmWarehouseHolidayList(warehouseHolidayRequestDto);
	}



	/**
     * 출고처 휴일설정 등록
     */
    @PostMapping(value = "/admin/ur/warehouse/addWarehouseHoliday")
    @ApiOperation(value = "출고처 휴일설정 등록")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공"),
            @ApiResponse(code = 9999, message = "" +
                    "[VALID_ERROR] 888888888 - 데이터가 유효하지 않습니다. \n"
            )
    })
    public ApiResult<?> addWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception {
    	saveWarehouseHolidayRequestDto.setInsertSaveDataList((List<WarehouseHolidayResultVo>) BindUtil.convertJsonArrayToDtoList(saveWarehouseHolidayRequestDto.getInsertData(), WarehouseHolidayResultVo.class));

        return ApiResult.success(warehouseHolidayBiz.addWarehouseHoliday(saveWarehouseHolidayRequestDto));
    }


	/**
     * 출고처 휴일설정 수정
     */
    @PostMapping(value = "/admin/ur/warehouse/putWarehouseHoliday")
    @ApiOperation(value = "출고처 휴일설정 등록")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공"),
            @ApiResponse(code = 9999, message = "" +
                    "[VALID_ERROR] 888888888 - 데이터가 유효하지 않습니다. \n"
            )
    })
    public ApiResult<?> putWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception {
    	saveWarehouseHolidayRequestDto.setInsertSaveDataList((List<WarehouseHolidayResultVo>) BindUtil.convertJsonArrayToDtoList(saveWarehouseHolidayRequestDto.getInsertData(), WarehouseHolidayResultVo.class));

        return ApiResult.success(warehouseHolidayBiz.putWarehouseHoliday(saveWarehouseHolidayRequestDto));
    }



	/**
	 * Scheduler 출고처 설정 목록
	 * @param getWarehouseListRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/getScheduleWarehouseHolidayList")
	@ApiOperation(value = "출고처 검색 팝업")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	public ApiResult<?> getScheduleWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
		return warehouseHolidayBiz.getScheduleWarehouseHolidayList((WarehouseHolidayRequestDto) BindUtil.convertRequestToObject(request, WarehouseHolidayRequestDto.class));
	}


	/**
	 * Scheduler 출고처 휴일정보 조회
	 * @param warehouseHolidayRequestDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/getHolidayWarehouseInfo")
	public ApiResult<?> getHolidayWarehouseInfo(@RequestBody WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
		return warehouseHolidayBiz.getHolidayWarehouseInfo(warehouseHolidayRequestDto);
	}


	@PostMapping(value = "/admin/ur/warehouse/getWarehouseHolidayListById")
	@ApiOperation(value = "출고처 휴일리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getWarehouseHolidayListById(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {

		return warehouseHolidayBiz.getWarehouseHolidayListById(warehouseHolidayRequestDto);
	}


}
