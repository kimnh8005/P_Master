package kr.co.pulmuone.bos.statics.claim;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsResponseDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsResponseDto;
import kr.co.pulmuone.v1.statics.claim.service.ClaimStaticsBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
public class ClaimStaticsController {

    @Autowired
    private ClaimStaticsBiz claimStaticsBiz;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @RequestMapping(value = "/admin/statics/claim/getClaimStaticsList")
    @ApiOperation(value = "클레임 현황 통계", httpMethod = "POST", notes = "클레임 현황 통계")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ClaimStaticsResponseDto.class)
    })
    public ApiResult<?> getClaimStaticsList() throws Exception {
        ClaimStaticsRequestDto dto = BindUtil.bindDto(request, ClaimStaticsRequestDto.class);
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setBuyerTypeCdList(Stream.of(dto.getBuyerTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setGoodsTpCdList(Stream.of(dto.getGoodsTpCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        return claimStaticsBiz.getClaimStaticsList(dto);
    }

    @PostMapping(value = "/admin/statics/claim/getClaimStaticsExcelDownload")
    @ApiOperation(value = "클레임 현황 통계 엑셀 다운로드", httpMethod = "POST", notes = "클레임 현황 통계 엑셀 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getClaimStaticsExcelDownload(@RequestBody ClaimStaticsRequestDto dto) {
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setBuyerTypeCdList(Stream.of(dto.getBuyerTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setGoodsTpCdList(Stream.of(dto.getGoodsTpCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        ExcelDownloadDto excelDownloadDto = claimStaticsBiz.getClaimStaticsExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/statics/claim/getClaimReasonStaticsList")
    @ApiOperation(value = "클레임 사유별 현황 통계", httpMethod = "POST", notes = "클레임 사유별 현황 통계")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ClaimReasonStaticsResponseDto.class)
    })
    public ApiResult<?> getClaimReasonStaticsList() throws Exception {
        ClaimReasonStaticsRequestDto dto = BindUtil.bindDto(request, ClaimReasonStaticsRequestDto.class);
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setBuyerTypeCdList(Stream.of(dto.getBuyerTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setGoodsTpCdList(Stream.of(dto.getGoodsTpCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        return claimStaticsBiz.getClaimReasonStaticsList(dto);
    }

    @PostMapping(value = "/admin/statics/claim/getClaimReasonStaticsExcelDownload")
    @ApiOperation(value = "클레임 사유별 현황 통계 엑셀 다운로드", httpMethod = "POST", notes = "클레임 사유별 현황 통계 엑셀 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getClaimReasonStaticsExcelDownload(@RequestBody ClaimReasonStaticsRequestDto dto) {
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setBuyerTypeCdList(Stream.of(dto.getBuyerTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));
        dto.setGoodsTpCdList(Stream.of(dto.getGoodsTpCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        ExcelDownloadDto excelDownloadDto = claimStaticsBiz.getClaimReasonStaticsExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
