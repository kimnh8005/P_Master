package kr.co.pulmuone.mall.promotion.advertising;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.mall.promotion.advertising.service.PromotionAdvertisingMallService;
import kr.co.pulmuone.v1.comm.enums.AdvertisingEnums;
import kr.co.pulmuone.v1.promotion.event.dto.EventListByUserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PromotionAdvertisingController {
    @Autowired
    public PromotionAdvertisingMallService promotionAdvertisingMallService;

    @GetMapping(value = "/ad/gateway")
    @ApiOperation(value = "제휴광고 gateway", httpMethod = "GET", notes = "제휴광고 gateway (코드 없음)")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public String gateway() throws Exception {
		return promotionAdvertisingMallService.getRedirectAdExternalUrl(null, null);
    }

    @GetMapping(value = "/ad/gateway/{pmAdExternalCd}")
    @ApiOperation(value = "제휴광고 gateway", httpMethod = "GET", notes = "제휴광고 gateway")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public String gateway(@PathVariable(value = "pmAdExternalCd") String pmAdExternalCd, HttpServletRequest request) throws Exception {

        if(AdvertisingEnums.AdvertCompany.LINK_PRICE.getCode().equals(pmAdExternalCd)) {
            String lpinfo = request.getParameter("lpinfo");
            return promotionAdvertisingMallService.getRedirectAdExternalUrlForLinkPrice(lpinfo);
        } else {
            return promotionAdvertisingMallService.getRedirectAdExternalUrl(pmAdExternalCd, null);
        }
    }

    @GetMapping(value = "/ad/gateway/{pmAdExternalCd}/{ilGoodsId}")
    @ApiOperation(value = "제휴광고 gateway", httpMethod = "GET", notes = "일반이벤트 조회 (상품 PK)")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public String gateway(@PathVariable(value = "pmAdExternalCd") String pmAdExternalCd, @PathVariable(value = "ilGoodsId") String ilGoodsId) throws Exception {
        return promotionAdvertisingMallService.getRedirectAdExternalUrl(pmAdExternalCd, ilGoodsId);
    }
}
