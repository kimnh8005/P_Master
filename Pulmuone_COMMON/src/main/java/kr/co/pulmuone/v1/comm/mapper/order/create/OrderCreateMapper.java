package kr.co.pulmuone.v1.comm.mapper.order.create;

import java.util.List;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseVo;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderInfoDto;
import kr.co.pulmuone.v1.order.create.dto.PaymentInfoDto;
import kr.co.pulmuone.v1.order.create.dto.UserGroupInfoDto;
import kr.co.pulmuone.v1.order.create.dto.vo.CreateInfoVo;
import kr.co.pulmuone.v1.order.create.dto.CreateInfoDto;
import kr.co.pulmuone.v1.order.create.dto.GoodsInfoDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateListRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelRequestDto;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderCreateMapper {

    String getOrderNumber();

    long getOrderDetlSeq();

    long getOrderShippingPriceSeq();

    int addOrder(OrderVo orderVo);

    int addOrderDt(OrderDtVo orderDtVo);

    int addShippingZone(OrderShippingZoneVo orderShippingZoneVo);

    int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo);

    int addShippingPrice(OrderShippingPriceVo orderShippingPriceVo);

    int addOrderDetl(OrderDetlVo orderDetlVo);

    List<OrderExcelResponseDto> getExcelUploadList(OrderExcelRequestDto orderExcelRequestDto) throws Exception;

    GoodsInfoDto getGoodsInfo(long ilGoodsId) throws Exception;

    UserGroupInfoDto getUserGroupInfo(long urUserId) throws Exception;

    int addCreateInfo(CreateInfoVo createInfoVo) throws Exception;

    int putOrderInfo(OrderCreateRequestDto orderCreateRequestDto) throws Exception;

    Page<CreateInfoDto> getOrderCreateList(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    int deleteOrderCreateInfo(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    int deleteOrder(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    int deleteOrderDt(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    int deleteShippingZone(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    int deleteShippingZoneHist(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    int deleteShippingPrice(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    int deleteOrderDetl(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    OrderInfoDto getOrderInfo(OrderCardPayRequestDto OrderCardPayRequestDto) throws Exception;

    PaymentInfoDto getPaymentInfo(long odPaymentMasterId) throws Exception;

    int putPaymentMasterInfo(OrderPaymentMasterVo OrderPaymentMasterVo) throws Exception;

    List<String> selectOrderDetailDcList(@Param("odOrderId") long odOrderId);


    int addOrderDetailStatusHistory(OrderDetlHistVo orderDetlHistVo);

    int putOrderDetailStatus(OrderStatusUpdateDto orderStatusUpdateDto);

    int addOrderCopyTrackingNumber(@Param("odOrderDetlId") long odOrderDetlId, @Param("orderCopyOdid") String orderCopyOdid, @Param("createId") String createId) throws Exception;

    void putOrderFail(@Param("odOrderId") Long odOrderId);

    void putOrderSuccess(@Param("odPaymentMasterId") Long odPaymentMasterId);

    ItemWarehouseVo getIlItemWarehouseIdByIlGoodsId(Long ilGoodsId);
}
