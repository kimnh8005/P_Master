package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.goods;

import kr.co.pulmuone.v1.batch.goods.goods.dto.GoodsDetailImageBatchRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDetailImageBatchMapper {

    List<Long> getNoBatchGoodsDetailImage();

    void updateGoodsDetailImageGenInfo(GoodsDetailImageBatchRequestDto goodsDetailImageBatchRequestDto);
}
