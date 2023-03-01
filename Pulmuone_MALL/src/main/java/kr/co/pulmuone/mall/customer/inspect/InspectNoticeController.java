package kr.co.pulmuone.mall.customer.inspect;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.inspect.dto.InspectNoticeResponseDto;
import kr.co.pulmuone.v1.customer.inspect.service.InspectNoticeBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InspectNoticeController {

    @Autowired
    InspectNoticeBiz inspectNoticeBiz;

    /**
     * 정기정검 조회
     * @param InspectNoticeRequestDto
     * @return InspectNoticeResponseDto
     * @throws Exception
     */
    @ApiOperation(value = "정기정검 조회")
    @GetMapping(value = "/customer/inspect/getInspectNotice")
    @ApiResponse(code = 900, message = "response data", response = InspectNoticeResponseDto.class)
    public ApiResult<?> getInspectNotice() throws Exception {
        return inspectNoticeBiz.getInspectNotice();
    }

}
