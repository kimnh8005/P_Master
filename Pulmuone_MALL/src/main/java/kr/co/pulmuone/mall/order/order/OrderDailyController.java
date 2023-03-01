package kr.co.pulmuone.mall.order.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneRequestDto;
import kr.co.pulmuone.v1.order.order.service.MallOrderDailyDetailBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 일일배송 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 16.     김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderDailyController {

    @Autowired
    private MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

    /**
     * 주문 상세 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "일일배송 변경 내역 조회")
    @PostMapping(value = "/order/daily/getOrderDailyShippingZoneHist")
    public ApiResult<?> getOrderDailyShippingZoneHist(@RequestParam(value = "odOrderId", required = true) String odOrderId
            ,@RequestParam(value = "odShippingZoneId", required = true) String odShippingZoneId
            ,@RequestParam(value = "odOrderDetlId", required = true) String odOrderDetlId) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        return mallOrderDailyDetailBiz.getOrderDailyShippingZoneHist(Long.parseLong(odOrderId), Long.parseLong(odShippingZoneId), Long.parseLong(odOrderDetlId));
    }

    /**
     * 일일배송 배송지 정보 수정
     * @param MallOrderDailyShippingZoneRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "일일배송 배송지 정보 수정")
    @PostMapping(value = "/order/daily/putOrderDailyShippingZone")
    public ApiResult<?> putOrderDailyShippingZone(MallOrderDailyShippingZoneRequestDto mallOrderDailyShippingZoneRequestDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }
        return mallOrderDailyDetailBiz.putOrderDailyShippingZone(mallOrderDailyShippingZoneRequestDto);
    }
}
