package kr.co.pulmuone.v1.goods.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.stock.dto.StockUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockUploadListResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockUploadListResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 재고 엑셀 업로드 내역  BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.11.20   이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsStockExcelUploadListBizImpl implements GoodsStockExcelUploadListBiz {

	@Autowired
    GoodsStockExcelUploadListService goodsStockExcelUploadListService;

	/**
	 * ERP 재고 엑셀 업로드 내역 조회
	 * @param	StockUploadListRequestDto
	 * @return	StockUploadListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockUploadList(StockUploadListRequestDto dto) throws Exception {
		StockUploadListResponseDto stockUploadListResponseDto = new StockUploadListResponseDto();

		Page<StockUploadListResultVo> stockUploadList = goodsStockExcelUploadListService.getStockUploadList(dto);

		stockUploadListResponseDto.setTotal(stockUploadList.getTotal());
		stockUploadListResponseDto.setRows(stockUploadList.getResult());

        return ApiResult.success(stockUploadListResponseDto);

	}

	/**
	 * ERP 재고 엑셀 업로드 내역 엑셀용 데이타 조회
	 * @return List<StockUploadListResultVo>
	 * @throws Exception
	 */

	  @Override public List<StockUploadListResultVo> getStockUploadListExcelDownload(StockUploadListRequestDto stockUploadListRequestDto) throws Exception {

	      return goodsStockExcelUploadListService.getStockUploadListExcelDownload(stockUploadListRequestDto); // rows

	  }

}
