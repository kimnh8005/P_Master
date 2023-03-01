package kr.co.pulmuone.v1.comm.mapper.shopping.cart;

import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGiftDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetSaveShippingCostGoodsListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

	List<Long> getSaveShippingCostGoodsList(GetSaveShippingCostGoodsListRequestDto getSaveShippingCostGoodsListRequestDto) throws Exception ;

	int putCart(SpCartVo spCartVo) throws Exception;

	int delCart(@Param("spCartId") Long spCartId) throws Exception;

	int delCartAddGoodsBySpCartId(@Param("spCartId") Long spCartId) throws Exception;

	int delCartPickGoodsBySpCartId(@Param("spCartId") Long spCartId) throws Exception;

	int delCartAddGoods(@Param("spCartAddGoodsId") Long spCartAddGoodsId) throws Exception;

	int getCartTypeSummary(@Param("deliveryTypeList") List<String> deliveryTypeList,
						   @Param("spCartIds") List<Long> spCartIds) throws Exception;

	int addCart(SpCartVo spCartVo) throws Exception;

	int addCartAddGoods(SpCartAddGoodsVo SpCartAddGoodsVo) throws Exception;

	int addCartPickGoods(SpCartPickGoodsVo spCartPickGoodsVo) throws Exception;

	boolean ifCheckAddCartMerge(Long ilGoodsId) throws Exception;

	Long getCartIdByIlGoodsId(@Param("urPcidCd") String urPcidCd, @Param("urUserId") Long urUserId,
							  @Param("ilGoodsId") Long ilGoodsId, @Param("ilGoodsReserveOptionId") Long ilGoodsReserveOptionId, @Param("deliveryType") String deliveryType)
			throws Exception;

	int putCartPlusQty(@Param("spCartId") Long spCartId, @Param("qty") int qty) throws Exception;

	Long getCartAddGoodsIdByIlGoodsId(@Param("spCartId") Long spCartId, @Param("ilGoodsId") Long ilGoodsId)
			throws Exception;

	int putCartAddGoodsPlusQty(@Param("spCartAddGoodsId") Long spCartAddGoodsId, @Param("qty") int qty)
			throws Exception;

	SpCartVo getCart(Long spCartId) throws Exception;

	List<Long> getCartIdList(GetCartDataRequestDto reqDto) throws Exception;

	CartDeliveryTypeGroupByVo[] getCartDeliveryTypeGroupByList(@Param("deliveryTypeList") List<String> deliveryTypeList,
															   @Param("spCartIds") List<Long> spCartIds, @Param("gift")List<Long> gift, @Param("giftSize") int giftSize) throws Exception;

	ShippingTemplateGroupByVo[] getShippingTemplateGroupByListByDeliveryType(@Param("deliveryType") String deliveryType,
																			 @Param("spCartIds") List<Long> spCartIds, @Param("bridgeYn") String bridgeYn, @Param("urUserId") Long urUserId, @Param("gift") List<Long> gift)
			throws Exception;

	List<SpCartVo> getGoodsListByShippingPolicy(
			@Param("deliveryType") String deliveryType,
			@Param("shippingTemplateData") ShippingTemplateGroupByVo shippingTemplateData,
			@Param("spCartIds") List<Long> spCartIds) throws Exception;

	List<SpCartAddGoodsVo> getCartAddGoodsIdList(Long spCartId) throws Exception;

	List<SpCartAddGoodsVo> getCartAddGoodsIdListByExhibit(Long spCartId) throws Exception;

	List<SpCartPickGoodsVo> getCartPickGoodsList(Long spCartId) throws Exception;

	List<SpCartVo> getGiftGoodsListByShippingPolicy(@Param("shippingTemplateData") ShippingTemplateGroupByVo shippingTemplateData, @Param("gift") List<CartGiftDto> gift)
			throws Exception;

	void putCartUrUserId(@Param("urPcidCd") String urPcidCd, @Param("urUserId") Long urUserId) throws Exception;

	List<SpCartVo> getMergeCartListByUrUserId(Long urUserId) throws Exception;

	List<Long> getSpCardIdsByIlGoodsId(AddCartInfoRequestDto reqDto) throws Exception;
}
