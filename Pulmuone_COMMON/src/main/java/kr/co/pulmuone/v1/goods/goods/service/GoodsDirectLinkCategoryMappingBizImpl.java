package kr.co.pulmuone.v1.goods.goods.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDirectLinkCategoryMappingResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDirectLinkCategoryMappingListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class GoodsDirectLinkCategoryMappingBizImpl implements GoodsDirectLinkCategoryMappingBiz {

	@Autowired
	private GoodsDirectLinkCategoryMappingService goodsDirectLinkCategoryMappingService;

	// @Desc 네이버 표준 카테고리 맵핑 조회
	@Override
	public GoodsDirectLinkCategoryMappingResponseDto getGoodsDirectLinkCategoryMappingList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException{
		// TODO Auto-generated method stub

		return goodsDirectLinkCategoryMappingService.getGoodsDirectLinkCategoryMappingList(paramDto);
	}

	// @Desc 네이버 표준 카테고리 맵핑 조회내역 다운로드
	@Override
	public ExcelDownloadDto getGoodsDirectLinkCategoryMappingListExcel(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException{
		// TODO Auto-generated method stub
		String excelFileName = "직연동카테고리"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = {150, 150, 150, 150, 150, 150, 150, 300, 300, 150 };

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = {  "center", "center", "center", "center", "center", "center", "center", "center", "center", "center" };
		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = {"gearCateType", "ilCtgryStdId", "ilCtgryStdFullName", "categoryId", "categoryName", "ilCtgryStdNm1", "ilCtgryStdNm2", "ilCtgryStdNm3", "mappingYn", "createDt" };

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		// 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
		String[] firstHeaderListOfFirstWorksheet = {"직연동몰", "표준 카테고리 코드", "풀무원 표준 카테고리", "카테고리 코드", "직연동몰 카테고리", "대분류", "중분류", "소분류", "직연동몰 카테고리 매핑여부", "등록일자" };

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
		List<GoodsDirectLinkCategoryMappingListVo> resultList = goodsDirectLinkCategoryMappingService.getGoodsDirectLinkCategoryMappingListExcel(paramDto);

		firstWorkSheetDto.setExcelDataList(resultList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
											.excelFileName(excelFileName) //
											.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	// @Desc 표준 카테고리 매핑 조회
	@Override
	public GoodsDirectLinkCategoryMappingResponseDto getIfNaverCategoryList(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {
		// TODO Auto-generated method stub

		return goodsDirectLinkCategoryMappingService.getIfNaverCategoryList(paramDto);
	}

	// @Desc 네이버 표준 카테고리 맵핑 등록
	@Override
	public GoodsDirectLinkCategoryMappingResponseDto addGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {
		// TODO Auto-generated method stub

		return goodsDirectLinkCategoryMappingService.addGoodsDirectLinkCategoryMapping(paramDto);
	}

	// @Desc 네이버 표준 카테고리 맵핑 수정
	@Override
	public GoodsDirectLinkCategoryMappingResponseDto putGoodsDirectLinkCategoryMapping(GoodsDirectLinkCategoryMappingRequestDto paramDto) throws BaseException {
		// TODO Auto-generated method stub

		return goodsDirectLinkCategoryMappingService.putGoodsDirectLinkCategoryMapping(paramDto);
	}

}
