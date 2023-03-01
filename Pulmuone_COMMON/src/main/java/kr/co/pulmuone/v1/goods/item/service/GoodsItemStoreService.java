package kr.co.pulmuone.v1.goods.item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemStoreMapper;
import lombok.RequiredArgsConstructor;

/**
 * dto, vo import 하기
 *
 * <PRE>
 * Forbiz Korea
 * 품목 매장 관리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 06. 10.                홍진영          최초작성
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class GoodsItemStoreService {

	@Autowired
	private final GoodsItemStoreMapper goodsItemStoreMapper;

	/**
	 * 품목 매장 정보 리스트
	 *
	 * @param ilItemCode
	 * @return
	 */
	protected List<ItemStoreInfoDto> getStoreList(String ilItemCode) {
		return goodsItemStoreMapper.getStoreList(ilItemCode);
	}

	/**
	 * 품목 매장 가격 히스토리 정보
	 */
	protected ItemStorePriceLogResponseDto getStorePriceLogList(ItemStorePriceLogRequestDto reqDto) throws Exception {

		ItemStorePriceLogResponseDto resDto = new ItemStorePriceLogResponseDto();

		PageMethod.startPage(reqDto.getPage(), reqDto.getPageSize());
		Page<ItemStorePriceLogDto> data = goodsItemStoreMapper.getStorePriceLogList(reqDto);

		resDto.setTotal(data.getTotal());
		resDto.setRows(data.getResult());

		return resDto;
	}

	/**
	 * 매장 재고 리스트
	 * @param itemStoreStockRequestDto
	 * @return
	 * @throws Exception
	 */
	protected Page<ItemStoreStockDto> getItemStoreStockList(ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception {
		PageMethod.startPage(itemStoreStockRequestDto.getPage(), itemStoreStockRequestDto.getPageSize());
		ArrayList<String> itemCodeArray = new ArrayList<String>();

		if (!StringUtil.isEmpty(itemStoreStockRequestDto.getItemCodes())) {

			// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
			String ilItemCodeListStr = itemStoreStockRequestDto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

			String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");

			for(int i = 0; i < ilItemCodeListArray.length; i++) {
				String ilItemCodeSearchVal = ilItemCodeListArray[i];
				if(ilItemCodeSearchVal.isEmpty()) {
					continue;
				}
				itemCodeArray.add(ilItemCodeSearchVal);
			}
		}
		itemStoreStockRequestDto.setItemCodeList(itemCodeArray);
		if ("multiSection".equals(itemStoreStockRequestDto.getSelectConditionType()) && !itemStoreStockRequestDto.getItemStoreType().isEmpty()) {
			itemStoreStockRequestDto.setItemStoreTypeList(getSearchKeyToSearchKeyList(itemStoreStockRequestDto.getItemStoreType(), Constants.ARRAY_SEPARATORS));
		}

		return goodsItemStoreMapper.getItemStoreStockList(itemStoreStockRequestDto);
	}

	/**
	 * 매장 재고 리스트 엑셀 다운
	 * @param itemStoreStockRequestDto
	 * @return
	 * @throws Exception
	 */
	protected ExcelDownloadDto getItemStoreStockListExcel(ItemStoreStockRequestDto itemStoreStockRequestDto) throws Exception {

		String excelFileName = "매장 재고 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름
		ExcelDownloadDto excelDownloadDto = null;

		Integer[] widthListOfFirstWorksheet = { 120, 300, 120, 120, 120, 120, 200, 120, 120 };
		String[] alignListOfFirstWorksheet = { "center", "center", "center", "center", "center", "center", "center", "center", "center" };
		String[] propertyListOfFirstWorksheet = { "itemCode", "itemNm", "companyNm", "brandNm", "dispBrandNm", "storeNm", "category", "itemStoreType", "storeStock" };
		String[] firstHeaderListOfFirstWorksheet = { "품목코드", "마스터품목명", "공급업체", "표준브랜드", "전시브랜드", "매장", "표준카테고리(대분류)", "유형", "재고"};

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
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<ItemStoreStockDto> itemStoreStockList = getItemStoreStockList(itemStoreStockRequestDto).getResult();

		firstWorkSheetDto.setExcelDataList(itemStoreStockList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
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

		if (StringUtils.isNotEmpty(searchKey)) {
			searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
					.map(String::trim)
					.filter( x -> StringUtils.isNotEmpty(x) )
					.collect(Collectors.toList()));

			for (String key : searchKeyList) {
				if ("ALL".equals(key)) {
					return new ArrayList<String>();
				}
			}
		}

		return searchKeyList;
	}
}
