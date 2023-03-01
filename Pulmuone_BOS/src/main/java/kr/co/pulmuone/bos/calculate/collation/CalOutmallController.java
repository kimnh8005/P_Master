package kr.co.pulmuone.bos.calculate.collation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallDetlListResponseDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListResponseDto;
import kr.co.pulmuone.v1.calculate.collation.service.CalOutmallBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > 외부몰 주문 대사 Controller
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
public class CalOutmallController {


    @Autowired
    private CalOutmallBiz calOutmallBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * 외부몰 주문 대사 리스트 조회
     * @param request
     * @param calOutmallListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/collation/getOutmallList")
    @ApiOperation(value = "외부몰 주문 대사 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalOutmallListResponseDto.class)
    })
    public ApiResult<?> getOutmallList(HttpServletRequest request, CalOutmallListRequestDto calOutmallListRequestDto) throws Exception {
        return calOutmallBiz.getOutmallList(BindUtil.bindDto(request, CalOutmallListRequestDto.class));
    }

    /**
     * 외부몰 주문 대사파일 업로드
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/collation/addOutmallExcelUpload")
    @ApiOperation(value = "외부몰 주문 대사 엑셀업로드", httpMethod = "POST", notes = "PG 거래 내역 대사 엑셀업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addOutmallExcelUpload(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if (iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }
        return calOutmallBiz.addOutmallExcelUpload(file);
    }




    /**
     * 외부몰 주문 대사 상세내역 리스트 조회
     * @param request
     * @param calOutmallListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/collation/getOutmallDetlList")
    @ApiOperation(value = "외부몰 주문 대사 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalOutmallDetlListResponseDto.class)
    })
    public ApiResult<?> getOutmallDetlList(HttpServletRequest request, CalOutmallListRequestDto calOutmallListRequestDto) throws Exception {
        return calOutmallBiz.getOutmallDetlList(BindUtil.bindDto(request, CalOutmallListRequestDto.class));
    }

    /**
     * 외부몰 주문 대사 상세내역 엑셀 다운로드
     * @param calOutmallListRequestDto
     * @return ModelAndView
     * @throws Exception
     */
    @ApiOperation(value = "외부몰 주문 대사 상세내역 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/calculate/collation/getOutmallDetlExcelList")
    public ModelAndView getOutmallDetlExcelList(@RequestBody CalOutmallListRequestDto calOutmallListRequestDto) throws Exception {

        ExcelDownloadDto excelDownloadDto = calOutmallBiz.getOutmallDetlExcelList(calOutmallListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    /**
     * 외부몰 주문 대사 업로드 실패내역 엑셀 다운로드
     * @param calOutmallListRequestDto
     * @return ModelAndView
     * @throws Exception
     */
    @ApiOperation(value = "외부몰 주문 대사 상세내역 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/calculate/collation/getCalOutmallUploadFailList")
    public ModelAndView getCalOutmallUploadFailList(@RequestBody CalOutmallListRequestDto calOutmallListRequestDto) throws Exception {

        ExcelDownloadDto excelDownloadDto = calOutmallBiz.getCalOutmallUploadFailList(calOutmallListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }


}
