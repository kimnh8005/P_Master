package kr.co.pulmuone.bos.policy.shippingarea;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;

import kr.co.pulmuone.v1.goods.etc.dto.CertificationRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadListRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaListRequestDto;
import kr.co.pulmuone.v1.policy.shippingarea.service.PolicyShippingAreaBiz;

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
 * Pulmuone
 * 도서산간/배송불가 권역 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일           :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20210928		   	   남기승         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class PolicyShippingAreaController {

    @Autowired
    private PolicyShippingAreaBiz policyShippingAreaBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰


    @PostMapping(value = "/admin/policy/addShippingAreaExcelUpload")
    @ApiOperation(value = "도서산간/배송불가 엑셀업로드", httpMethod = "POST", notes = "도서산간/배송불가 엑셀업로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addShippingAreaExcelUpload(MultipartHttpServletRequest fileRequest, ShippingAreaListRequestDto shippingAreaListRequestDto) throws Exception{
        MultipartFile file = null;
        Iterator<String> iterator = fileRequest.getFileNames();
        if (iterator.hasNext()) {
            file = fileRequest.getFile(iterator.next());
        }
        return policyShippingAreaBiz.addShippingAreaExcelUpload(file, shippingAreaListRequestDto);

    }

    @PostMapping(value = "/admin/policy/getUploadShippingAreaInfo")
    @ApiOperation(value = "도서산간/배송불가 엑셀업로드 내역 조회", httpMethod = "POST", notes = "도서산간/배송불가 엑셀업로드 내역 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> getUploadShippingAreaInfo(ShippingAreaExcelUploadDto shippingAreaExcelUploadDto) throws Exception {
        return policyShippingAreaBiz.getUploadShippingAreaInfo(shippingAreaExcelUploadDto);
    }

    @PostMapping(value = "/admin/policy/getShippingAreaExcelInfoList")
    @ApiOperation(value = "도서산간/배송불가 엑셀업로드 리스트 조회", httpMethod = "POST", notes = "도서산간/배송불가 엑셀업로드 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ShippingAreaExcelUploadListRequestDto.class)
    })
    public ApiResult<?> getShippingAreaExcelInfoList(HttpServletRequest request) throws Exception {
        ShippingAreaExcelUploadListRequestDto dto = BindUtil.bindDto(request, ShippingAreaExcelUploadListRequestDto.class);
        return policyShippingAreaBiz.getPolicyShippingAreaList(dto);
    }

    @PostMapping(value = "/admin/policy/getShippingAreaInfoList")
    @ApiOperation(value = "도서산간/배송불가 리스트 조회", httpMethod = "POST", notes = "도서산간/배송불가 엑셀업로드 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ShippingAreaExcelUploadListRequestDto.class)
    })
    public ApiResult<?> getShippingAreaInfoList(HttpServletRequest request) throws Exception {
        ShippingAreaExcelUploadListRequestDto dto = BindUtil.bindDto(request, ShippingAreaExcelUploadListRequestDto.class);
        return policyShippingAreaBiz.getPolicyShippingAreaInfoList(dto);
    }

    @PostMapping(value = "/admin/policy/getShippingAreaInfoExcelDownload")
    @ApiOperation(value = "도서산간/배송불가 엑셀 적용 내역 다운로드", httpMethod = "POST", notes = "도서산간/배송불가 엑셀 적용 내역 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getShippingAreaInfoExcelDownload(@RequestBody ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) {
        ExcelDownloadDto excelDownloadDto = policyShippingAreaBiz.getShippingAreaInfoExcelDownload(shippingAreaExcelUploadFailRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @PostMapping(value = "/admin/policy/getShippingAreaFailExcelDownload")
    @ApiOperation(value = "도서산간/배송불가 엑셀업로드 실패내역 다운로드", httpMethod = "POST", notes = "도서산간/배송불가 엑셀업로드 실패내역 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getShippingAreaFailExcelDownload(@RequestBody ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) {
        ExcelDownloadDto excelDownloadDto = policyShippingAreaBiz.getShippingAreaFailExcelDownload(shippingAreaExcelUploadFailRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @PostMapping(value = "/admin/policy/delShippingAreaInfo")
    @ApiOperation(value = "도서산간/배송불가 정보 일괄 삭제", httpMethod = "POST", notes = "도서산간/배송불가 정보 일괄 삭제")
    @ApiImplicitParams({ @ApiImplicitParam(name = "psShippingAreaExcelInfoId", value = "도서산간/배송불가 엑셀 적용 PK", required = true, dataType = "long") })
    public ApiResult<?> delShippingAreaInfo(@RequestBody ShippingAreaExcelUploadFailRequestDto shippingAreaExcelUploadFailRequestDto) throws Exception{
        return policyShippingAreaBiz.delShippingAreaInfo(shippingAreaExcelUploadFailRequestDto);
    }
}
