package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;

@Mapper
public interface GoodsShippingTemplateMapper {

	ShippingDataResultVo getShippingInfo(@Param("ilGoodsId") Long ilGoodsId, @Param("urWareHouseId") Long urWareHouseId)
			throws Exception;

	ShippingDataResultVo getShippingInfoByShippingTmplId(Long ilShippingTmplId) throws Exception;

	ShippingAreaVo getShippingArea(String zipCode) throws Exception;

	ShippingAreaVo getAdditionalShippingAmountArea(@Param("zipCode") String zipCode) throws Exception;
}
