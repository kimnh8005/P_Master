package kr.co.pulmuone.v1.order.order.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.mapper.order.order.MallOrderDailyDetailMapper;
import kr.co.pulmuone.v1.comm.mapper.order.registration.OrderRegistrationSeqMapper;
import kr.co.pulmuone.v1.order.delivery.service.ShippingZoneBiz;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneHistListDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송주문상세 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 16.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class MallOrderDailyDetailService {

    private final MallOrderDailyDetailMapper mallOrderDailyDetailMapper;

    private final OrderRegistrationSeqMapper orderRegistrationSeqMapper;

    @Autowired
    OrderDetailBiz orderDetailBiz;

    @Autowired
    ShippingZoneBiz shippingZoneBiz;

    @Autowired
    MallOrderScheduleBiz mallOrderScheduleBiz;

    @Autowired
    MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

    @Autowired
    OrderScheduleBiz orderScheduleBiz;

    /**
     * 현재 일일배송 배송지 정보 조회
     * @param odOrderId
     * @return
     */
    protected MallOrderDailyShippingZoneHistListDto getOrderDailyShippingZone(long odOrderId, long odShippingZoneId) {
    	return mallOrderDailyDetailMapper.getOrderDailyShippingZone(odOrderId, odShippingZoneId);
    }

    /**
     * 일일배송 배송지 변경 이력 조회
     * @param odOrderId
     * @return
     */
    protected List<MallOrderDailyShippingZoneHistListDto> getOrderDailyShippingZoneHistList(long odOrderId, long odShippingZoneId) {
    	return mallOrderDailyDetailMapper.getOrderDailyShippingZoneHistList(odOrderId, odShippingZoneId);
    }

    /**
     * 주문 배송지
     * OD_SHIPPING_ZONE.OD_SHIPPING_ZONE_ID PK 조회
     * @return
     */
    protected long getOrderShippingZoneSeq(){
        return orderRegistrationSeqMapper.getOrderShippingZoneSeq();
    }

    /**
     * 일일배송 배송지 리스트 조회
     * @param orderDetailScheduleListRequestDto
     * @return
     */
    protected List<MallOrderDailyShippingZoneHistListDto> getOrderDailyShippingZoneList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto){
        return mallOrderDailyDetailMapper.getOrderDailyShippingZoneList(orderDetailScheduleListRequestDto);
    }

    /**
     * 주문상세PK로 일일상품 정보 조회
     * @param odOrderDetlId
     * @return MallOrderDetailGoodsDto
     */
    protected MallOrderDetailGoodsDto getOrderDailyDetailByOdOrderDetlId(Long odOrderDetlId){
        return mallOrderDailyDetailMapper.getOrderDailyDetailByOdOrderDetlId(odOrderDetlId);
    }

    /**
     * 주문배송지PK 정보로 배송타입 조회
     * @param odShippingZoneId
     * @return
     */
    protected String getOrderDailyDetailDeliveryTypeByOdShippingZoneId(long odShippingZoneId) {
        return mallOrderDailyDetailMapper.getOrderDailyDetailDeliveryTypeByOdShippingZoneId(odShippingZoneId);
    }

    /**
     * 일일상품 배송지 변경이력 count 조회
     * @param odOrderDetlId
     * @param promotionType
     * @return int
     */
    protected int getOrderDailyShippingZoneChangeCount(String promotionType, Long odOrderDetlId){
        return mallOrderDailyDetailMapper.getOrderDailyShippingZoneChangeCount(promotionType, odOrderDetlId);
    }

    /**
     * 주문PK로 같은 일일상품유형의 주문상세PK 조회
     * @param odOrderId
     * @param goodsDailyTp
     * @return MallOrderDetailGoodsDto
     */
    protected List<MallOrderDetailGoodsDto> getOrderDailyDetailByOdOrderId(Long odOrderId, String goodsDailyTp){
        return mallOrderDailyDetailMapper.getOrderDailyDetailByOdOrderId(odOrderId, goodsDailyTp);
    }

    /**
     * 주문상세PK로 주문건내 같은 일일상품유형의 상품PK 조회
     * @param odOrderDetlId
     * @return long
     */
    protected List<Long> getOrderGoodsIdListByOdOrderDetlId(@Param("odOrderDetlId")Long odOrderDetlId){
        return mallOrderDailyDetailMapper.getOrderGoodsIdListByOdOrderDetlId(odOrderDetlId);
    }

    /**
     * @Desc 주문 배송지 수정 위한 요청 파라미터 세팅
     * @param mallOrderDailyShippingZoneRequestDto
     * @return OrderShippingZoneVo
     */
    protected OrderShippingZoneVo setOrderShippingZoneVo(MallOrderDailyShippingZoneRequestDto mallOrderDailyShippingZoneRequestDto) {
        OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
                .odShippingZoneId(mallOrderDailyShippingZoneRequestDto.getOdShippingZoneId())
                .recvNm(mallOrderDailyShippingZoneRequestDto.getRecvNm())
                .recvHp(mallOrderDailyShippingZoneRequestDto.getRecvHp())
                .recvZipCd(mallOrderDailyShippingZoneRequestDto.getRecvZipCd())
                .recvAddr1(mallOrderDailyShippingZoneRequestDto.getRecvAddr1())
                .recvAddr2(mallOrderDailyShippingZoneRequestDto.getRecvAddr2())
                .recvBldNo(mallOrderDailyShippingZoneRequestDto.getRecvBldNo())
                .deliveryMsg(mallOrderDailyShippingZoneRequestDto.getDeliveryMsg())
                .doorMsgCd(mallOrderDailyShippingZoneRequestDto.getDoorMsgCd())
                .doorMsg(mallOrderDailyShippingZoneRequestDto.getDoorMsg())
                .urStoreId(Long.parseLong(mallOrderDailyShippingZoneRequestDto.getUrStoreId()))
                .build();

        return orderShippingZoneVo;
    }

    /**
     * 베이비밀, 잇슬림 배송지 변경
     * @param mallOrderDailyShippingZoneRequestDto
     * @param orderDetailGoodsDto
     * @return long
     */
    protected void putOrderDailyShippingZoneForBabymealAndEatsslim(MallOrderDailyShippingZoneRequestDto mallOrderDailyShippingZoneRequestDto, MallOrderDetailGoodsDto orderDetailGoodsDto) throws Exception{

        // 주문 배송지 수정 위한 요청 파라미터 세팅
        OrderShippingZoneVo orderShippingZoneVo = setOrderShippingZoneVo(mallOrderDailyShippingZoneRequestDto);

        // 같은 주문건에서 동일 브랜드 주소 모두 변경(베이비밀 일괄배송 제외)
        List<MallOrderDetailGoodsDto> orderDetailGoodsList = getOrderDailyDetailByOdOrderId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getGoodsDailyTp());
        for(MallOrderDetailGoodsDto dto : orderDetailGoodsList){
            orderShippingZoneVo.setOdShippingZoneId(dto.getOdShippingZoneId());
            orderDetailBiz.putShippingZone(orderShippingZoneVo);

            // OD_ORDER_DETL_DAILY 테이블의 주문상세번호로 스토어PK 업데이트 처리
            orderScheduleBiz.putOrderDetlDailyUrStoreId(dto.getOdOrderId(), 0, dto.getOdShippingZoneId());

            // 주문 배송 정보 조회
            OrderShippingZoneVo orderShipingZoneVo = shippingZoneBiz.getOrderShippingZone(dto.getOdShippingZoneId());

            // 주문 배송 정보 이력 등록
            OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
                    .odOrderId(orderShipingZoneVo.getOdOrderId())
                    .odShippingZoneId(dto.getOdShippingZoneId())
                    .deliveryType(orderShipingZoneVo.getDeliveryType())
                    .shippingType(orderShipingZoneVo.getShippingType())
                    .recvNm(mallOrderDailyShippingZoneRequestDto.getRecvNm())
                    .recvHp(mallOrderDailyShippingZoneRequestDto.getRecvHp())
                    .recvZipCd(mallOrderDailyShippingZoneRequestDto.getRecvZipCd())
                    .recvAddr1(mallOrderDailyShippingZoneRequestDto.getRecvAddr1())
                    .recvAddr2(mallOrderDailyShippingZoneRequestDto.getRecvAddr2())
                    .recvBldNo(mallOrderDailyShippingZoneRequestDto.getRecvBldNo())
                    .deliveryMsg(mallOrderDailyShippingZoneRequestDto.getDeliveryMsg())
                    .doorMsgCd(mallOrderDailyShippingZoneRequestDto.getDoorMsgCd())
                    .doorMsg(mallOrderDailyShippingZoneRequestDto.getDoorMsg())
                    .build();
            orderDetailBiz.addShippingZoneHist(orderShippingZoneHistVo);
        }
    }

    /**
     * 일일배송상품 도착일 정보 리스트 조회
     * @param deliveryDt
     * @param orderDetailGoodsDto
     * @return long
     */
    protected List<OrderDetailScheduleListDto> getOrderDetailScheduleArrivalDateList(String deliveryDt, MallOrderDetailGoodsDto orderDetailGoodsDto) throws Exception{
        List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList = new ArrayList<>();
        OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();

        // 1. 주문 녹즙 스케쥴 리스트 조회
        if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){
            // 1-1. 녹즙-내맘대로 인 경우 -> OD_ORDER_ID 세팅
            orderDetailScheduleListRequestDto.setOdOrderId(String.valueOf(orderDetailGoodsDto.getOdOrderId()));
            orderDetailScheduleListRequestDto.setPromotionYn("Y");
            orderDetailScheduleListRequestDto.setChangeDate(deliveryDt);

            orderDetailScheduleArrivalDateList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
        }else{

            // 1.2 - 녹즙인 경우 - > 같은 주문건에서 녹즙주문건 모두 조회
            List<MallOrderDetailGoodsDto> orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getGoodsDailyTp());

            for(MallOrderDetailGoodsDto dto : orderDetailGoodsList){
                // OD_ORDER_DETL_ID 세팅
                orderDetailScheduleListRequestDto.setOdOrderDetlId(dto.getOdOrderDetlId());
                orderDetailScheduleListRequestDto.setChangeDate(deliveryDt);
                List<OrderDetailScheduleListDto>  orderDetailList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
                orderDetailScheduleArrivalDateList.addAll(orderDetailList);
            }
        }

        return orderDetailScheduleArrivalDateList;
    }
    
}