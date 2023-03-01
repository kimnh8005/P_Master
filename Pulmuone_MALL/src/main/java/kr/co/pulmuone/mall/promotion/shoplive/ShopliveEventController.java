package kr.co.pulmuone.mall.promotion.shoplive;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.mall.promotion.shoplive.service.ShopliveEventMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopliveEventController {

    @Autowired
    ShopliveEventMallService shopliveEventMallService;

    @PostMapping(value = "/promotion/event/getShopliveInfo")
    @ApiOperation(value = "샵라이브 정보 조회", httpMethod = "POST", notes = "샵라이브 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ShopliveInfoResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NO_EVENT] NO_EVENT - 정상적인 이벤트 아님 \n" +
                    "[ONLY_EMPLOYEE] ONLY_EMPLOYEE - 임직원 전용이벤트 \n" +
                    "[PASS_VALIDATION] PASS_VALIDATION - 통과"
            )
    })
    public ApiResult<?> getShopliveInfo(ShopliveInfoRequestDto dto) throws Exception {
        return shopliveEventMallService.getShopliveInfo(dto);
    }

}
