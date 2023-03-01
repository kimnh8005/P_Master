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
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListRequestDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListResponseDto;
import kr.co.pulmuone.v1.calculate.order.service.CalGoodsBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 상품 정산 Controller
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
public class CalGoodsController {


    @Autowired
    private CalGoodsBiz calGoodsBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * 상품 정산 리스트 조회
     * @param request
     * @param calGoodsListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/order/getGoodsList")
    @ApiOperation(value = "상품 정산 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalGoodsListResponseDto.class)
    })
    public ApiResult<?> getGoodsList(HttpServletRequest request, CalGoodsListRequestDto calGoodsListRequestDto) throws Exception {
        return calGoodsBiz.getGoodsList(BindUtil.bindDto(request, CalGoodsListRequestDto.class));
    }

    /**
     * 상품 정산 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "상품 정산 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/calculate/order/getGoodsListExportExcel")
    public ModelAndView getGoodsListExportExcel(@RequestBody CalGoodsListRequestDto calGoodsListRequestDto) {

        ExcelDownloadDto excelDownloadDto = calGoodsBiz.getGoodsListExportExcel(calGoodsListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

    /**
     * 상품 정산 (IF 아닌) 리스트 조회
     * @param request
     * @param calGoodsListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/order/getGoodsNotIfList")
    @ApiOperation(value = "상품 정산 (IF 아닌) 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalGoodsListResponseDto.class)
    })
    public ApiResult<?> getGoodsNotIfList(HttpServletRequest request, CalGoodsListRequestDto calGoodsListRequestDto) throws Exception {
        return calGoodsBiz.getGoodsNotIfList(BindUtil.bindDto(request, CalGoodsListRequestDto.class));
    }

    /**
     * 상품 정산 (IF 아닌) 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "상품 정산 (IF 아닌) 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/calculate/order/getGoodsNotIfListExportExcel")
    public ModelAndView getGoodsNotIfListExportExcel(@RequestBody CalGoodsListRequestDto calGoodsListRequestDto) {

        ExcelDownloadDto excelDownloadDto = calGoodsBiz.getGoodsNotIfListExportExcel(calGoodsListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 매장 상품 정산 리스트 조회
     * @param request
     * @param calGoodsListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/order/getStoreGoodsList")
    @ApiOperation(value = "매장 상품 정산 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalGoodsListResponseDto.class)
    })
    public ApiResult<?> getStoreGoodsList(HttpServletRequest request, CalGoodsListRequestDto calGoodsListRequestDto) throws Exception {
        return calGoodsBiz.getStoreGoodsList(BindUtil.bindDto(request, CalGoodsListRequestDto.class));
    }

    /**
     * 매장 상품 정산 리스트 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "매장 상품 정산 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/calculate/order/getStoreGoodsListExportExcel")
    public ModelAndView getStoreGoodsListExportExcel(@RequestBody CalGoodsListRequestDto calGoodsListRequestDto) {

        ExcelDownloadDto excelDownloadDto = calGoodsBiz.getStoreGoodsListExportExcel(calGoodsListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }


}
