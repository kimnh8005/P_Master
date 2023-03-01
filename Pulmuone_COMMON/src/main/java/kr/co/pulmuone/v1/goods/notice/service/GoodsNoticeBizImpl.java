package kr.co.pulmuone.v1.goods.notice.service;

import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeDto;

import java.util.List;

@Service
public class GoodsNoticeBizImpl implements GoodsNoticeBiz {

    @Autowired
    private GoodsNoticeService goodsNoticeService;
    /**
     * 상품공통공지 조회
     *
     * @param ilNoticeId
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getGoodsNoticeInfo(String ilNoticeId) {
    	return ApiResult.success(goodsNoticeService.getGoodsNoticeInfo(ilNoticeId));
    }
    /**
     * 상품공통공지 목록 조회
     *
     * @param GoodsNoticeDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getGoodsNoticeList(GoodsNoticeDto dto) {
    	return ApiResult.success(goodsNoticeService.getGoodsNoticeList(dto));
    }
    /**
     * 상품공통공지 신규 등록
     *
     * @param GoodsNoticeDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> addGoodsNotice(GoodsNoticeDto dto) {
    	return ApiResult.result(goodsNoticeService.addGoodsNotice(dto));
    }
    /**
     * 상품공통공지 수정
     *
     * @param GoodsNoticeDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> putGoodsNotice(GoodsNoticeDto dto) {
    	return ApiResult.result(goodsNoticeService.putGoodsNotice(dto));
    }

    @Override
    public List<GoodsNoticeResponseDto> getGoodsNoticeListByUser(String warehouseGroupCode, Long urWarehouseId) {
        return goodsNoticeService.getGoodsNoticeListByUser(warehouseGroupCode, urWarehouseId);
    }
}


