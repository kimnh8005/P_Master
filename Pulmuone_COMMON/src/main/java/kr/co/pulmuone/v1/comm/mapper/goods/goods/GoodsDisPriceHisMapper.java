package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsDisPriceHisRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsDisPriceHisVo;

@Mapper
public interface GoodsDisPriceHisMapper {

	/**
     * @Desc 상품 할인 업데이트 목록 조회
     * @param goodsRequestDto
     * @return Page<GoodsVo>
     */
    Page<GoodsDisPriceHisVo> getGoodsDisPriceHisList(GoodsDisPriceHisRequestDto paramDto);

}
