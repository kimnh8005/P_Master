package kr.co.pulmuone.v1.order.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.order.order.OrderOrderMapper;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailPayResultDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.WareHouseDailyShippingVo;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOrderService {

	private final OrderOrderMapper orderOrderMapper;

	@Autowired
	private MallOrderDetailService orderDetailService;

	/**
	 * 상품 구매수량
	 *
	 * @param ilGoodsId,urUserId
	 * @return Integer
	 * @throws Exception
	 */
	protected int getOrderGoodsBuyQty(Long ilGoodsId, String urUserId) throws Exception {
		return orderOrderMapper.getOrderGoodsBuyQty(ilGoodsId, urUserId);
	}

	/**
	 * 일별 출고 한도 체크 가능 출고일자 리턴
	 *
	 * @param urWarehouseId
	 * @param forwardingDateList
	 * @param limitCnt
	 * @return
	 * @throws Exception
	 */
	protected List<ArrivalScheduledDateDto> removeDailyDeliveryLimitCnt(Long urWarehouseId,
			List<ArrivalScheduledDateDto> scheduledDateList, int limitCnt, boolean isDawnDelivery) throws Exception {
		List<LocalDate> limitCntOrderDateList = orderOrderMapper.getOverDeliveryLimitCntDateList(urWarehouseId,
				scheduledDateList, limitCnt, isDawnDelivery);

		if (limitCntOrderDateList != null && limitCntOrderDateList.size() > 0) {
			scheduledDateList = scheduledDateList.stream()
					.filter(dto -> (limitCntOrderDateList.indexOf(dto.getForwardingScheduledDate()) < 0 ? true : false))
					.collect(Collectors.toList());
		}

		return scheduledDateList;
	}

	 /**
   * kk
   *
   * @param ilGoodsId,urUserId
   * @return Integer
   * @throws Exception
   */
  protected int getOrderCountByUser(String start,String end) throws Exception {
    return orderOrderMapper.getOrderCountByUser(start, end);
  }

  /**
   * kk
   *
   * @param ilGoodsId,urUserId
   * @return Integer
   * @throws Exception
   */
  protected int getOrderPriceByUser(String start, String end) throws Exception {
    return orderOrderMapper.getOrderPriceByUser(start, end);
  }

	/**
	 * 주문 완료 가상계좌 정보 조회
	 *
	 * @param	odid, urUserId, guestCi
	 * @return	virtualAccountResponseDto
	 * @throws	Exception
	 */
  protected VirtualAccountResponseDto getVirtualAccount(String odid, String urUserId, String guestCi) throws Exception{
	  return orderOrderMapper.getVirtualAccount(odid, urUserId, guestCi);
  }

	/**
	 * odid와 주문자정보 일치여부 확인
	 *
	 * @param	odid, urUserId, guestCi
	 * @throws	Exception
	 */
	protected int getOrderOdidCount(String odid, String urUserId, String guestCi) throws Exception{
		  return orderOrderMapper.getOrderOdidCount(odid, urUserId, guestCi);
	}

	/**
	 * odid 로 PG 승인을 위한 정보 조회
	 *
	 * @param odid
	 * @return
	 * @throws Exception
	 */
	protected PgApprovalOrderDataDto getPgApprovalOrderDataByOdid(String odid) throws Exception {
		return orderOrderMapper.getPgApprovalOrderDataByOdid(odid);
	}

	/**
	 * odPaymentMasterId 로 PG 승인을 위한 정보 조회
	 *
	 * @param odid
	 * @return
	 * @throws Exception
	 */
	protected List<PgApprovalOrderDataDto> getPgApprovalOrderDataByOdPaymentMasterId(String odPaymentMasterId) throws Exception {
		return orderOrderMapper.getPgApprovalOrderDataByOdPaymentMasterId(odPaymentMasterId);
	}

	/**
	 * odid 로 PG 승인을 위한 정보 조회 - 주문생성
	 *
	 * @param odid
	 * @return
	 * @throws Exception
	 */
	protected PgApprovalOrderDataDto getPgApprovalOrderCreateDataByOdid(List<String> odIdList) throws Exception {
		return orderOrderMapper.getPgApprovalOrderCreateDataByOdid(odIdList);
	}

	/**
	 * 주문에 사용된 PmCouponIssueId 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected List<Long> getOrderUsePmCouponIssueIdList(Long odOrderId) throws Exception {
		return orderOrderMapper.getOrderUsePmCouponIssueIdList(odOrderId);
	}

	/**
	 * 주문 상품 정보 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected List<StockCheckOrderDetailDto> getStockCheckOrderDetailList(Long odOrderId) throws Exception {
		return orderOrderMapper.getStockCheckOrderDetailList(odOrderId);
	}

	/**
	 * 주문 상품 도착예정일 변경
	 *
	 * @param orderDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderDetailArrivalScheduledDate(OrderDetlVo orderDetlVo) throws Exception {
		return orderOrderMapper.putOrderDetailArrivalScheduledDate(orderDetlVo);
	}

	/**
	 * 주문배송지PK로 주문상세 배송유형 변경
	 *
	 * @param odShippingZoneId
	 * @parma goodsDeliveryType
	 * @return
	 * @throws Exception
	 */
	protected int putOrderDetailGoodsDeliveryType(Long odShippingZoneId, String goodsDeliveryType, String orderStatusDetailType) throws Exception{
		return orderOrderMapper.putOrderDetailGoodsDeliveryType(odShippingZoneId, goodsDeliveryType, orderStatusDetailType);
	}

	/**
	 * 주문상세PK로 주문상세 배송유형 변경
	 *
	 * @param odOrderDetlId
	 * @parma goodsDeliveryType
	 * @return
	 * @throws Exception
	 */
	protected int putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(Long odOrderDetlId, String goodsDeliveryType, String orderStatusDetailType) throws Exception{
		return orderOrderMapper.putOrderDetailGoodsDeliveryTypeByOdOrderDetlId(odOrderDetlId, goodsDeliveryType, orderStatusDetailType);
	}

	protected List<ArrivalScheduledDateDto> getOrderDetailDisposalGoodsArrivalScheduledList(List<Long> odOrderDetlIds) throws Exception {
		return orderOrderMapper.getOrderDetailDisposalGoodsArrivalScheduledList(odOrderDetlIds);
	}

	/**
	 * 주문 상품 정보 조회
	 *
	 * @param odOrderId
	 * @return
	 * @throws Exception
	 */
	protected List<StockCheckOrderDetailDto> getStockCheckOrderDetailListByOdOrderDetlId(Long odOrderDetlId) throws Exception {
		return orderOrderMapper.getStockCheckOrderDetailListByOdOrderDetlId(odOrderDetlId);
	}

	protected boolean isOrderDetailDailyDelivery(Long odOrderDetlId) throws Exception {
		return orderOrderMapper.isOrderDetailDailyDelivery(odOrderDetlId);
	}

	/**
	 * 현금영수증 발급내역 조회
	 *
	 * @param cashReceiptIssuedListRequestDto
	 * @return CashReceiptIssuedListResponseDto
	 * @throws Exception
	 */
	protected CashReceiptIssuedListResponseDto getCashReceiptIssuedList(CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception{
		CashReceiptIssuedListResponseDto responseDto = new CashReceiptIssuedListResponseDto();

		long totalCount = orderOrderMapper.getCashReceiptIssuedListCount(cashReceiptIssuedListRequestDto);
		List<CashReceiptIssuedDto> cashReceiptIssuedDtoList = new ArrayList<>();
		if(totalCount > 0) {
			cashReceiptIssuedDtoList = orderOrderMapper.getCashReceiptIssuedList(cashReceiptIssuedListRequestDto);

			// 현금영수증 URL조회
			for(CashReceiptIssuedDto dto : cashReceiptIssuedDtoList){
				MallOrderDetailPayResultDto mallOrderDetailPayResultDto = orderDetailService.getOrderDetailPayInfo(dto.getOdOrderId());
				if(ObjectUtils.isNotEmpty(mallOrderDetailPayResultDto)){
					String billUrl = orderDetailService.getOrderBillUrl(mallOrderDetailPayResultDto);
					dto.setBillUrl(billUrl);
				}
			}
		}

		responseDto.setRows(cashReceiptIssuedDtoList);
		responseDto.setTotal(totalCount);

		return responseDto;
	}

	/**
	 * 현금영수증 발급 요청파라미터 세팅
	 *
	 * @param cashReceiptIssueRequestDto
	 * @param orderDto
	 * @return ReceiptIssueRequestDto
	 * @throws Exception
	 */
	protected ReceiptIssueRequestDto setReceiptIssueRequestDto(CashReceiptIssueRequestDto cashReceiptIssueRequestDto, MallOrderDto orderDto) {
		ReceiptIssueRequestDto receiptIssueRequestDto = new ReceiptIssueRequestDto();
		receiptIssueRequestDto.setOdid(orderDto.getOdid());
		receiptIssueRequestDto.setGoodsName(orderDto.getGoodsNm());
		receiptIssueRequestDto.setReceiptType(OrderEnums.CashReceipt.findByCode(cashReceiptIssueRequestDto.getReceiptType()));
		receiptIssueRequestDto.setRegNumber(cashReceiptIssueRequestDto.getRegNumber());
		receiptIssueRequestDto.setTotalPrice(cashReceiptIssueRequestDto.getPaymentPrice());
		// 공급가액 = ROUND(과세 금액 / 1.1)
		int supplyAmount = Math.round(Float.valueOf(cashReceiptIssueRequestDto.getTaxablePrice()) / new Float(1.1));
		receiptIssueRequestDto.setSupPrice(supplyAmount);
		//부가세 = 과세금액 - 공급가액
		receiptIssueRequestDto.setTax(cashReceiptIssueRequestDto.getTaxablePrice() - supplyAmount);
		receiptIssueRequestDto.setSrcvPrice(0);	//봉사료
		receiptIssueRequestDto.setBuyerName(orderDto.getBuyerNm());
		receiptIssueRequestDto.setBuyerEmail(orderDto.getBuyerMail());
		receiptIssueRequestDto.setBuyerMobile(orderDto.getBuyerHp());

		return receiptIssueRequestDto;
	}

	/**
	 * 현금영수증 발급내역 엑셀 다운로드 목록 조회
	 *
	 * @param cashReceiptIssuedListRequestDto
	 * @return ExcelDownloadDto
	 * @throws Exception
	 */
	protected ExcelDownloadDto getCashReceiptIssuedListExportExcel(CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto) throws Exception{
		String excelFileName = "현금영수증 발급내역 엑셀다운로드";
		String excelSheetName = "sheet";
		/* 화면값보다 20더 하면맞다. */
		Integer[] widthListOfFirstWorksheet = { 100, 100, 100, 110, 120, 120, 110, 110, 80, 80, 80, 100, 100 };

		String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center" };

		String[] propertyListOfFirstWorksheet = { "rnum", "cashReceiptType", "createDt", "odid", "payTp", "pgService", "statusNm",
				"taxablePrice", "nonTaxablePrice", "paymentPrice", "cashReceiptNo", "cashReceiptAuthNo" };

		String[] firstHeaderListOfFirstWorksheet = { "No", "구분", "주문일자", "주문번호", "결제수단", "PG구분", "결제상태", "과세", "면세", "발급금액",
				"증빙번호", "승인번호" };

		String[] cellTypeListOfFirstWorksheet = {	"String", "String", "String", "String", "String", "String", "String", "int", "int", "int",
				"String", "String"};

		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()
				.workSheetName(excelSheetName)
				.propertyList(propertyListOfFirstWorksheet)
				.widthList(widthListOfFirstWorksheet)
				.alignList(alignListOfFirstWorksheet)
				.cellTypeList(cellTypeListOfFirstWorksheet)
				.build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		List<CashReceiptIssuedDto> cashReceiptIssuedDtoList = orderOrderMapper.getCashReceiptIssuedListExportExcel(cashReceiptIssuedListRequestDto);

		firstWorkSheetDto.setExcelDataList(cashReceiptIssuedDtoList);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	protected boolean isOdidValuePaymentMasterId(String odid) throws Exception {
		if (odid == null) {
			return false;
		}
		return odid.indexOf(Constants.ORDER_ODID_DIV_PAYMENT_MASTER) < 0 ? false : true;
	}

	protected boolean isOdidValueAddPaymentMasterId(String odid) throws Exception {
		if (odid == null) {
			return false;
		}
		return odid.indexOf(Constants.ORDER_ODID_DIV_ADD_PAYMENT_MASTER) < 0 ? false : true;
	}

	protected String getPaymentMasterIdByOdid(String odid) throws Exception {
		if (odid == null) {
			return "";
		}
		return odid.replace(Constants.ORDER_ODID_DIV_PAYMENT_MASTER, "");
	}

	protected void putWarehouseDailyShippingCount(List<Long> odOrderIds) throws Exception {
		if (odOrderIds != null && !odOrderIds.isEmpty()) {
			List<WareHouseDailyShippingVo> list = orderOrderMapper.getWarehouseAndShippingDateGroupList(odOrderIds);
			if (list != null && !list.isEmpty()) {
				for(WareHouseDailyShippingVo vo : list) {
					vo.setShippingCount(orderOrderMapper.getOrderWarehouseDailyShippingCount(vo));
					orderOrderMapper.putWarehouseDailyShippingCount(vo);
					if ("Y".equals(vo.getDawnDeliveryYn())) {
						// 새벽배송 아닌 주문건 업데이트
						vo.setDawnDeliveryYn("N");
						vo.setShippingCount(orderOrderMapper.getOrderWarehouseDailyShippingCount(vo));
						orderOrderMapper.putWarehouseDailyShippingCount(vo);
					}
				}
			}
		}
	}

	/**
	 * 주문 완료 결제 정보 조회
	 *
	 * @param	odid, urUserId, guestCi
	 * @return	virtualAccountResponseDto
	 * @throws	Exception
	 *
	 */
	protected PaymentInfoResponseDto getPaymentInfo(String odid, String urUserId, String guestCi) throws Exception{
		List<PaymentGoodsDetailInfoResponseDto> list = orderOrderMapper.getPaymentInfo(odid, urUserId, guestCi);

		PaymentInfoResponseDto paymentInfoResponseDto = new PaymentInfoResponseDto();

		// 결제 완료 상품이 존재 하는 경우에만 실행
		if(!CollectionUtils.isEmpty(list)) {
			paymentInfoResponseDto.setOdid(odid);
			paymentInfoResponseDto.setTotalPrice(list.stream().mapToInt(x -> x.getPaidPrice()).sum()); // 총 결제 금액
			paymentInfoResponseDto.setTotalOrderCnt(list.stream().mapToInt(x -> x.getOrderCnt()).sum()); // 총 결제 상품 수
			paymentInfoResponseDto.setPaymentGoodsList(list);
		}
		return paymentInfoResponseDto;
	}
}
