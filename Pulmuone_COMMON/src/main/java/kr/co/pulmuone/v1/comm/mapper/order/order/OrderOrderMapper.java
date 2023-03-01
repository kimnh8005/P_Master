package kr.co.pulmuone.v1.comm.mapper.order.order;

import java.time.LocalDate;
import java.util.List;

import kr.co.pulmuone.v1.order.order.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.WareHouseDailyShippingVo;

@Mapper
public interface OrderOrderMapper {

  int getOrderGoodsBuyQty(@Param("ilGoodsId") Long ilGoodsId, @Param("urUserId") String urUserId) throws Exception;

  List<LocalDate> getOverDeliveryLimitCntDateList(@Param("urWarehouseId") Long urWarehouseId,
                                                  @Param("scheduledDateList") List<ArrivalScheduledDateDto> scheduledDateList,
                                                  @Param("limitCnt") int limitCnt, @Param("isDawnDelivery") boolean isDawnDelivery) throws Exception;

  int getOrderCountByUser(String start, String end) throws Exception;

  int getOrderPriceByUser(String start, String end) throws Exception;

  VirtualAccountResponseDto getVirtualAccount(@Param("odid") String odid, @Param("urUserId") String urUserId, @Param("guestCi") String guestCi) throws Exception;

  int getOrderOdidCount(@Param("odid") String odid, @Param("urUserId") String urUserId, @Param("guestCi") String guestCi) throws Exception;

  PgApprovalOrderDataDto getPgApprovalOrderDataByOdid(String odid) throws Exception;

  List<PgApprovalOrderDataDto> getPgApprovalOrderDataByOdPaymentMasterId(String odPaymentMasterId) throws Exception;

  PgApprovalOrderDataDto getPgApprovalOrderCreateDataByOdid(@Param("odIdList") List<String> odIdList) throws Exception;

  List<Long> getOrderUsePmCouponIssueIdList(Long odOrderId) throws Exception;

  List<StockCheckOrderDetailDto> getStockCheckOrderDetailList(Long odOrderId) throws Exception;

  int putOrderDetailArrivalScheduledDate(OrderDetlVo orderDetlVo) throws Exception;

  int putOrderDetailGoodsDeliveryType(@Param("odShippingZoneId")Long odShippingZoneId, @Param("goodsDeliveryType")String goodsDeliveryType, @Param("orderStatusDetailType") String orderStatusDetailType) throws Exception;

  int putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(@Param("odOrderDetlId")Long odOrderDetlId, @Param("goodsDeliveryType")String goodsDeliveryType, @Param("orderStatusDetailType") String orderStatusDetailType) throws Exception;

  List<ArrivalScheduledDateDto> getOrderDetailDisposalGoodsArrivalScheduledList(@Param("odOrderDetlIds") List<Long> odOrderDetlIds) throws Exception;

  List<StockCheckOrderDetailDto> getStockCheckOrderDetailListByOdOrderDetlId(Long odOrderId) throws Exception;

  boolean isOrderDetailDailyDelivery(Long odOrderDetlId) throws Exception;

  long getCashReceiptIssuedListCount(CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception;

  List<CashReceiptIssuedDto> getCashReceiptIssuedList(CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception;

  List<CashReceiptIssuedDto> getCashReceiptIssuedListExportExcel(CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception;

  List<WareHouseDailyShippingVo> getWarehouseAndShippingDateGroupList(@Param("odOrderIds") List<Long> odOrderIds) throws Exception;

  int getOrderWarehouseDailyShippingCount(WareHouseDailyShippingVo vo) throws Exception;

  void putWarehouseDailyShippingCount(WareHouseDailyShippingVo vo) throws Exception;

  List<PaymentGoodsDetailInfoResponseDto> getPaymentInfo(@Param("odid") String odid, @Param("urUserId") String urUserId, @Param("guestCi") String guestCi) throws Exception;
}
