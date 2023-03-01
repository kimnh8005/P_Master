package kr.co.pulmuone.v1.goods.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPackageListVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoodsPackageListBizImpl implements GoodsPackageListBiz{

	@Autowired
    GoodsPackageListService goodsPackageListService;

    @Autowired
    GoodsListService goodsListService;

	@Override
	public ApiResult<?> getGoodsPackageList(GoodsPackageListRequestDto paramDto) {
		// TODO Auto-generated method stub

		GoodsPackageListResponseDto goodsListResponseDto = new GoodsPackageListResponseDto();

		Page<GoodsPackageListVo> goodsList = goodsPackageListService.getGoodsPackageList(paramDto);

        goodsListResponseDto.setTotal(goodsList.getTotal());
        goodsListResponseDto.setRows(goodsList.getResult());

        return ApiResult.success(goodsListResponseDto);

	}

	@Override
	public ApiResult<?> getGoodsPackageDetailList(GoodsPackageListRequestDto paramDto) {
		// TODO Auto-generated method stub
		GoodsPackageListResponseDto goodsListResponseDto = new GoodsPackageListResponseDto();

		List<GoodsPackageListVo> goodsPackageList = goodsPackageListService.getGoodsPackageDetailList(paramDto);
		goodsListResponseDto.setRows(goodsPackageList);
		return ApiResult.success(goodsListResponseDto);
	}

	@Override
	public ExcelDownloadDto getGoodsPackageExcelList(GoodsPackageListRequestDto paramDto) {
		// TODO Auto-generated method stub

		String excelFileName = ""; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름
		ExcelDownloadDto excelDownloadDto = null;
		ArrayList<String> goodsCdArray = null;

		if(paramDto.getSearchType().equals("single") && !StringUtil.isEmpty(paramDto.getGoodsCodes())) {

       		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String ilItemCodeListStr = paramDto.getGoodsCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            goodsCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
            paramDto.setGoodsCodeArray(goodsCdArray);
       	}

       	if(paramDto.getSearchType().equals("multi")) {
       		paramDto.setSaleStatusList(goodsListService.getSearchKeyToSearchKeyList(paramDto.getSaleStatus(), Constants.ARRAY_SEPARATORS)); // 판매상태
       	}


		if(paramDto.getExcelDom().equals("STANDARD_LIST")) {
			excelFileName = "묶음 상품 기본정보 양식";

			/*
			 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
			 * 120 pixel 로 고정됨
			 */
			Integer[] widthListOfFirstWorksheet = { //
					120, 120, 200, 260, 120,
					120, 120, 160, 120, 200,
					200, 400, 200, 200, 120,
					120
			};

			/*
			 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
			 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
			 * 지정시 "left" (좌측 정렬) 로 지정됨
			 */
			String[] alignListOfFirstWorksheet = { //
					"center", "center", "center", "center", "center",
					"center", "center", "center", "center", "center",
					"center", "left", "center", "center", "center",
					"center"
			};

			/*
			 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
			 * 함
			 */
			String[] propertyListOfFirstWorksheet = { //
					"goodsId", "goodsTpNm", "promotionName", "goodsNm", "standardPriceStr",
					"recommendedPriceStr", "salePriceStr", "discountTypeName", "salePriceStr", "goodsCreateDate",
					"goodsModifyDate", "categoryDepthName", "saleStartDate", "saleEndDate", "saleStatusName",
					"dispYn" };

			// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
			String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
					"상품코드", "상품유형", "프로모션명", "상품명", "원가총액",
					"정상가 총액", "묶음상품 판매가", "할인유형", "판매가", "상품등록일",
					"최근수정일", "전시 카테고리", "판매기간(시작일)" , "판매기간(종료일)", "판매상태" ,
					"전시상태"};

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
			List<GoodsPackageListVo> goodsPackageList = goodsPackageListService.getGoodsPackageListExcel(paramDto);

			firstWorkSheetDto.setExcelDataList(goodsPackageList);

			// excelDownloadDto 생성 후 workSheetDto 추가
			excelDownloadDto = ExcelDownloadDto.builder() //
					.excelFileName(excelFileName) //
					.build();

			excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		}else if(paramDto.getExcelDom().equals("DETAIL_LIST")) {

			excelFileName = "묶음 상품 구성상품 정보양식";

			/*
			 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
			 * 120 pixel 로 고정됨
			 */
			Integer[] widthListOfFirstWorksheet = { //
					150, 150, 150, 300, 120,
					120, 100, 140, 160, 120,
					200, 200, 400, 200, 200,
					120, 120
			};

			/*
			 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
			 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
			 * 지정시 "left" (좌측 정렬) 로 지정됨
			 */
			String[] alignListOfFirstWorksheet = { //
					"center", "center", "center", "left", "center",
					"center", "center", "center", "center", "center",
					"center", "center", "left", "center", "center",
					"center", "center"
			};

			/*
			 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
			 * 함
			 */
			String[] propertyListOfFirstWorksheet = { //
					"parentGoodsId", "targetGoodsId", "goodsTpNm", "goodsNm", "standardPriceStr",
					"recommendedPriceStr", "goodsQty", "salePriceStr",  "discountTypeName", "salePriceStr",
					"goodsCreateDate", "goodsModifyDate", "categoryDepthName",  "saleStartDate", "saleEndDate",
					"saleStatusName", "dispYn"
					};

			// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
			String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
					"묶음상품코드", "구성상품코드", "구성상품유형", "상품명", "원가",
					"정상가", "수량", "묶음상품 판매가", "할인유형", "판매가",
					"상품등록일", "최근수정일", "전시 카테고리", "판매기간(시작일)", "판매기간(종료일)",
					"판매상태", "전시상태"};

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
			List<GoodsPackageListVo> goodsPackageList = goodsPackageListService.getGoodsPackageDetailListExcel(paramDto);

			firstWorkSheetDto.setExcelDataList(goodsPackageList);

			// excelDownloadDto 생성 후 workSheetDto 추가
			excelDownloadDto = ExcelDownloadDto.builder() //
					.excelFileName(excelFileName) //
					.build();

			excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);
		}

		return excelDownloadDto;


	}



}
