package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > 통합몰 매출 대사 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 28.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class CalSalesBizImpl implements CalSalesBiz {

    @Autowired
    private CalSalesService calSalesService;

    /**
     * 통합몰 매출 대사 리스트 조회
     *
     * @param dto CalSalesListRequestDto
     * @return ApiResult<?>
     */
    @Override
    public ApiResult<?> getSalesList(CalSalesListRequestDto dto) {
        return ApiResult.success(calSalesService.getSalesList(dto));
    }

    @Override
    public ExcelDownloadDto getSalesListExportExcel(CalSalesListRequestDto dto) throws Exception {
        String excelFileName = "통합몰 매출 대사 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                200, 200, 300, 300, 200, 300, 300, 300, 300, 300,
                300, 300, 300, 300, 300, 200, 300, 300, 300, 300,
                300, 300, 300
        };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center"
        };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "erpSettleTypeNm", "erpOdid", "erpOdOrderDetlSeq", "erpSettleDt", "settleItemCnt", "settlePrice", "vatRemoveSettlePrice", "vat", "odid", "odOrderDetlSeq",
                "sellersNm", "warehouseNm", "settleDt", "ilItemCd", "itemBarcode", "goodsNm", "cnt", "orderStatusCdNm", "recommendedPrice", "directPrice",
                "directDiscountInfo", "salePrice", "paidPrice"
        };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "ERP 구분", "ERP 주문번호", "ERP 주문 상세번호", "ERP 정산처리일자", "ERP 수량", "ERP 매출금액 (VAT 포함)", "매출금액 (VAT 제외)", "VAT", "주문번호", "주문 상세번호",
                "판매처명", "출고처명", "정산처리일자", "품목코드", "품목바코드", "상품명", "수량", "주문상태", "정상가", "즉시 할인금액",
                "즉시할인유형", "판매가", "매출금액"
        };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        CalSalesListResponseDto excelInfo = calSalesService.getSalesExcelList(dto);
        firstWorkSheetDto.setExcelDataList(excelInfo.getRows());

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }
}