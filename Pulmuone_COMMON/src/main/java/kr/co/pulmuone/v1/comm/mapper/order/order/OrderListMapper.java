package kr.co.pulmuone.v1.comm.mapper.order.order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.order.order.dto.*;
import org.apache.ibatis.annotations.Mapper;


/**
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1    2020. 12. 15.            석세동         수정
 *  1.2    2021. 01. 11.            김명진         엑셀다운로드 추가
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderListMapper {

    /**
     * @Desc 주문 목록 조회
     * @param orderListRequestDto
     * @return Page<OrderVo>
     */
    long getOrderListCount(OrderListRequestDto orderListRequestDto);

    List<OrderListDto> getOrderList(OrderListRequestDto orderListRequestDto);

    List<OrderExcelListDto> getOrderExcelList(OrderListRequestDto orderListRequestDto);

    long getOrderDetailListCount(OrderListRequestDto orderListRequestDto);

    OrderDetailTotalCountDto getOrderTotalCountInfo(OrderListRequestDto orderListRequestDto);

    List<OrderDetailListDto> getOrderDetailList(OrderListRequestDto orderListRequestDto);

    List<OrderDetailExcelListDto> getOrderDetailExcelList(OrderListRequestDto orderListRequestDto);

    List<LinkedHashMap<String, Object>> getOrderDetailExcelListMap(OrderListRequestDto orderListRequestDto);

    Page<OrderDetailListDto> getPayCompleteOrderDetailList(OrderListRequestDto orderListRequestDto);

    Page<OrderDetailListDto> getCancelReqOrderDetailList(OrderListRequestDto orderListRequestDto);

    Page<OrderDetailListDto> getDeliveryReadyOrderDetailList(OrderListRequestDto orderListRequestDto);

    Page<OrderDetailListDto> getDeliveryIngOrderDetailList(OrderListRequestDto orderListRequestDto);

    Page<OrderDetailListDto> getUnreleasedOrderDetailList(OrderListRequestDto orderListRequestDto);

    Page<OrderDetailListDto> getReturnOrderDetailList(OrderListRequestDto orderListRequestDto);

    Page<OrderDetailListDto> getRefundOrderDetailList(OrderListRequestDto orderListRequestDto);

	/**
	 * 반품 사유 코드 목록 조회
	 * @param
	 * @return List<GetCodeListResultVo>
	 */
	List<GetCodeListResultVo> getReturnReasonList();

    /**
     * CS환불 리스트 총 건수 조회
     * @param csRefundRequest
     * @return
     */
    OrderCSRefundTotalInfoDto getCSRefundListCount(OrderListRequestDto csRefundRequest);

    /**
     * CS환불 리스트 조회
     * @param csRefundRequest
     * @return
     */
    List<OrderCSRefundListDto> getCSRefundList(OrderListRequestDto csRefundRequest);
}
