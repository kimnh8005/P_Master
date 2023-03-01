package kr.co.pulmuone.v1.goods.item.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemChangeLogListVo;

class itemChangeLogListServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	ItemChangeLogListService itemChangeLogListService;

	@Test
	void 마스터품목_업데이트내역() {
		ItemChangeLogListRequestDto itemChangeLogListRequestDto = new ItemChangeLogListRequestDto();

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

		assertTrue(itemChangeLogListResponseDto.getTotal() > 0);
	}

	@Test
	void 마스터품목_업데이트상세내역() {
		ItemChangeLogListRequestDto itemChangeLogListRequestDto = new ItemChangeLogListRequestDto();

		itemChangeLogListRequestDto.setIlItemCode("0932840");
		itemChangeLogListRequestDto.setCreateDate("2021-04-22 19:13:09");

		List<ItemChangeLogListVo> itemChangeLogPopup = itemChangeLogListService.getItemChangeLogPopup(itemChangeLogListRequestDto);

		ItemChangeLogListResponseDto itemChangeLogListResponseDto = new ItemChangeLogListResponseDto();

		itemChangeLogListResponseDto.setRows(itemChangeLogPopup);

		assertTrue(itemChangeLogListResponseDto.getRows().size() > 0);
	}
}
