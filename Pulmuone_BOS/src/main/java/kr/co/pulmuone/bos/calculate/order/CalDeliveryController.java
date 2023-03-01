package kr.co.pulmuone.bos.calculate.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.order.dto.CalDeliveryListRequestDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalDeliveryListResponseDto;
import kr.co.pulmuone.v1.calculate.order.service.CalDeliveryBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 택배비 내역 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class CalDeliveryController {


    @Autowired
    private CalDeliveryBiz calDeliveryBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * 택배비 내역 리스트 조회
     * @param request
     * @param calDeliveryListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/order/getDeliveryList")
    @ApiOperation(value = "택배비 내역 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalDeliveryListResponseDto.class)
    })
    public ApiResult<?> getDeliveryList(HttpServletRequest request, CalDeliveryListRequestDto calDeliveryListRequestDto) throws Exception {
        return calDeliveryBiz.getDeliveryList(BindUtil.bindDto(request, CalDeliveryListRequestDto.class));
    }

    /**
     * 택배비 내역 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "택배비 내역 리스트 엑셀 다운로드 목록 조회", httpMethod = "POST")
    @PostMapping(value = "/admin/calculate/order/getDeliveryListExportExcel")
    public ModelAndView getDeliveryListExportExcel(@RequestBody CalDeliveryListRequestDto calDeliveryListRequestDto) {

        ExcelDownloadDto excelDownloadDto = calDeliveryBiz.getDeliveryListExportExcel(calDeliveryListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

}
