package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistCategoryRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistCategoryVo;


@Mapper
public interface GoodsRegistCategoryMapper {
	List<GoodsRegistCategoryVo> getDisplayCategoryList(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto);

	List<GoodsRegistCategoryVo> recentCategory1DepthIdList(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto);

	List<GoodsRegistCategoryVo> ilGoodsDisplayCategoryList(GoodsRegistRequestDto goodsRegistRequestDto);
}
