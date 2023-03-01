package kr.co.pulmuone.v1.comm.mapper.goods.itemprice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.goods.discount.dto.vo.DiscountInfoVo;

@Mapper
public interface ItemDiscountMapper {

	int addItemDiscount(DiscountInfoVo discountInfoVo);

}
