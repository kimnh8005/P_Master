package kr.co.pulmuone.mall.app.api;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.app.api.AppApiBiz;
import kr.co.pulmuone.v1.app.api.dto.AppversionResponseDto;
import kr.co.pulmuone.v1.app.api.dto.PushTokenRequestDto;
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
public class AppApiController {

    @Autowired
    private final AppApiBiz appApiBiz;

    @Autowired(required = true)
    private HttpServletRequest request;

    @GetMapping(value = "/app/api/getAppVer")
    @ApiOperation(value = "앱 최신 버전 조회")
    @ApiResponses(value = {@ApiResponse(code = 0000, message = "response data", response = AppversionResponseDto.class)})
    public ApiResult<?> appVersion() throws Exception {
        return appApiBiz.appVersion(request);
    }

    @PostMapping(value = "/app/api/setPushService")
    @ApiOperation(value = "푸시 토큰 저장", httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 0000, message = "response data")})
    public ApiResult<?> pushService(PushTokenRequestDto pushTokenRequestDto) throws Exception {
        return appApiBiz.pushService(pushTokenRequestDto, request);
    }


    @PostMapping(value = "/app/api/readPush")
    @ApiOperation(value = "푸시 알림 읽음 처리", httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(code = 0000, message = "response data")})
    public ApiResult<?> readPush(@RequestParam(value = "sequence", required = true) String sequence,
                                 @RequestParam(value = "deviceId", required = true) String deviceId) {
        return appApiBiz.readPush(sequence, deviceId);
    }

}
