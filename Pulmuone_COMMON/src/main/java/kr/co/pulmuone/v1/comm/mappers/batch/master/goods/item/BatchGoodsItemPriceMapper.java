package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item;

import java.util.List;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceApprovalRequestDto;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigApproveVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceIfTempVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceListVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;

@Mapper
public interface BatchGoodsItemPriceMapper {

	public List<ItemPriceListVo> getItemPriceInfo(@Param("ilItemCd")String ilItemCd);

	int addItemPriceOrig(ItemPriceOrigVo itemPriceOrigVo);

	ItemPriceOrigVo getItemPriceOrigLastly(String ilItemCode);

	ItemPriceOrigVo getItemPriceOrigLastlyByErpProductType(@Param("ilItemCode") String ilItemCode, @Param("erpProductType") String erpProductType);

	int putItemPriceOrig(ItemPriceOrigVo itemPriceOrigVo);

	int updateItemPriceOrigOfFutureByErp(ItemPriceOrigVo itemPriceOrigVo);

	int addItemPriceIfTemp(ItemPriceIfTempVo priceTempVo);

	ItemPriceOrigVo getItemPriceOrigInfo(ItemPriceOrigVo itemPriceOrigVo);

	void spGoodsPriceUpdateWhenItemPriceChanges(@Param("ilItemCode") String ilItemCode, @Param("inDebugFlag") boolean inDebugFlag);

	void spPackageGoodsPriceUpdateWhenItemPriceChanges(@Param("inDebugFlag") boolean inDebugFlag);

	int getGoodsCountByItemCode(@Param("ilItemCode") String ilItemCode);

	MasterItemVo getItemInfo(@Param("ilItemCode") String ilItemCode);

	List<ItemPriceIfTempVo> getItemPriceIfTempList();

	void delItemPriceIfTemp();

	List<ItemPriceOrigApproveVo> getItemPriceOrigApprove(@Param("startDate") String startDate);

	void putItemPriceOrigApprove(@Param("list") List<ItemPriceOrigApproveVo> list);

	void setItemPriceAppr(ItemPriceApprovalRequestDto itemPriceApprovalRequestDto);

	void setItemPriceApprStatusHistory(ApprovalStatusVo approvalStatusVo);

}
