package kr.co.pulmuone.bos.statics.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.statics.user.dto.*;
import kr.co.pulmuone.v1.statics.user.service.UserStaticsBiz;
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
public class UserStaticsController {

    @Autowired
    private UserStaticsBiz userStaticsBiz;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    @RequestMapping(value = "/admin/statics/user/getUserTypeStaticsList")
    @ApiOperation(value = "회원 유형별 판매/매출 현황 통계", httpMethod = "POST", notes = "회원 유형별 판매/매출 현황 통계")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserTypeStaticsResponseDto.class)
    })
    public ApiResult<?> getUserTypeStaticsList() throws Exception {
        UserTypeStaticsRequestDto dto = BindUtil.bindDto(request, UserTypeStaticsRequestDto.class);
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        return userStaticsBiz.getUserTypeStaticsList(dto);
    }

    @PostMapping(value = "/admin/statics/user/getUserTypeStaticsExcelDownload")
    @ApiOperation(value = "회원 유형별 판매/매출 현황 통계 엑셀 다운로드", httpMethod = "POST", notes = "회원 유형별 판매/매출 현황 통계 엑셀 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getUserTypeStaticsExcelDownload(@RequestBody UserTypeStaticsRequestDto dto) {
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        ExcelDownloadDto excelDownloadDto = userStaticsBiz.getUserTypeStaticsExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/statics/user/getUserGroupStaticsList")
    @ApiOperation(value = "일반 회원 등급별 판매현황 통계", httpMethod = "POST", notes = "일반 회원 등급별 판매현황 통계")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserGroupStaticsResponseDto.class)
    })
    public ApiResult<?> getUserGroupStaticsList() throws Exception {
        UserGroupStaticsRequestDto dto = BindUtil.bindDto(request, UserGroupStaticsRequestDto.class);
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        return userStaticsBiz.getUserGroupStaticsList(dto);
    }

    @PostMapping(value = "/admin/statics/user/getUserGroupStaticsExcelDownload")
    @ApiOperation(value = "일반 회원 등급별 판매현황 통계 - 엑셀 다운로드", httpMethod = "POST", notes = "일반 회원 등급별 판매현황 통계 - 엑셀 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getUserGroupStaticsExcelDownload(@RequestBody UserGroupStaticsRequestDto dto) {
        dto.setAgentTypeCdList(Stream.of(dto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
                .collect(Collectors.toList()));

        ExcelDownloadDto excelDownloadDto = userStaticsBiz.getUserGroupStaticsExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/statics/user/getUserCountStaticsList")
    @ApiOperation(value = "회원 보유현황 통계", httpMethod = "POST", notes = "회원 보유현황 통계")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserCountStaticsResponseDto.class)
    })
    public ApiResult<?> getUserCountStaticsList() throws Exception {
        UserCountStaticsRequestDto dto = BindUtil.bindDto(request, UserCountStaticsRequestDto.class);
        return userStaticsBiz.getUserCountStaticsList(dto);
    }

    @PostMapping(value = "/admin/statics/user/getUserCountStaticsExcelDownload")
    @ApiOperation(value = "회원 보유 현황 통계 엑셀 다운로드", httpMethod = "POST", notes = "회원 보유 현황 통계 엑셀 다운로드")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ModelAndView.class)
    })
    public ModelAndView getUserCountStaticsExcelDownload(@RequestBody UserCountStaticsRequestDto dto) {
        ExcelDownloadDto excelDownloadDto = userStaticsBiz.getUserCountStaticsExcelDownload(dto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;
    }

}
