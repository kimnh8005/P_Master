package kr.co.pulmuone.bos.promotion.advertising;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.promotion.advertising.service.PromotionAdvertisingBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.promotion.advertising.dto.*;
import kr.co.pulmuone.v1.promotion.advertising.service.PromotionAdvertisingBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

@RestController
public class PromotionAdvertisingController {

    @Autowired
    private PromotionAdvertisingBiz promotionAdvertisingBiz;

    @Autowired
    private PromotionAdvertisingBosService promotionAdvertisingBosService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @PostMapping(value = "/admin/promotion/advertising/getAdvertisingExternalList")
    @ApiOperation(value = "외부광고코드 리스트 조회", httpMethod = "POST", notes = "외부광고코드 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = AdvertisingExternalListResponseDto.class)
    })
    public ApiResult<?> getAdvertisingExternalList() throws Exception {
        return ApiResult.success(promotionAdvertisingBiz.getAdvertisingExternalList(BindUtil.bindDto(request, AdvertisingExternalListRequestDto.class)));
    }

    @PostMapping(value = "/admin/promotion/advertising/getAdvertisingExternal")
    @ApiOperation(value = "외부광고코드 단건 조회", httpMethod = "POST", notes = "외부광고코드 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = AdvertisingExternalResponseDto.class)
    })
    public ApiResult<?> getAdvertisingExternal(AdvertisingExternalRequestDto dto) throws Exception {
        return ApiResult.success(promotionAdvertisingBiz.getAdvertisingExternal(dto));
    }

    @PostMapping(value = "/admin/promotion/advertising/getAdvertisingExternalListExcelDownload")
    @ApiOperation(value = "외부광고코드 다운로드", httpMethod = "POST", notes = "외부광고코드 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getAdvertisingExternalListExcelDownload(@RequestBody AdvertisingExternalListRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = promotionAdvertisingBiz.getAdvertisingExternalListExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @PostMapping(value = "/admin/promotion/advertising/addAdvertisingExternal")
    @ApiOperation(value = "외부광고 코드 단건 등록", httpMethod = "POST", notes = "외부광고 코드 단건 등록")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        return promotionAdvertisingBosService.addAdvertisingExternal(dto);
    }

    @PostMapping(value = "/admin/promotion/advertising/putAdvertisingExternal")
    @ApiOperation(value = "외부광고 코드 단건 수정", httpMethod = "POST", notes = "외부광고 코드 단건 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> putAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        promotionAdvertisingBosService.putAdvertisingExternal(dto);
        return ApiResult.success();
    }

    @PostMapping(value = "/admin/promotion/advertising/addAdvertisingExternalExcelUpload")
    @ApiOperation(value = "외부광고코드 엑셀업로드", httpMethod = "POST", notes = "외부광고코드 엑셀업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addAdvertisingExternalExcelUpload(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if (iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }
        return promotionAdvertisingBiz.addAdvertisingExternalExcelUpload(file);
    }

    @GetMapping(value = "/admin/promotion/advertising/getAdvertisingType")
    @ApiOperation(value = "외부광고 코드 유형 조회", httpMethod = "GET", notes = "외부광고 코드 유형 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = String.class)
    })
    public ApiResult<?> getAdvertisingType(AdvertisingTypeRequestDto dto) throws Exception {
        return ApiResult.success(promotionAdvertisingBiz.getAdvertisingType(dto));
    }

    @PostMapping(value = "/admin/promotion/advertising/isExistPmAdExternalCd")
    @ApiOperation(value = "외부광고 코드 중복 여부", httpMethod = "POST", notes = "외부광고 코드 중복 여부")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = String.class)
    })
    public ApiResult<?> isExistPmAdExternalCd(String pmAdExternalCd) throws Exception {
        return ApiResult.success(promotionAdvertisingBiz.isExistPmAdExternalCd(pmAdExternalCd));
    }

}
