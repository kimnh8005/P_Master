package kr.co.pulmuone.bos.statics.pm;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsRequestDto;
import kr.co.pulmuone.v1.statics.pm.dto.PromotionStaticsResponseDto;
import kr.co.pulmuone.v1.statics.pm.service.PromotionStaticsBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class PromotionStaticsController {

    @Autowired
    private PromotionStaticsBiz promotionStaticsBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @Autowired(required = true)
    private HttpServletRequest request;

    @PostMapping(value = "/admin/statics/pm/getStaticsInternalAdvertisingList")
    @ApiOperation(value = "내부광고코드별 매출현황통계 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PromotionStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getStaticsInternalAdvertisingList(PromotionStaticsRequestDto promotionStaticsRequestDto) throws Exception {
        return promotionStaticsBiz.getStaticsInternalAdvertisingList(promotionStaticsRequestDto);
    }

    @RequestMapping(value = "/admin/statics/pm/getExportExcelStaticsInternalAdvertisingList")
    @ApiOperation(value = "내부광고코드별 매출현황통계 엑셀 다운로드")
    public ModelAndView getExportExcelStaticsInternalAdvertisingList(@RequestBody PromotionStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = promotionStaticsBiz.getExportExcelStaticsInternalAdvertisingList(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }



    @PostMapping(value = "/admin/statics/pm/getStaticsAdvertisingList")
    @ApiOperation(value = "외부광고코드별 매출현황 통계 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PromotionStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getStaticsAdvertisingList(PromotionStaticsRequestDto promotionStaticsRequestDto) throws Exception {
        return promotionStaticsBiz.getStaticsAdvertisingList(promotionStaticsRequestDto);
    }

    @RequestMapping(value = "/admin/statics/pm/getExportExcelStaticsAdvertisingList")
    @ApiOperation(value = "외부광고코드별 매출현황 통계 엑셀 다운로드")
    public ModelAndView getExportExcelStaticsAdvertisingList(@RequestBody PromotionStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = promotionStaticsBiz.getExportExcelStaticsAdvertisingList(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @PostMapping(value = "/admin/statics/pm/getStaticsCouponSaleStatusList")
    @ApiOperation(value = "쿠폰별 매출현황 통계 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PromotionStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getStaticsCouponSaleStatusList(PromotionStaticsRequestDto promotionStaticsRequestDto) throws Exception {
        return promotionStaticsBiz.getStaticsCouponSaleStatusList(promotionStaticsRequestDto);
    }

    @RequestMapping(value = "/admin/statics/pm/getExportExcelStaticsCouponSaleStatusList")
    @ApiOperation(value = "쿠폰별 매출현황 통계 엑셀 다운로드")
    public ModelAndView getExportExcelStaticsCouponSaleStatusList(@RequestBody PromotionStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = promotionStaticsBiz.getExportExcelStaticsCouponSaleStatusList(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }



    @PostMapping(value = "/admin/statics/pm/getStaticsUserGroupCouponStatusList")
    @ApiOperation(value = "회원등급 쿠폰현황 통계 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PromotionStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getStaticsUserGroupCouponStatusList(PromotionStaticsRequestDto promotionStaticsRequestDto) throws Exception {
        return promotionStaticsBiz.getStaticsUserGroupCouponStatusList(promotionStaticsRequestDto);
    }

    @RequestMapping(value = "/admin/statics/pm/getExportExcelStaticsUserGroupCouponStatusList")
    @ApiOperation(value = "회원등급 쿠폰현황 통계 엑셀 다운로드")
    public ModelAndView getExportExcelStaticsUserGroupCouponStatusList(@RequestBody PromotionStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = promotionStaticsBiz.getExportExcelStaticsUserGroupCouponStatusList(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }



    @PostMapping(value = "/admin/statics/pm/getStaticsPointStatusList")
    @ApiOperation(value = "적립금 현황 통계 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PromotionStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getStaticsPointStatusList(PromotionStaticsRequestDto promotionStaticsRequestDto) throws Exception {
        return promotionStaticsBiz.getStaticsPointStatusList(promotionStaticsRequestDto);
    }

    @RequestMapping(value = "/admin/statics/pm/getExportExcelStaticsPointStatusList")
    @ApiOperation(value = "적립금 현황 통계 엑셀 다운로드")
    public ModelAndView getExportExcelStaticsPointStatusList(@RequestBody PromotionStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = promotionStaticsBiz.getExportExcelStaticsPointStatusList(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @GetMapping(value = "/admin/statics/pm/getAdvertisingType")
    @ApiOperation(value = "내부광고 코드 유형 조회", httpMethod = "GET", notes = "내부광고 코드 유형 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = String.class)
    })
    public ApiResult<?> getAdvertisingType(PromotionStaticsRequestDto dto) throws Exception {
        return ApiResult.success(promotionStaticsBiz.getAdvertisingType(dto));
    }

    @PostMapping(value = "/admin/statics/pm/getStaticsAdvertisingGoodsList")
    @ApiOperation(value = "외부광고코드별 매출현황 통계 상품별 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = PromotionStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getStaticsAdvertisingShopLiveList(PromotionStaticsRequestDto promotionStaticsRequestDto) throws Exception {
        return promotionStaticsBiz.getStaticsAdvertisingGoodsList(promotionStaticsRequestDto);
    }

    @RequestMapping(value = "/admin/statics/pm/getExportExcelStaticsAdvertisingGoodsList")
    @ApiOperation(value = "외부광고코드별 매출현황 통계 상품별 엑셀 다운로드")
    public ModelAndView getExportExcelStaticsAdvertisingShopLiveList(@RequestBody PromotionStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = promotionStaticsBiz.getExportExcelStaticsAdvertisingGoodsList(dto);
        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);
        return modelAndView;
    }
}
