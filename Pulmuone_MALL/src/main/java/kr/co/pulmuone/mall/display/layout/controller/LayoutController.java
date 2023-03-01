package kr.co.pulmuone.mall.display.layout.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.category.dto.GetMallDataResponseDto;
import kr.co.pulmuone.mall.display.layout.dto.CommonDataDto;
import kr.co.pulmuone.mall.display.layout.service.LayoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class LayoutController {

    @Autowired
    LayoutService layoutService;

    /**
     * 레이아웃 공통 데이타 조회
     * @return
     */
    @GetMapping(value = "/display/layout/getCommonData")
    @ApiOperation(value = "레이아웃 공통 데이타 조회", httpMethod = "GET", notes = "레이아웃 공통 데이타 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = CommonDataDto.class)
    })
    public ApiResult<?> getCommonData() throws Exception {
        return layoutService.getCommonData();
    }


    /**
     * 레이아웃 몰별 데이타 조회
     * @param mallDiv
     * @return
     */
    @GetMapping(value = "/display/layout/getMallData/{mallDiv}")
    @ApiOperation(value = "레이아웃 몰별 데이타 조회", httpMethod = "GET", notes = "레이아웃 몰별 데이타 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetMallDataResponseDto.class),
            @ApiResponse(code = 901, message = "" + "MALL_CATEGORY_DATA_EMPTY - 몰 카테고리 데이터가 없습니다. \n" )
    })
    public ApiResult<?> getMallData(@PathVariable(value = "mallDiv") String mallDiv) throws Exception {

        return layoutService.getMallData(mallDiv);
    }


    /**
     * 레이아웃 관련 정보(PC)
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/display/layout/getLayoutInfoPc")
    public ApiResult<?> getLayoutInfoPc(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return layoutService.getLayoutInfoPc(request, response);
    }


    /**
     * 레이아웃 관련 정보(Mobile)
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/display/layout/getLayoutInfoMobile")
    public ApiResult<?> getLayoutInfoMobile(HttpServletRequest request, HttpServletResponse response, String autoLoginKey) throws Exception {
        return layoutService.getLayoutInfoMobie(request, response, autoLoginKey);

    }



}
