package kr.co.pulmuone.mall.promotion.linkprice;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.promotion.event.dto.EventListByUserResponseDto;
import kr.co.pulmuone.v1.promotion.linkprice.dto.LinkPriceOrderListAPIResponseDto;
import kr.co.pulmuone.v1.promotion.linkprice.service.LinkPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
public class LinkPriceApiController {

    @Autowired
    public LinkPriceService linkPriceService;

    @GetMapping(value = "/ad/linkprice/orderlist")
    @ApiOperation(value = "링크프라이스 주문내역 조회 API", httpMethod = "GET", notes = "링크프라이스 주문내역 조회 API")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public List<LinkPriceOrderListAPIResponseDto> orderlist(HttpServletRequest request) throws Exception {

        // String userAgent = request.getParameter("user-agent");
        String userAgent = "";  // 안쓰기로함.
        String paidYmd = request.getParameter("paid_ymd");
        String confirmedYmd = request.getParameter("confirmed_ymd");
        String canceledYmd = request.getParameter("canceled_ymd");

        String ymdType = "";
        String ymd = "";
        if(paidYmd != null && paidYmd.length() == 8) {
            ymdType = PromotionConstants.LP_SEARCH_TYPE_PAID;
            ymd = paidYmd;
        }
        if(confirmedYmd != null && confirmedYmd.length() == 8) {
            ymdType = PromotionConstants.LP_SEARCH_TYPE_CONFIRMED;
            ymd = confirmedYmd;
        }
        if(canceledYmd != null && canceledYmd.length() == 8) {
            ymdType = PromotionConstants.LP_SEARCH_TYPE_CANCELED;
            ymd = canceledYmd;
        }
//
//        if("mobile".equals(userAgent)) {
//            userAgent = PromotionConstants.LP_WEB_MOBILE;
//        } else {
//            userAgent = PromotionConstants.LP_WEB_PC;
//        }

        if(!"".equals(ymd) && ymd.length() == 8) {
            return linkPriceService.getLinkPriceOrderListAPIInfo(userAgent, ymdType, ymd);
        }

        return null;
    }
}
