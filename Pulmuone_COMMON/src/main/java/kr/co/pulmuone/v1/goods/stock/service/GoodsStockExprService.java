package kr.co.pulmuone.v1.goods.stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockExprMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockExprRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExprResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsStockExprService {

	@Autowired
	private final GoodsStockExprMapper goodsStockExprMapper;

	/**
	 * 연동 재고 내역관리 조회
	 * @param	StockExprRequestDto
	 * @return	StockExprResponseDto
	 * @throws Exception
	 */
	protected Page<StockExprResultVo> getStockExprList(StockExprRequestDto dto) {

		ArrayList<String> ilItemCdArray = null;
		StockExprRequestDto stockExprRequestDto = new StockExprRequestDto();

        // 권한 체크
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        stockExprRequestDto.setListAuthSupplierId(listAuthSupplierId);
        stockExprRequestDto.setListAuthWarehouseId(listAuthWarehouseId);

		if(!StringUtil.isEmpty(dto.getSelectConditionType())) {
			//단일조건 검색
			if(!StringUtil.isEmpty(dto.getItemCodes()) && dto.getSelectConditionType().equals("codeSearch")) {
				// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
				String ilItemCodeListStr = dto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
				ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
			}

			// 품목코드 or 품목바코드로 조회시에는 ERP 연동여부 외 다른 검색 조건 무시
	        if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {
	        	stockExprRequestDto.setItemCodes(dto.getItemCodes());
	        	stockExprRequestDto.setSelectConditionType(dto.getSelectConditionType());
	        	stockExprRequestDto.setIlItemCodeArray(ilItemCdArray);
	        	stockExprRequestDto.setPage(dto.getPage());
	        	stockExprRequestDto.setPageSize(dto.getPageSize());

	        	PageMethod.startPage(stockExprRequestDto.getPage(), stockExprRequestDto.getPageSize());

	            return goodsStockExprMapper.getStockExprList(stockExprRequestDto);
	        }

	        //복수조건 검색
	        if(dto.getSelectConditionType().equals("condSearch")) {
	           stockExprRequestDto.setSelectConditionType(dto.getSelectConditionType());
	           stockExprRequestDto.setItemName(dto.getItemName());
	           stockExprRequestDto.setUrSupplierId(dto.getUrSupplierId());
	           stockExprRequestDto.setUrWarehouseId(dto.getUrWarehouseId());
	           stockExprRequestDto.setStartCreateDate(dto.getStartCreateDate());
	           stockExprRequestDto.setEndCreateDate(dto.getEndCreateDate());
	           stockExprRequestDto.setPage(dto.getPage());
	       	   stockExprRequestDto.setPageSize(dto.getPageSize());
	        }
		}

           stockExprRequestDto.setPage(dto.getPage());
    	   stockExprRequestDto.setPageSize(dto.getPageSize());

           PageMethod.startPage(stockExprRequestDto.getPage(), stockExprRequestDto.getPageSize());

        return goodsStockExprMapper.getStockExprList(stockExprRequestDto);
	}

	/**
	 * 통합ERP 재고 연동 내역 관리 조회
	 * @param	StockExprRequestDto
	 * @return	StockExprResponseDto
	 * @throws Exception
	 */
	protected Page<StockExprResultVo> getStockErpList(StockExprRequestDto dto) {

		ArrayList<String> ilItemCdArray = null;
		StockExprRequestDto stockExprRequestDto = new StockExprRequestDto();

		if(!StringUtil.isEmpty(dto.getSelectConditionType())) {
			//단일조건 검색
			if(!StringUtil.isEmpty(dto.getItemCodes()) && dto.getSelectConditionType().equals("codeSearch")) {
				// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
				String ilItemCodeListStr = dto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
				ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
			}

			// 품목코드 or 품목바코드로 조회시에는 ERP 연동여부 외 다른 검색 조건 무시
	        if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {
	        	stockExprRequestDto.setItemCodes(dto.getItemCodes());
	        	stockExprRequestDto.setSelectConditionType(dto.getSelectConditionType());
	        	stockExprRequestDto.setIlItemCodeArray(ilItemCdArray);
	        	stockExprRequestDto.setPage(dto.getPage());
	        	stockExprRequestDto.setPageSize(dto.getPageSize());

	        	PageMethod.startPage(stockExprRequestDto.getPage(), stockExprRequestDto.getPageSize());

	            return goodsStockExprMapper.getStockErpList(stockExprRequestDto);
	        }

	        //복수조건 검색
	        if(dto.getSelectConditionType().equals("condSearch")) {
	           stockExprRequestDto.setSelectConditionType(dto.getSelectConditionType());
	           stockExprRequestDto.setItemName(dto.getItemName());
	           stockExprRequestDto.setStockTp(dto.getStockTp());
	           stockExprRequestDto.setUrSupplierId(dto.getUrSupplierId());
	           stockExprRequestDto.setUrWarehouseId(dto.getUrWarehouseId());
	           stockExprRequestDto.setStartCreateDate(dto.getStartCreateDate());
	           stockExprRequestDto.setEndCreateDate(dto.getEndCreateDate());
	           stockExprRequestDto.setPage(dto.getPage());
	       	   stockExprRequestDto.setPageSize(dto.getPageSize());
	        }
		}

           stockExprRequestDto.setPage(dto.getPage());
    	   stockExprRequestDto.setPageSize(dto.getPageSize());

           PageMethod.startPage(stockExprRequestDto.getPage(), stockExprRequestDto.getPageSize());

        return goodsStockExprMapper.getStockErpList(stockExprRequestDto);
	}

	/**
	 * 재고 미연동 품목리스트 조회
	 * @param	StockExprRequestDto
	 * @return	StockExprResponseDto
	 * @throws Exception
	 */
	protected Page<StockExprResultVo> getStockNonErpList(StockExprRequestDto dto) {

		ArrayList<String> ilItemCdArray = null;

        // 권한 체크
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        dto.setListAuthSupplierId(listAuthSupplierId);
        dto.setListAuthWarehouseId(listAuthWarehouseId);

		if(!StringUtil.isEmpty(dto.getSelectConditionType())){
			//단일조건 검색
			if(!StringUtil.isEmpty(dto.getItemCodes()) && dto.getSelectConditionType().equals("codeSearch")) {
				// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
				String ilItemCodeListStr = dto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
				ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
			}

			// 품목코드 or 품목바코드로 조회시에는 ERP 연동여부 외 다른 검색 조건 무시
	        if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {
	        	StockExprRequestDto stockExprRequestDto = new StockExprRequestDto();

	        	stockExprRequestDto.setItemCodes(dto.getItemCodes());
	        	stockExprRequestDto.setSelectConditionType(dto.getSelectConditionType());
	        	stockExprRequestDto.setIlItemCodeArray(ilItemCdArray);
	        	stockExprRequestDto.setPage(dto.getPage());
	        	stockExprRequestDto.setPageSize(dto.getPageSize());

	        	PageMethod.startPage(stockExprRequestDto.getPage(), stockExprRequestDto.getPageSize());

	            return goodsStockExprMapper.getStockNonErpList(stockExprRequestDto);
	        }

	        //복수조건 검색
	        if(dto.getSelectConditionType().equals("condSearch")) {
	        	dto.setSaleStatusList(getSearchKeyToSearchKeyList(dto.getSaleStatus(), Constants.ARRAY_SEPARATORS));       // 판매상태
	            dto.setGoodsTypeList(getSearchKeyToSearchKeyList(dto.getGoodsType(), Constants.ARRAY_SEPARATORS));         // 상품유형
				dto.setIsNoGoodsItem("N");
				if (dto.getGoodsTypeList() != null && dto.getGoodsTypeList().size() > 0) {
					int i = 0;
					for (String status : dto.getGoodsTypeList()) {
						if ("NO_GOODS".equals(status)) { // 상품 미생성 품목 체크
							dto.setIsNoGoodsItem("Y");
							dto.getGoodsTypeList().remove(i);
							break;
						}
						i++;
					}
				}
	            dto.setStorageMethodTypeList(getSearchKeyToSearchKeyList(dto.getKeepMethod(), Constants.ARRAY_SEPARATORS));// 보관방법
	        }
		}

           PageMethod.startPage(dto.getPage(), dto.getPageSize());

        return goodsStockExprMapper.getStockNonErpList(dto);
	}

	/**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) )
                                       .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

    /**
	 * @Desc  재고 미연동 품목리스트 - 재고수정
	 * @param StockExprRequestDto
	 * @return int
	 */
	protected int putStockNonErp(StockExprRequestDto dto) {
		return goodsStockExprMapper.putStockNonErp(dto);
	}

	/**
	 * 유통기한별 재고 연동 내역 관리 엑셀 다운로드
	 * @param	StockExprRequestDto
	 * @return	excelDownloadDto
	 */
	protected ExcelDownloadDto getStockExprExportExcel(StockExprRequestDto dto) {

		 String excelFileName = "item_InterfacedStock"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                150, 150, 150, 350, 150, 150, 230, 180, 150, 150};

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "center", "center", "left", "center", "center", "center","center","center","center"};

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "baseDt", "ilItemCd", "itemBarcode", "itemNm", "supplierNm", "storageMethodTpNm", "warehouseNm", "expirationDt", "restDistributionPeriod", "stockQty"};

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "연동일자", "품목코드", "품목바코드", "품목명", "공급업체", "보관방법", "출고처", "유통기한", "잔여 유통기간", "재고"};

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
	        List<StockExprResultVo> orgaDisList = getStockExprList(dto);

	        firstWorkSheetDto.setExcelDataList(orgaDisList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;

	}

	/**
	 * 통합ERP 재고 연동 내역 관리 엑셀 다운로드
	 * @param	StockExprRequestDto
	 * @return	excelDownloadDto
	 */
	protected ExcelDownloadDto getStockErpExportExcel(StockExprRequestDto dto) {

		 String excelFileName = "item_ErpInterfacedStock"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                150, 150, 150, 350, 150, 200, 150, 150};

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "center", "center", "left", "center", "center", "center","center"};

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "baseDt", "ilItemCd", "itemBarcode", "itemNm", "supplierNm", "warehouseNm", "stockTpNm", "stockQty"};

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "연동일자", "품목코드", "품목바코드", "품목명", "공급업체", "출고처", "타입", "재고"};

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
	        List<StockExprResultVo> orgaDisList = getStockErpList(dto);

	        firstWorkSheetDto.setExcelDataList(orgaDisList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;

	}

	/**
	 * 재고 미연동 품목리스트 엑셀 다운로드
	 * @param	StockExprRequestDto
	 * @return	excelDownloadDto
	 */
	protected ExcelDownloadDto getStockNonErpExportExcel(StockExprRequestDto dto) {

		 String excelFileName = "item_NonErpInterfacedStock"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                200, 150, 150, 150, 180, 500, 230, 250, 150, 250, 150};

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "center", "center", "center", "center", "left", "center", "center", "center","center", "center"};

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "ilItemCd", "barcode", "goodsTpNm", "ilGoodsId", "saleStatusNm", "itemNm", "supplierNm", "ctgryStdNm", "storageMethodTpNm", "warehouseNm", "stock"};

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "품목코드", "품목바코드", "상품유형", "상품코드", "상품판매상태", "마스터품목명", "공급업체", "표준카테고리(대분류)", "보관방법", "출고처", "재고"};

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
	        List<StockExprResultVo> orgaDisList = getStockNonErpList(dto);

	        firstWorkSheetDto.setExcelDataList(orgaDisList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;

	}

}
