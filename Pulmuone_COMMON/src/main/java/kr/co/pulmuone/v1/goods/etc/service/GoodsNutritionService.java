package kr.co.pulmuone.v1.goods.etc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.goods.etc.GoodsNutritionMapper;
import kr.co.pulmuone.v1.goods.etc.dto.GoodsNutritionRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsNutritionVo;
import lombok.RequiredArgsConstructor;

/**
 * dto, vo import 하기
 * <PRE>
 * Forbiz Korea
 * 상품영양정보
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.07.07  박영후           최초작성
 *  1.0    2020.10.26  이성준           리팩토링
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class GoodsNutritionService {

	@Autowired
    private final GoodsNutritionMapper goodsNutritionMapper;

	/**
	 * 상품영양정보 목록 조회
	 * @param ItemNutritionListResponseDto
	 * @return List<GoodsItemNutritionVo>
	 */
	protected Page<GoodsNutritionVo> getGoodsNutritionList(GoodsNutritionRequestDto goodsNutritionRequestDto) {
		PageMethod.startPage(goodsNutritionRequestDto.getPage(), goodsNutritionRequestDto.getPageSize());
		return goodsNutritionMapper.getGoodsNutritionList(goodsNutritionRequestDto);
	}

	/**
	 * 상품영양정보 상세 조회
	 * @param ilNutritionCode 상품 영양정보 분류(ERP 분류정보) 코드 PK
	 * @return GoodsItemNutritionVo
	 */
	protected GoodsNutritionVo getGoodsNutrition(String ilNutritionCode) {
		return goodsNutritionMapper.getGoodsNutrition(ilNutritionCode);
	}

	/**
	 * 상품영양정보
	 * @param GoodsNutritionRequestDto
	 * @return int
	 */
	protected int duplicateGoodsNutritionByNameCount(GoodsNutritionRequestDto goodsNutritionRequestDto) {
		return goodsNutritionMapper.duplicateGoodsNutritionByNameCount(goodsNutritionRequestDto);
	}

	/**
	 * 상품영양정보 추가
	 * @param GoodsNutritionRequestDto
	 * @return int
	 */
	protected int addGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) {
		return goodsNutritionMapper.addGoodsNutrition(goodsNutritionRequestDto);
	}

	/**
	 * 상품영양정보 수정
	 * @param	GoodsitemNutritionRequestDto
	 * @return	GoodsItemNutritionResponseDto
	 */
	protected int putGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) {
		return goodsNutritionMapper.putGoodsNutrition(goodsNutritionRequestDto);
	}

	/**
	 * 상품영양정보 삭제
	 * @param	GoodsitemNutritionRequestDto
	 * @return	GoodsItemNutritionResponseDto
	 */
	protected int delGoodsNutrition(GoodsNutritionRequestDto goodsNutritionRequestDto) {
		return goodsNutritionMapper.delGoodsNutrition(goodsNutritionRequestDto);
	}


	/**
	 * 상품영양정보 엑셀용 데이타 조회
	 * @return List<GoodsItemNutritionVo>
	 * @throws Exception
	 */
	 protected List<GoodsNutritionVo> getGoodsNutritionExcelList() {
		 return goodsNutritionMapper.getGoodsNutritionExcelList();
	 }

    /**
	 * 상품영양정보 엑셀다운로드
	 *
	 * @param GoodsNutritionRequestDto
	 * @return ExcelDownloadDto
	 */
	protected ExcelDownloadDto getGoodsNutritionExportExcel(GoodsNutritionRequestDto dto) {

        /*
         * 엑셀 파일 / 워크시트 정보
         *
         */

        String excelFileName = "item_nutrition"; // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
         *
         */

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                250, 250, 250 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "ilNutritionCode", "nutritionName", "nutritionUnit" };

        /*
         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
         *
         */
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "분류코드", "분류명", "분류단위"
        };

        /*
         * 워크시트 DTO 생성 후 정보 세팅
         *
         */
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
         *
         */
        List<GoodsNutritionVo> itemNutritionList = this.getGoodsNutritionExcelList();

        firstWorkSheetDto.setExcelDataList(itemNutritionList);

        /*
         * excelDownloadDto 생성 후 workSheetDto 추가
         *
         */
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
	}

}
