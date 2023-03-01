package kr.co.pulmuone.v1.promotion.linkprice.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.promotion.linkprice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;

import java.util.List;

/**
 * <PRE>
 * 풀무원
 * LinkPrice Service
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0       2021.07.19.           최용호         최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkPriceBizImpl implements LinkPriceBiz {

    @Autowired
    private LinkPriceService linkPriceService;

    @Override
    public ApiResult<?> insertLinkPriceByOrder (LinkPriceDto linkPriceDto) throws Exception {
        return linkPriceService.insertLinkPriceByOrder(linkPriceDto);
    }

    /**
     * 링크프라이스 내역조회
     */
    @Override
    public ApiResult<?>  getLinkPriceList(LinkPriceRequestDto dto) throws Exception {
        Page<LinkPriceResultVo> voList = linkPriceService.getLinkPriceList(dto);

        LinkPriceResponseDto result = new LinkPriceResponseDto();
        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());
        return ApiResult.success(result);
    }

    /**
     * 링크프라이스 내역조회 엑셀 다운로드
     */
    @Override
    public ExcelDownloadDto getLinkPriceListExcel(LinkPriceRequestDto dto) throws Exception {

        String excelFileName = "링크프라이스 내역조회 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        ExcelWorkSheetDto firstWorkSheetDto = null;

        if(Constants.BOS_EXCEL_DOWNLOAD_TYPE_DETAIL.equals(dto.getDownType())) {
            excelFileName = "링크프라이스 상세내역조회 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
            /*
             * 컬럼별 width 목록 : 단위 pixel
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
             */
            Integer[] widthListOfFirstWorksheet = { //
                    80, 100, 150, 100, 100, 500, 80, 80, 80, 80, 100, 80, 100, 100 };

            /*
             * 본문 데이터 컬럼별 정렬 목록
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
             * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
             */
            String[] alignListOfFirstWorksheet = {
                    "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center" };

            /*
             * 본문 데이터 컬럼별 데이터 property 목록
             * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
             */
            String[] propertyListOfFirstWorksheet = {
                    "rowNumber", "ordNo", "lpinfo", "paidDt", "ilGoodsId", "goodsNm", "isEmpDiscount", "isNotDlv" , "isNotBrand", "orderCnt", "ordTotalPrice", "claimCnt", "cnclTotalPrice", "ordPointPrice" };

            // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
            String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "No", "주문번호", "LPINFO", "결제일자", "상품코드", "상품명", "임직원할인여부", "정산불가지역여부", "정산불가브랜드여부", "주문수량", "주문금액", "A/S수량", "A/S금액", "사용적립금" };

            // 워크시트 DTO 생성 후 정보 세팅
            firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                    .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                    .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                    .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                    .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                    .build();

            // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
            firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼
        } else {
            /*
             * 컬럼별 width 목록 : 단위 pixel
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
             */
            Integer[] widthListOfFirstWorksheet = { //
                    80, 100, 150, 100, 100, 100, 100 };

            /*
             * 본문 데이터 컬럼별 정렬 목록
             * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
             * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
             */
            String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center" };

            /*
             * 본문 데이터 컬럼별 데이터 property 목록
             * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
             */
            String[] propertyListOfFirstWorksheet = {
                    "rowNumber", "ordNo", "lpinfo", "paidDt", "ordTotalPrice", "cnclTotalPrice", "ordPointPrice" };

            // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
            String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                    "No", "주문번호", "LPINFO", "결제일자", "주문금액", "A/S금액", "사용적립금" };

            // 워크시트 DTO 생성 후 정보 세팅
            firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                    .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                    .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                    .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                    .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                    .build();

            // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
            firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼
        }

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<LinkPriceExcelResultVo> itemList = linkPriceService.getLinkPriceListExcel(dto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    /**
     * 링크프라이스 내역조회 Total data 조회
     */
    @Override
    public ApiResult<?>  getLinkPriceListTotal(LinkPriceRequestDto dto) throws Exception {
        return ApiResult.success(linkPriceService.getLinkPriceListTotal(dto));
    }

    /**
     * 링크프라이 실적 결제정보 저장 및 전송
     */
    @Override
    public void saveAndSendLinkPrice(PgApprovalOrderDataDto orderData) throws Exception {
        linkPriceService.saveAndSendLinkPrice(orderData);
    }

    /**
     * 가상계좌 실적 결제정보 저장
     */
    @Override
    public void virtualBankSaveLinkPrice(PgApprovalOrderDataDto orderData) throws Exception {
        linkPriceService.virtualBankSaveLinkPrice(orderData);
    }

    /**
     * 가상계좌 실적 결제정보 전송
     */
    @Override
    public void virtualBankSendLinkPrice(PgApprovalOrderDataDto orderData) throws Exception {
        linkPriceService.virtualBankSendLinkPrice(orderData);
    }
}
