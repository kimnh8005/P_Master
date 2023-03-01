package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemChangeLogListResponseDto;

public interface ItemChangeLogListBiz {
	ApiResult<?> getItemChangeLogList(ItemChangeLogListRequestDto itemChangeLogListRequestDto);

	ExcelDownloadDto getItemChangeLogListExcel(ItemChangeLogListRequestDto itemChangeLogListRequestDto);


	/**
	 * 업데이트 상세 내역
	 * @param goodsChangeLogListRequestDto
	 * @return
	 * @throws Exception
	 */
	ItemChangeLogListResponseDto getItemChangeLogPopup (ItemChangeLogListRequestDto itemChangeLogListRequestDto) throws Exception;
}
