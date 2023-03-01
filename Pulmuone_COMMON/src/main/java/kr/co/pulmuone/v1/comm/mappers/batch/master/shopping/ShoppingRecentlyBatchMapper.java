package kr.co.pulmuone.v1.comm.mappers.batch.master.shopping;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingRecentlyBatchMapper {

    List<Long> getNonMemberRecently(@Param("targetDay") int targetDay);

    List<Long> getDoNotSearchGoodsId();

    void delRecentlyByIdList(@Param("spRecentlyViewIdList") List<Long> spRecentlyViewIdList);

    void delRecentlyByGoodsIdList(@Param("goodsIdList") List<Long> goodsIdList);

}
