package kr.co.pulmuone.v1.goods.stock.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockExcelUploadDetlListMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadDetlListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadDetlListResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsStockExcelUploadDetlListService {

	@Autowired
	private final GoodsStockExcelUploadDetlListMapper goodsStockExcelUploadDetlListMapper;

	/**
	 * ERP 재고 엑셀 업로드 상세내역 조회
	 * @param	StockExcelUploadDetlListRequestDto
	 * @return	StockExcelUploadDetlListRequestDto
	 * @throws Exception
	 */
	protected Page<StockExcelUploadDetlListResultVo> getStockUploadDetlList(StockExcelUploadDetlListRequestDto dto) {

		ArrayList<String> ilItemCdArray = null;

		if(dto.getIlStockExcelUploadLogId() != null || !dto.getIlStockExcelUploadLogId().equals("0")) {//업로드ID 상세보기를 위해서 사용, 0이면 일반조회 아니면 상세조회

			if(!StringUtil.isEmpty(dto.getItemCodes()) && dto.getSelectConditionType().equals("codeSearch")) {
				// 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
				String ilItemCodeListStr = dto.getItemCodes().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
				ilItemCdArray = StringUtil.getArrayListComma(ilItemCodeListStr);
			}

			// 품목코드 or 품목바코드로 조회시에는 ERP 연동여부 외 다른 검색 조건 무시
	        if (ilItemCdArray != null && !ilItemCdArray.isEmpty()) {

	        	StockExcelUploadDetlListRequestDto stockExcelUplaodList = new StockExcelUploadDetlListRequestDto();

	            stockExcelUplaodList.setItemCodes(dto.getItemCodes());
	            stockExcelUplaodList.setSelectConditionType(dto.getSelectConditionType());
	            stockExcelUplaodList.setIlItemCodeArray(ilItemCdArray);
	            stockExcelUplaodList.setIlStockExcelUploadLogId(dto.getIlStockExcelUploadLogId());
	            stockExcelUplaodList.setPage(dto.getPage());
	            stockExcelUplaodList.setPageSize(dto.getPageSize());

	            PageMethod.startPage(stockExcelUplaodList.getPage(), stockExcelUplaodList.getPageSize());

	            return goodsStockExcelUploadDetlListMapper.getStockUploadDetlList(stockExcelUplaodList);
	        }

			   PageMethod.startPage(dto.getPage(), dto.getPageSize());

		}else {
			PageMethod.startPage(dto.getPage(), dto.getPageSize());
		}

        return goodsStockExcelUploadDetlListMapper.getStockUploadDetlList(dto);
	}

}
