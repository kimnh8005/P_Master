package kr.co.pulmuone.v1.comm.mapper.goods.item;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecificsValueVo;

@Mapper
public interface GoodsItemSpecificsValueMapper {

    /**
     * @Desc 품목별 상품정보제공고시 값 삭제
     * @param itemSpecificsValueVo
     * @return int
     */
    int delItemSpecificsValue(ItemSpecificsValueVo itemSpecificsValueVo);
}
