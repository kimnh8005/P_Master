package kr.co.pulmuone.bos.order.status;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.order.status.dto.OrderClaimStatusRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusSelectRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.            이명수         최초작성
 *  1.1    2021. 01. 11.            김명진         파라미터 수정
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
public class OrderStatusController {

    @Autowired
    private OrderStatusBiz orderStatusBiz;


    /**
     * 주문상태 수정
     * @param odOrderDetlId
     * @param orderStatusCd
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "상품 주문상태 조회")
    @PostMapping(value = "/admin/order/status/getOrderDetailStatusInfo")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> getOrderDetailStatusInfo(@RequestParam(value = "detlIdList[]", required = true) List<Long> detlIdList) throws Exception {
    	OrderStatusSelectRequestDto orderStatusSelectRequestDto = OrderStatusSelectRequestDto.builder()
    			.detlIdList(detlIdList)
    			.build();
    	return orderStatusBiz.getOrderDetailStatusInfo(orderStatusSelectRequestDto);
    }

    /**
     * 주문상태 수정
     * @param odOrderDetlId
     * @param orderStatusCd
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "주문상태 수정")
    @PostMapping(value = "/admin/order/status/putOrderDetailStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> putOrderDetailStatus(
            @RequestParam(value = "detlIdList[]", required = true) List<Long> detlIdList,
            @RequestParam(value = "psShippingCompIdList[]", required = false) List<Long> shippingCompIdList,
            @RequestParam(value = "trackingNoList[]", required = false) List<String> trackingNoList,
            @RequestParam(value = "statusCd", required = true) String statusCd) throws Exception {

        UserVo userVo = SessionUtil.getBosUserVO();

        OrderStatusUpdateRequestDto orderStatusUpdateRequestDto = OrderStatusUpdateRequestDto.builder()
                .userId(Long.parseLong(userVo.getUserId()))
                .loginName(userVo.getLoginName())
                .detlIdList(detlIdList)
                .shippingCompIdList(shippingCompIdList)
                .trackingNoList(trackingNoList)
                .statusCd(statusCd)
                .build();

    	return orderStatusBiz.putOrderDetailListStatus(orderStatusUpdateRequestDto);
    }

    /**
     * 취소 요청 상태 완료로 일괄 변경
     * @param orderClaimStatusRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @ApiOperation(value = "취소 요청 상태 완료로 일괄 변경")
    @PostMapping(value = "/admin/order/status/putClaimDetailStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> putClaimDetailStatus(@RequestBody OrderClaimStatusRequestDto orderClaimStatusRequestDto) throws Exception {
        log.debug("------------------------------------취소 요청 -> 완료 일괄 변경 START");
    	return ApiResult.success(orderStatusBiz.putClaimCancelReqeustToCancelComplete(orderClaimStatusRequestDto));
    }
}
