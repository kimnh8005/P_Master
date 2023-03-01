package kr.co.pulmuone.bos.calculate.collation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListResponseDto;
import kr.co.pulmuone.v1.calculate.collation.service.CalSalesBiz;
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
 * 정산관리 > 대사관리 > 통합몰 매출 대사 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 28.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class CalSalesController {


    @Autowired
    private CalSalesBiz calSalesBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @PostMapping(value = "/admin/calculate/collation/getSalesList")
    @ApiOperation(value = "통합몰 매출 대사 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalSalesListResponseDto.class)
    })
    public ApiResult<?> getSalesList(HttpServletRequest request) throws Exception {
        return calSalesBiz.getSalesList(BindUtil.bindDto(request, CalSalesListRequestDto.class));
    }

    @PostMapping(value = "/admin/calculate/collation/getSalesListExportExcel")
    @ApiOperation(value = "통합몰 매출 대사 엑셀 다운로드", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getSalesListExportExcel(@RequestBody CalSalesListRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = calSalesBiz.getSalesListExportExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
