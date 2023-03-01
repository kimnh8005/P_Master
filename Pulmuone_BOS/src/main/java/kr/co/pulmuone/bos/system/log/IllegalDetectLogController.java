package kr.co.pulmuone.bos.system.log;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponRequestDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogResponseDto;
import kr.co.pulmuone.v1.system.log.service.IllegalDetectLogBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IllegalDetectLogController {

    @Autowired(required=true)
    private HttpServletRequest request;

    @Autowired
    private IllegalDetectLogBiz ilegalDetectLogBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * 부정거래탐지 리스트 조회
     */
    @PostMapping(value = "/admin/st/log/getIllegalDetectLogList")
    @ApiResponse(code = 900, message = "response data", response = IllegalDetectLogResponseDto.class)
    public ApiResult<?> getIllegalDetectLogList(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception {
        return ilegalDetectLogBiz.getIllegalDetectLogList((IllegalDetectLogRequestDto) BindUtil.convertRequestToObject(request, IllegalDetectLogRequestDto.class));
    }

    /**
     * 부정거래 탐지 상태변경처리
     * @param
     * @return ApiResult
     */
    @RequestMapping(value = "/admin/st/log/putCompleteRequest")
    @ApiOperation(value = "부정거래 탐지 상태변경처리", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
    public ApiResult<?> putCompleteRequest(@RequestBody IllegalDetectLogRequestDto dto) throws Exception {
        return ilegalDetectLogBiz.putCompleteRequest(dto);
    }

    /**
     * 부정거래 탐지 상세조회
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/st/log/getIllegalDetectLogDetail")
    @ApiResponse(code = 900, message = "response data", response = CouponRequestDto.class)
    @ResponseBody
    public ApiResult<?> getIllegalDetectLogDetail(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception {

        return ilegalDetectLogBiz.getIllegalDetectLogDetail(illegalDetectLogRequestDto);
    }



    /**
     * 부정거래 탐지 내용수정
     *
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "부정거래 탐지 내용수정")
    @PostMapping(value = "/admin/st/log/putIllegalDetectDetailInfo")
    @ResponseBody
    public ApiResult<?> putIllegalDetectDetailInfo(IllegalDetectLogRequestDto illegalDetectLogRequestDto) throws Exception {
        return ilegalDetectLogBiz.putIllegalDetectDetailInfo(illegalDetectLogRequestDto);
    }



    /**
     * 부정거래탐지 리스트 엑셀 다운로드 목록 조회
     *
     * @param illegalDetectLogRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "부정거래탐지 리스트 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/st/log/illegalDetectListExportExcel")
    public ModelAndView illegalDetectListExportExcel(@RequestBody IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        ExcelDownloadDto excelDownloadDto = ilegalDetectLogBiz.illegalDetectListExportExcel(illegalDetectLogRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }


    /**
     * 부정거래탐지 회원ID 엑셀 다운로드 목록 조회
     *
     * @param illegalDetectLogRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "부정거래탐지 회원ID 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/st/log/illegalDetectUserIdxportExcel")
    public ModelAndView illegalDetectUserIdxportExcel(@RequestBody IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        ExcelDownloadDto excelDownloadDto = ilegalDetectLogBiz.illegalDetectUserIdxportExcel(illegalDetectLogRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

    /**
     * 부정거래탐지 주문번호 엑셀 다운로드 목록 조회
     *
     * @param illegalDetectLogRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "부정거래탐지 주문번호 엑셀 다운로드 목록 조회")
    @PostMapping(value = "/admin/st/log/illegalDetectOrderExportExcel")
    public ModelAndView illegalDetectOrderExportExcel(@RequestBody IllegalDetectLogRequestDto illegalDetectLogRequestDto) {

        ExcelDownloadDto excelDownloadDto = ilegalDetectLogBiz.illegalDetectOrderExportExcel(illegalDetectLogRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }
}
