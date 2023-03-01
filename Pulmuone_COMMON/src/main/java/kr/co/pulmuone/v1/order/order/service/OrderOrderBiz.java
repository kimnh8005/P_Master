package kr.co.pulmuone.v1.order.order.service;

import java.time.LocalDate;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.PutOrderPaymentCompleteResDto;
import kr.co.pulmuone.v1.order.order.dto.VirtualAccountResponseDto;
import kr.co.pulmuone.v1.order.order.dto.putVirtualBankOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.pg.dto.EtcDataCartDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueResponseDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNotiRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisVirtualAccountReturnRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpVirtualAccountReturnRequestDto;

public interface OrderOrderBiz {

  /**
   * 상품구매 수량
   *
   * @param
   * @return int
   * @throws Exception
   */
  int getOrderGoodsBuyQty(Long ilGoodsId, String urUserId) throws Exception;

  List<ArrivalScheduledDateDto> removeDailyDeliveryLimitCnt(Long urWarehouseId,
                                                            List<ArrivalScheduledDateDto> scheduledDateList,
                                                            int limitCnt, boolean isDawnDelivery) throws Exception;

  int getOrderCountByUser(String start, String end) throws Exception;

  int getOrderPriceByUser(String start, String end) throws Exception;

  VirtualAccountResponseDto getVirtualAccount(String odid, String urUserId, String guestCi) throws Exception;

  int getOrderOdidCount(String odid, String urUserId, String guestCi) throws Exception;

  PgApprovalOrderDataDto getPgApprovalOrderDataByOdid(String odid) throws Exception;

  List<PgApprovalOrderDataDto> getPgApprovalOrderDataByOdPaymentMasterId(String odPaymentMasterId) throws Exception;

  PgApprovalOrderDataDto getPgApprovalOrderCreateDataByOdid(List<String> odIdList) throws Exception;

  void convertPgApprovalOrderDataDto(PgApprovalOrderDataDto dto, EtcDataCartDto etcDto) throws Exception;

  PgEnums.PgErrorType isPgApprovalOrderValidation (PgApprovalOrderDataDto orderData) throws Exception;

  PutOrderPaymentCompleteResDto putOrderPaymentComplete (PgApprovalOrderDataDto orderData, KcpApprovalResponseDto approvalResDto) throws Exception;

  PgApprovalOrderPaymentDataDto convertPgApprovalOrderPaymentData(String PaymentType, KcpApprovalResponseDto approvalResDto) throws Exception;

  PutOrderPaymentCompleteResDto putOrderPaymentComplete (PgApprovalOrderDataDto orderData, InicisApprovalResponseDto approvalResDto) throws Exception;

  PgApprovalOrderPaymentDataDto convertPgApprovalOrderPaymentData(String PaymentType, InicisApprovalResponseDto approvalResDto) throws Exception;

  PutOrderPaymentCompleteResDto putOrderPaymentComplete (PgApprovalOrderDataDto orderData, InicisMobileApprovalResponseDto approvalResDto) throws Exception;

  PgApprovalOrderPaymentDataDto convertPgApprovalOrderPaymentData(String PaymentType, InicisMobileApprovalResponseDto approvalResDto) throws Exception;

  PutOrderPaymentCompleteResDto putOrderPaymentComplete (PgApprovalOrderDataDto orderData, InicisNonAuthenticationCartPayRequestDto payReqDto, InicisNonAuthenticationCartPayResponseDto approvalResDto) throws Exception;

  PutOrderPaymentCompleteResDto putOrderPaymentComplete (PgApprovalOrderDataDto orderData, VirtualAccountDataResponseDto virtualAccountResDto) throws Exception;

  void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, putVirtualBankOrderPaymentDataDto reqDto) throws Exception;

  void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, KcpVirtualAccountReturnRequestDto reqDto) throws Exception;

  void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, InicisVirtualAccountReturnRequestDto reqDto) throws Exception;

  void putVirtualBankOrderPaymentComplete(PgApprovalOrderDataDto orderData, InicisNotiRequestDto paymentData) throws Exception;

  int putOrderDetailArrivalScheduledDate(OrderDetlVo orderDetlVo) throws Exception;
  /**
   * 재고 차감을 위한 주문 상품 정보 조회
   * @param odOrderId
   * @return
   */
  List<StockCheckOrderDetailDto> getStockCheckOrderDetailList(Long odOrderId) throws Exception;

  int putOrderDetailGoodsDeliveryType(Long odShippingZoneId, String goodsDeliveryType, String orderStatusDetailType) throws Exception;

  int putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(Long odOrderDetlId, String goodsDeliveryType, String orderStatusDetailType) throws Exception;

  /**
   * 폐기 임박 상품 스케줄 정보 조회
   *
   * @param odOrderDetlIds
   * @return
   * @throws Exception
   */
  List<ArrivalScheduledDateDto> getOrderDetailDisposalGoodsArrivalScheduledList(List<Long> odOrderDetlIds) throws Exception;

  List<StockCheckOrderDetailDto> getStockCheckOrderDetailListByOdOrderDetlId(Long odOrderDetlId) throws Exception;

  boolean isOrderDetailDailyDelivery(Long odOrderDetlId) throws Exception;

  CashReceiptIssuedListResponseDto getCashReceiptIssuedList(CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception;

  ApiResult<?> cashReceiptIssue(CashReceiptIssueRequestDto cashReceiptIssueRequestDto) throws Exception;

  ExcelDownloadDto getCashReceiptIssuedListExportExcel(CashReceiptIssuedListRequestDto dto) throws Exception;

  boolean isOdidValuePaymentMasterId(String odid) throws Exception;

  boolean isOdidValueAddPaymentMasterId(String odid) throws Exception;

  String getPaymentMasterIdByOdid(String odid) throws Exception;

  void putWarehouseDailyShippingCount(String odOrderIds) throws Exception;

  void putWarehouseDailyShippingCount(List<Long> odOrderIds) throws Exception;

  PaymentInfoResponseDto getPaymentInfo(String odid, String urUserId, String guestCi) throws Exception;
}
