package kr.co.pulmuone.bos.calculate.order;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListRequestDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListResponseDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListRequestDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListResponseDto;
import kr.co.pulmuone.v1.calculate.order.service.CalGoodsBiz;
import kr.co.pulmuone.v1.calculate.order.service.CalOrderBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 주문 정산 Controller
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
public class CalOrderController {


    @Autowired
    private CalOrderBiz calOrderBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * 주문 정산 리스트 조회
     * @param request
     * @param calOrderListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/order/getOrderList")
    @ApiOperation(value = "주문 정산 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalOrderListResponseDto.class)
    })
    public ApiResult<?> getOrderList(HttpServletRequest request, CalOrderListRequestDto calOrderListRequestDto) throws Exception {
        return calOrderBiz.getOrderList(BindUtil.bindDto(request, CalOrderListRequestDto.class));
    }

    /**
     * 주문 정산 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "주문 정산 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/calculate/order/getOrderListExportExcel")
    public ModelAndView getOrderListExportExcel(@RequestBody CalOrderListRequestDto calOrderListRequestDto) {

        ExcelDownloadDto excelDownloadDto = calOrderBiz.getOrderListExportExcel(calOrderListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

}
