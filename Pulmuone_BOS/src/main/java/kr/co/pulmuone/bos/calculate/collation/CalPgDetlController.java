package kr.co.pulmuone.bos.calculate.collation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListResponseDto;
import kr.co.pulmuone.v1.calculate.collation.service.CalPgDetlBiz;
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
 * 정산관리 > 대사관리 > PG 대사 상세내역 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 26.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class CalPgDetlController {

    @Autowired
    private CalPgDetlBiz calPgDetlBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @PostMapping(value = "/admin/calculate/collation/getPgDetlList")
    @ApiOperation(value = "PG 대사 상세내역 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalPgListResponseDto.class)
    })
    public ApiResult<?> getPgDetailList(HttpServletRequest request) throws Exception {
        return calPgDetlBiz.getPgDetailList(BindUtil.bindDto(request, CalPgDetlListRequestDto.class));
    }

    @PostMapping(value = "/admin/calculate/collation/getPgDetlListExportExcel")
    @ApiOperation(value = "PG 대사 상세내역 리스트 조회 엑셀 다운로드", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getPgDetlListExportExcel(@RequestBody CalPgDetlListRequestDto dto) {
        ExcelDownloadDto excelDownloadDto = calPgDetlBiz.getPgDetlListExportExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
