package kr.co.pulmuone.v1.goods.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExprRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExprResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExprResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * 유통기한별 재고 연동 내역 관리 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.12.08  이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsStockExprBizImpl implements GoodsStockExprBiz {

	@Autowired
    GoodsStockExprService goodsStockExprService;

	/**
	 * 유통기한별 재고 연동 내역 관리 조회
	 * @param	StockExprRequestDto
	 * @return	StockExprResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockExprList(StockExprRequestDto dto) throws Exception {
		StockExprResponseDto stockExprResponseDto = new StockExprResponseDto();

		Page<StockExprResultVo> stockExprList = goodsStockExprService.getStockExprList(dto);

		stockExprResponseDto.setTotal(stockExprList.getTotal());
		stockExprResponseDto.setRows(stockExprList.getResult());

        return ApiResult.success(stockExprResponseDto);
	}

	/**
	 * 통합ERP 재고 연동 내역 관리 조회
	 * @param	StockExprRequestDto
	 * @return	StockExprResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockErpList(StockExprRequestDto dto) throws Exception {
		StockExprResponseDto stockExprResponseDto = new StockExprResponseDto();

		Page<StockExprResultVo> stockExprList = goodsStockExprService.getStockErpList(dto);

		stockExprResponseDto.setTotal(stockExprList.getTotal());
		stockExprResponseDto.setRows(stockExprList.getResult());

        return ApiResult.success(stockExprResponseDto);
	}

	/**
	 * 재고 미연동 품목리스트
	 * @param	StockExprRequestDto
	 * @return	StockExprResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockNonErpList(StockExprRequestDto dto) throws Exception {
		StockExprResponseDto stockExprResponseDto = new StockExprResponseDto();

		Page<StockExprResultVo> stockExprList = goodsStockExprService.getStockNonErpList(dto);

		stockExprResponseDto.setTotal(stockExprList.getTotal());
		stockExprResponseDto.setRows(stockExprList.getResult());

        return ApiResult.success(stockExprResponseDto);
	}

	/**
	 * 재고 미연동 품목리스트 - 재고수정
	 * @param	StockExprRequestDto
	 * @return	StockExprResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putStockNonErp(StockExprRequestDto dto) throws Exception {

		goodsStockExprService.putStockNonErp(dto);

        return ApiResult.success();
	}

	/**
	 * 유통기한별 재고 연동 내역 관리 엑셀 다운로드
	 * @param	StockExprRequestDto
	 * @return	List<StockExprResultVo>
	 */
	@Override
	public ExcelDownloadDto getStockExprExportExcel(StockExprRequestDto dto) {
		return goodsStockExprService.getStockExprExportExcel(dto);
	}

	/**
	 * 통합ERP 재고 연동 내역 관리 엑셀 다운로드
	 * @param	StockExprRequestDto
	 * @return	List<StockExprResultVo>
	 */
	@Override
	public ExcelDownloadDto getStockErpExportExcel(StockExprRequestDto dto) {
		return goodsStockExprService.getStockErpExportExcel(dto);
	}

	/**
	 * 재고 미연동 품목리스트 엑셀 다운로드
	 * @param	StockExprRequestDto
	 * @return	List<StockExprResultVo>
	 */
	@Override
	public ExcelDownloadDto getStockNonErpExportExcel(StockExprRequestDto dto) {
	     return  goodsStockExprService.getStockNonErpExportExcel(dto);
	}

}
