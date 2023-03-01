package kr.co.pulmuone.v1.goods.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsAllModifyResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDisPriceHisRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistCategoryRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsAllModifyVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDisPriceHisVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistAdditionalGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistVo;

/**
* <PRE>
* Forbiz Korea
* 상품일괄수정 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 01. 08.                정형진          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class GoodsAllModifyBizImpl implements GoodsAllModifyBiz{

	@Autowired
    private GoodsAllModifyService goodsAllModifyService;

	/**
     * @Desc 상품 일괄수정 대상 상품 조회
     *
     * @param GoodsAllModifyRequestDto
     * @return ApiResult
     */
	@Override
	public ApiResult<?> getGoodsAllModifyList(GoodsAllModifyRequestDto paramDto) {
		// TODO Auto-generated method stub
		GoodsAllModifyResponseDto goodsAllModifyResponseDto = new GoodsAllModifyResponseDto();

		List<GoodsAllModifyVo> getGoodsList = goodsAllModifyService.getGoodsAllModifyList(paramDto);	//상품 내역

		goodsAllModifyResponseDto.setRows(getGoodsList);

		return ApiResult.success(goodsAllModifyResponseDto);
	}

	/**
     * @Desc 상품 일괄수정 대상 상품 수정
     *
     * @param GoodsAllModifyRequestDto
     * @return ApiResult
     */
	@Override
	public ApiResult<?> putGoodsAllModify(GoodsAllModifyRequestDto paramDto) {
		// TODO Auto-generated method stub

		if(paramDto.getGoodsSelectType().equals("promotionAdd")) {
			// 프로모션 상품명 수정
			goodsAllModifyService.putPromotionInfoModify(paramDto);
		}else if(paramDto.getGoodsSelectType().equals("displayCategoryAdd")) {
			// 전시 카테고리 수정
			goodsAllModifyService.putDispCategoryInfoBulkModify(paramDto);
		}else if(paramDto.getGoodsSelectType().equals("purchaseAdd")) {
			// 구매허용범위/쿠폰사용여부 수정
			goodsAllModifyService.putPurchasModify(paramDto);
		}else if(paramDto.getGoodsSelectType().equals("goodsNoticeAdd")) {
			// 상품 공지 변경
			goodsAllModifyService.putNoticeGoodsModify(paramDto);
		}else if(paramDto.getGoodsSelectType().equals("goodsAdd")) {
			// 추가상품
			if(paramDto.getGoodsAddType().equals("add")) {
				goodsAllModifyService.putGoodsAddModify(paramDto);
			}else if(paramDto.getGoodsAddType().equals("del")) {
				goodsAllModifyService.delGoodsAddModify(paramDto);
			}

		}

		return ApiResult.success();

	}

	/**
     * @Desc 상품 일괄수정 추가 상품 조회
     *
     * @param GoodsAllModifyRequestDto
     * @return ApiResult
     */
	@Override
	public ApiResult<?> getGoodsAdditionList(GoodsAllModifyRequestDto paramDto) {
		// TODO Auto-generated method stub
		GoodsAllModifyResponseDto goodsAllModifyResponseDto = new GoodsAllModifyResponseDto();

		List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList = goodsAllModifyService.goodsAdditionalGoodsMappingList(paramDto);

		goodsAllModifyResponseDto.setGoodsAdditionalGoodsMappingList(goodsAdditionalGoodsMappingList);

		return ApiResult.success(goodsAllModifyResponseDto);
	}

	/**
     * @Desc 상품 일괄수정 상품 공지 조회
     *
     * @param GoodsAllModifyRequestDto
     * @return ApiResult
     */
	@Override
	public ApiResult<?> getGoodsNoticeInfoList(GoodsAllModifyRequestDto paramDto) {
		// TODO Auto-generated method stub

		GoodsAllModifyResponseDto goodsAllModifyResponseDto = new GoodsAllModifyResponseDto();

		GoodsRegistVo ilGoodsDetail = goodsAllModifyService.getGoodsNoticeInfo(paramDto);	//상품 내역

		goodsAllModifyResponseDto.setGoodsNoticeInfo(ilGoodsDetail);

		return ApiResult.success(goodsAllModifyResponseDto);
	}

	/**
     * @Desc 상품 일괄수정 엑셀다운로드
     *
     * @param GoodsAllModifyRequestDto
     * @return ExcelDownloadDto
     */
	@Override
	public ExcelDownloadDto createGoodsAllModifyExcel(GoodsAllModifyRequestDto paramDto) {
		// TODO Auto-generated method stub
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        ArrayList<String> searchGoodsIdArray = new ArrayList<String>();

        searchGoodsIdArray = StringUtil.getArrayListComma(paramDto.getSearchGoodsId());
        paramDto.setIlGoodsIds(searchGoodsIdArray);

		String excelFileName = "상품정보 일괄수정 양식"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				180, 160, 120, 240, 120,
				100, 100, 100, 100, 100,
				140, 200, 200, 200, 120,
				120, 120 };

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center",
				"center", "center" };

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */
		String[] propertyListOfFirstWorksheet = { //
				"ilItemCd", "itemBarcode", "ilGoodsId", "promotionNm", "supplierName",
				"goodsNm", "saleStatusName", "dispYn", "recommendedPrice", "discountPriceRatio",
				"salePrice", "categoryDepthName", "warehouseNm", "displayTarget",
				"couponUseYn", "goodsAddInfo" };

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"품목코드", "품목바코드", "상품코드", "프로모션명", "공급업체",
				"상품명", "판매상태", "전시상태", "정상가", "할인율",
				"판매가", "전시 카테고리", "출고처명", "판매허용범위",
				"쿠폰사용", "추가상품" };

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
		List<GoodsAllModifyVo> goodsPriceList = goodsAllModifyService.getGoodsAllModifyListExcel(paramDto);

		firstWorkSheetDto.setExcelDataList(goodsPriceList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}
}
