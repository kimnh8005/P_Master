package kr.co.pulmuone.v1.batch.goods.item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceApprovalRequestDto;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigApproveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item.BatchGoodsItemPriceMapper;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceIfTempVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceListVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;
import lombok.RequiredArgsConstructor;

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
*  1.0    2021. 02.24    정형진         최초작성
* =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class BatchGoodsItemPriceService {

	@Autowired
    private BatchGoodsItemPriceMapper batchGoodsItemPriceMapper;

	/**
     * @Desc 품목정보 - 연동 , 백암물류 품목, 신규 데이터
     * @param
     * @return List<ItemErpStock3PlSearchResultVo>
     */
	protected List<ItemPriceListVo> getItemPriceInfo(String ilItemCd){
		return batchGoodsItemPriceMapper.getItemPriceInfo(ilItemCd);
	}

	protected int addItemPriceOrig(ItemPriceOrigVo itemPriceOrigVo) {
		return batchGoodsItemPriceMapper.addItemPriceOrig(itemPriceOrigVo);
	}

	protected ItemPriceOrigVo getItemPriceOrigInfo(ItemPriceOrigVo itemPriceOrigVo){
		return batchGoodsItemPriceMapper.getItemPriceOrigInfo(itemPriceOrigVo);
	}


	protected ItemPriceOrigVo getItemPriceOrigLastly(String ilItemCode){
		return batchGoodsItemPriceMapper.getItemPriceOrigLastly(ilItemCode);
	}

	protected ItemPriceOrigVo getItemPriceOrigLastlyByErpProductType(String ilItemCode, String erpProductType){
		return batchGoodsItemPriceMapper.getItemPriceOrigLastlyByErpProductType(ilItemCode, erpProductType);
	}

	protected int updateItemPriceOrigOfFutureByErp(ItemPriceOrigVo itemPriceOrigVo) {
		return batchGoodsItemPriceMapper.updateItemPriceOrigOfFutureByErp(itemPriceOrigVo);
	}

	protected int putItemPriceOrig(ItemPriceOrigVo itemPriceOrigVo) {
		return batchGoodsItemPriceMapper.putItemPriceOrig(itemPriceOrigVo);
	}

	protected int addItemPriceIfTemp(ItemPriceIfTempVo priceTempVo) {
		return batchGoodsItemPriceMapper.addItemPriceIfTemp(priceTempVo);
	}

	protected void spGoodsPriceUpdateWhenItemPriceChanges(String ilItemCode) {
		boolean isDebugFlag = false;
		batchGoodsItemPriceMapper.spGoodsPriceUpdateWhenItemPriceChanges(ilItemCode, isDebugFlag);
	}

	protected void spPackageGoodsPriceUpdateWhenItemPriceChanges() {
		boolean isDebugFlag = false;
		batchGoodsItemPriceMapper.spPackageGoodsPriceUpdateWhenItemPriceChanges(isDebugFlag);
	}

	protected int getGoodsCountByItemCode(String ilItemCode) {
		return batchGoodsItemPriceMapper.getGoodsCountByItemCode(ilItemCode);
	}

	protected MasterItemVo getItemInfo(String ilItemCode){
		return batchGoodsItemPriceMapper.getItemInfo(ilItemCode);
	}

	protected List<ItemPriceIfTempVo> getItemPriceIfTempList(){
		return batchGoodsItemPriceMapper.getItemPriceIfTempList();
	}

	protected void delItemPriceIfTemp() {
		batchGoodsItemPriceMapper.delItemPriceIfTemp();
	}

	protected void runItemPriceOrigCorrection() {
		// 대상 조회
		String startDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		List<ItemPriceOrigApproveVo> targetList = batchGoodsItemPriceMapper.getItemPriceOrigApprove(startDate);

		if(targetList == null || targetList.isEmpty()){
			return;
		}

		// 반영 대상 계산
		List<ItemPriceOrigApproveVo> putList = targetList.stream()
				.filter(vo -> !vo.getStandardPrice().equals(vo.getStandardPriceNow()))
				.collect(Collectors.toList());
		if(putList.isEmpty()){
			return;
		}

		// 반영
		batchGoodsItemPriceMapper.putItemPriceOrigApprove(putList);
	}


	protected void setItemPriceAppr(ItemPriceApprovalRequestDto itemPriceApprovalRequestDto) {
		batchGoodsItemPriceMapper.setItemPriceAppr(itemPriceApprovalRequestDto);
	}

	protected void setItemPriceApprStatusHistory(ApprovalStatusVo approvalStatusVo) {
		batchGoodsItemPriceMapper.setItemPriceApprStatusHistory(approvalStatusVo);
	}

}
