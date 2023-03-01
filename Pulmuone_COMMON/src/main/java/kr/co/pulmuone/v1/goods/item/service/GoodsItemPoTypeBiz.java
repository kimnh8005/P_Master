package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeRequestDto;

public interface GoodsItemPoTypeBiz {

	ApiResult<?> getItemPoTypeList(ItemPoTypeListRequestDto itemPoTypeListRequestDto);

	ApiResult<?> getItemPoType(String ilPoTpId);

	ApiResult<?> getItemPoDay(String ilPoTpId, String eventStartDtNumber);

	ApiResult<?> addItemPoType(ItemPoTypeRequestDto itemPoTypeRequestDto) throws Exception;

	ApiResult<?> putItemPoType(ItemPoTypeRequestDto itemPoTypeRequestDto) throws Exception;

	ApiResult<?> delItemPoType(String ilPoTpId) throws Exception;

}
