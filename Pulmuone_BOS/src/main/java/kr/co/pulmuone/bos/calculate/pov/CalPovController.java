package kr.co.pulmuone.bos.calculate.pov;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovListRequestDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovListResponseDto;
import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovProcessVo;
import kr.co.pulmuone.v1.calculate.pov.service.CalPovBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.SessionUtil;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > POV I/F > POV I/F Controller
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
public class CalPovController {


    @Autowired
    private CalPovBiz calPovBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * POV I/F 리스트 조회
     * @param request
     * @param calPovListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/pov/getPovList")
    @ApiOperation(value = "POV I/F 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalPovListResponseDto.class)
    })
    public ApiResult<?> getPovList(@RequestParam("findYear") String year, @RequestParam("findMonth") String month) throws Exception {
        return calPovBiz.getPovList(year, month);
    }

    @PostMapping(value = "/admin/calculate/pov/getPovListExportExcel")
    @ApiOperation(value = "POV I/F 리스트 엑셀 다운로드", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalPovListResponseDto.class)
    })
    public ModelAndView getPovListExportExcel(@RequestBody CalPovListRequestDto reqDto) throws Exception {
    	ExcelDownloadDto excelDownloadDto = calPovBiz.getPovListExportExcel(reqDto.getFindYear(), reqDto.getFindMonth());

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @PostMapping(value = "/admin/calculate/pov/odPovInterface")
    @ApiOperation(value = "POV I/F 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalPovListResponseDto.class)
    })
    public ApiResult<?> odPovInterface(@RequestParam("scenario") String scenario, @RequestParam("findYear") String year, @RequestParam("findMonth") String month) throws Exception {

    	UserVo bosUserVO = SessionUtil.getBosUserVO();

        return calPovBiz.odPovInterface(scenario, year, month, bosUserVO.getLoginName() + " / " + bosUserVO.getLoginId());
    }

    /**
     * POV I/F 파일 업로드
     * @param request
     * @param uploadType
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/pov/addPovExcelUpload")
    @ApiOperation(value = "POV I/F 엑셀업로드", httpMethod = "POST", notes = "PG 거래 내역 대사 엑셀업로드")
    @ApiResponses(value = {
        @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addPgExcelUpload(MultipartHttpServletRequest request, @RequestParam("scenario") String scenario,
    		@RequestParam("year") String year, @RequestParam("month") String month) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if (iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }

        UserVo bosUserVO = SessionUtil.getBosUserVO();

		CalPovProcessVo calPovProcessVo = CalPovProcessVo.builder().year(year).month(month).scenario(scenario)
				.creator(bosUserVO.getLoginName() + " / " + bosUserVO.getLoginId()).build();

        System.out.println("scenario : " + scenario);
        return calPovBiz.addPovExcelUpload(file, calPovProcessVo);
    }
}
