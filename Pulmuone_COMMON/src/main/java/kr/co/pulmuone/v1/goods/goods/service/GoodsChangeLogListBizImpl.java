package kr.co.pulmuone.v1.goods.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogListVo;
import kr.co.pulmuone.v1.goods.price.dto.GoodsDiscountResponseDto;
import kr.co.pulmuone.v1.policy.excel.service.PolicyExcelTmpltBiz;
import kr.co.pulmuone.v1.comm.constants.Constants;


/**
* <PRE>
* Forbiz Korea
* 상품리스트 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자	  :  작성내역
* -----------------------------------------------------------------------
*  1.0	2021. 04. 21.				임상건		  최초작성
* =======================================================================
* </PRE>
*/
@Service
public class GoodsChangeLogListBizImpl  implements GoodsChangeLogListBiz {

	@Autowired
	GoodsChangeLogListService goodsChangeLogListService;

	@Autowired
	private PolicyExcelTmpltBiz policyExcelTmpltBiz;

	/**
	 * @Desc 상품 업데이트 내역 조회
	 * @param goodsListRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getGoodsChangeLogList(GoodsChangeLogListRequestDto goodsChangeLogListRequestDto){
		GoodsChangeLogListResponseDto goodsChangeLogListResponseDto = new GoodsChangeLogListResponseDto();
		ArrayList<String> ilItemCdArray = new ArrayList<String>();
		String codeStrFlag = "Y";
		if (!StringUtil.isEmpty(goodsChangeLogListRequestDto.getFindKeyword())) {

			// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
			String ilItemCodeListStr = goodsChangeLogListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

			String regExp = "^[0-9]+$";
			String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
			for(int i = 0; i < ilItemCodeListArray.length; i++) {
				String ilItemCodeSearchVal = ilItemCodeListArray[i];
				if(ilItemCodeSearchVal.isEmpty()) {
					continue;
				}
				ilItemCdArray.add(ilItemCodeSearchVal);
			}
		}

		goodsChangeLogListRequestDto.setFindKeywordList(ilItemCdArray); // 검색어
		goodsChangeLogListRequestDto.setFindKeywordStrFlag(codeStrFlag);
		//goodsListRequestDto.setGoodsTypeList(goodsChangeLogListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형

		Page<GoodsChangeLogListVo> goodsList = goodsChangeLogListService.getGoodsChangeLogList(goodsChangeLogListRequestDto);

		goodsChangeLogListResponseDto.setTotal(goodsList.getTotal());
		goodsChangeLogListResponseDto.setRows(goodsList.getResult());

		return ApiResult.success(goodsChangeLogListResponseDto);
	}

	@Override
	public ExcelDownloadDto getGoodsChangeLogListExcel(GoodsChangeLogListRequestDto goodsChangeLogListRequestDto) {
		String excelFileName = "상품 업데이트 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		ArrayList<String> ilItemCdArray = new ArrayList<String>();
		String codeStrFlag = "Y";

		if (!StringUtil.isEmpty(goodsChangeLogListRequestDto.getFindKeyword())) {
			// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
			String ilItemCodeListStr = goodsChangeLogListRequestDto.getFindKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

			String regExp = "^[0-9]+$";
			String[] ilItemCodeListArray = ilItemCodeListStr.split(",+");
			for(int i = 0; i < ilItemCodeListArray.length; i++) {
				String ilItemCodeSearchVal = ilItemCodeListArray[i];
				if(ilItemCodeSearchVal.isEmpty()) {
					continue;
				}
				ilItemCdArray.add(ilItemCodeSearchVal);
			}
		}

		goodsChangeLogListRequestDto.setFindKeywordList(ilItemCdArray); // 검색어
		goodsChangeLogListRequestDto.setFindKeywordStrFlag(codeStrFlag);
		//goodsListRequestDto.setGoodsTypeList(goodsChangeLogListService.getSearchKeyToSearchKeyList(goodsListRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS)); // 상품유형

		List<GoodsChangeLogListVo> goodsList = goodsChangeLogListService.getGoodsChangeLogListExcel(goodsChangeLogListRequestDto);

		//엑셀다운로드 양식을 위한 공통
		ExcelWorkSheetDto firstWorkSheetDto = policyExcelTmpltBiz.getCommonDownloadExcelTmplt(goodsChangeLogListRequestDto.getPsExcelTemplateId());
		firstWorkSheetDto.setExcelDataList(goodsList);

		if (firstWorkSheetDto.getExcelFileName() != null && firstWorkSheetDto.getExcelFileName() != "") {
			excelFileName = firstWorkSheetDto.getExcelFileName();
		}

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();
		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	public GoodsChangeLogListResponseDto getGoodsChangeLogPopup (GoodsChangeLogListRequestDto goodsChangeLogListRequestDto) throws Exception {
		List<GoodsChangeLogListVo> goodsChangeLogPopup = goodsChangeLogListService.getGoodsChangeLogPopup(goodsChangeLogListRequestDto);

		GoodsChangeLogListResponseDto goodsChangeLogListResponseDto = new GoodsChangeLogListResponseDto();

		goodsChangeLogListResponseDto.setRows(goodsChangeLogPopup);

		return goodsChangeLogListResponseDto;
	}
}
