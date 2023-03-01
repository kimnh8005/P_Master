package kr.co.pulmuone.v1.goods.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockExcelUploadListMapper;
import kr.co.pulmuone.v1.goods.stock.dto.StockUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockUploadListResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsStockExcelUploadListService {

	@Autowired
	private final GoodsStockExcelUploadListMapper goodsStockExcelUploadListMapper;

	/**
	 * ERP 재고 엑셀 업로드 내역 조회
	 * @param	StockUploadListRequestDto
	 * @return	StockUploadListResponseDto
	 * @throws Exception
	 */
    @UserMaskingRun
	protected Page<StockUploadListResultVo> getStockUploadList(StockUploadListRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        return goodsStockExcelUploadListMapper.getStockUploadList(dto);
	}

	/**
	 * ERP 재고 엑셀 업로드 내역 엑셀용 데이타 조회
	 * @return List<GoodsItemNutritionVo>
	 * @throws Exception
	 */
	 protected List<StockUploadListResultVo> getStockUploadListExcelDownload(StockUploadListRequestDto dto) {
		 return goodsStockExcelUploadListMapper.getStockUploadListExcelDownload(dto);
	 }

}
