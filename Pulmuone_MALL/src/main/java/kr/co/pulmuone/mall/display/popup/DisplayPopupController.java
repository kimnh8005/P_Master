package kr.co.pulmuone.mall.display.popup;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.display.contents.dto.InventoryContentsInfoResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetPopupInfoMallResultVo;
import kr.co.pulmuone.mall.display.popup.service.DisplayPopupMallService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20201123  	      천혜현           최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class DisplayPopupController {

	@Autowired
    private DisplayPopupMallService displayPopupMallService;

    /**
     * 공지 팝업 조회(Mobile)
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @GetMapping(value = "/display/popup/getPopupInfoMobile")
    @ApiOperation(value = "공지 팝업 조회(Mobile)", httpMethod = "GET", notes = "공지 팝업 조회(Mobile)")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetPopupInfoMallResultVo.class)
    })
    public ApiResult<?> getPopupInfoMobile(HttpServletRequest request) throws Exception {
        return displayPopupMallService.getPopupInfoMobile(request);
    }

    /**
     * 공지 팝업 조회(pc)
     *
     * @return ApiResult<?>
     * @throws Exception exception
     */
    @GetMapping(value = "/display/popup/getPopupInfoPc")
    @ApiOperation(value = "공지 팝업 조회(pc)", httpMethod = "GET", notes = "공지 팝업 조회(pc)")
    @ApiResponses(value = {
           @ApiResponse(code = 900, message = "response data", response = GetPopupInfoMallResultVo.class)
    })
    public ApiResult<?> getPopupInfoPc(HttpServletRequest request) throws Exception {
        return displayPopupMallService.getPopupInfoPc(request);
    }


}
