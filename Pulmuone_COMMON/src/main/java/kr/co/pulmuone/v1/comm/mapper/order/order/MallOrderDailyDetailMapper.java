package kr.co.pulmuone.v1.comm.mapper.order.order;

import java.util.List;

import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneHistListDto;

/**
 * <PRE>
 * Forbiz Korea
 * 프론트 일일배송주문상세 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 16.    김명진           최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface MallOrderDailyDetailMapper {

    /**
     * 현재 일일배송 배송지 정보 조회
     * @param odOrderId
     * @return
     */
	MallOrderDailyShippingZoneHistListDto getOrderDailyShippingZone(@Param(value = "odOrderId") long odOrderId, @Param(value = "odShippingZoneId") long odShippingZoneId);

    /**
     * 일일배송 배송지 변경 이력 조회
     * @param odOrderId
     * @return
     */
	List<MallOrderDailyShippingZoneHistListDto> getOrderDailyShippingZoneHistList(@Param(value = "odOrderId") long odOrderId, @Param(value = "odShippingZoneId") long odShippingZoneId);

    /**
     * 일일배송 배송지 리스트 조회
     * @param orderDetailScheduleListRequestDto
     * @return
     */
    List<MallOrderDailyShippingZoneHistListDto> getOrderDailyShippingZoneList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto);

    /**
     * 주문상세PK로 일일상품 정보 조회
     * @param odOrderDetlId
     * @return MallOrderDetailGoodsDto
     */
    MallOrderDetailGoodsDto getOrderDailyDetailByOdOrderDetlId(Long odOrderDetlId);

    /**
     * 주문배송지PK 정보로 배송타입 조회
     * @param odShippingZoneId
     * @return
     */
    String getOrderDailyDetailDeliveryTypeByOdShippingZoneId(long odShippingZoneId);

    /**
     * 일일상품 배송지 변경이력 count 조회
     * @param odOrderDetlId
     * @parma promotionType
     * @return int
     */
    int getOrderDailyShippingZoneChangeCount(@Param(value ="promotionType")String promotionType, @Param(value="odOrderDetlId")Long odOrderDetlId);

    /**
     * 주문PK로 같은 일일상품유형의 주문상세PK 조회
     * @param odOrderId
     * @param goodsDailyTp
     * @return MallOrderDetailGoodsDto
     */
    List<MallOrderDetailGoodsDto> getOrderDailyDetailByOdOrderId(@Param("odOrderId")Long odOrderId, @Param("goodsDailyTp")String goodsDailyTp);

    /**
     * 주문상세PK로 주문건내 같은 일일상품유형의 상품PK 조회
     * @param odOrderDetlId
     * @return long
     */
    List<Long> getOrderGoodsIdListByOdOrderDetlId(@Param("odOrderDetlId")Long odOrderDetlId);
}