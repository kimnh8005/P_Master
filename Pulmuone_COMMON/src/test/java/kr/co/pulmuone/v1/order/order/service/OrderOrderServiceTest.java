package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.order.order.dto.CashReceiptIssueRequestDto;
import kr.co.pulmuone.v1.order.order.dto.CashReceiptIssuedListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptIssueRequestDto;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderOrderServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderOrderService orderOrderService;

    @Test
    void 상품구매수량_조회_정상() throws Exception {

        Long ilGoodsId = 1189L;
        String urUserId = "100";

        Assertions.assertTrue(orderOrderService.getOrderGoodsBuyQty(ilGoodsId, urUserId) >= 0);
    }

    @Test
    void getVirtualAccount_조회결과없음() throws Exception{
    	String odid = "123456";
    	String urUserId = "123456";
    	String guestCi = null;

    	Assertions.assertNull(orderOrderService.getVirtualAccount(odid, urUserId, guestCi));
    }

//    @Test
//    void getVirtualAccount_성공() throws Exception{
//    	// 현재 OD_PAYMENT 테이블의 데이터 없음
//    	// 추후 수정 예정
//    }

    @Test
    void getOrderOdidCount_성공() throws Exception{
    	String odid = "2020121412345551";
    	String urUserId = "1646893";
    	String guestCi = null;

    	Assertions.assertTrue(orderOrderService.getOrderOdidCount(odid, urUserId, guestCi) > 0);
    }

    @Test
    void getOrderOdidCount_조회결과없음() throws Exception{
    	String odid = "2020123456";
    	String urUserId = "123456";
    	String guestCi = null;

    	Assertions.assertFalse(orderOrderService.getOrderOdidCount(odid, urUserId, guestCi) > 0);
    }

    @Test
    void isOrderDetailDailyDelivery_성공() throws Exception{
    	Long odOrderDetlId = 0L;
    	Assertions.assertFalse(orderOrderService.isOrderDetailDailyDelivery(odOrderDetlId));
    }

    @Test
    void getCashReceiptIssuedList_성공() throws Exception{
        CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto = new CashReceiptIssuedListRequestDto();
        cashReceiptIssuedListRequestDto.setCashReceiptType(OrderEnums.CashReceipt.USER.getCode());

        Assertions.assertTrue(orderOrderService.getCashReceiptIssuedList(cashReceiptIssuedListRequestDto).getTotal() > 0);
    }

    @Test
    void getCashReceiptIssuedList_조회결과없음() throws Exception{
        CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto = new CashReceiptIssuedListRequestDto();
        cashReceiptIssuedListRequestDto.setSearchType("odid");
        cashReceiptIssuedListRequestDto.setSearchWord("0");

        Assertions.assertFalse(orderOrderService.getCashReceiptIssuedList(cashReceiptIssuedListRequestDto).getTotal() > 0);
    }

    @Test
    void setReceiptIssueRequestDto_성공() throws Exception{
        CashReceiptIssueRequestDto cashReceiptIssueRequestDto = new CashReceiptIssueRequestDto();
        cashReceiptIssueRequestDto.setReceiptType(OrderEnums.CashReceipt.USER.getCode());
        cashReceiptIssueRequestDto.setRegNumber("0000");
        cashReceiptIssueRequestDto.setPaymentPrice(1000);
        cashReceiptIssueRequestDto.setTaxablePrice(900);

        MallOrderDto orderDto = new MallOrderDto();
        orderDto.setOdid("1000000");
        orderDto.setGoodsNm("테스트");
        orderDto.setBuyerHp("01000000000");
        orderDto.setBuyerMail("test@test.com");
        orderDto.setBuyerNm("테스트");

        ReceiptIssueRequestDto reqDto = orderOrderService.setReceiptIssueRequestDto(cashReceiptIssueRequestDto, orderDto);
        Assertions.assertTrue(ObjectUtils.isNotEmpty(reqDto));
    }

    @Test
    void getCashReceiptIssuedListExportExcel_성공() throws Exception{
        CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto = new CashReceiptIssuedListRequestDto();
        cashReceiptIssuedListRequestDto.setCashReceiptType(OrderEnums.CashReceipt.USER.getCode());

        ExcelDownloadDto responseDto = orderOrderService.getCashReceiptIssuedListExportExcel(cashReceiptIssuedListRequestDto);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(responseDto));
    }

    @Test
    void getCashReceiptIssuedListExportExcel_조회결과없음() throws Exception{
        CashReceiptIssuedListRequestDto cashReceiptIssuedListRequestDto = new CashReceiptIssuedListRequestDto();
        cashReceiptIssuedListRequestDto.setSearchType("odid");
        cashReceiptIssuedListRequestDto.setSearchWord("0");

        ExcelDownloadDto responseDto = orderOrderService.getCashReceiptIssuedListExportExcel(cashReceiptIssuedListRequestDto);

        Assertions.assertFalse(responseDto.getExcelWorkSheetList().get(0).getExcelDataList().size() > 0);
    }
}