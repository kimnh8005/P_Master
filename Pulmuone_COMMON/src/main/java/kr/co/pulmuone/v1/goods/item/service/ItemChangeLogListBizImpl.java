package kr.co.pulmuone.v1.goods.item.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemChangeLogListVo;
import kr.co.pulmuone.v1.policy.excel.service.PolicyExcelTmpltBiz;


/**
* <PRE>
* Forbiz Korea
* 마스터 품목리스트 BizImpl
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
public class ItemChangeLogListBizImpl  implements ItemChangeLogListBiz {

	@Autowired
	ItemChangeLogListService itemChangeLogListService;

	@Autowired
	private PolicyExcelTmpltBiz policyExcelTmpltBiz;

	/**
	 * @Desc 마스터 품목 업데이트 내역 조회
	 * @param goodsListRequestDto
	 * @return ApiResult
	 */
	@Override
	public ApiResult<?> getItemChangeLogList(ItemChangeLogListRequestDto itemChangeLogListRequestDto){
		ItemChangeLogListResponseDto itemChangeLogListResponseDto = new ItemChangeLogListResponseDto();

		ArrayList<String> ilItemCdArray = new ArrayList<String>();
		String codeStrFlag = "Y";
		if (!StringUtil.isEmpty(itemChangeLogListRequestDto.getIlItemCode())) {

			// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
			String ilItemCodeListStr = itemChangeLogListRequestDto.getIlItemCode().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

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

		itemChangeLogListRequestDto.setIlItemCodeArray(ilItemCdArray); // 검색어
		itemChangeLogListRequestDto.setIlItemCodeStrFlag(codeStrFlag);

		Page<ItemChangeLogListVo> goodsList = itemChangeLogListService.getItemChangeLogList(itemChangeLogListRequestDto);

		itemChangeLogListResponseDto.setTotal(goodsList.getTotal());
		itemChangeLogListResponseDto.setRows(goodsList.getResult());

		return ApiResult.success(itemChangeLogListResponseDto);
	}

	@Override
	public ExcelDownloadDto getItemChangeLogListExcel(ItemChangeLogListRequestDto itemChangeLogListRequestDto) {
		String excelFileName = "마스터 품목 업데이트 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		ArrayList<String> ilItemCdArray = new ArrayList<String>();
		String codeStrFlag = "Y";
		if (!StringUtil.isEmpty(itemChangeLogListRequestDto.getIlItemCode())) {

			// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
			String ilItemCodeListStr = itemChangeLogListRequestDto.getIlItemCode().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");

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

		itemChangeLogListRequestDto.setIlItemCodeArray(ilItemCdArray); // 검색어
		itemChangeLogListRequestDto.setIlItemCodeStrFlag(codeStrFlag);

		List<ItemChangeLogListVo> goodsList = itemChangeLogListService.getItemChangeLogListExcel(itemChangeLogListRequestDto);

		//엑셀다운로드 양식을 위한 공통
		ExcelWorkSheetDto firstWorkSheetDto = policyExcelTmpltBiz.getCommonDownloadExcelTmplt(itemChangeLogListRequestDto.getPsExcelTemplateId());
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

	public ItemChangeLogListResponseDto getItemChangeLogPopup (ItemChangeLogListRequestDto itemChangeLogListRequestDto) throws Exception {
		List<ItemChangeLogListVo> itemChangeLogPopup = itemChangeLogListService.getItemChangeLogPopup(itemChangeLogListRequestDto);

		ItemChangeLogListResponseDto itemChangeLogListResponseDto = new ItemChangeLogListResponseDto();

		itemChangeLogListResponseDto.setRows(itemChangeLogPopup);

		return itemChangeLogListResponseDto;
	}
}
