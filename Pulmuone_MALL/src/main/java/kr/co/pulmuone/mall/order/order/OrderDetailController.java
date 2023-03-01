package kr.co.pulmuone.mall.order.order;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.mall.MallApplyPaymentDirectOrderRequestDto;
import kr.co.pulmuone.v1.order.order.service.MallOrderDetailBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.    세        이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderDetailController {

    @Autowired
    private MallOrderDetailBiz mallOrderDetailBiz;

    /**
     * 주문 상세 조회
     * @param odOrderId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "주문 상세 조회")
    @PostMapping(value = "/order/getOrderDetail")
    public ApiResult<?> getOrderDetail(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }


        return mallOrderDetailBiz.getOrderDetail(Long.parseLong(odOrderId));
    }


    /**
     * 클레임 상세 조회
     * @param odClaimId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "주문 클레임 상세 조회")
    @PostMapping(value = "/order/getClaimDetail")
    public ApiResult<?> getClaimDetail(@RequestParam(value = "odClaimId", required = true) String odClaimId) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        return mallOrderDetailBiz.getClaimDetail(Long.parseLong(odClaimId));
    }

    /**
     * 주문 상담원 결제 상세
     * @param odOrderId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "주문 상담원 결제 상세")
    @PostMapping(value = "/order/getDirectOrderDetail")
    public ApiResult<?> getDirectOrderDetail(@RequestParam(value = "odOrderId", required = true) String odOrderId) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        return mallOrderDetailBiz.getDirectOrderDetail(Long.parseLong(odOrderId));
    }

    /**
     * 주문 상담원 결제 요청
     * @param odOrderId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "주문 상담원 결제 요청")
    @PostMapping(value = "/order/applyPaymentDirectOrder")
    public ApiResult<?> applyPaymentDirectOrder(MallApplyPaymentDirectOrderRequestDto reqDto) throws Exception {
        // 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId()) && StringUtil.isEmpty(buyerVo.getNonMemberCiCd())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        return mallOrderDetailBiz.applyPaymentDirectOrder(reqDto);
    }
}
