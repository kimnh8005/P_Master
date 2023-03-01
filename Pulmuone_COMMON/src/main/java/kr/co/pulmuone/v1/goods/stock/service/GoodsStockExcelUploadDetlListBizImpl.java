package kr.co.pulmuone.v1.goods.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadDetlListResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 재고 엑셀 업로드 상세내역 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.11.23  이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsStockExcelUploadDetlListBizImpl implements GoodsStockExcelUploadDetlListBiz {

	@Autowired
    GoodsStockExcelUploadDetlListService goodsStockExcelUploadDetlListService;

	/**
	 * ERP 재고 엑셀 업로드 상세내역 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockUploadDetlList(StockExcelUploadDetlListRequestDto dto) throws Exception {
		StockExcelUploadDetlListResponseDto responseDto = new StockExcelUploadDetlListResponseDto();

		Page<StockExcelUploadDetlListResultVo> stockDetlList = goodsStockExcelUploadDetlListService.getStockUploadDetlList(dto);

		responseDto.setTotal(stockDetlList.getTotal());
		responseDto.setRows(stockDetlList.getResult());

        return ApiResult.success(responseDto);

	}

	/**
	 * ERP 재고 엑셀 업로드 상세내역 엑셀 다운로드
	 * @param	StockExcelUploadDetlListRequestDto
	 * @return	List<StockExcelUploadDetlListResultVo>
	 * @throws Exception
	 */
	@Override
	public ExcelDownloadDto getStockUploadDetlExcel(StockExcelUploadDetlListRequestDto dto) {
		 String excelFileName = "item_stockUploadDetl"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                180, 370, 150, 150, 150, 150};

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "left", "center", "center", "center", "center"};

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "ilItemCd", "itemNm", "expirationDt", "stockQty", "successYn", "createDt"};

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "마스터 품목코드", "마스터 품목명", "유통기한", "수량", "성공여부", "등록일시"};

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
	        List<StockExcelUploadDetlListResultVo> orgaDisList = goodsStockExcelUploadDetlListService.getStockUploadDetlList(dto);

	        firstWorkSheetDto.setExcelDataList(orgaDisList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;
	}


}
