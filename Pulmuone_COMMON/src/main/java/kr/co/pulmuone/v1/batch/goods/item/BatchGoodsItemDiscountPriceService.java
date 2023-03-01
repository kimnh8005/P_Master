package kr.co.pulmuone.v1.batch.goods.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPriceSearchResponseDto;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item.BatchGoodsItemDiscountPriceMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.discount.dto.vo.DiscountInfoVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceDiscountIfTempVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 *
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 03. 11    정형진         최초작성
* =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchGoodsItemDiscountPriceService {

	@Autowired
    private BatchGoodsItemDiscountPriceMapper batchGoodsItemDiscountPriceMapper;

	/**
	 * @Desc  행사발주요청 추가
	 * @param ItemPoTypeRequestDto
	 * @return ItemPoRequestVo
	 */
	protected int putGoodsDiscountWithItemDiscount(List<ItemPriceDiscountIfTempVo> priceDiscountIfTempList) {

		int successCount = 0;

		if(priceDiscountIfTempList.size() > 0) {


			for(ItemPriceDiscountIfTempVo priceDiscountIfTemp : priceDiscountIfTempList) {
				String startDate = StringUtil.nvl(priceDiscountIfTemp.getStartDt(),DateUtil.getDate(1, "yyyy-MM-dd"));

				ItemPriceOrigVo itemPriceOrigVo = new ItemPriceOrigVo();
				itemPriceOrigVo.setIlItemCode(priceDiscountIfTemp.getIlItemCd());
				itemPriceOrigVo.setStartDate(startDate);	//빈값 존재
				itemPriceOrigVo.setStandardPrice(String.valueOf(priceDiscountIfTemp.getStandardPrice() == null ? 0 : priceDiscountIfTemp.getStandardPrice()));
				itemPriceOrigVo.setRecommendedPrice(String.valueOf(priceDiscountIfTemp.getNormalRecommendedPrice() == null ? 0 : priceDiscountIfTemp.getNormalRecommendedPrice()));
				itemPriceOrigVo.setCreateId(0);	//배치용 ID 추후 확정 예정
				itemPriceOrigVo.setModifyId(0);	//배치용 ID 추후 확정 예정
				itemPriceOrigVo.setManagerUpdateYn("N");
				itemPriceOrigVo.setSystemUpdateYn("Y");		// 배치 insert 이므로 systemUpdate = Y
				successCount += batchGoodsItemDiscountPriceMapper.addItemPriceOrig(itemPriceOrigVo);

				//품목 할인 저장(IL_ITEM_DISCOUNT)
				DiscountInfoVo discountInfoVo = new DiscountInfoVo();
				discountInfoVo.setIlItemCode(priceDiscountIfTemp.getIlItemCd());
				discountInfoVo.setDiscountType(GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode());
				discountInfoVo.setDiscountStartDate(startDate);
				discountInfoVo.setDiscountEndDate(priceDiscountIfTemp.getEndDt());
				discountInfoVo.setDiscountMethodType(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
				discountInfoVo.setDiscountSalePrice(String.valueOf(priceDiscountIfTemp.getRecommendedPrice() == null ? priceDiscountIfTemp.getNormalRecommendedPrice() : priceDiscountIfTemp.getRecommendedPrice()));
				discountInfoVo.setCreateId(0);  //배치용 ID 추후 확정 예정
				batchGoodsItemDiscountPriceMapper.addItemDiscount(discountInfoVo);

				//품목에 해당 하는 상품을 조회한다 - 묶음 상품 제외
				List<ItemInfoVo> goodsIdList = batchGoodsItemDiscountPriceMapper.getGoodsIdListByItemCd(priceDiscountIfTemp.getIlItemCd());
				for(ItemInfoVo itemInfoVo : goodsIdList) {
					//해당 상품을 가격 정보를 변경한다

					DiscountInfoVo goodsDiscountVo = new DiscountInfoVo();
					goodsDiscountVo.setIlGoodsId(itemInfoVo.getGoodsId());
					goodsDiscountVo.setDiscountType(GoodsEnums.GoodsDiscountType.ERP_EVENT.getCode());
					goodsDiscountVo.setDiscountStartDate(startDate);
					goodsDiscountVo.setDiscountEndDate(priceDiscountIfTemp.getEndDt());
					goodsDiscountVo.setDiscountMethodType(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode());
					goodsDiscountVo.setDiscountSalePrice(String.valueOf(priceDiscountIfTemp.getRecommendedPrice() == null ? priceDiscountIfTemp.getNormalRecommendedPrice() : priceDiscountIfTemp.getRecommendedPrice()));
					goodsDiscountVo.setCreateId(0);	//배치용 ID 확인 필요
					goodsDiscountVo.setModifyId(0);	//배치용 ID 확인 필요

					Long ilGoodsDiscountId = batchGoodsItemDiscountPriceMapper.checkDuplicateErpDiscount(goodsDiscountVo); // 상품번호, 할인시작일로 동일 할인이 존재하는지 확인
					if (ilGoodsDiscountId == null || ilGoodsDiscountId == 0) { // 없으면 insert
						batchGoodsItemDiscountPriceMapper.addGoodsDiscountByBatch(goodsDiscountVo);
					} else { // 있으면 update
						goodsDiscountVo.setIlGoodsDiscountId(ilGoodsDiscountId); // 검색된 IL_GOODS_DISCOUNT_ID 설정
						batchGoodsItemDiscountPriceMapper.putGoodsDiscountByBatch(goodsDiscountVo);
					}

					batchGoodsItemDiscountPriceMapper.putGoodsBatchChange(itemInfoVo.getGoodsId());

					this.spGoodsPriceUpdateWhenItemPriceChanges(itemInfoVo.getItemCode()); // 상품가격 업데이트 프로시저 호출

				}

			}

			this.spPackageGoodsPriceUpdateWhenItemPriceChanges(); // 묶음상품가격 업데이트 프로시저 호출
		}

		return successCount;
	}

	protected void spGoodsPriceUpdateWhenItemPriceChanges(String ilItemCode) {
		boolean isDebugFlag = false;
		batchGoodsItemDiscountPriceMapper.spGoodsPriceUpdateWhenItemPriceChanges(ilItemCode, isDebugFlag);
	}

	protected void spPackageGoodsPriceUpdateWhenItemPriceChanges() {
		boolean isDebugFlag = false;
		batchGoodsItemDiscountPriceMapper.spPackageGoodsPriceUpdateWhenItemPriceChanges(isDebugFlag);
	}

	protected int addItemPriceDiscountIfTemp(ItemPriceDiscountIfTempVo priceDiscountIfTempVo) {
		return batchGoodsItemDiscountPriceMapper.addItemPriceDiscountIfTemp(priceDiscountIfTempVo);
	}

	protected void delItemPriceDiscountIfTemp() {
		batchGoodsItemDiscountPriceMapper.delItemPriceDiscountIfTemp();
	}

	protected List<ItemPriceDiscountIfTempVo> getItemPriceDiscountIfTempList(){
		return batchGoodsItemDiscountPriceMapper.getItemPriceDiscountIfTempList();
	}

}
