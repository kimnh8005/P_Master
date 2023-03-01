package kr.co.pulmuone.bos.outmall.order;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.outmall.order.dto.*;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallShippingExceldownHistVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallTrackingNumberHistVo;
import kr.co.pulmuone.v1.outmall.order.service.OutmallOrderBiz;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerListRequestDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;

@RestController
public class OutmallOrderController {

    @Autowired
    private OutmallOrderBiz outmallOrderBiz;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @PostMapping(value = "/admin/outmall/order/getCollectionMallInterfaceList")
    @ApiOperation(value = "수집몰 연동내역 리스트 조회", httpMethod = "POST", notes = "수집몰 연동내역 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CollectionMallInterfaceListResponseDto.class)
    })
    public ApiResult<?> getCollectionMallInterfaceList() throws Exception {
        return outmallOrderBiz.getCollectionMallInterfaceList(BindUtil.bindDto(request, CollectionMallInterfaceListRequestDto.class));
    }

    @PostMapping(value = "/admin/outmall/order/putCollectionMallInterfaceProgress")
    @ApiOperation(value = "수집몰 연동내역 진행상태 변경 - 단건", httpMethod = "POST", notes = "수집몰 연동내역 진행상태 변경 - 단건")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processCode", value = "처리상태 코드", required = true, dataType = "String"),
            @ApiImplicitParam(name = "ifEasyadminInfoId", value = "수집몰 연동 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> putCollectionMallInterfaceProgress(String processCode, Long ifEasyadminInfoId) throws Exception {
        return outmallOrderBiz.putCollectionMallInterfaceProgress(processCode, ifEasyadminInfoId);
    }

    @PostMapping(value = "/admin/outmall/order/putCollectionMallInterfaceProgressList")
    @ApiOperation(value = "수집몰 연동내역 진행상태 변경 - 리스트", httpMethod = "POST", notes = "수집몰 연동내역 진행상태 변경 - 리스트")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> putCollectionMallInterfaceProgressList(CollectionMallInterfaceProgressRequestDto dto) throws Exception {
        dto.setIfEasyadminInfoIdList(BindUtil.convertJsonArrayToDtoList(dto.getIfEasyadminInfoIdParam(), Long.class));
        return outmallOrderBiz.putCollectionMallInterfaceProgressList(dto);
    }

    @PostMapping(value = "/admin/outmall/order/getCollectionMallFailExcelDownload")
    @ApiOperation(value = "실패내역 다운로드", httpMethod = "POST", notes = "실패내역 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getCollectionMallFailExcelDownload(@RequestBody CollectionMallInterfaceFailRequestDto dto) {
        ExcelDownloadDto excelDownloadDto = outmallOrderBiz.getCollectionMallFailExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @GetMapping(value = "/admin/outmall/order/getSellersList")
    @ApiOperation(value = "외부몰 클레임 주문리스트 조회조건", httpMethod = "GET", notes = "외부몰 클레임 주문리스트 조회조건")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetCodeListResultVo.class)
    })
    public ApiResult<?> getSellersList(String sellersGroupCode) throws Exception {
        return outmallOrderBiz.getSellersList(sellersGroupCode);
    }

    @PostMapping(value = "/admin/outmall/order/getClaimOrderList")
    @ApiOperation(value = "외부몰 클레임 주문리스트 조회", httpMethod = "POST", notes = "외부몰 클레임 주문리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ClaimOrderListResponseDto.class)
    })
    public ApiResult<?> getClaimOrderList() throws Exception {
        ClaimOrderListRequestDto dto = BindUtil.bindDto(request, ClaimOrderListRequestDto.class);
        if (dto.getAdminSearchValue().length() > 0 && StringUtil.isNumeric(dto.getAdminSearchValue())) {
            dto.setAdminIdList(Collections.singletonList(dto.getAdminSearchValue()));
        }

        return outmallOrderBiz.getClaimOrderList(dto);
    }

    @ApiOperation(value = "외부몰 클레임 주문리스트 엑셀다운로드")
    @PostMapping(value = "/admin/outmall/order/getClaimOrderListExportExcel")
    public ModelAndView getClaimOrderListExportExcel(@RequestBody ClaimOrderListRequestDto dto) throws Exception {
        // ClaimOrderListRequestDto dto = BindUtil.bindDto(request, ClaimOrderListRequestDto.class);
        if (dto.getAdminSearchValue().length() > 0 && StringUtil.isNumeric(dto.getAdminSearchValue())) {
            dto.setAdminIdList(Collections.singletonList(dto.getAdminSearchValue()));
        }

        ExcelDownloadDto excelDownloadDto = outmallOrderBiz.getClaimOrderListExportExcel(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @PostMapping(value = "/admin/outmall/order/putClaimOrderProgress")
    @ApiOperation(value = "외부몰 클레임 주문 진행상태 변경 - 단건", httpMethod = "POST", notes = "외부몰 클레임 주문 진행상태 변경 - 단건")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> putClaimOrderProgress(ClaimOrderProgressRequestDto dto) throws Exception {
        return outmallOrderBiz.putClaimOrderProgress(dto);
    }

    @PostMapping(value = "/admin/outmall/order/putClaimOrderProgressList")
    @ApiOperation(value = "외부몰 클레임 주문 진행상태 변경 - 리스트", httpMethod = "POST", notes = "외부몰 클레임 주문 진행상태 변경 - 리스트")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> putClaimOrderProgressList(ClaimOrderProgressRequestDto dto) throws Exception {
        dto.setIfEasyadminOrderClaimIdList(BindUtil.convertJsonArrayToDtoList(dto.getIfEasyadminOrderClaimIdParam(), Long.class));
        return outmallOrderBiz.putClaimOrderProgressList(dto);
    }

    @PostMapping(value = "/admin/outmall/order/addOutMallExcelUpload")
    @ApiOperation(value = "외부몰 주문 엑셀업로드", httpMethod = "POST", notes = "외부몰 주문 엑셀업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addOutMallExcelUpload(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = null;
        Iterator<String> iterator = request.getFileNames();
        if (iterator.hasNext()) {
            file = request.getFile(iterator.next());
        }
        return outmallOrderBiz.addOutMallExcelUpload(file);
    }

    @PostMapping(value = "/admin/outmall/order/getOutMallExcelInfoList")
    @ApiOperation(value = "외부몰 주문 엑셀업로드 리스트 조회", httpMethod = "POST", notes = "외부몰 주문 엑셀업로드 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = OutMallExcelListResponseDto.class)
    })
    public ApiResult<?> getOutMallExcelInfoList() throws Exception {
        OutMallExcelListRequestDto dto = BindUtil.bindDto(request, OutMallExcelListRequestDto.class);
        if (dto.getAdminSearchValue().length() > 0 && StringUtil.isNumeric(dto.getAdminSearchValue())) {
            dto.setAdminIdList(Collections.singletonList(dto.getAdminSearchValue()));
        }
        return outmallOrderBiz.getOutMallExcelInfoList(dto);
    }

    @PostMapping(value = "/admin/outmall/order/getOutMallFailExcelDownload")
    @ApiOperation(value = "외부몰 주문 엑셀업로드 실패내역 다운로드", httpMethod = "POST", notes = "외부몰 주문 엑셀업로드 실패내역 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getOutMallFailExcelDownload(@RequestBody OutMallExcelFailRequestDto dto) throws InvocationTargetException, IllegalAccessException {
        ExcelDownloadDto excelDownloadDto = outmallOrderBiz.getOutMallFailExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }


    @PostMapping(value = "/admin/outmall/order/getOutmallShippingInfoDownload")
    @ApiOperation(value = "외부몰 배송정보 내역 다운로드", httpMethod = "POST", notes = "외부몰 배송정보 내역 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getOutmallShippingInfoDownload(@RequestBody OutMallShippingInfoDownloadRequestDto dto) {
        ExcelDownloadDto excelDownloadDto = outmallOrderBiz.getOutmallShippingInfoDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @PostMapping(value = "/admin/outmall/order/getOutMallShippingExceldownHist")
    @ApiOperation(value = "외부몰 배송정보 다운로드 내역 조회", httpMethod = "POST", notes = "외부몰 배송정보 다운로드 내역 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = OutMallShippingExceldownHistVo.class)
    })
    public ApiResult<?> getOutMallShippingExceldownHist(OutMallShippingInfoDownloadHistRequestDto dto) throws Exception{
        return ApiResult.success(outmallOrderBiz.getOutMallShippingExceldownHist((OutMallShippingInfoDownloadHistRequestDto) BindUtil.convertRequestToObject(request,
        		OutMallShippingInfoDownloadHistRequestDto.class)));
    }

    @PostMapping(value = "/admin/outmall/order/getOutMallTrackingNumberHist")
    @ApiOperation(value = "외부몰 배송정보 송장등록이력 조회", httpMethod = "POST", notes = "외부몰 배송정보 송장등록이력 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = OutMallTrackingNumberHistVo.class)
    })
    public ApiResult<?> getOutMallTrackingNumberHist(OutMallTrackingNumberHistRequestDto dto) throws Exception{
        return ApiResult.success(outmallOrderBiz.getOutMallTrackingNumberHist((OutMallTrackingNumberHistRequestDto) BindUtil.convertRequestToObject(request,
                OutMallTrackingNumberHistRequestDto.class)));
    }

}
