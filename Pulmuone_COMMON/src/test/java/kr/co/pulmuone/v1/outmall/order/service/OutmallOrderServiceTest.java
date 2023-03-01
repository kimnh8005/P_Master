package kr.co.pulmuone.v1.outmall.order.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.outmall.order.dto.*;
import kr.co.pulmuone.v1.outmall.order.dto.vo.CollectionMallInterfaceFailVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelFailVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelSuccessVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OutmallOrderServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private OutmallOrderService outmallOrderService;

    @Test
    void getCollectionMallInterfaceList_조회_성공() {
        //given
        CollectionMallInterfaceListRequestDto dto = new CollectionMallInterfaceListRequestDto();
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2021-12-30");

        //when
        CollectionMallInterfaceListResponseDto result = outmallOrderService.getCollectionMallInterfaceList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void putCollectionMallInterfaceProgress_수정_성공() {
        //given
        String processCode = "T";
        Long adminId = 1L;
        Long ifEasyadminInfoId = 4L;

        //when, then
        outmallOrderService.putCollectionMallInterfaceProgress(processCode, adminId, ifEasyadminInfoId);
    }

    @Test
    void putCollectionMallInterfaceProgressList_수정_성공() {
        //given
        CollectionMallInterfaceProgressRequestDto dto = new CollectionMallInterfaceProgressRequestDto();
        dto.setIfEasyadminInfoIdList(Collections.singletonList(1L));

        //when, then
        outmallOrderService.putCollectionMallInterfaceProgressList(dto);
    }

    @Test
    void getCollectionMallFailExcelDownload_조회_성공() {
        //given
        CollectionMallInterfaceFailRequestDto dto = new CollectionMallInterfaceFailRequestDto();
        dto.setIfEasyadminInfoId(638L);
        dto.setFailType("B");

        //when
        List<CollectionMallInterfaceFailVo> result = outmallOrderService.getCollectionMallFailExcelDownload(dto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getClaimOrderList_조회_성공() {
        //given
        ClaimOrderListRequestDto dto = new ClaimOrderListRequestDto();
        dto.setSelectConditionType("singleSection");
        dto.setSingleSearchType("");

        //when
        ClaimOrderListResponseDto result = outmallOrderService.getClaimOrderList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getSellersList_조회_성공() {
        //given, when
        GetCodeListResponseDto result = outmallOrderService.getSellersList("");

        //then
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void putClaimOrderProgress_수정_성공() {
        //given
        ClaimOrderProgressRequestDto dto = new ClaimOrderProgressRequestDto();
        dto.setProcessCode("I");
        dto.setIfEasyadminOrderClaimId(1L);

        //when, then
        outmallOrderService.putClaimOrderProgress(dto);
    }

    @Test
    void putClaimOrderProgressList_수정_성공() {
        //given
        ClaimOrderProgressRequestDto dto = new ClaimOrderProgressRequestDto();
        dto.setIfEasyadminOrderClaimIdList(Collections.singletonList(1L));
        dto.setProcessCode("I");

        //when, then
        outmallOrderService.putClaimOrderProgressList(dto);
    }

    @Test
    void addOutMallExcelInfo_입력_성공() {
        //given
        OutMallExcelInfoVo vo = new OutMallExcelInfoVo();
        vo.setOutMallType(ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode());
        vo.setTotalCount(10);
        vo.setUploadStartDateTime("2021-01-18 15:26:00");
        vo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_IN_PROGRESS.getCode());
        vo.setCreateId(1);

        //when, then
        outmallOrderService.addOutMallExcelInfo(vo);
    }

    @Test
    void putOutMallExcelInfo_수정_성공() {
        //given
        OutMallExcelInfoVo vo = new OutMallExcelInfoVo();
        vo.setIfOutmallExcelInfoId(5L);
        vo.setSuccessCount(9);
        vo.setFailCount(1);
        vo.setUploadEndDateTime("2021-01-18 15:26:30");
        vo.setUploadExecutionTime(30);
        vo.setUploadStatusCode(ExcelUploadValidateEnums.UploadStatusCode.UPLOAD_DONE.getCode());

        //when, then
        outmallOrderService.putOutMallExcelInfo(vo);
    }

    @Test
    void addOutMallExcelSuccess_입력_성공() {
        //given
        Long ifOutmallExcelInfoId = 1L;
        List<OutMallExcelSuccessVo> voList = new ArrayList<>();
        OutMallExcelSuccessVo vo1 = new OutMallExcelSuccessVo();
        vo1.setCollectionMallId("1234");
        vo1.setCollectionMallDetailId("1234");
        vo1.setOmSellersId("12");
        vo1.setIlGoodsId(10L);
        vo1.setGoodsName("test");
        vo1.setOrderCount(1);
        vo1.setPaidPrice(1);
        vo1.setBuyerName("test");
        vo1.setBuyerTel("02-123-1234");
        vo1.setBuyerMobile("010-1234-5678");
        vo1.setReceiverName("test");
        vo1.setReceiverTel("02-123-1234");
        vo1.setReceiverMobile("010-1234-5678");
        vo1.setReceiverMail("test@test.co.kr");
        vo1.setReceiverZipCode("012345");
        vo1.setReceiverAddress1("test");
        vo1.setReceiverAddress2("test");
        vo1.setShippingPrice(1);
        vo1.setDeliveryMessage("test");
        vo1.setOutMallId("123");
        voList.add(vo1);
        OutMallExcelSuccessVo vo2 = new OutMallExcelSuccessVo();
        vo2.setCollectionMallId("1234");
        vo2.setCollectionMallDetailId("1234");
        vo2.setOmSellersId("12");
        vo2.setIlGoodsId(10L);
        vo2.setGoodsName("test");
        vo2.setOrderCount(1);
        vo2.setPaidPrice(1);
        vo2.setBuyerName("test");
        vo2.setBuyerTel("02-123-1234");
        vo2.setBuyerMobile("010-1234-5678");
        vo2.setReceiverName("test");
        vo2.setReceiverTel("02-123-1234");
        vo2.setReceiverMobile("010-1234-5678");
        vo2.setReceiverMail("test@test.co.kr");
        vo2.setReceiverZipCode("012345");
        vo2.setReceiverAddress1("test");
        vo2.setReceiverAddress2("test");
        vo2.setShippingPrice(1);
        vo2.setDeliveryMessage("test");
        vo2.setOutMallId("123");
        voList.add(vo2);

        //when, then
        outmallOrderService.addOutMallExcelSuccess(ifOutmallExcelInfoId, voList);
    }

    @Test
    void addOutMallExcelFail_입력_성공() {
        //given
        Long ifOutmallExcelInfoId = 1L;
        List<OutMallExcelFailVo> voList = new ArrayList<>();
        OutMallExcelFailVo vo1 = new OutMallExcelFailVo();
        vo1.setCollectionMallId("1234");
        vo1.setCollectionMallDetailId("1234");
        vo1.setOmSellersId("12");
        vo1.setIlGoodsId(10L);
        vo1.setGoodsName("test");
        vo1.setOrderCount(1);
        vo1.setPaidPrice(1);
        vo1.setBuyerName("test");
        vo1.setBuyerTel("02-123-1234");
        vo1.setBuyerMobile("010-1234-5678");
        vo1.setReceiverName("test");
        vo1.setReceiverTel("02-123-1234");
        vo1.setReceiverMobile("010-1234-5678");
        vo1.setReceiverMail("test@test.co.kr");
        vo1.setReceiverZipCode("012345");
        vo1.setReceiverAddress1("test");
        vo1.setReceiverAddress2("test");
        vo1.setShippingPrice(1);
        vo1.setDeliveryMessage("test");
        vo1.setOutMallId("123");
        vo1.setFailMessage("test");
        voList.add(vo1);

        OutMallExcelFailVo vo2 = new OutMallExcelFailVo();
        vo2.setCollectionMallId("1234");
        vo2.setCollectionMallDetailId("1234");
        vo2.setOmSellersId("12");
        vo2.setIlGoodsId(10L);
        vo2.setGoodsName("test");
        vo2.setOrderCount(1);
        vo2.setPaidPrice(1);
        vo2.setBuyerName("test");
        vo2.setBuyerTel("02-123-1234");
        vo2.setBuyerMobile("010-1234-5678");
        vo2.setReceiverName("test");
        vo2.setReceiverTel("02-123-1234");
        vo2.setReceiverMobile("010-1234-5678");
        vo2.setReceiverMail("test@test.co.kr");
        vo2.setReceiverZipCode("012345");
        vo2.setReceiverAddress1("test");
        vo2.setReceiverAddress2("test");
        vo2.setShippingPrice(1);
        vo2.setDeliveryMessage("test");
        vo2.setOutMallId("123");
        vo2.setFailMessage("test");
        voList.add(vo2);

        //when, then
        outmallOrderService.addOutMallExcelFail(ifOutmallExcelInfoId, voList);
    }

    @Test
    void getOutMallExcelDetailId_조회_성공() {
        //given
        List<String> detailIdList = Arrays.asList("264851", "264852");

        //when
        List<String> result = outmallOrderService.getOutMallExcelDetailId("E", detailIdList);

        //then
        assertTrue(result.size() > 0);
    }


    @Test
    void getOutMallExcelInfoList_조회_성공() {
        //given
        OutMallExcelListRequestDto dto = new OutMallExcelListRequestDto();
        dto.setCreateStartDate("2021-01-01");
        dto.setCreateEndDate("2021-08-30");

        //when
        OutMallExcelListResponseDto result = outmallOrderService.getOutMallExcelInfoList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getOutMallFailExcelDownload_조회_성공() {
        //given
        OutMallExcelFailRequestDto dto = new OutMallExcelFailRequestDto();
        dto.setIfOutmallExcelInfoId(1229L);
        dto.setFailType("B");

        //when
        List<OutMallExcelFailVo> result = outmallOrderService.getOutMallFailExcelDownload(dto);

        //then
        assertTrue(result.size() > 0);
    }

}