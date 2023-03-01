package kr.co.pulmuone.v1.batch.goods.goods;

import kr.co.pulmuone.v1.batch.goods.goods.dto.GoodsSaleStatusBatchRequestDto;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsRegistMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.goods.GoodsSaleStatusMapper;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsSaleStatusService {

    @Autowired
    private final GoodsSaleStatusMapper goodsSaleStatusMapper;

    @Autowired
    private final GoodsRegistMapper goodsRegistMapper;

    /**
     * 영구판매중지 전환 대상 상품 select
     * @param goodsSaleStatusBatchRequestDto
     * @return IL_GOODS_ID List
     */
    protected List<Long> getGoodsIdListByGoodsBatch(GoodsSaleStatusBatchRequestDto goodsSaleStatusBatchRequestDto) {
        return goodsSaleStatusMapper.getGoodsIdListByGoodsBatch(goodsSaleStatusBatchRequestDto);
    }

    /**
     * 영구판매중지 전환 대상 상품 update
     * @param goodsIdList
     */
    protected void updateGoodsSaleStatusByGoodsIds(List<Long> goodsIdList) {
        goodsSaleStatusMapper.updateGoodsSaleStatusByGoodsIdList(goodsIdList);
    }

    /**
     * 묶음 구성상품 중 영구판매중지 전환 대상 select
     * @param goodsIdList
     * @return IL_GOODS_ID List
     */
    protected List<Long> getPackageGoodsIdListByGoodsBatch(List<Long> goodsIdList) {
        return goodsSaleStatusMapper.getPackageGoodsIdListByGoodsBatch(goodsIdList);
    }


    /**
     * @Desc 상품 변경내역 저장
     * @param goodsChangeLogVo
     * @return int
     */
    protected int addGoodsChangeLog(GoodsChangeLogVo goodsChangeLogVo) {
        return goodsRegistMapper.addGoodsChangeLog(goodsChangeLogVo);
    }
}
