package kr.co.pulmuone.bos.calculate.collation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListResponseDto;
import kr.co.pulmuone.v1.calculate.collation.service.CalPgBiz;
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
 * 정산관리 > 대사관리 > PG 거래 내역 대사 Controller
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
public class CalPgController {


    @Autowired
    private CalPgBiz calPgBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    /**
     * PG 거래 내역 대사 리스트 조회
     * @param request
     * @param calPgListRequestDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/collation/getPgList")
    @ApiOperation(value = "PG 거래 내역 대사 리스트 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = CalPgListResponseDto.class)
    })
    public ApiResult<?> getPgList(HttpServletRequest request, CalPgListRequestDto calPgListRequestDto) throws Exception {
        return calPgBiz.getPgList(BindUtil.bindDto(request, CalPgListRequestDto.class));
    }


    /**
     * PG 거래 내역 대사 파일 업로드
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/calculate/collation/addPgExcelUpload")
    @ApiOperation(value = "PG 거래 내역 대사 엑셀업로드", httpMethod = "POST", notes = "PG 거래 내역 대사 엑셀업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addPgExcelUpload(MultipartHttpServletRequest request, CalPgListRequestDto calPgListRequestDto) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if (iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }
        return calPgBiz.addPgExcelUpload(file, calPgListRequestDto);
    }


    /**
     * PG 거래 내역 대사 파일 업로드 실패내역 엑셀 다운로드
     * @param calPgListRequestDto
     * @return ModelAndView
     * @throws Exception
     */
    @ApiOperation(value = "PG 거래내역 대사 파일 업로드 실패내역 엑셀 다운로드", httpMethod = "POST")
    @PostMapping(value = "/admin/calculate/collation/getCalPgUploadFailList")
    public ModelAndView getCalPgUploadFailList(@RequestBody CalPgListRequestDto calPgListRequestDto) throws Exception {

        ExcelDownloadDto excelDownloadDto = calPgBiz.getCalPgUploadFailList(calPgListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }


}
