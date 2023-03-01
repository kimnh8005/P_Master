package kr.co.pulmuone.mall.app.setting;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.app.setting.AppSettingBiz;
import kr.co.pulmuone.v1.app.setting.dto.AppSettingResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AppSettingController {

    @Autowired
    private final AppSettingBiz appSettingBiz;

    @Autowired(required = true)
    private HttpServletRequest request;

    @GetMapping(value = "/app/setting/getSettingInfo")
    @ApiOperation(value = "앱 설정 화면 정보 조회", httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = 0000, message = "response data" , response = AppSettingResponseDto.class )})
    public ApiResult<?> getSettingInfo() throws Exception {
        return appSettingBiz.getSettingInfo(request);
    }

    @PostMapping(value = "/app/setting/updatePushAllowed")
    @ApiOperation(value = "푸시 사용 여부", httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 0000, message = "response data")})
    public ApiResult<?> updatePushAllowed(@RequestParam(value = "deviceId", required = true) String deviceId,
                                          @RequestParam(value = "pushAllowed", required = true) String pushAllowed) throws Exception {
        return appSettingBiz.updatePushAllowed(deviceId, pushAllowed);
    }

    @PostMapping(value = "/app/setting/updateNightAllowed")
    @ApiOperation(value = "푸시 야간 사용 여부", httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 0000, message = "response data")})
    public ApiResult<?> updateNightAllowed(@RequestParam(value = "deviceId", required = true) String deviceId,
                                           @RequestParam(value = "nightAllowed", required = true) String nightAllowed) throws Exception {
        return appSettingBiz.updateNightAllowed(deviceId, nightAllowed);
    }
}
