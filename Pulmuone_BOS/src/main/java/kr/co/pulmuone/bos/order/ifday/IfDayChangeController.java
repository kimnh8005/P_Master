package kr.co.pulmuone.bos.order.ifday;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelFailRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelListRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelListResponseDto;
import kr.co.pulmuone.v1.order.ifday.service.IfDayExcelBiz;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelFailRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelListRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelListResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Iterator;

@RestController
public class IfDayChangeController {

    @Autowired
    private IfDayExcelBiz ifDayExcelBiz;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @PostMapping(value = "/admin/order/ifDay/addIfDayExcelUpload")
    @ApiOperation(value = "I/F 일자 변경 엑셀업로드", httpMethod = "POST", notes = "I/F 일자 변경 엑셀업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addIfDayExcelUpload(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if (iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }
        return ifDayExcelBiz.addIfDayExcelUpload(file);
    }


    @PostMapping(value = "/admin/order/ifDay/getIfDayExcelInfoList")
    @ApiOperation(value = "I/F 일자 변경 엑셀업로드 리스트 조회", httpMethod = "POST", notes = "I/F 일자 변경 엑셀업로드 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = IfDayExcelListResponseDto.class)
    })
    public ApiResult<?> getIfDayExcelInfoList() throws Exception {
        IfDayExcelListRequestDto dto = BindUtil.bindDto(request, IfDayExcelListRequestDto.class);
        if (dto.getAdminSearchValue().length() > 0 && StringUtil.isNumeric(dto.getAdminSearchValue())) {
            dto.setAdminIdList(Collections.singletonList(dto.getAdminSearchValue()));
        }
        return ifDayExcelBiz.getIfDayExcelInfoList(dto);
    }

    @PostMapping(value = "/admin/order/ifDay/getIfDayFailExcelDownload")
    @ApiOperation(value = "I/F 일자 변경 엑셀업로드 실패내역 다운로드", httpMethod = "POST", notes = "I/F 일자 변경 엑셀업로드 실패내역 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getOutMallFailExcelDownload(@RequestBody IfDayExcelFailRequestDto dto) {
        ExcelDownloadDto excelDownloadDto = ifDayExcelBiz.getIfDayFailExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }
}
