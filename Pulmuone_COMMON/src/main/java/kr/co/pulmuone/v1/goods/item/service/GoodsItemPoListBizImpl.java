package kr.co.pulmuone.v1.goods.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListRequestVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoListResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * 발주리스트 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2021.02.05   이성준
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsItemPoListBizImpl implements GoodsItemPoListBiz {

	@Autowired
	GoodsItemPoListService goodsItemPoListService;

	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> getPoList(ItemPoListRequestDto dto) {
		ItemPoListResponseDto result = new ItemPoListResponseDto();
		Page<ItemPoListResultVo> rows = goodsItemPoListService.getPoList(dto);

		result.setTotal(rows.getTotal());
		result.setRows(rows.getResult());

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getPoInfoList(ItemPoListRequestDto dto) {
		ItemPoListResponseDto result = new ItemPoListResponseDto();

	    List<ItemPoListResultVo> rows = goodsItemPoListService.getPoTypeInfoList(dto);

	    ItemPoListResultVo saveRows = goodsItemPoListService.getSavedPoInfo(dto);

	    if(saveRows != null) {
	       rows.add(saveRows);
	    }else {
	       ItemPoListResultVo vo = new ItemPoListResultVo();
	       vo.setSumPoUserQty(0);
	       vo.setSumBoxPoQty(0);
	       vo.setStrManager("");
	       rows.add(vo);
	    }

		result.setRows(rows);

		return ApiResult.success(result);
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> getPoResultList(ItemPoListRequestDto dto) {
		//dto.setSearchPoIfYn("Y"); // 연동된 발주목록만 조회하기 위해 설정
		dto.setPoResultYn("Y");		// 발주내역조회시 필요값.
		return this.getPoList(dto);
	}

	/**
	 * @Desc 발주유형 목록조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	@Override
	public ApiResult<?> getPoTpList(ItemPoListRequestDto dto) {
		ItemPoListResponseDto result = new ItemPoListResponseDto();

		List<ItemPoListResultVo> rows = goodsItemPoListService.getPoTpList(dto);// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
	 * @Desc 발주유형 목록조회(onChange)
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	@Override
	public ApiResult<?> getOnChangePoTpList(ItemPoListRequestDto dto) {
		ItemPoListResponseDto result = new ItemPoListResponseDto();

		List<ItemPoListResultVo> rows = goodsItemPoListService.getOnChangePoTpList(dto);// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
	 * @Desc ERP 카테고리 (대분류) 목록조회
	 * @param ItemPoListRequestDto
	 * @return List<ItemPoListResultVo>
	 */
	@Override
	public ApiResult<?> getErpCtgryList(ItemPoListRequestDto dto) {
		ItemPoListResponseDto result = new ItemPoListResponseDto();

		List<ItemPoListResultVo> rows = goodsItemPoListService.getErpCtgryList(dto);// rows

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
     * @Desc 발주수량 변경
     * @param ItemPoListRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putItemPo(ItemPoListRequestDto dto) throws Exception{

    	int size = dto.getPoList().size();

    	ItemPoListRequestVo vo = new ItemPoListRequestVo();

		for(int i=0; i < size; i++) {

			String str = dto.getPoList().get(i);

			String[] arry = str.split("-");

			vo.setIlPoId(arry[0]);
			vo.setPiecePoQty(arry[1]);
			vo.setMemo(arry[2].trim());
			vo.setCreateId((SessionUtil.getBosUserVO()).getUserId());

			if (goodsItemPoListService.putItemPo(vo) > 0) {
				goodsItemPoListService.addItemPoSavedLog(vo);
			}
		}

        return ApiResult.success();
    }

    /**
	 * 발주 리스트 엑셀 다운로드(풀무원식품)
	 * @param	ItemPoListRequestDto
	 * @return	List<ItemPoListResultVo>
	 */
	@Override
	public ExcelDownloadDto getPfPoListExportExcel(ItemPoListRequestDto dto) {
		 String excelFileName = "item_pfPoList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                150, 150, 150, 500, 150, 150, 150, 170, 200, 150
	                , 170, 160, 160, 160, 160, 180, 180, 180, 180, 150
					, 150, 150, 200, 200, 200, 200, 150, 150, 150, 150
					, 150, 150, 150, 150, 150, 150, 150
	        };

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center"
	        };

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "poTpTemplateNm", "ilItemCd", "barcode", "itemNm", "storageMethodNm", "distributionPeriod", "pcsPerBox", "saleStatusNm", "goodsOutmallSaleStatNm", "targetStock"
	                , "recommendPoQty", "piecePoQty", "boxPoQty", "eventPoQty", "stockClosed", "stockDiscardD0", "stockDiscardD1", "stockConfirmed", "stockScheduledD0", "stockScheduledD1More"
					, "expectedResidualQty", "outbound0", "outbound1", "outbound2", "outbound3More", "outboundDayAvg", "missedOutbound", "memo", "outbound3weekTotal", "outbound2weekTotal"
					, "outbound1weekTotal", "excelManager", "ctgryStdNm", "erpCtgryLv1Id", "ilGoodsId", "dispStr", "stockScheduledDt"
	        };

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "발주유형", "품목코드", "품목바코드", "마스터품목명", "보관방법", "유통기간", "박스 입수량", "상품 판매상태", "외부몰 판매상태", "안전재고(목표재고)"
	                , "권고수량", "낱개 발주수량", "BOX 발주수량", "행사 발주수량", "전일 마감재고", "폐기 예정수량 D0", "폐기 예정수량 D1", "입고확정", "입고예정", "입고 대기수량"
					, "예상 잔여수량", "D-0", "D+1", "D+2", "D+3이상", "일평균 출고량", "결품 예상일", "발주메모", "직전 3주 출고수량", "직전 2주 출고수량"
					, "직전 1주 출고수량", "관리자", "표준카테고리(대분류)", "ERP카테고리(대분류)", "상품코드", "상품 전시상태", "입고 예정일자"
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
	        List<ItemPoListResultVo> poList = goodsItemPoListService.getPoList(dto);

	        firstWorkSheetDto.setExcelDataList(poList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;
	}

	/**
	 * 발주 리스트 엑셀 다운로드(올가)
	 * @param	ItemPoListRequestDto
	 * @return	List<ItemPoListResultVo>
	 */
	@Override
	public ExcelDownloadDto getOgPoListExportExcel(ItemPoListRequestDto dto) {
		 String excelFileName = "item_OgPoList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = {
	                150, 150, 200, 150, 150, 500, 150, 150, 150, 150
	                , 150, 150, 150, 150, 150, 150, 150, 150, 150, 150
	                , 150, 150, 150, 150, 200, 150, 150, 150, 150, 150
	                , 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150
	        };

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = {
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"
	        };

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "poTpTemplateNm", "erpCtgryLv1Id", "erpEvent", "ilItemCd", "barcode", "itemNm", "dispStr", "saleStatusNm", "goodsOutmallSaleStatNm", "poProRea"
	                , "recommendPoQty", "piecePoQty", "eventPoQty", "stockScheduledDt", "stockClosed", "stockConfirmed", "stockScheduledD0", "stockScheduledD1More","expectedResidualQty", "offStock"
	                , "outbound0", "outbound1", "outbound2", "outbound3More", "memo", "erpEventOrderAvg", "nonErpEventOrderAvg", "excelManager", "ctgryStdNm", "ilGoodsId"
	                , "storageMethodNm","distributionPeriod", "pcsPerBox", "targetStock", "stockDiscardD0", "stockDiscardD1", "outboundDayAvg", "missedOutbound", "outbound3weekTotal", "outbound2weekTotal", "outbound1weekTotal"
	        };

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "발주유형", "ERP카테고리(대분류)", "행사정보(올가전용)", "품목코드", "품목바코드", "마스터품목명", "상품 전시상태", "상품 판매상태", "외부몰 판매상태", "off발주상태(올가전용)"
	                , "권고수량", "낱개 발주수량", "행사 발주수량", "입고 예정일자", "전일 마감재고", "입고확정", "입고예정", "입고 대기수량", "예상 잔여수량", "off재고(올가전용)"
	                , "D-0", "D+1", "D+2", "D+3이상", "발주메모", "프로모션 판매평균", "일반 판매평균", "관리자", "표준카테고리(대분류)", "상품코드"
	                , "보관방법", "유통기간", "박스입수량", "안전재고(목표재고)", "폐기 예정수량 D0", "폐기 예정수량 D1", "일평균 출고량", "결품 예상일", "직전 3주 출고수량", "직전 2주 출고수량", "직전 1주 출고수량"
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
	        List<ItemPoListResultVo> poList = goodsItemPoListService.getPoList(dto);

	        firstWorkSheetDto.setExcelDataList(poList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;
	}

	/**
	 * 발주 내역 엑셀 다운로드(풀무원식품)
	 * @param	ItemPoListRequestDto
	 * @return	List<ItemPoListResultVo>
	 */
	@Override
	public ExcelDownloadDto getPfPoResultListExportExcel(ItemPoListRequestDto dto) {
		 String excelFileName = "item_pfPoResultList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                150, 150, 150, 500, 150, 150, 150, 170, 200, 150
	                , 170, 160, 160, 160, 160, 180, 180, 180, 180, 180
					, 150, 150, 150, 200, 200, 200, 200, 150, 150, 150
					, 150, 150, 150, 150, 150, 150, 150, 150, 150
	        };

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center"
	        };

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "poTpTemplateNm", "ilItemCd", "barcode", "itemNm", "storageMethodNm", "distributionPeriod", "pcsPerBox", "saleStatusNm", "goodsOutmallSaleStatNm", "targetStock"
	                , "recommendPoQty", "piecePoQty", "poSystemQty", "boxPoQty", "eventPoQty", "poIfYn", "excelManager", "stockClosed", "stockDiscardD0", "stockDiscardD1", "stockConfirmed"
					, "stockScheduledD0", "stockScheduledD1More", "expectedResidualQty", "outbound0", "outbound1", "outbound2", "outbound3More", "outboundDayAvg", "missedOutbound", "memo"
					, "outbound3weekTotal", "outbound2weekTotal", "outbound1weekTotal", "ctgryStdNm", "erpCtgryLv1Id", "ilGoodsId", "dispStr", "stockScheduledDt"
	        };

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "발주유형", "품목코드", "품목바코드", "마스터품목명", "보관방법", "유통기간", "박스 입수량", "상품 판매상태", "외부몰 판매상태", "안전재고(목표재고)"
	                , "권고수량", "낱개 발주수량", "추가 발주수량", "BOX 발주수량", "행사 발주수량", "발주여부", "관리자", "전일 마감재고", "폐기 예정수량 D0", "폐기 예정수량 D1", "입고확정"
					, "입고예정", "입고 대기수량", "예상 잔여수량", "D-0", "D+1", "D+2", "D+3이상", "일평균 출고량", "결품 예상일", "발주메모"
					, "직전 3주 출고수량", "직전 2주 출고수량", "직전 1주 출고수량", "표준카테고리(대분류)", "ERP카테고리(대분류)", "상품코드", "상품 전시상태", "입고 예정일자"
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

	        // dto.setSearchPoIfYn("Y"); // 연동된 발주목록만 조회하기 위해 설정
			dto.setPoResultYn("Y");		// 발주내역 전체 다운로드

	        List<ItemPoListResultVo> poList = goodsItemPoListService.getPoList(dto);

	        firstWorkSheetDto.setExcelDataList(poList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;
	}

	/**
	 * 발주 내역 엑셀 다운로드(올가)
	 * @param	ItemPoListRequestDto
	 * @return	List<ItemPoListResultVo>
	 */
	@Override
	public ExcelDownloadDto getOgPoResultListExportExcel(ItemPoListRequestDto dto) {
		 String excelFileName = "item_OgPoResultList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = {
	                150, 150, 200, 150, 150, 500, 150, 150, 150, 150
	                , 150, 150, 150, 150, 150, 150, 150, 150, 150, 150
	                , 150, 150, 150, 150, 200, 150, 150, 150, 150, 150
	                , 150, 150, 150, 150, 150, 150, 150, 150, 150, 150
	                , 150, 150, 150
	        };

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = {
	        		"center", "center", "center", "center", "center", "center", "center", "center", "center", "center"
	        		, "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"
	        		, "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"
	        		, "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"
	        		, "center", "center", "center"
	        };

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "poTpTemplateNm", "erpCtgryLv1Id", "erpEvent", "ilItemCd", "barcode", "itemNm", "dispStr", "saleStatusNm", "goodsOutmallSaleStatNm", "poProRea"
	                , "recommendPoQty", "piecePoQty", "poSystemQty", "eventPoQty", "stockScheduledDt", "poIfYn", "excelManager", "stockClosed", "stockConfirmed", "stockScheduledD0", "stockScheduledD1More"
	                ,"expectedResidualQty", "offStock", "outbound0", "outbound1", "outbound2", "outbound3More", "erpEventOrderAvg", "nonErpEventOrderAvg", "memo", "ctgryStdNm"
	                , "ilGoodsId", "storageMethodNm","distributionPeriod", "pcsPerBox", "targetStock", "stockDiscardD0", "stockDiscardD1", "outboundDayAvg", "missedOutbound", "outbound3weekTotal"
					, "outbound2weekTotal", "outbound1weekTotal"
	        };

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "발주유형", "ERP카테고리(대분류)", "행사정보(올가전용)", "품목코드", "품목바코드", "마스터품목명", "상품 전시상태", "상품 판매상태", "외부몰 판매상태", "off발주상태(올가전용)"
	                , "권고수량", "낱개 발주수량", "추가 발주수량", "행사 발주수량", "입고 예정일자", "발주여부", "관리자", "전일 마감재고", "입고확정", "입고예정", "입고 대기수량"
	                , "예상 잔여수량", "off재고(올가전용)", "D-0", "D+1", "D+2", "D+3이상", "프로모션 판매평균", "일반 판매평균", "발주메모", "표준카테고리(대분류)"
	                , "상품코드", "보관방법", "유통기간", "박스입수량", "안전재고(목표재고)", "폐기 예정수량 D0", "폐기 예정수량 D1", "일평균 출고량", "결품 예상일", "직전 3주 출고수량"
					, "직전 2주 출고수량", "직전 1주 출고수량"
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

	        //dto.setSearchPoIfYn("Y"); // 연동된 발주목록만 조회하기 위해 설정
	        dto.setPoResultYn("Y");		// 발주내역 전체 다운로드

	        List<ItemPoListResultVo> poList = goodsItemPoListService.getPoList(dto);

	        firstWorkSheetDto.setExcelDataList(poList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;
	}


}
