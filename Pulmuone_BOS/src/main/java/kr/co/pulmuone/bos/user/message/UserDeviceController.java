package kr.co.pulmuone.bos.user.message;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.user.message.service.UserDeviceBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    2020. 6. 26.                jg          최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
public class UserDeviceController {

    @Autowired
    private UserDeviceBosService userDeviceBosService;

    @Autowired(required = true)
    private HttpServletRequest request;

    /**
     * APP 설치 단말기 목록 조회
     *
     * @param dto GetDeviceListRequestDto
     * @return GetDeviceListResponseDto
     * @throws Exception Exception
     */
    @PostMapping(value = "/admin/ur/userDevice/getDeviceList")
    @ApiOperation(value = "APP 설치 단말기 목록 조회", httpMethod = "POST", notes = "APP 설치 단말기 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetDeviceListResponseDto.class)
    })
    public ApiResult<?> getDeviceList(GetDeviceListRequestDto dto) throws Exception {
        dto = (GetDeviceListRequestDto) BindUtil.convertRequestToObject(request, GetDeviceListRequestDto.class);
        return userDeviceBosService.getDeviceList(dto);
    }

    /**
     * 푸시 가능 회원 조회
     *
     * @param dto GetBuyerDeviceListRequestDto
     * @return GetBuyerDeviceListResponseDto
     * @throws Exception Exception
     */
    @PostMapping(value = "/admin/ur/userDevice/getBuyerDeviceList")
    @ApiOperation(value = "푸시 가능 회원 조회", httpMethod = "POST", notes = "APP 설치 단말기 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetBuyerDeviceListResponseDto.class)
    })
    public ApiResult<?> getBuyerDeviceList(GetBuyerDeviceListRequestDto dto) throws Exception {
        dto = (GetBuyerDeviceListRequestDto) BindUtil.convertRequestToObject(request, GetBuyerDeviceListRequestDto.class);
        return userDeviceBosService.getBuyerDeviceList(dto);
    }


    /**
     * APP 플랫폼 유형 조회
     *
     * @param dto GetDeviceListRequestDto
     * @return GetDeviceListResponseDto
     * @throws Exception Exception
     */
    @PostMapping(value = "/admin/ur/userDevice/getDeviceEventImage")
	@ApiOperation(value = "APP 플랫폼 유형 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetDeviceListResponseDto.class)
	})
    public ApiResult<?> getDeviceEvnetImage(GetDeviceListRequestDto dto) throws Exception {
		return userDeviceBosService.getDeviceEvnetImage((GetDeviceListRequestDto) BindUtil.convertRequestToObject(request, GetDeviceListRequestDto.class));
    }



    /**
     * APP 이벤트 이미지 저장
     *   - post
     * @param DisplayBrandRequestDto
     * @return
     * @throws Exception
    */
    @PostMapping(value = "/admin/ur/userDevice/setDeviceEventImage")
    @ApiOperation(value = "APP 이벤트 이미지 저장")
    public ApiResult<?> setDeviceEventImage(GetDeviceRequestDto getDeviceRequestDto) throws Exception {
        return userDeviceBosService.setDeviceEventImage(getDeviceRequestDto);
    }
}