package kr.co.pulmuone.bos.order.delivery;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlListResponseDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberExcelUploadRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberListResponseDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberFailVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import kr.co.pulmuone.v1.order.delivery.service.OrderBulkTrackingNumberBiz;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;

/**
 * <PRE>
 * Forbiz Korea
 * 일괄 송장 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일				:  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 24.       이규한          	  최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class OrderBulkTrackingNumberController {

	private final OrderBulkTrackingNumberBiz orderBulkTrackingNumberBiz;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	/**
	 * 일괄송장 엑셀 업로드
	 *
	 * @param OrderBulkTrackingNumberExcelUploadRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "일괄송장 엑셀 업로드", httpMethod = "POST")
	@PostMapping(value = "/admin/order/delivery/orderBulkTrackingNumberExcelUpload")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> orderBulkTrackingNumberExcelUpload(MultipartHttpServletRequest request, OrderBulkTrackingNumberExcelUploadRequestDto paramDto) throws Exception {
		MultipartFile file = null;
		Iterator<String> iterator = request.getFileNames();
		if (iterator.hasNext()) {
			file = request.getFile(iterator.next());
		}

		System.out.println("paramDto : " + paramDto);
		return orderBulkTrackingNumberBiz.orderBulkTrackingNumberExcelUpload(file, paramDto);
	}



    /**
     * 일괄송장 입력 내역 목록 조회
     *
     * @param OrderBulkTrackingNumberListRequestDto
     * @return ApiResult<?>
     */
	@ApiOperation(value = "일괄송장 입력 내역 목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/delivery/getOrderBulkTrackingNumberList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderBulkTrackingNumberListResponseDto.class) })
    public ApiResult<?> getOrderBulkTrackingNumberList(HttpServletRequest request, OrderBulkTrackingNumberListRequestDto paramDto) throws Exception {
    	return orderBulkTrackingNumberBiz.getOrderBulkTrackingNumberList(BindUtil.bindDto(request, OrderBulkTrackingNumberListRequestDto.class));
    }

    /**
     * 일괄송장 입력 실패내역 엑셀 다운로드
     *
     * @param OrderBulkTrackingNumberListRequestDto
     * @return ModelAndView
     */
	@ApiOperation(value = "일괄송장 입력 실패내역 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/order/delivery/orderBulkTrackingNumberFailExcelDownload")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderBulkTrackingNumberFailVo.class) })
    public ModelAndView orderBulkTrackingNumberFailExcelDownload(@RequestBody OrderBulkTrackingNumberListRequestDto paramDto) throws Exception {
        ExcelDownloadDto excelDownloadDto = orderBulkTrackingNumberBiz.getOrderBulkTrackingNumberFailList(paramDto);
        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);
        return modelAndView;
    }

    /**
     * 일괄 송장 입력 내역 상세 목록 조회
     *
     * @param OrderBulkTrackingNumberDetlListRequestDto
     * @return ApiResult<?>
     */
	@ApiOperation(value = "일괄 송장 입력 내역 상세 목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/order/delivery/getOrderBulkTrackingNumberDetlList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderBulkTrackingNumberDetlListResponseDto.class) })
    public ApiResult<?> getOrderBulkTrackingNumberDetlList(HttpServletRequest request, OrderBulkTrackingNumberDetlListRequestDto paramDto) throws Exception {
    	return orderBulkTrackingNumberBiz.getOrderBulkTrackingNumberDetlList(BindUtil.bindDto(request, OrderBulkTrackingNumberDetlListRequestDto.class));
    }

    /**
     * 일괄 송장 입력 내역 상세 엑셀 다운로드
     *
     * @param OrderBulkTrackingNumberDetlListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "일괄 송장 입력 내역 상세 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/order/delivery/orderBulkTrackingNumberDetlExcelDownload")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = OrderBulkTrackingNumberDetlDto.class) })
    public ModelAndView orderBulkTrackingNumberDetlExcelDownload(@RequestBody OrderBulkTrackingNumberDetlListRequestDto paramDto) throws Exception {
    	ExcelDownloadDto excelDownloadDto = orderBulkTrackingNumberBiz.getOrderBulkTrackingNumberDetlExcelList(paramDto);
        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);
        return modelAndView;
    }
}