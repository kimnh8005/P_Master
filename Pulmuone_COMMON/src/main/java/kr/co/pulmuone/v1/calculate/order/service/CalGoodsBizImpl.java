package kr.co.pulmuone.v1.calculate.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.calculate.employee.dto.OuIdListResponseDto;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListRequestDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 상품정산 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class CalGoodsBizImpl implements CalGoodsBiz {

	@Autowired
	CalGoodsService calGoodsService;

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	@Override
	public ApiResult<?> getOuIdAllList() {
		List<SettleOuMngVo> list = calGoodsService.getOuIdAllList();
		OuIdListResponseDto ouIdListResponseDto = OuIdListResponseDto.builder()
				.rows(calGoodsService.getOuIdAllList())
				.build();
		System.out.println(list);

		return ApiResult.success(ouIdListResponseDto);
	}

	/**
	 * 임직원 지원금 정산 리스트 조회
	 * @param calGoodsListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getGoodsList(CalGoodsListRequestDto calGoodsListRequestDto) {

		calGoodsListRequestDto.setOmSellersIdList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		calGoodsListRequestDto.setSalesGubunList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getSalesGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calGoodsListRequestDto.setGoodsTypeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
		calGoodsListRequestDto.setPaymentMethodCodeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단

		long totalCnt = calGoodsService.getGoodsListCount(calGoodsListRequestDto);

		List<CalGoodsListDto> goodsList = new ArrayList<>();
		if (totalCnt > 0) {
			goodsList = calGoodsService.getGoodsList(calGoodsListRequestDto);
		}

		return ApiResult.success(
				CalGoodsListResponseDto.builder()
									.rows(goodsList)
									.total(totalCnt)
									.build()
		);
	}


	/**
     * @Desc 상품 정산 리스트 엑셀 다운로드 목록 조회
     * @param CalGoodsListRequestDto : 상품 정산 리스트  검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     */
    @Override
    public ExcelDownloadDto getGoodsListExportExcel(CalGoodsListRequestDto calGoodsListRequestDto) {

    	calGoodsListRequestDto.setExcelYn("Y");
    	calGoodsListRequestDto.setOmSellersIdList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
    	calGoodsListRequestDto.setSalesGubunList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getSalesGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calGoodsListRequestDto.setGoodsTypeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
		calGoodsListRequestDto.setPaymentMethodCodeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단


        String excelFileName = "상품 정산 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
                200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
                "center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "orderType", "agentTypeCd", "goodsSettleDt", "goodsOrderDt", "paymentDt", "pgServiceNm", "orderPaymentType", "sellersNm", "odid", "odOrderDetlId",
                "urSupplierId", "warehouseName", "ilItemCd",  "ilGoodsId", "goodsNm", "goodsTpCd", "orderCnt", "recommendedPrice", "directPrice",
                "discountEmployeePrice", "salePrice", "goodsCouponPrice", "cartCouponPrice", "orderPrice", "paidPriceGoods", "totalSalePrice", "totalSaleNonTaxPrice",
				"taxablePrice", "nonTaxablePrice", "vat","taxYn","orderChangeYn","outmallId","outmallDetailId"};

		String[] cellTypeListOfFirstWorksheet = {
				"string", "string", "string", "string", "string", "string", "string", "string", "string", "string",
				"string", "string", "string", "string", "string", "string", "string", "int", "int", "int",
				"int", "double", "int","int","int","int", "double", "double",
				"double","double", "double","string","string","string" , "string"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "구분", "주문경로(유형)", "정산처리일자", "주문일자", "결제일자/환불일자", "PG", "결제수단/환불수단", "판매처", "BOS주문번호", "주문상세번호",
                "공급업체", "출고처", "품목코드", "상품코드", "상품명", "상품유형", "수량", "정상가", "즉시 할인금액",
				"임직원 할인", "판매가", "상품쿠폰 할인금액","장바구니쿠폰 할인금액","주문금액","결제금액(배송비제외)", "예상상품매출(VAT포함)", "예상상품매출(VAT별도)",
				"매출금액","매출금액(VAT제외)", "VAT금액","과세구분","통합 ERP I/F 여부","외부몰 주문번호" , "외부몰 주문상세번호"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.cellTypeList(cellTypeListOfFirstWorksheet)
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        // 상품정산 조회 조건 : 구분
        if( StringUtils.isNotEmpty(calGoodsListRequestDto.getSalesGubun()) && calGoodsListRequestDto.getSalesGubun().indexOf("ALL") < 0 ) {
        	calGoodsListRequestDto.setSalesGubunList(Stream.of(calGoodsListRequestDto.getSalesGubun().split(Constants.ARRAY_SEPARATORS))
								                .map(String::trim)
								                .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
								                .collect(Collectors.toList())); // 검색어
		}

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<CalGoodsListDto> itemList = calGoodsService.getGoodsList(calGoodsListRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }

	/**
	 * 상품 정산 (IF 아닌) 리스트 조회
	 * @param calGoodsListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getGoodsNotIfList(CalGoodsListRequestDto calGoodsListRequestDto) {

		calGoodsListRequestDto.setOmSellersIdList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		calGoodsListRequestDto.setSalesGubunList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getSalesGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calGoodsListRequestDto.setGoodsTypeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
		calGoodsListRequestDto.setPaymentMethodCodeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단

		long totalCnt = calGoodsService.getGoodsNotIfListCount(calGoodsListRequestDto);

		List<CalGoodsListDto> goodsList = new ArrayList<>();
		if (totalCnt > 0) {
			goodsList = calGoodsService.getGoodsNotIfList(calGoodsListRequestDto);
		}

		return ApiResult.success(
				CalGoodsListResponseDto.builder()
						.rows(goodsList)
						.total(totalCnt)
						.build()
		);
	}

	/**
	 * @Desc 상품 정산 (IF 아닌) 리스트 엑셀 다운로드 목록 조회
	 * @param CalGoodsListRequestDto : 상품 정산 리스트  검색 조건 request dto
	 * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
	 */
	@Override
	public ExcelDownloadDto getGoodsNotIfListExportExcel(CalGoodsListRequestDto calGoodsListRequestDto) {

		calGoodsListRequestDto.setExcelYn("Y");
		calGoodsListRequestDto.setOmSellersIdList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		calGoodsListRequestDto.setSalesGubunList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getSalesGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calGoodsListRequestDto.setGoodsTypeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
		calGoodsListRequestDto.setPaymentMethodCodeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단


		String excelFileName = "상품 정산 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200};

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center"};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = { //
				"orderType", "agentTypeCd", "goodsSettleDt", "goodsOrderDt", "paymentDt", "pgServiceNm", "orderPaymentType", "sellersNm", "odid", "odOrderDetlId",
				"urSupplierId", "warehouseName", "ilItemCd",  "ilGoodsId", "goodsNm", "goodsTpCd", "orderCnt", "recommendedPrice", "directPrice",
				"discountEmployeePrice", "salePrice", "goodsCouponPrice", "cartCouponPrice", "orderPrice", "paidPriceGoods", "totalSalePrice", "totalSaleNonTaxPrice",
				"taxYn","orderChangeYn","outmallId","outmallDetailId", "branchStoreId", "branchStoreNm", "buyerNm"};

		String[] cellTypeListOfFirstWorksheet = {
				"string", "string", "string", "string", "string", "string", "string", "string", "string", "string",
				"string", "string", "string", "string", "string", "string", "string", "int", "int", "int",
				"int", "double", "int","int","int","int", "double", "double",
				"string","string","string" , "string", "string", "string", "string"};

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"구분", "주문경로(유형)", "정산처리일자", "주문일자", "결제일자/환불일자", "PG", "결제수단/환불수단", "판매처", "BOS주문번호", "주문상세번호",
				"공급업체", "출고처", "품목코드", "상품코드", "상품명", "상품유형", "수량", "정상가", "즉시 할인금액",
				"임직원 할인", "판매가", "상품쿠폰 할인금액","장바구니쿠폰 할인금액","주문금액","결제금액(배송비제외)", "예상상품매출(VAT포함)", "예상상품매출(VAT별도)",
				"과세구분","통합 ERP I/F 여부","외부몰 주문번호" , "외부몰 주문상세번호", "가맹점코드", "가맹점명", "주문자명"};

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.cellTypeList(cellTypeListOfFirstWorksheet)
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

		// 상품정산 조회 조건 : 구분
		if( StringUtils.isNotEmpty(calGoodsListRequestDto.getSalesGubun()) && calGoodsListRequestDto.getSalesGubun().indexOf("ALL") < 0 ) {
			calGoodsListRequestDto.setSalesGubunList(Stream.of(calGoodsListRequestDto.getSalesGubun().split(Constants.ARRAY_SEPARATORS))
					.map(String::trim)
					.filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
					.collect(Collectors.toList())); // 검색어
		}

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
		 * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
		 */
		List<CalGoodsListDto> itemList = calGoodsService.getGoodsNotIfList(calGoodsListRequestDto);

		firstWorkSheetDto.setExcelDataList(itemList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;

	}

	/**
	 * 매장 상품 정산 리스트 조회
	 * @param calGoodsListRequestDto
	 * @return
	 */
	@Override
	public ApiResult<?> getStoreGoodsList(CalGoodsListRequestDto calGoodsListRequestDto) {

		calGoodsListRequestDto.setOmSellersIdList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		calGoodsListRequestDto.setSalesGubunList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getSalesGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calGoodsListRequestDto.setGoodsTypeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
		calGoodsListRequestDto.setPaymentMethodCodeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단

		long totalCnt = calGoodsService.getStoreGoodsListCount(calGoodsListRequestDto);

		List<CalGoodsListDto> goodsList = new ArrayList<>();
		if (totalCnt > 0) {
			goodsList = calGoodsService.getStoreGoodsList(calGoodsListRequestDto);
		}

		return ApiResult.success(
				CalGoodsListResponseDto.builder()
						.rows(goodsList)
						.total(totalCnt)
						.build()
		);
	}

	/**
	 * 매장 상품 정산 리스트 목록  엑셀 다운로드
	 * @param calGoodsListRequestDto
	 * @return
	 */
	@Override
	public ExcelDownloadDto getStoreGoodsListExportExcel(CalGoodsListRequestDto calGoodsListRequestDto) {

		calGoodsListRequestDto.setExcelYn("Y");
		calGoodsListRequestDto.setOmSellersIdList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getOmSellersId(), Constants.ARRAY_SEPARATORS)); // 판매처
		calGoodsListRequestDto.setSalesGubunList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getSalesGubun(), Constants.ARRAY_SEPARATORS)); // 구분
		calGoodsListRequestDto.setGoodsTypeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형
		calGoodsListRequestDto.setPaymentMethodCodeList(calGoodsService.getSearchKeyToSearchKeyList(calGoodsListRequestDto.getPaymentMethodCode(), Constants.ARRAY_SEPARATORS)); // 결제수단


		String excelFileName = "매장 상품 정산 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200, 200, 200, 200,
				200, 200, 200, 200, 200, 200, 200, 200 };

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center", "center", "center"};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = { //
				"orderType", "agentTypeCd", "goodsSettleDt", "goodsOrderDt", "paymentDt", "pgServiceNm", "orderPaymentType", "sellersNm", "odid", "odOrderDetlId",
				"urSupplierId", "warehouseName", "storeName", "ilItemCd",  "ilGoodsId", "goodsNm", "goodsTpCd", "orderCnt", "recommendedPrice", "directPrice",
				"discountEmployeePrice", "salePrice", "goodsCouponPrice", "cartCouponPrice", "orderPrice", "paidPriceGoods", "totalSalePrice", "totalSaleNonTaxPrice",
				"taxablePrice", "nonTaxablePrice", "vat","taxYn","orderChangeYn","outmallId","outmallDetailId"};

		String[] cellTypeListOfFirstWorksheet = {
				"string", "string", "string", "string", "string", "string", "string", "string", "string", "string",
				"string", "string", "string", "string", "string", "string", "string", "int", "int", "int",
				"int", "double", "int","int","int","int", "double", "double",
				"double","double", "double","string","string","string" , "string"};

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"구분", "주문경로(유형)", "정산처리일자", "주문일자", "결제일자/환불일자", "PG", "결제수단/환불수단", "판매처", "BOS주문번호", "주문상세번호",
				"공급업체", "출고처", "매장명", "품목코드", "상품코드", "상품명", "상품유형", "수량", "정상가", "즉시 할인금액",
				"임직원 할인", "판매가", "상품쿠폰 할인금액","장바구니쿠폰 할인금액","주문금액","결제금액(배송비제외)", "예상상품매출(VAT포함)", "예상상품매출(VAT별도)",
				"매출금액","매출금액(VAT제외)", "VAT금액","과세구분","통합 ERP I/F 여부","외부몰 주문번호" , "외부몰 주문상세번호"};

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.cellTypeList(cellTypeListOfFirstWorksheet)
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

		// 상품정산 조회 조건 : 구분
		if( StringUtils.isNotEmpty(calGoodsListRequestDto.getSalesGubun()) && calGoodsListRequestDto.getSalesGubun().indexOf("ALL") < 0 ) {
			calGoodsListRequestDto.setSalesGubunList(Stream.of(calGoodsListRequestDto.getSalesGubun().split(Constants.ARRAY_SEPARATORS))
					.map(String::trim)
					.filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
					.collect(Collectors.toList())); // 검색어
		}

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
		 * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
		 */
		List<CalGoodsListDto> itemList = calGoodsService.getStoreGoodsList(calGoodsListRequestDto);

		firstWorkSheetDto.setExcelDataList(itemList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;

	}
}