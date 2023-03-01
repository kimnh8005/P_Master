package kr.co.pulmuone.v1.goods.goods.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogListVo;
import kr.co.pulmuone.v1.goods.price.dto.GoodsDiscountResponseDto;

public interface GoodsChangeLogListBiz {
	ApiResult<?> getGoodsChangeLogList(GoodsChangeLogListRequestDto goodsChangeLogListRequestDto);

	ExcelDownloadDto getGoodsChangeLogListExcel(GoodsChangeLogListRequestDto goodsChangeLogListRequestDto);


	/**
	 * 업데이트 상세 내역
	 * @param goodsChangeLogListRequestDto
	 * @return
	 * @throws Exception
	 */
	GoodsChangeLogListResponseDto getGoodsChangeLogPopup (GoodsChangeLogListRequestDto goodsChangeLogListRequestDto) throws Exception;
}
