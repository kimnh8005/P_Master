package kr.co.pulmuone.mall.order.delivery;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.DeliveryEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.delivery.service.MallOrderDeliveryBiz;
import kr.co.pulmuone.v1.order.delivery.service.ShippingZoneBiz;
import kr.co.pulmuone.v1.order.order.dto.mall.MallArriveDateListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallArriveDateListResponseDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallArriveDateUpdateRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.outmall.order.dto.CollectionMallInterfaceListRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 배송 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 18.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class OrderDeliveryController {

    @Autowired
    private MallOrderDeliveryBiz mallOrderDeliveryBiz;	// Mall 주문 배송 관련 Biz

    @Autowired
    private ShippingZoneBiz shippingZoneBiz;	// Mall 주문 배송지 정보 수정

    @Autowired
    private HttpServletRequest request;

    /**
     * 도착예정일 변경일자 조회
     * @param mallArriveDateListRequestDto
     * @return mallArriveDateListRequestDto
     * @throws Exception
     */
    @ApiOperation(value = "도착예정일 변경일자 조회")
    @PostMapping(value = "/order/delivery/getArriveDateList")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = MallArriveDateListResponseDto.class),
			@ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") })
    public ApiResult<?> getArriveDateList(MallArriveDateListRequestDto mallArriveDateListRequestDto) throws Exception {
        // 로그인 체크
    	// 비회원도 도착예정일 변경 가능 -> 주석처리
//        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//        }
    	return mallOrderDeliveryBiz.getArriveDateList(mallArriveDateListRequestDto);
    }

    /**
     * 도착예정일 변경
     * @param mallArriveDateUpdateRequestDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "도착 예정일 변경")
    @PostMapping(value = "/order/delivery/putArriveDate")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") })
    public ApiResult<?> putArriveDate(MallArriveDateUpdateRequestDto mallArriveDateUpdateRequestDto) throws Exception {

    	// 로그인 체크
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        Long createId = Constants.GUEST_CREATE_USER_ID;
        if (StringUtil.isNotEmpty(buyerVo.getUrUserId())) {
        	createId = Long.parseLong(buyerVo.getUrUserId());
        }

        try{
            return mallOrderDeliveryBiz.putArriveDate(mallArriveDateUpdateRequestDto, createId);
        }catch(BaseException be){
            return ApiResult.result(be.getMessageEnum());
        }catch (Exception e) {
            e.printStackTrace();
            return ApiResult.result(DeliveryEnums.ChangeArriveDateValidation.FAIL);
        }
    }

    /**
     * 배송지정보 변경
     * @param orderShippingZoneVo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "배송지정보 변경")
    @PostMapping(value = "/order/delivery/putShippingZone")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
            @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n") })
    public ApiResult<?> putShippingZone(OrderShippingZoneVo orderShippingZoneVo) throws Exception {
        // 로그인 체크
    	// 비회원도 배송지 정보 변경 가능 -> 주석처리
//        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
//        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
//            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
//        }
        return shippingZoneBiz.putShippingZone(orderShippingZoneVo);
    }





}