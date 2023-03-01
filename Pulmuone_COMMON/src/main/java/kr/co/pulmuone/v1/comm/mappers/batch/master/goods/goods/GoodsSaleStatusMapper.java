package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.goods;

import java.util.List;

import kr.co.pulmuone.v1.batch.goods.goods.dto.GoodsSaleStatusBatchRequestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsSaleStatusMapper {

    List<Long> getGoodsIdListByGoodsBatch(GoodsSaleStatusBatchRequestDto goodsSaleStatusBatchRequestDto);

    void updateGoodsSaleStatusByGoodsIdList(List<Long> goodsIdList);

    // 묶음 상품
    List<Long> getPackageGoodsIdListByGoodsBatch(List<Long> goodsIdList);

}

