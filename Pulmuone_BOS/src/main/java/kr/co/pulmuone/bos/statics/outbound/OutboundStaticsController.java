package kr.co.pulmuone.bos.statics.outbound;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsResponseDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsResponseDto;
import kr.co.pulmuone.v1.statics.outbound.service.OutboundStaticsBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class OutboundStaticsController {

    @Autowired
    private OutboundStaticsBiz outboundStaticsBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @RequestMapping(value = "/admin/statics/outbound/getOutboundStaticsList")
    @ApiOperation(value = "출고처 / 판매처 별 출고통계 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = OutboundStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getOutboundStaticsList(HttpServletRequest request) throws Exception {
        return outboundStaticsBiz.getOutboundStaticsList(BindUtil.bindDto(request, OutboundStaticsRequestDto.class));
    }

    @RequestMapping(value = "/admin/statics/outbound/getExportExcelOutboundStaticsList")
    @ApiOperation(value = "출고처 / 판매처 별 출고통계 엑셀 다운로드")
    public ModelAndView getExportExcelOutboundStaticsList(@RequestBody OutboundStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = outboundStaticsBiz.getExportExcelOutboundStaticsList(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/statics/outbound/getMissOutboundStaticsList")
    @ApiOperation(value = "미출 통계 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = MissOutboundStaticsResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> selectMissOutboundStaticsList(HttpServletRequest request) throws Exception {
        return outboundStaticsBiz.getMissOutboundStaticsList(BindUtil.bindDto(request, MissOutboundStaticsRequestDto.class));
    }

    @RequestMapping(value = "/admin/statics/outbound/getExportExcelMissOutboundStaticsList")
    @ApiOperation(value = "미출 통계 엑셀다운로드")
    public ModelAndView getExportExcelMissOutboundStaticsList(@RequestBody MissOutboundStaticsRequestDto dto) throws Exception {
        ExcelDownloadDto excelDownloadDto = outboundStaticsBiz.getExportExcelMissOutboundStaticsList(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
