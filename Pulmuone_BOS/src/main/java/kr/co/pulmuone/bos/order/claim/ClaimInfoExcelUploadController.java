package kr.co.pulmuone.bos.order.claim;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadListRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimInfoExcelUploadBiz;
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
public class ClaimInfoExcelUploadController {

    @Autowired
    private ClaimInfoExcelUploadBiz claimInfoExcelUploadBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @PostMapping(value = "/admin/claim/addClaimExcelUpload")
    @ApiOperation(value = "클레임 엑셀업로드", httpMethod = "POST", notes = "클레임 엑셀업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addClaimExcelUpload(MultipartHttpServletRequest fileRequest) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = fileRequest.getFileNames();
        if (iterator.hasNext()) {
            file = fileRequest.getFile(iterator.next());
        }
        return claimInfoExcelUploadBiz.addClaimExcelUpload(file);
    }


    @PostMapping(value = "/admin/claim/getClaimExcelInfoList")
    @ApiOperation(value = "클레임 엑셀업로드 리스트 조회", httpMethod = "POST", notes = "클레임 엑셀업로드 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ClaimInfoExcelUploadListRequestDto.class)
    })
    public ApiResult<?> getClaimExcelInfoList(HttpServletRequest request) throws Exception {
        ClaimInfoExcelUploadListRequestDto dto = BindUtil.bindDto(request, ClaimInfoExcelUploadListRequestDto.class);
        if (dto.getAdminSearchValue().length() > 0 && StringUtil.isNumeric(dto.getAdminSearchValue())) {
            dto.setAdminIdList(Collections.singletonList(dto.getAdminSearchValue()));
        }
        return claimInfoExcelUploadBiz.getClaimExcelInfoList(dto);
    }

    @PostMapping(value = "/admin/claim/getClaimFailExcelDownload")
    @ApiOperation(value = "클레임 엑셀업로드 실패내역 다운로드", httpMethod = "POST", notes = "클레임 엑셀업로드 실패내역 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getOutMallFailExcelDownload(@RequestBody ClaimInfoExcelUploadFailRequestDto dto) {
        ExcelDownloadDto excelDownloadDto = claimInfoExcelUploadBiz.getClaimFailExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }
}
