package kr.co.pulmuone.bos.user.warehouse;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
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
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateResponseDto;
import kr.co.pulmuone.v1.user.warehouse.service.WarehouseBiz;
import org.springframework.web.servlet.ModelAndView;

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
 *  1.0    20200713		       안치열            최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class WarehouseController {

	@Autowired
	private WarehouseBiz warehouseBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
	private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 출고처 목록 조회
	 * @param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/getWarehouseList")
	@ApiOperation(value = "출고처 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getWarehouseList(WarehouseRequestDto warehouseRequestDto) throws Exception {

		return warehouseBiz.getWarehouseList((WarehouseRequestDto) BindUtil.convertRequestToObject(request, WarehouseRequestDto.class));
	}



	/**
	 * 출고처 상세조회
	 */
	@PostMapping(value = "/admin/ur/warehouse/getWarehouse")
	@ApiOperation(value = "출고처 상세조회")
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseModifyResponseDto.class)
	})
	public ApiResult<?> getWarehouse(WarehouseRequestDto warehouseRequestDto) throws Exception {
		return warehouseBiz.getWarehouse(warehouseRequestDto);
	}

	/**
	 * 출고처 정보 등록
	 * @param warehouseModifyDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/addWarehouse")
	@ApiOperation(value = "출고처 정보 등록", httpMethod = "POST")
	public ApiResult<?> addWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception {

		return warehouseBiz.addWarehouse(warehouseModifyDto);
	}


	/**
	 * 출고처 정보 수정
	 * @param warehouseModifyDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/warehouse/putWarehouse")
	@ApiOperation(value = "출고처 정보 수정", httpMethod = "POST")
	public ApiResult<?> putWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception {

		return warehouseBiz.putWarehouse(warehouseModifyDto);
	}

    /**
     * @Desc  배송정책  정보 조회
     * @param warehouseTemplateRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/ur/warehouse/getShippingTemplate")
    @ResponseBody
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = WarehouseTemplateResponseDto.class)
	})
    public ApiResult<?> getShippingTemplate(@RequestBody WarehouseTemplateRequestDto warehouseTemplateRequestDto)throws Exception{
        return warehouseBiz.getShippingTemplate(warehouseTemplateRequestDto);
    }

	/**
	 * 출고처 리스트 엑셀 다운로드 목록 조회
	 *
	 * @param warehouseRequestDto
	 * @return ModelAndView
	 */
	@ApiOperation(value = "출고처 리스트 엑셀 다운로드 목록 조회")
	@PostMapping(value = "/admin/ur/warehouse/getWarehouseExcelDownload")
	public ModelAndView getWarehouseExcelDownload(@RequestBody WarehouseRequestDto warehouseRequestDto) {

		ExcelDownloadDto excelDownloadDto = warehouseBiz.getWarehouseExcelDownload(warehouseRequestDto);

		ModelAndView modelAndView = new ModelAndView(excelDownloadView);
		modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

		return modelAndView;

	}
}
