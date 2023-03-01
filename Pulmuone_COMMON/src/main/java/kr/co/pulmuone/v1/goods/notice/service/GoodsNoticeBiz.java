package kr.co.pulmuone.v1.goods.notice.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeDto;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeResponseDto;

import java.util.List;

public interface GoodsNoticeBiz {

	ApiResult<?> getGoodsNoticeInfo(String ilNoticeId);

	ApiResult<?> getGoodsNoticeList(GoodsNoticeDto dto);

	ApiResult<?> addGoodsNotice(GoodsNoticeDto dto);

	ApiResult<?> putGoodsNotice(GoodsNoticeDto dto);

	List<GoodsNoticeResponseDto> getGoodsNoticeListByUser(String warehouseGroupCode, Long urWarehouseId);

}
