package kr.co.pulmuone.mall.customer.stndpnt;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.mall.customer.stndpnt.service.StandingPointMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StandingPointController {

    @Autowired
    private StandingPointMallService standingPointMallService;

    @PostMapping(value = "/customer/stndpnt/addStandingPoint")
    @ApiOperation(value = "상품입점상담 등록")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : null")
    })
    public ApiResult<?> addStandingPointQna(StandingPointMallRequestDto dto) throws Exception {
        return standingPointMallService.addStandingPointQna(dto);
    }

}
